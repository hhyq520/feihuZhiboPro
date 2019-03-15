package cn.feihutv.zhibofeihu.data.db.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by huanghao on 2017/10/12.
 */
@Entity
public class SysGameBetBean {
    /**
     * levelStart : 11
     * levelEnd : 15
     * maxBankerCnt : 25
     * maxBankerBet : 20000
     * maxPlayerCnt : 50
     * maxPlayerBat : 4000
     */
    @Id
    private long levelStart;
    private long levelEnd;
    private String maxBankerCnt;
    private String maxBankerBet;
    private String maxPlayerCnt;
    private String maxPlayerBat;
    @Generated(hash = 1616372470)
    public SysGameBetBean(long levelStart, long levelEnd, String maxBankerCnt,
            String maxBankerBet, String maxPlayerCnt, String maxPlayerBat) {
        this.levelStart = levelStart;
        this.levelEnd = levelEnd;
        this.maxBankerCnt = maxBankerCnt;
        this.maxBankerBet = maxBankerBet;
        this.maxPlayerCnt = maxPlayerCnt;
        this.maxPlayerBat = maxPlayerBat;
    }
    @Generated(hash = 1374211934)
    public SysGameBetBean() {
    }
    public long getLevelStart() {
        return this.levelStart;
    }
    public void setLevelStart(long levelStart) {
        this.levelStart = levelStart;
    }
    public long getLevelEnd() {
        return this.levelEnd;
    }
    public void setLevelEnd(long levelEnd) {
        this.levelEnd = levelEnd;
    }
    public String getMaxBankerCnt() {
        return this.maxBankerCnt;
    }
    public void setMaxBankerCnt(String maxBankerCnt) {
        this.maxBankerCnt = maxBankerCnt;
    }
    public String getMaxBankerBet() {
        return this.maxBankerBet;
    }
    public void setMaxBankerBet(String maxBankerBet) {
        this.maxBankerBet = maxBankerBet;
    }
    public String getMaxPlayerCnt() {
        return this.maxPlayerCnt;
    }
    public void setMaxPlayerCnt(String maxPlayerCnt) {
        this.maxPlayerCnt = maxPlayerCnt;
    }
    public String getMaxPlayerBat() {
        return this.maxPlayerBat;
    }
    public void setMaxPlayerBat(String maxPlayerBat) {
        this.maxPlayerBat = maxPlayerBat;
    }
   

 
}
