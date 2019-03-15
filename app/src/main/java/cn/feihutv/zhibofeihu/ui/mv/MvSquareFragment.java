package cn.feihutv.zhibofeihu.ui.mv;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.CancelMVNoticeResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.QueryMVNoticeResponse;
import cn.feihutv.zhibofeihu.ui.base.BaseFragment;
import cn.feihutv.zhibofeihu.ui.mv.demand.MyDemandActivity;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/
 *     desc   : mv 广场
 *     version: 1.0
 * </pre>
 */
public class MvSquareFragment extends BaseFragment implements MvSquareMvpView {

    @Inject
    MvSquareMvpPresenter<MvSquareMvpView> mPresenter;

    @BindView(R.id.sliding_tabs)
    SlidingTabLayout mTabLayout;

    @BindView(R.id.vp_contribute)
    ViewPager mViewPager;

    @BindView(R.id.mv_center)
    FrameLayout mv_center;

    @BindView(R.id.mv_center_red_msg)
    ImageView  mv_center_red_msg;//mv中心 红点

    private boolean haveMsg=false;//是否有消息


    private String[] titles = {"MV广场", "需求广场"};

    private List<Fragment>  mMvPageFragmentList;

    public static MvSquareFragment newInstance() {
        Bundle args = new Bundle();
        MvSquareFragment fragment = new MvSquareFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mv_square;
    }


    @Override
    protected void initWidget(View view) {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this, view));

        //Presenter调用初始化
        mPresenter.onAttach(MvSquareFragment.this);

        mMvPageFragmentList=new ArrayList<>();
        mMvPageFragmentList.add(MvVideoListFragment.newInstance());
        mMvPageFragmentList.add(DemandSquareFragment.newInstance());
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {

                return mMvPageFragmentList.get(position);
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

        mv_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击了需求中心 则取消显示msg 红点
                if(!TCUtils.checkGuest(getActivity())) {
                    if (haveMsg) {
                        mPresenter.cancelMVNotice();
                        haveMsg = false;
                    }
                    goActivity(MyDemandActivity.class);
                }
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                JZVideoPlayerStandard.releaseAllVideos();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @Override
    public void onQueryMVNoticeResp(QueryMVNoticeResponse response) {

        if(response.getCode()==0){
            if(response.getCount()>0){
                haveMsg=true;
                mv_center_red_msg.setVisibility(View.VISIBLE);
            }else {
                haveMsg=false;
                mv_center_red_msg.setVisibility(View.GONE);
            }
        }

    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }



    @Override
    public void onResume() {
        super.onResume();
        queryMVNotice();
    }


    public void queryMVNotice(){

        if(mPresenter!=null){
            if(mPresenter.isGuestUser()){
                //是游客不请求数据
                return;
            }
        }

        if (isUser() && !haveMsg) {//是普通用户且没有消息才进行查询
            if (mPresenter != null) {
                mPresenter.queryMVNotice();
            }
        }

    }

    private boolean isUser(){//是否为普通用户
        boolean user=false;
        if (mPresenter != null) {
            int status = mPresenter.getUserData().getCertifiStatus();
            if (1 != status) {
                user = true;//普通用户
            }
        }
        return user;
    }


    @Override
    public void onCancelMVNoticeResp(CancelMVNoticeResponse response) {

    }

    @Override
    protected void lazyLoad() {

    }


    @Override
    public void onDestroyView() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroyView();
    }


}
