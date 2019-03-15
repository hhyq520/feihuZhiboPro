package cn.feihutv.zhibofeihu.data.network.socket.model.live;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by huanghao on 2017/10/13.
 */

public class StartLiveRequest {
    @Expose
    @SerializedName("device")
    private String device;
    @Expose
    @SerializedName("liveName")
    private String liveName;
    @Expose
    @SerializedName("adress")
    private String adress;
    @Expose
    @SerializedName("reconnect")
    private String reconnect;
    @Expose
    @SerializedName("tag")
    private String tag;

    public StartLiveRequest(String device, String liveName, String adress, String reconnect, String tag) {
        this.device = device;
        this.liveName = liveName;
        this.adress = adress;
        this.reconnect = reconnect;
        this.tag = tag;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getLiveName() {
        return liveName;
    }

    public void setLiveName(String liveName) {
        this.liveName = liveName;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getReconnect() {
        return reconnect;
    }

    public void setReconnect(String reconnect) {
        this.reconnect = reconnect;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
