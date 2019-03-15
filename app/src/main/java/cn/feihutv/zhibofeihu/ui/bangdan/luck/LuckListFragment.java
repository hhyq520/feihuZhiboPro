package cn.feihutv.zhibofeihu.ui.bangdan.luck;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.rxbus.RxBus;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.rxbus.bangdan.LuckPush;
import cn.feihutv.zhibofeihu.ui.base.BaseFragment;

/**
 * <pre>
 *     @author : liwen.chen
 *     time   : 2017/
 *     desc   : 幸运榜
 *     version: 1.0
 * </pre>
 */

public class LuckListFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.luck_tabs)
    SlidingTabLayout mTabLayout;

    @BindView(R.id.vp_luck)
    ViewPager mViewPager;

    @BindView(R.id.iv_more)
    ImageView mView;

    private String[] titles = {"幸运记录", "总榜"};

    private int currentItem = 0;

    private int rankType1 = 1;

    private int rankType2 = 1;

    private PopupWindow mPopWindow;


    public static LuckListFragment getInstance() {
        LuckListFragment fragment = new LuckListFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_luckylist;
    }

    @Override
    protected void initWidget(View view) {

        setUnBinder(ButterKnife.bind(this, view));

        mViewPager.setCurrentItem(0);
        mViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return DayListFragment.newInstance();
                    case 1:
                        return TotalListFragment.newInstance();
                    default:
                        break;
                }
                return null;
            }

            @Override
            public int getCount() {
                return titles.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        });

        mTabLayout.setViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentItem == 0) {
                    showPopupWindow(currentItem, rankType1);
                } else if (currentItem == 1) {
                    showPopupWindow(currentItem, rankType2);
                }
            }
        });
    }


    private void showPopupWindow(int currentItem, int rankType) {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_bangdan_choose, null);
        mPopWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        TextView tv1 = (TextView) contentView.findViewById(R.id.pop_day);
        TextView tv2 = (TextView) contentView.findViewById(R.id.pop_month);
        TextView tv3 = (TextView) contentView.findViewById(R.id.pop_total);
        View view = contentView.findViewById(R.id.view);
        if (currentItem == 0) {
            // 幸运礼物
            tv1.setText("幸运礼物");
            tv2.setText("飞虎流星");
            tv3.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        } else if (currentItem == 1) {
            // 总榜
            tv1.setText("幸运用户");
            tv2.setText("幸运主播");
            tv3.setText("飞虎流星");
            tv3.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);
        }
        if (rankType == 2) {
            tv1.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
            tv2.setTextColor(ContextCompat.getColor(getContext(), R.color.btn_sure_forbidden));
            tv3.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
        } else if (rankType == 3) {
            tv1.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
            tv2.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
            tv3.setTextColor(ContextCompat.getColor(getContext(), R.color.btn_sure_forbidden));
        } else {
            tv1.setTextColor(ContextCompat.getColor(getContext(), R.color.btn_sure_forbidden));
            tv2.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
            tv3.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
        }
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopWindow.setOutsideTouchable(true);
        mPopWindow.showAsDropDown(mView, 0, 0);
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pop_day:
                mPopWindow.dismiss();
                if (currentItem == 0) {
                    rankType1 = 1;
                    RxBus.get().send(RxBusCode.RX_BUS_CODE_POSITION_CURRENTITEM, new LuckPush
                            (currentItem, rankType1));
                } else {
                    rankType2 = 1;
                    RxBus.get().send(RxBusCode.RX_BUS_CODE_POSITION_CURRENTITEM, new LuckPush
                            (currentItem, rankType2));
                }
                break;
            case R.id.pop_month:
                mPopWindow.dismiss();
                if (currentItem == 0) {
                    rankType1 = 2;
                    RxBus.get().send(RxBusCode.RX_BUS_CODE_POSITION_CURRENTITEM, new LuckPush
                            (currentItem, rankType1));
                } else {
                    rankType2 = 2;
                    RxBus.get().send(RxBusCode.RX_BUS_CODE_POSITION_CURRENTITEM, new LuckPush
                            (currentItem, rankType2));
                }
                break;
            case R.id.pop_total:
                mPopWindow.dismiss();
                if (currentItem == 0) {
                    rankType1 = 3;
                    RxBus.get().send(RxBusCode.RX_BUS_CODE_POSITION_CURRENTITEM, new LuckPush
                            (currentItem, rankType1));
                } else {
                    rankType2 = 3;
                    RxBus.get().send(RxBusCode.RX_BUS_CODE_POSITION_CURRENTITEM, new LuckPush
                            (currentItem, rankType2));
                }
                break;
            default:
                break;
        }
    }
}
