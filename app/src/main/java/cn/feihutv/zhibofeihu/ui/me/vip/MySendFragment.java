package cn.feihutv.zhibofeihu.ui.me.vip;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView;
import java.util.ArrayList;
import java.util.List;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.vip.GetVipSendLogResponse;
import cn.feihutv.zhibofeihu.ui.me.adapter.MySendAdapter;
import cn.feihutv.zhibofeihu.ui.base.BaseFragment;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.ui.widget.StatusView;
import cn.feihutv.zhibofeihu.utils.ListViewDecoration;

/**
 * <pre>
 *     author : liwen.chen
 *     time   : 2017/
 *     desc   : 我的--vip--我的赠送
 *     version: 1.0
 * </pre>
 */
public class MySendFragment extends BaseFragment implements MySendMvpView, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    MySendMvpPresenter<MySendMvpView> mPresenter;

    @BindView(R.id.my_send_swip)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.my_send_rv)
    RecyclerView mRecyclerView;

    @BindView(R.id.rl_send)
    RelativeLayout rlSend;

    @BindView(R.id.ll_error_view)
    LinearLayout llErrorView;

    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;

    @BindView(R.id.btn_send)
    Button btnSend;

    private boolean isFirstLoad = true;

    private boolean isPrepared;//当前页面是否加载

    private boolean isLoadEnd = false;

    private boolean isRefresh;

    private int offset = 0;

    private MySendAdapter mMySendAdapter;

    private List<GetVipSendLogResponse.SendLogList> mSendLogLists = new ArrayList<>();

    public static MySendFragment newInstance() {
        Bundle args = new Bundle();
        MySendFragment fragment = new MySendFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_send;
    }


    @Override
    protected void initWidget(View view) {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this, view));

        //Presenter调用初始化
        mPresenter.onAttach(MySendFragment.this);

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

        mMySendAdapter = new MySendAdapter(mSendLogLists);
        mRecyclerView.setAdapter(mMySendAdapter);

        mRefreshLayout.setOnRefreshListener(this);

        llErrorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.getVipSendLog(offset);
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), GiveFriendsActivity.class));
            }
        });

        mMySendAdapter.setLoadMoreView(new SimpleLoadMoreView());

        mMySendAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (!isLoadEnd) {
                    isLoadEnd = true;
                    mPresenter.getVipSendLog(offset);
                }
            }
        }, mRecyclerView);
    }

    @Override
    protected void lazyLoad() {
        if (isPrepared && isVisible && mSendLogLists.size() == 0) {
            isPrepared = false;
            mPresenter.getVipSendLog(offset);
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
            rlSend.setVisibility(View.VISIBLE);
            if (llNoData.getVisibility() != View.VISIBLE) {
                llNoData.setVisibility(View.VISIBLE);
            }

            if (llErrorView.getVisibility() == View.VISIBLE) {
                llErrorView.setVisibility(View.GONE);
            }
            hideLoading();
        }
        if (isLoadEnd) {
            mMySendAdapter.loadMoreEnd();
        }
    }

    @Override
    public void showContent() {
        mRefreshLayout.setVisibility(View.VISIBLE);
        rlSend.setVisibility(View.GONE);
        hideLoading();

        if (isLoadEnd) {
            mMySendAdapter.loadMoreComplete();
            isLoadEnd = false;
        }
    }

    @Override
    public void showErrorView() {
        isRefresh = false;
        mRefreshLayout.setRefreshing(false);
        if (isFirstLoad) {
            mRefreshLayout.setVisibility(View.GONE);
            rlSend.setVisibility(View.VISIBLE);
            if (llNoData.getVisibility() == View.VISIBLE) {
                llNoData.setVisibility(View.GONE);
            }

            if (llErrorView.getVisibility() != View.VISIBLE) {
                llErrorView.setVisibility(View.VISIBLE);
            }
            hideLoading();
        }
        if (isLoadEnd) {
            mMySendAdapter.loadMoreFail();
            isLoadEnd = false;
        }
    }

    @Override
    public void getDatas(List<GetVipSendLogResponse.SendLogList> sendLogLists, int offSet) {
        if (isRefresh) {
            if (isNetworkConnected()) {
                mSendLogLists.clear();
                mRefreshLayout.setRefreshing(false);
                isRefresh = false;
                offset = 0;
            }
        }

        mSendLogLists.addAll(sendLogLists);
        offset = offSet;
        isFirstLoad = false;
        mMySendAdapter.setNewData(mSendLogLists);
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        mPresenter.getVipSendLog(0);
    }
}
