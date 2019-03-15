package cn.feihutv.zhibofeihu.data.network.http.model.mv;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/12/04
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class ShareMvRequest {

    @Expose
    @SerializedName("mvId")
    private  String  mvId;
    @Expose
    @SerializedName("to")
    private  int to; //分享到平台 1朋友圈 2微博 3微信好友 4QQ空间 5qq好友


    public ShareMvRequest(String mvId, int to) {
        this.mvId = mvId;
        this.to = to;
    }

    public String getMvId() {
        return mvId;
    }

    public void setMvId(String mvId) {
        this.mvId = mvId;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }
}
