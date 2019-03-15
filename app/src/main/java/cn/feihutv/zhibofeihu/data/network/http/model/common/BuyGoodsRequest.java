package cn.feihutv.zhibofeihu.data.network.http.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by huanghao on 2017/10/23.
 */

public class BuyGoodsRequest {
    @Expose
    @SerializedName("id")
    private int id;
    @Expose
    @SerializedName("cnt")
    private int cnt;


    public BuyGoodsRequest(int id, int cnt) {
        this.id = id;
        this.cnt = cnt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }
}
