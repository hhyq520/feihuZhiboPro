package cn.feihutv.zhibofeihu.data.db;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.data.db.model.DaoMaster;
import cn.feihutv.zhibofeihu.data.db.model.DaoSession;
import cn.feihutv.zhibofeihu.data.db.model.HistoryRecordBean;
import cn.feihutv.zhibofeihu.data.db.model.HistoryRecordBeanDao;
import cn.feihutv.zhibofeihu.data.db.model.MessageEntity;
import cn.feihutv.zhibofeihu.data.db.model.MessageEntityDao;
import cn.feihutv.zhibofeihu.data.db.model.MvDownLog;
import cn.feihutv.zhibofeihu.data.db.model.MvDownLogDao;
import cn.feihutv.zhibofeihu.data.db.model.NoticeSnsEntity;
import cn.feihutv.zhibofeihu.data.db.model.NoticeSnsEntityDao;
import cn.feihutv.zhibofeihu.data.db.model.NoticeSysEntity;
import cn.feihutv.zhibofeihu.data.db.model.NoticeSysEntityDao;
import cn.feihutv.zhibofeihu.data.db.model.RecentItem;
import cn.feihutv.zhibofeihu.data.db.model.RecentItemDao;
import cn.feihutv.zhibofeihu.data.db.model.SearchHistoryInfo;
import cn.feihutv.zhibofeihu.data.db.model.SearchHistoryInfoDao;
import cn.feihutv.zhibofeihu.data.db.model.SysConfigBean;
import cn.feihutv.zhibofeihu.data.db.model.SysFontColorBean;
import cn.feihutv.zhibofeihu.data.db.model.SysFontColorBeanDao;
import cn.feihutv.zhibofeihu.data.db.model.SysGameBetBean;
import cn.feihutv.zhibofeihu.data.db.model.SysGameBetBeanDao;
import cn.feihutv.zhibofeihu.data.db.model.SysGiftNewBean;
import cn.feihutv.zhibofeihu.data.db.model.SysGiftNewBeanDao;
import cn.feihutv.zhibofeihu.data.db.model.SysGoodsNewBean;
import cn.feihutv.zhibofeihu.data.db.model.SysGoodsNewBeanDao;
import cn.feihutv.zhibofeihu.data.db.model.SysGuardGoodsBean;
import cn.feihutv.zhibofeihu.data.db.model.SysGuardGoodsBeanDao;
import cn.feihutv.zhibofeihu.data.db.model.SysHBBean;
import cn.feihutv.zhibofeihu.data.db.model.SysItemBean;
import cn.feihutv.zhibofeihu.data.db.model.SysLaunchTagBean;
import cn.feihutv.zhibofeihu.data.db.model.SysLaunchTagBeanDao;
import cn.feihutv.zhibofeihu.data.db.model.SysLevelBean;
import cn.feihutv.zhibofeihu.data.db.model.SysLevelBeanDao;
import cn.feihutv.zhibofeihu.data.db.model.SysMountNewBean;
import cn.feihutv.zhibofeihu.data.db.model.SysMountNewBeanDao;
import cn.feihutv.zhibofeihu.data.db.model.SysSignAwardBean;
import cn.feihutv.zhibofeihu.data.db.model.SysSignAwardBeanDao;
import cn.feihutv.zhibofeihu.data.db.model.SysTipsBean;
import cn.feihutv.zhibofeihu.data.db.model.SysVipBean;
import cn.feihutv.zhibofeihu.data.db.model.SysVipBeanDao;
import cn.feihutv.zhibofeihu.data.db.model.SysVipGoodsBean;
import cn.feihutv.zhibofeihu.data.db.model.SysVipGoodsBeanDao;
import cn.feihutv.zhibofeihu.data.local.model.SysbagBean;
import cn.feihutv.zhibofeihu.data.network.http.model.common.AwardsBean;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import io.reactivex.Observable;

import static cn.feihutv.zhibofeihu.data.db.model.HistoryRecordBeanDao.*;


/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/09/25
 *     desc   : 数据库操作业务实现类
 *     version: 1.0
 * </pre>
 */
@Singleton
public class AppDbHelper implements DbHelper {


    private final DaoSession mDaoSession;

    @Inject
    public AppDbHelper(DbOpenHelper dbOpenHelper) {
        mDaoSession = new DaoMaster(dbOpenHelper.getWritableDb()).newSession();
    }


    @Override
    public Observable<Boolean> saveSysGiftNew(final List<SysGiftNewBean> mGiftNewBeanList) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (mGiftNewBeanList != null) {
                    mDaoSession.getSysGiftNewBeanDao().insertOrReplaceInTx(mGiftNewBeanList);
                }
                return true;
            }
        });
    }

    @Override
    public List<SysGiftNewBean> getSysGiftNew() {
        return mDaoSession.getSysGiftNewBeanDao().queryBuilder()
                .list();
    }

    @Override
    public SysGiftNewBean getGiftBeanByID(String giftID) {

        return mDaoSession.getSysGiftNewBeanDao().queryBuilder()
                .where(SysGiftNewBeanDao.Properties.Id.eq(giftID))
                .unique();
    }

    @Override
    public SysMountNewBean getMountBeanByID(String mountID) {
        return mDaoSession.getSysMountNewBeanDao().queryBuilder()
                .where(SysMountNewBeanDao.Properties.Id.eq(mountID))
                .unique();
    }

    @Override
    public SysGoodsNewBean getGoodsBeanByGiftID(final String giftID) {
        List<SysGoodsNewBean> list = mDaoSession.getSysGoodsNewBeanDao().queryBuilder()
                .where(SysGoodsNewBeanDao.Properties.ObjId.eq(giftID))
                .list();
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Observable<List<SysGiftNewBean>> getBagBeanList(final List<SysbagBean> sysbagBeen) {
        return Observable.fromCallable(new Callable<List<SysGiftNewBean>>() {
            @Override
            public List<SysGiftNewBean> call() throws Exception {
                List<SysGiftNewBean> sysGiftNewBeanList = new ArrayList<SysGiftNewBean>();
                for (SysbagBean item : sysbagBeen) {
                    SysGiftNewBean giftBean = mDaoSession.getSysGiftNewBeanDao().queryBuilder()
                            .where(SysGiftNewBeanDao.Properties.Id.eq(item.getId()))
                            .unique();
                    if (giftBean != null) {
                        giftBean.setGiftCount(item.getCnt());
                        sysGiftNewBeanList.add(giftBean);
                    }
                }
                return sysGiftNewBeanList;
            }
        });
    }

    @Override
    public List<SysGameBetBean> getSysGameBet(final int level) {
        return mDaoSession.getSysGameBetBeanDao().queryBuilder()
                .where(SysGameBetBeanDao.Properties.LevelEnd.ge(level), SysGameBetBeanDao.Properties.LevelStart.le(level)).list();
    }

    @Override
    public SysLevelBean getLevelBeanByID(String level) {
        return mDaoSession.getSysLevelBeanDao().queryBuilder()
                .where(SysLevelBeanDao.Properties.Level.eq(level))
                .unique();
    }

    @Override
    public String getGoodIDByGiftID(String id) {
       List<SysGoodsNewBean> list= mDaoSession.getSysGoodsNewBeanDao().queryBuilder()
                .where(SysGoodsNewBeanDao.Properties.ObjId.eq(id))
                .list();
        if(list.size()>0){
            return list.get(0).getId();
        }else{
            return "";
        }

    }

    @Override
    public Observable<SysGoodsNewBean> getGoodIDByMountID(final String id) {
        return Observable.fromCallable(new Callable<SysGoodsNewBean>() {
            @Override
            public SysGoodsNewBean call() throws Exception {
                return mDaoSession.getSysGoodsNewBeanDao().queryBuilder()
                        .where(SysGoodsNewBeanDao.Properties.ObjId.eq(id))
                        .unique();
            }
        });
    }


    @Override
    public List<SysGiftNewBean> getLiveGiftList() {
        //如果是联合查询或者需要费时操作的查询需要使用rxjava 异步查询方式
        return mDaoSession.getSysGiftNewBeanDao().queryBuilder()
                .orderDesc(SysGiftNewBeanDao.Properties.SortOrder)
                .list();
    }

    @Override
    public Observable<List<SysGoodsNewBean>> getStoreGiftList() {
        return Observable.fromCallable(new Callable<List<SysGoodsNewBean>>() {
            @Override
            public List<SysGoodsNewBean> call() throws Exception {
                List<SysGoodsNewBean> storeGifts = new ArrayList<>();
                List<SysGoodsNewBean> sysGoods = mDaoSession.getSysGoodsNewBeanDao().queryBuilder()
                        .orderAsc(SysGoodsNewBeanDao.Properties.SortOrder)
                        .list();
                if (sysGoods != null) {
                    for (SysGoodsNewBean item : sysGoods) {
                        if (item.getShow().equals("1")) {
                            SysGiftNewBean giftNewBean = mDaoSession.getSysGiftNewBeanDao().queryBuilder()
                                    .where(SysGiftNewBeanDao.Properties.Id.eq(item.getObjId()))
                                    .unique();
                            if (giftNewBean != null) {
                                long chazhi = SharePreferenceUtil.getSessionLong(FeihuZhiboApplication.getApplication(), "chazhi");
                                long second = (long) (System.currentTimeMillis() / 1000) + chazhi;
                                if (second >= Long.valueOf(giftNewBean.getShelfBegin()) && second <= Long.valueOf(giftNewBean.getShelfEnd())) {
                                    storeGifts.add(item);
                                }
                            }
                        }

                    }
                }
                return storeGifts;
            }
        });
    }

    // TODO: 2017/11/8 需要简化查询条件
    @Override
    public Observable<List<SysGoodsNewBean>> getStoreZuojia() {
        return Observable.fromCallable(new Callable<List<SysGoodsNewBean>>() {
            @Override
            public List<SysGoodsNewBean> call() throws Exception {
                List<SysGoodsNewBean> storeGifts = new ArrayList<>();
                List<SysGoodsNewBean> sysGoods = mDaoSession.getSysGoodsNewBeanDao().queryBuilder()
                        .orderDesc(SysGoodsNewBeanDao.Properties.SortOrder)
                        .list();
                if (sysGoods != null) {
                    for (SysGoodsNewBean item : sysGoods) {
                        if (item.getShow().equals("2")) {

                            long chazhi = SharePreferenceUtil.getSessionLong(FeihuZhiboApplication.getApplication(), "chazhi");
                            long second = (long) (System.currentTimeMillis() / 1000) + chazhi;
                            if (second >= Long.valueOf(item.getShelfBegin()) && second <= Long.valueOf(item.getShelfEnd())) {
                                storeGifts.add(item);
                            }
                        }
                    }
                }
                return storeGifts;
            }
        });
    }

    @Override
    public Observable<Boolean> saveHistoryRecord(final HistoryRecordBean recordBean) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                HistoryRecordBean unique = mDaoSession.getHistoryRecordBeanDao().queryBuilder().where(HistoryRecordBeanDao.Properties.UserId.eq(recordBean.getUserId()), HistoryRecordBeanDao.Properties.RoomId.eq(recordBean.getRoomId())).unique();
                AppLogger.i("unique" + unique);
                if (unique != null) {
                    unique.setTime(recordBean.getTime());
                    mDaoSession.getHistoryRecordBeanDao().update(unique);
                } else {
                    mDaoSession.getHistoryRecordBeanDao().insert(recordBean);
                }
                return true;
            }
        });
    }

    @Override
    public Observable<List<HistoryRecordBean>> getAllHistory(final String userId) {
        return Observable.fromCallable(new Callable<List<HistoryRecordBean>>() {
            @Override
            public List<HistoryRecordBean> call() throws Exception {
                return mDaoSession.getHistoryRecordBeanDao()
                        .queryBuilder().orderDesc(Properties.Time)
                        .where(Properties.UserId.eq(userId))
                        .list();
            }
        });
    }

    @Override
    public Observable<Boolean> deleteAllHistory(final String userId) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                List<HistoryRecordBean> historyRecordBeanList = mDaoSession.getHistoryRecordBeanDao()
                        .queryBuilder().orderDesc(Properties.Time)
                        .where(Properties.UserId.eq(userId))
                        .list();
                mDaoSession.getHistoryRecordBeanDao().deleteInTx(historyRecordBeanList);
                return true;
            }
        });
    }

    @Override
    public Observable<Boolean> saveSearchInfo(final SearchHistoryInfo historyInfo) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mDaoSession.getSearchHistoryInfoDao().insertOrReplace(historyInfo);
                return true;
            }
        });
    }

    @Override
    public Observable<List<SearchHistoryInfo>> getAllSearchInfo() {
        return Observable.fromCallable(new Callable<List<SearchHistoryInfo>>() {
            @Override
            public List<SearchHistoryInfo> call() throws Exception {
                return mDaoSession.getSearchHistoryInfoDao()
                        .queryBuilder().orderDesc(SearchHistoryInfoDao.Properties.Time)
                        .list();
            }
        });
    }

    @Override
    public Observable<Boolean> deleteAllSearchInfo() {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mDaoSession.getSearchHistoryInfoDao().deleteAll();
                return true;
            }
        });
    }

    @Override
    public Observable<Boolean> deleteSearchInfoByAccountId(final String accountId) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mDaoSession.getSearchHistoryInfoDao().deleteByKey(accountId);
                return true;
            }
        });
    }

    @Override
    public Observable<Boolean> saveNoticeSysEntity(final NoticeSysEntity entity) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mDaoSession.getNoticeSysEntityDao().insertOrReplace(entity);
                return true;
            }
        });
    }

    @Override
    public Observable<Boolean> updateNoticeSys(final NoticeSysEntity entity) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mDaoSession.getNoticeSysEntityDao().update(entity);
                return true;
            }
        });
    }

    @Override
    public Observable<List<NoticeSysEntity>> queryNoticeSysByUid(final String userId) {
        return Observable.fromCallable(new Callable<List<NoticeSysEntity>>() {
            @Override
            public List<NoticeSysEntity> call() throws Exception {
                return mDaoSession.getNoticeSysEntityDao().queryBuilder()
                        .where(NoticeSysEntityDao.Properties.Uid.eq(userId))
                        .orderDesc(NoticeSysEntityDao.Properties.Time)
                        .list();
            }
        });
    }

    @Override
    public Observable<Boolean> saveNoticeSnsEntity(final NoticeSnsEntity entity) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mDaoSession.getNoticeSnsEntityDao().insertOrReplace(entity);
                return true;
            }
        });
    }

    @Override
    public Observable<List<NoticeSnsEntity>> getNoticeSnsEntity(final String uid) {
        return Observable.fromCallable(new Callable<List<NoticeSnsEntity>>() {
            @Override
            public List<NoticeSnsEntity> call() throws Exception {
                return mDaoSession.getNoticeSnsEntityDao().queryBuilder()
                        .where(NoticeSnsEntityDao.Properties.Uid.eq(uid))
                        .orderDesc(NoticeSnsEntityDao.Properties.Time)
                        .list();
            }
        });
    }

    @Override
    public Observable<Boolean> deleteNoticeSnsEntityById(final long id) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mDaoSession.getNoticeSnsEntityDao().deleteByKey(id);
                return true;
            }
        });
    }

    @Override
    public Observable<Boolean> saveMessageEntity(final MessageEntity entity) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mDaoSession.getMessageEntityDao().insertOrReplace(entity);
                return true;
            }
        });
    }

    @Override
    public Observable<Boolean> updataMessageEntity(final MessageEntity entity) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mDaoSession.getMessageEntityDao().update(entity);
                return true;
            }
        });
    }

    @Override
    public Observable<Boolean> saveRecentItem(final RecentItem entity) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mDaoSession.getRecentItemDao().insertOrReplace(entity);
                return true;
            }
        });
    }

    @Override
    public Observable<Boolean> deleteRecentItem(final RecentItem enity) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mDaoSession.getRecentItemDao().delete(enity);
                return true;
            }
        });
    }

    @Override
    public List<RecentItem> queryRecentMessageItem(String uid) {
        return mDaoSession.getRecentItemDao()
                .queryBuilder()
                .where(RecentItemDao.Properties.Uid.eq(uid))
                .orderDesc(RecentItemDao.Properties.Time)
                .list();
    }

    @Override
    public Observable<Boolean> updateRecentItem(final RecentItem enity) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mDaoSession.getRecentItemDao().update(enity);
                return true;
            }
        });
    }

    @Override
    public List<MessageEntity> queryListMessage(String senderId, String uid) {
        return mDaoSession.getMessageEntityDao().queryBuilder()
                .where(MessageEntityDao.Properties.SenderId.eq(senderId), MessageEntityDao.Properties.UserId.eq(uid))
                .orderAsc(MessageEntityDao.Properties.Time).list();
    }

    @Override
    public List<SysLaunchTagBean> getkaiboSysLaunchTagBean() {
        return mDaoSession.getSysLaunchTagBeanDao().queryBuilder().where(SysLaunchTagBeanDao.Properties.Type.eq("2")).orderDesc(SysLaunchTagBeanDao.Properties.LaunchSort)
                .list();
    }

    @Override
    public Observable<Boolean> saveSysFontColorBean(final List<SysFontColorBean> list) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (list != null) {
                    mDaoSession.getSysFontColorBeanDao().insertOrReplaceInTx(list);
                }
                return true;
            }
        });
    }

    @Override
    public SysFontColorBean getSysFontColorBeanByKey(String key) {
        SysFontColorBean sysSysFontColorBean = mDaoSession.getSysFontColorBeanDao().queryBuilder().
                where(SysFontColorBeanDao.Properties.Key.eq(key))
                .unique();
        return sysSysFontColorBean;
    }

    @Override
    public SysVipBean getSysVipBeanByVip(int vip) {
        return mDaoSession.getSysVipBeanDao().queryBuilder().where(SysVipBeanDao.Properties.Vip.eq(vip)).unique();
    }

    @Override
    public SysVipGoodsBean getSysVipNameById(String id) {
        return mDaoSession.getSysVipGoodsBeanDao().queryBuilder().where(SysVipGoodsBeanDao.Properties.GoodsId.eq(id)).unique();
    }

    @Override
    public void saveMvDownLog(MvDownLog downLog) {
          mDaoSession.getMvDownLogDao().save(downLog);
    }

    @Override
    public MvDownLog getMvDownLogByMvId(String mvId, String userId) {
        return mDaoSession.getMvDownLogDao().queryBuilder()
                .where(MvDownLogDao.Properties.MvId.eq(mvId),
                        MvDownLogDao.Properties.UserId.eq(userId))
                .unique()
                ;
    }

    @Override
    public List<MvDownLog> getMvDownLogList(String userId) {
        return mDaoSession.getMvDownLogDao().queryBuilder().where(
                        MvDownLogDao.Properties.UserId.eq(userId))
                .list()
                ;
    }

    @Override
    public void deleteMvDownLogById(String id) {
        mDaoSession.getMvDownLogDao().deleteByKey(id);
    }

    @Override
    public Observable<Boolean> saveSysHBBean(final List<SysHBBean> beanList) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (beanList != null) {
                    mDaoSession.getSysHBBeanDao().insertOrReplaceInTx(beanList);
                }
                return true;
            }
        });
    }

    @Override
    public List<SysHBBean> getSysHBBean() {
        return mDaoSession.getSysHBBeanDao().queryBuilder()
                .list();
    }

    @Override
    public Observable<Boolean> saveSysLevelBean(final List<SysLevelBean> beanList) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (beanList != null) {
                    mDaoSession.getSysLevelBeanDao().insertOrReplaceInTx(beanList);
                }
                return true;
            }
        });
    }

    @Override
    public List<SysLevelBean> getSysLevelBean() {
        return mDaoSession.getSysLevelBeanDao().queryBuilder()
                .list();

    }

    @Override
    public Observable<Boolean> saveSysTipsBean(final List<SysTipsBean> beanList) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (beanList != null) {
                    mDaoSession.getSysTipsBeanDao().insertOrReplaceInTx(beanList);
                }
                return true;
            }
        });
    }

    @Override
    public List<SysTipsBean> getSysTipsBean() {
        return mDaoSession.getSysTipsBeanDao().queryBuilder()
                .list();

    }

    @Override
    public Observable<Boolean> saveSysGoodsNewBean(final List<SysGoodsNewBean> beanList) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (beanList != null) {
                    mDaoSession.getSysGoodsNewBeanDao().insertOrReplaceInTx(beanList);
                }
                return true;
            }
        });
    }

    @Override
    public List<SysGoodsNewBean> getSysGoodsNewBean() {
        return mDaoSession.getSysGoodsNewBeanDao().queryBuilder()
                .list();
    }

    @Override
    public Observable<Boolean> saveSysItemBean(final List<SysItemBean> beanList) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (beanList != null) {
                    mDaoSession.getSysItemBeanDao().insertOrReplaceInTx(beanList);
                }
                return true;
            }
        });
    }

    @Override
    public List<SysItemBean> getSysItemBean() {
        return mDaoSession.getSysItemBeanDao().queryBuilder()
                .list();


    }

    @Override
    public Observable<Boolean> saveSysGameBetBean(final List<SysGameBetBean> beanList) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (beanList != null) {
                    mDaoSession.getSysGameBetBeanDao().insertOrReplaceInTx(beanList);
                }
                return true;
            }
        });
    }

    @Override
    public List<SysGameBetBean> getSysGameBetBean() {
        return mDaoSession.getSysGameBetBeanDao().queryBuilder()
                .list();

    }

    @Override
    public Observable<Boolean> saveSysMountNewBean(final List<SysMountNewBean> beanList) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (beanList != null) {
                    mDaoSession.getSysMountNewBeanDao().insertOrReplaceInTx(beanList);
                }
                return true;
            }
        });
    }

    @Override
    public List<SysMountNewBean> getSysMountNewBean() {
        return mDaoSession.getSysMountNewBeanDao().queryBuilder()
                .list();
    }

    @Override
    public Observable<Boolean> saveSysConfigBean(final SysConfigBean mSysConfigBean) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (mSysConfigBean != null) {
                    mSysConfigBean.setId(1L);
                    mDaoSession.getSysConfigBeanDao().insertOrReplace(mSysConfigBean);
                }
                return true;
            }
        });
    }

    @Override
    public List<SysConfigBean> getSysConfigBean() {
        return mDaoSession.getSysConfigBeanDao().queryBuilder()
                .list();

    }


    @Override
    public SysConfigBean getSysConfig() {
        return mDaoSession.getSysConfigBeanDao().queryBuilder().unique();
    }

    @Override
    public Observable<Boolean> saveSysLaunchTagBean(final List<SysLaunchTagBean> beanList) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (beanList != null) {
                    mDaoSession.getSysLaunchTagBeanDao().insertOrReplaceInTx(beanList);
                } else {
                    AppLogger.w("SysLaunchTagBean data is null ...");
                }
                return true;
            }
        });
    }

    @Override
    public Observable<List<SysLaunchTagBean>> getSysLaunchTagBean() {
        return Observable.fromCallable(new Callable<List<SysLaunchTagBean>>() {
            @Override
            public List<SysLaunchTagBean> call() throws Exception {
                return mDaoSession.getSysLaunchTagBeanDao().queryBuilder().orderDesc(SysLaunchTagBeanDao.Properties.LaunchSort)
                        .list();
            }
        });
    }

    @Override
    public Observable<Boolean> saveSysGuardGoodsBean(final List<SysGuardGoodsBean> sysGuardGoodsBeen) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (sysGuardGoodsBeen != null) {
                    mDaoSession.getSysGuardGoodsBeanDao().insertOrReplaceInTx(sysGuardGoodsBeen);
                } else {
                    AppLogger.w("SysGuardGoodsBean data is null ...");
                }
                return true;
            }
        });
    }

    @Override
    public Observable<List<SysGuardGoodsBean>> getSysGuardGoodsBean() {
        return Observable.fromCallable(new Callable<List<SysGuardGoodsBean>>() {
            @Override
            public List<SysGuardGoodsBean> call() throws Exception {
                return mDaoSession.getSysGuardGoodsBeanDao().queryBuilder().orderAsc(SysGuardGoodsBeanDao.Properties.GoodsId)
                        .list();
            }
        });
    }

    @Override
    public Observable<Boolean> saveSysSignAwardBean(final List<SysSignAwardBean> sysSignAwardBeen) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (sysSignAwardBeen != null) {
                    mDaoSession.getSysSignAwardBeanDao().insertOrReplaceInTx(sysSignAwardBeen);
                }
                return true;
            }
        });
    }

    @Override
    public Observable<List<AwardsBean>> getSysSignAwardBean() {
        return Observable.fromCallable(new Callable<List<AwardsBean>>() {
            @Override
            public List<AwardsBean> call() throws Exception {
                List<AwardsBean> awardsBeen = new ArrayList<AwardsBean>();
                List<SysSignAwardBean> sysSignAwardBeen = mDaoSession.getSysSignAwardBeanDao().queryBuilder().orderAsc(SysSignAwardBeanDao.Properties.Day)
                        .list();
                awardsBeen.clear();
                for (int i = 0; i < sysSignAwardBeen.size(); i++) {
                    AwardsBean awardsBean = new AwardsBean();
                    awardsBean.setDay(Integer.parseInt(sysSignAwardBeen.get(i).getDay()));
                    awardsBean.setAwardsDatases(AwardsBean.AwardsDatas.arrayAwardsBeanFromData(sysSignAwardBeen.get(i).getGoods()));
                    awardsBean.setSignDay(false);
                    awardsBeen.add(awardsBean);
                }
                return awardsBeen;
            }
        });
    }

    @Override
    public Observable<Boolean> saveSysVipGoodsBean(final List<SysVipGoodsBean> sysVipGoodsBeen) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (sysVipGoodsBeen != null) {
                    mDaoSession.getSysVipGoodsBeanDao().insertOrReplaceInTx(sysVipGoodsBeen);
                }
                return true;
            }
        });
    }

    @Override
    public Observable<List<SysVipGoodsBean>> getSysVipGoodsBean() {
        return Observable.fromCallable(new Callable<List<SysVipGoodsBean>>() {
            @Override
            public List<SysVipGoodsBean> call() throws Exception {
                return mDaoSession.getSysVipGoodsBeanDao().queryBuilder().orderAsc(SysVipGoodsBeanDao.Properties.SortOrder).list();
            }
        });
    }

    @Override
    public Observable<Boolean> saveSysVipBean(final List<SysVipBean> sysVipBeen) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (sysVipBeen != null) {
                    mDaoSession.getSysVipBeanDao().insertOrReplaceInTx(sysVipBeen);
                }
                return true;
            }
        });
    }

    @Override
    public Observable<List<SysVipBean>> getSysVipBean() {
        return Observable.fromCallable(new Callable<List<SysVipBean>>() {
            @Override
            public List<SysVipBean> call() throws Exception {
                return mDaoSession.getSysVipBeanDao().queryBuilder().orderAsc(SysVipBeanDao.Properties.Vip).list();
            }
        });
    }

}
