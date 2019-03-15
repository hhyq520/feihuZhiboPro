package cn.feihutv.zhibofeihu.data.network.http.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.user.BagResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.user.LoginResponse;

/**
 * Created by huanghao on 2017/10/11.
 */

public class GetVerifyCodeResponse extends BaseResponse{

    @Expose
    @SerializedName("Data")
    private VerifyCodeResponseData mVerifyCodeResponseData;


    public VerifyCodeResponseData getVerifyCodeResponseData() {
        return mVerifyCodeResponseData;
    }

    public void setVerifyCodeResponseData(VerifyCodeResponseData verifyCodeResponseData) {
        mVerifyCodeResponseData = verifyCodeResponseData;
    }

    public static class VerifyCodeResponseData {

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
