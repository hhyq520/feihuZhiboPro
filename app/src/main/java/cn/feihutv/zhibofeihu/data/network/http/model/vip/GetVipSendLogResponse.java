package cn.feihutv.zhibofeihu.data.network.http.model.vip;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/10/31
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class GetVipSendLogResponse extends BaseResponse {


    @Expose
    @SerializedName("Data")
    private GetVipSendLogData mGetVipSendLogData;

    public GetVipSendLogData getGetVipSendLogData() {
        return mGetVipSendLogData;
    }

    public void setGetVipSendLogData(GetVipSendLogData getVipSendLogData) {
        mGetVipSendLogData = getVipSendLogData;
    }

    public static class GetVipSendLogData {


        @Expose
        @SerializedName("NextOffset")
        private int nextOffset;  //用于设置offset


        @Expose
        @SerializedName("List")
        private List<SendLogList> mReceiveLogList;

        public int getNextOffset() {
            return nextOffset;
        }

        public void setNextOffset(int nextOffset) {
            this.nextOffset = nextOffset;
        }

        public List<SendLogList> getReceiveLogList() {
            return mReceiveLogList;
        }

        public void setReceiveLogList(List<SendLogList> receiveLogList) {
            mReceiveLogList = receiveLogList;
        }
    }


    public static class SendLogList {
        @Expose
        @SerializedName("UserId")
        private String userId;  //用户头像

        @Expose
        @SerializedName("Nickname")
        private String nickname;  //用户

        @Expose
        @SerializedName("Avatar")
        private String avatar;  //用户头像

        @Expose
        @SerializedName("Time")
        private Long time;  //收到的时间戳（秒）

        @Expose
        @SerializedName("Days")
        private int days;  //收到的VIP时长（天）

        @Expose
        @SerializedName("GoodsId")
        private int goodsId;

        public int getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(int goodsId) {
            this.goodsId = goodsId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public Long getTime() {
            return time;
        }

        public void setTime(Long time) {
            this.time = time;
        }

        public int getDays() {
            return days;
        }

        public void setDays(int days) {
            this.days = days;
        }
    }

}
