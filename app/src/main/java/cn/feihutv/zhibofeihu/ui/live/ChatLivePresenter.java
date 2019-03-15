package cn.feihutv.zhibofeihu.ui.live;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
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
import cn.feihutv.zhibofeihu.data.local.model.TCChatEntity;
import cn.feihutv.zhibofeihu.data.network.http.model.common.FollowRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.FollowResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomContriRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomContriResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.UnFollowRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.UnFollowResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.home.LoadRoomListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.home.LoadRoomListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.BanRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.BanResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.CancelRoomMgrRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.CancelRoomMgrResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.SendLoudSpeakRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.SendLoudSpeakResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.SetRoomMgrRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.SetRoomMgrResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GamePreemptRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GamePreemptResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameRoundResultDetailRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameRoundResultDetailResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameStatusRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameStatusResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.JackpotCountDownRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.JackpotCountDownResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.common.SendRoomMsgRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.common.SendRoomResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.GetLiveStatusRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.GetLiveStatusResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.JoinRoomRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.JoinRoomResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.LoadOtherUserInfoRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.LoadOtherUserInfoResponce;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.rxbus.RxBusSubscribe;
import cn.feihutv.zhibofeihu.rxbus.ThreadMode;
import cn.feihutv.zhibofeihu.ui.live.ChatLiveMvpView;
import cn.feihutv.zhibofeihu.ui.live.ChatLiveMvpPresenter;
import cn.feihutv.zhibofeihu.ui.base.BasePresenter;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.DataManager;

import com.androidnetworking.error.ANError;
import com.bumptech.glide.Glide;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import cn.feihutv.zhibofeihu.ui.widget.dialog.DerectMsgDialog;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.CustomDialogUtils;
import cn.feihutv.zhibofeihu.utils.FileUtils;
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
public class ChatLivePresenter<V extends ChatLiveMvpView> extends BasePresenter<V>
        implements ChatLiveMvpPresenter<V> {


    @Inject
    public ChatLivePresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
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
    public void showUserInfo(String id) {
        showUserInfoDialog(id);
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
                        if (response.getCode() == 0) {
                            getMvpView().notifyChatMsg(tcChatEntity);
                        } else {
                            if (response.getCode() == 4025) {
                                getMvpView().onToast("您已被禁言");
                            } else if (response.getCode() == 4009) {
                                getMvpView().onToast("内容太长");
                            } else if (response.getCode() == 4602) {
                                getMvpView().onToast("您已被禁言");
                            }
                            AppLogger.e("joinRoom" + response.getErrMsg());
                        }
                    }
                }, getConsumer());
    }

    @Override
    public void loadRoomContriList(String userId, int rankType) {
        getCompositeDisposable().add(getDataManager()
                .doLoadRoomContriApiCall(new LoadRoomContriRequest(userId, rankType))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoadRoomContriResponce>() {
                    @Override
                    public void accept(@NonNull LoadRoomContriResponce loadRoomContriResponce) throws Exception {
                        if (loadRoomContriResponce.getCode() == 0) {
                            getMvpView().notifyContriList(loadRoomContriResponce.getRoomContriDataList());
                        } else {
                            AppLogger.e(loadRoomContriResponce.getErrMsg());
                        }
                    }
                }, getConsumer()));

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
                        if (jackpotCountDownResponce.getCode() == 0) {
                            getMvpView().notifyCaichiCountDown(jackpotCountDownResponce.getJackpotCountDownData());
                        } else {
                            AppLogger.e(jackpotCountDownResponce.getErrMsg());
                        }
                    }
                }, getConsumer()));
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
                        if (sendLoudSpeakResponce.getCode() == 0) {
                            getMvpView().onToast("发送全站消息成功");
                        } else {
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
                }, getConsumer()));
    }

    @Override
    public SysMountNewBean getMountBeanByID(String id) {
        return getDataManager().getMountBeanByID(id);
    }

    @Override
    public SysGiftNewBean getGiftBeanByID(String id) {
        return getDataManager().getGiftBeanByID(id);
    }

    @Override
    public void getLiveStatus() {
        getDataManager()
                .doGetLiveStatusApiCall(new GetLiveStatusRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetLiveStatusResponce>() {
                    @Override
                    public void accept(@NonNull GetLiveStatusResponce response) throws Exception {
                        if (response.getCode() == 0) {
                            getMvpView().notifyLiveState(response.isLiveStatus());
                        } else {
                            getMvpView().notifyLiveState(true);
                            AppLogger.e("GetLiveStatus" + response.getErrMsg());
                        }
                    }
                }, getConsumer());
    }

    @Override
    public void getGameStatus(final String name, final boolean showStyleDialog) {
        getCompositeDisposable().add(getDataManager()
                .doGetGameStatusApiCall(new GetGameStatusRequest(name))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetGameStatusResponce>() {
                    @Override
                    public void accept(@NonNull GetGameStatusResponce getGameStatusResponce) throws Exception {
                        if (getGameStatusResponce.getCode() == 0) {
                            getMvpView().notifyGameState(getGameStatusResponce.getGameStatusData(), name, showStyleDialog);
                        } else {
                            AppLogger.e(getGameStatusResponce.getErrMsg());
                        }
                    }
                }, getConsumer()));
    }

    @Override
    public void showJoinResult(final String name, int issueRound) {
        getCompositeDisposable().add(getDataManager()
                .doGetGameRoundResultDetailApiCall(new GetGameRoundResultDetailRequest(name, issueRound))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetGameRoundResultDetailResponce>() {
                    @Override
                    public void accept(@NonNull GetGameRoundResultDetailResponce getGameRoundResultDetailResponce) throws Exception {
                        if (getGameRoundResultDetailResponce.getCode() == 0) {
                            getMvpView().getJoinResult(getGameRoundResultDetailResponce, name);
                        } else {
                            AppLogger.e(getGameRoundResultDetailResponce.getErrMsg());
                        }
                    }
                }, getConsumer()));
    }

    @Override
    public void getGameStatus2(final String name) {
        getCompositeDisposable().add(getDataManager()
                .doGetGameStatusApiCall(new GetGameStatusRequest(name))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetGameStatusResponce>() {
                    @Override
                    public void accept(@NonNull GetGameStatusResponce getGameStatusResponce) throws Exception {
                        if (getGameStatusResponce.getCode() == 0) {
                            getMvpView().notifyGameState2(getGameStatusResponce.getGameStatusData(), name);
                        } else {
                            AppLogger.e(getGameStatusResponce.getErrMsg());
                        }
                    }
                }, getConsumer()));
    }

    @Override
    public void gamePreempt(final String name, int openStyle) {
        getCompositeDisposable().add(getDataManager()
                .doGamePreemptApiCall(new GamePreemptRequest(name, openStyle))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GamePreemptResponce>() {
                    @Override
                    public void accept(@NonNull GamePreemptResponce gamePreemptResponce) throws Exception {
                        if (gamePreemptResponce.getCode() == 0) {
                            getMvpView().notifyGamePreempt(gamePreemptResponce, name, true);
                        } else {
                            getMvpView().notifyGamePreempt(gamePreemptResponce, name, false);
                        }
                    }
                }, getConsumer())

        );

    }



    /**
     * 用户信息的dialog
     */
    private LoadOtherUserInfoResponce.OtherUserInfo otherUserInfo;
    private Button tv_ban;
    private TextView tv_focus;
    private ImageView imgAdd;
    private void showUserInfoDialog(final String userId) {
        if (userId.equals(SharePreferenceUtil.getSession(getMvpView().getChatContext(), "PREF_KEY_USERID"))) {
            return;
        }
        if (userId.startsWith("g")) {
            return;
        }
        final Dialog pickDialog4 = new Dialog(getMvpView().getChatContext(), R.style.color_dialog);
        pickDialog4.setContentView(R.layout.live_userinfo);

        UiUtil.initialize(getMvpView().getChatContext());
        WindowManager windowManager = getMvpView().getChatActivity().getWindowManager();
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
        tv_focus = (TextView) pickDialog4.findViewById(R.id.tv_focus);
        final LinearLayout ll_focus = (LinearLayout) pickDialog4.findViewById(R.id.ll_focus);
        final TextView tv_community = (TextView) pickDialog4.findViewById(R.id.tv_community);
        final TextView tvFans = (TextView) pickDialog4.findViewById(R.id.tv_fans);
        imgAdd = (ImageView) pickDialog4.findViewById(R.id.img_add);
        TextView tv_message = (TextView) pickDialog4.findViewById(R.id.tv_message);
        tv_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMvpView().goReportActivity(userId);
            }
        });
        ll_focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(getMvpView().getChatContext(), "10072");
                if (otherUserInfo != null && otherUserInfo.isFollowed()) {
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
                                        tv_focus.setTextColor(getMvpView().getChatActivity().getResources().getColor(R.color.btn_sure_forbidden));
                                        otherUserInfo.setFollowed(false);
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
                            CustomDialogUtils.showQZLoginDialog(getMvpView().getChatActivity(), false);
                        } else {
                            CustomDialogUtils.showLoginDialog(getMvpView().getChatActivity(), false);
                        }

                    } else {
                        MobclickAgent.onEvent(getMvpView().getChatContext(), "10073");
                        getCompositeDisposable().add(getDataManager()
                                .doFollowApiCall(new FollowRequest(userId))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<FollowResponce>() {
                                    @Override
                                    public void accept(@NonNull FollowResponce followResponce) throws Exception {
                                        if (followResponce.getCode() == 0) {
                                            tv_focus.setText("已关注");

                                            String followers = "";
                                            otherUserInfo.setFollowers(otherUserInfo.getFollowers()+1);
                                            if (otherUserInfo.getFollowers() >= 10000) {
                                                followers = new BigDecimal((double) otherUserInfo.getFollowers() / 10000).setScale(2, BigDecimal.ROUND_HALF_DOWN) + "万";
                                            } else {
                                                followers = otherUserInfo.getFollowers() + "";
                                            }
                                            tvFans.setText(followers);
                                            imgAdd.setVisibility(View.GONE);
                                            tv_focus.setTextColor(getMvpView().getChatActivity().getResources().getColor(R.color.btn_sure_forbidden));
                                            otherUserInfo.setFollowed(true);
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
                getMvpView().goOthersCommunityActivity(userId);
            }
        });
        tv_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MobclickAgent.onEvent(getMvpView().getChatContext(), "10055");
                getMvpView().initDerectMsg(userId, otherUserInfo.getNickName(), otherUserInfo.getHeadUrl());
            }
        });
        final TextView tv_nickName = (TextView) pickDialog4.findViewById(R.id.tv_name);
        final ImageView imgSex = (ImageView) pickDialog4.findViewById(R.id.img_sex);
        tv_ban = (Button) pickDialog4.findViewById(R.id.tv_ban);

        View view_manger = (View) pickDialog4.findViewById(R.id.view_manger);
        final TextView tv_setmanger = (TextView) pickDialog4.findViewById(R.id.tv_setmanger);

        if (!userId.equals(SharePreferenceUtil.getSession(getMvpView().getChatContext(), "PREF_KEY_USERID"))) {
            view_manger.setVisibility(View.VISIBLE);
            tv_setmanger.setVisibility(View.VISIBLE);
        }
        tv_setmanger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //是场控
                if (otherUserInfo != null && otherUserInfo.isRoomMgr()) {
                    pickDialog4.dismiss();
                    showConfirmDialog(tv_setmanger, userId, "您确定取消该场控吗？", false);
                } else {
                    pickDialog4.dismiss();
                    showConfirmDialog(tv_setmanger, userId, "您确定将此人设置为场控？", true);
                }
            }
        });

        tv_ban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otherUserInfo != null && !otherUserInfo.isBaned()) {
                    showBanConfirmDialog(userId, "您确定将此人禁言？");
                }
            }
        });

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
                        if (response.getCode() == 0) {
                            otherUserInfo = response.getOtherUserInfo();
                            if (otherUserInfo.isFollowed()) {
                                tv_focus.setText("已关注");
                                imgAdd.setVisibility(View.GONE);
                                tv_focus.setTextColor(getMvpView().getChatActivity().getResources().getColor(R.color.btn_sure_forbidden));
                            } else {
                                tv_focus.setTextColor(getMvpView().getChatActivity().getResources().getColor(R.color.btn_sure_forbidden));
                                tv_focus.setText("关注");
                                imgAdd.setVisibility(View.VISIBLE);
                            }
                            if (otherUserInfo.isBaned()) {
                                tv_ban.setText("已禁言");
                                tv_ban.setEnabled(false);
                            }
                            if (otherUserInfo.isRoomMgr()) {
                                tv_setmanger.setText("取消场控");
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
                            TCUtils.showLevelWithUrl(getMvpView().getChatContext(), imgLevel, otherUserInfo.getLevel());
                            TCUtils.showPicWithUrl(getMvpView().getChatContext(), imgHead, otherUserInfo.getHeadUrl(), R.drawable.face);
                            if (otherUserInfo.getGuardType() == 0) {
                                imgGuard.setVisibility(View.GONE);
                            } else {
                                if (otherUserInfo.isGuardExpired()) {
                                    imgGuard.setVisibility(View.GONE);
                                } else {
                                    tv_ban.setVisibility(View.GONE);
                                    imgGuard.setImageResource(R.drawable.icon_guard);
                                }
                            }

                            if (otherUserInfo.isLiang()) {
                                imgLiang.setImageResource(R.drawable.icon_beauti_logo);
                            } else {
                                imgLiang.setVisibility(View.GONE);
                            }
                            if (otherUserInfo.getVip() > 0) {
                                if (otherUserInfo.isVipExpired()) {
                                    imgVip.setVisibility(View.GONE);
                                } else {
                                    tv_ban.setVisibility(View.GONE);
                                    String vipIconRootPath = getDataManager().getSysConfigBean().get(0).getCosVipIconRootPath();
                                    Glide.with(getMvpView().getChatContext()).load(vipIconRootPath + "icon_vip" + otherUserInfo.getVip() + ".png").into(imgVip);
                                }
                            } else {
                                imgVip.setVisibility(View.GONE);
                            }
                            pickDialog4.show();
                        } else {
                            AppLogger.e(response.getErrMsg());
                        }
                    }
                }, getConsumer());
    }

    @Override
    public void notifyCare(boolean havaCare, String userID) {
        if(tv_focus!=null&&otherUserInfo!=null){
            if(userID.equals(otherUserInfo.getUserId())){
                if(havaCare){
                    tv_focus.setText("已关注");
                    imgAdd.setVisibility(View.GONE);
                    otherUserInfo.setFollowed(true);
                }else{
                    tv_focus.setText("关注");
                    imgAdd.setVisibility(View.VISIBLE);
                    otherUserInfo.setFollowed(false);
                }
            }
        }
    }

    private void showBanConfirmDialog(final String userId, String text) {
        final Dialog pickDialog2 = new Dialog(getMvpView().getChatContext(), R.style.color_dialog);
        pickDialog2.setContentView(R.layout.pop_kaibo);

        WindowManager windowManager = getMvpView().getChatActivity().getWindowManager();
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
                                            pickDialog2.dismiss();
                                            getMvpView().onToast("禁言成功");
                                            if (tv_ban != null) {
                                                tv_ban.setText("已禁言");
                                                tv_ban.setEnabled(false);
                                            }
                                        } else {
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

    private void showConfirmDialog(final TextView textView, final String userId, final String text, final boolean isSetManger) {
        final Dialog pickDialog2 = new Dialog(getMvpView().getChatContext(), R.style.color_dialog);
        pickDialog2.setContentView(R.layout.pop_kaibo);

        WindowManager windowManager = getMvpView().getChatActivity().getWindowManager();
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
                if (isSetManger) {
                    getCompositeDisposable().
                            add(getDataManager().
                                    doSetRoomMgrApiCall(new SetRoomMgrRequest(userId))
                                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<SetRoomMgrResponce>() {
                                        @Override
                                        public void accept(@NonNull SetRoomMgrResponce setRoomMgrResponce) throws Exception {
                                            if (setRoomMgrResponce.getCode() == 0) {
                                                if (textView != null) {
                                                    textView.setText("取消场控");
                                                }
                                                if (otherUserInfo != null) {
                                                    otherUserInfo.setRoomMgr(true);
                                                }
                                                getMvpView().onToast("设置成功");
                                                pickDialog2.dismiss();
                                            } else {
                                                if (setRoomMgrResponce.getCode() == 4026) {
                                                    getMvpView().onToast("场控数量已满");
                                                } else {
                                                    getMvpView().onToast("设置失败");
                                                }
                                                AppLogger.e(setRoomMgrResponce.getErrMsg());
                                            }
                                        }
                                    }, new Consumer<Throwable>() {
                                        @Override
                                        public void accept(@NonNull Throwable throwable) throws Exception {

                                        }
                                    }));
                } else {
                    getCompositeDisposable().
                            add(getDataManager().
                                    doCancelRoomMgrCall(new CancelRoomMgrRequest(userId))
                                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<CancelRoomMgrResponce>() {
                                        @Override
                                        public void accept(@NonNull CancelRoomMgrResponce cancelRoomMgrResponce) throws Exception {
                                            if (cancelRoomMgrResponce.getCode() == 0) {
                                                if (textView != null) {
                                                    textView.setText("设置场控");
                                                }
                                                if (otherUserInfo != null) {
                                                    otherUserInfo.setRoomMgr(false);
                                                }
                                                getMvpView().onToast("取消成功");
                                                pickDialog2.dismiss();
                                            } else {
                                                getMvpView().onToast("取消失败");
                                                AppLogger.e(cancelRoomMgrResponce.getErrMsg());
                                            }
                                        }
                                    }, new Consumer<Throwable>() {
                                        @Override
                                        public void accept(@NonNull Throwable throwable) throws Exception {

                                        }
                                    }));
                }
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
