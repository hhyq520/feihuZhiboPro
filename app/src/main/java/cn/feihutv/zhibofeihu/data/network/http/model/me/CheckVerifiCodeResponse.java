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
 *      time   : 2017/11/1 17:54
 *      desc   : 检查原手机号码
 *      version: 1.0
 * </pre>
 */

public class CheckVerifiCodeResponse extends BaseResponse {


    /**
     * Data : false
     */

    @Expose
    @SerializedName("Data")
    private boolean mBoolean;


    public boolean isBoolean() {
        return mBoolean;
    }

    public void setBoolean(boolean aBoolean) {
        mBoolean = aBoolean;
    }
}
