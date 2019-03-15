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
public class NoticeRoomCarePush extends  BasePush{

    @Expose
    @SerializedName("FansCnt")
    private int fansCnt;

    @Expose
    @SerializedName("Sender")
    private ChatSender sender;


    public int getFansCnt() {
        return fansCnt;
    }

    public void setFansCnt(int fansCnt) {
        this.fansCnt = fansCnt;
    }

    public ChatSender getSender() {
        return sender;
    }

    public void setSender(ChatSender sender) {
        this.sender = sender;
    }
}
