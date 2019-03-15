package cn.feihutv.zhibofeihu.ui.me.roommrgs;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.feihutv.zhibofeihu.R;

import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.GetRoomMrgsResponce;
import cn.feihutv.zhibofeihu.ui.me.adapter.CzRecordAdapter;
import cn.feihutv.zhibofeihu.ui.me.adapter.RoomMrgsAdapter;
import cn.feihutv.zhibofeihu.ui.me.roommrgs.RoomMrgsMvpView;
import cn.feihutv.zhibofeihu.ui.me.roommrgs.RoomMrgsMvpPresenter;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.ui.widget.StatusView;
import cn.feihutv.zhibofeihu.ui.widget.TCActivityTitle;
import cn.feihutv.zhibofeihu.utils.ListViewDecoration;
import cn.feihutv.zhibofeihu.utils.weiget.dialog.RxDialogSureCancel;

/**
 * <pre>
 *     @author : liwen.chen
 *     time   :
 *     desc   : 我的场控
 *     version: 1.0
 * </pre>
 */
public class RoomMrgsActivity extends BaseActivity implements RoomMrgsMvpView, SwipeRefreshLayout.OnRefreshListener, StatusView.ClickRefreshListener, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {

    @Inject
    RoomMrgsMvpPresenter<RoomMrgsMvpView> mPresenter;

    @BindView(R.id.roommrgs_back)
    TCActivityTitle mTitle;

    @BindView(R.id.roommrgs_swipe_layout)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    @BindView(R.id.status_view)
    StatusView mStatusView;

    private boolean isFirstLoad = true;

    private boolean isRefresh;

    private RoomMrgsAdapter mAdapter;

    private List<GetRoomMrgsResponce.RoomMrgData> mRoomMrgDatas = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_room_mrgs;
    }


    @Override
    protected void initWidget() {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this));

        //persenter调用初始化
        mPresenter.onAttach(RoomMrgsActivity.this);

        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.btn_sure_forbidden));

        mRefreshLayout.setOnRefreshListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));// 布局管理器。

        mRecyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。

        mRecyclerView.addItemDecoration(new ListViewDecoration());// 添加分割线。

        mStatusView.setClickRefreshListener(this);

        mAdapter = new RoomMrgsAdapter(mRoomMrgDatas,false);
        mRecyclerView.setAdapter(mAdapter);

        mPresenter.getRoomMgrs();

        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemChildClickListener(this);
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
    public void showContent() {
        mRefreshLayout.setVisibility(View.VISIBLE);
        mStatusView.setVisibility(View.GONE);
    }

    @Override
    public void showNodataView() {
        if (isFirstLoad) {
            mRefreshLayout.setVisibility(View.GONE);
            mStatusView.showNoDataView();
            mStatusView.setNoDataBg(R.drawable.icon_no_attention);
            mStatusView.setTvNoData("暂无场控,马上去开播设置你的场控吧~");
            hideLoading();
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
    }

    @Override
    public void getDatas(List<GetRoomMrgsResponce.RoomMrgData> roomMrgDataList) {
        isFirstLoad = false;
        if (isRefresh) {
            if (isNetworkConnected()) {
                mRoomMrgDatas.clear();
                mRefreshLayout.setRefreshing(false);
                isRefresh = false;
            }
        }

        mRoomMrgDatas.addAll(roomMrgDataList);
        mAdapter.setNewData(mRoomMrgDatas);
    }

    @Override
    public void showCancelSucc(int position) {
        onToast("取消成功", Gravity.CENTER, 0, 0);
        mRoomMrgDatas.remove(position);
        mAdapter.notifyItemRemoved(position);
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        mPresenter.getRoomMgrs();
    }

    @Override
    public void clickTorefresh() {
        mPresenter.getRoomMgrs();
    }

    @Override
    protected void onDestroy() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        onToast("点击了第" + position + "个， " + mRoomMrgDatas.get(position).getUserId());
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
        final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(RoomMrgsActivity.this);
        rxDialogSureCancel.setContent("  您确定要取消该场控？  ");
        rxDialogSureCancel.setSure("考虑一下");
        rxDialogSureCancel.setCancel("确定");
        rxDialogSureCancel.setSureListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rxDialogSureCancel.dismiss();
            }
        });
        rxDialogSureCancel.setCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 取消场控
                mPresenter.cancelRoomMgr(mRoomMrgDatas.get(position).getUserId(), position);
                rxDialogSureCancel.dismiss();
            }
        });

        rxDialogSureCancel.show();
    }
}
