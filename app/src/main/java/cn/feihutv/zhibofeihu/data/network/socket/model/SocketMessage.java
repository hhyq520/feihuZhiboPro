package cn.feihutv.zhibofeihu.data.network.socket.model;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/10/16
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class SocketMessage {


    public String reqTag;

    public int code;

    public String msgType;

    public String data;


    public String action;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getReqTag() {
        return reqTag;
    }

    public void setReqTag(String reqTag) {
        this.reqTag = reqTag;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
