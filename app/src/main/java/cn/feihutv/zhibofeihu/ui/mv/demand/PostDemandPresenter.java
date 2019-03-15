package cn.feihutv.zhibofeihu.ui.mv.demand;

import javax.inject.Inject;

import cn.feihutv.zhibofeihu.data.DataManager;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetNeedSysHBRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetNeedSysHBResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.PostNeedRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.PostNeedResponse;
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
public class PostDemandPresenter<V extends PostDemandMvpView> extends BasePresenter<V>
        implements PostDemandMvpPresenter<V> {


    @Inject
    public PostDemandPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void getNeedSysHB() {
         getCompositeDisposable().add(getDataManager()
                         .doGetNeedSysHBCall(new GetNeedSysHBRequest())
                         .subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribe(new Consumer<GetNeedSysHBResponse>() {
                             @Override
                             public void accept(@NonNull GetNeedSysHBResponse response) throws Exception {
                                 getMvpView().onGetNeedSysHBResp(response);
                             }
                         }, getConsumer())

                 );
    }

    @Override
    public void postNeedData(PostNeedRequest request) {
        getCompositeDisposable().add(getDataManager()
                .doPostNeedCall(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PostNeedResponse>() {
                    @Override
                    public void accept(@NonNull PostNeedResponse response) throws Exception {

                        getMvpView().onPostNeedDataResp(response);

                    }
                }, getConsumer())

        );
    }
}
