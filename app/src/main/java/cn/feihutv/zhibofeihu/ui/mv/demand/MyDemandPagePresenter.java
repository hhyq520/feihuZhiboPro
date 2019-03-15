package cn.feihutv.zhibofeihu.ui.mv.demand;

import javax.inject.Inject;

import cn.feihutv.zhibofeihu.data.DataManager;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.DeleteMVRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.DeleteMVResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.DeleteNeedRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.DeleteNeedResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.EditNeedRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.EditNeedResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMyNeedListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMyNeedListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMyNeedMVListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMyNeedMVListResponse;
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
public class MyDemandPagePresenter<V extends MyDemandPageMvpView> extends BasePresenter<V>
        implements MyDemandPageMvpPresenter<V> {


    @Inject
    public MyDemandPagePresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void getMyNeedList(String offset, String count) {
         getCompositeDisposable().add(getDataManager()
                         .doGetMyNeedListCall(new GetMyNeedListRequest(offset,count))
                         .subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribe(new Consumer<GetMyNeedListResponse>() {
                             @Override
                             public void accept(@NonNull GetMyNeedListResponse response) throws Exception {
                                 getMvpView().onGetMyNeedListResp(response);
                             }
                         }, getConsumer())

                 );
    }

    @Override
    public void editNeed(EditNeedRequest request) {
        getMvpView().showLoading("正在修改中...");
        getCompositeDisposable().add(getDataManager()
                .doEditNeedCall(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<EditNeedResponse>() {
                    @Override
                    public void accept(@NonNull EditNeedResponse response) throws Exception {
                        getMvpView().onEditNeedResp(response);
                        getMvpView().hideLoading();
                    }
                }, getConsumer())

        );
    }

    @Override
    public void getMyNeedMVList(String status, String offset, String count) {
        getCompositeDisposable().add(getDataManager()
                .doGetMyNeedMVListCall(new GetMyNeedMVListRequest(status,offset,count))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetMyNeedMVListResponse>() {
                    @Override
                    public void accept(@NonNull GetMyNeedMVListResponse response) throws Exception {
                        getMvpView().onGetMyNeedMVListResp(response);
                    }
                }, getConsumer())

        );
    }

    @Override
    public void deleteMV(String mvId) {
        getCompositeDisposable().add(getDataManager()
                .doDeleteMVCall(new DeleteMVRequest(mvId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DeleteMVResponse>() {
                    @Override
                    public void accept(@NonNull DeleteMVResponse response) throws Exception {
                        getMvpView().onDeleteMVResp(response);
                    }
                }, getConsumer())

        );
    }

    @Override
    public void deleteNeed(String nid) {
        getCompositeDisposable().add(getDataManager()
                .doDeleteNeedCall(new DeleteNeedRequest(nid))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DeleteNeedResponse>() {
                    @Override
                    public void accept(@NonNull DeleteNeedResponse response) throws Exception {
                        getMvpView().onDeleteNeedResp(response);
                    }
                }, getConsumer())

        );
    }
}
