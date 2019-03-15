package cn.feihutv.zhibofeihu.data.network.http.model.me;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/6 14:26
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class GetBlacklistRequest {

    @Expose
    @SerializedName("offset")
    private String offset;

    @Expose
    @SerializedName("count")
    private String count;

    public GetBlacklistRequest(String offset, String count) {
        this.offset = offset;
        this.count = count;
    }
}
