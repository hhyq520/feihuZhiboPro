package cn.feihutv.zhibofeihu.ui.me.concern;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.me.FollowsResponse;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.ui.me.adapter.ConcernAdapter;
import cn.feihutv.zhibofeihu.ui.me.dynamic.OthersCommunityActivity;
import cn.feihutv.zhibofeihu.ui.widget.StatusView;
import cn.feihutv.zhibofeihu.ui.widget.TCActivityTitle;
import cn.feihutv.zhibofeihu.utils.ListViewDecoration;

/**
 * <pre>
 *     @author : liwen.chen
 *     time   :
 *     desc   : 关注列表
 *     version: 1.0
 * </pre>
 */
public class ConcernActivity extends BaseActivity implements ConcernMvpView, SwipeRefreshLayout.OnRefreshListener, StatusView.ClickRefreshListener {

    @BindView(R.id.concer_back)
    TCActivityTitle mTitle;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.recycleview_concer)
    RecyclerView mRecyclerView;

    @BindView(R.id.status_view)
    StatusView mStatusView;

    private int offset = 0;

    private boolean isFirstLoad = true;

    private ConcernAdapter mAdapter;

    private boolean isRefresh;

    private boolean isLoadEnd = false;

    @Inject
    ConcernMvpPresenter<ConcernMvpView> mPresenter;

    private List<FollowsResponse.FollowsResponseData> mResponseDatas = new ArrayList<>();


    @Override
    protected int getLayoutId() {
        return R.layout.activity_concern;
    }


    @Override
    protected void initWidget() {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this));

        //persenter调用初始化
        mPresenter.onAttach(ConcernActivity.this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //添加分隔线
        mRecyclerView.addItemDecoration(new ListViewDecoration(1));
        mRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        mRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);

        mRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

        mAdapter = new ConcernAdapter(mResponseDatas);
        mRecyclerView.setAdapter(mAdapter);

        mRefreshLayout.setOnRefreshListener(this);
        mStatusView.setClickRefreshListener(this);

        mPresenter.getFollows(offset);

        mAdapter.setLoadMoreView(new SimpleLoadMoreView());

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (!isLoadEnd) {
                    isLoadEnd = true;
                    mPresenter.getFollows(offset);
                }
            }
        }, mRecyclerView);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), OthersCommunityActivity.class);
                intent.putExtra("userId", mResponseDatas.get(position).getUserId());
                startActivity(intent);
            }
        });

    }

    @Override
    protected void eventOnClick() {
        mTitle.setReturnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroy();
    }


    @Override
    public void showNoData() {
        if (isFirstLoad) {
            mRefreshLayout.setVisibility(View.GONE);
            mStatusView.showNoDataView();
            mStatusView.setNoDataBg(R.drawable.icon_no_attention);
            mStatusView.setTvNoData("一个人太孤单了，去多交几个朋友吧");
            hideLoading();
        }

        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
            if (mResponseDatas.size() > 0) {
                mResponseDatas.clear();
                mAdapter.setNewData(mResponseDatas);

                mRefreshLayout.setVisibility(View.GONE);
                mStatusView.showNoDataView();
                mStatusView.setNoDataBg(R.drawable.icon_no_attention);
                mStatusView.setTvNoData("一个人太孤单了，去多交几个朋友吧");
            }
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
    public void getDatas(List<FollowsResponse.FollowsResponseData> responseData) {
        if (isRefresh) {
            if (isNetworkConnected()) {
                mResponseDatas.clear();
                mRefreshLayout.setRefreshing(false);
                isRefresh = false;
            }
        }
        mResponseDatas.addAll(responseData);
        offset = mResponseDatas.size();
        mAdapter.setNewData(mResponseDatas);
        isFirstLoad = false;
    }

    @Override
    public Activity getActivity() {
        return ConcernActivity.this;
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        offset = 0;
        mPresenter.getFollows(0);
    }

    @Override
    public void clickTorefresh() {
        mPresenter.getFollows(0);
    }
}
