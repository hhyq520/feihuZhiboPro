package cn.feihutv.zhibofeihu.data.network.http.model.liveroom;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * Created by huanghao on 2017/10/17.
 */

public class LoadRoomMemberResponce extends BaseResponse {
    @Expose
    @SerializedName("Data")
    private RoomMemberData roomMemberData;

    public RoomMemberData getRoomMemberData() {
        return roomMemberData;
    }

    public void setRoomMemberData(RoomMemberData roomMemberData) {
        this.roomMemberData = roomMemberData;
    }

    public static class RoomMemberData{
        @Expose
        @SerializedName("NextOffset")
        private int NextOffset;
        @Expose
        @SerializedName("NextType")
        private String NextType;
        @Expose
        @SerializedName("List")
        private List<MemberData> memberDatas;

        public int getNextOffset() {
            return NextOffset;
        }

        public void setNextOffset(int nextOffset) {
            NextOffset = nextOffset;
        }

        public String getNextType() {
            return NextType;
        }

        public void setNextType(String nextType) {
            NextType = nextType;
        }

        public List<MemberData> getMemberDatas() {
            return memberDatas;
        }

        public void setMemberDatas(List<MemberData> memberDatas) {
            this.memberDatas = memberDatas;
        }
    }

    public static class MemberData{
        @Expose
        @SerializedName("UserId")
        private String UserId;
        @Expose
        @SerializedName("IsMgr")
        private boolean IsMgr ;
        @Expose
        @SerializedName("NickName")
        private String NickName;
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
