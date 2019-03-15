package cn.feihutv.zhibofeihu.data.network.socket.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/09/27
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class LoadUserDataBaseResponse extends SocketBaseResponse{

    @Expose
    @SerializedName("Data")
    private UserData mUserData;

    public UserData getUserData() {
        return mUserData;
    }

    @Override
    public String toString() {
        return "LoadUserDataBaseResponse{" +
                "mUserData=" + mUserData +
                '}';
    }

    public static class UserData{
        @Expose
        @SerializedName("UserId")
        private String userId;

        @Expose
        @SerializedName("Phone")
        private String phone;

        @Expose
        @SerializedName("NickName")
        private String nickName;

        @Expose
        @SerializedName("AccountId")
        private String accountId;

        @Expose
        @SerializedName("HeadUrl")
        private String headUrl;

        @Expose
        @SerializedName("Gender")
        private int gender;

        @Expose
        @SerializedName("HB")
        private long hB;

        @Expose
        @SerializedName("GHB")
        private long gHB;


        @Expose
        @SerializedName("YHB")
        private long yHB;

        @Expose
        @SerializedName("Exp")
        private int exp;

        @Expose
        @SerializedName("Level")
        private int level;

        @Expose
        @SerializedName("Location")
        private String location;

        @Expose
        @SerializedName("RoomName")
        private String roomName;
        @Expose
        @SerializedName("Signature")
        private String signature;

        @Expose
        @SerializedName("CertifiStatus")
        private int certifiStatus;

        @Expose
        @SerializedName("Income")
        private long income;

        @Expose
        @SerializedName("Contri")
        private long contri;

        @Expose
        @SerializedName("CrtMount")
        private int crtMount;

        @Expose
        @SerializedName("LiveCover")
        private String liveCover;

        @Expose
        @SerializedName("Follows")
        private int follows;

        @Expose
        @SerializedName("Followers")
        private int followers;

        @Expose
        @SerializedName("LiveCnt")
        private int liveCnt;

        @Expose
        @SerializedName("GuildName")
        private String guildName;

        @Expose
        @SerializedName("Modified")
        private Modified mModified;

        @Expose
        @SerializedName("IsLiang")
        private boolean isLiang;

        @Expose
        @SerializedName("Vip")
        private int vip;

        @Expose
        @SerializedName("VipExpired")
        private boolean vipExpired;

        @Expose
        @SerializedName("VipCZZ")
        private int vipCZZ;

        @Expose
        @SerializedName("VipExpireTime")
        private long vipExpireTime;

        public int getVipCZZ() {
            return vipCZZ;
        }

        public void setVipCZZ(int vipCZZ) {
            this.vipCZZ = vipCZZ;
        }

        public long getVipExpireTime() {
            return vipExpireTime;
        }

        public void setVipExpireTime(long vipExpireTime) {
            this.vipExpireTime = vipExpireTime;
        }

        public boolean isLiang() {
            return isLiang;
        }

        public void setLiang(boolean liang) {
            isLiang = liang;
        }

        public int getVip() {
            return vip;
        }

        public void setVip(int vip) {
            this.vip = vip;
        }

        public boolean isVipExpired() {
            return vipExpired;
        }

        public void setVipExpired(boolean vipExpired) {
            this.vipExpired = vipExpired;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
        public String getUserId() {
            return userId;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
        public String getPhone() {
            return phone;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }
        public String getNickName() {
            return nickName;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }
        public String getAccountId() {
            return accountId;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }
        public String getHeadUrl() {
            return headUrl;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }
        public int getGender() {
            return gender;
        }

        public void sethB(long hB) {
            this.hB = hB;
        }
        public long gethB() {
            return hB;
        }

        public void setgHB(long gHB) {
            this.gHB = gHB;
        }
        public long getgHB() {
            return gHB;
        }

        public void setyHB(long yHB) {
            this.yHB = yHB;
        }
        public long getyHB() {
            return yHB;
        }

        public void setExp(int exp) {
            this.exp = exp;
        }
        public int getExp() {
            return exp;
        }

        public void setLevel(int level) {
            this.level = level;
        }
        public int getLevel() {
            return level;
        }

        public void setLocation(String location) {
            this.location = location;
        }
        public String getLocation() {
            return location;
        }

        public void setRoomName(String roomName) {
            this.roomName = roomName;
        }
        public String getRoomName() {
            return roomName;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }
        public String getSignature() {
            return signature;
        }

        public void setCertifiStatus(int certifiStatus) {
            this.certifiStatus = certifiStatus;
        }
        public int getCertifiStatus() {
            return certifiStatus;
        }

        public long getIncome() {
            return income;
        }

        public void setIncome(long income) {
            this.income = income;
        }

        public long getContri() {
            return contri;
        }

        public void setContri(long contri) {
            this.contri = contri;
        }

        public void setCrtMount(int crtMount) {
            this.crtMount = crtMount;
        }
        public int getCrtMount() {
            return crtMount;
        }

        public void setLiveCover(String liveCover) {
            this.liveCover = liveCover;
        }
        public String getLiveCover() {
            return liveCover;
        }

        public void setFollows(int follows) {
            this.follows = follows;
        }
        public int getFollows() {
            return follows;
        }

        public void setFollowers(int followers) {
            this.followers = followers;
        }
        public int getFollowers() {
            return followers;
        }

        public void setLiveCnt(int liveCnt) {
            this.liveCnt = liveCnt;
        }
        public int getLiveCnt() {
            return liveCnt;
        }

        public void setGuildName(String guildName) {
            this.guildName = guildName;
        }
        public String getGuildName() {
            return guildName;
        }

        public void setmModified(Modified mModified) {
            this.mModified = mModified;
        }
        public Modified getmModified() {
            return mModified;
        }

        @Override
        public String toString() {
            return "UserData{" +
                    "userId='" + userId + '\'' +
                    ", phone='" + phone + '\'' +
                    ", nickName='" + nickName + '\'' +
                    ", accountId='" + accountId + '\'' +
                    ", headUrl='" + headUrl + '\'' +
                    ", gender=" + gender +
                    ", hB=" + hB +
                    ", gHB=" + gHB +
                    ", yHB=" + yHB +
                    ", exp=" + exp +
                    ", level=" + level +
                    ", location='" + location + '\'' +
                    ", roomName='" + roomName + '\'' +
                    ", signature='" + signature + '\'' +
                    ", certifiStatus=" + certifiStatus +
                    ", income=" + income +
                    ", contri=" + contri +
                    ", crtMount=" + crtMount +
                    ", liveCover='" + liveCover + '\'' +
                    ", follows=" + follows +
                    ", followers=" + followers +
                    ", liveCnt=" + liveCnt +
                    ", guildName='" + guildName + '\'' +
                    ", mModified=" + mModified +
                    ", isLiang=" + isLiang +
                    ", vip=" + vip +
                    ", vipExpired=" + vipExpired +
                    ", vipCZZ=" + vipCZZ +
                    ", vipExpireTime=" + vipExpireTime +
                    '}';
        }
    }




    public static class Modified{
        @Expose
        @SerializedName("NickName")
        private int nickName;


        @Expose
        @SerializedName("Gender")
        private int gender;


        public int getNickName() {
            return nickName;
        }

        public void setNickName(int nickName) {
            this.nickName = nickName;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }
    }


}
