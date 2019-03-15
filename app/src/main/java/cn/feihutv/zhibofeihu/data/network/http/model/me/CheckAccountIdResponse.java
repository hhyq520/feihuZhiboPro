package cn.feihutv.zhibofeihu.data.network.http.model.me;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/8 21:16
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class CheckAccountIdResponse extends BaseResponse {

    /**
     * Data : {"Nickname":"德森比尔","Uid":"11332484"}
     */

    @Expose
    @SerializedName("Data")
    private CheckAccountIdResponseData mCheckAccountIdResponseData;

    public CheckAccountIdResponseData getCheckAccountIdResponseData() {
        return mCheckAccountIdResponseData;
    }

    public void setCheckAccountIdResponseData(CheckAccountIdResponseData checkAccountIdResponseData) {
        mCheckAccountIdResponseData = checkAccountIdResponseData;
    }

    public static class CheckAccountIdResponseData {
        /**
         * Nickname : 德森比尔
         * Uid : 11332484
         * Avatar
         */

        @Expose
        @SerializedName("Nickname")
        private String nickname;
        @Expose
        @SerializedName("Uid")
        private String uid;

        @Expose
        @SerializedName("Avatar")
        private String avatar;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
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
    }
}
