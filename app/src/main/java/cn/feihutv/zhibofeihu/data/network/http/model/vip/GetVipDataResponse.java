package cn.feihutv.zhibofeihu.data.network.http.model.vip;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/10/31
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class GetVipDataResponse extends BaseResponse {



    @Expose
    @SerializedName("Data")
    private GetVipData  mGetVipData;

    public GetVipData getGetVipData() {
        return mGetVipData;
    }

    public void setGetVipData(GetVipData getVipData) {
        mGetVipData = getVipData;
    }


    public static class GetVipData{

        @Expose
        @SerializedName("Vip")
        private int vip;  //当前VIP等级


        @Expose
        @SerializedName("VipCZZ")
        private int vipCZZ;  //成长值

        @Expose
        @SerializedName("VipExpireTime")
        private long vipExpireTime;   //VIP过期时间


        @Expose
        @SerializedName("ExpiredDaysLater")
        private boolean   expiredDaysLater; //true提示即将过期  false不提示

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

        public long getVipExpireTime() {
            return vipExpireTime;
        }

        public void setVipExpireTime(long vipExpireTime) {
            this.vipExpireTime = vipExpireTime;
        }

        public boolean isExpiredDaysLater() {
            return expiredDaysLater;
        }

        public void setExpiredDaysLater(boolean expiredDaysLater) {
            this.expiredDaysLater = expiredDaysLater;
        }
    }

}
