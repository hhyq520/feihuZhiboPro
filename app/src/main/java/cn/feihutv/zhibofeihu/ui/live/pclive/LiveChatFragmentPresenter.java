package cn.feihutv.zhibofeihu.ui.live.pclive;

import android.text.TextUtils;

import cn.feihutv.zhibofeihu.data.db.model.SysConfigBean;
import cn.feihutv.zhibofeihu.data.db.model.SysGiftNewBean;
import cn.feihutv.zhibofeihu.data.db.model.SysMountNewBean;
import cn.feihutv.zhibofeihu.data.local.model.SysbagBean;
import cn.feihutv.zhibofeihu.data.local.model.TCChatEntity;
import cn.feihutv.zhibofeihu.data.network.http.model.common.BuyGoodsRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.BuyGoodsResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.SendGiftRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.SendGiftResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.GetCurrMountRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.GetCurrMountResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.SendLoudSpeakRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.SendLoudSpeakResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.user.BagRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.user.BagResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.common.SendRoomMsgRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.common.SendRoomResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.LeaveRoomRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.LeaveRoomResponce;
import cn.feihutv.zhibofeihu.ui.base.BasePresenter;
import cn.feihutv.zhibofeihu.data.DataManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cn.feihutv.zhibofeihu.utils.AppLogger;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * <pre>
 *     author : huang hao
 *     time   :
 *     desc   : p业务处理实现类
 *     version: 1.0
 * </pre>
 */
public class LiveChatFragmentPresenter<V extends LiveChatFragmentMvpView> extends BasePresenter<V>
        implements LiveChatFragmentMvpPresenter<V> {


    @Inject
    public LiveChatFragmentPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void loadRoomById(String userId) {
        getCompositeDisposable().
                add(getDataManager().
                        doGetRoomDataApiCall(new LoadRoomRequest(userId))
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<LoadRoomResponce>() {
                            @Override
                            public void accept(@NonNull final LoadRoomResponce loadRoomResponce) throws Exception {
                                if (loadRoomResponce.getCode() == 0) {
                                    getDataManager()
                                            .doLoadLeaveRoomApiCall(new LeaveRoomRequest())
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Consumer<LeaveRoomResponce>() {
                                                @Override
                                                public void accept(@NonNull LeaveRoomResponce response) throws Exception {
                                                    if(response.getCode()==0){
                                                        getMvpView().gotoRoom(loadRoomResponce.getLoadRoomData());
                                                    }else{
                                                        AppLogger.e("leaveRoom"+response.getErrMsg());
                                                    }
                                                }
                                            }, getConsumer());
                                } else {
                                    AppLogger.e(loadRoomResponce.getErrMsg());
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {

                            }
                        }));
    }

    @Override
    public void sendLoudspeaker(String msg) {
        getCompositeDisposable().add(getDataManager()
                .doSendLoudSpeakApiCall(new SendLoudSpeakRequest(msg))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SendLoudSpeakResponce>() {
                    @Override
                    public void accept(@NonNull SendLoudSpeakResponce sendLoudSpeakResponce) throws Exception {
                        if(sendLoudSpeakResponce.getCode()==0){
                            getMvpView().onToast("发送全站消息成功");
                        }else{
                            if (sendLoudSpeakResponce.getCode() == 4602) {
                                getMvpView().onToast("您已被禁言");
                            } else if (sendLoudSpeakResponce.getCode() == 4009) {
                                getMvpView().onToast("内容太长");
                            } else if (sendLoudSpeakResponce.getCode() == 4202) {
                                getMvpView().onToast("余额不足");
                            } else if (sendLoudSpeakResponce.getCode() == 4025) {
                                getMvpView().onToast("您已被禁言");
                            }
                            AppLogger.e(sendLoudSpeakResponce.getErrMsg());
                        }
                    }
                },getConsumer()));
    }

    @Override
    public void sendRoomMsg(final TCChatEntity tcChatEntity, String msg) {
        getDataManager()
                .doSendRoomMsgApiCall(new SendRoomMsgRequest(msg))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SendRoomResponce>() {
                    @Override
                    public void accept(@NonNull SendRoomResponce response) throws Exception {
                        if(response.getCode()==0){
                            getMvpView().notifyChatMsg(tcChatEntity);
                        }else{
                            if (response.getCode()== 4025) {
                                getMvpView().onToast("您已被禁言");
                            } else if (response.getCode()== 4009) {
                                getMvpView().onToast("内容太长");
                            } else if (response.getCode() == 4602) {
                                getMvpView().onToast("您已被禁言");
                            }
                            AppLogger.e("joinRoom"+response.getErrMsg());
                        }
                    }
                }, getConsumer());
    }

    @Override
    public void requestBagData() {
        getCompositeDisposable().
                add(getDataManager().
                        doGetUserBagDataApiCall(new BagRequest()).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<BagResponse>() {
                            @Override
                            public void accept(@NonNull BagResponse bagResponse) throws Exception {
                                List<SysbagBean> sysbagBeanList=new ArrayList<SysbagBean>();
                                if (bagResponse.getCode() == 0) {
                                    if (bagResponse.getBagResponseData().size() > 0) {
                                        for (BagResponse.BagResponseData info : bagResponse.getBagResponseData()) {
                                            if(info.getCnt()>0) {
                                                SysbagBean bag = new SysbagBean();
                                                bag.setCnt(info.getCnt());
                                                bag.setId(info.getId());
                                                sysbagBeanList.add(bag);
                                            }
                                        }
                                    }
                                } else {
                                    AppLogger.i(bagResponse.getErrMsg());
                                }
                                getMvpView().showGiftDialog(sysbagBeanList);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                            }
                        }));
    }

    @Override
    public void dealBagSendGift(final List<SysbagBean> sysbagBeanList, final int id, final int count) {
        SysGiftNewBean gift = getDataManager().getGiftBeanByID(String.valueOf(id));
        if (gift == null) {
            return;
        }
        if (isExistbag(sysbagBeanList,id)) {
            for (final SysbagBean item : sysbagBeanList) {
                if (item.getId() == id) {
                    if (item.getCnt() < count) {
                        final int needCount = count - item.getCnt();
                        final String gooId=getDataManager().getGoodIDByGiftID(String.valueOf(id));
                        if(!TextUtils.isEmpty(gooId)) {
                            getCompositeDisposable().add(getDataManager()
                                    .doBuyGoodsApiCall(new BuyGoodsRequest(Integer.valueOf(gooId), needCount))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<BuyGoodsResponce>() {
                                        @Override
                                        public void accept(@NonNull BuyGoodsResponce responce) throws Exception {
                                            if (responce.getCode() == 0) {
                                                sendGift(sysbagBeanList, id, count);
                                            } else {
                                                sendGift(sysbagBeanList, id, item.getCnt());
                                                if (responce.getCode() == 4202) {
                                                    getMvpView().onToast("余额不足,请充值");
                                                } else if (responce.getCode() == 4203) {
                                                    getMvpView().onToast("礼物已下架");
                                                }
                                            }
                                        }
                                    }, getConsumer()));
                        }else{
                            getMvpView().onToast("礼物数量不足");
                        }
                    } else {
                        sendGift(sysbagBeanList,id, count);
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void dealSendGift(final List<SysbagBean> sysbagBeanList, final int id, final int count) {
        SysGiftNewBean gift = getDataManager().getGiftBeanByID(String.valueOf(id));
        if (gift == null) {
            return;
        }
        String gooId=getDataManager().getGoodIDByGiftID(String.valueOf(id));
        if (isExistbag(sysbagBeanList,id)) {
            for (final SysbagBean item : sysbagBeanList) {
                if (item.getId() == id) {
                    if (item.getCnt() < count) {
                        int needCount = count - item.getCnt();
                        getCompositeDisposable().add(getDataManager()
                                .doBuyGoodsApiCall(new BuyGoodsRequest(Integer.valueOf(gooId), needCount))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<BuyGoodsResponce>() {
                                    @Override
                                    public void accept(@NonNull BuyGoodsResponce responce) throws Exception {
                                        if(responce.getCode()==0){
                                            sendGift(sysbagBeanList,id, count);
                                        }else{
                                            sendGift(sysbagBeanList,id, item.getCnt());
                                            if (responce.getCode() == 4202) {
                                                getMvpView().onToast("余额不足,请充值");
                                            }else if (responce.getCode()== 4203) {
                                                getMvpView().onToast("礼物已下架");
                                            }
                                        }
                                    }
                                },getConsumer()));
                    } else {
                        sendGift(sysbagBeanList,id, count);
                    }
                    break;
                }
            }
        } else {
            if (gift != null) {
                buyGoods(sysbagBeanList,Integer.valueOf(gooId), id, count);
            }
        }
    }

    @Override
    public List<SysGiftNewBean> getSysGiftNewBean() {
        return getDataManager().getSysGiftNew();
    }

    @Override
    public SysGiftNewBean getGiftBeanByID(String id) {
        return getDataManager().getGiftBeanByID(id);
    }

    @Override
    public void getCurrMount(String roomId) {
        getCompositeDisposable().add(getDataManager()
                .doGetCurrMountApiCall(new GetCurrMountRequest(roomId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetCurrMountResponce>() {
                    @Override
                    public void accept(@NonNull GetCurrMountResponce getCurrMountResponce) throws Exception {
                        if(getCurrMountResponce.getCode()==0){
                            getMvpView().notifyGetCurrMount(getCurrMountResponce.getCurrMountData());
                        }else{
                            AppLogger.e(getCurrMountResponce.getErrMsg());
                        }
                    }
                },getConsumer()));
    }

    @Override
    public SysMountNewBean getMountBeanByID(String id) {
        return getDataManager().getMountBeanByID(id);
    }

    @Override
    public SysConfigBean getSysConfigBean() {
        return getDataManager().getSysConfig();
    }

    public void buyGoods(final List<SysbagBean> sysbagBeanList,int goodId, final int giftid, final int count) {
        getCompositeDisposable().add(getDataManager()
                .doBuyGoodsApiCall(new BuyGoodsRequest(goodId, count))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BuyGoodsResponce>() {
                    @Override
                    public void accept(@NonNull BuyGoodsResponce responce) throws Exception {
                        if(responce.getCode()==0){
                            sendGift(sysbagBeanList,giftid, count);
                        }else{
                            if (responce.getCode() == 4202) {
                                getMvpView().onToast("余额不足,请充值");
                            }else if (responce.getCode()== 4203) {
                                getMvpView().onToast("礼物已下架");
                            }
                            AppLogger.e(responce.getErrMsg());
                        }
                    }
                },getConsumer()));

    }

    public void sendGift(final List<SysbagBean> sysbagBeanList, final int id, final int count) {
        getCompositeDisposable().add(getDataManager()
                .doSendGiftApiCall(new SendGiftRequest(id, count))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SendGiftResponce>() {
                    @Override
                    public void accept(@NonNull SendGiftResponce responce) throws Exception {
                        if(responce.getCode()==0){
                            getMvpView().sendGiftSuccess(sysbagBeanList,id,count);
                        }else{
                            if (responce.getCode() == 4023) {
                                getMvpView().onToast("你已掉线，请重新登录！");
                            } else {
                                getMvpView().onToast("赠送失败");
                            }
                            AppLogger.e(responce.getErrMsg());
                        }
                    }
                },getConsumer()));
    }

    private boolean isExistbag(List<SysbagBean> sysbagBeanList,int id) {
        boolean isExist = false;
        for (SysbagBean item : sysbagBeanList) {
            if (item.getId() == id) {
                isExist = true;
                break;
            }
        }
        return isExist;
    }
}
