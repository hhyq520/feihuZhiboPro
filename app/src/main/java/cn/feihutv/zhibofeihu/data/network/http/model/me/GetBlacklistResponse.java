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
 *      time   : 2017/11/6 14:26
 *      desc   : 拉取黑名单
 *      version: 1.0
 * </pre>
 */

public class GetBlacklistResponse extends BaseResponse {


    @Expose
    @SerializedName("Data")
    private List<GetBlacklistResponseData> mBlacklistResponseDatas;

    public List<GetBlacklistResponseData> getBlacklistResponseDatas() {
        return mBlacklistResponseDatas;
    }

    public void setBlacklistResponseDatas(List<GetBlacklistResponseData> blacklistResponseDatas) {
        mBlacklistResponseDatas = blacklistResponseDatas;
    }

    public static class GetBlacklistResponseData {
        /**
         * UserId : 11335867
         * NickName : 骑行者
         * HeadUrl : https://img.feihutv.cn/uploadHtml/images/roomBg.png
         * AccountId : 24840111
         * Gender : 1
         * Level : 33
         */

        @Expose
        @SerializedName("UserId")
        private String userId;
        @Expose
        @SerializedName("NickName")
        private String nickName;
        @Expose
        @SerializedName("HeadUrl")
        private String headUrl;
        @Expose
        @SerializedName("AccountId")
        private String accountId;
        @Expose
        @SerializedName("Gender")
        private int gender;
        @Expose
        @SerializedName("Level")
        private int level;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }
    }
}
