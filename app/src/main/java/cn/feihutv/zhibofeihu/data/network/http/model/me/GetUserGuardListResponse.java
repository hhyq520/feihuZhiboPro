package cn.feihutv.zhibofeihu.data.network.http.model.me;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/7 14:35
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class GetUserGuardListResponse extends BaseResponse {


    @Expose
    @SerializedName("Data")
    private List<GetUserGuardListResponseData> mGetUserGuardListResponseDatas;

    public List<GetUserGuardListResponseData> getGetUserGuardListResponseDatas() {
        return mGetUserGuardListResponseDatas;
    }

    public void setGetUserGuardListResponseDatas(List<GetUserGuardListResponseData> getUserGuardListResponseDatas) {
        mGetUserGuardListResponseDatas = getUserGuardListResponseDatas;
    }

    public static class GetUserGuardListResponseData {
        /**
         * Avatar : https://img.feihutv.cn/uploadHtml/images/roomBg.png
         * Level : 29
         * Nickname : 周润发
         * Rank : 1
         * Time : 2590784
         * UserId : 11335652
         * BroadcastType : 1主机 2手机
         */

        @Expose
        @SerializedName("Avatar")
        private String avatar;
        @Expose
        @SerializedName("Level")
        private int level;
        @Expose
        @SerializedName("Nickname")
        private String nickname;
        @Expose
        @SerializedName("Rank")
        private int rank;
        @Expose
        @SerializedName("Time")
        private double time;
        @Expose
        @SerializedName("UserId")
        private String userId;

        @Expose
        @SerializedName("BroadcastType")
        private int broadcastType;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
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

        public double getTime() {
            return time;
        }

        public void setTime(double time) {
            this.time = time;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getBroadcastType() {
            return broadcastType;
        }

        public void setBroadcastType(int broadcastType) {
            this.broadcastType = broadcastType;
        }
    }
}
