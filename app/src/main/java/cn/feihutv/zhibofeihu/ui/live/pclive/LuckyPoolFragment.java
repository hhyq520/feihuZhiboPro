package cn.feihutv.zhibofeihu.ui.live.pclive;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
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

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.feihutv.zhibofeihu.BuildConfig;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.GetExtGameIconRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.GetExtGameIconResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameListResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameRoundResultDetailRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameRoundResultDetailResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameStatusRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameStatusResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetJackpotDataRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetJackpotDataResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.JoinRoomResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.LeaveRoomRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.LeaveRoomResponce;
import cn.feihutv.zhibofeihu.ui.base.BaseFragment;
import cn.feihutv.zhibofeihu.ui.home.HotFragment;
import cn.feihutv.zhibofeihu.ui.live.OnItemClickListener;
import cn.feihutv.zhibofeihu.ui.live.adapter.PCGameImageAdapter;
import cn.feihutv.zhibofeihu.ui.live.adapter.WanfaResultAdapter;
import cn.feihutv.zhibofeihu.ui.widget.CaichiView;
import cn.feihutv.zhibofeihu.ui.widget.LoadingView;
import cn.feihutv.zhibofeihu.ui.widget.MyCaichiView;
import cn.feihutv.zhibofeihu.ui.widget.PlayPcJincaiView;
import cn.feihutv.zhibofeihu.ui.widget.PlayPcShakeView;
import cn.feihutv.zhibofeihu.ui.widget.dialog.HuDongDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.JcHistoryDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.JcXiaZhuDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.KjHistoryDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.LyHistoryDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.LyHuDongDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.LyXiaZhuDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.WeekStarDialog;
import cn.feihutv.zhibofeihu.ui.widget.flashview.FlashDataParser;
import cn.feihutv.zhibofeihu.ui.widget.flashview.FlashView;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.AppUtils;
import cn.feihutv.zhibofeihu.utils.CustomDialogUtils;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.FloatViewWindowManger;
import cn.feihutv.zhibofeihu.utils.ListViewDecoration;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.feihutv.zhibofeihu.utils.UiUtil;
import cn.feihutv.zhibofeihu.utils.glideutil.GlideApp;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Administrator on 2017/3/9.
 * <p>
 * 幸运奖池
 */

public class LuckyPoolFragment extends BaseFragment {


    @BindView(R.id.img_lucky)
    ImageView imgLucky;
    @BindView(R.id.img_yaogan)
    ImageView imgYaogan;
    @BindView(R.id.img_tuzi)
    ImageView imgTuzi;
    Unbinder unbinder;
    @BindView(R.id.text_no)
    TextView textNo;
    @BindView(R.id.icon_back)
    ImageView iconBack;
    @BindView(R.id.img_love)
    ImageView imgLove;
    @BindView(R.id.text_love)
    TextView textLove;
    @BindView(R.id.img_cucmb)
    ImageView imgCucmb;
    @BindView(R.id.text_cucmb)
    TextView textCucmb;
    @BindView(R.id.img_banana)
    ImageView imgBanana;
    @BindView(R.id.text_banana)
    TextView textBanana;
    @BindView(R.id.img_meteor)
    ImageView imgMeteor;
    @BindView(R.id.text_meteor)
    TextView textMeteor;
    @BindView(R.id.caichi_view)
    LinearLayout caichiView;
    @BindView(R.id.t1)
    TextView t1;
    @BindView(R.id.t4)
    TextView t4;
    @BindView(R.id.t5)
    TextView t5;
    @BindView(R.id.gift_lin)
    LinearLayout giftLin;
    @BindView(R.id.my_lin)
    LinearLayout myLin;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.record_view)
    LinearLayout recordView;
    @BindView(R.id.text_ly)
    TextView textLy;
    @BindView(R.id.text_yxjc)
    TextView textYxjc;
    @BindView(R.id.linearLayout1)
    LinearLayout linearLayout1;
    @BindView(R.id.relativeLayout2)
    RelativeLayout relativeLayout2;
    @BindView(R.id.relativeLayout3)
    RelativeLayout relativeLayout3;
    @BindView(R.id.text_cucmb_zb)
    TextView textCucmbZb;
    @BindView(R.id.text_banana_zb)
    TextView textBananaZb;
    @BindView(R.id.text_love_zb)
    TextView textLoveZb;
    @BindView(R.id.text_meteor_zb)
    TextView textMeteorZb;
    @BindView(R.id.root_view)
    RelativeLayout rootView;

    PlayPcShakeView playPcShakeView;
    PlayPcJincaiView playPcJincaiView;
    @BindView(R.id.linearLayoutWeek)
    LinearLayout linearLayoutWeek;
    @BindView(R.id.icon_history)
    Button iconHistory;
    @BindView(R.id.wanfa_text)
    Button wanfaText;
    @BindView(R.id.frm_button_bg)
    FrameLayout frm_button_bg;
    @BindView(R.id.caichi_view_first)
    LinearLayout caichiViewFirst;
    @BindView(R.id.recycle_view)
    RecyclerView recycle_view;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.text_title)
    TextView textTitle;
    //    @BindView(R.id.wait_view)
//    ProgressBar waitView;
    private LoadingView loadingView;
    Unbinder unbinder1;

    private JoinRoomResponce.JoinRoomData roomInfo;
    private String roomId;
    private CaichiView view1;
    private CaichiView view4;
    private MyCaichiView view5;
    private List<View> views = new ArrayList<>();
    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    private long timeDuration;

    public static LuckyPoolFragment getInstance(JoinRoomResponce.JoinRoomData roomInfo) {
        LuckyPoolFragment fragment = new LuckyPoolFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        fragment.roomInfo = roomInfo;
        fragment.roomId = roomInfo.getRoomId();
        return fragment;
    }

    private void setImageVisual(boolean isVis) {
        if (isVis) {
            recycle_view.setVisibility(View.VISIBLE);
        } else {
            recycle_view.setVisibility(View.GONE);
        }

    }

    public void showWeekStarDialog(String url,boolean isLand) {
        WeekStarDialog jincaiDialog = new WeekStarDialog(getContext(), true, true, url, getActivity());
        jincaiDialog.show();
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = jincaiDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = display.getWidth(); //设置宽度

        Rect frame = new Rect();
        getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;  //状态栏高
        if (isLand) {
            lp.height =display.getHeight() - TCUtils.dip2px(getContext(), 270)-statusBarHeight;
        }else{
            lp.height =display.getHeight() - TCUtils.dip2px(getContext(), 245)-statusBarHeight;
        }

        jincaiDialog.getWindow().setAttributes(lp);
        jincaiDialog.setOnStrClickListener(new WeekStarDialog.GoRoomListener() {
            @Override
            public void goRoom(LoadRoomResponce.LoadRoomData tcRoomInfo) {
                gotoRoom(tcRoomInfo);
            }

            @Override
            public void showLogin() {
                if (BuildConfig.isForceLoad.equals("1")) {
                    CustomDialogUtils.showQZLoginDialog(getActivity(), false);
                } else {
                    CustomDialogUtils.showLoginDialog(getActivity(), false);
                }
            }
        });
        jincaiDialog.show();
    }

    @Override
    protected void initWidget(View view) {
        setUnBinder(ButterKnife.bind(this, view));

        loadingView = new LoadingView();
        setTextSelect(1);
        view1 = new CaichiView(getContext(), 0, false);
        view4 = new CaichiView(getContext(), 20, false);
        view5 = new MyCaichiView(getContext());
        views.add(view1.getView());
        views.add(view4.getView());
        views.add(view5.getView());
        viewpager.setAdapter(new MyPagerAdapter(views));
        viewpager.setCurrentItem(0);
        viewpager.setOffscreenPageLimit(2);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.e("print", "onPageSelected: ---->" + position);
                viewpager.setCurrentItem(position);
                if (position % 3 == 2) {
                    setTextSelect(5);
                    myLin.setVisibility(View.VISIBLE);
                    giftLin.setVisibility(View.GONE);
                    if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                        if (mListener != null) {
                            mListener.onItemClick(0);
                        }
                        setTextSelect(4);
                        viewpager.setCurrentItem(1);
                    }
                } else if (position % 3 == 1) {
                    setTextSelect(4);
                    myLin.setVisibility(View.GONE);
                    giftLin.setVisibility(View.VISIBLE);
                } else {
                    setTextSelect(1);
                    myLin.setVisibility(View.GONE);
                    giftLin.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        initWanfaIcon();

        playPcJincaiView = new PlayPcJincaiView(getContext());
        playPcShakeView = new PlayPcShakeView(getContext());
        playPcJincaiView.setVisibility(View.GONE);
        playPcShakeView.setVisibility(View.GONE);
        rootView.addView(playPcJincaiView);
        rootView.addView(playPcShakeView);


        playPcJincaiView.setJincaiListener(new PlayPcJincaiView.JinCaiInterface() {
            @Override
            public void gameHelp() {
                showJincaiDialog();
            }

            @Override
            public void jinCai(int pos, int playCnt) {
                if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                    if (BuildConfig.isForceLoad.equals("1")) {
                        CustomDialogUtils.showQZLoginDialog(getActivity(), false);
                    } else {
                        CustomDialogUtils.showLoginDialog(getActivity(), false);
                    }
                } else {
                    if (SharePreferenceUtil.getSessionInt(getContext(), "PREF_KEY_LEVEL") < 6) {
                        FHUtils.showToast("六级开启玩法");
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
                        CustomDialogUtils.showQZLoginDialog(getActivity(), false);
                    } else {
                        CustomDialogUtils.showLoginDialog(getActivity(), false);
                    }
                } else {
                    if (SharePreferenceUtil.getSessionInt(getContext(), "PREF_KEY_LEVEL") < 6) {
                        FHUtils.showToast("六级开启玩法");
                    } else {
                        showHuDongDialog(blankCnt);
                    }
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
                MobclickAgent.onEvent(getContext(), "10039");
                if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                    if (BuildConfig.isForceLoad.equals("1")) {
                        CustomDialogUtils.showQZLoginDialog(getActivity(), false);
                    } else {
                        CustomDialogUtils.showLoginDialog(getActivity(), false);
                    }
                } else {
                    showJcHistoryDialog("yxjc");
                }
            }

            @Override
            public void showResult(int issueRound) {
                if (!isLand)
                    showJoinResult("yxjc", issueRound);
            }

            @Override
            public void showshakeDialog() {
                if (loadingView != null && !loadingView.isAdded()) {
                    loadingView.show(getFragmentManager(), "");
                }
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
                                        if (playPcShakeView != null) {
                                            playPcJincaiView.setVisibility(View.GONE);
                                            playPcJincaiView.setshouldTimerOnFalse();
                                            playPcShakeView.onFresh(responce.getGameStatusData());
                                            playPcShakeView.setVisibility(View.VISIBLE);

                                        }
                                        setImageVisual(false);

                                    } else {
                                        FHUtils.showToast("乐瑶主播还未开启！");
                                    }
                                } else {
                                    FHUtils.showToast("请求失败");
                                }
                                if (loadingView != null) {
                                    loadingView.dismiss();
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
                if (!id.equals(roomInfo.getRoomId())) {
                    new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                            .doGetRoomDataApiCall(new LoadRoomRequest(id))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<LoadRoomResponce>() {
                                @Override
                                public void accept(@NonNull LoadRoomResponce responce) throws Exception {
                                    if (responce.getCode() == 0) {
                                        final LoadRoomResponce.LoadRoomData data = responce.getLoadRoomData();
                                        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                                                .doLoadLeaveRoomApiCall(new LeaveRoomRequest())
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(new Consumer<LeaveRoomResponce>() {
                                                    @Override
                                                    public void accept(@NonNull LeaveRoomResponce responce) throws Exception {
                                                        if (responce.getCode() == 0) {
                                                            gotoRoom(data);
                                                        }
                                                    }
                                                }, new Consumer<Throwable>() {
                                                    @Override
                                                    public void accept(@NonNull Throwable throwable) throws Exception {

                                                    }
                                                })

                                        );

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

        playPcShakeView.setJincaiListener(new PlayPcShakeView.JinCaiInterface() {
            @Override
            public void gameHelp() {
                showLyhelpDialog();
                MobclickAgent.onEvent(getContext(), "10070");
            }

            @Override
            public void jinCai(int pos, int playCnt) {
                if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                    if (BuildConfig.isForceLoad.equals("1")) {
                        CustomDialogUtils.showQZLoginDialog(getActivity(), false);
                    } else {
                        CustomDialogUtils.showLoginDialog(getActivity(), false);
                    }
                } else {
                    if (SharePreferenceUtil.getSessionInt(getContext(), "PREF_KEY_LEVEL") < 6) {
                        FHUtils.showToast("六级开启玩法");
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
                        CustomDialogUtils.showQZLoginDialog(getActivity(), false);
                    } else {
                        CustomDialogUtils.showLoginDialog(getActivity(), false);
                    }
                } else {
                    if (SharePreferenceUtil.getSessionInt(getContext(), "PREF_KEY_LEVEL") < 6) {
                        FHUtils.showToast("六级开启玩法");
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
                        CustomDialogUtils.showQZLoginDialog(getActivity(), false);
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
                if (!isLand)
                    showJoinResult("lyzb", issueRound);
            }

            @Override
            public void showJincaiDlg() {
                if (loadingView != null && !loadingView.isAdded()) {
                    loadingView.show(getFragmentManager(), "");
                }
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

                                        if (playPcJincaiView != null) {
                                            playPcShakeView.setVisibility(View.GONE);
                                            playPcShakeView.setshouldTimerOnFalse();
                                            playPcJincaiView.setVisibility(View.VISIBLE);
                                            playPcJincaiView.onFresh(openStyle, responce.getGameStatusData());
                                        }

                                        setImageVisual(false);

                                    } else {
                                        FHUtils.showToast("游戏竞猜还未开启！");
                                    }
                                } else {
                                    FHUtils.showToast("请求失败");
                                }
                                if (loadingView != null) {
                                    loadingView.dismiss();
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
                if (!roomId.equals(roomInfo.getRoomId())) {
                    new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                            .doGetRoomDataApiCall(new LoadRoomRequest(roomId))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<LoadRoomResponce>() {
                                @Override
                                public void accept(@NonNull LoadRoomResponce responce) throws Exception {
                                    if (responce.getCode() == 0) {
                                        final LoadRoomResponce.LoadRoomData data = responce.getLoadRoomData();
                                        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                                                .doLoadLeaveRoomApiCall(new LeaveRoomRequest())
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(new Consumer<LeaveRoomResponce>() {
                                                    @Override
                                                    public void accept(@NonNull LeaveRoomResponce responce) throws Exception {
                                                        if (responce.getCode() == 0) {
                                                            gotoRoom(data);
                                                        }
                                                    }
                                                }, new Consumer<Throwable>() {
                                                    @Override
                                                    public void accept(@NonNull Throwable throwable) throws Exception {

                                                    }
                                                })

                                        );

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
        int vipLevel= SharePreferenceUtil.getSessionInt(FeihuZhiboApplication.getApplication(), "PREF_KEY_VIP");
        boolean isvipExpired=SharePreferenceUtil.getSessionBoolean(FeihuZhiboApplication.getApplication(), "PREF_KEY_VIPEXPIRED");
        boolean isGuard=SharePreferenceUtil.getSessionBoolean(FeihuZhiboApplication.getApplication(), "meisGuard");
        if (FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().size() > 0) {
            String giftUrl = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosLuckGiftIconRootPath();
            int enableVip1=FeihuZhiboApplication.getApplication().mDataManager.getGiftBeanByID(String.valueOf("1")).getEnableVip();
            int enableVip2=FeihuZhiboApplication.getApplication().mDataManager.getGiftBeanByID(String.valueOf("2")).getEnableVip();
            int enableVip8=FeihuZhiboApplication.getApplication().mDataManager.getGiftBeanByID(String.valueOf("8")).getEnableVip();
            if((vipLevel>0&&!isvipExpired)||isGuard) {
                if(enableVip1==1){
                    GlideApp.loadImg(getContext(), giftUrl + "1_base_vip.png", DiskCacheStrategy.ALL, imgCucmb);
                }else{
                    GlideApp.loadImg(getContext(), giftUrl + "1_base.png", DiskCacheStrategy.ALL, imgCucmb);
                }
                if(enableVip2==1){
                    GlideApp.loadImg(getContext(), giftUrl + "2_base_vip.png", DiskCacheStrategy.ALL, imgBanana);
                }else{
                    GlideApp.loadImg(getContext(), giftUrl + "2_base.png", DiskCacheStrategy.ALL, imgBanana);
                }
                if(enableVip8==1){
                    GlideApp.loadImg(getContext(), giftUrl + "8_base_vip.png", DiskCacheStrategy.ALL, imgLove);
                }else{
                    GlideApp.loadImg(getContext(), giftUrl + "8_base.png", DiskCacheStrategy.ALL, imgLove);
                }



            }else{
                GlideApp.loadImg(getContext(), giftUrl + "1_base.png", DiskCacheStrategy.ALL, imgCucmb);
                GlideApp.loadImg(getContext(), giftUrl + "2_base.png", DiskCacheStrategy.ALL, imgBanana);
                GlideApp.loadImg(getContext(), giftUrl + "8_base.png", DiskCacheStrategy.ALL, imgLove);
            }
            GlideApp.loadImg(getContext(), giftUrl + "20_base.png", DiskCacheStrategy.ALL, imgMeteor);
        }
    }

    List<GetExtGameIconResponce.ExtGameIconData> datas = new ArrayList<>();
    PCGameImageAdapter gameAdapter;

    private void initWanfaIcon() {
        gameAdapter = new PCGameImageAdapter(datas);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getContext(), 4);
        recycle_view.setLayoutManager(gridLayoutManager);
        recycle_view.addItemDecoration(new ListViewDecoration(3));
        recycle_view.setAdapter(gameAdapter);
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
                            datas = responce.getExtGameIconDatas();
                            gameAdapter.setNewData(datas);
                        } else {
                            AppLogger.e(responce.getErrMsg());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                })

        );
        gameAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GetExtGameIconResponce.ExtGameIconData item = (GetExtGameIconResponce.ExtGameIconData) adapter.getItem(position);
                if (item.getHtmlUrl().equals("yxjc")) {
                    timeDuration = System.currentTimeMillis();
                    kaiqiang("yxjc");
                    MobclickAgent.onEvent(getContext(), "100150");
                } else if (item.getHtmlUrl().equals("lyzb")) {
                    timeDuration = System.currentTimeMillis();
                    kaiqiang("lyzb");
                    MobclickAgent.onEvent(getContext(), "10056");
                } else if (item.getHtmlUrl().equals("xyjc")) {
                    setImageVisual(false);
                    caichiView.setVisibility(View.VISIBLE);
                    getCaichiData();
                    MobclickAgent.onEvent(getContext(), "10032");
                } else {
                    showWeekStarDialog(item.getHtmlUrl(),false);
                }
            }
        });
    }

    private void setTextSelect(int pos) {
        if (pos == 1) {
            t1.setSelected(true);
        } else {
            t1.setSelected(false);
        }
        if (pos == 4) {
            t4.setSelected(true);
        } else {
            t4.setSelected(false);
        }
        if (pos == 5) {
            t5.setSelected(true);
        } else {
            t5.setSelected(false);
        }

    }

    private void getCaichiData() {
        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .doGetJackpotDataApiCall(new GetJackpotDataRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetJackpotDataResponce>() {
                    @Override
                    public void accept(@NonNull GetJackpotDataResponce responce) throws Exception {
                        if (responce.getCode() == 0) {
                            int hugua = responce.getJackpotData().getYi();
                            int banana = responce.getJackpotData().getEr();
                            int liux = responce.getJackpotData().getErshi();
                            int love = responce.getJackpotData().getBa();
                            textLove.setText(String.valueOf(love));
                            textBanana.setText(String.valueOf(banana));
                            textMeteor.setText(String.valueOf(liux));
                            textCucmb.setText(String.valueOf(hugua));
                            int master1 = responce.getJackpotData().getMaster1();
                            int master2 = responce.getJackpotData().getMaster2();
                            int master8 = responce.getJackpotData().getMaster8();
                            textCucmbZb.setText(String.valueOf(master1));
                            textBananaZb.setText(String.valueOf(master2));
                            textLoveZb.setText(String.valueOf(master8));
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
    public void onDestroy() {
        if (playPcJincaiView != null) {
            playPcJincaiView.onDestory();
        }
        if (playPcShakeView != null) {
            playPcShakeView.onDestory();
        }
        super.onDestroy();
    }

    public void jcClick() {
        timeDuration = System.currentTimeMillis();
        kaiqiang("yxjc");
    }

    public void lyClick() {
        timeDuration = System.currentTimeMillis();
        kaiqiang("lyzb");
    }

    @OnClick({R.id.linearLayoutWeek, R.id.wanfa_text, R.id.img_lucky, R.id.img_yaogan, R.id.img_tuzi, R.id.icon_back, R.id.icon_history, R.id.t1, R.id.t4, R.id.t5})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linearLayoutWeek:
//               showWeekStarDialog();
                break;
            case R.id.img_lucky:
                setImageVisual(false);
                caichiView.setVisibility(View.VISIBLE);
                getCaichiData();
                MobclickAgent.onEvent(getContext(), "10032");
                break;
            case R.id.img_yaogan:
                timeDuration = System.currentTimeMillis();
                kaiqiang("lyzb");
                MobclickAgent.onEvent(getContext(), "10056");
                break;
            case R.id.img_tuzi:
                timeDuration = System.currentTimeMillis();
                kaiqiang("yxjc");
                MobclickAgent.onEvent(getContext(), "100150");
                break;
            case R.id.icon_back:
                if (webView.getVisibility() == View.GONE && recordView.getVisibility() == View.GONE) {
                    caichiView.setVisibility(View.GONE);
                    setImageVisual(true);
                } else {
                    frm_button_bg.setVisibility(View.VISIBLE);
                    caichiViewFirst.setVisibility(View.VISIBLE);
                    webView.setVisibility(View.GONE);
                    recordView.setVisibility(View.GONE);
                    wanfaText.setSelected(false);
                    iconHistory.setSelected(false);
                    textTitle.setText("幸运彩池");
                }

                break;
            case R.id.icon_history:
                frm_button_bg.setVisibility(View.GONE);
                webView.setVisibility(View.GONE);
                recordView.setVisibility(View.VISIBLE);
                textTitle.setText("幸运记录");
                caichiViewFirst.setVisibility(View.GONE);
                break;
            case R.id.t1:
                setTextSelect(1);
                viewpager.setCurrentItem(0);
                myLin.setVisibility(View.GONE);
                giftLin.setVisibility(View.VISIBLE);
                break;
            case R.id.t4:
                setTextSelect(4);
                viewpager.setCurrentItem(1);
                myLin.setVisibility(View.GONE);
                giftLin.setVisibility(View.VISIBLE);
                break;
            case R.id.t5:
                setTextSelect(5);
                viewpager.setCurrentItem(2);
                myLin.setVisibility(View.VISIBLE);
                giftLin.setVisibility(View.GONE);
//                if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
//                    if(mListener!=null){
//                        mListener.onItemClick(0);
//                    }
//                    setTextSelect(4);
//                    viewpager.setCurrentItem(1);
//                }
                break;
            case R.id.wanfa_text:
                frm_button_bg.setVisibility(View.GONE);
                webView.loadUrl("https://img.feihutv.cn/uploadHtml/luckyPoolDoc_v2.html");
                textTitle.setText("玩法说明");
                webView.setVisibility(View.VISIBLE);
                recordView.setVisibility(View.GONE);
                caichiViewFirst.setVisibility(View.GONE);
//                showCaichiHelp();
                break;
        }
    }

    private boolean isLand = false;

    public void setIsLand(boolean island) {
        this.isLand = island;
    }

    public void setCaichiViewGone() {
        if (caichiView != null) {
            caichiView.setVisibility(View.GONE);
        }
        if (playPcJincaiView != null) {
            if (playPcJincaiView.getVisibility() == View.VISIBLE) {
                playPcJincaiView.setVisibility(View.GONE);
                playPcJincaiView.setshouldTimerOnFalse();
                long timeDur = System.currentTimeMillis() - timeDuration;
                Map<String, String> map_value = new HashMap<String, String>();
                map_value.put("type", "游戏竞猜");
                MobclickAgent.onEventValue(getContext(), "10047", map_value, (int) (timeDur / 1000));
            }
        }
        if (playPcShakeView != null) {
            if (playPcShakeView.getVisibility() == View.VISIBLE) {
                playPcShakeView.setVisibility(View.GONE);
                playPcShakeView.setshouldTimerOnFalse();
                long timeDur = System.currentTimeMillis() - timeDuration;
                Map<String, String> map_value = new HashMap<String, String>();
                map_value.put("type", "乐摇主播");
                MobclickAgent.onEventValue(getContext(), "10047", map_value, (int) (timeDur / 1000));
            }
        }


        setImageVisual(true);

    }


    private void showCaichiHelp() {
        Dialog pickDialog = new Dialog(getContext(), R.style.floag_dialog);
        pickDialog.setContentView(R.layout.caichi_help_dialog);
        WebView webView = (WebView) pickDialog.findViewById(R.id.webView);
        webView.loadUrl("https://img.feihutv.cn/uploadHtml/luckyPoolDoc_v2.html");
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.CENTER);
        lp.width = display.getWidth() - TCUtils.dip2px(getContext(), 75); //设置宽度
        pickDialog.getWindow().setAttributes(lp);
        pickDialog.show();
    }

    private void kaiqiang(final String style) {
        if (loadingView != null && !loadingView.isAdded()) {
            loadingView.show(getFragmentManager(), "");
        }
        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .doGetGameStatusApiCall(new GetGameStatusRequest(style))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetGameStatusResponce>() {
                    @Override
                    public void accept(@NonNull GetGameStatusResponce responce) throws Exception {
                        if (responce.getCode() == 0) {
                            if (style.equals("yxjc")) {
                                int openStyle = responce.getGameStatusData().getOpenStyle();
                                if (caichiView != null) {
                                    caichiView.setVisibility(View.GONE);
                                }
                                if (playPcJincaiView != null) {
                                    playPcJincaiView.setVisibility(View.VISIBLE);
                                    playPcShakeView.setVisibility(View.GONE);
                                    playPcShakeView.setshouldTimerOnFalse();
                                    playPcJincaiView.onFresh(openStyle, responce.getGameStatusData());
                                }
                                setImageVisual(false);
                            } else {
                                if (playPcShakeView != null) {
                                    playPcJincaiView.setVisibility(View.GONE);
                                    playPcJincaiView.setshouldTimerOnFalse();
                                    playPcShakeView.onFresh(responce.getGameStatusData());
                                    playPcShakeView.setVisibility(View.VISIBLE);
                                }

                                setImageVisual(false);
                                if (caichiView != null) {
                                    caichiView.setVisibility(View.GONE);
                                }
                            }
                        } else {
                            FHUtils.showToast("请求失败");
                        }
                        if (loadingView != null) {
                            loadingView.dismiss();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                })
        );
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
//        webView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bgpupelto));
        webView.loadUrl("https://img.feihutv.cn/uploadHtml/lyzbExplain_v2.html");
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
                MobclickAgent.onEvent(getContext(), "10067");
                showJoinSuccess();
            }
        });
    }

    private void showJoinResult(final String name, int issueRound) {
        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .doGetGameRoundResultDetailApiCall(new GetGameRoundResultDetailRequest(name, issueRound))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetGameRoundResultDetailResponce>() {
                    @Override
                    public void accept(@NonNull GetGameRoundResultDetailResponce responce) throws Exception {
                        if (responce.getCode() == 0) {
                            if (responce.getGetGameRoundResultDetailDatas() != null && responce.getGetGameRoundResultDetailDatas().size() > 0) {
                                showWanfaResultDialog(responce.getGetGameRoundResultDetailDatas(), name);
                            }
                        } else {
                            AppLogger.e(responce.getErrMsg());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                })

        );
    }

    private void showWanfaResultDialog(List<GetGameRoundResultDetailResponce.GetGameRoundResultDetailData> list, String name) {
        final Dialog dialog = new Dialog(getContext(), R.style.user_dialog);
        dialog.setContentView(R.layout.wanfa_result);

        UiUtil.initialize(getContext());
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = dialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();

        lp.width = (int) (display.getWidth() - TCUtils.dip2px(getContext(), 50)); //设置宽度
        dlgwin.setGravity(Gravity.CENTER);
        dialog.getWindow().setAttributes(lp);
        dialog.show();

        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new ListViewDecoration());
        WanfaResultAdapter adapter = new WanfaResultAdapter(getContext(), name);
        recyclerView.setAdapter(adapter);
        adapter.setDatas(list);
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                    .doGetGameListApiCall(new GetGameListRequest(roomId))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<GetGameListResponce>() {
                        @Override
                        public void accept(@NonNull GetGameListResponce responce) throws Exception {
                            if (responce.getCode() == 0) {
                                int len = responce.getGameList().size();
                                if (len > 0) {
                                    textNo.setVisibility(View.GONE);
                                    if (len == 1) {
                                        final String name = responce.getGameList().get(0);
                                        if (name.equals("xyjc")) {
//
                                        } else if (name.equals("yxjc")) {
//
                                        } else if (name.equals("lyzb")) {
//
                                        }
                                    } else if (len == 2) {
                                        final String name = responce.getGameList().get(0);
                                        final String name1 = responce.getGameList().get(1);
                                        if (name.equals("xyjc")) {
//
                                        } else if (name.equals("yxjc")) {
//
                                        } else if (name.equals("lyzb")) {
//
                                        }
                                        if (name1.equals("xyjc")) {
//
                                        } else if (name1.equals("yxjc")) {
//
                                        } else if (name1.equals("lyzb")) {

                                        }
                                    } else {
                                    }
                                } else {
                                    textNo.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {

                        }
                    }));
        }
    }

    private void showLyKjHistoryDialog() {
        LyHistoryDialog jincaiDialog = new LyHistoryDialog(getContext(), true);
        jincaiDialog.show();
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = jincaiDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = display.getWidth(); //设置宽度
        lp.height = display.getHeight() - TCUtils.dip2px(getContext(), 218); //设置宽度
        jincaiDialog.getWindow().setAttributes(lp);
    }

    private void showJcHistoryDialog(String gameName) {
        JcHistoryDialog jincaiDialog = new JcHistoryDialog(getContext(), gameName, true);
        jincaiDialog.show();
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = jincaiDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = display.getWidth(); //设置宽度
        lp.height = display.getHeight() - TCUtils.dip2px(getContext(), 218); //设置宽度
        jincaiDialog.getWindow().setAttributes(lp);
    }


    private void gotoRoom(final LoadRoomResponce.LoadRoomData tcRoomInfo) {
        if (tcRoomInfo.getRoomId().equals(roomId)) {
            return;
        }
        FloatViewWindowManger.removeSmallWindow();

        // 1为PC   2为手机
        getActivity().finish();
        AppUtils.startLiveActivity(LuckyPoolFragment.this, tcRoomInfo.getRoomId(), tcRoomInfo.getHeadUrl(), tcRoomInfo.getBroadcastType(), false, HotFragment.START_LIVE_PLAY);
    }

    private void showJincaiDialog() {
        Dialog pickDialog = new Dialog(getContext(), R.style.floag_dialog);
        pickDialog.setContentView(R.layout.jincai_help_dialog);
        WebView webView = (WebView) pickDialog.findViewById(R.id.webView);
//        webView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bgpupelto));
        webView.loadUrl("https://img.feihutv.cn/uploadHtml/yxjcExplain_v2.html");
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
        KjHistoryDialog jincaiDialog = new KjHistoryDialog(getContext(), true);
        jincaiDialog.show();
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = jincaiDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = display.getWidth(); //设置宽度
        lp.height = display.getHeight() - TCUtils.dip2px(getContext(), 218);
        jincaiDialog.getWindow().setAttributes(lp);
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

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_luckpool;
    }


    @Override
    protected void lazyLoad() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder1 = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder1.unbind();
    }

    class MyPagerAdapter extends PagerAdapter {
        private List<View> mViewList;
        // 标题数组
        String[] titles = {"礼物", "包裹"};

        public MyPagerAdapter(List<View> mViewList) {
            this.mViewList = mViewList;
        }

        @Override
        public int getCount() {
            return mViewList.size();//页卡数
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;//官方推荐写法
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View v = mViewList.get(position);
            ViewGroup parent = (ViewGroup) v.getParent();
            //Log.i("ViewPaperAdapter", parent.toString());
            if (parent != null) {
                parent.removeAllViews();
            }

            container.addView(mViewList.get(position));//添加页卡
            return mViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));//删除页卡
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];//页卡标题
        }

    }
}
