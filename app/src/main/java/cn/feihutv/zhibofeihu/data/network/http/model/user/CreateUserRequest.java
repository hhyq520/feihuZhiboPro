package cn.feihutv.zhibofeihu.data.network.http.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by huanghao on 2017/10/11.
 */

public class CreateUserRequest {
    @Expose
    @SerializedName("phone")
    private String phone;
    @Expose
    @SerializedName("userPass")
    private String userPass;
    @Expose
    @SerializedName("nickName")
    private String nickName;
    @Expose
    @SerializedName("veriCode")
    private String veriCode;
    @Expose
    @SerializedName("deviceId")
    private String deviceId;

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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getVeriCode() {
        return veriCode;
    }

    public void setVeriCode(String veriCode) {
        this.veriCode = veriCode;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public CreateUserRequest(String phone,String userPass,String nickName,String veriCode,String deviceId){
        this.phone=phone;
        this.userPass=userPass;
        this.nickName=nickName;
        this.veriCode=veriCode;
        this.deviceId=deviceId;
    }
}
