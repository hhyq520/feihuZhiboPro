package cn.feihutv.zhibofeihu.data.network.http.model.liveroom;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * Created by huanghao on 2017/10/21.
 */

public class GetCurrMountResponce extends BaseResponse{
    @Expose
    @SerializedName("Data")
    private CurrMountData currMountData;

    public CurrMountData getCurrMountData() {
        return currMountData;
    }

    public void setCurrMountData(CurrMountData currMountData) {
        this.currMountData = currMountData;
    }


    public static class CurrMountData{
        @Expose
        @SerializedName("MountId")
        private int id;
        @Expose
        @SerializedName("GuardType")
        private int GuardType;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getGuardType() {
            return GuardType;
        }

        public void setGuardType(int guardType) {
            GuardType = guardType;
        }
    }
}
