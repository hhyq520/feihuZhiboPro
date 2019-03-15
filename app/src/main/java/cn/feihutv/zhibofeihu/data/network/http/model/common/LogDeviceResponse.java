package cn.feihutv.zhibofeihu.data.network.http.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * Created by chenliwen on 2017/10/10 09:36.
 * desc: Android提交版本信息
 */

public class LogDeviceResponse extends BaseResponse {

    @Expose
    @SerializedName("Data")
    private int deviceId;

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }
}
