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
public class GetMyMVListRequest  {

    @Expose
    @SerializedName("status")
    private  String  status ;//状态 0全部  1已过期 2审核失败 3已完成 4待审核 5待成交 6待修改


    @Expose
    @SerializedName("offset")
    private  String  offset;//偏移量（首次传0，之后从返回值中获取）
    @Expose
    @SerializedName("count")
    private  String  count;//加载条数（最多20）

    public GetMyMVListRequest(String status, String offset, String count) {
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
