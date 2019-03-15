package cn.feihutv.zhibofeihu.ui.mv.demand;

import javax.inject.Inject;

import cn.feihutv.zhibofeihu.data.DataManager;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.EnablePostMVRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.EnablePostMVResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetNeedCollectListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetNeedCollectListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.UnCollectNeedRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.UnCollectNeedResponse;
import cn.feihutv.zhibofeihu.ui.base.BasePresenter;
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
public class MyMvCollectPresenter<V extends MyMvCollectMvpView> extends BasePresenter<V>
        implements MyMvCollectMvpPresenter<V> {


    @Inject
    public MyMvCollectPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void getNeedCollectList(String offset, String count) {
        getMvpView().showLoading();
         getCompositeDisposable().add(getDataManager()
                         .doGetNeedCollectCall(new GetNeedCollectListRequest(offset,count))
                         .subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribe(new Consumer<GetNeedCollectListResponse>() {
                             @Override
                             public void accept(@NonNull GetNeedCollectListResponse response) throws Exception {
                                 getMvpView().hideLoading();
                                 getMvpView().onGetNeedCollectListResp(response);
                             }
                         }, getConsumer())

                 );

    }


    @Override
    public void unCollectNeed(String needId) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .doUnCollectNeedCall(new UnCollectNeedRequest(needId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UnCollectNeedResponse>() {
                    @Override
                    public void accept(@NonNull UnCollectNeedResponse response) throws Exception {
                        getMvpView().hideLoading();
                        getMvpView().onUnCollectNeedResp(response);
                    }
                }, getConsumer())

        );
    }



    @Override
    public void enablePostMV(String mvId) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .doEnablePostMVCall(new EnablePostMVRequest(mvId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<EnablePostMVResponse>() {
                    @Override
                    public void accept(@NonNull EnablePostMVResponse response) throws Exception {
                        getMvpView().hideLoading();
                        getMvpView().onEnablePostMVResp(response);
                    }
                }, getConsumer())

        );
    }
}
