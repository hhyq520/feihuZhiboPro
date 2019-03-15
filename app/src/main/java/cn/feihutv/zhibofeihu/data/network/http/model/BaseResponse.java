package cn.feihutv.zhibofeihu.data.network.http.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/09/25
 *     desc   : 回应协议基类
 *     version: 1.0
 * </pre>
 */
public class BaseResponse {

    @Expose
    @SerializedName("Code")
    private int code;

    @Expose
    @SerializedName("ErrMsg")
    private String errMsg;

    @Expose
    @SerializedName("ErrExtMsg")
    private String mErrExtMsg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getErrExtMsg() {
        return mErrExtMsg;
    }

    public void setErrExtMsg(String errExtMsg) {
        mErrExtMsg = errExtMsg;
    }
}
