package cn.feihutv.zhibofeihu.data.network.http.model.bangdan;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * <pre>
 *      author : liwen.chen on 2017/10/11 14:04.
 *      time   : 2017/10/11 14:04
 *      desc   : 拉取贡献榜和排行榜response
 *      version: 1.0
 * </pre>
 */

public class LoadContriRankListResponse extends BaseResponse {


    @Expose
    @SerializedName("Data")
    private List<LoadContriRankListResponseData> mDatas;

    public List<LoadContriRankListResponseData> getDatas() {
        return mDatas;
    }

    public void setDatas(List<LoadContriRankListResponseData> datas) {
        mDatas = datas;
    }

    @Override
    public String toString() {
        return "LoadContriRankListResponse{" +
                "mDatas=" + mDatas +
                '}';
    }

    public static class LoadContriRankListResponseData {
        /**
         * BroadcastType
         * HB : 40
         * HeadUrl : https://img.feihutv.cn//headIcon/11332503/59e562502a17e46d8a81598c.jpg
         * Level : 39
         * LiveStatus : false
         * NickName : 飞虎99099888
         * Rank : 1
         * UserId : 11332503
         * Vip : 0
         * VipExpired : true
         * isLiang : true
         * ShowId
         */

        @Expose
        @SerializedName("HB")
        private long hB;
        @Expose
        @SerializedName("BroadcastType")
        private int broadcastType;
        @Expose
        @SerializedName("HeadUrl")
        private String headUrl;
        @Expose
        @SerializedName("Level")
        private int level;
        @Expose
        @SerializedName("LiveStatus")
        private boolean liveStatus;
        @Expose
        @SerializedName("NickName")
        private String nickName;
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
        @SerializedName("ShowId")
        private String showId;

        public String getShowId() {
            return showId;
        }

        public void setShowId(String showId) {
            this.showId = showId;
        }

        public long gethB() {
            return hB;
        }

        public void sethB(long hB) {
            this.hB = hB;
        }

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public boolean isLiveStatus() {
            return liveStatus;
        }

        public void setLiveStatus(boolean liveStatus) {
            this.liveStatus = liveStatus;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
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
