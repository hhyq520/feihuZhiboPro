package cn.feihutv.zhibofeihu.data.network.socket.model.live;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import cn.feihutv.zhibofeihu.data.network.socket.model.SocketBaseResponse;

/**
 * Created by huanghao on 2017/10/17.
 */

public class LoadOtherUserInfoResponce extends SocketBaseResponse {

    @Expose
    @SerializedName("Data")
    private OtherUserInfo otherUserInfo;

    public OtherUserInfo getOtherUserInfo() {
        return otherUserInfo;
    }

    public void setOtherUserInfo(OtherUserInfo otherUserInfo) {
        this.otherUserInfo = otherUserInfo;
    }

    public static class OtherUserInfo implements Parcelable {
        @Expose
        @SerializedName("UserId")
        private String UserId;
        @Expose
        @SerializedName("NickName")
        private String NickName;
        @Expose
        @SerializedName("AccountId")
        private String AccountId;
        @Expose
        @SerializedName("Gender")
        private int Gender;
        @Expose
        @SerializedName("HeadUrl")
        private String HeadUrl;
        @Expose
        @SerializedName("Level")
        private int Level;
        @Expose
        @SerializedName("Signature")
        private String Signature;
        @Expose
        @SerializedName("Location")
        private String Location;
        @Expose
        @SerializedName("CertifiStatus")
        private int CertifiStatus;
        @Expose
        @SerializedName("LiveCover")
        private String LiveCover;
        @Expose
        @SerializedName("Income")
        private long Income;
        @Expose
        @SerializedName("Contri")
        private long Contri;
        @Expose
        @SerializedName("Followed")
        private boolean Followed;
        @Expose
        @SerializedName("Follows")
        private int Follows;
        @Expose
        @SerializedName("Followers")
        private int Followers;
        @Expose
        @SerializedName("IsFollower")
        private boolean IsFollower;
        @Expose
        @SerializedName("IsBaned")
        private boolean IsBaned;
        @Expose
        @SerializedName("IsRoomMgr")
        private boolean IsRoomMgr;
        @Expose
        @SerializedName("RoomStatus")
        private boolean RoomStatus;
        @Expose
        @SerializedName("Blocked")
        private boolean Blocked;
        @Expose
        @SerializedName("IsLiang")
        private boolean IsLiang;
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
        @SerializedName("BroadcastType")
        private int broadcastType;

        protected OtherUserInfo(Parcel in) {
            UserId = in.readString();
            NickName = in.readString();
            AccountId = in.readString();
            Gender = in.readInt();
            HeadUrl = in.readString();
            Level = in.readInt();
            Signature = in.readString();
            Location = in.readString();
            CertifiStatus = in.readInt();
            LiveCover = in.readString();
            Income = in.readLong();
            Contri = in.readLong();
            Followed = in.readByte() != 0;
            Follows = in.readInt();
            Followers = in.readInt();
            IsFollower = in.readByte() != 0;
            IsBaned = in.readByte() != 0;
            IsRoomMgr = in.readByte() != 0;
            RoomStatus = in.readByte() != 0;
            Blocked = in.readByte() != 0;
            IsLiang = in.readByte() != 0;
            Vip = in.readInt();
            VipExpired = in.readByte() != 0;
            GuardType = in.readInt();
            GuardExpired = in.readByte() != 0;
            broadcastType = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(UserId);
            dest.writeString(NickName);
            dest.writeString(AccountId);
            dest.writeInt(Gender);
            dest.writeString(HeadUrl);
            dest.writeInt(Level);
            dest.writeString(Signature);
            dest.writeString(Location);
            dest.writeInt(CertifiStatus);
            dest.writeString(LiveCover);
            dest.writeLong(Income);
            dest.writeLong(Contri);
            dest.writeByte((byte) (Followed ? 1 : 0));
            dest.writeInt(Follows);
            dest.writeInt(Followers);
            dest.writeByte((byte) (IsFollower ? 1 : 0));
            dest.writeByte((byte) (IsBaned ? 1 : 0));
            dest.writeByte((byte) (IsRoomMgr ? 1 : 0));
            dest.writeByte((byte) (RoomStatus ? 1 : 0));
            dest.writeByte((byte) (Blocked ? 1 : 0));
            dest.writeByte((byte) (IsLiang ? 1 : 0));
            dest.writeInt(Vip);
            dest.writeByte((byte) (VipExpired ? 1 : 0));
            dest.writeInt(GuardType);
            dest.writeByte((byte) (GuardExpired ? 1 : 0));
            dest.writeInt(broadcastType);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<OtherUserInfo> CREATOR = new Creator<OtherUserInfo>() {
            @Override
            public OtherUserInfo createFromParcel(Parcel in) {
                return new OtherUserInfo(in);
            }

            @Override
            public OtherUserInfo[] newArray(int size) {
                return new OtherUserInfo[size];
            }
        };

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

        public String getAccountId() {
            return AccountId;
        }

        public void setAccountId(String accountId) {
            AccountId = accountId;
        }

        public int getGender() {
            return Gender;
        }

        public void setGender(int gender) {
            Gender = gender;
        }

        public String getHeadUrl() {
            return HeadUrl;
        }

        public void setHeadUrl(String headUrl) {
            HeadUrl = headUrl;
        }

        public int getLevel() {
            return Level;
        }

        public void setLevel(int level) {
            Level = level;
        }

        public String getSignature() {
            return Signature;
        }

        public void setSignature(String signature) {
            Signature = signature;
        }

        public String getLocation() {
            return Location;
        }

        public void setLocation(String location) {
            Location = location;
        }

        public int getCertifiStatus() {
            return CertifiStatus;
        }

        public void setCertifiStatus(int certifiStatus) {
            CertifiStatus = certifiStatus;
        }

        public String getLiveCover() {
            return LiveCover;
        }

        public void setLiveCover(String liveCover) {
            LiveCover = liveCover;
        }

        public long getIncome() {
            return Income;
        }

        public void setIncome(long income) {
            Income = income;
        }

        public long getContri() {
            return Contri;
        }

        public void setContri(long contri) {
            Contri = contri;
        }

        public boolean isFollowed() {
            return Followed;
        }

        public void setFollowed(boolean followed) {
            Followed = followed;
        }

        public int getFollows() {
            return Follows;
        }

        public void setFollows(int follows) {
            Follows = follows;
        }

        public int getFollowers() {
            return Followers;
        }

        public void setFollowers(int followers) {
            Followers = followers;
        }

        public boolean isFollower() {
            return IsFollower;
        }

        public void setFollower(boolean follower) {
            IsFollower = follower;
        }

        public boolean isBaned() {
            return IsBaned;
        }

        public void setBaned(boolean baned) {
            IsBaned = baned;
        }

        public boolean isRoomMgr() {
            return IsRoomMgr;
        }

        public void setRoomMgr(boolean roomMgr) {
            IsRoomMgr = roomMgr;
        }

        public boolean isRoomStatus() {
            return RoomStatus;
        }

        public void setRoomStatus(boolean roomStatus) {
            RoomStatus = roomStatus;
        }

        public boolean isBlocked() {
            return Blocked;
        }

        public void setBlocked(boolean blocked) {
            Blocked = blocked;
        }

        public boolean isLiang() {
            return IsLiang;
        }

        public void setLiang(boolean liang) {
            IsLiang = liang;
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

        public int getBroadcastType() {
            return broadcastType;
        }

        public void setBroadcastType(int broadcastType) {
            this.broadcastType = broadcastType;
        }
    }
}
