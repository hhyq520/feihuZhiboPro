package cn.feihutv.zhibofeihu.data.network.http.model.wanfa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.BanResponce;

/**
 * Created by huanghao on 2017/10/18.
 */

public class GetJackpotDataResponce extends BaseResponse{
    @Expose
    @SerializedName("Data")
    private JackpotData jackpotData;

    public JackpotData getJackpotData() {
        return jackpotData;
    }

    public void setJackpotData(JackpotData jackpotData) {
        this.jackpotData = jackpotData;
    }

    public static class JackpotData {
        @Expose
        @SerializedName("1")
        private int yi;
        @Expose
        @SerializedName("2")
        private int er;
        @Expose
        @SerializedName("8")
        private int ba;
        @Expose
        @SerializedName("20")
        private int ershi;
        @Expose
        @SerializedName("master1")
        private int master1;
        @Expose
        @SerializedName("master2")
        private int master2;
        @Expose
        @SerializedName("master8")
        private int master8;
        @Expose
        @SerializedName("CountDown")
        private int CountDown;
        @Expose
        @SerializedName("GiftId")
        private int GiftId;

        public int getYi() {
            return yi;
        }

        public void setYi(int yi) {
            this.yi = yi;
        }

        public int getEr() {
            return er;
        }

        public void setEr(int er) {
            this.er = er;
        }

        public int getBa() {
            return ba;
        }

        public void setBa(int ba) {
            this.ba = ba;
        }

        public int getErshi() {
            return ershi;
        }

        public void setErshi(int ershi) {
            this.ershi = ershi;
        }

        public int getMaster1() {
            return master1;
        }

        public void setMaster1(int master1) {
            this.master1 = master1;
        }

        public int getMaster2() {
            return master2;
        }

        public void setMaster2(int master2) {
            this.master2 = master2;
        }

        public int getMaster8() {
            return master8;
        }

        public void setMaster8(int master8) {
            this.master8 = master8;
        }

        public int getCountDown() {
            return CountDown;
        }

        public void setCountDown(int countDown) {
            CountDown = countDown;
        }

        public int getGiftId() {
            return GiftId;
        }

        public void setGiftId(int giftId) {
            GiftId = giftId;
        }
    }
}
