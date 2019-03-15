package cn.feihutv.zhibofeihu.data.network.http.model.me;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/3 16:00
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class EncashRequest {

    @Expose
    @SerializedName("ghb")
    private String ghb;

    public EncashRequest(String ghb) {
        this.ghb = ghb;
    }
}
