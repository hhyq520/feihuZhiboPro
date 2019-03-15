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
public class PostMvRequest {

    @Expose
    @SerializedName("title")
    private  String title ;//mv标题

    @Expose
    @SerializedName("cover")
    private  String    cover;//封面地址

    @Expose
    @SerializedName("videoId")
    private  String videoId  ;//网宿云视频videoId
    @Expose
    @SerializedName("needId")
    private  String    needId; //0公开 非0定制

    public PostMvRequest(String title, String cover, String videoId, String needId) {
        this.title = title;
        this.cover = cover;
        this.videoId = videoId;
        this.needId = needId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getNeedId() {
        return needId;
    }

    public void setNeedId(String needId) {
        this.needId = needId;
    }
}
