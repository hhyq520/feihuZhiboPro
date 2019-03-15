package cn.feihutv.zhibofeihu.data.network.http.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/09/27
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class GetSocketTokenRequest {

    @Expose
    @SerializedName("type")
    private String type="1";//默认值1
    @Expose
    @SerializedName("phoneOperation")
    private String phoneOperation;
    @Expose
    @SerializedName("phoneModel")
    private String phoneModel;
    @Expose
    @SerializedName("mac")
    private String mac;
    @Expose
    @SerializedName("reconnect")
    private String reconnect;


    public GetSocketTokenRequest(String type, String phoneOperation, String phoneModel, String mac, String reconnect) {
        this.type = type;
        this.phoneOperation = phoneOperation;
        this.phoneModel = phoneModel;
        this.mac = mac;
        this.reconnect = reconnect;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhoneOperation() {
        return phoneOperation;
    }

    public void setPhoneOperation(String phoneOperation) {
        this.phoneOperation = phoneOperation;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getReconnect() {
        return reconnect;
    }

    public void setReconnect(String reconnect) {
        this.reconnect = reconnect;
    }
}
