package cn.feihutv.zhibofeihu.data.local.model;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/14 19:52
 *      desc   : VIP特权
 *      version: 1.0
 * </pre>
 */

public class VipPrivilegeEntity {

    private int nameId;

    private int resId;

    private int descId;

    public int getDescId() {
        return descId;
    }

    public void setDescId(int descId) {
        this.descId = descId;
    }

    public int getNameId() {
        return nameId;
    }

    public void setNameId(int nameId) {
        this.nameId = nameId;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
