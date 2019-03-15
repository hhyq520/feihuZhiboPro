package cn.feihutv.zhibofeihu.data.network.socket.model.live;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.home.GetBannerResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.home.LoadRoomListResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.SocketBaseResponse;

/**
 * Created by huanghao on 2017/10/13.
 */

public class JoinRoomResponce {

    @Expose
    @SerializedName("MsgType")
    public String msgType;

    @Expose
    @SerializedName("ErrExtMsg")
    public  ErrExtMsgData errExtMsgData;

    @Expose
    @SerializedName("Data")
    private JoinRoomData mJoinRoomData;

    @Expose
    @SerializedName("Code")
    public int code;


    @Expose
    @SerializedName("errCode")
    public int errCode;

    @Expose
    @SerializedName("ErrMsg")
    public String errMsg;

    public ErrExtMsgData getErrExtMsgData() {
        return errExtMsgData;
    }

    public void setErrExtMsgData(ErrExtMsgData errExtMsgData) {
        this.errExtMsgData = errExtMsgData;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public JoinRoomData getmJoinRoomData() {
        return mJoinRoomData;
    }

    public void setmJoinRoomData(JoinRoomData mJoinRoomData) {
        this.mJoinRoomData = mJoinRoomData;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public static class ErrExtMsgData{
        @Expose
        @SerializedName("Desc")
        private String Desc;
        @Expose
        @SerializedName("Duration")
        private String Duration;

        public String getDesc() {
            return Desc;
        }

        public void setDesc(String desc) {
            Desc = desc;
        }

        public String getDuration() {
            return Duration;
        }

        public void setDuration(String duration) {
            Duration = duration;
        }
    }

    public static class JoinRoomData{
        @Expose
        @SerializedName("RoomId")
        private String RoomId;
        @Expose
        @SerializedName("Master")
        private MasterData masterDataList;
        @Expose
        @SerializedName("PlayUrl")
        private String PlayUrl;
        @Expose
        @SerializedName("RoomStatus")
        private boolean RoomStatus;
        @Expose
        @SerializedName("PlayStatus")
        private boolean PlayStatus;
        @Expose
        @SerializedName("OnlineUserCnt")
        private int OnlineUserCnt;
        @Expose
        @SerializedName("IsRoomMgr")
        private boolean IsRoomMgr;
        @Expose
        @SerializedName("Xiaolaba")
        private int Xiaolaba;

        public String getRoomId() {
            return RoomId;
        }

        public void setRoomId(String roomId) {
            RoomId = roomId;
        }

        public MasterData getMasterDataList() {
            return masterDataList;
        }

        public void setMasterDataList(MasterData masterDataList) {
            this.masterDataList = masterDataList;
        }

        public String getPlayUrl() {
            return PlayUrl;
        }

        public void setPlayUrl(String playUrl) {
            PlayUrl = playUrl;
        }

        public boolean isRoomStatus() {
            return RoomStatus;
        }

        public void setRoomStatus(boolean roomStatus) {
            RoomStatus = roomStatus;
        }

        public boolean isPlayStatus() {
            return PlayStatus;
        }

        public void setPlayStatus(boolean playStatus) {
            PlayStatus = playStatus;
        }

        public int getOnlineUserCnt() {
            return OnlineUserCnt;
        }

        public void setOnlineUserCnt(int onlineUserCnt) {
            OnlineUserCnt = onlineUserCnt;
        }

        public boolean isRoomMgr() {
            return IsRoomMgr;
        }

        public void setRoomMgr(boolean roomMgr) {
            IsRoomMgr = roomMgr;
        }

        public int getXiaolaba() {
            return Xiaolaba;
        }

        public void setXiaolaba(int xiaolaba) {
            Xiaolaba = xiaolaba;
        }
    }

    public static class MasterData{
        @Expose
        @SerializedName("UserId")
        private String UserId;
        @Expose
        @SerializedName("NickName")
        private String NickName;
        @Expose
        @SerializedName("AccountId")
        private String AccountId;
        @Expose
        @SerializedName("HeadUrl")
        private String HeadUrl;
        @Expose
        @SerializedName("Gender")
        private int Gender;
        @Expose
        @SerializedName("HB")
        private long HB;
        @Expose
        @SerializedName("Followers")
        private int Followers ;
        @Expose
        @SerializedName("LiveCover")
        private String LiveCover ;
        @Expose
        @SerializedName("RoomName")
        private String RoomName;
        @Expose
        @SerializedName("Followed")
        private boolean Followed;


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

        public String getAccountId() {
            return AccountId;
        }

        public void setAccountId(String accountId) {
            AccountId = accountId;
        }

        public String getHeadUrl() {
            return HeadUrl;
        }

        public void setHeadUrl(String headUrl) {
            HeadUrl = headUrl;
        }

        public int getGender() {
            return Gender;
        }

        public void setGender(int gender) {
            Gender = gender;
        }

        public long getHB() {
            return HB;
        }

        public void setHB(long HB) {
            this.HB = HB;
        }

        public int getFollowers() {
            return Followers;
        }

        public void setFollowers(int followers) {
            Followers = followers;
        }

        public String getLiveCover() {
            return LiveCover;
        }

        public void setLiveCover(String liveCover) {
            LiveCover = liveCover;
        }

        public String getRoomName() {
            return RoomName;
        }

        public void setRoomName(String roomName) {
            RoomName = roomName;
        }

        public boolean isFollowed() {
            return Followed;
        }

        public void setFollowed(boolean followed) {
            Followed = followed;
        }
    }
}
