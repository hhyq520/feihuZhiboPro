package cn.feihutv.zhibofeihu.data.network.http.model.me;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/10/26
 *     desc   : 社区-获取动态列表
 *     version: 1.0
 * </pre>
 */
public class LoadFeedListResponse  extends BaseResponse {



    @Expose
    @SerializedName("Data")
    public List<DynamicItem> mDynamicItemList; //   空或无表示自己


    public List<DynamicItem> getDynamicItemList() {
        return mDynamicItemList;
    }

    public void setDynamicItemList(List<DynamicItem> dynamicItemList) {
        mDynamicItemList = dynamicItemList;
    }
}
