package cn.feihutv.zhibofeihu.ui.me.dynamic;

import android.view.Gravity;

import cn.feihutv.zhibofeihu.data.network.http.model.common.FollowRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.FollowResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LogShareRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.UnFollowRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.UnFollowResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.me.BlockRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.BlockResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.UnblockRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.UnblockResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.LoadOtherUserInfoRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.LoadOtherUserInfoResponce;
import cn.feihutv.zhibofeihu.ui.me.dynamic.OthersCommunityMvpView;
import cn.feihutv.zhibofeihu.ui.me.dynamic.OthersCommunityMvpPresenter;
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
 *     author : sichu.chen
 *     time   :
 *     desc   : p业务处理实现类
 *     version: 1.0
 * </pre>
 */
public class OthersCommunityPresenter<V extends OthersCommunityMvpView> extends BasePresenter<V>
        implements OthersCommunityMvpPresenter<V> {


    @Inject
    public OthersCommunityPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void loadOtherUserInfo(String userId) {
        getMvpView().showLoading();
        getDataManager().doLoadOtherUserInfoApiCall(new LoadOtherUserInfoRequest(userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoadOtherUserInfoResponce>() {
                    @Override
                    public void accept(@NonNull LoadOtherUserInfoResponce loadOtherUserInfoResponce) throws Exception {
                        if (loadOtherUserInfoResponce.getCode() == 0) {
                            getMvpView().getDatas(loadOtherUserInfoResponce.getOtherUserInfo());
                        } else {
                            AppLogger.i(loadOtherUserInfoResponce.getCode() + "  " + loadOtherUserInfoResponce.getErrMsg());
                            getMvpView().onToast("获取信息失败", Gravity.CENTER, 0, 0);
                        }
                        getMvpView().hideLoading();
                    }
                }, getConsumer());
    }

    @Override
    public void unfollow(String userId) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .doUnFollowApiCall(new UnFollowRequest(userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UnFollowResponce>() {
                    @Override
                    public void accept(@NonNull UnFollowResponce unFollowResponce) throws Exception {
                        if (unFollowResponce.getCode() == 0) {
                            getMvpView().unFollow();
                        } else {
                            AppLogger.i(unFollowResponce.getCode() + " " + unFollowResponce.getErrMsg());
                        }
                        getMvpView().hideLoading();
                    }
                }, getConsumer())

        );

    }

    @Override
    public void follow(String userId) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .doFollowApiCall(new FollowRequest(userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FollowResponce>() {
                    @Override
                    public void accept(@NonNull FollowResponce followResponce) throws Exception {
                        if (followResponce.getCode() == 0) {
                            getMvpView().followSucc();
                        } else {
                            AppLogger.i(followResponce.getCode() + " " + followResponce.getErrMsg());
                        }
                        getMvpView().hideLoading();
                    }
                }, getConsumer())

        );

    }

    @Override
    public void logShare(int to) {
        getCompositeDisposable().add(getDataManager()
                .doLogShareApiCall(new LogShareRequest(5, to))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object obj) throws Exception {
                        getMvpView().onToast("分享成功");
                    }
                }, getConsumer())

        );

    }

    @Override
    public void unblock(String userId) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .doUnblockCall(new UnblockRequest(userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UnblockResponse>() {
                    @Override
                    public void accept(@NonNull UnblockResponse unblockResponse) throws Exception {
                        if (unblockResponse.getCode() == 0) {
                            getMvpView().unBlockSucc();
                        } else {
                            AppLogger.i(unblockResponse.getCode() + " " + unblockResponse.getErrMsg());
                            getMvpView().onToast("移除失败，请稍后再试", Gravity.CENTER, 0, 0);
                        }
                        getMvpView().hideLoading();
                    }
                }, getConsumer())

        );

    }

    @Override
    public void block(String userId) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .doBlockCall(new BlockRequest(userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BlockResponse>() {
                    @Override
                    public void accept(@NonNull BlockResponse blockResponse) throws Exception {
                        if (blockResponse.getCode() == 0) {
                            getMvpView().blockSucc();
                        } else {
                            AppLogger.i(blockResponse.getCode() + " " + blockResponse.getErrMsg());
                        }
                        getMvpView().hideLoading();
                    }
                }, getConsumer())

        );

    }
}
