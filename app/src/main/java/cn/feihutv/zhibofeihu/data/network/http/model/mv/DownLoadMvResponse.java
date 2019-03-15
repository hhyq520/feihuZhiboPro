package cn.feihutv.zhibofeihu.data.network.http.model.mv;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/12/05
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class DownLoadMvResponse extends BaseResponse {

    @Expose
    @SerializedName("Data")
    private  String mvUrl;//

    public String getMvUrl() {
        return mvUrl;
    }

    public void setMvUrl(String mvUrl) {
        this.mvUrl = mvUrl;
    }
}
