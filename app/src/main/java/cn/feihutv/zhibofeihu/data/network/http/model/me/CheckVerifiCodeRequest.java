package cn.feihutv.zhibofeihu.data.network.http.model.me;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/1 17:54
 *      desc   : 检查原手机号码
 *      version: 1.0
 * </pre>
 */

public class CheckVerifiCodeRequest {

    @Expose
    @SerializedName("verifiCode")
    private String verifiCode;

    public CheckVerifiCodeRequest(String verifiCode) {
        this.verifiCode = verifiCode;
    }
}
