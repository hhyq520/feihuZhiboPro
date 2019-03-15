package cn.feihutv.zhibofeihu.data.network.http.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by huanghao on 2017/10/16.
 */

public class LogShareRequest {
    @Expose
    @SerializedName("from")
    private int from;
    @Expose
    @SerializedName("to")
    private int to;

    public LogShareRequest(int from, int to) {
        this.from = from;
        this.to = to;
    }


    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }
}
