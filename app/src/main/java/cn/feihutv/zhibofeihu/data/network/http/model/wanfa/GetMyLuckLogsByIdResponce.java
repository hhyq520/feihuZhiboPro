package cn.feihutv.zhibofeihu.data.network.http.model.wanfa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * Created by huanghao on 2017/10/19.
 */

public class GetMyLuckLogsByIdResponce extends BaseResponse{
    @Expose
    @SerializedName("Data")
    private MyLuckLogsById luckLogsById;

    public MyLuckLogsById getLuckLogsById() {
        return luckLogsById;
    }

    public void setLuckLogsById(MyLuckLogsById luckLogsById) {
        this.luckLogsById = luckLogsById;
    }


    public static  class  MyLuckLogsById{
        @Expose
        @SerializedName("NextOffset")
        private int NextOffset;
        @Expose
        @SerializedName("List")
        private List<MyLuckLogs> luckLogsList;

        public int getNextOffset() {
            return NextOffset;
        }

        public void setNextOffset(int nextOffset) {
            NextOffset = nextOffset;
        }

        public List<MyLuckLogs> getLuckLogsList() {
            return luckLogsList;
        }

        public void setLuckLogsList(List<MyLuckLogs> luckLogsList) {
            this.luckLogsList = luckLogsList;
        }
    }
    public static class MyLuckLogs{
        @Expose
        @SerializedName("logTime")
        private int logTime;
        @Expose
        @SerializedName("giftId")
        private int giftId;
        @Expose
        @SerializedName("giftCnt")
        private int giftCnt;
        @Expose
        @SerializedName("hb")
        private int hb;
        @Expose
        @SerializedName("awardCnt")
        private int awardCnt;

        public int getLogTime() {
            return logTime;
        }

        public void setLogTime(int logTime) {
            this.logTime = logTime;
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
    }
}
