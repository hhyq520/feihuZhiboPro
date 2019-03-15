package cn.feihutv.zhibofeihu.data.network.http.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by huanghao on 2017/10/25.
 */

public class GetFriendsRequest {
    @Expose
    @SerializedName("offset")
    private int offset;
    @Expose
    @SerializedName("count")
    private int count;

    public GetFriendsRequest(int offset, int count) {
        this.offset = offset;
        this.count = count;
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
}
