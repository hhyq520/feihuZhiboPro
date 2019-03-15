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
 *     author : sichu.chen
 *     time   : 2017/10/26
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class PostFeedCommentResponse extends BaseResponse {


    /**
     * Data : 5a2513f32a17e4270d4baf2b
     */

    @Expose
    @SerializedName("Data")
    private String Data;


    public String getData() {
        return Data;
    }

    public void setData(String Data) {
        this.Data = Data;
    }
}


