package cn.feihutv.zhibofeihu.data.network.http.model.me;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/6 16:25
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class UnblockRequest {

    @Expose
    @SerializedName("userId")
    private String userId;

    public UnblockRequest(String userId) {
        this.userId = userId;
    }
}
