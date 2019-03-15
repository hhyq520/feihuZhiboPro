package cn.feihutv.zhibofeihu.ui.me.encash;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetEncashListResponse;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.ui.me.adapter.WithdrawalrecordAdapter;
import cn.feihutv.zhibofeihu.ui.widget.StatusView;
import cn.feihutv.zhibofeihu.ui.widget.TCActivityTitle;
import cn.feihutv.zhibofeihu.utils.ListViewDecoration;

/**
 * <pre>
 *     @author : liwen.chen
 *     time   :
 *     desc   : 提现记录
 *     version: 1.0
 * </pre>
 */
public class WithdrawalrecordActivity extends BaseActivity implements WithdrawalrecordMvpView, SwipeRefreshLayout.OnRefreshListener, StatusView.ClickRefreshListener {

    @Inject
    WithdrawalrecordMvpPresenter<WithdrawalrecordMvpView> mPresenter;

    @BindView(R.id.cz_eui_edit)
    TCActivityTitle mTitle;

    @BindView(R.id.cz_swipe_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.cz_recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.status_view)
    StatusView mStatusView;

    private boolean isRefresh;

    private boolean isFirstLoad = true;

    private WithdrawalrecordAdapter mAdapter;

    private List<GetEncashListResponse.GetEncashListResponseData> mEncashListResponseDatas = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_withdrawalrecord;
    }


    @Override
    protected void initWidget() {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this));

        //persenter调用初始化
        mPresenter.onAttach(WithdrawalrecordActivity.this);

        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.btn_sure_forbidden));

        mSwipeRefreshLayout.setOnRefreshListener(this);

        mStatusView.setClickRefreshListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));// 布局管理器。

        mRecyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。

        mRecyclerView.addItemDecoration(new ListViewDecoration());// 添加分割线。

        mAdapter = new WithdrawalrecordAdapter(mEncashListResponseDatas);
        mRecyclerView.setAdapter(mAdapter);

        mPresenter.getEncashList();
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
    public void showNodataView() {
        if (isFirstLoad) {
            mSwipeRefreshLayout.setVisibility(View.GONE);
            mStatusView.showNoDataView();
            mStatusView.setNoDataBg(R.drawable.icon_no_attention);
            mStatusView.setTvNoData("还没有提现记录哦~");
            hideLoading();
        }
    }

    @Override
    public void showErrorView() {
        isRefresh = false;
        mSwipeRefreshLayout.setRefreshing(false);
        if (isFirstLoad) {
            mSwipeRefreshLayout.setVisibility(View.GONE);
            mStatusView.showErrorView();
            hideLoading();
        }
    }

    @Override
    public void showContent() {
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        mStatusView.setVisibility(View.GONE);
    }

    @Override
    public void getDatas(List<GetEncashListResponse.GetEncashListResponseData> getEncashListResponseDatas) {
        isFirstLoad = false;
        if (isRefresh) {
            if (isNetworkConnected()) {
                mEncashListResponseDatas.clear();
                mSwipeRefreshLayout.setRefreshing(false);
                isRefresh = false;
            }
        }

        mEncashListResponseDatas.addAll(getEncashListResponseDatas);
        mAdapter.setNewData(mEncashListResponseDatas);
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        mPresenter.getEncashList();
    }

    @Override
    protected void onDestroy() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void clickTorefresh() {
        mPresenter.getEncashList();
    }
}
