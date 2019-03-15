package cn.feihutv.zhibofeihu.data.network.http.model.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/30 13:59
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class SearchResponse extends BaseResponse {


    @Expose
    @SerializedName("Data")
    private List<SearchResponseData> mSearchResponseDatas;

    public List<SearchResponseData> getSearchResponseDatas() {
        return mSearchResponseDatas;
    }

    public void setSearchResponseDatas(List<SearchResponseData> searchResponseDatas) {
        mSearchResponseDatas = searchResponseDatas;
    }

    public static class SearchResponseData {
        /**
         * BroadcastType : 0
         * Followed : false
         * HeadUrl : https://q.qlogo.cn/qqapp/1106077402/369ECE76CBC4F9133F17A582C8D9194C/100
         * IsMaster : false
         * Level : 2
         * NickName : ★→★追忆
         * Signature :
         * UserId : 11336202
         */

        @Expose
        @SerializedName("BroadcastType")
        private int broadcastType;
        @Expose
        @SerializedName("Followed")
        private boolean followed;
        @Expose
        @SerializedName("HeadUrl")
        private String headUrl;
        @Expose
        @SerializedName("IsMaster")
        private boolean isMaster;
        @Expose
        @SerializedName("Level")
        private int level;
        @Expose
        @SerializedName("NickName")
        private String nickName;
        @Expose
        @SerializedName("Signature")
        private String signature;
        @Expose
        @SerializedName("UserId")
        private String userId;

        public int getBroadcastType() {
            return broadcastType;
        }

        public void setBroadcastType(int broadcastType) {
            this.broadcastType = broadcastType;
        }

        public boolean isFollowed() {
            return followed;
        }

        public void setFollowed(boolean followed) {
            this.followed = followed;
        }

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }

        public boolean isMaster() {
            return isMaster;
        }

        public void setMaster(boolean master) {
            isMaster = master;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}

