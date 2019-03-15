package cn.feihutv.zhibofeihu.ui.mv;

import javax.inject.Inject;

import cn.feihutv.zhibofeihu.data.DataManager;
import cn.feihutv.zhibofeihu.data.db.model.SysConfigBean;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.CollectNeedRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.CollectNeedResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.EnablePostMVRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.EnablePostMVResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetAllNeedListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetAllNeedListResponse;
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
public class DemandSquarePresenter<V extends DemandSquareMvpView> extends BasePresenter<V>
        implements DemandSquareMvpPresenter<V> {


    @Inject
    public DemandSquarePresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void getAllNeedList(String forPostMV, String forMe, String offset, String count) {
         getCompositeDisposable().add(getDataManager()
                         .doGetAllNeedListCall(new GetAllNeedListRequest(forPostMV,forMe,offset,count))
                         .subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribe(new Consumer<GetAllNeedListResponse>() {
                             @Override
                             public void accept(@NonNull GetAllNeedListResponse response) throws Exception {

                                 getMvpView().onGetAllNeedList(response);
                             }
                         }, new Consumer<Throwable>() {
                             @Override
                             public void accept(@NonNull Throwable throwable) throws Exception {
                                 getMvpView().onToast("请求失败，请稍后重试!");
                                 getMvpView().onGetAllNeedList(null);
                             }
                         })

                 );
    }

    @Override
    public void unCollectNeed(String id) {
         getCompositeDisposable().add(getDataManager()
                         .doUnCollectNeedCall(new UnCollectNeedRequest(id))
                         .subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribe(new Consumer<UnCollectNeedResponse>() {
                             @Override
                             public void accept(@NonNull UnCollectNeedResponse response) throws Exception {

                                 getMvpView().onUnCollectNeedResp(response);
                             }
                         }, getConsumer())

                 );
    }

    @Override
    public void collectNeed(String id) {
        getCompositeDisposable().add(getDataManager()
                .doCollectNeedCall(new CollectNeedRequest(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CollectNeedResponse>() {
                    @Override
                    public void accept(@NonNull CollectNeedResponse response) throws Exception {

                        getMvpView().onCollectNeedResp(response);
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

    @Override
    public SysConfigBean getSysConfig() {
        return getDataManager().getSysConfig();
    }
}
