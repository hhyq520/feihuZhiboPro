package cn.feihutv.zhibofeihu.data.network.http.model.home;

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
 *      author : liwen.chen
 *      time   : 2017/10/12 10:11
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class GetBannerResponse extends BaseResponse {


    /**
     * Data : {"listJson":"[{\"BannerImg\":\"https://img.feihutv.cn/banner/1499652629.jpeg\",\"BannerDirType\":2,\"BannerDirContent\":\"http://img.feihutv.cn/uploadHtml/1499672482.html\",\"BeginTime\":1499327230,\"EndTime\":1561967230}]","serverTime":1508318421}
     */

    @Expose
    @SerializedName("Data")
    private GetBannerResponseData data;


    public GetBannerResponseData getData() {
        return data;
    }

    public void setData(GetBannerResponseData data) {
        this.data = data;
    }

    public static class GetBannerResponseData {
        /**
         * listJson : [{"BannerImg":"https://img.feihutv.cn/banner/1499652629.jpeg","BannerDirType":2,"BannerDirContent":"http://img.feihutv.cn/uploadHtml/1499672482.html","BeginTime":1499327230,"EndTime":1561967230}]
         * serverTime : 1508318421
         */

        private String listJson;
        private long serverTime;

        public String getListJson() {
            return listJson;
        }

        public void setListJson(String listJson) {
            this.listJson = listJson;
        }

        public long getServerTime() {
            return serverTime;
        }

        public void setServerTime(int serverTime) {
            this.serverTime = serverTime;
        }

    }


    public static class BannerData {

        /**
         * BannerImg : https://img.feihutv.cn/banner/1499652629.jpeg
         * BannerDirType : 2
         * BannerDirContent : http://img.feihutv.cn/uploadHtml/1499672482.html
         * BeginTime : 1499327230
         * EndTime : 1561967230
         */

        private String BannerImg;
        private int BannerDirType;
        private String BannerDirContent;
        private int BeginTime;
        private int EndTime;


        public static List<BannerData> arrayBannerDataFromData(String str) {

            Type listType = new TypeToken<ArrayList<BannerData>>() {}.getType();

            return new Gson().fromJson(str, listType);
        }

        public String getBannerImg() {
            return BannerImg;
        }

        public void setBannerImg(String BannerImg) {
            this.BannerImg = BannerImg;
        }

        public int getBannerDirType() {
            return BannerDirType;
        }

        public void setBannerDirType(int BannerDirType) {
            this.BannerDirType = BannerDirType;
        }

        public String getBannerDirContent() {
            return BannerDirContent;
        }

        public void setBannerDirContent(String BannerDirContent) {
            this.BannerDirContent = BannerDirContent;
        }

        public int getBeginTime() {
            return BeginTime;
        }

        public void setBeginTime(int BeginTime) {
            this.BeginTime = BeginTime;
        }

        public int getEndTime() {
            return EndTime;
        }

        public void setEndTime(int EndTime) {
            this.EndTime = EndTime;
        }

        @Override
        public String toString() {
            return "BannerData{" +
                    "BannerImg='" + BannerImg + '\'' +
                    ", BannerDirType=" + BannerDirType +
                    ", BannerDirContent='" + BannerDirContent + '\'' +
                    ", BeginTime=" + BeginTime +
                    ", EndTime=" + EndTime +
                    '}';
        }
    }

}
