package cn.feihutv.zhibofeihu.data.network.http.model.me;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/3 10:31
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class PayRequest {

    @Expose
    @SerializedName("pf")
    private String pf;

    @Expose
    @SerializedName("hb")
    private int hb;

    public PayRequest(String pf, int hb) {
        this.pf = pf;
        this.hb = hb;
    }
}
