package cn.feihutv.zhibofeihu.data.network.socket.model.live;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by huanghao on 2017/10/17.
 */

public class LoadOtherUserInfoRequest {
    @Expose
    @SerializedName("userId")
    private String userId;

    public LoadOtherUserInfoRequest(String userId) {
        this.userId=userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
