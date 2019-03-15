package cn.feihutv.zhibofeihu.ui.home.history;

import java.util.List;

import cn.feihutv.zhibofeihu.data.db.model.HistoryRecordBean;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomResponce;
import cn.feihutv.zhibofeihu.ui.base.BasePresenter;
import cn.feihutv.zhibofeihu.data.DataManager;


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
public class HistoryPresenter<V extends HistoryMvpView> extends BasePresenter<V>
        implements HistoryMvpPresenter<V> {


    @Inject
    public HistoryPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void queryHistory() {
        // 查询观看历史记录
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .getAllHistory(getDataManager().getCurrentUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<HistoryRecordBean>>() {
                    @Override
                    public void accept(@NonNull List<HistoryRecordBean> historyRecordBeen) throws Exception {
                        getMvpView().hideLoading();
                        getMvpView().getAllHistory(historyRecordBeen);
                    }
                }));

    }

    @Override
    public void deleteAllHistory() {
        getCompositeDisposable().add(getDataManager()
                .deleteAllHistory(getDataManager().getCurrentUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        getMvpView().onToast("删除成功");
                    }
                }));
    }

    @Override
    public void loadRoomById(String roomId) {
        getCompositeDisposable().add(getDataManager()
                .doGetRoomDataApiCall(new LoadRoomRequest(roomId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoadRoomResponce>() {
                    @Override
                    public void accept(@NonNull LoadRoomResponce loadRoomResponce) throws Exception {
                        if (loadRoomResponce.getCode() == 0) {
                            getMvpView().gotoRoom(loadRoomResponce.getLoadRoomData());
                        } else {
                            AppLogger.i("throwable" + loadRoomResponce.getErrMsg() + loadRoomResponce.getCode());
                        }
                    }
                }, getConsumer()));
    }
}
