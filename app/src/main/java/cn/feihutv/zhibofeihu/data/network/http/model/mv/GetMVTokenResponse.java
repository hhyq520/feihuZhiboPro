package cn.feihutv.zhibofeihu.data.network.http.model.mv;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/11/11
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class GetMVTokenResponse extends BaseResponse{


    @Expose
    @SerializedName("Data")
    private  GetMVTokenData mGetMVTokenData;

    public GetMVTokenData getGetMVTokenData() {
        return mGetMVTokenData;
    }

    public void setGetMVTokenData(GetMVTokenData getMVTokenData) {
        mGetMVTokenData = getMVTokenData;
    }

    public static class GetMVTokenData{


        @Expose
        @SerializedName("userId")
        private  String userId ; //网宿云点播userId



        @Expose
        @SerializedName("token")
        private  String token; //网宿云点播userId



        @Expose
        @SerializedName("timeStamp")
        private int   timeStamp; //时间戳


        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public int getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(int timeStamp) {
            this.timeStamp = timeStamp;
        }
    }



}
