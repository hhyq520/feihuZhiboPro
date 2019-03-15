package cn.feihutv.zhibofeihu.data.network.http.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/09/27
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class CheckUpdateRequest {

    @Expose
    @SerializedName("platId")
    private String platId;

    public CheckUpdateRequest(String platId) {
        this.platId = platId;
    }

    public String getPlatId() {
        return platId;
    }

    public void setPlatId(String platId) {
        this.platId = platId;
    }
}
