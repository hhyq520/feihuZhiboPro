package cn.feihutv.zhibofeihu.data.network.http.model.mv;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/11/14
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class GiftMVRequest {

    @Expose
    @SerializedName("mvId")
    private  String mvId;//


    @Expose
    @SerializedName("giftId")
    private  String  giftId;

    @Expose
    @SerializedName("giftCnt")
    private  String  giftCnt;

    public GiftMVRequest(String mvId, String giftId, String giftCnt) {
        this.mvId = mvId;
        this.giftId = giftId;
        this.giftCnt = giftCnt;
    }


    public String getMvId() {
        return mvId;
    }

    public void setMvId(String mvId) {
        this.mvId = mvId;
    }

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getGiftCnt() {
        return giftCnt;
    }

    public void setGiftCnt(String giftCnt) {
        this.giftCnt = giftCnt;
    }
}
