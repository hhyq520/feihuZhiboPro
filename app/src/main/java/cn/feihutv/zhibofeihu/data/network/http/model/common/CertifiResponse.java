package cn.feihutv.zhibofeihu.data.network.http.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.user.BagResponse;

/**
 * Created by huanghao on 2017/10/10.
 */

public class CertifiResponse extends BaseResponse{
    @Expose
    @SerializedName("Data")
    private CertifiData mCertifiData;

    public CertifiData getCertifiData() {
        return mCertifiData;
    }

    public void setCertifiData(CertifiData certifiData) {
        mCertifiData = certifiData;
    }

    public static class CertifiData{
        @Expose
        @SerializedName("CertifiMsg")
        private String CertifiMsg;//http://dev.feihutv.cn/fhzb/sysdata.json,

        public String getCertifiMsg() {
            return CertifiMsg;
        }

        public void setCertifiMsg(String certifiMsg) {
            CertifiMsg = certifiMsg;
        }
    }
}
