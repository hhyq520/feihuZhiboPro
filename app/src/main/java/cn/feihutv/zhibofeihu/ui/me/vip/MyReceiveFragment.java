package cn.feihutv.zhibofeihu.ui.me.vip;

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

import cn.feihutv.zhibofeihu.data.network.http.model.vip.GetVipReceiveLogResponse;
import cn.feihutv.zhibofeihu.ui.bangdan.adapter.GuardListAdapter;
import cn.feihutv.zhibofeihu.ui.me.adapter.MyReceiveAdapter;
import cn.feihutv.zhibofeihu.ui.me.vip.MyReceiveMvpView;
import cn.feihutv.zhibofeihu.ui.me.vip.MyReceiveMvpPresenter;
import cn.feihutv.zhibofeihu.ui.base.BaseFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.ui.widget.StatusView;
import cn.feihutv.zhibofeihu.utils.ListViewDecoration;

/**
 * <pre>
 *     @author : liwen.chen
 *     time   : 2017/
 *     desc   : 我的--vip--我的收获
 *     version: 1.0
 * </pre>
 */
public class MyReceiveFragment extends BaseFragment implements MyReceiveMvpView, SwipeRefreshLayout.OnRefreshListener, StatusView.ClickRefreshListener {

    @Inject
    MyReceiveMvpPresenter<MyReceiveMvpView> mPresenter;

    @BindView(R.id.my_receive_swip)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.my_receive_rv)
    RecyclerView mRecyclerView;

    @BindView(R.id.status_view)
    StatusView mStatusView;

    private boolean isFirstLoad = true;

    private boolean isPrepared;//当前页面是否加载

    private boolean isLoadEnd = false;

    private boolean isRefresh;

    private int offset = 0;

    private MyReceiveAdapter mMyReceiveAdapter;

    private List<GetVipReceiveLogResponse.ReceiveLogList> mReceiveLogLists = new ArrayList<>();

    public static MyReceiveFragment newInstance() {
        Bundle args = new Bundle();
        MyReceiveFragment fragment = new MyReceiveFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_receive;
    }


    @Override
    protected void initWidget(View view) {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this, view));

        //Presenter调用初始化
        mPresenter.onAttach(MyReceiveFragment.this);

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

        mMyReceiveAdapter = new MyReceiveAdapter(mReceiveLogLists);
        mRecyclerView.setAdapter(mMyReceiveAdapter);

        mRefreshLayout.setOnRefreshListener(this);

        mStatusView.setClickRefreshListener(this);

        mMyReceiveAdapter.setLoadMoreView(new SimpleLoadMoreView());

        mMyReceiveAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (!isLoadEnd) {
                    isLoadEnd = true;
                    mPresenter.getVipRecvLog(offset);
                }
            }
        }, mRecyclerView);
    }

    @Override
    protected void lazyLoad() {
        if (isPrepared && isVisible && mReceiveLogLists.size() == 0) {
            isPrepared = false;
            mPresenter.getVipRecvLog(offset);
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
            mStatusView.setNoDataBg(R.drawable.pic_remind_blank);
            mStatusView.setTvNoData("还没有赠送记录，累觉不爱");
            hideLoading();
        }
        if (isLoadEnd) {
            mMyReceiveAdapter.loadMoreEnd();
        }
    }

    @Override
    public void showContent() {
        mRefreshLayout.setVisibility(View.VISIBLE);
        mStatusView.setVisibility(View.GONE);
        hideLoading();

        if (isLoadEnd) {
            mMyReceiveAdapter.loadMoreComplete();
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
            mMyReceiveAdapter.loadMoreFail();
            isLoadEnd = false;
        }
    }

    @Override
    public void getDatas(List<GetVipReceiveLogResponse.ReceiveLogList> vipReceiveLogDataList, int offSet) {
        if (isRefresh) {
            if (isNetworkConnected()) {
                mReceiveLogLists.clear();
                mRefreshLayout.setRefreshing(false);
                isRefresh = false;
                offset = 0;
            }
        }

        mReceiveLogLists.addAll(vipReceiveLogDataList);
        offset = offSet;
        isFirstLoad = false;
        mMyReceiveAdapter.setNewData(mReceiveLogLists);
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        mPresenter.getVipRecvLog(0);
    }

    @Override
    public void clickTorefresh() {
        mPresenter.getVipRecvLog(offset);
    }
}
