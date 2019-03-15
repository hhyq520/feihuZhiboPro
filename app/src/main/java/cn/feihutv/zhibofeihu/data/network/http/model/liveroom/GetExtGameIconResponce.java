package cn.feihutv.zhibofeihu.data.network.http.model.liveroom;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * Created by huanghao on 2017/11/30.
 */

public class GetExtGameIconResponce extends BaseResponse{

    @Expose
    @SerializedName("Data")
    private List<ExtGameIconData> extGameIconDatas;

    public List<ExtGameIconData> getExtGameIconDatas() {
        return extGameIconDatas;
    }

    public void setExtGameIconDatas(List<ExtGameIconData> extGameIconDatas) {
        this.extGameIconDatas = extGameIconDatas;
    }

    public static class ExtGameIconData{
        @Expose
        @SerializedName("BeginTime")
        private long BeginTime;
        @Expose
        @SerializedName("EndTime")
        private long EndTime;
        @Expose
        @SerializedName("GameIcon")
        private String GameIcon;
        @Expose
        @SerializedName("GameName")
        private String GameName;
        @Expose
        @SerializedName("HtmlUrl")
        private String HtmlUrl;
        @Expose
        @SerializedName("Sort")
        private int Sort;

        public long getBeginTime() {
            return BeginTime;
        }

        public void setBeginTime(long beginTime) {
            BeginTime = beginTime;
        }

        public long getEndTime() {
            return EndTime;
        }

        public void setEndTime(long endTime) {
            EndTime = endTime;
        }

        public String getGameIcon() {
            return GameIcon;
        }

        public void setGameIcon(String gameIcon) {
            GameIcon = gameIcon;
        }

        public String getGameName() {
            return GameName;
        }

        public void setGameName(String gameName) {
            GameName = gameName;
        }

        public String getHtmlUrl() {
            return HtmlUrl;
        }

        public void setHtmlUrl(String htmlUrl) {
            HtmlUrl = htmlUrl;
        }

        public int getSort() {
            return Sort;
        }

        public void setSort(int sort) {
            Sort = sort;
        }
    }
}
