package cn.feihutv.zhibofeihu.data.network.http.model.me;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/8 21:26
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class CheckGuardInfoRequest {

    @Expose
    @SerializedName("accountId")
    private String accountId;

    public CheckGuardInfoRequest(String accountId) {
        this.accountId = accountId;
    }
}
