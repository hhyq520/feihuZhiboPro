package cn.feihutv.zhibofeihu.data.db.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

/**
 * <pre>
 *      author : liwen.chen
 *      time   : 2017/10/19 20:15
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */
@Entity
public class HistoryRecordBean {

    @Id(autoincrement = true)
    private Long id;

    private String userId;

    private String roomId;  // 房间Id

    private String headUrl;  // 直播封面

    private String title;  // 直播标题

    private String hostName;  // 主播昵称

    private long time; // 观看时间

    private int brocastType;

    @Generated(hash = 430498614)
    public HistoryRecordBean(Long id, String userId, String roomId, String headUrl,
            String title, String hostName, long time, int brocastType) {
        this.id = id;
        this.userId = userId;
        this.roomId = roomId;
        this.headUrl = headUrl;
        this.title = title;
        this.hostName = hostName;
        this.time = time;
        this.brocastType = brocastType;
    }

    @Generated(hash = 1791356846)
    public HistoryRecordBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoomId() {
        return this.roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getHeadUrl() {
        return this.headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHostName() {
        return this.hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getBrocastType() {
        return this.brocastType;
    }

    public void setBrocastType(int brocastType) {
        this.brocastType = brocastType;
    }
}
