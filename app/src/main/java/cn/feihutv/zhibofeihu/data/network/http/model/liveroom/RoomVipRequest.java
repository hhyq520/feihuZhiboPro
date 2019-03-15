package cn.feihutv.zhibofeihu.data.network.http.model.liveroom;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by huanghao on 2017/11/10.
 */

public class RoomVipRequest {
    @Expose
    @SerializedName("offset")
    private int offset;
    @Expose
    @SerializedName("count")
    private int count;

    public RoomVipRequest(int offset, int count) {
        this.offset = offset;
        this.count = count;
    }
}
