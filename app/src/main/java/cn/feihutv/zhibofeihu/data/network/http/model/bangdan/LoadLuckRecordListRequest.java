package cn.feihutv.zhibofeihu.data.network.http.model.bangdan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/10/25 21:03
 *      desc   : 幸运榜请求体
 *      version: 1.0
 * </pre>
 */

public class LoadLuckRecordListRequest {

    @Expose
    @SerializedName("rankType")
    private String rankType;

    @Expose
    @SerializedName("offset")
    private String offset;

    @Expose
    @SerializedName("count")
    private String count;

    public LoadLuckRecordListRequest(String rankType, String offset, String count) {
        this.rankType = rankType;
        this.offset = offset;
        this.count = count;
    }

    public String getRankType() {
        return rankType;
    }

    public void setRankType(String rankType) {
        this.rankType = rankType;
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
