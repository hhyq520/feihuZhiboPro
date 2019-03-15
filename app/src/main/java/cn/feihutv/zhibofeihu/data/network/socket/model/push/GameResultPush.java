package cn.feihutv.zhibofeihu.data.network.socket.model.push;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/10/16
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class GameResultPush extends  BasePush{

    @Expose
    @SerializedName("Result")
    private List<Integer> result;

    @Expose
    @SerializedName("GameName")
    private String gameName;

    @Expose
    @SerializedName("IssueRound")
    private int issueRound;

    @Expose
    @SerializedName("OpenStyle")
    private int openStyle;

    public List<Integer> getResult() {
        return result;
    }

    public void setResult(List<Integer> result) {
        this.result = result;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public int getIssueRound() {
        return issueRound;
    }

    public void setIssueRound(int issueRound) {
        this.issueRound = issueRound;
    }

    public int getOpenStyle() {
        return openStyle;
    }

    public void setOpenStyle(int openStyle) {
        this.openStyle = openStyle;
    }
}
