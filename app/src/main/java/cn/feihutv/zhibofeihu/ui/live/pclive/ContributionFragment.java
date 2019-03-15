package cn.feihutv.zhibofeihu.ui.live.pclive;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.ui.base.BaseFragment;
import cn.feihutv.zhibofeihu.ui.widget.CustomsChildViewPager;

/**
 * Created by Administrator on 2017/3/9.
 */

public class ContributionFragment extends BaseFragment {


    @BindView(R.id.view_pager)
    CustomsChildViewPager viewPager;
    @BindView(R.id.txt_day)
    TextView txtDay;
    @BindView(R.id.txt_month)
    TextView txtMonth;
    @BindView(R.id.txt_sum)
    TextView txtSum;
    @BindView(R.id.txt_guard)
    TextView txtGuard;
    Unbinder unbinder;
    private String userId;
    private String[] title={"日榜","月榜","总榜","守护榜"};

    public static ContributionFragment getInstance(String userId) {
        ContributionFragment fragment = new ContributionFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        fragment.userId=userId;
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_contribute;
    }

    @Override
    protected void initWidget(View view) {
        setUnBinder(ButterKnife.bind(this, view));
        txtDay.setSelected(true);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return  PcFansRankFragment.newInstance(1);
                    case 1:
                        return  PcFansRankFragment.newInstance(2);
                    case 2:
                        return  PcFansRankFragment.newInstance(3);
                    case 3:
                        return  PcGuardFragment.newInstance(userId);
                    default :
                        break;
                }
                return null;
            }

            @Override
            public int getCount() {
                return title.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return title[position] ;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    txtDay.setSelected(true);
                    txtGuard.setSelected(false);
                    txtMonth.setSelected(false);
                    txtSum.setSelected(false);
                } else if (position == 1) {
                    txtDay.setSelected(false);
                    txtGuard.setSelected(false);
                    txtMonth.setSelected(true);
                    txtSum.setSelected(false);
                } else if (position == 2) {
                    txtDay.setSelected(false);
                    txtGuard.setSelected(false);
                    txtMonth.setSelected(false);
                    txtSum.setSelected(true);
                } else if (position == 3) {
                    txtDay.setSelected(false);
                    txtGuard.setSelected(true);
                    txtMonth.setSelected(false);
                    txtSum.setSelected(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    protected void lazyLoad() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.txt_day, R.id.txt_month, R.id.txt_sum, R.id.txt_guard})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_day:
                viewPager.setCurrentItem(0);
                txtDay.setSelected(true);
                txtGuard.setSelected(false);
                txtMonth.setSelected(false);
                txtSum.setSelected(false);
                break;
            case R.id.txt_month:
                viewPager.setCurrentItem(1);
                txtDay.setSelected(false);
                txtGuard.setSelected(false);
                txtMonth.setSelected(true);
                txtSum.setSelected(false);
                break;
            case R.id.txt_sum:
                viewPager.setCurrentItem(2);
                txtDay.setSelected(false);
                txtGuard.setSelected(false);
                txtMonth.setSelected(false);
                txtSum.setSelected(true);
                break;
            case R.id.txt_guard:
                viewPager.setCurrentItem(3);
                txtDay.setSelected(false);
                txtGuard.setSelected(true);
                txtMonth.setSelected(false);
                txtSum.setSelected(false);
                break;
        }
    }
}
