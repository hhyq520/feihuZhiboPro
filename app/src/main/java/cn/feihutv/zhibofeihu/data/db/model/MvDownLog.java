package cn.feihutv.zhibofeihu.data.db.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/12/12
 *     desc   : mv 下载日志
 *     version: 1.0
 * </pre>
 */
@Entity(nameInDb = "MvDownLog")
public class MvDownLog {


    @Id
    @Property(nameInDb = "id")
    private String id;// id 由用户id+mvid 拼接而成昨晚记录的唯一id存储

    @Property(nameInDb = "userId")
    private String userId;//

    @Property(nameInDb = "mvId")
    private String mvId;//mvId

    @Property(nameInDb = "mvUrl")
    private String mvUrl;//下载链接


    @Property(nameInDb = "state")
    private int state=-1;//下载状态,-1 未下载，0：已下载；1：正在下载;2:已暂停；3：下载失败；4：连接已过期


    @Property(nameInDb = "mvDownUrlValidityTime")
    private Long mvDownUrlValidityTime; //下载连接有效时间，单位毫秒时间戳


    @Property(nameInDb = "savePath")
    private String savePath;//本地存储路径

    @Property(nameInDb = "icon")
    private String icon;//封面

    @Property(nameInDb = "title")
    private String title;//标题

    @Property(nameInDb = "zbName")
    private String zbName;//主播名称


    @Property(nameInDb = "zbIcon")
    private String zbIcon;//主播cion

    @Property(nameInDb = "downTime")
    private Long downTime;//下载时间


    @Property(nameInDb = "seeTime")
    private Long seeTime;//观看时长

    @Property(nameInDb = "seeState")
    private int seeState;//观看状态






    @Generated(hash = 162310866)
    public MvDownLog(String id, String userId, String mvId, String mvUrl, int state,
            Long mvDownUrlValidityTime, String savePath, String icon, String title,
            String zbName, String zbIcon, Long downTime, Long seeTime,
            int seeState) {
        this.id = id;
        this.userId = userId;
        this.mvId = mvId;
        this.mvUrl = mvUrl;
        this.state = state;
        this.mvDownUrlValidityTime = mvDownUrlValidityTime;
        this.savePath = savePath;
        this.icon = icon;
        this.title = title;
        this.zbName = zbName;
        this.zbIcon = zbIcon;
        this.downTime = downTime;
        this.seeTime = seeTime;
        this.seeState = seeState;
    }


    @Generated(hash = 1523568313)
    public MvDownLog() {
    }


    public String getMvId() {
        return this.mvId;
    }


    public void setMvId(String mvId) {
        this.mvId = mvId;
    }


    public String getMvUrl() {
        return this.mvUrl;
    }


    public void setMvUrl(String mvUrl) {
        this.mvUrl = mvUrl;
    }


    public int getState() {
        return this.state;
    }


    public void setState(int state) {
        this.state = state;
    }


    public Long getMvDownUrlValidityTime() {
        return this.mvDownUrlValidityTime;
    }


    public void setMvDownUrlValidityTime(Long mvDownUrlValidityTime) {
        this.mvDownUrlValidityTime = mvDownUrlValidityTime;
    }


    public String getId() {
        return this.id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getUserId() {
        return this.userId;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getSavePath() {
        return this.savePath;
    }


    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }


    public String getIcon() {
        return this.icon;
    }


    public void setIcon(String icon) {
        this.icon = icon;
    }


    public String getTitle() {
        return this.title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public String getZbName() {
        return this.zbName;
    }


    public void setZbName(String zbName) {
        this.zbName = zbName;
    }


    public String getZbIcon() {
        return this.zbIcon;
    }


    public void setZbIcon(String zbIcon) {
        this.zbIcon = zbIcon;
    }


    public Long getDownTime() {
        return this.downTime;
    }


    public void setDownTime(Long downTime) {
        this.downTime = downTime;
    }


    public Long getSeeTime() {
        return this.seeTime;
    }


    public void setSeeTime(Long seeTime) {
        this.seeTime = seeTime;
    }


    public int getSeeState() {
        return this.seeState;
    }


    public void setSeeState(int seeState) {
        this.seeState = seeState;
    }




}
