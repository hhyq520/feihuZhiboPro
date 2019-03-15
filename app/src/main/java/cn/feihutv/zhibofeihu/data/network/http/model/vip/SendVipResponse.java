package cn.feihutv.zhibofeihu.data.network.http.model.vip;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/10/31
 *     desc   : 赠送好友VIP
 *     version: 1.0
 * </pre>
 */
public class SendVipResponse extends BaseResponse {


    @Expose
    @SerializedName("Data")
    private SendVipData mSendVipData;


    public static class SendVipData {

        @Expose
        @SerializedName("Vip")
        private int vip;     //购买后的VIP等级

        @Expose
        @SerializedName("VipCZZ")
        private int vipCZZ;  //购买后的成长值

        @Expose
        @SerializedName("Xiaolaba")
        private int xiaolaba;  //购买后的小喇叭数量

        public int getVip() {
            return vip;
        }

        public void setVip(int vip) {
            this.vip = vip;
        }

        public int getVipCZZ() {
            return vipCZZ;
        }

        public void setVipCZZ(int vipCZZ) {
            this.vipCZZ = vipCZZ;
        }

        public int getXiaolaba() {
            return xiaolaba;
        }

        public void setXiaolaba(int xiaolaba) {
            this.xiaolaba = xiaolaba;
        }
    }
}
