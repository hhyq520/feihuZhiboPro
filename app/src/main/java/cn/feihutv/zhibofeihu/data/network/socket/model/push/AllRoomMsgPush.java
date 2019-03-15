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
public class AllRoomMsgPush  extends BasePush {

    @Expose
    @SerializedName("Sender")
    private ChatSender sender;


    @Expose
    @SerializedName("MsgName")
    private String msgName;


    @Expose
    @SerializedName("MsgLevel")
    private String msgLevel;

    @Expose
    @SerializedName("RoomName")
    private String roomName;


    @Expose
    @SerializedName("RoomId")
    private String roomId;


    @Expose
    @SerializedName("MasterName")
    private String masterName;



    @Expose
    @SerializedName("GiftId")
    private int giftId=-1;

    @Expose
    @SerializedName("Msg")
    private String msg;


    @Expose
    @SerializedName("GiftCnt")
    private int giftCnt=-1;

    public String getMsgLevel() {
        return msgLevel;
    }

    public void setMsgLevel(String msgLevel) {
        this.msgLevel = msgLevel;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public int getGiftId() {
        return giftId;
    }

    public void setGiftId(int giftId) {
        this.giftId = giftId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getGiftCnt() {
        return giftCnt;
    }

    public void setGiftCnt(int giftCnt) {
        this.giftCnt = giftCnt;
    }

    public ChatSender getSender() {
        return sender;
    }

    public void setSender(ChatSender sender) {
        this.sender = sender;
    }

    public String getMsgName() {
        return msgName;
    }

    public void setMsgName(String msgName) {
        this.msgName = msgName;
    }
}
