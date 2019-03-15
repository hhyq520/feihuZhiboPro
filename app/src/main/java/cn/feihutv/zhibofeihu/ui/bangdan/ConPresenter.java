package cn.feihutv.zhibofeihu.ui.bangdan;

import cn.feihutv.zhibofeihu.data.network.http.model.bangdan.LoadContriRankListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.bangdan.LoadContriRankListResponse;
import cn.feihutv.zhibofeihu.ui.bangdan.ConMvpView;
import cn.feihutv.zhibofeihu.ui.bangdan.ConMvpPresenter;
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
public class ConPresenter<V extends ConMvpView> extends BasePresenter<V>
        implements ConMvpPresenter<V> {


    @Inject
    public ConPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void loadContriRankList(String rankType) {
        getMvpView().showProgressView();
        getCompositeDisposable().add(getDataManager()
                .doLoadContriRankListApiCall(new LoadContriRankListRequest(rankType))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoadContriRankListResponse>() {
                    @Override
                    public void accept(@NonNull LoadContriRankListResponse loadContriRankListResponse) throws Exception {
                        if (loadContriRankListResponse.getCode() == 0) {
                            if (loadContriRankListResponse.getDatas().size()> 0) {
                                getMvpView().getDatas(loadContriRankListResponse.getDatas());
                                getMvpView().showContentView();
                            } else {
                                getMvpView().showNodataView();
                            }

                        } else {
                            getMvpView().showErrorView();
                        }
                        getMvpView().hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        getMvpView().showErrorView();
                    }
                })

        );
    }
}
