package cn.feihutv.zhibofeihu.data.network.http.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * Created by huanghao on 2017/10/25.
 */

public class LoadMsgListResponce extends BaseResponse{

    @Expose
    @SerializedName("Data")
    private List<LoadMsgListData> loadMsgListData;

    public List<LoadMsgListData> getLoadMsgListData() {
        return loadMsgListData;
    }

    public void setLoadMsgListData(List<LoadMsgListData> loadMsgListData) {
        this.loadMsgListData = loadMsgListData;
    }


    public static class LoadMsgListData{
        private String TradeId; // 交易id
        private String Content;
        private String HeadUrl;
        private String Level;
        private String NickName;
        private int Time;
        private String Type;
        private String UserId;
        private String AccountId;
        private String Action;
        private int Amount;
        @Expose
        @SerializedName("Cnt")
        private int cnt;
        @Expose
        @SerializedName("ExpireAt")
        private int expireAt;
        @Expose
        @SerializedName("Gift")
        private int gift;
        @Expose
        @SerializedName("FeedId")
        private String feedId;
        @Expose
        @SerializedName("FeedImg")
        private String feedImg;
        @Expose
        @SerializedName("ReplyContent")
        private String replyContent; // 评论内容

        @Expose
        @SerializedName("FeedMsgType")
        private int feedMsgType;  // 动态消息类型 1点赞 2评论 3转发

        @Expose
        @SerializedName("FeedType")
        private int feedType;  // 动态类型 1普通 2MV

        @Expose
        @SerializedName("FeedContent")
        private String feedContent;  // 普通动态的内容（无图时）


        public String getTradeId() {
            return TradeId;
        }

        public void setTradeId(String tradeId) {
            TradeId = tradeId;
        }

        public String getContent() {
            return Content;
        }

        public void setContent(String content) {
            Content = content;
        }

        public String getHeadUrl() {
            return HeadUrl;
        }

        public void setHeadUrl(String headUrl) {
            HeadUrl = headUrl;
        }

        public String getLevel() {
            return Level;
        }

        public void setLevel(String level) {
            Level = level;
        }

        public String getNickName() {
            return NickName;
        }

        public void setNickName(String nickName) {
            NickName = nickName;
        }

        public int getTime() {
            return Time;
        }

        public void setTime(int time) {
            Time = time;
        }

        public String getType() {
            return Type;
        }

        public void setType(String type) {
            Type = type;
        }

        public String getUserId() {
            return UserId;
        }

        public void setUserId(String userId) {
            UserId = userId;
        }

        public String getAccountId() {
            return AccountId;
        }

        public void setAccountId(String accountId) {
            AccountId = accountId;
        }

        public String getAction() {
            return Action;
        }

        public void setAction(String action) {
            Action = action;
        }

        public int getAmount() {
            return Amount;
        }

        public void setAmount(int amount) {
            Amount = amount;
        }

        public int getCnt() {
            return cnt;
        }

        public void setCnt(int cnt) {
            this.cnt = cnt;
        }

        public int getExpireAt() {
            return expireAt;
        }

        public void setExpireAt(int expireAt) {
            this.expireAt = expireAt;
        }

        public int getGift() {
            return gift;
        }

        public void setGift(int gift) {
            this.gift = gift;
        }

        public String getFeedId() {
            return feedId;
        }

        public void setFeedId(String feedId) {
            this.feedId = feedId;
        }

        public String getFeedImg() {
            return feedImg;
        }

        public void setFeedImg(String feedImg) {
            this.feedImg = feedImg;
        }

        public String getReplyContent() {
            return replyContent;
        }

        public void setReplyContent(String replyContent) {
            this.replyContent = replyContent;
        }

        public String getFeedContent() {
            return feedContent;
        }

        public void setFeedContent(String feedContent) {
            this.feedContent = feedContent;
        }

        public int getFeedMsgType() {
            return feedMsgType;
        }

        public void setFeedMsgType(int feedMsgType) {
            this.feedMsgType = feedMsgType;
        }

        public int getFeedType() {
            return feedType;
        }

        public void setFeedType(int feedType) {
            this.feedType = feedType;
        }
    }
}
