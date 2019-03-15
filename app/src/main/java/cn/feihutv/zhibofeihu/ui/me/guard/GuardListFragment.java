package cn.feihutv.zhibofeihu.ui.me.guard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView;

import java.util.ArrayList;
import java.util.List;

import cn.feihutv.zhibofeihu.R;

import cn.feihutv.zhibofeihu.data.network.http.model.me.GetUserGuardListResponse;
import cn.feihutv.zhibofeihu.ui.bangdan.adapter.GuardListAdapter;
import cn.feihutv.zhibofeihu.ui.bangdan.adapter.GuardsAdapter;
import cn.feihutv.zhibofeihu.ui.me.dynamic.OthersCommunityActivity;
import cn.feihutv.zhibofeihu.ui.me.guard.GuardListMvpView;
import cn.feihutv.zhibofeihu.ui.me.guard.GuardListMvpPresenter;
import cn.feihutv.zhibofeihu.ui.base.BaseFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.ui.widget.StatusView;
import cn.feihutv.zhibofeihu.utils.AppUtils;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.ListViewDecoration;
import cn.feihutv.zhibofeihu.utils.TCUtils;

/**
 * <pre>
 *     author : liwen.chen
 *     time   : 2017/
 *     desc   : 我守护的
 *     version: 1.0
 * </pre>
 */
public class GuardListFragment extends BaseFragment implements GuardListMvpView, SwipeRefreshLayout.OnRefreshListener, StatusView.ClickRefreshListener {

    @Inject
    GuardListMvpPresenter<GuardListMvpView> mPresenter;

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

    private GuardListAdapter mAdapter;
    private String userId;

    private List<GetUserGuardListResponse.GetUserGuardListResponseData> mGetUserGuardListResponseDatas = new ArrayList<>();

    public static GuardListFragment newInstance(String userId) {
        Bundle args = new Bundle();
        GuardListFragment fragment = new GuardListFragment();
        args.putString("userId", userId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_guard_list;
    }


    @Override
    protected void initWidget(View view) {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this, view));

        //Presenter调用初始化
        mPresenter.onAttach(GuardListFragment.this);

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

        mAdapter = new GuardListAdapter(mGetUserGuardListResponseDatas);
        mRecyclerView.setAdapter(mAdapter);

        mRefreshLayout.setOnRefreshListener(this);

        mStatusView.setClickRefreshListener(this);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                goLiveRoom(mGetUserGuardListResponseDatas.get(position).getUserId(), mGetUserGuardListResponseDatas.get(position).getAvatar(), mGetUserGuardListResponseDatas.get(position).getBroadcastType());
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                goLiveRoom(mGetUserGuardListResponseDatas.get(position).getUserId(), mGetUserGuardListResponseDatas.get(position).getAvatar(), mGetUserGuardListResponseDatas.get(position).getBroadcastType(), true);
            }
        });

        mAdapter.setLoadMoreView(new SimpleLoadMoreView());

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (!isLoadEnd) {
                    isLoadEnd = true;
                    mPresenter.getUserGuardList(userId, offset);
                }
            }
        }, mRecyclerView);
    }

    @Override
    protected void lazyLoad() {
        if (isPrepared && isVisible && mGetUserGuardListResponseDatas.size() == 0) {
            Bundle bundle = getArguments();
            userId = bundle.getString("userId");
            isPrepared = false;
            mPresenter.getUserGuardList(userId, offset);
        }
    }

    @Override
    public void showNoData() {
        if (isFirstLoad) {
            mRefreshLayout.setVisibility(View.GONE);
            mStatusView.showNoDataView();
            mStatusView.setTvNoData("暂未守护任何主播\n快去守护你心仪的主播吧~");
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
    public void getDatas(List<GetUserGuardListResponse.GetUserGuardListResponseData> getUserGuardListResponseDatas) {
        if (isRefresh) {
            if (isNetworkConnected()) {
                mGetUserGuardListResponseDatas.clear();
                mRefreshLayout.setRefreshing(false);
                isRefresh = false;
                offset = 0;
            }
        }

        mGetUserGuardListResponseDatas.addAll(getUserGuardListResponseDatas);
        offset = mGetUserGuardListResponseDatas.size();
        isFirstLoad = false;
        mAdapter.setNewData(mGetUserGuardListResponseDatas);
    }

    @Override
    public void onDestroyView() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroyView();
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        mPresenter.getUserGuardList(userId, 0);
    }

    private void goLiveRoom(String userId, String headUrl, int broadCast) {
        if (userId != mPresenter.getLoinUserId()) {
            AppUtils.startLiveActivity(getContext(), userId, headUrl, broadCast, true);
        }
    }


    private void goLiveRoom(String userId, String headUrl, int broadCast, boolean isNeedGuard) {
        if (userId != mPresenter.getLoinUserId()) {
            AppUtils.startLiveActivity(getContext(), userId, headUrl, broadCast, true, isNeedGuard);
        }
    }

    @Override
    public void clickTorefresh() {
        mPresenter.getUserGuardList(userId, offset);
    }
}
