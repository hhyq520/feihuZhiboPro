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
public class RoomMgrPush extends  BasePush{

    @Expose
    @SerializedName("Status")
    private boolean status;



    @Expose
    @SerializedName("Target")
    private ChatSender target;


    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ChatSender getTarget() {
        return target;
    }

    public void setTarget(ChatSender target) {
        this.target = target;
    }
}
