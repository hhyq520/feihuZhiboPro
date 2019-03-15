package cn.feihutv.zhibofeihu.data.network.http.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * Created by huanghao on 2017/10/17.
 */

public class LoadRoomContriResponce extends BaseResponse{
    @Expose
    @SerializedName("Data")
    private List<RoomContriData> roomContriDataList;

    public List<RoomContriData> getRoomContriDataList() {
        return roomContriDataList;
    }

    public void setRoomContriDataList(List<RoomContriData> roomContriDataList) {
        this.roomContriDataList = roomContriDataList;
    }


    public static class RoomContriData{
        @Expose
        @SerializedName("GuardExpired")
        private boolean GuardExpired ;
        @Expose
        @SerializedName("GuardType")
        private int GuardType ;
        @Expose
        @SerializedName("Vip")
        private int Vip ;
        @Expose
        @SerializedName("VipExpired")
        private boolean VipExpired ;
        @Expose
        @SerializedName("IsLiang")
        private boolean isLiang ;
        @Expose
        @SerializedName("UserId")
        private String UserId ;
        @Expose
        @SerializedName("Rank")
        private int Rank  ;
        @Expose
        @SerializedName("HB")
        private long HB ;
        @Expose
        @SerializedName("NickName")
        private String NickName ;
        @Expose
        @SerializedName("HeadUrl")
        private String HeadUrl ;
        @Expose
        @SerializedName("Level")
        private int Level ;

        public String getUserId() {
            return UserId;
        }

        public void setUserId(String userId) {
            UserId = userId;
        }

        public int getRank() {
            return Rank;
        }

        public void setRank(int rank) {
            Rank = rank;
        }

        public long getHB() {
            return HB;
        }

        public void setHB(long HB) {
            this.HB = HB;
        }


        public String getNickName() {
            return NickName;
        }

        public void setNickName(String nickName) {
            NickName = nickName;
        }

        public int getLevel() {
            return Level;
        }

        public void setLevel(int level) {
            Level = level;
        }

        public String getHeadUrl() {
            return HeadUrl;
        }

        public void setHeadUrl(String headUrl) {
            HeadUrl = headUrl;
        }

        public boolean isGuardExpired() {
            return GuardExpired;
        }

        public void setGuardExpired(boolean guardExpired) {
            GuardExpired = guardExpired;
        }

        public int getGuardType() {
            return GuardType;
        }

        public void setGuardType(int guardType) {
            GuardType = guardType;
        }

        public int getVip() {
            return Vip;
        }

        public void setVip(int vip) {
            Vip = vip;
        }

        public boolean isVipExpired() {
            return VipExpired;
        }

        public void setVipExpired(boolean vipExpired) {
            VipExpired = vipExpired;
        }

        public boolean isLiang() {
            return isLiang;
        }

        public void setLiang(boolean liang) {
            isLiang = liang;
        }
    }
}
