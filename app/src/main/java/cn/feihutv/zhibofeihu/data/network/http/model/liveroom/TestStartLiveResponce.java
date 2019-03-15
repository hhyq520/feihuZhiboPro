package cn.feihutv.zhibofeihu.data.network.http.model.liveroom;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.socket.model.live.JoinRoomResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.StartLiveResponce;

/**
 * Created by huanghao on 2017/11/29.
 */

public class TestStartLiveResponce {
    @Expose
    @SerializedName("MsgType")
    public String msgType;

    @Expose
    @SerializedName("ErrExtMsg")
    public ErrExtMsgData errExtMsgData;

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



}
