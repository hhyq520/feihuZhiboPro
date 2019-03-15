package cn.feihutv.zhibofeihu.data.network.http.model.bangdan;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/10/27 10:31
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class LoadLuckRankListResponse extends BaseResponse {


    @Expose
    @SerializedName("Data")
    private List<LoadLuckRankListResponseData> mResponseDatas;

    public List<LoadLuckRankListResponseData> getResponseDatas() {
        return mResponseDatas;
    }

    public void setResponseDatas(List<LoadLuckRankListResponseData> responseDatas) {
        mResponseDatas = responseDatas;
    }

    public static class LoadLuckRankListResponseData {
        /**
         * AwardCnt : 276
         * HeadUrl : http://q.qlogo.cn/qqapp/1106077402/9FB5F3B4FDEE8A4C7F40151D4FC99645/100
         * Level : 1
         * NickName : 一人一半
         * ObtainHB : 1070264
         * Rank : 1
         * UserId : 11333235
         */

        @Expose
        @SerializedName("AwardCnt")
        private int awardCnt;
        @Expose
        @SerializedName("HeadUrl")
        private String headUrl;
        @Expose
        @SerializedName("Level")
        private int level;
        @Expose
        @SerializedName("NickName")
        private String nickName;
        @Expose
        @SerializedName("ObtainHB")
        private long obtainHB;
        @Expose
        @SerializedName("Rank")
        private int rank;
        @Expose
        @SerializedName("UserId")
        private String userId;
        @Expose
        @SerializedName("BroadcastType")
        private int broadcastType;

        @Expose
        @SerializedName("LiveStatus")
        private boolean liveStatus;

        public int getAwardCnt() {
            return awardCnt;
        }

        public void setAwardCnt(int awardCnt) {
            this.awardCnt = awardCnt;
        }

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public long getObtainHB() {
            return obtainHB;
        }

        public void setObtainHB(long obtainHB) {
            this.obtainHB = obtainHB;
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

        public int getBroadcastType() {
            return broadcastType;
        }

        public void setBroadcastType(int broadcastType) {
            this.broadcastType = broadcastType;
        }

        public boolean isLiveStatus() {
            return liveStatus;
        }

        public void setLiveStatus(boolean liveStatus) {
            this.liveStatus = liveStatus;
        }
    }
}
