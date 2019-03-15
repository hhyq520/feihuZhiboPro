package cn.feihutv.zhibofeihu.data.network.http.model.wanfa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by huanghao on 2017/10/19.
 */

public class GamePreemptRequest {
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("openStyle")
    private int openStyle;

    public GamePreemptRequest(String name, int openStyle) {
        this.name = name;
        this.openStyle = openStyle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOpenStyle() {
        return openStyle;
    }

    public void setOpenStyle(int openStyle) {
        this.openStyle = openStyle;
    }
}
