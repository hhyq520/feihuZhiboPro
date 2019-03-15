package cn.feihutv.zhibofeihu.data.network.http.model.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by clw on 2017/10/22.
 */

public class SearchRequest {

    @Expose
    @SerializedName("q")
    private String accountId;

    public SearchRequest(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
