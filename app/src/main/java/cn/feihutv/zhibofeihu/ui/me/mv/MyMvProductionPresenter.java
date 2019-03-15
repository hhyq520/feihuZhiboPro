package cn.feihutv.zhibofeihu.ui.me.mv;

import javax.inject.Inject;

import cn.feihutv.zhibofeihu.data.DataManager;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMyMVListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMyMVListResponse;
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
public class MyMvProductionPresenter<V extends MyMvProductionMvpView> extends BasePresenter<V>
        implements MyMvProductionMvpPresenter<V> {


    @Inject
    public MyMvProductionPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }


    @Override
    public void getMyMVList(String status, String offset, String count) {
         getCompositeDisposable().add(getDataManager()
                         .doGetMyMVListCall(new GetMyMVListRequest(status,offset,count))
                         .subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribe(new Consumer<GetMyMVListResponse>() {
                             @Override
                             public void accept(@NonNull GetMyMVListResponse response) throws Exception {
                                 getMvpView().GetMyMVListResp(response);
                             }
                         }, getConsumer())

                 );
    }




}
