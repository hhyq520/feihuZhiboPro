package cn.feihutv.zhibofeihu.data.network.socket.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/10/10
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class SocketBaseResponse {

    public boolean success = false;

    //是否首次连接成功
    public boolean isFirstConnected = true;

    public String dataJson;

    @Expose
    @SerializedName("MsgType")
    public String msgType;

    @Expose
    @SerializedName("Code")
    public int code;


    @Expose
    @SerializedName("errCode")
    public int errCode;

    @Expose
    @SerializedName("ErrMsg")
    public String errMsg;

    public String ErrExtMsg;


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
        if(0==code){
            setSuccess(true);
        }
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }


    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    @Override
    public String toString() {
        return "SocketBaseResponse{" +
                "success=" + success +
                ", isFirstConnected=" + isFirstConnected +
                ", dataJson='" + dataJson + '\'' +
                ", msgType=" + msgType +
                ", code=" + code +
                ", errCode=" + errCode +
                ", errMsg='" + errMsg + '\'' +
                ", ErrExtMsg='" + ErrExtMsg + '\'' +
                '}';
    }
}
