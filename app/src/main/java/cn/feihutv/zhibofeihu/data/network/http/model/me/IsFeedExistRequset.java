package cn.feihutv.zhibofeihu.data.network.http.model.me;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/21 15:24
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class IsFeedExistRequset {

    @Expose
    @SerializedName("id")
    private String id;  // 动态id

    public IsFeedExistRequset(String id) {
        this.id = id;
    }
}
