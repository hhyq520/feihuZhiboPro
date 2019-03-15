package cn.feihutv.zhibofeihu.ui.live.pclive;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chinanetcenter.StreamPusher.sdk.SPManager;
import com.cnc.mediaplayer.sdk.lib.event.AuthEvent;
import com.cnc.mediaplayer.sdk.lib.event.GeneralEvent;
import com.cnc.mediaplayer.sdk.lib.event.OnStatusCodeEventListener;
import com.cnc.mediaplayer.sdk.lib.event.PlayEvent;
import com.cnc.mediaplayer.sdk.lib.renderview.IRenderView;
import com.cnc.mediaplayer.sdk.lib.videoview.IMediaEventsListener;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.BuildConfig;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.db.model.SysConfigBean;
import cn.feihutv.zhibofeihu.data.db.model.SysGiftNewBean;
import cn.feihutv.zhibofeihu.data.db.model.SysMountNewBean;
import cn.feihutv.zhibofeihu.data.local.SysdataHelper;
import cn.feihutv.zhibofeihu.data.local.model.SysbagBean;
import cn.feihutv.zhibofeihu.data.local.model.TCChatEntity;
import cn.feihutv.zhibofeihu.data.network.http.model.common.FollowResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadMsgListResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.UnFollowResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.GetExtGameIconRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.GetExtGameIconResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.LoadOtherRoomsResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameStatusRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameStatusResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.JackpotCountDownResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.common.NetworkChangePush;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.JoinRoomResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.LeaveRoomRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.LeaveRoomResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.AllRoomMsgPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.BannedJoinRoomPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.GameResultPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.JackpotGiftCountdownPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.NoticeJoinRoomPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.NoticeLeaveRoomPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.RoomBanPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.RoomChatPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.RoomFollowPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.RoomGiftPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.RoomIncomePush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.RoomMgrPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.UpdatePlayStatusPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.UpdatePlayUrlPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.ValueChangePush;
import cn.feihutv.zhibofeihu.rxbus.RxBus;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.rxbus.RxBusSubscribe;
import cn.feihutv.zhibofeihu.rxbus.ThreadMode;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.live.OnItemClickListener;
import cn.feihutv.zhibofeihu.ui.live.OnStrClickListener;
import cn.feihutv.zhibofeihu.ui.live.ReportActivity;
import cn.feihutv.zhibofeihu.ui.live.models.GiftModel;
import cn.feihutv.zhibofeihu.ui.live.models.TabEntity;
import cn.feihutv.zhibofeihu.ui.me.dynamic.OthersCommunityActivity;
import cn.feihutv.zhibofeihu.ui.me.recharge.RechargeActivity;
import cn.feihutv.zhibofeihu.ui.me.vip.MyVipActivity;
import cn.feihutv.zhibofeihu.ui.me.vip.QuickUpgradeActivity;
import cn.feihutv.zhibofeihu.ui.mv.demand.PostDemandActivity;
import cn.feihutv.zhibofeihu.ui.widget.CNCVideoViewEx;
import cn.feihutv.zhibofeihu.ui.widget.DanmuBase.DanmuModel;
import cn.feihutv.zhibofeihu.ui.widget.GuardDialogFragment;
import cn.feihutv.zhibofeihu.ui.widget.LivePlayInPCLandView;
import cn.feihutv.zhibofeihu.ui.widget.PlayLandJincaiDialog;
import cn.feihutv.zhibofeihu.ui.widget.bsrgift.BSRGiftLayout;
import cn.feihutv.zhibofeihu.ui.widget.bsrgift.GiftAnmManager;
import cn.feihutv.zhibofeihu.ui.widget.dialog.CaichiDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.DerectMsgDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.GiftLandDialogView;
import cn.feihutv.zhibofeihu.ui.widget.dialog.GiftNumDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.LandHuDongDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.LandJcXiaZhuDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.LyLandHuDongDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.LyLandXiaZhuDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.MessageDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.PlaylandShakeDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.TCInputTextMsgDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.UserDialogFragment;
import cn.feihutv.zhibofeihu.ui.widget.flashview.FlashDataParser;
import cn.feihutv.zhibofeihu.ui.widget.flashview.FlashView;
import cn.feihutv.zhibofeihu.ui.widget.pathView.ViewPoint;
import cn.feihutv.zhibofeihu.utils.AlterWindowUtil;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.AppUtils;
import cn.feihutv.zhibofeihu.utils.CustomDialogUtils;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.FileUtils;
import cn.feihutv.zhibofeihu.utils.FloatViewWindowManger;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import cn.feihutv.zhibofeihu.utils.ShareUtil;
import cn.feihutv.zhibofeihu.utils.TCConstants;
import cn.feihutv.zhibofeihu.utils.TCDanmuMgr;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.feihutv.zhibofeihu.utils.UiUtil;
import cn.feihutv.zhibofeihu.utils.weiget.dialog.RxDialogSureCancel;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * <pre>
 *     author : huang hao
 *     time   :
 *     desc   : 界面绘制
 *     version: 1.0
 * </pre>
 */
public class LivePlayInPCActivity extends BaseActivity implements LivePlayInPCMvpView, OnStatusCodeEventListener, IMediaEventsListener, TCInputTextMsgDialog.OnTextSendListener {

    @Inject
    LivePlayInPCMvpPresenter<LivePlayInPCMvpView> mPresenter;
    @BindView(R.id.pc_view)
    View pcView;
    @BindView(R.id.btn_full)
    ImageView imgFull;
    @BindView(R.id.tl_liveplay)
    CommonTabLayout tabLayout;
    @BindView(R.id.vp_liveplay)
    ViewPager viewPager;
    @BindView(R.id.ctl_btnSwitch)
    ImageView ctl_btnSwitch;
    //    @BindView(R.id.tv_member_counts)
//    TextView tv_member_counts;
    @BindView(R.id.tv_broadcasting_time)
    TextView tv_name;
    @BindView(R.id.video_view)
    CNCVideoViewEx videoViewEx;
    @BindView(R.id.video_frm)
    FrameLayout videoFrm;
    @BindView(R.id.tv_pc_account)
    TextView tv_play_account;
    @BindView(R.id.up_view)
    RelativeLayout up_view;
    @BindView(R.id.rl_controllLayer_land)
    LivePlayInPCLandView land_view;
    @BindView(R.id.mHeadIcon)
    ImageView mHeadIcon;
    @BindView(R.id.img_live_exit)
    ImageView img_live_exit;
    @BindView(R.id.btn_share)
    ImageView btn_share;
    @BindView(R.id.rl_controllLayer)
    RelativeLayout rlControllLayer;
    @BindView(R.id.recommend_head_image_left)
    ImageView recommendHeadImageLeft;
    @BindView(R.id.recommend_nickname_left)
    TextView recommendNicknameLeft;
    @BindView(R.id.recommend_count_left)
    TextView recommendCountLeft;
    @BindView(R.id.recommend_head_image_right)
    ImageView recommendHeadImageRight;
    @BindView(R.id.recommend_nickname_right)
    TextView recommendNicknameRight;
    @BindView(R.id.recommend_count_right)
    TextView recommendCountRight;
    @BindView(R.id.loading_view_rel)
    RelativeLayout loading_view_rel;
    @BindView(R.id.recommend_view)
    RelativeLayout recommendView;
    @BindView(R.id.flashview)
    FlashView flashview;
    @BindView(R.id.img_t1)
    ImageView imgT1;
    @BindView(R.id.img_t2)
    ImageView imgT2;
    @BindView(R.id.img_t3)
    ImageView imgT3;
    @BindView(R.id.img_t4)
    ImageView imgT4;
    @BindView(R.id.caichi_time_left)
    LinearLayout caichiTimeLeft;
    @BindView(R.id.text_caichi_open)
    TextView textCaichiOpen;
    @BindView(R.id.rec_left)
    LinearLayout recLeft;
    @BindView(R.id.rec_right)
    LinearLayout recRight;
    @BindView(R.id.cover_img)
    ImageView coverImg;
    @BindView(R.id.loading_view)
    FlashView loadingView;
    @BindView(R.id.ll_tuijian)
    LinearLayout linearLayout;
    @BindView(R.id.tv_member)
    TextView countMember;
    @BindView(R.id.zuojia_img)
    ImageView zuojiaImg;
    @BindView(R.id.gift_layout)
    BSRGiftLayout giftLayout;

    private Handler mOnStatusCodeEventHandler;
    private String roomId;
    private String headImgUrl;
    private String mPlayUrl;
    private JoinRoomResponce.JoinRoomData roomInfo;
    private Timer messageTimer;
    private MessageTimerTask messageTimerTask;
    private Timer chatMessageTimer;
    private ChatMessageTimerTask chatMessageTimerTask;
    private Timer staticGiftTimer;
    private StaticGiftTimerTask giftTimerTask;
    private Timer mGiftTimer;
    private GiftTimerTask mGiftTimerTask;
    private Timer mCaichiTimer;

    List<LoadOtherRoomsResponce.OtherRoomData> datas = new ArrayList<>();
    private String userId;  // 主播的id
    private String accountId;  //飞虎号
    private boolean haveCare;
    private String mPusherNickname;
    private boolean isGood = false;
    private List<String> titles;
    private TCInputTextMsgDialog mInputTextMsgDialog;
    private int caichicountDown;
    private List<SysGiftNewBean> animGiftQune = new ArrayList<>();
    private int loginDialogCount = 1;
    private boolean mIsActivityPaused = true;
    public static int OVERLAY_PERMISSION_REQ_CODE = 1234;
    private boolean showWindow = true;
    private int mCurrentMemberCount;
    private List<AllRoomMsgPush> allRoomMessage = new ArrayList<>();
    private List<RoomChatPush> allChatMessage = new ArrayList<>();
    private List<GiftModel> staticGiftModels = new ArrayList<>();
    private List<GiftModel> vipGiftModels = new ArrayList<>();
    private int LyresultCount = 0;
    private int JcresultCount = 0;
    private String gameName1;
    private int issueRound;
    private String jcGameResultJson;
    private String lyGameResultJson;
    private int yxjcIssueRound;
    private int lyjcIssueRound;
    private boolean falshViewisEnd = true;
    private int giftCheck = 0;
    private LuckyPoolFragment luckyPoolFragment;
    private MessageInPlayFragment messageInPlayFragment;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    /**
     * 标识网络已恢复
     */
    protected boolean mConnectionRecover = false;
    protected String mToastText = "";
    protected long mLastShowToastTime = 0;
    protected boolean mConnectionError;
    protected final int CONNECTION_INIT = -2;
    protected int mLastConnectionType = CONNECTION_INIT;

    private final int SHOW_MESSAGE = 1;
    private final int SHOW_CHAT_MESSAGE = 2;
    private final int STATICGIFTSHOW = 3;
    private final int GIFT_SHOW = 4;
    private final int CAICHI_OPEN = 5;
    private boolean isNeedGuard = false;
    private boolean isFromLand = false;
    private String weekStarUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private class MessageTimerTask extends TimerTask {
        public void run() {
            dealNews(SHOW_MESSAGE);
        }
    }

    private class ChatMessageTimerTask extends TimerTask {
        public void run() {
            dealNews(SHOW_CHAT_MESSAGE);
        }
    }

    private class StaticGiftTimerTask extends TimerTask {
        public void run() {
            dealNews(STATICGIFTSHOW);
        }
    }

    private class GiftTimerTask extends TimerTask {
        public void run() {
            dealNews(GIFT_SHOW);
        }
    }

    private class CaichiTimerTask extends TimerTask {
        public void run() {
            dealNews(CAICHI_OPEN);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_live_play_in_pc;
    }

    @Override
    protected void beforeSet() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void initWidget() {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this));

        //persenter调用初始化
        mPresenter.onAttach(LivePlayInPCActivity.this);

        SharePreferenceUtil.saveSeesionBoolean(FeihuZhiboApplication.getApplication(), "meisGuard", false);
        initView();
    }


    private boolean zuojiaIsEnd = true;

    public void zuoJiaAnimation() {
        GiftModel model = vipGiftModels.remove(0);
        SysMountNewBean mountNewBeanBean = mPresenter.getMountBeanByID(String.valueOf(model.getGiftid()));
        if (mountNewBeanBean != null) {
            if (FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().size() > 0) {
                String url = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosMountRootPath();
                RequestBuilder<Bitmap> req = Glide.with(LivePlayInPCActivity.this).asBitmap().apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL));
                req.load(url + "/" + mountNewBeanBean.getIcon()).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        giftAnmManager.showCarTwo(resource);
                    }
                });
            }
        }
    }

    public void setFabLoc(ViewPoint newLoc) {
        zuojiaImg.setTranslationX(newLoc.x);
        zuojiaImg.setTranslationY(newLoc.y);
    }

    private void dealNews(final int what) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(what);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        switch (integer) {
                            case SHOW_MESSAGE:
                                showAllRoomMessage();
                                break;
                            case SHOW_CHAT_MESSAGE:
                                showChatMessage();
                                break;
                            case STATICGIFTSHOW:
                                showStaticGiftShow();
                                break;
                            case GIFT_SHOW:
                                showGift();
                                break;
                            case CAICHI_OPEN:
                                caichiOpen();
                                break;
                            default:
                                break;
                        }
                    }
                });
    }

    private void caichiOpen() {
        if (land_view != null) {
            land_view.setTimeLeftVis(true);
        }
        if (caichiTimeLeft == null) {
            return;
        }
        if (caichiTimeLeft != null) {
            caichiTimeLeft.setVisibility(View.VISIBLE);
        }
        if (caichicountDown == 0 || caichicountDown > 600) {
            if (caichiTimeLeft != null) {
                caichiTimeLeft.setVisibility(View.GONE);
            }
            if (land_view != null) {
                land_view.setTimeLeftVis(false);
            }
        } else {
            caichicountDown--;
            if (caichicountDown <= 60) {
                imgT1.setImageResource(R.drawable.t0);
                imgT2.setImageResource(R.drawable.t0);
                land_view.setT1(0);
                land_view.setT2(0);
                if (caichicountDown < 10) {
                    imgT3.setImageResource(R.drawable.t0);
                    setT(imgT4, caichicountDown);
                    land_view.setT3(0);
                    land_view.setT4(caichicountDown);
                } else {
                    int b = caichicountDown / 10;
                    int a = caichicountDown % 10;
                    setT(imgT3, b);
                    setT(imgT4, a);
                    land_view.setT3(b);
                    land_view.setT4(a);
                }
            } else {
                int min = caichicountDown / 60;
                int sec = caichicountDown % 60;
                int b = min / 10;
                int a = min % 10;
                int c = sec / 10;
                int d = sec % 10;
                setT(imgT1, b);
                setT(imgT2, a);
                setT(imgT3, c);
                setT(imgT4, d);
                land_view.setT3(c);
                land_view.setT4(d);
                land_view.setT1(b);
                land_view.setT2(a);
            }
        }
    }

    private void setT(ImageView img, int count) {
        switch (count) {
            case 0:
                img.setImageResource(R.drawable.t0);
                break;
            case 1:
                img.setImageResource(R.drawable.t1);
                break;
            case 2:
                img.setImageResource(R.drawable.t2);
                break;
            case 3:
                img.setImageResource(R.drawable.t3);
                break;
            case 4:
                img.setImageResource(R.drawable.t4);
                break;
            case 5:
                img.setImageResource(R.drawable.t5);
                break;
            case 6:
                img.setImageResource(R.drawable.t6);
                break;
            case 7:
                img.setImageResource(R.drawable.t7);
                break;
            case 8:
                img.setImageResource(R.drawable.t8);
                break;
            case 9:
                img.setImageResource(R.drawable.t9);
                break;
        }
    }

    private void showGift() {
        if (JcresultCount > 0) {
            JcresultCount++;
//            if (gameName1.equals("yxjc")) {
            if (JcresultCount > 15) {
                if (yxjcIssueRound != 0) {
                    if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                        JcresultCount = 0;
                        mPresenter.showJoinResult("yxjc", yxjcIssueRound);
                    } else if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                        JcresultCount = 0;
                        mPresenter.showORJoinResult("yxjc", yxjcIssueRound);
                    }
                }
//                }
            }
        }
        if (LyresultCount > 0) {
            LyresultCount++;
//            if (gameName1.equals("lyzb")) {
            if (LyresultCount > 17) {
                if (lyjcIssueRound != 0) {
                    if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                        LyresultCount = 0;
                        mPresenter.showJoinResult("lyzb", lyjcIssueRound);
                    } else if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                        LyresultCount = 0;
                        mPresenter.showORJoinResult("lyzb", lyjcIssueRound);
                    }
                }
            }
//            }
        }

//        if (resultCount > 0) {
//            resultCount++;
//            if (gameName1.equals("yxjc")) {
//                if (resultCount > 15) {
//                    if (issueRound != 0) {
//                        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//                            resultCount = 0;
//                            mPresenter.showJoinResult(gameName1, issueRound);
//                        } else if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
//                            resultCount = 0;
//                            mPresenter.showORJoinResult(gameName1, issueRound);
//                        }
//                    }
//                }
//            } else {
//                if (resultCount > 17) {
//                    if (issueRound != 0) {
//                        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//                            resultCount = 0;
//                            mPresenter.showJoinResult(gameName1, issueRound);
//                        } else if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
//                            resultCount = 0;
//                            mPresenter.showORJoinResult(gameName1, issueRound);
//                        }
//                    }
//                }
//            }
//        }

        if (loginDialogCount > 0) {
            loginDialogCount++;
        }
        if (BuildConfig.isForceLoad.equals("1")) {
            if (loginDialogCount == 31) {
                loginDialogCount = 0;
                if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {

                    if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                        CustomDialogUtils.showQZLoginDialog(LivePlayInPCActivity.this, true);
                    } else {
                        CustomDialogUtils.showQZLoginDialog(LivePlayInPCActivity.this, false);
                    }
                    CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                        @Override
                        public void cancleClick() {
                            loginDialogCount = 1;
                        }

                        @Override
                        public void loginSuccess() {
                            if (roomId != null) {
                                mPresenter.joinRoom(roomId, "0", false);
                            }
                        }
                    });
                }
            }
        }


        if (!falshViewisEnd) {
            giftCheck++;
        }
        if (giftCheck == 7) {
            falshViewisEnd = true;
        }
        if (vipGiftModels.size() > 0 && zuojiaIsEnd) {
            zuoJiaAnimation();
        }

        if (animGiftQune != null && animGiftQune.size() > 0 && falshViewisEnd) {
            final SysGiftNewBean giftBean = animGiftQune.remove(0);
            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                // 如果是竖排,则改为横排
                flashview.setVisibility(View.VISIBLE);
            } else if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                // 如果是横排,则改为竖排
                if (currentPos == 0) {
                    if (flashview.getVisibility() != View.VISIBLE) {
                        flashview.setVisibility(View.VISIBLE);
                    }
                } else {
                    flashview.setVisibility(View.GONE);
                }
            }
            List<SysConfigBean> sysConfigBeanList = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean();
            final SysConfigBean sysConfigBean = sysConfigBeanList.get(0);
            if(sysConfigBean==null){
                return;
            }
            if (giftBean.is520()) {
                flashview.reload("cucumber520", "flashAnims");
                String iconname = "";
                if (giftBean.getEnableVip() == 1 && giftBean.isVip()) {
                    iconname = giftBean.getId() + "_vip.png";
                } else {
                    iconname = giftBean.getId() + ".png";
                }
                final String olddownUrl520 = sysConfigBean.getCosGiftTemplate520();
                final String picFile520=olddownUrl520.substring(olddownUrl520.lastIndexOf("/") + 1,olddownUrl520.length() - 4);
                String fileName = FlashDataParser.getExternalStorageDirectory(LivePlayInPCActivity.this) + "/"+picFile520+"/" + iconname;
                Bitmap bitmap = FileUtils.readBitmap565FromFile(fileName);
                if (bitmap != null) {
                    flashview.replaceBitmap("520_1.png", bitmap);
                    flashview.play("cucumber520", FlashDataParser.FlashLoopTimeOnce);
                } else {
                    falshViewisEnd = true;
                    flashview.setVisibility(View.GONE);
                    flashview.stop();
                    giftCheck = 0;
                }
            } else if (giftBean.is1314()) {
                flashview.reload("cucumber1314", "flashAnims");
                String iconname = "";
                if (giftBean.getEnableVip() == 1 && giftBean.isVip()) {
                    iconname = giftBean.getId() + "_vip.png";
                } else {
                    iconname = giftBean.getId() + ".png";
                }
                final String olddownPicUrl1314 = sysConfigBean.getCosGiftTemplate1314();
                final String picFile1314=olddownPicUrl1314.substring(olddownPicUrl1314.lastIndexOf("/") + 1,olddownPicUrl1314.length() - 4);
                String fileName = FlashDataParser.getExternalStorageDirectory(LivePlayInPCActivity.this) + "/"+picFile1314+"/" + iconname;
                Bitmap bitmap = FileUtils.readBitmap565FromFile(fileName);
                if (bitmap != null) {
                    flashview.replaceBitmap("1314_1.png", getDefaultBitmap(bitmap));
                    flashview.play("cucumber1314", FlashDataParser.FlashLoopTimeOnce);
                } else {
                    falshViewisEnd = true;
                    flashview.setVisibility(View.GONE);
                    flashview.stop();
                    giftCheck = 0;
                }
            } else {
                String animName = giftBean.getAnimName().split("_")[0];
                if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    if (giftBean.isMount()) {
                        flashview.reload(animName, "mountLandFlashAnim");
                    } else {
                        flashview.reload(animName, "landFlash");
                    }
                } else if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    if (giftBean.isMount()) {
                        flashview.reload(animName, "mountFlashAnim");
                    } else {
                        flashview.reload(animName, "flashAnims");
                    }
                }
                flashview.play(animName, FlashDataParser.FlashLoopTimeOnce);
            }

            falshViewisEnd = false;
            flashview.setEventCallback(new FlashDataParser.IFlashViewEventCallback() {
                @Override
                public void onEvent(FlashDataParser.FlashViewEvent e, FlashDataParser.FlashViewEventData data) {
                    if (e.equals(FlashDataParser.FlashViewEvent.ONELOOPEND)) {
                        falshViewisEnd = true;
                        flashview.setVisibility(View.GONE);
                        flashview.stop();
                        giftCheck = 0;
                    }
                }
            });
        }
    }

    private Bitmap getDefaultBitmap(Bitmap bitmap) {
        int px = TCUtils.dip2px(this, 120);
        Bitmap mDefauleBitmap = null;
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix matrix = new Matrix();
            matrix.postScale(((float) px) / width, ((float) px) / height);
            mDefauleBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        }
        return mDefauleBitmap;
    }

    private Bitmap getDefaultBitmap520(Bitmap bitmap) {
        int px = TCUtils.dip2px(this, 20);
        Bitmap mDefauleBitmap = null;
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix matrix = new Matrix();
            matrix.postScale(((float) px) / width, ((float) px) / height);
            mDefauleBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        }
        return mDefauleBitmap;
    }

    private void showStaticGiftShow() {
        if (staticGiftModels.size() >= 2) {
            GiftModel giftModel = staticGiftModels.get(0);
            if (land_view != null)
                land_view.showGift(giftModel.getTag(), giftModel.getCount(), giftModel.getGiftname(),
                        giftModel.getUserName(), giftModel.getHeadUrl(), giftModel.getGiftid(), giftModel.isVip());
            staticGiftModels.remove(giftModel);
        } else if (staticGiftModels.size() > 0) {
            GiftModel giftModel = staticGiftModels.remove(0);
            land_view.showGift(giftModel.getTag(), giftModel.getCount(), giftModel.getGiftname(),
                    giftModel.getUserName(), giftModel.getHeadUrl(), giftModel.getGiftid(), giftModel.isVip());
        }
    }

    private void showChatMessage() {
        if (allChatMessage != null && allChatMessage.size() > 0) {
            RoomChatPush chatPush = allChatMessage.remove(0);
            String message = chatPush.getMsg();
            String userId = chatPush.getSender().getUserId();
            String nickname = chatPush.getSender().getNickName();
            int level = chatPush.getSender().getLevel();
            if (land_view != null && land_view.getVisibility() == View.VISIBLE) {
                TCChatEntity entity = new TCChatEntity();
                entity.setSenderName("" + nickname + "  ");
                entity.setContext(message);
                entity.setUserId(userId);
                entity.setType(TCConstants.TEXT_TYPE);
                entity.setLevel(level);
                entity.setGuardType(chatPush.getSender().getGuardType());
                entity.setGuardExpired(chatPush.getSender().isGuardExpired());
                entity.setVip(chatPush.getSender().getVip());
                entity.setVipExpired(chatPush.getSender().isVipExpired());
                entity.setAccountId(chatPush.getSender().getShowId());
                entity.setLiang(chatPush.getSender().isLiang());
                land_view.reciveMessage(entity);
            }
        }
    }


    private void showAllRoomMessage() {
        if (allRoomMessage != null && allRoomMessage.size() > 0) {
            AllRoomMsgPush pushData = allRoomMessage.remove(0);
            String msg10 = pushData.getMsg();
            String msgType = pushData.getMsgName();
            if (land_view != null && land_view.getVisibility() == View.VISIBLE) {
                TCDanmuMgr mDanmuMgr = land_view.getmDanmuMgr();
                if (mDanmuMgr != null) {
                    if (msgType.equals("Sys")) {
                        mDanmuMgr.addSysDanmu(msg10);
                        return;
                    }
                }
                String masterName = pushData.getMasterName();
                String roomName = pushData.getRoomName();
                String roomId = pushData.getRoomId();
                int giftId = pushData.getGiftId();
                int giftCnt = pushData.getGiftCnt();
                if (mDanmuMgr != null) {
                    if (msgType.equals("CountDown5min")) {
                        if (AppUtils.judegeShowWanfa()) {
                            mDanmuMgr.addHudongDanmu(msg10, 3);
                        }
                    } else if (msgType.equals("CountDown60s")) {
                        if (AppUtils.judegeShowWanfa()) {
                            mDanmuMgr.addHudongDanmu(msg10, 1);
                        }
                    } else if (msgType.equals("GameStartRound60s")) {
                        if (AppUtils.judegeShowWanfa()) {
                            mDanmuMgr.addGameStartRound60s();
                        }
                    } else {
                        String nickname = pushData.getSender().getNickName();
                        String headUrl = pushData.getSender().getHeadUrl();
                        int level = pushData.getSender().getLevel();
                        if (land_view != null && land_view.getVisibility() == View.VISIBLE) {
                            if (mDanmuMgr != null) {
                                if (msgType.equals("GameMsg")) {
                                    if (AppUtils.judegeShowWanfa()) {
                                        mDanmuMgr.addGameDanmu(msg10, masterName, roomId);
                                    }
                                } else if (msgType.equals("GaoShePao")) {
                                    mDanmuMgr.addDaPaoDanmu(masterName, nickname, roomId);
                                } else if (msgType.equals("Loudspeaker")) {
                                    if (roomId.equals(roomInfo.getRoomId())) {
                                        mDanmuMgr.addLabaDanmu(msg10, nickname, masterName, true, roomId);
                                    } else {
                                        mDanmuMgr.addLabaDanmu(msg10, nickname, masterName, false, roomId);
                                    }
                                } else if (msgType.equals("LargeQuantityGift")) {
                                    if ((pushData.getSender().getVip() > 0 && !pushData.getSender().isVipExpired()) || (pushData.getSender().getGuardType() > 0 && !pushData.getSender().isGuardExpired())) {
                                        mDanmuMgr.addGiftDanmu(nickname, masterName, giftId, giftCnt, false, roomId, true);
                                    } else {
                                        mDanmuMgr.addGiftDanmu(nickname, masterName, giftId, giftCnt, false, roomId, false);
                                    }
                                } else if (msgType.equals("Money300")) {
                                    mDanmuMgr.addMoney300Danmu(nickname, masterName, giftId, giftCnt, roomId);
                                } else if (msgType.equals("Money180")) {
                                    if (roomId.equals(roomInfo.getRoomId())) {
                                        if ((pushData.getSender().getVip() > 0 && !pushData.getSender().isVipExpired()) || (pushData.getSender().getGuardType() > 0 && !pushData.getSender().isGuardExpired())) {
                                            mDanmuMgr.addGiftDanmu(nickname, masterName, giftId, giftCnt, true, roomId, true);
                                        } else {
                                            mDanmuMgr.addGiftDanmu(nickname, masterName, giftId, giftCnt, true, roomId, false);
                                        }
                                    }
                                } else if (msgType.equals("CCAward")) {
                                    mDanmuMgr.addCCAwardDanmu(msg10, nickname, giftId);
                                } else if (msgType.equals("GameWin")) {
                                    if (AppUtils.judegeShowWanfa()) {
                                        mDanmuMgr.addGamewinDanmu(msg10, nickname);
                                    }
                                } else if (msgType.equals("BuyVIP")) {
                                    DanmuModel model = new DanmuModel(headUrl, level,
                                            "恭喜" + nickname + "开通VIP，成为全平台的贵宾！", pushData.getSender().getVip(),
                                            pushData.getSender().isVipExpired(), pushData.getSender().getGuardType(),
                                            pushData.getSender().isGuardExpired(), pushData.getSender().isLiang(), pushData.getSender().getShowId());
                                    if (land_view != null && land_view.getVisibility() == View.VISIBLE) {
                                        land_view.setVipDanmu(model);
                                    }
                                    mDanmuMgr.addVipSuccess(nickname);
                                } else if (msgType.equals("BuyGuard")) {
                                    DanmuModel model = new DanmuModel(headUrl, level,
                                            nickname + "成功守护主播，果然是真爱啊~", pushData.getSender().getVip(),
                                            pushData.getSender().isVipExpired(), pushData.getSender().getGuardType(),
                                            pushData.getSender().isGuardExpired(), pushData.getSender().isLiang(), pushData.getSender().getShowId());
                                    if (land_view != null && land_view.getVisibility() == View.VISIBLE) {
                                        land_view.setVipDanmu(model);
                                    }
                                    mDanmuMgr.addGuardSuccess(nickname, masterName);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private GiftAnmManager giftAnmManager;

    private void initView() {
        Intent intent = getIntent();
        roomId = intent.getStringExtra("room_id");
        headImgUrl = intent.getStringExtra("headUrl");
        isNeedGuard = intent.getBooleanExtra("isNeedGuard", false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Glide.with(this).load(headImgUrl)
                    .apply(new RequestOptions().dontAnimate().transform(new BlurTransformation(this, 1, 5)).placeholder(R.drawable.land_cover).diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .into(coverImg);
        }
        giftAnmManager = new GiftAnmManager(giftLayout, this);
        loadingView.setVisibility(View.VISIBLE);
        loadingView.reload("loading", "flashAnims");
        loadingView.play("loading", FlashDataParser.FlashLoopTimeForever);
        loadingView.setScale(0.25f, 0.25f);
        if (!isNetworkConnected()) {
            onToast("当前无网络连接，请检查网络！");
            return;
        }
        if (roomId != null) {
            mPresenter.joinRoom(roomId, "0", true);
        }
        flashview.setIsPc();
    }


    @Override
    protected void onDestroy() {
        //销毁之前必须调用
        mPresenter.onDetach();
        if (mOnStatusCodeEventHandler != null) {
            mOnStatusCodeEventHandler.removeCallbacksAndMessages(null);
            mOnStatusCodeEventHandler = null;
        }
        if (loadingView != null) {
            loadingView.stop();
        }
        if (land_view != null) {
            land_view.onDestory();
        }
        if (videoViewEx != null) {
            videoViewEx.onDestroy();
        }
        if (mGiftTimer != null) {
            mGiftTimer.cancel();
            mGiftTimer = null;
        }
        if (null != mGiftTimerTask) {
            mGiftTimerTask.cancel();
            mGiftTimerTask = null;
        }
        if (mCaichiTimer != null) {
            mCaichiTimer.cancel();
            mCaichiTimer = null;
        }

        if (caichiTimerTask != null) {
            caichiTimerTask.cancel();
            caichiTimerTask = null;
        }
        if (messageTimer != null) {
            messageTimer.cancel();
            messageTimer = null;
        }
        if (messageTimerTask != null) {
            messageTimerTask.cancel();
            messageTimerTask = null;
        }
        if (chatMessageTimer != null) {
            chatMessageTimer.cancel();
            chatMessageTimer = null;
        }
        if (chatMessageTimerTask != null) {
            chatMessageTimerTask.cancel();
            chatMessageTimerTask = null;
        }

        if (animGiftQune.size() > 0) {
            animGiftQune.clear();
        }

        allRoomMessage.clear();
        releaseData();
        if (staticGiftTimer != null) {
            staticGiftTimer.cancel();
        }
        if (giftTimerTask != null) {
            giftTimerTask.cancel();
        }
        if (staticGiftModels.size() > 0) {
            staticGiftModels.clear();
        }

        if (liveChatFragment != null) {
            liveChatFragment.cancelTimer();
        }

        super.onDestroy();
    }


    private void releaseData() {
        try {
            tabLayout = null;
            viewPager = null;
            ctl_btnSwitch = null;
//            tv_member_counts = null;
            tv_name = null;
            videoViewEx = null;
            videoFrm = null;
            tv_play_account = null;
            up_view = null;
            land_view = null;
            if (mHeadIcon != null) {
                mHeadIcon.setImageDrawable(null);
                mHeadIcon = null;
            }
            img_live_exit = null;
            btn_share = null;
            rlControllLayer = null;
            if (recommendHeadImageLeft != null) {
                recommendHeadImageLeft.setImageDrawable(null);
                recommendHeadImageLeft = null;
            }
            recommendNicknameLeft = null;
            recommendCountLeft = null;
            if (recommendHeadImageRight != null) {
                recommendHeadImageRight.setImageDrawable(null);
                recommendHeadImageRight = null;
            }
            recommendNicknameRight = null;
            recommendCountRight = null;
            loading_view_rel = null;
            recommendView = null;
            flashview = null;
            if (imgT1 != null) {
                imgT1.setImageDrawable(null);
                imgT1 = null;
            }
            if (imgT2 != null) {
                imgT2.setImageDrawable(null);
                imgT2 = null;
            }
            if (imgT3 != null) {
                imgT3.setImageDrawable(null);
                imgT3 = null;
            }
            if (imgT4 != null) {
                imgT4.setImageDrawable(null);
                imgT4 = null;
            }
            caichiTimeLeft = null;
            textCaichiOpen = null;
            recLeft = null;
            recRight = null;
            if (coverImg != null) {
                coverImg.setImageDrawable(null);
                coverImg = null;
            }
            loadingView.stop();
            loadingView = null;
            if (titles != null) {
                titles.clear();
                titles = null;
            }
            if (animGiftQune != null) {
                animGiftQune.clear();
                animGiftQune = null;
            }
            roomInfo = null;
            if (mInputTextMsgDialog != null && mInputTextMsgDialog.isShowing()) {
                mInputTextMsgDialog.dismiss();
            }
            mInputTextMsgDialog = null;
            if (messageDialog != null && messageDialog.isShowing()) {
                messageDialog.dismiss();
            }
            if (messageDialog != null && messageDialog.isShowing()) {
                messageDialog.dismiss();
            }
            messageDialog = null;
            giftDialogView = null;

            messageInPlayFragment = null;
            luckyPoolFragment = null;
            if (datas != null) {
                datas.clear();
                datas = null;
            }
            if (allRoomMessage != null) {
                allRoomMessage.clear();
                allRoomMessage = null;
            }
            if (allChatMessage != null) {
                allChatMessage.clear();
                allChatMessage = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusCodeCallback(int what, String extra) {
        if (mOnStatusCodeEventHandler != null) {
            mOnStatusCodeEventHandler.obtainMessage(what, extra).sendToTarget();
        }
    }

    @Override
    public void onMediaPause() {

    }

    @Override
    public void onMediaStart() {
        loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBufferingStart() {
        loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBufferingEnd() {
        loadingView.setVisibility(View.GONE);
    }

    @Override
    public void onMediaCompletion() {
        loadingView.setVisibility(View.GONE);
    }

    @Override
    public void onMediaPrepared() {

    }

    @Override
    public void onMediaError(int i, int i1) {

    }

    @Override
    public void onMediaInfo(int what, int extra) {
        String errorText = "[" + extra + "] " + PlayEvent.getErrorText(extra);
        AppLogger.e("onMediaError: " + what + "," + extra + "。错误说明：" + errorText);
    }

    @Override
    public void joinRoomRespoce(JoinRoomResponce responce) {
        if (responce.getCode() == 0) {
            FeihuZhiboApplication.getApplication().mDataManager.saveXiaolabaCount(responce.getmJoinRoomData().getXiaolaba());
            roomInfo = responce.getmJoinRoomData();
            if (roomInfo != null && roomInfo.getRoomId().equals(roomId)) {

                initSdk(roomInfo);
                mPresenter.saveHistory(responce.getmJoinRoomData());

                if (isNeedGuard) {
                    GuardDialogFragment dialogFragment = GuardDialogFragment.newInstance(roomInfo.getMasterDataList().getHeadUrl(),
                            roomInfo.getMasterDataList().getNickName(), roomInfo.getRoomId(), roomInfo.getMasterDataList().getAccountId());
                    dialogFragment.show(getSupportFragmentManager(), "");
                }
            } else {
                onToast("获取直播间信息失败");
                finish();
            }
        } else {
            if (responce.getCode() == 4604) {
                String desc = responce.getErrExtMsgData().getDesc();
                String duration = responce.getErrExtMsgData().getDuration();
                showBanDialog(desc, duration);
            }
            AppLogger.e(responce.getErrMsg());
        }
    }

    @Override
    public void showLogin() {
        if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
            if (BuildConfig.isForceLoad.equals("1")) {
                loginDialogCount = 0;
                CustomDialogUtils.showQZLoginDialog(LivePlayInPCActivity.this, true);
                CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                    @Override
                    public void cancleClick() {
                        loginDialogCount = 1;
                    }

                    @Override
                    public void loginSuccess() {
                        if (roomId != null) {
                            mPresenter.joinRoom(roomId, "0", false);
                        }
                    }
                });
            } else {
                CustomDialogUtils.showLoginDialog(LivePlayInPCActivity.this, true);
            }
        } else {

        }
    }

    @Override
    public void onTextSend(String msg, boolean tanmuOpen) {
        if (tanmuOpen) {
            mPresenter.sendLoudspeaker(msg);
        } else {
            mPresenter.sendToRoom(msg);
        }
    }

    @Override
    public void loadOtherRoomsResponce(LoadOtherRoomsResponce responce) {
        loadingView.setVisibility(View.GONE);
        datas = responce.getOtherRoomDatas();
        imgFull.setEnabled(false);
        if (datas.size() >= 2) {
            recommendView.setVisibility(View.VISIBLE);
            rlControllLayer.setVisibility(View.INVISIBLE);
            String userCount = "";
            if (datas.get(0).getOnlineUserCnt() >= 10000) {
                userCount = new BigDecimal((double) datas.get(0).getOnlineUserCnt() / 10000).setScale(1, BigDecimal.ROUND_HALF_DOWN) + "万人";
            } else {
                userCount = datas.get(0).getOnlineUserCnt() + "人";
            }
            recommendCountLeft.setText("在线:" + userCount);
            TCUtils.showPicWithUrl(LivePlayInPCActivity.this, recommendHeadImageLeft, datas.get(0).getHeadUrl(), R.drawable.face);
            recommendNicknameLeft.setText(datas.get(0).getNickName());

            String userCount1 = "";
            if (datas.get(0).getOnlineUserCnt() >= 10000) {
                userCount1 = new BigDecimal((double) datas.get(1).getOnlineUserCnt() / 10000).setScale(1, BigDecimal.ROUND_HALF_DOWN) + "万人";
            } else {
                userCount1 = datas.get(1).getOnlineUserCnt() + "人";
            }
            recommendCountRight.setText("在线:" + userCount1);
            TCUtils.showPicWithUrl(LivePlayInPCActivity.this, recommendHeadImageRight, datas.get(1).getHeadUrl(), R.drawable.face);
            recommendNicknameRight.setText(datas.get(1).getNickName());
            if (land_view != null && land_view.getVisibility() == View.VISIBLE) {
                land_view.setRecommendView(datas);
            }
        } else {
            if (linearLayout != null) {
                linearLayout.setVisibility(View.GONE);
            }
            recommendView.setVisibility(View.VISIBLE);
            rlControllLayer.setVisibility(View.INVISIBLE);
        }
    }

    private CaichiTimerTask caichiTimerTask;

    @Override
    public void notifyCaichiCountDown(JackpotCountDownResponce.JackpotCountDownData data) {
        caichicountDown = data.getCountDown();
        int giftId = data.getGiftId();
        if (textCaichiOpen == null) {
            return;
        }
        if (giftId == 1) {
            textCaichiOpen.setText("距离幸运黄瓜开奖");
            if (land_view != null) {
                land_view.setCaichiOpenLand("距离幸运黄瓜开奖");
            }
        } else if (giftId == 2) {
            textCaichiOpen.setText("距离幸运香蕉开奖");
            if (land_view != null) {
                land_view.setCaichiOpenLand("距离幸运香蕉开奖");
            }
        } else if (giftId == 8) {
            textCaichiOpen.setText("距离幸运爱心开奖");
            if (land_view != null) {
                land_view.setCaichiOpenLand("距离幸运爱心开奖");
            }
        } else if (giftId == 20) {
            textCaichiOpen.setText("距离飞虎流星开奖");
            if (land_view != null) {
                land_view.setCaichiOpenLand("距离飞虎流星开奖");
            }
        }
        caichiTimerTask = new CaichiTimerTask();
        mCaichiTimer.schedule(caichiTimerTask, 1000, 1000);
    }

    @Override
    public void notifyGetCurrMount(int id) {
        if (id != 0) {
            SysMountNewBean mountBean = mPresenter.getMountBeanByID(String.valueOf(id));
            if (mountBean != null) {
                if (mountBean.getIsAnimation().equals("1")) {
                    SysGiftNewBean giftBean = new SysGiftNewBean();
                    giftBean.setAnimName(mountBean.getAnimName());
                    giftBean.setMount(true);
                    giftBean.setIs520(false);
                    giftBean.setIs1314(false);
                    animGiftQune.add(0, giftBean);
                } else {
                    GiftModel giftModel = new GiftModel(SharePreferenceUtil.getSession(LivePlayInPCActivity.this, "PREF_KEY_USERID"),
                            0, "驾驶[" + mountBean.getName() + "]进入直播间", SharePreferenceUtil.getSession(LivePlayInPCActivity.this, "PREF_KEY_NICKNAME"),
                            SharePreferenceUtil.getSession(LivePlayInPCActivity.this, "PREF_KEY_HEADURL"), id, false);
                    if (SharePreferenceUtil.getSessionInt(LivePlayInPCActivity.this, "PREF_KEY_VIP") > 0) {
                        if (!SharePreferenceUtil.getSessionBoolean(LivePlayInPCActivity.this, "PREF_KEY_VIPEXPIRED")) {
                            if (isFirst) {
//                                vipGiftModels.add(giftModel);
//                                isFirst=false;
                            }
                        } else {

                        }
                    } else {
                    }

                }
            }
        }
    }

    @Override
    public Activity getPcActivity() {
        return LivePlayInPCActivity.this;
    }

    @Override
    public void setLoginDialogCount(int count) {
        loginDialogCount = count;
    }

    @Override
    public void goReportActivity(String id) {
        Intent intent = new Intent(LivePlayInPCActivity.this, ReportActivity.class);
        intent.putExtra("userId", roomInfo.getRoomId());
        startActivity(intent);
    }

    @Override
    public void goOthersCommunityActivity(String id) {
        Intent intent = new Intent(LivePlayInPCActivity.this, OthersCommunityActivity.class);
        intent.putExtra("userId", id);
        startActivity(intent);
    }

    @Override
    public void followOrUnfollow(boolean isfollow) {
        if (isfollow) {
            ctl_btnSwitch.setVisibility(View.GONE);
            roomInfo.getMasterDataList().setFollowed(true);
            if (land_view != null && land_view.getVisibility() == View.VISIBLE) {
                land_view.setSwitchView(true);
            }
        } else {
            ctl_btnSwitch.setVisibility(View.VISIBLE);
            ctl_btnSwitch.setImageResource(R.drawable.icon_focus);
            roomInfo.getMasterDataList().setFollowed(false);
            if (land_view != null && land_view.getVisibility() == View.VISIBLE) {
                land_view.setSwitchView(false);
            }
        }
    }


    @RxBusSubscribe(code = RxBusCode.RX_BUS_NOTICE_CODE_HAVACARE, threadMode = ThreadMode.MAIN)
    public void onReceiveHavecare(String userId) {
        if (userId.equals(roomInfo.getMasterDataList().getUserId())) {
            ctl_btnSwitch.setVisibility(View.GONE);
            roomInfo.getMasterDataList().setFollowed(true);
            if (land_view != null && land_view.getVisibility() == View.VISIBLE) {
                land_view.setSwitchView(true);
            }
        }
        if (messageInPlayFragment != null) {
            messageInPlayFragment.loadFriends();
        }
        if (messageDialog != null) {
            messageDialog.loadFriends();
        }
    }

    @RxBusSubscribe(code = RxBusCode.RX_BUS_NOTICE_CODE_NOHAVACARE, threadMode = ThreadMode.MAIN)
    public void onReceiveNoHavecare(String userId) {
        if (userId.equals(roomInfo.getMasterDataList().getUserId())) {
            ctl_btnSwitch.setVisibility(View.VISIBLE);
            ctl_btnSwitch.setImageResource(R.drawable.icon_focus);
            roomInfo.getMasterDataList().setFollowed(false);
            if (land_view != null && land_view.getVisibility() == View.VISIBLE) {
                land_view.setSwitchView(false);
            }
        }
        if (messageInPlayFragment != null) {
            messageInPlayFragment.loadFriends();
        }
        if (messageDialog != null) {
            messageDialog.loadFriends();
        }
    }

    @Override
    public void careResponce(FollowResponce responce) {
        if (responce.getCode() == 0) {

            roomInfo.getMasterDataList().setFollowed(true);
            ctl_btnSwitch.setImageResource(R.drawable.icon_havecare);
            ctl_btnSwitch.setVisibility(View.GONE);
            if (land_view != null && land_view.getVisibility() == View.VISIBLE) {
                land_view.setSwitchView(true);
            }
            if (SharePreferenceUtil.getSessionBoolean(FeihuZhiboApplication.getApplication(), "first_concern", true)) {
                SharePreferenceUtil.saveSeesionBoolean(FeihuZhiboApplication.getApplication(), "first_concern", false);
                showFirstConcernDialog();
            } else {
                onToast("关注成功！");
            }
        } else {
            onToast("关注失败！");
            ctl_btnSwitch.setImageResource(R.drawable.icon_focus);
        }
    }

    private void showFirstConcernDialog() {
        final Dialog pickDialog2 = new Dialog(this, R.style.color_dialog);
        pickDialog2.setContentView(R.layout.pop_pc_first_concern);

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog2.getWindow();
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.CENTER);
        lp.width = (int) (display.getWidth()); //设置宽度
        pickDialog2.getWindow().setAttributes(lp);
        TextView tvOk = (TextView) pickDialog2.findViewById(R.id.tv_ok);
        TextView tvCancel = (TextView) pickDialog2.findViewById(R.id.tv_cancel);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAppDetailSettingIntent();
                pickDialog2.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDialog2.dismiss();
            }
        });
        pickDialog2.setCanceledOnTouchOutside(false);
        pickDialog2.show();
    }

    private void getAppDetailSettingIntent() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        startActivity(localIntent);
    }


    @Override
    public void uncareRespoce(UnFollowResponce responce) {
        if (responce.getCode() == 0) {
            onToast("取消关注成功！");
            roomInfo.getMasterDataList().setFollowed(false);
            ctl_btnSwitch.setImageResource(R.drawable.icon_focus);
            ctl_btnSwitch.setVisibility(View.GONE);
            if (land_view != null && land_view.getVisibility() == View.VISIBLE) {
                land_view.setSwitchView(false);
            }
        } else {
            ctl_btnSwitch.setImageResource(R.drawable.icon_havecare);
        }
    }

    GiftLandDialogView giftDialogView;

    @Override
    public void showLandGiftDialog(final List<SysbagBean> sysbagBeanList) {
        if (FeihuZhiboApplication.getApplication().mDataManager.getLiveGiftList().size() == 0) {
            return;
        }
        giftDialogView = new GiftLandDialogView(this, sysbagBeanList);
        Window dlgwin = giftDialogView.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        dlgwin.setGravity(Gravity.BOTTOM);
        dlgwin.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //lp.width = TCUtils.dipToPx(this,200);
        dlgwin.setAttributes(lp);
        giftDialogView.show();


        giftDialogView.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (land_view != null && land_view.getVisibility() == View.VISIBLE) {
                    land_view.setRlControllLayerLand();
                }
            }
        });
        giftDialogView.setGiftDialogListener(new GiftLandDialogView.GiftLandDialogListener() {
            @Override
            public void sendGift(int id, int count, boolean isbag) {
                if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                    if (BuildConfig.isForceLoad.equals("1")) {
                        loginDialogCount = 0;
                        CustomDialogUtils.showQZLoginDialog(LivePlayInPCActivity.this, true);
                        CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                            @Override
                            public void cancleClick() {
                                loginDialogCount = 1;
                            }

                            @Override
                            public void loginSuccess() {
                                if (roomId != null) {
                                    mPresenter.joinRoom(roomId, "0", false);
                                }
                            }
                        });
                    } else {
                        CustomDialogUtils.showLoginDialog(LivePlayInPCActivity.this, true);
                    }
                } else {
                    if (isbag) {
                        mPresenter.dealBagSendGift(sysbagBeanList, id, count);
                    } else {
                        mPresenter.dealSendGift(sysbagBeanList, id, count);
                    }
                }
            }

            @Override
            public void charge() {
                if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                    if (BuildConfig.isForceLoad.equals("1")) {
                        loginDialogCount = 0;
                        CustomDialogUtils.showQZLoginDialog(LivePlayInPCActivity.this, true);
                        CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                            @Override
                            public void cancleClick() {
                                loginDialogCount = 1;
                            }

                            @Override
                            public void loginSuccess() {
                                if (roomId != null) {
                                    mPresenter.joinRoom(roomId, "0", false);
                                }
                            }
                        });
                    } else {
                        CustomDialogUtils.showLoginDialog(LivePlayInPCActivity.this, true);
                    }
                } else {
                    Intent intent = new Intent(LivePlayInPCActivity.this, RechargeActivity.class);
                    intent.putExtra("fromWhere", "直播间礼物页面");
                    startActivity(intent);
                }
            }

            @Override
            public void showNumDialog() {
                showGiftNumDialog();
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        falshViewisEnd = true;
        flashview.setVisibility(View.GONE);
        flashview.stop();
        giftCheck = 0;
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            up_view.setVisibility(View.GONE);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoViewEx.getLayoutParams();
            layoutParams.height = getResources().getDisplayMetrics().heightPixels;
            layoutParams.width = getResources().getDisplayMetrics().widthPixels;
            videoViewEx.setLayoutParams(layoutParams);
            land_view.setVisibility(View.VISIBLE);
            land_view.getTvHubiLand().setText(roomInfo.getMasterDataList().getHB() + "");
            land_view.getTvPcAccountLand().setText(roomInfo.getMasterDataList().getAccountId());
            TCUtils.showPicWithUrl(this, land_view.getHeadImg(), roomInfo.getMasterDataList().getHeadUrl(), R.drawable.face);
            String userCount = "";
            if (roomInfo.getOnlineUserCnt() >= 10000) {
                userCount = new BigDecimal((double) roomInfo.getOnlineUserCnt() / 10000).setScale(1, BigDecimal.ROUND_HALF_DOWN) + "万人";
            } else {
                userCount = roomInfo.getOnlineUserCnt() + "人";
            }
            land_view.getTvMemberCountsLand(userCount);
            land_view.setSwitchView(roomInfo.getMasterDataList().isFollowed());
            if (luckyPoolFragment != null) {
                luckyPoolFragment.setIsLand(true);
            }

            flashview.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            int news_count = SharePreferenceUtil.getSessionInt(LivePlayInPCActivity.this, "news_count");
            if (tabLayout != null && roomInfo != null) {
                if (news_count > 0) {
                    tabLayout.showDot(1);
                } else {
                    tabLayout.hideMsg(1);
                }
            }
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoViewEx.getLayoutParams();
            layoutParams.height = TCUtils.dipToPx(LivePlayInPCActivity.this, 200);
            layoutParams.width = getResources().getDisplayMetrics().widthPixels;
            videoViewEx.setLayoutParams(layoutParams);
            up_view.setVisibility(View.VISIBLE);
            land_view.setVisibility(View.GONE);
            if (luckyPoolFragment != null) {
                luckyPoolFragment.setIsLand(false);
            }
            if (currentPos == 0) {
                if (flashview.getVisibility() != View.VISIBLE) {
                    flashview.setVisibility(View.VISIBLE);
                }
            } else {
                flashview.setVisibility(View.GONE);
            }
            final WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attrs);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            if (isFromLand) {
                isFromLand = false;
//                LiveChatFragment.setFullScreen(false);
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                viewPager.setCurrentItem(2);
                if (luckyPoolFragment != null) {
                    luckyPoolFragment.showWeekStarDialog(weekStarUrl, true);
                }
            }
        }
    }

    @Override
    public void sendGiftSuccess(List<SysbagBean> sysbagBeanList, int id, int count) {
        if (LiveChatFragment.liveChatFragment != null) {
            TCChatEntity entity = new TCChatEntity();
            entity.setSenderName("" + SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_NICKNAME") + "  ");
            SysGiftNewBean giftBean = mPresenter.getGiftBeanByID(String.valueOf(id));
            entity.setContext("送出了" + giftBean.getName() + "×" + count);
            entity.setType(TCConstants.PC_TYPE);
            entity.setLevel(SharePreferenceUtil.getSessionInt(FeihuZhiboApplication.getApplication(), "PREF_KEY_LEVEL"));
            LiveChatFragment.liveChatFragment.notifyMsg(entity);
        }

        for (SysbagBean sysbagBeen : sysbagBeanList) {
            if (sysbagBeen.getId() == id) {
                if (sysbagBeen.getCnt() > count) {
                    sysbagBeen.setCnt(sysbagBeen.getCnt() - count);
                } else {
                    sysbagBeanList.remove(sysbagBeen);
                }
                if (giftDialogView != null) {
                    giftDialogView.setData(SysdataHelper.getbagBeanList(mPresenter.getSysGiftNewBean(), sysbagBeanList));
                }
                break;
            }
        }
        if (giftDialogView != null) {
            giftDialogView.onFreshCoin();
        }
    }

    @Override
    public void leaveRoomResponce(int code) {
        if (code == 1) {
            gotoRoomData(datas.get(0));
        } else if (code == 2) {
            gotoRoomData(datas.get(1));
        }
    }

    @Override
    public void gotoRoom(LoadRoomResponce.LoadRoomData data) {
        gotoRoomData(data);
    }

    @Override
    public void DerectMsgDialogDissMiss() {
        if (messageInPlayFragment != null) {
            messageInPlayFragment.onFreshDatas();
        }
    }

    DerectMsgDialog derectMsgDialog;

    @Override
    public void initDerectMsg(String sendId, String nickName, String headUrl, boolean isPcLand) {
        derectMsgDialog = new DerectMsgDialog(LivePlayInPCActivity.this, sendId, nickName, headUrl, true);
        Window dlgwin = derectMsgDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        dlgwin.setGravity(Gravity.BOTTOM);
        dlgwin.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        UiUtil.initialize(LivePlayInPCActivity.this);
        int screenWidth = UiUtil.getScreenWidth();
        int screenHeight = UiUtil.getScreenHeight();
        lp.width = screenWidth;
        if (isPcLand) {
            lp.height = (int) (screenHeight - TCUtils.dip2px(LivePlayInPCActivity.this, 100));
        } else {
            lp.height = (int) (screenHeight - TCUtils.dip2px(LivePlayInPCActivity.this, 270));
        }
        dlgwin.setAttributes(lp);
        derectMsgDialog.setOnStrClickListener(new OnStrClickListener() {
            @Override
            public void onItemClick(String userId) {
                if (!TextUtils.isEmpty(userId)) {
                    Intent intent = new Intent(LivePlayInPCActivity.this, OthersCommunityActivity.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                }
            }
        });
        derectMsgDialog.show();
        derectMsgDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                int news_count = SharePreferenceUtil.getSessionInt(LivePlayInPCActivity.this, "news_count");
                if (news_count > 0) {
                    if (land_view != null) {
                        land_view.setImgMsgNewVis(true);
                    }
                } else {
                    if (land_view != null) {
                        land_view.setImgMsgNewVis(false);
                    }
                }
                DerectMsgDialogDissMiss();
            }
        });
    }

    @Override
    public void notifyWanfaHistory() {
        RxBus.get().send(RxBusCode.RX_BUS_NOTICE_CODE_NOTIFYWANFAHISTORY);
    }

    private void gotoRoomData(LoadRoomResponce.LoadRoomData data) {
        if (data.getRoomId().equals(roomInfo.getRoomId())) {
            return;
        }
        // 1为PC   2为手机
        finish();
        AppUtils.startLiveActivity(LivePlayInPCActivity.this, data.getRoomId(), data.getHeadUrl(), data.getBroadcastType(), false, -1);
    }

    private void gotoRoomData(final LoadOtherRoomsResponce.OtherRoomData tcRoomInfo) {

        if (tcRoomInfo.getRoomId().equals(roomInfo.getRoomId())) {
            return;
        }

        finish();
        AppUtils.startLiveActivity(LivePlayInPCActivity.this, tcRoomInfo.getRoomId(), tcRoomInfo.getHeadUrl(), tcRoomInfo.getBroadcastType(), false, -1);
    }

    private GiftNumDialog giftNumDialog;

    private void showGiftNumDialog() {
        giftNumDialog = new GiftNumDialog(LivePlayInPCActivity.this, R.style.InputDialog);
        Window dlgwin = giftNumDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        dlgwin.setGravity(Gravity.BOTTOM);
        dlgwin.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dlgwin.setAttributes(lp);
        giftNumDialog.show();
        giftNumDialog.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (giftDialogView != null) {
                    giftDialogView.setEditText(position);
                }
            }
        });
    }

    private boolean isFirst = true;

    private void initSdk(JoinRoomResponce.JoinRoomData roomInfo) {
        if (messageTimer == null) {
            messageTimer = new Timer(true);
            messageTimerTask = new MessageTimerTask();
            messageTimer.schedule(messageTimerTask, 1000, 2500);
        }
        if (chatMessageTimer == null) {
            chatMessageTimer = new Timer(true);
            chatMessageTimerTask = new ChatMessageTimerTask();
            chatMessageTimer.schedule(chatMessageTimerTask, 1000, 2000);
        }
        if (staticGiftTimer == null) {
            staticGiftTimer = new Timer(true);
            giftTimerTask = new StaticGiftTimerTask();
            staticGiftTimer.schedule(giftTimerTask, 1000, 500);
        }
        mPlayUrl = roomInfo.getPlayUrl();
        initRecommendView();
        userId = roomInfo.getMasterDataList().getUserId();
        initLiveView();
        initHandler();
        videoViewEx.setAspectRatio(IRenderView.AR_ASPECT_FILL_PARENT);
        videoViewEx.play(mPlayUrl);
        videoViewEx.setOnStatusCodeEventListener(this);
        videoViewEx.setOnMediaEventsListener(this);
        initEvent();
        int news_count = SharePreferenceUtil.getSessionInt(LivePlayInPCActivity.this, "news_count");
        if (tabLayout != null && roomInfo != null) {
            if (news_count > 0) {
                tabLayout.showDot(1);
            } else {
                tabLayout.hideMsg(1);
            }
        }
        mInputTextMsgDialog = new TCInputTextMsgDialog(this, R.style.InputDialog);
        mInputTextMsgDialog.setmOnTextSendListener(this);

        if (mGiftTimer == null) {
            mGiftTimer = new Timer(true);
            mGiftTimerTask = new GiftTimerTask();
            mGiftTimer.schedule(mGiftTimerTask, 1000, 1000);
        }
        if (mCaichiTimer == null) {
            mCaichiTimer = new Timer(true);
        }
        mPresenter.getJackpotCountDown();
        mPresenter.getCurrMount(roomInfo.getRoomId());
    }

    private int currentPos = 0;
    private int lastPos = 0;
    private int lastlastPos = 0;
    private FragmentViewPagerAdapter adapter;
    private UserDialogFragment userDialogFragment;

    private void initEvent() {
        titles = new ArrayList<>();
        titles.add("聊天");
        titles.add("私信");
        titles.add("玩法");
        titles.add("榜单");
        titles.add("观众");
        if (adapter == null) {
            adapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), titles, roomInfo);
        }
        adapter.setFragmentViewPagerAdapterListener(new FragmentViewPagerAdapterListener() {
            @Override
            public void onitemClick(String userId, boolean isBangdan) {
                mPresenter.showUserInfo(roomInfo, userId, false, isBangdan, false);
            }

            @Override
            public void onDanmuClick(String roomId) {

            }
        });
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.setCurrentTab(position);
                if (position == 1) {
                    if (tabLayout != null) {
                        SharePreferenceUtil.saveSeesionInt(LivePlayInPCActivity.this, "news_count", 0);
                        tabLayout.hideMsg(1);
                    }
                }
                lastlastPos = lastPos;
                lastPos = currentPos;
                currentPos = position;
                if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                    if (position == 1) {
                        if (lastPos == 1) {
                            if (lastlastPos == 1) {
                                viewPager.setCurrentItem(0);
                            } else {
                                viewPager.setCurrentItem(lastlastPos);
                            }

                        } else {
                            viewPager.setCurrentItem(lastPos);
                        }

                        if (BuildConfig.isForceLoad.equals("1")) {
                            loginDialogCount = 0;
                            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                                CustomDialogUtils.showQZLoginDialog(LivePlayInPCActivity.this, true);
                            } else {
                                CustomDialogUtils.showQZLoginDialog(LivePlayInPCActivity.this, false);
                            }
                            CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                                @Override
                                public void cancleClick() {
                                    loginDialogCount = 1;
                                    if (viewPager == null) {
                                        return;
                                    }
//                                    if (currentPos == 1) {
//                                        viewPager.setCurrentItem(0);
//                                    } else {
//                                        viewPager.setCurrentItem(currentPos);
//                                    }


                                }

                                @Override
                                public void loginSuccess() {
                                    if (roomId != null) {
                                        mPresenter.joinRoom(roomId, "0", false);
                                    }
                                }
                            });
                        } else {
                            // TODO: 2017/9/7  普通登录
//                            showLoginDialog(true);
                        }

                    }
                } else {

                }
                if (position == 0) {
                    if (flashview.getVisibility() != View.VISIBLE) {
                        flashview.setVisibility(View.VISIBLE);
                    }
                } else {
                    flashview.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        String[] mTitles = {"聊天", "私信", "玩法", "榜单", "观众"};
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i]));
        }
        tabLayout.setTabData(mTabEntities);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
                currentPos = position;
                if (position == 0) {
                    if (flashview.getVisibility() != View.VISIBLE) {
                        flashview.setVisibility(View.VISIBLE);
                    }
                } else {
                    flashview.setVisibility(View.GONE);
                }
                if (position == 1) {
                    if (tabLayout != null) {
                        tabLayout.hideMsg(1);
                    }
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        land_view.setOnHorControllListener(new LivePlayInPCLandView.OnHorControllListener() {
            @Override
            public void fullScreen() {
                if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    // 如果是横屏,则改为竖屏
                    LiveChatFragment.setFullScreen(false);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    LiveChatFragment.setFullScreen(true);
                    // 如果是竖屏,则改为横屏
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
            }

            @Override
            public void headImageClick(String userId) {
                Log.e("useId", "headImageClick: ---->" + userId);
                if (TextUtils.isEmpty(userId)) {
                    mPresenter.showUserInfo(roomInfo, roomInfo.getMasterDataList().getUserId(), true, false, true);
                } else {
                    mPresenter.showUserInfo(roomInfo, userId, true, false, false);
                }
            }

            @Override
            public void switchClick() {
                btnSwitchClick(true);
            }

            @Override
            public void share() {
                showLiveShareDialog(true);
            }

            @Override
            public void sendgift() {
                mPresenter.requestBagData();
            }

            @Override
            public void msgClick() {
                if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                    if (BuildConfig.isForceLoad.equals("1")) {
                        loginDialogCount = 0;
                        CustomDialogUtils.showQZLoginDialog(LivePlayInPCActivity.this, true);
                        CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                            @Override
                            public void cancleClick() {
                                loginDialogCount = 1;
                            }

                            @Override
                            public void loginSuccess() {
                                if (roomId != null) {
                                    mPresenter.joinRoom(roomId, "0", false);
                                }
                            }
                        });
                    } else {
                        CustomDialogUtils.showLoginDialog(LivePlayInPCActivity.this, true);
                    }
                } else {
                    showInputMsgDialog();
                }
            }

            @Override
            public void onClickMsgBnt() {
                MobclickAgent.onEvent(LivePlayInPCActivity.this, "10023");
                if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                    if (BuildConfig.isForceLoad.equals("1")) {
                        loginDialogCount = 0;
                        CustomDialogUtils.showQZLoginDialog(LivePlayInPCActivity.this, true);
                        CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                            @Override
                            public void cancleClick() {
                                loginDialogCount = 1;
                            }

                            @Override
                            public void loginSuccess() {
                                if (roomId != null) {
                                    mPresenter.joinRoom(roomId, "0", false);
                                }
                            }
                        });
                    } else {
                        CustomDialogUtils.showLoginDialog(LivePlayInPCActivity.this, true);
                    }
                } else {
                    SharePreferenceUtil.saveSeesionInt(LivePlayInPCActivity.this, "news_count", 0);
                    showMessageDialog();
                }
            }

            @Override
            public void shakeClick(GetGameStatusResponce.GameStatusData json) {
                showShakeDialog(json);
            }

            @Override
            public void jincaiClick(int style, GetGameStatusResponce.GameStatusData json) {
                showGuessDialog(style, json);
            }

            @Override
            public void caichiClick() {
                showCaichiDialog();
            }

            @Override
            public void recClick(int pos) {
                if (pos == 1) {
                    if (datas.size() >= 2) {
                        mPresenter.leaveRoom(1);

                    }
                } else {
                    if (datas.size() >= 2) {
                        mPresenter.leaveRoom(2);

                    }
                }
            }

            @Override
            public void danmuClick(int id) {
                if (!String.valueOf(id).equals(roomInfo.getRoomId())) {
                    if (id == 0) {
                        return;
                    }
                    mPresenter.loadRoomById(String.valueOf(id));
                }
            }

            @Override
            public void showWeekStarDialog(String url) {
                isFromLand = true;
                weekStarUrl = url;
                LiveChatFragment.setFullScreen(false);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                viewPager.setCurrentItem(2);
//                if (luckyPoolFragment != null) {
//                    luckyPoolFragment.showWeekStarDialog(url);
//                }
            }

            @Override
            public void showOnlineDialog() {
                userDialogFragment = new UserDialogFragment();
                userDialogFragment.show(getSupportFragmentManager(), "");
            }

            @Override
            public void showGameDialog() {
                showLoading();
                new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                        .doGetExtGameIconCall(new GetExtGameIconRequest())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<GetExtGameIconResponce>() {
                            @Override
                            public void accept(@NonNull GetExtGameIconResponce responce) throws Exception {
                                land_view.showGameDialog(responce);
                                hideLoading();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {

                            }
                        })

                );
            }
        });
        videoViewEx.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (land_view != null && land_view.getVisibility() == View.VISIBLE) {
                    land_view.onTouchEvent(false, event);
                }
                return false;
            }
        });
    }

    Dialog pickDialogHelp;

    private void showJincaiDialog() {
        pickDialogHelp = new Dialog(this, R.style.floag_dialog);
        pickDialogHelp.setContentView(R.layout.jincai_help_dialog);
        LinearLayout linearLayout = (LinearLayout) pickDialogHelp.findViewById(R.id.web_bg);
        ViewGroup.LayoutParams layoutParams = linearLayout.getLayoutParams();
        layoutParams.height = TCUtils.dipToPx(this, 200);
        WebView webView = (WebView) pickDialogHelp.findViewById(R.id.webView);
        webView.setBackgroundColor(ContextCompat.getColor(this, R.color.bgpupelto));
        webView.loadUrl(mPresenter.getSysConfigBean().getYxjcExplainUrl());
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialogHelp.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        lp.width = TCUtils.dip2px(this, 350); //设置宽度
//        lp.height = display.getHeight() - TCUtils.dip2px(this, 50);
        lp.x = TCUtils.dip2px(this, 30);
        pickDialogHelp.getWindow().setAttributes(lp);
        pickDialogHelp.show();
    }


    PlayLandJincaiDialog gusessDialog;

    private void showGuessDialog(int style, GetGameStatusResponce.GameStatusData json) {
        if (land_view != null && land_view.getVisibility() == View.VISIBLE) {
            land_view.setImgVisual(false);
        }
        gusessDialog = new PlayLandJincaiDialog(this, style, json, jcGameResultJson, yxjcIssueRound);
        gusessDialog.show();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = gusessDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = display.getWidth(); //设置宽度
        lp.height = display.getHeight();
        gusessDialog.getWindow().setAttributes(lp);
        gusessDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                gusessDialog.dismiss();
                if (land_view != null && land_view.getVisibility() == View.VISIBLE) {
                    land_view.setImgVisual(true);
                }
            }
        });
        gusessDialog.setJincaiListener(new PlayLandJincaiDialog.JinCaiInterface() {
            @Override
            public void gameHelp() {
                showJincaiDialog();
            }

            @Override
            public void jinCai(int pos, int playCnt) {
                if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                    if (BuildConfig.isForceLoad.equals("1")) {
                        loginDialogCount = 0;
                        CustomDialogUtils.showQZLoginDialog(LivePlayInPCActivity.this, true);
                        CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                            @Override
                            public void cancleClick() {
                                loginDialogCount = 1;
                            }

                            @Override
                            public void loginSuccess() {


                            }
                        });
                    } else {
                        CustomDialogUtils.showLoginDialog(LivePlayInPCActivity.this, true);
                    }
                } else {
                    if (SharePreferenceUtil.getSessionInt(LivePlayInPCActivity.this, "PREF_KEY_LEVEL") < 6) {
                        onToast("六级开启玩法");
                    } else {
                        showJcXiazhuDialog(pos, playCnt);
                    }
                }
                MobclickAgent.onEvent(LivePlayInPCActivity.this, "10044");
            }

            @Override
            public void huDong(int blankCnt) {
                if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                    if (BuildConfig.isForceLoad.equals("1")) {
                        loginDialogCount = 0;
                        CustomDialogUtils.showQZLoginDialog(LivePlayInPCActivity.this, true);
                        CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                            @Override
                            public void cancleClick() {
                                loginDialogCount = 1;
                            }

                            @Override
                            public void loginSuccess() {
                                if (roomId != null) {
                                    mPresenter.joinRoom(roomId, "0", false);
                                }
                            }
                        });
                    } else {
                        CustomDialogUtils.showLoginDialog(LivePlayInPCActivity.this, true);
                    }
                } else {
                    if (SharePreferenceUtil.getSessionInt(LivePlayInPCActivity.this, "PREF_KEY_LEVEL") < 6) {
                        onToast("六级开启玩法");
                    } else {
                        showHuDongDialog(blankCnt);
                    }
                }
                MobclickAgent.onEvent(LivePlayInPCActivity.this, "10041");
            }

            @Override
            public void showResult(int issueRound) {
                showJoinResult("yxjc", issueRound);
            }

            @Override
            public void JoinSuccess() {
                showJoinSuccess();
            }


            @Override
            public void showshakeDlg() {
                new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                        .doGetGameStatusApiCall(new GetGameStatusRequest("lyzb"))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<GetGameStatusResponce>() {
                            @Override
                            public void accept(@NonNull GetGameStatusResponce responce) throws Exception {
                                if (responce.getCode() == 0) {
                                    boolean isGaming = responce.getGameStatusData().isGaming();
                                    if (isGaming) {
                                        showShakeDialog(responce.getGameStatusData());
                                    } else {
                                        onToast("乐瑶主播还未开启！");
                                    }
                                } else {
                                    AppLogger.e(responce.getErrMsg());
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {

                            }
                        }));
            }

            @Override
            public void goRoom(String id) {
                new CompositeDisposable().
                        add(FeihuZhiboApplication.getApplication().mDataManager.
                                doGetRoomDataApiCall(new LoadRoomRequest(id))
                                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<LoadRoomResponce>() {
                                    @Override
                                    public void accept(@NonNull final LoadRoomResponce loadRoomResponce) throws Exception {
                                        if (loadRoomResponce.getCode() == 0) {
                                            FeihuZhiboApplication.getApplication().mDataManager
                                                    .doLoadLeaveRoomApiCall(new LeaveRoomRequest())
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(new Consumer<LeaveRoomResponce>() {
                                                        @Override
                                                        public void accept(@NonNull LeaveRoomResponce response) throws Exception {
                                                            if (response.getCode() == 0) {
                                                                gusessDialog.dismiss();
                                                                gotoRoomData(loadRoomResponce.getLoadRoomData());
                                                            } else {
                                                                AppLogger.e("leaveRoom" + response.getErrMsg());
                                                            }
                                                        }
                                                    }, new Consumer<Throwable>() {
                                                        @Override
                                                        public void accept(@NonNull Throwable throwable) throws Exception {

                                                        }
                                                    });
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
            public void showLogin() {
                if (BuildConfig.isForceLoad.equals("1")) {
                    loginDialogCount = 0;
                    CustomDialogUtils.showQZLoginDialog(LivePlayInPCActivity.this, true);
                    CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                        @Override
                        public void cancleClick() {
                            loginDialogCount = 1;
                        }

                        @Override
                        public void loginSuccess() {
                            if (roomId != null) {
                                mPresenter.joinRoom(roomId, "0", false);
                            }
                        }
                    });
                } else {
                    CustomDialogUtils.showLoginDialog(LivePlayInPCActivity.this, true);
                }
            }

        });
    }

    private LandHuDongDialog landJincaiDialog;

    private void showHuDongDialog(int blankCnt) {
        landJincaiDialog = new LandHuDongDialog(this, blankCnt);
        landJincaiDialog.show();
        landJincaiDialog.setCanceledOnTouchOutside(false);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = landJincaiDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM | Gravity.RIGHT);
        lp.width = display.getWidth() / 2; //设置宽度
        lp.height = TCUtils.dipToPx(this, 200);
        landJincaiDialog.getWindow().setAttributes(lp);
        landJincaiDialog.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showJoinSuccess();
            }
        });
    }

    LandJcXiaZhuDialog jincaixiazhuDialog;

    private void showJcXiazhuDialog(int pos, int playcnt) {
        jincaixiazhuDialog = new LandJcXiaZhuDialog(this, pos, playcnt);
        jincaixiazhuDialog.show();
        jincaixiazhuDialog.setCanceledOnTouchOutside(true);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = jincaixiazhuDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM | Gravity.RIGHT);
        lp.width = TCUtils.dipToPx(this, 400); //设置宽度
        lp.height = (display.getHeight() - TCUtils.dipToPx(this, 70));
        jincaixiazhuDialog.getWindow().setAttributes(lp);
        jincaixiazhuDialog.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showJoinSuccess();
            }
        });
    }

    CaichiDialog caichiDialog;

    private void showCaichiDialog() {
        caichiDialog = new CaichiDialog(this);
        caichiDialog.setOnItemClickListener(new CaichiDialog.CaichiListener() {
            @Override
            public void wanfaClick() {
                showCaichiHelp();
            }

            @Override
            public void showLogin() {
                if (BuildConfig.isForceLoad.equals("1")) {
                    loginDialogCount = 0;
                    CustomDialogUtils.showQZLoginDialog(LivePlayInPCActivity.this, true);
                    CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                        @Override
                        public void cancleClick() {
                            loginDialogCount = 1;
                        }

                        @Override
                        public void loginSuccess() {
                            if (roomId != null) {
                                mPresenter.joinRoom(roomId, "0", false);
                            }
                        }
                    });
                } else {
                    CustomDialogUtils.showLoginDialog(LivePlayInPCActivity.this, true);
                }
            }
        });
        caichiDialog.show();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = caichiDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = display.getWidth(); //设置宽度
        lp.height = (display.getHeight() - TCUtils.dip2px(LivePlayInPCActivity.this, 150));
        caichiDialog.getWindow().setAttributes(lp);

    }

    private void showCaichiHelp() {
        Dialog pickDialog = new Dialog(this, R.style.floag_dialog);
        pickDialog.setContentView(R.layout.caichi_help_dialog);
        WebView webView = (WebView) pickDialog.findViewById(R.id.webView);
        webView.loadUrl(mPresenter.getSysConfigBean().getLuckyPoolDocUrl());
        pickDialog.show();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        lp.width = TCUtils.dip2px(this, 300); //设置宽度
        lp.x = TCUtils.dip2px(this, 50);
        pickDialog.getWindow().setAttributes(lp);

    }

    PlaylandShakeDialog shakeDialog;

    private void showShakeDialog(GetGameStatusResponce.GameStatusData json) {
        if (land_view != null && land_view.getVisibility() == View.VISIBLE) {
            land_view.setImgVisual(false);
        }
        shakeDialog = new PlaylandShakeDialog(this, json, lyGameResultJson, lyjcIssueRound);
        shakeDialog.show();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = shakeDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.RIGHT);
        lp.width = display.getWidth(); //设置宽度
        lp.height = display.getHeight();
        shakeDialog.getWindow().setAttributes(lp);
        shakeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (land_view != null && land_view.getVisibility() == View.VISIBLE) {
                    land_view.setImgVisual(true);
                }
                shakeDialog.dismiss();
            }
        });
        shakeDialog.setJincaiListener(new PlaylandShakeDialog.JinCaiInterface() {
            @Override
            public void gameHelp() {
                showLyhelpDialog();
                MobclickAgent.onEvent(LivePlayInPCActivity.this, "10070");
            }

            @Override
            public void jinCai(int pos, int playCnt) {
                if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                    if (BuildConfig.isForceLoad.equals("1")) {
                        loginDialogCount = 0;
                        CustomDialogUtils.showQZLoginDialog(LivePlayInPCActivity.this, true);
                        CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                            @Override
                            public void cancleClick() {
                                loginDialogCount = 1;
                            }

                            @Override
                            public void loginSuccess() {
                                if (roomId != null) {
                                    mPresenter.joinRoom(roomId, "0", false);
                                }
                            }
                        });
                    } else {
                        CustomDialogUtils.showLoginDialog(LivePlayInPCActivity.this, true);
                    }
                } else {
                    if (SharePreferenceUtil.getSessionInt(LivePlayInPCActivity.this, "PREF_KEY_LEVEL") < 6) {
                        onToast("六级开启玩法");
                    } else {
                        showLyXiazhuDialog(pos, playCnt);
                    }
                }
                MobclickAgent.onEvent(LivePlayInPCActivity.this, "10066");
            }

            @Override
            public void huDong(int blankCnt) {
                if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                    if (BuildConfig.isForceLoad.equals("1")) {
                        loginDialogCount = 0;
                        CustomDialogUtils.showQZLoginDialog(LivePlayInPCActivity.this, true);
                        CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                            @Override
                            public void cancleClick() {
                                loginDialogCount = 1;
                            }

                            @Override
                            public void loginSuccess() {
                                if (roomId != null) {
                                    mPresenter.joinRoom(roomId, "0", false);
                                }
                            }
                        });
                    } else {
                        CustomDialogUtils.showLoginDialog(LivePlayInPCActivity.this, true);
                    }
                } else {
                    if (SharePreferenceUtil.getSessionInt(LivePlayInPCActivity.this, "PREF_KEY_LEVEL") < 6) {
                        onToast("六级开启玩法");
                    } else {
                        showLyHuDongDialog(blankCnt);
                    }
                }
                MobclickAgent.onEvent(LivePlayInPCActivity.this, "10059");
            }

            @Override
            public void showResult(int issueRound) {
                showJoinResult("lyzb", issueRound);
            }

            @Override
            public void close() {
                if (landjincaiDialog != null) {
                    if (landjincaiDialog.isShowing()) {
                        landjincaiDialog.dismiss();
                    }
                }
            }

            @Override
            public void showJincaiDlg() {
                new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                        .doGetGameStatusApiCall(new GetGameStatusRequest("yxjc"))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<GetGameStatusResponce>() {
                            @Override
                            public void accept(@NonNull GetGameStatusResponce responce) throws Exception {
                                if (responce.getCode() == 0) {
                                    boolean isGaming = responce.getGameStatusData().isGaming();
                                    if (isGaming) {
                                        int openStyle = responce.getGameStatusData().getOpenStyle();
                                        showGuessDialog(openStyle, responce.getGameStatusData());
                                    } else {
                                        onToast("游戏竞猜还未开启！");
                                    }
                                } else {
                                    AppLogger.e(responce.getErrMsg());
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {

                            }
                        }));
            }

            @Override
            public void goRoom(String id) {
                new CompositeDisposable().
                        add(FeihuZhiboApplication.getApplication().mDataManager.
                                doGetRoomDataApiCall(new LoadRoomRequest(id))
                                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<LoadRoomResponce>() {
                                    @Override
                                    public void accept(@NonNull final LoadRoomResponce loadRoomResponce) throws Exception {
                                        if (loadRoomResponce.getCode() == 0) {
                                            FeihuZhiboApplication.getApplication().mDataManager
                                                    .doLoadLeaveRoomApiCall(new LeaveRoomRequest())
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(new Consumer<LeaveRoomResponce>() {
                                                        @Override
                                                        public void accept(@NonNull LeaveRoomResponce response) throws Exception {
                                                            if (response.getCode() == 0) {
                                                                shakeDialog.dismiss();
                                                                gotoRoomData(loadRoomResponce.getLoadRoomData());
                                                            } else {
                                                                AppLogger.e("leaveRoom" + response.getErrMsg());
                                                            }
                                                        }
                                                    }, new Consumer<Throwable>() {
                                                        @Override
                                                        public void accept(@NonNull Throwable throwable) throws Exception {

                                                        }
                                                    });
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
            public void showLogin() {
                if (BuildConfig.isForceLoad.equals("1")) {
                    loginDialogCount = 0;
                    CustomDialogUtils.showQZLoginDialog(LivePlayInPCActivity.this, true);
                    CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                        @Override
                        public void cancleClick() {
                            loginDialogCount = 1;
                        }

                        @Override
                        public void loginSuccess() {
                            if (roomId != null) {
                                mPresenter.joinRoom(roomId, "0", false);
                            }
                        }
                    });
                } else {
                    CustomDialogUtils.showLoginDialog(LivePlayInPCActivity.this, true);
                }
            }

            @Override
            public void JoinSuccess() {
                showJoinSuccess();
            }
        });
    }


    private void showJoinResult(final String name, int issueRound) {
        mPresenter.showJoinResult(name, issueRound);
    }

    private LyLandHuDongDialog landjincaiDialog;

    private void showLyHuDongDialog(int blankCnt) {
        landjincaiDialog = new LyLandHuDongDialog(this, blankCnt);
        landjincaiDialog.show();
        landjincaiDialog.setCanceledOnTouchOutside(true);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = landjincaiDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM | Gravity.RIGHT);
        lp.width = TCUtils.dipToPx(this, 400); //设置宽度
        lp.height = (display.getHeight() - TCUtils.dipToPx(this, 70));
        landjincaiDialog.getWindow().setAttributes(lp);
        landjincaiDialog.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showJoinSuccess();
            }
        });
    }

    private void showLyhelpDialog() {
        Dialog pickDialog = new Dialog(this, R.style.floag_dialog);
        pickDialog.setContentView(R.layout.jincai_help_dialog);
        LinearLayout linearLayout = (LinearLayout) pickDialog.findViewById(R.id.web_bg);
        ViewGroup.LayoutParams layoutParams = linearLayout.getLayoutParams();
        layoutParams.height = TCUtils.dipToPx(this, 200);
        WebView webView = (WebView) pickDialog.findViewById(R.id.webView);
        webView.setBackgroundColor(ContextCompat.getColor(this, R.color.bgpupelto));
        webView.loadUrl(mPresenter.getSysConfigBean().getLyzbExplainUrl());
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        lp.width = TCUtils.dip2px(this, 350); //设置宽度
//        lp.height = display.getHeight() - TCUtils.dip2px(this, 100);
        lp.x = TCUtils.dip2px(this, 30);
        pickDialog.getWindow().setAttributes(lp);
        pickDialog.show();
    }

    private void showLyXiazhuDialog(int pos, int playcnt) {
        LyLandXiaZhuDialog jincaiDialog = new LyLandXiaZhuDialog(this, pos, playcnt);
        jincaiDialog.show();
        jincaiDialog.setCanceledOnTouchOutside(true);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = jincaiDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM | Gravity.RIGHT);
        lp.width = TCUtils.dipToPx(this, 400); //设置宽度
        lp.height = (display.getHeight() - TCUtils.dipToPx(this, 70));
        jincaiDialog.getWindow().setAttributes(lp);
        jincaiDialog.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                MobclickAgent.onEvent(LivePlayInPCActivity.this, "10067");
                showJoinSuccess();
            }
        });
    }

    private void showJoinSuccess() {
        showJoinResultDialog(0);
    }

    private void showJoinResultDialog(int style) {
        final Dialog pickDialog = new Dialog(this, R.style.floag_dialog);
        pickDialog.setContentView(R.layout.jincai_result_dialog);
        FlashView jflashView = (FlashView) pickDialog.findViewById(R.id.jincai_flash_view);
        jflashView.setVisibility(View.VISIBLE);
        if (style == 0) {
            jflashView.reload("gold", "flashAnims");
            jflashView.play("gold", FlashDataParser.FlashLoopTimeOnce);
        } else if (style == 1) {
            jflashView.reload("win", "flashAnims");
            jflashView.play("win", FlashDataParser.FlashLoopTimeOnce);
        } else {
            jflashView.reload("lose", "flashAnims");
            jflashView.play("lose", FlashDataParser.FlashLoopTimeOnce);
        }
        jflashView.setScale(0.6f, 0.6f);
        jflashView.setEventCallback(new FlashDataParser.IFlashViewEventCallback() {
            @Override
            public void onEvent(FlashDataParser.FlashViewEvent e, FlashDataParser.FlashViewEventData data) {
                if (e == FlashDataParser.FlashViewEvent.ONELOOPEND) {
                    pickDialog.dismiss();
                }
            }
        });
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.CENTER);
        lp.width = TCUtils.dip2px(this, 300);
        pickDialog.getWindow().setAttributes(lp);
        pickDialog.show();
    }

    /**
     * 私信
     */
    MessageDialog messageDialog;

    private void showMessageDialog() {
        messageDialog = new MessageDialog(this, true, roomInfo);
        Window dlgwin = messageDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        dlgwin.setGravity(Gravity.BOTTOM);
        dlgwin.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        UiUtil.initialize(this);
        int screenWidth = UiUtil.getScreenWidth();
        int screenHeight = UiUtil.getScreenHeight();
        lp.width = screenWidth;
        lp.height = (int) (screenHeight * 0.65);
        dlgwin.setAttributes(lp);
        messageDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                int news_count = SharePreferenceUtil.getSessionInt(LivePlayInPCActivity.this, "news_count");
                if (news_count > 0) {
                    if (land_view != null) {
                        land_view.setImgMsgNewVis(true);
                    }
                } else {
                    if (land_view != null) {
                        land_view.setImgMsgNewVis(false);
                    }
                }
            }
        });
        messageDialog.show();
    }

    /**
     * 发消息弹出框
     */
    private void showInputMsgDialog() {
        mInputTextMsgDialog = new TCInputTextMsgDialog(this, R.style.InputDialog);
        mInputTextMsgDialog.setmOnTextSendListener(this);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mInputTextMsgDialog.getWindow().getAttributes();

        lp.width = (int) (display.getWidth()); //设置宽度
        mInputTextMsgDialog.getWindow().setAttributes(lp);
        mInputTextMsgDialog.setCancelable(true);
        mInputTextMsgDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mInputTextMsgDialog.show();
    }

    Dialog pickDialog3;

    private void showLiveShareDialog(boolean isPcLand) {
        pickDialog3 = new Dialog(this, R.style.color_dialog);
        if (isPcLand) {
            pickDialog3.setContentView(R.layout.live_share_gray_dialog);
        } else {
            pickDialog3.setContentView(R.layout.start_live_share);
        }


        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog3.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = (int) (display.getWidth()); //设置宽度
        pickDialog3.getWindow().setAttributes(lp);

        ImageView tv_share_friends = (ImageView) pickDialog3.findViewById(R.id.tv_share_friends);
        ImageView tv_share_weibo = (ImageView) pickDialog3.findViewById(R.id.tv_share_weibo);
        ImageView tv_share_wechat = (ImageView) pickDialog3.findViewById(R.id.tv_share_wechat);
        ImageView tv_share_qzone = (ImageView) pickDialog3.findViewById(R.id.tv_share_qzone);
        ImageView tv_share_qq = (ImageView) pickDialog3.findViewById(R.id.tv_share_qq);
        tv_share_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(LivePlayInPCActivity.this, "10029");
                share(SHARE_MEDIA.WEIXIN_CIRCLE);
            }
        });
        tv_share_weibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(LivePlayInPCActivity.this, "10027");
                share(SHARE_MEDIA.SINA);
            }
        });
        tv_share_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(LivePlayInPCActivity.this, "10028");
                share(SHARE_MEDIA.WEIXIN);
            }
        });
        tv_share_qzone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(SHARE_MEDIA.QZONE);
            }
        });
        tv_share_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(LivePlayInPCActivity.this, "10030");
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPerssion()) {
                        share(SHARE_MEDIA.QQ);
                    }
                } else {
                    share(SHARE_MEDIA.QQ);
                }
            }
        });
        pickDialog3.show();
    }

    private boolean checkPerssion() {
        if (ActivityCompat.checkSelfPermission(LivePlayInPCActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 需要弹出dialog让用户手动赋予权限
            ActivityCompat.requestPermissions(LivePlayInPCActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1010);
            return false;
        } else {
            return true;
        }
    }

    private void share(SHARE_MEDIA plat) {
        if (roomInfo != null && roomInfo.getMasterDataList() != null) {
            String headUrl = roomInfo.getMasterDataList().getHeadUrl();
            String nickName = roomInfo.getMasterDataList().getNickName();
            String title = roomInfo.getMasterDataList().getRoomName();
            if (TextUtils.isEmpty(nickName)) {
                nickName = "";
            }

            ShareUtil.ShareWeb(this, "精彩就不间断，主播带你嗨，还有幸运礼包等你拿，赶快来看看！", nickName + "【" + title + "】,火热直播中！",
                    headUrl, TCConstants.REGISTER_URL, plat, umShareListener);
        }
    }

    UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            showLoading();
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            int to = 0;
            if (share_media.equals(SHARE_MEDIA.WEIXIN_CIRCLE)) {
                to = 1;
            } else if (share_media.equals(SHARE_MEDIA.SINA)) {
                to = 2;
            } else if (share_media.equals(SHARE_MEDIA.WEIXIN)) {
                to = 3;
            } else if (share_media.equals(SHARE_MEDIA.QZONE)) {
                to = 4;
            } else if (share_media.equals(SHARE_MEDIA.QQ)) {
                to = 5;
            }
            mPresenter.logShare(1, to);
            onToast("分享成功");
            hideLoading();
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            onToast("分享失败");
            hideLoading();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            hideLoading();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private void btnSwitchClick(boolean isLand) {
        if (roomInfo.getMasterDataList().isFollowed()) {
            mPresenter.uncare(userId);
        } else {
            if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                if (BuildConfig.isForceLoad.equals("1")) {
                    loginDialogCount = 0;
                    CustomDialogUtils.showQZLoginDialog(LivePlayInPCActivity.this, isLand);
                    CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                        @Override
                        public void cancleClick() {
                            loginDialogCount = 1;
                        }

                        @Override
                        public void loginSuccess() {
                            if (roomId != null) {
                                mPresenter.joinRoom(roomId, "0", false);
                            }
                        }
                    });
                } else {
                    CustomDialogUtils.showLoginDialog(LivePlayInPCActivity.this, isLand);
                }


            } else {
                mPresenter.care(userId);
            }
        }
    }


    public interface FragmentViewPagerAdapterListener {
        void onitemClick(String userId, boolean isBangdan);

        void onDanmuClick(String roomId);
    }

    private LiveChatFragment liveChatFragment;

    public class FragmentViewPagerAdapter extends FragmentStatePagerAdapter {
        private List<String> titles;
        private JoinRoomResponce.JoinRoomData roomInfo;

        public FragmentViewPagerAdapter(FragmentManager manager, List<String> titles, JoinRoomResponce.JoinRoomData roomInfo) {
            super(manager);
            this.titles = titles;
            this.roomInfo = roomInfo;

        }

        private FragmentViewPagerAdapterListener mListener;

        public void setFragmentViewPagerAdapterListener(FragmentViewPagerAdapterListener listener) {
            mListener = listener;
        }

        private boolean isFirst = true;

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    MobclickAgent.onEvent(LivePlayInPCActivity.this, "10021");
                    if (liveChatFragment == null) {
                        liveChatFragment = LiveChatFragment.getInstance(roomInfo, LivePlayInPCActivity.this);
                        liveChatFragment.setOnChatClickListener(new LiveChatFragment.OnChatClickListener() {
                            @Override
                            public void listClick(String userID) {
                                if (mListener != null) {
                                    mListener.onitemClick(userID, false);
                                }
                            }

                            @Override
                            public void danmuClick(String roomId) {
                                if (mListener != null) {
                                    mListener.onDanmuClick(roomId);
                                }
                            }

                            @Override
                            public void showLogin(final boolean isFromEdit) {
                                if (isFromEdit) {
                                    if (!isFirst) {
                                        return;
                                    }
                                }
                                loginDialogCount = 0;
                                CustomDialogUtils.showQZLoginDialog(LivePlayInPCActivity.this, false);
                                if (isFromEdit) {
                                    isFirst = false;
                                }
                                CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                                    @Override
                                    public void cancleClick() {
                                        loginDialogCount = 1;
                                        if (isFromEdit) {
                                            isFirst = true;
                                        }
                                    }

                                    @Override
                                    public void loginSuccess() {
                                        if (roomInfo != null) {
                                            mPresenter.joinRoom(roomInfo.getRoomId(), "0", false);
                                        }
                                        if (isFromEdit) {
                                            isFirst = true;
                                        }
                                    }
                                });
                            }

                            @Override
                            public void jcClick() {
                                viewPager.setCurrentItem(2);
                                if (luckyPoolFragment != null) {
                                    luckyPoolFragment.jcClick();
                                }
                            }

                            @Override
                            public void lyClick() {
                                viewPager.setCurrentItem(2);
                                if (luckyPoolFragment != null) {
                                    luckyPoolFragment.lyClick();
                                }
                            }
                        });
                    }
                    return liveChatFragment;
                case 1:
                    if (messageInPlayFragment == null) {
                        messageInPlayFragment = MessageInPlayFragment.getInstance(roomInfo);
                    }
                    messageInPlayFragment.setOnStrClickListener(new OnStrClickListener() {
                        @Override
                        public void onItemClick(String userId) {
                            if (tabLayout != null) {
                                tabLayout.hideMsg(1);
                            }
                        }
                    });
                    messageInPlayFragment.setShowDialogListener(new MessageInPlayFragment.showDialogListener() {
                        @Override
                        public void showDialog(String sendId, String nickName, String headUrl, boolean isPcLand) {
                            initDerectMsg(sendId, nickName, headUrl, isPcLand);
                        }
                    });

                    return messageInPlayFragment;
                case 2:
                    if (luckyPoolFragment == null) {
                        luckyPoolFragment = LuckyPoolFragment.getInstance(roomInfo);
                        luckyPoolFragment.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                loginDialogCount = 0;
                                CustomDialogUtils.showQZLoginDialog(LivePlayInPCActivity.this, false);
                                CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                                    @Override
                                    public void cancleClick() {
                                        loginDialogCount = 1;
                                    }

                                    @Override
                                    public void loginSuccess() {
                                        if (roomInfo != null) {
                                            mPresenter.joinRoom(roomInfo.getRoomId(), "0", false);
                                        }
                                    }
                                });
                            }
                        });
                    }
                    return luckyPoolFragment;
                case 3:
                    MobclickAgent.onEvent(LivePlayInPCActivity.this, "10050");
                    ContributionFragment contributionFragment = ContributionFragment.getInstance(roomInfo.getMasterDataList().getUserId());
                    return contributionFragment;
                default:
                case 4:
                    MobclickAgent.onEvent(LivePlayInPCActivity.this, "10051");
                    UserOnlineFragment userOnlineFragment = UserOnlineFragment.getInstance();
                    return userOnlineFragment;
            }
        }

        @Override
        public int getCount() {
            return titles.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

    }


    private void initHandler() {
        mOnStatusCodeEventHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case GeneralEvent.PARAMETER_ERROR:
                        AppLogger.e("handleMessage: ---->输入参数错误");
                        break;
                    case GeneralEvent.SDK_INIT_SUCCESS:
                        AppLogger.e("handleMessage: ---->SDK初始化成功");
                        break;
                    case GeneralEvent.SDK_INIT_FAILED:
                        AppLogger.e("handleMessage: ---->SDK初始化失败");
                        break;
                    case AuthEvent.AUTHORIZING:
                        AppLogger.e("handleMessage: ---->正在鉴权中");
                        break;
                    case AuthEvent.SDK_EXPIRED:
                        AppLogger.e("handleMessage: ---->过期");
                        break;
                    case AuthEvent.VERSION_OLD:
                        AppLogger.e("handleMessage: ---->SDK版本过低");
                        break;
                    case AuthEvent.SDK_UNMATCHED:
                        AppLogger.e("handleMessage: ---->SDK类型不匹配");
                        break;
                    case AuthEvent.APPID_UNMATCHED:
                        AppLogger.e("handleMessage: ---->AppID类型不匹配");
                        break;
                    case AuthEvent.APPKEY_UNMATCHED:
                        AppLogger.e("handleMessage: ---->authKey 不匹配");
                        break;
                    case AuthEvent.RESP_ERROR:
                        AppLogger.e("handleMessage: ---->鉴权服务器响应错误");
                        break;
                    case AuthEvent.UNKNOWN_ERROR:
                        AppLogger.e("handleMessage: ---->未知鉴权错误");
                        break;
                    case 5401:
                        isGood = true;
                        loadingView.setVisibility(View.GONE);
                        coverImg.setImageBitmap(null);
                        coverImg.setVisibility(View.GONE);
                        pcView.setVisibility(View.GONE);
                        AppLogger.e("handleMessage: ---->请求成功");
                        if (imgFull != null) {
                            imgFull.setEnabled(true);
                        }
                        break;
                    default:
                        AppLogger.e("handleMessage: ---->" + msg.what + "  " + msg.obj.toString());
                        break;
                }
            }
        };
    }

    private void initLiveView() {
//        tv_member_counts.setVisibility(View.VISIBLE);
        String userCount = "";
        if (roomInfo.getOnlineUserCnt() >= 10000) {
            userCount = new BigDecimal((double) roomInfo.getOnlineUserCnt() / 10000).setScale(1, BigDecimal.ROUND_HALF_DOWN) + "万人";
        } else {
            userCount = roomInfo.getOnlineUserCnt() + "人";
        }
        countMember.setText(userCount);
//        tv_member_counts.setTextColor(ContextCompat.getColor(this, R.color.app_white));
//        tv_member_counts.setText(userCount);
        if (tv_name != null) {
            tv_name.setVisibility(View.VISIBLE);
        }
        mPusherNickname = roomInfo.getMasterDataList().getNickName();

        // 飞虎号
        accountId = roomInfo.getMasterDataList().getAccountId();
        tv_play_account.setVisibility(View.VISIBLE);
        tv_play_account.setText(accountId);
        ctl_btnSwitch.setVisibility(View.VISIBLE);
        TCUtils.showPicWithUrl(this, mHeadIcon, roomInfo.getMasterDataList().getHeadUrl(), R.drawable.face);

        // 观众是否关注了主播
        haveCare = roomInfo.getMasterDataList().isFollowed();
        if (haveCare) {
            // 已关注
            //  ctl_btnSwitch.setImageResource(R.drawable.icon_havecare);
            ctl_btnSwitch.setVisibility(View.GONE);
        } else {
            // 未关注
            ctl_btnSwitch.setImageResource(R.drawable.icon_focus);
        }

    }

    private void initRecommendView() {
        if (TextUtils.isEmpty(mPlayUrl)) {
            mPresenter.loadOtherRooms();
            tv_name.setText("下播");
        }
    }

    public void showBanDialog(String msg, String time) {
        final Dialog pickDialog2 = new Dialog(this, R.style.floag_dialog);
        pickDialog2.setContentView(R.layout.pop_ban_zhibo);

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog2.getWindow();
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.CENTER);
        lp.width = (int) (display.getWidth()); //设置宽度
        pickDialog2.getWindow().setAttributes(lp);

        TextView tvContent = (TextView) pickDialog2.findViewById(R.id.tv_pop);
        String str = "<font color='#5d5d5d'>对不起,您因</font>" +
                "<font color='#5d5d5d'>[</font>" + msg +
                "<font color='#5d5d5d'>]</font>" +
                "<font color='#5d5d5d'>被踢出直播间,时长为</font>" +
                time + "分钟" +
                "<font color='#5d5d5d'>.</font>";
        tvContent.setText(Html.fromHtml(str));

        TextView tvOk = (TextView) pickDialog2.findViewById(R.id.tv_ok);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                pickDialog2.dismiss();
            }
        });
        pickDialog2.setCanceledOnTouchOutside(false);
        pickDialog2.show();
    }


    /**
     * 重连
     */
    protected void reconnect(int delayMilis) {
        mOnStatusCodeEventHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (videoViewEx == null) {
                    return;
                }
                mConnectionRecover = false;
                videoViewEx.reconnect();
            }
        }, delayMilis);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == 1010) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                share(SHARE_MEDIA.QQ);
            } else {
                // Permission Denied
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (land_view != null) {
            land_view.onResume();
        }

        mIsActivityPaused = false;
        if (videoViewEx != null) {
            videoViewEx.onResume();
        }

        if (land_view != null && land_view.getVisibility() == View.VISIBLE && giftDialogView != null) {
            giftDialogView.onFreshCoin();
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (videoViewEx != null) {
            videoViewEx.onStart();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (land_view != null) {
            land_view.onPause();
        }
        mIsActivityPaused = true;
        stopPlay(false);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (videoViewEx != null) {
            videoViewEx.onStop();
        }
    }

    protected void stopPlay(boolean clearLastFrame) {
        if (videoViewEx != null) {
            videoViewEx.onPause();
        }
    }

    @OnClick({R.id.btn_mv, R.id.img_live_exit, R.id.btn_share, R.id.btn_full, R.id.ctl_btnSwitch, R.id.mHeadIcon, R.id.video_frm, R.id.rec_left, R.id.rec_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_mv:
                if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                    if (BuildConfig.isForceLoad.equals("1")) {
                        loginDialogCount = 0;
                        CustomDialogUtils.showQZLoginDialog(LivePlayInPCActivity.this, false);
                        CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                            @Override
                            public void cancleClick() {
                                loginDialogCount = 1;
                            }

                            @Override
                            public void loginSuccess() {
                                if (roomId != null) {
                                    mPresenter.joinRoom(roomId, "0", false);
                                }
                            }
                        });
                    } else {
                        CustomDialogUtils.showLoginDialog(LivePlayInPCActivity.this, false);
                    }
                } else {
                    Intent intent = new Intent(LivePlayInPCActivity.this, PostDemandActivity.class);
                    intent.putExtra("userId", roomInfo.getMasterDataList().getUserId());
                    startActivity(intent);
                }
                break;
            case R.id.img_live_exit:
                if (Build.VERSION.SDK_INT >= 23) {
                    askForPermission();
                } else {
                    if (AlterWindowUtil.checkAlertWindowsPermission(this) && isGood && showWindow) {
                        showVideoWindow();
                        finish();
                    } else {
                        quitroom();
                        finish();
                    }

                }
                break;
            case R.id.btn_share:
                showLiveShareDialog(false);
                break;
            case R.id.btn_full:
                if (!imgFull.isEnabled()) {
                    onToast("当前不支持切换横屏！");
                    return;
                }
                try {
                    if (allRoomMessage != null) {
                        allRoomMessage.clear();
                    }
                    if (allChatMessage != null) {
                        allChatMessage.clear();
                    }
                    if (LiveChatFragment.liveChatFragment != null) {
                        LiveChatFragment.liveChatFragment.clearDanmuMessage();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                MobclickAgent.onEvent(LivePlayInPCActivity.this, "10052");
                if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    // 如果是横屏,则改为竖屏
                    LiveChatFragment.setFullScreen(false);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    // 如果是竖屏,则改为横排
                    if (land_view != null) {
                        if (roomInfo != null) {
                            land_view.setTcRoomInfo(roomInfo);
                        }
                    }
                    if (isGood && land_view != null) {
                        land_view.loadRoomCri();
                        LiveChatFragment.setFullScreen(true);
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    }
                    flashview.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.ctl_btnSwitch:
                btnSwitchClick(false);
                break;
            case R.id.mHeadIcon:
                //点击主播头像
                mPresenter.showUserInfo(roomInfo, roomInfo.getMasterDataList().getUserId(), false, false, true);
                break;
            case R.id.video_frm:
                //隐藏view
                hideOrShowView();
                break;
            case R.id.rec_left:
                if (datas.size() >= 2) {
                    mPresenter.leaveRoom(1);

                }
                break;
            case R.id.rec_right:
                if (datas.size() >= 2) {
                    mPresenter.leaveRoom(2);

                }
                break;
        }
    }

    private void hideOrShowView() {
        if (rlControllLayer.getVisibility() == View.VISIBLE) {
            rlControllLayer.setVisibility(View.INVISIBLE);
        } else {
            rlControllLayer.setVisibility(View.VISIBLE);
        }
        if (LiveChatFragment.liveChatFragment != null) {
            LiveChatFragment.liveChatFragment.hideSoftInputFromWindow();
        }
        if (luckyPoolFragment != null) {
            luckyPoolFragment.setCaichiViewGone();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent rstData = new Intent();
        int memberCount = mCurrentMemberCount - 1;
        rstData.putExtra(TCConstants.MEMBER_COUNT, memberCount >= 0 ? memberCount : 0);
        rstData.putExtra(TCConstants.ROOM_ID, roomId);
        setResult(0, rstData);
        finish();
        if (Build.VERSION.SDK_INT >= 23) {
            askForPermission();
        } else {
            if (AlterWindowUtil.checkAlertWindowsPermission(this) && isGood && showWindow) {
                showVideoWindow();
            } else {
                quitroom();
            }
        }
    }

    /**
     * 请求用户给予悬浮窗的权限
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void askForPermission() {
        if (!Settings.canDrawOverlays(this)) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);

            } catch (Exception e) {
                e.printStackTrace();
            }
            quitroom();
            finish();
        } else {
            if (isGood && showWindow) {
                showVideoWindow();
                finish();
            } else {
                quitroom();
                finish();
            }

        }
    }

    private void showVideoWindow() {
        if (!SharePreferenceUtil.getSessionBoolean(this, "xuanfuchuan", true)) {
            quitroom();
            return;
        }
        FloatViewWindowManger.createSmallWindow(getApplicationContext(), mPlayUrl, roomId, headImgUrl, 1);
    }

    private void quitroom() {
        mPresenter.leaveRoom(0);
    }

    ///房间聊天消息推送
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_ROOM_CHAT, threadMode = ThreadMode.MAIN)
    public void onReceiveRoomChatPush(RoomChatPush pushData) {
        if (land_view != null && land_view.getVisibility() == View.VISIBLE) {
            allChatMessage.add(pushData);
        }
    }

    ///加入房间消息推送
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_NOTICE_JOIN_ROOM, threadMode = ThreadMode.MAIN)
    public void onReceiveJoinRoomPush(NoticeJoinRoomPush pushData) {
        int totalNumber = pushData.getTotalRoomMembers();
        int mountId = pushData.getMount();
        int contri1 = pushData.getContri();
        if (mountId != 0) {
            GiftModel giftModel = new GiftModel("", 0, " ", pushData.getSender().getNickName(), "", mountId, false);
            SysMountNewBean mountBean = mPresenter.getMountBeanByID(String.valueOf(mountId));
            if (mountBean != null && mountBean.getIsAnimation().equals("1") && TextUtils.isEmpty(mountBean.getAnimName())) {
                vipGiftModels.add(giftModel);
            } else {

            }
        }
        String userCount = "";
        if (totalNumber >= 10000) {
            userCount = new BigDecimal((double) totalNumber / 10000).setScale(1, BigDecimal.ROUND_HALF_DOWN) + "万人";
        } else {
            userCount = totalNumber + "人";
        }
//        tv_member_counts.setText(userCount);
        if (land_view != null && land_view.getVisibility() == View.VISIBLE) {
            land_view.getTvMemberCountsLand(userCount);
        }
        int level = pushData.getSender().getLevel();
        String joinUserId = pushData.getSender().getUserId();
        String headUrl = pushData.getSender().getHeadUrl();
        if (land_view != null && land_view.getVisibility() == View.VISIBLE) {
            land_view.sortContriList(joinUserId, contri1, headUrl);
            TCChatEntity entity = new TCChatEntity();
            entity.setSenderName("" + pushData.getSender().getNickName() + "  ");
            if (mountId != 0) {
                SysMountNewBean mountBean = mPresenter.getMountBeanByID(String.valueOf(mountId));
                if (mountBean != null) {
                    entity.setZuojia("驾驶" + mountBean.getName());
                    entity.setContext("进入直播间");
                } else {
                    entity.setZuojia("");
                    entity.setContext("进入直播间");
                }
                entity.setType(TCConstants.JOIN_TYPE);
                if (mountBean != null) {
                    if (land_view != null && land_view.getVisibility() == View.VISIBLE) {
                        if (!mountBean.getIsAnimation().equals("1")) {
                            GiftModel giftModel = new GiftModel(joinUserId, 0, "驾驶[" + mountBean.getName() + "]进入直播间", pushData.getSender().getNickName(), headUrl, mountId, false);
                            staticGiftModels.add(giftModel);
                        } else {
                            if (!TextUtils.isEmpty(mountBean.getAnimName())) {
                                SysGiftNewBean giftBean = new SysGiftNewBean();
                                giftBean.setAnimName(mountBean.getAnimName());
                                giftBean.setMount(true);
                                giftBean.setIs520(false);
                                giftBean.setIs1314(false);
                                animGiftQune.add(0, giftBean);
                            }
                        }
                    } else {
                        if (mountBean.getIsAnimation().equals("1") && !TextUtils.isEmpty(mountBean.getAnimName())) {
                            SysGiftNewBean giftBean = new SysGiftNewBean();
                            giftBean.setAnimName(mountBean.getAnimName());
                            giftBean.setMount(true);
                            giftBean.setIs520(false);
                            giftBean.setIs1314(false);
                            animGiftQune.add(0, giftBean);
                        }
                    }
                }
            } else {
                entity.setContext("进入直播间");
                entity.setType(TCConstants.CONCERN_TYPE);
            }
            entity.setGuardType(pushData.getSender().getGuardType());
            entity.setGuardExpired(pushData.getSender().isGuardExpired());
            entity.setVip(pushData.getSender().getVip());
            entity.setVipExpired(pushData.getSender().isVipExpired());
            entity.setAccountId(pushData.getSender().getShowId());
            entity.setLiang(pushData.getSender().isLiang());
            entity.setLevel(level);
            entity.setUserId(joinUserId);
            land_view.reciveMessage(entity);

            if (pushData.getSender().getVip() > 0 || pushData.getSender().getGuardType() != 0) {
                if (!pushData.getSender().isVipExpired() || !pushData.getSender().isGuardExpired()) {
                    if (!TextUtils.isEmpty(entity.getZuojia())) {
                        DanmuModel model = new DanmuModel(headUrl, level,
                                pushData.getSender().getNickName() + entity.getZuojia() + "进入直播间~", pushData.getSender().getVip(),
                                pushData.getSender().isVipExpired(), pushData.getSender().getGuardType(),
                                pushData.getSender().isGuardExpired(), pushData.getSender().isLiang(), pushData.getSender().getShowId());
                        land_view.setVipDanmu(model);
                    } else {
                        DanmuModel model = new DanmuModel(headUrl, level,
                                pushData.getSender().getNickName() + "进入直播间~", pushData.getSender().getVip(),
                                pushData.getSender().isVipExpired(), pushData.getSender().getGuardType(),
                                pushData.getSender().isGuardExpired(), pushData.getSender().isLiang(), pushData.getSender().getShowId());
                        land_view.setVipDanmu(model);
                    }

                }
            }
        }
    }

    ///离开房间消息推送
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_NOTICE_LEAVE_ROOM, threadMode = ThreadMode.MAIN)
    public void onReceiveLeaveRoomPush(NoticeLeaveRoomPush pushData) {
        int totalNumber1 = pushData.getTotalRoomMembers();
        String uid = pushData.getUid();
        if (land_view != null) {
            land_view.deleteContriList(uid);
        }
        String userCount1 = "";
        if (totalNumber1 >= 10000) {
            userCount1 = new BigDecimal((double) totalNumber1 / 10000).setScale(1, BigDecimal.ROUND_HALF_DOWN) + "万人";
        } else {
            userCount1 = totalNumber1 + "人";
        }
        countMember.setText(userCount1);
    }

    ///直播房间内拉流地址变化
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_UPDATE_PLAY_URL, threadMode = ThreadMode.MAIN)
    public void onReceiveUpdatePlayUrlPush(UpdatePlayUrlPush pushData) {
        String playUrl = pushData.getPlayUrl();
        boolean roomstate = pushData.isRoomStatus();
        mPlayUrl = playUrl;
        if (roomstate && !TextUtils.isEmpty(playUrl)) {
            videoViewEx.play(mPlayUrl);
            if (recommendView != null) {
                recommendView.setVisibility(View.GONE);
                rlControllLayer.setVisibility(View.VISIBLE);
                if (land_view != null) {
                    land_view.setReccommendGone();
                }
            }
            tv_name.setText("直播中");
        } else {
            tv_name.setText("下播");
            if (recommendView != null) {
                initRecommendView();
            }
            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                // 如果是横屏,则改为竖屏
                LiveChatFragment.setFullScreen(false);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                if (landJincaiDialog != null) {
                    if (landJincaiDialog.isShowing()) {
                        landJincaiDialog.dismiss();
                    }
                }
                if (caichiDialog != null) {
                    if (caichiDialog.isShowing()) {
                        caichiDialog.dismiss();
                    }
                }
                if (gusessDialog != null) {
                    if (gusessDialog.isShowing()) {
                        gusessDialog.dismiss();
                    }
                }
                if (shakeDialog != null) {
                    if (shakeDialog.isShowing()) {
                        shakeDialog.dismiss();
                    }
                }

                if (giftDialogView != null) {
                    if (giftDialogView.isShowing()) {
                        giftDialogView.dismiss();
                    }
                }

                if (pickDialog3 != null) {
                    if (pickDialog3 != null) {
                        pickDialog3.dismiss();
                    }
                }

                if (messageDialog != null) {
                    if (messageDialog.isShowing()) {
                        messageDialog.dismiss();
                    }
                }

                if (pickDialogHelp != null) {
                    if (pickDialogHelp.isShowing()) {
                        pickDialogHelp.dismiss();
                    }
                }

                if (jincaixiazhuDialog != null) {
                    if (jincaixiazhuDialog.isShowing()) {
                        jincaixiazhuDialog.dismiss();
                    }
                }
            }
        }
    }

    ///主播被关注
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_ROOM_FOLLOW, threadMode = ThreadMode.MAIN)
    public void onReceiveRoomFollowPush(RoomFollowPush pushData) {
        String userId = pushData.getSender().getUserId();
        String nickname = pushData.getSender().getNickName();
        int level = pushData.getSender().getLevel();
        if (land_view != null && land_view.getVisibility() == View.VISIBLE) {
            TCChatEntity entity = new TCChatEntity();
            entity.setSenderName("  " + nickname + "    ");
            entity.setContext("关注了主播");
            entity.setUserId(userId);
            entity.setLevel(level);
            entity.setGuardType(pushData.getSender().getGuardType());
            entity.setGuardExpired(pushData.getSender().isGuardExpired());
            entity.setVip(pushData.getSender().getVip());
            entity.setVipExpired(pushData.getSender().isVipExpired());
            entity.setAccountId(pushData.getSender().getShowId());
            entity.setLiang(pushData.getSender().isLiang());
            entity.setType(TCConstants.CONCERN_TYPE);
            land_view.reciveMessage(entity);
        }
    }

    ///用户被禁言
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_ROOM_BAN, threadMode = ThreadMode.MAIN)
    public void onReceiveRoomBanPush(RoomBanPush pushData) {
        String userId = pushData.getTarget().getUserId();
        String nickname = pushData.getTarget().getNickName();
        int level = pushData.getTarget().getLevel();
        String banName = pushData.getSender().getNickName();
        if (land_view != null && land_view.getVisibility() == View.VISIBLE) {
            TCChatEntity entity = new TCChatEntity();
            entity.setUserId(userId);
            entity.setSenderName(nickname + "");
            entity.setGuardType(pushData.getSender().getGuardType());
            entity.setGuardExpired(pushData.getSender().isGuardExpired());
            entity.setVip(pushData.getSender().getVip());
            entity.setVipExpired(pushData.getSender().isVipExpired());
            entity.setAccountId(pushData.getSender().getShowId());
            entity.setLiang(pushData.getSender().isLiang());
            entity.setBanName("被" + banName);
            entity.setContext("禁言30分钟");
            entity.setLevel(level);
            entity.setType(TCConstants.BAN_TYPE);
            land_view.reciveMessage(entity);
        }
    }

    ///收到礼物
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_ROOM_GIFT, threadMode = ThreadMode.MAIN)
    public void onReceiveRoomGiftPush(RoomGiftPush pushData) {
        int contri = pushData.getContri();
        int giftid = pushData.getGiftId();
        int giftcount = pushData.getGiftCnt();


        String userId = pushData.getSender().getUserId();
        String nickname = pushData.getSender().getNickName();
        String headUrl = pushData.getSender().getHeadUrl();
        if (land_view != null && land_view.getVisibility() == View.VISIBLE) {
            land_view.sortContriList(userId, contri, headUrl);
            land_view.getTvHubiLand().setText(pushData.getIncome() + "");
        }
        SysGiftNewBean giftBean = mPresenter.getGiftBeanByID(String.valueOf(giftid));
        if (giftBean == null) {
            return;
        }
        if ((pushData.getSender().getVip() > 0 && !pushData.getSender().isVipExpired()) || (pushData.getSender().getGuardType() > 0 && !pushData.getSender().isGuardExpired())) {
            giftBean.setVip(true);
        }
        if (giftBean.getIsAnimation().equals("1")) {
            animGiftQune.add(giftBean);
        }
        if (!giftBean.getIsAnimation().equals("1") && giftcount == 520) {
            giftBean.setIs520(true);
            giftBean.setIs1314(false);
            animGiftQune.add(giftBean);

        }
        if (!giftBean.getIsAnimation().equals("1") && giftcount == 1314) {
            giftBean.setIs520(false);
            giftBean.setIs1314(true);
            animGiftQune.add(giftBean);

        }

        if (land_view != null && land_view.getVisibility() == View.VISIBLE) {
            if (!giftBean.getIsAnimation().equals("1")) {
                if ((pushData.getSender().getVip() > 0 && !pushData.getSender().isVipExpired()) || (pushData.getSender().getGuardType() > 0 && !pushData.getSender().isGuardExpired())) {
                    GiftModel giftModel = new GiftModel(userId, giftcount, giftBean.getName(), nickname, headUrl, giftid, true);
                    staticGiftModels.add(giftModel);
                } else {
                    GiftModel giftModel = new GiftModel(userId, giftcount, giftBean.getName(), nickname, headUrl, giftid, false);
                    staticGiftModels.add(giftModel);
                }
            }
        }
    }

    ///用户被禁止观看
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_BANNED_JOIN_ROOM, threadMode = ThreadMode.MAIN)
    public void onReceiveBannedJoinRoomPush(BannedJoinRoomPush pushData) {
        String msg1 = pushData.getMsg();
        String duration = pushData.getDuration();
        showBanDialog(msg1, duration);
    }

    ///全站消息
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_ALL_ROOM_MSG, threadMode = ThreadMode.MAIN)
    public void onReceiveAllRoomMsgPush(AllRoomMsgPush pushData) {
        allRoomMessage.add(pushData);
    }

    ///彩池倒计时
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_JACKPOT_GIFT_COUNTDOWN, threadMode = ThreadMode.MAIN)
    public void onReceiveJackpotGiftCountdownPush(JackpotGiftCountdownPush pushData) {
        int gid = pushData.getGiftId();
        if (gid == 1) {
            textCaichiOpen.setText("距离幸运黄瓜开奖");
            if (land_view != null) {
                land_view.setCaichiOpenLand("距离幸运黄瓜开奖");
            }
        } else if (gid == 2) {
            textCaichiOpen.setText("距离幸运香蕉开奖");
            if (land_view != null) {
                land_view.setCaichiOpenLand("距离幸运香蕉开奖");
            }
        } else if (gid == 8) {
            textCaichiOpen.setText("距离幸运爱心开奖");
            if (land_view != null) {
                land_view.setCaichiOpenLand("距离幸运爱心开奖");
            }
        } else if (gid == 20) {
            textCaichiOpen.setText("距离飞虎流星开奖");
            if (land_view != null) {
                land_view.setCaichiOpenLand("距离飞虎流星开奖");
            }
        }
        caichicountDown = 600;
    }

    //直播状态发生变化
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_UPDATE_PLAY_STATUS, threadMode = ThreadMode.MAIN)
    public void onReceiveUpdatePlayStatusPush(UpdatePlayStatusPush pushData) {

    }

    ///房管更新
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_ROOM_MGR, threadMode = ThreadMode.MAIN)
    public void onReceiveRoomMgrPush(RoomMgrPush pushData) {
        boolean status = pushData.isStatus();
        String userId = pushData.getTarget().getUserId();
        String nickname = pushData.getTarget().getNickName();
        int level = pushData.getTarget().getLevel();
        if (status) {
            if (land_view != null) {
                TCChatEntity entity = new TCChatEntity();
                entity.setSenderName(nickname + "    ");
                entity.setContext("成为场控");
                entity.setUserId(userId);
                entity.setLevel(level);
                entity.setGuardType(pushData.getTarget().getGuardType());
                entity.setGuardExpired(pushData.getTarget().isGuardExpired());
                entity.setVip(pushData.getTarget().getVip());
                entity.setVipExpired(pushData.getTarget().isVipExpired());
                entity.setAccountId(pushData.getTarget().getShowId());
                entity.setLiang(pushData.getTarget().isLiang());
                entity.setType(TCConstants.CONCERN_TYPE);
                land_view.reciveMessage(entity);
            }
            if (userId.equals(SharePreferenceUtil.getSession(LivePlayInPCActivity.this, "PREF_KEY_USERID"))) {
                if (roomInfo != null) {
                    roomInfo.setRoomMgr(true);
                }
            }
        } else {
            if (userId.equals(SharePreferenceUtil.getSession(LivePlayInPCActivity.this, "PREF_KEY_USERID"))) {
                if (roomInfo != null) {
                    roomInfo.setRoomMgr(false);
                }
            }
        }
    }

    //收到消息
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_NOTICE_SECRET_MESSAGE, threadMode = ThreadMode.MAIN)
    public void onReceiveLoadMsgListDataPush(LoadMsgListResponce.LoadMsgListData pushData) {
        if (tabLayout != null && currentPos != 1) {
            tabLayout.showDot(1);
        }
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            if (messageDialog != null) {
                messageDialog.update(pushData.getTime(), pushData.getContent(), pushData.getUserId(), pushData.getHeadUrl(), pushData.getNickName());
            }

            if (land_view != null) {
                land_view.setImgMsgNewVis(true);
            }
        } else {
            if (messageInPlayFragment != null) {
                messageInPlayFragment.updataMessage(pushData);
            }
        }
        if (derectMsgDialog != null) {
            derectMsgDialog.update(pushData.getTime(), pushData.getContent(), pushData.getUserId(), pushData.getHeadUrl());
        }


    }

    //游戏结果揭晓
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_GAME_RESULT, threadMode = ThreadMode.MAIN)
    public void onReceiveGameResultPush(GameResultPush pushData) {
        String json = pushData.getResult().toString();
        gameName1 = pushData.getGameName();
        if (gameName1.equals("yxjc")) {
            JcresultCount = 1;
        } else {
            LyresultCount = 1;
        }
        issueRound = pushData.getIssueRound();
        if (gameName1.equals("yxjc")) {
            yxjcIssueRound = issueRound;
            jcGameResultJson = json;
        } else {
            lyjcIssueRound = issueRound;
            lyGameResultJson = json;
        }
    }

    //切换网络
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_NOTICE_NETWORK_CHANGE, threadMode = ThreadMode.MAIN)
    public void onReceiveNetworkChangePush(NetworkChangePush push) {
        if (push.getCode() == -1) {
            showComfirmDialog("无网络，确定退出直播间", true);
            mConnectionError = true;
        } else {
            if (push.getCode() == 0) {
                showNetChangeDialog();

            }
            //网络从错误中恢复，或者网络连接有变化，即做重连准备
            if (mConnectionError || mLastConnectionType != CONNECTION_INIT) {
                mConnectionError = false;
                mConnectionRecover = true;
                if (videoViewEx != null) {
                    onToast("网络恢复，1s后尝试重连...");
                    reconnect(1000);
                }
            }
            mLastConnectionType = push.getCode();
        }
    }


    RxDialogSureCancel comfirmDialog;

    public void showComfirmDialog(String msg, final Boolean isError) {
        comfirmDialog = new RxDialogSureCancel(this);//提示弹窗
        comfirmDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return true;
            }
        });
        comfirmDialog.setContent(msg);
        comfirmDialog.setCancel("取消");
        comfirmDialog.setSure("确定");

        if (!isError) {
            comfirmDialog.getTvSure().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    comfirmDialog.dismiss();
                    Intent rstData = new Intent();
                    int memberCount = mCurrentMemberCount - 1;
                    rstData.putExtra(TCConstants.MEMBER_COUNT, memberCount >= 0 ? memberCount : 0);
                    rstData.putExtra(TCConstants.ROOM_ID, roomId);
                    setResult(0, rstData);
                    quitroom();
                    finish();
                }
            });
            comfirmDialog.getTvCancel().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    comfirmDialog.dismiss();
                }
            });
        } else {
            comfirmDialog.getTvSure().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //当情况为错误的时候，直接停止拉流
                    Intent rstData = new Intent();
                    int memberCount = mCurrentMemberCount - 1;
                    rstData.putExtra(TCConstants.MEMBER_COUNT, memberCount >= 0 ? memberCount : 0);
                    rstData.putExtra(TCConstants.ROOM_ID, roomId);
                    setResult(0, rstData);
                    quitroom();
                    finish();
                    comfirmDialog.dismiss();
                }
            });
            comfirmDialog.getTvCancel().setVisibility(View.GONE);
        }
        comfirmDialog.setCanceledOnTouchOutside(false);
        comfirmDialog.show();

    }

    public void showNetChangeDialog() {
        SPManager.onPause();
        final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(LivePlayInPCActivity.this);//提示弹窗
        rxDialogSureCancel.setContent(R.string.tip_4g);
        rxDialogSureCancel.setCancel("取消");
        rxDialogSureCancel.setSure("确定");
        rxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitroom();
                rxDialogSureCancel.cancel();
            }
        });
        rxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPManager.startPushStream();
                rxDialogSureCancel.cancel();
            }
        });
        rxDialogSureCancel.setCanceledOnTouchOutside(false);
        rxDialogSureCancel.show();
    }

    ///数值变化
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_VALUE_CHANGE, threadMode = ThreadMode.MAIN)
    public void onReceiveValueChangePush(ValueChangePush pushData) {
        if (pushData.getType().equals("Xiaolaba")) {
            if (mInputTextMsgDialog != null) {
                mInputTextMsgDialog.onFreshXiaolabaCount();
            }
        }


    }

    @RxBusSubscribe(code = RxBusCode.RX_BUS_NOTICE_CODE_GUARD, threadMode = ThreadMode.MAIN)
    public void onReceiveGuard() {

        if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
            if (BuildConfig.isForceLoad.equals("1")) {
                loginDialogCount = 0;
                if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    CustomDialogUtils.showQZLoginDialog(LivePlayInPCActivity.this, true);
                } else {
                    CustomDialogUtils.showQZLoginDialog(LivePlayInPCActivity.this, false);
                }
                CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                    @Override
                    public void cancleClick() {
                        loginDialogCount = 1;
                    }

                    @Override
                    public void loginSuccess() {
                        if (roomId != null) {
                            mPresenter.joinRoom(roomId, "0", false);
                        }
                    }
                });
            } else {
                CustomDialogUtils.showLoginDialog(LivePlayInPCActivity.this, true);
            }
        } else {
            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                // 如果是横屏,则改为竖屏
                LiveChatFragment.setFullScreen(false);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            GuardDialogFragment dialogFragment = GuardDialogFragment.newInstance(roomInfo.getMasterDataList().getHeadUrl(),
                    roomInfo.getMasterDataList().getNickName(), roomInfo.getRoomId(), roomInfo.getMasterDataList().getAccountId());
            dialogFragment.show(getSupportFragmentManager(), "");
        }
    }

    @RxBusSubscribe(code = RxBusCode.RX_BUS_NOTICE_CODE_VIP, threadMode = ThreadMode.MAIN)
    public void onReceiveVip() {
        if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
            if (BuildConfig.isForceLoad.equals("1")) {
                loginDialogCount = 0;
                if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    CustomDialogUtils.showQZLoginDialog(LivePlayInPCActivity.this, true);
                } else {
                    CustomDialogUtils.showQZLoginDialog(LivePlayInPCActivity.this, false);
                }
                CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                    @Override
                    public void cancleClick() {
                        loginDialogCount = 1;
                    }

                    @Override
                    public void loginSuccess() {
                        if (roomId != null) {
                            mPresenter.joinRoom(roomId, "0", false);
                        }
                    }
                });
            } else {
                CustomDialogUtils.showLoginDialog(LivePlayInPCActivity.this, true);
            }
        } else {
            if (SharePreferenceUtil.getSessionInt(FeihuZhiboApplication.getApplication(), "PREF_KEY_VIP") > 0) {
                if (!SharePreferenceUtil.getSessionBoolean(FeihuZhiboApplication.getApplication(), "PREF_KEY_VIPEXPIRED")) {
                    Intent intent = new Intent(LivePlayInPCActivity.this, QuickUpgradeActivity.class);
                    intent.putExtra("isFromRoom", true);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(LivePlayInPCActivity.this, MyVipActivity.class);
                    intent.putExtra("isFromRoom", true);
                    startActivity(intent);
                }
            } else {
                Intent intent = new Intent(LivePlayInPCActivity.this, MyVipActivity.class);
                intent.putExtra("isFromRoom", true);
                startActivity(intent);
            }

        }
    }

    @RxBusSubscribe(code = RxBusCode.RX_BUS_NOTICE_CODE_USERINFO, threadMode = ThreadMode.MAIN)
    public void onReceiveUserinfo(String userId) {
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            mPresenter.showUserInfo(roomInfo, userId, true, true, false);
        } else {
            mPresenter.showUserInfo(roomInfo, userId, false, true, false);
        }
        if (userDialogFragment != null) {
            userDialogFragment.dismiss();
        }
    }

    @RxBusSubscribe(code = RxBusCode.RX_BUS_NOTICE_CODE_USERINFO, threadMode = ThreadMode.MAIN)
    public void onReceiveUserinfo(Bundle bundle) {
        Boolean isbangdan = bundle.getBoolean("isBangdan");
        String id = bundle.getString("userId");
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            mPresenter.showUserInfo(roomInfo, id, true, isbangdan, false);
        } else {
            mPresenter.showUserInfo(roomInfo, id, false, isbangdan, false);
        }
        if (userDialogFragment != null) {
            userDialogFragment.dismiss();
        }
    }

    private String content;
    private String imgUrl;
    private String targetUrl;

    @RxBusSubscribe(code = RxBusCode.RX_BUS_NOTICE_CODE_SHARE, threadMode = ThreadMode.MAIN)
    public void onReceiveShare(JSONObject pushdata) {
        try {
            content = pushdata.getString("content");
            imgUrl = pushdata.getString("imgUrl");
            targetUrl = pushdata.getString("targetUrl");
            showWebShareDialog();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @RxBusSubscribe(code = RxBusCode.RX_BUS_NOTICE_CODE_ROOMINCOME, threadMode = ThreadMode.MAIN)
    public void onReceiveRoomIcome(RoomIncomePush push) {
        if (land_view != null && land_view.getVisibility() == View.VISIBLE) {
            land_view.getTvHubiLand().setText(push.getIncome() + "");
        }
    }

    private void showWebShareDialog() {
        Dialog pickDialog3 = new Dialog(this, R.style.floag_dialog);
        pickDialog3.setContentView(R.layout.live_share_gray_dialog);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog3.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = (int) (display.getWidth()); //设置宽度
        pickDialog3.getWindow().setAttributes(lp);

        ImageView tv_share_friends = (ImageView) pickDialog3.findViewById(R.id.tv_share_friends);
        ImageView tv_share_weibo = (ImageView) pickDialog3.findViewById(R.id.tv_share_weibo);
        ImageView tv_share_wechat = (ImageView) pickDialog3.findViewById(R.id.tv_share_wechat);
        ImageView tv_share_qzone = (ImageView) pickDialog3.findViewById(R.id.tv_share_qzone);
        ImageView tv_share_qq = (ImageView) pickDialog3.findViewById(R.id.tv_share_qq);
        tv_share_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                share1(SHARE_MEDIA.WEIXIN_CIRCLE);
            }
        });
        tv_share_weibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share1(SHARE_MEDIA.SINA);
            }
        });
        tv_share_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                share1(SHARE_MEDIA.WEIXIN);
            }
        });
        tv_share_qzone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share1(SHARE_MEDIA.QZONE);
            }
        });
        tv_share_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(LivePlayInPCActivity.this, "10030");
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPerssion()) {
                        share1(SHARE_MEDIA.QQ);
                    }
                } else {
                    share1(SHARE_MEDIA.QQ);
                }
            }
        });
        pickDialog3.show();
    }

    private void share1(SHARE_MEDIA plat) {
        ShareUtil.ShareWeb(this, content, "飞虎直播",
                imgUrl, targetUrl, plat, umShareListener1);
    }

    UMShareListener umShareListener1 = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            FHUtils.showToast("分享成功");
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            FHUtils.showToast("分享失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {

        }
    };
}
