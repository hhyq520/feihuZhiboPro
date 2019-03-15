package cn.feihutv.zhibofeihu.data.db.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by chenliwen on 2017/5/24 14:19.
 * 佛祖保佑，永无BUG
 * 私信
 */
@Entity
public class MessageEntity {
    @Id
    private long time;  // 消息时间秒时间戳

    private String content;  // 消息内容

    private String senderId;  // 发信人id

    private String headUrl; // 发信人头像

    private String nickName; // 发信人昵称

    private boolean isComMeg;// 是否为收到的消息

    private String level;  // 发信人等级

    private String userId;  // 当前登录用户的id

    private int msgStatus;  // 发送状态  1代表消息发送成功 2代表消息发送失败  3代表消息正在发送

    @Generated(hash = 291729062)
    public MessageEntity(long time, String content, String senderId, String headUrl,
            String nickName, boolean isComMeg, String level, String userId,
            int msgStatus) {
        this.time = time;
        this.content = content;
        this.senderId = senderId;
        this.headUrl = headUrl;
        this.nickName = nickName;
        this.isComMeg = isComMeg;
        this.level = level;
        this.userId = userId;
        this.msgStatus = msgStatus;
    }

    @Generated(hash = 1797882234)
    public MessageEntity() {
    }

    public int getMsgStatus() {
        return this.msgStatus;
    }

    public void setMsgStatus(int msgStatus) {
        this.msgStatus = msgStatus;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLevel() {
        return this.level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public boolean getIsComMeg() {
        return this.isComMeg;
    }

    public void setIsComMeg(boolean isComMeg) {
        this.isComMeg = isComMeg;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadUrl() {
        return this.headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getSenderId() {
        return this.senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }




}
