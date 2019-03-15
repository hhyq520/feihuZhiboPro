package cn.feihutv.zhibofeihu.ui.me.encash;

import cn.feihutv.zhibofeihu.data.network.http.model.me.GetEncashInfoRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetEncashInfoResponse;
import cn.feihutv.zhibofeihu.ui.me.encash.MyEarningsMvpView;
import cn.feihutv.zhibofeihu.ui.me.encash.MyEarningsMvpPresenter;
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
public class MyEarningsPresenter<V extends MyEarningsMvpView> extends BasePresenter<V>
        implements MyEarningsMvpPresenter<V> {


    @Inject
    public MyEarningsPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void getEncashInfo() {
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .doGetEncashInfoCall(new GetEncashInfoRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetEncashInfoResponse>() {
                    @Override
                    public void accept(@NonNull GetEncashInfoResponse getEncashInfoResponse) throws Exception {
                        if (getEncashInfoResponse.getCode() == 0) {
                            getMvpView().getEncashInfo(getEncashInfoResponse.getEncashInfoResponseData());
                        } else {
                            AppLogger.i(getEncashInfoResponse.getCode() + "  " + getEncashInfoResponse.getErrMsg());
                        }
                        getMvpView().hideLoading();
                    }
                }, getConsumer())

        );

    }
}
