package cn.feihutv.zhibofeihu.ui.live.models;

/**
 * Created by huanghao on 2017/8/12.
 */

public class GiftModel {
    private String tag;
    private int count;
    private String giftname;
    private String userName;
    private String headUrl;
    private int giftid;
    private boolean isVip;
    public GiftModel(String tag, int count, String giftname, String userName, String headUrl, int giftid, boolean isVip){
        this.tag=tag;
        this.count=count;
        this.giftname=giftname;
        this.userName=userName;
        this.headUrl=headUrl;
        this.giftid=giftid;
        this.isVip = isVip;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getGiftname() {
        return giftname;
    }

    public void setGiftname(String giftname) {
        this.giftname = giftname;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public int getGiftid() {
        return giftid;
    }

    public void setGiftid(int giftid) {
        this.giftid = giftid;
    }

    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }
}
