package cn.feihutv.zhibofeihu.ui.me.roommrgs;

import android.view.Gravity;

import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.CancelRoomMgrRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.CancelRoomMgrResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.GetRoomMrgsRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.GetRoomMrgsResponce;
import cn.feihutv.zhibofeihu.ui.me.roommrgs.RoomMrgsMvpView;
import cn.feihutv.zhibofeihu.ui.me.roommrgs.RoomMrgsMvpPresenter;
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
public class RoomMrgsPresenter<V extends RoomMrgsMvpView> extends BasePresenter<V>
        implements RoomMrgsMvpPresenter<V> {


    @Inject
    public RoomMrgsPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void getRoomMgrs() {
        getCompositeDisposable().add(getDataManager()
                .doGetRoomMrgsRequestApiCall(new GetRoomMrgsRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetRoomMrgsResponce>() {
                    @Override
                    public void accept(@NonNull GetRoomMrgsResponce getRoomMrgsResponce) throws Exception {
                        if (getRoomMrgsResponce.getCode() == 0) {
                            if (getRoomMrgsResponce.getRoomMrgDataList().size() > 0) {
                                getMvpView().showContent();
                                getMvpView().getDatas(getRoomMrgsResponce.getRoomMrgDataList());
                            } else {
                                getMvpView().showNodataView();
                            }
                        } else {
                            AppLogger.i(getRoomMrgsResponce.getCode() + " " + getRoomMrgsResponce.getErrMsg());
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

    @Override
    public void cancelRoomMgr(String userId, final int position) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .doCancelRoomMgrCall(new CancelRoomMgrRequest(userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CancelRoomMgrResponce>() {
                    @Override
                    public void accept(@NonNull CancelRoomMgrResponce cancelRoomMgrResponce) throws Exception {
                        getMvpView().hideLoading();
                        if (cancelRoomMgrResponce.getCode() == 0) {
                            // 取消成功
                            getMvpView().showCancelSucc(position);
                        } else {
                            getMvpView().onToast("取消失败，请稍后再试~", Gravity.CENTER, 0, 0);
                        }
                    }
                }, getConsumer())

        );

    }
}
