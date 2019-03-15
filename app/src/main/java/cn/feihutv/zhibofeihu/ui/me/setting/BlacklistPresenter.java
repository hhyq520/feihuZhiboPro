package cn.feihutv.zhibofeihu.ui.me.setting;

import android.view.Gravity;

import cn.feihutv.zhibofeihu.data.network.http.model.me.GetBlacklistRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetBlacklistResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.UnblockRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.UnblockResponse;
import cn.feihutv.zhibofeihu.ui.me.setting.BlacklistMvpView;
import cn.feihutv.zhibofeihu.ui.me.setting.BlacklistMvpPresenter;
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
public class BlacklistPresenter<V extends BlacklistMvpView> extends BasePresenter<V>
        implements BlacklistMvpPresenter<V> {


    @Inject
    public BlacklistPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void getBlacklist(int offset) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .doGetBlacklistCall(new GetBlacklistRequest(String.valueOf(offset), String.valueOf(15)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetBlacklistResponse>() {
                    @Override
                    public void accept(@NonNull GetBlacklistResponse getBlacklistResponse) throws Exception {
                        if (getBlacklistResponse.getCode() == 0) {
                            if (getBlacklistResponse.getBlacklistResponseDatas().size() > 0) {
                                getMvpView().showContent();
                                getMvpView().getDatas(getBlacklistResponse.getBlacklistResponseDatas());
                            } else {
                                // 无数据
                                getMvpView().showNoData();
                            }
                        } else {
                            AppLogger.i(getBlacklistResponse.getCode() + " " + getBlacklistResponse.getErrMsg());
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

    @Override
    public void unblock(String userId, final int position) {
        getCompositeDisposable().add(getDataManager()
                .doUnblockCall(new UnblockRequest(userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UnblockResponse>() {
                    @Override
                    public void accept(@NonNull UnblockResponse unblockResponse) throws Exception {
                        if (unblockResponse.getCode() == 0) {
                            getMvpView().removeSucc(position);
                            getMvpView().onToast("成功移除", Gravity.CENTER, 0, 0);
                        } else {
                            getMvpView().onToast("移除失败", Gravity.CENTER, 0, 0);
                        }
                    }
                }, getConsumer())

        );

    }
}
