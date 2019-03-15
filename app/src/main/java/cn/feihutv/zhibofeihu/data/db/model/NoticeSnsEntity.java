package cn.feihutv.zhibofeihu.data.db.model;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by pengzl on 2017/5/2.
 * 社区动态消息
 */
@Entity
public class NoticeSnsEntity {

    @Id(autoincrement = true)
    private Long id;

    private String uid;  // 用户的id

    private long time;  // 消息时间秒时间戳

    private String content;  // 消息内容

    private String headUrl; // 好友头像地址

    private String level;  // 发信人等级

    private String nickName;  // 发信人昵称

    private String userId;  // 好友id

    private String FeedId; //动态id

    private String imgUrl; // 动态的第一张图片（无图时为空）

    private String replyContent;  // 评论内容

    private int feedMsgType;  // 动态消息类型 1点赞 2评论 3转发

    private int FeedType;  // 动态类型 1普通 2MV

    private String FeedContent;  // 普通动态的内容（无图时）

    @Generated(hash = 2075579622)
    public NoticeSnsEntity(Long id, String uid, long time, String content,
            String headUrl, String level, String nickName, String userId,
            String FeedId, String imgUrl, String replyContent, int feedMsgType,
            int FeedType, String FeedContent) {
        this.id = id;
        this.uid = uid;
        this.time = time;
        this.content = content;
        this.headUrl = headUrl;
        this.level = level;
        this.nickName = nickName;
        this.userId = userId;
        this.FeedId = FeedId;
        this.imgUrl = imgUrl;
        this.replyContent = replyContent;
        this.feedMsgType = feedMsgType;
        this.FeedType = FeedType;
        this.FeedContent = FeedContent;
    }

    @Generated(hash = 523351403)
    public NoticeSnsEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHeadUrl() {
        return this.headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getLevel() {
        return this.level;
    }

    public void setLevel(String level) {
        this.level = level;
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

    public String getFeedId() {
        return this.FeedId;
    }

    public void setFeedId(String FeedId) {
        this.FeedId = FeedId;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getReplyContent() {
        return this.replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public int getFeedMsgType() {
        return this.feedMsgType;
    }

    public void setFeedMsgType(int feedMsgType) {
        this.feedMsgType = feedMsgType;
    }

    public int getFeedType() {
        return this.FeedType;
    }

    public void setFeedType(int FeedType) {
        this.FeedType = FeedType;
    }

    public String getFeedContent() {
        return this.FeedContent;
    }

    public void setFeedContent(String FeedContent) {
        this.FeedContent = FeedContent;
    }


}
