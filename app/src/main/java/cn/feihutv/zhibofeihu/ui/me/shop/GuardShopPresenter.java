package cn.feihutv.zhibofeihu.ui.me.shop;

import android.view.Gravity;

import java.util.List;

import cn.feihutv.zhibofeihu.data.db.model.SysGuardGoodsBean;
import cn.feihutv.zhibofeihu.data.network.http.model.me.BuyGuardRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.BuyGuardResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.CheckGuardInfoRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.CheckGuardInfoResponse;
import cn.feihutv.zhibofeihu.ui.base.BasePresenter;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.DataManager;


import javax.inject.Inject;

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
public class GuardShopPresenter<V extends GuardShopMvpView> extends BasePresenter<V>
        implements GuardShopMvpPresenter<V> {


    @Inject
    public GuardShopPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void checkGuardInfo(String accountId) {
        if (accountId.equals(getUserData().getAccountId())) {
            getMvpView().onToast("不能输入自己的ID", Gravity.CENTER, 0, 0);
            return;
        }
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .doCheckGuardInfoCall(new CheckGuardInfoRequest(accountId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CheckGuardInfoResponse>() {
                    @Override
                    public void accept(@NonNull CheckGuardInfoResponse checkGuardInfoResponse) throws Exception {
                        if (checkGuardInfoResponse.getCode() == 0) {
                            getMvpView().checkGuardInfoSucc(checkGuardInfoResponse.getCheckGuardInfoResponseData());
                        } else {
                            if (checkGuardInfoResponse.getCode() == 4041) {
                                getMvpView().onToast("您搜索的主播不存在，请确认后重新输入", Gravity.CENTER, 0, 0);
                            } else if (checkGuardInfoResponse.getCode() == 4017) {
                                getMvpView().onToast("TA还不是主播哦~", Gravity.CENTER, 0, 0);
                            } else {
                                getMvpView().onToast("查询失败！", Gravity.CENTER, 0, 0);
                            }
                        }
                        getMvpView().hideLoading();
                    }
                }, getConsumer())

        );

    }

    @Override
    public void buyGuard(String uid, int goodsId, final int huType) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .doBuyGuardCall(new BuyGuardRequest(uid, goodsId, huType))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BuyGuardResponse>() {
                    @Override
                    public void accept(@NonNull BuyGuardResponse buyGuardResponse) throws Exception {
                        getMvpView().hideLoading();
                        if (buyGuardResponse.getCode() == 0) {
                            getMvpView().showBuySuccDialog(huType);
                        } else {
                            if (buyGuardResponse.getCode() == 4202) {
                                // 虎币不足
                                getMvpView().showHbNotEnoughDialog();
                            } else if (buyGuardResponse.getCode() == 4041) {
                                getMvpView().onToast("用户不存在", Gravity.CENTER, 0, 0);
                            }
                        }
                    }
                }, getConsumer())

        );

    }

    @Override
    public void getSysGuardGoodsBean() {
        getCompositeDisposable().add(getDataManager()
                .getSysGuardGoodsBean()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<SysGuardGoodsBean>>() {
                    @Override
                    public void accept(@NonNull List<SysGuardGoodsBean> sysGuardGoodsBeen) throws Exception {
                        getMvpView().getSysGuardGoodsSucc(sysGuardGoodsBeen);
                    }
                }, getConsumer())

        );

    }
}
