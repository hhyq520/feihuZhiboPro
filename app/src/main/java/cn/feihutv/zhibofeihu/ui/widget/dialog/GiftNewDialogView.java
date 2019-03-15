package cn.feihutv.zhibofeihu.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import cn.feihutv.zhibofeihu.ui.widget.GiftView;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;

/**
 * Created by huanghao on 2017/4/22.
 */

public class GiftNewDialogView extends Dialog {

    @BindView(R.id.viewpager)
    ViewPager viewpager;

//    @BindView(R.id.text)
//    TextView text;

    @BindView(R.id.yin_coin)
    TextView yinCoin;

//    @BindView(R.id.text_hb)
//    TextView textHb;

    @BindView(R.id.hb_coin)
    TextView hbCoin;

    @BindView(R.id.charge)
    TextView charge;

    @BindView(R.id.editText)
    TextView editText;

    @BindView(R.id.send_view)
    LinearLayout sendView;

    @BindView(R.id.donate)
    Button donate;

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

    @BindView(R.id.textGift)
    TextView textGift;

    @BindView(R.id.textBag)
    TextView textBag;

    @BindView(R.id.sc_tv)
    TextView scTv;
    @BindView(R.id.other_img)
    ImageView otherImg;

    private Context mContext;
    private List<SysGiftNewBean> listDatas;//礼物数据源
    private List<View> mViewList = new ArrayList<>();
    private int currentPos = 0;
    private GiftView giftView;
    private GiftView bagView;
    private boolean isPc = false;
    private MyPagerAdapter myPagerAdapter;
    private List<SysbagBean> sysbagBeanList;

    public GiftNewDialogView(List<SysbagBean> sysbagBeanList,Context context, boolean isPc) {
        super(context, R.style.user_dialog);
        mContext = context;
        this.sysbagBeanList=sysbagBeanList;
        this.isPc = isPc;
    }

    public GiftNewDialogView(List<SysbagBean> sysbagBeanList,Context context, boolean isPc, boolean aa) {
        super(context, R.style.user_pc_dialog);
        mContext = context;
        this.sysbagBeanList=sysbagBeanList;
        this.isPc = isPc;
    }

    @OnClick({R.id.other_count, R.id.yishengyishi, R.id.woaini, R.id.yaobaobao, R.id.qiqieshunli, R.id.shiquanshimei, R.id.yixinyiyi, R.id.textGift, R.id.textBag, R.id.charge, R.id.other_img})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.other_count:
                countView.setVisibility(View.GONE);
                changeTextColor(otherCount);
                if(mListener!=null){
                    mListener.showNumDialog();
                }
                break;
            case R.id.yishengyishi:
                countView.setVisibility(View.GONE);
                editText.setText("1314");
                changeTextColor(yishengyishi);
                break;
            case R.id.woaini:
                countView.setVisibility(View.GONE);
                editText.setText("520");
                changeTextColor(woaini);
                break;
            case R.id.yaobaobao:
                countView.setVisibility(View.GONE);
                editText.setText("188");
                changeTextColor(yaobaobao);
                break;
            case R.id.qiqieshunli:
                countView.setVisibility(View.GONE);
                editText.setText("66");
                changeTextColor(qiqieshunli);
                break;
            case R.id.shiquanshimei:
                countView.setVisibility(View.GONE);
                editText.setText("10");
                changeTextColor(shiquanshimei);
                break;
            case R.id.yixinyiyi:
                countView.setVisibility(View.GONE);
                editText.setText("1");
                changeTextColor(yixinyiyi);
                break;
            case R.id.textGift:
                viewpager.setCurrentItem(0);
                textGift.setTextColor(ContextCompat.getColor(mContext, R.color.btn_sure_forbidden));
                textBag.setTextColor(ContextCompat.getColor(mContext, R.color.text_gray));
                break;
            case R.id.textBag:
                viewpager.setCurrentItem(1);
                textGift.setTextColor(ContextCompat.getColor(mContext, R.color.text_gray));
                textBag.setTextColor(ContextCompat.getColor(mContext, R.color.btn_sure_forbidden));
                break;
            case R.id.charge:
                if (mListener != null) {
                    mListener.chargr();
                }
                break;
            case R.id.other_img:
                countView.setVisibility(View.VISIBLE);
                if(!TextUtils.isEmpty(editText.getText())){
                int num=Integer.valueOf(editText.getText().toString());
                if(num==1){
                    changeTextColor(yixinyiyi);
                }else if(num==10){
                    changeTextColor(shiquanshimei);
                }else if(num==66){
                    changeTextColor(qiqieshunli);
                }
                else if(num==188){
                    changeTextColor(yaobaobao);
                }
                else if(num==520){
                    changeTextColor(woaini);
                }
                else if(num==1314){
                    changeTextColor(yishengyishi);
                }else{
                    changeTextColor(otherCount);
                }
                }
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

    public interface GiftDialogListener {
        void sendGift(int id, int count, boolean isBag);

        void chargr();

        void showNumDialog();
    }

    private GiftDialogListener mListener;

    public void setGiftDialogListener(GiftDialogListener listener) {
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isPc) {
            setContentView(R.layout.gift_dialog_new_pc_view);
        } else {
            setContentView(R.layout.gift_dialog_new_view);
        }
        ButterKnife.bind(this);
        initView();
        initListener();
    }

    public void setEditText(int pos){
        if(editText!=null){
            editText.setText(pos+"");
        }
    }

    private void initListener() {
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPos == 0){
                    if (giftView.getSelectGiftID() == -1){
                        return;
                    }
                }else{
                    if (bagView.getSelectGiftID() == -1){
                        return;
                    }
                }


                if (!TextUtils.isEmpty(editText.getText())) {
                    sendView.setVisibility(View.VISIBLE);
                    countView.setVisibility(View.VISIBLE);
                    if (currentPos == 0) {
                        if (mListener != null) {
                            if (giftView.getSelectGiftID() != -1) {
                                mListener.sendGift(giftView.getSelectGiftID(), Integer.valueOf(editText.getText().toString()),false);
                            }
                        }

                    } else {
                        if (mListener != null)
                            if (bagView.getSelectGiftID() != -1) {
                                mListener.sendGift(bagView.getSelectGiftID(), Integer.valueOf(editText.getText().toString()),true);
                            }
                    }
                    countView.setVisibility(View.INVISIBLE);
                }
            }
        });

        sendView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(countView.getVisibility()== View.VISIBLE){
                    countView.setVisibility(View.GONE);
                }else {
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

    public void setData(List<SysGiftNewBean> datas) {
        if (bagView != null) {
            bagView.setDatas(datas);
        }
    }

    public void setYinCoin(){
        try {
            String yhbCountText = "";
            long yhbCount = SharePreferenceUtil.getSessionLong(mContext,"PREF_KEY_YHB");
            if (yhbCount >= 10000) {
                yhbCountText = new BigDecimal((double) yhbCount / 10000).setScale(2, BigDecimal.ROUND_DOWN) + "万";
            } else {
                yhbCountText = yhbCount + "";
            }
            yinCoin.setText(yhbCountText);
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    public void setHbCoin(){
        try {
            String hbCountText = "";
            long hbCount = SharePreferenceUtil.getSessionLong(mContext,"PREF_KEY_HB");
            if (hbCount >= 10000) {
                hbCountText = new BigDecimal((double) hbCount / 10000).setScale(2, BigDecimal.ROUND_DOWN) + "万";
            } else {
                hbCountText = hbCount + "";
            }
            hbCoin.setText(hbCountText);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void initView() {
        List<SysGiftNewBean> sysGiftBeenNew = new ArrayList<>();
        for (SysGiftNewBean item : FeihuZhiboApplication.getApplication().mDataManager.getLiveGiftList()) {
            if (!SysdataHelper.isGiftVisual(item.getId(), FeihuZhiboApplication.getApplication().mDataManager.getSysGoodsNewBean())) {

            } else {
                sysGiftBeenNew.add(item);
            }
        }

        giftView = new GiftView(mContext, sysGiftBeenNew, false);
        bagView = new GiftView(mContext, SysdataHelper.getbagBeanList(
                FeihuZhiboApplication.getApplication().mDataManager.getSysGiftNew(),
                sysbagBeanList), true);
        mViewList.add(giftView.getView());
        mViewList.add(bagView.getView());
        scTv.setSelected(true);
        myPagerAdapter=new MyPagerAdapter(mViewList);
        viewpager.setAdapter(myPagerAdapter);
        viewpager.setCurrentItem(0);
        viewpager.setOffscreenPageLimit(2);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewpager.setCurrentItem(position);
                currentPos = position;
                if (position == 0) {
                    textGift.setTextColor(ContextCompat.getColor(mContext, R.color.btn_sure_forbidden));
                    textBag.setTextColor(ContextCompat.getColor(mContext, R.color.noticetextcolor));
                } else {
                    textGift.setTextColor(ContextCompat.getColor(mContext, R.color.noticetextcolor));
                    textBag.setTextColor(ContextCompat.getColor(mContext, R.color.btn_sure_forbidden));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        String yhbCountText = "";
        long yhbCount = SharePreferenceUtil.getSessionLong(mContext,"PREF_KEY_YHB");
        if (yhbCount >= 10000) {
            yhbCountText = new BigDecimal((double) yhbCount / 10000).setScale(2, BigDecimal.ROUND_DOWN) + "万";
        } else {
            yhbCountText = yhbCount + "";
        }
        yinCoin.setText(yhbCountText);
        String hbCountText = "";
        long hbCount = SharePreferenceUtil.getSessionLong(mContext,"PREF_KEY_HB");
        if (hbCount >= 10000) {
            hbCountText = new BigDecimal((double) hbCount / 10000).setScale(2, BigDecimal.ROUND_DOWN) + "万";
        } else {
            hbCountText = hbCount + "";
        }
        hbCoin.setText(hbCountText);
        giftView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                SysGiftNewBean gift = FeihuZhiboApplication.getApplication().mDataManager.getGiftBeanByID(String.valueOf(position));
                if(gift!=null) {
                    scTv.setText(gift.getTips());
                    scTv.setSelected(true);
                }
                if (gift != null) {
                    if (gift.getIsAnimation().equals("1")) {
                        countView.setVisibility(View.GONE);
                        sendView.setVisibility(View.VISIBLE);
                        otherImg.setVisibility(View.GONE);
                        editText.setText("1");
                        sendView.setEnabled(false);
                    }else{
//                        editText.setText("1");
                        otherImg.setVisibility(View.VISIBLE);
                        sendView.setVisibility(View.VISIBLE);
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
                    scTv.setSelected(true);
                }
                if (gift != null) {
                    if (gift.getIsAnimation().equals("1")) {
                        countView.setVisibility(View.GONE);
                        sendView.setVisibility(View.VISIBLE);
                        otherImg.setVisibility(View.GONE);
                        editText.setText("1");
                        sendView.setEnabled(false);
                    }else{
//                        editText.setText("1");
                        otherImg.setVisibility(View.VISIBLE);
                        sendView.setVisibility(View.VISIBLE);
                        sendView.setEnabled(true);
                    }
                }
            }
        });
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
