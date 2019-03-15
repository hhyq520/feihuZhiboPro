package cn.feihutv.zhibofeihu.ui.me.encash;

import cn.feihutv.zhibofeihu.data.network.http.model.me.GetEncashListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetEncashListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetPayListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetPayListResponse;
import cn.feihutv.zhibofeihu.ui.me.encash.WithdrawalrecordMvpView;
import cn.feihutv.zhibofeihu.ui.me.encash.WithdrawalrecordMvpPresenter;
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
public class WithdrawalrecordPresenter<V extends WithdrawalrecordMvpView> extends BasePresenter<V>
        implements WithdrawalrecordMvpPresenter<V> {


    @Inject
    public WithdrawalrecordPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void getEncashList() {
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .doGetEncashListCall(new GetEncashListRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetEncashListResponse>() {
                    @Override
                    public void accept(@NonNull GetEncashListResponse getEncashListResponse) throws Exception {
                        if (getEncashListResponse.getCode() == 0) {
                            if (getEncashListResponse.getGetEncashListResponses().size() > 0) {
                                getMvpView().showContent();
                                getMvpView().getDatas(getEncashListResponse.getGetEncashListResponses());
                            } else {
                                getMvpView().showNodataView();
                            }
                        } else {
                            AppLogger.i(getEncashListResponse.getCode() + " " + getEncashListResponse.getErrMsg());
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
