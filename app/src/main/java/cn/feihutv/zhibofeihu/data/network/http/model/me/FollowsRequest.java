package cn.feihutv.zhibofeihu.data.network.http.model.me;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/10/31 21:01
 *      desc   : 关注和粉丝列表
 *      version: 1.0
 * </pre>
 */

public class FollowsRequest {

    @Expose
    @SerializedName("offset")
    private String offset;

    @Expose
    @SerializedName("count")
    private String count;

    public FollowsRequest(String offset, String count) {
        this.offset = offset;
        this.count = count;
    }
}
