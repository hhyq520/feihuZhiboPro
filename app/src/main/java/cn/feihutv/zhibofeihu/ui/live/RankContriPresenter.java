package cn.feihutv.zhibofeihu.ui.live;

import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomContriRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomContriResponce;
import cn.feihutv.zhibofeihu.ui.live.RankContriMvpView;
import cn.feihutv.zhibofeihu.ui.live.RankContriMvpPresenter;
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
 *     author : huang hao
 *     time   :
 *     desc   : p业务处理实现类
 *     version: 1.0
 * </pre>
 */
public class RankContriPresenter<V extends RankContriMvpView> extends BasePresenter<V>
        implements RankContriMvpPresenter<V> {


    @Inject
    public RankContriPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void loadRoomContriList(final int type) {
        getCompositeDisposable().add(getDataManager()
                .doLoadRoomContriApiCall(new LoadRoomContriRequest("",type))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoadRoomContriResponce>() {
                    @Override
                    public void accept(@NonNull LoadRoomContriResponce loadRoomContriResponce) throws Exception {
                        if(loadRoomContriResponce.getCode()==0){
                            getMvpView().notifyContriList(loadRoomContriResponce.getRoomContriDataList(),type);
                        }else{
                            AppLogger.e(loadRoomContriResponce.getErrMsg());
                        }
                        getMvpView().freshStop();
                    }
                },getConsumer()));
    }
}
