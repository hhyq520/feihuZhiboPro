package cn.feihutv.zhibofeihu.data.network.http.model.me;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/10/25
 *     desc   : 获取上传图片到cos的sign
 *     version: 1.0
 * </pre>
 */
public class GetCosSignRequest {


    @Expose
    @SerializedName("type")
    public int type; //上传图片的类型 1头像 2封面 3动态图 4举报图片



    @Expose
    @SerializedName("ext")
    public String ext; //图片扩展名 png  或  jpg

    public GetCosSignRequest(int type, String ext) {
        this.type = type;
        this.ext = ext;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
}
