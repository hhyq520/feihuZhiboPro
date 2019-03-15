package cn.feihutv.zhibofeihu.data.network.socket.model.push;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/10/16
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class BannedLivePush extends  BasePush{


    @Expose
    @SerializedName("Msg")
    private String msg;

    @Expose
    @SerializedName("Duration")
    private String duration;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
