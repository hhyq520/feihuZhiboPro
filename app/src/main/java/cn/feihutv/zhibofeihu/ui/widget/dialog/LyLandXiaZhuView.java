package cn.feihutv.zhibofeihu.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.db.model.SysGameBetBean;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GameBetRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GameBetResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameBetRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameBetResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetUserGameDataV3Request;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetUserGameDataV3Responce;
import cn.feihutv.zhibofeihu.ui.live.OnItemClickListener;
import cn.feihutv.zhibofeihu.ui.me.recharge.RechargeActivity;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by huanghao on 2017/6/12.
 */

public class LyLandXiaZhuView{
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_hubi)
    TextView tvHubi;
    @BindView(R.id.charge)
    TextView charge;
    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.all_in)
    TextView allIn;
    @BindView(R.id.get_hubi)
    TextView getHubi;
    @BindView(R.id.left_join)
    TextView leftJoin;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.left_join_count)
    TextView leftJoinCount;
    private Context mContext;
    private int pos;
    private int playerCnt;
    private int currentBetPos = 0;
    private List<Integer> hasPlayerBet = new ArrayList<Integer>();
    private int maxJionHb;
    private int leftCnt = 0;
    private String sum1 = "0";
    private String sum2 = "0";
    private String sum3 = "0";
    private String sum4 = "0";
    private String sum5 = "0";
    private String sum6 = "0";
    private String bet1 = "0";
    private String bet2 = "0";
    private String bet3 = "0";
    private String bet4 = "0";
    private String bet5 = "0";
    private String bet6 = "0";
    private View rootView;
    public LyLandXiaZhuView(Context context, int pos, int playerCnt) {
        mContext = context;
        this.pos = pos;
        this.playerCnt = playerCnt;
        rootView = LayoutInflater.from(mContext).inflate(
                R.layout.jcxiazhu_land_dialog, null);
        ButterKnife.bind(this,rootView);
        initView();
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public void initView() {
        if (pos == 1) {
            title.setText("总和为单");
            currentBetPos = 0;
        } else if (pos == 2) {
            title.setText("总和为双");
            currentBetPos = 0;
        } else if (pos == 3) {
            title.setText("有对子");
            currentBetPos = 1;
        } else if (pos == 4) {
            title.setText("没有对子");
            currentBetPos = 1;
        } else if (pos == 5) {
            title.setText("总和末尾为0");
            currentBetPos = 2;
        } else if (pos == 6) {
            title.setText("总和末尾不为0");
            currentBetPos = 2;
        }
        long hubi = SharePreferenceUtil.getSessionLong(mContext,"PREF_KEY_HB");
        String hubitext = "";
        if (hubi >= 10000) {
            hubitext = new BigDecimal((double) hubi / 10000).setScale(2, BigDecimal.ROUND_DOWN) + "万";
        } else {
            hubitext = hubi + "";
        }
        tvHubi.setText(hubitext);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(editText.getText())) {
                    if (maxJionHb > 100) {
                        if (Integer.valueOf(editText.getText().toString()) > maxJionHb) {
                            FHUtils.showToast("超出单次参与虎币上限");
                            return;
                        }
                        if (Long.valueOf(editText.getText().toString()) >SharePreferenceUtil.getSessionLong(mContext,"PREF_KEY_HB")) {
                            FHUtils.showToast("余额不足！");
                            return;
                        }
                    }
                    int value = 0;
                    if (pos == 1) {
                        value = Integer.valueOf(editText.getText().toString()) * 2;
//                        int left = Integer.valueOf(sum1) - Integer.valueOf(bet1);
//                        leftJoinCount.setText(left + "");
                    } else if (pos == 2) {
                        value = Integer.valueOf(editText.getText().toString()) * 2;
//                        int left = Integer.valueOf(sum2) - Integer.valueOf(bet2);
//                        leftJoinCount.setText(left + "");
                    } else if (pos == 3) {
                        value = Integer.valueOf(editText.getText().toString()) * 3 + Integer.valueOf(editText.getText().toString()) / 2;
//                        int left = Integer.valueOf(sum3) - Integer.valueOf(bet3);
//                        leftJoinCount.setText(left + "");
                    } else if (pos == 4) {
                        value = Integer.valueOf(editText.getText().toString()) + Integer.valueOf(editText.getText().toString()) * 2 / 5;
//                        int left = Integer.valueOf(sum4) - Integer.valueOf(bet4);
//                        leftJoinCount.setText(left + "");
                    } else if (pos == 5) {
                        value = Integer.valueOf(editText.getText().toString()) * 11;
//                        int left = Integer.valueOf(sum5) - Integer.valueOf(bet5);
//                        leftJoinCount.setText(left + "");
                    } else if (pos == 6) {
                        value = Integer.valueOf(editText.getText().toString()) + Integer.valueOf(editText.getText().toString()) / 10;
//                        int left = Integer.valueOf(sum6) - Integer.valueOf(bet6);
//                        leftJoinCount.setText(left + "");
                    }
                    String valueCount = "";
                    if (value >= 10000) {
                        valueCount = new BigDecimal((double) value / 10000).setScale(2, BigDecimal.ROUND_DOWN) + "万";
                    } else {
                        valueCount = value + "";
                    }
                    getHubi.setText(valueCount);
                } else {
                    getHubi.setText("0");
                }
            }
        });
        int level = SharePreferenceUtil.getSessionInt(mContext,"PREF_KEY_LEVEL");
        final List<SysGameBetBean> sysGameBet = FeihuZhiboApplication.getApplication().mDataManager.getSysGameBet(level);
        if (sysGameBet != null && sysGameBet.size()>0) {
            maxJionHb = Integer.valueOf(sysGameBet.get(0).getMaxPlayerBat());
        }
        allIn.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
        allIn.getPaint().setAntiAlias(true);//抗锯齿
        charge.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
        charge.getPaint().setAntiAlias(true);//抗锯齿
//        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
//                .doGetUserGameDataV3ApiCall(new GetUserGameDataV3Request("lyzb"))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<GetUserGameDataV3Responce>() {
//                    @Override
//                    public void accept(@NonNull GetUserGameDataV3Responce responce) throws Exception {
//                        if(responce.getCode()==0){
//                            playerCnt = responce.getUserGameDataV3Data().getPlayerCnt();
//                            hasPlayerBet.add(responce.getUserGameDataV3Data().getRoundPlayerBet().getBet1());
//                            hasPlayerBet.add(responce.getUserGameDataV3Data().getRoundPlayerBet().getBet2());
//                            hasPlayerBet.add(responce.getUserGameDataV3Data().getRoundPlayerBet().getBet3());
//                            if (sysGameBet != null) {
//                                leftCnt = Integer.valueOf(sysGameBet.get(0).getMaxPlayerCnt()) - playerCnt;
//                                leftJoin.setText(String.valueOf(leftCnt));
//                            }
//                        }else{
//
//                        }
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable throwable) throws Exception {
//
//                    }
//                }));
//        getGameBetFun();
    }

    private void getGameBetFun() {
        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .doGetGameBetApiCall(new GetGameBetRequest("lyzb"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetGameBetResponce> () {
                    @Override
                    public void accept(@NonNull GetGameBetResponce responce) throws Exception {
                        if(responce.getCode()==0) {
                            GetGameBetResponce.GetGameBetData data = responce.getGetGameBetData();
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

                            if (pos == 1) {
                                int left = Integer.valueOf(sum1) - Integer.valueOf(bet1);
                                String leftCount = "";
                                if (left >= 10000) {
                                    leftCount = new BigDecimal((double) left / 10000).setScale(2, BigDecimal.ROUND_DOWN) + "万";
                                } else {
                                    leftCount = left + "";
                                }
                                leftJoinCount.setText(leftCount);
                            } else if (pos == 2) {
                                int left = Integer.valueOf(sum2) - Integer.valueOf(bet2);
                                String leftCount = "";
                                if (left >= 10000) {
                                    leftCount = new BigDecimal((double) left / 10000).setScale(2, BigDecimal.ROUND_DOWN) + "万";
                                } else {
                                    leftCount = left + "";
                                }
                                leftJoinCount.setText(leftCount);
                            } else if (pos == 3) {
                                int left = Integer.valueOf(sum3) - Integer.valueOf(bet3);
                                String leftCount = "";
                                if (left >= 10000) {
                                    leftCount = new BigDecimal((double) left / 10000).setScale(2, BigDecimal.ROUND_DOWN) + "万";
                                } else {
                                    leftCount = left + "";
                                }
                                leftJoinCount.setText(leftCount);
                            } else if (pos == 4) {
                                int left = Integer.valueOf(sum4) - Integer.valueOf(bet4);
                                String leftCount = "";
                                if (left >= 10000) {
                                    leftCount = new BigDecimal((double) left / 10000).setScale(2, BigDecimal.ROUND_DOWN) + "万";
                                } else {
                                    leftCount = left + "";
                                }
                                leftJoinCount.setText(leftCount);
                            } else if (pos == 5) {
                                int left = Integer.valueOf(sum5) - Integer.valueOf(bet5);
                                String leftCount = "";
                                if (left >= 10000) {
                                    leftCount = new BigDecimal((double) left / 10000).setScale(2, BigDecimal.ROUND_DOWN) + "万";
                                } else {
                                    leftCount = left + "";
                                }
                                leftJoinCount.setText(leftCount);
                            } else if (pos == 6) {
                                int left = Integer.valueOf(sum6) - Integer.valueOf(bet6);
                                String leftCount = "";
                                if (left >= 10000) {
                                    leftCount = new BigDecimal((double) left / 10000).setScale(2, BigDecimal.ROUND_DOWN) + "万";
                                } else {
                                    leftCount = left + "";
                                }
                                leftJoinCount.setText(leftCount);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));
    }

    public void  clearNum(){
        editText.setText("");
    }

    private void allIn(){
        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .doGetGameBetApiCall(new GetGameBetRequest("lyzb"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetGameBetResponce> () {
                    @Override
                    public void accept(@NonNull GetGameBetResponce responce) throws Exception {
                        if(responce.getCode()==0) {
                            GetGameBetResponce.GetGameBetData data = responce.getGetGameBetData();
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

                            if (pos == 1) {
                                int left = Integer.valueOf(sum1) - Integer.valueOf(bet1);
                                String leftCount = "";
                                if (left >= 10000) {
                                    leftCount = new BigDecimal((double) left / 10000).setScale(2, BigDecimal.ROUND_DOWN) + "万";
                                } else {
                                    leftCount = left + "";
                                }
                                leftJoinCount.setText(leftCount);
                            } else if (pos == 2) {
                                int left = Integer.valueOf(sum2) - Integer.valueOf(bet2);
                                String leftCount = "";
                                if (left >= 10000) {
                                    leftCount = new BigDecimal((double) left / 10000).setScale(2, BigDecimal.ROUND_DOWN) + "万";
                                } else {
                                    leftCount = left + "";
                                }
                                leftJoinCount.setText(leftCount);
                            } else if (pos == 3) {
                                int left = Integer.valueOf(sum3) - Integer.valueOf(bet3);
                                String leftCount = "";
                                if (left >= 10000) {
                                    leftCount = new BigDecimal((double) left / 10000).setScale(2, BigDecimal.ROUND_DOWN) + "万";
                                } else {
                                    leftCount = left + "";
                                }
                                leftJoinCount.setText(leftCount);
                            } else if (pos == 4) {
                                int left = Integer.valueOf(sum4) - Integer.valueOf(bet4);
                                String leftCount = "";
                                if (left >= 10000) {
                                    leftCount = new BigDecimal((double) left / 10000).setScale(2, BigDecimal.ROUND_DOWN) + "万";
                                } else {
                                    leftCount = left + "";
                                }
                                leftJoinCount.setText(leftCount);
                            } else if (pos == 5) {
                                int left = Integer.valueOf(sum5) - Integer.valueOf(bet5);
                                String leftCount = "";
                                if (left >= 10000) {
                                    leftCount = new BigDecimal((double) left / 10000).setScale(2, BigDecimal.ROUND_DOWN) + "万";
                                } else {
                                    leftCount = left + "";
                                }
                                leftJoinCount.setText(leftCount);
                            } else if (pos == 6) {
                                int left = Integer.valueOf(sum6) - Integer.valueOf(bet6);
                                String leftCount = "";
                                if (left >= 10000) {
                                    leftCount = new BigDecimal((double) left / 10000).setScale(2, BigDecimal.ROUND_DOWN) + "万";
                                } else {
                                    leftCount = left + "";
                                }
                                leftJoinCount.setText(leftCount);
                            }

                            int left = 0;
                            if (pos == 1) {
                                left = Integer.valueOf(sum1) - Integer.valueOf(bet1);
                            } else if (pos == 2) {
                                left = Integer.valueOf(sum2) - Integer.valueOf(bet2);
                            } else if (pos == 3) {
                                left = Integer.valueOf(sum3) - Integer.valueOf(bet3);
                            } else if (pos == 4) {
                                left = Integer.valueOf(sum4) - Integer.valueOf(bet4);
                            } else if (pos == 5) {
                                left = Integer.valueOf(sum5) - Integer.valueOf(bet5);
                            } else if (pos == 6) {
                                left = Integer.valueOf(sum6) - Integer.valueOf(bet6);
                            }
                            if (hasPlayerBet.size() > 0) {
                                if ((maxJionHb - hasPlayerBet.get(currentBetPos)) < left) {
                                    if (SharePreferenceUtil.getSessionLong(mContext,"PREF_KEY_HB") > (maxJionHb - hasPlayerBet.get(currentBetPos))) {
                                        editText.setText((maxJionHb - hasPlayerBet.get(currentBetPos)) + "");
                                        return;
                                    }
                                } else {
                                    if (SharePreferenceUtil.getSessionLong(mContext,"PREF_KEY_HB")  >= left) {
                                        editText.setText(left + "");
                                        return;
                                    }
                                }
                            }
                            editText.setText(SharePreferenceUtil.getSessionLong(mContext,"PREF_KEY_HB")  + "");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));
    }

    @OnClick({R.id.all_in, R.id.btn_commit, R.id.charge})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.all_in:
                onFresh();
                break;
            case R.id.btn_commit:
                if (SharePreferenceUtil.getSessionInt(FeihuZhiboApplication.getApplication(),"PREF_KEY_LEVEL") < 6) {
                    FHUtils.showToast("六级开启玩法");
                    return;
                }
                if (!TextUtils.isEmpty(editText.getText())) {
                    if (Integer.valueOf(editText.getText().toString()) < 100) {
                        FHUtils.showToast("请输入不小于100的虎币数");
                        return;
                    }
                    if (SharePreferenceUtil.getSessionInt(mContext,"PREF_KEY_LEVEL") < 6) {
                        FHUtils.showToast("超出等级限制,无法下注");
                        return;
                    }
                    final long myHubi = SharePreferenceUtil.getSessionLong(mContext,"PREF_KEY_HB");

                    new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                            .doGameBetApiCall(new GameBetRequest("lyzb", "player",pos, Integer.valueOf(editText.getText().toString())))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<GameBetResponce>() {
                                @Override
                                public void accept(@NonNull GameBetResponce gameBetResponce) throws Exception {
                                    if(gameBetResponce.getCode()==0){
//                                        FHUtils.showToast("投入成功");
                                        if (hasPlayerBet.size() > 0) {
                                            if (hasPlayerBet.get(currentBetPos) == 0) {
                                                if (leftCnt > 0) {
                                                    leftCnt--;
                                                }
                                                leftJoin.setText(leftCnt + "");
                                            }
                                        }
                                        long hubi = myHubi - Long.valueOf(editText.getText().toString());

                                        String hubitext = "";
                                        if (hubi >= 10000) {
                                            hubitext = new BigDecimal((double) hubi / 10000).setScale(2, BigDecimal.ROUND_DOWN) + "万";
                                        } else {
                                            hubitext = hubi + "";
                                        }

                                        tvHubi.setText(hubitext);
                                        if (hasPlayerBet.size() > 0) {
                                            int value = hasPlayerBet.get(currentBetPos) + Integer.valueOf(editText.getText().toString());
                                            hasPlayerBet.set(currentBetPos, value);
                                        }
                                        if (mListener != null) {
                                            mListener.onItemClick(1);
                                        }

                                        getGameBetFun();
                                    }else{
                                        if (gameBetResponce.getCode()== 4704) {
                                            FHUtils.showToast("庄家押注不够，无法押注");
                                        } else if (gameBetResponce.getCode() == 4707) {
                                            FHUtils.showToast("今日坐闲次数超出等级限制");
                                        } else if (gameBetResponce.getCode() == 4202) {
                                            FHUtils.showToast("余额不足");
                                        } else if (gameBetResponce.getCode()== 4708) {
                                            FHUtils.showToast("坐闲押注虎币超出等级限制");
                                        } else if (gameBetResponce.getCode()== 4701) {
                                            FHUtils.showToast("未在竞猜时间，下局再来");
                                        }
                                    }
                                }
                            },new Consumer<Throwable>() {
                                @Override
                                public void accept(@NonNull Throwable throwable) throws Exception {

                                }
                            }));
                } else {
                    FHUtils.showToast("请输入虎币数");
                }
                break;
            case R.id.charge:
                Intent intent = new Intent(mContext, RechargeActivity.class);
                intent.putExtra("fromWhere", "我要竞猜页面");
                mContext.startActivity(intent);
                break;
        }
    }
    public View getView() {
        return rootView;
    }

    public void onFresh(int pos1,int playerCnt1){
        this.pos = pos1;
        this.playerCnt = playerCnt1;
        if (pos == 1) {
            title.setText("总和为单");
            currentBetPos = 0;
        } else if (pos == 2) {
            title.setText("总和为双");
            currentBetPos = 0;
        } else if (pos == 3) {
            title.setText("有对子");
            currentBetPos = 1;
        } else if (pos == 4) {
            title.setText("没有对子");
            currentBetPos = 1;
        } else if (pos == 5) {
            title.setText("总和末尾为0");
            currentBetPos = 2;
        } else if (pos == 6) {
            title.setText("总和末尾不为0");
            currentBetPos = 2;
        }
        long hubi = SharePreferenceUtil.getSessionLong(mContext,"PREF_KEY_HB");
        String hubitext = "";
        if (hubi >= 10000) {
            hubitext = new BigDecimal((double) hubi / 10000).setScale(2, BigDecimal.ROUND_DOWN) + "万";
        } else {
            hubitext = hubi + "";
        }
        tvHubi.setText(hubitext);
        int level = SharePreferenceUtil.getSessionInt(mContext,"PREF_KEY_LEVEL");
        final List<SysGameBetBean> sysGameBet = FeihuZhiboApplication.getApplication().mDataManager.getSysGameBet(level);
        if (sysGameBet != null && sysGameBet.size()>0) {
            maxJionHb = Integer.valueOf(sysGameBet.get(0).getMaxPlayerBat());
        }
        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .doGetUserGameDataV3ApiCall(new GetUserGameDataV3Request("lyzb"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetUserGameDataV3Responce>() {
                    @Override
                    public void accept(@NonNull GetUserGameDataV3Responce responce) throws Exception {
                        if(responce.getCode()==0){
                            playerCnt = responce.getUserGameDataV3Data().getPlayerCnt();
                            if(hasPlayerBet.size()>0){
                                hasPlayerBet.clear();
                            }
                            hasPlayerBet.add(responce.getUserGameDataV3Data().getRoundPlayerBet().getBet1());
                            hasPlayerBet.add(responce.getUserGameDataV3Data().getRoundPlayerBet().getBet2());
                            hasPlayerBet.add(responce.getUserGameDataV3Data().getRoundPlayerBet().getBet3());
                            if (sysGameBet != null) {
                                leftCnt = Integer.valueOf(sysGameBet.get(0).getMaxPlayerCnt()) + responce.getUserGameDataV3Data().getExtVipCnt()- playerCnt;
                                leftJoin.setText(String.valueOf(leftCnt));
                            }
                        }else{

                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));
        getGameBetFun();
    }

    public void onFresh(){
        int level = SharePreferenceUtil.getSessionInt(mContext,"PREF_KEY_LEVEL");
        final List<SysGameBetBean> sysGameBet = FeihuZhiboApplication.getApplication().mDataManager.getSysGameBet(level);
        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .doGetUserGameDataV3ApiCall(new GetUserGameDataV3Request("lyzb"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetUserGameDataV3Responce>() {
                    @Override
                    public void accept(@NonNull GetUserGameDataV3Responce responce) throws Exception {
                        if(responce.getCode()==0){
                            playerCnt = responce.getUserGameDataV3Data().getPlayerCnt();
                            if(hasPlayerBet.size()>0){
                                hasPlayerBet.clear();
                            }
                            hasPlayerBet.add(responce.getUserGameDataV3Data().getRoundPlayerBet().getBet1());
                            hasPlayerBet.add(responce.getUserGameDataV3Data().getRoundPlayerBet().getBet2());
                            hasPlayerBet.add(responce.getUserGameDataV3Data().getRoundPlayerBet().getBet3());
                            if (sysGameBet != null) {
                                leftCnt = Integer.valueOf(sysGameBet.get(0).getMaxPlayerCnt()) + responce.getUserGameDataV3Data().getExtVipCnt()- playerCnt;
                                leftJoin.setText(String.valueOf(leftCnt));
                            }
                            allIn();
                        }else{

                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));
    }
}
