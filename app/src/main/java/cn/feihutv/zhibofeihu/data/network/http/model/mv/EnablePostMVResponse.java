package cn.feihutv.zhibofeihu.data.network.http.model.mv;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/11/28
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class EnablePostMVResponse extends BaseResponse{


    @Expose
    @SerializedName("Data")
    private  boolean  isEnable ;//是否可行

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }
}
