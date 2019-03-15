package cn.feihutv.zhibofeihu.ui.me.recharge;


import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetPayListResponse;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import javax.inject.Inject;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.ui.me.adapter.CzRecordAdapter;
import cn.feihutv.zhibofeihu.ui.widget.StatusView;
import cn.feihutv.zhibofeihu.ui.widget.TCActivityTitle;
import cn.feihutv.zhibofeihu.utils.ListViewDecoration;

/**
 * <pre>
 *     author : liwen.chen
 *     time   :
 *     desc   : 充值记录页面
 *     version: 1.0
 * </pre>
 */
public class PrepaidrecordsActivity extends BaseActivity implements PrepaidrecordsMvpView, SwipeRefreshLayout.OnRefreshListener, StatusView.ClickRefreshListener {

    @Inject
    PrepaidrecordsMvpPresenter<PrepaidrecordsMvpView> mPresenter;

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

    private CzRecordAdapter mAdapter;

    private List<GetPayListResponse.GetPayListResponseData> mPayListResponseDatas = new ArrayList<>();


    @Override
    protected int getLayoutId() {
        return R.layout.activity_prepaidrecords;
    }


    @Override
    protected void initWidget() {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this));

        //persenter调用初始化
        mPresenter.onAttach(PrepaidrecordsActivity.this);

        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.btn_sure_forbidden));

        mSwipeRefreshLayout.setOnRefreshListener(this);

        mStatusView.setClickRefreshListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));// 布局管理器。

        mRecyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。

        mRecyclerView.addItemDecoration(new ListViewDecoration());// 添加分割线。

        mAdapter = new CzRecordAdapter(mPayListResponseDatas);
        mRecyclerView.setAdapter(mAdapter);

        mPresenter.getPayList();
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
    public void getDatas(List<GetPayListResponse.GetPayListResponseData> payListResponseDatas) {
        isFirstLoad = false;
        if (isRefresh) {
            if (isNetworkConnected()) {
                mPayListResponseDatas.clear();
                mSwipeRefreshLayout.setRefreshing(false);
                isRefresh = false;
            }
        }

        mPayListResponseDatas.addAll(payListResponseDatas);
        mAdapter.setNewData(mPayListResponseDatas);
    }

    @Override
    public void showNodataView() {
        if (isFirstLoad) {
            mSwipeRefreshLayout.setVisibility(View.GONE);
            mStatusView.setTvNoData("没有充值记录，去充值一笔吧~");
            mStatusView.setNoDataBg(R.drawable.icon_no_attention);
            mStatusView.showNoDataView();
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
    protected void onDestroy() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        mPresenter.getPayList();
    }

    @Override
    public void clickTorefresh() {
        mPresenter.getPayList();
    }
}
