package cn.feihutv.zhibofeihu.data.db;

import java.util.List;

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
import cn.feihutv.zhibofeihu.data.local.model.SysbagBean;
import cn.feihutv.zhibofeihu.data.network.http.model.common.AwardsBean;
import io.reactivex.Observable;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/09/25
 *     desc   : 业务操作数据库接口,定义具体业务方法+
 *     version: 1.0
 * </pre>
 */
public interface DbHelper {


    //保存礼物
    Observable<Boolean> saveSysGiftNew(List<SysGiftNewBean> mGiftNewBeanList);

    //获取礼物
    List<SysGiftNewBean> getSysGiftNew();

    //保存
    Observable<Boolean> saveSysHBBean(List<SysHBBean> beanList);

    List<SysHBBean> getSysHBBean();

    //保存
    Observable<Boolean> saveSysLevelBean(List<SysLevelBean> beanList);

    List<SysLevelBean> getSysLevelBean();

    //保存
    Observable<Boolean> saveSysTipsBean(List<SysTipsBean> beanList);

    List<SysTipsBean> getSysTipsBean();

    //保存
    Observable<Boolean> saveSysGoodsNewBean(List<SysGoodsNewBean> beanList);

    List<SysGoodsNewBean> getSysGoodsNewBean();

    //保存
    Observable<Boolean> saveSysItemBean(List<SysItemBean> beanList);

    List<SysItemBean> getSysItemBean();

    //保存
    Observable<Boolean> saveSysGameBetBean(List<SysGameBetBean> beanList);

    List<SysGameBetBean> getSysGameBetBean();

    //保存
    Observable<Boolean> saveSysMountNewBean(List<SysMountNewBean> beanList);

    List<SysMountNewBean> getSysMountNewBean();

    //保存
    Observable<Boolean> saveSysConfigBean(SysConfigBean mSysConfigBean);

    List<SysConfigBean> getSysConfigBean();

    //查询系统配置
    SysConfigBean getSysConfig();

    //标签
    Observable<Boolean> saveSysLaunchTagBean(List<SysLaunchTagBean> mSysLaunchTagBean);

    Observable<List<SysLaunchTagBean>> getSysLaunchTagBean();

    // 座驾
    Observable<Boolean> saveSysGuardGoodsBean(List<SysGuardGoodsBean> sysGuardGoodsBeen);


    Observable<List<SysGuardGoodsBean>> getSysGuardGoodsBean();

    //签到奖励
    Observable<Boolean> saveSysSignAwardBean(List<SysSignAwardBean> sysSignAwardBeen);

    Observable<List<AwardsBean>> getSysSignAwardBean();

    //保存VipGoods
    Observable<Boolean> saveSysVipGoodsBean(List<SysVipGoodsBean> sysVipGoodsBeen);

    Observable<List<SysVipGoodsBean>> getSysVipGoodsBean();

    //保存Vip
    Observable<Boolean> saveSysVipBean(List<SysVipBean> sysVipGoodsBeen);

    Observable<List<SysVipBean>> getSysVipBean();

    //获取单个礼物
    SysGiftNewBean getGiftBeanByID(String giftID);

    //获取单个座驾
    SysMountNewBean getMountBeanByID(String mountID);

    //根据礼物Id获取单个商品
    SysGoodsNewBean getGoodsBeanByGiftID(String giftID);

    //获取背包所有礼物
    Observable<List<SysGiftNewBean>> getBagBeanList(List<SysbagBean> sysbagBeen);

    //根据等级获得玩法下注情况
    List<SysGameBetBean> getSysGameBet(int level);

    //根据等级获取等级信息
    SysLevelBean getLevelBeanByID(String level);

    //根据id获取商品信息
    String getGoodIDByGiftID(String id);

    /**
     * 根据座驾Id获取单个商品
     */
    Observable<SysGoodsNewBean> getGoodIDByMountID(String id);

    /**
     * 获取直播间礼物并排序
     */
    List<SysGiftNewBean> getLiveGiftList();

    /**
     * 获取商城礼物列表并排序
     */
    Observable<List<SysGoodsNewBean>> getStoreGiftList();

    /**
     * 获取商城座驾列表并排序
     */
    Observable<List<SysGoodsNewBean>> getStoreZuojia();

    /**
     * 插入观看历史记录
     *
     * @param recordBean
     * @return
     */
    Observable<Boolean> saveHistoryRecord(HistoryRecordBean recordBean);

    /**
     * 查询所有观看历史记录
     */
    Observable<List<HistoryRecordBean>> getAllHistory(String userId);

    /**
     * 删除所有观看历史记录
     */

    Observable<Boolean> deleteAllHistory(String userId);

    /**
     * 插入搜索记录
     */
    Observable<Boolean> saveSearchInfo(SearchHistoryInfo historyInfo);

    /**
     * 查询搜索记录
     */
    Observable<List<SearchHistoryInfo>> getAllSearchInfo();

    /**
     * 删除所有观看历史记录
     */

    Observable<Boolean> deleteAllSearchInfo();

    /**
     * 删除所有观看历史记录
     */
    Observable<Boolean> deleteSearchInfoByAccountId(String accountId);

    //保存系统消息
    Observable<Boolean> saveNoticeSysEntity(NoticeSysEntity entity);

    // 更新系统消息
    Observable<Boolean> updateNoticeSys(NoticeSysEntity entity);

    // 查询系统消息
    Observable<List<NoticeSysEntity>> queryNoticeSysByUid(String userId);

    //保存社区消息
    Observable<Boolean> saveNoticeSnsEntity(NoticeSnsEntity entity);

    // 获取动态消息
    Observable<List<NoticeSnsEntity>> getNoticeSnsEntity(String uid);

    // 根据id删除动态消息
    Observable<Boolean> deleteNoticeSnsEntityById(long id);

    //保存私信消息
    Observable<Boolean> saveMessageEntity(MessageEntity entity);

    //更新私信消息
    Observable<Boolean> updataMessageEntity(MessageEntity entity);

    //保存私信列表
    Observable<Boolean> saveRecentItem(RecentItem entity);

    //删除已个私信
    Observable<Boolean> deleteRecentItem(RecentItem enity);

    //查询所有私信
    List<RecentItem> queryRecentMessageItem(String uid);

    Observable<Boolean> updateRecentItem(RecentItem enity);

    //加载历史消息
    List<MessageEntity> queryListMessage(String senderId, String uid);

    //获取开播标签
    List<SysLaunchTagBean> getkaiboSysLaunchTagBean();

    //保存字体颜色配置
    Observable<Boolean> saveSysFontColorBean(List<SysFontColorBean> beanList);

    SysFontColorBean getSysFontColorBeanByKey(String key);

    SysVipBean getSysVipBeanByVip(int vip);

    // 根据VIP id查询vip名称
    SysVipGoodsBean getSysVipNameById(String id);


    //保存下载mv日志
    void saveMvDownLog(MvDownLog downLog);

    //查询单个mv日志
    MvDownLog getMvDownLogByMvId(String mvId,String userId);

    //查询用户mv下载列表日志
    List<MvDownLog>  getMvDownLogList(String userId);

    //删除mv下载日志
    void deleteMvDownLogById(String id);

}
