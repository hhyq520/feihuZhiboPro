package cn.feihutv.zhibofeihu.ui.bangdan.luck;

import cn.feihutv.zhibofeihu.data.network.http.model.bangdan.LoadLuckRankListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.bangdan.LoadLuckRecordListRequest;
import cn.feihutv.zhibofeihu.ui.bangdan.luck.TotalListMvpView;
import cn.feihutv.zhibofeihu.ui.bangdan.luck.TotalListMvpPresenter;
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
public class TotalListPresenter<V extends TotalListMvpView> extends BasePresenter<V>
        implements TotalListMvpPresenter<V> {


    @Inject
    public TotalListPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void loadLuckRankList(String rankType) {
        getMvpView().showLoading();
         getCompositeDisposable().add(getDataManager()
                         .doLoadLuckRankListApiCall(new LoadLuckRecordListRequest(rankType, String.valueOf(0), String.valueOf(20)))
                         .subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribe(new Consumer<LoadLuckRankListResponse>() {
                             @Override
                             public void accept(@NonNull LoadLuckRankListResponse loadLuckRankListResponse) throws Exception {
                                 if (loadLuckRankListResponse.getCode() == 0) {
                                     getMvpView().getDatas(loadLuckRankListResponse.getResponseDatas());
                                 } else {
                                     AppLogger.i(loadLuckRankListResponse.getCode() + " " + loadLuckRankListResponse.getErrMsg());
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
