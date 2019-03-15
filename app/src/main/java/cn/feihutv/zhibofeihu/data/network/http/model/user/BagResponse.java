package cn.feihutv.zhibofeihu.data.network.http.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.common.CheckUpdateResponse;

/**
 * Created by huanghao on 2017/10/9.
 */

public class BagResponse extends BaseResponse{
    @Expose
    @SerializedName("Data")
    private List<BagResponseData> mBagResponseDataList;

    public List<BagResponseData> getBagResponseData() {
        return mBagResponseDataList;
    }

    public void setBagResponseData(List<BagResponseData> bagResponseDataList) {
        mBagResponseDataList = bagResponseDataList;
    }
    public static class BagResponseData{
        @Expose
        @SerializedName("Id")
        private int Id;

        @Expose
        @SerializedName("Cnt")
        private int Cnt;

        public int getId() {
            return Id;
        }

        public void setId(int id) {
            Id = id;
        }

        public int getCnt() {
            return Cnt;
        }

        public void setCnt(int cnt) {
            Cnt = cnt;
        }
    }
}
