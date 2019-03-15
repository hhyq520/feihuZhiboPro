package cn.feihutv.zhibofeihu.data.network.http.model.me;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/22.
 */

public class DynamicItem implements Serializable {

//    "Id":"590013a73401362cef6c71ba",//动态ID
//            "UserId": "test7",//用户id
//            "HeadUrl": "",//头像
//            "NickName": "",//昵称
//            "PublishTime": 1491560803,//动态发布时间
//            "Location": "",//发布该动态时的位置
//            "Content": "",//动态内容
//            "ImgList": ["",""...],//动态中的图片列表
//            "Forwarding": 100,//转发数
//            "Comments": 100,//评论数
//            "Likes": 100,//赞数
//            "Liked":true//我是否已经赞过该动态
//            “Quote”:{//分享引用的原动态，字段可能不存在
//        "Id":"",
//                "UserId":"",
//                "NickName":"",
//                "ImgList":["",],
//        "Content":"",
//    }
    /**
     * {
     "ImgList": [
     "https://img.feihutv.cn//feed/11332617/5a20f9048520872ed5267fb1.jpg"
     ],
     "UserId": "11332617"
     }
     */


    @Expose
    @SerializedName("Id")
    private String id;
    @Expose
    @SerializedName("UserId")
    private String userid;
    @Expose
    @SerializedName("HeadUrl")
    private String headurl;
    @Expose
    @SerializedName("NickName")
    private String nickname;
    @Expose
    @SerializedName("PublishTime")
    private long publishtime;
    @Expose
    @SerializedName("Location")
    private String location;
    @Expose
    @SerializedName("Content")
    private String content;

    @Expose
    @SerializedName("Forwarding")
    private int forwarding;
    @Expose
    @SerializedName("Comments")
    private int comments;
    @Expose
    @SerializedName("Likes")
    private int likes;
    @Expose
    @SerializedName("Liked")
    private boolean liked;

    @Expose
    @SerializedName("FeedType")
    private int feedType;

    @Expose
    @SerializedName("JumpId")
    private String jumpId;

    @Expose
    @SerializedName("Quote")
    private QuoteItem mQuoteItem;

    @Expose
    @SerializedName("ImgList")
    private List<String> imgList;


    private List<PhotoInfo> photos=new ArrayList<>();

    public List<PhotoInfo> getPhotos() {
        photos.clear();
        if(getImgList()!=null) {
            for (String url:getImgList()){
                PhotoInfo photoInfo=new PhotoInfo();
                photoInfo.setUrl(url);
                photos.add(photoInfo);
            }
        }
        return photos;
    }

    public void setPhotos(List<PhotoInfo> photos) {
        this.photos = photos;
    }

    public List<String> getImgList() {
        if(imgList==null){
            imgList=new ArrayList<>();
        }
        return imgList;
    }

    public String getJumpId() {
        return jumpId;
    }

    public void setJumpId(String jumpId) {
        this.jumpId = jumpId;
    }

    public int getFeedType() {
        return feedType;
    }

    public void setFeedType(int feedType) {
        this.feedType = feedType;
    }

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getHeadurl() {
        return headurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Long getPublishtime() {
        return publishtime;
    }

    public void setPublishtime(long publishtime) {
        this.publishtime = publishtime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public int getForwarding() {
        return forwarding;
    }

    public void setForwarding(int forwarding) {
        this.forwarding = forwarding;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public QuoteItem getQuoteItem() {
        return mQuoteItem;
    }

    public void setQuoteItem(QuoteItem quoteItem) {
        mQuoteItem = quoteItem;
    }

    public static class QuoteItem{
        @Expose
        @SerializedName("Id")
        private String id;
        @Expose
        @SerializedName("UserId")
        private String userId;
        @Expose
        @SerializedName("NickName")
        private String nickName;
        @Expose
        @SerializedName("Content")
        private String content;
        @Expose
        @SerializedName("ImgList")
        private List<PhotoInfo> photos; // 图片列表

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public List<PhotoInfo> getPhotos() {
            return photos;
        }

        public void setPhotos(List<PhotoInfo> photos) {
            this.photos = photos;
        }
    }
}
