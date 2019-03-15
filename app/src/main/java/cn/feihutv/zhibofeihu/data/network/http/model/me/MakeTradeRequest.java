package cn.feihutv.zhibofeihu.data.network.http.model.me;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/13 15:38
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class MakeTradeRequest {

    @Expose
    @SerializedName("to")
    private String to;

    @Expose
    @SerializedName("amount")
    private String amount;

    @Expose
    @SerializedName("cnt")
    private String cnt;

    public MakeTradeRequest(String to, String amount, String cnt) {
        this.to = to;
        this.amount = amount;
        this.cnt = cnt;
    }
}
