package cn.feihutv.zhibofeihu.data.network.socket.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by huanghao on 2017/10/17.
 */

public class SendRoomMsgRequest {

    @Expose
    @SerializedName("msg")
    private String msg;

    public SendRoomMsgRequest(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
