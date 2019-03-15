package cn.feihutv.zhibofeihu.data.network.http.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by huanghao on 2017/10/17.
 */

public class LoadRoomContriRequest {
    @Expose
    @SerializedName("userId")
    private String userId;
    @Expose
    @SerializedName("rankType")
    private int rankType;

    public LoadRoomContriRequest() {
    }

    public LoadRoomContriRequest(String userId, int rankType) {
        this.userId = userId;
        this.rankType = rankType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getRankType() {
        return rankType;
    }

    public void setRankType(int rankType) {
        this.rankType = rankType;
    }
}
