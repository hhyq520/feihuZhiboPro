package cn.feihutv.zhibofeihu.data.network.http.model.me;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/17 11:03
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class GetMsgSwitchStatusResponse extends BaseResponse {


    /**
     * Data : {"MsgSwitchFeed":true,"MsgSwitchMsrMessage":true}
     */

    @Expose
    @SerializedName("Data")
    private GetMsgSwitchStatusResponseData mSwitchStatusResponseData;

    public GetMsgSwitchStatusResponseData getSwitchStatusResponseData() {
        return mSwitchStatusResponseData;
    }

    public void setSwitchStatusResponseData(GetMsgSwitchStatusResponseData switchStatusResponseData) {
        mSwitchStatusResponseData = switchStatusResponseData;
    }

    public static class GetMsgSwitchStatusResponseData {
        /**
         * MsgSwitchFeed : true
         * MsgSwitchMsrMessage : true
         */

        @Expose
        @SerializedName("MsgSwitchFeed")
        private boolean msgSwitchFeed;

        @Expose
        @SerializedName("MsgSwitchMsrMessage")
        private boolean msgSwitchMsrMessage;

        public boolean isMsgSwitchFeed() {
            return msgSwitchFeed;
        }

        public void setMsgSwitchFeed(boolean msgSwitchFeed) {
            this.msgSwitchFeed = msgSwitchFeed;
        }

        public boolean isMsgSwitchMsrMessage() {
            return msgSwitchMsrMessage;
        }

        public void setMsgSwitchMsrMessage(boolean msgSwitchMsrMessage) {
            this.msgSwitchMsrMessage = msgSwitchMsrMessage;
        }
    }
}
