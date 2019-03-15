package cn.feihutv.zhibofeihu.data.network.http.model.wanfa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * Created by huanghao on 2017/10/19.
 */

public class GetGameBetResponce extends BaseResponse{

    @Expose
    @SerializedName("Data")
    private GetGameBetData getGameBetData;

    public GetGameBetData getGetGameBetData() {
        return getGameBetData;
    }

    public void setGetGameBetData(GetGameBetData getGameBetData) {
        this.getGameBetData = getGameBetData;
    }

    public static class GetGameBetData{
        @Expose
        @SerializedName("BankerBet")
        private BankerBet bankerBet;
        @Expose
        @SerializedName("PlayerBet")
        private PlayerBet playerBet;

        public BankerBet getBankerBet() {
            return bankerBet;
        }

        public void setBankerBet(BankerBet bankerBet) {
            this.bankerBet = bankerBet;
        }

        public PlayerBet getPlayerBet() {
            return playerBet;
        }

        public void setPlayerBet(PlayerBet playerBet) {
            this.playerBet = playerBet;
        }
    }

    public static class BankerBet{
        @Expose
        @SerializedName("1")
        private String bet1;
        @Expose
        @SerializedName("2")
        private String bet2;
        @Expose
        @SerializedName("3")
        private String bet3;
        @Expose
        @SerializedName("4")
        private String bet4;
        @Expose
        @SerializedName("5")
        private String bet5;
        @Expose
        @SerializedName("6")
        private String bet6;

        public String getBet1() {
            return bet1;
        }

        public void setBet1(String bet1) {
            this.bet1 = bet1;
        }

        public String getBet2() {
            return bet2;
        }

        public void setBet2(String bet2) {
            this.bet2 = bet2;
        }

        public String getBet3() {
            return bet3;
        }

        public void setBet3(String bet3) {
            this.bet3 = bet3;
        }

        public String getBet4() {
            return bet4;
        }

        public void setBet4(String bet4) {
            this.bet4 = bet4;
        }

        public String getBet5() {
            return bet5;
        }

        public void setBet5(String bet5) {
            this.bet5 = bet5;
        }

        public String getBet6() {
            return bet6;
        }

        public void setBet6(String bet6) {
            this.bet6 = bet6;
        }
    }

    public static class PlayerBet{
        @Expose
        @SerializedName("1")
        private String bet1;
        @Expose
        @SerializedName("2")
        private String bet2;
        @Expose
        @SerializedName("3")
        private String bet3;
        @Expose
        @SerializedName("4")
        private String bet4;
        @Expose
        @SerializedName("5")
        private String bet5;
        @Expose
        @SerializedName("6")
        private String bet6;

        public String getBet1() {
            return bet1;
        }

        public void setBet1(String bet1) {
            this.bet1 = bet1;
        }

        public String getBet2() {
            return bet2;
        }

        public void setBet2(String bet2) {
            this.bet2 = bet2;
        }

        public String getBet3() {
            return bet3;
        }

        public void setBet3(String bet3) {
            this.bet3 = bet3;
        }

        public String getBet4() {
            return bet4;
        }

        public void setBet4(String bet4) {
            this.bet4 = bet4;
        }

        public String getBet5() {
            return bet5;
        }

        public void setBet5(String bet5) {
            this.bet5 = bet5;
        }

        public String getBet6() {
            return bet6;
        }

        public void setBet6(String bet6) {
            this.bet6 = bet6;
        }
    }
}
