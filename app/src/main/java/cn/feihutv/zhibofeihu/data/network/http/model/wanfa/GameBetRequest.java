package cn.feihutv.zhibofeihu.data.network.http.model.wanfa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by huanghao on 2017/10/19.
 */

public class GameBetRequest {
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("side")
    private String side;
    @Expose
    @SerializedName("opt")
    private int opt;
    @Expose
    @SerializedName("bet")
    private int bet;

    public GameBetRequest(String name, String side, int opt, int bet) {
        this.name = name;
        this.side = side;
        this.opt = opt;
        this.bet = bet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public int getOpt() {
        return opt;
    }

    public void setOpt(int opt) {
        this.opt = opt;
    }

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }
}
