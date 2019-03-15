package cn.feihutv.zhibofeihu.data.db.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class SysGoodsNewBean {
    /**
     * id : 1
     * name : 直播间-小黄瓜
     * goods : [{"type":1,"cnt":1,"id":1}]
     * price : 10
     * realPrice : 0
     * enableYHB : 1
     * show : 0
     * sortOrder : 0
     * remark : 这个不能修改和删除
     */
    @Id
    private String id;
    private String name;
    private String objType;
    private String objId;
    private String price;
    private String discount;
    private String icon;
    private String shelfBegin;
    private String shelfEnd;
    private String show;
    private String tagIcon;
    private int sortOrder;
    private int giftCount = 0;
    private boolean isSelected;
    private int needVip;
    @Generated(hash = 1187953644)
    public SysGoodsNewBean(String id, String name, String objType, String objId,
            String price, String discount, String icon, String shelfBegin,
            String shelfEnd, String show, String tagIcon, int sortOrder,
            int giftCount, boolean isSelected, int needVip) {
        this.id = id;
        this.name = name;
        this.objType = objType;
        this.objId = objId;
        this.price = price;
        this.discount = discount;
        this.icon = icon;
        this.shelfBegin = shelfBegin;
        this.shelfEnd = shelfEnd;
        this.show = show;
        this.tagIcon = tagIcon;
        this.sortOrder = sortOrder;
        this.giftCount = giftCount;
        this.isSelected = isSelected;
        this.needVip = needVip;
    }
    @Generated(hash = 915922582)
    public SysGoodsNewBean() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getObjType() {
        return this.objType;
    }
    public void setObjType(String objType) {
        this.objType = objType;
    }
    public String getObjId() {
        return this.objId;
    }
    public void setObjId(String objId) {
        this.objId = objId;
    }
    public String getPrice() {
        return this.price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getDiscount() {
        return this.discount;
    }
    public void setDiscount(String discount) {
        this.discount = discount;
    }
    public String getIcon() {
        return this.icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public String getShelfBegin() {
        return this.shelfBegin;
    }
    public void setShelfBegin(String shelfBegin) {
        this.shelfBegin = shelfBegin;
    }
    public String getShelfEnd() {
        return this.shelfEnd;
    }
    public void setShelfEnd(String shelfEnd) {
        this.shelfEnd = shelfEnd;
    }
    public String getShow() {
        return this.show;
    }
    public void setShow(String show) {
        this.show = show;
    }
    public String getTagIcon() {
        return this.tagIcon;
    }
    public void setTagIcon(String tagIcon) {
        this.tagIcon = tagIcon;
    }
    public int getSortOrder() {
        return this.sortOrder;
    }
    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
    public int getGiftCount() {
        return this.giftCount;
    }
    public void setGiftCount(int giftCount) {
        this.giftCount = giftCount;
    }
    public boolean getIsSelected() {
        return this.isSelected;
    }
    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
    public int getNeedVip() {
        return this.needVip;
    }
    public void setNeedVip(int needVip) {
        this.needVip = needVip;
    }




}