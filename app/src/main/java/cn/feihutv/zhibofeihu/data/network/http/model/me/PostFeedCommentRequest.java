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
public class PostFeedCommentRequest {

    @Expose
    @SerializedName("id")
    public String id; //

    @Expose
    @SerializedName("content")
    public String content; //

    @Expose
    @SerializedName("reply")
    public String reply; //

    public PostFeedCommentRequest(String id, String content, String reply) {
        this.id = id;
        this.content = content;
        this.reply = reply;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
}


