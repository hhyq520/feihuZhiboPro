package cn.feihutv.zhibofeihu.data.network.http.model.me;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/14 11:01
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class LoadLiangSearchKeyResponse extends BaseResponse {


    @Expose
    @SerializedName("Data")
    private List<LoadLiangSearchKeyResponseData> mLoadLiangSearchKeyResponseDatas;

    public List<LoadLiangSearchKeyResponseData> getLoadLiangSearchKeyResponseDatas() {
        return mLoadLiangSearchKeyResponseDatas;
    }

    public void setLoadLiangSearchKeyResponseDatas(List<LoadLiangSearchKeyResponseData> loadLiangSearchKeyResponseDatas) {
        mLoadLiangSearchKeyResponseDatas = loadLiangSearchKeyResponseDatas;
    }

    public static class LoadLiangSearchKeyResponseData {
        /**
         * LiangId : 1
         * PriceStart : 50000
         * PriceEnd : 50000
         */

        @Expose
        @SerializedName("LiangId")
        private int liangId;
        @Expose
        @SerializedName("PriceStart")
        private int priceStart;
        @Expose
        @SerializedName("PriceEnd")
        private int priceEnd;

        public int getLiangId() {
            return liangId;
        }

        public void setLiangId(int liangId) {
            this.liangId = liangId;
        }

        public int getPriceStart() {
            return priceStart;
        }

        public void setPriceStart(int priceStart) {
            this.priceStart = priceStart;
        }

        public int getPriceEnd() {
            return priceEnd;
        }

        public void setPriceEnd(int priceEnd) {
            this.priceEnd = priceEnd;
        }
    }
}
