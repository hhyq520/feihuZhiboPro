package cn.feihutv.zhibofeihu.data.local.model;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/3/7.
 */
@Entity
public class TCRoomInfo  {
    @Id
    private String roomId;
    private String roomName;
    @Transient
    private TCUserInfo master;
    private String playUrl;
    private boolean roomStatus;
    private int broadcastType;
    private int onlineUserCnt;
    private String NickName ;  //用户昵称
    private String headUrl;  //用户头像
    private String LiveCover;//直播间封面
    private String Location;
    private boolean IsRoomMgr;
    private String GameOwner;//已获得权限 yxjc,lyzb,none
    @Transient
    private List<TCUserInfo> contributionList;



    @Generated(hash = 44216491)
    public TCRoomInfo(String roomId, String roomName, String playUrl,
            boolean roomStatus, int broadcastType, int onlineUserCnt,
            String NickName, String headUrl, String LiveCover, String Location,
            boolean IsRoomMgr, String GameOwner) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.playUrl = playUrl;
        this.roomStatus = roomStatus;
        this.broadcastType = broadcastType;
        this.onlineUserCnt = onlineUserCnt;
        this.NickName = NickName;
        this.headUrl = headUrl;
        this.LiveCover = LiveCover;
        this.Location = Location;
        this.IsRoomMgr = IsRoomMgr;
        this.GameOwner = GameOwner;
    }

    @Generated(hash = 819524885)
    public TCRoomInfo() {
    }



    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public TCUserInfo getMaster() {
        return master;
    }

    public void setMaster(TCUserInfo master) {
        this.master = master;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public boolean isRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(boolean roomStatus) {
        this.roomStatus = roomStatus;
    }

    public int getBroadcastType() {
        return broadcastType;
    }

    public void setBroadcastType(int broadcastType) {
        this.broadcastType = broadcastType;
    }

    public int getOnlineUserCnt() {
        return onlineUserCnt;
    }

    public void setOnlineUserCnt(int onlineUserCnt) {
        this.onlineUserCnt = onlineUserCnt;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public List<TCUserInfo> getContributionList() {
        return contributionList;
    }

    public void setContributionList(List<TCUserInfo> contributionList) {
        this.contributionList = contributionList;
    }

    public boolean getRoomStatus() {
        return this.roomStatus;
    }

    @Override
    public String toString() {
        return "TCRoomInfo{" +
            "roomId='" + roomId + '\'' +
            ", roomName='" + roomName + '\'' +
            ", master=" + master +
            ", playUrl='" + playUrl + '\'' +
            ", roomStatus=" + roomStatus +
            ", broadcastType=" + broadcastType +
            ", onlineUserCnt=" + onlineUserCnt +
            ", NickName='" + NickName + '\'' +
            ", headUrl='" + headUrl + '\'' +
            ", contributionList=" + contributionList +
            '}';
    }

    public String getLiveCover() {
        return LiveCover;
    }

    public void setLiveCover(String liveCover) {
        LiveCover = liveCover;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public boolean isRoomMgr() {
        return IsRoomMgr;
    }

    public void setRoomMgr(boolean roomMgr) {
        IsRoomMgr = roomMgr;
    }

    public boolean getIsRoomMgr() {
        return this.IsRoomMgr;
    }

    public void setIsRoomMgr(boolean IsRoomMgr) {
        this.IsRoomMgr = IsRoomMgr;
    }

    public String getGameOwner() {
        return GameOwner;
    }

    public void setGameOwner(String gameOwner) {
        GameOwner = gameOwner;
    }
}
