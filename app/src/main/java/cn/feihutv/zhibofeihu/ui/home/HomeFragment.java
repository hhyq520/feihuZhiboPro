package cn.feihutv.zhibofeihu.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.androidkun.xtablayout.XTabLayout;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.db.model.SysLaunchTagBean;
import cn.feihutv.zhibofeihu.di.component.ActivityComponent;
import cn.feihutv.zhibofeihu.ui.base.BaseFragment;
import cn.feihutv.zhibofeihu.ui.home.adapter.TabAdapter;
import cn.feihutv.zhibofeihu.ui.home.history.HistoryActivity;
import cn.feihutv.zhibofeihu.ui.home.search.SearchUserActivity;
import cn.feihutv.zhibofeihu.ui.home.signin.SignInDialog;

/**
 * <pre>
 *     @author : liwen.chen
 *     time   : 2017/
 *     desc   : 界面绘制
 *     version: 1.0
 * </pre>
 */
public class HomeFragment extends BaseFragment implements HomeMvpView {

    @Inject
    HomeMvpPresenter<HomeMvpView> mPresenter;

    @BindView(R.id.sliding_tabs)
    XTabLayout tabLayout;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.find)
    ImageView ivFind;

    @BindView(R.id.history)
    ImageView ivHistory;
    Unbinder unbinder;

    private TabAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.home_fragment;
    }


    @Override
    protected void initWidget(View view) {

        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            mPresenter.onAttach(this);
        }
    }


    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.getLaunchTag();
    }

    @Override
    public void onDestroyView() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void loadLaunchTag(final List<SysLaunchTagBean> sysLaunchTagBeen) {
        mAdapter = new TabAdapter(getFragmentManager(), sysLaunchTagBeen);
        viewPager.setAdapter(mAdapter);

        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < sysLaunchTagBeen.size(); i++) {
            if (sysLaunchTagBeen.get(i).getDefaultTag() == 1) {
                viewPager.setCurrentItem(i);
                return;
            }
        }

        viewPager.setOffscreenPageLimit(2);

//        tabLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                //设置最小宽度，使其可以在滑动一部分距离
//                ViewGroup slidingTabStrip = (ViewGroup) tabLayout.getChildAt(0);
//                slidingTabStrip.setMinimumWidth(slidingTabStrip.getMeasuredWidth() + ivHistory.getMeasuredWidth());
//            }
//        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick({R.id.find, R.id.history})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.find:
                MobclickAgent.onEvent(getContext(), "10009");
                startActivity(new Intent(getContext(), SearchUserActivity.class));
                break;
            case R.id.history:
                MobclickAgent.onEvent(getContext(), "10014");
                startActivity(new Intent(getContext(), HistoryActivity.class));
                break;
            default:
                break;
        }
    }
}
