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
public class RoomGiftPush extends BasePush{


    @Expose
    @SerializedName("Sender")
    private ChatSender sender;


    @Expose
    @SerializedName("Contri")
    private int contri;

    @Expose
    @SerializedName("GiftId")
    private int giftId;

    @Expose
    @SerializedName("GiftCnt")
    private int giftCnt;

    @Expose
    @SerializedName("Income")
    private long income;


    public ChatSender getSender() {
        return sender;
    }

    public void setSender(ChatSender sender) {
        this.sender = sender;
    }

    public int getContri() {
        return contri;
    }

    public void setContri(int contri) {
        this.contri = contri;
    }

    public int getGiftId() {
        return giftId;
    }

    public void setGiftId(int giftId) {
        this.giftId = giftId;
    }

    public int getGiftCnt() {
        return giftCnt;
    }

    public void setGiftCnt(int giftCnt) {
        this.giftCnt = giftCnt;
    }

    public long getIncome() {
        return income;
    }

    public void setIncome(long income) {
        this.income = income;
    }
}
