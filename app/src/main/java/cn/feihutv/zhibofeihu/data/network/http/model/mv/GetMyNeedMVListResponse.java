package cn.feihutv.zhibofeihu.data.network.http.model.mv;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/11/14
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class GetMyNeedMVListResponse extends BaseResponse{

    @Expose
    @SerializedName("Data")
    private  GetMyNeedMVListData mGetMyNeedMVListData;

    public GetMyNeedMVListData getGetMyNeedMVListData() {
        return mGetMyNeedMVListData;
    }

    public void setGetMyNeedMVListData(GetMyNeedMVListData getMyNeedMVListData) {
        mGetMyNeedMVListData = getMyNeedMVListData;
    }

    public static class GetMyNeedMVListData{


        @Expose
        @SerializedName("NextOffset")
        private  String  nextOffset;//加载条数（最多20）


        @Expose
        @SerializedName("List")
        private List<GetMyNeedMVList> mGetMyNeedMVLists;//

        public String getNextOffset() {
            return nextOffset;
        }

        public void setNextOffset(String nextOffset) {
            this.nextOffset = nextOffset;
        }

        public List<GetMyNeedMVList> getGetMyNeedMVLists() {
            return mGetMyNeedMVLists;
        }

        public void setGetMyNeedMVLists(List<GetMyNeedMVList> getMyNeedMVLists) {
            mGetMyNeedMVLists = getMyNeedMVLists;
        }
    }



    public static class GetMyNeedMVList implements Serializable{

        @Expose
        @SerializedName("Avatar")
        private  String  avatar;// "https://img.f", //主播头像

        @Expose
        @SerializedName("Cover")
        private  String  cover;// "https://img",  //mv封面

        @Expose
        @SerializedName("MVId")
        private  String  mvId;// 4,

        @Expose
        @SerializedName("Nickname")
        private  String  nickname;// "admin",//主播昵称

        @Expose
        @SerializedName("Require")
        private  String  require;// "主播说爱我",//需求要求

        @Expose
        @SerializedName("SongName")
        private  String  songName;// "菊花台-,

        @Expose
        @SerializedName("SingerName")
        private  String  singerName;// 周杰伦",


        @Expose
        @SerializedName("T")
        private  Long  t;// 213026,//mv过期时间剩秒数，负数表示已过期

        @Expose
        @SerializedName("Uid")
        private  String  uid;// "11332482" //主播的uid


        @Expose
        @SerializedName("Title")
        private  String  title;//


        @Expose
        @SerializedName("MVPostTime")
        private  Long  mVPostTime;//


        @Expose
        @SerializedName("NeedHb")
        private  Long  needHb;//
        @Expose
        @SerializedName("VideoId")
        private  String  videoId;//


        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getMvId() {
            return mvId;
        }

        public void setMvId(String mvId) {
            this.mvId = mvId;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getRequire() {
            return require;
        }

        public void setRequire(String require) {
            this.require = require;
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

        public Long getT() {
            return t;
        }

        public void setT(Long t) {
            this.t = t;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Long getVPostTime() {
            return mVPostTime;
        }

        public void setVPostTime(Long VPostTime) {
            mVPostTime = VPostTime;
        }

        public Long getNeedHb() {
            return needHb;
        }

        public void setNeedHb(Long needHb) {
            this.needHb = needHb;
        }

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }
    }
}
