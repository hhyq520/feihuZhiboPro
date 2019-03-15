package cn.feihutv.zhibofeihu.data.network.http.model.me;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/10/25
 *     desc   : 上传图片
 *     version: 1.0
 * </pre>
 */
public class upLoadPhotoRequest {


    @Expose
    @SerializedName("bucket")
    protected String bucket="feihuzhibo";



    @Expose
    @SerializedName("cosPath")
    public String cosPath;

    @Expose
    @SerializedName("srcPath")
    public String srcPath;


    @Expose
    @SerializedName("sign")
    public String sign;

    public upLoadPhotoRequest(String cosPath, String srcPath, String sign) {
        this.cosPath = cosPath;
        this.srcPath = srcPath;
        this.sign = sign;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getCosPath() {
        return cosPath;
    }

    public void setCosPath(String cosPath) {
        this.cosPath = cosPath;
    }

    public String getSrcPath() {
        return srcPath;
    }

    public void setSrcPath(String srcPath) {
        this.srcPath = srcPath;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
