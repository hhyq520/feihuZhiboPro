package cn.feihutv.zhibofeihu.data.network.socket.model.live;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.SocketBaseResponse;

/**
 * Created by huanghao on 2017/10/13.
 */

public class StartLiveResponce  {

    @Expose
    @SerializedName("MsgType")
    public String msgType;

    @Expose
    @SerializedName("ErrExtMsg")
    public JoinRoomResponce.ErrExtMsgData errExtMsgData;

    @Expose
    @SerializedName("Code")
    public int code;


    @Expose
    @SerializedName("errCode")
    public int errCode;

    @Expose
    @SerializedName("ErrMsg")
    public String errMsg;

    @Expose
    @SerializedName("Data")
    private StartLiveData mStartLiveData;

    public StartLiveData getmStartLiveData() {
        return mStartLiveData;
    }

    public void setmStartLiveData(StartLiveData mStartLiveData) {
        this.mStartLiveData = mStartLiveData;
    }

    public JoinRoomResponce.ErrExtMsgData getErrExtMsgData() {
        return errExtMsgData;
    }

    public void setErrExtMsgData(JoinRoomResponce.ErrExtMsgData errExtMsgData) {
        this.errExtMsgData = errExtMsgData;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
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


    public static class  StartLiveData{
        @Expose
        @SerializedName("PushUrl")
        private String PushUrl;
        @Expose
        @SerializedName("OfflineGifts")
        private List<OfflineGiftsData> offlineGiftsDataList;
        @Expose
        @SerializedName("focusModeEnabled")
        private boolean focusModeEnabled;
        @Expose
        @SerializedName("focusMode")
        private String focusMode;
        @Expose
        @SerializedName("audioQuality")
        private int audioQuality;
        @Expose
        @SerializedName("videoQuality")
        private int videoQuality;
        @Expose
        @SerializedName("encodingSizeLevel")
        private int encodingSizeLevel;
        @Expose
        @SerializedName("encoderRCMode")
        private int encoderRCMode;

        public String getPushUrl() {
            return PushUrl;
        }

        public void setPushUrl(String pushUrl) {
            PushUrl = pushUrl;
        }

        public List<OfflineGiftsData> getOfflineGiftsDataList() {
            return offlineGiftsDataList;
        }

        public void setOfflineGiftsDataList(List<OfflineGiftsData> offlineGiftsDataList) {
            this.offlineGiftsDataList = offlineGiftsDataList;
        }

        public boolean isFocusModeEnabled() {
            return focusModeEnabled;
        }

        public void setFocusModeEnabled(boolean focusModeEnabled) {
            this.focusModeEnabled = focusModeEnabled;
        }

        public String getFocusMode() {
            return focusMode;
        }

        public void setFocusMode(String focusMode) {
            this.focusMode = focusMode;
        }

        public int getAudioQuality() {
            return audioQuality;
        }

        public void setAudioQuality(int audioQuality) {
            this.audioQuality = audioQuality;
        }

        public int getVideoQuality() {
            return videoQuality;
        }

        public void setVideoQuality(int videoQuality) {
            this.videoQuality = videoQuality;
        }

        public int getEncodingSizeLevel() {
            return encodingSizeLevel;
        }

        public void setEncodingSizeLevel(int encodingSizeLevel) {
            this.encodingSizeLevel = encodingSizeLevel;
        }

        public int getEncoderRCMode() {
            return encoderRCMode;
        }

        public void setEncoderRCMode(int encoderRCMode) {
            this.encoderRCMode = encoderRCMode;
        }
    }

    public static class OfflineGiftsData{
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
        @SerializedName("GiftId")
        private int  GiftId;
        @Expose
        @SerializedName("GiftNum")
        private int  GiftNum;

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

        public int getGiftId() {
            return GiftId;
        }

        public void setGiftId(int giftId) {
            GiftId = giftId;
        }

        public int getGiftNum() {
            return GiftNum;
        }

        public void setGiftNum(int giftNum) {
            GiftNum = giftNum;
        }
    }
}
