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

public class SignResponse extends BaseResponse{
    /**
     * Data : {"Awards":[{"type":4,"id":0,"cnt":10},{"type":5,"id":0,"cnt":10}],"Days":1,"Signed":false}
     */

    @Expose
    @SerializedName("Data")
    private SignData mSignData;

    public SignData getSignData() {
        return mSignData;
    }

    public void setSignData(SignData signData) {
        mSignData = signData;
    }

    public static class SignData {
        /**
         * Awards : [{"type":4,"id":0,"cnt":10},{"type":5,"id":0,"cnt":10}]
         * Days : 1
         * Signed : false
         */

        @Expose
        @SerializedName("Days")
        private int days;
        @Expose
        @SerializedName("Signed")
        private boolean signed;
        @Expose
        @SerializedName("Awards")
        private List<AwardsResponseData> mAwardsBeen;

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

        public List<AwardsResponseData> getAwardsBeen() {
            return mAwardsBeen;
        }

        public void setAwardsBeen(List<AwardsResponseData> awardsBeen) {
            mAwardsBeen = awardsBeen;
        }

        public static class AwardsResponseData {
            /**
             * type : 4
             * id : 0
             * cnt : 10
             */

            @Expose
            @SerializedName("type")
            private int type;
            @Expose
            @SerializedName("id")
            private int id;
            @Expose
            @SerializedName("cnt")
            private int cnt;

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getCnt() {
                return cnt;
            }

            public void setCnt(int cnt) {
                this.cnt = cnt;
            }
        }
    }
}
