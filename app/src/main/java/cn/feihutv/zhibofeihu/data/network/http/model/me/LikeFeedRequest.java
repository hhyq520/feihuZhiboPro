package cn.feihutv.zhibofeihu.data.network.http.model.me;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/10/26
 *     desc   : 动态点赞
 *     version: 1.0
 * </pre>
 */
public class LikeFeedRequest {

    @Expose
    @SerializedName("id")
    public String id; //  动态ID


    public LikeFeedRequest(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
