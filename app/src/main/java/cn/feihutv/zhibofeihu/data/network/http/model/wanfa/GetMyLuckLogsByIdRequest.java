package cn.feihutv.zhibofeihu.data.network.http.model.wanfa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by huanghao on 2017/10/19.
 */

public class GetMyLuckLogsByIdRequest {
    @Expose
    @SerializedName("offset")
    private int offset;
    @Expose
    @SerializedName("count")
    private int count;
    @Expose
    @SerializedName("giftId")
    private int giftId;

    public GetMyLuckLogsByIdRequest(int giftId,int offset, int count) {
        this.giftId = giftId;
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

    public int getGiftId() {
        return giftId;
    }

    public void setGiftId(int giftId) {
        this.giftId = giftId;
    }
}
