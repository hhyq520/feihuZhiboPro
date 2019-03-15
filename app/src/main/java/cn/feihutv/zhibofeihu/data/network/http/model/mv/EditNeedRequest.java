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
public class EditNeedRequest {


    @Expose
    @SerializedName("needId")
    private  String  needId ;//需求id

    @Expose
    @SerializedName("songName")
    private  String      songName;//歌名

    @Expose
    @SerializedName("singerName")
    private  String    singerName;//歌手名
    @Expose
    @SerializedName("require")
    private  String         require;//具体要求


    public EditNeedRequest(String needId, String songName, String singerName, String require) {
        this.needId = needId;
        this.songName = songName;
        this.singerName = singerName;
        this.require = require;
    }

    public String getNeedId() {
        return needId;
    }

    public void setNeedId(String needId) {
        this.needId = needId;
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
}
