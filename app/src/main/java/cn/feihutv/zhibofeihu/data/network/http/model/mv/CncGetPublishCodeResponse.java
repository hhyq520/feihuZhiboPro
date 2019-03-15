package cn.feihutv.zhibofeihu.data.network.http.model.mv;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/11/15
 *     desc   : 网宿请求播放代码请求
 *     version: 1.0
 * </pre>
 */
public class CncGetPublishCodeResponse {


    @SerializedName("code")
    private  String code;// 200：成功；

    @SerializedName("message")
    private  String message;//


    @SerializedName("data")
    private  CncGetPublishCodeData mCncGetPublishCodeData;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CncGetPublishCodeData getCncGetPublishCodeData() {
        return mCncGetPublishCodeData;
    }

    public void setCncGetPublishCodeData(CncGetPublishCodeData cncGetPublishCodeData) {
        mCncGetPublishCodeData = cncGetPublishCodeData;
    }

    public static class CncGetPublishCodeData{


        @SerializedName("encrypt")
        private  int encrypt;//视频是否加密：1为加密，0为不加密

        @SerializedName("swfCode")
        private  String swfCode;//

        @SerializedName("jsCode")
        private  String jsCode;//


    @SerializedName("htmlCode")
    private  String htmlCode;//

        @SerializedName("videoUrl")
        private List<CncVideoUrlData> mVideoUrlData;//

        public int getEncrypt() {
            return encrypt;
        }

        public void setEncrypt(int encrypt) {
            this.encrypt = encrypt;
        }

        public String getSwfCode() {
            return swfCode;
        }

        public void setSwfCode(String swfCode) {
            this.swfCode = swfCode;
        }

        public String getJsCode() {
            return jsCode;
        }

        public void setJsCode(String jsCode) {
            this.jsCode = jsCode;
        }


        public List<CncVideoUrlData> getVideoUrlData() {
            return mVideoUrlData;
        }

        public void setVideoUrlData(List<CncVideoUrlData> videoUrlData) {
            mVideoUrlData = videoUrlData;
        }

        @Override
        public String toString() {
            return "CncGetPublishCodeData{" +
                    "encrypt=" + encrypt +
                    ", swfCode='" + swfCode + '\'' +
                    ", jsCode='" + jsCode + '\'' +
                    ", htmlCode='" + htmlCode + '\'' +
                    ", mVideoUrlData=" + mVideoUrlData +
                    '}';
        }
    }



    public static class CncVideoUrlData{

        @SerializedName("urlType")
        private  String urlType;//PC端、移动端

        @SerializedName("fluentUrl")
        private  String fluentUrl;//流畅码率视频url


        @SerializedName("sdUrl")
        private  String sdUrl;//标清码率视频url


        @SerializedName("highUrl")
        private  String highUrl;//高清码率视频url

        @SerializedName("hdPullUrl")
        private  String hdPullUrl;//标清码率视频url


        @SerializedName("originUrl")
        private  String originUrl;//标清码率视频url

        public String getOriginUrl() {
            return originUrl;
        }

        public void setOriginUrl(String originUrl) {
            this.originUrl = originUrl;
        }

        public String getUrlType() {
            return urlType;
        }

        public void setUrlType(String urlType) {
            this.urlType = urlType;
        }

        public String getFluentUrl() {
            return fluentUrl;
        }

        public void setFluentUrl(String fluentUrl) {
            this.fluentUrl = fluentUrl;
        }

        public String getSdUrl() {
            return sdUrl;
        }

        public void setSdUrl(String sdUrl) {
            this.sdUrl = sdUrl;
        }

        public String getHighUrl() {
            return highUrl;
        }

        public void setHighUrl(String highUrl) {
            this.highUrl = highUrl;
        }

        public String getHdPullUrl() {
            return hdPullUrl;
        }

        public void setHdPullUrl(String hdPullUrl) {
            this.hdPullUrl = hdPullUrl;
        }

        @Override
        public String toString() {
            return "CncVideoUrlData{" +
                    "urlType='" + urlType + '\'' +
                    ", fluentUrl='" + fluentUrl + '\'' +
                    ", sdUrl='" + sdUrl + '\'' +
                    ", highUrl='" + highUrl + '\'' +
                    ", hdPullUrl='" + hdPullUrl + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "CncGetPublishCodeResponse{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", mCncGetPublishCodeData=" + mCncGetPublishCodeData +
                '}';
    }
}
