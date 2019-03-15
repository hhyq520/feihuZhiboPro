package cn.feihutv.zhibofeihu.ui.live;

import javax.inject.Inject;

import cn.feihutv.zhibofeihu.data.DataManager;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.LoadRoomGuestRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.LoadRoomGuestResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.LoadRoomMemberResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.LoadRoomMembersRequest;
import cn.feihutv.zhibofeihu.ui.base.BasePresenter;
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
public class UserOnlineFragmentPresenter<V extends UserOnlineFragmentMvpView> extends BasePresenter<V>
        implements UserOnlineFragmentMvpPresenter<V> {


    @Inject
    public UserOnlineFragmentPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void loadRoomMembers(int offset, int count, String type) {
        getCompositeDisposable().add(getDataManager()
                .doLoadRoomMembersApiCall(new LoadRoomMembersRequest(offset,count,type))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoadRoomMemberResponce>() {
                    @Override
                    public void accept(@NonNull LoadRoomMemberResponce loadRoomMemberResponce) throws Exception {
                        if(loadRoomMemberResponce.getCode()==0){
                            getMvpView().notifyList(loadRoomMemberResponce.getRoomMemberData());
                        }else{
                            AppLogger.e(loadRoomMemberResponce.getErrMsg());
                        }
                        getMvpView().onFreshStop();
                    }
                },getConsumer()));
    }

    @Override
    public void loadRoomGuestCnt() {
        getCompositeDisposable().add(getDataManager()
                .doLoadRoomGuestApiCall(new LoadRoomGuestRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoadRoomGuestResponce>() {
                    @Override
                    public void accept(@NonNull LoadRoomGuestResponce loadRoomGuestResponce) throws Exception {
                        if(loadRoomGuestResponce.getCode()==0){
                            getMvpView().notifyGuestCnt(loadRoomGuestResponce.getRoomGuestData());
                        }else{
                            AppLogger.e(loadRoomGuestResponce.getErrMsg());
                        }
                        getMvpView().loadMoreStop();
                    }
                },getConsumer()));
    }

    @Override
    public void loadRoomMembersMore(int offset, int count, String type) {
        getCompositeDisposable().add(
                getDataManager()
                .doLoadRoomMembersApiCall(new LoadRoomMembersRequest(offset,count,type))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoadRoomMemberResponce>() {
                    @Override
                    public void accept(@NonNull LoadRoomMemberResponce loadRoomMemberResponce) throws Exception {
                        if(loadRoomMemberResponce.getCode()==0){
                            getMvpView().notifyMoreList(loadRoomMemberResponce.getRoomMemberData());
                        }else{
                            AppLogger.e(loadRoomMemberResponce.getErrMsg());
                        }
                        getMvpView().loadMoreStop();
                    }
                },getConsumer())
        );
    }
}
