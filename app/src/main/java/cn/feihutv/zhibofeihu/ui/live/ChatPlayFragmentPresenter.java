package cn.feihutv.zhibofeihu.ui.live;

import android.app.Dialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import cn.feihutv.zhibofeihu.data.db.model.SysConfigBean;
import cn.feihutv.zhibofeihu.data.db.model.SysGiftNewBean;
import cn.feihutv.zhibofeihu.data.db.model.SysMountNewBean;
import cn.feihutv.zhibofeihu.data.local.model.SysbagBean;
import cn.feihutv.zhibofeihu.data.local.model.TCChatEntity;
import cn.feihutv.zhibofeihu.data.network.http.model.common.BuyGoodsRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.BuyGoodsResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.FollowRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.FollowResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomContriRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomContriResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomResponce;
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
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameListResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameRoundResultDetailRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameRoundResultDetailResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameStatusRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameStatusResponce;
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
import cn.feihutv.zhibofeihu.ui.base.BasePresenter;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.DataManager;

import com.bumptech.glide.Glide;
import com.umeng.analytics.MobclickAgent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cn.feihutv.zhibofeihu.ui.live.adapter.WanfaResultAdapter;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.CustomDialogUtils;
import cn.feihutv.zhibofeihu.utils.ListViewDecoration;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
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
public class ChatPlayFragmentPresenter<V extends ChatPlayFragmentMvpView> extends BasePresenter<V>
        implements ChatPlayFragmentMvpPresenter<V> {


    @Inject
    public ChatPlayFragmentPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public List<SysGiftNewBean> getSysGiftNewBean() {
        return getDataManager().getSysGiftNew();
    }

    @Override
    public SysConfigBean getSysConfigBean() {
        return getDataManager().getSysConfig();
    }

    @Override
    public void joinRoom(String roomId) {
        getDataManager()
                .doLoadJoinRoomSocketApiCall(new JoinRoomRequest(roomId,"0"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JoinRoomResponce>() {
                    @Override
                    public void accept(@NonNull JoinRoomResponce response) throws Exception {

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
    public void showUserInfo(JoinRoomResponce.JoinRoomData roomData,String id, boolean isBangdan,boolean isHost) {
        showUserInfoDialog(roomData,id,isBangdan,isHost);
    }

    @Override
    public void loadRoomContriList(String userId, int rankType) {
        getCompositeDisposable().add(getDataManager()
                .doLoadRoomContriApiCall(new LoadRoomContriRequest(userId,rankType))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoadRoomContriResponce>() {
                    @Override
                    public void accept(@NonNull LoadRoomContriResponce loadRoomContriResponce) throws Exception {
                        if(loadRoomContriResponce.getCode()==0){
                            getMvpView().notifyContriList(loadRoomContriResponce.getRoomContriDataList());
                        }else{
                            AppLogger.e(loadRoomContriResponce.getErrMsg());
                        }
                    }
                },getConsumer()));

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
                            getMvpView().notifyGetCurrMount(getCurrMountResponce.getCurrMountData());
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
    public void unFollowed(String id) {
        getCompositeDisposable().add(getDataManager()
                .doUnFollowApiCall(new UnFollowRequest(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UnFollowResponce>() {
                    @Override
                    public void accept(@NonNull UnFollowResponce unFollowResponce) throws Exception {
                       getMvpView().notifyUncareResponce(unFollowResponce);
                    }
                }, getConsumer()));
    }

    @Override
    public void Followed(String id) {
        getCompositeDisposable().add(getDataManager()
                .doFollowApiCall(new FollowRequest(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FollowResponce>() {
                    @Override
                    public void accept(@NonNull FollowResponce followResponce) throws Exception {
                        getMvpView().notifyFollowResponce(followResponce);
                    }
                }, getConsumer()));
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
                                getMvpView().showGiftDialog(sysbagBeanList);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                            }
                        }));
    }

    @Override
    public SysGiftNewBean getGiftBeanByID(String id) {
        return getDataManager().getGiftBeanByID(id);
    }

    @Override
    public String getGoodIDByGiftID(String id) {
        return getDataManager().getGoodIDByGiftID(id);
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
    public void getGameList(String usetId) {
         getCompositeDisposable().add(getDataManager()
                         .doGetGameListApiCall(new GetGameListRequest(usetId))
                         .subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribe(new Consumer<GetGameListResponce>() {
                             @Override
                             public void accept(@NonNull GetGameListResponce responce) throws Exception {
                                    if(responce.getCode()==0){
                                        getMvpView().getGameListSuccess(responce);
                                    }else{
                                        AppLogger.e(responce.getErrMsg());
                                    }
                             }
                         },getConsumer())

                 );

    }

    @Override
    public void kaiqiang(final String style) {
         getCompositeDisposable().add(getDataManager()
                         .doGetGameStatusApiCall(new GetGameStatusRequest(style))
                         .subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribe(new Consumer<GetGameStatusResponce>() {
                             @Override
                             public void accept(@NonNull GetGameStatusResponce responce) throws Exception {
                                    getMvpView().kaiqiangResponce(style,responce);
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

    private void showWanfaResultDialog(List<GetGameRoundResultDetailResponce.GetGameRoundResultDetailData> list, String name) {
        final Dialog dialog = new Dialog(getMvpView().getChatPlayContext(), R.style.user_dialog);
        dialog.setContentView(R.layout.wanfa_result);

        UiUtil.initialize(getMvpView().getChatPlayContext());
        WindowManager windowManager = getMvpView().getChatPlayActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = dialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();

        lp.width = (int) (display.getWidth() - TCUtils.dip2px(getMvpView().getChatPlayContext(), 50)); //设置宽度
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
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getMvpView().getChatPlayContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new ListViewDecoration(1));
        WanfaResultAdapter adapter = new WanfaResultAdapter(getMvpView().getChatPlayContext(), name);
        recyclerView.setAdapter(adapter);
        adapter.setDatas(list);
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

    @Override
    public void sendRoomMsg(final TCChatEntity tcChatEntity, String msg) {
        getDataManager()
                .doSendRoomMsgApiCall(new SendRoomMsgRequest(msg))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SendRoomResponce>() {
                    @Override
                    public void accept(@NonNull SendRoomResponce response) throws Exception {
                        if(response.getCode()==0){
                            getMvpView().notifyChatMsg(tcChatEntity);
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

    /**
     * 用户信息的dialog
     */
    private LoadOtherUserInfoResponce.OtherUserInfo otherUserInfo;

    private void showUserInfoDialog(final JoinRoomResponce.JoinRoomData tcRoomInfo, final String userId, boolean isBangdan,boolean isHost) {
        if (userId.equals(SharePreferenceUtil.getSession(getMvpView().getChatPlayContext(),"PREF_KEY_USERID"))) {
            return;
        }
        if (userId.startsWith("g")) {
            return;
        }
        final Dialog pickDialog4 = new Dialog(getMvpView().getChatPlayContext(), R.style.color_dialog);
        pickDialog4.setContentView(R.layout.live_userinfo);

        UiUtil.initialize(getMvpView().getChatPlayContext());
        WindowManager windowManager = getMvpView().getChatPlayActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog4.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();

        lp.width = (int) (display.getWidth()); //设置宽度
        dlgwin.setGravity(Gravity.BOTTOM);
        pickDialog4.getWindow().setAttributes(lp);
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
        final TextView tvGuard = (TextView) pickDialog4.findViewById(R.id.tv_guard);
        final View viewGuard = (View) pickDialog4.findViewById(R.id.view_guard);
        final TextView tvFans = (TextView) pickDialog4.findViewById(R.id.tv_fans);
        tvGuard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        tv_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                    if (BuildConfig.isForceLoad.equals("1")) {
                        getMvpView().setLoginDialogCount(0);
                        CustomDialogUtils.showQZLoginDialog(getMvpView().getChatPlayActivity(), false);
                        CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                            @Override
                            public void cancleClick() {
                                getMvpView().setLoginDialogCount(1);
                            }

                            @Override
                            public void loginSuccess() {
                                if(tcRoomInfo!=null) {
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
                        CustomDialogUtils.showLoginDialog(getMvpView().getChatPlayActivity(), false);
                    }
                } else {
                    getMvpView().goReportActivity(userId);
                }
            }
        });
        ll_focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otherUserInfo != null && otherUserInfo.isFollowed()) {
                    MobclickAgent.onEvent(getMvpView().getChatPlayContext(), "10072");
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
                                        tv_focus.setTextColor(getMvpView().getChatPlayContext().getResources().getColor(R.color.btn_sure_forbidden));
                                        tv_focus.setSelected(false);
                                        otherUserInfo.setFollowed(false);
                                        if (otherUserInfo.getUserId().equals(tcRoomInfo.getRoomId())) {
                                            getMvpView().getConcernImage().setVisibility(View.VISIBLE);
                                            getMvpView().getConcernImage().setImageResource(R.drawable.icon_focus);
                                            tcRoomInfo.getMasterDataList().setFollowed(false);
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
                            CustomDialogUtils.showQZLoginDialog(getMvpView().getChatPlayActivity(), false);
                            CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                                @Override
                                public void cancleClick() {
                                    getMvpView().setLoginDialogCount(1);
                                }

                                @Override
                                public void loginSuccess() {
                                    if(tcRoomInfo!=null) {
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
                            CustomDialogUtils.showLoginDialog(getMvpView().getChatPlayActivity(), false);
                        }
                    } else {
                        MobclickAgent.onEvent(getMvpView().getChatPlayContext(), "10073");
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
                                            tv_focus.setTextColor(getMvpView().getChatPlayContext().getResources().getColor(R.color.btn_sure_forbidden));
                                            tv_focus.setSelected(true);
                                            otherUserInfo.setFollowed(true);
                                            if (otherUserInfo.getUserId().equals(tcRoomInfo.getRoomId())) {
                                                getMvpView().getConcernImage().setVisibility(View.GONE);
                                                tcRoomInfo.getMasterDataList().setFollowed(true);
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
        tv_ban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otherUserInfo != null && !otherUserInfo.isBaned()) {
                    showBanConfirmDialog(tv_ban, userId, "您确定将此人在直播间禁言？");
                }
            }
        });
        tv_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(getMvpView().getChatPlayContext(), "10054");
                if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                    if (BuildConfig.isForceLoad.equals("1")) {
                        getMvpView().setLoginDialogCount(0);
                        CustomDialogUtils.showQZLoginDialog(getMvpView().getChatPlayActivity(), false);
                        CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                            @Override
                            public void cancleClick() {
                                getMvpView().setLoginDialogCount(1);
                            }

                            @Override
                            public void loginSuccess() {
                                if(tcRoomInfo!=null) {
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
                        CustomDialogUtils.showLoginDialog(getMvpView().getChatPlayActivity(), false);
                    }
                } else {
                    getMvpView().goOthersCommunityActivity(userId);
                }
            }
        });
        tv_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 点击用户详情里的私信
                MobclickAgent.onEvent(getMvpView().getChatPlayContext(), "10055");
                if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                    if (BuildConfig.isForceLoad.equals("1")) {
                        getMvpView().setLoginDialogCount(0);
                        CustomDialogUtils.showQZLoginDialog(getMvpView().getChatPlayActivity(), false);
                        CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                            @Override
                            public void cancleClick() {
                                getMvpView().setLoginDialogCount(1);
                            }

                            @Override
                            public void loginSuccess() {
                                if(tcRoomInfo!=null) {
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
                        CustomDialogUtils.showLoginDialog(getMvpView().getChatPlayActivity(), false);
                    }
                } else {
                    getMvpView().initDerectMsg(userId, otherUserInfo.getNickName(), otherUserInfo.getHeadUrl());
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
                        pickDialog4.show();
                        getMvpView().hideLoading();
                        if(response.getCode()==0){
                            otherUserInfo=response.getOtherUserInfo();
                            if (otherUserInfo.isFollowed()) {
                                tv_focus.setText("已关注");
                                tv_focus.setSelected(true);
                                imgAdd.setVisibility(View.GONE);
                                tv_focus.setTextColor(getMvpView().getChatPlayContext().getResources().getColor(R.color.btn_sure_forbidden));
                            } else {
                                tv_focus.setTextColor(getMvpView().getChatPlayContext().getResources().getColor(R.color.btn_sure_forbidden));
                                tv_focus.setText("关注");
                                imgAdd.setVisibility(View.VISIBLE);
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

                            tv_nickName.setText(otherUserInfo.getNickName());
                            tvAccountId.setText(otherUserInfo.getAccountId() + "");
                            tvSign.setText(otherUserInfo.getSignature());
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
                                income = new BigDecimal((double) otherUserInfo.getIncome() / 10000).setScale(2, BigDecimal.ROUND_HALF_DOWN) + "万";
                            } else {
                                income = otherUserInfo.getIncome() + "";
                            }
                            tvIncome.setText(income);
                            String contri = "";
                            if (otherUserInfo.getContri() >= 10000) {
                                contri = new BigDecimal((double) otherUserInfo.getContri() / 10000).setScale(2, BigDecimal.ROUND_HALF_DOWN) + "万";
                            } else {
                                contri = otherUserInfo.getContri() + "";
                            }
                            tvExpend.setText(contri);

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
                                    Glide.with(getMvpView().getChatPlayContext()).load(vipIconRootPath + "icon_vip" + otherUserInfo.getVip() + ".png").into(imgVip);
                                }
                            }else{
                                imgVip.setVisibility(View.GONE);
                            }
                            TCUtils.showLevelWithUrl(getMvpView().getChatPlayContext(),imgLevel,otherUserInfo.getLevel());
                            TCUtils.showPicWithUrl(getMvpView().getChatPlayContext(), imgHead, otherUserInfo.getHeadUrl(), R.drawable.face);
                        }else{
                            AppLogger.e(response.getErrMsg());
                        }
                    }
                }, getConsumer());
    }

    private void showBanConfirmDialog(final TextView tvBan, final String userId, String text) {
        final Dialog pickDialog2 = new Dialog(getMvpView().getChatPlayContext(), R.style.floag_dialog);
        pickDialog2.setContentView(R.layout.pop_kaibo);

        WindowManager windowManager = getMvpView().getChatPlayActivity().getWindowManager();
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


}
