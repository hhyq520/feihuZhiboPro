package cn.feihutv.zhibofeihu.data.network.http.model.me;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/8 16:57
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class LoadShopAccountIdListResponse extends BaseResponse {


    @Expose
    @SerializedName("Data")
    private List<LoadShopAccountIdListResponseData> mAccountIdListResponseDatas;


    public List<LoadShopAccountIdListResponseData> getAccountIdListResponseDatas() {
        return mAccountIdListResponseDatas;
    }

    public void setAccountIdListResponseDatas(List<LoadShopAccountIdListResponseData> accountIdListResponseDatas) {
        mAccountIdListResponseDatas = accountIdListResponseDatas;
    }

    public static class LoadShopAccountIdListResponseData {
        /**
         * AccountId : 66908888
         * Id : 0
         * Price : 80000
         */

        @Expose
        @SerializedName("AccountId")
        private String accountId;
        @Expose
        @SerializedName("Id")
        private int id;
        @Expose
        @SerializedName("Price")
        private int price;

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }
    }
}
