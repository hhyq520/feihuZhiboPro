package cn.feihutv.zhibofeihu.data.network.socket.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/10/12
 *     desc   : 验证socket token 请求
 *     version: 1.0
 * </pre>
 */
public class VerifySocketTokenRequest {

    @Expose
    @SerializedName("token")
    private String token;

    @Expose
    @SerializedName("reconnect")
    private String reconnect;

    public VerifySocketTokenRequest(String token, String reconnect) {
        this.token = token;
        this.reconnect = reconnect;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setReconnect(String reconnect) {
        this.reconnect = reconnect;
    }
}
