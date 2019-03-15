package cn.feihutv.zhibofeihu.data.network.socket.xsocket.tcp.client;


import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import cn.feihutv.zhibofeihu.data.network.socket.xsocket.BaseXSocket;
import cn.feihutv.zhibofeihu.data.network.socket.xsocket.tcp.client.bean.TargetInfo;
import cn.feihutv.zhibofeihu.data.network.socket.xsocket.tcp.client.bean.TcpMsg;
import cn.feihutv.zhibofeihu.data.network.socket.xsocket.tcp.client.listener.TcpClientListener;
import cn.feihutv.zhibofeihu.data.network.socket.xsocket.tcp.client.manager.TcpClientManager;
import cn.feihutv.zhibofeihu.data.network.socket.xsocket.tcp.client.state.ClientState;
import cn.feihutv.zhibofeihu.data.network.socket.xsocket.utils.CharsetUtil;
import cn.feihutv.zhibofeihu.data.network.socket.xsocket.utils.ExceptionUtils;
import cn.feihutv.zhibofeihu.utils.AppLogger;

import static cn.feihutv.zhibofeihu.data.network.socket.xsocket.utils.CharsetUtil.dataToString;
import static cn.feihutv.zhibofeihu.data.network.socket.xsocket.utils.CharsetUtil.int2byteArray;
import static cn.feihutv.zhibofeihu.data.network.socket.xsocket.utils.CharsetUtil.stringToData;

/**
 * tcp客户端
 */
public class XTcpClient extends BaseXSocket {
    public static final String TAG = "XTcpClient";
    protected TargetInfo mTargetInfo;//目标ip和端口号
    protected Socket mSocket;
    protected volatile ClientState mClientState;
    protected TcpConnConfig mTcpConnConfig;
    protected ConnectionThread mConnectionThread;
    protected SendThread mSendThread;
    protected ReceiveThread mReceiveThread;
    protected Collection<TcpClientListener> mTcpClientListeners;
    private LinkedBlockingQueue<TcpMsg> msgQueue;

    private XTcpClient() {
        super();
    }

    /**
     * 创建tcp连接，需要提供服务器信息
     *
     * @param targetInfo
     * @return
     */
    public static XTcpClient getTcpClient(TargetInfo targetInfo) {
        return getTcpClient(targetInfo, null);
    }

    public static XTcpClient getTcpClient(TargetInfo targetInfo, TcpConnConfig tcpConnConfig) {
        XTcpClient XTcpClient = TcpClientManager.getTcpClient(targetInfo);
        if (XTcpClient == null) {
            XTcpClient = new XTcpClient();
            XTcpClient.init(targetInfo, tcpConnConfig);
            TcpClientManager.putTcpClient(XTcpClient);
        }
        return XTcpClient;
    }

    /**
     * 根据socket创建client端，目前仅用在socketServer接受client之后
     *
     * @param socket
     * @return
     */
    public static XTcpClient getTcpClient(Socket socket, TargetInfo targetInfo) {
        return getTcpClient(socket, targetInfo, null);
    }

    public static XTcpClient getTcpClient(Socket socket, TargetInfo targetInfo, TcpConnConfig connConfig) {
        if (!socket.isConnected()) {
            ExceptionUtils.throwException("socket is closeed");
        }
        XTcpClient xTcpClient = new XTcpClient();
        xTcpClient.init(targetInfo, connConfig);
        xTcpClient.mSocket = socket;
        xTcpClient.mClientState = ClientState.Connected;
        xTcpClient.onConnectSuccess();
        return xTcpClient;
    }


    private void init(TargetInfo targetInfo, TcpConnConfig connConfig) {
        this.mTargetInfo = targetInfo;
        mClientState = ClientState.Disconnected;
        if(mTcpClientListeners!=null){
            mTcpClientListeners.clear();
            mTcpClientListeners=null;
        }
        mTcpClientListeners = new CopyOnWriteArrayList<>();
        if (mTcpConnConfig == null && connConfig == null) {
            mTcpConnConfig = new TcpConnConfig.Builder().create();
        } else if (connConfig != null) {
            mTcpConnConfig = connConfig;
        }
    }

    public synchronized TcpMsg sendMsg(String message) {
        TcpMsg msg = new TcpMsg(message, mTargetInfo, TcpMsg.MsgType.Send);
        return sendMsg(msg);
    }

    public synchronized TcpMsg sendMsg(byte[] message) {
        int i = message.length;
        byte[] h = int2byteArray(i);
        message = CharsetUtil.appendByteArray(h, message);
        TcpMsg msg = new TcpMsg(message, mTargetInfo, TcpMsg.MsgType.Send);
        return sendMsg(msg);
    }

    public synchronized TcpMsg sendMsg(TcpMsg msg) {
        if (isDisconnected()) {
            AppLogger.e("socket", "发送消息 " + msg + "，当前没有tcp连接，先进行连接");
            if(getMsgQueue().contains(msg)){
                getMsgQueue().remove(msg);
            }
            return null;
//            connect();
        }
        boolean re = enqueueTcpMsg(msg);
        if (re) {
            return msg;
        }
        return null;
    }

    public synchronized boolean cancelMsg(TcpMsg msg) {
        return getSendThread().cancel(msg);
    }

    public synchronized boolean cancelMsg(int msgId) {
        return getSendThread().cancel(msgId);
    }

    public synchronized void connect() {
        if (!isDisconnected()) {
            AppLogger.d(TAG, "已经连接了或正在连接");
            return;
        }
        setClientState(ClientState.Disconnected);
        AppLogger.d(TAG, "start connect");
        try {
            getConnectionThread().start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public synchronized Socket getSocket() {
        if (mSocket == null || isDisconnected() || !mSocket.isConnected()) {
            mSocket = new Socket();
            try {
                mSocket.setSoTimeout((int) mTcpConnConfig.getReceiveTimeout());
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        return mSocket;
    }

    public synchronized void reconnect() {
        reconnect("重连", null);
    }

    public synchronized void close() {
        closeSocket();
        setClientState(ClientState.Disconnected);
        notifyDisconnected("退出登录", null);
        AppLogger.i("socket", "tcp closed");
    }

    protected synchronized void onErrorDisConnect(String msg, Exception e) {
        if (isDisconnected()) {
            return;
        }
    }

    protected synchronized void reconnect(String msg, Exception e) {
        closeSocket();
        setClientState(ClientState.Disconnected);
        notifyDisconnected(msg, e);
        AppLogger.d(TAG, "tcp closed");
        new InterruptThread().start();
    }

    private class InterruptThread extends Thread {
        public void run() {
            try {
                synchronized(this){
                    interruptConnectionThread();
                    wait(100);
                    interruptSendThread();
                    wait(100);
                    interruptReceiveThread();
                    wait(1000);
                    connect();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



    private synchronized boolean closeSocket() {
        if (mSocket != null) {
            try {
               mSocket.close();
                mSocket=null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    //连接已经连接，接下来的流程，创建发送和接受消息的线程
    private void onConnectSuccess() {
        AppLogger.d(TAG, "tcp connect 建立成功");
        if(!getClientState().equals(ClientState.Connected)){
            setClientState(ClientState.Connected);//标记为已连接
            getSendThread().start();
            getReceiveThread().start();
        }
        notifyConnected(true);
    }

    public void startSendThread(){
        //getSendThread().start();
    }

    /**
     * tcp连接线程
     */
    private class ConnectionThread extends Thread {
        @Override
        public void run() {
            try {
                int localPort = mTcpConnConfig.getLocalPort();
                if (localPort > 0) {
                    if (!getSocket().isBound()) {
                        getSocket().bind(new InetSocketAddress(localPort));
                    }
                }
                getSocket().connect(new InetSocketAddress(mTargetInfo.getIp(), mTargetInfo.getPort()),
                        (int) mTcpConnConfig.getConnTimeout());
                AppLogger.d(TAG, "创建连接成功,target=" + mTargetInfo + ",localport=" + localPort);
            } catch (Exception e) {
                AppLogger.d(TAG, "创建连接失败,target=" + mTargetInfo + "," + e);
                onErrorDisConnect("创建连接失败", e);
                notifyConnected(false);//连接失败
                return;
            }
            onConnectSuccess();
        }
    }

    public boolean enqueueTcpMsg(final TcpMsg tcpMsg) {
        if (tcpMsg == null || getMsgQueue().contains(tcpMsg)) {
            return false;
        }
        try {
            getMsgQueue().put(tcpMsg);
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected synchronized LinkedBlockingQueue<TcpMsg> getMsgQueue() {
        if (msgQueue == null) {
            msgQueue = new LinkedBlockingQueue<>();
        }
        return msgQueue;
    }

    private class  SendThread extends Thread {
        private TcpMsg sendingTcpMsg;

        protected SendThread setSendingTcpMsg(TcpMsg sendingTcpMsg) {
            this.sendingTcpMsg = sendingTcpMsg;
            return this;
        }

        public TcpMsg getSendingTcpMsg() {
            return this.sendingTcpMsg;
        }

        public boolean cancel(TcpMsg packet) {
            return getMsgQueue().remove(packet);
        }

        public boolean cancel(int tcpMsgID) {
            return getMsgQueue().remove(new TcpMsg(tcpMsgID));
        }

        @Override
        public void run() {
            TcpMsg msg;
            try {
                while (isConnected() && !Thread.interrupted() && (msg = getMsgQueue().take()) != null) {
                    setSendingTcpMsg(msg);//设置正在发送的
                    //AppLogger.d(TAG, "tcp sending msg=" + msg);
                    byte[] data = msg.getSourceDataBytes();
                    if (data == null) {//根据编码转换消息
                        data = stringToData(msg.getSourceDataString(), mTcpConnConfig.getCharsetName());
                    }
                    if (data != null && data.length > 0) {
                        try {
                            getSocket().getOutputStream().write(data);
                            getSocket().getOutputStream().flush();
                            msg.setTime();
                            notifySended(msg);//消息已发送
                        }catch (java.net.SocketException se){
                            se.printStackTrace();
                            AppLogger.e("socket","发送消息失败:SocketException");
                            onErrorDisConnect("发送消息失败", se);
                        }catch (IOException e) {
                            e.printStackTrace();
                            AppLogger.e("socket","发送消息失败");
                            onErrorDisConnect("发送消息失败", e);
                            return;
                        }
                    }

                    Thread.sleep(10);
                }
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
        }
    }

    private class ReceiveThread extends Thread {
        @Override
        public void run() {
            try {
                //已获得的数据缓存
                byte[] bytesCache = new byte[0];
                //输入流
                InputStream is = getSocket().getInputStream();

                while (isConnected() && !Thread.interrupted()) {

                    Thread.sleep(10);

                    //解析包头
                    while (bytesCache.length > 4) {
                        //获取包头中定义的长度信息
                        int len = this.getPkgLen(bytesCache);
                        //读取包总字节数
                        int readLen = 4 + len;
                        //剩下的字节数
                        int leftLen = bytesCache.length - readLen;
                        if (leftLen >= 0) {
                            //获取消息包体
                            byte[] msg = new byte[len];
                            System.arraycopy(bytesCache, 4, msg, 0, len);
                            this.receiveMsg(msg);
                            //剩下未处理的字节保留到下次处理
                            byte[] left = new byte[leftLen];
                            if (leftLen > 0) {
                                System.arraycopy(bytesCache, readLen, left, 0, leftLen);
                            }
                            bytesCache = left;
                        }else{
                            //继续读取字节
                            break;
                        }
                    }

                    byte[] result = mTcpConnConfig.getStickPackageHelper().execute(is);//粘包处理
                    if(result.length>0) {
                       bytesCache = CharsetUtil.appendByteArray(bytesCache, result);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                AppLogger.e(TAG, "tcp Receive  error  " + e);
                onErrorDisConnect("接受消息错误", e);
            }
        }

        public int getPkgLen(byte[] target){
            byte[] a =  new byte[4];
            System.arraycopy(target,0,a,0,4);
            int v0 = (a[0] & 0xff) << 24;//&0xff将byte值无差异转成int,避免Java自动类型提升后,会保留高位的符号位
            int v1 = (a[1] & 0xff) << 16;
            int v2 = (a[2] & 0xff) << 8;
            int v3 = (a[3] & 0xff) ;
            return v0 + v1 + v2 + v3;
        }

        public void receiveMsg(byte[] result){
            //AppLogger.d(TAG, "tcp Receive 解决粘包之后的数据 " + Arrays.toString(result));
            TcpMsg tcpMsg = new TcpMsg(result, mTargetInfo, TcpMsg.MsgType.Receive);
            tcpMsg.setTime();
            String msgstr = dataToString(result, mTcpConnConfig.getCharsetName());
            tcpMsg.setSourceDataString(msgstr);
            boolean va = mTcpConnConfig.getValidationHelper().execute(result);
            if (!va) {
                AppLogger.d(TAG, "tcp Receive 数据验证失败 ");
                notifyValidationFail(tcpMsg);//验证失败
                return;
            }
            byte[][] decodebytes = mTcpConnConfig.getDecodeHelper().execute(result, mTargetInfo, mTcpConnConfig);
            tcpMsg.setEndDecodeData(decodebytes);
            notifyReceive(tcpMsg);//notify listener
        }
    }

    protected synchronized ReceiveThread getReceiveThread() {
        if (mReceiveThread == null || !mReceiveThread.isAlive()) {
            mReceiveThread = new ReceiveThread();
        }
        return mReceiveThread;
    }

    protected synchronized void interruptReceiveThread() {
        if (mReceiveThread != null && mReceiveThread.isAlive()) {
            mReceiveThread.interrupt();
        }
    }

    protected synchronized SendThread getSendThread() {
//        if (mSendThread == null || !mSendThread.isAlive()) {
        //Thread already started 每次都new一个
            mSendThread = new SendThread();
//        }
        return mSendThread;
    }

    protected synchronized void interruptSendThread() {
        if (mSendThread != null && mSendThread.isAlive()) {
            mSendThread.interrupt();
        }
    }

    protected ConnectionThread getConnectionThread() {
        if (mConnectionThread == null || !mConnectionThread.isAlive() || mConnectionThread.isInterrupted()) {
            mConnectionThread = new ConnectionThread();
        }
        return mConnectionThread;
    }

    protected synchronized void interruptConnectionThread() {
        if (mConnectionThread != null && mConnectionThread.isAlive()) {
            mConnectionThread.interrupt();
        }
    }

    public synchronized ClientState getClientState() {
        return mClientState;
    }

    protected void setClientState(ClientState state) {
        if (mClientState != state) {
            mClientState = state;
        }
    }

    public boolean isDisconnected() {
        return getClientState() == ClientState.Disconnected;
    }

    public boolean isConnected() {
        return getClientState() == ClientState.Connected;
    }

    private void notifyConnected(final boolean isSucceed) {
        for (TcpClientListener clientListener : mTcpClientListeners) {
            clientListener.onConnected(isSucceed,XTcpClient.this);
        }
    }

    private void notifyDisconnected(final String msg, final Exception e) {
        for (TcpClientListener clientListener : mTcpClientListeners) {
            clientListener.onDisconnected(XTcpClient.this, msg, e);
        }
    }


    private void notifyReceive(final TcpMsg tcpMsg) {
        for (TcpClientListener clientListener : mTcpClientListeners) {
            clientListener.onReceive(XTcpClient.this, tcpMsg);
        }
    }


    private void notifySended(final TcpMsg tcpMsg) {
        for (TcpClientListener clientListener : mTcpClientListeners) {
            clientListener.onSended(XTcpClient.this, tcpMsg);
        }
    }

    private void notifyValidationFail(final TcpMsg tcpMsg) {
        for (TcpClientListener clientListener : mTcpClientListeners) {
            clientListener.onValidationFail(XTcpClient.this, tcpMsg);
        }
    }

    public TargetInfo getTargetInfo() {
        return mTargetInfo;
    }

    public void addTcpClientListener(TcpClientListener listener) {
        if (mTcpClientListeners.contains(listener)) {
            return;
        }
        mTcpClientListeners.add(listener);
    }



    public synchronized void removeTcpClientListener(TcpClientListener listener) {
        Iterator<TcpClientListener> sListIterator = mTcpClientListeners.iterator();
            while (sListIterator.hasNext()) {
                TcpClientListener tcpClientListener = sListIterator.next();
                if (tcpClientListener.equals(listener)) {
                    mTcpClientListeners.remove(tcpClientListener);
                }
            }
    }

    public void config(TcpConnConfig tcpConnConfig) {
        mTcpConnConfig = tcpConnConfig;
    }

    @Override
    public String toString() {
        return "XTcpClient{" +
                "mTargetInfo=" + mTargetInfo + ",state=" + mClientState + ",isconnect=" + isConnected() +
                '}';
    }
}
