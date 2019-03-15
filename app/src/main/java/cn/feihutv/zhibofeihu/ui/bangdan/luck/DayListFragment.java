package cn.feihutv.zhibofeihu.ui.bangdan.luck;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.feihutv.zhibofeihu.BuildConfig;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;

import cn.feihutv.zhibofeihu.data.network.http.model.bangdan.LoadLuckRecordListResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.GameStartRoundPush;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.rxbus.RxBusSubscribe;
import cn.feihutv.zhibofeihu.rxbus.ThreadMode;
import cn.feihutv.zhibofeihu.rxbus.bangdan.LuckPush;
import cn.feihutv.zhibofeihu.ui.bangdan.RankingFragment;
import cn.feihutv.zhibofeihu.ui.bangdan.adapter.DayListAdapter;
import cn.feihutv.zhibofeihu.ui.bangdan.luck.DayListMvpView;
import cn.feihutv.zhibofeihu.ui.bangdan.luck.DayListMvpPresenter;
import cn.feihutv.zhibofeihu.ui.base.BaseFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.ui.live.pclive.LivePlayInPCActivity;
import cn.feihutv.zhibofeihu.ui.me.dynamic.OthersCommunityActivity;
import cn.feihutv.zhibofeihu.ui.widget.StatusView;
import cn.feihutv.zhibofeihu.ui.widget.swiperefreshlayout.VRefreshLayout;
import cn.feihutv.zhibofeihu.utils.AppUtils;
import cn.feihutv.zhibofeihu.utils.CustomDialogUtils;
import cn.feihutv.zhibofeihu.utils.ListViewDecoration;

/**
 * <pre>
 *     author : liwen.chen
 *     time   : 2017/
 *     desc   : 幸运记录
 *     version: 1.0
 * </pre>
 */
public class  DayListFragment extends BaseFragment implements DayListMvpView, StatusView.ClickRefreshListener, VRefreshLayout.OnRefreshListener, BaseQuickAdapter.OnItemChildClickListener {

    @Inject
    DayListMvpPresenter<DayListMvpView> mPresenter;

    @BindView(R.id.luck_swip)
    VRefreshLayout mRefreshLayout;

    @BindView(R.id.luck_rv)
    RecyclerView mRecyclerView;

    @BindView(R.id.status_view)
    StatusView mStatusView;

    private boolean isPrepared;

    private boolean isRefresh;

    private DayListAdapter mAdapter;

    private List<LoadLuckRecordListResponse.LoadLuckRecordListResponseData.ListResponseData> mListResponseDatas = new ArrayList<>();

    private String rankType = "1";

    private int offset = 0;

    private int count = 20;

    private boolean isFirstLoad = true;
    private LoadUserDataBaseResponse.UserData userDatas;

    public static DayListFragment newInstance() {
        Bundle args = new Bundle();
        DayListFragment fragment = new DayListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_daylist;
    }


    @Override
    protected void initWidget(View view) {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this, view));

        //Presenter调用初始化
        mPresenter.onAttach(DayListFragment.this);

        userDatas = mPresenter.getUserDatas();

        isPrepared = true;

        mStatusView.setClickRefreshListener(this);

        mRefreshLayout.addOnRefreshListener(this);

        mAdapter = new DayListAdapter(mListResponseDatas, userDatas);

        mRecyclerView.addItemDecoration(new ListViewDecoration(2));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.loadLuckRecordList(rankType, offset, count);
            }
        }, mRecyclerView);

        mAdapter.setOnItemChildClickListener(this);

    }

    @Override
    protected void lazyLoad() {
        if (isPrepared && isVisible && mListResponseDatas.size() == 0) {
            isPrepared = false;
            mPresenter.loadLuckRecordList(rankType, offset, count);
        }
    }

    // 接收父级发送的信息
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_POSITION_CURRENTITEM, threadMode = ThreadMode.MAIN)
    public void onReceivePositioncurrentitem(LuckPush luckPush) {
        if (luckPush.getCurrentPosition() == 0) {
            mListResponseDatas.clear();
            offset = 0;
            rankType = String.valueOf(luckPush.getRankType());
            mPresenter.loadLuckRecordList(rankType, offset, count);
        }
    }


    @Override
    public void onDestroyView() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroyView();
    }


    @Override
    public void getDatas(LoadLuckRecordListResponse.LoadLuckRecordListResponseData recordListResponseData) {
        if (isRefresh) {
            if (isNetworkConnected()) {
                mListResponseDatas.clear();
                mRefreshLayout.refreshComplete();
                isRefresh = false;
            }
        }
        if (recordListResponseData.getList().size() > 0) {
            // 显示加载成功
            isFirstLoad = false;
            showDatas();
            mListResponseDatas.addAll(recordListResponseData.getList());
            mAdapter.setNewData(mListResponseDatas);
            offset = recordListResponseData.getNextOffset();
        } else {
            if (isFirstLoad) {
                // 显示无数据
                showNoDatas();
            }
        }
    }

    @Override
    public void showErrorView() {
        isRefresh = false;
        mRefreshLayout.refreshComplete();
        if (isFirstLoad) {
            // 显示网络错误 点击重试
            mRefreshLayout.setVisibility(View.GONE);
            mStatusView.showErrorView();
        }
    }

    private void showNoDatas() {
        mRefreshLayout.setVisibility(View.GONE);
        mStatusView.showNoDataView();
    }

    private void showDatas() {
        mRefreshLayout.setVisibility(View.VISIBLE);
        mStatusView.setVisibility(View.GONE);
    }


    @Override
    public void clickTorefresh() {
        mPresenter.loadLuckRecordList(rankType, offset, count);
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        offset = 0;
        mPresenter.loadLuckRecordList(rankType, offset, count);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.rl_user:
                goOthersCommunite(mListResponseDatas.get(position).getUserId());
                break;
            case R.id.rl_host:
                goLiveRoom(mListResponseDatas.get(position).getRoomUserId(), mListResponseDatas.get(position).getHeadUrl(), mListResponseDatas.get(position).getBroadcastType(), true, 100);
                break;
            default:
                break;
        }
    }

    private void goOthersCommunite(String userId) {
        if (mPresenter.isGuestUser()) {
            if (BuildConfig.isForceLoad.equals("1")) {
                CustomDialogUtils.showQZLoginDialog(getActivity(), false);
            } else {
                CustomDialogUtils.showLoginDialog(getActivity(), false);
            }
        } else {
            if (!userId.equals(mPresenter.getLoinUserId())) {
                Intent intent = new Intent(getContext(), OthersCommunityActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        }
    }

    private void goLiveRoom(String userId, String headUrl, int broadType, boolean isShowWindow, int requestCode) {
        if (!userId.equals(mPresenter.getLoinUserId())) {
            AppUtils.startLiveActivity(DayListFragment.this, userId, headUrl, broadType, isShowWindow, requestCode);
        }
    }

}
