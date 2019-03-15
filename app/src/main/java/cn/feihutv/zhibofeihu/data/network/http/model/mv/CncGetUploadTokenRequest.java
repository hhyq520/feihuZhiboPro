package cn.feihutv.zhibofeihu.data.network.http.model.mv;

import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/11/21
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class CncGetUploadTokenRequest {


    @SerializedName("userId")
    private  String userId;//

    @SerializedName("token")
    private  String token;//

    @SerializedName("timeStamp")
    private  int timeStamp;//

    @SerializedName("originFileName")
    private  String originFileName;//

    @SerializedName("fileMd5")
    private  String fileMd5;//

    @SerializedName("originFileSize")
    private  String originFileSize;//


    @SerializedName("domain")
    private  String domain;//视频域名

    @SerializedName("cmd")
    private  String cmd;//一体化命令

    @SerializedName("overwrite")
    private  String overwrite;//上传策略，是否覆盖，只能为0或1

    @SerializedName("videoSource")
    private  String videoSource;//视频上传来源，要求只能为web或sdk;默认sdk


    public CncGetUploadTokenRequest(String userId, String token, int timeStamp) {
        this.userId = userId;
        this.token = token;
        this.timeStamp = timeStamp;
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

    public String getOriginFileName() {
        return originFileName;
    }

    public void setOriginFileName(String originFileName) {
        this.originFileName = originFileName;
    }

    public String getFileMd5() {
        return fileMd5;
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }

    public String getOriginFileSize() {
        return originFileSize;
    }

    public void setOriginFileSize(String originFileSize) {
        this.originFileSize = originFileSize;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getOverwrite() {
        return overwrite;
    }

    public void setOverwrite(String overwrite) {
        this.overwrite = overwrite;
    }

    public String getVideoSource() {
        return videoSource;
    }

    public void setVideoSource(String videoSource) {
        this.videoSource = videoSource;
    }
}
