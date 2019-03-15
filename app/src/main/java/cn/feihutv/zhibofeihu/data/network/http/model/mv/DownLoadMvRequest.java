package cn.feihutv.zhibofeihu.data.network.http.model.mv;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/12/05
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class DownLoadMvRequest {

    @Expose
    @SerializedName("mvId")
    private  String mvId;//


    public DownLoadMvRequest(String mvId) {
        this.mvId = mvId;
    }

    public String getMvId() {
        return mvId;
    }

    public void setMvId(String mvId) {
        this.mvId = mvId;
    }
}
