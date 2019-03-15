package cn.feihutv.zhibofeihu.ui.widget.dialog;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameBetHistoryRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameBetHistoryResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameBetRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameBetResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameSettleHistoryRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameSettleHistoryResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameStatusResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.GameResultPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.GameStartRoundPush;
import cn.feihutv.zhibofeihu.rxbus.RxBus;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.rxbus.RxBusSubscribe;
import cn.feihutv.zhibofeihu.rxbus.ThreadMode;
import cn.feihutv.zhibofeihu.ui.live.OnItemClickListener;
import cn.feihutv.zhibofeihu.ui.live.adapter.ShakeListAdapter;
import cn.feihutv.zhibofeihu.ui.widget.JincaiView;
import cn.feihutv.zhibofeihu.ui.widget.VerticalViewPager;
import cn.feihutv.zhibofeihu.ui.widget.ZQImageViewRoundOval;
import cn.feihutv.zhibofeihu.ui.widget.dialog.adapter.JcLandHistoryAdapter;
import cn.feihutv.zhibofeihu.ui.widget.dialog.adapter.KjLyhistoryAdapter;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.ListViewDecoration;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import cn.feihutv.zhibofeihu.utils.glideutil.GlideApp;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by huanghao on 2017/6/9.
 */

public class PlaylandShakeDialog extends Dialog {

    @BindView(R.id.guess_history)
    TextView guessHistory;
    @BindView(R.id.award_history)
    TextView awardHistory;
    @BindView(R.id.play)
    TextView play;
    @BindView(R.id.game_process)
    TextView gameProcess;
    @BindView(R.id.game_countdown)
    TextView gameCountdown;
    @BindView(R.id.game_help)
    ImageView gameHelp;
    @BindView(R.id.view_pager)
    VerticalViewPager viewPager;
    @BindView(R.id.img_dot_1)
    ImageView imgDot1;
    @BindView(R.id.img_dot_2)
    ImageView imgDot2;
    @BindView(R.id.img_dot_3)
    ImageView imgDot3;
    @BindView(R.id.head_img1)
    ZQImageViewRoundOval headImg1;
    @BindView(R.id.head_img2)
    ZQImageViewRoundOval headImg2;
    @BindView(R.id.head_img3)
    ZQImageViewRoundOval headImg3;
    @BindView(R.id.tv_num1)
    TextView tvNum1;
    @BindView(R.id.tv_num2)
    TextView tvNum2;
    @BindView(R.id.tv_num3)
    TextView tvNum3;
    @BindView(R.id.play_game)
    ImageView playGame;
    @BindView(R.id.lin_time_left)
    LinearLayout linTimeLeft;
    @BindView(R.id.recycle_view)
    RecyclerView recycleView;
    @BindView(R.id.kaijilu_view)
    RelativeLayout kaijiluView;
    @BindView(R.id.tv_hubi)
    TextView tvHubi;
    @BindView(R.id.yin_coin)
    TextView yinCoin;
    @BindView(R.id.recycle_view_jincai)
    RecyclerView recycleViewJincai;
    @BindView(R.id.jincai_view)
    LinearLayout jincaiView;
    @BindView(R.id.end_bg)
    ImageView endBg;
    @BindView(R.id.img_jincai)
    ImageView imgJincai;
    @BindView(R.id.nick_name)
    TextView nickName;
    @BindView(R.id.go_room)
    Button goRoom;
    @BindView(R.id.lin_begin)
    LinearLayout linBegin;
    @BindView(R.id.list_horizontal)
    RecyclerView listHorizontal;
    @BindView(R.id.frm_top)
    FrameLayout frmTop;
    @BindView(R.id.btn_hudong)
    Button btnHudong;
    @BindView(R.id.first_view)
    FrameLayout firstView;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.line_bottom)
    LinearLayout lineBottom;
    @BindView(R.id.frm_hudong)
    FrameLayout frmHudong;
    @BindView(R.id.frm_jincai)
    FrameLayout frmJincai;
    private List<View> mViewList = new ArrayList<>();
    private Context mContext;
    private GetGameStatusResponce.GameStatusData data;
    private Timer timer;
    private Timer timer1;
    private Timer timer2;
    private int progress = 0;
    private int left = 0;
    private List<Integer> result = new ArrayList<Integer>();
    private JincaiView view1;
    private JincaiView view3;
    private JincaiView view2;
    private int sumTime = 220;
    private int playerCnt;//今日已使用竞猜次数
    private int blankCnt;
    private int imgPos = 0;
    private ShakeListAdapter adapter;
    List<String> filmList = new ArrayList<>();
    private int curIndex = 0;
    private KjLyhistoryAdapter kjhistoryAdapter;
    private JcLandHistoryAdapter jcHistoryAdapter;
    private int gameRound = 1;
    private int tCount = 0;
    private int issueRound;
    private String nickname;
    private String roomId;
    private int sumRound = 10;
    private CarouselLayoutManager carouselLayoutManager;
    private long timeDuration;
    private String gameResultJson;
    private int getIssueRound = 0;
    LyLandHuDongView landHuDongView;
    LyLandXiaZhuView landJcXiaZhuView;
    private int currentViewpager=0;
    //游戏一局开始
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_GAME_START_ROUND, threadMode = ThreadMode.MAIN)
    public void onReceiveGameStartRoundPush(GameStartRoundPush pushData) {
        Message newJoiner = handler.obtainMessage(2);
        Bundle args = new Bundle();
        args.putInt("round", pushData.getRound());
        args.putString("gameName", pushData.getGameName());
        args.putString("nickname", pushData.getNickname());
        args.putString("roomId", pushData.getRoomId());
        newJoiner.obj = args;
        handler.sendMessage(newJoiner);
    }

    //游戏结果揭晓
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_GAME_RESULT, threadMode = ThreadMode.MAIN)
    public void onReceiveGameResultPush(GameResultPush pushData) {
        Message newJoiner = handler.obtainMessage(3);
        Bundle args = new Bundle();
        args.putString("msg", pushData.getResult().toString());
        args.putInt("issueRound", pushData.getIssueRound());
        args.putString("gameName", pushData.getGameName());
        newJoiner.obj = args;
        handler.sendMessage(newJoiner);
    }

    @OnClick(R.id.frm_top)
    public void onClick() {
    }


    public interface JinCaiInterface {
        void gameHelp();

        void jinCai(int pos, int playCnt);

        void huDong(int blanlCnt);

        void showResult(int issueRound);

        void close();

        void showJincaiDlg();

        void goRoom(String id);

        void showLogin();

        void JoinSuccess();
    }

    private JinCaiInterface mListener;

    public void setJincaiListener(JinCaiInterface listener) {
        mListener = listener;
    }

    public PlaylandShakeDialog(Context context, GetGameStatusResponce.GameStatusData data, String gameResultJson, int lyissueRound) {
        super(context, R.style.floag_dialog);
        mContext = context;
        this.data = data;
        timer = new Timer();
        timer1 = new Timer();
        this.gameResultJson = gameResultJson;
        this.getIssueRound = lyissueRound;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (left <= 0) {
                        if (gameRound == 6) {
                            if (endBg != null) {
                                endBg.setVisibility(View.GONE);
                            }
                            tCount++;
                        }

                        if (tCount > 60) {
                            //dismiss();
                        }
                    }

                    if (left <= 0) {
                        gameCountdown.setText("距离下局" + "0\'" + "0\"");
                        return;
                    }
                    left--;
                    if (left >= 40) {
                        int min = (left - 40) / 60;
                        int sec = (left - 40) % 60;
                        if (sec < 10) {
                            gameCountdown.setText("竞猜倒计时" + min + "\'" + "0" + sec + "\"");
                        } else {
                            gameCountdown.setText("竞猜倒计时" + min + "\'" + sec + "\"");
                        }
                    } else if (left >= 30) {
                        gameCountdown.setText("锁定倒计时" + "0\'" + "0" + (left - 30) + "\"");
                    } else if (left >= 20) {
                        gameCountdown.setText("开奖倒计时" + "0\'" + "0" + (left - 20) + "\"");
                    } else if (left > 0) {
                        if (left < 10) {
                            gameCountdown.setText("距离下局" + 0 + "\'" + "0" + left + "\"");
                        } else {
                            gameCountdown.setText("距离下局" + 0 + "\'" + left + "\"");
                        }
                    }

                    if (left <= 40) {
                        if (view1 != null) {
                            view1.setButtonEnable(false);
                        }
                        if (view2 != null) {
                            view2.setButtonEnable(false);
                        }
                        if (view3 != null) {
                            view3.setButtonEnable(false);
                        }
                        btnHudong.setEnabled(false);
                        btnHudong.setBackgroundResource(R.drawable.gray_shape_100);
                    } else {
                        btnHudong.setEnabled(true);
                        btnHudong.setBackgroundResource(R.drawable.yellow_bg);
                    }

                    break;
                case 2:
                    Bundle bundle = (Bundle) msg.obj;
                    gameRound = bundle.getInt("round");
                    String gameName = bundle.getString("gameName");
                    if (gameName.equals("lyzb")) {
                        nickname = bundle.getString("nickname");
                        roomId = bundle.getString("roomId");
                        if (gameRound == 1) {
                            if (!TextUtils.isEmpty(nickname)) {
                                linBegin.setVisibility(View.VISIBLE);
                                nickName.setText(nickname);
                            } else {
                                linBegin.setVisibility(View.GONE);
                            }
                        }
                        if (TextUtils.isEmpty(roomId)) {
                            sumRound = 1;
                        } else {
                            sumRound = 10;
                        }
                        String str = "<font color='#a0a0a0'>游戏已进行</font>" +
                                "<font color='#ffbb00'>" + gameRound + "</font>" +
                                "<font color='#a0a0a0'>/" + sumRound + "</font>";
                        gameProcess.setText(Html.fromHtml(str));
                        left = sumTime;
                        gameCountdown.setText("竞猜倒计时" + "3\'" + "00\"");
                        tvNum1.setVisibility(View.GONE);
                        tvNum2.setVisibility(View.GONE);
                        tvNum3.setVisibility(View.GONE);
                        headImg1.setVisibility(View.GONE);
                        headImg1.setImageDrawable(null);
                        headImg2.setVisibility(View.GONE);
                        headImg2.setImageDrawable(null);
                        headImg3.setVisibility(View.GONE);
                        headImg3.setImageDrawable(null);
                        if (result.size() > 0) {
                            result.clear();
                        }
                    }

                    break;
                case 3:
                    Bundle bundle1 = (Bundle) msg.obj;
                    String json = bundle1.getString("msg");
                    issueRound = bundle1.getInt("issueRound");
                    String gameName1 = bundle1.getString("gameName");
                    if (gameName1.equals("lyzb")) {
                        try {
                            JSONArray jsonArray = new JSONArray(json);
                            if (result.size() > 0) {
                                result.clear();
                            }
                            result.add(jsonArray.getInt(0));
                            result.add(jsonArray.getInt(1));
                            result.add(jsonArray.getInt(2));
                            headImg1.setVisibility(View.VISIBLE);
                            headImg2.setVisibility(View.VISIBLE);
                            headImg3.setVisibility(View.VISIBLE);
                            headImg1.setImageDrawable(null);
                            headImg2.setImageDrawable(null);
                            headImg3.setImageDrawable(null);
                            AnimationDrawable animationDrawable = (AnimationDrawable) playGame.getBackground();
                            animationDrawable.start();
                            if (timer2 == null) {
                                timer2 = new Timer();
                                timer2.schedule(task2, 100, 200);
                            }

                            progress = 0;
                            curIndex = 5;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 4:
                    new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                            .doGetGameBetApiCall(new GetGameBetRequest("lyzb"))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<GetGameBetResponce>() {
                                @Override
                                public void accept(@NonNull GetGameBetResponce getGameBetResponce) throws Exception {
                                    if (getGameBetResponce.getCode() == 0) {
                                        String sum1 = "0";
                                        String sum2 = "0";
                                        String sum3 = "0";
                                        String sum4 = "0";
                                        String sum5 = "0";
                                        String sum6 = "0";
                                        String bet1 = "0";
                                        String bet2 = "0";
                                        String bet3 = "0";
                                        String bet4 = "0";
                                        String bet5 = "0";
                                        String bet6 = "0";
                                        if (getGameBetResponce.getGetGameBetData().getBankerBet().getBet1() != null) {
                                            sum1 = getGameBetResponce.getGetGameBetData().getBankerBet().getBet1();
                                        }
                                        if (getGameBetResponce.getGetGameBetData().getBankerBet().getBet2() != null) {
                                            sum2 = getGameBetResponce.getGetGameBetData().getBankerBet().getBet2();
                                        }
                                        if (getGameBetResponce.getGetGameBetData().getBankerBet().getBet3() != null) {
                                            sum3 = getGameBetResponce.getGetGameBetData().getBankerBet().getBet3();
                                        }
                                        if (getGameBetResponce.getGetGameBetData().getBankerBet().getBet4() != null) {
                                            sum4 = getGameBetResponce.getGetGameBetData().getBankerBet().getBet4();
                                        }
                                        if (getGameBetResponce.getGetGameBetData().getBankerBet().getBet5() != null) {
                                            sum5 = getGameBetResponce.getGetGameBetData().getBankerBet().getBet5();
                                        }
                                        if (getGameBetResponce.getGetGameBetData().getBankerBet().getBet6() != null) {
                                            sum6 = getGameBetResponce.getGetGameBetData().getBankerBet().getBet6();
                                        }
                                        if (getGameBetResponce.getGetGameBetData().getPlayerBet().getBet1() != null) {
                                            bet1 = getGameBetResponce.getGetGameBetData().getPlayerBet().getBet1();
                                        }
                                        if (getGameBetResponce.getGetGameBetData().getPlayerBet().getBet2() != null) {
                                            bet2 = getGameBetResponce.getGetGameBetData().getPlayerBet().getBet2();
                                        }
                                        if (getGameBetResponce.getGetGameBetData().getPlayerBet().getBet3() != null) {
                                            bet3 = getGameBetResponce.getGetGameBetData().getPlayerBet().getBet3();
                                        }
                                        if (getGameBetResponce.getGetGameBetData().getPlayerBet().getBet4() != null) {
                                            bet4 = getGameBetResponce.getGetGameBetData().getPlayerBet().getBet4();
                                        }
                                        if (getGameBetResponce.getGetGameBetData().getPlayerBet().getBet5() != null) {
                                            bet5 = getGameBetResponce.getGetGameBetData().getPlayerBet().getBet5();
                                        }
                                        if (getGameBetResponce.getGetGameBetData().getPlayerBet().getBet6() != null) {
                                            bet6 = getGameBetResponce.getGetGameBetData().getPlayerBet().getBet6();
                                        }
                                        if (view1 != null) {
                                            view1.updateData(Integer.valueOf(bet1), Integer.valueOf(bet2), Integer.valueOf(sum1), Integer.valueOf(sum2));
                                        }
                                        if (view2 != null) {
                                            view2.updateData(Integer.valueOf(bet3), Integer.valueOf(bet4), Integer.valueOf(sum3), Integer.valueOf(sum4));
                                        }
                                        if (view3 != null) {
                                            view3.updateData(Integer.valueOf(bet5), Integer.valueOf(bet6), Integer.valueOf(sum5), Integer.valueOf(sum6));
                                        }
                                        if (left > 119) {
                                            if (Integer.valueOf(sum1) > 0) {
                                                view1.setUpButtonEnable(true);
                                            } else {
                                                view1.setUpButtonEnable(false);
                                            }
                                            if (Integer.valueOf(sum2) > 0) {
                                                view1.setDownButtonEnable(true);
                                            } else {
                                                view1.setDownButtonEnable(false);
                                            }
                                            if (Integer.valueOf(sum3) > 0) {
                                                view2.setUpButtonEnable(true);
                                            } else {
                                                view2.setUpButtonEnable(false);
                                            }
                                            if (Integer.valueOf(sum4) > 0) {
                                                view2.setDownButtonEnable(true);
                                            } else {
                                                view2.setDownButtonEnable(false);
                                            }
                                            if (Integer.valueOf(sum5) > 0) {
                                                view3.setUpButtonEnable(true);
                                            } else {
                                                view3.setUpButtonEnable(false);
                                            }
                                            if (Integer.valueOf(sum6) > 0) {
                                                view3.setDownButtonEnable(true);
                                            } else {
                                                view3.setDownButtonEnable(false);
                                            }
                                        }
                                    } else {
                                        AppLogger.e(getGameBetResponce.getErrMsg());
                                    }
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(@NonNull Throwable throwable) throws Exception {

                                }
                            })

                    );
                    break;
                case 5:
                    progress++;
                    try {
                        if (progress <= 20 || (progress > 30 && progress <= 50) || (progress > 60 && progress <= 80)) {
//                        fancyCoverFlow.setSelection(curIndex);
                            if (carouselLayoutManager != null)
                                carouselLayoutManager.scrollToPosition(curIndex);
                        }
                        if (progress == 20) {
//                        fancyCoverFlow.setSelection(result.get(0));
                            if (carouselLayoutManager != null)
                                carouselLayoutManager.scrollToPosition(result.get(0));
                            if (filmList.size() > 0 && result.size() > 0) {
                                GlideApp.loadImg(mContext, filmList.get(result.get(0)), R.drawable.face, 1000, headImg1);
                            }
                            anim(result.get(0));
                        } else if (progress == 50) {
//                        fancyCoverFlow.setSelection(result.get(1));
                            if (carouselLayoutManager != null)
                                carouselLayoutManager.scrollToPosition(result.get(1));
                            if (filmList.size() > 0 && result.size() > 0) {
                                GlideApp.loadImg(mContext, filmList.get(result.get(1)), R.drawable.face, 1000, headImg2);
                            }
                            anim1(result.get(1));
                        } else if (progress == 80) {
//                        fancyCoverFlow.setSelection(result.get(2));
                            if (carouselLayoutManager != null)
                                carouselLayoutManager.scrollToPosition(result.get(2));
                            if (filmList.size() > 0 && result.size() > 0) {
                                GlideApp.loadImg(mContext, filmList.get(result.get(2)), R.drawable.face, 1000, headImg3);
                            }
                            anim2(result.get(2));
                        } else if (progress > 80) {
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    };


    private void anim(final int num) {
        int width = mContext.getResources().getDisplayMetrics().widthPixels;
        ObjectAnimator objectAnimator = ObjectAnimator
                .ofFloat(headImg1, "translationX", width / 2, 0)
                .setDuration(1000);
        objectAnimator.start();
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                tvNum1.setVisibility(View.VISIBLE);
                tvNum1.setText(num + "");
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void anim1(final int num) {
        int width = mContext.getResources().getDisplayMetrics().widthPixels;
        ObjectAnimator objectAnimator = ObjectAnimator//
                .ofFloat(headImg2, "translationX", width / 2, 0)//
                .setDuration(1000);
        objectAnimator.start();
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                tvNum2.setVisibility(View.VISIBLE);
                tvNum2.setText(num + "");
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void anim2(final int num) {
        int width = mContext.getResources().getDisplayMetrics().widthPixels;
        ObjectAnimator objectAnimator = ObjectAnimator//
                .ofFloat(headImg3, "translationX", width / 2, 0)//
                .setDuration(1000);
        objectAnimator.start();
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                tvNum3.setVisibility(View.VISIBLE);
                tvNum3.setText(num + "");
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void initRecyclerView(final RecyclerView recyclerView, final CarouselLayoutManager layoutManager, final ShakeListAdapter adapter) {
        // enable zoom effect. this line can be customized
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        layoutManager.setMaxVisibleItems(2);

        recyclerView.setLayoutManager(layoutManager);
        // we expect only fixed sized item for now
        recyclerView.setHasFixedSize(true);
        // sample adapter with random data
        recyclerView.setAdapter(adapter);
        // enable center post scrolling
        recyclerView.addOnScrollListener(new CenterScrollListener());
        // enable center post touching on item and item click listener
//        DefaultChildSelectionListener.initCenterItemListener(new DefaultChildSelectionListener.OnCenterItemClickListener() {
//            @Override
//            public void onCenterItemClicked(@NonNull final RecyclerView recyclerView, @NonNull final CarouselLayoutManager carouselLayoutManager, @NonNull final View v) {
//                final int position = recyclerView.getChildLayoutPosition(v);
//                final String msg = String.format(Locale.US, "Item %1$d was clicked", position);
//            }
//        }, recyclerView, layoutManager);

//        layoutManager.addOnItemSelectionListener(new CarouselLayoutManager.OnCenterItemSelectionListener() {
//
//            @Override
//            public void onCenterItemChanged(final int adapterPosition) {
//                if (CarouselLayoutManager.INVALID_POSITION != adapterPosition) {
//
//                }
//            }
//        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_dialog_land_view);
        ButterKnife.bind(this);
        timeDuration = System.currentTimeMillis();
        RxBus.get().register(this);
        headImg1.setType(3);
        headImg1.setRoundRadius(100);
        headImg3.setType(3);
        headImg3.setRoundRadius(100);
        headImg2.setType(3);
        headImg2.setRoundRadius(100);
        play.setSelected(true);
        playGame.setBackgroundResource(R.drawable.paly_anim);
        adapter = new ShakeListAdapter(mContext, filmList);
        carouselLayoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, false);
        initRecyclerView(listHorizontal, carouselLayoutManager, adapter);
        carouselLayoutManager.scrollToPosition(5);
        curIndex = 5;
        timer.schedule(task, 1000, 1000);
        timer1.schedule(task1, 1000, 5000);

        view1 = new JincaiView(mContext, 3, 0, 0, 0, 0, true,true);
        view2 = new JincaiView(mContext, 4, 0, 0, 0, 0, true,true);
        view3 = new JincaiView(mContext, 5, 0, 0, 0, 0, true,true);
        mViewList.add(view1.getView());
        mViewList.add(view2.getView());
        mViewList.add(view3.getView());
        viewPager.setAdapter(new VPagerAdapter(mViewList));
        viewPager.setCurrentItem(0);


        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));// 布局管理器。
        recycleView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        recycleView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        recycleView.addItemDecoration(new ListViewDecoration(1));// 添加分割线。
        kjhistoryAdapter = new KjLyhistoryAdapter(mContext,true);
        recycleView.setAdapter(kjhistoryAdapter);


        recycleViewJincai.setLayoutManager(new LinearLayoutManager(getContext()));// 布局管理器。
        recycleViewJincai.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        recycleViewJincai.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        recycleViewJincai.addItemDecoration(new ListViewDecoration(1));
        jcHistoryAdapter = new JcLandHistoryAdapter(mContext, 2);
        recycleViewJincai.setAdapter(jcHistoryAdapter);

        view1.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (mListener != null) {
                    if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                        if (mListener != null) {
                            mListener.showLogin();
                        }
                    }else {
                        if (SharePreferenceUtil.getSessionInt(FeihuZhiboApplication.getApplication(),"PREF_KEY_LEVEL") < 6) {
                            FHUtils.showToast("六级开启玩法");
                            return;
                        }
                        if (position == 1) {
                            firstView.setVisibility(View.GONE);
                            kaijiluView.setVisibility(View.GONE);
                            jincaiView.setVisibility(View.GONE);
                            frmHudong.setVisibility(View.GONE);
                            frmJincai.setVisibility(View.VISIBLE);
                            landJcXiaZhuView.onFresh(1, playerCnt);
                        } else {
                            firstView.setVisibility(View.GONE);
                            kaijiluView.setVisibility(View.GONE);
                            jincaiView.setVisibility(View.GONE);
                            frmHudong.setVisibility(View.GONE);
                            frmJincai.setVisibility(View.VISIBLE);
                            landJcXiaZhuView.onFresh(2, playerCnt);
                        }
                    }
                }
            }
        });
        view2.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (mListener != null) {
                    if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                        if (mListener != null) {
                            mListener.showLogin();
                        }
                    }else {
                        if (SharePreferenceUtil.getSessionInt(FeihuZhiboApplication.getApplication(),"PREF_KEY_LEVEL") < 6) {
                            FHUtils.showToast("六级开启玩法");
                            return;
                        }
                        if (position == 1) {
                            firstView.setVisibility(View.GONE);
                            kaijiluView.setVisibility(View.GONE);
                            jincaiView.setVisibility(View.GONE);
                            frmHudong.setVisibility(View.GONE);
                            frmJincai.setVisibility(View.VISIBLE);
                            landJcXiaZhuView.onFresh(3, playerCnt);
                        } else {
                            firstView.setVisibility(View.GONE);
                            kaijiluView.setVisibility(View.GONE);
                            jincaiView.setVisibility(View.GONE);
                            frmHudong.setVisibility(View.GONE);
                            frmJincai.setVisibility(View.VISIBLE);
                            landJcXiaZhuView.onFresh(4, playerCnt);
                        }
                    }
                }
            }
        });
        view3.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (mListener != null) {
                    if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                        if (mListener != null) {
                            mListener.showLogin();
                        }
                    }else {
                        if (SharePreferenceUtil.getSessionInt(FeihuZhiboApplication.getApplication(),"PREF_KEY_LEVEL") < 6) {
                            FHUtils.showToast("六级开启玩法");
                            return;
                        }
                        if (position == 1) {
                            firstView.setVisibility(View.GONE);
                            kaijiluView.setVisibility(View.GONE);
                            jincaiView.setVisibility(View.GONE);
                            frmHudong.setVisibility(View.GONE);
                            frmJincai.setVisibility(View.VISIBLE);
                            landJcXiaZhuView.onFresh(5, playerCnt);
                        } else {
                            firstView.setVisibility(View.GONE);
                            kaijiluView.setVisibility(View.GONE);
                            jincaiView.setVisibility(View.GONE);
                            frmHudong.setVisibility(View.GONE);
                            frmJincai.setVisibility(View.VISIBLE);
                            landJcXiaZhuView.onFresh(6, playerCnt);
                        }
                    }
                }
            }
        });
        gameRound = data.getRound();
        sumRound = data.getTotalRound();
        if (gameRound == 0) {
            gameRound = 1;
        }
        int time = data.getTime();
        int severTime = data.getServerTime();
        left = sumTime - (severTime - time);
        String str = "<font color='#a0a0a0'>游戏已进行</font>" +
                "<font color='#ffbb00'>" + gameRound + "</font>" +
                "<font color='#a0a0a0'>/" + sumRound + "</font>";
        gameProcess.setText(Html.fromHtml(str));
        playerCnt = data.getPlayerCnt();
        blankCnt = data.getBankerCnt();
        issueRound = data.getIssueRound();
        nickname = data.getNickname();
        roomId = data.getRoomId();
        if (!TextUtils.isEmpty(nickname)) {
            nickName.setText(nickname);
        } else {
            linBegin.setVisibility(View.GONE);
        }
        goRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.goRoom(roomId);
                }
            }
        });
        if (left >= 40) {
            int min = (left - 40) / 60;
            int sec = (left - 40) % 60;
            if (sec < 10) {
                gameCountdown.setText("竞猜倒计时" + min + "\'" + "0" + sec + "\"");
            } else {
                gameCountdown.setText("竞猜倒计时" + min + "\'" + sec + "\"");
            }
        } else if (left >= 30) {
            gameCountdown.setText("锁定倒计时" + "0\'" + "0" + (left - 30) + "\"");
        } else if (left >= 20) {
            gameCountdown.setText("开奖倒计时" + "0\'" + "0" + (left - 20) + "\"");
        } else if (left > 0) {
            if (left < 10) {
                gameCountdown.setText("距离下局" + 0 + "\'" + "0" + left + "\"");
            } else {
                gameCountdown.setText("距离下局" + 0 + "\'" + left + "\"");
            }
        } else if (left <= 0) {
            left = 220;
            gameCountdown.setText("竞猜倒计时" + "3\'" + "40\"");
        }

        if (endBg != null && gameRound == 6 && left < 60) {
            endBg.setVisibility(View.GONE);
        }
        for (int i = 0; i < data.getTop10Avatar().size(); i++) {
            filmList.add(data.getTop10Avatar().get(i));
        }
        adapter.setDatas(filmList);
        String sum1 = "0";
        String sum2 = "0";
        String sum3 = "0";
        String sum4 = "0";
        String sum5 = "0";
        String sum6 = "0";
        String bet1 = "0";
        String bet2 = "0";
        String bet3 = "0";
        String bet4 = "0";
        String bet5 = "0";
        String bet6 = "0";
        if (data.getBankerBet().getBet1() != null) {
            sum1 = data.getBankerBet().getBet1();
        }
        if (data.getBankerBet().getBet2() != null) {
            sum2 = data.getBankerBet().getBet2();
        }
        if (data.getBankerBet().getBet3() != null) {
            sum3 = data.getBankerBet().getBet3();
        }
        if (data.getBankerBet().getBet4() != null) {
            sum4 = data.getBankerBet().getBet4();
        }
        if (data.getBankerBet().getBet5() != null) {
            sum5 = data.getBankerBet().getBet5();
        }
        if (data.getBankerBet().getBet6() != null) {
            sum6 = data.getBankerBet().getBet6();
        }
        if (data.getPlayerBet().getBet1() != null) {
            bet1 = data.getPlayerBet().getBet1();
        }
        if (data.getPlayerBet().getBet2() != null) {
            bet2 = data.getPlayerBet().getBet2();
        }
        if (data.getPlayerBet().getBet3() != null) {
            bet3 = data.getPlayerBet().getBet3();
        }
        if (data.getPlayerBet().getBet4() != null) {
            bet4 = data.getPlayerBet().getBet4();
        }
        if (data.getPlayerBet().getBet5() != null) {
            bet5 = data.getPlayerBet().getBet5();
        }
        if (data.getPlayerBet().getBet6() != null) {
            bet6 = data.getPlayerBet().getBet6();
        }

        view1.updateData(Integer.valueOf(bet1), Integer.valueOf(bet2), Integer.valueOf(sum1), Integer.valueOf(sum2));
        view2.updateData(Integer.valueOf(bet3), Integer.valueOf(bet4), Integer.valueOf(sum3), Integer.valueOf(sum4));
        view3.updateData(Integer.valueOf(bet5), Integer.valueOf(bet6), Integer.valueOf(sum5), Integer.valueOf(sum6));
        if (left > 40) {
            if (Integer.valueOf(sum1) > 0) {
                view1.setUpButtonEnable(true);
            } else {
                view1.setUpButtonEnable(false);
            }
            if (Integer.valueOf(sum2) > 0) {
                view1.setDownButtonEnable(true);
            } else {
                view1.setDownButtonEnable(false);
            }
            if (Integer.valueOf(sum3) > 0) {
                view2.setUpButtonEnable(true);
            } else {
                view2.setUpButtonEnable(false);
            }
            if (Integer.valueOf(sum4) > 0) {
                view2.setDownButtonEnable(true);
            } else {
                view2.setDownButtonEnable(false);
            }
            if (Integer.valueOf(sum5) > 0) {
                view3.setUpButtonEnable(true);
            } else {
                view3.setUpButtonEnable(false);
            }
            if (Integer.valueOf(sum6) > 0) {
                view3.setDownButtonEnable(true);
            } else {
                view3.setDownButtonEnable(false);
            }
        }


        imgDot1.setSelected(true);
        imgDot2.setSelected(false);
        imgDot3.setSelected(false);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    imgDot1.setSelected(true);
                    imgDot2.setSelected(false);
                    imgDot3.setSelected(false);
                } else if (position == 1) {
                    imgDot2.setSelected(true);
                    imgDot1.setSelected(false);
                    imgDot3.setSelected(false);
                } else {
                    imgDot2.setSelected(false);
                    imgDot3.setSelected(true);
                    imgDot1.setSelected(false);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (!TextUtils.isEmpty(gameResultJson) && getIssueRound != 0 && filmList.size() > 0) {
            if (getIssueRound == issueRound) {
                try {
                    JSONArray jsonArray = new JSONArray(gameResultJson);
                    if (result.size() > 0) {
                        result.clear();
                    }
                    result.add(jsonArray.getInt(0));
                    result.add(jsonArray.getInt(1));
                    result.add(jsonArray.getInt(2));
                    headImg1.setVisibility(View.VISIBLE);
                    headImg2.setVisibility(View.VISIBLE);
                    headImg3.setVisibility(View.VISIBLE);
                    if (left < 20 && left > 17) {
                        if (timer2 == null) {
                            timer2 = new Timer();
                            timer2.schedule(task2, 100, 100);
                        }

                        progress = 0;
                        curIndex = 5;
                    } else if (left > 14 && left <= 17) {
                        tvNum1.setVisibility(View.VISIBLE);
                        tvNum1.setText(result.get(0) + "");
                        Glide.with(mContext).load(filmList.get(result.get(0))).apply(new RequestOptions()
                                .placeholder(R.drawable.face).diskCacheStrategy(DiskCacheStrategy.RESOURCE).centerCrop()).transition(DrawableTransitionOptions.withCrossFade(1000))
                                .into(headImg1);
                        if (timer2 == null) {
                            timer2 = new Timer();
                            timer2.schedule(task2, 100, 100);
                        }
                        progress = 30;
                        curIndex = result.get(0);
                    } else if (left > 10 && left <= 14) {
                        tvNum1.setVisibility(View.VISIBLE);
                        tvNum1.setText(result.get(0) + "");
                        tvNum2.setVisibility(View.VISIBLE);
                        tvNum2.setText(result.get(1) + "");
                        Glide.with(mContext).load(filmList.get(result.get(0))).apply(new RequestOptions()
                                .placeholder(R.drawable.face).diskCacheStrategy(DiskCacheStrategy.RESOURCE).centerCrop()).transition(DrawableTransitionOptions.withCrossFade(1000))
                                .into(headImg1);

                        Glide.with(mContext).load(filmList.get(result.get(1))).apply(new RequestOptions()
                                .placeholder(R.drawable.face).diskCacheStrategy(DiskCacheStrategy.RESOURCE).centerCrop()).transition(DrawableTransitionOptions.withCrossFade(1000))
                                .into(headImg2);
                        if (timer2 == null) {
                            timer2 = new Timer();
                            timer2.schedule(task2, 100, 100);
                        }
                        progress = 60;
                        curIndex = result.get(1);
                    } else if (left <= 10 && left > 1) {
                        tvNum1.setVisibility(View.VISIBLE);
                        tvNum1.setText(result.get(0) + "");
                        tvNum2.setVisibility(View.VISIBLE);
                        tvNum2.setText(result.get(1) + "");
                        tvNum3.setVisibility(View.VISIBLE);
                        tvNum3.setText(result.get(2) + "");
                        Glide.with(mContext).load(filmList.get(result.get(0))).apply(new RequestOptions()
                                .placeholder(R.drawable.face).diskCacheStrategy(DiskCacheStrategy.RESOURCE).centerCrop()).transition(DrawableTransitionOptions.withCrossFade(1000))
                                .into(headImg1);

                        Glide.with(mContext).load(filmList.get(result.get(1))).apply(new RequestOptions()
                                .placeholder(R.drawable.face).diskCacheStrategy(DiskCacheStrategy.RESOURCE).centerCrop()).transition(DrawableTransitionOptions.withCrossFade(1000))
                                .into(headImg2);

                        Glide.with(mContext).load(filmList.get(result.get(2))).apply(new RequestOptions()
                                .placeholder(R.drawable.face).diskCacheStrategy(DiskCacheStrategy.RESOURCE).centerCrop()).transition(DrawableTransitionOptions.withCrossFade(1000))
                                .into(headImg3);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        webView.loadUrl(FeihuZhiboApplication.getApplication().mDataManager.getSysConfig().getLyzbExplainUrl());
        landHuDongView = new LyLandHuDongView(mContext, blankCnt);
        frmHudong.addView(landHuDongView.getView());
        landJcXiaZhuView = new LyLandXiaZhuView(mContext, 1, 0);
        frmJincai.addView(landJcXiaZhuView.getView());
        landJcXiaZhuView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(mListener!=null){
                    mListener.JoinSuccess();
                }
            }
        });
        landHuDongView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(mListener!=null){
                    mListener.JoinSuccess();
                }
            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (timer != null) {
            timer.cancel();
        }
        if (timer1 != null) {
            timer1.cancel();
        }
        if (timer2 != null) {
            timer2.cancel();
        }
        RxBus.get().unRegister(this);
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }

        long timeDur = System.currentTimeMillis() - timeDuration;
        Map<String, String> map_value = new HashMap<String, String>();
        map_value.put("type", "乐摇主播");
        MobclickAgent.onEventValue(mContext, "10071", map_value, (int) (timeDur / 1000));
    }


    @OnClick({R.id.frm_top, R.id.btn_hudong, R.id.guess_history, R.id.award_history, R.id.play, R.id.game_help, R.id.img_jincai})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.frm_top:
                if (firstView.getVisibility() == View.VISIBLE) {
                    dismiss();
                }
                if (kaijiluView.getVisibility() == View.VISIBLE) {
                    dismiss();
                }
                if (jincaiView.getVisibility() == View.VISIBLE) {
                    dismiss();
                }
                if (frmHudong.getVisibility() == View.VISIBLE) {
                    frmHudong.setVisibility(View.GONE);
                    firstView.setVisibility(View.VISIBLE);
                }
                if (frmJincai.getVisibility() == View.VISIBLE) {
                    frmJincai.setVisibility(View.GONE);
                    landJcXiaZhuView.clearNum();
                    firstView.setVisibility(View.VISIBLE);
                }
                if(webView.getVisibility()==View.VISIBLE){
                    webView.setVisibility(View.GONE);
                    if(currentViewpager==0) {
                        firstView.setVisibility(View.VISIBLE);
                    }else if(currentViewpager==1){
                        jincaiView.setVisibility(View.VISIBLE);
                    }else if(currentViewpager==2){
                        kaijiluView.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case R.id.btn_hudong:
                if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                    if (mListener != null) {
                        mListener.showLogin();
                    }
                }else{
                    if (SharePreferenceUtil.getSessionInt(FeihuZhiboApplication.getApplication(),"PREF_KEY_LEVEL") < 6) {
                        FHUtils.showToast("六级开启玩法");
                        return;
                    }
                    kaijiluView.setVisibility(View.GONE);
                    firstView.setVisibility(View.GONE);
                    jincaiView.setVisibility(View.GONE);
                    frmHudong.setVisibility(View.VISIBLE);
                    frmJincai.setVisibility(View.GONE);
                    landJcXiaZhuView.clearNum();
                    landHuDongView.onFresh();
                }

                break;
            case R.id.guess_history:
                if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                    if (mListener != null) {
                        mListener.showLogin();
                    }
                } else {
                    currentViewpager=1;
                    guessHistory.setSelected(true);
                    play.setSelected(false);
                    awardHistory.setSelected(false);
                    kaijiluView.setVisibility(View.GONE);
                    jincaiView.setVisibility(View.VISIBLE);
                    firstView.setVisibility(View.GONE);
                    frmHudong.setVisibility(View.GONE);
                    frmJincai.setVisibility(View.GONE);
                    landJcXiaZhuView.clearNum();
                    webView.setVisibility(View.GONE);
                    initJincaiRec();
                }
                MobclickAgent.onEvent(getContext(), "10057");
                break;
            case R.id.award_history:
                currentViewpager=2;
                MobclickAgent.onEvent(getContext(), "10058");
                initRec();
                kaijiluView.setVisibility(View.VISIBLE);
                jincaiView.setVisibility(View.GONE);
                firstView.setVisibility(View.GONE);
                frmHudong.setVisibility(View.GONE);
                frmJincai.setVisibility(View.GONE);
                landJcXiaZhuView.clearNum();
                webView.setVisibility(View.GONE);
                guessHistory.setSelected(false);
                play.setSelected(false);
                awardHistory.setSelected(true);
                break;
            case R.id.play:
                currentViewpager=0;
                guessHistory.setSelected(false);
                play.setSelected(true);
                awardHistory.setSelected(false);
                firstView.setVisibility(View.VISIBLE);
                kaijiluView.setVisibility(View.GONE);
                jincaiView.setVisibility(View.GONE);
                frmHudong.setVisibility(View.GONE);
                frmJincai.setVisibility(View.GONE);
                landJcXiaZhuView.clearNum();
                webView.setVisibility(View.GONE);
                break;
            case R.id.game_help:
                webView.setVisibility(View.VISIBLE);
                firstView.setVisibility(View.GONE);
                kaijiluView.setVisibility(View.GONE);
                jincaiView.setVisibility(View.GONE);
                frmHudong.setVisibility(View.GONE);
                frmJincai.setVisibility(View.GONE);
                landJcXiaZhuView.clearNum();
                break;
            case R.id.img_jincai:
                MobclickAgent.onEvent(mContext, "10038");
                if (mListener != null) {
                    mListener.showJincaiDlg();
                }
                dismiss();
                break;
        }
    }

    @RxBusSubscribe(code = RxBusCode.RX_BUS_NOTICE_CODE_NOTIFYWANFAHISTORY,threadMode = ThreadMode.MAIN)
    public void onReceiveNotify(){
        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .doGetGameBetHistoryApiCall(new GetGameBetHistoryRequest("lyzb"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetGameBetHistoryResponce>() {
                    @Override
                    public void accept(@NonNull GetGameBetHistoryResponce responce) throws Exception {
                        if (responce.getCode() == 0) {
                            int hubi = responce.getGetGameBetHistoryData().getHB();
                            int yhubi = responce.getGetGameBetHistoryData().getYHB();
                            if(tvHubi!=null&&yinCoin!=null&&jcHistoryAdapter!=null) {
                                tvHubi.setText(String.valueOf(hubi));
                                yinCoin.setText(String.valueOf(yhubi));
                                jcHistoryAdapter.setDatas(responce.getGetGameBetHistoryData().getBetLogsList());
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


    private void initRec() {
        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .doGetGameSettleHistoryApiCall(new GetGameSettleHistoryRequest("lyzb"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetGameSettleHistoryResponce>() {
                    @Override
                    public void accept(@NonNull GetGameSettleHistoryResponce responce) throws Exception {
                        if (responce.getCode() == 0) {
                            List<String> datas = new ArrayList<String>();
                            for (int i = 0; i < responce.getDatas().size(); i++) {
                                datas.add(responce.getDatas().get(i).toString());
                            }
                            kjhistoryAdapter.setDatas(datas);
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

    private void initJincaiRec() {
        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .doGetGameBetHistoryApiCall(new GetGameBetHistoryRequest("lyzb"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetGameBetHistoryResponce>() {
                    @Override
                    public void accept(@NonNull GetGameBetHistoryResponce responce) throws Exception {
                        if (responce.getCode() == 0) {
                            int hubi = responce.getGetGameBetHistoryData().getHB();
                            int yhubi = responce.getGetGameBetHistoryData().getYHB();
                            tvHubi.setText(String.valueOf(hubi));
                            yinCoin.setText(String.valueOf(yhubi));
                            jcHistoryAdapter.setDatas(responce.getGetGameBetHistoryData().getBetLogsList());
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


    class VPagerAdapter extends PagerAdapter {
        private List<View> mViewList;
        // 标题数组
        String[] titles = {"礼物", "包裹"};

        public VPagerAdapter(List<View> mViewList) {
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

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };
    TimerTask task1 = new TimerTask() {
        @Override
        public void run() {
            Message message = new Message();
            message.what = 4;
            handler.sendMessage(message);
        }
    };
    TimerTask task2 = new TimerTask() {
        @Override
        public void run() {
            Message message = new Message();
            message.what = 5;
            handler.sendMessage(message);
            curIndex = curIndex % 10; // 图片区间[0,count_drawable)
            curIndex++;
        }
    };
}
