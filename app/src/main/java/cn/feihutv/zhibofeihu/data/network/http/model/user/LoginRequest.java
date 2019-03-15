package cn.feihutv.zhibofeihu.data.network.http.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/09/25
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class LoginRequest {

    //普通登录-
    public static class ServerLoginRequest {
        @Expose
        @SerializedName("phone")
        private String phone;

        @Expose
        @SerializedName("userPass")
        private String userPass;

        public ServerLoginRequest(String phone, String userPass) {
            this.phone = phone;
            this.userPass = userPass;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getUserPass() {
            return userPass;
        }

        public void setUserPass(String userPass) {
            this.userPass = userPass;
        }
    }


    //第三方平台登录
    public static class PlatformLoginRequest {

        @Expose
        @SerializedName("pf")
        private String pf;

        @Expose
        @SerializedName("openId")
        private String openId;

        @Expose
        @SerializedName("accessToken")
        private String accessToken;
        @Expose
        @SerializedName("deviceId")
        private String deviceId;

        public PlatformLoginRequest() {
        }

        public PlatformLoginRequest(String pf, String openId, String accessToken, String deviceId) {
            this.pf = pf;
            this.openId = openId;
            this.accessToken = accessToken;
            this.deviceId = deviceId;
        }

        public String getPf() {
            return pf;
        }

        public void setPf(String pf) {
            this.pf = pf;
        }

        public String getOpenId() {
            return openId;
        }

        public void setOpenId(String openId) {
            this.openId = openId;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }
    }


    //游客登录
    public static class GuestLoginRequest {

    }

}
