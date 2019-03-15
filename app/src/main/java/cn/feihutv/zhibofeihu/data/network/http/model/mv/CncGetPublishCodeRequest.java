package cn.feihutv.zhibofeihu.data.network.http.model.mv;

import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/11/15
 *     desc   : 网宿请求播放代码请求
 *     version: 1.0
 * </pre>
 */
public class CncGetPublishCodeRequest {


    @SerializedName("userId")
    private  String userId;//

    @SerializedName("token")
    private  String token;//

    @SerializedName("timeStamp")
    private  int timeStamp;//

    @SerializedName("videoId")
    private  String videoId;//

    @SerializedName("codeType")
    private  int codeType;//


    @SerializedName("format")
    private  String format;//


    public CncGetPublishCodeRequest(String userId, String token, int timeStamp, String videoId) {
        this.userId = userId;
        this.token = token;
        this.timeStamp = timeStamp;
        this.videoId = videoId;
    }


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

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public int getCodeType() {
        return codeType;
    }

    public void setCodeType(int codeType) {
        this.codeType = codeType;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
