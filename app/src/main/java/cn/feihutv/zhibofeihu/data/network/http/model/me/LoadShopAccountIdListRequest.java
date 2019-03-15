package cn.feihutv.zhibofeihu.data.network.http.model.me;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/8 16:57
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class LoadShopAccountIdListRequest {

    @Expose
    @SerializedName("liangId")
    private int liangId;

    public LoadShopAccountIdListRequest(int liangId) {
        this.liangId = liangId;
    }
}
