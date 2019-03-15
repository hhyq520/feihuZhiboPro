package cn.feihutv.zhibofeihu.ui.me.shop;

import android.view.Gravity;

import cn.feihutv.zhibofeihu.data.network.http.model.me.BuyShopAccountIdRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.BuyShopAccountIdResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LoadLiangSearchKeyRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LoadLiangSearchKeyResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LoadShopAccountIdListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LoadShopAccountIdListResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseResponse;
import cn.feihutv.zhibofeihu.ui.me.shop.BeautiMvpView;
import cn.feihutv.zhibofeihu.ui.me.shop.BeautiMvpPresenter;
import cn.feihutv.zhibofeihu.ui.base.BasePresenter;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.DataManager;

import com.androidnetworking.error.ANError;

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
public class BeautiPresenter<V extends BeautiMvpView> extends BasePresenter<V>
        implements BeautiMvpPresenter<V> {


    @Inject
    public BeautiPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void loadShopAccountIdList(int liangId) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .doLoadShopAccountIdListCall(new LoadShopAccountIdListRequest(liangId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoadShopAccountIdListResponse>() {
                    @Override
                    public void accept(@NonNull LoadShopAccountIdListResponse loadShopAccountIdListResponse) throws Exception {
                        getMvpView().hideLoading();
                        if (loadShopAccountIdListResponse.getCode() == 0) {
                            getMvpView().getDatas(loadShopAccountIdListResponse.getAccountIdListResponseDatas());
                        } else {
                            getMvpView().onToast("加载失败，请重新获取");
                            AppLogger.i(loadShopAccountIdListResponse.getCode() + "  " + loadShopAccountIdListResponse.getErrMsg());
                        }
                    }
                }, getConsumer())

        );

    }

    @Override
    public void buyShopAccountId(final String id) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .doBuyShopAccountIdCall(new BuyShopAccountIdRequest(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BuyShopAccountIdResponse>() {
                    @Override
                    public void accept(@NonNull BuyShopAccountIdResponse buyShopAccountIdResponse) throws Exception {
                        getMvpView().hideLoading();
                        if (buyShopAccountIdResponse.getCode() == 0) {
                            getMvpView().buySucc(id);
                        } else {
                            AppLogger.i(buyShopAccountIdResponse.getCode() + " " + buyShopAccountIdResponse.getErrMsg());
                            if (buyShopAccountIdResponse.getCode() == 4202) {
                                getMvpView().showNotEnough();
                            } else if (buyShopAccountIdResponse.getCode() == 4205) {
                                getMvpView().onToast("购买失败, 帐号不存在", Gravity.CENTER, 0, 0);
                            } else {
                                getMvpView().onToast("购买失败", Gravity.CENTER, 0, 0);
                            }
                        }
                    }
                }, getConsumer())

        );

    }

    @Override
    public void saveaccountId(String accountId) {
        LoadUserDataBaseResponse.UserData userData = getDataManager().getUserData();
        userData.setAccountId(accountId);
        getDataManager().saveUserData(userData);
    }

    @Override
    public void loadLiangSearchKey() {
        getCompositeDisposable().add(getDataManager()
                .doLoadLiangSearchKeyCall(new LoadLiangSearchKeyRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoadLiangSearchKeyResponse>() {
                    @Override
                    public void accept(@NonNull LoadLiangSearchKeyResponse loadLiangSearchKeyResponse) throws Exception {
                        if (loadLiangSearchKeyResponse.getCode() == 0) {
                            if (loadLiangSearchKeyResponse.getLoadLiangSearchKeyResponseDatas().size() > 0) {
                                getMvpView().succLiangSearch(loadLiangSearchKeyResponse.getLoadLiangSearchKeyResponseDatas());
                            }
                        } else {
                            getMvpView().onToast("获取失败~", Gravity.CENTER, 0, 0);
                        }
                    }
                }, getConsumer())

        );

    }
}
