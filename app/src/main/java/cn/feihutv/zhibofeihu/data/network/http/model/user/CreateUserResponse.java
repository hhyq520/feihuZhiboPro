package cn.feihutv.zhibofeihu.data.network.http.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * Created by huanghao on 2017/10/11.
 */

public class CreateUserResponse extends BaseResponse{

    @Expose
    @SerializedName("Data")
    private CreateUserResponseData mCreateUserResponseData;


    public CreateUserResponseData getCreateUserResponseData() {
        return mCreateUserResponseData;
    }

    public void setCreateUserResponseData(CreateUserResponseData createUserResponseData) {
        mCreateUserResponseData = createUserResponseData;
    }

    public static class CreateUserResponseData {

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
