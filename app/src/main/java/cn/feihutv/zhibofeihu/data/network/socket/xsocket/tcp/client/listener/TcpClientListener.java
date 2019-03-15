package cn.feihutv.zhibofeihu.data.network.socket.xsocket.tcp.client.listener;


import cn.feihutv.zhibofeihu.data.network.socket.xsocket.tcp.client.XTcpClient;
import cn.feihutv.zhibofeihu.data.network.socket.xsocket.tcp.client.bean.TcpMsg;

/**
 */
public interface TcpClientListener {

    void onConnected(boolean isSucceed,XTcpClient client);

    void onSended(XTcpClient client, TcpMsg tcpMsg);

    void onDisconnected(XTcpClient client, String msg, Exception e);

    void onReceive(XTcpClient client, TcpMsg tcpMsg);

    void onValidationFail(XTcpClient client, TcpMsg tcpMsg);

    class SimpleTcpClientListener implements TcpClientListener {

        @Override
        public void onConnected(boolean isSucceed,XTcpClient client) {

        }

        @Override
        public void onSended(XTcpClient client, TcpMsg tcpMsg) {

        }

        @Override
        public void onDisconnected(XTcpClient client, String msg, Exception e) {

        }

        @Override
        public void onReceive(XTcpClient client, TcpMsg tcpMsg) {

        }

        @Override
        public void onValidationFail(XTcpClient client, TcpMsg tcpMsg) {

        }

    }
}
