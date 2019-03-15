package cn.feihutv.zhibofeihu.ui.me.vip;

import cn.feihutv.zhibofeihu.data.network.http.model.vip.GetVipReceiveLogRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.vip.GetVipReceiveLogResponse;
import cn.feihutv.zhibofeihu.ui.me.vip.MyReceiveMvpView;
import cn.feihutv.zhibofeihu.ui.me.vip.MyReceiveMvpPresenter;
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
public class MyReceivePresenter<V extends MyReceiveMvpView> extends BasePresenter<V>
        implements MyReceiveMvpPresenter<V> {


    @Inject
    public MyReceivePresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void getVipRecvLog(int offset) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .doGetVipRecvLogCall(new GetVipReceiveLogRequest(offset, 20))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetVipReceiveLogResponse>() {
                    @Override
                    public void accept(@NonNull GetVipReceiveLogResponse getVipReceiveLogResponse) throws Exception {
                        if (getVipReceiveLogResponse.getCode() == 0) {
                            if (getVipReceiveLogResponse.getGetVipReceiveLogData().getReceiveLogList().size() > 0) {
                                getMvpView().showContent();
                                getMvpView().getDatas(getVipReceiveLogResponse.getGetVipReceiveLogData().getReceiveLogList(), getVipReceiveLogResponse.getGetVipReceiveLogData().getNextOffset());
                            } else {
                                getMvpView().showNoData();
                            }
                        } else {
                            AppLogger.i(getVipReceiveLogResponse.getCode() + " " + getVipReceiveLogResponse.getErrMsg());
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
