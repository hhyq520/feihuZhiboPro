package cn.feihutv.zhibofeihu.data.local.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/3/7.
 */

public class TCUserInfo   {

    @SerializedName("UserId")
    private String userId;    //用户Id

    private String phoneNumber;     //用户手机号

    private String NickName;  //用户昵称

    @SerializedName("UserId")
    private String accountId; //用户对外显示的ID

    private String headUrl;  //用户头像

    private int gender;   //用户性别1男 0女 -1 保密

    private long hb;  //用户虎币值

    private long ghb;//用户金虎币值

    private long yhb;//用户银虎币值

    private int exp;//经验值
    private int level;    //用户等级

    private String roomName;//直播间名字
    private String singature; //个性签名

    private String location; //地点

    private int CertifiStatus; //是否认证

    private int LiveCnt; // 开播次数

    private String cosHeadIconName; //头像储存cos地址

    private boolean haveCare;  // 用户是否被关注

    private int income;//总收到金虎币
    private int contri;//总送出虎币
    private String LiveCover;//直播间封面
    private int mNickname;//昵称被修改次数；
    private int mGender;//性别被修改次数；
    private int followers;//粉丝数量
    private int follows;//关注数量
    private boolean followed;//是否已关注
    private int CrtMount;//当前座驾ID
    private String GuildName;

    public int getLiveCnt() {
        return LiveCnt;
    }

    public void setLiveCnt(int liveCnt) {
        LiveCnt = liveCnt;
    }

    public boolean isHaveCare() {
        return haveCare;
    }

    public void setHaveCare(boolean haveCare) {
        this.haveCare = haveCare;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getSingature() {
        return singature;
    }

    public void setSingature(String singature) {
        this.singature = singature;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCosHeadIconName() {
        return cosHeadIconName;
    }

    public void setCosHeadIconName(String cosHeadIconName) {
        this.cosHeadIconName = cosHeadIconName;
    }

    @Override
    public String toString() {
        return "TCUserInfo{" +
            "userId='" + userId + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", NickName='" + NickName + '\'' +
            ", accountId='" + accountId + '\'' +
            ", headUrl='" + headUrl + '\'' +
            ", gender=" + gender +
            ", hb=" + hb +
            ", ghb=" + ghb +
            ", yhb=" + yhb +
            ", exp=" + exp +
            ", level=" + level +
            ", roomName='" + roomName + '\'' +
            ", singature='" + singature + '\'' +
            ", location='" + location + '\'' +
            ", CertifiStatus=" + CertifiStatus +
            ", LiveCnt=" + LiveCnt +
            ", cosHeadIconName='" + cosHeadIconName + '\'' +
            ", haveCare=" + haveCare +
            ", income=" + income +
            ", contri=" + contri +
            ", LiveCover='" + LiveCover + '\'' +
            ", mNickname=" + mNickname +
            ", mGender=" + mGender +
            ", followers=" + followers +
            ", follows=" + follows +
            ", followed=" + followed +
            ", CrtMount=" + CrtMount +
            '}';
    }

    public long getHb() {
        return hb;
    }

    public void setHb(int hb) {
        this.hb = hb;
    }

    public long getGhb() {
        return ghb;
    }

    public void setGhb(long ghb) {
        this.ghb = ghb;
    }

    public long getYhb() {
        return yhb;
    }

    public void setYhb(long yhb) {
        this.yhb = yhb;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomName() {
        return roomName;
    }

    public int getIncome() {
        return income;
    }

    public void setIncome(int income) {
        this.income = income;
    }

    public int getContri() {
        return contri;
    }

    public void setContri(int contri) {
        this.contri = contri;
    }

    public int getmNickname() {
        return mNickname;
    }

    public void setmNickname(int mNickname) {
        this.mNickname = mNickname;
    }

    public int getmGender() {
        return mGender;
    }

    public void setmGender(int mGender) {
        this.mGender = mGender;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public boolean isFollowed() {
        return followed;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }

    public int getFollows() {
        return follows;
    }

    public void setFollows(int follows) {
        this.follows = follows;
    }

    public String getLiveCover() {
        return LiveCover;
    }

    public void setLiveCover(String liveCover) {
        LiveCover = liveCover;
    }

    public int getCrtMount() {
        return CrtMount;
    }

    public void setCrtMount(int crtMount) {
        CrtMount = crtMount;
    }

    public int getCertifiStatus() {
        return CertifiStatus;
    }

    public void setCertifiStatus(int certifiStatus) {
        CertifiStatus = certifiStatus;
    }


    public String getGuildName() {
        return GuildName;
    }

    public void setGuildName(String guildName) {
        GuildName = guildName;
    }
}
