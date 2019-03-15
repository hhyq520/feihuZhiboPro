package cn.feihutv.zhibofeihu.data.network.http.model.me;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by suneee on 2016/11/17.
 */
public class PhotoInfo implements Serializable {

    @Expose
    @SerializedName("url")
    private String url;
    @Expose
    @SerializedName("w")
    private int w;
    @Expose
    @SerializedName("h")
    private int h;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }
}
