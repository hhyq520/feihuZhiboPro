package cn.feihutv.zhibofeihu.ui.me;

import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomContriRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomContriResponce;
import cn.feihutv.zhibofeihu.ui.me.ContributionListMvpView;
import cn.feihutv.zhibofeihu.ui.me.ContributionListMvpPresenter;
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
 *     @author : liwen.chen
 *     time   :
 *     desc   : p业务处理实现类
 *     version: 1.0
 * </pre>
 */
public class ContributionListPresenter<V extends ContributionListMvpView> extends BasePresenter<V>
        implements ContributionListMvpPresenter<V> {


    @Inject
    public ContributionListPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void loadRoomContriList(String userId, int rankType) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .doLoadRoomContriApiCall(new LoadRoomContriRequest(userId, rankType))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoadRoomContriResponce>() {
                    @Override
                    public void accept(@NonNull LoadRoomContriResponce loadRoomContriResponce) throws Exception {
                        if (loadRoomContriResponce.getCode() == 0) {
                            if (loadRoomContriResponce.getRoomContriDataList().size() > 0) {
                                getMvpView().showContent();
                                getMvpView().getDatas(loadRoomContriResponce.getRoomContriDataList());
                            } else {
                                getMvpView().showNoData();
                            }
                        } else {
                            AppLogger.i(loadRoomContriResponce.getCode() + " " + loadRoomContriResponce.getErrMsg());
                            getMvpView().onToast("获取失败， 请重新获取");
                        }
                        getMvpView().hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        getMvpView().hideLoading();
                    }
                })

        );

    }
}
