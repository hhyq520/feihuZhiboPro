package cn.feihutv.zhibofeihu.data.network.http.model.mv;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/11/14
 *     desc   : 获取我的需求下的 待完成，已完成作品列表
 *     version: 1.0
 * </pre>
 */
public class GetMyNeedMVListRequest {

    @Expose
    @SerializedName("status")
    private  String status;//mv状态   4待成交，3已成交

    @Expose
    @SerializedName("offset")
    private  String offset;//偏移量（首次传0，之后从返回值中获取）
    @Expose
    @SerializedName("count")
    private  String count;//加载条数（最多20）

    public GetMyNeedMVListRequest(String status, String offset, String count) {
        this.status = status;
        this.offset = offset;
        this.count = count;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
