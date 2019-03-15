package cn.feihutv.zhibofeihu.ui.live;

import android.os.Bundle;

import cn.feihutv.zhibofeihu.data.network.http.model.common.LogShareRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LogShareResponce;
import cn.feihutv.zhibofeihu.data.db.model.HistoryRecordBean;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.CloseLiveRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.CloseLiveResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.JoinRoomRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.JoinRoomResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.LeaveRoomRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.LeaveRoomResponce;
import cn.feihutv.zhibofeihu.ui.live.PLVideoViewMvpView;
import cn.feihutv.zhibofeihu.ui.live.PLVideoViewMvpPresenter;
import cn.feihutv.zhibofeihu.ui.base.BasePresenter;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.DataManager;

import com.androidnetworking.error.ANError;
import com.chinanetcenter.StreamPusher.sdk.SPManager;

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
public class PLVideoViewPresenter<V extends PLVideoViewMvpView> extends BasePresenter<V>
        implements PLVideoViewMvpPresenter<V> {


    @Inject
    public PLVideoViewPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void joinRoom(String roomId, String reconnect, final boolean isFirst) {
        getDataManager()
                .doLoadJoinRoomSocketApiCall(new JoinRoomRequest(roomId, reconnect))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JoinRoomResponce>() {
                    @Override
                    public void accept(@NonNull JoinRoomResponce response) throws Exception {
                        if(isFirst) {
                            getMvpView().joinRoomRespoce(response);
                        }
                    }
                }, getConsumer());
    }

    @Override
    public void leaveRoom(String roomId) {
        getDataManager()
                .doLoadLeaveRoomApiCall(new LeaveRoomRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LeaveRoomResponce>() {
                    @Override
                    public void accept(@NonNull LeaveRoomResponce response) throws Exception {
                        if (response.getCode() == 0) {

                        } else {
                            AppLogger.e("leaveRoom" + response.getErrMsg());
                        }
                    }
                }, getConsumer());
    }

    @Override
    public void logShare(int from, int to) {
        getDataManager()
                .doLogShareApiCall(new LogShareRequest(from, to))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LogShareResponce>() {
                    @Override
                    public void accept(@NonNull LogShareResponce response) throws Exception {
                        if (response.getCode() == 0) {

                        } else {
                            AppLogger.e("leaveRoom" + response.getErrMsg());
                        }
                    }
                }, getConsumer());
    }

    public void saveHistory(JoinRoomResponce.JoinRoomData joinRoomData) {
        HistoryRecordBean historyRecordBean = new HistoryRecordBean();
        historyRecordBean.setUserId(getDataManager().getCurrentUserId());
        historyRecordBean.setHeadUrl(joinRoomData.getMasterDataList().getHeadUrl());
        historyRecordBean.setHostName(joinRoomData.getMasterDataList().getNickName());
        historyRecordBean.setTitle(joinRoomData.getMasterDataList().getRoomName());
        historyRecordBean.setRoomId(joinRoomData.getRoomId());
        historyRecordBean.setTime(System.currentTimeMillis());
        getCompositeDisposable().add(getDataManager()
                .saveHistoryRecord(historyRecordBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {

                    }
                }, getConsumer()));
    }
}
