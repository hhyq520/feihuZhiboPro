package cn.feihutv.zhibofeihu.data.network.http.model.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *      author : liwen.chen
 *      time   : 2017/10/12 10:11
 *      desc   : 请求首页banner
 *      version: 1.0
 * </pre>
 */

public class GetBannerRequest {

    @Expose
    @SerializedName("type")
    private String type;

    public GetBannerRequest(String type) {
        this.type = type;
    }

    public GetBannerRequest() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
