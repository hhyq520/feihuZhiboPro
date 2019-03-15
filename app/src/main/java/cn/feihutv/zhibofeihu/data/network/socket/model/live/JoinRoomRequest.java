package cn.feihutv.zhibofeihu.data.network.socket.model.live;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by huanghao on 2017/10/13.
 */

public class JoinRoomRequest {
    @Expose
    @SerializedName("roomId")
    private String roomId;
    @Expose
    @SerializedName("reconnect")
    private String reconnect;

    public JoinRoomRequest(String roomId, String reconnect) {
        this.roomId = roomId;
        this.reconnect = reconnect;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getReconnect() {
        return reconnect;
    }

    public void setReconnect(String reconnect) {
        this.reconnect = reconnect;
    }
}
