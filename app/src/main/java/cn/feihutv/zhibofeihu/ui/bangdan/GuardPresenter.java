package cn.feihutv.zhibofeihu.ui.bangdan;

import cn.feihutv.zhibofeihu.data.network.http.model.bangdan.GetGuardRankRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.bangdan.GetGuardRankResponse;
import cn.feihutv.zhibofeihu.ui.bangdan.GuardMvpView;
import cn.feihutv.zhibofeihu.ui.bangdan.GuardMvpPresenter;
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
public class GuardPresenter<V extends GuardMvpView> extends BasePresenter<V>
        implements GuardMvpPresenter<V> {


    @Inject
    public GuardPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void getGuardRank() {
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .doGetGuardRankApiCall(new GetGuardRankRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetGuardRankResponse>() {
                    @Override
                    public void accept(@NonNull GetGuardRankResponse getGuardRankResponse) throws Exception {
                        if (getGuardRankResponse.getCode() == 0) {
                            getMvpView().getDatas(getGuardRankResponse.getResponseDatas());
                        } else {
                            AppLogger.i(getGuardRankResponse.getCode() + " " + getGuardRankResponse.getErrMsg());
                        }
                        getMvpView().hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        getMvpView().hideLoading();
                        getMvpView().showErrorView();
                    }
                })

        );

    }
}
