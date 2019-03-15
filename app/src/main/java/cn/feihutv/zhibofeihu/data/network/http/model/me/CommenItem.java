package cn.feihutv.zhibofeihu.data.network.http.model.me;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/22.
 */

public class CommenItem implements Serializable {
    //    "Id":"",
//            "UserId": "test1",//评论者id
//            "NickName": "",//评论者昵称
//            "HeadUrl": "",//评论者头像
//            "ReplyTo": {//无回复人则字段不存在
//        "UserId": "", //被回复的人id
//                "NickName": ""//被回复的人昵称
//    },
//            "Content": "",//评论的内容
//            "ReplyTime": 1491560803//评论发表的时间

    @Expose
    @SerializedName("type")
    public String type;

    @Expose
    @SerializedName("Id")
    public String id;

    @Expose
    @SerializedName("UserId")
    public String userId;

    @Expose
    @SerializedName("NickName")
    public String nickName;

    @Expose
    @SerializedName("HeadUrl")
    private String headUrl;

    @Expose
    @SerializedName("Content")
    private String content;

    @Expose
    @SerializedName("ReplyTo")
    private ReplyToItem replyTo;

    @Expose
    @SerializedName("ReplyTime")
    private long replyTime;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ReplyToItem getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(ReplyToItem replyTo) {
        this.replyTo = replyTo;
    }

    public long getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(long replyTime) {
        this.replyTime = replyTime;
    }

    public static class ReplyToItem {
        @Expose
        @SerializedName("UserId")
        private String userId;
        @Expose
        @SerializedName("NickName")
        private String nickName;

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
    }
}
