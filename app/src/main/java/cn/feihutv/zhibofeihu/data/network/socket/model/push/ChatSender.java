package cn.feihutv.zhibofeihu.data.network.socket.model.push;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/10/16
 *     desc   : 基本描述
 *     version: 1.0
 *        ShowId       string //飞虎ID
 Vip          int    //vip等级
 VipExpired   bool   //vip是否过期
 GuardType    int    //0未守护，1银虎守护，2金虎守护
 GuardExpired bool   //守护是否过期
 * </pre>
 */
public class ChatSender {

    @Expose
    @SerializedName("UserId")
    private String userId;
    @Expose
    @SerializedName("Gender")
    private int gender;
    @Expose
    @SerializedName("NickName")
    private String nickName;
    @Expose
    @SerializedName("Level")
    private int level;
    @Expose
    @SerializedName("HeadUrl")
    private String headUrl;
    @Expose
    @SerializedName("ShowId")
    private String ShowId;
    @Expose
    @SerializedName("Vip")
    private int Vip;
    @Expose
    @SerializedName("VipExpired")
    private boolean VipExpired;
    @Expose
    @SerializedName("GuardType")
    private int GuardType;
    @Expose
    @SerializedName("GuardExpired")
    private boolean GuardExpired;
    @Expose
    @SerializedName("IsLiang")
    private boolean IsLiang ;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getShowId() {
        return ShowId;
    }

    public void setShowId(String showId) {
        ShowId = showId;
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
}
