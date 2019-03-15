package cn.feihutv.zhibofeihu.ui.me.guard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/7 14:51
 *      desc   : 我的--守护
 *      version: 1.0
 * </pre>
 */

public class GuardActivity extends BaseActivity {

    @BindView(R.id.sliding_tabs)
    SlidingTabLayout mTabLayout;

    @BindView(R.id.vp_contribute)
    ViewPager mViewPager;

    private String[] titles;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_guard;
    }

    @Override
    protected void initWidget() {
        setUnBinder(ButterKnife.bind(this));

        Intent intent = getIntent();
        final String userId = intent.getStringExtra("userId");
        titles = intent.getStringArrayExtra("titles");

        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return GuardsFragment.newInstance(userId);
                    case 1:
                        return GuardListFragment.newInstance(userId);
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
    }


    @OnClick(R.id.tv_back)
    public void onViewClicked() {
        finish();
    }
}
