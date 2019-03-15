package cn.feihutv.zhibofeihu.data.network.http.model.me;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/8 21:26
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class CheckGuardInfoResponse extends BaseResponse {


    /**
     * Data : {"Avatar":"http://img.feihutv.cn//headIcon/11332484/5960ddff2a17e437a30c51d7.png","ExpiredTime":1510191701,"Nickname":"德森比尔","Uid":"11332484"}
     */

    @Expose
    @SerializedName("Data")
    private CheckGuardInfoResponseData mCheckGuardInfoResponseData;

    public CheckGuardInfoResponseData getCheckGuardInfoResponseData() {
        return mCheckGuardInfoResponseData;
    }

    public void setCheckGuardInfoResponseData(CheckGuardInfoResponseData checkGuardInfoResponseData) {
        mCheckGuardInfoResponseData = checkGuardInfoResponseData;
    }

    public static class CheckGuardInfoResponseData {
        /**
         * Avatar : http://img.feihutv.cn//headIcon/11332484/5960ddff2a17e437a30c51d7.png
         * ExpiredTime : 1510191701
         * Nickname : 德森比尔
         * Uid : 11332484
         * GuardType:0 0:未开通 1：银虎 2：金虎
         */

        @Expose
        @SerializedName("Avatar")
        private String avatar;
        @Expose
        @SerializedName("ExpiredTime")
        private long expiredTime;
        @Expose
        @SerializedName("Nickname")
        private String nickname;

        @Expose
        @SerializedName("Uid")
        private String uid;

        @Expose
        @SerializedName("GuardType")
        private int guardType;


        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public long getExpiredTime() {
            return expiredTime;
        }

        public void setExpiredTime(long expiredTime) {
            this.expiredTime = expiredTime;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public int getGuardType() {
            return guardType;
        }

        public void setGuardType(int guardType) {
            this.guardType = guardType;
        }
    }
}
