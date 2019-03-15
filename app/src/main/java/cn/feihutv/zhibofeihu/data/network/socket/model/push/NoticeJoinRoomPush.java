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
public class NoticeJoinRoomPush extends BasePush {

    @Expose
    @SerializedName("Sender")
    private ChatSender mSender;

    @Expose
    @SerializedName("TotalRoomMembers")
    private int totalRoomMembers;

    @Expose
    @SerializedName("Contri")
    private int contri;


    @Expose
    @SerializedName("Mount")
    private int Mount;

    public ChatSender getSender() {
        return mSender;
    }

    public void setSender(ChatSender sender) {
        mSender = sender;
    }

    public int getTotalRoomMembers() {
        return totalRoomMembers;
    }

    public void setTotalRoomMembers(int totalRoomMembers) {
        this.totalRoomMembers = totalRoomMembers;
    }

    public int getContri() {
        return contri;
    }

    public void setContri(int contri) {
        this.contri = contri;
    }

    public int getMount() {
        return Mount;
    }

    public void setMount(int mount) {
        Mount = mount;
    }



}
