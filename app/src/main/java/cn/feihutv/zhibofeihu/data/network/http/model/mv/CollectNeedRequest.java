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
public class CollectNeedRequest {


    @Expose
    @SerializedName("needId")
    private  String   needId ;//需求id

    public CollectNeedRequest(String needId) {
        this.needId = needId;
    }


    public String getNeedId() {
        return needId;
    }

    public void setNeedId(String needId) {
        this.needId = needId;
    }
}
