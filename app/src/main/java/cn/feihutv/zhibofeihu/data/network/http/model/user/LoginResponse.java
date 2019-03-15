package cn.feihutv.zhibofeihu.data.network.http.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/09/25
 *     desc   : 登录响应
 *     version: 1.0
 * </pre>
 */
public class LoginResponse {

    @Expose
    @SerializedName("Code")
    private int code;

    @Expose
    @SerializedName("ErrMsg")
    private String errMsg;

    @Expose
    @SerializedName("ErrExtMsg")
    private String errExtMsg;

    @Expose
    @SerializedName("ErrExtMsg1")
    private String errExtMsgTime;


    @Expose
    @SerializedName("ErrExtMsg2")
    private String errExtMsgQ;


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

    public String getErrExtMsg() {
        return errExtMsg;
    }

    public void setErrExtMsg(String errExtMsg) {
        this.errExtMsg = errExtMsg;
    }

    public String getErrExtMsgTime() {
        return errExtMsgTime;
    }

    public void setErrExtMsgTime(String errExtMsgTime) {
        this.errExtMsgTime = errExtMsgTime;
    }

    public String getErrExtMsgQ() {
        return errExtMsgQ;
    }

    public void setErrExtMsgQ(String errExtMsgQ) {
        this.errExtMsgQ = errExtMsgQ;
    }



    @Expose
    @SerializedName("Data")
    private LoginResponseData mLoginResponseData;


    public LoginResponseData getLoginResponseData() {
        return mLoginResponseData;
    }

    public void setLoginResponseData(LoginResponseData loginResponseData) {
        this.mLoginResponseData = loginResponseData;
    }

    public static class LoginResponseData {

        @Expose
        @SerializedName("serverTime")
        private String serverTime;

        @Expose
        @SerializedName("token")
        private String token;

        @Expose
        @SerializedName("uid")
        private String uid;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getServerTime() {
            return serverTime;
        }

        public void setServerTime(String serverTime) {
            this.serverTime = serverTime;
        }

    }

}
