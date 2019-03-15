package cn.feihutv.zhibofeihu.data.network.http.model.wanfa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by huanghao on 2017/10/19.
 */

public class GetGameRoundResultDetailRequest {
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("issueRound")
    private int issueRound;

    public GetGameRoundResultDetailRequest(String name, int issueRound) {
        this.name = name;
        this.issueRound = issueRound;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIssueRound() {
        return issueRound;
    }

    public void setIssueRound(int issueRound) {
        this.issueRound = issueRound;
    }
}
