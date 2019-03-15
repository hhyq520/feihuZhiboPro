package cn.feihutv.zhibofeihu.data.network.http.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/03/08
 *     desc   : 请求协议错误处理实现类
 *     version: 1.0
 * </pre>
 */
public class ApiError {

    private int errorCode;

    @Expose
    @SerializedName("result")
    private String statusCode;

    @Expose
    @SerializedName("desc")
    private String message;



    public ApiError(int errorCode, String statusCode, String message) {
        this.errorCode = errorCode;
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        ApiError apiError = (ApiError) object;

        if (errorCode != apiError.errorCode) return false;
        if (statusCode != null ? !statusCode.equals(apiError.statusCode)
                : apiError.statusCode != null)
            return false;
        return message != null ? message.equals(apiError.message) : apiError.message == null;
    }

    @Override
    public int hashCode() {
        int result = errorCode;
        result = 31 * result + (statusCode != null ? statusCode.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }
}
