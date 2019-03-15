package cn.feihutv.zhibofeihu.data.network.http.model.common;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * Created by huanghao on 2017/10/10.
 */

public class LoadSignResponce extends BaseResponse{
    /**
     * Data : {"Days":0,"Signed":false}
     */

    @Expose
    @SerializedName("Data")
    private LoadSignData mSignData;

    @Override
    public String toString() {
        return "LoadSignResponce{" +
                "mSignData=" + mSignData +
                '}';
    }

    public LoadSignData getSignData() {
        return mSignData;
    }

    public void setSignData(LoadSignData signData) {
        mSignData = signData;
    }

    public static class LoadSignData {
        /**
         * Days :   本轮已签到天数
         * Signed : true今日已签 false今日未签
         */

        @Expose
        @SerializedName("Days")
        private int days;

        @Expose
        @SerializedName("Signed")
        private boolean signed;

        public int getDays() {
            return days;
        }

        public void setDays(int days) {
            this.days = days;
        }

        public boolean isSigned() {
            return signed;
        }

        public void setSigned(boolean signed) {
            this.signed = signed;
        }


        @Override
        public String toString() {
            return "LoadSignData{" +
                    "days=" + days +
                    ", signed=" + signed +
                    '}';
        }
    }

}
