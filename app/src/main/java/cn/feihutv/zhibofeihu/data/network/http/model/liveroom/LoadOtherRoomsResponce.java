package cn.feihutv.zhibofeihu.data.network.http.model.liveroom;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * Created by huanghao on 2017/10/23.
 */

public class LoadOtherRoomsResponce extends BaseResponse{

    @Expose
    @SerializedName("Data")
    private List<OtherRoomData> otherRoomDatas;

    public List<OtherRoomData> getOtherRoomDatas() {
        return otherRoomDatas;
    }

    public void setOtherRoomDatas(List<OtherRoomData> otherRoomDatas) {
        this.otherRoomDatas = otherRoomDatas;
    }

    public static class OtherRoomData{
        @Expose
        @SerializedName("RoomId")
        private String RoomId;
        @Expose
        @SerializedName("NickName")
        private String NickName ;
        @Expose
        @SerializedName("RoomName")
        private String RoomName ;
        @Expose
        @SerializedName("HeadUrl")
        private String HeadUrl ;
        @Expose
        @SerializedName("LiveCover")
        private String LiveCover;
        @Expose
        @SerializedName("Location")
        private String Location ;
        @Expose
        @SerializedName("RoomStatus")
        private boolean RoomStatus  ;
        @Expose
        @SerializedName("BroadcastType")
        private int  BroadcastType ;
        @Expose
        @SerializedName("OnlineUserCnt")
        private int OnlineUserCnt  ;
        @Expose
        @SerializedName("GameOwner")
        private String GameOwner   ;

        public String getRoomId() {
            return RoomId;
        }

        public void setRoomId(String roomId) {
            RoomId = roomId;
        }

        public String getNickName() {
            return NickName;
        }

        public void setNickName(String nickName) {
            NickName = nickName;
        }

        public String getRoomName() {
            return RoomName;
        }

        public void setRoomName(String roomName) {
            RoomName = roomName;
        }

        public String getHeadUrl() {
            return HeadUrl;
        }

        public void setHeadUrl(String headUrl) {
            HeadUrl = headUrl;
        }

        public String getLiveCover() {
            return LiveCover;
        }

        public void setLiveCover(String liveCover) {
            LiveCover = liveCover;
        }

        public String getLocation() {
            return Location;
        }

        public void setLocation(String location) {
            Location = location;
        }

        public boolean isRoomStatus() {
            return RoomStatus;
        }

        public void setRoomStatus(boolean roomStatus) {
            RoomStatus = roomStatus;
        }

        public int getBroadcastType() {
            return BroadcastType;
        }

        public void setBroadcastType(int broadcastType) {
            BroadcastType = broadcastType;
        }

        public int getOnlineUserCnt() {
            return OnlineUserCnt;
        }

        public void setOnlineUserCnt(int onlineUserCnt) {
            OnlineUserCnt = onlineUserCnt;
        }

        public String getGameOwner() {
            return GameOwner;
        }

        public void setGameOwner(String gameOwner) {
            GameOwner = gameOwner;
        }
    }
}
