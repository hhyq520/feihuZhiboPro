package cn.feihutv.zhibofeihu.data.network.http.model.vip;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/10/31
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class GetVipReceiveLogRequest {


    @Expose
    @SerializedName("offset")
    private int  offset;//偏移量（首次传0，之后从返回值中获取）



    @Expose
    @SerializedName("count")
    private int   count;//加载条数（最多20）


    public GetVipReceiveLogRequest(int offset, int count) {
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
