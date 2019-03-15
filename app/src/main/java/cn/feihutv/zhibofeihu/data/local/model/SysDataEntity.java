package cn.feihutv.zhibofeihu.data.local.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;

import cn.feihutv.zhibofeihu.data.db.model.SysConfigBean;
import cn.feihutv.zhibofeihu.data.db.model.SysFontColorBean;
import cn.feihutv.zhibofeihu.data.db.model.SysGameBetBean;
import cn.feihutv.zhibofeihu.data.db.model.SysGiftNewBean;
import cn.feihutv.zhibofeihu.data.db.model.SysGoodsNewBean;
import cn.feihutv.zhibofeihu.data.db.model.SysGuardGoodsBean;
import cn.feihutv.zhibofeihu.data.db.model.SysHBBean;
import cn.feihutv.zhibofeihu.data.db.model.SysItemBean;
import cn.feihutv.zhibofeihu.data.db.model.SysLaunchTagBean;
import cn.feihutv.zhibofeihu.data.db.model.SysLevelBean;
import cn.feihutv.zhibofeihu.data.db.model.SysMountNewBean;
import cn.feihutv.zhibofeihu.data.db.model.SysSignAwardBean;
import cn.feihutv.zhibofeihu.data.db.model.SysTipsBean;
import cn.feihutv.zhibofeihu.data.db.model.SysVipBean;
import cn.feihutv.zhibofeihu.data.db.model.SysVipGoodsBean;

/**
 * Created by Administrator on 2017/3/20.
 */

public class SysDataEntity {
    @SerializedName("sysGameBet")
    private List<SysGameBetBean> sysGameBetX;
    private List<SysGiftNewBean> sysGiftNew;
    private List<SysHBBean> sysHB;
    private List<SysGoodsNewBean> sysGoodsNew;
    private List<SysLevelBean> sysLevel;
    private List<SysTipsBean> sysTips;
    private List<SysItemBean> sysItem;
    private List<SysMountNewBean> sysMountNew;
    private SysConfigBean sysConfig;
    private List<SysLaunchTagBean> sysLaunchTag;
    private List<SysSignAwardBean> sysSignAward;
    private List<SysFontColorBean> sysFontColor;
    private List<SysVipBean> sysVip;
    private List<SysVipGoodsBean> sysVipGoods;

    @Expose
    @SerializedName("sysGuardGoods")
    private List<SysGuardGoodsBean> mSysGuardGoodsBeen;

    public List<SysVipGoodsBean> getSysVipGoods() {
        return sysVipGoods;
    }

    public void setSysVipGoods(List<SysVipGoodsBean> sysVipGoods) {
        this.sysVipGoods = sysVipGoods;
    }

    public List<SysVipBean> getSysVip() {
        return sysVip;
    }

    public void setSysVip(List<SysVipBean> sysVip) {
        this.sysVip = sysVip;
    }

    public List<SysGuardGoodsBean> getSysGuardGoodsBeen() {
        return mSysGuardGoodsBeen;
    }

    public void setSysGuardGoodsBeen(List<SysGuardGoodsBean> sysGuardGoodsBeen) {
        mSysGuardGoodsBeen = sysGuardGoodsBeen;
    }

    public List<SysGiftNewBean> getSysGift() {
        return sysGiftNew;
    }

    public void setSysGift(List<SysGiftNewBean> sysGift) {
        this.sysGiftNew = sysGift;
    }

    public List<SysHBBean> getSysHB() {
        return sysHB;
    }

    public void setSysHB(List<SysHBBean> sysHB) {
        this.sysHB = sysHB;
    }

    public List<SysGoodsNewBean> getSysGoods() {
        return sysGoodsNew;
    }

    public void setSysGoods(List<SysGoodsNewBean> sysGoods) {
        this.sysGoodsNew = sysGoods;
    }

    public List<SysLevelBean> getSysLevel() {
        return sysLevel;
    }

    public void setSysLevel(List<SysLevelBean> sysLevel) {
        this.sysLevel = sysLevel;
    }

    public List<SysTipsBean> getSysTips() {
        return sysTips;
    }

    public void setSysTips(List<SysTipsBean> sysTips) {
        this.sysTips = sysTips;
    }

    public List<SysItemBean> getSysItem() {
        return sysItem;
    }

    public void setSysItem(List<SysItemBean> sysItem) {
        this.sysItem = sysItem;
    }

    public List<SysMountNewBean> getSysMount() {
        return sysMountNew;
    }

    public void setSysMount(List<SysMountNewBean> sysMount) {
        this.sysMountNew = sysMount;
    }

    public List<SysGameBetBean> getSysGameBetX() {
        return sysGameBetX;
    }

    public void setSysGameBetX(List<SysGameBetBean> sysGameBetX) {
        this.sysGameBetX = sysGameBetX;
    }

    public SysConfigBean getSysConfig() {
        return sysConfig;
    }

    public void setSysConfig(SysConfigBean sysConfig) {
        this.sysConfig = sysConfig;
    }

    public List<SysLaunchTagBean> getSysLaunchTag() {
        return sysLaunchTag;
    }

    public void setSysLaunchTag(List<SysLaunchTagBean> sysLaunchTag) {
        this.sysLaunchTag = sysLaunchTag;
    }

    public List<SysSignAwardBean> getSysSignAward() {
        return sysSignAward;
    }

    public void setSysSignAward(List<SysSignAwardBean> sysSignAward) {
        this.sysSignAward = sysSignAward;
    }

    public List<SysFontColorBean> getSysFontColor() {
        return sysFontColor;
    }

    public void setSysFontColor(List<SysFontColorBean> sysFontColor) {
        this.sysFontColor = sysFontColor;
    }
}
