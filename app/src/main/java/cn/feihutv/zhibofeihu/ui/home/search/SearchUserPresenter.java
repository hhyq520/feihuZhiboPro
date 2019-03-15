package cn.feihutv.zhibofeihu.ui.home.search;

import android.view.Gravity;

import java.util.List;

import cn.feihutv.zhibofeihu.data.db.model.SearchHistoryInfo;
import cn.feihutv.zhibofeihu.data.network.http.model.common.FollowRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.FollowResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.home.RecommendRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.home.RecommendResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.home.SearchRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.home.SearchResponse;
import cn.feihutv.zhibofeihu.ui.base.BasePresenter;
import cn.feihutv.zhibofeihu.data.DataManager;

import javax.inject.Inject;

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
public class SearchUserPresenter<V extends SearchUserMvpView> extends BasePresenter<V>
        implements SearchUserMvpPresenter<V> {


    @Inject
    public SearchUserPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void search(String accountId) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .doSearchApiCall(new SearchRequest(accountId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SearchResponse>() {
                    @Override
                    public void accept(@NonNull SearchResponse searchResponse) throws Exception {
                        if (searchResponse.getCode() == 0) {
                            if (searchResponse.getSearchResponseDatas().size() > 0) {
                                getMvpView().getDatas(searchResponse.getSearchResponseDatas());
                            } else {
                                getMvpView().showNoDatas();
                            }
                        }
                        getMvpView().hideLoading();
                    }
                }, getConsumer()));
    }

    @Override
    public void follow(String userId, final int position) {
        getCompositeDisposable().add(getDataManager()
                .doFollowApiCall(new FollowRequest(userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FollowResponce>() {
                    @Override
                    public void accept(@NonNull FollowResponce followResponce) throws Exception {
                        if (followResponce.getCode() == 0) {
                            getMvpView().onToast("关注成功", Gravity.CENTER, 0, 0);
                            getMvpView().followSuccess(position);
                        } else {
                            getMvpView().onToast("关注失败", Gravity.CENTER, 0, 0);
                        }
                    }
                }, getConsumer()));
    }

    @Override
    public void loadRecommed(final boolean isFromSearch) {
        getCompositeDisposable().add(getDataManager()
                .doLoadRecommendedListApiCall(new RecommendRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RecommendResponse>() {
                    @Override
                    public void accept(@NonNull RecommendResponse recommendResponse) throws Exception {
                        if (recommendResponse.getCode() == 0) {
                            if (recommendResponse.getResponseDatas().size() > 0) {
                                getMvpView().showContent(isFromSearch);
                                getMvpView().getRecommedDatas(recommendResponse.getResponseDatas());
                            } else {
                                getMvpView().showNoRemmendHost();
                            }
                        } else {
                            getMvpView().showErrorView();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        getMvpView().showErrorView();
                    }
                }));
    }

    @Override
    public void querySearch() {
        getCompositeDisposable().add(getDataManager()
                .getAllSearchInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<SearchHistoryInfo>>() {
                    @Override
                    public void accept(@NonNull List<SearchHistoryInfo> searchHistoryInfos) throws Exception {
                        getMvpView().getSearchInfo(searchHistoryInfos);
                    }
                }, getConsumer()));
    }

    @Override
    public void deleteSearchInfoByKey(String accountId) {
        getCompositeDisposable().add(getDataManager()
                .deleteSearchInfoByAccountId(accountId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                    }
                }, getConsumer())

        );

    }

    @Override
    public void deleteAllSearchInfo() {
        getCompositeDisposable().add(getDataManager()
                .deleteAllSearchInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {

                    }
                }, getConsumer())

        );

    }

    @Override
    public void saveSearchInfo(SearchHistoryInfo searchHistoryInfo) {
        getCompositeDisposable().add(getDataManager()
                .saveSearchInfo(searchHistoryInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {

                    }
                }, getConsumer())

        );

    }
}
