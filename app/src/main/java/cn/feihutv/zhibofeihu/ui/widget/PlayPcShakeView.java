package cn.feihutv.zhibofeihu.ui.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.umeng.analytics.MobclickAgent;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameBetRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameBetResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameStatusResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.GameResultPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.GameStartRoundPush;
import cn.feihutv.zhibofeihu.rxbus.RxBus;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.rxbus.RxBusSubscribe;
import cn.feihutv.zhibofeihu.rxbus.ThreadMode;
import cn.feihutv.zhibofeihu.ui.live.OnItemClickListener;
import cn.feihutv.zhibofeihu.ui.live.adapter.ShakeListAdapter;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.glideutil.GlideApp;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by huanghao on 2017/7/24.
 */

public class PlayPcShakeView extends LinearLayout {

    @BindView(R.id.guess_history)
    Button guessHistory;
    @BindView(R.id.award_history)
    Button awardHistory;
    @BindView(R.id.play)
    Button play;
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
    @BindView(R.id.tv_time_left)
    TextView tvTimeLeft;
    @BindView(R.id.lin_clock)
    LinearLayout linClock;
    @BindView(R.id.end_bg)
    ImageView endBg;
    @BindView(R.id.nick_name)
    TextView nickName;
    @BindView(R.id.go_room)
    Button goRoom;
    @BindView(R.id.lin_begin)
    LinearLayout linBegin;
    @BindView(R.id.list_horizontal)
    RecyclerView listHorizontal;
    private List<View> mViewList = new ArrayList<>();
    private String json;
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
    private int gameRound = 1;
    private int tCount = 0;
    private int issueRound;
    private String nickname;
    private String roomId;
    private int sumRound = 10;
    private Context mContext;
    private View rootView;
    private boolean shouldTimerOn = false;
    private CarouselLayoutManager carouselLayoutManager;

    public PlayPcShakeView(Context context) {
        this(context, null, 0);
    }

    public PlayPcShakeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayPcShakeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        timer = new Timer();
        timer1 = new Timer();
        initView();
    }

    public void setJson(String json) {

    }

    public void setshouldTimerOnFalse() {
        shouldTimerOn = false;
    }

    public void onFresh(GetGameStatusResponce.GameStatusData data) {
        shouldTimerOn = true;
        gameRound = data.getRound();
        sumRound = data.getTotalRound();
        if (gameRound == 0) {
            gameRound = 1;
        }
        int time = data.getTime();
        int severTime = data.getServerTime();
        left = sumTime - (severTime - time);
        gameProcess.setText("游戏已进行" + gameRound + "/" + sumRound);
        playerCnt = data.getPlayerCnt();
        blankCnt = data.getBankerCnt();
        nickname = data.getNickname();
        roomId = data.getRoomId();
        if (!TextUtils.isEmpty(nickname)) {
            nickName.setText(nickname);
        } else {
            linBegin.setVisibility(View.GONE);
        }

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

        if (filmList.size() > 0) {
            filmList.clear();
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

    private void initView() {
        rootView = View.inflate(mContext, R.layout.ly_pc_dialog_view, this);
        ButterKnife.bind(this, rootView);
        RxBus.get().register(this);
        headImg1.setType(3);
        headImg1.setRoundRadius(100);
        headImg3.setType(3);
        headImg3.setRoundRadius(100);
        headImg2.setType(3);
        headImg2.setRoundRadius(100);
        playGame.setBackgroundResource(R.drawable.paly_anim);
        adapter = new ShakeListAdapter(mContext, filmList);
        carouselLayoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, false);
        initRecyclerView(listHorizontal, carouselLayoutManager, adapter);
        carouselLayoutManager.scrollToPosition(5);
        curIndex = 5;
        timer.schedule(task, 1000, 1000);
        timer1.schedule(task1, 1000, 5000);
        endBg.setImageResource(R.drawable.play_game_bg);
        view1 = new JincaiView(mContext, 3, 0, 0, 0, 0, true,false);
        view2 = new JincaiView(mContext, 4, 0, 0, 0, 0, true,false);
        view3 = new JincaiView(mContext, 5, 0, 0, 0, 0, true,false);
        mViewList.add(view1.getView());
        mViewList.add(view2.getView());
        mViewList.add(view3.getView());
        viewPager.setAdapter(new VPagerAdapter(mViewList));
        viewPager.setCurrentItem(0);

        view1.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (mListener != null) {
                    if (position == 1) {
                        mListener.jinCai(1, playerCnt);
                    } else {
                        mListener.jinCai(2, playerCnt);
                    }

                }
            }
        });
        view2.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (mListener != null) {
                    if (position == 1) {
                        mListener.jinCai(3, playerCnt);
                    } else {
                        mListener.jinCai(4, playerCnt);
                    }

                }
            }
        });
        view3.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (mListener != null) {
                    if (position == 1) {
                        mListener.jinCai(5, playerCnt);
                    } else {
                        mListener.jinCai(6, playerCnt);
                    }

                }
            }
        });

        goRoom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.goRoom(roomId);
                }
            }
        });


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
    }

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
        args.putInt("issueRound", issueRound);
        args.putString("gameName", pushData.getGameName());
        newJoiner.obj = args;
        handler.sendMessage(newJoiner);
    }


    public interface JinCaiInterface {
        void gameHelp();

        void jinCai(int pos, int playCnt);

        void huDong(int blanlCnt);

        void awardHistory();

        void jcHistory();

        void showResult(int issueRound);

        void showJincaiDlg();

        void goRoom(String roomId);
    }

    private JinCaiInterface mListener;

    public void setJincaiListener(JinCaiInterface listener) {
        mListener = listener;
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
                        gameCountdown.setText("距离下局" + "0\'" + "00\"");
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

                    if (left == 60) {
//                        linClock.setVisibility(View.VISIBLE);
                    } else if (left < 60 && left >= 0) {
                        tvTimeLeft.setText(left + "");
                    } else {
                        linClock.setVisibility(View.GONE);
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
                        play.setEnabled(false);
                        play.setBackgroundResource(R.drawable.gray_shape_100);
                    } else {
                        play.setEnabled(true);
                        play.setBackgroundResource(R.drawable.yellow_bg);
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
                        linClock.setVisibility(View.GONE);
                        gameProcess.setText("游戏已进行" + gameRound + "/" + sumRound);
                        left = sumTime;
                        gameCountdown.setText("竞猜倒计时" + "3\'" + "00\"");
                        tvNum1.setVisibility(View.INVISIBLE);
                        tvNum2.setVisibility(View.INVISIBLE);
                        tvNum3.setVisibility(View.INVISIBLE);
                        headImg1.setVisibility(View.INVISIBLE);
                        headImg1.setImageDrawable(null);
                        headImg2.setVisibility(View.INVISIBLE);
                        headImg2.setImageDrawable(null);
                        headImg3.setVisibility(View.INVISIBLE);
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
                            Log.e("gameSettleRes", result.get(0) + "/" + result.get(1) + "/" + result.get(2));
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
                    if (!shouldTimerOn) {
                        return;
                    }
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
                                    } else {
                                        AppLogger.e(getGameBetResponce.getErrMsg());
                                    }
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(@NonNull Throwable throwable) throws Exception {

                                }
                            }));
                    break;
                case 5:
                    try {
                        progress++;
                        if (progress <= 20 || (progress > 30 && progress <= 50) || (progress > 60 && progress <= 80)) {
//                            fancyCoverFlow.setSelection(curIndex);
                            if (carouselLayoutManager != null)
                                carouselLayoutManager.scrollToPosition(curIndex);
                        }
                        if (progress == 20) {
//                            fancyCoverFlow.setSelection(result.get(0));
                            if (carouselLayoutManager != null)
                                carouselLayoutManager.scrollToPosition(result.get(0));
                            if (filmList.size() > 0 && result.size() > 0) {
                                GlideApp.loadImg(mContext, filmList.get(result.get(0)), R.drawable.face, 1000, headImg1);
                            }
                            anim(result.get(0));
                        } else if (progress == 50) {

//                            fancyCoverFlow.setSelection(result.get(1));
                            if (carouselLayoutManager != null)
                                carouselLayoutManager.scrollToPosition(result.get(1));
                            if (filmList.size() > 0 && result.size() > 0) {
                                GlideApp.loadImg(mContext, filmList.get(result.get(1)), R.drawable.face, 1000, headImg2);
                            }
                            anim1(result.get(1));
                        } else if (progress == 80) {

//                            fancyCoverFlow.setSelection(result.get(2));
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


    @OnClick({R.id.guess_history, R.id.award_history, R.id.play, R.id.game_help, R.id.img_jincai})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.guess_history:
                if (mListener != null) {
                    mListener.jcHistory();
                }
                break;
            case R.id.award_history:
                if (mListener != null) {
                    mListener.awardHistory();
                }
                break;
            case R.id.play:
                if (mListener != null) {
                    mListener.huDong(blankCnt);
                }
                break;
            case R.id.game_help:
                if (mListener != null) {
                    mListener.gameHelp();
                }
                break;
            case R.id.img_jincai:
                MobclickAgent.onEvent(mContext, "10038");
                if (mListener != null) {
                    mListener.showJincaiDlg();
                }
//                setVisibility(GONE);
                break;
        }
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


    public void onDestory() {
        if (timer != null) {
            timer.cancel();
        }
        if (timer1 != null) {
            timer1.cancel();
        }
        if (timer2 != null) {
            timer2.cancel();
        }
        if (task1 != null) {
            task.cancel();
        }
        if (task1 != null) {
            task.cancel();
        }
        if (task2 != null) {
            task2.cancel();
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        RxBus.get().unRegister(this);
    }
}
