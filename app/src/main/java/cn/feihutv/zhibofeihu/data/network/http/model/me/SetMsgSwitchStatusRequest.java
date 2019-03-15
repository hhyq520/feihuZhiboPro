package cn.feihutv.zhibofeihu.data.network.http.model.me;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/17 11:03
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class SetMsgSwitchStatusRequest {

    @Expose
    @SerializedName("key")
    private String key; //  可设置值为MsgSwitchFeed 或 MsgSwitchMsrMessage

    @Expose
    @SerializedName("value")
    private String value;  // 1接收消息  0屏蔽

    public SetMsgSwitchStatusRequest(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
