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
public class RoomBanPush extends  BasePush{

    @Expose
    @SerializedName("Sender")
    private ChatSender sender;


    @Expose
    @SerializedName("Target")
    private ChatSender target;

    public ChatSender getSender() {
        return sender;
    }

    public void setSender(ChatSender sender) {
        this.sender = sender;
    }

    public ChatSender getTarget() {
        return target;
    }

    public void setTarget(ChatSender target) {
        this.target = target;
    }
}
