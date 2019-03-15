package cn.feihutv.zhibofeihu.ui.me;

import cn.feihutv.zhibofeihu.data.network.http.model.me.GetUserGuardsRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetUserGuardsResponse;
import cn.feihutv.zhibofeihu.ui.me.HisGuardMvpView;
import cn.feihutv.zhibofeihu.ui.me.HisGuardMvpPresenter;
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
public class HisGuardPresenter<V extends HisGuardMvpView> extends BasePresenter<V>
        implements HisGuardMvpPresenter<V> {


    @Inject
    public HisGuardPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void getUserGuards(String userId, int offset) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .doGetUserGuardsCall(new GetUserGuardsRequest(userId, String.valueOf(offset), String.valueOf(20)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetUserGuardsResponse>() {
                    @Override
                    public void accept(@NonNull GetUserGuardsResponse getUserGuardsResponse) throws Exception {
                        if (getUserGuardsResponse.getCode() == 0) {
                            if (getUserGuardsResponse.getGuardsResponseDatas().size() > 0) {
                                getMvpView().showContent();
                                getMvpView().getDatas(getUserGuardsResponse.getGuardsResponseDatas());
                            } else {
                                getMvpView().showNoData();
                            }
                        } else {
                            AppLogger.i(getUserGuardsResponse.getCode() + " " + getUserGuardsResponse.getErrMsg());
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
