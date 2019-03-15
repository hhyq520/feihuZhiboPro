package cn.feihutv.zhibofeihu.ui.me.dynamic;

import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomContriRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomContriResponce;
import cn.feihutv.zhibofeihu.ui.me.dynamic.OtherPageMvpView;
import cn.feihutv.zhibofeihu.ui.me.dynamic.OtherPageMvpPresenter;
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
public class OtherPagePresenter<V extends OtherPageMvpView> extends BasePresenter<V>
        implements OtherPageMvpPresenter<V> {


    @Inject
    public OtherPagePresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void loadRoomContriList(String userId) {
        getCompositeDisposable().add(getDataManager()
                .doLoadRoomContriApiCall(new LoadRoomContriRequest(userId, 3))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoadRoomContriResponce>() {
                    @Override
                    public void accept(@NonNull LoadRoomContriResponce loadRoomContriResponce) throws Exception {
                        if (loadRoomContriResponce.getCode() == 0) {
                            getMvpView().getContriDatas(loadRoomContriResponce.getRoomContriDataList());
                        } else {
                            AppLogger.i(loadRoomContriResponce.getCode() + " " + loadRoomContriResponce.getErrMsg());
                        }
                    }
                }, getConsumer())

        );

    }
}
