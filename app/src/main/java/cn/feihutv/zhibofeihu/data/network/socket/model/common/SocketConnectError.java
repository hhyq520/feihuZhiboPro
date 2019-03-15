package cn.feihutv.zhibofeihu.data.network.socket.model.common;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/10/18
 *     desc   : socket 连接异常
 *     version: 1.0
 * </pre>
 */
public class SocketConnectError {

    public String isReconnecting;

    public SocketConnectError() {
    }

    public SocketConnectError(String isReconnecting) {
        this.isReconnecting = isReconnecting;
    }
}
