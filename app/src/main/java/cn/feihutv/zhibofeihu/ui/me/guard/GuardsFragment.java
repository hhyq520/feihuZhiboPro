package cn.feihutv.zhibofeihu.ui.me.guard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView;

import java.util.ArrayList;
import java.util.List;

import cn.feihutv.zhibofeihu.R;

import cn.feihutv.zhibofeihu.data.network.http.model.me.GetUserGuardsResponse;
import cn.feihutv.zhibofeihu.ui.bangdan.adapter.GuardsAdapter;
import cn.feihutv.zhibofeihu.ui.base.BaseFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.ui.me.dynamic.OthersCommunityActivity;
import cn.feihutv.zhibofeihu.ui.widget.StatusView;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.ListViewDecoration;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.feihutv.zhibofeihu.utils.glideutil.GlideApp;

/**
 * <pre>
 *     author : liwen.chen
 *     time   : 2017/
 *     desc   : 我的守护
 *     version: 1.0
 * </pre>
 */
public class GuardsFragment extends BaseFragment implements GuardsMvpView, SwipeRefreshLayout.OnRefreshListener, StatusView.ClickRefreshListener, View.OnClickListener {

    @Inject
    GuardsMvpPresenter<GuardsMvpView> mPresenter;

    @BindView(R.id.bangdan_swip)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.bangdan_rv)
    RecyclerView mRecyclerView;

    @BindView(R.id.status_view)
    StatusView mStatusView;

    private boolean isFirstLoad = true;

    private boolean isPrepared;//当前页面是否加载

    private boolean isLoadEnd = false;

    private boolean isRefresh;

    private int offset = 0;

    private View mHeadView;

    private String userId;

    private ImageView mPolygonImageView1, mPolygonImageView2, mPolygonImageView3;
    private ImageView ivLevel1, ivLevel2, ivLevel3;
    private TextView tvNick1, tvNick2, tvNick3, tvCount1, tvCount2, tvCount3;

    private RelativeLayout mRelativeLayout1, mRelativeLayout2, mRelativeLayout3;

    private GuardsAdapter mAdapter;

    private List<GetUserGuardsResponse.GetUserGuardsResponseData> mGetUserGuardsResponseDatas = new ArrayList<>();
    private List<GetUserGuardsResponse.GetUserGuardsResponseData> mAdapterDatas = new ArrayList<>();

    public static GuardsFragment newInstance(String userId) {
        Bundle args = new Bundle();
        GuardsFragment fragment = new GuardsFragment();
        args.putString("userId", userId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_guards;
    }


    @Override
    protected void initWidget(View view) {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this, view));

        //Presenter调用初始化
        mPresenter.onAttach(GuardsFragment.this);

        isPrepared = true;

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //添加分隔线
        mRecyclerView.addItemDecoration(new ListViewDecoration());
        mRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        mRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);

        mRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

        mAdapter = new GuardsAdapter(mAdapterDatas);
        mRecyclerView.setAdapter(mAdapter);

        mRefreshLayout.setOnRefreshListener(this);

        mStatusView.setClickRefreshListener(this);

        mHeadView = LayoutInflater.from(getActivity()).inflate(R.layout.view_head_me_guards, null);
        mPolygonImageView1 = (ImageView) mHeadView.findViewById(R.id.polygonImageView_1);
        mPolygonImageView2 = (ImageView) mHeadView.findViewById(R.id.polygonImageView_2);
        mPolygonImageView3 = (ImageView) mHeadView.findViewById(R.id.polygonImageView_3);
        tvNick1 = (TextView) mHeadView.findViewById(R.id.tv_nick_1);
        tvNick2 = (TextView) mHeadView.findViewById(R.id.tv_nick_2);
        tvNick3 = (TextView) mHeadView.findViewById(R.id.tv_nick_3);
        ivLevel1 = (ImageView) mHeadView.findViewById(R.id.iv_level_1);
        ivLevel2 = (ImageView) mHeadView.findViewById(R.id.iv_level_2);
        ivLevel3 = (ImageView) mHeadView.findViewById(R.id.iv_level_3);
        tvCount1 = (TextView) mHeadView.findViewById(R.id.tv_count1);
        tvCount2 = (TextView) mHeadView.findViewById(R.id.tv_count2);
        tvCount3 = (TextView) mHeadView.findViewById(R.id.tv_count3);

        mPolygonImageView1.setOnClickListener(this);
        mPolygonImageView2.setOnClickListener(this);
        mPolygonImageView3.setOnClickListener(this);

        mRelativeLayout1 = (RelativeLayout) mHeadView.findViewById(R.id.rl_1);
        mRelativeLayout2 = (RelativeLayout) mHeadView.findViewById(R.id.rl_2);
        mRelativeLayout3 = (RelativeLayout) mHeadView.findViewById(R.id.rl_3);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                goOthersCommunite(mAdapterDatas.get(position).getUserId());
            }
        });

        mAdapter.setLoadMoreView(new SimpleLoadMoreView());

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (!isLoadEnd) {
                    isLoadEnd = true;
                    mPresenter.getUserGuards(userId, offset);
                }
            }
        }, mRecyclerView);

        mAdapter.addHeaderView(mHeadView);
    }

    @Override
    protected void lazyLoad() {
        if (isPrepared && isVisible && mGetUserGuardsResponseDatas.size() == 0) {
            Bundle bundle = getArguments();
            userId = bundle.getString("userId");
            isPrepared = false;
            mPresenter.getUserGuards(userId, offset);
        }
    }


    @Override
    public void onDestroyView() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroyView();
    }


    @Override
    public void showNoData() {
        if (isFirstLoad) {
            mRefreshLayout.setVisibility(View.GONE);
            mStatusView.showNoDataView();
            mStatusView.setTvNoData("暂无守护者，多去开播涨涨粉吧~");
            hideLoading();
        }
        if (isLoadEnd) {
            mAdapter.loadMoreEnd();
        }
    }

    @Override
    public void showContent() {
        mRefreshLayout.setVisibility(View.VISIBLE);
        mStatusView.setVisibility(View.GONE);
        hideLoading();

        if (isLoadEnd) {
            mAdapter.loadMoreComplete();
            isLoadEnd = false;
        }
    }

    @Override
    public void showErrorView() {
        isRefresh = false;
        mRefreshLayout.setRefreshing(false);
        if (isFirstLoad) {
            mRefreshLayout.setVisibility(View.GONE);
            mStatusView.showErrorView();
            hideLoading();
        }
        if (isLoadEnd) {
            mAdapter.loadMoreFail();
            isLoadEnd = false;
        }
    }

    @Override
    public void getDatas(List<GetUserGuardsResponse.GetUserGuardsResponseData> getUserGuardsResponseDatas) {
        if (isRefresh) {
            if (isNetworkConnected()) {
                mGetUserGuardsResponseDatas.clear();
                mAdapterDatas.clear();
                mRefreshLayout.setRefreshing(false);
                isRefresh = false;
            }
        }
        isFirstLoad = false;
        mGetUserGuardsResponseDatas.addAll(getUserGuardsResponseDatas);
        offset = mGetUserGuardsResponseDatas.size();
        if (mGetUserGuardsResponseDatas.size() >= 3) {
            mRelativeLayout1.setVisibility(View.VISIBLE);
            mRelativeLayout2.setVisibility(View.VISIBLE);
            mRelativeLayout3.setVisibility(View.VISIBLE);
            GlideApp.loadImg(getContext(), mGetUserGuardsResponseDatas.get(0).getAvatar(), DiskCacheStrategy.ALL, mPolygonImageView1);
            GlideApp.loadImg(getContext(), mGetUserGuardsResponseDatas.get(1).getAvatar(), DiskCacheStrategy.ALL, mPolygonImageView2);
            GlideApp.loadImg(getContext(), getUserGuardsResponseDatas.get(2).getAvatar(), DiskCacheStrategy.ALL, mPolygonImageView3);
            tvNick1.setText(mGetUserGuardsResponseDatas.get(0).getNickname());
            tvNick2.setText(mGetUserGuardsResponseDatas.get(1).getNickname());
            tvNick3.setText(mGetUserGuardsResponseDatas.get(2).getNickname());
            tvCount1.setText(String.valueOf(FHUtils.intToF(mGetUserGuardsResponseDatas.get(0).getFriendliness())));
            tvCount2.setText(String.valueOf(FHUtils.intToF(mGetUserGuardsResponseDatas.get(1).getFriendliness())));
            tvCount3.setText(String.valueOf(FHUtils.intToF(mGetUserGuardsResponseDatas.get(2).getFriendliness())));
            TCUtils.showLevelWithUrl(getContext(), ivLevel1, mGetUserGuardsResponseDatas.get(0).getLevel());
            TCUtils.showLevelWithUrl(getContext(), ivLevel2, mGetUserGuardsResponseDatas.get(1).getLevel());
            TCUtils.showLevelWithUrl(getContext(), ivLevel3, mGetUserGuardsResponseDatas.get(2).getLevel());

        }

        if (mGetUserGuardsResponseDatas.size() == 2) {
            mRelativeLayout1.setVisibility(View.VISIBLE);
            mRelativeLayout2.setVisibility(View.VISIBLE);
            mRelativeLayout3.setVisibility(View.INVISIBLE);
            GlideApp.loadImg(getContext(), mGetUserGuardsResponseDatas.get(0).getAvatar(), DiskCacheStrategy.ALL, mPolygonImageView1);
            GlideApp.loadImg(getContext(), mGetUserGuardsResponseDatas.get(1).getAvatar(), DiskCacheStrategy.ALL, mPolygonImageView2);
            tvNick1.setText(mGetUserGuardsResponseDatas.get(0).getNickname());
            tvNick2.setText(mGetUserGuardsResponseDatas.get(1).getNickname());
            tvCount1.setText(String.valueOf(FHUtils.intToF(mGetUserGuardsResponseDatas.get(0).getFriendliness())));
            tvCount2.setText(String.valueOf(FHUtils.intToF(mGetUserGuardsResponseDatas.get(1).getFriendliness())));
            TCUtils.showLevelWithUrl(getContext(), ivLevel1, mGetUserGuardsResponseDatas.get(0).getLevel());
            TCUtils.showLevelWithUrl(getContext(), ivLevel2, mGetUserGuardsResponseDatas.get(1).getLevel());

        }
        if (mGetUserGuardsResponseDatas.size() == 1) {
            mRelativeLayout1.setVisibility(View.VISIBLE);
            mRelativeLayout2.setVisibility(View.INVISIBLE);
            mRelativeLayout3.setVisibility(View.INVISIBLE);
            GlideApp.loadImg(getContext(), mGetUserGuardsResponseDatas.get(0).getAvatar(), DiskCacheStrategy.ALL, mPolygonImageView1);
            tvNick1.setText(mGetUserGuardsResponseDatas.get(0).getNickname());
            tvCount1.setText(FHUtils.intToF(mGetUserGuardsResponseDatas.get(0).getFriendliness()));
            TCUtils.showLevelWithUrl(getContext(), ivLevel1, mGetUserGuardsResponseDatas.get(0).getLevel());
        }
        // 列表数据从第四个开始
        if (mGetUserGuardsResponseDatas.size() > 3) {
            mAdapterDatas.clear();
            for (int i = 3; i < mGetUserGuardsResponseDatas.size(); i++) {
                mAdapterDatas.add(mGetUserGuardsResponseDatas.get(i));
            }
            mAdapter.setNewData(mAdapterDatas);
        }
    }

    private void goOthersCommunite(String userId) {
        if (userId != mPresenter.getLoinUserId()) {
            Intent intent = new Intent(getContext(), OthersCommunityActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        }
    }
    
    @Override
    public void onRefresh() {
        isRefresh = true;
        mPresenter.getUserGuards(userId, 0);
    }

    @Override
    public void clickTorefresh() {
        mPresenter.getUserGuards(userId, offset);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.polygonImageView_1:
                goOthersCommunite(mGetUserGuardsResponseDatas.get(0).getUserId());
                break;
            case R.id.polygonImageView_2:
                goOthersCommunite(mGetUserGuardsResponseDatas.get(1).getUserId());
                break;
            case R.id.polygonImageView_3:
                goOthersCommunite(mGetUserGuardsResponseDatas.get(2).getUserId());
                break;
            default:
                break;
        }
    }
}
