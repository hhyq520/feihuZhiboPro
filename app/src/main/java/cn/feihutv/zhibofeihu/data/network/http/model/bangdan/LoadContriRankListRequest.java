package cn.feihutv.zhibofeihu.data.network.http.model.bangdan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *      author : liwen.chen on 2017/10/11 14:04.
 *      time   : 2017/10/11 14:04
 *      desc   : 拉取贡献榜和排行榜request
 *      version: 1.0
 * </pre>
 */

public class LoadContriRankListRequest {

    @Expose
    @SerializedName("rankType")
    String rankType;

    public LoadContriRankListRequest(String rankType) {
        this.rankType = rankType;
    }

    public String getRankType() {
        return rankType;
    }

    public void setRankType(String rankType) {
        this.rankType = rankType;
    }
}
