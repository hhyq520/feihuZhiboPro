package cn.feihutv.zhibofeihu.ui.me.guard;

import cn.feihutv.zhibofeihu.data.network.http.model.me.GetUserGuardListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetUserGuardListResponse;
import cn.feihutv.zhibofeihu.ui.me.guard.GuardListMvpView;
import cn.feihutv.zhibofeihu.ui.me.guard.GuardListMvpPresenter;
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
public class GuardListPresenter<V extends GuardListMvpView> extends BasePresenter<V>
        implements GuardListMvpPresenter<V> {


    @Inject
    public GuardListPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void getUserGuardList(String usrdId, int offset) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .doGetUserGuardListCall(new GetUserGuardListRequest(usrdId, String.valueOf(offset), String.valueOf(20)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetUserGuardListResponse>() {
                    @Override
                    public void accept(@NonNull GetUserGuardListResponse getUserGuardListResponse) throws Exception {
                        if (getUserGuardListResponse.getCode() == 0) {
                            if (getUserGuardListResponse.getGetUserGuardListResponseDatas().size() > 0) {
                                getMvpView().showContent();
                                getMvpView().getDatas(getUserGuardListResponse.getGetUserGuardListResponseDatas());
                            } else {
                                getMvpView().showNoData();
                            }
                        } else {
                            AppLogger.i(getUserGuardListResponse.getCode() + " " + getUserGuardListResponse.getErrMsg());
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
