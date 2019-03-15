package cn.feihutv.zhibofeihu.data.db.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/14 15:49
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */
@Entity
public class SysVipGoodsBean {


    /**
     * goodsId : 1
     * goodsName : 首次开通 30天
     * duration : 2592000
     * czz : 1000
     * xiaolaba : 4
     * hb : 60000
     * recommend : 1
     * discount : 0
     * vipMountDays : 0
     * sortOrder : 50
     * isSelect : 0:未选中  1:已选中
     */

    @Id
    private String goodsId;
    private String goodsName;
    private String duration;
    private String czz;
    private String xiaolaba;
    private long hb;
    private int recommend;
    private int discount;
    private String vipMountDays;
    private int sortOrder;
    private int isSelect;
    @Generated(hash = 1864832082)
    public SysVipGoodsBean(String goodsId, String goodsName, String duration,
            String czz, String xiaolaba, long hb, int recommend, int discount,
            String vipMountDays, int sortOrder, int isSelect) {
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.duration = duration;
        this.czz = czz;
        this.xiaolaba = xiaolaba;
        this.hb = hb;
        this.recommend = recommend;
        this.discount = discount;
        this.vipMountDays = vipMountDays;
        this.sortOrder = sortOrder;
        this.isSelect = isSelect;
    }
    @Generated(hash = 1784753727)
    public SysVipGoodsBean() {
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
    public String getCzz() {
        return this.czz;
    }
    public void setCzz(String czz) {
        this.czz = czz;
    }
    public String getXiaolaba() {
        return this.xiaolaba;
    }
    public void setXiaolaba(String xiaolaba) {
        this.xiaolaba = xiaolaba;
    }
    public long getHb() {
        return this.hb;
    }
    public void setHb(long hb) {
        this.hb = hb;
    }
    public int getRecommend() {
        return this.recommend;
    }
    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }
    public int getDiscount() {
        return this.discount;
    }
    public void setDiscount(int discount) {
        this.discount = discount;
    }
    public String getVipMountDays() {
        return this.vipMountDays;
    }
    public void setVipMountDays(String vipMountDays) {
        this.vipMountDays = vipMountDays;
    }
    public int getSortOrder() {
        return this.sortOrder;
    }
    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
    public int getIsSelect() {
        return this.isSelect;
    }
    public void setIsSelect(int isSelect) {
        this.isSelect = isSelect;
    }

}
