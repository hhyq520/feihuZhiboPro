package cn.feihutv.zhibofeihu.data.network.http.model.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * Created by clw on 2017/10/22.
 */

public class RecommendResponse extends BaseResponse {



    @Expose
    @SerializedName("Data")
    private List<RecommendResponseData> mResponseDatas;

    public List<RecommendResponseData> getResponseDatas() {
        return mResponseDatas;
    }

    public void setResponseDatas(List<RecommendResponseData> responseDatas) {
        mResponseDatas = responseDatas;
    }

    public static class RecommendResponseData {
        /**
         * BroadcastType : 2
         * GameOwner : none
         * HeadUrl : http://img.feihutv.cn//headIcon/11332511/59647cf98520875f1395468a.png
         * LiveCover : http://img.feihutv.cn//headIcon/11332511/59642d692a17e474709d8f46.png
         * Location :
         * NickName : 追 忆
         * OnlineUserCnt : 0
         * RoomId : 11332511
         * RoomName : 测试
         * RoomStatus : true
         */

        @Expose
        @SerializedName("BroadcastType")
        private int broadcastType;
        @Expose
        @SerializedName("GameOwner")
        private String gameOwner;
        @Expose
        @SerializedName("HeadUrl")
        private String headUrl;
        @Expose
        @SerializedName("LiveCover")
        private String liveCover;
        @Expose
        @SerializedName("Location")
        private String location;
        @Expose
        @SerializedName("NickName")
        private String nickName;
        @Expose
        @SerializedName("OnlineUserCnt")
        private String onlineUserCnt;
        @Expose
        @SerializedName("RoomId")
        private String roomId;
        @Expose
        @SerializedName("RoomName")
        private String roomName;
        @Expose
        @SerializedName("RoomStatus")
        private boolean roomStatus;

        public int getBroadcastType() {
            return broadcastType;
        }

        public void setBroadcastType(int broadcastType) {
            this.broadcastType = broadcastType;
        }

        public String getGameOwner() {
            return gameOwner;
        }

        public void setGameOwner(String gameOwner) {
            this.gameOwner = gameOwner;
        }

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }

        public String getLiveCover() {
            return liveCover;
        }

        public void setLiveCover(String liveCover) {
            this.liveCover = liveCover;
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

        public String getOnlineUserCnt() {
            return onlineUserCnt;
        }

        public void setOnlineUserCnt(String onlineUserCnt) {
            this.onlineUserCnt = onlineUserCnt;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getRoomName() {
            return roomName;
        }

        public void setRoomName(String roomName) {
            this.roomName = roomName;
        }

        public boolean isRoomStatus() {
            return roomStatus;
        }

        public void setRoomStatus(boolean roomStatus) {
            this.roomStatus = roomStatus;
        }
    }
}
