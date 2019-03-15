package cn.feihutv.zhibofeihu.ui.me.dynamic;

import cn.feihutv.zhibofeihu.data.network.http.model.me.LoadAllFeedListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LoadFeedListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LoadFeedListResponse;
import cn.feihutv.zhibofeihu.ui.me.dynamic.MyDynamicMvpView;
import cn.feihutv.zhibofeihu.ui.me.dynamic.MyDynamicMvpPresenter;
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
public class MyDynamicPresenter<V extends MyDynamicMvpView> extends BasePresenter<V>
        implements MyDynamicMvpPresenter<V> {


    @Inject
    public MyDynamicPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    // 获取动态列表
    @Override
    public void loadFeedList(String userId, String last) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .doLoadFeedListApiCall(new LoadFeedListRequest(userId, last, String.valueOf(20)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoadFeedListResponse>() {
                    @Override
                    public void accept(@NonNull LoadFeedListResponse loadFeedListResponse) throws Exception {
                        if (loadFeedListResponse.getCode() == 0) {
                            getMvpView().getDatas(loadFeedListResponse.getDynamicItemList());
                        } else {
                            getMvpView().getDatasError();
                        }
                        getMvpView().hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        getMvpView().hideLoading();
                        getMvpView().getDatasError();
                    }
                })

        );

    }

    // 获取全部动态
    @Override
    public void loadAllFeedList(int offset) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .doLoadAllFeedListCall(new LoadAllFeedListRequest(offset, 20))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoadFeedListResponse>() {
                    @Override
                    public void accept(@NonNull LoadFeedListResponse loadFeedListResponse) throws Exception {
                        if (loadFeedListResponse.getCode() == 0) {
                            getMvpView().getDatas(loadFeedListResponse.getDynamicItemList());
                        } else {
                            getMvpView().getDatasError();
                        }
                        getMvpView().hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        getMvpView().hideLoading();
                        AppLogger.i(throwable.toString());
                        getMvpView().getDatasError();
                    }
                })

        );

    }
}
