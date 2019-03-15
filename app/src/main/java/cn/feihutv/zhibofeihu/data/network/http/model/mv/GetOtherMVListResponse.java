package cn.feihutv.zhibofeihu.data.network.http.model.mv;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/11/14
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class GetOtherMVListResponse extends BaseResponse {

    @Expose
    @SerializedName("Data")
    private  GetOtherMVListData mGetOtherMVListData;

    public GetOtherMVListData getGetOtherMVListData() {
        return mGetOtherMVListData;
    }

    public void setGetOtherMVListData(GetOtherMVListData getOtherMVListData) {
        mGetOtherMVListData = getOtherMVListData;
    }

    public static class GetOtherMVListData{

        @Expose
        @SerializedName("NextOffset")
        private  String  nextOffset;//加载条数（最多20）

        @Expose
        @SerializedName("List")
        private List<GetMyCustomMadeMVListResponse.GetMyCustomMadeMVList>  mGetOtherMVLists;//加载条数（最多20）

        public String getNextOffset() {
            return nextOffset;
        }

        public void setNextOffset(String nextOffset) {
            this.nextOffset = nextOffset;
        }

        public List<GetMyCustomMadeMVListResponse.GetMyCustomMadeMVList> getGetOtherMVLists() {
            return mGetOtherMVLists;
        }

        public void setGetOtherMVLists(List<GetMyCustomMadeMVListResponse.GetMyCustomMadeMVList> getOtherMVLists) {
            mGetOtherMVLists = getOtherMVLists;
        }
    }


}
