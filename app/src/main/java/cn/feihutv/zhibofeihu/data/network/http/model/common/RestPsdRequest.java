package cn.feihutv.zhibofeihu.data.network.http.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by huanghao on 2017/10/11.
 */

public class RestPsdRequest {
    @Expose
    @SerializedName("phone")
    private String phone;
    @Expose
    @SerializedName("pass")
    private String pass;
    @Expose
    @SerializedName("verifiCode")
    private String verifiCode;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public RestPsdRequest(String phone,String pass,String verifiCode){
        this.phone=phone;
        this.pass=pass;
        this.verifiCode=verifiCode;
    }


    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getVerifiCode() {
        return verifiCode;
    }

    public void setVerifiCode(String verifiCode) {
        this.verifiCode = verifiCode;
    }
}
