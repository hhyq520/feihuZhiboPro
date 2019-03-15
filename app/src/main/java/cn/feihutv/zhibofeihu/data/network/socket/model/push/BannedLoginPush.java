package cn.feihutv.zhibofeihu.data.network.socket.model.push;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.annotation.Annotation;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/10/16
 *     desc   : 被禁号了
 *     version: 1.0
 * </pre>
 */
public class BannedLoginPush extends BasePush {


    @Expose
    @SerializedName("Msg")
    private String msg;

    @Expose
    @SerializedName("Duration")
    private String duration;

    @Expose
    @SerializedName("QQ")
    private String qq;

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

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

}
