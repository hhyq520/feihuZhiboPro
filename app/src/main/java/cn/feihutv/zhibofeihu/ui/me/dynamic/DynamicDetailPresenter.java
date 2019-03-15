package cn.feihutv.zhibofeihu.ui.me.dynamic;

import android.view.Gravity;

import javax.inject.Inject;

import cn.feihutv.zhibofeihu.data.DataManager;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LogShareRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LogShareResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.me.DeleteFeedRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.DeleteFeedResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LikeFeedRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LikeFeedResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LoadCommentListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LoadCommentListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LoadFeedDetailRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LoadFeedDetailResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.PostFeedCommentRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.PostFeedCommentResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.ShareFeedRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.ShareFeedResponse;
import cn.feihutv.zhibofeihu.ui.base.BasePresenter;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * <pre>
 *     author : sichu.chen
 *     time   :
 *     desc   : p业务处理实现类
 *     version: 1.0
 * </pre>
 */
public class DynamicDetailPresenter<V extends DynamicDetailMvpView> extends BasePresenter<V>
        implements DynamicDetailMvpPresenter<V> {


    @Inject
    public DynamicDetailPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void loadFeedDetail(String id) {
        getCompositeDisposable().add(getDataManager()
                .doLoadFeedDetailApiCall(new LoadFeedDetailRequest(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoadFeedDetailResponse>() {
                    @Override
                    public void accept(@NonNull LoadFeedDetailResponse response) throws Exception {
                        if (response.getCode() == 0) {
                            getMvpView().onLoadFeedDetailResp(response.getDetailResponseData());
                        } else if (response.getCode() == 4301) {
                            getMvpView().onToast("该动态已被删除", Gravity.CENTER, 0, 0);
                            getMvpView().feedNotExist();
                        } else {
                            getMvpView().onToast("加载失败", Gravity.CENTER, 0, 0);
                            AppLogger.i(response.getCode() + " " + response.getErrMsg());
                        }
                        getMvpView().hideLoading();
                    }
                }, getConsumer())

        );
    }

    @Override
    public void loadCommentList(String id, String last, int cnt) {
        getCompositeDisposable().add(getDataManager()
                .doLoadCommentListApiCall(new LoadCommentListRequest(id, last, cnt + ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoadCommentListResponse>() {
                    @Override
                    public void accept(@NonNull LoadCommentListResponse loadCommentListResponse) throws Exception {
                        getMvpView().onLoadCommentListResp(loadCommentListResponse.getCommenItemList());
                    }
                }, getConsumer())

        );

    }

    @Override
    public void likeFeed(String id) {
        getCompositeDisposable().add(getDataManager()
                .doLikeFeedApiCall(new LikeFeedRequest(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LikeFeedResponse>() {
                    @Override
                    public void accept(@NonNull LikeFeedResponse response) throws Exception {
                        getMvpView().onLikeFeedResp(response);
                    }
                }, getConsumer())

        );
    }

    @Override
    public void logShare(int from, int to) {
        getCompositeDisposable().add(getDataManager()
                .doLogShareApiCall(new LogShareRequest(from, to))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LogShareResponce>() {
                    @Override
                    public void accept(@NonNull LogShareResponce logShareResponce) throws Exception {
                        getMvpView().onLogShareResp(logShareResponce);
                    }
                }, getConsumer())

        );
    }

    @Override
    public void shareFeed(String id) {
        getCompositeDisposable().add(getDataManager()
                .doShareFeedApiCall(new ShareFeedRequest(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ShareFeedResponse>() {
                    @Override
                    public void accept(@NonNull ShareFeedResponse shareFeedResponse) throws Exception {
                        getMvpView().onShareFeedResp(shareFeedResponse);
                    }
                }, getConsumer())

        );
    }

    @Override
    public void deleteFeed(String id) {
        getCompositeDisposable().add(getDataManager()
                .doDeleteFeedApiCall(new DeleteFeedRequest(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DeleteFeedResponse>() {
                    @Override
                    public void accept(@NonNull DeleteFeedResponse deleteFeedResponse) throws Exception {
                        getMvpView().onDeleteFeedResp(deleteFeedResponse);
                    }
                }, getConsumer())

        );
    }

    @Override
    public void postFeedComment(String id, String content, String reply) {
        getCompositeDisposable().add(getDataManager()
                .doPostFeedCommentApiCall(new PostFeedCommentRequest(id, content, reply))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PostFeedCommentResponse>() {
                    @Override
                    public void accept(@NonNull PostFeedCommentResponse postFeedCommentResponse) throws Exception {
                        getMvpView().onPostFeedCommentResp(postFeedCommentResponse);
                    }
                }, getConsumer())

        );
    }
}
