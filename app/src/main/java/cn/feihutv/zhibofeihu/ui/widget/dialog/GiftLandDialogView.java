package cn.feihutv.zhibofeihu.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.db.model.SysGiftNewBean;
import cn.feihutv.zhibofeihu.data.local.SysdataHelper;
import cn.feihutv.zhibofeihu.data.local.model.SysbagBean;
import cn.feihutv.zhibofeihu.ui.live.OnItemClickListener;
import cn.feihutv.zhibofeihu.ui.widget.GiftlandView;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;


/**
 * Created by huanghao on 2017/4/25.
 */

public class GiftLandDialogView extends Dialog {


    @BindView(R.id.sliding_tabs)
    TabLayout slidingTabs;
    @BindView(R.id.line_top)
    View lineTop;
    @BindView(R.id.sc_tv)
    TextView scTv;
    @BindView(R.id.line_bottom)
    View lineBottom;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.recycle_frm)
    RelativeLayout recycleFrm;
    @BindView(R.id.text)
    ImageView text;
    @BindView(R.id.yin_coin)
    TextView yinCoin;
    @BindView(R.id.text_hb)
    ImageView textHb;
    @BindView(R.id.hb_coin)
    TextView hbCoin;
    @BindView(R.id.charge)
    TextView charge;
    @BindView(R.id.editText)
    TextView editText;
    @BindView(R.id.other_img)
    ImageView otherImg;
    @BindView(R.id.send_view)
    LinearLayout sendView;
    @BindView(R.id.donate)
    Button donate;
    @BindView(R.id.send_lin)
    LinearLayout sendLin;
    @BindView(R.id.other_count)
    TextView otherCount;
    @BindView(R.id.yishengyishi)
    TextView yishengyishi;
    @BindView(R.id.woaini)
    TextView woaini;
    @BindView(R.id.yaobaobao)
    TextView yaobaobao;
    @BindView(R.id.qiqieshunli)
    TextView qiqieshunli;
    @BindView(R.id.shiquanshimei)
    TextView shiquanshimei;
    @BindView(R.id.yixinyiyi)
    TextView yixinyiyi;
    @BindView(R.id.count_view)
    LinearLayout countView;
    private Context mContext;
    private List<View> mViewList = new ArrayList<>();
    private int currentPos = 0;
    private GiftlandView giftView;
    private GiftlandView bagView;
    private List<SysbagBean> sysbagBeanList;

    public GiftLandDialogView(Context context, List<SysbagBean> sysbagBeanList) {
        super(context, R.style.user_dialog);
        mContext = context;
        this.sysbagBeanList = sysbagBeanList;
    }

    public interface GiftLandDialogListener {
        void sendGift(int id, int count, boolean isbag);

        void charge();

        void showNumDialog();
    }

    private GiftLandDialogListener mListener;

    public void setGiftDialogListener(GiftLandDialogListener listener) {
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gift_dialog_land_view);
        ButterKnife.bind(this);
        initView();
        initListener();
    }

    public void setEditText(int pos) {
        if (editText != null) {
            editText.setText(pos + "");
        }
    }

    private void initListener() {
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPos == 0) {
                    if (giftView.getSelectGiftID() == -1) {
                        return;
                    }
                } else {
                    if (bagView.getSelectGiftID() == -1) {
                        return;
                    }
                }

                sendView.setVisibility(View.VISIBLE);
                countView.setVisibility(View.VISIBLE);
//                outFrm.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(editText.getText())) {
                    if (currentPos == 0) {
                        if (mListener != null)
                            mListener.sendGift(giftView.getSelectGiftID(), Integer.valueOf(editText.getText().toString()), false);

                    } else {
                        if (mListener != null)
                            mListener.sendGift(bagView.getSelectGiftID(), Integer.valueOf(editText.getText().toString()), true);
                    }
                    countView.setVisibility(View.INVISIBLE);
//                    outFrm.setVisibility(View.GONE);
                }
            }
        });
        sendView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countView.getVisibility() == View.VISIBLE) {
                    countView.setVisibility(View.INVISIBLE);
                } else {
                    countView.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(editText.getText())) {
                        int num = Integer.valueOf(editText.getText().toString());
                        if (num == 1) {
                            changeTextColor(yixinyiyi);
                        } else if (num == 10) {
                            changeTextColor(shiquanshimei);
                        } else if (num == 66) {
                            changeTextColor(qiqieshunli);
                        } else if (num == 188) {
                            changeTextColor(yaobaobao);
                        } else if (num == 520) {
                            changeTextColor(woaini);
                        } else if (num == 1314) {
                            changeTextColor(yishengyishi);
                        } else {
                            changeTextColor(otherCount);
                        }
                    }
                }
            }
        });
    }


    public void onFreshCoin() {
        long yhbcoin = SharePreferenceUtil.getSessionLong(mContext, "PREF_KEY_YHB");
        String yhb = "";
        if (yhbcoin >= 10000) {
            yhb = new BigDecimal((double) yhbcoin / 10000).setScale(2, BigDecimal.ROUND_DOWN) + "万";
        } else {
            yhb = yhbcoin + "";
        }
        yinCoin.setText(yhb);
        long hbcoin = SharePreferenceUtil.getSessionLong(mContext, "PREF_KEY_HB");
        String hb = "";
        if (hbcoin >= 10000) {
            hb = new BigDecimal((double) hbcoin / 10000).setScale(2, BigDecimal.ROUND_DOWN) + "万";
        } else {
            hb = hbcoin + "";
        }
        hbCoin.setText(hb);
    }

    @OnClick({R.id.other_count, R.id.yishengyishi, R.id.woaini, R.id.yaobaobao, R.id.qiqieshunli, R.id.shiquanshimei, R.id.yixinyiyi, R.id.charge, R.id.other_img})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.other_count:
                countView.setVisibility(View.INVISIBLE);
                editText.setText("");
                changeTextColor(otherCount);
//                outFrm.setVisibility(View.GONE);
                if (mListener != null) {
                    mListener.showNumDialog();
                }
                break;
            case R.id.yishengyishi:
                countView.setVisibility(View.INVISIBLE);
                editText.setText("1314");
                changeTextColor(yishengyishi);
//                outFrm.setVisibility(View.GONE);
                break;
            case R.id.woaini:
                countView.setVisibility(View.INVISIBLE);
                editText.setText("520");
                changeTextColor(woaini);
//                outFrm.setVisibility(View.GONE);
                break;
            case R.id.yaobaobao:
                countView.setVisibility(View.INVISIBLE);
                editText.setText("188");
                changeTextColor(yaobaobao);
//                outFrm.setVisibility(View.GONE);
                break;
            case R.id.qiqieshunli:
                countView.setVisibility(View.INVISIBLE);
                editText.setText("66");
                changeTextColor(qiqieshunli);
//                outFrm.setVisibility(View.GONE);
                break;
            case R.id.shiquanshimei:
                countView.setVisibility(View.INVISIBLE);
                editText.setText("10");
                changeTextColor(shiquanshimei);
//                outFrm.setVisibility(View.GONE);
                break;
            case R.id.yixinyiyi:
                countView.setVisibility(View.INVISIBLE);
                editText.setText("1");
                changeTextColor(yixinyiyi);
//                outFrm.setVisibility(View.GONE);
                break;
            case R.id.charge:
                if (mListener != null) {
                    mListener.charge();
                }
                break;
            case R.id.other_img:
//                countView.setVisibility(View.VISIBLE);
//                outFrm.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void changeTextColor(TextView textView) {
        otherCount.setTextColor(ContextCompat.getColor(mContext, R.color.app_white));
        yishengyishi.setTextColor(ContextCompat.getColor(mContext, R.color.app_white));
        woaini.setTextColor(ContextCompat.getColor(mContext, R.color.app_white));
        yaobaobao.setTextColor(ContextCompat.getColor(mContext, R.color.app_white));
        qiqieshunli.setTextColor(ContextCompat.getColor(mContext, R.color.app_white));
        shiquanshimei.setTextColor(ContextCompat.getColor(mContext, R.color.app_white));
        yixinyiyi.setTextColor(ContextCompat.getColor(mContext, R.color.app_white));

        textView.setTextColor(ContextCompat.getColor(mContext, R.color.btn_sure_forbidden));

    }

    private void initView() {
        List<SysGiftNewBean> sysGiftBeenNew = new ArrayList<>();
        for (SysGiftNewBean item : FeihuZhiboApplication.getApplication().mDataManager.getLiveGiftList()) {
            if (!SysdataHelper.isGiftVisual(item.getId(), FeihuZhiboApplication.getApplication().mDataManager.getSysGoodsNewBean())) {

            } else {
                sysGiftBeenNew.add(item);
            }
        }
        giftView = new GiftlandView(mContext, sysGiftBeenNew, false);
        bagView = new GiftlandView(mContext, SysdataHelper.getbagBeanList(FeihuZhiboApplication.getApplication().mDataManager.getSysGiftNew(), sysbagBeanList), true);
        mViewList.add(giftView.getView());
        mViewList.add(bagView.getView());

        slidingTabs.addTab(slidingTabs.newTab().setText(R.string.gift));

        slidingTabs.addTab(slidingTabs.newTab().setText(R.string.bag));
        slidingTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                viewpager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewpager.setAdapter(new LandPagerAdapter(mViewList));
        viewpager.setCurrentItem(0);
        viewpager.setOffscreenPageLimit(2);
        slidingTabs.setupWithViewPager(viewpager);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.e("print", "onPageSelected: ---->" + position);
                viewpager.setCurrentItem(position);
                currentPos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        long yhbcoin = SharePreferenceUtil.getSessionLong(mContext, "PREF_KEY_YHB");
        String yhb = "";
        if (yhbcoin >= 10000) {
            yhb = new BigDecimal((double) yhbcoin / 10000).setScale(2, BigDecimal.ROUND_DOWN) + "万";
        } else {
            yhb = yhbcoin + "";
        }
        yinCoin.setText(yhb);
        long hbcoin = SharePreferenceUtil.getSessionLong(mContext, "PREF_KEY_HB");
        String hb = "";
        if (hbcoin >= 10000) {
            hb = new BigDecimal((double) hbcoin / 10000).setScale(2, BigDecimal.ROUND_DOWN) + "万";
        } else {
            hb = hbcoin + "";
        }
        hbCoin.setText(hb);

        giftView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                SysGiftNewBean gift = FeihuZhiboApplication.getApplication().mDataManager.getGiftBeanByID(String.valueOf(position));
                if (gift != null) {
                    scTv.setText(gift.getTips());
                }
                if (gift != null) {
                    if (gift.getIsAnimation().equals("1")) {
                        countView.setVisibility(View.INVISIBLE);
                        sendView.setVisibility(View.VISIBLE);
                        otherImg.setVisibility(View.GONE);
                        editText.setText("1");
//                        outFrm.setVisibility(View.GONE);
                        sendView.setEnabled(false);
                    } else {
                        otherImg.setVisibility(View.VISIBLE);
                        sendView.setVisibility(View.VISIBLE);
//                        editText.setText("1");
                        sendView.setEnabled(true);
                    }
                }
            }
        });
        bagView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                SysGiftNewBean gift = FeihuZhiboApplication.getApplication().mDataManager.getGiftBeanByID(String.valueOf(position));
                if (gift != null) {
                    scTv.setText(gift.getTips());
                }
                if (gift != null) {
                    if (gift.getIsAnimation().equals("1")) {
                        countView.setVisibility(View.INVISIBLE);
                        sendView.setVisibility(View.VISIBLE);
                        otherImg.setVisibility(View.GONE);
                        editText.setText("1");
                        sendView.setEnabled(false);
//                        outFrm.setVisibility(View.GONE);
                    } else {
//                        editText.setText("1");
                        otherImg.setVisibility(View.VISIBLE);
                        sendView.setVisibility(View.VISIBLE);
                        sendView.setEnabled(true);
                    }
                }
            }
        });
    }

    public void setData(List<SysGiftNewBean> datas) {
        if (bagView != null) {
            bagView.setDatas(datas);
        }
    }


    class LandPagerAdapter extends PagerAdapter {
        private List<View> mViewList;
        // 标题数组
        String[] titles = {"礼物", "包裹"};

        public LandPagerAdapter(List<View> mViewList) {
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
