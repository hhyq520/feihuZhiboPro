package cn.feihutv.zhibofeihu.data.network.http.model.mv;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/11/14
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class GetMVDetailResponse extends BaseResponse{

    @Expose
    @SerializedName("Data")
    private GetMVDetail mGetMVDetail;

    public GetMVDetail getGetMVDetail() {
        return mGetMVDetail;
    }

    public void setGetMVDetail(GetMVDetail getMVDetail) {
        mGetMVDetail = getMVDetail;
    }

    public static class GetMVDetail{

        @Expose
        @SerializedName("Comments")
        private  String  comments;// 0,       //评论数

        @Expose
        @SerializedName("Cover")
        private  String  cover;// "asd",         //mv封面

        @Expose
        @SerializedName("Followed")
        private  boolean  followed;// false,      //已关注主播

        @Expose
        @SerializedName("GiftIncome")
        private  Long  giftIncome;// 0,         //礼物收入

        @Expose
        @SerializedName("Liked")
        private  boolean  liked;// false,             //是否已赞

        @Expose
        @SerializedName("Likes")
        private  int  likes;// 0,                   //点赞数量

        @Expose
        @SerializedName("MVId")
        private  String  mVId;// "2",

        @Expose
        @SerializedName("MasterAvatar")
        private  String  masterAvatar;// "https://i",         //主播头像

        @Expose
        @SerializedName("MasterNickname")
        private  String  masterNickname;// " admin",     //主播昵称

        @Expose
        @SerializedName("MasterUid")
        private  String  masterUid;// "29",                      //主播uid

        @Expose
        @SerializedName("Plays")
        private  String  plays;// 0,                                  //播放数

        @Expose
        @SerializedName("T")
        private  Long  t;// 1510107298,                       //mv发布时间戳 秒

        @Expose
        @SerializedName("Title")
        private  String  title;// "testMV",                          //mv标题

        @Expose
        @SerializedName("UserAvatar")
        private  String  userAvatar;// "https",                 //需求方头像

        @Expose
        @SerializedName("UserNickname")
        private  String  userNickname;// "admin",         //需求方昵称

        @Expose
        @SerializedName("UserUid")
        private  String  userUid;// "29",                          //需求方uid

        @Expose
        @SerializedName("VideoId")
        private  String  videoId;// "3C07EB596D7C8D98D213 0A059B922747"

        @Expose
        @SerializedName("BroadCastType")
        private int broadCastType;

        public int getBroadCastType() {
            return broadCastType;
        }

        public void setBroadCastType(int broadCastType) {
            this.broadCastType = broadCastType;
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

        public boolean getFollowed() {
            return followed;
        }

        public void setFollowed(boolean followed) {
            this.followed = followed;
        }

        public Long getGiftIncome() {
            return giftIncome;
        }

        public void setGiftIncome(Long giftIncome) {
            this.giftIncome = giftIncome;
        }

        public boolean getLiked() {
            return liked;
        }

        public void setLiked(boolean liked) {
            this.liked = liked;
        }

        public int getLikes() {
            return likes;
        }

        public void setLikes(int likes) {
            this.likes = likes;
        }

        public String getVId() {
            return mVId;
        }

        public void setVId(String VId) {
            mVId = VId;
        }

        public String getMasterAvatar() {
            return masterAvatar;
        }

        public void setMasterAvatar(String masterAvatar) {
            this.masterAvatar = masterAvatar;
        }

        public String getMasterNickname() {
            return masterNickname;
        }

        public void setMasterNickname(String masterNickname) {
            this.masterNickname = masterNickname;
        }

        public String getMasterUid() {
            return masterUid;
        }

        public void setMasterUid(String masterUid) {
            this.masterUid = masterUid;
        }

        public String getPlays() {
            return plays;
        }

        public void setPlays(String plays) {
            this.plays = plays;
        }

        public Long getT() {
            return t;
        }

        public void setT(Long t) {
            this.t = t;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUserAvatar() {
            return userAvatar;
        }

        public void setUserAvatar(String userAvatar) {
            this.userAvatar = userAvatar;
        }

        public String getUserNickname() {
            return userNickname;
        }

        public void setUserNickname(String userNickname) {
            this.userNickname = userNickname;
        }

        public String getUserUid() {
            return userUid;
        }

        public void setUserUid(String userUid) {
            this.userUid = userUid;
        }

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }
    }

}
