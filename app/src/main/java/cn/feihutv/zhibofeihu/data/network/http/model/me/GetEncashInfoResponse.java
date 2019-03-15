package cn.feihutv.zhibofeihu.data.network.http.model.me;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/3 16:33
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class GetEncashInfoResponse extends BaseResponse {


    /**
     * Data : {"AlipayAccount":"","GHB":"53546","Increase":"1"}
     */

    @Expose
    @SerializedName("Data")
    private GetEncashInfoResponseData mEncashInfoResponseData;

    public GetEncashInfoResponseData getEncashInfoResponseData() {
        return mEncashInfoResponseData;
    }

    public void setEncashInfoResponseData(GetEncashInfoResponseData encashInfoResponseData) {
        mEncashInfoResponseData = encashInfoResponseData;
    }

    public static class GetEncashInfoResponseData {
        /**
         * AlipayAccount :
         * GHB : 53546
         * Increase : 1
         */

        @Expose
        @SerializedName("AlipayAccount")
        private String alipayAccount;
        @Expose
        @SerializedName("GHB")
        private int gHB;
        @Expose
        @SerializedName("Increase")
        private String increase;

        public String getAlipayAccount() {
            return alipayAccount;
        }

        public void setAlipayAccount(String alipayAccount) {
            this.alipayAccount = alipayAccount;
        }

        public int getgHB() {
            return gHB;
        }

        public void setgHB(int gHB) {
            this.gHB = gHB;
        }

        public String getIncrease() {
            return increase;
        }

        public void setIncrease(String increase) {
            this.increase = increase;
        }
    }
}
