package cn.feihutv.zhibofeihu.data.network.http.model.me;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/1 18:51
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class ModifyPhoneRequest {

    @Expose
    @SerializedName("verifiCode")
    private String verifiCode;

    @Expose
    @SerializedName("newPhone")
    private String newPhone;

    @Expose
    @SerializedName("newVerifiCode")
    private String newVerifiCode;

    public ModifyPhoneRequest(String verifiCode, String newPhone, String newVerifiCode) {
        this.verifiCode = verifiCode;
        this.newPhone = newPhone;
        this.newVerifiCode = newVerifiCode;
    }
}
