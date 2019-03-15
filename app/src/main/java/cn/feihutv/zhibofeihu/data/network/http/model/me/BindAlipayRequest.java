package cn.feihutv.zhibofeihu.data.network.http.model.me;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/3 17:23
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class BindAlipayRequest {

    @Expose
    @SerializedName("account")
    private String account;

    @Expose
    @SerializedName("realname")
    private String realname;

    public BindAlipayRequest(String account, String realname) {
        this.account = account;
        this.realname = realname;
    }
}
