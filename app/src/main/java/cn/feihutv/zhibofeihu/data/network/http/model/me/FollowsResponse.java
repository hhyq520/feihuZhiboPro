package cn.feihutv.zhibofeihu.data.network.http.model.me;

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
 *      @author : liwen.chen
 *      time   : 2017/10/31 21:02
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class FollowsResponse  extends BaseResponse {


    @Expose
    @SerializedName("Data")
    private List<FollowsResponseData> mFollowsResponseDatas;


    public List<FollowsResponseData> getFollowsResponseDatas() {
        return mFollowsResponseDatas;
    }

    public void setFollowsResponseDatas(List<FollowsResponseData> followsResponseDatas) {
        mFollowsResponseDatas = followsResponseDatas;
    }

    public static class FollowsResponseData {
        /**
         * UserId : 11332483
         * NickName :  妖孽〃那里逃 °
         * HeadUrl : http://wx.qlogo.cn/mmopen/iaiauObkqHk3AQpSicg59oVDxaqyRKS52HgJvWOamUKp6w42HuLkdeBoKg2aNOJYJ6m9dv0OkGPCfWRozn9iaBPMXMWdndytGyPD/0
         * Gender : 1
         * Level : 1
         * RoomStatus : false
         * IsFollower : true
         * IsLiang:false
         * Vip : 1
         * VipExpired : false
         */

        @Expose
        @SerializedName("UserId")
        private String userId;
        @Expose
        @SerializedName("NickName")
        private String nickName;
        @Expose
        @SerializedName("HeadUrl")
        private String headUrl;
        @Expose
        @SerializedName("Gender")
        private int gender;
        @Expose
        @SerializedName("Level")
        private int level;
        @Expose
        @SerializedName("RoomStatus")
        private boolean roomStatus;
        @Expose
        @SerializedName("IsFollower")
        private boolean isFollower;

        @Expose
        @SerializedName("IsLiang")
        private boolean isLiang;

        @Expose
        @SerializedName("Vip")
        private int Vip;

        @Expose
        @SerializedName("VipExpired")
        private boolean vipExpired;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public boolean isRoomStatus() {
            return roomStatus;
        }

        public void setRoomStatus(boolean roomStatus) {
            this.roomStatus = roomStatus;
        }

        public boolean isFollower() {
            return isFollower;
        }

        public void setFollower(boolean follower) {
            isFollower = follower;
        }

        public boolean isLiang() {
            return isLiang;
        }

        public void setLiang(boolean liang) {
            isLiang = liang;
        }

        public int getVip() {
            return Vip;
        }

        public void setVip(int vip) {
            Vip = vip;
        }

        public boolean isVipExpired() {
            return vipExpired;
        }

        public void setVipExpired(boolean vipExpired) {
            this.vipExpired = vipExpired;
        }
    }
}
