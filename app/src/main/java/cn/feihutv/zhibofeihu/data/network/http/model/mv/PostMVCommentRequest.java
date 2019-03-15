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
public class PostMVCommentRequest {


    @Expose
    @SerializedName("mvId")
    private  String   mvId;//int

    @Expose
    @SerializedName("commentId")
    private  String   commentId;//@某条评论的id

    @Expose
    @SerializedName("content")
    private  String    content;//评论内容

    public PostMVCommentRequest(String mvId, String commentId, String content) {
        this.mvId = mvId;
        this.commentId = commentId;
        this.content = content;
    }

    public String getMvId() {
        return mvId;
    }

    public void setMvId(String mvId) {
        this.mvId = mvId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

