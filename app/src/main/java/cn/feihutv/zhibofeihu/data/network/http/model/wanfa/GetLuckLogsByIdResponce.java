package cn.feihutv.zhibofeihu.data.network.http.model.wanfa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * Created by huanghao on 2017/10/18.
 */

public class GetLuckLogsByIdResponce extends BaseResponse{
    @Expose
    @SerializedName("Data")
    private LuckLogsById luckLogsById;

    public LuckLogsById getLuckLogsById() {
        return luckLogsById;
    }

    public void setLuckLogsById(LuckLogsById luckLogsById) {
        this.luckLogsById = luckLogsById;
    }

    public static  class  LuckLogsById{
        @Expose
        @SerializedName("NextOffset")
        private int NextOffset;
        @Expose
        @SerializedName("List")
        private List<LuckLogs> luckLogsList;

        public int getNextOffset() {
            return NextOffset;
        }

        public void setNextOffset(int nextOffset) {
            NextOffset = nextOffset;
        }

        public List<LuckLogs> getLuckLogsList() {
            return luckLogsList;
        }

        public void setLuckLogsList(List<LuckLogs> luckLogsList) {
            this.luckLogsList = luckLogsList;
        }
    }
    public static class LuckLogs{
        @Expose
        @SerializedName("logTime")
        private int logTime;
        @Expose
        @SerializedName("nickname")
        private String nickname;
        @Expose
        @SerializedName("roomAccountId")
        private int roomAccountId;
        @Expose
        @SerializedName("giftCnt")
        private int giftCnt;
        @Expose
        @SerializedName("hb")
        private int hb;
        @Expose
        @SerializedName("awardCnt")
        private int awardCnt;
        @Expose
        @SerializedName("giftId")
        private int giftId;
        @Expose
        @SerializedName("showVipGiftIcon")
        private boolean showVipGiftIcon;

        public int getLogTime() {
            return logTime;
        }

        public void setLogTime(int logTime) {
            this.logTime = logTime;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getRoomAccountId() {
            return roomAccountId;
        }

        public void setRoomAccountId(int roomAccountId) {
            this.roomAccountId = roomAccountId;
        }

        public int getGiftCnt() {
            return giftCnt;
        }

        public void setGiftCnt(int giftCnt) {
            this.giftCnt = giftCnt;
        }

        public int getHb() {
            return hb;
        }

        public void setHb(int hb) {
            this.hb = hb;
        }

        public int getAwardCnt() {
            return awardCnt;
        }

        public void setAwardCnt(int awardCnt) {
            this.awardCnt = awardCnt;
        }

        public int getGiftId() {
            return giftId;
        }

        public void setGiftId(int giftId) {
            this.giftId = giftId;
        }

        public boolean isShowVipGiftIcon() {
            return showVipGiftIcon;
        }

        public void setShowVipGiftIcon(boolean showVipGiftIcon) {
            this.showVipGiftIcon = showVipGiftIcon;
        }
    }
}
