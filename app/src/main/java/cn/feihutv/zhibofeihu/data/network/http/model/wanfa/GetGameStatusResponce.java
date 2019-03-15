package cn.feihutv.zhibofeihu.data.network.http.model.wanfa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * Created by huanghao on 2017/10/19.
 */

public class GetGameStatusResponce extends BaseResponse{
    @Expose
    @SerializedName("Data")
    private GameStatusData gameStatusData;

    public GameStatusData getGameStatusData() {
        return gameStatusData;
    }

    public void setGameStatusData(GameStatusData gameStatusData) {
        this.gameStatusData = gameStatusData;
    }

    public static class GameStatusData{
        @Expose
        @SerializedName("CountDown")
        private int CountDown;
        @Expose
        @SerializedName("IsSelf")
        private boolean IsSelf;
        @Expose
        @SerializedName("IsSelf2")
        private boolean IsSelf2;
        @Expose
        @SerializedName("NextStatus")
        private int NextStatus;
        @Expose
        @SerializedName("Nickname")
        private String Nickname;
        @Expose
        @SerializedName("RoomId")
        private String RoomId;
        @Expose
        @SerializedName("IsGaming")
        private boolean IsGaming;
        @Expose
        @SerializedName("OpenStyle")
        private int OpenStyle;
        @Expose
        @SerializedName("Round")
        private int Round;
        @Expose
        @SerializedName("TotalRound")
        private int TotalRound;
        @Expose
        @SerializedName("Time")
        private int Time;
        @Expose
        @SerializedName("ServerTime")
        private int ServerTime;
        @Expose
        @SerializedName("BankerBet")
        private BankerBet bankerBet;
        @Expose
        @SerializedName("PlayerBet")
        private PlayerBet playerBet;
        @Expose
        @SerializedName("Top10Avatar")
        private List<String> Top10Avatar;
        @Expose
        @SerializedName("IssueRound")
        private int IssueRound;
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

        public int getCountDown() {
            return CountDown;
        }

        public void setCountDown(int countDown) {
            CountDown = countDown;
        }

        public boolean isSelf() {
            return IsSelf;
        }

        public void setSelf(boolean self) {
            IsSelf = self;
        }

        public boolean isSelf2() {
            return IsSelf2;
        }

        public void setSelf2(boolean self2) {
            IsSelf2 = self2;
        }

        public int getNextStatus() {
            return NextStatus;
        }

        public void setNextStatus(int nextStatus) {
            NextStatus = nextStatus;
        }

        public String getNickname() {
            return Nickname;
        }

        public void setNickname(String nickname) {
            Nickname = nickname;
        }

        public String getRoomId() {
            return RoomId;
        }

        public void setRoomId(String roomId) {
            RoomId = roomId;
        }

        public boolean isGaming() {
            return IsGaming;
        }

        public void setGaming(boolean gaming) {
            IsGaming = gaming;
        }

        public int getOpenStyle() {
            return OpenStyle;
        }

        public void setOpenStyle(int openStyle) {
            OpenStyle = openStyle;
        }

        public int getRound() {
            return Round;
        }

        public void setRound(int round) {
            Round = round;
        }

        public int getTotalRound() {
            return TotalRound;
        }

        public void setTotalRound(int totalRound) {
            TotalRound = totalRound;
        }

        public int getTime() {
            return Time;
        }

        public void setTime(int time) {
            Time = time;
        }

        public int getServerTime() {
            return ServerTime;
        }

        public void setServerTime(int serverTime) {
            ServerTime = serverTime;
        }

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

        public List<String> getTop10Avatar() {
            return Top10Avatar;
        }

        public void setTop10Avatar(List<String> top10Avatar) {
            Top10Avatar = top10Avatar;
        }

        public int getIssueRound() {
            return IssueRound;
        }

        public void setIssueRound(int issueRound) {
            IssueRound = issueRound;
        }

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

    public static class RoundBankerBet{
        @Expose
        @SerializedName("1")
        private String bet1;
        @Expose
        @SerializedName("2")
        private String bet2;
        @Expose
        @SerializedName("3")
        private String bet3;

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
    }

    public static class RoundPlayerBet{
        @Expose
        @SerializedName("1")
        private String bet1;
        @Expose
        @SerializedName("2")
        private String bet2;
        @Expose
        @SerializedName("3")
        private String bet3;

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
    }
}
