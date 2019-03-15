package cn.feihutv.zhibofeihu.ui.mv;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cn.feihutv.zhibofeihu.data.DataManager;
import cn.feihutv.zhibofeihu.data.db.model.SysGiftNewBean;
import cn.feihutv.zhibofeihu.data.local.model.SysbagBean;
import cn.feihutv.zhibofeihu.data.network.http.model.common.BuyGoodsRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.BuyGoodsResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetAllMvListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetAllMvListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GiftMVRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GiftMVResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.ShareMvRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.ShareMvResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.user.BagRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.user.BagResponse;
import cn.feihutv.zhibofeihu.ui.base.BasePresenter;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * <pre>
 *     author : sichu.chen
 *     time   :
 *     desc   : p业务处理实现类
 *     version: 1.0
 * </pre>
 */
public class MvVideoListPresenter<V extends MvVideoListMvpView> extends BasePresenter<V>
        implements MvVideoListMvpPresenter<V> {

    String giftMvId = "";

    @Inject
    public MvVideoListPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void getAllMVList(String offset) {

        getCompositeDisposable().add(getDataManager()
                .doGetAllMvListCall(new GetAllMvListRequest(offset))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetAllMvListResponse>() {
                    @Override
                    public void accept(@NonNull GetAllMvListResponse getAllMvListResponse) throws Exception {
                        getMvpView().onGetAllMVListResp(getAllMvListResponse);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        getMvpView().onToast("网络异常，请稍后重试...");
                        getMvpView().onGetAllMVListResp(null);
                    }
                })

        );

    }

    @Override
    public void shareMv(String mvId, int to) {
        getCompositeDisposable().add(getDataManager()
                .doShareMvCall(new ShareMvRequest(mvId, to))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ShareMvResponse>() {
                    @Override
                    public void accept(@NonNull ShareMvResponse shareMvResponse) throws Exception {

                    }
                }, getConsumer()));
    }

    @Override
    public void requestBagData(String mvId) {
        giftMvId = mvId;
        getCompositeDisposable().
                add(getDataManager()
                        .doGetUserBagDataApiCall(new BagRequest()).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<BagResponse>() {
                            @Override
                            public void accept(@NonNull BagResponse bagResponse) throws Exception {
                                List<SysbagBean> sysbagBeanList = new ArrayList<SysbagBean>();
                                if (bagResponse.getCode() == 0) {
                                    if (bagResponse.getBagResponseData().size() > 0) {
                                        for (BagResponse.BagResponseData info : bagResponse.getBagResponseData()) {
                                            if (info.getCnt() > 0) {
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
        if (isExistbag(sysbagBeanList, id)) {
            for (final SysbagBean item : sysbagBeanList) {
                if (item.getId() == id) {
                    if (item.getCnt() < count) {
                        final int needCount = count - item.getCnt();
                        final String gooId = getDataManager().getGoodIDByGiftID(String.valueOf(id));
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
                    } else {
                        sendGift(sysbagBeanList, id, count);
                    }
                    break;
                }
            }
        }
    }

    private boolean isExistbag(List<SysbagBean> sysbagBeanList, int id) {
        boolean isExist = false;
        for (SysbagBean item : sysbagBeanList) {
            if (item.getId() == id) {
                isExist = true;
                break;
            }
        }
        return isExist;
    }

    @Override
    public void dealSendGift(final List<SysbagBean> sysbagBeanList, final int id, final int count) {
        SysGiftNewBean gift = getDataManager().getGiftBeanByID(String.valueOf(id));
        if (gift == null) {
            return;
        }
        String gooId = getDataManager().getGoodIDByGiftID(String.valueOf(id));
        if (isExistbag(sysbagBeanList, id)) {
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
                    } else {
                        sendGift(sysbagBeanList, id, count);
                    }
                    break;
                }
            }
        } else {
            if (gift != null) {
                buyGoods(sysbagBeanList, Integer.valueOf(gooId), id, count);
            }
        }
    }

    public void buyGoods(final List<SysbagBean> sysbagBeanList, int goodId, final int giftid, final int count) {
        getCompositeDisposable().add(getDataManager()
                .doBuyGoodsApiCall(new BuyGoodsRequest(goodId, count))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BuyGoodsResponce>() {
                    @Override
                    public void accept(@NonNull BuyGoodsResponce responce) throws Exception {
                        if (responce.getCode() == 0) {
                            sendGift(sysbagBeanList, giftid, count);
                        } else {
                            if (responce.getCode() == 4202) {
                                getMvpView().onToast("余额不足,请充值");
                            } else if (responce.getCode() == 4203) {
                                getMvpView().onToast("礼物已下架");
                            }
                            AppLogger.e(responce.getErrMsg());
                        }
                    }
                }, getConsumer()));

    }

    @Override
    public void giftMV(String mvId, String giftId, String giftCnt) {
        getCompositeDisposable().add(getDataManager()
                .doGiftMVCall(new GiftMVRequest(mvId, giftId, giftCnt))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GiftMVResponse>() {
                    @Override
                    public void accept(GiftMVResponse response) throws Exception {
                        getMvpView().onGiftMVResp(response);
                    }
                }, getConsumer()));
    }


    public void sendGift(final List<SysbagBean> sysbagBeanList, int id, int count) {

        giftMV(giftMvId, id + "", count + "");

//        getCompositeDisposable().add(getDataManager()
//                .doSendGiftApiCall(new SendGiftRequest(id, count))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<SendGiftResponce>() {
//                    @Override
//                    public void accept(@NonNull SendGiftResponce responce) throws Exception {
//                        if(responce.getCode()==0){
////                            getMvpView().sendGiftSuccess(sysbagBeanList,id,count);
//                        }else{
//                            if (responce.getCode() == 4023) {
//                                getMvpView().onToast("你已掉线，请重新登录！");
//                            } else {
//                                getMvpView().onToast("赠送失败");
//                            }
//                            AppLogger.e(responce.getErrMsg());
//                        }
//                    }
//                },getConsumer()));


    }
}
