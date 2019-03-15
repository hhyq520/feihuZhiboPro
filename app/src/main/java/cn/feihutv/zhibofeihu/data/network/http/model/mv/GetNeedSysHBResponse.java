package cn.feihutv.zhibofeihu.data.network.http.model.mv;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/11/16
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class GetNeedSysHBResponse extends BaseResponse{


    @Expose
    @SerializedName("Data")
    private List<String> mHbList;

    public List<String> getHbList() {
        return mHbList;
    }

    public void setHbList(List<String> hbList) {
        mHbList = hbList;
    }
}
