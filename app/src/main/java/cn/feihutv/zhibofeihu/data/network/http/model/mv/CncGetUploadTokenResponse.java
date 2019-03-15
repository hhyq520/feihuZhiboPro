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
public class CncGetUploadTokenResponse {


    @SerializedName("code")
    private  int code;// 200：成功；



    @SerializedName("message")
    private  String message;//


    @SerializedName("data")
    private  CncGetUploadTokenData mUploadTokenData;//



 public static class    CncGetUploadTokenData{


     @SerializedName("fileKey")
     private  String fileKey;//

     @SerializedName("uploadToken")
     private  String uploadToken;//

     @SerializedName("uploadUrl")
     private  String uploadUrl;//

     @SerializedName("videoId")
     private  String videoId;//

     @SerializedName("bucketName")
     private  String bucketName;//

     public String getBucketName() {
         return bucketName;
     }

     public void setBucketName(String bucketName) {
         this.bucketName = bucketName;
     }

     public String getFileKey() {
         return fileKey;
     }

     public void setFileKey(String fileKey) {
         this.fileKey = fileKey;
     }

     public String getUploadToken() {
         return uploadToken;
     }

     public void setUploadToken(String uploadToken) {
         this.uploadToken = uploadToken;
     }

     public String getUploadUrl() {
         return uploadUrl;
     }

     public void setUploadUrl(String uploadUrl) {
         this.uploadUrl = uploadUrl;
     }

     public String getVideoId() {
         return videoId;
     }

     public void setVideoId(String videoId) {
         this.videoId = videoId;
     }

     @Override
     public String toString() {
         return "CncGetUploadTokenData{" +
                 "fileKey='" + fileKey + '\'' +
                 ", uploadToken='" + uploadToken + '\'' +
                 ", uploadUrl='" + uploadUrl + '\'' +
                 ", videoId='" + videoId + '\'' +
                 '}';
     }
 }





    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CncGetUploadTokenData getUploadTokenData() {
        return mUploadTokenData;
    }

    public void setUploadTokenData(CncGetUploadTokenData uploadTokenData) {
        mUploadTokenData = uploadTokenData;
    }

    @Override
    public String toString() {
        return "CncGetUploadTokenResponse{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", mUploadTokenData=" + mUploadTokenData +
                '}';
    }
}
