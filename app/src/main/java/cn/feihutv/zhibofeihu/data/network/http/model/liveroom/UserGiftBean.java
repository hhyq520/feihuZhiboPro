package cn.feihutv.zhibofeihu.data.network.http.model.liveroom;

/**
 * Created by huanghao on 2017/5/4.
 */

public class UserGiftBean {
    private String UserId;
    private String NickName;
    private String HeadUrl;
    private int GiftId;
    private int GiftNum;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getHeadUrl() {
        return HeadUrl;
    }

    public void setHeadUrl(String headUrl) {
        HeadUrl = headUrl;
    }

    public int getGiftId() {
        return GiftId;
    }

    public void setGiftId(int giftId) {
        GiftId = giftId;
    }

    public int getGiftNum() {
        return GiftNum;
    }

    public void setGiftNum(int giftNum) {
        GiftNum = giftNum;
    }
}
