package cn.feihutv.zhibofeihu.data.network.http.model.me;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/10/27
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class LoadUserHBDataRequest {
    @Expose
    @SerializedName("userId")
    public String userId; //

    public LoadUserHBDataRequest(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
