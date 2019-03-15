package cn.feihutv.zhibofeihu.ui.widget.dialog;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.ui.live.OnStrClickListener;
import cn.feihutv.zhibofeihu.ui.live.RankContriFragment;
import cn.feihutv.zhibofeihu.ui.live.UserOnlineFragment;

/**
 * Created by Administrator on 2017/3/14.
 */

public class ContributionListDialogFragment extends DialogFragment implements View.OnClickListener {


    private TextView con_back;
    private TabLayout slidingTabs;
    private ViewPager viewpager;
    private TabAdaper tabAdaper;
    private OnStrClickListener mlistener;
    public void setOnStrClickListener(OnStrClickListener listener){
        mlistener=listener;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_contributionlist_tab, container);
        initView(view);
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
//        Dialog dialog = getDialog();
//        if (dialog != null) {
//            DisplayMetrics dm = new DisplayMetrics();
//            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//            dialog.getWindow().setLayout((int) (dm.widthPixels),(int)(dm.heightPixels*0.64));
//            dialog.getWindow().setGravity(Gravity.BOTTOM);
//            dialog.setCanceledOnTouchOutside(true);
//        }
    }

    private void initView(View view) {
        slidingTabs = (TabLayout) view.findViewById(R.id.sliding_tabs);
        viewpager = (ViewPager) view.findViewById(R.id.viewpager);
        con_back = (TextView) view.findViewById(R.id.con_back);
        con_back.setOnClickListener(this);
        slidingTabs.addTab(slidingTabs.newTab().setText(getString(R.string.day_con)));

        slidingTabs.addTab(slidingTabs.newTab().setText(getString(R.string.month_con)));

        slidingTabs.addTab(slidingTabs.newTab().setText(getString(R.string.sum_con)));
        slidingTabs.addTab(slidingTabs.newTab().setText(getString(R.string.online_user)));
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
        tabAdaper = new TabAdaper(getChildFragmentManager());
        viewpager.setAdapter(tabAdaper);
        viewpager.setCurrentItem(0);
        viewpager.setOffscreenPageLimit(3);
        slidingTabs.setupWithViewPager(viewpager);


        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        con_back.setOnClickListener(this);
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.con_back:
                dismiss();
                break;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.cb_dialog);
    }


    class TabAdaper extends FragmentPagerAdapter {

        // 标题数组
        String[] titles = {"贡献日榜", "贡献月榜", "贡献总榜","在线用户"};
        private List<Fragment> mFragments=new ArrayList<>();
        public TabAdaper(FragmentManager fm) {
            super(fm);
            RankContriFragment r1= RankContriFragment.getInstance(getContext().getString(R.string.day_con));
            RankContriFragment r2=RankContriFragment.getInstance(getContext().getString(R.string.month_con));
            RankContriFragment r3=RankContriFragment.getInstance(getContext().getString(R.string.sum_con));
            UserOnlineFragment userOnlineFragment=UserOnlineFragment.getInstance();
            r1.setOnItemClickListener(new OnStrClickListener() {
                @Override
                public void onItemClick(String userId) {
                    if(mlistener!=null){
                        mlistener.onItemClick(userId);
                    }
                }
            });
            r2.setOnItemClickListener(new OnStrClickListener() {
                @Override
                public void onItemClick(String userId) {
                    if(mlistener!=null){
                        mlistener.onItemClick(userId);
                    }
                }
            });
            r3.setOnItemClickListener(new OnStrClickListener() {
                @Override
                public void onItemClick(String userId) {
                    if(mlistener!=null){
                        mlistener.onItemClick(userId);
                    }
                }
            });
            userOnlineFragment.setUserOnlineFragmentListener(new UserOnlineFragment.UserOnlineFragmentListener() {
                @Override
                public void onitemClick(String userId) {
                    if(mlistener!=null){
                        mlistener.onItemClick(userId);
                    }
                }
            });
            mFragments.add(r1);
            mFragments.add(r2);
            mFragments.add(r3);
            mFragments.add(userOnlineFragment);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
