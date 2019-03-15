package cn.feihutv.zhibofeihu.data.local.model;

/**
 * 消息体类
 */
public class TCChatEntity {

    private String imgUrl;
    private String grpSendName;
    private String context;
    private int type;
    private int level;
    private String zuojia;
    private String banName;
    private String giftimg;
    private String userId;
    private String accountId;
    private int Vip;
    private boolean VipExpired;
    private int GuardType;
    private boolean GuardExpired;
    private boolean IsLiang ;
    public TCChatEntity() {

    }

    public String getSenderName() {
        return grpSendName;
    }

    public void setSenderName(String grpSendName) {
        this.grpSendName = grpSendName;
    }


    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TCChatEntity that = (TCChatEntity) o;

        if (type != that.type) return false;
        if (!imgUrl.equals(that.imgUrl)) return false;
        if (!grpSendName.equals(that.grpSendName)) return false;
        return context.equals(that.context);

    }

    @Override
    public int hashCode() {
        int result = imgUrl.hashCode();
        result = 31 * result + grpSendName.hashCode();
        result = 31 * result + context.hashCode();
        result = 31 * result + type;
        return result;
    }

    public String getGiftimg() {
        return giftimg;
    }

    public void setGiftimg(String giftimg) {
        this.giftimg = giftimg;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getZuojia() {
        return zuojia;
    }

    public void setZuojia(String zuojia) {
        this.zuojia = zuojia;
    }

    public String getBanName() {
        return banName;
    }

    public void setBanName(String banName) {
        this.banName = banName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
        return IsLiang;
    }

    public void setLiang(boolean liang) {
        IsLiang = liang;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
