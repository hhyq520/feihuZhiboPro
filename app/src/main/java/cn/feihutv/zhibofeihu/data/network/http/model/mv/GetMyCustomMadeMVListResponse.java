package cn.feihutv.zhibofeihu.data.network.http.model.mv;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/11/28
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class GetMyCustomMadeMVListResponse extends BaseResponse {

    @Expose
    @SerializedName("Data")
    private GetMyCustomMadeMVListData mGetMyCustomMadeMVListData;

    public GetMyCustomMadeMVListData getGetMyCustomMadeMVListData() {
        return mGetMyCustomMadeMVListData;
    }

    public void setGetMyCustomMadeMVListData(GetMyCustomMadeMVListData getMyCustomMadeMVListData) {
        mGetMyCustomMadeMVListData = getMyCustomMadeMVListData;
    }

    public static class GetMyCustomMadeMVListData{

        @Expose
        @SerializedName("List")
        private List<GetMyCustomMadeMVList> mGetMyCustomMadeMVLists;

        @Expose
        @SerializedName("NextOffset")
        private  String   nextOffset;


        public List<GetMyCustomMadeMVList> getGetMyCustomMadeMVLists() {
            return mGetMyCustomMadeMVLists;
        }

        public void setGetMyCustomMadeMVLists(List<GetMyCustomMadeMVList> getMyCustomMadeMVLists) {
            mGetMyCustomMadeMVLists = getMyCustomMadeMVLists;
        }

        public String getNextOffset() {
            return nextOffset;
        }

        public void setNextOffset(String nextOffset) {
            this.nextOffset = nextOffset;
        }
    }

public static class GetMyCustomMadeMVList{



    @Expose
    @SerializedName("Avatar")
    private  String  avatar;// "https://im",  //主播头像

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
    @SerializedName("Liked")
    private  String  liked;//false,                   //是否已赞

    @Expose
    @SerializedName("MVId")
    private  String  mVId;// 4,

    @Expose
    @SerializedName("Nickname")
    private  String  nickname;// "admin",        //主播昵称

    @Expose
    @SerializedName("T")
    private  Long  t;// 1510385510,                //mv发布时间戳，秒

    @Expose
    @SerializedName("Title")
    private  String  title;// "testMV2",                //mv标题

    @Expose
    @SerializedName("Uid")
    private  String  uid;// "11332482",              //主播uid

    @Expose
    @SerializedName("VideoId")
    private  String  videoId;// "3C07EB596D7C8D98D2130A059B922747"


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

    public String getLiked() {
        return liked;
    }

    public void setLiked(String liked) {
        this.liked = liked;
    }

    public String getVId() {
        return mVId;
    }

    public void setVId(String VId) {
        mVId = VId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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
