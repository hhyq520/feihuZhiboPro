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
public class GetAllMvListRequest {

    @Expose
    @SerializedName("offset")
    private  String  offset;//首次可设置为“”

    public GetAllMvListRequest(String offset) {
        this.offset = offset;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }
}
