package cn.feihutv.zhibofeihu.data.network.socket.model.push;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by huanghao on 2017/12/7.
 */

public class RoomIncomePush extends BasePush{
    @Expose
    @SerializedName("Income")
    private long Income;

    public long getIncome() {
        return Income;
    }

    public void setIncome(long income) {
        Income = income;
    }
}
