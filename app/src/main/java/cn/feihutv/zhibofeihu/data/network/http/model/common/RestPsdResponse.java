package cn.feihutv.zhibofeihu.data.network.http.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * Created by huanghao on 2017/10/11.
 */

public class RestPsdResponse extends BaseResponse{

    @Expose
    @SerializedName("Data")
    private RestPsdResponseData mRestPsdResponseData;


    public RestPsdResponseData getRestPsdResponseData() {
        return mRestPsdResponseData;
    }

    public void setRestPsdResponseData(RestPsdResponseData restPsdResponseData) {
        mRestPsdResponseData = restPsdResponseData;
    }

    public static class RestPsdResponseData {

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
