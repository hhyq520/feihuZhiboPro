package cn.feihutv.zhibofeihu.data.db.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by chenliwen on 2017/5/24 10:09.
 * 佛祖保佑，永无BUG
 * 系统消息
 */
@Entity
public class NoticeSysEntity {

    @Id
    private Long id;
    private String uid;  // 用户的id

    private int time;  // 消息时间秒时间戳

    private String content;  // 消息内容

    private String action;  // 系统消息中交易请求消息有该字段

    private int gift;  // 礼物id

    private int cnt;  // 礼物个数

    private int amount;  // 交易总额

    private int expireAt;  // 过期时间

    private String userId;  // 发起人id

    private String nickName;  // 发起人飞虎昵称

    private String accountId;  // 发起人飞虎id

    private String tradeld; // 交易id

    private boolean isAccept;// 是否接受

    private boolean isIgnore;//是否忽略




    @Generated(hash = 539719940)
    public NoticeSysEntity(Long id, String uid, int time, String content,
            String action, int gift, int cnt, int amount, int expireAt,
            String userId, String nickName, String accountId, String tradeld,
            boolean isAccept, boolean isIgnore) {
        this.id = id;
        this.uid = uid;
        this.time = time;
        this.content = content;
        this.action = action;
        this.gift = gift;
        this.cnt = cnt;
        this.amount = amount;
        this.expireAt = expireAt;
        this.userId = userId;
        this.nickName = nickName;
        this.accountId = accountId;
        this.tradeld = tradeld;
        this.isAccept = isAccept;
        this.isIgnore = isIgnore;
    }

    @Generated(hash = 1905018434)
    public NoticeSysEntity() {
    }


 
   
    public boolean getIsAccept() {
        return this.isAccept;
    }

    public void setIsAccept(boolean isAccept) {
        this.isAccept = isAccept;
    }

    public String getTradeld() {
        return this.tradeld;
    }

    public void setTradeld(String tradeld) {
        this.tradeld = tradeld;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getExpireAt() {
        return this.expireAt;
    }

    public void setExpireAt(int expireAt) {
        this.expireAt = expireAt;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getCnt() {
        return this.cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public int getGift() {
        return this.gift;
    }

    public void setGift(int gift) {
        this.gift = gift;
    }

    public String getAction() {
        return this.action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTime() {
        return this.time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

   

    public boolean isIgnore() {
        return isIgnore;
    }

    public void setIgnore(boolean ignore) {
        isIgnore = ignore;
    }

    public boolean getIsIgnore() {
        return this.isIgnore;
    }

    public void setIsIgnore(boolean isIgnore) {
        this.isIgnore = isIgnore;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
