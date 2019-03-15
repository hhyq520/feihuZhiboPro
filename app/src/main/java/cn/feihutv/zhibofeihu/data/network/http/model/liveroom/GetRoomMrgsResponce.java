package cn.feihutv.zhibofeihu.data.network.http.model.liveroom;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * Created by huanghao on 2017/10/18.
 */

public class GetRoomMrgsResponce extends BaseResponse {

    @Expose
    @SerializedName("Data")
    private List<RoomMrgData> roomMrgDataList;

    public List<RoomMrgData> getRoomMrgDataList() {
        return roomMrgDataList;
    }

    public void setRoomMrgDataList(List<RoomMrgData> roomMrgDataList) {
        this.roomMrgDataList = roomMrgDataList;
    }


    public static class RoomMrgData {
        @Expose
        @SerializedName("UserId")
        private String UserId;
        @Expose
        @SerializedName("NickName")
        private String NickName;
        @Expose
        @SerializedName("HeadUrl")
        private String HeadUrl;
        @Expose
        @SerializedName("Level")
        private int Level;

        @Expose
        @SerializedName("ShowId")
        private String showId;

        @Expose
        @SerializedName("VipExpired")
        private boolean vipExpired;
        @Expose
        @SerializedName("GuardType")
        private int GuardType ;
        @Expose
        @SerializedName("GuardExpired")
        private boolean GuardExpired ;

        @Expose
        @SerializedName("IsLiang")
        private boolean isLiang;

        @Expose
        @SerializedName("Vip")
        private int vip;

        public String getUserId() {
            return UserId;
        }

        public void setUserId(String userId) {
            UserId = userId;
        }

        public String getNickName() {
            return NickName;
        }

        public void setNickName(String nickName) {
            NickName = nickName;
        }

        public String getHeadUrl() {
            return HeadUrl;
        }

        public void setHeadUrl(String headUrl) {
            HeadUrl = headUrl;
        }

        public int getLevel() {
            return Level;
        }

        public void setLevel(int level) {
            Level = level;
        }

        public String getShowId() {
            return showId;
        }

        public void setShowId(String showId) {
            this.showId = showId;
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

        public int getVip() {
            return vip;
        }

        public void setVip(int vip) {
            this.vip = vip;
        }

        public int getGuardType() {
            return GuardType;
        }

        public void setGuardType(int guardType) {
            GuardType = guardType;
        }

        public boolean isGuardExpired() {
            return GuardExpired;
        }

        public void setGuardExpired(boolean guardExpired) {
            GuardExpired = guardExpired;
        }
    }
}
