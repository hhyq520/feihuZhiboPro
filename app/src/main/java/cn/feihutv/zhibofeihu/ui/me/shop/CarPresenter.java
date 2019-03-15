package cn.feihutv.zhibofeihu.ui.me.shop;

import android.util.Log;
import android.view.Gravity;

import cn.feihutv.zhibofeihu.data.db.model.SysGoodsNewBean;
import cn.feihutv.zhibofeihu.data.network.http.model.common.BuyGoodsRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.BuyGoodsResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.me.SetMountRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.SetMountResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseResponse;
import cn.feihutv.zhibofeihu.ui.me.shop.CarMvpView;
import cn.feihutv.zhibofeihu.ui.me.shop.CarMvpPresenter;
import cn.feihutv.zhibofeihu.ui.base.BasePresenter;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.DataManager;

import com.androidnetworking.error.ANError;

import java.util.List;

import javax.inject.Inject;

import cn.feihutv.zhibofeihu.utils.AppLogger;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * <pre>
 *     author : liwen.chen
 *     time   :
 *     desc   : p业务处理实现类
 *     version: 1.0
 * </pre>
 */
public class CarPresenter<V extends CarMvpView> extends BasePresenter<V>
        implements CarMvpPresenter<V> {


    @Inject
    public CarPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void buyGoods(final int id, int cnt) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .doBuyGoodsApiCall(new BuyGoodsRequest(id, cnt))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BuyGoodsResponce>() {
                    @Override
                    public void accept(@NonNull BuyGoodsResponce buyGoodsResponce) throws Exception {
                        if (buyGoodsResponce.getCode() == 0) {
                            // 购买成功
                            getCompositeDisposable().add(getDataManager()
                                    .doSetMountCall(new SetMountRequest(id))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<SetMountResponse>() {
                                        @Override
                                        public void accept(@NonNull SetMountResponse setMountResponse) throws Exception {
                                            if (setMountResponse.getCode() == 0) {
                                                getMvpView().setMountSucc();
                                            }
                                        }
                                    }, getConsumer())

                            );
                            getMvpView().buyGoodsSucc();
                        } else {
                            // 购买失败
                            if (buyGoodsResponce.getCode() == 4202) {
                                getMvpView().hbNotEnough();
                            } else if (buyGoodsResponce.getCode() == 4203) {
                                getMvpView().onToast("礼物已下架", Gravity.CENTER, 0, 0);
                            } else if (buyGoodsResponce.getCode() == 4206) {
                                getMvpView().showNotVip();
                            } else {
                                getMvpView().onToast("购买失败", Gravity.CENTER, 0, 0);
                            }
                            AppLogger.i(buyGoodsResponce.getCode() + " " + buyGoodsResponce.getErrMsg());
                        }
                        getMvpView().hideLoading();
                    }
                }, getConsumer())

        );

    }

    @Override
    public void getStoreZuojia() {
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .getStoreZuojia()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<SysGoodsNewBean>>() {
                    @Override
                    public void accept(@NonNull List<SysGoodsNewBean> sysGoodsNewBeen) throws Exception {
                        if (sysGoodsNewBeen.size() > 0) {
                            getMvpView().getZjSucc(sysGoodsNewBeen);
                        } else {
                            getMvpView().onToast("获取失败");
                        }

                        getMvpView().hideLoading();
                    }
                }, getConsumer())

        );

    }


    @Override
    public void saveUserDatas(LoadUserDataBaseResponse.UserData userData) {
        getDataManager().saveUserData(userData);
    }
}
