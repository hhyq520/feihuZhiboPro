package cn.feihutv.zhibofeihu.data.network.http.model.liveroom;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by huanghao on 2017/10/17.
 */

public class LoadRoomMembersRequest {
    @Expose
    @SerializedName("offset")
    private int offset;
    @Expose
    @SerializedName("count")
    private int count;
    @Expose
    @SerializedName("type")
    private String type;

    public LoadRoomMembersRequest(int offset, int count, String type) {
        this.offset = offset;
        this.count = count;
        this.type = type;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
