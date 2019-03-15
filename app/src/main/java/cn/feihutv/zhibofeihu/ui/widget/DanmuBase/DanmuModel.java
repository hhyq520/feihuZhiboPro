package cn.feihutv.zhibofeihu.ui.widget.DanmuBase;

/**
 * Created by huanghao on 2017/11/13.
 */

public class DanmuModel {
    private String headUrl;
    private int level;
    private String Content;
    private int Vip;
    private boolean VipExpired;
    private int GuardType;
    private boolean GuardExpired;
    private boolean isLiang;
    private String accountId;
    public DanmuModel(String headUrl, int level, String content, int vip, boolean vipExpired, int guardType, boolean guardExpired, boolean isLiang,String accountId) {
        this.headUrl = headUrl;
        this.level = level;
        Content = content;
        Vip = vip;
        VipExpired = vipExpired;
        GuardType = guardType;
        GuardExpired = guardExpired;
        this.isLiang = isLiang;
        this.accountId=accountId;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public int getVip() {
        return Vip;
    }

    public void setVip(int vip) {
        Vip = vip;
    }

    public boolean isVipExpired() {
        return VipExpired;
    }

    public void setVipExpired(boolean vipExpired) {
        VipExpired = vipExpired;
    }

    public int getGuardType() {
        return GuardType;
    }

    public void setGuardType(int guardType) {
        GuardType = guardType;
    }

    public boolean isGuardExpired() {
        return GuardExpired;
    }

    public void setGuardExpired(boolean guardExpired) {
        GuardExpired = guardExpired;
    }

    public boolean isLiang() {
        return isLiang;
    }

    public void setLiang(boolean liang) {
        isLiang = liang;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
