package cn.feihutv.zhibofeihu.utils;

import android.text.TextUtils;

import java.util.List;

import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.data.db.model.MessageEntity;
import cn.feihutv.zhibofeihu.data.db.model.NoticeSnsEntity;
import cn.feihutv.zhibofeihu.data.db.model.NoticeSysEntity;
import cn.feihutv.zhibofeihu.data.db.model.RecentItem;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadMsgListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadMsgListResponce;
import cn.feihutv.zhibofeihu.rxbus.RxBus;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by huanghao on 2017/10/25.
 */

public class DbMessageUtil {
    public static void ListenPushMessage() {
        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .doLoadMsgListRequestApiCall(new LoadMsgListRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoadMsgListResponce>() {
                    @Override
                    public void accept(@NonNull LoadMsgListResponce responce) throws Exception {
                        if (responce.getCode() == 0) {
                            List<LoadMsgListResponce.LoadMsgListData> listDatas = responce.getLoadMsgListData();
                            if (listDatas != null && listDatas.size() > 0) {
                                for (int i = 0; i < listDatas.size(); i++) {
                                    if (listDatas.get(i).getType().equals("sys")) {
                                        SharePreferenceUtil.saveSeesionBoolean(FeihuZhiboApplication.getApplication(), "Mine_notice", true);
                                        SharePreferenceUtil.saveSeesionBoolean(FeihuZhiboApplication.getApplication(), "haveNoticeSys", true);
                                        // 系统消息
                                        NoticeSysEntity sysEntity = new NoticeSysEntity();
                                        sysEntity.setUid(SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_USERID"));
                                        sysEntity.setTime(listDatas.get(i).getTime());
                                        sysEntity.setContent(listDatas.get(i).getContent());
                                        sysEntity.setAction(listDatas.get(i).getAction());
                                        sysEntity.setGift(listDatas.get(i).getGift());
                                        sysEntity.setCnt(listDatas.get(i).getCnt());
                                        sysEntity.setAmount(listDatas.get(i).getAmount());
                                        sysEntity.setExpireAt(listDatas.get(i).getExpireAt());
                                        sysEntity.setUserId(listDatas.get(i).getUserId());
                                        sysEntity.setNickName(listDatas.get(i).getNickName());
                                        sysEntity.setAccountId(listDatas.get(i).getAccountId());
                                        sysEntity.setTradeld(listDatas.get(i).getTradeId());
                                        sysEntity.setIsAccept(false);

                                        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                                                .saveNoticeSysEntity(sysEntity)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(new Consumer<Object>() {
                                                    @Override
                                                    public void accept(@NonNull Object obj) throws Exception {

                                                    }
                                                }, new Consumer<Throwable>() {
                                                    @Override
                                                    public void accept(@NonNull Throwable throwable) throws Exception {

                                                    }
                                                }));

                                        RxBus.get().send(RxBusCode.RX_BUS_CODE_NOTICE_SYS_MESSAGE);

                                    }
                                    if (listDatas.get(i).getType().equals("feed")) {
                                        SharePreferenceUtil.saveSeesionBoolean(FeihuZhiboApplication.getApplication(), "haveNoticeSns", true);
                                        SharePreferenceUtil.saveSeesionBoolean(FeihuZhiboApplication.getApplication(), "Mine_notice", true);
                                        // 社区动态消息
                                        NoticeSnsEntity snsEntity = new NoticeSnsEntity();
                                        snsEntity.setUid(SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_USERID"));
                                        snsEntity.setTime(listDatas.get(i).getTime());
                                        snsEntity.setContent(listDatas.get(i).getContent());
                                        snsEntity.setHeadUrl(listDatas.get(i).getHeadUrl());
                                        snsEntity.setLevel(listDatas.get(i).getLevel());
                                        snsEntity.setNickName(listDatas.get(i).getNickName());
                                        snsEntity.setUserId(listDatas.get(i).getUserId());
                                        snsEntity.setFeedId(listDatas.get(i).getFeedId());
                                        snsEntity.setImgUrl(listDatas.get(i).getFeedImg());
                                        snsEntity.setFeedContent(listDatas.get(i).getFeedContent());
                                        snsEntity.setFeedMsgType(listDatas.get(i).getFeedMsgType());
                                        snsEntity.setFeedType(listDatas.get(i).getFeedType());
                                        snsEntity.setReplyContent(listDatas.get(i).getReplyContent());
                                        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                                                .saveNoticeSnsEntity(snsEntity)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(new Consumer<Object>() {
                                                    @Override
                                                    public void accept(@NonNull Object obj) throws Exception {

                                                    }
                                                }, new Consumer<Throwable>() {
                                                    @Override
                                                    public void accept(@NonNull Throwable throwable) throws Exception {

                                                    }
                                                }));

                                        RxBus.get().send(RxBusCode.RX_BUS_CODE_NOTICE_SYS_MESSAGE);
                                    }
                                    if (listDatas.get(i).getType().equals("message")) {
                                        int news_count = SharePreferenceUtil.getSessionInt(FeihuZhiboApplication.getApplication(), "news_count");
                                        SharePreferenceUtil.saveSeesionInt(FeihuZhiboApplication.getApplication(), "news_count", news_count + 1);
                                        // 私信消息
                                        if (TextUtils.isEmpty(listDatas.get(i).getUserId())) {
                                            SharePreferenceUtil.saveSeesion(FeihuZhiboApplication.getApplication(), "FH_MSG" + SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_USERID"), listDatas.get(i).getContent());
                                            SharePreferenceUtil.saveSeesionBoolean(FeihuZhiboApplication.getApplication(), "fhkf_news", true);
                                        }
                                        MessageEntity messageEntity = new MessageEntity();
                                        messageEntity.setTime(listDatas.get(i).getTime());
                                        messageEntity.setContent(listDatas.get(i).getContent());
                                        messageEntity.setSenderId(listDatas.get(i).getUserId());
                                        messageEntity.setHeadUrl(listDatas.get(i).getHeadUrl());
                                        messageEntity.setLevel(listDatas.get(i).getLevel());
                                        messageEntity.setNickName(listDatas.get(i).getNickName());
                                        messageEntity.setIsComMeg(true);  // 收到消息
                                        messageEntity.setUserId(SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_USERID"));  // 当前登录用户的id
                                        messageEntity.setMsgStatus(1);
                                        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                                                .saveMessageEntity(messageEntity)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(new Consumer<Object>() {
                                                    @Override
                                                    public void accept(@NonNull Object obj) throws Exception {

                                                    }
                                                }, new Consumer<Throwable>() {
                                                    @Override
                                                    public void accept(@NonNull Throwable throwable) throws Exception {

                                                    }
                                                }));

                                        RecentItem recentItem = new RecentItem();
                                        recentItem.setUserId(listDatas.get(i).getUserId());
                                        recentItem.setHeadImg(listDatas.get(i).getHeadUrl());
                                        recentItem.setName(listDatas.get(i).getNickName());
                                        recentItem.setMessage(listDatas.get(i).getContent());
                                        recentItem.setTime((long) (listDatas.get(i).getTime()) * 1000);
                                        recentItem.setUid(SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_USERID"));
                                        recentItem.setIsRead(false);
                                        if (!recentItem.getUserId().equals("")) {
                                            new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                                                    .saveRecentItem(recentItem)
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(new Consumer<Object>() {
                                                        @Override
                                                        public void accept(@NonNull Object obj) throws Exception {

                                                        }
                                                    }, new Consumer<Throwable>() {
                                                        @Override
                                                        public void accept(@NonNull Throwable throwable) throws Exception {

                                                        }
                                                    }));
                                        }
                                        RxBus.get().send(RxBusCode.RX_BUS_CODE_NOTICE_SECRET_MESSAGE, listDatas.get(i));
                                    }
                                }
                            }
                        } else {
                            AppLogger.e(responce.getErrMsg());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                })
        );
    }
}
