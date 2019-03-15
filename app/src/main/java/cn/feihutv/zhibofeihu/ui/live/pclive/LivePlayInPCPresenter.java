package cn.feihutv.zhibofeihu.ui.live.pclive;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.feihutv.zhibofeihu.BuildConfig;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.data.db.model.HistoryRecordBean;
import cn.feihutv.zhibofeihu.data.db.model.SysConfigBean;
import cn.feihutv.zhibofeihu.data.db.model.SysGiftNewBean;
import cn.feihutv.zhibofeihu.data.db.model.SysMountNewBean;
import cn.feihutv.zhibofeihu.data.local.model.SysbagBean;
import cn.feihutv.zhibofeihu.data.local.model.TCChatEntity;
import cn.feihutv.zhibofeihu.data.network.http.model.common.BuyGoodsRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.BuyGoodsResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.FollowRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.FollowResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LogShareRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LogShareResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.SendGiftRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.SendGiftResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.UnFollowRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.UnFollowResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.BanRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.BanResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.GetCurrMountRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.GetCurrMountResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.LoadOtherRoomsRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.LoadOtherRoomsResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.SendLoudSpeakRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.SendLoudSpeakResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.user.BagRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.user.BagResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameRoundResultDetailRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameRoundResultDetailResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.JackpotCountDownRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.JackpotCountDownResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.common.SendRoomMsgRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.common.SendRoomResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.JoinRoomRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.JoinRoomResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.LeaveRoomRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.LeaveRoomResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.LoadOtherUserInfoRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.LoadOtherUserInfoResponce;
import cn.feihutv.zhibofeihu.rxbus.RxBus;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.ui.live.adapter.WanfaResultAdapter;
import cn.feihutv.zhibofeihu.ui.base.BasePresenter;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.DataManager;

import com.bumptech.glide.Glide;
import com.umeng.analytics.MobclickAgent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cn.feihutv.zhibofeihu.ui.widget.dialog.DerectMsgDialog;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.CustomDialogUtils;
import cn.feihutv.zhibofeihu.utils.ListViewDecoration;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import cn.feihutv.zhibofeihu.utils.TCConstants;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.feihutv.zhibofeihu.utils.UiUtil;
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
public class LivePlayInPCPresenter<V extends LivePlayInPCMvpView> extends BasePresenter<V>
        implements LivePlayInPCMvpPresenter<V> {


    @Inject
    public LivePlayInPCPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
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
    public void leaveRoom(final int code) {
        getDataManager()
                .doLoadLeaveRoomApiCall(new LeaveRoomRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LeaveRoomResponce>() {
                    @Override
                    public void accept(@NonNull LeaveRoomResponce response) throws Exception {
                        if(response.getCode()==0){
                            getMvpView().leaveRoomResponce(code);
                        }else{
                            AppLogger.e(response.getErrMsg());
                        }
                    }
                }, getConsumer());
    }

    @Override
    public void loadRoomById(String id) {
        getCompositeDisposable().
                add(getDataManager().
                        doGetRoomDataApiCall(new LoadRoomRequest(id))
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<LoadRoomResponce>() {
                            @Override
                            public void accept(@NonNull final LoadRoomResponce loadRoomResponce) throws Exception {
                                if (loadRoomResponce.getCode() == 0) {
                                    getDataManager()
                                            .doLoadLeaveRoomApiCall(new LeaveRoomRequest())
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Consumer<LeaveRoomResponce>() {
                                                @Override
                                                public void accept(@NonNull LeaveRoomResponce response) throws Exception {
                                                    if(response.getCode()==0){
                                                        getMvpView().gotoRoom(loadRoomResponce.getLoadRoomData());
                                                    }else{
                                                        AppLogger.e("leaveRoom"+response.getErrMsg());
                                                    }
                                                }
                                            }, getConsumer());
                                } else {
                                    AppLogger.e(loadRoomResponce.getErrMsg());
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {

                            }
                        }));
    }

    @Override
    public void showJoinResult(final String name, int issueRound) {
        getCompositeDisposable().add(getDataManager()
                .doGetGameRoundResultDetailApiCall(new GetGameRoundResultDetailRequest(name,issueRound))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetGameRoundResultDetailResponce>() {
                    @Override
                    public void accept(@NonNull GetGameRoundResultDetailResponce responce) throws Exception {
                        if(responce.getCode()==0){
                            List<GetGameRoundResultDetailResponce.GetGameRoundResultDetailData> list=responce.getGetGameRoundResultDetailDatas();
                            if (list != null && list.size() > 0) {
                                showWanfaResultDialog(list, name);
                            }
                        }else{
                            AppLogger.e(responce.getErrMsg());
                        }
                    }
                },getConsumer()));
    }

    @Override
    public void showORJoinResult(final String name, int issueRound) {
        getCompositeDisposable().add(getDataManager()
                .doGetGameRoundResultDetailApiCall(new GetGameRoundResultDetailRequest(name,issueRound))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetGameRoundResultDetailResponce>() {
                    @Override
                    public void accept(@NonNull GetGameRoundResultDetailResponce responce) throws Exception {
                        if(responce.getCode()==0){
                            List<GetGameRoundResultDetailResponce.GetGameRoundResultDetailData> list=responce.getGetGameRoundResultDetailDatas();
                            if (list != null && list.size() > 0) {
                                showOrWanfaResultDialog(list, name);
                            }
                        }else{
                            AppLogger.e(responce.getErrMsg());
                        }
                    }
                },getConsumer()));
    }

    @Override
    public SysConfigBean getSysConfigBean() {
        return getDataManager().getSysConfig();
    }


    private void showWanfaResultDialog(List<GetGameRoundResultDetailResponce.GetGameRoundResultDetailData> list, String name) {
        final Dialog dialog = new Dialog(getMvpView().getPcActivity(), R.style.color_dialog);
        dialog.setContentView(R.layout.wanfa_pc_result);
        dialog.setCanceledOnTouchOutside(true);
        UiUtil.initialize(getMvpView().getPcActivity());
        WindowManager windowManager = getMvpView().getPcActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = dialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();

        lp.width = TCUtils.dip2px(getMvpView().getPcActivity(), 300);
        lp.x = TCUtils.dip2px(getMvpView().getPcActivity(), 20);
        dlgwin.setGravity(Gravity.CENTER);
        dialog.getWindow().setAttributes(lp);
        dialog.show();

        Button button=(Button) dialog.findViewById(R.id.btn_know);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMvpView().notifyWanfaHistory();
                dialog.dismiss();
            }
        });

        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getMvpView().getPcActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new ListViewDecoration());
        WanfaResultAdapter adapter = new WanfaResultAdapter(getMvpView().getPcActivity(), name);
        recyclerView.setAdapter(adapter);
        adapter.setDatas(list);
    }

    private void showOrWanfaResultDialog(List<GetGameRoundResultDetailResponce.GetGameRoundResultDetailData> list, String name) {
        final Dialog dialog = new Dialog(getMvpView().getPcActivity(), R.style.color_dialog);
        dialog.setContentView(R.layout.wanfa_result);

        UiUtil.initialize(getMvpView().getPcActivity());
        WindowManager windowManager =getMvpView().getPcActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = dialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();

        lp.width = (int) (display.getWidth() - TCUtils.dip2px(getMvpView().getPcActivity(), 60)); //设置宽度
        dlgwin.setGravity(Gravity.CENTER);
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        Button button=(Button) dialog.findViewById(R.id.btn_know);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getMvpView().getPcActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new ListViewDecoration());
        WanfaResultAdapter adapter = new WanfaResultAdapter(getMvpView().getPcActivity(), name);
        recyclerView.setAdapter(adapter);
        adapter.setDatas(list);
    }

    @Override
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
                },getConsumer()));
    }

    @Override
    public void loadOtherRooms() {
        getCompositeDisposable().add(getDataManager()
                .doLoadOtherRoomsApiCall(new LoadOtherRoomsRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoadOtherRoomsResponce>() {
                    @Override
                    public void accept(@NonNull LoadOtherRoomsResponce responce) throws Exception {
                        if(responce.getCode()==0){
                            getMvpView().loadOtherRoomsResponce(responce);
                        }else{
                            AppLogger.e(responce.getErrMsg());
                        }
                    }
                },getConsumer())

        );
    }

    @Override
    public void sendLoudspeaker(String msg) {
        getCompositeDisposable().add(getDataManager()
                .doSendLoudSpeakApiCall(new SendLoudSpeakRequest(msg))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SendLoudSpeakResponce>() {
                    @Override
                    public void accept(@NonNull SendLoudSpeakResponce sendLoudSpeakResponce) throws Exception {
                        if(sendLoudSpeakResponce.getCode()==0){
                            getMvpView().onToast("发送全站消息成功");
                        }else{
                            if (sendLoudSpeakResponce.getCode() == 4602) {
                                getMvpView().onToast("您已被禁言");
                            } else if (sendLoudSpeakResponce.getCode() == 4009) {
                                getMvpView().onToast("内容太长");
                            } else if (sendLoudSpeakResponce.getCode() == 4202) {
                                getMvpView().onToast("余额不足");
                            } else if (sendLoudSpeakResponce.getCode() == 4025) {
                                getMvpView().onToast("您已被禁言");
                            }
                            AppLogger.e(sendLoudSpeakResponce.getErrMsg());
                        }
                    }
                },getConsumer()));
    }

    @Override
    public void sendToRoom(final String msg) {
        getDataManager()
                .doSendRoomMsgApiCall(new SendRoomMsgRequest(msg))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SendRoomResponce>() {
                    @Override
                    public void accept(@NonNull SendRoomResponce response) throws Exception {
                        if(response.getCode()==0){
                            if (LiveChatFragment.liveChatFragment != null) {
                                TCChatEntity entity = new TCChatEntity();
                                entity.setSenderName(SharePreferenceUtil.getSession(getMvpView().getPcActivity(),"PREF_KEY_NICKNAME") + "  ");
                                entity.setLevel(SharePreferenceUtil.getSessionInt(getMvpView().getPcActivity(),"PREF_KEY_LEVEL"));
                                entity.setContext(msg);
                                entity.setType(TCConstants.PC_TYPE);
                                entity.setUserId(SharePreferenceUtil.getSession(getMvpView().getPcActivity(),"PREF_KEY_USERID"));
                                LiveChatFragment.liveChatFragment.notifyMsg(entity);
                            }
                        }else{
                            if (response.getCode()== 4025) {
                                getMvpView().onToast("您已被禁言");
                            } else if (response.getCode()== 4009) {
                                getMvpView().onToast("内容太长");
                            } else if (response.getCode() == 4602) {
                                getMvpView().onToast("您已被禁言");
                            }
                            AppLogger.e("joinRoom"+response.getErrMsg());
                        }
                    }
                }, getConsumer());
    }

    @Override
    public void getJackpotCountDown() {
        getCompositeDisposable().add(getDataManager()
                .doJackpotCountDownRequestApiCall(new JackpotCountDownRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JackpotCountDownResponce>() {
                    @Override
                    public void accept(@NonNull JackpotCountDownResponce jackpotCountDownResponce) throws Exception {
                        if(jackpotCountDownResponce.getCode()==0){
                            getMvpView().notifyCaichiCountDown(jackpotCountDownResponce.getJackpotCountDownData());
                        }else{
                            AppLogger.e(jackpotCountDownResponce.getErrMsg());
                        }
                    }
                },getConsumer()));
    }

    @Override
    public void getCurrMount(String roomId) {
        getCompositeDisposable().add(getDataManager()
                .doGetCurrMountApiCall(new GetCurrMountRequest(roomId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetCurrMountResponce>() {
                    @Override
                    public void accept(@NonNull GetCurrMountResponce getCurrMountResponce) throws Exception {
                        if(getCurrMountResponce.getCode()==0){
                            int guardType=getCurrMountResponce.getCurrMountData().getGuardType();
                            if(guardType>0){
                                SharePreferenceUtil.saveSeesionBoolean(FeihuZhiboApplication.getApplication(),"meisGuard",true);
                            }else{
                                SharePreferenceUtil.saveSeesionBoolean(FeihuZhiboApplication.getApplication(),"meisGuard",false);
                            }
                            getMvpView().notifyGetCurrMount(getCurrMountResponce.getCurrMountData().getId());
                        }else{
                            AppLogger.e(getCurrMountResponce.getErrMsg());
                        }
                    }
                },getConsumer()));
    }

    @Override
    public SysMountNewBean getMountBeanByID(String id) {
        return getDataManager().getMountBeanByID(id);
    }

    @Override
    public void showUserInfo(final JoinRoomResponce.JoinRoomData tcRoomInfo,String userId, boolean isPCland, boolean isBangdan,boolean isHost) {
        showUserInfoDialog(tcRoomInfo,userId,isPCland,isBangdan,isHost);
    }

    @Override
    public void care(String userId) {
        getCompositeDisposable().add(getDataManager()
                .doFollowApiCall(new FollowRequest(userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FollowResponce>() {
                    @Override
                    public void accept(@NonNull FollowResponce followResponce) throws Exception {
                       getMvpView().careResponce(followResponce);
                    }
                }, getConsumer()));
    }

    @Override
    public void uncare(String userId) {
        getCompositeDisposable().add(getDataManager()
                .doUnFollowApiCall(new UnFollowRequest(userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UnFollowResponce>() {
                    @Override
                    public void accept(@NonNull UnFollowResponce unFollowResponce) throws Exception {
                      getMvpView().uncareRespoce(unFollowResponce);
                    }
                }, getConsumer()));
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

    @Override
    public void requestBagData() {
        getCompositeDisposable().
                add(getDataManager().
                        doGetUserBagDataApiCall(new BagRequest()).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<BagResponse>() {
                            @Override
                            public void accept(@NonNull BagResponse bagResponse) throws Exception {
                                List<SysbagBean> sysbagBeanList=new ArrayList<SysbagBean>();
                                if (bagResponse.getCode() == 0) {
                                    if (bagResponse.getBagResponseData().size() > 0) {
                                        for (BagResponse.BagResponseData info : bagResponse.getBagResponseData()) {
                                            if(info.getCnt()>0) {
                                                SysbagBean bag = new SysbagBean();
                                                bag.setCnt(info.getCnt());
                                                bag.setId(info.getId());
                                                sysbagBeanList.add(bag);
                                            }
                                        }
                                    }
                                } else {
                                    AppLogger.i(bagResponse.getErrMsg());
                                }
                                getMvpView().showLandGiftDialog(sysbagBeanList);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                            }
                        }));
    }

    @Override
    public void dealBagSendGift(final List<SysbagBean> sysbagBeanList, final int id, final int count) {
        SysGiftNewBean gift = getDataManager().getGiftBeanByID(String.valueOf(id));
        if (gift == null) {
            return;
        }
        if (isExistbag(sysbagBeanList,id)) {
            for (final SysbagBean item : sysbagBeanList) {
                if (item.getId() == id) {
                    if (item.getCnt() < count) {
                        final int needCount = count - item.getCnt();
                        final String gooId=getDataManager().getGoodIDByGiftID(String.valueOf(id));
                        if(!TextUtils.isEmpty(gooId)) {
                            getCompositeDisposable().add(getDataManager()
                                    .doBuyGoodsApiCall(new BuyGoodsRequest(Integer.valueOf(gooId), needCount))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<BuyGoodsResponce>() {
                                        @Override
                                        public void accept(@NonNull BuyGoodsResponce responce) throws Exception {
                                            if (responce.getCode() == 0) {
                                                sendGift(sysbagBeanList, id, count);
                                            } else {
                                                sendGift(sysbagBeanList, id, item.getCnt());
                                                if (responce.getCode() == 4202) {
                                                    getMvpView().onToast("余额不足,请充值");
                                                } else if (responce.getCode() == 4203) {
                                                    getMvpView().onToast("礼物已下架");
                                                }
                                            }
                                        }
                                    }, getConsumer()));
                        }else{
                            getMvpView().onToast("礼物数量不足");
                        }
                    } else {
                        sendGift(sysbagBeanList,id, count);
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void dealSendGift(final List<SysbagBean> sysbagBeanList, final int id, final int count) {
        SysGiftNewBean gift = getDataManager().getGiftBeanByID(String.valueOf(id));
        if (gift == null) {
            return;
        }
        String gooId=getDataManager().getGoodIDByGiftID(String.valueOf(id));
        if (isExistbag(sysbagBeanList,id)) {
            for (final SysbagBean item : sysbagBeanList) {
                if (item.getId() == id) {
                    if (item.getCnt() < count) {
                        int needCount = count - item.getCnt();
                        getCompositeDisposable().add(getDataManager()
                                .doBuyGoodsApiCall(new BuyGoodsRequest(Integer.valueOf(gooId), needCount))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<BuyGoodsResponce>() {
                                    @Override
                                    public void accept(@NonNull BuyGoodsResponce responce) throws Exception {
                                        if(responce.getCode()==0){
                                            sendGift(sysbagBeanList,id, count);
                                        }else{
                                            sendGift(sysbagBeanList,id, item.getCnt());
                                            if (responce.getCode() == 4202) {
                                                getMvpView().onToast("余额不足,请充值");
                                            }else if (responce.getCode()== 4203) {
                                                getMvpView().onToast("礼物已下架");
                                            }
                                        }
                                    }
                                },getConsumer()));
                    } else {
                        sendGift(sysbagBeanList,id, count);
                    }
                    break;
                }
            }
        } else {
            if (gift != null) {
                buyGoods(sysbagBeanList,Integer.valueOf(gooId), id, count);
            }
        }
    }

    @Override
    public List<SysGiftNewBean> getSysGiftNewBean() {
        return getDataManager().getSysGiftNew();
    }

    @Override
    public SysGiftNewBean getGiftBeanByID(String id) {
        return getDataManager().getGiftBeanByID(id);
    }

    public void buyGoods(final List<SysbagBean> sysbagBeanList,int goodId, final int giftid, final int count) {
        getCompositeDisposable().add(getDataManager()
                .doBuyGoodsApiCall(new BuyGoodsRequest(goodId, count))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BuyGoodsResponce>() {
                    @Override
                    public void accept(@NonNull BuyGoodsResponce responce) throws Exception {
                        if(responce.getCode()==0){
                            sendGift(sysbagBeanList,giftid, count);
                        }else{
                            if (responce.getCode() == 4202) {
                                getMvpView().onToast("余额不足,请充值");
                            }else if (responce.getCode()== 4203) {
                                getMvpView().onToast("礼物已下架");
                            }
                            AppLogger.e(responce.getErrMsg());
                        }
                    }
                },getConsumer()));

    }

    public void sendGift(final List<SysbagBean> sysbagBeanList, final int id, final int count) {
        getCompositeDisposable().add(getDataManager()
                .doSendGiftApiCall(new SendGiftRequest(id, count))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SendGiftResponce>() {
                    @Override
                    public void accept(@NonNull SendGiftResponce responce) throws Exception {
                        if(responce.getCode()==0){
                            getMvpView().sendGiftSuccess(sysbagBeanList,id,count);
                        }else{
                            if (responce.getCode() == 4023) {
                                getMvpView().onToast("你已掉线，请重新登录！");
                            } else {
                                getMvpView().onToast("赠送失败");
                            }
                            AppLogger.e(responce.getErrMsg());
                        }
                    }
                },getConsumer()));
    }

    private boolean isExistbag(List<SysbagBean> sysbagBeanList,int id) {
        boolean isExist = false;
        for (SysbagBean item : sysbagBeanList) {
            if (item.getId() == id) {
                isExist = true;
                break;
            }
        }
        return isExist;
    }

    /**
     * 用户信息的dialog
     */
    private LoadOtherUserInfoResponce.OtherUserInfo otherUserInfo;
    private Dialog pickDialog4;
    private void showUserInfoDialog(final JoinRoomResponce.JoinRoomData tcRoomInfo,final String userId, final boolean isPCland, final boolean isBangdan,boolean isHost) {
        if (userId.equals(SharePreferenceUtil.getSession(getMvpView().getPcActivity(),"PREF_KEY_USERID"))) {
            return;
        }
        if (userId.startsWith("g")) {
            return;
        }
        pickDialog4 = new Dialog(getMvpView().getPcActivity(), R.style.color_dialog);
        if (isPCland) {
            pickDialog4.setContentView(R.layout.live_userinfo_land);
        } else {
            pickDialog4.setContentView(R.layout.live_userinfo_pc);
        }


        UiUtil.initialize(getMvpView().getPcActivity());
        WindowManager windowManager = getMvpView().getPcActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog4.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        if (isPCland) {
            dlgwin.setGravity(Gravity.CENTER);
            pickDialog4.getWindow().setAttributes(lp);
        } else {
            lp.width = (int) (display.getWidth() * 0.8); //设置宽度
            dlgwin.setGravity(Gravity.CENTER);
//            lp.y = 110;
            pickDialog4.getWindow().setAttributes(lp);
        }

        FrameLayout top_view = (FrameLayout) pickDialog4.findViewById(R.id.top_view);
        top_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDialog4.dismiss();
            }
        });
        Button tv_report = (Button) pickDialog4.findViewById(R.id.tv_report);
        final TextView tv_focus = (TextView) pickDialog4.findViewById(R.id.tv_focus);
        final LinearLayout ll_focus = (LinearLayout) pickDialog4.findViewById(R.id.ll_focus);
        TextView tv_community = (TextView) pickDialog4.findViewById(R.id.tv_community);
        TextView tv_message = (TextView) pickDialog4.findViewById(R.id.tv_message);
        final ImageView imgAdd = (ImageView) pickDialog4.findViewById(R.id.img_add);
        final Button tv_ban = (Button) pickDialog4.findViewById(R.id.tv_ban);
        TextView tvGuard = (TextView) pickDialog4.findViewById(R.id.tv_guard);
        View viewGuard = (View) pickDialog4.findViewById(R.id.view_guard);
        final TextView tvFans = (TextView) pickDialog4.findViewById(R.id.tv_fans);
        tvGuard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pickDialog4 != null) {
                    pickDialog4.dismiss();
                }
                RxBus.get().send(RxBusCode.RX_BUS_NOTICE_CODE_GUARD);
            }
        });
        if(isHost){
            tvGuard.setVisibility(View.VISIBLE);
            viewGuard.setVisibility(View.VISIBLE);
        }

        if (isBangdan) {
            tv_ban.setVisibility(View.INVISIBLE);
        }
        tv_ban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otherUserInfo != null && !otherUserInfo.isBaned()) {
                    showBanConfirmDialog(tv_ban, userId, "您确定将此人在直播间禁言？",isPCland);
                }
            }
        });
        tv_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pickDialog4 != null) {
                    pickDialog4.dismiss();
                }
                if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                    if (BuildConfig.isForceLoad.equals("1")) {
                        getMvpView().setLoginDialogCount(0);
                        CustomDialogUtils.showQZLoginDialog(getMvpView().getPcActivity(), isPCland);
                        CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                            @Override
                            public void cancleClick() {
                                getMvpView().setLoginDialogCount(1);
                            }

                            @Override
                            public void loginSuccess() {
                                if ( tcRoomInfo!= null) {
                                    getDataManager()
                                            .doLoadJoinRoomSocketApiCall(new JoinRoomRequest(tcRoomInfo.getRoomId(),"0"))
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Consumer<JoinRoomResponce>() {
                                                @Override
                                                public void accept(@NonNull JoinRoomResponce response) throws Exception {
                                                    if(response.getCode()==0){

                                                    }else{
                                                        AppLogger.e("joinRoom"+response.getErrMsg());
                                                    }
                                                }
                                            }, getConsumer());
                                }
                            }
                        });
                    } else {
                        CustomDialogUtils.showLoginDialog(getMvpView().getPcActivity(), isPCland);
                    }
                } else {
                    getMvpView().goReportActivity(tcRoomInfo.getRoomId());

                }
            }
        });
        ll_focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otherUserInfo != null && otherUserInfo.isFollowed()) {
                    MobclickAgent.onEvent(getMvpView().getPcActivity(), "10072");
                    getCompositeDisposable().add(getDataManager()
                            .doUnFollowApiCall(new UnFollowRequest(userId))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<UnFollowResponce>() {
                                @Override
                                public void accept(@NonNull UnFollowResponce unFollowResponce) throws Exception {
                                    if (unFollowResponce.getCode() == 0) {
                                        tv_focus.setText("关注");
                                        imgAdd.setVisibility(View.VISIBLE);
                                        tv_focus.setTextColor(getMvpView().getPcActivity().getResources().getColor(R.color.btn_sure_forbidden));
                                        tv_focus.setSelected(false);
                                        otherUserInfo.setFollowed(false);
                                        if (otherUserInfo.getUserId().equals(tcRoomInfo.getRoomId())) {
                                            getMvpView().followOrUnfollow(false);
                                        }
                                        String followers = "";
                                        otherUserInfo.setFollowers(otherUserInfo.getFollowers()-1);
                                        if (otherUserInfo.getFollowers() >= 10000) {
                                            followers = new BigDecimal((double) otherUserInfo.getFollowers() / 10000).setScale(2, BigDecimal.ROUND_HALF_DOWN) + "万";
                                        } else {
                                            followers = otherUserInfo.getFollowers() + "";
                                        }
                                        tvFans.setText(followers);
                                    } else {
                                        AppLogger.i(unFollowResponce.getErrMsg());
                                    }
                                }
                            }, getConsumer()));
                } else {
                    if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                        if (BuildConfig.isForceLoad.equals("1")) {
                            getMvpView().setLoginDialogCount(0);
                            CustomDialogUtils.showQZLoginDialog(getMvpView().getPcActivity(), isPCland);
                            CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                                @Override
                                public void cancleClick() {
                                    getMvpView().setLoginDialogCount(1);
                                }

                                @Override
                                public void loginSuccess() {
                                    if (tcRoomInfo != null) {
                                        getDataManager()
                                                .doLoadJoinRoomSocketApiCall(new JoinRoomRequest(tcRoomInfo.getRoomId(),"0"))
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(new Consumer<JoinRoomResponce>() {
                                                    @Override
                                                    public void accept(@NonNull JoinRoomResponce response) throws Exception {
                                                        if(response.getCode()==0){

                                                        }else{
                                                            AppLogger.e("joinRoom"+response.getErrMsg());
                                                        }
                                                    }
                                                }, getConsumer());
                                    }
                                }
                            });
                        } else {
                            CustomDialogUtils.showLoginDialog(getMvpView().getPcActivity(), isPCland);
                        }
                    } else {
                        MobclickAgent.onEvent(getMvpView().getPcActivity(), "10073");
                        getCompositeDisposable().add(getDataManager()
                                .doFollowApiCall(new FollowRequest(userId))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<FollowResponce>() {
                                    @Override
                                    public void accept(@NonNull FollowResponce followResponce) throws Exception {
                                        if (followResponce.getCode() == 0) {
                                            tv_focus.setText("已关注");
                                            imgAdd.setVisibility(View.GONE);
                                            tv_focus.setTextColor(getMvpView().getPcActivity().getResources().getColor(R.color.btn_sure_forbidden));
                                            tv_focus.setSelected(true);
                                            otherUserInfo.setFollowed(true);
                                            if (otherUserInfo.getUserId().equals(tcRoomInfo.getRoomId())) {
                                                getMvpView().followOrUnfollow(true);

                                            }
                                            String followers = "";
                                            otherUserInfo.setFollowers(otherUserInfo.getFollowers()+1);
                                            if (otherUserInfo.getFollowers() >= 10000) {
                                                followers = new BigDecimal((double) otherUserInfo.getFollowers() / 10000).setScale(2, BigDecimal.ROUND_HALF_DOWN) + "万";
                                            } else {
                                                followers = otherUserInfo.getFollowers() + "";
                                            }
                                            tvFans.setText(followers);
                                        } else {
                                            AppLogger.i(followResponce.getErrMsg());
                                        }
                                    }
                                }, getConsumer()));
                    }
                }
            }
        });
        tv_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(getMvpView().getPcActivity(), "10054");
                if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                    if (BuildConfig.isForceLoad.equals("1")) {
                        getMvpView().setLoginDialogCount(0);
                        CustomDialogUtils.showQZLoginDialog(getMvpView().getPcActivity(), isPCland);
                        CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                            @Override
                            public void cancleClick() {
                                getMvpView().setLoginDialogCount(1);
                            }

                            @Override
                            public void loginSuccess() {
                                if (tcRoomInfo != null) {
                                    getDataManager()
                                            .doLoadJoinRoomSocketApiCall(new JoinRoomRequest(tcRoomInfo.getRoomId(),"0"))
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Consumer<JoinRoomResponce>() {
                                                @Override
                                                public void accept(@NonNull JoinRoomResponce response) throws Exception {
                                                    if(response.getCode()==0){

                                                    }else{
                                                        AppLogger.e("joinRoom"+response.getErrMsg());
                                                    }
                                                }
                                            }, getConsumer());
                                }
                            }
                        });
                    } else {
                        CustomDialogUtils.showLoginDialog(getMvpView().getPcActivity(), isPCland);
                    }
                } else {
                    if (pickDialog4 != null) {
                        pickDialog4.dismiss();
                    }
                    getMvpView().goOthersCommunityActivity(otherUserInfo.getUserId());
                }
            }
        });
        tv_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 私信
                MobclickAgent.onEvent(getMvpView().getPcActivity(), "10055");

                if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                    if (BuildConfig.isForceLoad.equals("1")) {
                        getMvpView().setLoginDialogCount(0);
                        CustomDialogUtils.showQZLoginDialog(getMvpView().getPcActivity(), isPCland);
                        CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                            @Override
                            public void cancleClick() {
                                getMvpView().setLoginDialogCount(1);

                            }

                            @Override
                            public void loginSuccess() {
                                if (tcRoomInfo != null) {
                                    getDataManager()
                                            .doLoadJoinRoomSocketApiCall(new JoinRoomRequest(tcRoomInfo.getRoomId(),"0"))
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Consumer<JoinRoomResponce>() {
                                                @Override
                                                public void accept(@NonNull JoinRoomResponce response) throws Exception {
                                                    if(response.getCode()==0){

                                                    }else{
                                                        AppLogger.e("joinRoom"+response.getErrMsg());
                                                    }
                                                }
                                            }, getConsumer());
                                }
                            }
                        });
                    } else {
                        CustomDialogUtils.showLoginDialog(getMvpView().getPcActivity(), isPCland);
                    }
                } else {
                    if(otherUserInfo!=null) {
                        if (pickDialog4 != null) {
                            pickDialog4.dismiss();
                        }
                        getMvpView().initDerectMsg(userId, otherUserInfo.getNickName(), otherUserInfo.getHeadUrl(),isPCland);
//                        initDerectMsg(userId, otherUserInfo.getNickName(), otherUserInfo.getHeadUrl(),isPCland);
                    }
                }
            }
        });
        final TextView tv_nickName = (TextView) pickDialog4.findViewById(R.id.tv_name);
        final ImageView imgSex = (ImageView) pickDialog4.findViewById(R.id.img_sex);
        final TextView tvAccountId = (TextView) pickDialog4.findViewById(R.id.tv_accountId);
        final TextView tvSign = (TextView) pickDialog4.findViewById(R.id.tv_live);
        final TextView tvConcern = (TextView) pickDialog4.findViewById(R.id.tv_concern);

        final TextView tvIncome = (TextView) pickDialog4.findViewById(R.id.tv_income);
        final TextView tvExpend = (TextView) pickDialog4.findViewById(R.id.tv_expend);
        final ImageView imgHead = (ImageView) pickDialog4.findViewById(R.id.img_user);
        final ImageView imgLevel = (ImageView) pickDialog4.findViewById(R.id.img_level);
        final ImageView imgVip = (ImageView) pickDialog4.findViewById(R.id.img_vip);
        final ImageView imgGuard = (ImageView) pickDialog4.findViewById(R.id.img_guard);
        final ImageView imgLiang = (ImageView) pickDialog4.findViewById(R.id.img_liang);
        getMvpView().showLoading();
        getDataManager()
                .doLoadOtherUserInfoApiCall(new LoadOtherUserInfoRequest(userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoadOtherUserInfoResponce>() {
                    @Override
                    public void accept(@NonNull LoadOtherUserInfoResponce response) throws Exception {
                        getMvpView().hideLoading();
                        pickDialog4.show();
                        if(response.getCode()==0){
                            otherUserInfo=response.getOtherUserInfo();
                            if (otherUserInfo.isFollowed()) {
                                tv_focus.setText("已关注");
                                tv_focus.setSelected(true);
                                imgAdd.setVisibility(View.GONE);
                                tv_focus.setTextColor(getMvpView().getPcActivity().getResources().getColor(R.color.btn_sure_forbidden));
                            } else {
                                tv_focus.setTextColor(getMvpView().getPcActivity().getResources().getColor(R.color.btn_sure_forbidden));
                                tv_focus.setText("关注");
                                imgAdd.setVisibility(View.VISIBLE);
                                tv_focus.setSelected(false);
                            }
                            if (otherUserInfo.isBaned()) {
                                tv_ban.setText("已禁言");
                            } else {
                                if (tcRoomInfo != null && tcRoomInfo.isRoomMgr()) {
                                    if (tcRoomInfo.getRoomId().equals(otherUserInfo.getUserId())) {
                                        tv_ban.setVisibility(View.INVISIBLE);
                                    } else {
                                        if (otherUserInfo.isRoomMgr()) {
                                            tv_ban.setVisibility(View.INVISIBLE);
                                        } else {
                                            tv_ban.setVisibility(View.VISIBLE);
                                        }
                                    }
                                } else {
                                    tv_ban.setVisibility(View.INVISIBLE);
                                }
                            }
                            if (isBangdan) {
                                tv_ban.setVisibility(View.INVISIBLE);
                            }
                            tv_nickName.setText(otherUserInfo.getNickName());
                            tvAccountId.setText(otherUserInfo.getAccountId() + "");
                            if (!TextUtils.isEmpty(otherUserInfo.getSignature())) {
                                tvSign.setText(otherUserInfo.getSignature());
                            }
                            if (otherUserInfo.getGender() == 1) {
                                imgSex.setVisibility(View.VISIBLE);
                                imgSex.setImageResource(R.drawable.ss_male);
                            } else if (otherUserInfo.getGender() == 2) {
                                imgSex.setVisibility(View.VISIBLE);
                                imgSex.setImageResource(R.drawable.ss_female);
                            } else {
                                imgSex.setVisibility(View.INVISIBLE);
                            }
                            String follows = "";
                            if (otherUserInfo.getFollows() >= 10000) {
                                follows = new BigDecimal((double) otherUserInfo.getFollows() / 10000).setScale(2, BigDecimal.ROUND_HALF_DOWN) + "万";
                            } else {
                                follows = otherUserInfo.getFollows() + "";
                            }
                            tvConcern.setText(follows);
                            String followers = "";
                            if (otherUserInfo.getFollowers() >= 10000) {
                                followers = new BigDecimal((double) otherUserInfo.getFollowers() / 10000).setScale(2, BigDecimal.ROUND_HALF_DOWN) + "万";
                            } else {
                                followers = otherUserInfo.getFollowers() + "";
                            }
                            tvFans.setText(followers);
                            String income = "";
                            if (otherUserInfo.getIncome() >= 10000) {
                                if(otherUserInfo.getIncome()>=100000000){
                                    income = new BigDecimal((double) otherUserInfo.getIncome() / 100000000).setScale(2, BigDecimal.ROUND_HALF_DOWN) + "亿";
                                }else{
                                    income = new BigDecimal((double) otherUserInfo.getIncome() / 10000).setScale(2, BigDecimal.ROUND_HALF_DOWN) + "万";
                                }
                            } else {
                                income = otherUserInfo.getIncome() + "";
                            }
                            tvIncome.setText(income);
                            String contri = "";
                            if (otherUserInfo.getContri() >= 10000) {
                                if(otherUserInfo.getContri()>=100000000){
                                    contri = new BigDecimal((double) otherUserInfo.getContri() / 10000).setScale(2, BigDecimal.ROUND_HALF_DOWN) + "亿";
                                }else {
                                    contri = new BigDecimal((double) otherUserInfo.getContri() / 10000).setScale(2, BigDecimal.ROUND_HALF_DOWN) + "万";
                                }
                            } else {
                                contri = otherUserInfo.getContri() + "";
                            }
                            tvExpend.setText(contri);
                            TCUtils.showLevelWithUrl(getMvpView().getPcActivity(),imgLevel, otherUserInfo.getLevel());
                            Log.e("print", "cbBlock: ------>" + otherUserInfo);
                            TCUtils.showPicWithUrl(getMvpView().getPcActivity(), imgHead, otherUserInfo.getHeadUrl(), R.drawable.face);
                            if(otherUserInfo.getGuardType()==0){
                                imgGuard.setVisibility(View.GONE);
                            }else{
                                if(otherUserInfo.isGuardExpired()){
                                    imgGuard.setVisibility(View.GONE);
                                }else{
                                    tv_ban.setVisibility(View.GONE);
                                    imgGuard.setImageResource(R.drawable.icon_guard);
                                }
                            }

                            if(otherUserInfo.isLiang()){
                                imgLiang.setImageResource(R.drawable.icon_beauti_logo);
                            }else{
                                imgLiang.setVisibility(View.GONE);
                            }
                            if(otherUserInfo.getVip()>0) {
                                if (otherUserInfo.isVipExpired()) {
                                    imgVip.setVisibility(View.GONE);
                                } else {
                                    tv_ban.setVisibility(View.GONE);
                                    String vipIconRootPath = getDataManager().getSysConfigBean().get(0).getCosVipIconRootPath();
                                    Glide.with(getMvpView().getPcActivity()).load(vipIconRootPath + "icon_vip" + otherUserInfo.getVip() + ".png").into(imgVip);
                                }
                            }else{
                                imgVip.setVisibility(View.GONE);
                            }
                        }else{
                            AppLogger.e(response.getErrMsg());
                        }
                    }
                }, getConsumer());


    }


    private void showBanConfirmDialog(final TextView tvBan, final String userId, String text,boolean isPcLand) {
        final Dialog pickDialog2 = new Dialog(getMvpView().getPcActivity(), R.style.color_dialog);
        if(isPcLand){
            pickDialog2.setContentView(R.layout.pop_kaibo_land);
        }else{
            pickDialog2.setContentView(R.layout.pop_kaibo);
        }


        WindowManager windowManager = getMvpView().getPcActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog2.getWindow();
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.CENTER);
        lp.width = (int) (display.getWidth()); //设置宽度

        pickDialog2.getWindow().setAttributes(lp);
        Button button = (Button) pickDialog2.findViewById(R.id.btn_pop_renzheng);
        button.setText("确定");
        TextView tv_pop = (TextView) pickDialog2.findViewById(R.id.tv_pop);
        tv_pop.setText(text);
        TextView tv_cancle = (TextView) pickDialog2.findViewById(R.id.tv_cancle);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCompositeDisposable().
                        add(getDataManager().
                                doBanApiCall(new BanRequest(userId))
                                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<BanResponce>() {
                                    @Override
                                    public void accept(@NonNull BanResponce banResponce) throws Exception {
                                        if (banResponce.getCode() == 0) {
                                            tvBan.setText("已禁言");
                                            getMvpView().onToast("禁言成功");
                                            tvBan.setEnabled(false);
                                            pickDialog2.dismiss();
                                        } else {
                                            if (banResponce.getCode() == 4024) {
                                                getMvpView().onToast("你还不是场控哦！");
                                            }
                                            AppLogger.e(banResponce.getErrMsg());
                                        }
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(@NonNull Throwable throwable) throws Exception {

                                    }
                                }));
            }
            });

        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDialog2.dismiss();
            }
        });
        pickDialog2.setCanceledOnTouchOutside(false);
        pickDialog2.show();
    }

    /**
     * 点击用户的私信
     *
     * @param sendId
     * @param nickName
     * @param headUrl
     */
    DerectMsgDialog derectMsgDialog;

    private void initDerectMsg(String sendId, String nickName, String headUrl,boolean isPcLand) {
        derectMsgDialog = new DerectMsgDialog(getMvpView().getPcActivity(), sendId, nickName, headUrl,true);
        Window dlgwin = derectMsgDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        dlgwin.setGravity(Gravity.BOTTOM);
        dlgwin.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        UiUtil.initialize(getMvpView().getPcActivity());
        int screenWidth = UiUtil.getScreenWidth();
        int screenHeight = UiUtil.getScreenHeight();
        lp.width = screenWidth;
        if(isPcLand){
            lp.height = (int) (screenHeight -TCUtils.dip2px(getMvpView().getPcActivity(),100));
        }else{
            lp.height = (int) (screenHeight -TCUtils.dip2px(getMvpView().getPcActivity(),270));
        }
        dlgwin.setAttributes(lp);
        derectMsgDialog.show();
        derectMsgDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                getMvpView().DerectMsgDialogDissMiss();
            }
        });
    }
}
