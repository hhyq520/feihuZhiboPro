package cn.feihutv.zhibofeihu.data.network.http.model.vip;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/10/31
 *     desc   : 赠送好友VIP
 *     version: 1.0
 * </pre>
 */
public class SendVipRequest {


    @Expose
    @SerializedName("goodsId")
    private int goodsId;


    @Expose
    @SerializedName("to")
    private String toUser; //接收者


    public SendVipRequest(int goodsId, String toUser) {
        this.goodsId = goodsId;
        this.toUser = toUser;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }
}
