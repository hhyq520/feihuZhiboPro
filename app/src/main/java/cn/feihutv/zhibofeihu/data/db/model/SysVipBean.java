package cn.feihutv.zhibofeihu.data.db.model;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/14 15:43
 *      desc   : SysVip
 *      version: 1.0
 * </pre>
 */
@Entity
public class SysVipBean {

    /**
     * vip : 1
     * needCzz : 1 需要的成长值
     * day : 10    每日成长值
     * watch30min : 10  看直播30分钟获得的成长值
     * online1hour : 10  在线一小时获得的成长值
     * giftExpAdd : 20  送礼增加经验值
     * gameCnt : 50  竞猜互动次数/天
     * vipMountId : 1
     */

    @Id
    private Long vip;
    private int needCzz;
    private String day;
    private String watch30min;
    private String online1hour;
    private String giftExpAdd;
    private String gameCnt;
    private String vipMountId;
    @Generated(hash = 1312837316)
    public SysVipBean(Long vip, int needCzz, String day, String watch30min,
            String online1hour, String giftExpAdd, String gameCnt,
            String vipMountId) {
        this.vip = vip;
        this.needCzz = needCzz;
        this.day = day;
        this.watch30min = watch30min;
        this.online1hour = online1hour;
        this.giftExpAdd = giftExpAdd;
        this.gameCnt = gameCnt;
        this.vipMountId = vipMountId;
    }
    @Generated(hash = 106125142)
    public SysVipBean() {
    }
    public Long getVip() {
        return this.vip;
    }
    public void setVip(Long vip) {
        this.vip = vip;
    }
    public int getNeedCzz() {
        return this.needCzz;
    }
    public void setNeedCzz(int needCzz) {
        this.needCzz = needCzz;
    }
    public String getDay() {
        return this.day;
    }
    public void setDay(String day) {
        this.day = day;
    }
    public String getWatch30min() {
        return this.watch30min;
    }
    public void setWatch30min(String watch30min) {
        this.watch30min = watch30min;
    }
    public String getOnline1hour() {
        return this.online1hour;
    }
    public void setOnline1hour(String online1hour) {
        this.online1hour = online1hour;
    }
    public String getGiftExpAdd() {
        return this.giftExpAdd;
    }
    public void setGiftExpAdd(String giftExpAdd) {
        this.giftExpAdd = giftExpAdd;
    }
    public String getGameCnt() {
        return this.gameCnt;
    }
    public void setGameCnt(String gameCnt) {
        this.gameCnt = gameCnt;
    }
    public String getVipMountId() {
        return this.vipMountId;
    }
    public void setVipMountId(String vipMountId) {
        this.vipMountId = vipMountId;
    }
}
