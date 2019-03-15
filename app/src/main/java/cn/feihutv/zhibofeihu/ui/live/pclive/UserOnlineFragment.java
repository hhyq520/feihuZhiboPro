package cn.feihutv.zhibofeihu.ui.live.pclive;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.ui.base.BaseFragment;
import cn.feihutv.zhibofeihu.ui.live.OnLineUserFragment;
import cn.feihutv.zhibofeihu.ui.live.PCVipUserFragment;
import cn.feihutv.zhibofeihu.ui.live.VipUserFragment;
import cn.feihutv.zhibofeihu.ui.widget.CustomsChildViewPager;

/**
 * Created by Administrator on 2017/3/9.
 */

public class UserOnlineFragment extends BaseFragment {


    @BindView(R.id.view_pager)
    CustomsChildViewPager viewPager;
    @BindView(R.id.txt_day)
    TextView txtDay;
    @BindView(R.id.txt_month)
    TextView txtMonth;
    Unbinder unbinder;
    private String userId;
    private String[] title={"贵宾席","在线用户"};

    public static UserOnlineFragment getInstance() {
        UserOnlineFragment fragment = new UserOnlineFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pconline;
    }

    @Override
    protected void initWidget(View view) {
        setUnBinder(ButterKnife.bind(this, view));
        txtDay.setSelected(true);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return VipUserFragment.newInstance(false);
                    case 1:
                        return OnLineUserFragment.newInstance();
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
                    txtMonth.setSelected(false);
                } else if (position == 1) {
                    txtDay.setSelected(false);
                    txtMonth.setSelected(true);
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
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.txt_day, R.id.txt_month})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_day:
                viewPager.setCurrentItem(0);
                txtDay.setSelected(true);
                txtMonth.setSelected(false);
                break;
            case R.id.txt_month:
                viewPager.setCurrentItem(1);
                txtDay.setSelected(false);
                txtMonth.setSelected(true);
                break;
        }
    }
}
