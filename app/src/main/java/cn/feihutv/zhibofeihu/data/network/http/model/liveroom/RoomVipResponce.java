package cn.feihutv.zhibofeihu.data.network.http.model.liveroom;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * Created by huanghao on 2017/11/10.
 */

public class RoomVipResponce extends BaseResponse{

    @Expose
    @SerializedName("Data")
    private RoomVipData roomVipData;

    public RoomVipData getRoomVipData() {
        return roomVipData;
    }

    public void setRoomVipData(RoomVipData roomVipData) {
        this.roomVipData = roomVipData;
    }

    public static class RoomVipData{
        @Expose
        @SerializedName("NextOffset")
        private int NextOffset;
        @Expose
        @SerializedName("List")
        private List<RoomVipModel> roomVipModelList;

        public int getNextOffset() {
            return NextOffset;
        }

        public void setNextOffset(int nextOffset) {
            NextOffset = nextOffset;
        }

        public List<RoomVipModel> getRoomVipModelList() {
            return roomVipModelList;
        }

        public void setRoomVipModelList(List<RoomVipModel> roomVipModelList) {
            this.roomVipModelList = roomVipModelList;
        }
    }

    public static class RoomVipModel{
        @Expose
        @SerializedName("UserId")
        private String UserId;
        @Expose
        @SerializedName("IsMgr")
        private boolean IsMgr ;
        @Expose
        @SerializedName("NickName")
        private String NickName ;
        @Expose
        @SerializedName("HeadUrl")
        private String HeadUrl;
        @Expose
        @SerializedName("Level")
        private int Level ;
        @Expose
        @SerializedName("Vip")
        private int Vip  ;
        @Expose
        @SerializedName("VipExpired")
        private boolean VipExpired  ;
        @Expose
        @SerializedName("GuardType")
        private int GuardType  ;
        @Expose
        @SerializedName("Friendliness")
        private int Friendliness   ;
        @Expose
        @SerializedName("GuardExpired")
        private boolean GuardExpired   ;
        @Expose
        @SerializedName("IsLiang")
        private boolean IsLiang    ;

        public String getUserId() {
            return UserId;
        }

        public void setUserId(String userId) {
            UserId = userId;
        }

        public boolean isMgr() {
            return IsMgr;
        }

        public void setMgr(boolean mgr) {
            IsMgr = mgr;
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

        public int getGuardType() {
            return GuardType;
        }

        public void setGuardType(int guardType) {
            GuardType = guardType;
        }

        public int getFriendliness() {
            return Friendliness;
        }

        public void setFriendliness(int friendliness) {
            Friendliness = friendliness;
        }

        public boolean isGuardExpired() {
            return GuardExpired;
        }

        public void setGuardExpired(boolean guardExpired) {
            GuardExpired = guardExpired;
        }

        public boolean isLiang() {
            return IsLiang;
        }

        public void setLiang(boolean liang) {
            IsLiang = liang;
        }
    }
}
