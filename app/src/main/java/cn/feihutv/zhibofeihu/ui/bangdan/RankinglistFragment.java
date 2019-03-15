package cn.feihutv.zhibofeihu.ui.bangdan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import com.flyco.tablayout.SlidingTabLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.ui.base.BaseFragment;

/**
 * Created by chenliwen on 2017/4/6 20:13.
 * 佛祖保佑，永无BUG
 * <p>
 * 排行榜
 */

public class RankinglistFragment extends BaseFragment {

    @BindView(R.id.sliding_tabs)
    SlidingTabLayout mTabLayout;

    @BindView(R.id.vp_contribute)
    ViewPager mViewPager;

    private String[] titles = {"日榜", "月榜", "总榜"};

    public static RankinglistFragment getInstance() {
        RankinglistFragment fragment = new RankinglistFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_rankinglist;
    }

    @Override
    protected void initWidget(View view) {

        setUnBinder(ButterKnife.bind(this, view));

        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return RankingFragment.newInstance(String.valueOf(position + 1));
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
    }

    @Override
    protected void lazyLoad() {

    }
}
