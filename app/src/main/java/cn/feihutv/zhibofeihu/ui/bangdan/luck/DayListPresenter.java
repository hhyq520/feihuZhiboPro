package cn.feihutv.zhibofeihu.ui.bangdan.luck;

import cn.feihutv.zhibofeihu.data.network.http.model.bangdan.LoadLuckRecordListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.bangdan.LoadLuckRecordListResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseResponse;
import cn.feihutv.zhibofeihu.ui.bangdan.luck.DayListMvpView;
import cn.feihutv.zhibofeihu.ui.bangdan.luck.DayListMvpPresenter;
import cn.feihutv.zhibofeihu.ui.base.BasePresenter;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.DataManager;

import com.androidnetworking.error.ANError;

import javax.inject.Inject;

import cn.feihutv.zhibofeihu.utils.AppLogger;
import io.reactivex.Scheduler;
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
public class DayListPresenter<V extends DayListMvpView> extends BasePresenter<V>
        implements DayListMvpPresenter<V> {


    @Inject
    public DayListPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void loadLuckRecordList(String rankType, int offset, int count) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .doLoadLuckRecordListApiCall(new LoadLuckRecordListRequest(rankType, String.valueOf(offset), String.valueOf(count)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoadLuckRecordListResponse>() {
                    @Override
                    public void accept(@NonNull LoadLuckRecordListResponse loadLuckRecordListResponse) throws Exception {
                        if (loadLuckRecordListResponse.getCode() == 0) {
                            getMvpView().getDatas(loadLuckRecordListResponse.getResponseData());
                        } else {
                            AppLogger.i(loadLuckRecordListResponse.getCode() + loadLuckRecordListResponse.getErrMsg());
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
    public LoadUserDataBaseResponse.UserData getUserDatas() {
        return getDataManager().getUserData();
    }
}
