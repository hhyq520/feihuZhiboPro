package cn.feihutv.zhibofeihu.ui.me.vip;

import cn.feihutv.zhibofeihu.data.db.model.SysVipBean;
import cn.feihutv.zhibofeihu.data.db.model.SysVipGoodsBean;
import cn.feihutv.zhibofeihu.data.network.http.model.vip.BuyVipRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.vip.BuyVipResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseResponse;
import cn.feihutv.zhibofeihu.ui.base.BasePresenter;
import cn.feihutv.zhibofeihu.data.DataManager;


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
 *     author : liwen.chen
 *     time   :
 *     desc   : p业务处理实现类
 *     version: 1.0
 * </pre>
 */
public class MyVipPresenter<V extends MyVipMvpView> extends BasePresenter<V>
        implements MyVipMvpPresenter<V> {


    @Inject
    public MyVipPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void getSysVipGoodsBean() {
        getCompositeDisposable().add(getDataManager()
                .getSysVipGoodsBean()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<SysVipGoodsBean>>() {
                    @Override
                    public void accept(@NonNull List<SysVipGoodsBean> sysVipGoodsBeen) throws Exception {
                        if (sysVipGoodsBeen.size() > 0) {
                            getMvpView().getSysVipGoodsSucc(sysVipGoodsBeen);
                        }
                    }
                }, getConsumer())

        );

    }

    @Override
    public void getSysVipBean() {
        getCompositeDisposable().add(getDataManager()
                .getSysVipBean()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<SysVipBean>>() {
                    @Override
                    public void accept(@NonNull List<SysVipBean> sysVipBeen) throws Exception {
                        if (sysVipBeen.size() > 0) {
                            getMvpView().getSysVipSucc(sysVipBeen);
                        }
                    }
                }, getConsumer())

        );
    }

    @Override
    public void buyVip(final int goodsId) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .doBuyVipCall(new BuyVipRequest(goodsId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BuyVipResponse>() {
                    @Override
                    public void accept(@NonNull BuyVipResponse buyVipResponse) throws Exception {
                        if (buyVipResponse.getCode() == 0) {
                            getMvpView().buyVipSucc(buyVipResponse.getBuyVipData(), goodsId);
                        } else {
                            if (buyVipResponse.getCode() == 4202) {
                                // 虎币不足
                                getMvpView().failToBuy();
                            } else if (buyVipResponse.getCode() == 4000) {
                                getMvpView().onToast("非首次购买，请选择其它类型");
                            }
                            AppLogger.i(buyVipResponse.getCode() + " " + buyVipResponse.getErrMsg());
                        }
                        getMvpView().hideLoading();
                    }
                }, getConsumer())

        );

    }

    @Override
    public void saveUserDatas(LoadUserDataBaseResponse.UserData userData) {
        if (userData != null) {
            getDataManager().saveUserData(userData);
        }
    }
}
