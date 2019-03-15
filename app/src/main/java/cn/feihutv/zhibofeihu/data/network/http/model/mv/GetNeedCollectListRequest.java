package cn.feihutv.zhibofeihu.data.network.http.model.mv;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/11/14
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class GetNeedCollectListRequest {

    @Expose
    @SerializedName("offset")
    private  String offset;//偏移量（首次传0，之后从返回值中获取）
    @Expose
    @SerializedName("count")
    private  String  count;//加载条数（最多20）

    public GetNeedCollectListRequest(String offset, String count) {
        this.offset = offset;
        this.count = count;
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
