package cn.feihutv.zhibofeihu.ui.me.recharge;

import android.view.Gravity;

import cn.feihutv.zhibofeihu.data.db.model.SysHBBean;
import cn.feihutv.zhibofeihu.data.network.http.model.me.PayRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.PayResponse;
import cn.feihutv.zhibofeihu.ui.base.BasePresenter;
import cn.feihutv.zhibofeihu.data.DataManager;

import java.util.List;

import javax.inject.Inject;

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
public class RechargePresenter<V extends RechargeMvpView> extends BasePresenter<V>
        implements RechargeMvpPresenter<V> {


    @Inject
    public RechargePresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void getSysHBBean() {

        List<SysHBBean> sysHBBean = getDataManager().getSysHBBean();
        getMvpView().getSysHb(sysHBBean);

    }

    @Override
    public void pay(final String pf, int hb) {
        getMvpView().showLoading();
         getCompositeDisposable().add(getDataManager()
                         .doPayCall(new PayRequest(pf, hb))
                         .subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribe(new Consumer<PayResponse>() {
                             @Override
                             public void accept(@NonNull PayResponse payResponse) throws Exception {
                                 if (payResponse.getCode() == 0) {
                                     getMvpView().paySucc(pf, payResponse.getData());
                                 } else {
                                     getMvpView().onToast("充值失败", Gravity.CENTER, 0, 0);
                                 }
                                 getMvpView().hideLoading();
                             }
                         }, getConsumer())

                 );

    }
}
