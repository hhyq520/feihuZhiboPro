package cn.feihutv.zhibofeihu.data.network.http.model.bangdan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/10/27 18:59
 *      desc   : 守护榜
 *      version: 1.0
 * </pre>
 */

public class GetGuardRankResponse extends BaseResponse {


    @Expose
    @SerializedName("Data")
    private List<GetGuardRankResponseData> mResponseDatas;

    public List<GetGuardRankResponseData> getResponseDatas() {
        return mResponseDatas;
    }

    public void setResponseDatas(List<GetGuardRankResponseData> responseDatas) {
        mResponseDatas = responseDatas;
    }

    public static class GetGuardRankResponseData {
        /**
         * Avatar : https://img.feihutv.cn/uploadHtml/images/roomBg.png
         * GuardCnt : 1
         * LiveStatus : false
         * Nickname : 东南西北中发白
         * Rank : 1
         * UserId : 11335622
         * "Vip": 0,
         * "VipExpired": true,
         * "isLiang": false
         * Level
         */


        @Expose
        @SerializedName("Avatar")
        private String avatar;
        @Expose
        @SerializedName("GuardCnt")
        private int guardCnt;
        @Expose
        @SerializedName("LiveStatus")
        private boolean liveStatus;
        @Expose
        @SerializedName("Nickname")
        private String nickname;
        @Expose
        @SerializedName("Rank")
        private int rank;
        @Expose
        @SerializedName("UserId")
        private String userId;

        @Expose
        @SerializedName("Vip")
        private int vip;

        @Expose
        @SerializedName("VipExpired")
        private boolean vipExpired;

        @Expose
        @SerializedName("IsLiang")
        private boolean isLiang;

        @Expose
        @SerializedName("BroadcastType")
        private int broadcastType;

        @Expose
        @SerializedName("Level")
        private int level;

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getGuardCnt() {
            return guardCnt;
        }

        public void setGuardCnt(int guardCnt) {
            this.guardCnt = guardCnt;
        }

        public boolean isLiveStatus() {
            return liveStatus;
        }

        public void setLiveStatus(boolean liveStatus) {
            this.liveStatus = liveStatus;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getVip() {
            return vip;
        }

        public void setVip(int vip) {
            this.vip = vip;
        }

        public boolean isVipExpired() {
            return vipExpired;
        }

        public void setVipExpired(boolean vipExpired) {
            this.vipExpired = vipExpired;
        }

        public boolean isLiang() {
            return isLiang;
        }

        public void setLiang(boolean liang) {
            isLiang = liang;
        }

        public int getBroadcastType() {
            return broadcastType;
        }

        public void setBroadcastType(int broadcastType) {
            this.broadcastType = broadcastType;
        }
    }
}
