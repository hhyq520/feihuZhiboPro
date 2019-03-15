package cn.feihutv.zhibofeihu.data.network.http.model.wanfa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.user.BagResponse;

/**
 * Created by huanghao on 2017/10/17.
 */

public class JackpotCountDownResponce extends BaseResponse{
    @Expose
    @SerializedName("Data")
    private JackpotCountDownData jackpotCountDownData;

    public JackpotCountDownData getJackpotCountDownData() {
        return jackpotCountDownData;
    }

    public void setJackpotCountDownData(JackpotCountDownData jackpotCountDownData) {
        this.jackpotCountDownData = jackpotCountDownData;
    }

    public static class JackpotCountDownData{
        @Expose
        @SerializedName("CountDown")
        private int CountDown;
        @Expose
        @SerializedName("GiftId")
        private int GiftId;

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
