package cn.feihutv.zhibofeihu.data.network.http.model.me;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;


/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/10/25
 *     desc   : 获取上传图片到cos的sign
 *     version: 1.0
 * </pre>
 */
public class GetCosSignResponse extends BaseResponse {

    @Expose
    @SerializedName("Data")
    private GetCosSignData cosSignData;

    public GetCosSignData getCosSignData() {
        return cosSignData;
    }

    public void setCosSignData(GetCosSignData cosSignData) {
        this.cosSignData = cosSignData;
    }

    public static class GetCosSignData {
        @Expose
        @SerializedName("sign")
        private String sign;


        @Expose
        @SerializedName("imgName")
        private String imgName;

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getImgName() {
            return imgName;
        }

        public void setImgName(String imgName) {
            this.imgName = imgName;
        }
    }
}
