package cn.feihutv.zhibofeihu.data.network.http.model.me;

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
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/10 20:18
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class LoadBagGiftsResponse extends BaseResponse {


    @Expose
    @SerializedName("Data")
    private List<LoadBagGiftsResponseData> mBagGiftsResponseDatas;

    @Override
    public String toString() {
        return "LoadBagGiftsResponse{" +
                "mBagGiftsResponseDatas=" + mBagGiftsResponseDatas +
                '}';
    }

    public List<LoadBagGiftsResponseData> getBagGiftsResponseDatas() {
        return mBagGiftsResponseDatas;
    }

    public void setBagGiftsResponseDatas(List<LoadBagGiftsResponseData> bagGiftsResponseDatas) {
        mBagGiftsResponseDatas = bagGiftsResponseDatas;
    }

    public static class LoadBagGiftsResponseData {
        /**
         * Cnt : 0
         * Id : 15
         */

        @Expose
        @SerializedName("Cnt")
        private int cnt;
        @Expose
        @SerializedName("Id")
        private int id;

        public int getCnt() {
            return cnt;
        }

        public void setCnt(int cnt) {
            this.cnt = cnt;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "LoadBagGiftsResponseData{" +
                    "cnt=" + cnt +
                    ", id=" + id +
                    '}';
        }
    }
}
