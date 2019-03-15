package cn.feihutv.zhibofeihu.data.network.http.model.mv;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/11/11
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class GetAllNeedListRequest {

    @Expose
    @SerializedName("forPostMV")
    private  String   forPostMV;//1用在发布mv选择需求，0用在mv主页需求广场

    @Expose
    @SerializedName("forMe")
    private  String forMe;//1, //加载指定我的,初次传1，之后从返回值中获取

    @Expose
    @SerializedName("offset")
    private  String  offset;//偏移量（首次传0，之后从返回值中获取）
    @Expose
    @SerializedName("count")
    private  String  count;//加载条数（最多20）


    public GetAllNeedListRequest(String forPostMV, String forMe, String offset, String count) {
        this.forPostMV = forPostMV;
        this.forMe = forMe;
        this.offset = offset;
        this.count = count;
    }

    public String getForPostMV() {
        return forPostMV;
    }

    public void setForPostMV(String forPostMV) {
        this.forPostMV = forPostMV;
    }

    public String getForMe() {
        return forMe;
    }

    public void setForMe(String forMe) {
        this.forMe = forMe;
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
