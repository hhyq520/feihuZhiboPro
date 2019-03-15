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
public class ShareFeedRequest {

    @Expose
    @SerializedName("id")
    public String id; //

    public ShareFeedRequest(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
