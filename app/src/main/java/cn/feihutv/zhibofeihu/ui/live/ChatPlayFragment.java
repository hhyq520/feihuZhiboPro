package cn.feihutv.zhibofeihu.ui.live;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.umeng.analytics.MobclickAgent;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomContriResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.UnFollowResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.GetCurrMountResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.GetExtGameIconRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.GetExtGameIconResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.LoadOtherRoomsResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameListResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameStatusRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameStatusResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.JackpotCountDownResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseResponse;
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
import cn.feihutv.zhibofeihu.data.network.socket.model.push.UpdatePlayUrlPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.ValueChangePush;
import cn.feihutv.zhibofeihu.di.component.ActivityComponent;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.rxbus.RxBusSubscribe;
import cn.feihutv.zhibofeihu.rxbus.ThreadMode;
import cn.feihutv.zhibofeihu.ui.base.BaseFragment;
import cn.feihutv.zhibofeihu.ui.home.HotFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.ui.live.adapter.ChatMsgRecycleAdapter;
import cn.feihutv.zhibofeihu.ui.live.adapter.GameImageAdapter;
import cn.feihutv.zhibofeihu.ui.live.adapter.TCUserContriListAdapter;
import cn.feihutv.zhibofeihu.ui.live.models.GiftModel;
import cn.feihutv.zhibofeihu.ui.me.dynamic.OthersCommunityActivity;
import cn.feihutv.zhibofeihu.ui.me.recharge.RechargeActivity;
import cn.feihutv.zhibofeihu.ui.me.vip.MyVipActivity;
import cn.feihutv.zhibofeihu.ui.me.vip.QuickUpgradeActivity;
import cn.feihutv.zhibofeihu.ui.mv.demand.PostDemandActivity;
import cn.feihutv.zhibofeihu.ui.widget.DanmuBase.DanmakuActionManager;
import cn.feihutv.zhibofeihu.ui.widget.DanmuBase.DanmakuChannel;
import cn.feihutv.zhibofeihu.ui.widget.DanmuBase.DanmuModel;
import cn.feihutv.zhibofeihu.ui.widget.GuardDialogFragment;
import cn.feihutv.zhibofeihu.ui.widget.MagicTextView;
import cn.feihutv.zhibofeihu.ui.widget.TalkGiftView;
import cn.feihutv.zhibofeihu.ui.widget.bsrgift.BSRGiftLayout;
import cn.feihutv.zhibofeihu.ui.widget.bsrgift.GiftAnmManager;
import cn.feihutv.zhibofeihu.ui.widget.dialog.CaichiHiatoryDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.DerectMsgDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.GiftNewDialogView;
import cn.feihutv.zhibofeihu.ui.widget.dialog.GiftNumDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.HuDongDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.JcHistoryDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.JcXiaZhuDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.KjHistoryDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.LyHistoryDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.LyHuDongDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.LyXiaZhuDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.MessageDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.OrigCaichiDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.PlayJincaiDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.PlayShakeDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.TCInputTextMsgDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.UserDialogFragment;
import cn.feihutv.zhibofeihu.ui.widget.dialog.WeekStarDialog;
import cn.feihutv.zhibofeihu.ui.widget.flashview.FlashDataParser;
import cn.feihutv.zhibofeihu.ui.widget.flashview.FlashView;
import cn.feihutv.zhibofeihu.ui.widget.pathView.ViewPoint;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.AppUtils;
import cn.feihutv.zhibofeihu.utils.CustomDialogUtils;
import cn.feihutv.zhibofeihu.utils.FileUtils;
import cn.feihutv.zhibofeihu.utils.NumAnim;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import cn.feihutv.zhibofeihu.utils.SoftKeyBoardListener;
import cn.feihutv.zhibofeihu.utils.TCConstants;
import cn.feihutv.zhibofeihu.utils.TCDanmuMgr;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.feihutv.zhibofeihu.utils.UiUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.ui.widget.DanmakuView;

import static com.umeng.socialize.utils.ContextUtil.getPackageName;

/**
 * <pre>
 *     author : huang hao
 *     time   :
 *     desc   : 界面绘制
 *     version: 1.0
 * </pre>
 */
public class ChatPlayFragment extends BaseFragment implements ChatPlayFragmentMvpView, TCInputTextMsgDialog.OnTextSendListener, View.OnClickListener {

    @Inject
    ChatPlayFragmentMvpPresenter<ChatPlayFragmentMvpView> mPresenter;
    private JoinRoomResponce.JoinRoomData tcRoomInfo;

    private ImageView mHeadIcon;
    private TextView mtvPuserName;
    private TextView tvMemberCounts;
    private ImageView concern;
    private Button back;
    private TextView tv_play_hubi;
    private TextView tv_play_account;
    private ImageView btnMessageInput;
    private LinearLayout toolBar;
    private RelativeLayout topView;
    private LinearLayout llHubi;
    private DanmakuView danmakuView;
    private RecyclerView mListViewMsg;
    private TalkGiftView talkGiftView;
    private LinearLayout giftcontent;
    private RecyclerView mUserAvatarList;
    private FlashView flashview;
    private ImageView btnGame;
    private RelativeLayout hubiView;
    private ImageView btnMsg;
    private ImageView btnGift;
    private Button btnShare;
    private Button btnRefresh;
    private RelativeLayout rlControllLayer;
    private ImageView imgT1;
    private ImageView imgT2;
    private ImageView imgT3;
    private ImageView imgT4;
    private ImageView imgLy;
    private ImageView imgJc;
    private LinearLayout caichiTimeLeft;
    private TextView textCaichiOpen;
    private ImageView msgNew;
    private DanmakuActionManager danmakuActionManager;
    DanmakuChannel vipDanmakuView;
    private String mPusherNickname;
    private boolean haveCare;//是否关注
    private long gold;  // 主播的虎币
    private String accountId;  //飞虎号
    private String masteruserId;  // 主播的id
    private TCInputTextMsgDialog mInputTextMsgDialog;
    private TCDanmuMgr mDanmuMgr;
    private ChatMsgRecycleAdapter mChatMsgListAdapter;
    private ArrayList<TCChatEntity> mArrayListChatEntity = new ArrayList<>();
    private int loginDialogCount = 1;
    private TCUserContriListAdapter mAvatarListAdapter;
    private int caichicountDown;
    private List<GiftModel> staticGiftModels = new ArrayList<>();
    private List<GiftModel> vipGiftModels = new ArrayList<>();
    private List<SysGiftNewBean> animGiftQune = new ArrayList<>();
    private List<View> giftViewCollection = new ArrayList<View>();
    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;
    private PopupWindow popWindow;
    List<LoadOtherRoomsResponce.OtherRoomData> datas = new ArrayList<>();
    List<LoadRoomContriResponce.RoomContriData> contributeModels = new ArrayList<>();
    private List<AllRoomMsgPush> allRoomMessage = new ArrayList<>();
    private String gameName1;
    private int issueRound;
    private String jcGameResultJson;
    private String lyGameResultJson;
    private int yxjcIssueRound;
    private int lyjcIssueRound;
    private int LyresultCount = 0;
    private int JcresultCount = 0;
    private boolean falshViewisEnd = true;
    private int giftCheck = 0;

    private RequestBuilder<Bitmap> mRequestBuilder;
    /**
     * 动画相关
     */
    private NumAnim giftNumAnim;
    private TranslateAnimation inAnim;
    private TranslateAnimation outAnim;
    //  推荐
    private RelativeLayout recommendView;
    private TextView recommendCountLeft;
    private TextView recommendNicknameLeft;
    private TextView recommendCountRight;
    private TextView recommendNicknameRight;
    private ImageView recommendHeadImageLeft;
    private ImageView recommendHeadImageRight;
    private LinearLayout ll_tuijian;
    private LinearLayout recLeft;
    private LinearLayout recRight;
    private FansContriDialogFragment fansContriDialogFragment;

    private Timer messageTimer;
    private MessageTimerTask messageTimerTask;
    private Timer staticGiftTimer;
    private StaticGiftTimerTask giftTimerTask;
    private Timer mGiftTimer;
    private GiftTimerTask mGiftTimerTask;
    private Timer mCaichiTimer;
    private final int SHOW_MESSAGE = 1;
    private final int STATICGIFTSHOW = 2;
    private final int GIFT_SHOW = 3;
    private final int CAICHI_OPEN = 4;

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

    private class MessageTimerTask extends TimerTask {
        public void run() {
            dealNews(SHOW_MESSAGE);
        }
    }

    private class StaticGiftTimerTask extends TimerTask {
        public void run() {
            dealNews(STATICGIFTSHOW);
        }
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
                            case STATICGIFTSHOW:
                                showStaticGift();
                                break;
                            case GIFT_SHOW:
                                giftShow();
                                break;
                            case CAICHI_OPEN:
                                dealCaichi();
                                break;
                            default:
                                break;
                        }
                    }


                });
    }

    private void dealCaichi() {
        if (caichiTimeLeft != null) {
            caichiTimeLeft.setVisibility(View.VISIBLE);
        }
        if (caichicountDown == 0 || caichicountDown > 600) {
            if (caichiTimeLeft != null) {
                caichiTimeLeft.setVisibility(View.GONE);
            }
        } else {
            caichicountDown--;
            if (caichicountDown <= 60) {
                imgT1.setImageResource(R.drawable.t0);
                imgT2.setImageResource(R.drawable.t0);
                if (caichicountDown < 10) {
                    imgT3.setImageResource(R.drawable.t0);
                    setT(imgT4, caichicountDown);
                } else {
                    int b = caichicountDown / 10;
                    int a = caichicountDown % 10;
                    setT(imgT3, b);
                    setT(imgT4, a);
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


    private void giftShow() {
//        if (resultCount > 0) {
//            resultCount++;
//            if (gameName1.equals("yxjc")) {
//                if (resultCount > 15) {
//                    if (issueRound != 0) {
//                        resultCount = 0;
//                        mPresenter.showJoinResult(gameName1, issueRound);
//                    }
//                }
//            } else {
//                if (resultCount > 17) {
//                    if (issueRound != 0) {
//                        resultCount = 0;
//                        mPresenter.showJoinResult(gameName1, issueRound);
//                    }
//                }
//            }
//        }
        if (JcresultCount > 0) {
            JcresultCount++;
            if (JcresultCount > 15) {
                if (yxjcIssueRound != 0) {
                    JcresultCount = 0;
                    mPresenter.showJoinResult("yxjc", yxjcIssueRound);
                }
            }
        }
        if (LyresultCount > 0) {
            LyresultCount++;
            if (LyresultCount > 17) {
                if (yxjcIssueRound != 0) {
                    LyresultCount = 0;
                    mPresenter.showJoinResult("lyzb", lyjcIssueRound);
                }
            }
        }

        if (!falshViewisEnd) {
            giftCheck++;
        }
        Log.e("aaaaaaa", falshViewisEnd + "/" + giftCheck);
        if (giftCheck == 7) {
            falshViewisEnd = true;
        }

        if (vipGiftModels.size() > 0 && zuojiaIsEnd) {
            zuoJiaAnimation();
        }

        if (animGiftQune.size() > 0 && falshViewisEnd) {
            final SysGiftNewBean giftBean = animGiftQune.remove(0);
            flashview.setVisibility(View.VISIBLE);
            falshViewisEnd = false;
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
                String fileName = FlashDataParser.getExternalStorageDirectory(getContext()) + "/"+picFile520+"/" + iconname;
                Bitmap bitmap = FileUtils.readBitmap565FromFile(fileName);
                if (bitmap != null) {
                    flashview.replaceBitmap("520_1.png", bitmap);
                    flashview.play("cucumber520", FlashDataParser.FlashLoopTimeOnce);
                } else {
                    falshViewisEnd = true;
                    flashview.setVisibility(View.GONE);
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
                String fileName = FlashDataParser.getExternalStorageDirectory(getContext()) + "/"+picFile1314+"/" + iconname;
                Bitmap bitmap = FileUtils.readBitmap565FromFile(fileName);
                if (bitmap != null) {
                    flashview.replaceBitmap("1314_1.png", getDefaultBitmap(bitmap));
                    flashview.play("cucumber1314", FlashDataParser.FlashLoopTimeOnce);
                } else {
                    falshViewisEnd = true;
                    flashview.setVisibility(View.GONE);
                    giftCheck = 0;
                }
            } else {
                String animName = giftBean.getAnimName().split("_")[0];
                if (!giftBean.isMount()) {
                    flashview.reload(animName, "flashAnims");
                } else {
                    flashview.reload(animName, "mountFlashAnim");
                    Log.e("aaaaaaaa", "fffffff");
                }
                flashview.play(animName, FlashDataParser.FlashLoopTimeOnce);
            }
            flashview.setEventCallback(new FlashDataParser.IFlashViewEventCallback() {
                @Override
                public void onEvent(FlashDataParser.FlashViewEvent e, FlashDataParser.FlashViewEventData data) {
                    if (e.equals(FlashDataParser.FlashViewEvent.ONELOOPEND)) {
                        falshViewisEnd = true;
                        flashview.setVisibility(View.GONE);
                        giftCheck = 0;
                    }
                }
            });
        }


        if (loginDialogCount > 0) {
            loginDialogCount++;
        }
        if (loginDialogCount == 31) {
            loginDialogCount = 0;
            if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                if (BuildConfig.isForceLoad.equals("1")) {
                    CustomDialogUtils.showQZLoginDialog(getActivity(), false);
                    CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                        @Override
                        public void cancleClick() {
                            loginDialogCount = 1;
                        }

                        @Override
                        public void loginSuccess() {
                            if (tcRoomInfo != null) {
                                mPresenter.joinRoom(tcRoomInfo.getRoomId());
                            }
                        }
                    });
                }
            }
        }
    }

    private Bitmap getDefaultBitmap(Bitmap bitmap) {
        Bitmap mDefauleBitmap = null;
        int px = TCUtils.dip2px(getContext(), 120);
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix matrix = new Matrix();
            matrix.postScale(((float) px) / width, ((float) px) / height);
            mDefauleBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        }
        return mDefauleBitmap;
    }

    private void showStaticGift() {
        if (staticGiftModels.size() >= 2) {
            GiftModel giftModel = staticGiftModels.get(0);
            showGift(giftModel.getTag(), giftModel.getCount(), giftModel.getGiftname(),
                    giftModel.getUserName(), giftModel.getHeadUrl(), giftModel.getGiftid(), giftModel.isVip());
            staticGiftModels.remove(giftModel);
        } else if (staticGiftModels.size() > 0) {
            GiftModel giftModel = staticGiftModels.remove(0);
            showGift(giftModel.getTag(), giftModel.getCount(), giftModel.getGiftname(),
                    giftModel.getUserName(), giftModel.getHeadUrl(), giftModel.getGiftid(), giftModel.isVip());
        }
    }

    /**
     * 显示礼物的方法
     */
    private void showGift(String tag, int count, String giftname, String userName, String headUrl, int giftid, boolean isVip) {
        View giftView = giftcontent.findViewWithTag(tag);
        if (giftView == null) {/*该用户不在礼物显示列表*/

            if (giftcontent.getChildCount() >= 2) {/*如果正在显示的礼物的个数超过两个，那么就移除最后一次更新时间比较长的*/
                View giftView1 = giftcontent.getChildAt(0);
                ImageView picTv1 = (ImageView) giftView1.findViewById(R.id.crvheadimage);
                long lastTime1 = (Long) picTv1.getTag(R.id.time_value);
                View giftView2 = giftcontent.getChildAt(1);
                ImageView picTv2 = (ImageView) giftView2.findViewById(R.id.crvheadimage);
                long lastTime2 = (Long) picTv2.getTag(R.id.time_value);
                if (lastTime1 > lastTime2) {/*如果第二个View显示的时间比较长*/
                    removeGiftView(1);
                } else {/*如果第一个View显示的时间长*/
                    removeGiftView(0);
                }
            }

            giftView = addGiftView();/*获取礼物的View的布局*/
            giftView.setTag(tag);/*设置view标识*/

            ImageView crvheadimage = (ImageView) giftView.findViewById(R.id.crvheadimage);
            TextView giftUserName = (TextView) giftView.findViewById(R.id.gift_user_nickname);
            TextView giftNAme = (TextView) giftView.findViewById(R.id.gift_name);
            ImageView giftImage = (ImageView) giftView.findViewById(R.id.ivgift);
            final MagicTextView giftNum = (MagicTextView) giftView.findViewById(R.id.giftNum);/*找到数量控件*/
            if (count == 0) {
                giftNum.setVisibility(View.INVISIBLE);
            } else {
                giftNum.setVisibility(View.VISIBLE);
            }
            if (count == 520) {
                giftNum.setText("520x1");/*设置礼物数量*/
            } else if (count == 1314) {
                giftNum.setText("1314x1");/*设置礼物数量*/
            } else {
                giftNum.setText("x" + count);/*设置礼物数量*/
            }

            if (count == 0) {
                giftNAme.setText(giftname);
            } else {
                giftNAme.setText("送出了" + giftname);
            }
            giftUserName.setText(userName);
            TCUtils.showPicWithUrl(getContext(), crvheadimage, headUrl, R.drawable.face);
            crvheadimage.setTag(R.id.time_value, System.currentTimeMillis());/*设置时间标记*/
            giftNum.setTag(R.id.gift_num, count);/*给数量控件设置标记*/
            giftNum.setTag(R.id.gift_id, giftid);
            if (count == 520) {
                giftNum.setTag(R.id.gift_type, 1);//520标注1，1314标注2，其他0
            } else if (count == 1314) {
                giftNum.setTag(R.id.gift_type, 2);//520标注1，1314标注2，其他0
            } else {
                giftNum.setTag(R.id.gift_type, 0);//520标注1，1314标注2，其他0
            }
            if (count != 0) {
                SysGiftNewBean giftBean = mPresenter.getGiftBeanByID(String.valueOf(giftid));
                if (giftBean != null) {
                    if (mPresenter.getSysConfigBean() != null) {
                        String url = mPresenter.getSysConfigBean().getCosGiftRootPath();
                        if (giftBean.getEnableVip() == 1 && isVip) {
                            String iconName = giftBean.getIcon().substring(0, giftBean.getIcon().lastIndexOf(".")) + "_vip.png";
                            mRequestBuilder.load(url + "/" + iconName).into(giftImage);
                        } else {
                            mRequestBuilder.load(url + "/" + giftBean.getIcon()).into(giftImage);
                        }

                    }
                }
            } else {
                SysMountNewBean mountNewBeanBean = mPresenter.getMountBeanByID(String.valueOf(giftid));
                if (mountNewBeanBean != null) {
                    if (mPresenter.getSysConfigBean() != null) {
                        String url = mPresenter.getSysConfigBean().getCosMountRootPath();
                        mRequestBuilder.load(url + "/" + mountNewBeanBean.getIcon()).into(giftImage);
                    }
                }
            }
            giftcontent.addView(giftView);/*将礼物的View添加到礼物的ViewGroup中*/
            giftcontent.invalidate();/*刷新该view*/
            giftView.startAnimation(inAnim);/*开始执行显示礼物的动画*/
            inAnim.setAnimationListener(new Animation.AnimationListener() {/*显示动画的监听*/
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    giftNumAnim.start(giftNum);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        } else {/*该用户在礼物显示列表*/
            ImageView crvheadimage = (ImageView) giftView.findViewById(R.id.crvheadimage);/*找到头像控件*/
            TextView giftUserName = (TextView) giftView.findViewById(R.id.gift_user_nickname);
            TextView giftNAme = (TextView) giftView.findViewById(R.id.gift_name);
            ImageView giftImage = (ImageView) giftView.findViewById(R.id.ivgift);
            MagicTextView giftNum = (MagicTextView) giftView.findViewById(R.id.giftNum);/*找到数量控件*/
            if (count == 0) {
                giftNum.setVisibility(View.INVISIBLE);
            } else {
                giftNum.setVisibility(View.VISIBLE);
            }
            if ((int) (giftNum.getTag(R.id.gift_id)) == giftid) {
                int showNum = (Integer) giftNum.getTag(R.id.gift_num) + count;
                int giftType = (Integer) giftNum.getTag(R.id.gift_type);
                if (giftType == 0) {
                    if (count == 520) {
                        giftNum.setText("520x1");
                        giftNum.setTag(R.id.gift_num, 520);/*给数量控件设置标记*/
                    } else if (count == 1314) {
                        giftNum.setText("1314x1");
                        giftNum.setTag(R.id.gift_num, 1314);/*给数量控件设置标记*/
                    } else {
                        giftNum.setText("x" + showNum);
                        giftNum.setTag(R.id.gift_num, showNum);/*给数量控件设置标记*/
                    }
                } else if (giftType == 1) {
                    //520
                    if (count == 520) {
                        giftNum.setText("520x" + showNum / 520);
                        giftNum.setTag(R.id.gift_num, showNum);/*给数量控件设置标记*/
                    } else if (count == 1314) {
                        giftNum.setText("1314x1");
                        giftNum.setTag(R.id.gift_num, 1314);/*给数量控件设置标记*/
                    } else {
                        giftNum.setText("x" + count);
                        giftNum.setTag(R.id.gift_num, count);/*给数量控件设置标记*/
                    }
                } else if (giftType == 2) {
                    //1314
                    if (count == 1314) {
                        giftNum.setText("1314x" + showNum / 1314);
                        giftNum.setTag(R.id.gift_num, showNum);/*给数量控件设置标记*/
                    } else if (count == 520) {
                        giftNum.setText("520x1");
                        giftNum.setTag(R.id.gift_num, 520);/*给数量控件设置标记*/
                    } else {
                        giftNum.setText("x" + count);
                        giftNum.setTag(R.id.gift_num, count);/*给数量控件设置标记*/
                    }
                }
                giftNum.setTag(R.id.gift_id, giftid);
            } else {
                giftNum.setText("x" + count);
                giftNum.setTag(R.id.gift_num, count);/*给数量控件设置标记*/
                giftNum.setTag(R.id.gift_id, giftid);
            }

            if (count == 520) {
                giftNum.setTag(R.id.gift_type, 1);//520标注1，1314标注2，其他0
            } else if (count == 1314) {
                giftNum.setTag(R.id.gift_type, 2);//520标注1，1314标注2，其他0
            } else {
                giftNum.setTag(R.id.gift_type, 0);//520标注1，1314标注2，其他0
            }

            if (count == 0) {
                giftNAme.setText(giftname);
            } else {
                giftNAme.setText("送出了" + giftname);
            }
            giftUserName.setText(userName);
            TCUtils.showPicWithUrl(getContext(), crvheadimage, headUrl, R.drawable.face);
            crvheadimage.setTag(R.id.time_value, System.currentTimeMillis());/*设置时间标记*/
            if (count != 0) {
                giftNumAnim.start(giftNum);
            }
            if (count != 0) {
                SysGiftNewBean giftBean = mPresenter.getGiftBeanByID(String.valueOf(giftid));
                if (giftBean != null) {
                    if (mPresenter.getSysConfigBean() != null) {
                        String url = mPresenter.getSysConfigBean().getCosGiftRootPath();
                        if (giftBean.getEnableVip() == 1 && isVip) {
                            String iconName = giftBean.getIcon().substring(0, giftBean.getIcon().lastIndexOf(".")) + "_vip.png";
                            mRequestBuilder.load(url + "/" + iconName).into(giftImage);
                        } else {
                            mRequestBuilder.load(url + "/" + giftBean.getIcon()).into(giftImage);
                        }
                    }
                }
            } else {
                SysMountNewBean mountNewBeanBean = mPresenter.getMountBeanByID(String.valueOf(giftid));
                if (mountNewBeanBean != null) {
                    if (mPresenter.getSysConfigBean() != null) {
                        String url = mPresenter.getSysConfigBean().getCosMountRootPath();
                        mRequestBuilder.load(url + "/" + mountNewBeanBean.getIcon()).into(giftImage);
                    }
                }
            }
        }
    }

    /**
     * 添加礼物view,(考虑垃圾回收)
     */
    private View addGiftView() {
        View view = null;
        if (giftViewCollection.size() <= 0) {
            /*如果垃圾回收中没有view,则生成一个*/
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_gift, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.topMargin = 10;
            view.setLayoutParams(lp);
            giftcontent.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View view) {
                }

                @Override
                public void onViewDetachedFromWindow(View view) {
                    giftViewCollection.add(view);
                }
            });
        } else {
            view = giftViewCollection.get(0);
            giftViewCollection.remove(view);
        }
        return view;
    }

    private void showAllRoomMessage() {
        if (allRoomMessage.size() > 0) {
            AllRoomMsgPush pushData = allRoomMessage.remove(0);
            String msg10 = pushData.getMsg();
            String msgType = pushData.getMsgName();
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
                    if (mDanmuMgr != null) {
                        if (msgType.equals("GameMsg")) {
                            if (AppUtils.judegeShowWanfa()) {
                                mDanmuMgr.addGameDanmu(msg10, masterName, roomId);
                            }
                        } else if (msgType.equals("GaoShePao")) {
                            mDanmuMgr.addDaPaoDanmu(masterName, nickname, roomId);
                        } else if (msgType.equals("Loudspeaker")) {
                            if (roomId.equals(tcRoomInfo.getRoomId())) {
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
                            if (roomId.equals(tcRoomInfo.getRoomId())) {
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
                            danmakuActionManager.addDanmu(model);

                            final TCChatEntity entity = new TCChatEntity();
                            entity.setSenderName("");
                            entity.setLevel(0);
                            entity.setContext("系统提示：" + "恭喜" + nickname + "开通VIP，成为全平台的贵宾！");
                            entity.setType(TCConstants.SYSTEM_TYPE);
                            entity.setUserId("0");
                            notifyMsg(entity);

                            mDanmuMgr.addVipSuccess(nickname);
                        } else if (msgType.equals("BuyGuard")) {
                            DanmuModel model = new DanmuModel(headUrl, level,
                                    nickname + "成功守护主播，果然是真爱啊~", pushData.getSender().getVip(),
                                    pushData.getSender().isVipExpired(), pushData.getSender().getGuardType(),
                                    pushData.getSender().isGuardExpired(), pushData.getSender().isLiang(), pushData.getSender().getShowId());
                            danmakuActionManager.addDanmu(model);

                            final TCChatEntity entity = new TCChatEntity();
                            entity.setSenderName("");
                            entity.setLevel(0);
                            entity.setContext("系统提示：" + nickname + "成功守护主播，果然是真爱啊~");
                            entity.setType(TCConstants.SYSTEM_TYPE);
                            entity.setUserId("0");
                            notifyMsg(entity);
                            mDanmuMgr.addGuardSuccess(nickname, masterName);
                        }
                    }
                }
            }
        }
    }


    public static ChatPlayFragment getInstance(JoinRoomResponce.JoinRoomData roomInfo) {
        ChatPlayFragment fragment = new ChatPlayFragment();
        fragment.tcRoomInfo = roomInfo;
        return fragment;
    }

    public interface ChatPlayFragmentListener {
        void back();

        void onfresh();

        void showContribueDialog();

        void uadatePlayUrl(String playurl, boolean roomstate);

        void showPausePicture(boolean state);

        void hideProessBar();

        void socketDisconnect();

        void socketReconnect();
    }

    private ChatPlayFragmentListener mListener;

    public void setChatPlayFragmentListener(ChatPlayFragmentListener listener) {
        mListener = listener;
    }

    public interface PlayShareListener {
        void qqShare();

        void qqzoneShare();

        void wxShare();

        void wbShare();

        void wxcircleShare();


    }

    private PlayShareListener playShareListener;

    public void setShareListener(PlayShareListener listener) {
        playShareListener = listener;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat_play_fragment;
    }

    @Override
    public void gotoRoom(LoadRoomResponce.LoadRoomData data) {
        if (tcRoomInfo != null) {
            gotoRoomData(data);
        }
    }

    @Override
    public Context getChatPlayContext() {
        return getContext();
    }

    @Override
    public Activity getChatPlayActivity() {
        return getActivity();
    }

    @Override
    public void goReportActivity(String userId) {
        Intent intent = new Intent(getActivity(), ReportActivity.class);
        intent.putExtra("userId", userId);
        getContext().startActivity(intent);
    }

    @Override
    public int getLoginDialogCount() {
        return loginDialogCount;
    }

    @Override
    public void setLoginDialogCount(int count) {
        loginDialogCount = count;
    }

    @Override
    public ImageView getConcernImage() {
        return concern;
    }

    @Override
    public void goOthersCommunityActivity(String userId) {
        Intent intent = new Intent(getActivity(), OthersCommunityActivity.class);
        intent.putExtra("userId", userId);
        getContext().startActivity(intent);
    }

    @Override
    public void notifyChatMsg(TCChatEntity tcChatEntity) {
//        notifyMsg(tcChatEntity);
    }

    @Override
    public void notifyContriList(List<LoadRoomContriResponce.RoomContriData> datas) {
        contributeModels = datas;
        mAvatarListAdapter.setDatas(datas);
    }

    @Override
    public void notifyCaichiCountDown(JackpotCountDownResponce.JackpotCountDownData data) {
        caichicountDown = data.getCountDown();
        int giftId = data.getGiftId();
        if (giftId == 1) {
            textCaichiOpen.setText("距离幸运黄瓜开奖");
        } else if (giftId == 2) {
            textCaichiOpen.setText("距离幸运香蕉开奖");
        } else if (giftId == 8) {
            textCaichiOpen.setText("距离幸运爱心开奖");
        } else if (giftId == 20) {
            textCaichiOpen.setText("距离飞虎流星开奖");
        }
        // if (caichicountDown <= 600) {
        mCaichiTimer.schedule(new CaichiTimerTask(), 1000, 1000);
    }

    @Override
    public void notifyGetCurrMount(GetCurrMountResponce.CurrMountData data) {
        TCChatEntity entity = new TCChatEntity();
        final LoadUserDataBaseResponse.UserData userData = FeihuZhiboApplication.getApplication().mDataManager.getUserData();
        entity.setGuardType(data.getGuardType());
        if (data.getGuardType() != 0) {
            entity.setGuardExpired(false);
            SharePreferenceUtil.saveSeesionBoolean(FeihuZhiboApplication.getApplication(), "meisGuard", true);
        } else {
            entity.setGuardExpired(true);
            SharePreferenceUtil.saveSeesionBoolean(FeihuZhiboApplication.getApplication(), "meisGuard", false);
        }
        entity.setVip(userData.getVip());
        entity.setVipExpired(userData.isVipExpired());
        entity.setAccountId(userData.getAccountId());
        entity.setLiang(userData.isLiang());
        entity.setSenderName("" + userData.getNickName() + "  ");
        entity.setLevel(userData.getLevel());
        entity.setUserId(userData.getUserId());
        entity.setType(TCConstants.JOIN_TYPE);
        if (data.getId() != 0) {
            SysMountNewBean mountBean = mPresenter.getMountBeanByID(String.valueOf(data.getId()));
            if (mountBean != null) {
                entity.setZuojia("驾驶" + mountBean.getName());
                entity.setContext("进入直播间");
                try {
//                                showGift(SharePreferenceUtil.getLoginUser(getContext()).getUserId(),
//                                    0, "驾驶[" + mountBean.getName() + "]进入直播间", SharePreferenceUtil.getLoginUser(getContext()).getNickName(),
//                                    SharePreferenceUtil.getLoginUser(getContext()).getHeadUrl(), id);
                    if (!mountBean.getIsAnimation().equals("1")) {
                        GiftModel giftModel = new GiftModel(SharePreferenceUtil.getSession(getContext(), "PREF_KEY_USERID"),
                                0, "驾驶[" + mountBean.getName() + "]进入直播间", SharePreferenceUtil.getSession(getContext(), "PREF_KEY_NICKNAME"),
                                SharePreferenceUtil.getSession(getContext(), "PREF_KEY_HEADURL"), data.getId(), false);

                        staticGiftModels.add(giftModel);

                    } else {
                        GiftModel giftModel = new GiftModel(SharePreferenceUtil.getSession(getContext(), "PREF_KEY_USERID"),
                                0, "驾驶[" + mountBean.getName() + "]进入直播间", SharePreferenceUtil.getSession(getContext(), "PREF_KEY_NICKNAME"),
                                SharePreferenceUtil.getSession(getContext(), "PREF_KEY_HEADURL"), data.getId(), false);
                        if (TextUtils.isEmpty(mountBean.getAnimName())) {
                            vipGiftModels.add(giftModel);
                        } else {
                            SysGiftNewBean giftBean = new SysGiftNewBean();
                            giftBean.setAnimName(mountBean.getAnimName());
                            giftBean.setMount(true);
                            giftBean.setIs520(false);
                            giftBean.setIs1314(false);
                            animGiftQune.add(0, giftBean);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                entity.setZuojia("");
                entity.setContext("进入直播间");
            }
        } else {
            entity.setZuojia("");
            entity.setContext("进入直播间");
        }
        notifyMsg(entity);

        if ((userData.getVip() > 0 && !userData.isVipExpired()) || data.getGuardType() != 0) {
            if (!TextUtils.isEmpty(entity.getZuojia())) {
                DanmuModel model = new DanmuModel(userData.getHeadUrl(), userData.getLevel(),
                        userData.getNickName() + entity.getZuojia() + "进入直播间~", userData.getVip(),
                        userData.isVipExpired(), data.getGuardType(),
                        data.getGuardType() == 0 ? true : false, userData.isLiang(), userData.getAccountId());
                danmakuActionManager.addDanmu(model);
            } else {
                DanmuModel model = new DanmuModel(userData.getHeadUrl(), userData.getLevel(),
                        userData.getNickName() + "进入直播间~", userData.getVip(),
                        userData.isVipExpired(), data.getGuardType(),
                        data.getGuardType() == 0 ? true : false, userData.isLiang(), userData.getAccountId());
                danmakuActionManager.addDanmu(model);
            }

        }
    }

    @Override
    public void notifyUncareResponce(UnFollowResponce responce) {
        if (responce.getCode() == 0) {
            tcRoomInfo.getMasterDataList().setFollowed(false);
            concern.setImageResource(R.drawable.icon_focus);
            concern.setVisibility(View.GONE);
            onToast("取消关注成功");
        } else {
            concern.setImageResource(R.drawable.icon_havecare);
        }
    }

    @Override
    public void notifyFollowResponce(FollowResponce responce) {
        if (responce.getCode() == 0) {
            tcRoomInfo.getMasterDataList().setFollowed(true);
            concern.setImageResource(R.drawable.icon_havecare);
            concern.setVisibility(View.GONE);
            if (SharePreferenceUtil.getSessionBoolean(FeihuZhiboApplication.getApplication(), "first_concern", true)) {
                SharePreferenceUtil.saveSeesionBoolean(FeihuZhiboApplication.getApplication(), "first_concern", false);
                showFirstConcernDialog();
            } else {
                onToast("关注成功");
            }
        } else {
            concern.setImageResource(R.drawable.icon_focus);
        }
    }

    private void showFirstConcernDialog() {
        final Dialog pickDialog2 = new Dialog(getActivity(), R.style.color_dialog);
        pickDialog2.setContentView(R.layout.pop_first_concern);

        WindowManager windowManager = getActivity().getWindowManager();
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

    GiftNewDialogView giftDialogView;

    @Override
    public void showGiftDialog(final List<SysbagBean> sysbagBeanList) {
        setGameIconVisual(false);
        if (FeihuZhiboApplication.getApplication().mDataManager.getLiveGiftList().size() == 0) {
            return;
        }
        giftDialogView = new GiftNewDialogView(sysbagBeanList, getContext(), false);
        Window dlgwin = giftDialogView.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        dlgwin.setGravity(Gravity.BOTTOM);
        dlgwin.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dlgwin.setAttributes(lp);
        giftDialogView.show();
        mListViewMsg.setVisibility(View.GONE);

        giftDialogView.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                toolBar.setVisibility(View.VISIBLE);
                mListViewMsg.setVisibility(View.VISIBLE);
                setGameIconVisual(true);
            }
        });
        toolBar.setVisibility(View.INVISIBLE);
        giftDialogView.setGiftDialogListener(new GiftNewDialogView.GiftDialogListener() {
            @Override
            public void sendGift(int id, int count, boolean isBag) {
                if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                    if (BuildConfig.isForceLoad.equals("1")) {
                        loginDialogCount = 0;
                        CustomDialogUtils.showQZLoginDialog(getActivity(), false);
                        CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                            @Override
                            public void cancleClick() {
                                loginDialogCount = 1;
                            }

                            @Override
                            public void loginSuccess() {
                                if (tcRoomInfo != null) {
                                    mPresenter.joinRoom(tcRoomInfo.getRoomId());
                                }
                            }
                        });
                    } else {
                        CustomDialogUtils.showLoginDialog(getActivity(), false);
                    }
                } else {
                    if (sysbagBeanList.size() == 0) {
                        if (isBag) {
                            mPresenter.dealBagSendGift(sysbagBeanList, id, count);
                        } else {
                            mPresenter.dealSendGift(sysbagBeanList, id, count);
                        }
                    } else {
                        if (isBag) {
                            mPresenter.dealBagSendGift(sysbagBeanList, id, count);
                        } else {
                            mPresenter.dealSendGift(sysbagBeanList, id, count);
                        }
                    }
                }
            }

            @Override
            public void chargr() {
                if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                    if (BuildConfig.isForceLoad.equals("1")) {
                        loginDialogCount = 0;
                        CustomDialogUtils.showQZLoginDialog(getActivity(), false);
                        CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                            @Override
                            public void cancleClick() {
                                loginDialogCount = 1;
                            }

                            @Override
                            public void loginSuccess() {
                                if (tcRoomInfo != null) {
                                    mPresenter.joinRoom(tcRoomInfo.getRoomId());
                                }
                            }
                        });
                    } else {
                        CustomDialogUtils.showLoginDialog(getActivity(), false);
                    }
                } else {
                    Intent intent = new Intent(getActivity(), RechargeActivity.class);
                    intent.putExtra("fromWhere", "直播间礼物");
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
    public void sendGiftSuccess(List<SysbagBean> sysbagBeanList, int id, int count) {
//        TCChatEntity entity = new TCChatEntity();
//        entity.setSenderName("" + SharePreferenceUtil.getSession(getContext(),"PREF_KEY_NICKNAME") + "  ");
        SysGiftNewBean giftBean = mPresenter.getGiftBeanByID(String.valueOf(id));
//        entity.setContext("送出了" + giftBean.getName() + "×" + count);
//        entity.setType(TCConstants.GIFT_TYPE);
//        entity.setUserId(SharePreferenceUtil.getSession(getContext(),"PREF_KEY_USERID"));
//        entity.setLevel(SharePreferenceUtil.getSessionInt(getContext(),"PREF_KEY_LEVEL"));
//        notifyMsg(entity);
        if (!giftBean.getIsAnimation().equals("1")) {
            GiftModel giftModel = new GiftModel(SharePreferenceUtil.getSession(getContext(), "PREF_KEY_USERID"), count, giftBean.getName(),
                    SharePreferenceUtil.getSession(getContext(), "PREF_KEY_NICKNAME"),
                    SharePreferenceUtil.getSession(getContext(), "PREF_KEY_HEADURL"), id, false);
//            staticGiftModels.add(giftModel);
        }

        for (SysbagBean sysbagBeen : sysbagBeanList) {
            if (sysbagBeen.getId() == id) {
                if (sysbagBeen.getCnt() > count) {
                    sysbagBeen.setCnt(sysbagBeen.getCnt() - count);
                } else {
                    sysbagBeanList.remove(sysbagBeen);
                }
                break;
            }
        }
        if (giftDialogView != null) {
            giftDialogView.setData(SysdataHelper.getbagBeanList(mPresenter.getSysGiftNewBean(), sysbagBeanList));
        }
    }

    @Override
    public void getGameListSuccess(GetGameListResponce getGameListResponce) {
        if (getGameListResponce.getGameList().size() > 0) {
            View contentView = LayoutInflater.from(getContext()).inflate(R.layout.pop_game_dialog, null);
            popWindow = new PopupWindow(contentView, TCUtils.dipToPx(getContext(), 134), ViewGroup.LayoutParams.WRAP_CONTENT);
            popWindow.setAnimationStyle(R.style.anim);// 淡入淡出动画
            popWindow.setTouchable(true);
            popWindow.setFocusable(true);
            popWindow.setBackgroundDrawable(new BitmapDrawable());
            popWindow.setOutsideTouchable(true);
            popWindow.setTouchInterceptor(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                        popWindow.dismiss();
                        return true;
                    }
                    return false;
                }
            });
//            popWindow.showAsDropDown(btnGame, -popWindow.getWidth() / 2 + TCUtils.dip2px(getContext(), 14), TCUtils.dip2px(getContext(), 3));
            int width = getResources().getDisplayMetrics().widthPixels;
            popWindow.showAtLocation(btnGame, Gravity.BOTTOM | Gravity.LEFT, width / 4 - TCUtils.dip2px(getContext(), 21), TCUtils.dip2px(getContext(), 50));
            final ImageView imageViewJc = (ImageView) contentView.findViewById(R.id.img_jc);
            ImageView imageViewLy = (ImageView) contentView.findViewById(R.id.img_ly);
            ImageView imageViewCaichi = (ImageView) contentView.findViewById(R.id.img_caichi);
            ImageView imgViewWeekstar = (ImageView) contentView.findViewById(R.id.img_weekstar);

            LinearLayout linearLayout = (LinearLayout) contentView.findViewById(R.id.lin_game);

            imageViewCaichi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dynamicChangeToolBarH(245);
                    showCaichiDialog();
                    popWindow.dismiss();
                    MobclickAgent.onEvent(getContext(), "10032");
                }
            });
            imageViewLy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.kaiqiang("lyzb");
                    popWindow.dismiss();
                    MobclickAgent.onEvent(getContext(), "10056");
                }
            });
            imageViewJc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.kaiqiang("yxjc");
                    popWindow.dismiss();
                    MobclickAgent.onEvent(getContext(), "100150");
                }
            });
            imgViewWeekstar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dynamicChangeToolBarH(245);
                    popWindow.dismiss();
//                    showWeekStarDialog();
                }
            });
            if (AppUtils.judegeShowWanfa()) {
                linearLayout.setVisibility(View.VISIBLE);
            } else {
                linearLayout.setVisibility(View.GONE);
            }

        } else {
            onToast("活动还未开启！");
        }
    }

    @Override
    public void kaiqiangResponce(String name, GetGameStatusResponce responce) {
        if (responce.getCode() == 0) {
            int openStyle = responce.getGameStatusData().getOpenStyle();
            if (name.equals("yxjc")) {
//                    dynamicChangeToolBarH(310);
                showGuessDialog(openStyle, responce.getGameStatusData());

            } else {

                showShakeDialog(responce);
            }
        } else {
            AppLogger.e(responce.getErrMsg());
        }
    }

    private void showWeekStarDialog(String url) {
        setGameIconVisual(false);
        WeekStarDialog jincaiDialog = new WeekStarDialog(getContext(), true, false, url, getActivity());
        jincaiDialog.show();
        jincaiDialog.setOnStrClickListener(new WeekStarDialog.GoRoomListener() {
            @Override
            public void goRoom(LoadRoomResponce.LoadRoomData tcRoomInfo) {
                gotoRoomData(tcRoomInfo);
            }

            @Override
            public void showLogin() {
                if (BuildConfig.isForceLoad.equals("1")) {
                    loginDialogCount = 0;
                    CustomDialogUtils.showQZLoginDialog(getActivity(), false);
                    CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                        @Override
                        public void cancleClick() {
                            loginDialogCount = 1;
                        }

                        @Override
                        public void loginSuccess() {
                            if (tcRoomInfo != null) {
                                mPresenter.joinRoom(tcRoomInfo.getRoomId());
                            }
                        }
                    });
                } else {
                    CustomDialogUtils.showLoginDialog(getActivity(), false);
                }
            }
        });
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = jincaiDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = display.getWidth(); //设置宽度
        jincaiDialog.getWindow().setAttributes(lp);
        jincaiDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                dynamicChangeToolBarH(15);
                setGameIconVisual(true);
            }
        });
        jincaiDialog.show();
    }

    @Override
    public void loadOtherRoomsResponce(LoadOtherRoomsResponce responce) {
        if (mListener != null) {
            mListener.hideProessBar();
        }
        datas = responce.getOtherRoomDatas();
        if (datas.size() >= 2) {
            recommendView.setVisibility(View.VISIBLE);
            String userCount = "";
            if (datas.get(0).getOnlineUserCnt() >= 10000) {
                userCount = new BigDecimal((double) datas.get(0).getOnlineUserCnt() / 10000).setScale(1, BigDecimal.ROUND_HALF_DOWN) + "万人";
            } else {
                userCount = datas.get(0).getOnlineUserCnt() + "人";
            }
            recommendCountLeft.setText("在线:" + userCount);
            TCUtils.showPicWithUrl(getContext(), recommendHeadImageLeft, datas.get(0).getHeadUrl(), R.drawable.face);
            recommendNicknameLeft.setText(datas.get(0).getNickName());

            String userCount1 = "";
            if (datas.get(0).getOnlineUserCnt() >= 10000) {
                userCount1 = new BigDecimal((double) datas.get(1).getOnlineUserCnt() / 10000).setScale(1, BigDecimal.ROUND_HALF_DOWN) + "万人";
            } else {
                userCount1 = datas.get(1).getOnlineUserCnt() + "人";
            }
            recommendCountRight.setText("在线:" + userCount1);
            TCUtils.showPicWithUrl(getContext(), recommendHeadImageRight, datas.get(1).getHeadUrl(), R.drawable.face);
            recommendNicknameRight.setText(datas.get(1).getNickName());
        } else {
            ll_tuijian.setVisibility(View.GONE);
            recommendView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void leaveRoomResponce(int code) {
        if (datas.get(code).getRoomId().equals(tcRoomInfo.getRoomId())) {
            return;
        }
        // 1为PC   2为手机
        getActivity().finish();
        AppUtils.startLiveActivity(ChatPlayFragment.this, datas.get(code).getRoomId(), datas.get(code).getHeadUrl(), datas.get(code).getBroadcastType(), false, HotFragment.START_LIVE_PLAY);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mDanmuMgr != null) {
            mDanmuMgr.resume();
        }
        int news_count = SharePreferenceUtil.getSessionInt(getContext(), "news_count");
        if (news_count > 0) {
            msgNew.setVisibility(View.VISIBLE);
        } else {
            msgNew.setVisibility(View.INVISIBLE);
        }

        if (giftDialogView != null) {
            giftDialogView.setHbCoin();
            giftDialogView.setYinCoin();
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        if (mDanmuMgr != null) {
            mDanmuMgr.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDanmuMgr != null) {
            mDanmuMgr.destroy();
            mDanmuMgr = null;
        }
        if (null != mGiftTimerTask) {
            mGiftTimerTask.cancel();
        }
        if (mCaichiTimer != null) {
            mCaichiTimer.cancel();
        }
        if (popWindow != null) {
            popWindow.dismiss();
        }
        if (messageTimer != null) {
            messageTimer.cancel();
        }
        if (animGiftQune.size() > 0) {
            animGiftQune.clear();
        }

        if (staticGiftTimer != null) {
            staticGiftTimer.cancel();
        }
        if (giftTimerTask != null) {
            giftTimerTask.cancel();
        }
        if (staticGiftModels.size() > 0) {
            staticGiftModels.clear();
        }
        if (fansContriDialogFragment != null) {
            fansContriDialogFragment.dismiss();
        }
        if (clearTimingTask != null) {
            clearTimingTask.cancel();
        }
        if (clearTimingTimer != null) {
            clearTimingTimer.cancel();
        }
    }

    private void showShakeDialog(GetGameStatusResponce json) {
        setGameIconVisual(false);
        toolBar.setVisibility(View.INVISIBLE);
        dynamicChangeToolBarH(255);
        final PlayShakeDialog jincaiDialog = new PlayShakeDialog(getContext(), json.getGameStatusData(), lyGameResultJson, lyjcIssueRound);
        jincaiDialog.show();
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = jincaiDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = display.getWidth(); //设置宽度
        jincaiDialog.getWindow().setAttributes(lp);
        jincaiDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                toolBar.setVisibility(View.VISIBLE);
                dynamicChangeToolBarH(15);
                setGameIconVisual(true);
                jincaiDialog.dismiss();
            }
        });
        jincaiDialog.setJincaiListener(new PlayShakeDialog.JinCaiInterface() {
            @Override
            public void gameHelp() {
                showLyhelpDialog();
                MobclickAgent.onEvent(getContext(), "10070");
            }

            @Override
            public void jinCai(int pos, int playCnt) {
                if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                    if (BuildConfig.isForceLoad.equals("1")) {
                        loginDialogCount = 0;
                        CustomDialogUtils.showQZLoginDialog(getActivity(), false);
                        CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                            @Override
                            public void cancleClick() {
                                loginDialogCount = 1;
                            }

                            @Override
                            public void loginSuccess() {
                                if (tcRoomInfo != null) {
                                    mPresenter.joinRoom(tcRoomInfo.getRoomId());
                                }
                            }
                        });
                    } else {
                        CustomDialogUtils.showLoginDialog(getActivity(), false);
                    }
                } else {
                    if (SharePreferenceUtil.getSessionInt(getContext(), "PREF_KEY_LEVEL") < 6) {
                        onToast("六级开启玩法");
                    } else {
                        showLyXiazhuDialog(pos, playCnt);
                    }
                }
                MobclickAgent.onEvent(getContext(), "10066");
            }

            @Override
            public void huDong(int blankCnt) {
                if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                    if (BuildConfig.isForceLoad.equals("1")) {
                        loginDialogCount = 0;
                        CustomDialogUtils.showQZLoginDialog(getActivity(), false);
                        CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                            @Override
                            public void cancleClick() {
                                loginDialogCount = 1;
                            }

                            @Override
                            public void loginSuccess() {
                                if (tcRoomInfo != null) {
                                    mPresenter.joinRoom(tcRoomInfo.getRoomId());
                                }
                            }
                        });
                    } else {
                        CustomDialogUtils.showLoginDialog(getActivity(), false);
                    }
                } else {
                    if (SharePreferenceUtil.getSessionInt(getContext(), "PREF_KEY_LEVEL") < 6) {
                        onToast("六级开启玩法");
                    } else {
                        showLyHuDongDialog(blankCnt);
                    }
                }
                MobclickAgent.onEvent(getContext(), "10059");
            }

            @Override
            public void awardHistory() {
                showLyKjHistoryDialog();
                MobclickAgent.onEvent(getContext(), "10058");
            }

            @Override
            public void jcHistory() {
                if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                    if (BuildConfig.isForceLoad.equals("1")) {
                        loginDialogCount = 0;
                        CustomDialogUtils.showQZLoginDialog(getActivity(), false);
                        CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                            @Override
                            public void cancleClick() {
                                loginDialogCount = 1;
                            }

                            @Override
                            public void loginSuccess() {
                                if (tcRoomInfo != null) {
                                    mPresenter.joinRoom(tcRoomInfo.getRoomId());
                                }
                            }
                        });
                    } else {
                        CustomDialogUtils.showLoginDialog(getActivity(), false);
                    }
                } else {
                    showJcHistoryDialog("lyzb");
                }
                MobclickAgent.onEvent(getContext(), "10057");
            }

            @Override
            public void showResult(int issueRound) {
                mPresenter.showJoinResult("lyzb", issueRound);
            }

            @Override
            public void showJincaiDlg() {
                new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                        .doGetGameStatusApiCall(new GetGameStatusRequest("yxjc"))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<GetGameStatusResponce>() {
                            @Override
                            public void accept(@NonNull GetGameStatusResponce getGameStatusResponce) throws Exception {
                                if (getGameStatusResponce.getCode() == 0) {
                                    boolean isGaming = getGameStatusResponce.getGameStatusData().isGaming();
                                    if (isGaming) {
                                        int openStyle = getGameStatusResponce.getGameStatusData().getOpenStyle();
                                        showGuessDialog(openStyle, getGameStatusResponce.getGameStatusData());
                                    } else {
                                        onToast("游戏竞猜还未开启！");
                                    }
                                } else {
                                    AppLogger.e(getGameStatusResponce.getErrMsg());
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                            }
                        }));
            }

            @Override
            public void goRoom(String roomId) {
                MobclickAgent.onEvent(getContext(), "10065");
                if (!roomId.equals(tcRoomInfo.getRoomId())) {
                    new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                            .doGetRoomDataApiCall(new LoadRoomRequest(roomId))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<LoadRoomResponce>() {
                                @Override
                                public void accept(@NonNull LoadRoomResponce responce) throws Exception {
                                    if (responce.getCode() == 0) {
                                        final LoadRoomResponce.LoadRoomData loadRoomData = responce.getLoadRoomData();
                                        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                                                .doLoadLeaveRoomApiCall(new LeaveRoomRequest())
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(new Consumer<LeaveRoomResponce>() {
                                                    @Override
                                                    public void accept(@NonNull LeaveRoomResponce responce) throws Exception {
                                                        if (responce.getCode() == 0) {
                                                            jincaiDialog.dismiss();
                                                            gotoRoomData(loadRoomData);
                                                        } else {
                                                            AppLogger.e(responce.getErrMsg());
                                                        }
                                                    }
                                                }, new Consumer<Throwable>() {
                                                    @Override
                                                    public void accept(@NonNull Throwable throwable) throws Exception {
                                                    }
                                                }));
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
            }
        });
    }


    private void showJcHistoryDialog(String gameName) {
        JcHistoryDialog jincaiDialog = new JcHistoryDialog(getContext(), gameName, false);
        jincaiDialog.show();
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = jincaiDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.CENTER);
        lp.width = display.getWidth() - TCUtils.dip2px(getContext(), 80); //设置宽度
        jincaiDialog.getWindow().setAttributes(lp);
    }

    private void showLyKjHistoryDialog() {
        LyHistoryDialog jincaiDialog = new LyHistoryDialog(getContext(), false);
        jincaiDialog.show();
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = jincaiDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.CENTER);
        lp.width = display.getWidth() - TCUtils.dip2px(getContext(), 60); //设置宽度
        jincaiDialog.getWindow().setAttributes(lp);
    }

    private void showLyHuDongDialog(int blankCnt) {
        LyHuDongDialog jincaiDialog = new LyHuDongDialog(getContext(), blankCnt);
        jincaiDialog.show();
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = jincaiDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = display.getWidth(); //设置宽度
        jincaiDialog.getWindow().setAttributes(lp);
        jincaiDialog.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showJoinSuccess();
            }
        });
    }

    private void showLyXiazhuDialog(int pos, int playcnt) {
        LyXiaZhuDialog jincaiDialog = new LyXiaZhuDialog(getContext(), pos, playcnt);
        jincaiDialog.show();
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = jincaiDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = display.getWidth(); //设置宽度
        jincaiDialog.getWindow().setAttributes(lp);
        jincaiDialog.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showJoinSuccess();
                MobclickAgent.onEvent(getContext(), "10067");
            }
        });
    }

    private void showJoinSuccess() {
        showJoinResultDialog(0);
    }

    private void showJoinResultDialog(int style) {
        final Dialog pickDialog = new Dialog(getContext(), R.style.floag_dialog);
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
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = display.getWidth(); //设置宽度
        pickDialog.getWindow().setAttributes(lp);
        pickDialog.show();
    }

    private void showLyhelpDialog() {
        Dialog pickDialog = new Dialog(getContext(), R.style.floag_dialog);
        pickDialog.setContentView(R.layout.jincai_help_dialog);
        WebView webView = (WebView) pickDialog.findViewById(R.id.webView);
        webView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bgpupelto));
        webView.loadUrl(mPresenter.getSysConfigBean().getLyzbExplainUrl());
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = display.getWidth(); //设置宽度
        pickDialog.getWindow().setAttributes(lp);
        pickDialog.show();
    }

    private void showGuessDialog(int style, GetGameStatusResponce.GameStatusData data) {
        toolBar.setVisibility(View.INVISIBLE);
        dynamicChangeToolBarH(255);
        setGameIconVisual(false);
        final PlayJincaiDialog jincaiDialog = new PlayJincaiDialog(getContext(), style, data, jcGameResultJson, yxjcIssueRound);
        jincaiDialog.show();
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = jincaiDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = display.getWidth(); //设置宽度
        jincaiDialog.getWindow().setAttributes(lp);
        jincaiDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                toolBar.setVisibility(View.VISIBLE);
                dynamicChangeToolBarH(15);
                setGameIconVisual(true);
            }
        });
        jincaiDialog.setJincaiListener(new PlayJincaiDialog.JinCaiInterface() {
            @Override
            public void gameHelp() {
                showJincaiDialog();
            }

            @Override
            public void jinCai(int pos, int playCnt) {
                if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                    if (BuildConfig.isForceLoad.equals("1")) {
                        loginDialogCount = 0;
                        CustomDialogUtils.showQZLoginDialog(getActivity(), false);
                        CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                            @Override
                            public void cancleClick() {
                                loginDialogCount = 1;
                            }

                            @Override
                            public void loginSuccess() {
                                if (tcRoomInfo != null) {
                                    mPresenter.joinRoom(tcRoomInfo.getRoomId());
                                }
                            }
                        });
                    } else {
                        CustomDialogUtils.showLoginDialog(getActivity(), false);
                    }
                } else {
                    if (SharePreferenceUtil.getSessionInt(getContext(), "PREF_KEY_LEVEL") < 6) {
                        onToast("六级开启玩法");
                    } else {
                        showJcXiazhuDialog(pos, playCnt);
                    }
                }
                MobclickAgent.onEvent(getContext(), "10044");
            }

            @Override
            public void huDong(int blankCnt) {
                if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                    if (BuildConfig.isForceLoad.equals("1")) {
                        loginDialogCount = 0;
                        CustomDialogUtils.showQZLoginDialog(getActivity(), false);
                        CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                            @Override
                            public void cancleClick() {
                                loginDialogCount = 1;
                            }

                            @Override
                            public void loginSuccess() {
                                if (tcRoomInfo != null) {
                                    mPresenter.joinRoom(tcRoomInfo.getRoomId());
                                }
                            }
                        });
                    } else {
                        CustomDialogUtils.showLoginDialog(getActivity(), false);
                    }
                } else {
                    if (SharePreferenceUtil.getSessionInt(getContext(), "PREF_KEY_LEVEL") < 6) {
                        onToast("六级开启玩法");
                    } else {
                        showHuDongDialog(blankCnt);
                    }
                }
                MobclickAgent.onEvent(getContext(), "10041");
            }

            @Override
            public void awardHistory() {
                MobclickAgent.onEvent(getContext(), "10040");
                showKjHistoryDialog();
            }

            @Override
            public void jcHistory() {
                MobclickAgent.onEvent(getContext(), "10039");
                if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                    if (BuildConfig.isForceLoad.equals("1")) {
                        loginDialogCount = 0;
                        CustomDialogUtils.showQZLoginDialog(getActivity(), false);
                        CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                            @Override
                            public void cancleClick() {
                                loginDialogCount = 1;
                            }

                            @Override
                            public void loginSuccess() {
                                if (tcRoomInfo != null) {
                                    mPresenter.joinRoom(tcRoomInfo.getRoomId());
                                }
                            }
                        });
                    } else {
                        CustomDialogUtils.showLoginDialog(getActivity(), false);
                    }
                } else {
                    showJcHistoryDialog("yxjc");
                }
            }

            @Override
            public void showResult(int issueRound) {
                mPresenter.showJoinResult("yxjc", issueRound);
            }

            @Override
            public void showshakeDialog() {
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
                                        showShakeDialog(responce);
                                    } else {
                                        onToast("乐瑶主播还未开启！");
                                    }
                                } else {

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
                if (!id.equals(tcRoomInfo.getRoomId())) {
                    new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                            .doGetRoomDataApiCall(new LoadRoomRequest(id))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<LoadRoomResponce>() {
                                @Override
                                public void accept(@NonNull LoadRoomResponce responce) throws Exception {
                                    if (responce.getCode() == 0) {
                                        final LoadRoomResponce.LoadRoomData loadRoomData = responce.getLoadRoomData();
                                        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                                                .doLoadLeaveRoomApiCall(new LeaveRoomRequest())
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(new Consumer<LeaveRoomResponce>() {
                                                    @Override
                                                    public void accept(@NonNull LeaveRoomResponce responce) throws Exception {
                                                        if (responce.getCode() == 0) {
                                                            jincaiDialog.dismiss();
                                                            gotoRoomData(loadRoomData);
                                                        } else {
                                                            AppLogger.e(responce.getErrMsg());
                                                        }
                                                    }
                                                }, new Consumer<Throwable>() {
                                                    @Override
                                                    public void accept(@NonNull Throwable throwable) throws Exception {
                                                    }
                                                }));
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

            }
        });
    }

    private void showJincaiDialog() {
        Dialog pickDialog = new Dialog(getContext(), R.style.floag_dialog);
        pickDialog.setContentView(R.layout.jincai_help_dialog);
        WebView webView = (WebView) pickDialog.findViewById(R.id.webView);
        webView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bgpupelto));
        webView.loadUrl(mPresenter.getSysConfigBean().getYxjcExplainUrl());

        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = display.getWidth(); //设置宽度
        pickDialog.getWindow().setAttributes(lp);
        pickDialog.show();
    }

    private void showJcXiazhuDialog(int pos, int playcnt) {
        JcXiaZhuDialog jincaiDialog = new JcXiaZhuDialog(getContext(), pos, playcnt);
        jincaiDialog.show();
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = jincaiDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = display.getWidth(); //设置宽度
        jincaiDialog.getWindow().setAttributes(lp);
        jincaiDialog.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showJoinSuccess();
            }
        });
    }

    private void showKjHistoryDialog() {
        KjHistoryDialog jincaiDialog = new KjHistoryDialog(getContext(), false);
        jincaiDialog.show();
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = jincaiDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.CENTER);
        lp.width = display.getWidth() - TCUtils.dip2px(getContext(), 80); //设置宽度
        jincaiDialog.getWindow().setAttributes(lp);
    }

    private void showHuDongDialog(int blankCnt) {
        HuDongDialog jincaiDialog = new HuDongDialog(getContext(), blankCnt);
        jincaiDialog.show();
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = jincaiDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = display.getWidth(); //设置宽度
        jincaiDialog.getWindow().setAttributes(lp);
        jincaiDialog.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showJoinSuccess();
            }
        });
    }

    private void dynamicChangeToolBarH(int heightPX) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) toolBar.getLayoutParams();
        layoutParams.bottomMargin = TCUtils.dip2px(getActivity(), heightPX);
        toolBar.setLayoutParams(layoutParams);
    }

    private void showCaichiDialog() {
        setGameIconVisual(false);
        final OrigCaichiDialog jincaiDialog = new OrigCaichiDialog(getContext());
        jincaiDialog.show();
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = jincaiDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = display.getWidth(); //设置宽度
        jincaiDialog.getWindow().setAttributes(lp);
        jincaiDialog.setOnItemClickListener(new OrigCaichiDialog.CaiOnclickListener() {
            @Override
            public void historyClick() {
                showCaichiHistoryDialog();
            }

            @Override
            public void helpClick() {
                showCaichiHelp();
            }

        });


        jincaiDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                toolBar.setVisibility(View.VISIBLE);
                setGameIconVisual(true);
                dynamicChangeToolBarH(15);
            }
        });
    }

    private void showCaichiHelp() {
        Dialog pickDialog = new Dialog(getContext(), R.style.floag_dialog);
        pickDialog.setContentView(R.layout.caichi_help_dialog);
        WebView webView = (WebView) pickDialog.findViewById(R.id.webView);
        webView.loadUrl(mPresenter.getSysConfigBean().getLuckyPoolDocUrl());

        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = display.getWidth(); //设置宽度
        pickDialog.getWindow().setAttributes(lp);
        pickDialog.show();
    }

    private void showCaichiHistoryDialog() {
        CaichiHiatoryDialog jincaiDialog = new CaichiHiatoryDialog(getContext());
        jincaiDialog.show();
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = jincaiDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = display.getWidth(); //设置宽度
        jincaiDialog.getWindow().setAttributes(lp);
        jincaiDialog.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (BuildConfig.isForceLoad.equals("1")) {
                    loginDialogCount = 0;
                    CustomDialogUtils.showQZLoginDialog(getActivity(), false);
                    CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                        @Override
                        public void cancleClick() {
                            loginDialogCount = 1;
                        }

                        @Override
                        public void loginSuccess() {
                            if (tcRoomInfo != null) {
                                mPresenter.joinRoom(tcRoomInfo.getRoomId());
                            }
                        }
                    });
                } else {
                    CustomDialogUtils.showLoginDialog(getActivity(), false);
                }
            }
        });
    }


    private GiftNumDialog giftNumDialog;

    private void showGiftNumDialog() {
        giftNumDialog = new GiftNumDialog(getContext(), R.style.InputDialog);
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


    private void gotoRoomData(LoadRoomResponce.LoadRoomData data) {
        if (data.getRoomId().equals(tcRoomInfo.getRoomId())) {
            return;
        }
        // 1为PC   2为手机
        getActivity().finish();
        AppUtils.startLiveActivity(ChatPlayFragment.this, data.getRoomId(), data.getHeadUrl(), data.getBroadcastType(), false, HotFragment.START_LIVE_PLAY);
    }

    @Override
    protected void initWidget(View view) {
        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            mPresenter.onAttach(this);
        }
        SharePreferenceUtil.saveSeesionBoolean(FeihuZhiboApplication.getApplication(), "meisGuard", false);
        mRequestBuilder = Glide.with(this).asBitmap().apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL));
        initViews(view);
        initView();
    }

    private boolean zuojiaIsEnd = true;

    public void zuoJiaAnimation() {
        GiftModel model = vipGiftModels.remove(0);
        SysMountNewBean mountNewBeanBean = mPresenter.getMountBeanByID(String.valueOf(model.getGiftid()));
        if (mountNewBeanBean != null) {
            if (mPresenter.getSysConfigBean() != null) {
                String url = mPresenter.getSysConfigBean().getCosMountRootPath();
                RequestBuilder<Bitmap> req = Glide.with(getContext()).asBitmap().apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL));
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
        imgZuojia.setTranslationX(newLoc.x);
        imgZuojia.setTranslationY(newLoc.y);
    }

    private ImageView imgZuojia;
    private BSRGiftLayout giftLayout;
    private GiftAnmManager giftAnmManager;

    private void initViews(View view) {
        mHeadIcon = (ImageView) view.findViewById(R.id.mHeadIcon);
        FrameLayout member_frm = (FrameLayout) view.findViewById(R.id.member_frm);
        mtvPuserName = (TextView) view.findViewById(R.id.tv_broadcasting_time);
        giftLayout = (BSRGiftLayout) view.findViewById(R.id.gift_layout);
        giftAnmManager = new GiftAnmManager(giftLayout, getContext());
        imgZuojia = (ImageView) view.findViewById(R.id.zuojia_img);
        tvMemberCounts = (TextView) view.findViewById(R.id.text_member);

        concern = (ImageView) view.findViewById(R.id.ctl_btnSwitch);

        back = (Button) view.findViewById(R.id.back);

        tv_play_hubi = (TextView) view.findViewById(R.id.tv_play_hubi);

        tv_play_account = (TextView) view.findViewById(R.id.tv_play_account);

        btnMessageInput = (ImageView) view.findViewById(R.id.btn_message_input);

        toolBar = (LinearLayout) view.findViewById(R.id.tool_bar);

        topView = (RelativeLayout) view.findViewById(R.id.top_view);

        llHubi = (LinearLayout) view.findViewById(R.id.ll_hubi);

        danmakuView = (DanmakuView) view.findViewById(R.id.danmakuView);

        mListViewMsg = (RecyclerView) view.findViewById(R.id.im_msg_listview);

        talkGiftView = (TalkGiftView) view.findViewById(R.id.talk_gift_view);

        giftcontent = (LinearLayout) view.findViewById(R.id.gift_content);

        mUserAvatarList = (RecyclerView) view.findViewById(R.id.rv_user_avatar);


        flashview = (FlashView) view.findViewById(R.id.flashview);

        btnGame = (ImageView) view.findViewById(R.id.btn_game);

        hubiView = (RelativeLayout) view.findViewById(R.id.hubi_view);

        btnMsg = (ImageView) view.findViewById(R.id.btn_msg);

        btnGift = (ImageView) view.findViewById(R.id.btn_gift);

        imgJc = (ImageView) view.findViewById(R.id.pic_jc);

        imgLy = (ImageView) view.findViewById(R.id.pic_ly);

        vipDanmakuView = (DanmakuChannel) view.findViewById(R.id.dan_vip);

        btnShare = (Button) view.findViewById(R.id.btn_share);

        btnRefresh = (Button) view.findViewById(R.id.btn_refresh);

        rlControllLayer = (RelativeLayout) view.findViewById(R.id.rl_controllLayer);

        imgT1 = (ImageView) view.findViewById(R.id.img_t1);

        imgT2 = (ImageView) view.findViewById(R.id.img_t2);

        imgT3 = (ImageView) view.findViewById(R.id.img_t3);

        imgT4 = (ImageView) view.findViewById(R.id.img_t4);

        caichiTimeLeft = (LinearLayout) view.findViewById(R.id.caichi_time_left);

        textCaichiOpen = (TextView) view.findViewById(R.id.text_caichi_open);

        msgNew = (ImageView) view.findViewById(R.id.msg_new);

        recommendView = (RelativeLayout) view.findViewById(R.id.recommend_view);

        recommendCountLeft = (TextView) view.findViewById(R.id.recommend_count_left);

        recommendNicknameLeft = (TextView) view.findViewById(R.id.recommend_nickname_left);

        recommendCountRight = (TextView) view.findViewById(R.id.recommend_count_right);

        recommendNicknameRight = (TextView) view.findViewById(R.id.recommend_nickname_right);

        recommendHeadImageLeft = (ImageView) view.findViewById(R.id.recommend_head_image_left);

        recommendHeadImageRight = (ImageView) view.findViewById(R.id.recommend_head_image_right);

        ll_tuijian = (LinearLayout) view.findViewById(R.id.ll_tuijian);
        recLeft = (LinearLayout) view.findViewById(R.id.rec_left);
        recRight = (LinearLayout) view.findViewById(R.id.rec_right);
        ImageView btn_mv = (ImageView) view.findViewById(R.id.btn_mv);
        btn_mv.setOnClickListener(this);
        mHeadIcon.setOnClickListener(this);
        concern.setOnClickListener(this);
        back.setOnClickListener(this);
        btnMessageInput.setOnClickListener(this);
        btnMsg.setOnClickListener(this);
        btnGift.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);
        llHubi.setOnClickListener(this);
        rlControllLayer.setOnClickListener(this);
        btnGame.setOnClickListener(this);
        recRight.setOnClickListener(this);
        recLeft.setOnClickListener(this);
        imgJc.setOnClickListener(this);
        imgLy.setOnClickListener(this);
        member_frm.setOnClickListener(this);
    }

    private LinearLayoutManager msglinearLayoutManager;

    private void initView() {
        if (tcRoomInfo == null) {
            return;
        }
        if (messageTimer == null) {
            messageTimer = new Timer(true);
            messageTimerTask = new MessageTimerTask();
            messageTimer.schedule(messageTimerTask, 1000, 2500);
        }
        if (staticGiftTimer == null) {
            staticGiftTimer = new Timer(true);
            giftTimerTask = new StaticGiftTimerTask();
            staticGiftTimer.schedule(giftTimerTask, 1000, 500);
        }
        softKeyboardListnenr();
        TCUtils.showPicWithUrl(getContext(), mHeadIcon, tcRoomInfo.getMasterDataList().getHeadUrl(), R.drawable.face);
        mtvPuserName.setVisibility(View.VISIBLE);
        mPusherNickname = tcRoomInfo.getMasterDataList().getNickName();
        // mtvPuserName.setText(TCUtils.getLimitString(mPusherNickname, 4));
        String followers = "";
        if (tcRoomInfo.getMasterDataList().getFollowers() >= 10000) {
            followers = new BigDecimal((double) tcRoomInfo.getOnlineUserCnt() / 10000).setScale(1, BigDecimal.ROUND_HALF_DOWN) + "万";
        } else {
            followers = tcRoomInfo.getOnlineUserCnt() + "";
        }
        tvMemberCounts.setText(followers);
        concern.setVisibility(View.VISIBLE);
        // 观众是否关注了主播
        haveCare = tcRoomInfo.getMasterDataList().isFollowed();
        if (haveCare) {
            // 已关注
            //concern.setImageResource(R.drawable.icon_havecare);
            concern.setVisibility(View.GONE);
        } else {
            // 未关注
            concern.setImageResource(R.drawable.icon_focus);
        }
        if (SharePreferenceUtil.getSession(getContext(), "PREF_KEY_USERID") != null) {
            if (SharePreferenceUtil.getSession(getContext(), "PREF_KEY_USERID").equals(tcRoomInfo.getMasterDataList().getUserId())) {
                concern.setVisibility(View.GONE);
            }
        }
        // 当前主播的总虎币
        gold = tcRoomInfo.getMasterDataList().getHB();
        tv_play_hubi.setVisibility(View.VISIBLE);
        tv_play_hubi.setText(gold + "");
        // 飞虎号
        accountId = tcRoomInfo.getMasterDataList().getAccountId();
        tv_play_account.setVisibility(View.VISIBLE);
        tv_play_account.setText(accountId);
        masteruserId = tcRoomInfo.getMasterDataList().getUserId();
        mInputTextMsgDialog = new TCInputTextMsgDialog(getContext(), R.style.InputDialog);
        mInputTextMsgDialog.setmOnTextSendListener(this);
        mInputTextMsgDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                toolBar.setVisibility(View.VISIBLE);
                topView.setVisibility(View.VISIBLE);
                llHubi.setVisibility(View.VISIBLE);
            }
        });
        mDanmuMgr = new TCDanmuMgr(mPresenter.getSysConfigBean(), mPresenter.getSysGiftNewBean(), getContext(), false, false, false);
        mDanmuMgr.setDanmakuView(danmakuView);
        danmakuView.setOnDanmakuClickListener(new IDanmakuView.OnDanmakuClickListener() {
            @Override
            public void onDanmakuClick(BaseDanmaku latest) {
                if (latest.userId == 0) {
                    return;
                }
                if (latest.userId != Integer.parseInt(tcRoomInfo.getRoomId())) {
                    mPresenter.loadRoomById(String.valueOf(latest.userId));
                }
            }

            @Override
            public void onDanmakuClick(IDanmakus danmakus) {

            }
        });
        danmakuActionManager = new DanmakuActionManager();
        vipDanmakuView.setDanAction(danmakuActionManager);
        danmakuActionManager.addChannel(vipDanmakuView);
        mListViewMsg.setVisibility(View.VISIBLE);

        msglinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mListViewMsg.setLayoutManager(msglinearLayoutManager);// 布局管理器。
        mListViewMsg.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。

        mChatMsgListAdapter = new ChatMsgRecycleAdapter(getContext(), false);
        mListViewMsg.setAdapter(mChatMsgListAdapter);


        final TCChatEntity entity = new TCChatEntity();
        entity.setSenderName("");
        entity.setLevel(0);
        entity.setContext("我们直播倡导绿色直播，封面和直播内容涉及色情、低俗、暴力、引诱、暴露等都会将被封停账号，同时禁止直播闹事，集会，文明直播从我做起【网警24小时在线巡查】");
        entity.setType(TCConstants.SYSTEM_TYPE);
        entity.setUserId("0");
        notifyMsg(entity);

        mChatMsgListAdapter.setOnStrClickListener(new OnStrClickListener() {
            @Override
            public void onItemClick(String id) {
                if (!TextUtils.isEmpty(id) && !id.equals("0")) {
                    mPresenter.showUserInfo(tcRoomInfo, id, false, false);
                }
            }
        });
        talkGiftView.setGiftImgGone();
        talkGiftView.setmOnTextSendListener(new TalkGiftView.OnTextSendListener() {
            @Override
            public void onTextSend(String msg, boolean mDanmuOpen) {
                sendMessage(msg);
                dynamicChangeListviewH(0);
            }

            @Override
            public void OnEditClick() {

            }

            @Override
            public void giftClick() {

            }

            @Override
            public void OnClick() {

            }

            @Override
            public void keyBoardHide() {

            }

            @Override
            public void showLogin() {

            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mUserAvatarList.setLayoutManager(linearLayoutManager);
        mUserAvatarList.getItemAnimator().setChangeDuration(0);
        ((SimpleItemAnimator) mUserAvatarList.getItemAnimator()).setSupportsChangeAnimations(false);
        mAvatarListAdapter = new TCUserContriListAdapter(getContext(), "1");
        mAvatarListAdapter.setHasStableIds(true);
        mAvatarListAdapter.setOnItemClickListener(new OnStrClickListener() {
            @Override
            public void onItemClick(String useId) {
                mPresenter.showUserInfo(tcRoomInfo, useId, false, false);
            }
        });
        mUserAvatarList.setAdapter(mAvatarListAdapter);
        mPresenter.loadRoomContriList("", 0);
        giftNumAnim = new NumAnim();
        inAnim = (TranslateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.gift_in);
        outAnim = (TranslateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.gift_out);
        clearTiming();
        if (mGiftTimer == null) {
            mGiftTimer = new Timer(true);
            mGiftTimerTask = new GiftTimerTask();
            mGiftTimer.schedule(mGiftTimerTask, 1000, 1000);
        }

        if (mCaichiTimer == null) {
            mCaichiTimer = new Timer(true);

        }
        mPresenter.getJackpotCountDown();
        if (!SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
            mPresenter.getCurrMount(tcRoomInfo.getRoomId());
        }
    }


    @Override
    public void showLogin() {
        if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
            if (BuildConfig.isForceLoad.equals("1")) {
                loginDialogCount = 0;
                CustomDialogUtils.showQZLoginDialog(getActivity(), false);
                CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                    @Override
                    public void cancleClick() {
                        loginDialogCount = 1;
                    }

                    @Override
                    public void loginSuccess() {
                        if (tcRoomInfo != null) {
                            mPresenter.joinRoom(tcRoomInfo.getRoomId());
                        }
                    }
                });
            } else {
                CustomDialogUtils.showLoginDialog(getActivity(), false);
            }
        } else {

        }
    }

    @Override
    public void onTextSend(String msg, boolean tanmuOpen) {
        if (tanmuOpen) {
            sendLoudSpeaker(msg);
        } else {
            sendMessage(msg);
        }
    }

    @Override
    protected void lazyLoad() {

    }

    UserDialogFragment userDialogFragment;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_mv:
                if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                    if (BuildConfig.isForceLoad.equals("1")) {
                        loginDialogCount = 0;
                        CustomDialogUtils.showQZLoginDialog(getActivity(), false);
                        CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                            @Override
                            public void cancleClick() {
                                loginDialogCount = 1;
                            }

                            @Override
                            public void loginSuccess() {
                                if (tcRoomInfo != null) {
                                    mPresenter.joinRoom(tcRoomInfo.getRoomId());
                                }
                            }
                        });
                    } else {
                        CustomDialogUtils.showLoginDialog(getActivity(), false);
                    }
                } else {
                    Intent intent = new Intent(getContext(), PostDemandActivity.class);
                    intent.putExtra("userId", tcRoomInfo.getMasterDataList().getUserId());
                    startActivity(intent);
                }
                break;
            case R.id.pic_jc:
                mPresenter.kaiqiang("yxjc");
                MobclickAgent.onEvent(getContext(), "100150");
                break;
            case R.id.pic_ly:
                mPresenter.kaiqiang("lyzb");
                MobclickAgent.onEvent(getContext(), "10056");
                break;
            case R.id.member_frm:
                setGameIconVisual(false);
                userDialogFragment = new UserDialogFragment();
                userDialogFragment.setOnStrClickListener(new OnStrClickListener() {
                    @Override
                    public void onItemClick(String userId) {
                        setGameIconVisual(true);
                    }
                });
                userDialogFragment.show(getFragmentManager(), "");
                break;
            case R.id.mHeadIcon:
                //点击主播头像
                mPresenter.showUserInfo(tcRoomInfo, tcRoomInfo.getMasterDataList().getUserId(), false, true);
                break;
            //关注
            case R.id.ctl_btnSwitch:
                if (tcRoomInfo.getMasterDataList().isFollowed()) {
                    mPresenter.unFollowed(masteruserId);
                } else {
                    if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                        if (BuildConfig.isForceLoad.equals("1")) {
                            loginDialogCount = 0;
                            CustomDialogUtils.showQZLoginDialog(getActivity(), false);
                            CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                                @Override
                                public void cancleClick() {
                                    loginDialogCount = 1;
                                }

                                @Override
                                public void loginSuccess() {
                                    if (tcRoomInfo != null) {
                                        mPresenter.joinRoom(tcRoomInfo.getRoomId());
                                    }
                                }
                            });
                        } else {
                            CustomDialogUtils.showLoginDialog(getActivity(), false);
                        }
                    } else {
                        mPresenter.Followed(masteruserId);
                    }

                }
                break;
            case R.id.back:
                if (mListener != null) {
                    mListener.back();
                }
                break;
            case R.id.btn_message_input:
                MobclickAgent.onEvent(getContext(), "10021");
                if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                    if (BuildConfig.isForceLoad.equals("1")) {
                        loginDialogCount = 0;
                        CustomDialogUtils.showQZLoginDialog(getActivity(), false);
                        CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                            @Override
                            public void cancleClick() {
                                loginDialogCount = 1;
                            }

                            @Override
                            public void loginSuccess() {
                                if (tcRoomInfo != null) {
                                    mPresenter.joinRoom(tcRoomInfo.getRoomId());
                                }
                            }
                        });
                    } else {
                        CustomDialogUtils.showLoginDialog(getActivity(), false);
                    }
                } else {
                    toolBar.setVisibility(View.INVISIBLE);
//                            topView.setVisibility(View.INVISIBLE);
//                            llHubi.setVisibility(View.INVISIBLE);
                    long currentTime = Calendar.getInstance().getTimeInMillis();
                    if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                        lastClickTime = currentTime;
                        showInputMsgDialog();
                    }
                }

                break;
            case R.id.btn_gift:
                requestSysbag();
                break;
            case R.id.btn_share:
                showLiveShareDialog();
                break;
            case R.id.btn_refresh:
                if (mListener != null) {
                    mListener.onfresh();
                }
                break;
            case R.id.tv_share_friends:
                MobclickAgent.onEvent(getContext(), "10029");
                if (playShareListener != null) {
                    playShareListener.wxcircleShare();
                }
                break;
            case R.id.tv_share_weibo:
                MobclickAgent.onEvent(getContext(), "10027");
                if (playShareListener != null) {
                    playShareListener.wbShare();
                }
                break;
            case R.id.tv_share_wechat:
                MobclickAgent.onEvent(getContext(), "10028");
                if (playShareListener != null) {
                    playShareListener.wxShare();
                }
                break;
            case R.id.tv_share_qzone:
                if (playShareListener != null) {
                    playShareListener.qqzoneShare();
                }
                break;
            case R.id.tv_share_qq:
                MobclickAgent.onEvent(getContext(), "10030");
                if (playShareListener != null) {
                    playShareListener.qqShare();
                }
                break;
            case R.id.ll_hubi:
                MobclickAgent.onEvent(getContext(), "10020");
                fansContriDialogFragment = FansContriDialogFragment.newInstance(tcRoomInfo.getMasterDataList().getUserId(), true);
                Bundle bundle = new Bundle();
                fansContriDialogFragment.setArguments(bundle);
                fansContriDialogFragment.show(getFragmentManager(), "");
                break;
            case R.id.rl_controllLayer:
                if (talkGiftView.getVisibility() == View.VISIBLE) {
                    talkGiftView.hideSoftInputFromWindow();
                    dynamicChangeListviewH(0);
                    talkGiftView.setVisibility(View.GONE);
                    toolBar.setVisibility(View.VISIBLE);
                    topView.setVisibility(View.VISIBLE);
                    llHubi.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_game:
                MobclickAgent.onEvent(getContext(), "10146");
//                        mPresenter.getGameList(tcRoomInfo.getRoomId());
                showGameDialog();
                break;
            case R.id.btn_msg:
                MobclickAgent.onEvent(getContext(), "10023");
                if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                    if (BuildConfig.isForceLoad.equals("1")) {
                        loginDialogCount = 0;
                        CustomDialogUtils.showQZLoginDialog(getActivity(), false);
                        CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                            @Override
                            public void cancleClick() {
                                loginDialogCount = 1;
                            }

                            @Override
                            public void loginSuccess() {
                                if (tcRoomInfo != null) {
                                    mPresenter.joinRoom(tcRoomInfo.getRoomId());
                                }
                            }
                        });
                    } else {
                        CustomDialogUtils.showLoginDialog(getActivity(), false);
                    }
                } else {
                    showMessageDialog();
                }
                SharePreferenceUtil.saveSeesionInt(getContext(), "news_count", 0);
                if (msgNew != null) {
                    msgNew.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.rec_left:
                if (datas.size() >= 2) {
                    if (tcRoomInfo != null) {
                        mPresenter.leaveRoom(0);
                    }
                }
                break;
            case R.id.rec_right:
                if (datas.size() >= 2) {
                    if (tcRoomInfo != null) {
                        mPresenter.leaveRoom(1);
                    }
                }
                break;
        }
    }

    public void initRecommendView(boolean isNull) {
        mtvPuserName.setText("下播");
        if (isNull) {
            mPresenter.loadOtherRooms();
        }
    }


    List<GetExtGameIconResponce.ExtGameIconData> gamedatas = new ArrayList<>();
    GameImageAdapter adapter;

    private void showGameDialog() {
        setGameIconVisual(false);
        final Dialog pickDialog3 = new Dialog(getContext(), R.style.floag_dialog);
        pickDialog3.setContentView(R.layout.game_view);

        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog3.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = (int) (display.getWidth()); //设置宽度
        pickDialog3.getWindow().setAttributes(lp);
        pickDialog3.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                setGameIconVisual(true);
            }
        });
        showLoading();
        FrameLayout cancel = (FrameLayout) pickDialog3.findViewById(R.id.frm_cancel);
        final RecyclerView recyclerView = (RecyclerView) pickDialog3.findViewById(R.id.recycle_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new GameImageAdapter(gamedatas);
        recyclerView.setAdapter(adapter);
        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .doGetExtGameIconCall(new GetExtGameIconRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetExtGameIconResponce>() {
                    @Override
                    public void accept(@NonNull GetExtGameIconResponce responce) throws Exception {
                        if (responce.getCode() == 0) {
                            if (datas.size() > 0) {
                                datas.clear();
                            }
                            gamedatas = responce.getExtGameIconDatas();
                            adapter.setNewData(gamedatas);
                            pickDialog3.show();
                        } else {
                            pickDialog3.dismiss();
                            AppLogger.e(responce.getErrMsg());
                        }
                        hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                })

        );

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GetExtGameIconResponce.ExtGameIconData item = (GetExtGameIconResponce.ExtGameIconData) adapter.getItem(position);
                if (item.getHtmlUrl().equals("yxjc")) {
                    mPresenter.kaiqiang("yxjc");
                    MobclickAgent.onEvent(getContext(), "100150");
                } else if (item.getHtmlUrl().equals("lyzb")) {
                    mPresenter.kaiqiang("lyzb");
                    MobclickAgent.onEvent(getContext(), "10056");
                } else if (item.getHtmlUrl().equals("xyjc")) {
                    dynamicChangeToolBarH(245);
                    showCaichiDialog();
                    MobclickAgent.onEvent(getContext(), "10032");
                } else {
                    dynamicChangeToolBarH(245);
                    showWeekStarDialog(item.getHtmlUrl());
                }
                pickDialog3.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDialog3.dismiss();
            }
        });
    }

    public void setGameIconVisual(boolean isVisual) {
        if (isVisual) {
            imgLy.setVisibility(View.VISIBLE);
            imgJc.setVisibility(View.VISIBLE);
        } else {
            imgJc.setVisibility(View.GONE);
            imgLy.setVisibility(View.GONE);
        }
    }


    /**
     * 私信
     */
    MessageDialog messageDialog;

    private void showMessageDialog() {
        setGameIconVisual(false);
        SharePreferenceUtil.saveSeesionInt(getContext(), "news_count", 0);
        messageDialog = new MessageDialog(getContext(), false, tcRoomInfo);
        Window dlgwin = messageDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        dlgwin.setGravity(Gravity.BOTTOM);
        dlgwin.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        UiUtil.initialize(getContext());
        int screenWidth = UiUtil.getScreenWidth();
        int screenHeight = UiUtil.getScreenHeight();
        lp.width = screenWidth;
        lp.height = screenHeight / 2;
        dlgwin.setAttributes(lp);
        messageDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                setGameIconVisual(true);
            }
        });
        messageDialog.show();
    }


    @Override
    public void onDestroyView() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroyView();
    }

    private void sendLoudSpeaker(String msg) {
        talkGiftView.setVisibility(View.GONE);
        toolBar.setVisibility(View.VISIBLE);
        topView.setVisibility(View.VISIBLE);
        llHubi.setVisibility(View.VISIBLE);
        mPresenter.sendLoudspeaker(msg);
    }


    private void requestSysbag() {
        mPresenter.requestBagData();
    }

    /**
     * 发消息弹出框
     */
    private boolean isInputMsgDialogShowing = false;

    private void showInputMsgDialog() {
        setGameIconVisual(false);
        isInputMsgDialogShowing = true;
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mInputTextMsgDialog.getWindow().getAttributes();

        lp.width = (int) (display.getWidth()); //设置宽度
        mInputTextMsgDialog.getWindow().setAttributes(lp);
        mInputTextMsgDialog.setCancelable(true);
        mInputTextMsgDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                toolBar.setVisibility(View.VISIBLE);
                topView.setVisibility(View.VISIBLE);
                llHubi.setVisibility(View.VISIBLE);
                dynamicChangeListviewH(0);
                isInputMsgDialogShowing = false;
                setGameIconVisual(true);
            }
        });
        mInputTextMsgDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mInputTextMsgDialog.show();
    }

    private void showLiveShareDialog() {
        setGameIconVisual(false);
        final Dialog pickDialog3 = new Dialog(getContext(), R.style.floag_dialog);
        pickDialog3.setContentView(R.layout.start_live_share);

        WindowManager windowManager = getActivity().getWindowManager();
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
        tv_share_friends.setOnClickListener(this);
        tv_share_weibo.setOnClickListener(this);
        tv_share_wechat.setOnClickListener(this);
        tv_share_qzone.setOnClickListener(this);
        tv_share_qq.setOnClickListener(this);
        pickDialog3.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                setGameIconVisual(true);
            }
        });
        pickDialog3.show();
    }

    /**
     * 软键盘显示与隐藏的监听
     */
    private void softKeyboardListnenr() {
        SoftKeyBoardListener.setListener(getActivity(), new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {/*软键盘显示：执行隐藏title动画，并修改listview高度和装载礼物容器的高度*/
                if (isInputMsgDialogShowing) {
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mListViewMsg.getLayoutParams();
                    layoutParams.bottomMargin = height;
                    mListViewMsg.setLayoutParams(layoutParams);
                }
            }

            @Override
            public void keyBoardHide(int height) {/*软键盘隐藏：隐藏聊天输入框并显示聊天按钮，执行显示title动画，并修改listview高度和装载礼物容器的高度*/
                if (talkGiftView.isFaceViewVisual()) {
                    dynamicChangeListviewH(180);
                } else {
                    dynamicChangeListviewH(0);
                }
            }
        });
    }

    /**
     * 定时清除礼物
     */
    TimerTask clearTimingTask;
    Timer clearTimingTimer;

    private void clearTiming() {
        try {
            clearTimingTask = new TimerTask() {
                @Override
                public void run() {
                    if (giftcontent != null) {
                        int count = giftcontent.getChildCount();
                        for (int i = 0; i < count; i++) {
                            View view = giftcontent.getChildAt(i);
                            ImageView crvheadimage = (ImageView) view.findViewById(R.id.crvheadimage);
                            long nowtime = System.currentTimeMillis();
                            long upTime = (Long) crvheadimage.getTag(R.id.time_value);
                            if ((nowtime - upTime) >= 3000) {
                                removeGiftView(i);
                                return;
                            }
                        }
                    }
                }
            };
            clearTimingTimer = new Timer();
            clearTimingTimer.schedule(clearTimingTask, 0, 3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除礼物view
     */
    private void removeGiftView(final int index) {
        try {
            final View removeView = giftcontent.getChildAt(index);
            outAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    try {
                        giftcontent.removeViewAt(index);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        removeView.startAnimation(outAnim);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 动态的修改listview的高度
     *
     * @param heightPX
     */
    private void dynamicChangeListviewH(int heightPX) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mListViewMsg.getLayoutParams();
        layoutParams.bottomMargin = TCUtils.dip2px(getActivity(), heightPX);
        mListViewMsg.setLayoutParams(layoutParams);
    }

    private void notifyMsg(final TCChatEntity entity) {
        if (mArrayListChatEntity.size() > 200) {
            mArrayListChatEntity.remove(0);
        }

        mArrayListChatEntity.add(entity);
        mChatMsgListAdapter.setDatas(mArrayListChatEntity);
        mListViewMsg.smoothScrollToPosition(mArrayListChatEntity.size() - 1);
    }

    private void sendMessage(final String msg) {
        talkGiftView.setVisibility(View.GONE);
        toolBar.setVisibility(View.VISIBLE);
        topView.setVisibility(View.VISIBLE);
        llHubi.setVisibility(View.VISIBLE);
        if (msg.length() == 0)
            return;
        try {
            byte[] byte_num = msg.getBytes("utf8");
            if (byte_num.length > 160) {
                Toast.makeText(getActivity(), "请输入内容", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }

        //消息回显
        final TCChatEntity entity = new TCChatEntity();
        entity.setSenderName(SharePreferenceUtil.getSession(getContext(), "PREF_KEY_NICKNAME") + "   ");
        entity.setLevel(SharePreferenceUtil.getSessionInt(getContext(), "PREF_KEY_LEVEL"));
        entity.setContext(msg);
        entity.setType(TCConstants.TEXT_TYPE);
        entity.setUserId(SharePreferenceUtil.getSession(getContext(), "PREF_KEY_USERID"));
        mPresenter.sendRoomMsg(entity, msg);
    }

    private void sortContriList(String userid, long contri, String headurl) {
        if (contributeModels.size() == 0) {
            LoadRoomContriResponce.RoomContriData contributeModel = new LoadRoomContriResponce.RoomContriData();
            contributeModel.setUserId(userid);
            contributeModel.setHB(contri);
            contributeModel.setHeadUrl(headurl);
            contributeModels.add(contributeModel);
            if (mAvatarListAdapter != null) {
                mAvatarListAdapter.setDatas(contributeModels);
            }
        } else {
            if (isExistContriList(userid, contri)) {
                sortContri();
            } else {
                if (contributeModels.size() < 20) {
                    LoadRoomContriResponce.RoomContriData contributeModel = new LoadRoomContriResponce.RoomContriData();
                    contributeModel.setUserId(userid);
                    contributeModel.setHB(contri);
                    contributeModel.setHeadUrl(headurl);
                    contributeModels.add(contributeModel);
                    sortContri();
                } else {
                    if (contributeModels.get(19).getHB() < contri) {
                        contributeModels.remove(19);
                        LoadRoomContriResponce.RoomContriData contributeModel = new LoadRoomContriResponce.RoomContriData();
                        contributeModel.setUserId(userid);
                        contributeModel.setHB(contri);
                        contributeModel.setHeadUrl(headurl);
                        contributeModels.add(contributeModel);
                        sortContri();
                    }
                }
            }
        }
    }

    private void deleteContriList(String uid) {
        if (contributeModels.size() > 0) {
            for (LoadRoomContriResponce.RoomContriData item : contributeModels) {
                if (item.getUserId().equals(uid)) {
                    contributeModels.remove(item);
                    if (mAvatarListAdapter != null) {
                        mAvatarListAdapter.setDatas(contributeModels);
                    }
                    break;
                }
            }

        }

    }

    private void sortContri() {
        if (contributeModels.size() > 0)
            Collections.sort(contributeModels, new Comparator<LoadRoomContriResponce.RoomContriData>() {
                @Override
                public int compare(LoadRoomContriResponce.RoomContriData lhs, LoadRoomContriResponce.RoomContriData rhs) {
                    long sort1 = lhs.getHB();
                    long sort2 = rhs.getHB();
                    return (sort1 == sort2 ? 0 : (sort1 < sort2 ? 1 : -1));
                }
            });
        if (mAvatarListAdapter != null) {
            mAvatarListAdapter.notifyItemRangeChanged(0, contributeModels.size());
//                    mAvatarListAdapter.setDatas(contributeModels);
        }
    }

    private boolean isExistContriList(String userId, long contri) {
        boolean isExist = false;
        for (LoadRoomContriResponce.RoomContriData item : contributeModels) {
            if (item.getUserId().equals(userId)) {
                isExist = true;
                item.setHB(contri);
                break;
            }
        }
        return isExist;
    }

    public void showBanDialog(String msg, String time) {
        final Dialog pickDialog2 = new Dialog(getContext(), R.style.floag_dialog);
        pickDialog2.setContentView(R.layout.pop_ban_zhibo);

        WindowManager windowManager = getActivity().getWindowManager();
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
                getActivity().finish();
                pickDialog2.dismiss();
            }
        });
        pickDialog2.setCanceledOnTouchOutside(false);
        pickDialog2.show();
    }

    ///房间聊天消息推送
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_ROOM_CHAT, threadMode = ThreadMode.MAIN)
    public void onReceiveRoomChatPush(RoomChatPush pushData) {
        String message = pushData.getMsg();
        String userId = pushData.getSender().getUserId();
        String nickname = pushData.getSender().getNickName();
        int level = pushData.getSender().getLevel();
//                if (userId.equals(SharePreferenceUtil.getSession(getContext(), "PREF_KEY_USERID"))) {
//
//                } else {
        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName("" + nickname + "  ");
        entity.setContext(message);
        entity.setUserId(userId);
        entity.setGuardType(pushData.getSender().getGuardType());
        entity.setGuardExpired(pushData.getSender().isGuardExpired());
        entity.setVip(pushData.getSender().getVip());
        entity.setVipExpired(pushData.getSender().isVipExpired());
        entity.setAccountId(pushData.getSender().getShowId());
        entity.setLiang(pushData.getSender().isLiang());
        entity.setType(TCConstants.TEXT_TYPE);
        entity.setLevel(level);
        notifyMsg(entity);
//                }
    }

    ///加入房间消息推送
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_NOTICE_JOIN_ROOM, threadMode = ThreadMode.MAIN)
    public void onReceiveJoinRoomPush(NoticeJoinRoomPush pushData) {
        int totalNumber = pushData.getTotalRoomMembers();
        int mountId = pushData.getMount();
        int contri1 = pushData.getContri();
        String userCount = "";
        if (totalNumber >= 10000) {
            userCount = new BigDecimal((double) totalNumber / 10000).setScale(1, BigDecimal.ROUND_HALF_DOWN) + "万";
        } else {
            userCount = totalNumber + "";
        }
        tvMemberCounts.setText(userCount);
        int level = pushData.getSender().getLevel();
        String userId = pushData.getSender().getUserId();
        String headUrl = pushData.getSender().getHeadUrl();
        sortContriList(userId, contri1, headUrl);
        TCChatEntity entity = new TCChatEntity();
        entity.setGuardType(pushData.getSender().getGuardType());
        entity.setGuardExpired(pushData.getSender().isGuardExpired());
        entity.setVip(pushData.getSender().getVip());
        entity.setVipExpired(pushData.getSender().isVipExpired());
        entity.setAccountId(pushData.getSender().getShowId());
        entity.setLiang(pushData.getSender().isLiang());
        entity.setSenderName(" " + pushData.getSender().getNickName() + "  ");
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
                if (userId.equals(SharePreferenceUtil.getSession(getContext(), "PREF_KEY_USERID"))) {

                } else {
                    if (!mountBean.getIsAnimation().equals("1")) {
                        GiftModel giftModel = new GiftModel(userId, 0, "驾驶[" + mountBean.getName() + "]进入直播间", pushData.getSender().getNickName(), headUrl, mountId, false);
                        staticGiftModels.add(giftModel);


                    } else {
                        if (mountBean.getIsAnimation().equals("1") && TextUtils.isEmpty(mountBean.getAnimName())) {
                            GiftModel giftModel = new GiftModel(userId, 0, "驾驶[" + mountBean.getName() + "]进入直播间", pushData.getSender().getNickName(), headUrl, mountId, false);
                            vipGiftModels.add(giftModel);
                        } else {
                            SysGiftNewBean giftBean = new SysGiftNewBean();
                            giftBean.setAnimName(mountBean.getAnimName());
                            giftBean.setIs520(false);
                            giftBean.setIs1314(false);
                            giftBean.setMount(true);
                            animGiftQune.add(0, giftBean);
                        }

                    }
                }
            }
        } else {
            entity.setContext("进入直播间");
            entity.setType(TCConstants.JOIN_TYPE);
        }
        entity.setLevel(level);
        entity.setUserId(userId);
        notifyMsg(entity);

        if (pushData.getSender().getVip() > 0 || pushData.getSender().getGuardType() != 0) {
            if (!pushData.getSender().isVipExpired() || !pushData.getSender().isGuardExpired()) {
                if (TextUtils.isEmpty(entity.getZuojia())) {
                    DanmuModel model = new DanmuModel(headUrl, level,
                            pushData.getSender().getNickName() + "进入直播间~", pushData.getSender().getVip(),
                            pushData.getSender().isVipExpired(), pushData.getSender().getGuardType(),
                            pushData.getSender().isGuardExpired(), pushData.getSender().isLiang(), pushData.getSender().getShowId());
                    danmakuActionManager.addDanmu(model);
                } else {
                    DanmuModel model = new DanmuModel(headUrl, level,
                            pushData.getSender().getNickName() + entity.getZuojia() + "进入直播间~", pushData.getSender().getVip(),
                            pushData.getSender().isVipExpired(), pushData.getSender().getGuardType(),
                            pushData.getSender().isGuardExpired(), pushData.getSender().isLiang(), pushData.getSender().getShowId());
                    danmakuActionManager.addDanmu(model);
                }

            }
        }
    }

    ///离开房间消息推送
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_NOTICE_LEAVE_ROOM, threadMode = ThreadMode.MAIN)
    public void onReceiveLeaveRoomPush(NoticeLeaveRoomPush pushData) {
        int totalNumber1 = pushData.getTotalRoomMembers();
        String uid = pushData.getUid();
        deleteContriList(uid);
        String userCount1 = "";
        if (totalNumber1 >= 10000) {
            userCount1 = new BigDecimal((double) totalNumber1 / 10000).setScale(1, BigDecimal.ROUND_HALF_DOWN) + "万人";
        } else {
            userCount1 = totalNumber1 + "人";
        }
        tvMemberCounts.setText(userCount1);
    }

    ///直播房间内拉流地址变化
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_UPDATE_PLAY_URL, threadMode = ThreadMode.MAIN)
    public void onReceiveUpdatePlayUrlPush(UpdatePlayUrlPush pushData) {
        String playUrl = pushData.getPlayUrl();
        boolean roomstate = pushData.isRoomStatus();
        if (mListener != null) {
            mListener.uadatePlayUrl(playUrl, roomstate);
        }
        if (roomstate && !TextUtils.isEmpty(playUrl)) {
            if (recommendView != null) {
                recommendView.setVisibility(View.GONE);
            }
            mtvPuserName.setText("直播中");
        } else {
            if (recommendView != null) {
                initRecommendView(true);
            }
            mtvPuserName.setText("下播");
        }
    }

    ///数值变化
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_VALUE_CHANGE, threadMode = ThreadMode.MAIN)
    public void onReceiveValueChangePush(ValueChangePush pushData) {
        String type = pushData.getType();
        int chgId = pushData.getChgId();
        int newValue = pushData.getNewVal();
        if (giftDialogView != null) {
            giftDialogView.setHbCoin();
            giftDialogView.setYinCoin();
        }
        if (type.equals("GHB")) {

        } else if (type.equals("Item")) {

        } else if (type.equals("HB")) {

        } else if (type.equals("Exp")) {

        } else if (type.equals("Level")) {

        } else if (type.equals("Xiaolaba")) {
            if (mInputTextMsgDialog != null) {
                mInputTextMsgDialog.onFreshXiaolabaCount();
            }
        }
    }

    ///收到礼物
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_ROOM_GIFT, threadMode = ThreadMode.MAIN)
    public void onReceiveRoomGiftPush(RoomGiftPush pushData) {
        int contri = pushData.getContri();
        int giftid = pushData.getGiftId();
        int giftcount = pushData.getGiftCnt();
        long income = pushData.getIncome();
        tv_play_hubi.setText(income + "");
        String userId = pushData.getSender().getUserId();
        String nickname = pushData.getSender().getNickName();
        int level = pushData.getSender().getLevel();
        String headUrl = pushData.getSender().getHeadUrl();
        sortContriList(userId, contri, headUrl);
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
            giftBean.setIs1314(true);
            giftBean.setIs520(false);
            animGiftQune.add(giftBean);
        }
//                if (userId.equals(SharePreferenceUtil.getSession(getContext(), "PREF_KEY_USERID"))) {
//
//                } else {
        TCChatEntity entity = new TCChatEntity();
        entity.setUserId(userId);
        entity.setSenderName("" + nickname + "    ");

        if (giftBean != null) {
            entity.setContext("送出了" + giftBean.getName() + "×" + giftcount);
        } else {
            entity.setContext("送给主播" + giftcount + "个" + "礼物");
        }
        entity.setType(TCConstants.GIFT_TYPE);
        entity.setGuardType(pushData.getSender().getGuardType());
        entity.setGuardExpired(pushData.getSender().isGuardExpired());
        entity.setVip(pushData.getSender().getVip());
        entity.setVipExpired(pushData.getSender().isVipExpired());
        entity.setAccountId(pushData.getSender().getShowId());
        entity.setLiang(pushData.getSender().isLiang());
        entity.setLevel(level);
        notifyMsg(entity);
        if (!giftBean.getIsAnimation().equals("1")) {
            if ((pushData.getSender().getVip() > 0 && !pushData.getSender().isVipExpired()) || (pushData.getSender().getGuardType() > 0 && !pushData.getSender().isGuardExpired())) {
                GiftModel giftModel = new GiftModel(userId, giftcount, giftBean.getName(),
                        nickname,
                        headUrl, giftid, true);

                staticGiftModels.add(giftModel);
            } else {
                GiftModel giftModel = new GiftModel(userId, giftcount, giftBean.getName(),
                        nickname,
                        headUrl, giftid, false);

                staticGiftModels.add(giftModel);
            }

        }
//                }
    }

    ///主播被关注
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_ROOM_FOLLOW, threadMode = ThreadMode.MAIN)
    public void onReceiveRoomFollowPush(RoomFollowPush pushData) {
        String userId = pushData.getSender().getUserId();
        String nickname = pushData.getSender().getNickName();
        int level = pushData.getSender().getLevel();
//                if (userId.equals(SharePreferenceUtil.getSession(getContext(), "PREF_KEY_USERID"))) {
//
//                } else {
        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName(" " + nickname + "");
        entity.setContext("关注了主播");
        entity.setGuardType(pushData.getSender().getGuardType());
        entity.setGuardExpired(pushData.getSender().isGuardExpired());
        entity.setVip(pushData.getSender().getVip());
        entity.setVipExpired(pushData.getSender().isVipExpired());
        entity.setAccountId(pushData.getSender().getShowId());
        entity.setLiang(pushData.getSender().isLiang());
        entity.setUserId(userId);
        entity.setLevel(level);
        entity.setType(TCConstants.CONCERN_TYPE);
        notifyMsg(entity);
//                }
    }

    ///用户被禁言
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_ROOM_BAN, threadMode = ThreadMode.MAIN)
    public void onReceiveRoomBanPush(RoomBanPush pushData) {
        String userId = pushData.getTarget().getUserId();
        String nickname = pushData.getTarget().getNickName();
        String banName = pushData.getSender().getNickName();
        int level = pushData.getTarget().getLevel();
        TCChatEntity entity = new TCChatEntity();
        entity.setUserId(userId);
        entity.setGuardType(pushData.getSender().getGuardType());
        entity.setGuardExpired(pushData.getSender().isGuardExpired());
        entity.setVip(pushData.getSender().getVip());
        entity.setVipExpired(pushData.getSender().isVipExpired());
        entity.setAccountId(pushData.getSender().getShowId());
        entity.setLiang(pushData.getSender().isLiang());
        entity.setSenderName(" " + nickname + "    ");
        entity.setBanName("被" + banName);
        entity.setContext("禁言30分钟");
        entity.setLevel(level);
        entity.setType(TCConstants.BAN_TYPE);
        notifyMsg(entity);
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

    //收到消息
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_NOTICE_SECRET_MESSAGE, threadMode = ThreadMode.MAIN)
    public void onReceiveLoadMsgListDataPush(LoadMsgListResponce.LoadMsgListData pushData) {
        if (messageDialog != null) {
            messageDialog.update(pushData.getTime(), pushData.getContent(), pushData.getUserId(), pushData.getHeadUrl(), pushData.getNickName());
        }
        if (derectMsgDialog != null) {
            derectMsgDialog.update(pushData.getTime(), pushData.getContent(), pushData.getUserId(), pushData.getHeadUrl());
        }
        SharePreferenceUtil.saveSeesionInt(getContext(), "news_count", SharePreferenceUtil.getSessionInt(getContext(), "news_count"));
        if (messageDialog != null && !messageDialog.isShowing()) {
            msgNew.setVisibility(View.VISIBLE);
        }
    }

    ///彩池倒计时
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_JACKPOT_GIFT_COUNTDOWN, threadMode = ThreadMode.MAIN)
    public void onReceiveJackpotGiftCountdownPush(JackpotGiftCountdownPush pushData) {
        int gid = pushData.getGiftId();
        if (gid == 1) {
            textCaichiOpen.setText("距离幸运黄瓜开奖");
        } else if (gid == 2) {
            textCaichiOpen.setText("距离幸运香蕉开奖");
        } else if (gid == 8) {
            textCaichiOpen.setText("距离幸运爱心开奖");
        } else if (gid == 20) {
            textCaichiOpen.setText("距离飞虎流星开奖");
        }
        caichicountDown = 600;
    }

    ///房管更新
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_ROOM_MGR, threadMode = ThreadMode.MAIN)
    public void onReceiveRoomMgrPush(RoomMgrPush pushData) {
        boolean status = pushData.isStatus();
        String userId = pushData.getTarget().getUserId();
        String nickname = pushData.getTarget().getNickName();
        int level = pushData.getTarget().getLevel();
        if (status) {
            TCChatEntity entity = new TCChatEntity();
            entity.setSenderName("系统提示: ");
            entity.setUserId(userId);
            entity.setContext(nickname + "成为场控");
            entity.setLevel(level);
            entity.setType(TCConstants.SYSTEM_TYPE);
            notifyMsg(entity);
            if (userId.equals(SharePreferenceUtil.getSession(getContext(), "PREF_KEY_USERID"))) {
                if (tcRoomInfo != null) {
                    tcRoomInfo.setRoomMgr(true);
                }
            }
        } else {
            if (userId.equals(SharePreferenceUtil.getSession(getContext(), "PREF_KEY_USERID"))) {
                if (tcRoomInfo != null) {
                    tcRoomInfo.setRoomMgr(false);
                }
            }
        }
    }

    //游戏结果揭晓
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_GAME_RESULT, threadMode = ThreadMode.MAIN)
    public void onReceiveGameResultPush(GameResultPush pushData) {
        gameName1 = pushData.getGameName();
        issueRound = pushData.getIssueRound();
        String json = pushData.getResult().toString();
//                resultCount = 1;
        if (gameName1.equals("yxjc")) {
            JcresultCount = 1;
        } else {
            LyresultCount = 1;
        }
        if (gameName1.equals("yxjc")) {
            yxjcIssueRound = issueRound;
            jcGameResultJson = json;
        } else {
            lyjcIssueRound = issueRound;
            lyGameResultJson = json;
        }
    }


    @RxBusSubscribe(code = RxBusCode.RX_BUS_NOTICE_CODE_USERINFO, threadMode = ThreadMode.MAIN)
    public void onReceiveUserinfo(String userId) {
        mPresenter.showUserInfo(tcRoomInfo, userId, true, false);
        if (messageDialog != null) {
            messageDialog.loadFriends();
        }
        if (userDialogFragment != null) {
            userDialogFragment.dismiss();
        }
    }

    @RxBusSubscribe(code = RxBusCode.RX_BUS_NOTICE_CODE_ROOMINCOME, threadMode = ThreadMode.MAIN)
    public void onReceiveRoomIcome(RoomIncomePush push) {
        tv_play_hubi.setText(push.getIncome() + "");
    }

    @RxBusSubscribe(code = RxBusCode.RX_BUS_NOTICE_CODE_USERINFO, threadMode = ThreadMode.MAIN)
    public void onReceiveUserinfo(Bundle bundle) {
        String id = bundle.getString("userId");
        mPresenter.showUserInfo(tcRoomInfo, id, false, false);
        if (messageDialog != null) {
            messageDialog.loadFriends();
        }
        if (userDialogFragment != null) {
            userDialogFragment.dismiss();
        }
    }

    @RxBusSubscribe(code = RxBusCode.RX_BUS_NOTICE_CODE_VIP, threadMode = ThreadMode.MAIN)
    public void onReceiveVip() {
        if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
            if (BuildConfig.isForceLoad.equals("1")) {
                loginDialogCount = 0;
                CustomDialogUtils.showQZLoginDialog(getActivity(), false);
                CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                    @Override
                    public void cancleClick() {
                        loginDialogCount = 1;
                    }

                    @Override
                    public void loginSuccess() {
                        if (tcRoomInfo != null) {
                            mPresenter.joinRoom(tcRoomInfo.getRoomId());
                        }
                    }
                });
            } else {
                CustomDialogUtils.showLoginDialog(getActivity(), false);
            }
        } else {
            if (SharePreferenceUtil.getSessionInt(FeihuZhiboApplication.getApplication(), "PREF_KEY_VIP") > 0) {
                if (!SharePreferenceUtil.getSessionBoolean(FeihuZhiboApplication.getApplication(), "PREF_KEY_VIPEXPIRED")) {
                    Intent intent = new Intent(getContext(), QuickUpgradeActivity.class);
                    intent.putExtra("isFromRoom", true);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getContext(), MyVipActivity.class);
                    intent.putExtra("isFromRoom", true);
                    startActivity(intent);
                }
            } else {
                Intent intent = new Intent(getContext(), MyVipActivity.class);
                intent.putExtra("isFromRoom", true);
                startActivity(intent);
            }

        }
    }


    @RxBusSubscribe(code = RxBusCode.RX_BUS_NOTICE_CODE_GUARD, threadMode = ThreadMode.MAIN)
    public void onReceiveGuard() {
        if (fansContriDialogFragment != null) {
            fansContriDialogFragment.dismiss();
        }
        if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
            if (BuildConfig.isForceLoad.equals("1")) {
                loginDialogCount = 0;
                CustomDialogUtils.showQZLoginDialog(getActivity(), false);
                CustomDialogUtils.setCancleListener(new CustomDialogUtils.CancleListener() {
                    @Override
                    public void cancleClick() {
                        loginDialogCount = 1;
                    }

                    @Override
                    public void loginSuccess() {
                        if (tcRoomInfo != null) {
                            mPresenter.joinRoom(tcRoomInfo.getRoomId());
                        }
                    }
                });
            } else {
                CustomDialogUtils.showLoginDialog(getActivity(), false);
            }
        } else {
            GuardDialogFragment dialogFragment = GuardDialogFragment.newInstance(tcRoomInfo.getMasterDataList().getHeadUrl(),
                    tcRoomInfo.getMasterDataList().getNickName(), tcRoomInfo.getRoomId(), tcRoomInfo.getMasterDataList().getAccountId());
            dialogFragment.show(getFragmentManager(), "");
        }
    }

    /**
     * 点击用户的私信
     *
     * @param sendId
     * @param nickName
     * @param headUrl
     */
    DerectMsgDialog derectMsgDialog;

    @Override
    public void initDerectMsg(String sendId, String nickName, String headUrl) {
        derectMsgDialog = new DerectMsgDialog(getContext(), sendId, nickName, headUrl, false);
        Window dlgwin = derectMsgDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        dlgwin.setGravity(Gravity.BOTTOM);
        dlgwin.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        UiUtil.initialize(getContext());
        int screenWidth = UiUtil.getScreenWidth();
        int screenHeight = UiUtil.getScreenHeight();
        lp.width = screenWidth;
        lp.height = (int) (screenHeight * 0.5);
        dlgwin.setAttributes(lp);
        derectMsgDialog.show();
    }
}
