package cn.feihutv.zhibofeihu.ui.widget;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import cn.feihutv.zhibofeihu.ui.widget.flashview.FlashDataParser;
import cn.feihutv.zhibofeihu.ui.widget.flashview.FlashView;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by huanghao on 2017/7/24.
 */

public class PlayPcJincaiView extends LinearLayout  {
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
    @BindView(R.id.play_one)
    ImageView playOne;
    @BindView(R.id.play_two)
    ImageView playTwo;
    @BindView(R.id.play_three)
    ImageView playThree;
    @BindView(R.id.game_help)
    ImageView gameHelp;
    @BindView(R.id.view_pager)
    VerticalViewPager viewPager;
    @BindView(R.id.img_dot_1)
    ImageView imgDot1;
    @BindView(R.id.img_dot_2)
    ImageView imgDot2;
    @BindView(R.id.flashview_one)
    FlashView flashviewOne;
    @BindView(R.id.flashview_two)
    FlashView flashviewTwo;
    @BindView(R.id.flashview_three)
    FlashView flashviewThree;
    @BindView(R.id.lin_time)
    LinearLayout linTime;
    @BindView(R.id.tv_time_left)
    TextView tvTimeLeft;
    @BindView(R.id.lin_clock)
    LinearLayout linClock;
    @BindView(R.id.end_bg)
    ImageView endBg;
    @BindView(R.id.img_shake)
    ImageView imgShake;
    @BindView(R.id.nick_name)
    TextView nickName;
    @BindView(R.id.go_room)
    Button goRoom;
    @BindView(R.id.lin_begin)
    LinearLayout linBegin;
    @BindView(R.id.linearLayout_bg)
    LinearLayout linearLayoutBg;
    private List<View> mViewList = new ArrayList<>();
    private Context mContext;
    private int style = 2;
    private String json;
    private Timer timer;
    private Timer timer1;
    private int progress = 0;
    private int left = 0;
    private List<Integer> result = new ArrayList<Integer>();
    private boolean pos1Good = false;
    private boolean pos2Good = false;
    private boolean pos3Good = false;
    private JincaiView view1;
    private JincaiView view2;
    private int sumTime = 220;
    private int playerCnt;//今日已使用竞猜次数
    private int blankCnt;
    private int imgPos = 0;
    private int gameRound = 1;
    private int tCount = 0;
    private int issueRound;
    private String nickname;
    private String roomId;
    private int sumRound = 10;
    private View rootView;
    private boolean shouldTimerOn = false;
    private boolean havaKaiJiang = false;


    //游戏一局开始
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_GAME_START_ROUND,threadMode = ThreadMode.MAIN)
    public void onReceiveGameStartRoundPush(GameStartRoundPush pushData){
        havaKaiJiang = false;
        Message newJoiner = handler.obtainMessage(2);
        Bundle args = new Bundle();
        args.putInt("round", pushData.getRound());
        args.putString("gameName", pushData.getGameName());
        args.putString("nickname", pushData.getNickname());
        args.putString("roomId", pushData.getRoomId());
        args.putInt("openStyle", pushData.getOpenStyle());
        newJoiner.obj = args;
        handler.sendMessage(newJoiner);
    }

    //游戏结果揭晓
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_GAME_RESULT,threadMode = ThreadMode.MAIN)
    public void onReceiveGameResultPush(GameResultPush pushData){
        havaKaiJiang = true;
        Message newJoiner = handler.obtainMessage(3);
        Bundle args = new Bundle();
        args.putString("msg", pushData.getResult().toString());
        args.putInt("issueRound", pushData.getIssueRound());
        args.putInt("openStyle", pushData.getOpenStyle());
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

        void showshakeDialog();

        void goRoom(String id);
    }

    private JinCaiInterface mListener;

    public void setJincaiListener(JinCaiInterface listener) {
        mListener = listener;
    }

    public PlayPcJincaiView(Context context) {
        this(context, null, 0);
    }

    public PlayPcJincaiView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayPcJincaiView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        timer = new Timer();
        timer1 = new Timer();
        initView();
    }

    public void setshouldTimerOnFalse() {
        shouldTimerOn = false;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (left <= 0) {
                        if (gameRound == 6) {
                            tCount++;
                        }
                        if (tCount > 60) {
                            //dismiss();
                        }
                    }

                    left--;

                    if (left <= 40) {
                        if (view1 != null) {
                            view1.setButtonEnable(false);
                        }
                        if (view2 != null) {
                            view2.setButtonEnable(false);
                        }
                        play.setEnabled(false);
                        play.setBackgroundResource(R.drawable.gray_shape_100);
                    } else {
//                        if (view1 != null) {
//                            view1.setButtonEnable(true);
//                        }
//                        if (view2 != null) {
//                            view2.setButtonEnable(true);
//                        }
                        play.setEnabled(true);
                        play.setBackgroundResource(R.drawable.yellow_bg);
                    }

                    if (left == 60) {
//                        linClock.setVisibility(View.VISIBLE);
                    } else if (left < 60 && left >= 0) {
                        tvTimeLeft.setText(left + "");
                    } else {
                        linClock.setVisibility(View.GONE);
                    }

                    if (left <= 0) {
                        gameCountdown.setText("距离下局" + "0\'" + "0\"");
                        return;
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
                    }

                    Log.e("time_left", left + "");
                    break;
                case 2:
                    Bundle bundle = (Bundle) msg.obj;
                    gameRound = bundle.getInt("round");
                    String gameName = bundle.getString("gameName");
                    if (gameName.equals("yxjc")) {
                        int openStyle = bundle.getInt("openStyle");
                        if (openStyle != style) {
                            style = openStyle;
                        }
                        onFreshView(style);
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
                        gameCountdown.setText("竞猜倒计时" + "3\'" + "40\"");
                        flashviewOne.setVisibility(View.GONE);
                        flashviewTwo.setVisibility(View.GONE);
                        flashviewThree.setVisibility(View.GONE);
                        playOne.setVisibility(View.VISIBLE);
                        playTwo.setVisibility(View.VISIBLE);
                        playThree.setVisibility(View.VISIBLE);
                        pos1Good = false;
                        pos3Good = false;
                        pos2Good = false;
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
                    if (gameName1.equals("yxjc")) {
                        style = bundle1.getInt("openStyle");
                        onFreshView(style);
                        Log.e("pcJinCaiView", style + "");
                        try {
                            JSONArray jsonArray = new JSONArray(json);
                            result.add(jsonArray.getInt(0));
                            result.add(jsonArray.getInt(1));
                            result.add(jsonArray.getInt(2));
                            Log.e("gameSettleRes", result.get(0) + "/" + result.get(1) + "/" + result.get(2));
                            giftAnim1();
//                            giftAnim2();
//                            giftAnim3();

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
                            .doGetGameBetApiCall(new GetGameBetRequest("yxjc"))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<GetGameBetResponce>() {
                                @Override
                                public void accept(@NonNull GetGameBetResponce getGameBetResponce) throws Exception {
                                    if(getGameBetResponce.getCode()==0){
                                        String sum1 = "0";
                                        String sum2 = "0";
                                        String sum3 = "0";
                                        String sum4 = "0";
                                        String bet1 = "0";
                                        String bet2 = "0";
                                        String bet3 = "0";
                                        String bet4 = "0";
                                        if (getGameBetResponce.getGetGameBetData().getBankerBet().getBet1()!=null) {
                                            sum1 = getGameBetResponce.getGetGameBetData().getBankerBet().getBet1();
                                        }
                                        if (getGameBetResponce.getGetGameBetData().getBankerBet().getBet2()!=null) {
                                            sum2 = getGameBetResponce.getGetGameBetData().getBankerBet().getBet2();
                                        }
                                        if (getGameBetResponce.getGetGameBetData().getBankerBet().getBet3()!=null) {
                                            sum3 = getGameBetResponce.getGetGameBetData().getBankerBet().getBet3();
                                        }
                                        if (getGameBetResponce.getGetGameBetData().getBankerBet().getBet4()!=null) {
                                            sum4 = getGameBetResponce.getGetGameBetData().getBankerBet().getBet4();
                                        }
                                        if (getGameBetResponce.getGetGameBetData().getPlayerBet().getBet1()!=null) {
                                            bet1 =  getGameBetResponce.getGetGameBetData().getPlayerBet().getBet1();
                                        }
                                        if (getGameBetResponce.getGetGameBetData().getPlayerBet().getBet2()!=null) {
                                            bet2 = getGameBetResponce.getGetGameBetData().getPlayerBet().getBet2();
                                        }
                                        if (getGameBetResponce.getGetGameBetData().getPlayerBet().getBet3()!=null) {
                                            bet3 = getGameBetResponce.getGetGameBetData().getPlayerBet().getBet3();
                                        }
                                        if (getGameBetResponce.getGetGameBetData().getPlayerBet().getBet4()!=null) {
                                            bet4 = getGameBetResponce.getGetGameBetData().getPlayerBet().getBet4();
                                        }
                                        if (view1 != null) {
                                            view1.updateData(Integer.valueOf(bet3), Integer.valueOf(bet4), Integer.valueOf(sum3), Integer.valueOf(sum4));
                                        }
                                        if (view2 != null) {
                                            view2.updateData(Integer.valueOf(bet1), Integer.valueOf(bet2), Integer.valueOf(sum1), Integer.valueOf(sum2));
                                        }
                                        if (left > 40) {
                                            if (Integer.valueOf(sum3) > 0) {
                                                view1.setUpButtonEnable(true);
                                            } else {
                                                view1.setUpButtonEnable(false);
                                            }
                                            if (Integer.valueOf(sum4) > 0) {
                                                view1.setDownButtonEnable(true);
                                            } else {
                                                view1.setDownButtonEnable(false);
                                            }
                                            if (Integer.valueOf(sum1) > 0) {
                                                view2.setUpButtonEnable(true);
                                            } else {
                                                view2.setUpButtonEnable(false);
                                            }
                                            if (Integer.valueOf(sum2) > 0) {
                                                view2.setDownButtonEnable(true);
                                            } else {
                                                view2.setDownButtonEnable(false);
                                            }
                                        }
                                    }else{
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
            }
        }
    };


    private void onFreshView(int openStyle) {
        shouldTimerOn = true;
        if (openStyle == 3) {
            linearLayoutBg.setBackgroundResource(R.drawable.bg_game_shake);
            playOne.setImageResource(R.drawable.pic_baoboa);
            playTwo.setImageResource(R.drawable.pic_baoboa);
            playThree.setImageResource(R.drawable.pic_baoboa);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) playOne.getLayoutParams();
            layoutParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
            playOne.setLayoutParams(layoutParams);
            FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) playTwo.getLayoutParams();
            layoutParams1.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
            playTwo.setLayoutParams(layoutParams1);
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) playThree.getLayoutParams();
            layoutParams2.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
            playThree.setLayoutParams(layoutParams2);
        } else if (openStyle == 2) {
            linearLayoutBg.setBackgroundResource(R.drawable.bg_game_prairie);
            playOne.setImageResource(R.drawable.pci_game_cc);
            playTwo.setImageResource(R.drawable.pci_game_cc);
            playThree.setImageResource(R.drawable.pci_game_cc);

            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) playOne.getLayoutParams();
            layoutParams.gravity = Gravity.CENTER;
            playOne.setLayoutParams(layoutParams);
            FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) playTwo.getLayoutParams();
            layoutParams1.gravity = Gravity.CENTER;
            playTwo.setLayoutParams(layoutParams1);
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) playThree.getLayoutParams();
            layoutParams2.gravity = Gravity.CENTER;
            playThree.setLayoutParams(layoutParams2);
        } else {
            linearLayoutBg.setBackgroundResource(R.drawable.bg_game_stage);
            playOne.setImageResource(R.drawable.pic_tiger_egg);
            playTwo.setImageResource(R.drawable.pic_tiger_egg);
            playThree.setImageResource(R.drawable.pic_tiger_egg);

            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) playOne.getLayoutParams();
            layoutParams.gravity = Gravity.CENTER;
            playOne.setLayoutParams(layoutParams);
            FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) playTwo.getLayoutParams();
            layoutParams1.gravity = Gravity.CENTER;
            playTwo.setLayoutParams(layoutParams1);
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) playThree.getLayoutParams();
            layoutParams2.gravity = Gravity.CENTER;
            playThree.setLayoutParams(layoutParams2);
        }
    }


    public void onFresh(int openStyle,GetGameStatusResponce.GameStatusData data) {
        shouldTimerOn = true;
        style = openStyle;
        if (openStyle == 3) {
            linearLayoutBg.setBackgroundResource(R.drawable.bg_game_shake);
            if (!havaKaiJiang) {
                playOne.setImageResource(R.drawable.pic_baoboa);
                playTwo.setImageResource(R.drawable.pic_baoboa);
                playThree.setImageResource(R.drawable.pic_baoboa);
            }
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) playOne.getLayoutParams();
            layoutParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
            playOne.setLayoutParams(layoutParams);
            FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) playTwo.getLayoutParams();
            layoutParams1.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
            playTwo.setLayoutParams(layoutParams1);
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) playThree.getLayoutParams();
            layoutParams2.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
            playThree.setLayoutParams(layoutParams2);
        } else if (openStyle == 2) {
            linearLayoutBg.setBackgroundResource(R.drawable.bg_game_prairie);
            if (!havaKaiJiang) {
                playOne.setImageResource(R.drawable.pci_game_cc);
                playTwo.setImageResource(R.drawable.pci_game_cc);
                playThree.setImageResource(R.drawable.pci_game_cc);
            }
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) playOne.getLayoutParams();
            layoutParams.gravity = Gravity.CENTER;
            playOne.setLayoutParams(layoutParams);
            FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) playTwo.getLayoutParams();
            layoutParams1.gravity = Gravity.CENTER;
            playTwo.setLayoutParams(layoutParams1);
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) playThree.getLayoutParams();
            layoutParams2.gravity = Gravity.CENTER;
            playThree.setLayoutParams(layoutParams2);
        } else {
            linearLayoutBg.setBackgroundResource(R.drawable.bg_game_stage);
            if (!havaKaiJiang) {
                playOne.setImageResource(R.drawable.pic_tiger_egg);
                playTwo.setImageResource(R.drawable.pic_tiger_egg);
                playThree.setImageResource(R.drawable.pic_tiger_egg);
            }
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) playOne.getLayoutParams();
            layoutParams.gravity = Gravity.CENTER;
            playOne.setLayoutParams(layoutParams);
            FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) playTwo.getLayoutParams();
            layoutParams1.gravity = Gravity.CENTER;
            playTwo.setLayoutParams(layoutParams1);
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) playThree.getLayoutParams();
            layoutParams2.gravity = Gravity.CENTER;
            playThree.setLayoutParams(layoutParams2);
        }
        Log.e("openStyle", openStyle + "");

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
                gameCountdown.setText("竞猜倒计时" + "3\'" + "00\"");
            }
            if (endBg != null && gameRound == 6 && left < 60) {
                endBg.setVisibility(View.GONE);
            }

        String sum1 = "0";
        String sum2 = "0";
        String sum3 = "0";
        String sum4 = "0";
        String bet1 = "0";
        String bet2 = "0";
        String bet3 = "0";
        String bet4 = "0";
        if (data.getBankerBet().getBet1()!=null) {
            sum1 = data.getBankerBet().getBet1();
        }
        if (data.getBankerBet().getBet2()!=null) {
            sum2 = data.getBankerBet().getBet2();
        }
        if (data.getBankerBet().getBet3()!=null) {
            sum3 = data.getBankerBet().getBet3();
        }
        if (data.getBankerBet().getBet4()!=null) {
            sum4 = data.getBankerBet().getBet4();
        }
        if (data.getPlayerBet().getBet1()!=null) {
            bet1 =  data.getPlayerBet().getBet1();
        }
        if (data.getPlayerBet().getBet2()!=null) {
            bet2 = data.getPlayerBet().getBet2();
        }
        if (data.getPlayerBet().getBet3()!=null) {
            bet3 = data.getPlayerBet().getBet3();
        }
        if (data.getPlayerBet().getBet4()!=null) {
            bet4 = data.getPlayerBet().getBet4();
        }

            view1.updateData(Integer.valueOf(bet3), Integer.valueOf(bet4), Integer.valueOf(sum3), Integer.valueOf(sum4));
            view2.updateData(Integer.valueOf(bet1), Integer.valueOf(bet2), Integer.valueOf(sum1), Integer.valueOf(sum2));
            if (Integer.valueOf(sum3) > 0) {
                view1.setUpButtonEnable(true);
            } else {
                view1.setUpButtonEnable(false);
            }
            if (Integer.valueOf(sum4) > 0) {
                view1.setDownButtonEnable(true);
            } else {
                view1.setDownButtonEnable(false);
            }
            if (Integer.valueOf(sum1) > 0) {
                view2.setUpButtonEnable(true);
            } else {
                view2.setUpButtonEnable(false);
            }
            if (Integer.valueOf(sum2) > 0) {
                view2.setDownButtonEnable(true);
            } else {
                view2.setDownButtonEnable(false);
            }
    }

    private void initView() {
//        if (style == 3) {
//            rootView = View.inflate(mContext, R.layout.jincai_pc_dialog_view, this);
//
//        } else if (style == 2) {
//            rootView = View.inflate(mContext, R.layout.jincai_pc_dialog_grass_view, this);
//
//        } else if (style == 1) {
//            rootView = View.inflate(mContext, R.layout.jincai_pc_dialog_cipin_view, this);
//
//        }
        rootView = View.inflate(mContext, R.layout.jc_pc_dialog_view, this);
        ButterKnife.bind(this, rootView);
        RxBus.get().register(this);
        timer.schedule(task, 1000, 1000);
        timer1.schedule(task1, 1000, 5000);
        view1 = new JincaiView(mContext, 1, 0, 0, 0, 0, true,false);
        view2 = new JincaiView(mContext, 2, 0, 0, 0, 0, true,false);
        mViewList.add(view2.getView());
        mViewList.add(view1.getView());
        viewPager.setAdapter(new VPagerAdapter(mViewList));
        viewPager.setCurrentItem(0);
        endBg.setImageResource(R.drawable.play_game_bg);
        view1.setOnItemClickListener(new OnItemClickListener() {
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
        view2.setOnItemClickListener(new OnItemClickListener() {
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
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    imgDot1.setSelected(true);
                    imgDot2.setSelected(false);
                } else {
                    imgDot2.setSelected(true);
                    imgDot1.setSelected(false);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void onDestory() {
        if (timer != null) {
            timer.cancel();
        }
        if (timer1 != null) {
            timer1.cancel();
        }
        if (task != null) {
            task.cancel();
        }
        if (task1 != null) {
            task1.cancel();
        }
        if(handler!=null){
            handler.removeCallbacksAndMessages(null);
            handler=null;
        }
        RxBus.get().unRegister(this);
    }

    private void giftAnim1() {
        pos1Good = true;
        flashviewOne.setVisibility(View.VISIBLE);

        playOne.setVisibility(View.GONE);
        if (style == 3) {
            if (result.get(0) == 0) {
                //母
                flashviewOne.reload("prize04", "flashAnims");
                flashviewOne.play("prize04", FlashDataParser.FlashLoopTimeOnce);

            } else {
                //共
                flashviewOne.reload("prize03", "flashAnims");
                flashviewOne.play("prize03", FlashDataParser.FlashLoopTimeOnce);
            }
        } else if (style == 2) {
            if (result.get(0) == 0) {
                //母
                flashviewOne.reload("prize06", "flashAnims");
                flashviewOne.play("prize06", FlashDataParser.FlashLoopTimeOnce);
            } else {
                //共
                flashviewOne.reload("prize02", "flashAnims");
                flashviewOne.play("prize02", FlashDataParser.FlashLoopTimeOnce);
            }
        } else if (style == 1) {
            if (result.get(0) == 0) {
                //母
                flashviewOne.reload("prize05", "flashAnims");
                flashviewOne.play("prize05", FlashDataParser.FlashLoopTimeOnce);
            } else {
                //共
                flashviewOne.reload("prize01", "flashAnims");
                flashviewOne.play("prize01", FlashDataParser.FlashLoopTimeOnce);
            }
        }
        flashviewOne.setEventCallback(new FlashDataParser.IFlashViewEventCallback() {
            @Override
            public void onEvent(FlashDataParser.FlashViewEvent e, FlashDataParser.FlashViewEventData data) {
                if (e == FlashDataParser.FlashViewEvent.MARK) {
                    flashviewOne.stop();
                    flashviewOne.setVisibility(View.GONE);
                    playOne.setVisibility(View.VISIBLE);
                    if (style == 3) {
                        if (result.get(0) == 0) {
                            //母
                            playOne.setImageResource(R.drawable.prize04);
                        } else {
                            //共
                            playOne.setImageResource(R.drawable.prize03);
                        }
                    } else if (style == 2) {
                        if (result.get(0) == 0) {
                            //母
                            playOne.setImageResource(R.drawable.prize06);
                        } else {
                            //共
                            playOne.setImageResource(R.drawable.prize02);
                        }
                    } else if (style == 1) {
                        if (result.get(0) == 0) {
                            //母
                            playOne.setImageResource(R.drawable.prize05);
                        } else {
                            //共
                            playOne.setImageResource(R.drawable.prize01);
                        }
                    }
                    giftAnim2();
                }
            }
        });
    }

    private void giftAnim2() {
        pos2Good = true;
        flashviewTwo.setVisibility(View.VISIBLE);
        playTwo.setVisibility(View.GONE);
        if (style == 3) {
            if (result.get(1) == 0) {
                //母
                flashviewTwo.reload("prize04", "flashAnims");
                flashviewTwo.play("prize04", FlashDataParser.FlashLoopTimeOnce);
            } else {
                //共
                flashviewTwo.reload("prize03", "flashAnims");
                flashviewTwo.play("prize03", FlashDataParser.FlashLoopTimeOnce);
            }
        } else if (style == 2) {
            if (result.get(1) == 0) {
                //母
                flashviewTwo.reload("prize06", "flashAnims");
                flashviewTwo.play("prize06", FlashDataParser.FlashLoopTimeOnce);
            } else {
                //共
                flashviewTwo.reload("prize02", "flashAnims");
                flashviewTwo.play("prize02", FlashDataParser.FlashLoopTimeOnce);
            }
        } else if (style == 1) {
            if (result.get(1) == 0) {
                //母
                flashviewTwo.reload("prize05", "flashAnims");
                flashviewTwo.play("prize05", FlashDataParser.FlashLoopTimeOnce);
            } else {
                //共
                flashviewTwo.reload("prize01", "flashAnims");
                flashviewTwo.play("prize01", FlashDataParser.FlashLoopTimeOnce);
            }
        }
        flashviewTwo.setEventCallback(new FlashDataParser.IFlashViewEventCallback() {
            @Override
            public void onEvent(FlashDataParser.FlashViewEvent e, FlashDataParser.FlashViewEventData data) {
                if (e == FlashDataParser.FlashViewEvent.MARK) {
                    flashviewTwo.stop();
                    flashviewTwo.setVisibility(View.GONE);
                    playTwo.setVisibility(View.VISIBLE);
                    if (style == 3) {
                        if (result.get(1) == 0) {
                            //母
                            playTwo.setImageResource(R.drawable.prize04);
                        } else {
                            //共
                            playTwo.setImageResource(R.drawable.prize03);
                        }
                    } else if (style == 2) {
                        if (result.get(1) == 0) {
                            //母
                            playTwo.setImageResource(R.drawable.prize06);
                        } else {
                            //共
                            playTwo.setImageResource(R.drawable.prize02);
                        }
                    } else if (style == 1) {
                        if (result.get(1) == 0) {
                            //母
                            playTwo.setImageResource(R.drawable.prize05);
                        } else {
                            //共
                            playTwo.setImageResource(R.drawable.prize01);
                        }
                    }
                    giftAnim3();
                }
            }
        });
    }

    private void giftAnim3() {
        pos3Good = true;
        flashviewThree.setVisibility(View.VISIBLE);
        playThree.setVisibility(View.GONE);
        if (style == 3) {
            if (result.get(2) == 0) {
                //母
                flashviewThree.reload("prize04", "flashAnims");
                flashviewThree.play("prize04", FlashDataParser.FlashLoopTimeOnce);
            } else {
                //共
                flashviewThree.reload("prize03", "flashAnims");
                flashviewThree.play("prize03", FlashDataParser.FlashLoopTimeOnce);
            }
        } else if (style == 2) {
            if (result.get(2) == 0) {
                //母
                flashviewThree.reload("prize06", "flashAnims");
                flashviewThree.play("prize06", FlashDataParser.FlashLoopTimeOnce);
            } else {
                //共
                flashviewThree.reload("prize02", "flashAnims");
                flashviewThree.play("prize02", FlashDataParser.FlashLoopTimeOnce);
            }
        } else if (style == 1) {
            if (result.get(2) == 0) {
                //母
                flashviewThree.reload("prize05", "flashAnims");
                flashviewThree.play("prize05", FlashDataParser.FlashLoopTimeOnce);
            } else {
                //共
                flashviewThree.reload("prize01", "flashAnims");
                flashviewThree.play("prize01", FlashDataParser.FlashLoopTimeOnce);
            }
        }
        flashviewThree.setEventCallback(new FlashDataParser.IFlashViewEventCallback() {
            @Override
            public void onEvent(FlashDataParser.FlashViewEvent e, FlashDataParser.FlashViewEventData data) {
                if (e == FlashDataParser.FlashViewEvent.MARK) {
                    flashviewThree.stop();
                    flashviewThree.setVisibility(View.GONE);
                    playThree.setVisibility(View.VISIBLE);
                    if (style == 3) {
                        if (result.get(2) == 0) {
                            //母
                            playThree.setImageResource(R.drawable.prize04);
                        } else {
                            //共
                            playThree.setImageResource(R.drawable.prize03);
                        }
                    } else if (style == 2) {
                        if (result.get(2) == 0) {
                            //母
                            playThree.setImageResource(R.drawable.prize06);
                        } else {
                            //共
                            playThree.setImageResource(R.drawable.prize02);
                        }
                    } else if (style == 1) {
                        if (result.get(2) == 0) {
                            //母
                            playThree.setImageResource(R.drawable.prize05);
                        } else {
                            //共
                            playThree.setImageResource(R.drawable.prize01);
                        }
                    }
                    if (endBg != null && gameRound == 6) {
                        endBg.setVisibility(View.GONE);
                    }
                    if (mListener != null) {
//                        mListener.showResult(issueRound);
                    }
                }
            }
        });
    }

    @OnClick({R.id.guess_history, R.id.award_history, R.id.play, R.id.game_help, R.id.img_shake})
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
            case R.id.img_shake:
                MobclickAgent.onEvent(mContext, "10038");
                if (mListener != null) {
                    mListener.showshakeDialog();
                }
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
}
