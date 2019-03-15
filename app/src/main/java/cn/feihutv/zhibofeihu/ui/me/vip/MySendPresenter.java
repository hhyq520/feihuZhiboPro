package cn.feihutv.zhibofeihu.ui.me.vip;

import cn.feihutv.zhibofeihu.data.network.http.model.vip.GetVipReceiveLogRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.vip.GetVipReceiveLogResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.vip.GetVipSendLogRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.vip.GetVipSendLogResponse;
import cn.feihutv.zhibofeihu.ui.me.vip.MySendMvpView;
import cn.feihutv.zhibofeihu.ui.me.vip.MySendMvpPresenter;
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
public class MySendPresenter<V extends MySendMvpView> extends BasePresenter<V>
        implements MySendMvpPresenter<V> {


    @Inject
    public MySendPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void getVipSendLog(int offset) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .doGetVipSendLogCall(new GetVipSendLogRequest(offset, 20))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetVipSendLogResponse>() {
                    @Override
                    public void accept(@NonNull GetVipSendLogResponse getVipSendLogResponse) throws Exception {
                        if (getVipSendLogResponse.getCode() == 0) {
                            if (getVipSendLogResponse.getGetVipSendLogData().getReceiveLogList().size() > 0) {
                                getMvpView().showContent();
                                getMvpView().getDatas(getVipSendLogResponse.getGetVipSendLogData().getReceiveLogList(), getVipSendLogResponse.getGetVipSendLogData().getNextOffset());
                            } else {
                                getMvpView().showNoData();
                            }
                        } else {
                            AppLogger.i(getVipSendLogResponse.getCode() + " " + getVipSendLogResponse.getErrMsg());
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
