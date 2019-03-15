package cn.feihutv.zhibofeihu.data.network.http.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/09/27
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class GetSocketTokenResponse {

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
    private GetSocketTokenData mTokenData;

    public GetSocketTokenData getTokenData() {
        return mTokenData;
    }

    public void setTokenData(GetSocketTokenData tokenData) {
        mTokenData = tokenData;
    }

    public static class GetSocketTokenData {

        @Expose
        @SerializedName("token")
        private String token;//
        @Expose
        @SerializedName("addrs")
        private List<String> address;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public List<String> getAddress() {
            return address;
        }

        public void setAddress(List<String> address) {
            this.address = address;
        }
    }

    @Override
    public String toString() {
        return "GetSocketTokenResponse{" +
                "code=" + code +
                ", errMsg='" + errMsg + '\'' +
                ", errExtMsg='" + errExtMsg + '\'' +
                ", errExtMsgTime='" + errExtMsgTime + '\'' +
                ", errExtMsgQ='" + errExtMsgQ + '\'' +
                ", mTokenData=" + mTokenData +
                '}';
    }
}
