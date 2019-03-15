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
 *      time   : 2017/11/3 14:58
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class GetPayListResponse extends BaseResponse {


    @Expose
    @SerializedName("Data")
    private List<GetPayListResponseData> mResponseDatas;

    public List<GetPayListResponseData> getResponseDatas() {
        return mResponseDatas;
    }

    public void setResponseDatas(List<GetPayListResponseData> responseDatas) {
        mResponseDatas = responseDatas;
    }

    public static class GetPayListResponseData {
        /**
         * Amount : 6
         * Createtime : 1501580120
         * HB : 600
         * Id : 323
         * Pf : weixin
         * Status : 1
         */

        @Expose
        @SerializedName("Amount")
        private int amount;
        @Expose
        @SerializedName("Createtime")
        private int createtime;
        @Expose
        @SerializedName("HB")
        private int hB;
        @Expose
        @SerializedName("Id")
        private int id;
        @Expose
        @SerializedName("Pf")
        private String pf;
        @Expose
        @SerializedName("Status")
        private int status;

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public int getCreatetime() {
            return createtime;
        }

        public void setCreatetime(int createtime) {
            this.createtime = createtime;
        }

        public int gethB() {
            return hB;
        }

        public void sethB(int hB) {
            this.hB = hB;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPf() {
            return pf;
        }

        public void setPf(String pf) {
            this.pf = pf;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
