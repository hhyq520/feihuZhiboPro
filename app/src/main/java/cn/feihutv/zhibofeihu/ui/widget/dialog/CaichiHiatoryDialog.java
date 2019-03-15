package cn.feihutv.zhibofeihu.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.ui.live.OnItemClickListener;
import cn.feihutv.zhibofeihu.ui.widget.CaichiView;
import cn.feihutv.zhibofeihu.ui.widget.MyCaichiView;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;

/**
 * Created by huanghao on 2017/6/16.
 */

public class CaichiHiatoryDialog extends Dialog {
    @BindView(R.id.t1)
    TextView t1;
    @BindView(R.id.t2)
    TextView t2;
    @BindView(R.id.t3)
    TextView t3;

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.gift_lin)
    LinearLayout giftLin;

    private Context mContext;
    private CaichiView view1;
    private CaichiView view2;
    private MyCaichiView view3;
    @BindView(R.id.my_lin)
    LinearLayout myLin;
    private List<View> views = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener mListener){
        onItemClickListener=mListener;
    }
    public CaichiHiatoryDialog(Context context) {
        super(context, R.style.floag_dialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caichi_history_dialog);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setTextSelect(1);
        view1 = new CaichiView(mContext, 0,false);
        view2 = new CaichiView(mContext, 20,false);
        view3 = new MyCaichiView(mContext);
        views.add(view1.getView());
        views.add(view2.getView());
        views.add(view3.getView());
        viewpager.setAdapter(new MyPagerAdapter(views));
        viewpager.setCurrentItem(0);
        viewpager.setOffscreenPageLimit(4);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.e("print", "onPageSelected: ---->" + position);
                viewpager.setCurrentItem(position);
                if (position%3 == 0) {
                    setTextSelect(1);
                    myLin.setVisibility(View.GONE);
                    giftLin.setVisibility(View.VISIBLE);
                } else if (position%3 == 1) {
                    setTextSelect(2);
                    myLin.setVisibility(View.GONE);
                    giftLin.setVisibility(View.VISIBLE);
                }else if (position%3 == 2) {
                    if(SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")){
                        if(onItemClickListener!=null){
                            onItemClickListener.onItemClick(0);
                            viewpager.setCurrentItem(1);
                            myLin.setVisibility(View.GONE);
                            giftLin.setVisibility(View.VISIBLE);
                            setTextSelect(2);
                        }
                    }else{
                        setTextSelect(3);
                        myLin.setVisibility(View.VISIBLE);
                        giftLin.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.t1, R.id.t2, R.id.t3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.t1:
                setTextSelect(1);
                myLin.setVisibility(View.GONE);
                viewpager.setCurrentItem(0);
                giftLin.setVisibility(View.VISIBLE);
                break;
            case R.id.t2:
                setTextSelect(2);
                viewpager.setCurrentItem(1);
                myLin.setVisibility(View.GONE);
                giftLin.setVisibility(View.VISIBLE);
                break;
            case R.id.t3:
                myLin.setVisibility(View.VISIBLE);
                giftLin.setVisibility(View.GONE);
                setTextSelect(3);
                viewpager.setCurrentItem(2);
//                if(SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")){
//                    if(onItemClickListener!=null){
//                        onItemClickListener.onItemClick(0);
//                        viewpager.setCurrentItem(1);
//                        setTextSelect(2);
//                    }
//                }else{
//
//                }
                break;
        }
    }



    private void setTextSelect(int pos) {
        if (pos == 1) {
            t1.setSelected(true);
        } else {
            t1.setSelected(false);
        }
        if (pos == 2) {
            t2.setSelected(true);
        } else {
            t2.setSelected(false);
        }
        if (pos == 3) {
            t3.setSelected(true);
        } else {
            t3.setSelected(false);
        }


    }

    class MyPagerAdapter extends PagerAdapter {
        private List<View> mViewList;
        // 标题数组
        String[] titles = {"礼物", "包裹","我的"};

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
