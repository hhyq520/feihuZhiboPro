package cn.feihutv.zhibofeihu.data.db.model;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import cn.feihutv.zhibofeihu.data.db.model.HistoryRecordBean;
import cn.feihutv.zhibofeihu.data.db.model.MessageEntity;
import cn.feihutv.zhibofeihu.data.db.model.MvDownLog;
import cn.feihutv.zhibofeihu.data.db.model.NoticeSnsEntity;
import cn.feihutv.zhibofeihu.data.db.model.NoticeSysEntity;
import cn.feihutv.zhibofeihu.data.db.model.RecentItem;
import cn.feihutv.zhibofeihu.data.db.model.SearchHistoryInfo;
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
import cn.feihutv.zhibofeihu.data.local.model.TCRoomInfo;

import cn.feihutv.zhibofeihu.data.db.model.HistoryRecordBeanDao;
import cn.feihutv.zhibofeihu.data.db.model.MessageEntityDao;
import cn.feihutv.zhibofeihu.data.db.model.MvDownLogDao;
import cn.feihutv.zhibofeihu.data.db.model.NoticeSnsEntityDao;
import cn.feihutv.zhibofeihu.data.db.model.NoticeSysEntityDao;
import cn.feihutv.zhibofeihu.data.db.model.RecentItemDao;
import cn.feihutv.zhibofeihu.data.db.model.SearchHistoryInfoDao;
import cn.feihutv.zhibofeihu.data.db.model.SysConfigBeanDao;
import cn.feihutv.zhibofeihu.data.db.model.SysFontColorBeanDao;
import cn.feihutv.zhibofeihu.data.db.model.SysGameBetBeanDao;
import cn.feihutv.zhibofeihu.data.db.model.SysGiftNewBeanDao;
import cn.feihutv.zhibofeihu.data.db.model.SysGoodsNewBeanDao;
import cn.feihutv.zhibofeihu.data.db.model.SysGuardGoodsBeanDao;
import cn.feihutv.zhibofeihu.data.db.model.SysHBBeanDao;
import cn.feihutv.zhibofeihu.data.db.model.SysItemBeanDao;
import cn.feihutv.zhibofeihu.data.db.model.SysLaunchTagBeanDao;
import cn.feihutv.zhibofeihu.data.db.model.SysLevelBeanDao;
import cn.feihutv.zhibofeihu.data.db.model.SysMountNewBeanDao;
import cn.feihutv.zhibofeihu.data.db.model.SysSignAwardBeanDao;
import cn.feihutv.zhibofeihu.data.db.model.SysTipsBeanDao;
import cn.feihutv.zhibofeihu.data.db.model.SysVipBeanDao;
import cn.feihutv.zhibofeihu.data.db.model.SysVipGoodsBeanDao;
import cn.feihutv.zhibofeihu.data.db.model.TCRoomInfoDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig historyRecordBeanDaoConfig;
    private final DaoConfig messageEntityDaoConfig;
    private final DaoConfig mvDownLogDaoConfig;
    private final DaoConfig noticeSnsEntityDaoConfig;
    private final DaoConfig noticeSysEntityDaoConfig;
    private final DaoConfig recentItemDaoConfig;
    private final DaoConfig searchHistoryInfoDaoConfig;
    private final DaoConfig sysConfigBeanDaoConfig;
    private final DaoConfig sysFontColorBeanDaoConfig;
    private final DaoConfig sysGameBetBeanDaoConfig;
    private final DaoConfig sysGiftNewBeanDaoConfig;
    private final DaoConfig sysGoodsNewBeanDaoConfig;
    private final DaoConfig sysGuardGoodsBeanDaoConfig;
    private final DaoConfig sysHBBeanDaoConfig;
    private final DaoConfig sysItemBeanDaoConfig;
    private final DaoConfig sysLaunchTagBeanDaoConfig;
    private final DaoConfig sysLevelBeanDaoConfig;
    private final DaoConfig sysMountNewBeanDaoConfig;
    private final DaoConfig sysSignAwardBeanDaoConfig;
    private final DaoConfig sysTipsBeanDaoConfig;
    private final DaoConfig sysVipBeanDaoConfig;
    private final DaoConfig sysVipGoodsBeanDaoConfig;
    private final DaoConfig tCRoomInfoDaoConfig;

    private final HistoryRecordBeanDao historyRecordBeanDao;
    private final MessageEntityDao messageEntityDao;
    private final MvDownLogDao mvDownLogDao;
    private final NoticeSnsEntityDao noticeSnsEntityDao;
    private final NoticeSysEntityDao noticeSysEntityDao;
    private final RecentItemDao recentItemDao;
    private final SearchHistoryInfoDao searchHistoryInfoDao;
    private final SysConfigBeanDao sysConfigBeanDao;
    private final SysFontColorBeanDao sysFontColorBeanDao;
    private final SysGameBetBeanDao sysGameBetBeanDao;
    private final SysGiftNewBeanDao sysGiftNewBeanDao;
    private final SysGoodsNewBeanDao sysGoodsNewBeanDao;
    private final SysGuardGoodsBeanDao sysGuardGoodsBeanDao;
    private final SysHBBeanDao sysHBBeanDao;
    private final SysItemBeanDao sysItemBeanDao;
    private final SysLaunchTagBeanDao sysLaunchTagBeanDao;
    private final SysLevelBeanDao sysLevelBeanDao;
    private final SysMountNewBeanDao sysMountNewBeanDao;
    private final SysSignAwardBeanDao sysSignAwardBeanDao;
    private final SysTipsBeanDao sysTipsBeanDao;
    private final SysVipBeanDao sysVipBeanDao;
    private final SysVipGoodsBeanDao sysVipGoodsBeanDao;
    private final TCRoomInfoDao tCRoomInfoDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        historyRecordBeanDaoConfig = daoConfigMap.get(HistoryRecordBeanDao.class).clone();
        historyRecordBeanDaoConfig.initIdentityScope(type);

        messageEntityDaoConfig = daoConfigMap.get(MessageEntityDao.class).clone();
        messageEntityDaoConfig.initIdentityScope(type);

        mvDownLogDaoConfig = daoConfigMap.get(MvDownLogDao.class).clone();
        mvDownLogDaoConfig.initIdentityScope(type);

        noticeSnsEntityDaoConfig = daoConfigMap.get(NoticeSnsEntityDao.class).clone();
        noticeSnsEntityDaoConfig.initIdentityScope(type);

        noticeSysEntityDaoConfig = daoConfigMap.get(NoticeSysEntityDao.class).clone();
        noticeSysEntityDaoConfig.initIdentityScope(type);

        recentItemDaoConfig = daoConfigMap.get(RecentItemDao.class).clone();
        recentItemDaoConfig.initIdentityScope(type);

        searchHistoryInfoDaoConfig = daoConfigMap.get(SearchHistoryInfoDao.class).clone();
        searchHistoryInfoDaoConfig.initIdentityScope(type);

        sysConfigBeanDaoConfig = daoConfigMap.get(SysConfigBeanDao.class).clone();
        sysConfigBeanDaoConfig.initIdentityScope(type);

        sysFontColorBeanDaoConfig = daoConfigMap.get(SysFontColorBeanDao.class).clone();
        sysFontColorBeanDaoConfig.initIdentityScope(type);

        sysGameBetBeanDaoConfig = daoConfigMap.get(SysGameBetBeanDao.class).clone();
        sysGameBetBeanDaoConfig.initIdentityScope(type);

        sysGiftNewBeanDaoConfig = daoConfigMap.get(SysGiftNewBeanDao.class).clone();
        sysGiftNewBeanDaoConfig.initIdentityScope(type);

        sysGoodsNewBeanDaoConfig = daoConfigMap.get(SysGoodsNewBeanDao.class).clone();
        sysGoodsNewBeanDaoConfig.initIdentityScope(type);

        sysGuardGoodsBeanDaoConfig = daoConfigMap.get(SysGuardGoodsBeanDao.class).clone();
        sysGuardGoodsBeanDaoConfig.initIdentityScope(type);

        sysHBBeanDaoConfig = daoConfigMap.get(SysHBBeanDao.class).clone();
        sysHBBeanDaoConfig.initIdentityScope(type);

        sysItemBeanDaoConfig = daoConfigMap.get(SysItemBeanDao.class).clone();
        sysItemBeanDaoConfig.initIdentityScope(type);

        sysLaunchTagBeanDaoConfig = daoConfigMap.get(SysLaunchTagBeanDao.class).clone();
        sysLaunchTagBeanDaoConfig.initIdentityScope(type);

        sysLevelBeanDaoConfig = daoConfigMap.get(SysLevelBeanDao.class).clone();
        sysLevelBeanDaoConfig.initIdentityScope(type);

        sysMountNewBeanDaoConfig = daoConfigMap.get(SysMountNewBeanDao.class).clone();
        sysMountNewBeanDaoConfig.initIdentityScope(type);

        sysSignAwardBeanDaoConfig = daoConfigMap.get(SysSignAwardBeanDao.class).clone();
        sysSignAwardBeanDaoConfig.initIdentityScope(type);

        sysTipsBeanDaoConfig = daoConfigMap.get(SysTipsBeanDao.class).clone();
        sysTipsBeanDaoConfig.initIdentityScope(type);

        sysVipBeanDaoConfig = daoConfigMap.get(SysVipBeanDao.class).clone();
        sysVipBeanDaoConfig.initIdentityScope(type);

        sysVipGoodsBeanDaoConfig = daoConfigMap.get(SysVipGoodsBeanDao.class).clone();
        sysVipGoodsBeanDaoConfig.initIdentityScope(type);

        tCRoomInfoDaoConfig = daoConfigMap.get(TCRoomInfoDao.class).clone();
        tCRoomInfoDaoConfig.initIdentityScope(type);

        historyRecordBeanDao = new HistoryRecordBeanDao(historyRecordBeanDaoConfig, this);
        messageEntityDao = new MessageEntityDao(messageEntityDaoConfig, this);
        mvDownLogDao = new MvDownLogDao(mvDownLogDaoConfig, this);
        noticeSnsEntityDao = new NoticeSnsEntityDao(noticeSnsEntityDaoConfig, this);
        noticeSysEntityDao = new NoticeSysEntityDao(noticeSysEntityDaoConfig, this);
        recentItemDao = new RecentItemDao(recentItemDaoConfig, this);
        searchHistoryInfoDao = new SearchHistoryInfoDao(searchHistoryInfoDaoConfig, this);
        sysConfigBeanDao = new SysConfigBeanDao(sysConfigBeanDaoConfig, this);
        sysFontColorBeanDao = new SysFontColorBeanDao(sysFontColorBeanDaoConfig, this);
        sysGameBetBeanDao = new SysGameBetBeanDao(sysGameBetBeanDaoConfig, this);
        sysGiftNewBeanDao = new SysGiftNewBeanDao(sysGiftNewBeanDaoConfig, this);
        sysGoodsNewBeanDao = new SysGoodsNewBeanDao(sysGoodsNewBeanDaoConfig, this);
        sysGuardGoodsBeanDao = new SysGuardGoodsBeanDao(sysGuardGoodsBeanDaoConfig, this);
        sysHBBeanDao = new SysHBBeanDao(sysHBBeanDaoConfig, this);
        sysItemBeanDao = new SysItemBeanDao(sysItemBeanDaoConfig, this);
        sysLaunchTagBeanDao = new SysLaunchTagBeanDao(sysLaunchTagBeanDaoConfig, this);
        sysLevelBeanDao = new SysLevelBeanDao(sysLevelBeanDaoConfig, this);
        sysMountNewBeanDao = new SysMountNewBeanDao(sysMountNewBeanDaoConfig, this);
        sysSignAwardBeanDao = new SysSignAwardBeanDao(sysSignAwardBeanDaoConfig, this);
        sysTipsBeanDao = new SysTipsBeanDao(sysTipsBeanDaoConfig, this);
        sysVipBeanDao = new SysVipBeanDao(sysVipBeanDaoConfig, this);
        sysVipGoodsBeanDao = new SysVipGoodsBeanDao(sysVipGoodsBeanDaoConfig, this);
        tCRoomInfoDao = new TCRoomInfoDao(tCRoomInfoDaoConfig, this);

        registerDao(HistoryRecordBean.class, historyRecordBeanDao);
        registerDao(MessageEntity.class, messageEntityDao);
        registerDao(MvDownLog.class, mvDownLogDao);
        registerDao(NoticeSnsEntity.class, noticeSnsEntityDao);
        registerDao(NoticeSysEntity.class, noticeSysEntityDao);
        registerDao(RecentItem.class, recentItemDao);
        registerDao(SearchHistoryInfo.class, searchHistoryInfoDao);
        registerDao(SysConfigBean.class, sysConfigBeanDao);
        registerDao(SysFontColorBean.class, sysFontColorBeanDao);
        registerDao(SysGameBetBean.class, sysGameBetBeanDao);
        registerDao(SysGiftNewBean.class, sysGiftNewBeanDao);
        registerDao(SysGoodsNewBean.class, sysGoodsNewBeanDao);
        registerDao(SysGuardGoodsBean.class, sysGuardGoodsBeanDao);
        registerDao(SysHBBean.class, sysHBBeanDao);
        registerDao(SysItemBean.class, sysItemBeanDao);
        registerDao(SysLaunchTagBean.class, sysLaunchTagBeanDao);
        registerDao(SysLevelBean.class, sysLevelBeanDao);
        registerDao(SysMountNewBean.class, sysMountNewBeanDao);
        registerDao(SysSignAwardBean.class, sysSignAwardBeanDao);
        registerDao(SysTipsBean.class, sysTipsBeanDao);
        registerDao(SysVipBean.class, sysVipBeanDao);
        registerDao(SysVipGoodsBean.class, sysVipGoodsBeanDao);
        registerDao(TCRoomInfo.class, tCRoomInfoDao);
    }
    
    public void clear() {
        historyRecordBeanDaoConfig.clearIdentityScope();
        messageEntityDaoConfig.clearIdentityScope();
        mvDownLogDaoConfig.clearIdentityScope();
        noticeSnsEntityDaoConfig.clearIdentityScope();
        noticeSysEntityDaoConfig.clearIdentityScope();
        recentItemDaoConfig.clearIdentityScope();
        searchHistoryInfoDaoConfig.clearIdentityScope();
        sysConfigBeanDaoConfig.clearIdentityScope();
        sysFontColorBeanDaoConfig.clearIdentityScope();
        sysGameBetBeanDaoConfig.clearIdentityScope();
        sysGiftNewBeanDaoConfig.clearIdentityScope();
        sysGoodsNewBeanDaoConfig.clearIdentityScope();
        sysGuardGoodsBeanDaoConfig.clearIdentityScope();
        sysHBBeanDaoConfig.clearIdentityScope();
        sysItemBeanDaoConfig.clearIdentityScope();
        sysLaunchTagBeanDaoConfig.clearIdentityScope();
        sysLevelBeanDaoConfig.clearIdentityScope();
        sysMountNewBeanDaoConfig.clearIdentityScope();
        sysSignAwardBeanDaoConfig.clearIdentityScope();
        sysTipsBeanDaoConfig.clearIdentityScope();
        sysVipBeanDaoConfig.clearIdentityScope();
        sysVipGoodsBeanDaoConfig.clearIdentityScope();
        tCRoomInfoDaoConfig.clearIdentityScope();
    }

    public HistoryRecordBeanDao getHistoryRecordBeanDao() {
        return historyRecordBeanDao;
    }

    public MessageEntityDao getMessageEntityDao() {
        return messageEntityDao;
    }

    public MvDownLogDao getMvDownLogDao() {
        return mvDownLogDao;
    }

    public NoticeSnsEntityDao getNoticeSnsEntityDao() {
        return noticeSnsEntityDao;
    }

    public NoticeSysEntityDao getNoticeSysEntityDao() {
        return noticeSysEntityDao;
    }

    public RecentItemDao getRecentItemDao() {
        return recentItemDao;
    }

    public SearchHistoryInfoDao getSearchHistoryInfoDao() {
        return searchHistoryInfoDao;
    }

    public SysConfigBeanDao getSysConfigBeanDao() {
        return sysConfigBeanDao;
    }

    public SysFontColorBeanDao getSysFontColorBeanDao() {
        return sysFontColorBeanDao;
    }

    public SysGameBetBeanDao getSysGameBetBeanDao() {
        return sysGameBetBeanDao;
    }

    public SysGiftNewBeanDao getSysGiftNewBeanDao() {
        return sysGiftNewBeanDao;
    }

    public SysGoodsNewBeanDao getSysGoodsNewBeanDao() {
        return sysGoodsNewBeanDao;
    }

    public SysGuardGoodsBeanDao getSysGuardGoodsBeanDao() {
        return sysGuardGoodsBeanDao;
    }

    public SysHBBeanDao getSysHBBeanDao() {
        return sysHBBeanDao;
    }

    public SysItemBeanDao getSysItemBeanDao() {
        return sysItemBeanDao;
    }

    public SysLaunchTagBeanDao getSysLaunchTagBeanDao() {
        return sysLaunchTagBeanDao;
    }

    public SysLevelBeanDao getSysLevelBeanDao() {
        return sysLevelBeanDao;
    }

    public SysMountNewBeanDao getSysMountNewBeanDao() {
        return sysMountNewBeanDao;
    }

    public SysSignAwardBeanDao getSysSignAwardBeanDao() {
        return sysSignAwardBeanDao;
    }

    public SysTipsBeanDao getSysTipsBeanDao() {
        return sysTipsBeanDao;
    }

    public SysVipBeanDao getSysVipBeanDao() {
        return sysVipBeanDao;
    }

    public SysVipGoodsBeanDao getSysVipGoodsBeanDao() {
        return sysVipGoodsBeanDao;
    }

    public TCRoomInfoDao getTCRoomInfoDao() {
        return tCRoomInfoDao;
    }

}
