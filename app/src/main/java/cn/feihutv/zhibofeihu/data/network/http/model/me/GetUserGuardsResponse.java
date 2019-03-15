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
 *      time   : 2017/11/7 14:28
 *      desc   : 我的守护
 *      version: 1.0
 * </pre>
 */

public class GetUserGuardsResponse extends BaseResponse {


    @Expose
    @SerializedName("Data")
    private List<GetUserGuardsResponseData> mGuardsResponseDatas;

    public List<GetUserGuardsResponseData> getGuardsResponseDatas() {
        return mGuardsResponseDatas;
    }

    public void setGuardsResponseDatas(List<GetUserGuardsResponseData> guardsResponseDatas) {
        mGuardsResponseDatas = guardsResponseDatas;
    }

    public static class GetUserGuardsResponseData {
        /**
         * Avatar : https://img.feihutv.cn/r/2923.jpg
         * Friendliness : 0
         * Nickname : r0000006
         * Rank : 1
         * UserId : 11334599
         * Level : 1
         */

        @Expose
        @SerializedName("Avatar")
        private String avatar;
        @Expose
        @SerializedName("Friendliness")
        private int friendliness;
        @Expose
        @SerializedName("Nickname")
        private String nickname;
        @Expose
        @SerializedName("Rank")
        private int rank;
        @Expose
        @SerializedName("UserId")
        private String userId;
        @Expose
        @SerializedName("Level")
        private int level;

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getFriendliness() {
            return friendliness;
        }

        public void setFriendliness(int friendliness) {
            this.friendliness = friendliness;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
