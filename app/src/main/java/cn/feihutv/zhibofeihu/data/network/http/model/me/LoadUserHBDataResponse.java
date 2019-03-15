package cn.feihutv.zhibofeihu.data.network.http.model.me;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/10/27
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class LoadUserHBDataResponse extends BaseResponse{
    @Expose
    @SerializedName("Data")
    public UserHBData mUserHBData; //

    public UserHBData getUserHBData() {
        return mUserHBData;
    }

    public void setUserHBData(UserHBData userHBData) {
        mUserHBData = userHBData;
    }

    public static class UserHBData{

        @Expose
        @SerializedName("Income")
        public String Income; //


        @Expose
        @SerializedName("Contri")
        public String Contri; //

        public String getIncome() {
            return Income;
        }

        public void setIncome(String income) {
            Income = income;
        }

        public String getContri() {
            return Contri;
        }

        public void setContri(String contri) {
            Contri = contri;
        }
    }





}
