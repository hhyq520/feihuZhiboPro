package cn.feihutv.zhibofeihu.ui.bangdan;

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
import cn.feihutv.zhibofeihu.data.network.http.model.bangdan.LoadContriRankListResponse;
import cn.feihutv.zhibofeihu.ui.bangdan.adapter.RankAdapter;
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
 *     desc   : 排行榜
 *     version: 1.0
 * </pre>
 */
public class RankingFragment extends BaseFragment implements RankingMvpView, StatusView.ClickRefreshListener, VRefreshLayout.OnRefreshListener, View.OnClickListener {

    @Inject
    RankingMvpPresenter<RankingMvpView> mPresenter;

    private boolean isFirstLoad = true;

    private boolean isPrepared;//当前页面是否加载

    private static final String RANKTYPE = "rankType";

    private List<LoadContriRankListResponse.LoadContriRankListResponseData> mRankListResponseDatas = new ArrayList<>();
    private List<LoadContriRankListResponse.LoadContriRankListResponseData> mAdapterDatas = new ArrayList<>();

    @BindView(R.id.bangdan_rv)
    RecyclerView mRecyclerView;

    @BindView(R.id.status_view)
    StatusView mStatusView;

    @BindView(R.id.bangdan_swip)
    VRefreshLayout mRefreshLayout;

    private RankAdapter mAdapter;
    private String mRankType;

    private boolean isRefresh;
    private ImageView mPolygonImageView1, mPolygonImageView2, mPolygonImageView3;
    private ImageView ivLevel1, ivLevel2, ivLevel3, ivVip1, ivVip2, ivVip3, ivLive1, ivLive2, ivLive3;
    private TextView tvNick1, tvNick2, tvNick3, tvCount1, tvCount2, tvCount3, tvFont1, tvFont2, tvFont3;
    private View mHeadView;
    private RelativeLayout mRelativeLayout1, mRelativeLayout2, mRelativeLayout3;
    private String mCosVipIconRootPath;

    public static RankingFragment newInstance(String rankType) {
        RankingFragment fragment = new RankingFragment();
        Bundle args = new Bundle();
        args.putString(RANKTYPE, rankType);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ranking;
    }


    @Override
    protected void initWidget(View view) {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this, view));

        //Presenter调用初始化
        mPresenter.onAttach(RankingFragment.this);

        isPrepared = true;

        mAdapter = new RankAdapter(mAdapterDatas);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //添加分隔线
        mRecyclerView.addItemDecoration(new ListViewDecoration());
        mRecyclerView.setAdapter(mAdapter);

        mStatusView.setClickRefreshListener(this);

        mRefreshLayout.addOnRefreshListener(this);

        mCosVipIconRootPath = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosVipIconRootPath();

        mHeadView = LayoutInflater.from(getActivity()).inflate(R.layout.view_head_bangdan, null);
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
        tvFont1 = mHeadView.findViewById(R.id.tv_ttf1);
        tvFont2 = mHeadView.findViewById(R.id.tv_ttf2);
        tvFont3 = mHeadView.findViewById(R.id.tv_ttf3);
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
                goLiveRoom(mAdapterDatas.get(position).getUserId(), mAdapterDatas.get(position).getHeadUrl(), mAdapterDatas.get(position).getBroadcastType(), true, 100);
            }
        });

        mAdapter.addHeaderView(mHeadView);
    }

    @Override
    protected void lazyLoad() {
        Bundle bundle = getArguments();
        mRankType = bundle.getString(RANKTYPE);
        if (isPrepared && isVisible && mRankListResponseDatas.size() == 0) {
            isPrepared = false;
            mPresenter.loadIncomeRankList(mRankType);
        }
    }

    @Override
    public void onDestroyView() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroyView();
    }


    @Override
    public void getDatas(List<LoadContriRankListResponse.LoadContriRankListResponseData> rankListResponseData) {
        if (isRefresh) {
            if (isNetworkConnected()) {
                mRankListResponseDatas.clear();
                mAdapterDatas.clear();
                mRefreshLayout.refreshComplete();
                isRefresh = false;
            }
        }
        if (rankListResponseData.size() >= 3) {
            mRelativeLayout1.setVisibility(View.VISIBLE);
            mRelativeLayout2.setVisibility(View.VISIBLE);
            mRelativeLayout3.setVisibility(View.VISIBLE);
            GlideApp.loadImg(getContext(), rankListResponseData.get(0).getHeadUrl(), DiskCacheStrategy.ALL, mPolygonImageView1);
            GlideApp.loadImg(getContext(), rankListResponseData.get(1).getHeadUrl(), DiskCacheStrategy.ALL, mPolygonImageView2);
            GlideApp.loadImg(getContext(), rankListResponseData.get(2).getHeadUrl(), DiskCacheStrategy.ALL, mPolygonImageView3);
            tvNick1.setText(TCUtils.getLimitString(rankListResponseData.get(0).getNickName(), 5));
            tvNick2.setText(TCUtils.getLimitString(rankListResponseData.get(1).getNickName(), 5));
            tvNick3.setText(TCUtils.getLimitString(rankListResponseData.get(2).getNickName(), 5));
            tvCount1.setText(String.valueOf(FHUtils.intToF(rankListResponseData.get(0).gethB())));
            tvCount2.setText(String.valueOf(FHUtils.intToF(rankListResponseData.get(1).gethB())));
            tvCount3.setText(String.valueOf(FHUtils.intToF(rankListResponseData.get(2).gethB())));
            TCUtils.showLevelWithUrl(getContext(), ivLevel1, rankListResponseData.get(0).getLevel());
            TCUtils.showLevelWithUrl(getContext(), ivLevel2, rankListResponseData.get(1).getLevel());
            TCUtils.showLevelWithUrl(getContext(), ivLevel3, rankListResponseData.get(2).getLevel());

            if (rankListResponseData.get(0).isLiang()) {
                tvFont1.setText(rankListResponseData.get(0).getShowId());
                TCUtils.setFontBeauti(getContext(), tvFont1);
            } else {
                tvFont1.setText(rankListResponseData.get(0).getShowId());
                TCUtils.setFontNormal(getContext(), tvFont1);
            }

            if (rankListResponseData.get(1).isLiang()) {
                tvFont2.setText(rankListResponseData.get(1).getShowId());
                TCUtils.setFontBeauti(getContext(), tvFont2);
            } else {
                tvFont2.setText(rankListResponseData.get(1).getShowId());
                TCUtils.setFontNormal(getContext(), tvFont2);
            }

            if (rankListResponseData.get(2).isLiang()) {
                tvFont3.setText(rankListResponseData.get(2).getShowId());
                TCUtils.setFontBeauti(getContext(), tvFont3);
            } else {
                tvFont3.setText(rankListResponseData.get(2).getShowId());
                TCUtils.setFontNormal(getContext(), tvFont3);
            }

            if (!rankListResponseData.get(0).isVipExpired()) {
                ivVip1.setVisibility(View.VISIBLE);
                Glide.with(getContext()).load(mCosVipIconRootPath + "icon_vip" + rankListResponseData.get(0).getVip() + ".png").into(ivVip1);
            } else {
                ivVip1.setVisibility(View.GONE);
            }

            if (!rankListResponseData.get(1).isVipExpired()) {
                ivVip2.setVisibility(View.VISIBLE);
                Glide.with(getContext()).load(mCosVipIconRootPath + "icon_vip" + rankListResponseData.get(1).getVip() + ".png").into(ivVip2);
            } else {
                ivVip2.setVisibility(View.GONE);
            }

            if (!rankListResponseData.get(2).isVipExpired()) {
                ivVip3.setVisibility(View.VISIBLE);
                Glide.with(getContext()).load(mCosVipIconRootPath + "icon_vip" + rankListResponseData.get(2).getVip() + ".png").into(ivVip3);
            } else {
                ivVip3.setVisibility(View.GONE);
            }

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
            GlideApp.loadImg(getContext(), rankListResponseData.get(1).getHeadUrl(), DiskCacheStrategy.ALL, mPolygonImageView1);
            tvNick1.setText(TCUtils.getLimitString(rankListResponseData.get(0).getNickName(), 5));
            tvNick2.setText(TCUtils.getLimitString(rankListResponseData.get(1).getNickName(), 5));
            tvCount1.setText(String.valueOf(FHUtils.intToF(rankListResponseData.get(0).gethB())));
            tvCount2.setText(String.valueOf(FHUtils.intToF(rankListResponseData.get(1).gethB())));
            TCUtils.showLevelWithUrl(getContext(), ivLevel1, rankListResponseData.get(0).getLevel());
            TCUtils.showLevelWithUrl(getContext(), ivLevel2, rankListResponseData.get(1).getLevel());
            if (rankListResponseData.get(0).isLiang()) {
                tvFont1.setText(rankListResponseData.get(0).getShowId());
                TCUtils.setFontBeauti(getContext(), tvFont1);
            } else {
                tvFont1.setText(rankListResponseData.get(0).getShowId());
                TCUtils.setFontNormal(getContext(), tvFont1);
            }

            if (rankListResponseData.get(1).isLiang()) {
                tvFont2.setText(rankListResponseData.get(1).getShowId());
                TCUtils.setFontBeauti(getContext(), tvFont2);
            } else {
                tvFont2.setText(rankListResponseData.get(1).getShowId());
                TCUtils.setFontNormal(getContext(), tvFont2);
            }

            if (!rankListResponseData.get(0).isVipExpired()) {
                ivVip1.setVisibility(View.VISIBLE);
                Glide.with(getContext()).load(mCosVipIconRootPath + "icon_vip" + rankListResponseData.get(0).getVip() + ".png").into(ivVip1);
            } else {
                ivVip1.setVisibility(View.GONE);
            }

            if (!rankListResponseData.get(1).isVipExpired()) {
                ivVip2.setVisibility(View.VISIBLE);
                Glide.with(getContext()).load(mCosVipIconRootPath + "icon_vip" + rankListResponseData.get(1).getVip() + ".png").into(ivVip2);
            } else {
                ivVip2.setVisibility(View.GONE);
            }

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
            tvNick1.setText(TCUtils.getLimitString(rankListResponseData.get(0).getNickName(), 5));
            tvCount1.setText(FHUtils.intToF(rankListResponseData.get(0).gethB()));
            TCUtils.showLevelWithUrl(getContext(), ivLevel1, rankListResponseData.get(0).getLevel());

            if (!rankListResponseData.get(0).isVipExpired()) {
                ivVip1.setVisibility(View.VISIBLE);
                Glide.with(getContext()).load(mCosVipIconRootPath + "icon_vip" + rankListResponseData.get(0).getVip() + ".png").into(ivVip1);
            } else {
                ivVip1.setVisibility(View.GONE);
            }

            if (rankListResponseData.get(0).isLiang()) {
                tvFont1.setText(rankListResponseData.get(0).getShowId());
                TCUtils.setFontBeauti(getContext(), tvFont1);
            } else {
                tvFont1.setText(rankListResponseData.get(0).getShowId());
                TCUtils.setFontNormal(getContext(), tvFont1);
            }

            if (rankListResponseData.get(0).isLiveStatus()) {
                ivLive1.setVisibility(View.VISIBLE);
            } else {
                ivLive1.setVisibility(View.GONE);
            }
        }
        isFirstLoad = false;
        mRankListResponseDatas.addAll(rankListResponseData);
        // 列表数据从第四个开始
        if (rankListResponseData.size() > 3) {
            for (int i = 3; i < rankListResponseData.size(); i++) {
                mAdapterDatas.add(rankListResponseData.get(i));
            }
            mAdapter.setNewData(mAdapterDatas);
        }

    }

    @Override
    public void showErrorView() {
        isRefresh = false;
        mRefreshLayout.refreshComplete();
        if (isFirstLoad) {
            mRefreshLayout.setVisibility(View.GONE);
            mStatusView.showErrorView();
            hideLoading();
        }
    }

    @Override
    public void showNodataView() {
        if (isFirstLoad) {
            mRefreshLayout.setVisibility(View.GONE);
            mStatusView.showNoDataView();
            mStatusView.setTvNoData("榜单主播被绑架了，快用礼物换回Ta");
            mStatusView.setNoDataBg(R.drawable.img_no_bd);
            hideLoading();
        }
    }

    @Override
    public void showProgressView() {
        if (isFirstLoad) {
            mRefreshLayout.setVisibility(View.GONE);
            showLoading();
        }
    }

    @Override
    public void showContentView() {
        mRefreshLayout.setVisibility(View.VISIBLE);
        mStatusView.setVisibility(View.GONE);
        hideLoading();
    }


    @Override
    public void clickTorefresh() {
        mPresenter.loadIncomeRankList(mRankType);
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        mPresenter.loadIncomeRankList(mRankType);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.polygonImageView_1:
                goLiveRoom(mRankListResponseDatas.get(0).getUserId(), mRankListResponseDatas.get(0).getHeadUrl(), mRankListResponseDatas.get(0).getBroadcastType(), true, 100);
                break;
            case R.id.polygonImageView_2:
                goLiveRoom(mRankListResponseDatas.get(1).getUserId(), mRankListResponseDatas.get(1).getHeadUrl(), mRankListResponseDatas.get(1).getBroadcastType(), true, 100);
                break;
            case R.id.polygonImageView_3:
                goLiveRoom(mRankListResponseDatas.get(2).getUserId(), mRankListResponseDatas.get(2).getHeadUrl(), mRankListResponseDatas.get(2).getBroadcastType(), true, 100);
                break;
            default:
                break;
        }
    }

    private void goLiveRoom(String userId, String headUrl, int broadType, boolean isShowWindow, int requestCode) {
        if (!userId.equals(FeihuZhiboApplication.getApplication().mDataManager.getCurrentUserId())) {
            AppUtils.startLiveActivity(RankingFragment.this, userId, headUrl, broadType, isShowWindow, requestCode);
        }
    }
}
