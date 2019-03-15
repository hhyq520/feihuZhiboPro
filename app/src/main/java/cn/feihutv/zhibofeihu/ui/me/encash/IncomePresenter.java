package cn.feihutv.zhibofeihu.ui.me.encash;

import cn.feihutv.zhibofeihu.data.db.model.SysConfigBean;
import cn.feihutv.zhibofeihu.data.network.http.model.me.EncashRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.EncashResponse;
import cn.feihutv.zhibofeihu.ui.base.BasePresenter;
import cn.feihutv.zhibofeihu.data.DataManager;

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
public class IncomePresenter<V extends IncomeMvpView> extends BasePresenter<V>
        implements IncomeMvpPresenter<V> {


    @Inject
    public IncomePresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void enCash(String cash) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .doEncashCall(new EncashRequest(cash))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<EncashResponse>() {
                    @Override
                    public void accept(@NonNull EncashResponse encashResponse) throws Exception {
                        if (encashResponse.getCode() == 0) {
                            getMvpView().showSucc();
                        } else {
                            if (encashResponse.getCode() == 4348) {
                                getMvpView().onToast("金虎币不足");
                            }
                            AppLogger.i(encashResponse.getCode() + " " + encashResponse.getErrMsg());
                        }
                        getMvpView().hideLoading();
                    }
                }, getConsumer()));
    }

    @Override
    public SysConfigBean getSysConfig() {
        return getDataManager().getSysConfig();
    }
}
