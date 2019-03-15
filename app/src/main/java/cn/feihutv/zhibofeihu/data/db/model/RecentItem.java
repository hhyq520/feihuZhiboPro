package cn.feihutv.zhibofeihu.data.db.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

@Entity
public class RecentItem  {

    @Id
    private String userId;  // 发信人的id

    private String headImg;// 头像

    private String name;// 消息来自

    private String message;// 消息内容

    private long time;// 消息日期

    private String uid;  // 登陆用户的uid

    private boolean isRead;  // 是否已读

    @Generated(hash = 1773090558)
    public RecentItem(String userId, String headImg, String name, String message,
            long time, String uid, boolean isRead) {
        this.userId = userId;
        this.headImg = headImg;
        this.name = name;
        this.message = message;
        this.time = time;
        this.uid = uid;
        this.isRead = isRead;
    }

    @Generated(hash = 1983251930)
    public RecentItem() {
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHeadImg() {
        return this.headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean getIsRead() {
        return this.isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }




}
