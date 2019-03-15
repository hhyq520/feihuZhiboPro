package cn.feihutv.zhibofeihu.ui.me.vip;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/15 20:15
 *      desc   : 我的--vip---收赠记录
 *      version: 1.0
 * </pre>
 */

public class ReceAndSendRecordActivity extends BaseActivity {

    @BindView(R.id.tv_back)
    TextView tvBack;

    @BindView(R.id.sliding_tabs)
    SlidingTabLayout mSlidingTabLayout;

    @BindView(R.id.vp_vip)
    ViewPager mViewPager;

    private String[] titles = {"我的收获", "我的赠送"};

    @Override
    protected int getLayoutId() {
        return R.layout.activity_rece_send;
    }

    @Override
    protected void initWidget() {
        setUnBinder(ButterKnife.bind(this));

        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return MyReceiveFragment.newInstance();
                    case 1:
                        return MySendFragment.newInstance();
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

        mSlidingTabLayout.setViewPager(mViewPager);
    }

    @Override
    protected void eventOnClick() {
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
