package cn.feihutv.zhibofeihu.ui.bangdan.luck;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.feihutv.zhibofeihu.BuildConfig;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.bangdan.LoadLuckRankListResponse;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.rxbus.RxBusSubscribe;
import cn.feihutv.zhibofeihu.rxbus.ThreadMode;
import cn.feihutv.zhibofeihu.rxbus.bangdan.LuckPush;
import cn.feihutv.zhibofeihu.ui.bangdan.adapter.TotalListAdapter;
import cn.feihutv.zhibofeihu.ui.base.BaseFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.ui.me.dynamic.OthersCommunityActivity;
import cn.feihutv.zhibofeihu.ui.widget.StatusView;
import cn.feihutv.zhibofeihu.ui.widget.swiperefreshlayout.VRefreshLayout;
import cn.feihutv.zhibofeihu.utils.AppUtils;
import cn.feihutv.zhibofeihu.utils.CustomDialogUtils;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.ListViewDecoration;
import cn.feihutv.zhibofeihu.utils.glideutil.GlideApp;

/**
 * <pre>
 *     author : liwen.chen
 *     time   : 2017/
 *     desc   : 界面绘制
 *     version: 1.0
 * </pre>
 */
public class TotalListFragment extends BaseFragment implements TotalListMvpView, StatusView.ClickRefreshListener, VRefreshLayout.OnRefreshListener, View.OnClickListener {

    @Inject
    TotalListMvpPresenter<TotalListMvpView> mPresenter;

    @BindView(R.id.luck_total_swip)
    VRefreshLayout mRefreshLayout;

    @BindView(R.id.luck_total_rv)
    RecyclerView mRecyclerView;

    @BindView(R.id.status_total_view)
    StatusView mStatusView;

    private boolean isRefresh;

    private List<LoadLuckRankListResponse.LoadLuckRankListResponseData> mLuckRankListResponseDatas = new ArrayList<>();

    private List<LoadLuckRankListResponse.LoadLuckRankListResponseData> mAdapterDatas = new ArrayList<>();

    private boolean isPrepared;

    private String rankType = "1";

    private boolean isFirstLoad = true;

    private TotalListAdapter mAdapter;

    private ImageView mPolygonImageView1, mPolygonImageView2, mPolygonImageView3;
    private ImageView ivLive1, ivLive2, ivLive3;
    private TextView tvNick1, tvNick2, tvNick3, tvCount1, tvCount2, tvCount3, tvAwardCount1, tvAwardCount2, tvAwardCount3;
    private View mHeadView;
    private RelativeLayout mRelativeLayout1, mRelativeLayout2, mRelativeLayout3;

    public static TotalListFragment newInstance() {
        Bundle args = new Bundle();
        TotalListFragment fragment = new TotalListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_totallist;
    }


    @Override
    protected void initWidget(View view) {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this, view));

        //Presenter调用初始化
        mPresenter.onAttach(TotalListFragment.this);

        isPrepared = true;

        mStatusView.setClickRefreshListener(this);

        mRefreshLayout.addOnRefreshListener(this);

        mAdapter = new TotalListAdapter(mLuckRankListResponseDatas);

        mRecyclerView.addItemDecoration(new ListViewDecoration());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mRecyclerView.setAdapter(mAdapter);


        mHeadView = LayoutInflater.from(getActivity()).inflate(R.layout.view_head_luck_total, null);
        mPolygonImageView1 = (ImageView) mHeadView.findViewById(R.id.polygonImageView_1);
        mPolygonImageView2 = (ImageView) mHeadView.findViewById(R.id.polygonImageView_2);
        mPolygonImageView3 = (ImageView) mHeadView.findViewById(R.id.polygonImageView_3);
        tvNick1 = (TextView) mHeadView.findViewById(R.id.tv_nick_1);
        tvNick2 = (TextView) mHeadView.findViewById(R.id.tv_nick_2);
        tvNick3 = (TextView) mHeadView.findViewById(R.id.tv_nick_3);
        tvCount1 = (TextView) mHeadView.findViewById(R.id.tv_count1);
        tvCount2 = (TextView) mHeadView.findViewById(R.id.tv_count2);
        tvCount3 = (TextView) mHeadView.findViewById(R.id.tv_count3);
        tvAwardCount1 = (TextView) mHeadView.findViewById(R.id.tv_awardcount1);
        tvAwardCount2 = (TextView) mHeadView.findViewById(R.id.tv_awardcount2);
        tvAwardCount3 = (TextView) mHeadView.findViewById(R.id.tv_awardcount3);
        ivLive1 = (ImageView) mHeadView.findViewById(R.id.iv_live1);
        ivLive2 = (ImageView) mHeadView.findViewById(R.id.iv_live2);
        ivLive3 = (ImageView) mHeadView.findViewById(R.id.iv_live3);

        mPolygonImageView1.setOnClickListener(this);
        mPolygonImageView2.setOnClickListener(this);
        mPolygonImageView3.setOnClickListener(this);

        mRelativeLayout1 = (RelativeLayout) mHeadView.findViewById(R.id.rl_1);
        mRelativeLayout2 = (RelativeLayout) mHeadView.findViewById(R.id.rl_2);
        mRelativeLayout3 = (RelativeLayout) mHeadView.findViewById(R.id.rl_3);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                goLiveRoom(mAdapterDatas.get(position).getUserId(), mAdapterDatas.get(position).getHeadUrl(), mAdapterDatas.get(position).getBroadcastType(), true, 100);
            }
        });

        mAdapter.addHeaderView(mHeadView);
    }

    @Override
    protected void lazyLoad() {
        if (isPrepared && isVisible && mLuckRankListResponseDatas.size() == 0) {
            isPrepared = false;
            mPresenter.loadLuckRankList(rankType);
        }

    }


    // 接收父级发送的信息
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_POSITION_CURRENTITEM, threadMode = ThreadMode.MAIN)
    public void onReceivePositioncurrentitem(LuckPush luckPush) {
        if (luckPush.getCurrentPosition() == 1) {
            mLuckRankListResponseDatas.clear();
            mAdapterDatas.clear();
            rankType = String.valueOf(luckPush.getRankType());
            mPresenter.loadLuckRankList(rankType);
        }
    }

    @Override
    public void onDestroyView() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroyView();
    }


    @Override
    public void getDatas(List<LoadLuckRankListResponse.LoadLuckRankListResponseData> rankListResponseData) {
        if (isRefresh) {
            if (isNetworkConnected()) {
                mLuckRankListResponseDatas.clear();
                mAdapterDatas.clear();
                mRefreshLayout.refreshComplete();
                isRefresh = false;
            }
        }

        if (rankListResponseData.size() > 0) {
            showDatas();
            if (rankListResponseData.size() >= 3) {
                mRelativeLayout1.setVisibility(View.VISIBLE);
                mRelativeLayout2.setVisibility(View.VISIBLE);
                mRelativeLayout3.setVisibility(View.VISIBLE);
                GlideApp.loadImg(getContext(), rankListResponseData.get(0).getHeadUrl(), DiskCacheStrategy.ALL, mPolygonImageView1);
                GlideApp.loadImg(getContext(), rankListResponseData.get(1).getHeadUrl(), DiskCacheStrategy.ALL, mPolygonImageView2);
                GlideApp.loadImg(getContext(), rankListResponseData.get(2).getHeadUrl(), DiskCacheStrategy.ALL, mPolygonImageView3);
                tvNick1.setText(rankListResponseData.get(0).getNickName());
                tvNick2.setText(rankListResponseData.get(1).getNickName());
                tvNick3.setText(rankListResponseData.get(2).getNickName());
                tvCount1.setText(FHUtils.intToF(rankListResponseData.get(0).getObtainHB()));
                tvCount2.setText(FHUtils.intToF(rankListResponseData.get(1).getObtainHB()));
                tvCount3.setText(FHUtils.intToF(rankListResponseData.get(2).getObtainHB()));
                tvAwardCount1.setText(FHUtils.intToF(rankListResponseData.get(0).getAwardCnt()));
                tvAwardCount2.setText(FHUtils.intToF(rankListResponseData.get(1).getAwardCnt()));
                tvAwardCount3.setText(FHUtils.intToF(rankListResponseData.get(2).getAwardCnt()));

                if (rankListResponseData.get(0).isLiveStatus()) {
                    ivLive1.setVisibility(View.VISIBLE);
                } else {
                    ivLive1.setVisibility(View.GONE);
                }

                if (rankListResponseData.get(1).isLiveStatus()) {
                    ivLive2.setVisibility(View.VISIBLE);
                } else {
                    ivLive2.setVisibility(View.GONE);
                }

                if (rankListResponseData.get(2).isLiveStatus()) {
                    ivLive3.setVisibility(View.VISIBLE);
                } else {
                    ivLive3.setVisibility(View.GONE);
                }

            }

            if (rankListResponseData.size() == 2) {
                mRelativeLayout1.setVisibility(View.VISIBLE);
                mRelativeLayout2.setVisibility(View.VISIBLE);
                mRelativeLayout3.setVisibility(View.INVISIBLE);
                GlideApp.loadImg(getContext(), rankListResponseData.get(0).getHeadUrl(), DiskCacheStrategy.ALL, mPolygonImageView1);
                GlideApp.loadImg(getContext(), rankListResponseData.get(1).getHeadUrl(), DiskCacheStrategy.ALL, mPolygonImageView2);
                tvNick1.setText(rankListResponseData.get(0).getNickName());
                tvNick2.setText(rankListResponseData.get(1).getNickName());
                tvCount1.setText(String.valueOf(FHUtils.intToF(rankListResponseData.get(0).getObtainHB())));
                tvCount2.setText(String.valueOf(FHUtils.intToF(rankListResponseData.get(1).getObtainHB())));
                tvAwardCount1.setText(FHUtils.intToF(rankListResponseData.get(0).getAwardCnt()));
                tvAwardCount2.setText(FHUtils.intToF(rankListResponseData.get(1).getAwardCnt()));
                if (rankListResponseData.get(0).isLiveStatus()) {
                    ivLive1.setVisibility(View.VISIBLE);
                } else {
                    ivLive1.setVisibility(View.GONE);
                }

                if (rankListResponseData.get(1).isLiveStatus()) {
                    ivLive2.setVisibility(View.VISIBLE);
                } else {
                    ivLive2.setVisibility(View.GONE);
                }
            }
            if (rankListResponseData.size() == 1) {
                mRelativeLayout1.setVisibility(View.VISIBLE);
                mRelativeLayout2.setVisibility(View.INVISIBLE);
                mRelativeLayout3.setVisibility(View.INVISIBLE);
                GlideApp.loadImg(getContext(), rankListResponseData.get(0).getHeadUrl(), DiskCacheStrategy.ALL, mPolygonImageView1);
                tvNick1.setText(rankListResponseData.get(0).getNickName());
                tvCount1.setText(FHUtils.intToF(rankListResponseData.get(0).getObtainHB()));
                tvAwardCount1.setText(FHUtils.intToF(rankListResponseData.get(0).getAwardCnt()));

                if (rankListResponseData.get(0).isLiveStatus()) {
                    ivLive1.setVisibility(View.VISIBLE);
                } else {
                    ivLive1.setVisibility(View.GONE);
                }
            }
            isFirstLoad = false;
            mLuckRankListResponseDatas.addAll(rankListResponseData);
            // 列表数据从第四个开始
            if (rankListResponseData.size() > 3) {
                for (int i = 3; i < rankListResponseData.size(); i++) {
                    mAdapterDatas.add(rankListResponseData.get(i));
                }
                mAdapter.setNewData(mAdapterDatas);
            }
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
        mPresenter.loadLuckRankList(rankType);
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        mPresenter.loadLuckRankList(rankType);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.polygonImageView_1:
                goLiveRoom(mLuckRankListResponseDatas.get(0).getUserId(), mLuckRankListResponseDatas.get(0).getHeadUrl(), mLuckRankListResponseDatas.get(0).getBroadcastType(), true, 100);
                break;
            case R.id.polygonImageView_2:
                goLiveRoom(mLuckRankListResponseDatas.get(1).getUserId(), mLuckRankListResponseDatas.get(1).getHeadUrl(), mLuckRankListResponseDatas.get(1).getBroadcastType(), true, 100);
                break;
            case R.id.polygonImageView_3:
                goLiveRoom(mLuckRankListResponseDatas.get(2).getUserId(), mLuckRankListResponseDatas.get(2).getHeadUrl(), mLuckRankListResponseDatas.get(2).getBroadcastType(), true, 100);
                break;
            default:
                break;
        }
    }

    private void goLiveRoom(String userId, String headUrl, int broadType, boolean isShowWindow, int requestCode) {
        if (userId.equals(FeihuZhiboApplication.getApplication().mDataManager.getCurrentUserId())) {
            return;
        }
        if ("2".equals(rankType)) {
            AppUtils.startLiveActivity(TotalListFragment.this, userId, headUrl, broadType, isShowWindow, requestCode);
        } else {
            if (mPresenter.isGuestUser()) {
                if (BuildConfig.isForceLoad.equals("1")) {
                    CustomDialogUtils.showQZLoginDialog(getActivity(), false);
                } else {
                    CustomDialogUtils.showLoginDialog(getActivity(), false);
                }
            } else {
                Intent intent = new Intent(getContext(), OthersCommunityActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        }
    }
}
