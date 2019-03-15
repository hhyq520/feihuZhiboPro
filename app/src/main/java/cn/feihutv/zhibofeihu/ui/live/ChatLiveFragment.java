package cn.feihutv.zhibofeihu.ui.live;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.Html;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import android.widget.SeekBar;
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

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.db.model.SysConfigBean;
import cn.feihutv.zhibofeihu.data.db.model.SysGiftNewBean;
import cn.feihutv.zhibofeihu.data.db.model.SysMountNewBean;
import cn.feihutv.zhibofeihu.data.local.model.TCChatEntity;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadMsgListResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomContriResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.GetExtGameIconRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.GetExtGameIconResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GamePreemptRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GamePreemptResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameRoundResultDetailResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameStatusRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameStatusResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.JackpotCountDownResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.JoinRoomResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.AllRoomMsgPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.BannedLivePush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.ForcedCloseLivePush;
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
import cn.feihutv.zhibofeihu.data.network.socket.model.push.StreamErrorPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.UpdatePlayUrlPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.ValueChangePush;
import cn.feihutv.zhibofeihu.di.component.ActivityComponent;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.rxbus.RxBusSubscribe;
import cn.feihutv.zhibofeihu.rxbus.ThreadMode;
import cn.feihutv.zhibofeihu.ui.base.BaseFragment;
import cn.feihutv.zhibofeihu.ui.live.adapter.ChatMsgRecycleAdapter;
import cn.feihutv.zhibofeihu.ui.live.adapter.GameImageAdapter;
import cn.feihutv.zhibofeihu.ui.live.adapter.TCUserContriListAdapter;
import cn.feihutv.zhibofeihu.ui.live.adapter.WanfaResultAdapter;
import cn.feihutv.zhibofeihu.ui.live.models.GiftModel;
import cn.feihutv.zhibofeihu.ui.me.dynamic.OthersCommunityActivity;
import cn.feihutv.zhibofeihu.ui.me.vip.MyVipActivity;
import cn.feihutv.zhibofeihu.ui.widget.DanmuBase.DanmakuActionManager;
import cn.feihutv.zhibofeihu.ui.widget.DanmuBase.DanmakuChannel;
import cn.feihutv.zhibofeihu.ui.widget.DanmuBase.DanmuModel;
import cn.feihutv.zhibofeihu.ui.widget.MagicTextView;
import cn.feihutv.zhibofeihu.ui.widget.TalkGiftView;
import cn.feihutv.zhibofeihu.ui.widget.bsrgift.BSRGiftLayout;
import cn.feihutv.zhibofeihu.ui.widget.bsrgift.GiftAnmManager;
import cn.feihutv.zhibofeihu.ui.widget.dialog.CaichiHiatoryDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.DerectMsgDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.HuDongDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.JcHistoryDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.JcXiaZhuDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.JincaiDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.KjHistoryDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.LyHistoryDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.LyHuDongDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.LyXiaZhuDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.MessageDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.OrigCaichiDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.ShakeDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.TCInputTextMsgDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.TimeLeftDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.UserDialogFragment;
import cn.feihutv.zhibofeihu.ui.widget.dialog.WeekStarDialog;
import cn.feihutv.zhibofeihu.ui.widget.flashview.FlashDataParser;
import cn.feihutv.zhibofeihu.ui.widget.flashview.FlashView;
import cn.feihutv.zhibofeihu.ui.widget.pathView.ViewPoint;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.FileUtils;
import cn.feihutv.zhibofeihu.utils.ListViewDecoration;
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
import master.flame.danmaku.ui.widget.DanmakuView;

/**
 * <pre>
 *     author : huang hao
 *     time   :
 *     desc   : 界面绘制
 *     version: 1.0
 * </pre>
 */
public class ChatLiveFragment extends BaseFragment implements ChatLiveMvpView, TCInputTextMsgDialog.OnTextSendListener, View.OnClickListener {

    @BindView(R.id.mHeadIcon)
    ImageView mHeadIcon;
    @BindView(R.id.tv_broadcasting_time)
    TextView tv_broadStatus;

    @BindView(R.id.text_member)
    TextView mMemberCount;
    @BindView(R.id.ctl_btnSwitch)
    ImageView ctl_btnSwitch;
    @BindView(R.id.tv_gold)
    TextView tv_gold;
    @BindView(R.id.tv_accountId)
    TextView tv_accountId;
    //头像列表控件
    @BindView(R.id.rv_user_avatar)
    RecyclerView mUserAvatarList;
    @BindView(R.id.tool_bar)
    LinearLayout toolBar;
    @BindView(R.id.ll_hubi)
    LinearLayout llHubi;
    @BindView(R.id.layout_live_pusher_info)
    RelativeLayout layout_live_pusher_info;
    @BindView(R.id.danmakuView)
    DanmakuView danmakuView;
    @BindView(R.id.dan_vip)
    DanmakuChannel vipDanmakuView;
    @BindView(R.id.im_msg_listview)
    RecyclerView mListViewMsg;
    @BindView(R.id.talk_gift_view)
    TalkGiftView talkGiftView;
    Unbinder unbinder;
    @BindView(R.id.btn_more)
    Button btn_more;
    @BindView(R.id.flashview)
    FlashView flashview;
    @BindView(R.id.btn_lucky)
    Button btnLucky;
    @BindView(R.id.btn_guess)
    Button btnGuess;
    @BindView(R.id.btn_shake)
    Button btnShake;
    @BindView(R.id.game_view)
    LinearLayout gameView;
    @BindView(R.id.kaijiang_view)
    LinearLayout kaiJingView;
    @BindView(R.id.icon_help)
    ImageView iconHelp;
    @BindView(R.id.text_time_left)
    TextView textTimeLeft;
    @BindView(R.id.count_view)
    RelativeLayout countView;
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
    @BindView(R.id.btn_message_input)
    Button btnMessageInput;
    @BindView(R.id.btn_derect_msg)
    Button btnDerectMsg;
    @BindView(R.id.btn_audio_ctrl)
    Button btnAudioCtrl;
    @BindView(R.id.view_margin_audio_ctrl)
    TextView viewMarginAudioCtrl;
    @BindView(R.id.btn_share)
    Button btnShare;
    @BindView(R.id.btn_grass)
    Button btnGrass;
    @BindView(R.id.btn_jihu)
    Button btnJihu;
    @BindView(R.id.btn_baoguo)
    Button btnBaoguo;
    @BindView(R.id.icon_history)
    ImageView iconHistory;
    @BindView(R.id.btn_audio_effect)
    Button btnAudioEffect;
    @BindView(R.id.btn_audio_close)
    Button btnAudioClose;
    @BindView(R.id.audio_plugin)
    LinearLayout audioPlugin;
    @BindView(R.id.rl_controllLayer)
    RelativeLayout rlControllLayer;
    @BindView(R.id.rl_publish_root)
    RelativeLayout rlPublishRoot;
    @BindView(R.id.text_caichi_open)
    TextView textCaichiOpen;
    @BindView(R.id.giftcontent)
    LinearLayout giftcontent;
    @BindView(R.id.btn_select_bgm)
    Button btnSelectBgm;
    @BindView(R.id.btn_stop_bgm)
    Button btnStopBgm;
    @BindView(R.id.btn_micore_bgm)
    Button btn_micore_bgm;
    @BindView(R.id.seekBar_bgm_volume)
    SeekBar seekBarBgmVolume;
    @BindView(R.id.seekBar_mic_volume)
    SeekBar seekBarMicVolume;
    @BindView(R.id.seekBar_voice_volume)
    SeekBar seekBarVoiceVolume;
    @BindView(R.id.textView_bgm_volume)
    TextView textViewBgmVolume;
    @BindView(R.id.layoutAudioControl)
    LinearLayout layoutAudioControl;
    @BindView(R.id.msg_new)
    ImageView msgNew;
    @BindView(R.id.pic_ly)
    ImageView pic_ly;
    @BindView(R.id.pic_jc)
    ImageView pic_jc;
    @BindView(R.id.member_frm)
    FrameLayout member_frm;
    @BindView(R.id.zuojia_img)
    ImageView zuojiaImg;
    Unbinder unbinder1;
    @BindView(R.id.gift_layout)
    BSRGiftLayout giftLayout;
    private DanmakuActionManager danmakuActionManager;
    private int selectTime = 9;
    private final int SHOW_MESSAGE = 1;
    private final int STATIC_GIFTSHOW = 2;
    private final int GIFT_SHOW = 3;
    private final int CAICHI = 4;
    private Timer messageTimer;
    private Timer staticGiftTimer;
    private MessageTimerTask messageTimerTask;
    private StaticGiftTimerTask giftTimerTask;
    private Timer mGiftTimer;
    private Timer mCaichiTimer;
    private GiftTimerTask mGiftTimerTask;
    private ObjectAnimator mObjAnim;
    private TCInputTextMsgDialog mInputTextMsgDialog;
    private TCDanmuMgr mDanmuMgr;
    private ChatMsgRecycleAdapter mChatMsgListAdapter;
    private ArrayList<TCChatEntity> mArrayListChatEntity = new ArrayList<>();
    private List<SysGiftNewBean> animGiftQune = new ArrayList<>();
    private List<GiftModel> staticGiftModels = new ArrayList<>();
    private List<GiftModel> vipGiftModels = new ArrayList<>();
    List<LoadRoomContriResponce.RoomContriData> contributeModels = new ArrayList<>();
    private List<View> giftViewCollection = new ArrayList<View>();
    private List<AllRoomMsgPush> allRoomMessage = new ArrayList<>();
    private TCUserContriListAdapter mAvatarListAdapter;
    private PopupWindow popWindow;
    private float mCurrentMicVolume = 1.0f;
    private int giftCheck = 0;
    private int checkLiveState = 0;
    private boolean falshViewisEnd = true;

    ImageView flashLightImg;
    ImageView changeCamImg;
    ImageView goBeautyImg;
    ImageView goChangkongImg;
    ImageView playGameImg;
    /**
     * 动画相关
     */
    private NumAnim giftNumAnim;
    private TranslateAnimation inAnim;
    private TranslateAnimation outAnim;
    private int LyresultCount = 0;
    private int JcresultCount = 0;
    //    private int resultCount=0;
    private String gameName1;
    private int issueRound;
    private int yxjcIssueRound;
    private int lyjcIssueRound;
    private int caichicountDown;
    private String gameResultJson;
    private String jcGameResultJson;
    private String lyGameResultJson;
    private RequestBuilder<Bitmap> mRequestBuilder;
    private boolean zuojiaIsEnd = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder1 = ButterKnife.bind(this, rootView);
        return rootView;
    }

    public interface ChatLiveFragmentListener {
        void closeLive();

        void showBeautyDialog();

        void showContribueDialog();

        void showRoomMrgsDialog();

        void socketDisconnect();

        void socketReconnect();

        void openFlashLight(boolean isOpen);

        void changeCamera(boolean isChange);

        void openMusic(String filePath);

        void stopMusic();

        void micoreMusic();

        void musicProcess(SeekBar seekBar);

        void volumeProcess(SeekBar seekBar);

        void successQuiteRoom();

        void openFile();

        void liveError();

        void micProgress(SeekBar seekBar);
    }

    public interface LiveShareListener {
        void qqShare();

        void qqzoneShare();

        void wxShare();

        void wbShare();

        void wxcircleShare();
    }


    private LiveShareListener liveShareListener;
    private ChatLiveFragmentListener mListener;

    public void setChatLiveFragmentListener(ChatLiveFragmentListener listener) {
        mListener = listener;
    }

    public void setShareListener(LiveShareListener listener) {
        liveShareListener = listener;
    }

    @Override
    public Context getChatContext() {
        return getContext();
    }

    @Override
    public Activity getChatActivity() {
        return getActivity();
    }

    @Override
    public void goReportActivity(String userId) {
        Intent intent = new Intent(getActivity(), ReportActivity.class);
        intent.putExtra("userId", userId);
        getContext().startActivity(intent);
    }

    @Override
    public void goOthersCommunityActivity(String userId) {
        MobclickAgent.onEvent(getContext(), "10054");
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
        mCaichiTimer.schedule(new CaichiTimerTask(), 1000, 1000);
    }

    @Override
    public void notifyLiveState(boolean state) {
        checkLiveState = 0;
        if (!state) {
            if (mListener != null) {
                mListener.liveError();
            }
        }
    }


    private class GiftTimerTask extends TimerTask {
        public void run() {
            dealNews(GIFT_SHOW);
        }
    }

    private class MessageTimerTask extends TimerTask {
        public void run() {
            dealNews(SHOW_MESSAGE);
        }
    }

    private class StaticGiftTimerTask extends TimerTask {
        public void run() {
            dealNews(STATIC_GIFTSHOW);
        }
    }

    private class CaichiTimerTask extends TimerTask {
        public void run() {
            dealNews(CAICHI);
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
                            case STATIC_GIFTSHOW:
                                showStaticGift();
                                break;
                            case GIFT_SHOW:
                                giftShow();
                                break;
                            case CAICHI:
                                dealCaichi();
                                break;
                            default:
                                break;
                        }
                    }
                });
    }

    private void dealCaichi() {
        if (caichiTimeLeft == null) {
            return;
        }
        caichiTimeLeft.setVisibility(View.VISIBLE);
        if (caichicountDown == 0 || caichicountDown > 600) {
            caichiTimeLeft.setVisibility(View.GONE);
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
        checkLiveState++;
        if (checkLiveState == 360) {
            mPresenter.getLiveStatus();
        }

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


//        if(resultCount>0){
//            resultCount++;
//            if(gameName1.equals("yxjc")) {
//                if (resultCount > 15) {
//                    if (issueRound != 0) {
//                        resultCount = 0;
//                        mPresenter.showJoinResult(gameName1, issueRound);
//                    }
//                }
//            }else{
//                if (resultCount > 17) {
//                    if (issueRound != 0) {
//                        resultCount = 0;
//                        mPresenter.showJoinResult(gameName1, issueRound);
//                    }
//                }
//            }
//        }
        if (selectTime == 0) {
            if (pickStyleDialog != null) {
                pickStyleDialog.dismiss();
            }
            if (kaiJingView != null && kaiJingView.getVisibility() == View.VISIBLE) {
                mPresenter.getGameStatus2("yxjc");
            }
        } else {
            selectTime--;
            if (textTimeLeft != null) {
                textTimeLeft.setText(selectTime + "");
            }
        }

        if (!falshViewisEnd) {
            giftCheck++;
        }
        if (giftCheck == 7) {
            falshViewisEnd = true;
        }
        if (vipGiftModels.size() > 0) {
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
                if (giftBean.isMount()) {
                    flashview.reload(animName, "mountFlashAnim");
                } else {
                    flashview.reload(animName, "flashAnims");
                }
                flashview.play(animName, FlashDataParser.FlashLoopTimeOnce);
            }
            flashview.setEventCallback(new FlashDataParser.IFlashViewEventCallback() {
                @Override
                public void onEvent(FlashDataParser.FlashViewEvent e, FlashDataParser.FlashViewEventData data) {
                    if (e.equals(FlashDataParser.FlashViewEvent.STOP)) {
                        falshViewisEnd = true;
                        flashview.setVisibility(View.GONE);
                        giftCheck = 0;
                    }
                }
            });
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

    @Override
    public void getJoinResult(GetGameRoundResultDetailResponce getGameRoundResultDetailResponce, String name) {
        if (getGameRoundResultDetailResponce.getGetGameRoundResultDetailDatas() != null && getGameRoundResultDetailResponce.getGetGameRoundResultDetailDatas().size() > 0) {
            showWanfaResultDialog(getGameRoundResultDetailResponce.getGetGameRoundResultDetailDatas(), name);
        }
    }

    @Override
    public void notifyGameState2(GetGameStatusResponce.GameStatusData data, String name) {
        if (name.equals("yxjc")) {
            int countDown = data.getCountDown();
            int nextStatus = data.getNextStatus();
            if (nextStatus == 1) {
                showTimeleftDialog(countDown, 2);
                dynamicChangeToolBarH(160);
                kaiJingView.setVisibility(View.GONE);
                gameView.setVisibility(View.GONE);
            } else if (nextStatus == 0) {
                showTimeleftDialog(countDown, 2);
                dynamicChangeToolBarH(160);
                kaiJingView.setVisibility(View.GONE);
                gameView.setVisibility(View.GONE);
            } else if (nextStatus == 2) {
                int openStyle = data.getOpenStyle();
                showGuessDialog(openStyle, data, false);
                gameView.setVisibility(View.GONE);
                kaiJingView.setVisibility(View.GONE);
            } else if (nextStatus == 3) {
                int openStyle = data.getOpenStyle();
                showGuessDialog(openStyle, data, true);
                gameView.setVisibility(View.GONE);
                kaiJingView.setVisibility(View.GONE);
            }
        } else {
            int countDown = data.getCountDown();
            boolean isSelf = data.isSelf();
            boolean isGaming = data.isGaming();
            if (isGaming) {
                if (isSelf) {
                    showShakeDialog(data, true);
                } else {
                    showShakeDialog(data, false);
                }
            } else {
                selectTime = 9;
                textTimeLeft.setVisibility(View.VISIBLE);
                dynamicChangeToolBarH(160);
                showTimeleftDialogLy(countDown);
            }
        }
    }

    private boolean isShakeDialogShow = false;

    private void showShakeDialog(GetGameStatusResponce.GameStatusData data, boolean isSelf) {
        isShakeDialogShow = true;
        toolBar.setVisibility(View.INVISIBLE);
        dynamicChangeToolBarH(255);
        setGameIconVisual(false);
        final ShakeDialog jincaiDialog = new ShakeDialog(getContext(), data, isSelf, lyGameResultJson, lyjcIssueRound);
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
                dynamicChangeListviewH(0);
                jincaiDialog.dismiss();
                isShakeDialogShow = false;
                setGameIconVisual(true);
            }
        });
        jincaiDialog.setJincaiListener(new ShakeDialog.JinCaiInterface() {
            @Override
            public void gameHelp() {
                showLyhelpDialog();
                MobclickAgent.onEvent(getContext(), "10070");
            }

            @Override
            public void jinCai(int pos, int playCnt) {
                if (SharePreferenceUtil.getSessionInt(getContext(), "PREF_KEY_LEVEL") < 6) {
                    onToast("六级开启玩法");
                } else {
                    showLyXiazhuDialog(pos, playCnt);
                }
                MobclickAgent.onEvent(getContext(), "10066");
            }

            @Override
            public void huDong(int blankCnt) {
                if (SharePreferenceUtil.getSessionInt(getContext(), "PREF_KEY_LEVEL") < 6) {
                    onToast("六级开启玩法");
                } else {
                    showLyHuDongDialog(blankCnt);
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
                showJcHistoryDialog("lyzb");
                MobclickAgent.onEvent(getContext(), "10057");
            }

            @Override
            public void showResult(int issueRound) {
                if (issueRound != 0) {
                    mPresenter.showJoinResult("lyzb", issueRound);
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
                            public void accept(@NonNull GetGameStatusResponce getGameStatusResponce) throws Exception {
                                if (getGameStatusResponce.getCode() == 0) {
                                    int countDown = getGameStatusResponce.getGameStatusData().getCountDown();
                                    boolean isSelf = getGameStatusResponce.getGameStatusData().isSelf();
                                    boolean isGaming = getGameStatusResponce.getGameStatusData().isGaming();
                                    if (isGaming) {
                                        if (isSelf) {
                                            int openStyle = getGameStatusResponce.getGameStatusData().getOpenStyle();
                                            showGuessDialog(openStyle, getGameStatusResponce.getGameStatusData(), true);

                                        } else {
                                            int openStyle = getGameStatusResponce.getGameStatusData().getOpenStyle();
                                            showGuessDialog(openStyle, getGameStatusResponce.getGameStatusData(), false);
                                        }
                                    } else {
                                        showTimeleftDialog(countDown, 2);
                                        dynamicChangeToolBarH(160);
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
            public void goRoom(String id) {

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
        lp.width = display.getWidth() - TCUtils.dip2px(getContext(), 80); //设置宽度
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
                MobclickAgent.onEvent(getContext(), "10060");
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

    private void showWanfaResultDialog(List<GetGameRoundResultDetailResponce.GetGameRoundResultDetailData> list, String name) {
        final Dialog dialog = new Dialog(getContext(), R.style.color_dialog);
        dialog.setContentView(R.layout.wanfa_result);

        UiUtil.initialize(getContext());
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = dialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();

        lp.width = (int) (display.getWidth() - TCUtils.dip2px(getContext(), 80)); //设置宽度
        dlgwin.setGravity(Gravity.CENTER);
        dialog.getWindow().setAttributes(lp);
        dialog.show();

        Button button = (Button) dialog.findViewById(R.id.btn_know);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new ListViewDecoration(1));
        WanfaResultAdapter adapter = new WanfaResultAdapter(getContext(), name);
        recyclerView.setAdapter(adapter);
        adapter.setDatas(list);
    }


    private void showTimeleftDialog(int countDown, final int style) {
        final TimeLeftDialog jincaiDialog = new TimeLeftDialog(getContext(), countDown);
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
                if (!isGuessDialogShow) {
                    toolBar.setVisibility(View.VISIBLE);
                    dynamicChangeToolBarH(15);
                    setGameIconVisual(true);
                }
            }
        });
        jincaiDialog.setTimeLeftListener(new TimeLeftDialog.TimeLeftInterface() {
            @Override
            public void start() {
                MobclickAgent.onEvent(getContext(), "10036");
                new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                        .doGamePreemptApiCall(new GamePreemptRequest("yxjc", style))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<GamePreemptResponce>() {
                            @Override
                            public void accept(@NonNull GamePreemptResponce responce) throws Exception {
                                if (responce.getCode() == 0) {
                                    showSuccessDialog("yxjc");
                                    jincaiDialog.dismiss();
                                    jincaiDialog.setButtonEnable(true);
                                } else {
                                    jincaiDialog.dismiss();
                                    if (responce.getCode() == 4709) {
                                        onToast("手慢了，下一局再来");
//                                        showFailureDialog("yxjc", true);
                                        jincaiDialog.setButtonEnable(false);
                                    } else if (responce.getCode() == 4710) {
                                        onToast("不能同时抢两个玩法");
                                    } else {
//                                        showFailureDialog("yxjc", false);
                                        onToast("手慢了，下一局再来");
                                        jincaiDialog.setButtonEnable(false);
                                    }
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
            public void begin() {
                MobclickAgent.onEvent(getContext(), "10037");
                new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                        .doGetGameStatusApiCall(new GetGameStatusRequest("yxjc"))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<GetGameStatusResponce>() {
                            @Override
                            public void accept(@NonNull GetGameStatusResponce getGameStatusResponce) throws Exception {
                                if (getGameStatusResponce.getCode() == 0) {
                                    boolean isSelf = getGameStatusResponce.getGameStatusData().isSelf();
                                    boolean isGaming = getGameStatusResponce.getGameStatusData().isGaming();
                                    if (isGaming) {
                                        if (isSelf) {
                                            int openStyle = getGameStatusResponce.getGameStatusData().getOpenStyle();
                                            jincaiDialog.dismiss();
                                            showGuessDialog(openStyle, getGameStatusResponce.getGameStatusData(), true);


                                        } else {
                                            int openStyle = getGameStatusResponce.getGameStatusData().getOpenStyle();
                                            jincaiDialog.dismiss();
                                            showGuessDialog(openStyle, getGameStatusResponce.getGameStatusData(), false);

                                        }
                                    } else {

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
        });
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
            crvheadimage.setTag(R.id.time_value, System.currentTimeMillis());
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
            AllRoomMsgPush pushdata = allRoomMessage.remove(0);
            String msgType = pushdata.getMsgName();
            String msg10 = pushdata.getMsg();
            if (mDanmuMgr != null) {
                if (msgType.equals("Sys")) {
                    mDanmuMgr.addSysDanmu(msg10);
                    return;
                }
            }
            String roomName = pushdata.getRoomName();
            String masterName = pushdata.getMasterName();
            String roomId = pushdata.getRoomId();
            int giftId = pushdata.getGiftId();
            int giftCnt = pushdata.getGiftCnt();
            if (mDanmuMgr != null) {
                if (msgType.equals("CountDown5min")) {
                    mDanmuMgr.addHudongDanmu(msg10, 3);
                } else if (msgType.equals("CountDown60s")) {
                    mDanmuMgr.addHudongDanmu(msg10, 1);
                } else if (msgType.equals("GameStartRound60s")) {
                    mDanmuMgr.addGameStartRound60s();
                } else {
                    String nickname = pushdata.getSender().getNickName();
                    int level = pushdata.getSender().getLevel();
                    String headUrl = pushdata.getSender().getHeadUrl();
                    if (mDanmuMgr != null) {
                        if (msgType.equals("GameMsg")) {
                            mDanmuMgr.addGameDanmu(msg10, masterName, roomId);
                        } else if (msgType.equals("GaoShePao")) {
                            mDanmuMgr.addDaPaoDanmu(masterName, nickname, roomId);
                        } else if (msgType.equals("Loudspeaker")) {
                            if (roomId.equals(tcRoomInfo.getRoomId())) {
                                mDanmuMgr.addLabaDanmu(msg10, nickname, masterName, true, roomId);
                            } else {
                                mDanmuMgr.addLabaDanmu(msg10, nickname, masterName, false, roomId);
                            }
                        } else if (msgType.equals("LargeQuantityGift")) {
                            if ((pushdata.getSender().getVip() > 0 && !pushdata.getSender().isVipExpired()) || (pushdata.getSender().getGuardType() > 0 && !pushdata.getSender().isGuardExpired())) {
                                mDanmuMgr.addGiftDanmu(nickname, masterName, giftId, giftCnt, false, roomId, true);
                            } else {
                                mDanmuMgr.addGiftDanmu(nickname, masterName, giftId, giftCnt, false, roomId, false);
                            }

                        } else if (msgType.equals("Money300")) {
                            mDanmuMgr.addMoney300Danmu(nickname, masterName, giftId, giftCnt, roomId);
                        } else if (msgType.equals("Money180")) {
                            if (roomId.equals(tcRoomInfo.getRoomId())) {
                                if ((pushdata.getSender().getVip() > 0 && !pushdata.getSender().isVipExpired()) || (pushdata.getSender().getGuardType() > 0 && !pushdata.getSender().isGuardExpired())) {
                                    mDanmuMgr.addGiftDanmu(nickname, masterName, giftId, giftCnt, true, roomId, true);
                                } else {
                                    mDanmuMgr.addGiftDanmu(nickname, masterName, giftId, giftCnt, true, roomId, false);
                                }
                            }
                        } else if (msgType.equals("CCAward")) {
                            mDanmuMgr.addCCAwardDanmu(msg10, nickname, giftId);
                        } else if (msgType.equals("GameWin")) {
                            mDanmuMgr.addGamewinDanmu(msg10, nickname);
                        } else if (msgType.equals("BuyVIP")) {
                            DanmuModel model = new DanmuModel(headUrl, level,
                                    "恭喜" + nickname + "开通VIP，成为全平台的贵宾！", pushdata.getSender().getVip(),
                                    pushdata.getSender().isVipExpired(), pushdata.getSender().getGuardType(),
                                    pushdata.getSender().isGuardExpired(), pushdata.getSender().isLiang(), pushdata.getSender().getShowId());
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
                                    nickname + "成功守护主播，果然是真爱啊~", pushdata.getSender().getVip(),
                                    pushdata.getSender().isVipExpired(), pushdata.getSender().getGuardType(),
                                    pushdata.getSender().isGuardExpired(), pushdata.getSender().isLiang(), pushdata.getSender().getShowId());
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


    @Inject
    ChatLiveMvpPresenter<ChatLiveMvpView> mPresenter;

    private JoinRoomResponce.JoinRoomData tcRoomInfo;

    public static ChatLiveFragment getInstance(JoinRoomResponce.JoinRoomData roomInfo) {
        ChatLiveFragment fragment = new ChatLiveFragment();
        fragment.tcRoomInfo = roomInfo;
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat_live;
    }

    @Override
    protected void initWidget(View view) {
        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            mPresenter.onAttach(this);
        }
        initView();
    }

    @Override
    protected void lazyLoad() {

    }


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
        zuojiaImg.setTranslationX(newLoc.x);
        zuojiaImg.setTranslationY(newLoc.y);
    }

    private GiftAnmManager giftAnmManager;

    private void initView() {
        giftAnmManager = new GiftAnmManager(giftLayout, getContext());
        mRequestBuilder = Glide.with(this).asBitmap().apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL));
        if (tcRoomInfo == null) return;
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
        tv_broadStatus.setVisibility(View.VISIBLE);
        tv_broadStatus.setText(R.string.t1);
//        startRecordAnimation();
        mMemberCount.setVisibility(View.VISIBLE);
        String userCount = "";
        if (tcRoomInfo.getOnlineUserCnt() >= 10000) {
            userCount = new BigDecimal((double) tcRoomInfo.getMasterDataList().getFollowers() / 10000).setScale(1, BigDecimal.ROUND_HALF_DOWN) + "万";
        } else {
            userCount = tcRoomInfo.getOnlineUserCnt() + "";
        }
        mMemberCount.setText(userCount);
        ctl_btnSwitch.setVisibility(View.VISIBLE);
        ctl_btnSwitch.setImageResource(R.drawable.btn_switch);
        tv_gold.setText(tcRoomInfo.getMasterDataList().getHB() + "");
        tv_accountId.setText(tcRoomInfo.getMasterDataList().getAccountId() + "");
        mInputTextMsgDialog = new TCInputTextMsgDialog(getContext(), R.style.InputDialog);
        mInputTextMsgDialog.setmOnTextSendListener(this);
        mDanmuMgr = new TCDanmuMgr(mPresenter.getSysConfigBean(), mPresenter.getSysGiftNewBean(), getContext(), false, false, false);
        mDanmuMgr.setDanmakuView(danmakuView);
        danmakuActionManager = new DanmakuActionManager();
        vipDanmakuView.setDanAction(danmakuActionManager);
        danmakuActionManager.addChannel(vipDanmakuView);
        mChatMsgListAdapter = new ChatMsgRecycleAdapter(getContext(), false);
        mChatMsgListAdapter.setOnStrClickListener(new OnStrClickListener() {
            @Override
            public void onItemClick(String id) {
                if (!TextUtils.isEmpty(id) && !id.equals("0")) {
                    mPresenter.showUserInfo(id);
                }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mListViewMsg.setLayoutManager(layoutManager);// 布局管理器。
        mListViewMsg.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        mListViewMsg.setAdapter(mChatMsgListAdapter);

        final TCChatEntity entity = new TCChatEntity();
        entity.setSenderName("");
        entity.setLevel(0);
        entity.setContext("系统提示：我们直播倡导绿色直播，封面和直播内容涉及色情、低俗、暴力、引诱、暴露等都会将被封停账号，同时禁止直播闹事，集会，文明直播从我做起【网警24小时在线巡查】");
        entity.setType(TCConstants.SYSTEM_TYPE);
        entity.setUserId("0");
        notifyMsg(entity);

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
        mAvatarListAdapter = new TCUserContriListAdapter(getContext(), "1");
        mUserAvatarList.getItemAnimator().setChangeDuration(0);
        ((SimpleItemAnimator) mUserAvatarList.getItemAnimator()).setSupportsChangeAnimations(false);
        mAvatarListAdapter.setHasStableIds(true);
        mAvatarListAdapter.setOnItemClickListener(new OnStrClickListener() {
            @Override
            public void onItemClick(String useId) {
                mPresenter.showUserInfo(useId);
            }
        });
        mUserAvatarList.setAdapter(mAvatarListAdapter);
        mPresenter.loadRoomContriList("", 0);
        giftNumAnim = new NumAnim();
        inAnim = (TranslateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.gift_in);
        outAnim = (TranslateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.gift_out);
        clearTiming();
        downPopwindow();
        if (mGiftTimer == null) {
            mGiftTimer = new Timer(true);
            mGiftTimerTask = new GiftTimerTask();
            mGiftTimer.schedule(mGiftTimerTask, 1000, 1000);
        }

        if (mCaichiTimer == null) {
            mCaichiTimer = new Timer(true);
        }
        mPresenter.getJackpotCountDown();
        rlControllLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (talkGiftView.getVisibility() == View.VISIBLE) {
                    talkGiftView.hideSoftInputFromWindow();
                    talkGiftView.setVisibility(View.GONE);
                    dynamicChangeListviewH(0);
                    mUserAvatarList.setVisibility(View.VISIBLE);
                    toolBar.setVisibility(View.VISIBLE);
                    llHubi.setVisibility(View.VISIBLE);
                    layout_live_pusher_info.setVisibility(View.VISIBLE);
                }
                if (gameView.getVisibility() == View.VISIBLE) {
                    toolBar.setVisibility(View.VISIBLE);
                    gameView.setVisibility(View.GONE);
                    dynamicChangeListviewH(0);
                    dynamicChangeToolBarH(15);
                    setGameIconVisual(true);
                }
                if (kaiJingView.getVisibility() == View.VISIBLE) {
                    toolBar.setVisibility(View.VISIBLE);
                    kaiJingView.setVisibility(View.GONE);
                    dynamicChangeListviewH(0);
                    dynamicChangeToolBarH(15);
                    setGameIconVisual(true);
                }

                if (layoutAudioControl.getVisibility() == View.VISIBLE) {
                    layoutAudioControl.setVisibility(View.GONE);
                    setGameIconVisual(true);
                }
            }
        });

        seekBarBgmVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mListener != null) {
                    mListener.musicProcess(seekBar);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarVoiceVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mListener != null) {
                    mListener.volumeProcess(seekBar);
                }
            }
        });
        seekBarMicVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mListener != null) {
                    mListener.micProgress(seekBar);
                }
                mCurrentMicVolume = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void downPopwindow() {
        // showAsDropDown(View anchor);相对某个控件的位置（正左下方），无偏移
        // showAsDropDown(View anchor, int x, int
        // y);相对某个控件的位置，有偏移;x表示相对x轴的偏移，正表示向左，负表示向右；y表示相对y轴的偏移，正是向下，负是向上；
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.pop_down, null);
        // 这里就给具体大小的数字，要不然位置不好计算
        popWindow = new PopupWindow(contentView, TCUtils.dipToPx(getContext(), 45), ViewGroup.LayoutParams.WRAP_CONTENT);
        popWindow.setAnimationStyle(R.style.anim);// 淡入淡出动画
//        popWindow.setTouchable(false);// 是否响应touch事件
        popWindow.setFocusable(true);// 是否具有获取焦点的能力
        // 点击PopupWindow以外的区域，PopupWindow是否会消失。
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        popWindow.setOutsideTouchable(true);
        goChangkongImg = (ImageView) contentView.findViewById(R.id.pop_manger_img);
        flashLightImg = (ImageView) contentView.findViewById(R.id.pop_flash_img);
        changeCamImg = (ImageView) contentView.findViewById(R.id.pop_chagecam_img);
        goBeautyImg = (ImageView) contentView.findViewById(R.id.pop_gobeauty_img);
        playGameImg = (ImageView) contentView.findViewById(R.id.pop_hudong_img);


        flashLightImg.setOnClickListener(this);
        playGameImg.setOnClickListener(this);
        changeCamImg.setOnClickListener(this);
        goBeautyImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    setGameIconVisual(false);
                    mListener.showBeautyDialog();
                    if (popWindow.isShowing()) {
                        popWindow.dismiss();
                    }
                }
            }
        });
        goChangkongImg.setOnClickListener(this);
    }

    /**
     * 定时清除礼物
     */
    private void clearTiming() {
        try {
            TimerTask task = new TimerTask() {
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
            Timer timer = new Timer();
            timer.schedule(task, 0, 3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除礼物view
     */
    private void removeGiftView(final int index) {
        final View removeView = giftcontent.getChildAt(index);
        outAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                giftcontent.removeViewAt(index);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                removeView.startAnimation(outAnim);
            }
        });
    }

    private void sendLoudSpeaker(String msg) {
        talkGiftView.setVisibility(View.GONE);
        mUserAvatarList.setVisibility(View.VISIBLE);
        toolBar.setVisibility(View.VISIBLE);
        llHubi.setVisibility(View.VISIBLE);
        layout_live_pusher_info.setVisibility(View.VISIBLE);
        mPresenter.sendLoudspeaker(msg);
    }


    private void sendMessage(String msg) {
        talkGiftView.setVisibility(View.GONE);
        mUserAvatarList.setVisibility(View.VISIBLE);
        toolBar.setVisibility(View.VISIBLE);
        llHubi.setVisibility(View.VISIBLE);
        layout_live_pusher_info.setVisibility(View.VISIBLE);
        if (msg.length() == 0)
            return;
        try {
            byte[] byte_num = msg.getBytes("utf8");
            if (byte_num.length > 160) {
                Toast.makeText(getContext(), "请输入内容", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }

        final TCChatEntity entity = new TCChatEntity();
        entity.setLevel(SharePreferenceUtil.getSessionInt(getContext(), "PREF_KEY_LEVEL"));
        entity.setSenderName(SharePreferenceUtil.getSession(getContext(), "PREF_KEY_NICKNAME") + "   ");
        entity.setContext(msg);
        entity.setType(TCConstants.TEXT_TYPE);
        mPresenter.sendRoomMsg(entity, msg);
    }


    @Override
    public void showLogin() {

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
    public void onDestroyView() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroyView();
        unbinder1.unbind();
    }

    private void notifyMsg(final TCChatEntity entity) {
        if (mArrayListChatEntity.size() > 200) {
            mArrayListChatEntity.remove(0);
        }
        mArrayListChatEntity.add(entity);
        mChatMsgListAdapter.setDatas(mArrayListChatEntity);
        mListViewMsg.smoothScrollToPosition(mArrayListChatEntity.size() - 1);
//        mListViewMsg.setSelection(mListViewMsg.getCount() - 1);
    }

    /**
     * 软键盘显示与隐藏的监听
     */
    private void softKeyboardListnenr() {
        SoftKeyBoardListener.setListener(getActivity(), new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {/*软键盘显示：执行隐藏title动画，并修改listview高度和装载礼物容器的高度*/
                AppLogger.e(height + "show软键盘高度");
                if (isInputMsgDialogShowing) {
//                    mListViewMsg.setVisibility(View.VISIBLE);
//                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mListViewMsg.getLayoutParams();
//                    layoutParams.bottomMargin =TCUtils.dip2px(getContext(),height);
//                    mListViewMsg.setLayoutParams(layoutParams);
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) toolBar.getLayoutParams();
                    layoutParams.bottomMargin = height;
                    toolBar.setLayoutParams(layoutParams);
                }
            }

            @Override
            public void keyBoardHide(int height) {/*软键盘隐藏：隐藏聊天输入框并显示聊天按钮，执行显示title动画，并修改listview高度和装载礼物容器的高度*/
                AppLogger.e(height + "hide软键盘高度");
                if (talkGiftView.isFaceViewVisual()) {
                    dynamicChangeListviewH(180);
                } else {
                    dynamicChangeListviewH(0);
                }
            }
        });
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

    private void dynamicChangeToolBarH(int heightPX) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) toolBar.getLayoutParams();
        layoutParams.bottomMargin = TCUtils.dip2px(getActivity(), heightPX);
        toolBar.setLayoutParams(layoutParams);
    }

    /**
     * 开启红点与计时动画
     */
    private void startRecordAnimation() {
//        mObjAnim = ObjectAnimator.ofFloat(mRecordBall, "alpha", 1f, 0f, 1f);
//        mObjAnim.setDuration(1000);
//        mObjAnim.setRepeatCount(-1);
//        mObjAnim.start();
    }

    /**
     * 关闭红点与计时动画
     */
    private void stopRecordAnimation() {
//        if (null != mObjAnim)
//            mObjAnim.cancel();
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
        stopRecordAnimation();
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
        if (messageTimer != null) {
            messageTimer.cancel();
        }
        if (staticGiftTimer != null) {
            staticGiftTimer.cancel();
        }
        if (giftTimerTask != null) {
            giftTimerTask.cancel();
        }
        if (animGiftQune.size() > 0) {
            animGiftQune.clear();
        }
        if (staticGiftModels.size() > 0) {
            staticGiftModels.clear();
        }
    }

    public void setGameIconVisual(boolean isVisual) {
        if (isVisual) {
            pic_jc.setVisibility(View.VISIBLE);
            pic_ly.setVisibility(View.VISIBLE);
        } else {
            pic_jc.setVisibility(View.GONE);
            pic_ly.setVisibility(View.GONE);
        }
    }


    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;
    private UserDialogFragment userDialogFragment;

    @OnClick({R.id.layoutAudioControl, R.id.game_view, R.id.kaijiang_view, R.id.lin_mis, R.id.text_music, R.id.member_frm, R.id.btn_zhouxin, R.id.pic_ly, R.id.pic_jc, R.id.ctl_btnSwitch, R.id.btn_message_input, R.id.btn_derect_msg, R.id.btn_share, R.id.ll_hubi, R.id.mHeadIcon, R.id.btn_more, R.id.btn_audio_ctrl, R.id.btn_lucky, R.id.btn_guess, R.id.btn_shake, R.id.icon_help, R.id.btn_grass,
            R.id.btn_jihu, R.id.btn_baoguo, R.id.icon_history, R.id.btn_select_bgm, R.id.btn_stop_bgm, R.id.btn_micore_bgm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutAudioControl:
            case R.id.game_view:
            case R.id.kaijiang_view:
            case R.id.text_music:
            case R.id.lin_mis:
                break;
            case R.id.member_frm:
                setGameIconVisual(false);
                userDialogFragment = UserDialogFragment.newInstance(true);
                userDialogFragment.setOnStrClickListener(new OnStrClickListener() {
                    @Override
                    public void onItemClick(String userId) {
                        setGameIconVisual(true);
                    }
                });
                userDialogFragment.show(getFragmentManager(), "");
                if (kaiJingView.getVisibility() == View.VISIBLE) {
                    kaiJingView.setVisibility(View.GONE);
                    toolBar.setVisibility(View.VISIBLE);
                    kaiJingView.setVisibility(View.GONE);
                    dynamicChangeListviewH(0);
                    dynamicChangeToolBarH(15);
                    setGameIconVisual(true);
                }
                break;
            case R.id.btn_zhouxin:
                gameView.setVisibility(View.GONE);
                dynamicChangeToolBarH(245);
//                showWeekStarDialog();
                break;
            case R.id.pic_ly:
                setGameIconVisual(false);
                dynamicChangeToolBarH(160);
                kaiqiangLx();
                break;
            case R.id.pic_jc:
                setGameIconVisual(false);
                dynamicChangeToolBarH(160);
                kaiqiang(-1);
                break;
            case R.id.ctl_btnSwitch:
                if (mListener != null) {
                    mListener.closeLive();
                }
                break;
            case R.id.btn_message_input:
                MobclickAgent.onEvent(getContext(), "10021");
                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                    lastClickTime = currentTime;
//                    mUserAvatarList.setVisibility(View.INVISIBLE);
                    toolBar.setVisibility(View.INVISIBLE);
//                    llHubi.setVisibility(View.INVISIBLE);
//                    member_frm.setVisibility(View.INVISIBLE);
//                    layout_live_pusher_info.setVisibility(View.INVISIBLE);
                    showInputMsgDialog();
                    hideHudong();
                }
                break;
            case R.id.btn_share:
                // 分享
                showLiveShareDialog();
                hideHudong();
                break;
            case R.id.tv_share_friends:
                MobclickAgent.onEvent(getContext(), "10029");
                if (FHUtils.isAppInstalled(getContext(), "com.tencent.mm")) {
                    if (liveShareListener != null) {
                        liveShareListener.wxcircleShare();
                    }
                } else {
                    onToast("未安装微信");
                }
                break;
            case R.id.tv_share_weibo:
                MobclickAgent.onEvent(getContext(), "10027");
                if (liveShareListener != null) {
                    liveShareListener.wbShare();
                }
                break;
            case R.id.tv_share_wechat:
                MobclickAgent.onEvent(getContext(), "10028");
                if (FHUtils.isAppInstalled(getContext(), "com.tencent.mm")) {
                    if (liveShareListener != null) {
                        liveShareListener.wxShare();
                    }
                } else {
                    onToast("未安装微信");
                }
                break;
            case R.id.tv_share_qzone:
                if (FHUtils.isAppInstalled(getContext(), "com.tencent.mobileqq")) {
                    if (liveShareListener != null) {
                        liveShareListener.qqzoneShare();
                    }
                } else {
                    onToast("未安装QQ");
                }
                break;
            case R.id.tv_share_qq:
                MobclickAgent.onEvent(getContext(), "10030");
                if (FHUtils.isAppInstalled(getContext(), "com.tencent.mobileqq")) {
                    if (liveShareListener != null) {
                        liveShareListener.qqShare();
                    }
                } else {
                    onToast("未安装QQ");
                }
                break;
            case R.id.ll_hubi:
                MobclickAgent.onEvent(getContext(), "10020");
                FansContriDialogFragment dialogFragment = FansContriDialogFragment.newInstance(tcRoomInfo.getMasterDataList().getUserId(), false);
                Bundle bundle = new Bundle();
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getFragmentManager(), "");
                dialogFragment.setOnItemClickListener(new OnStrClickListener() {
                    @Override
                    public void onItemClick(String userId) {
                        mPresenter.showUserInfo(userId);
                    }
                });
                if (kaiJingView.getVisibility() == View.VISIBLE) {
                    kaiJingView.setVisibility(View.GONE);
                    toolBar.setVisibility(View.VISIBLE);
                    kaiJingView.setVisibility(View.GONE);
                    dynamicChangeListviewH(0);
                    dynamicChangeToolBarH(15);
                    setGameIconVisual(true);
                }
                break;
            case R.id.mHeadIcon:
                mPresenter.showUserInfo(SharePreferenceUtil.getSession(getContext(), "PREF_KEY_USERID"));
                break;
            case R.id.btn_more:
                hideHudong();
                if (popWindow.isShowing()) {
                    popWindow.dismiss();
                } else {
                    popWindow.showAtLocation(btn_more, Gravity.BOTTOM | Gravity.RIGHT, TCUtils.dip2px(getContext(), 8), TCUtils.dip2px(getContext(), 51));
//                    popWindow.showAsDropDown(btn_more, popWindow.getWidth()-TCUtils.dip2px(getContext(), 10), TCUtils.dip2px(getContext(), 1));
                }
                break;
            case R.id.pop_flash_img:
                //闪光灯
                openFlashLight();
                break;
            case R.id.pop_chagecam_img:
                //翻转
                changeCam();
                break;
            case R.id.pop_manger_img:
                if (mListener != null) {
                    setGameIconVisual(false);
                    mListener.showRoomMrgsDialog();
                }
                break;
            case R.id.btn_audio_ctrl:
                setGameIconVisual(false);
                layoutAudioControl.setVisibility(layoutAudioControl.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                hideHudong();
                break;
            case R.id.pop_hudong_img:
                showGameDialog();
//                gameView.setVisibility(View.VISIBLE);
//                dynamicChangeToolBarH(205);
                if (popWindow != null && popWindow.isShowing()) {
                    popWindow.dismiss();
                }
//                setGameIconVisual(false);
                MobclickAgent.onEvent(getContext(), "10031");
                break;
            case R.id.btn_lucky:
                gameView.setVisibility(View.GONE);
                showCaichiDialog();
                dynamicChangeToolBarH(245);
                MobclickAgent.onEvent(getContext(), "10032");
                break;
            case R.id.btn_guess:
                kaiqiang(-1);
                break;
            case R.id.btn_shake:
                kaiqiangLx();
                break;
            case R.id.icon_help:
                showGameHelp();
                break;
            case R.id.btn_grass:
                showStyleDialog(2);
                MobclickAgent.onEvent(getContext(), "10033");
                break;
            case R.id.btn_jihu:
                showStyleDialog(1);
                MobclickAgent.onEvent(getContext(), "10034");
                break;
            case R.id.btn_baoguo:
                showStyleDialog(3);
                MobclickAgent.onEvent(getContext(), "10035");
                break;
            case R.id.icon_history:
                showCaichiHistoryDialog();
                break;
            case R.id.btn_derect_msg:
                if (msgNew != null) {
                    msgNew.setVisibility(View.INVISIBLE);
                }
                SharePreferenceUtil.saveSeesionInt(getContext(), "news_count", 0);
                MobclickAgent.onEvent(getContext(), "10023");
                showMessageDialog();
                hideHudong();
                break;
            case R.id.btn_select_bgm:
                btnSelectBgm.setSelected(true);
                btnStopBgm.setSelected(false);
                btn_micore_bgm.setSelected(false);
                openFile();
                break;
            case R.id.btn_stop_bgm:
                if (mListener != null) {
                    btnSelectBgm.setSelected(false);
                    btnStopBgm.setSelected(true);
                    btn_micore_bgm.setSelected(false);
                    mListener.stopMusic();
                }
                break;
            case R.id.btn_micore_bgm:
                if (mListener != null) {
                    btnSelectBgm.setSelected(false);
                    btnStopBgm.setSelected(false);
                    btn_micore_bgm.setSelected(true);
                    mListener.micoreMusic();
                }
                break;
        }
    }

    private void showWeekStarDialog(String url) {
        WeekStarDialog jincaiDialog = new WeekStarDialog(getContext(), false, false, url, getActivity());
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
            public void onDismiss(DialogInterface dialogInterface) {
                toolBar.setVisibility(View.VISIBLE);
                setGameIconVisual(true);
                dynamicChangeToolBarH(15);
            }
        });
        jincaiDialog.show();
    }


    /**
     * 私信
     */
    MessageDialog messageDialog;

    private void showMessageDialog() {
        setGameIconVisual(false);
        messageDialog = new MessageDialog(getContext(), false, null);
        Window dlgwin = messageDialog.getWindow();
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
        messageDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                setGameIconVisual(true);
            }
        });
        messageDialog.show();
    }

    @Override
    public void notifyGameState(GetGameStatusResponce.GameStatusData data, String name, boolean showStyleDialog) {
        if (!showStyleDialog) {
            if (name.equals("yxjc")) {
                int isOpenStyle = data.getOpenStyle();
                int nextStatus = data.getNextStatus();
                if (nextStatus == 1) {
                    selectTime = 9;
                    textTimeLeft.setText("9");
                    textTimeLeft.setVisibility(View.VISIBLE);
                    kaiJingView.setVisibility(View.VISIBLE);
//                    gameView.setVisibility(View.GONE);
                } else if (nextStatus == 0) {
                    selectTime = 9;
                    textTimeLeft.setText("9");
                    textTimeLeft.setVisibility(View.VISIBLE);
                    kaiJingView.setVisibility(View.VISIBLE);
                    gameView.setVisibility(View.GONE);
                } else if (nextStatus == 2) {
                    showGuessDialog(isOpenStyle, data, false);
                    gameView.setVisibility(View.GONE);
                    kaiJingView.setVisibility(View.GONE);
                } else if (nextStatus == 3) {
                    showGuessDialog(isOpenStyle, data, true);
//                    gameView.setVisibility(View.GONE);
                    kaiJingView.setVisibility(View.GONE);
                }
            } else if (name.equals("lyzb")) {
                int countDown = data.getCountDown();
                int nextStatus = data.getNextStatus();
                if (nextStatus == 0) {
                    selectTime = 9;
                    textTimeLeft.setVisibility(View.VISIBLE);
                    gameView.setVisibility(View.GONE);
                    dynamicChangeToolBarH(160);
                    showTimeleftDialogLy(countDown);
                } else if (nextStatus == 1) {
                    selectTime = 9;
                    textTimeLeft.setVisibility(View.VISIBLE);
                    gameView.setVisibility(View.GONE);
                    dynamicChangeToolBarH(160);
                    showTimeleftDialogLy(countDown);
                } else if (nextStatus == 2) {
                    showShakeDialog(data, false);
                    gameView.setVisibility(View.GONE);
                } else if (nextStatus == 3) {
                    showShakeDialog(data, true);
                    gameView.setVisibility(View.GONE);
                }
            }
        } else {
            int countDown = data.getCountDown();
            int nextStatus = data.getNextStatus();
            int openStyle = data.getOpenStyle();
            if (nextStatus == 2) {
                showGuessDialog(openStyle, data, false);
                gameView.setVisibility(View.GONE);
                kaiJingView.setVisibility(View.GONE);
                if (pickStyleDialog != null) {
                    pickStyleDialog.dismiss();
                }
            } else if (nextStatus == 3) {
                showGuessDialog(openStyle, data, true);
                gameView.setVisibility(View.GONE);
                kaiJingView.setVisibility(View.GONE);
                if (pickStyleDialog != null) {
                    pickStyleDialog.dismiss();
                }
            } else {
                showTimeleftDialog(countDown, data.getOpenStyle());
                dynamicChangeToolBarH(160);
                kaiJingView.setVisibility(View.GONE);
                gameView.setVisibility(View.GONE);
                if (pickStyleDialog != null) {
                    pickStyleDialog.dismiss();
                }
            }
        }
    }


    @Override
    public void notifyGamePreempt(GamePreemptResponce gamePreemptResponce, String name, boolean isSuccess) {
        if (name.equals("lyzb")) {
            if (isSuccess) {
                showSuccessDialog("lyzb");
                if (timeLeftDialog != null) {
                    timeLeftDialog.dismiss();
                }
                if (timeLeftDialog != null) {
                    timeLeftDialog.setButtonEnable(true);
                }
            } else {
                if (timeLeftDialog != null) {
                    timeLeftDialog.dismiss();
                }
                if (gamePreemptResponce.getCode() == 4709) {
//                        showFailureDialog("lyzb", true);
                    onToast("手慢了，下一局再来");
                    if (timeLeftDialog != null) {
                        timeLeftDialog.setButtonEnable(false);
                    }
                } else if (gamePreemptResponce.getCode() == 4710) {
                    onToast("不能同时抢两个玩法");
                } else {
//                        showFailureDialog("lyzb", false);
                    onToast("手慢了，下一局再来");
                    if (timeLeftDialog != null) {
                        timeLeftDialog.setButtonEnable(false);
                    }
                }
            }
        } else {

        }
    }

    private void showSuccessDialog(final String type) {
        final Dialog pickDialog = new Dialog(getContext(), R.style.floag_dialog);
        pickDialog.setContentView(R.layout.game_success_dialog);
        pickDialog.setCanceledOnTouchOutside(false);
        Button know = (Button) pickDialog.findViewById(R.id.btn_know);
        know.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDialog.dismiss();
                if (type.equals("yxjc")) {
                    new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                            .doGetGameStatusApiCall(new GetGameStatusRequest("yxjc"))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<GetGameStatusResponce>() {
                                @Override
                                public void accept(@NonNull GetGameStatusResponce getGameStatusResponce) throws Exception {
                                    if (getGameStatusResponce.getCode() == 0) {
                                        boolean isSelf = getGameStatusResponce.getGameStatusData().isSelf();
                                        boolean isGaming = getGameStatusResponce.getGameStatusData().isGaming();
                                        if (isGaming) {
                                            if (isSelf) {
                                                int openStyle = getGameStatusResponce.getGameStatusData().getOpenStyle();
                                                showGuessDialog(openStyle, getGameStatusResponce.getGameStatusData(), true);


                                            } else {
                                                int openStyle = getGameStatusResponce.getGameStatusData().getOpenStyle();
                                                showGuessDialog(openStyle, getGameStatusResponce.getGameStatusData(), false);

                                            }
                                        } else {

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
                } else {
                    new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                            .doGetGameStatusApiCall(new GetGameStatusRequest("lyzb"))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<GetGameStatusResponce>() {
                                @Override
                                public void accept(@NonNull GetGameStatusResponce getGameStatusResponce) throws Exception {
                                    if (getGameStatusResponce.getCode() == 0) {
                                        int countDown = getGameStatusResponce.getGameStatusData().getCountDown();
                                        boolean isSelf = getGameStatusResponce.getGameStatusData().isSelf();
                                        boolean isGaming = getGameStatusResponce.getGameStatusData().isGaming();
                                        if (isGaming) {
                                            if (isSelf) {
                                                showShakeDialog(getGameStatusResponce.getGameStatusData(), true);
                                            } else {
                                                showShakeDialog(getGameStatusResponce.getGameStatusData(), false);
                                            }
                                        } else {

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
            }
        });
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.CENTER);
        lp.width = display.getWidth() - TCUtils.dip2px(getContext(), 60); //设置宽度
        pickDialog.getWindow().setAttributes(lp);
        pickDialog.setCanceledOnTouchOutside(false);
        pickDialog.show();
    }

    private void showFailureDialog(final String type, boolean is) {
        final Dialog pickDialog = new Dialog(getContext(), R.style.floag_dialog);
        pickDialog.setContentView(R.layout.game_failure_dialog);
        pickDialog.setCanceledOnTouchOutside(false);
        Button begin = (Button) pickDialog.findViewById(R.id.btn_know);
        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDialog.dismiss();
            }
        });
        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("yxjc")) {
                    new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                            .doGetGameStatusApiCall(new GetGameStatusRequest("yxjc"))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<GetGameStatusResponce>() {
                                @Override
                                public void accept(@NonNull GetGameStatusResponce getGameStatusResponce) throws Exception {
                                    if (getGameStatusResponce.getCode() == 0) {
                                        showGuessDialog(getGameStatusResponce.getGameStatusData().getOpenStyle(),
                                                getGameStatusResponce.getGameStatusData(), false);
                                        gameView.setVisibility(View.GONE);
                                        kaiJingView.setVisibility(View.GONE);
                                    } else {
                                        onToast("开始失败");
                                        AppLogger.e(getGameStatusResponce.getErrMsg());
                                    }
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(@NonNull Throwable throwable) throws Exception {
                                    onToast("开始失败");
                                }
                            }));
                } else {
                    new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                            .doGetGameStatusApiCall(new GetGameStatusRequest("lyzb"))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<GetGameStatusResponce>() {
                                @Override
                                public void accept(@NonNull GetGameStatusResponce getGameStatusResponce) throws Exception {
                                    if (getGameStatusResponce.getCode() == 0) {
                                        showShakeDialog(getGameStatusResponce.getGameStatusData(), false);
                                        gameView.setVisibility(View.GONE);
                                    } else {
                                        onToast("开始失败");
                                        AppLogger.e(getGameStatusResponce.getErrMsg());
                                    }
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(@NonNull Throwable throwable) throws Exception {
                                    onToast("开始失败");
                                }
                            }));
                }
                pickDialog.dismiss();
            }
        });
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.CENTER);
        lp.width = display.getWidth() - TCUtils.dip2px(getContext(), 60); //设置宽度
        pickDialog.getWindow().setAttributes(lp);
        pickDialog.show();
    }


    private TimeLeftDialog timeLeftDialog;

    private void showTimeleftDialogLy(int countDown) {
        timeLeftDialog = new TimeLeftDialog(getContext(), countDown);
        timeLeftDialog.show();
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = timeLeftDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = display.getWidth(); //设置宽度
        timeLeftDialog.getWindow().setAttributes(lp);
        timeLeftDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (!isShakeDialogShow) {
                    toolBar.setVisibility(View.VISIBLE);
                    setGameIconVisual(true);
                    dynamicChangeToolBarH(15);
                }
            }
        });
        timeLeftDialog.setTimeLeftListener(new TimeLeftDialog.TimeLeftInterface() {
            @Override
            public void start() {
                MobclickAgent.onEvent(getContext(), "10048");
                mPresenter.gamePreempt("lyzb", 0);
            }

            @Override
            public void begin() {
                MobclickAgent.onEvent(getContext(), "10049");
                new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                        .doGetGameStatusApiCall(new GetGameStatusRequest("lyzb"))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<GetGameStatusResponce>() {
                            @Override
                            public void accept(@NonNull GetGameStatusResponce getGameStatusResponce) throws Exception {
                                if (getGameStatusResponce.getCode() == 0) {
                                    int countDown = getGameStatusResponce.getGameStatusData().getCountDown();
                                    boolean isSelf = getGameStatusResponce.getGameStatusData().isSelf();
                                    boolean isGaming = getGameStatusResponce.getGameStatusData().isGaming();
                                    if (isGaming) {
                                        if (isSelf) {
                                            showShakeDialog(getGameStatusResponce.getGameStatusData(), true);
                                            timeLeftDialog.dismiss();
                                        } else {
                                            showShakeDialog(getGameStatusResponce.getGameStatusData(), false);
                                            timeLeftDialog.dismiss();
                                        }
                                    } else {

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
        });
    }

    private boolean isGuessDialogShow = false;

    private void showGuessDialog(int style, GetGameStatusResponce.GameStatusData data, boolean isSelf) {
        isGuessDialogShow = true;
        toolBar.setVisibility(View.INVISIBLE);
        dynamicChangeToolBarH(255);
        setGameIconVisual(false);
        JincaiDialog jincaiDialog = new JincaiDialog(getContext(), style, data, isSelf, jcGameResultJson, yxjcIssueRound);
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
                isGuessDialogShow = false;
                toolBar.setVisibility(View.VISIBLE);
                dynamicChangeListviewH(0);
                dynamicChangeToolBarH(15);
                setGameIconVisual(true);
            }
        });
        jincaiDialog.setJincaiListener(new JincaiDialog.JinCaiInterface() {
            @Override
            public void gameHelp() {
                showJincaiDialog();
            }

            @Override
            public void jinCai(int pos, int playCnt) {
                if (SharePreferenceUtil.getSessionInt(getContext(), "PREF_KEY_LEVEL") < 6) {
                    onToast("六级开启玩法");
                } else {
                    showJcXiazhuDialog(pos, playCnt);
                }

                MobclickAgent.onEvent(getContext(), "10044");
            }

            @Override
            public void huDong(int blankCnt) {
                if (SharePreferenceUtil.getSessionInt(getContext(), "PREF_KEY_LEVEL") < 6) {
                    onToast("六级开启玩法");
                } else {
                    showHuDongDialog(blankCnt);
                }
                MobclickAgent.onEvent(getContext(), "10041");
            }

            @Override
            public void awardHistory() {
                showKjHistoryDialog();
                MobclickAgent.onEvent(getContext(), "10040");
            }

            @Override
            public void jcHistory() {
                showJcHistoryDialog("yxjc");
                MobclickAgent.onEvent(getContext(), "10039");
            }

            @Override
            public void showResult(int issueRound) {
                mPresenter.showJoinResult("yxjc", issueRound);
            }

            @Override
            public void showshakeDialog() {
                mPresenter.getGameStatus2("lyzb");
            }

            @Override
            public void goRoom(String id) {

            }
        });
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


    private void kaiqiang(final int style) {
        mPresenter.getGameStatus("yxjc", false);
    }

    private void kaiqiangLx() {
        mPresenter.getGameStatus("lyzb", false);
    }

    private Dialog pickStyleDialog;

    private void showStyleDialog(final int style) {
        pickStyleDialog = new Dialog(getContext(), R.style.kaijiang_dialog);
        pickStyleDialog.setContentView(R.layout.kaijiang_style_dialog);
        TextView textViewCancel = (TextView) pickStyleDialog.findViewById(R.id.cancel);
        TextView textViewOk = (TextView) pickStyleDialog.findViewById(R.id.ok);
        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickStyleDialog.dismiss();
            }
        });
        textViewOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getGameStatus("yxjc", true);
            }
        });
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickStyleDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = display.getWidth(); //设置宽度
        pickStyleDialog.getWindow().setAttributes(lp);
        pickStyleDialog.show();
    }

    private void showCaichiDialog() {
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
                dynamicChangeToolBarH(15);
                setGameIconVisual(true);
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

    private void showGameHelp() {
        Dialog pickDialog = new Dialog(getContext(), R.style.floag_dialog);
        pickDialog.setContentView(R.layout.game_help_dialog);
        WebView webView = (WebView) pickDialog.findViewById(R.id.webView);
        webView.loadUrl(mPresenter.getSysConfigBean().getHelpDocUrl());
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
    }

    public void setMusicPause(String text) {
        if (btnStopBgm != null) {
            btnStopBgm.setText(text);
        }
    }

    public void setMicBegin(String text) {
        if (btn_micore_bgm != null) {
            btn_micore_bgm.setText(text);
        }
    }


    private boolean mFrontCamera = false;
    private boolean mFlashTurnOn = false;


    private void openFile() {
        if (mListener != null) {
            mListener.openFile();
        }
    }

    private void changeCam() {
        mFrontCamera = !mFrontCamera;
        if (mListener != null) {
            mListener.changeCamera(mFrontCamera);
        }
        if (mFrontCamera) {
            changeCamImg.setImageResource(R.drawable.icon_camera_press);
        } else {
            changeCamImg.setImageResource(R.drawable.icon_camera);
        }
    }

    private void openFlashLight() {
        mFlashTurnOn = !mFlashTurnOn;
        if (mListener != null) {
            mListener.openFlashLight(mFlashTurnOn);
        }
        if (mFlashTurnOn) {
            flashLightImg.setImageResource(R.drawable.icon_flash_press);
        } else {
            flashLightImg.setImageResource(R.drawable.icon_flash);
        }
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
        mInputTextMsgDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mUserAvatarList.setVisibility(View.VISIBLE);
                toolBar.setVisibility(View.VISIBLE);
                llHubi.setVisibility(View.VISIBLE);
                member_frm.setVisibility(View.VISIBLE);
                layout_live_pusher_info.setVisibility(View.VISIBLE);
//                dynamicChangeListviewH(0);
                dynamicChangeToolBarH(15);
                isInputMsgDialogShowing = false;
                setGameIconVisual(true);
            }
        });
        mInputTextMsgDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mInputTextMsgDialog.show();
    }

    private void hideHudong() {
        if (gameView.getVisibility() == View.VISIBLE) {
            gameView.setVisibility(View.GONE);
            dynamicChangeListviewH(0);
            dynamicChangeToolBarH(15);
        }
        if (kaiJingView.getVisibility() == View.VISIBLE) {
            kaiJingView.setVisibility(View.GONE);
            dynamicChangeListviewH(0);
            dynamicChangeToolBarH(15);
        }
    }

    List<GetExtGameIconResponce.ExtGameIconData> datas = new ArrayList<>();
    GameImageAdapter adapter;

    private void showGameDialog() {
        setGameIconVisual(false);
        final Dialog pickDialog3 = new Dialog(getContext(), R.style.floag_dialog);
        pickDialog3.setContentView(R.layout.game_view_host);

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
                //  setGameIconVisual(true);
            }
        });
        final RecyclerView recyclerView = (RecyclerView) pickDialog3.findViewById(R.id.recycle_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new GameImageAdapter(datas);
        recyclerView.setAdapter(adapter);
        showLoading();
        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .doGetExtGameIconCall(new GetExtGameIconRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetExtGameIconResponce>() {
                    @Override
                    public void accept(@NonNull GetExtGameIconResponce responce) throws Exception {
                        if (responce.getCode() == 0) {
                            pickDialog3.show();
                            if (datas.size() > 0) {
                                datas.clear();
                            }
                            datas = responce.getExtGameIconDatas();
                            adapter.setNewData(datas);
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
        ImageView imageView = (ImageView) pickDialog3.findViewById(R.id.icon_help);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGameHelp();
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GetExtGameIconResponce.ExtGameIconData item = (GetExtGameIconResponce.ExtGameIconData) adapter.getItem(position);
                if (item.getHtmlUrl().equals("yxjc")) {
                    dynamicChangeToolBarH(160);
                    setGameIconVisual(false);
                    kaiqiang(-1);
                } else if (item.getHtmlUrl().equals("lyzb")) {
                    setGameIconVisual(false);
                    dynamicChangeToolBarH(160);
                    kaiqiangLx();
                } else if (item.getHtmlUrl().equals("xyjc")) {
                    showCaichiDialog();
                    dynamicChangeToolBarH(245);
                    MobclickAgent.onEvent(getContext(), "10032");
                } else {
                    dynamicChangeToolBarH(245);
                    showWeekStarDialog(item.getHtmlUrl());
                }
                pickDialog3.dismiss();
            }
        });
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
        pickDialog3.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                setGameIconVisual(true);
            }
        });
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
        pickDialog3.show();
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
//            mAvatarListAdapter.setDatas(contributeModels);
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
        final Dialog pickDialog2 = new Dialog(getActivity(), R.style.floag_dialog);
        pickDialog2.setContentView(R.layout.pop_ban);

        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog2.getWindow();
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.CENTER);
        lp.width = (int) (display.getWidth()); //设置宽度
        pickDialog2.getWindow().setAttributes(lp);

        TextView tvContent = (TextView) pickDialog2.findViewById(R.id.tv_pop);
        TextView tvTime = (TextView) pickDialog2.findViewById(R.id.tv_time);
        tvTime.setText("禁播时长为" + Integer.valueOf(time) + "分钟");
        String str = "<font color='#303030'>您因</font>" +
                "<font color='#fd593f'>" + msg + "</font>" +
                "<font color='#303030'>被系统禁播</font>";
        tvContent.setText(Html.fromHtml(str));

        TextView tvOk = (TextView) pickDialog2.findViewById(R.id.tv_ok);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.successQuiteRoom();
                }
                pickDialog2.dismiss();
            }
        });
        pickDialog2.setCanceledOnTouchOutside(false);
        pickDialog2.show();
    }

    ///房间聊天消息推送
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_ROOM_CHAT, threadMode = ThreadMode.MAIN)
    public void onReceiveRoomChatPush(RoomChatPush pushData) {
        String userId = pushData.getSender().getUserId();
        String nickname = pushData.getSender().getNickName();
        int level = pushData.getSender().getLevel();
//            if (userId.equals(SharePreferenceUtil.getSession(getContext(),"PREF_KEY_USERID"))) {
//
//            } else {
        TCChatEntity entity = new TCChatEntity();
        entity.setGuardType(pushData.getSender().getGuardType());
        entity.setGuardExpired(pushData.getSender().isGuardExpired());
        entity.setVip(pushData.getSender().getVip());
        entity.setVipExpired(pushData.getSender().isVipExpired());
        entity.setAccountId(pushData.getSender().getShowId());
        entity.setLiang(pushData.getSender().isLiang());
        entity.setSenderName("" + nickname + "  ");
        entity.setContext(pushData.getMsg());
        entity.setUserId(userId);
        entity.setType(TCConstants.TEXT_TYPE);
        entity.setLevel(level);
        notifyMsg(entity);
//            }
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
        mMemberCount.setText(userCount);
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
                if (!mountBean.getIsAnimation().equals("1")) {
                    GiftModel giftModel = new GiftModel(userId, 0, "驾驶[" + mountBean.getName() + "]进入直播间", pushData.getSender().getNickName(), headUrl, mountId, false);
                    staticGiftModels.add(giftModel);
                } else {
                    if (TextUtils.isEmpty(mountBean.getAnimName())) {
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
        } else {
            entity.setContext("进入直播间");
            entity.setType(TCConstants.JOIN_TYPE);
        }
        entity.setLevel(level);
        entity.setUserId(userId);
        notifyMsg(entity);

        if (pushData.getSender().getVip() > 0 || pushData.getSender().getGuardType() != 0) {
            if (!pushData.getSender().isVipExpired() || !pushData.getSender().isGuardExpired()) {
                if (!TextUtils.isEmpty(entity.getZuojia())) {
                    DanmuModel model = new DanmuModel(headUrl, level,
                            pushData.getSender().getNickName() + entity.getZuojia() + "进入直播间~", pushData.getSender().getVip(),
                            pushData.getSender().isVipExpired(), pushData.getSender().getGuardType(),
                            pushData.getSender().isGuardExpired(), pushData.getSender().isLiang(), pushData.getSender().getShowId());
                    danmakuActionManager.addDanmu(model);
                } else {
                    DanmuModel model = new DanmuModel(headUrl, level,
                            pushData.getSender().getNickName() + "进入直播间~", pushData.getSender().getVip(),
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
            userCount1 = new BigDecimal((double) totalNumber1 / 10000).setScale(1, BigDecimal.ROUND_HALF_DOWN) + "万";
        } else {
            userCount1 = totalNumber1 + "";
        }
        mMemberCount.setText(userCount1);
    }

    ///直播房间内拉流地址变化
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_UPDATE_PLAY_URL, threadMode = ThreadMode.MAIN)
    public void onReceiveUpdatePlayUrlPush(UpdatePlayUrlPush pushData) {

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

    ///收到礼物
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_ROOM_GIFT, threadMode = ThreadMode.MAIN)
    public void onReceiveRoomGiftPush(RoomGiftPush pushData) {
        int contri = pushData.getContri();
        int giftid = pushData.getGiftId();
        int giftcount = pushData.getGiftCnt();
        long income = pushData.getIncome();
        tv_gold.setText(income + "");
        String userId = pushData.getSender().getUserId();
        String nickname = pushData.getSender().getNickName();
        int level = pushData.getSender().getLevel();
        String headUrl = pushData.getSender().getHeadUrl();
        sortContriList(userId, contri, headUrl);
        if (userId.equals(SharePreferenceUtil.getSession(getContext(), "PREF_KEY_USERID"))) {

        } else {
            TCChatEntity entity = new TCChatEntity();
            entity.setUserId(userId);
            entity.setSenderName("" + nickname + "    ");
            SysGiftNewBean giftBean = mPresenter.getGiftBeanByID(String.valueOf(giftid));
            if (giftBean != null) {
                entity.setContext("送出了" + giftBean.getName() + "×" + giftcount);
            } else {
                entity.setContext("送出了" + giftcount + "个" + "礼物");
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
            if ((pushData.getSender().getVip() > 0 && !pushData.getSender().isVipExpired()) || (pushData.getSender().getGuardType() > 0 && !pushData.getSender().isGuardExpired())) {
                giftBean.setVip(true);
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
            if (giftBean.getIsAnimation().equals("1")) {
                animGiftQune.add(giftBean);
            }
        }
    }

    ///主播被关注
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_ROOM_FOLLOW, threadMode = ThreadMode.MAIN)
    public void onReceiveRoomFollowPush(RoomFollowPush pushData) {
        String userId = pushData.getSender().getUserId();
        String nickname = pushData.getSender().getNickName();
        int level = pushData.getSender().getLevel();
//            if (userId.equals(SharePreferenceUtil.getSession(getContext(),"PREF_KEY_USERID"))) {
//
//            } else {
        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName(" " + nickname);
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
//            }
    }

    ///用户被禁言
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_ROOM_BAN, threadMode = ThreadMode.MAIN)
    public void onReceiveRoomBanPush(RoomBanPush pushData) {
        String userId = pushData.getTarget().getUserId();
        String nickname = pushData.getTarget().getNickName();
        String banName = pushData.getSender().getNickName();
        int level = pushData.getTarget().getLevel();
        String headUrl = pushData.getTarget().getHeadUrl();
        if (userId.equals(SharePreferenceUtil.getSession(getContext(), "PREF_KEY_USERID"))) {

        } else {
            TCChatEntity entity = new TCChatEntity();
            entity.setSenderName(" " + nickname);
            entity.setUserId(userId);
            entity.setAccountId(pushData.getSender().getShowId());
            entity.setLiang(pushData.getSender().isLiang());
            entity.setGuardType(pushData.getSender().getGuardType());
            entity.setGuardExpired(pushData.getSender().isGuardExpired());
            entity.setVip(pushData.getSender().getVip());
            entity.setVipExpired(pushData.getSender().isVipExpired());
            entity.setBanName("被" + banName);
            entity.setContext("禁言30分钟");
            entity.setLevel(level);
            entity.setType(TCConstants.BAN_TYPE);
            notifyMsg(entity);
        }
    }

    ///主播被禁播了
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_BANNED_LIVE, threadMode = ThreadMode.MAIN)
    public void onReceiveBannedLivePush(BannedLivePush pushData) {

        String msg1 = pushData.getMsg();
        String duration = pushData.getDuration();
        showBanDialog(msg1, duration);
    }

    ///被迫下播
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_FORCED_CLOSE_LIVE, threadMode = ThreadMode.MAIN)
    public void onReceiveForcedCloseLivePush(ForcedCloseLivePush pushData) {
        AppLogger.e("主播强制下播");
        String msg2 = pushData.getMsg();
        showBanDialog(msg2, "");
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
        } else if (gid == 2) {
            textCaichiOpen.setText("距离幸运香蕉开奖");
        } else if (gid == 8) {
            textCaichiOpen.setText("距离幸运爱心开奖");
        } else if (gid == 20) {
            textCaichiOpen.setText("距离飞虎流星开奖");
        }
        caichicountDown = 600;
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

    ///房管更新
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_ROOM_MGR, threadMode = ThreadMode.MAIN)
    public void onReceiveRoomMgrPush(RoomMgrPush pushData) {
        boolean status = pushData.isStatus();
        if (status) {
            String userId = pushData.getTarget().getUserId();
            String nickname = pushData.getTarget().getNickName();
            int level = pushData.getTarget().getLevel();
            TCChatEntity entity = new TCChatEntity();
            entity.setSenderName("系统提示: ");
            entity.setUserId(userId);
            entity.setContext(nickname + "成为场控");
            entity.setLevel(level);
            entity.setType(TCConstants.SYSTEM_TYPE);
            notifyMsg(entity);
        }
    }

    ///推流状态异常
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_STREAM_ERROR, threadMode = ThreadMode.MAIN)
    public void onReceiveStreamErrorPush(StreamErrorPush pushData) {
        if (mListener != null) {
            mListener.liveError();
        }
    }

    //游戏结果揭晓
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_GAME_RESULT, threadMode = ThreadMode.MAIN)
    public void onReceiveGameResultPush(GameResultPush pushData) {

        gameResultJson = pushData.getResult().toString();
        gameName1 = pushData.getGameName();
        if (gameName1.equals("yxjc")) {
            JcresultCount = 1;
        } else {
            LyresultCount = 1;
        }
        issueRound = pushData.getIssueRound();
        if (gameName1.equals("yxjc")) {
            yxjcIssueRound = issueRound;
            jcGameResultJson = gameResultJson;
        } else {
            lyjcIssueRound = issueRound;
            lyGameResultJson = gameResultJson;
        }
    }

    @RxBusSubscribe(code = RxBusCode.RX_BUS_NOTICE_CODE_USERINFO, threadMode = ThreadMode.MAIN)
    public void onReceiveUserinfo(String userId) {
        mPresenter.showUserInfo(userId);
        if (userDialogFragment != null) {
            userDialogFragment.dismiss();
        }
    }

    @RxBusSubscribe(code = RxBusCode.RX_BUS_NOTICE_CODE_USERINFO, threadMode = ThreadMode.MAIN)
    public void onReceiveUserinfo(Bundle bundle) {
        String id = bundle.getString("userId");
        mPresenter.showUserInfo(id);
        if (userDialogFragment != null) {
            userDialogFragment.dismiss();
        }
    }

    @RxBusSubscribe(code = RxBusCode.RX_BUS_NOTICE_CODE_VIP, threadMode = ThreadMode.MAIN)
    public void onReceiveVip() {
        Intent intent = new Intent(getContext(), MyVipActivity.class);
        intent.putExtra("isFromRoom", true);
        startActivity(intent);
    }


    @RxBusSubscribe(code = RxBusCode.RX_BUS_NOTICE_CODE_HAVACARE, threadMode = ThreadMode.MAIN)
    public void onReceiveHavecare(String userId) {
        mPresenter.notifyCare(true, userId);
        if (messageDialog != null) {
            messageDialog.loadFriends();
        }
    }

    @RxBusSubscribe(code = RxBusCode.RX_BUS_NOTICE_CODE_NOHAVACARE, threadMode = ThreadMode.MAIN)
    public void onReceiveNoHavecare(String userId) {
        mPresenter.notifyCare(false, userId);
        if (messageDialog != null) {
            messageDialog.loadFriends();
        }
    }


    @RxBusSubscribe(code = RxBusCode.RX_BUS_NOTICE_CODE_ROOMINCOME, threadMode = ThreadMode.MAIN)
    public void onReceiveRoomIcome(RoomIncomePush push) {
        tv_gold.setText(push.getIncome() + "");
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
