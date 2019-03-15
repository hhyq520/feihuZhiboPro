package cn.feihutv.zhibofeihu.data.network.http.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * Created by huanghao on 2017/10/25.
 */

public class GetFriendsResponce extends BaseResponse{
    @Expose
    @SerializedName("Data")
    private List<GetFriendsData> getFriendsDatas;

    public List<GetFriendsData> getGetFriendsDatas() {
        return getFriendsDatas;
    }

    public void setGetFriendsDatas(List<GetFriendsData> getFriendsDatas) {
        this.getFriendsDatas = getFriendsDatas;
    }

    public static class GetFriendsData{
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
        @SerializedName("OnlineStatus")
        private boolean OnlineStatus;
        @Expose
        @SerializedName("Vip")
        private int Vip ;
        @Expose
        @SerializedName("VipExpired")
        private boolean VipExpired ;
        @Expose
        @SerializedName("IsLiang")
        private boolean IsLiang ;

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

        public boolean isOnlineStatus() {
            return OnlineStatus;
        }

        public void setOnlineStatus(boolean onlineStatus) {
            OnlineStatus = onlineStatus;
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
            return IsLiang;
        }

        public void setLiang(boolean liang) {
            IsLiang = liang;
        }
    }
}
