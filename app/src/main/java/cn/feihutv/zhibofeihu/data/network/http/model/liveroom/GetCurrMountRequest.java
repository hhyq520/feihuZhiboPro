package cn.feihutv.zhibofeihu.data.network.http.model.liveroom;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by huanghao on 2017/10/21.
 */

public class GetCurrMountRequest {
    @Expose
    @SerializedName("roomId")
    private String roomId;

    public GetCurrMountRequest(String roomId) {
        this.roomId = roomId;
    }
}
