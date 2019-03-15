package cn.feihutv.zhibofeihu.data.network.http.model.wanfa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * Created by huanghao on 2017/10/19.
 */

public class GetUserGameDataV3Responce extends BaseResponse{

    @Expose
    @SerializedName("Data")
    private  UserGameDataV3Data userGameDataV3Data;

    public UserGameDataV3Data getUserGameDataV3Data() {
        return userGameDataV3Data;
    }

    public void setUserGameDataV3Data(UserGameDataV3Data userGameDataV3Data) {
        this.userGameDataV3Data = userGameDataV3Data;
    }

    public static class UserGameDataV3Data{
        @Expose
        @SerializedName("BankerCnt")
        private int BankerCnt;
        @Expose
        @SerializedName("PlayerCnt")
        private int PlayerCnt;
        @Expose
        @SerializedName("RoundBankerBet")
        private RoundBankerBet roundBankerBet;
        @Expose
        @SerializedName("RoundPlayerBet")
        private RoundPlayerBet roundPlayerBet;
        @Expose
        @SerializedName("ExtVipCnt")
        private int ExtVipCnt;

        public int getBankerCnt() {
            return BankerCnt;
        }

        public void setBankerCnt(int bankerCnt) {
            BankerCnt = bankerCnt;
        }

        public int getPlayerCnt() {
            return PlayerCnt;
        }

        public void setPlayerCnt(int playerCnt) {
            PlayerCnt = playerCnt;
        }

        public RoundBankerBet getRoundBankerBet() {
            return roundBankerBet;
        }

        public void setRoundBankerBet(RoundBankerBet roundBankerBet) {
            this.roundBankerBet = roundBankerBet;
        }

        public RoundPlayerBet getRoundPlayerBet() {
            return roundPlayerBet;
        }

        public void setRoundPlayerBet(RoundPlayerBet roundPlayerBet) {
            this.roundPlayerBet = roundPlayerBet;
        }

        public int getExtVipCnt() {
            return ExtVipCnt;
        }

        public void setExtVipCnt(int extVipCnt) {
            ExtVipCnt = extVipCnt;
        }
    }

    public static class RoundBankerBet{
        @Expose
        @SerializedName("1")
        private int bet1;
        @Expose
        @SerializedName("2")
        private int bet2;
        @Expose
        @SerializedName("3")
        private int bet3;

        public int getBet1() {
            return bet1;
        }

        public void setBet1(int bet1) {
            this.bet1 = bet1;
        }

        public int getBet2() {
            return bet2;
        }

        public void setBet2(int bet2) {
            this.bet2 = bet2;
        }

        public int getBet3() {
            return bet3;
        }

        public void setBet3(int bet3) {
            this.bet3 = bet3;
        }
    }

    public static class RoundPlayerBet{
        @Expose
        @SerializedName("1")
        private int bet1;
        @Expose
        @SerializedName("2")
        private int bet2;
        @Expose
        @SerializedName("3")
        private int bet3;

        public int getBet1() {
            return bet1;
        }

        public void setBet1(int bet1) {
            this.bet1 = bet1;
        }

        public int getBet2() {
            return bet2;
        }

        public void setBet2(int bet2) {
            this.bet2 = bet2;
        }

        public int getBet3() {
            return bet3;
        }

        public void setBet3(int bet3) {
            this.bet3 = bet3;
        }
    }
}
