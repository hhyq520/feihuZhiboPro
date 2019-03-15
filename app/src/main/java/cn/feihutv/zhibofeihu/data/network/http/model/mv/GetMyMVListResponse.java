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
public class GetMyMVListResponse extends BaseResponse {

    @Expose
    @SerializedName("Data")
    private  GetMyMVListData  mGetMyMVListData;

    public GetMyMVListData getGetMyMVListData() {
        return mGetMyMVListData;
    }

    public void setGetMyMVListData(GetMyMVListData getMyMVListData) {
        mGetMyMVListData = getMyMVListData;
    }

    public static class GetMyMVListData{
        @Expose
        @SerializedName("NextOffset")
        private  String  nextOffset;//加载条数（最多20）

        @Expose
        @SerializedName("List")
        private List<GetMyMVList> mGetMyMVLists;

        public String getNextOffset() {
            return nextOffset;
        }

        public void setNextOffset(String nextOffset) {
            this.nextOffset = nextOffset;
        }

        public List<GetMyMVList> getGetMyMVLists() {
            return mGetMyMVLists;
        }

        public void setGetMyMVLists(List<GetMyMVList> getMyMVLists) {
            mGetMyMVLists = getMyMVLists;
        }
    }


    public static class GetMyMVList implements Serializable{

        @Expose
        @SerializedName("Avatar")
        private  String  avatar;// "https://im",  //需求方头像

        @Expose
        @SerializedName("Comments")
        private  String  comments;// 0,         //评论数

        @Expose
        @SerializedName("Cover")
        private  String  cover;// "https://img.fei",    //mv封面

        @Expose
        @SerializedName("GiftIncome")
        private  String  giftIncome;// 0,                 //礼物收入

        @Expose
        @SerializedName("Likes")
        private  String  likes;// 0,                         //点赞数量

        @Expose
        @SerializedName("MVId")
        private  String  mvId;// 4,

        @Expose
        @SerializedName("Nickname")
        private  String  nickname;// "admin",        //需求方昵称


        @Expose
        @SerializedName("T")
        private  Long  t;// 1510385510,                //mv发布时间戳，秒

        @Expose
        @SerializedName("Title")
        private  String  title;// "testMV2",                //mv标题

        @Expose
        @SerializedName("Uid")
        private  String  uid;// "11332482",              //需求方uid



        @Expose
        @SerializedName("NeedId")
        private String needId;// 1,//需求id

        @Expose
        @SerializedName("VideoId")
        private  String  videoId;// "3C07EB596D7C8D98D2130A059B922747"


        @Expose
        @SerializedName("Status")
        private  int  status;//mv状态

        @Expose
        @SerializedName("Require")
        private  String  require;// 需求要求

        @Expose
        @SerializedName("SongName")
        private  String  songName;//

        @Expose
        @SerializedName("NeedHb")
        private String hB;// 10000,//悬赏虎币数量

        @Expose
        @SerializedName("SingerName")
        private  String  singerName;//

        public String getMvId() {
            return mvId;
        }

        public void setMvId(String mvId) {
            this.mvId = mvId;
        }

        public String gethB() {
            return hB;
        }

        public void sethB(String hB) {
            this.hB = hB;
        }

        public String getNeedId() {
            return needId;
        }

        public void setNeedId(String needId) {
            this.needId = needId;
        }

        public Long getT() {
            return t;
        }

        public void setT(Long t) {
            this.t = t;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
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

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getComments() {
            return comments;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getGiftIncome() {
            return giftIncome;
        }

        public void setGiftIncome(String giftIncome) {
            this.giftIncome = giftIncome;
        }

        public String getLikes() {
            return likes;
        }

        public void setLikes(String likes) {
            this.likes = likes;
        }



        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }



        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }
    }

}
