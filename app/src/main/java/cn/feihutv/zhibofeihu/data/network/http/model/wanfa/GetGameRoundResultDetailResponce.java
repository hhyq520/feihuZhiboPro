package cn.feihutv.zhibofeihu.data.network.http.model.wanfa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * Created by huanghao on 2017/10/19.
 */

public class GetGameRoundResultDetailResponce extends BaseResponse{

    @Expose
    @SerializedName("Data")
    private List<GetGameRoundResultDetailData> getGameRoundResultDetailDatas;

    public List<GetGameRoundResultDetailData> getGetGameRoundResultDetailDatas() {
        return getGameRoundResultDetailDatas;
    }

    public void setGetGameRoundResultDetailDatas(List<GetGameRoundResultDetailData> getGameRoundResultDetailDatas) {
        this.getGameRoundResultDetailDatas = getGameRoundResultDetailDatas;
    }


    public static class GetGameRoundResultDetailData {
        @Expose
        @SerializedName("BetTime")
        private int BetTime;
        @Expose
        @SerializedName("Opt")
        private int Opt;
        @Expose
        @SerializedName("Side")
        private String Side;
        @Expose
        @SerializedName("Status")
        private int Status;
        @Expose
        @SerializedName("TotalBet")
        private int TotalBet;
        @Expose
        @SerializedName("WinHB")
        private int WinHB;
        @Expose
        @SerializedName("YHB")
        private int YHB;
        @Expose
        @SerializedName("Paired")
        private int Paired;

        public int getBetTime() {
            return BetTime;
        }

        public void setBetTime(int betTime) {
            BetTime = betTime;
        }

        public int getOpt() {
            return Opt;
        }

        public void setOpt(int opt) {
            Opt = opt;
        }

        public String getSide() {
            return Side;
        }

        public void setSide(String side) {
            Side = side;
        }

        public int getStatus() {
            return Status;
        }

        public void setStatus(int status) {
            Status = status;
        }

        public int getTotalBet() {
            return TotalBet;
        }

        public void setTotalBet(int totalBet) {
            TotalBet = totalBet;
        }

        public int getWinHB() {
            return WinHB;
        }

        public void setWinHB(int winHB) {
            WinHB = winHB;
        }

        public int getYHB() {
            return YHB;
        }

        public void setYHB(int YHB) {
            this.YHB = YHB;
        }

        public int getPaired() {
            return Paired;
        }

        public void setPaired(int paired) {
            Paired = paired;
        }
    }
}
