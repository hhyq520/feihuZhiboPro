package cn.feihutv.zhibofeihu.data.network.socket.xsocket;


import java.util.ArrayList;
import java.util.List;

import cn.feihutv.zhibofeihu.data.network.socket.RxSocketUtil;
import cn.feihutv.zhibofeihu.data.network.socket.model.SocketBaseResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.SocketMessage;
import cn.feihutv.zhibofeihu.data.network.socket.model.common.SocketConnectError;
import cn.feihutv.zhibofeihu.data.network.socket.xsocket.tcp.client.TcpConnConfig;
import cn.feihutv.zhibofeihu.data.network.socket.xsocket.tcp.client.XTcpClient;
import cn.feihutv.zhibofeihu.data.network.socket.xsocket.tcp.client.bean.TargetInfo;
import cn.feihutv.zhibofeihu.data.network.socket.xsocket.tcp.client.bean.TcpMsg;
import cn.feihutv.zhibofeihu.data.network.socket.xsocket.tcp.client.listener.TcpClientListener;
import cn.feihutv.zhibofeihu.rxbus.RxBus;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.utils.AppLogger;

public class FHSocket implements TcpClientListener {
    private volatile static FHSocket instance;
    private XTcpClient xTcpClient;
    private boolean tickPaused = false;//心跳暂停
    public static List<String> SocketAddress = new ArrayList<>();
    public IFHCallBack onSocketConnected = null;
    /**
     * pinging说明：
     * 表示服务端还没有响应前端ping指令
     * 如服务端返回了pong字符串，表示已经响应了ping指令，则该值将设置为false
     * 在心跳发出指令之前会检查pinging的状态，如果为true表示上一次的ping指令服务端还没有返回，此时为网络异常
     */
    private volatile boolean pinging = false;
    public boolean isFirstConnected = true;

    private int socketHearBeatReqCount=0;
    private int socketHearBeatRespCount=0;//socket req连接心跳次数


    public XTcpClient getxTcpClient() {
        return xTcpClient;
    }


    public void addTcpClientListener(TcpClientListener tcpClientListener){
        getxTcpClient().addTcpClientListener(tcpClientListener);
    }


    public static FHSocket getInstance() {
        if (instance == null) {
            synchronized (FHSocket.class) {
                if (instance == null) {
                    instance = new FHSocket();
                }
            }
        }
        return instance;
    }

    public static FHSocket getInstance1() {
        return instance;
    }

    /**
     * 构造方法
     */
    private FHSocket() {
        this.initAndConnect();
    }

    private void initAndConnect() {

            String string = SocketAddress.get(0);
            String[] split = string.split(":");
            TargetInfo targetInfo = new TargetInfo(split[0], Integer.parseInt(split[1]));
            xTcpClient = XTcpClient.getTcpClient(targetInfo);
            xTcpClient.addTcpClientListener(this);
            xTcpClient.config(new TcpConnConfig.Builder()
                    .setIsReconnect(false)
                    .create());
            //xTcpClient.connect();

    }

    public void connect(IFHCallBack onSocketConnected) {
        if (xTcpClient != null) {
            this.onSocketConnected = onSocketConnected;
            tickPaused = false;
            xTcpClient.reconnect();
        }
    }



    public void reConnecting(){
        close();
         initAndConnect();
        if (xTcpClient != null) {
            tickPaused = false;
            xTcpClient.reconnect();
        }

    }


    public void close() {
        if (xTcpClient != null) {
            tickPaused = true;
            this.isFirstConnected = true;
            xTcpClient.close();
        }
    }

    public boolean isDisconnected() {
        if (xTcpClient == null)
            return true;
        return xTcpClient.isDisconnected();
    }



    public void runHearBeat() {
        socketHearBeatReqCount++;

        if (tickPaused){
            AppLogger.e("socket","socket --------心跳服务暂停 socket 正在重连。。。!");
            return;
        }
        int absCount=Math.abs(socketHearBeatReqCount-socketHearBeatRespCount);

        if(absCount>2){
            AppLogger.e("socket","socket -------- 心跳异常 网络异常,或服务器故障,需重连!.   req="
                    +socketHearBeatReqCount+"  resp="+socketHearBeatRespCount);
            //3次心跳没回应则丢包 异常
            RxBus.get().send(RxBusCode.RX_BUS_CODE_SOCKET_LOST_CONNECTION_ERROR,new SocketConnectError());
            //检查网络，并且尝试重新连接
            if (xTcpClient != null) {
                socketHearBeatReqCount=0;
                socketHearBeatRespCount=0;
                xTcpClient.reconnect();
                tickPaused=true;//暂停发送心跳
            }
        }
        else  if(absCount>1){
            AppLogger.e("socket","socket -------- 心跳异常 网络异常.  req="
                    +socketHearBeatReqCount+"   resp="+socketHearBeatRespCount);

            //2次心跳没回应则丢包 异常
            RxBus.get().send(RxBusCode.RX_BUS_CODE_SOCKET_CONNECT_ERROR,new SocketConnectError());
            //发送心跳包
            pinging = true;
            sendMsg("ping");
        }
        else{
            //发送心跳包
            pinging = true;
            sendMsg("ping");
        }



    }



    @Override
    public void onConnected(boolean isSucceed,XTcpClient client) {
        tickPaused=false;
        pinging = false;
        SocketBaseResponse resp = new SocketBaseResponse();
        resp.success = isSucceed;
        if(isSucceed) {//连接成功
            if (isFirstConnected) {
                //是首次连接成功
                resp.success = true;
                resp.isFirstConnected = true;
                socketHearBeatReqCount = 0;
                socketHearBeatRespCount = 0;
                isFirstConnected = false;
            } else {
                //非首次连接成功
                resp.isFirstConnected = false;
            }
        }else{
            //连接失败
            socketHearBeatReqCount = 1;//重连失败后只需发送一次心跳后继续重连
            socketHearBeatRespCount = 0;
        }

        if (this.onSocketConnected != null) {
            this.onSocketConnected.cbBlock(resp);
        }

    }


    @Override
    public void onSended(XTcpClient client, TcpMsg tcpMsg) {
        AppLogger.i("socket", tcpMsg.getSourceDataString());
    }

    @Override
    public void onDisconnected(XTcpClient client, String msg, Exception e) {
        AppLogger.i("socket", client.getTargetInfo().getIp() + "断开连接 " + msg + e);
        RxBus.get().send(RxBusCode.RX_BUS_CODE_SOCKET_CONNECTION_INTERRUPT_ERROR,new SocketConnectError());
    }



    @Override
    public void onReceive(XTcpClient client, TcpMsg msg) {

        String message = msg.getSourceDataString();
        if (message.equals("pong")) {
            //心跳返回
            pinging = false;
            socketHearBeatReqCount=0;
             socketHearBeatRespCount=0;
            AppLogger.i("socket","the socket ------->收到心跳包回应......");
        }
    }



    @Override
    public void onValidationFail(XTcpClient client, TcpMsg tcpMsg) {

    }

    //api 发送socket 消息 方法，带回调
    public void sendMsg(String msg, final FHSendMessageCallBack callBack) {
        AppLogger.i("socket","the api msg  send ....[msg:"+msg);
        if (msg != null && xTcpClient != null) {
            TcpClientListener clientListener= new TcpClientListener() {
                @Override
                public void onConnected(boolean isSucceed, XTcpClient client) {

                }

                @Override
                public void onSended(XTcpClient client, TcpMsg tcpMsg) {
                    String recv = tcpMsg.getSourceDataString();
                    if (!recv.equals("pong")) {
                        //非心跳返回
                        SocketMessage socketMessage= RxSocketUtil.
                                parseSocketMsg(tcpMsg.getSourceDataString());
                        if("api".equals(socketMessage.getMsgType())){
                            //只处理api 回应
//                            callBack.onMessage(tcpMsg.getSourceDataString());
//                            removeTcpClientListener(this);
                            AppLogger.i("socket","the api msg 已发送--"+socketMessage.getAction());
                        }
                    }


                }

                @Override
                public void onDisconnected(XTcpClient client, String msg, Exception e) {

                }

                @Override
                public void onReceive(XTcpClient client, TcpMsg tcpMsg) {
                    String recv = tcpMsg.getSourceDataString();
                    if (!recv.equals("pong")) {
                        //非心跳返回
                        SocketMessage socketMessage= RxSocketUtil.
                                parseSocketMsg(tcpMsg.getSourceDataString());
                      if("api".equals(socketMessage.getMsgType())){
                          //只处理api 回应
                          callBack.onMessage(tcpMsg.getSourceDataString());

                          removeTcpClientListener(this);
                      }
                    }

                }


                @Override
                public void onValidationFail(XTcpClient client, TcpMsg tcpMsg) {
                    if (!tcpMsg.equals("pong")) {
                        SocketMessage socketMessage= RxSocketUtil.parseSocketMsg(tcpMsg.getSourceDataString());
                        if("api".equals(socketMessage.getMsgType())) {
                            //只处理api 回应
                            callBack.onFailure(tcpMsg.getSourceDataString());
                            removeTcpClientListener(this);
                        }
                    }
                }
            };
            xTcpClient.addTcpClientListener(clientListener);
            xTcpClient.sendMsg(msg.getBytes());
        }
    }


    public synchronized void removeTcpClientListener(TcpClientListener listener){
        xTcpClient.removeTcpClientListener(listener);
    }


    public void sendMsg(String msg) {
        AppLogger.i("socket","----------------the HeartBeat  send .... [msg:"+msg
                +socketHearBeatReqCount+"  resp="+socketHearBeatRespCount);
        if (msg != null && xTcpClient != null) {
            xTcpClient.sendMsg(msg.getBytes());
        }
    }

}

