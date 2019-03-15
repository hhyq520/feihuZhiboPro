package cn.feihutv.zhibofeihu.data.network.http.model.me;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
public class LoadCommentListResponse extends BaseResponse {

    @Expose
    @SerializedName("Data")
    public List<CommenItem> mCommenItemList; //

    public List<CommenItem> getCommenItemList() {
        return mCommenItemList;
    }

    public void setCommenItemList(List<CommenItem> commenItemList) {
        mCommenItemList = commenItemList;
    }
}
