package cn.feihutv.zhibofeihu.data.network.socket.model.push;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/10/16
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class ValueChangePush extends BasePush {

    @Expose
    @SerializedName("Type")
    private String type;

    @Expose
    @SerializedName("ChgId")
    private int chgId;


    @Expose
    @SerializedName("ChgVal")
    private int ChgVal;

    @Expose
    @SerializedName("NewVal")
    private int NewVal;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getChgId() {
        return chgId;
    }

    public void setChgId(int chgId) {
        this.chgId = chgId;
    }

    public int getChgVal() {
        return ChgVal;
    }

    public void setChgVal(int chgVal) {
        ChgVal = chgVal;
    }

    public int getNewVal() {
        return NewVal;
    }

    public void setNewVal(int newVal) {
        NewVal = newVal;
    }
}
