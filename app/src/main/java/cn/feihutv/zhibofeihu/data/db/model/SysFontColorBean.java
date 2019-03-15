package cn.feihutv.zhibofeihu.data.db.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by huanghao on 2017/11/13.
 */
@Entity
public class SysFontColorBean {
    @Id
    private String key;
    private String keyName;
    private String phone;
    private String pc;

    @Generated(hash = 970080013)
    public SysFontColorBean(String key, String keyName, String phone, String pc) {
        this.key = key;
        this.keyName = keyName;
        this.phone = phone;
        this.pc = pc;
    }
    @Generated(hash = 1828768274)
    public SysFontColorBean() {
    }
   
    public String getKey() {
        return this.key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getKeyName() {
        return this.keyName;
    }
    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getPc() {
        return this.pc;
    }
    public void setPc(String pc) {
        this.pc = pc;
    }
}
