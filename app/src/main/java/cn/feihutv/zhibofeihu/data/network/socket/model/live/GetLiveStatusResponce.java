package cn.feihutv.zhibofeihu.data.network.socket.model.live;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import cn.feihutv.zhibofeihu.data.network.socket.model.SocketBaseResponse;

/**
 * Created by huanghao on 2017/10/18.
 */

public class GetLiveStatusResponce extends SocketBaseResponse{
    @Expose
    @SerializedName("Data")
    private boolean liveStatus;

    public boolean isLiveStatus() {
        return liveStatus;
    }

    public void setLiveStatus(boolean liveStatus) {
        this.liveStatus = liveStatus;
    }
}
