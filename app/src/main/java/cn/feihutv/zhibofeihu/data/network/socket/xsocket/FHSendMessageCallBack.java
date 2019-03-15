package cn.feihutv.zhibofeihu.data.network.socket.xsocket;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/10/13
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public interface FHSendMessageCallBack {



    void onFailure(String errorMsg);

    void onMessage(String message);

}
