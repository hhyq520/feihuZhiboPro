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
public class PostNeedRequest {



    @Expose
    @SerializedName("forUid")
    private  String forUid ; //为空表示给所有主播，不为空表示需要定制

    @Expose
    @SerializedName("title")
    private  String title ; //标题

    @Expose
    @SerializedName("songName")
    private  String songName ;//歌名

    @Expose
    @SerializedName("singerName")
    private  String  singerName; //歌手名

    @Expose
    @SerializedName("require")
    private  String require ; //具体要求
    @Expose
    @SerializedName("hb")
    private  int  hb ; //给主播的报酬

    public PostNeedRequest() {
    }

    public PostNeedRequest(String forUid, String title, String songName, String singerName, String require, int hb) {
        this.forUid = forUid;
        this.title = title;
        this.songName = songName;
        this.singerName = singerName;
        this.require = require;
        this.hb = hb;
    }

    public String getForUid() {
        return forUid;
    }

    public void setForUid(String forUid) {
        this.forUid = forUid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getRequire() {
        return require;
    }

    public void setRequire(String require) {
        this.require = require;
    }

    public int getHb() {
        return hb;
    }

    public void setHb(int hb) {
        this.hb = hb;
    }
}
