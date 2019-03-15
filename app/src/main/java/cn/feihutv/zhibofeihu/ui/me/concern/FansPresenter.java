package cn.feihutv.zhibofeihu.ui.me.concern;

import cn.feihutv.zhibofeihu.data.network.http.model.me.FollowsRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.FollowsResponse;
import cn.feihutv.zhibofeihu.ui.me.concern.FansMvpView;
import cn.feihutv.zhibofeihu.ui.me.concern.FansMvpPresenter;
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
public class FansPresenter<V extends FansMvpView> extends BasePresenter<V>
        implements FansMvpPresenter<V> {


    @Inject
    public FansPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void getFollowers(int offset) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .doGetFollowersApiCall(new FollowsRequest(String.valueOf(offset), "20"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FollowsResponse>() {
                    @Override
                    public void accept(@NonNull FollowsResponse followsResponse) throws Exception {
                        if (followsResponse.getCode() == 0) {
                            if (followsResponse.getFollowsResponseDatas().size() > 0) {
                                // 有数据
                                getMvpView().showContent();
                                getMvpView().getDatas(followsResponse.getFollowsResponseDatas());
                            } else {
                                // 无数据
                                getMvpView().showNoData();
                            }
                        } else {
                            AppLogger.i(followsResponse.getCode() + " " + followsResponse.getErrMsg());
                        }
                        getMvpView().hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        getMvpView().hideLoading();
                        // 加载失败
                        getMvpView().showErrorView();
                    }
                })

        );
    }
}
