package cn.feihutv.zhibofeihu.data.network.http.model.vip;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/10/31
 *     desc   : 购买vip
 *     version: 1.0
 * </pre>
 */
public class BuyVipRequest {


    @Expose
    @SerializedName("goodsId")
    private int goodsId;

    public BuyVipRequest(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }
}
