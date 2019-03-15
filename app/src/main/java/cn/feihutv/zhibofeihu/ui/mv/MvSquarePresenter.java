package cn.feihutv.zhibofeihu.ui.mv;

import javax.inject.Inject;

import cn.feihutv.zhibofeihu.data.DataManager;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.CancelMVNoticeRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.CancelMVNoticeResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.QueryMVNoticeRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.QueryMVNoticeResponse;
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
public class MvSquarePresenter<V extends MvSquareMvpView> extends BasePresenter<V>
        implements MvSquareMvpPresenter<V> {


    @Inject
    public MvSquarePresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void queryMVNotice() {
         getCompositeDisposable().add(getDataManager()
                         .doQueryMVNoticeCall(new QueryMVNoticeRequest())
                         .subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribe(new Consumer<QueryMVNoticeResponse>() {
                             @Override
                             public void accept(@NonNull QueryMVNoticeResponse response) throws Exception {
                                 getMvpView().onQueryMVNoticeResp(response);
                             }
                         }, getConsumer())

                 );
    }

    @Override
    public void cancelMVNotice() {
        getCompositeDisposable().add(getDataManager()
                .doCancelMVNoticeCall(new CancelMVNoticeRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CancelMVNoticeResponse>() {
                    @Override
                    public void accept(@NonNull CancelMVNoticeResponse response) throws Exception {
                        getMvpView().onCancelMVNoticeResp(response);
                    }
                }, getConsumer())

        );
    }
}
