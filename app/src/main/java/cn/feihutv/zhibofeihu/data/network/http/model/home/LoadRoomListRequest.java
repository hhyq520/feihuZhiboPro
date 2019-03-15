package cn.feihutv.zhibofeihu.data.network.http.model.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by chenliwen on 2017/10/10 19:32.
 * 直播列表request
 */

public class LoadRoomListRequest {

    @Expose
    @SerializedName("tag")
    private String tag;

    @Expose
    @SerializedName("offset")
    private String offset;

    @Expose
    @SerializedName("count")
    private String count;

    public LoadRoomListRequest() {
    }

    public LoadRoomListRequest(String tag, String offset, String count) {
        this.tag = tag;
        this.offset = offset;
        this.count = count;
    }


    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
