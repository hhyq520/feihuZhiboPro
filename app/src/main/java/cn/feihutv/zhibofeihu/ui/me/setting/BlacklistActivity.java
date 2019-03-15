package cn.feihutv.zhibofeihu.ui.me.setting;

import android.app.Activity;
import android.content.Intent;
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
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetBlacklistResponse;
import cn.feihutv.zhibofeihu.ui.me.adapter.BlackListAdapter;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.ui.me.dynamic.OthersCommunityActivity;
import cn.feihutv.zhibofeihu.ui.widget.StatusView;
import cn.feihutv.zhibofeihu.ui.widget.TCActivityTitle;
import cn.feihutv.zhibofeihu.utils.ListViewDecoration;

/**
 * <pre>
 *     author : liwen.chen
 *     time   :
 *     desc   : 黑名单
 *     version: 1.0
 * </pre>
 */
public class BlacklistActivity extends BaseActivity implements BlacklistMvpView, SwipeRefreshLayout.OnRefreshListener, StatusView.ClickRefreshListener {

    @Inject
    BlacklistMvpPresenter<BlacklistMvpView> mPresenter;

    @BindView(R.id.tca_blacklist_reture)
    TCActivityTitle mTitle;

    @BindView(R.id.blacklist_swipe_layout)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.blacklist_recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.status_view)
    StatusView mStatusView;

    private boolean isFirstLoad = true;

    private boolean isLoadEnd = false;

    private boolean isRefresh;

    private int offset = 0;

    private BlackListAdapter mAdapter;

    private List<GetBlacklistResponse.GetBlacklistResponseData> mBlacklistResponseDatas = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_blacklist;
    }


    @Override
    protected void initWidget() {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this));

        //persenter调用初始化
        mPresenter.onAttach(BlacklistActivity.this);

        mRefreshLayout.setOnRefreshListener(this);

        mStatusView.setClickRefreshListener(this);

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

        mAdapter = new BlackListAdapter(mBlacklistResponseDatas);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setLoadMoreView(new SimpleLoadMoreView());

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (!isLoadEnd) {
                    isLoadEnd = true;
                    mPresenter.getBlacklist(offset);
                }
            }
        }, mRecyclerView);


        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), OthersCommunityActivity.class);
                intent.putExtra("userId", mBlacklistResponseDatas.get(position).getUserId());
                startActivity(intent);
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                mPresenter.unblock(mBlacklistResponseDatas.get(position).getUserId(), position);
            }
        });

        mPresenter.getBlacklist(offset);

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
            mStatusView.setTvNoData("暂无数据哦~");
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
    public void removeSucc(int position) {
        mBlacklistResponseDatas.remove(position);
        mAdapter.setNewData(mBlacklistResponseDatas);
    }


    @Override
    public void getDatas(List<GetBlacklistResponse.GetBlacklistResponseData> responseData) {
        if (isRefresh) {
            if (isNetworkConnected()) {
                mBlacklistResponseDatas.clear();
                mRefreshLayout.setRefreshing(false);
                isRefresh = false;
            }
        }
        mBlacklistResponseDatas.addAll(responseData);
        offset = mBlacklistResponseDatas.size();
        mAdapter.setNewData(mBlacklistResponseDatas);
        isFirstLoad = false;
    }

    @Override
    public Activity getActivity() {
        return BlacklistActivity.this;
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        offset = 0;
        mPresenter.getBlacklist(0);
    }

    @Override
    public void clickTorefresh() {
        mPresenter.getBlacklist(0);
    }
}
