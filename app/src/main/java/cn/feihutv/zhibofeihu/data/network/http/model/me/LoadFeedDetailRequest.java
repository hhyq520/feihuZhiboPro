package cn.feihutv.zhibofeihu.data.network.http.model.me;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/10/26
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class LoadFeedDetailRequest {

    @Expose
    @SerializedName("id")
    public String id; //

    public LoadFeedDetailRequest(String id) {
        this.id = id;
    }
}
