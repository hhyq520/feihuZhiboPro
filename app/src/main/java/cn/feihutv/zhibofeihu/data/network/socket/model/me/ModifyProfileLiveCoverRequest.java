package cn.feihutv.zhibofeihu.data.network.socket.model.me;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/10/25
 *     desc   : 修改直播封面
 *     version: 1.0
 * </pre>
 */
public class ModifyProfileLiveCoverRequest {

    /**
     * fieldName
     * 1、Signature
     * 2、Gender
     * 3、NickName
     * 4、HeadUrl
     * 5、LiveCover
      */


    @Expose
    @SerializedName("fieldName")
    public String fieldName;

    @Expose
    @SerializedName("fieldValue")
    public String fieldValue;

    public ModifyProfileLiveCoverRequest(String fieldName, String fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}

