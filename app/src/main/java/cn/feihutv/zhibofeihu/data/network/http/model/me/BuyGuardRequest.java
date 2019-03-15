package cn.feihutv.zhibofeihu.data.network.http.model.me;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/9 09:58
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class BuyGuardRequest {

    @Expose
    @SerializedName("roomId")
    private String roomId;

    @Expose
    @SerializedName("goodsId")
    private int goodsId;

    @Expose
    @SerializedName("huType")
    private int huType;

    public BuyGuardRequest(String roomId, int goodsId, int huType) {
        this.roomId = roomId;
        this.goodsId = goodsId;
        this.huType = huType;
    }
}
