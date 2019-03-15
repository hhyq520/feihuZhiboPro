package cn.feihutv.zhibofeihu.data.network.http.model.me;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/10/26
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class LoadFeedDetailResponse{





    /**
     * Data : {"Comments":0,"Content":"测试","FeedType":1,"Forwarding":0,"HeadUrl":"https://img.feihutv.cn//headIcon/11332481/59f9bc852a17e445ccd52d15.jpg","Id":"5a1bbde2852087446d397b3b","ImgList":["https://img.feihutv.cn//feed/11332481/5a1bbddd2a17e44021234abf.jpg","https://img.feihutv.cn//feed/11332481/5a1bbdde852087446d397b37.jpg","https://img.feihutv.cn//feed/11332481/5a1bbdde2a17e44021234ac0.jpg","https://img.feihutv.cn//feed/11332481/5a1bbdde852087446d397b38.jpg","https://img.feihutv.cn//feed/11332481/5a1bbddf2a17e44021234ac1.jpg","https://img.feihutv.cn//feed/11332481/5a1bbddf852087446d397b39.jpg","https://img.feihutv.cn//feed/11332481/5a1bbddf852087446d397b3a.jpg","https://img.feihutv.cn//feed/11332481/5a1bbddf2a17e44021234ac2.jpg","https://img.feihutv.cn//feed/11332481/5a1bbde02a17e44021234ac3.jpg"],"JumpId":0,"Liked":true,"Likes":1,"Location":"中国.深圳","NickName":"追 忆","PublishTime":1511767522,"UserId":"11332481"}
     */

    @Expose
    @SerializedName("Data")
    private LoadFeedDetailResponseData mDetailResponseData;
    /**
     * Code : 4301
     * ErrExtMsg : {}
     * ErrMsg : FeedNotFound
     */

    @Expose
    @SerializedName("Code")
    private int code;
    @Expose
    @SerializedName("ErrExtMsg")
    private ErrExtMsgBean errExtMsg;
    @Expose
    @SerializedName("ErrMsg")
    private String errMsg;

    public LoadFeedDetailResponseData getDetailResponseData() {
        return mDetailResponseData;
    }

    public void setDetailResponseData(LoadFeedDetailResponseData detailResponseData) {
        mDetailResponseData = detailResponseData;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ErrExtMsgBean getErrExtMsg() {
        return errExtMsg;
    }

    public void setErrExtMsg(ErrExtMsgBean errExtMsg) {
        this.errExtMsg = errExtMsg;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public static class LoadFeedDetailResponseData {
        /**
         * Comments : 0
         * Content : 测试
         * FeedType : 1
         * Forwarding : 0
         * HeadUrl : https://img.feihutv.cn//headIcon/11332481/59f9bc852a17e445ccd52d15.jpg
         * Id : 5a1bbde2852087446d397b3b
         * ImgList : ["https://img.feihutv.cn//feed/11332481/5a1bbddd2a17e44021234abf.jpg","https://img.feihutv.cn//feed/11332481/5a1bbdde852087446d397b37.jpg","https://img.feihutv.cn//feed/11332481/5a1bbdde2a17e44021234ac0.jpg","https://img.feihutv.cn//feed/11332481/5a1bbdde852087446d397b38.jpg","https://img.feihutv.cn//feed/11332481/5a1bbddf2a17e44021234ac1.jpg","https://img.feihutv.cn//feed/11332481/5a1bbddf852087446d397b39.jpg","https://img.feihutv.cn//feed/11332481/5a1bbddf852087446d397b3a.jpg","https://img.feihutv.cn//feed/11332481/5a1bbddf2a17e44021234ac2.jpg","https://img.feihutv.cn//feed/11332481/5a1bbde02a17e44021234ac3.jpg"]
         * JumpId : 0
         * Liked : true
         * Likes : 1
         * Location : 中国.深圳
         * NickName : 追 忆
         * PublishTime : 1511767522
         * UserId : 11332481
         */

        @Expose
        @SerializedName("Comments")
        private int comments;
        @Expose
        @SerializedName("Content")
        private String content;
        @Expose
        @SerializedName("FeedType")
        private int feedType;
        @Expose
        @SerializedName("Forwarding")
        private int forwarding;
        @Expose
        @SerializedName("HeadUrl")
        private String headUrl;
        @Expose
        @SerializedName("Id")
        private String id;
        @Expose
        @SerializedName("JumpId")
        private int jumpId;
        @Expose
        @SerializedName("Liked")
        private boolean liked;
        @Expose
        @SerializedName("Likes")
        private int likes;
        @Expose
        @SerializedName("Location")
        private String location;
        @Expose
        @SerializedName("NickName")
        private String nickName;
        @Expose
        @SerializedName("PublishTime")
        private int publishTime;
        @Expose
        @SerializedName("UserId")
        private String userId;
        @Expose
        @SerializedName("ImgList")
        private List<String> imgList;

        private List<PhotoInfo> photos = new ArrayList<>();

        public List<PhotoInfo> getPhotos() {
            photos.clear();
            if (getImgList() != null) {
                for (String url : getImgList()) {
                    PhotoInfo photoInfo = new PhotoInfo();
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
            if (imgList == null) {
                imgList = new ArrayList<>();
            }
            return imgList;
        }

        public int getComments() {
            return comments;
        }

        public void setComments(int comments) {
            this.comments = comments;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getFeedType() {
            return feedType;
        }

        public void setFeedType(int feedType) {
            this.feedType = feedType;
        }

        public int getForwarding() {
            return forwarding;
        }

        public void setForwarding(int forwarding) {
            this.forwarding = forwarding;
        }

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getJumpId() {
            return jumpId;
        }

        public void setJumpId(int jumpId) {
            this.jumpId = jumpId;
        }

        public boolean isLiked() {
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

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public int getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(int publishTime) {
            this.publishTime = publishTime;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }


        public void setImgList(List<String> imgList) {
            this.imgList = imgList;
        }
    }

    public static class ErrExtMsgBean {

    }

}
