package cn.feihutv.zhibofeihu.ui.me.recharge;

import cn.feihutv.zhibofeihu.data.network.http.model.me.GetPayListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetPayListResponse;
import cn.feihutv.zhibofeihu.ui.me.recharge.PrepaidrecordsMvpView;
import cn.feihutv.zhibofeihu.ui.me.recharge.PrepaidrecordsMvpPresenter;
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
public class PrepaidrecordsPresenter<V extends PrepaidrecordsMvpView> extends BasePresenter<V>
        implements PrepaidrecordsMvpPresenter<V> {


    @Inject
    public PrepaidrecordsPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void getPayList() {
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .doGetPayListCall(new GetPayListRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetPayListResponse>() {
                    @Override
                    public void accept(@NonNull GetPayListResponse getPayListResponse) throws Exception {
                        if (getPayListResponse.getCode() == 0) {
                            if (getPayListResponse.getResponseDatas().size() > 0) {
                                getMvpView().showContent();
                                getMvpView().getDatas(getPayListResponse.getResponseDatas());
                            } else {
                                getMvpView().showNodataView();
                            }
                        } else {
                            AppLogger.i(getPayListResponse.getCode() + " " + getPayListResponse.getErrMsg());
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
