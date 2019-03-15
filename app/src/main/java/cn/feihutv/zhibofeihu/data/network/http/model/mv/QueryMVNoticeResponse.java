package cn.feihutv.zhibofeihu.data.network.http.model.mv;

import com.google.gson.annotations.SerializedName;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/12/08
 *     desc   : 查询是否有mv系统消息
 *     version: 1.0
 * </pre>
 */
public class QueryMVNoticeResponse extends BaseResponse{


    @SerializedName("Data")
    private  int mCount;//

    public int getCount() {
        return mCount;
    }

    public void setCount(int count) {
        mCount = count;
    }
}
