package cn.feihutv.zhibofeihu.data.db.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/10 10:28
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */
@Entity
public class SysGuardGoodsBean {


    /**
     * goodsId : 2
     * goodsName : 年卡/365天
     * duration : 31536000
     * yinhu : {"hb":330000,"xiaolaba":25,"orig":360000}
     * jinhu : {"hb":550000,"xiaolaba":38,"orig":600000}
     */

    @Id
    private String goodsId;
    private String goodsName;
    private String duration;
    private String yinhu;
    private String jinhu;
    @Generated(hash = 2124622422)
    public SysGuardGoodsBean(String goodsId, String goodsName, String duration,
            String yinhu, String jinhu) {
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.duration = duration;
        this.yinhu = yinhu;
        this.jinhu = jinhu;
    }
    @Generated(hash = 1066537072)
    public SysGuardGoodsBean() {
    }
    public String getGoodsId() {
        return this.goodsId;
    }
    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }
    public String getGoodsName() {
        return this.goodsName;
    }
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }
    public String getDuration() {
        return this.duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }
    public String getYinhu() {
        return this.yinhu;
    }
    public void setYinhu(String yinhu) {
        this.yinhu = yinhu;
    }
    public String getJinhu() {
        return this.jinhu;
    }
    public void setJinhu(String jinhu) {
        this.jinhu = jinhu;
    }

    @Override
    public String toString() {
        return "SysGuardGoodsBean{" +
                "goodsId='" + goodsId + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", duration='" + duration + '\'' +
                ", yinhu='" + yinhu + '\'' +
                ", jinhu='" + jinhu + '\'' +
                '}';
    }
}
