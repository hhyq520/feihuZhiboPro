package cn.feihutv.zhibofeihu.ui.bangdan;

import android.graphics.Bitmap;
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

import cn.feihutv.zhibofeihu.BuildConfig;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;

import cn.feihutv.zhibofeihu.data.network.http.model.bangdan.GetGuardRankResponse;
import cn.feihutv.zhibofeihu.ui.bangdan.adapter.GuardAdapter;
import cn.feihutv.zhibofeihu.ui.base.BaseFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.ui.widget.StatusView;
import cn.feihutv.zhibofeihu.ui.widget.swiperefreshlayout.VRefreshLayout;
import cn.feihutv.zhibofeihu.utils.AppUtils;
import cn.feihutv.zhibofeihu.utils.CustomDialogUtils;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.ListViewDecoration;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.feihutv.zhibofeihu.utils.glideutil.GlideApp;

/**
 * <pre>
 *     @author : liwen.chen
 *     time   : 2017/
 *     desc   : 守护榜
 *     version: 1.0
 * </pre>
 */
public class GuardFragment extends BaseFragment implements GuardMvpView, StatusView.ClickRefreshListener, VRefreshLayout.OnRefreshListener, View.OnClickListener {

    @Inject
    GuardMvpPresenter<GuardMvpView> mPresenter;

    @BindView(R.id.guard_swip)
    VRefreshLayout mRefreshLayout;

    @BindView(R.id.guard_rv)
    RecyclerView mRecyclerView;

    @BindView(R.id.guard_status_view)
    StatusView mStatusView;

    private boolean isPrepared;

    private boolean isFirstLoad = true;

    private List<GetGuardRankResponse.GetGuardRankResponseData> mGuardRankResponseDatas = new ArrayList<>();

    private List<GetGuardRankResponse.GetGuardRankResponseData> mAdapterDatas = new ArrayList<>();

    private GuardAdapter mAdapter;

    private boolean isRefresh;

    private ImageView mPolygonImageView1, mPolygonImageView2, mPolygonImageView3;
    private ImageView ivLevel1, ivLevel2, ivLevel3, ivVip1, ivVip2, ivVip3, ivBeauti1, ivBeauti2, ivBeauti3, ivLive1, ivLive2, ivLive3;
    private TextView tvNick1, tvNick2, tvNick3, tvCount1, tvCount2, tvCount3;
    private View mHeadView;
    private RelativeLayout mRelativeLayout1, mRelativeLayout2, mRelativeLayout3;
    private String mCosVipIconRootPath;

    public static GuardFragment newInstance() {
        Bundle args = new Bundle();
        GuardFragment fragment = new GuardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_guard;
    }


    @Override
    protected void initWidget(View view) {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this, view));

        //Presenter调用初始化
        mPresenter.onAttach(GuardFragment.this);

        isPrepared = true;

        mAdapter = new GuardAdapter(mAdapterDatas);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //添加分隔线
        mRecyclerView.addItemDecoration(new ListViewDecoration());
        mRecyclerView.setAdapter(mAdapter);

        mStatusView.setClickRefreshListener(this);

        mRefreshLayout.addOnRefreshListener(this);

        mCosVipIconRootPath = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosVipIconRootPath();

        mHeadView = LayoutInflater.from(getActivity()).inflate(R.layout.view_head_guard, null);
        mPolygonImageView1 = (ImageView) mHeadView.findViewById(R.id.polygonImageView_1);
        mPolygonImageView2 = (ImageView) mHeadView.findViewById(R.id.polygonImageView_2);
        mPolygonImageView3 = (ImageView) mHeadView.findViewById(R.id.polygonImageView_3);
        tvNick1 = (TextView) mHeadView.findViewById(R.id.tv_nick_1);
        tvNick2 = (TextView) mHeadView.findViewById(R.id.tv_nick_2);
        tvNick3 = (TextView) mHeadView.findViewById(R.id.tv_nick_3);
        ivLevel1 = (ImageView) mHeadView.findViewById(R.id.iv_level_1);
        ivLevel2 = (ImageView) mHeadView.findViewById(R.id.iv_level_2);
        ivLevel3 = (ImageView) mHeadView.findViewById(R.id.iv_level_3);
        ivVip1 = (ImageView) mHeadView.findViewById(R.id.iv_vip_1);
        ivVip2 = (ImageView) mHeadView.findViewById(R.id.iv_vip_2);
        ivVip3 = (ImageView) mHeadView.findViewById(R.id.iv_vip_3);
        ivBeauti1 = (ImageView) mHeadView.findViewById(R.id.iv_beauti_1);
        ivBeauti2 = (ImageView) mHeadView.findViewById(R.id.iv_beauti_2);
        ivBeauti3 = (ImageView) mHeadView.findViewById(R.id.iv_beauti_3);
        tvCount1 = (TextView) mHeadView.findViewById(R.id.tv_count1);
        tvCount2 = (TextView) mHeadView.findViewById(R.id.tv_count2);
        tvCount3 = (TextView) mHeadView.findViewById(R.id.tv_count3);
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
                goLiveRoom(mAdapterDatas.get(position).getUserId(), mAdapterDatas.get(position).getAvatar(), mAdapterDatas.get(position).getBroadcastType(), true, 100);
            }
        });

        mAdapter.addHeaderView(mHeadView);
    }

    @Override
    protected void lazyLoad() {
        if (isPrepared && isVisible && mGuardRankResponseDatas.size() == 0) {
            isPrepared = false;
            mPresenter.getGuardRank();
        }
    }


    @Override
    public void onDestroyView() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroyView();
    }


    @Override
    public void getDatas(List<GetGuardRankResponse.GetGuardRankResponseData> rankResponseDatas) {
        if (isRefresh) {
            if (isNetworkConnected()) {
                mGuardRankResponseDatas.clear();
                mAdapterDatas.clear();
                mRefreshLayout.refreshComplete();
                isRefresh = false;
            }
        }

        if (rankResponseDatas.size() > 0) {
            // 显示加载成功
            isFirstLoad = false;
            showDatas();
            if (rankResponseDatas.size() >= 3) {
                mRelativeLayout1.setVisibility(View.VISIBLE);
                mRelativeLayout2.setVisibility(View.VISIBLE);
                mRelativeLayout3.setVisibility(View.VISIBLE);

                GlideApp.loadImg(getContext(), rankResponseDatas.get(0).getAvatar(), DiskCacheStrategy.ALL, mPolygonImageView1);
                GlideApp.loadImg(getContext(), rankResponseDatas.get(1).getAvatar(), DiskCacheStrategy.ALL, mPolygonImageView2);
                GlideApp.loadImg(getContext(), rankResponseDatas.get(2).getAvatar(), DiskCacheStrategy.ALL, mPolygonImageView3);
                tvNick1.setText(TCUtils.getLimitString(rankResponseDatas.get(0).getNickname(), 5));
                tvNick2.setText(TCUtils.getLimitString(rankResponseDatas.get(1).getNickname(), 5));
                tvNick3.setText(TCUtils.getLimitString(rankResponseDatas.get(2).getNickname(), 5));
                tvCount1.setText(String.valueOf(FHUtils.intToPeople(rankResponseDatas.get(0).getGuardCnt())) + "人");
                tvCount2.setText(String.valueOf(FHUtils.intToPeople(rankResponseDatas.get(1).getGuardCnt())) + "人");
                tvCount3.setText(String.valueOf(FHUtils.intToPeople(rankResponseDatas.get(2).getGuardCnt())) + "人");
                TCUtils.showLevelWithUrl(getContext(), ivLevel1, rankResponseDatas.get(0).getLevel());
                TCUtils.showLevelWithUrl(getContext(), ivLevel2, rankResponseDatas.get(1).getLevel());
                TCUtils.showLevelWithUrl(getContext(), ivLevel3, rankResponseDatas.get(2).getLevel());
                if (rankResponseDatas.get(0).isLiang()) {
                    ivBeauti1.setVisibility(View.VISIBLE);
                } else {
                    ivBeauti1.setVisibility(View.GONE);
                }

                if (rankResponseDatas.get(1).isLiang()) {
                    ivBeauti2.setVisibility(View.VISIBLE);
                } else {
                    ivBeauti2.setVisibility(View.GONE);
                }

                if (rankResponseDatas.get(2).isLiang()) {
                    ivBeauti3.setVisibility(View.VISIBLE);
                } else {
                    ivBeauti3.setVisibility(View.GONE);
                }

                if (!rankResponseDatas.get(0).isVipExpired()) {
                    ivVip1.setVisibility(View.VISIBLE);
                    Glide.with(getContext()).load(mCosVipIconRootPath + "icon_vip" + rankResponseDatas.get(0).getVip() + ".png").into(ivVip1);
                } else {
                    ivVip1.setVisibility(View.GONE);
                }

                if (!rankResponseDatas.get(1).isVipExpired()) {
                    ivVip2.setVisibility(View.VISIBLE);
                    Glide.with(getContext()).load(mCosVipIconRootPath + "icon_vip" + rankResponseDatas.get(1).getVip() + ".png").into(ivVip2);
                } else {
                    ivVip2.setVisibility(View.GONE);
                }

                if (!rankResponseDatas.get(2).isVipExpired()) {
                    ivVip3.setVisibility(View.VISIBLE);
                    Glide.with(getContext()).load(mCosVipIconRootPath + "icon_vip" + rankResponseDatas.get(2).getVip() + ".png").into(ivVip3);
                } else {
                    ivVip3.setVisibility(View.GONE);
                }

                if (rankResponseDatas.get(0).isLiveStatus()) {
                    ivLive1.setVisibility(View.VISIBLE);
                } else {
                    ivLive1.setVisibility(View.GONE);
                }

                if (rankResponseDatas.get(1).isLiveStatus()) {
                    ivLive2.setVisibility(View.VISIBLE);
                } else {
                    ivLive2.setVisibility(View.GONE);
                }

                if (rankResponseDatas.get(2).isLiveStatus()) {
                    ivLive3.setVisibility(View.VISIBLE);
                } else {
                    ivLive3.setVisibility(View.GONE);
                }

            }

            if (rankResponseDatas.size() == 2) {
                mRelativeLayout1.setVisibility(View.VISIBLE);
                mRelativeLayout2.setVisibility(View.VISIBLE);
                mRelativeLayout3.setVisibility(View.INVISIBLE);
                GlideApp.loadImg(getContext(), rankResponseDatas.get(0).getAvatar(), DiskCacheStrategy.ALL, mPolygonImageView1);
                GlideApp.loadImg(getContext(), rankResponseDatas.get(1).getAvatar(), DiskCacheStrategy.ALL, mPolygonImageView2);
                tvNick1.setText(TCUtils.getLimitString(rankResponseDatas.get(0).getNickname(), 5));
                tvNick2.setText(TCUtils.getLimitString(rankResponseDatas.get(1).getNickname(), 5));
                tvCount1.setText(String.valueOf(FHUtils.intToPeople(rankResponseDatas.get(0).getGuardCnt())) + "人");
                tvCount2.setText(String.valueOf(FHUtils.intToPeople(rankResponseDatas.get(1).getGuardCnt())) + "人");
                TCUtils.showLevelWithUrl(getContext(), ivLevel1, rankResponseDatas.get(0).getLevel());
                TCUtils.showLevelWithUrl(getContext(), ivLevel2, rankResponseDatas.get(1).getLevel());
                if (rankResponseDatas.get(0).isLiang()) {
                    ivBeauti1.setVisibility(View.VISIBLE);
                } else {
                    ivBeauti1.setVisibility(View.GONE);
                }

                if (rankResponseDatas.get(1).isLiang()) {
                    ivBeauti2.setVisibility(View.VISIBLE);
                } else {
                    ivBeauti2.setVisibility(View.GONE);
                }

                if (!rankResponseDatas.get(0).isVipExpired()) {
                    ivVip1.setVisibility(View.VISIBLE);
                    Glide.with(getContext()).load(mCosVipIconRootPath + "icon_vip" + rankResponseDatas.get(0).getVip() + ".png").into(ivVip1);
                } else {
                    ivVip1.setVisibility(View.GONE);
                }

                if (!rankResponseDatas.get(1).isVipExpired()) {
                    ivVip2.setVisibility(View.VISIBLE);
                    Glide.with(getContext()).load(mCosVipIconRootPath + "icon_vip" + rankResponseDatas.get(1).getVip() + ".png").into(ivVip2);
                } else {
                    ivVip2.setVisibility(View.GONE);
                }

                if (rankResponseDatas.get(0).isLiveStatus()) {
                    ivLive1.setVisibility(View.VISIBLE);
                } else {
                    ivLive1.setVisibility(View.GONE);
                }

                if (rankResponseDatas.get(1).isLiveStatus()) {
                    ivLive2.setVisibility(View.VISIBLE);
                } else {
                    ivLive2.setVisibility(View.GONE);
                }
            }
            if (rankResponseDatas.size() == 1) {
                mRelativeLayout1.setVisibility(View.VISIBLE);
                mRelativeLayout2.setVisibility(View.INVISIBLE);
                mRelativeLayout3.setVisibility(View.INVISIBLE);
                GlideApp.loadImg(getContext(), rankResponseDatas.get(0).getAvatar(), DiskCacheStrategy.ALL, mPolygonImageView1);
                tvNick1.setText(TCUtils.getLimitString(rankResponseDatas.get(0).getNickname(), 5));
                tvCount1.setText(FHUtils.intToPeople(rankResponseDatas.get(0).getGuardCnt()) + "人");
                TCUtils.showLevelWithUrl(getContext(), ivLevel1, rankResponseDatas.get(0).getLevel());
                if (!rankResponseDatas.get(0).isVipExpired()) {
                    ivVip1.setVisibility(View.VISIBLE);
                    Glide.with(getContext()).load(mCosVipIconRootPath + "icon_vip" + rankResponseDatas.get(0).getVip()+ ".png").into(ivVip1);
                } else {
                    ivVip1.setVisibility(View.GONE);
                }

                if (rankResponseDatas.get(0).isLiang()) {
                    ivBeauti1.setVisibility(View.VISIBLE);
                } else {
                    ivBeauti1.setVisibility(View.GONE);
                }

                if (rankResponseDatas.get(0).isLiveStatus()) {
                    ivLive1.setVisibility(View.VISIBLE);
                } else {
                    ivLive1.setVisibility(View.GONE);
                }
            }
            isFirstLoad = false;
            mGuardRankResponseDatas.addAll(rankResponseDatas);
            // 列表数据从第四个开始
            if (rankResponseDatas.size() > 3) {
                for (int i = 3; i < rankResponseDatas.size(); i++) {
                    mAdapterDatas.add(rankResponseDatas.get(i));
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
    public void clickTorefresh() {
        mPresenter.getGuardRank();
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        mPresenter.getGuardRank();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.polygonImageView_1:
                goLiveRoom(mGuardRankResponseDatas.get(0).getUserId(), mGuardRankResponseDatas.get(0).getAvatar(), mGuardRankResponseDatas.get(0).getBroadcastType(), true, 100);
                break;
            case R.id.polygonImageView_2:
                goLiveRoom(mGuardRankResponseDatas.get(1).getUserId(), mGuardRankResponseDatas.get(1).getAvatar(), mGuardRankResponseDatas.get(1).getBroadcastType(), true, 100);
                break;
            case R.id.polygonImageView_3:
                goLiveRoom(mGuardRankResponseDatas.get(2).getUserId(), mGuardRankResponseDatas.get(2).getAvatar(), mGuardRankResponseDatas.get(2).getBroadcastType(), true, 100);
                break;
            default:
                break;
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

    private void goLiveRoom(String userId, String headUrl, int broadType, boolean isShowWindow, int requestCode) {
        if (!userId.equals(FeihuZhiboApplication.getApplication().mDataManager.getCurrentUserId())) {
            AppUtils.startLiveActivity(GuardFragment.this, userId, headUrl, broadType, isShowWindow, requestCode);
        }
    }
}
