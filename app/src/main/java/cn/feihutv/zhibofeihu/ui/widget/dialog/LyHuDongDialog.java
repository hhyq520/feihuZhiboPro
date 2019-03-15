package cn.feihutv.zhibofeihu.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetUserGameDataV3Request;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetUserGameDataV3Responce;
import cn.feihutv.zhibofeihu.ui.live.OnItemClickListener;
import cn.feihutv.zhibofeihu.ui.me.recharge.RechargeActivity;
import cn.feihutv.zhibofeihu.ui.widget.HudongView;
import cn.feihutv.zhibofeihu.ui.widget.VerticalViewPager;
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

public class LyHuDongDialog extends Dialog {
    @BindView(R.id.viewpager)
    VerticalViewPager viewpager;
    @BindView(R.id.left_cishu)
    TextView leftCishu;
    @BindView(R.id.left_hubi)
    TextView leftHubi;
    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.all_in)
    TextView allIn;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_1)
    TextView tv1;
    @BindView(R.id.charge)
    TextView charge;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.avail_count)
    TextView availCount;
    @BindView(R.id.img_top)
    ImageView imgTop;
    @BindView(R.id.img_down)
    ImageView imgDown;
    private Context mContext;
    private int bankerCnt;
    private List<Integer> hasBankerBet = new ArrayList<Integer>();
    private int maxJionHb;
    private int pos = 0;
    private HudongView view1;
    private HudongView view2;
    private HudongView view3;
    private List<View> mViewList = new ArrayList<>();
    private int currentPos = 0;
    private int leftCnt = 0;
    private int currentBetPos = 0;

    public LyHuDongDialog(Context context, int bankerCnt) {
        super(context, R.style.floag_dialog);
        mContext = context;
        this.bankerCnt = bankerCnt;
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hudong_dialog);
        ButterKnife.bind(this);
        view1 = new HudongView(mContext, 3,false);
        view2 = new HudongView(mContext, 4,false);
        view3 = new HudongView(mContext, 5,false);
        mViewList.add(view1.getView());
        mViewList.add(view2.getView());
        mViewList.add(view3.getView());
        viewpager.setAdapter(new VPagerAdapter(mViewList));
        viewpager.setCurrentItem(0);
        view1.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                pos = position;
                if (position == 5) {
                    currentBetPos = 0;
                    tvTitle.setText("总和为单");
                    if (!TextUtils.isEmpty(editText.getText())) {
                        int value = Integer.valueOf(editText.getText().toString());
                        String valueCount = "";
                        if (value >= 10000) {
                            valueCount = new BigDecimal((double) value / 10000).setScale(2, BigDecimal.ROUND_DOWN) + "万";
                        } else {
                            valueCount = value + "";
                        }
                        availCount.setText(valueCount);
                    }
                } else if (position == 6) {
                    currentBetPos = 0;
                    tvTitle.setText("总和为双");
                    if (!TextUtils.isEmpty(editText.getText())) {
                        int value = Integer.valueOf(editText.getText().toString());
                        String valueCount = "";
                        if (value >= 10000) {
                            valueCount = new BigDecimal((double) value / 10000).setScale(2, BigDecimal.ROUND_DOWN) + "万";
                        } else {
                            valueCount = value + "";
                        }
                        availCount.setText(valueCount);
                    }
                }
            }
        });
        view2.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                pos = position;
                if (position == 7) {
                    tvTitle.setText("有对子");
                    currentBetPos = 1;
                    if (!TextUtils.isEmpty(editText.getText())) {
                        int value = Integer.valueOf(editText.getText().toString()) * 2 / 5;
                        String valueCount = "";
                        if (value >= 10000) {
                            valueCount = new BigDecimal((double) value / 10000).setScale(2, BigDecimal.ROUND_DOWN) + "万";
                        } else {
                            valueCount = value + "";
                        }
                        availCount.setText(valueCount);
                    }
                } else if (position == 8) {
                    currentBetPos = 1;
                    tvTitle.setText("没有对子");
                    if (!TextUtils.isEmpty(editText.getText())) {
                        int value = Integer.valueOf(editText.getText().toString()) * 5 / 2;
                        String valueCount = "";
                        if (value >= 10000) {
                            valueCount = new BigDecimal((double) value / 10000).setScale(2, BigDecimal.ROUND_DOWN) + "万";
                        } else {
                            valueCount = value + "";
                        }
                        availCount.setText(valueCount);
                    }
                }
            }
        });
        view3.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                pos = position;
                if (position == 9) {
                    currentBetPos = 2;
                    tvTitle.setText("总和末尾为0");
                    if (!TextUtils.isEmpty(editText.getText())) {
                        int value = Integer.valueOf(editText.getText().toString()) / 10;
                        String valueCount = "";
                        if (value >= 10000) {
                            valueCount = new BigDecimal((double) value / 10000).setScale(2, BigDecimal.ROUND_DOWN) + "万";
                        } else {
                            valueCount = value + "";
                        }
                        availCount.setText(valueCount);
                    }
                } else if (position == 10) {
                    tvTitle.setText("总和末尾不为0");
                    currentBetPos = 2;
                    if (!TextUtils.isEmpty(editText.getText())) {
                        int value = Integer.valueOf(editText.getText().toString()) * 10;
                        String valueCount = "";
                        if (value >= 10000) {
                            valueCount = new BigDecimal((double) value / 10000).setScale(2, BigDecimal.ROUND_DOWN) + "万";
                        } else {
                            valueCount = value + "";
                        }
                        availCount.setText(valueCount);
                    }
                }
            }
        });

        int level = SharePreferenceUtil.getSessionInt(mContext,"PREF_KEY_LEVEL");
        final List<SysGameBetBean> sysGameBet = FeihuZhiboApplication.getApplication().mDataManager.getSysGameBet(level);
        if (sysGameBet != null && sysGameBet.size()>0) {
            maxJionHb = Integer.valueOf(sysGameBet.get(0).getMaxBankerBet());
        }
        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .doGetUserGameDataV3ApiCall(new GetUserGameDataV3Request("lyzb"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetUserGameDataV3Responce>() {
                    @Override
                    public void accept(@NonNull GetUserGameDataV3Responce responce) throws Exception {
                        if(responce.getCode()==0){
                            bankerCnt = responce.getUserGameDataV3Data().getBankerCnt();
                            hasBankerBet.add(responce.getUserGameDataV3Data().getRoundBankerBet().getBet1());
                            hasBankerBet.add(responce.getUserGameDataV3Data().getRoundBankerBet().getBet2());
                            hasBankerBet.add(responce.getUserGameDataV3Data().getRoundBankerBet().getBet3());
                            if (sysGameBet != null) {
                                leftCnt = Integer.valueOf(sysGameBet.get(0).getMaxBankerCnt())+ responce.getUserGameDataV3Data().getExtVipCnt() - bankerCnt;
                                leftCishu.setText(String.valueOf(leftCnt));
                            }
                        }else{

                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));

        long hubi = SharePreferenceUtil.getSessionLong(mContext,"PREF_KEY_HB");
        String hubitext = "";
        if (hubi >= 10000) {
            hubitext = new BigDecimal((double) hubi / 10000).setScale(2, BigDecimal.ROUND_DOWN) + "万";
        } else {
            hubitext = hubi + "";
        }
        leftHubi.setText(hubitext);
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
                        try {
                            if (Integer.valueOf(editText.getText().toString()) > maxJionHb) {
                                FHUtils.showToast("超出单次参与虎币上限");
                                return;
                            }
                            if (Integer.valueOf(editText.getText().toString()) > SharePreferenceUtil.getSessionLong(mContext,"PREF_KEY_HB")) {
                                FHUtils.showToast("余额不足！");
                                return;
                            }
                        } catch (Exception e) {

                        }
                    }
                    int value = 0;
                    if (pos == 5) {
                        value = Integer.valueOf(editText.getText().toString());
                    } else if (pos == 6) {
                        value = Integer.valueOf(editText.getText().toString());
                    } else if (pos == 7) {
                        value = Integer.valueOf(editText.getText().toString()) * 2 / 5;
                    } else if (pos == 8) {
                        value = Integer.valueOf(editText.getText().toString()) * 5 / 2;
                    } else if (pos == 9) {
                        value = Integer.valueOf(editText.getText().toString()) / 10;
                    } else if (pos == 10) {
                        value = Integer.valueOf(editText.getText().toString()) * 10;
                    }
                    String valueCount = "";
                    if (value >= 10000) {
                        valueCount = new BigDecimal((double) value / 10000).setScale(2, BigDecimal.ROUND_DOWN) + "万";
                    } else {
                        valueCount = value + "";
                    }
                    availCount.setText(valueCount);
                } else {
                    availCount.setText("0");
                }
            }
        });
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPos = position;
                pos = 0;
                view1.clearSelect();
                view2.clearSelect();
                view3.clearSelect();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        allIn.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
        allIn.getPaint().setAntiAlias(true);//抗锯齿
        charge.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
        charge.getPaint().setAntiAlias(true);//抗锯齿
    }

    @OnClick({R.id.all_in, R.id.btn_commit, R.id.charge, R.id.img_top, R.id.img_down})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.all_in:
                if (pos == 0) {
                    FHUtils.showToast("请选择类别");
                    return;
                }
                if (hasBankerBet.size() > 0) {
                    if (SharePreferenceUtil.getSessionLong(mContext,"PREF_KEY_HB") > (maxJionHb - hasBankerBet.get(currentBetPos))) {
                        editText.setText((maxJionHb - hasBankerBet.get(currentBetPos)) + "");
                    } else {
                        editText.setText(SharePreferenceUtil.getSessionLong(mContext,"PREF_KEY_HB") + "");
                    }
                }
                break;
            case R.id.btn_commit:
                if (!TextUtils.isEmpty(editText.getText())) {
                    if (Integer.valueOf(editText.getText().toString()) < 1000) {
                        FHUtils.showToast("请输入不小于1000的虎币数");
                        return;
                    }
                    if (SharePreferenceUtil.getSessionInt(mContext,"PREF_KEY_LEVEL") < 6) {
                        FHUtils.showToast("超出等级限制,无法下注");
                        return;
                    }
                    if (pos == 0) {
                        FHUtils.showToast("请选择你认为会输的一方");
                        return;
                    }
                    final long myHubi = SharePreferenceUtil.getSessionLong(mContext,"PREF_KEY_HB");
                    new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                            .doGameBetApiCall(new GameBetRequest("lyzb", "banker", pos - 4, Integer.valueOf(editText.getText().toString())))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<GameBetResponce>() {
                                @Override
                                public void accept(@NonNull GameBetResponce gameBetResponce) throws Exception {
                                    if(gameBetResponce.getCode()==0){
//                                        FHUtils.showToast("投入成功");
                                        if (hasBankerBet.size() > 0) {
                                            if (hasBankerBet.get(currentBetPos) == 0) {
                                                if (leftCnt > 0) {
                                                    leftCnt--;
                                                }
                                                leftCishu.setText(leftCnt + "次");
                                            }
                                        }
                                        long hubi = myHubi - Integer.valueOf(editText.getText().toString());
                                        SharePreferenceUtil.saveSeesionLong(mContext, "PREF_KEY_HB", hubi);
                                        String hubitext = "";
                                        if (hubi >= 10000) {
                                            hubitext = new BigDecimal((double) hubi / 10000).setScale(2, BigDecimal.ROUND_DOWN) + "万";
                                        } else {
                                            hubitext = hubi + "";
                                        }
                                        leftHubi.setText(hubitext);
                                        if (hasBankerBet.size() > 0) {
                                            int value = hasBankerBet.get(currentBetPos) + Integer.valueOf(editText.getText().toString());
                                            hasBankerBet.set(currentBetPos, value);
                                        }

                                        if (mListener != null) {
                                            mListener.onItemClick(1);
                                        }

                                    }else{
                                        if (gameBetResponce.getCode() == 4705) {
                                            FHUtils.showToast("今日坐庄次数超出等级限制");
                                        } else if (gameBetResponce.getCode()== 4706) {
                                            FHUtils.showToast("坐庄押注虎币超出等级限制");
                                        } else if (gameBetResponce.getCode() == 4701) {
                                            FHUtils.showToast("未在竞猜时间，下局再来");
                                        } else {
                                            FHUtils.showToast("开启失败");
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
                intent.putExtra("fromWhere", "我要互动页面");
                mContext.startActivity(intent);
                break;
            case R.id.img_top:
                if (currentPos == 2) {
                    viewpager.setCurrentItem(1);
                } else if (currentPos == 1) {
                    viewpager.setCurrentItem(0);
                }

                break;
            case R.id.img_down:
                if (currentPos == 0) {
                    viewpager.setCurrentItem(1);
                } else if (currentPos == 1) {
                    viewpager.setCurrentItem(2);
                }
                break;
        }
    }

    class VPagerAdapter extends PagerAdapter {
        private List<View> mViewList;
        // 标题数组
        String[] titles = {"", "", ""};

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
}
