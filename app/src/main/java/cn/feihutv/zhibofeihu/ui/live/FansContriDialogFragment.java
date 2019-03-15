package cn.feihutv.zhibofeihu.ui.live;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SlidingTabLayout;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.ui.widget.TCActivityTitle;

/**
 * Created by huanghao on 2017/11/11.
 */

public class FansContriDialogFragment extends DialogFragment {

    @BindView(R.id.btn_back)
    TCActivityTitle btnBack;
    @BindView(R.id.fans_contri_tabs)
    SlidingTabLayout fansContriTabs;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    private OnStrClickListener onItemClickListener;
    private String[] titles = {"日榜", "月榜","总榜", "守护榜"};
    private String userId;
    private boolean isWatchLive;
    public static FansContriDialogFragment newInstance(String userId,boolean isWatchLive) {
        Bundle args = new Bundle();
        FansContriDialogFragment fragment = new FansContriDialogFragment();
        fragment.userId = userId;
        fragment.isWatchLive = isWatchLive;
        fragment.setArguments(args);
        return fragment;
    }


    public void setOnItemClickListener(OnStrClickListener listener) {
        onItemClickListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStyle(STYLE_NORMAL, R.style.cb_dialog);
        setStyle(STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fans_contri_dialogfragment, container);
        ButterKnife.bind(this, view);
        initView(view);
        return view;
    }

    private void initView(View view) {
        btnBack.setReturnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return FansRankFragment.newInstance(1);
                    case 1:
                        return FansRankFragment.newInstance(2);
                    case 2:
                        return FansRankFragment.newInstance(3);
                    case 3:
                        return GuardFragment.newInstance(userId,isWatchLive);
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
        fansContriTabs.setViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                    case 1:
                    case 2:
                        MobclickAgent.onEvent(getContext(), "10050");
                        break;
                    case 3:
                        MobclickAgent.onEvent(getContext(), "10051");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
