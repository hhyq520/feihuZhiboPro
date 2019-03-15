package cn.feihutv.zhibofeihu.data.network.http.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by huanghao on 2017/10/10.
 */

public class LoadRoomRequest {
    @Expose
    @SerializedName("roomId")
    private String roomId;

    public LoadRoomRequest(String id) {
        this.roomId = id;
    }


    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
