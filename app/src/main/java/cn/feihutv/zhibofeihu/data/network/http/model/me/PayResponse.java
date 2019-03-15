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
 *      time   : 2017/11/3 10:32
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class PayResponse extends BaseResponse {


    /**
     * Data : biz_content=%7B%22out_trade_no%22%3A%2215242%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E8%99%8E%E5%B8%81%C3%972000%22%2C%22total_amount%22%3A%2220%22%7D&sign=nHN43v4sv1ymDboDHhfIu8sKmOJdacEFcA1DzyqZ7W6mRAEMBlJ892m2HylrwMRMyzGqUopR7lFBhN1kwSEnRLIBVi0RQj1xEcrdvKCxsKRZAB3qM2eEdjG7%2BQHJDy7jtFYkY6RMpTXOMNt3pXNvozYCLAwO5%2FUUIUSueuvCzFpxdusSQLF%2Bnfg%2Ffn6uR9xxph29GoZe588VxTr37yMCszU8Q7wzQGEYr%2Bxndwqg2y%2FPCzg9o4KeeIZQ8yG2z3u%2Fg2EXRk1VnP1O9tXE7fBRyh5CW%2FsZeIQqHIojTHwSSQT65Z9L1uWBD0N6yiPV3G%2F0mqptof8EiF0oV33lGyPG7w%3D%3D&timestamp=2017-11-02+20%3A45%3A12&notify_url=https%3A%2F%2Fmm.feihutv.cn%2Fpay%2Falipay&version=1.0&app_id=2017032206344196&method=alipay.trade.app.pay&charset=utf-8&sign_type=RSA2
     */

    @Expose
    @SerializedName("Data")
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
