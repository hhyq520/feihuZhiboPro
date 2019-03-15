package cn.feihutv.zhibofeihu.data.network.http.model.me;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/1 15:43
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class BindPhoneRequest {

    @Expose
    @SerializedName("phone")
    private String phone;

    @Expose
    @SerializedName("verifiCode")
    private String verifiCode;

    @Expose
    @SerializedName("userPass")
    private String userPass;

    public BindPhoneRequest(String phone, String verifiCode, String userPass) {
        this.phone = phone;
        this.verifiCode = verifiCode;
        this.userPass = userPass;
    }
}
