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
public class NoticeLeaveRoomPush extends  BasePush{

    @Expose
    @SerializedName("TotalRoomMembers")
    private int totalRoomMembers;

    @Expose
    @SerializedName("Uid")
    private String uid;


    public int getTotalRoomMembers() {
        return totalRoomMembers;
    }

    public void setTotalRoomMembers(int totalRoomMembers) {
        this.totalRoomMembers = totalRoomMembers;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
