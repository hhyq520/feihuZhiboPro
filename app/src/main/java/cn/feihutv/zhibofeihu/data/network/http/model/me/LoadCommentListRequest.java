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
public class LoadCommentListRequest {

    @Expose
    @SerializedName("id")
    public String id; //

    @Expose
    @SerializedName("last")
    public String last; //


    @Expose
    @SerializedName("cnt")
    public String cnt; //

    public LoadCommentListRequest(String id, String last, String cnt) {
        this.id = id;
        this.last = last;
        this.cnt = cnt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getCnt() {
        return cnt;
    }

    public void setCnt(String cnt) {
        this.cnt = cnt;
    }
}
