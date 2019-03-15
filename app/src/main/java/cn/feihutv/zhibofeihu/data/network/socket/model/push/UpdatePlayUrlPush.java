package cn.feihutv.zhibofeihu.data.network.socket.model.push;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/10/16
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class UpdatePlayUrlPush extends  BasePush{

    @Expose
    @SerializedName("PlayUrl")
    private String playUrl;

    @Expose
    @SerializedName("RoomStatus")
    private boolean roomStatus;


    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public boolean isRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(boolean roomStatus) {
        this.roomStatus = roomStatus;
    }
}
