package cn.feihutv.zhibofeihu.ui.me;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.bangdan.LoadContriRankListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomContriResponce;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.me.adapter.ContriAdapter;
import cn.feihutv.zhibofeihu.ui.me.dynamic.OthersCommunityActivity;
import cn.feihutv.zhibofeihu.ui.widget.StatusView;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.ListViewDecoration;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.feihutv.zhibofeihu.utils.glideutil.GlideApp;

/**
 * <pre>
 *     @author : liwen.chen
 *     time   :
 *     desc   : 我的---贡献榜
 *     version: 1.0
 * </pre>
 */
public class ContributionListActivity extends BaseActivity implements ContributionListMvpView, SwipeRefreshLayout.OnRefreshListener, StatusView.ClickRefreshListener, View.OnClickListener {

    @Inject
    ContributionListMvpPresenter<ContributionListMvpView> mPresenter;

    @BindView(R.id.con_back)
    TextView tvBack;

    @BindView(R.id.contri_more)
    TextView tvMore;

    @BindView(R.id.con_swip)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.con_rv)
    RecyclerView mRecyclerView;

    @BindView(R.id.status_view)
    StatusView mStatusView;

    private boolean isFirstLoad = true;

    private boolean isRefresh;

    private int rankType = 1;

    private PopupWindow mPopWindow;

    private View mHeadView;

    private ImageView mPolygonImageView1, mPolygonImageView2, mPolygonImageView3;

    private String mCosVipIconRootPath;

    private ContriAdapter mAdapter;


    private String noDataStr = "暂无贡献哦，马上开播吸引粉丝送礼吧~";

    private TextView tvNick1, tvNick2, tvNick3, tvCount1, tvCount2, tvCount3;

    private ImageView ivLevel1, ivLevel2, ivLevel3, ivVip1, ivVip2, ivVip3, ivBeauti1, ivBeauti2, ivBeauti3;
    private RelativeLayout mRelativeLayout1, mRelativeLayout2, mRelativeLayout3;

    private List<LoadRoomContriResponce.RoomContriData> mRoomContriDatas = new ArrayList<>();
    private List<LoadRoomContriResponce.RoomContriData> mAdapterDatas = new ArrayList<>();

    private String userId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_contribution_list;
    }


    @Override
    protected void initWidget() {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this));

        //persenter调用初始化
        mPresenter.onAttach(ContributionListActivity.this);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        noDataStr = intent.getStringExtra("noDataTitle");

        tvBack.setOnClickListener(this);
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

        mAdapter = new ContriAdapter(mAdapterDatas);
        mRecyclerView.setAdapter(mAdapter);

        mRefreshLayout.setOnRefreshListener(this);

        mStatusView.setClickRefreshListener(this);

        mCosVipIconRootPath = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosVipIconRootPath();

        mHeadView = LayoutInflater.from(getActivity()).inflate(R.layout.view_head_me_contri, null);
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

        mPolygonImageView1.setOnClickListener(this);
        mPolygonImageView2.setOnClickListener(this);
        mPolygonImageView3.setOnClickListener(this);

        mRelativeLayout1 = (RelativeLayout) mHeadView.findViewById(R.id.rl_1);
        mRelativeLayout2 = (RelativeLayout) mHeadView.findViewById(R.id.rl_2);
        mRelativeLayout3 = (RelativeLayout) mHeadView.findViewById(R.id.rl_3);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                goOtherCommu(mAdapterDatas.get(position).getUserId());
            }
        });

        mAdapter.addHeaderView(mHeadView);

        mPresenter.loadRoomContriList(userId, rankType);
    }


    @OnClick(R.id.contri_more)
    public void onViewClicked() {
        if (mPopWindow != null) {
            if (mPopWindow.isShowing()) {
                mPopWindow.dismiss();
                mPopWindow = null;
                showPopupWindow();
            } else {
                showPopupWindow();
            }
        } else {
            showPopupWindow();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pop_day:
                tvMore.setText("日榜");
                mPopWindow.dismiss();
                rankType = 1;
                isFirstLoad = true;
                isRefresh = false;
                mRoomContriDatas.clear();
                mAdapterDatas.clear();
                mPresenter.loadRoomContriList(userId, rankType);
                break;
            case R.id.pop_month:
                tvMore.setText("月榜");
                mPopWindow.dismiss();
                rankType = 2;
                isFirstLoad = true;
                isRefresh = false;
                mRoomContriDatas.clear();
                mAdapterDatas.clear();
                mPresenter.loadRoomContriList(userId, rankType);
                break;
            case R.id.pop_total:
                tvMore.setText("总榜");
                mPopWindow.dismiss();
                rankType = 3;
                isFirstLoad = true;
                isRefresh = false;
                mRoomContriDatas.clear();
                mAdapterDatas.clear();
                mPresenter.loadRoomContriList(userId, rankType);
                break;
            case R.id.polygonImageView_1:
                goOtherCommu(mRoomContriDatas.get(0).getUserId());
                break;
            case R.id.polygonImageView_2:
                goOtherCommu(mRoomContriDatas.get(1).getUserId());
                break;
            case R.id.polygonImageView_3:
                goOtherCommu(mRoomContriDatas.get(2).getUserId());
                break;
            case R.id.con_back:
                finish();
                break;
            default:
                break;
        }
    }

    private void showPopupWindow() {
        View contentView = LayoutInflater.from(ContributionListActivity.this).inflate(R.layout.dialog_bangdan_choose, null);
        mPopWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        TextView tv1 = (TextView) contentView.findViewById(R.id.pop_day);
        TextView tv2 = (TextView) contentView.findViewById(R.id.pop_month);
        TextView tv3 = (TextView) contentView.findViewById(R.id.pop_total);
        if (rankType == 2) {
            tv1.setTextColor(ContextCompat.getColor(this, R.color.text_gray));
            tv2.setTextColor(ContextCompat.getColor(this, R.color.btn_sure_forbidden));
            tv3.setTextColor(ContextCompat.getColor(this, R.color.text_gray));
        } else if (rankType == 3) {
            tv1.setTextColor(ContextCompat.getColor(this, R.color.text_gray));
            tv2.setTextColor(ContextCompat.getColor(this, R.color.text_gray));
            tv3.setTextColor(ContextCompat.getColor(this, R.color.btn_sure_forbidden));
        } else {
            tv1.setTextColor(ContextCompat.getColor(this, R.color.btn_sure_forbidden));
            tv2.setTextColor(ContextCompat.getColor(this, R.color.text_gray));
            tv3.setTextColor(ContextCompat.getColor(this, R.color.text_gray));
        }
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopWindow.setOutsideTouchable(true);
        mPopWindow.showAsDropDown(tvMore, 0, 20);
    }

    @Override
    public void showNoData() {
        if (isFirstLoad) {
            mRefreshLayout.setVisibility(View.GONE);
            mStatusView.setVisibility(View.VISIBLE);
            mStatusView.showNoDataView();
            mStatusView.setTvNoData(noDataStr);
            hideLoading();
        }
    }

    @Override
    public void showContent() {
        mRefreshLayout.setVisibility(View.VISIBLE);
        mStatusView.setVisibility(View.GONE);
        hideLoading();
    }

    @Override
    public void showErrorView() {
        isRefresh = false;
        mRefreshLayout.setRefreshing(false);
        if (isFirstLoad) {
            mRefreshLayout.setVisibility(View.GONE);
            mStatusView.setVisibility(View.VISIBLE);
            mStatusView.showErrorView();
            hideLoading();
        }
    }

    @Override
    public void getDatas(List<LoadRoomContriResponce.RoomContriData> roomContriDataList) {
        if (isRefresh) {
            if (isNetworkConnected()) {
                mRoomContriDatas.clear();
                mAdapterDatas.clear();
                mRefreshLayout.setRefreshing(false);
                isRefresh = false;
            }
        }

        isFirstLoad = false;
        mRoomContriDatas.addAll(roomContriDataList);
        // 显示加载成功
        if (mRoomContriDatas.size() >= 3) {
            mRelativeLayout1.setVisibility(View.VISIBLE);
            mRelativeLayout2.setVisibility(View.VISIBLE);
            mRelativeLayout3.setVisibility(View.VISIBLE);

            GlideApp.loadImg(getActivity(), mRoomContriDatas.get(0).getHeadUrl(), DiskCacheStrategy.ALL, mPolygonImageView1);
            GlideApp.loadImg(getActivity(), mRoomContriDatas.get(1).getHeadUrl(), DiskCacheStrategy.ALL, mPolygonImageView2);
            GlideApp.loadImg(getActivity(), mRoomContriDatas.get(2).getHeadUrl(), DiskCacheStrategy.ALL, mPolygonImageView3);
            tvNick1.setText(TCUtils.getLimitString(mRoomContriDatas.get(0).getNickName(), 5));
            tvNick2.setText(TCUtils.getLimitString(mRoomContriDatas.get(1).getNickName(), 5));
            tvNick3.setText(TCUtils.getLimitString(mRoomContriDatas.get(2).getNickName(), 5));
            tvCount1.setText(String.valueOf(FHUtils.intToF(mRoomContriDatas.get(0).getHB())));
            tvCount2.setText(String.valueOf(FHUtils.intToF(mRoomContriDatas.get(1).getHB())));
            tvCount3.setText(String.valueOf(FHUtils.intToF(mRoomContriDatas.get(2).getHB())));
            TCUtils.showLevelWithUrl(getActivity(), ivLevel1, mRoomContriDatas.get(0).getLevel());
            TCUtils.showLevelWithUrl(getActivity(), ivLevel2, mRoomContriDatas.get(1).getLevel());
            TCUtils.showLevelWithUrl(getActivity(), ivLevel3, mRoomContriDatas.get(2).getLevel());
            if (mRoomContriDatas.get(0).isLiang()) {
                ivBeauti1.setVisibility(View.VISIBLE);
            } else {
                ivBeauti1.setVisibility(View.GONE);
            }

            if (mRoomContriDatas.get(1).isLiang()) {
                ivBeauti2.setVisibility(View.VISIBLE);
            } else {
                ivBeauti2.setVisibility(View.GONE);
            }

            if (mRoomContriDatas.get(2).isLiang()) {
                ivBeauti3.setVisibility(View.VISIBLE);
            } else {
                ivBeauti3.setVisibility(View.GONE);
            }

            if (!mRoomContriDatas.get(0).isVipExpired()) {
                ivVip1.setVisibility(View.VISIBLE);
                Glide.with(getActivity()).load(mCosVipIconRootPath + "icon_vip" + mRoomContriDatas.get(0).getVip() + ".png").into(ivVip1);
            } else {
                ivVip1.setVisibility(View.GONE);
            }

            if (!mRoomContriDatas.get(1).isVipExpired()) {
                ivVip2.setVisibility(View.VISIBLE);
                Glide.with(getActivity()).load(mCosVipIconRootPath + "icon_vip" + mRoomContriDatas.get(1).getVip() + ".png").into(ivVip2);
            } else {
                ivVip2.setVisibility(View.GONE);
            }

            if (!mRoomContriDatas.get(2).isVipExpired()) {
                ivVip3.setVisibility(View.VISIBLE);
                Glide.with(getActivity()).load(mCosVipIconRootPath + "icon_vip" + mRoomContriDatas.get(2).getVip() + ".png").into(ivVip3);
            } else {
                ivVip3.setVisibility(View.GONE);
            }

        }

        if (mRoomContriDatas.size() == 2) {
            mRelativeLayout1.setVisibility(View.VISIBLE);
            mRelativeLayout2.setVisibility(View.VISIBLE);
            mRelativeLayout3.setVisibility(View.INVISIBLE);
            GlideApp.loadImg(getActivity(), mRoomContriDatas.get(0).getHeadUrl(), DiskCacheStrategy.ALL, mPolygonImageView1);
            GlideApp.loadImg(getActivity(), mRoomContriDatas.get(1).getHeadUrl(), DiskCacheStrategy.ALL, mPolygonImageView2);
            tvNick1.setText(TCUtils.getLimitString(mRoomContriDatas.get(0).getNickName(), 5));
            tvNick2.setText(TCUtils.getLimitString(mRoomContriDatas.get(1).getNickName(), 5));
            tvCount1.setText(String.valueOf(FHUtils.intToF(mRoomContriDatas.get(0).getHB())));
            tvCount2.setText(String.valueOf(FHUtils.intToF(mRoomContriDatas.get(1).getHB())));
            TCUtils.showLevelWithUrl(getActivity(), ivLevel1, mRoomContriDatas.get(0).getLevel());
            TCUtils.showLevelWithUrl(getActivity(), ivLevel2, mRoomContriDatas.get(1).getLevel());
            if (mRoomContriDatas.get(0).isLiang()) {
                ivBeauti1.setVisibility(View.VISIBLE);
            } else {
                ivBeauti1.setVisibility(View.GONE);
            }

            if (mRoomContriDatas.get(1).isLiang()) {
                ivBeauti2.setVisibility(View.VISIBLE);
            } else {
                ivBeauti2.setVisibility(View.GONE);
            }

            if (!mRoomContriDatas.get(0).isVipExpired()) {
                ivVip1.setVisibility(View.VISIBLE);
                Glide.with(getActivity()).load(mCosVipIconRootPath + "icon_vip" + mRoomContriDatas.get(0).getVip() + ".png").into(ivVip1);
            } else {
                ivVip1.setVisibility(View.GONE);
            }

            if (!mRoomContriDatas.get(1).isVipExpired()) {
                ivVip2.setVisibility(View.VISIBLE);
                Glide.with(getActivity()).load(mCosVipIconRootPath + "icon_vip" + mRoomContriDatas.get(1).getVip() + ".png").into(ivVip2);
            } else {
                ivVip2.setVisibility(View.GONE);
            }

        }
        if (mRoomContriDatas.size() == 1) {
            mRelativeLayout1.setVisibility(View.VISIBLE);
            mRelativeLayout2.setVisibility(View.INVISIBLE);
            mRelativeLayout3.setVisibility(View.INVISIBLE);
            GlideApp.loadImg(getActivity(), mRoomContriDatas.get(0).getHeadUrl(), DiskCacheStrategy.ALL, mPolygonImageView1);
            tvNick1.setText(TCUtils.getLimitString(mRoomContriDatas.get(0).getNickName(), 5));
            tvCount1.setText(FHUtils.intToF(mRoomContriDatas.get(0).getHB()));
            TCUtils.showLevelWithUrl(getActivity(), ivLevel1, mRoomContriDatas.get(0).getLevel());
            if (!mRoomContriDatas.get(0).isVipExpired()) {
                ivVip1.setVisibility(View.VISIBLE);
                Glide.with(getActivity()).load(mCosVipIconRootPath + "icon_vip" + mRoomContriDatas.get(0).getVip() + ".png").into(ivVip1);
            } else {
                ivVip1.setVisibility(View.GONE);
            }

            if (mRoomContriDatas.get(0).isLiang()) {
                ivBeauti1.setVisibility(View.VISIBLE);
            } else {
                ivBeauti1.setVisibility(View.GONE);
            }
        }
        // 列表数据从第四个开始
        if (mRoomContriDatas.size() > 3) {
            mAdapterDatas.clear();
            for (int i = 3; i < mRoomContriDatas.size(); i++) {
                mAdapterDatas.add(mRoomContriDatas.get(i));
            }
            mAdapter.setNewData(mAdapterDatas);
        }
    }

    @Override
    public Activity getActivity() {
        return ContributionListActivity.this;
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        mPresenter.loadRoomContriList(userId, rankType);
    }

    private void goOtherCommu(String userId) {
        if (!userId.equals(FeihuZhiboApplication.getApplication().mDataManager.getCurrentUserId())) {
            Intent intent = new Intent(getActivity(), OthersCommunityActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        }
    }

    @Override
    public void clickTorefresh() {
        mPresenter.loadRoomContriList(userId, rankType);
    }

    @Override
    protected void onDestroy() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroy();
    }

}
