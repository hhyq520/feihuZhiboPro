package cn.feihutv.zhibofeihu.data.network.http.model.mv;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
public class GetMVCommentListResponse extends BaseResponse {

    @Expose
    @SerializedName("Data")
    private  GetMVCommentListData mGetMVCommentListData;

    public GetMVCommentListData getGetMVCommentListData() {
        return mGetMVCommentListData;
    }

    public void setGetMVCommentListData(GetMVCommentListData getMVCommentListData) {
        mGetMVCommentListData = getMVCommentListData;
    }

    public static class GetMVCommentListData{

        @Expose
        @SerializedName("NextOffset")
        private  String  nextOffset;//加载条数（最多20）

        @Expose
        @SerializedName("List")
        private List<GetMVCommentList> mGetMVCommentLists;

        public String getNextOffset() {
            return nextOffset;
        }

        public void setNextOffset(String nextOffset) {
            this.nextOffset = nextOffset;
        }

        public List<GetMVCommentList> getGetMVCommentLists() {
            return mGetMVCommentLists;
        }

        public void setGetMVCommentLists(List<GetMVCommentList> getMVCommentLists) {
            mGetMVCommentLists = getMVCommentLists;
        }
    }

    public static class GetMVCommentList{

        @Expose
        @SerializedName("Uid")
        private  String  Uid;// "29",                             //评论者uid

        @Expose
        @SerializedName("Nickname")
        private  String  nickname;// "admin",            //评论者昵称

        @Expose
        @SerializedName("Avatar")
        private  String  avatar;// "https://img.fl.png", //评论者的头像

        @Expose
        @SerializedName("CommentId")
        private  String  commentId;// 1,

        @Expose
        @SerializedName("Content")
        private  String  content;// "test",      //评论内容

        @Expose
        @SerializedName("Liked")
        private  boolean  liked;// false,           //是否已赞评论

        @Expose
        @SerializedName("Likes")
        private   int  likes;// 0,                 //评论获得的赞数

        @Expose
        @SerializedName("T")
        private  Long  t;// 1510390681,     //评论时间戳 秒

        @Expose
        @SerializedName("Vip")
        private  String  vip;// 3,                    //评论者vip等级

        @Expose
        @SerializedName("VipExpired")
        private  String   vipExpired;// false, //评论者vip是否过期

        @Expose
        @SerializedName("replyNickname")
        private  String  replyNickname;// "", //@的用户昵称

        @Expose
        @SerializedName("replyUid")
        private  String  replyUid;// "0"           //@的用户uid

        public String getUid() {
            return Uid;
        }

        public void setUid(String uid) {
            Uid = uid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getCommentId() {
            return commentId;
        }

        public void setCommentId(String commentId) {
            this.commentId = commentId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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

        public Long getT() {
            return t;
        }

        public void setT(Long t) {
            this.t = t;
        }

        public String getVip() {
            return vip;
        }

        public void setVip(String vip) {
            this.vip = vip;
        }

        public String getVipExpired() {
            return vipExpired;
        }

        public void setVipExpired(String vipExpired) {
            this.vipExpired = vipExpired;
        }

        public String getReplyNickname() {
            return replyNickname;
        }

        public void setReplyNickname(String replyNickname) {
            this.replyNickname = replyNickname;
        }

        public String getReplyUid() {
            return replyUid;
        }

        public void setReplyUid(String replyUid) {
            this.replyUid = replyUid;
        }
    }

}
