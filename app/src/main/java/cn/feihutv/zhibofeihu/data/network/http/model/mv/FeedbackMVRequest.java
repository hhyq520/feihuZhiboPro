package cn.feihutv.zhibofeihu.data.network.http.model.mv;

import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/11/14
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class FeedbackMVRequest {

    @SerializedName("mvId")
    private  String mvId;//


    @SerializedName("require")
    private  String require;//

    public FeedbackMVRequest(String mvId, String require) {
        this.mvId = mvId;
        this.require = require;
    }

    public String getMvId() {
        return mvId;
    }

    public void setMvId(String mvId) {
        this.mvId = mvId;
    }

    public String getRequire() {
        return require;
    }

    public void setRequire(String require) {
        this.require = require;
    }
}
