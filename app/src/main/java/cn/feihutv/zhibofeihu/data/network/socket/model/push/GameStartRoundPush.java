package cn.feihutv.zhibofeihu.data.network.socket.model.push;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/10/16
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class GameStartRoundPush extends BasePush {

    @Expose
    @SerializedName("GameName")
    private String gameName;

    @Expose
    @SerializedName("Round")
    private int round;


    @Expose
    @SerializedName("RoomId")
    private String roomId;


    @Expose
    @SerializedName("TotalRound")
    private int totalRound;

    @Expose
    @SerializedName("Nickname")
    private String nickname;


    @Expose
    @SerializedName("OpenStyle")
    private int openStyle;

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public int getTotalRound() {
        return totalRound;
    }

    public void setTotalRound(int totalRound) {
        this.totalRound = totalRound;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getOpenStyle() {
        return openStyle;
    }

    public void setOpenStyle(int openStyle) {
        this.openStyle = openStyle;
    }
}
