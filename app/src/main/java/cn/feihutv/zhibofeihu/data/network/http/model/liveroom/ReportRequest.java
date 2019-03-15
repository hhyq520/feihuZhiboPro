package cn.feihutv.zhibofeihu.data.network.http.model.liveroom;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by huanghao on 2017/11/1.
 */

public class ReportRequest {
    @Expose
    @SerializedName("userId")
    private String userId;
    @Expose
    @SerializedName("repType")
    private int repType;
    @Expose
    @SerializedName("content")
    private String content;
    @Expose
    @SerializedName("imgs")
    private String imgs;

    public ReportRequest(String userId, int repType, String content, String imgs) {
        this.userId = userId;
        this.repType = repType;
        this.content = content;
        this.imgs = imgs;
    }
}
