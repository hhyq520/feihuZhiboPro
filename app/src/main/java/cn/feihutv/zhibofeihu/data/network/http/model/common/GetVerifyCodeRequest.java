package cn.feihutv.zhibofeihu.data.network.http.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by huanghao on 2017/10/11.
 */

public class GetVerifyCodeRequest {
    @Expose
    @SerializedName("phone")
    private String phone;
    @Expose
    @SerializedName("codeId")
    private int codeId;
    @Expose
    @SerializedName("checkExist")
    private int checkExist;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getCodeId() {
        return codeId;
    }

    public void setCodeId(int codeId) {
        this.codeId = codeId;
    }

    public int getCheckExist() {
        return checkExist;
    }

    public void setCheckExist(int checkExist) {
        this.checkExist = checkExist;
    }

    public GetVerifyCodeRequest(String phone,int codeId,int checkExist){
        this.phone=phone;
        this.checkExist=checkExist;
        this.codeId=codeId;
    }
}
