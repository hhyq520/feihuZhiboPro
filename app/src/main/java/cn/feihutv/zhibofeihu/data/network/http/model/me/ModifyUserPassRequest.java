package cn.feihutv.zhibofeihu.data.network.http.model.me;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/10/31 19:20
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class ModifyUserPassRequest {

    @Expose
    @SerializedName("userPass")
    private String userPass;

    @Expose
    @SerializedName("newUserPass")
    private String newUserPass;

    public ModifyUserPassRequest(String userPass, String newUserPass) {
        this.userPass = userPass;
        this.newUserPass = newUserPass;
    }
}
