package cn.feihutv.zhibofeihu.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView;

import java.util.ArrayList;
import java.util.List;

import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;

import cn.feihutv.zhibofeihu.data.network.http.model.me.GetUserGuardsResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.LoadOtherUserInfoResponce;
import cn.feihutv.zhibofeihu.ui.bangdan.adapter.GuardsAdapter;
import cn.feihutv.zhibofeihu.ui.me.HisGuardMvpView;
import cn.feihutv.zhibofeihu.ui.me.HisGuardMvpPresenter;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.ui.me.dynamic.OtherPageFragment;
import cn.feihutv.zhibofeihu.ui.me.dynamic.OthersCommunityActivity;
import cn.feihutv.zhibofeihu.ui.widget.StatusView;
import cn.feihutv.zhibofeihu.ui.widget.TCActivityTitle;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.AppUtils;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.ListViewDecoration;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.feihutv.zhibofeihu.utils.glideutil.GlideApp;

/**
 * <pre>
 *     author : liwen.chen
 *     time   :
 *     desc   : TA的守护
 *     version: 1.0
 * </pre>
 */
public class HisGuardActivity extends BaseActivity implements HisGuardMvpView, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    @Inject
    HisGuardMvpPresenter<HisGuardMvpView> mPresenter;

    @BindView(R.id.tca_his_reture)
    TCActivityTitle mTitle;

    @BindView(R.id.bangdan_swip)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.bangdan_rv)
    RecyclerView mRecyclerView;

    @BindView(R.id.ll_view)
    LinearLayout mLinearLayout;

    private boolean isFirstLoad = true;

    private boolean isLoadEnd = false;

    private boolean isRefresh;

    private int offset = 0;

    private View mHeadView;

    private String userId;

    @BindView(R.id.tv_sure)
    TextView tvSure;

    @BindView(R.id.btn_shouhu)
    Button mButton;

    private ImageView mPolygonImageView1, mPolygonImageView2, mPolygonImageView3;
    private ImageView ivLevel1, ivLevel2, ivLevel3;
    private TextView tvNick1, tvNick2, tvNick3, tvCount1, tvCount2, tvCount3;

    private RelativeLayout mRelativeLayout1, mRelativeLayout2, mRelativeLayout3;

    private GuardsAdapter mAdapter;

    private List<GetUserGuardsResponse.GetUserGuardsResponseData> mGetUserGuardsResponseDatas = new ArrayList<>();
    private List<GetUserGuardsResponse.GetUserGuardsResponseData> mAdapterDatas = new ArrayList<>();
    private LoadOtherUserInfoResponce.OtherUserInfo mOtherUserInfo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_his_guard;
    }


    @Override
    protected void initWidget() {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this));

        //persenter调用初始化
        mPresenter.onAttach(HisGuardActivity.this);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        mOtherUserInfo = intent.getParcelableExtra("otherUserInfo");
        mButton.setOnClickListener(this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //添加分隔线
        mRecyclerView.addItemDecoration(new ListViewDecoration());
        mRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        mRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);

        mRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

        mAdapter = new GuardsAdapter(mAdapterDatas);
        mRecyclerView.setAdapter(mAdapter);

        mRefreshLayout.setOnRefreshListener(this);
        tvSure.setOnClickListener(this);

        mHeadView = LayoutInflater.from(this).inflate(R.layout.view_head_me_guards, null);
        mPolygonImageView1 = (ImageView) mHeadView.findViewById(R.id.polygonImageView_1);
        mPolygonImageView2 = (ImageView) mHeadView.findViewById(R.id.polygonImageView_2);
        mPolygonImageView3 = (ImageView) mHeadView.findViewById(R.id.polygonImageView_3);
        tvNick1 = (TextView) mHeadView.findViewById(R.id.tv_nick_1);
        tvNick2 = (TextView) mHeadView.findViewById(R.id.tv_nick_2);
        tvNick3 = (TextView) mHeadView.findViewById(R.id.tv_nick_3);
        ivLevel1 = (ImageView) mHeadView.findViewById(R.id.iv_level_1);
        ivLevel2 = (ImageView) mHeadView.findViewById(R.id.iv_level_2);
        ivLevel3 = (ImageView) mHeadView.findViewById(R.id.iv_level_3);
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
                goToOthersCommunite(mAdapterDatas.get(position).getUserId());
            }
        });

        mAdapter.setLoadMoreView(new SimpleLoadMoreView());

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (!isLoadEnd) {
                    isLoadEnd = true;
                    mPresenter.getUserGuards(userId, offset);
                }
            }
        }, mRecyclerView);

        mAdapter.addHeaderView(mHeadView);

        mPresenter.getUserGuards(userId, offset);


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
    protected void onDestroy() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroy();
    }


    @Override
    public void showNoData() {
        if (isFirstLoad) {
            mRefreshLayout.setVisibility(View.GONE);
            mLinearLayout.setVisibility(View.VISIBLE);
            mButton.setVisibility(View.GONE);
            hideLoading();
        }
        if (isLoadEnd) {
            mAdapter.loadMoreEnd();
        }
    }

    @Override
    public void showContent() {
        mRefreshLayout.setVisibility(View.VISIBLE);
        mLinearLayout.setVisibility(View.GONE);
        mButton.setVisibility(View.VISIBLE);
        hideLoading();

        if (isLoadEnd) {
            mAdapter.loadMoreComplete();
            isLoadEnd = false;
        }
    }

    @Override
    public void showErrorView() {
        isRefresh = false;
        mRefreshLayout.setRefreshing(false);
        if (isFirstLoad) {
            mRefreshLayout.setVisibility(View.GONE);
            hideLoading();
        }
        if (isLoadEnd) {
            mAdapter.loadMoreFail();
            isLoadEnd = false;
        }
    }

    @Override
    public void getDatas(List<GetUserGuardsResponse.GetUserGuardsResponseData> getUserGuardsResponseDatas) {
        if (isRefresh) {
            if (isNetworkConnected()) {
                mGetUserGuardsResponseDatas.clear();
                mRefreshLayout.setRefreshing(false);
                isRefresh = false;
            }
        }
        isFirstLoad = false;
        mGetUserGuardsResponseDatas.addAll(getUserGuardsResponseDatas);
        offset = mGetUserGuardsResponseDatas.size();
        if (mGetUserGuardsResponseDatas.size() >= 3) {
            mRelativeLayout1.setVisibility(View.VISIBLE);
            mRelativeLayout2.setVisibility(View.VISIBLE);
            mRelativeLayout3.setVisibility(View.VISIBLE);
            GlideApp.loadImg(HisGuardActivity.this, mGetUserGuardsResponseDatas.get(0).getAvatar(), DiskCacheStrategy.ALL, mPolygonImageView1);
            GlideApp.loadImg(HisGuardActivity.this, mGetUserGuardsResponseDatas.get(1).getAvatar(), DiskCacheStrategy.ALL, mPolygonImageView2);
            GlideApp.loadImg(HisGuardActivity.this, mGetUserGuardsResponseDatas.get(2).getAvatar(), DiskCacheStrategy.ALL, mPolygonImageView3);
            tvNick1.setText(mGetUserGuardsResponseDatas.get(0).getNickname());
            tvNick2.setText(mGetUserGuardsResponseDatas.get(1).getNickname());
            tvNick3.setText(mGetUserGuardsResponseDatas.get(2).getNickname());
            tvCount1.setText(String.valueOf(FHUtils.intToF(mGetUserGuardsResponseDatas.get(0).getFriendliness())));
            tvCount2.setText(String.valueOf(FHUtils.intToF(mGetUserGuardsResponseDatas.get(1).getFriendliness())));
            tvCount3.setText(String.valueOf(FHUtils.intToF(mGetUserGuardsResponseDatas.get(2).getFriendliness())));
            TCUtils.showLevelWithUrl(HisGuardActivity.this, ivLevel1, mGetUserGuardsResponseDatas.get(0).getLevel());
            TCUtils.showLevelWithUrl(HisGuardActivity.this, ivLevel2, mGetUserGuardsResponseDatas.get(1).getLevel());
            TCUtils.showLevelWithUrl(HisGuardActivity.this, ivLevel3, mGetUserGuardsResponseDatas.get(2).getLevel());

        }

        if (mGetUserGuardsResponseDatas.size() == 2) {
            mRelativeLayout1.setVisibility(View.VISIBLE);
            mRelativeLayout2.setVisibility(View.VISIBLE);
            mRelativeLayout3.setVisibility(View.INVISIBLE);
            GlideApp.loadImg(HisGuardActivity.this, mGetUserGuardsResponseDatas.get(0).getAvatar(), DiskCacheStrategy.ALL, mPolygonImageView1);
            GlideApp.loadImg(HisGuardActivity.this, mGetUserGuardsResponseDatas.get(1).getAvatar(), DiskCacheStrategy.ALL, mPolygonImageView2);
            tvNick1.setText(mGetUserGuardsResponseDatas.get(0).getNickname());
            tvNick2.setText(mGetUserGuardsResponseDatas.get(1).getNickname());
            tvCount1.setText(String.valueOf(FHUtils.intToF(mGetUserGuardsResponseDatas.get(0).getFriendliness())));
            tvCount2.setText(String.valueOf(FHUtils.intToF(mGetUserGuardsResponseDatas.get(1).getFriendliness())));
            TCUtils.showLevelWithUrl(HisGuardActivity.this, ivLevel1, mGetUserGuardsResponseDatas.get(0).getLevel());
            TCUtils.showLevelWithUrl(HisGuardActivity.this, ivLevel2, mGetUserGuardsResponseDatas.get(1).getLevel());

        }
        if (mGetUserGuardsResponseDatas.size() == 1) {
            mRelativeLayout1.setVisibility(View.VISIBLE);
            mRelativeLayout2.setVisibility(View.INVISIBLE);
            mRelativeLayout3.setVisibility(View.INVISIBLE);
            GlideApp.loadImg(HisGuardActivity.this, mGetUserGuardsResponseDatas.get(0).getAvatar(), DiskCacheStrategy.ALL, mPolygonImageView1);
            tvNick1.setText(mGetUserGuardsResponseDatas.get(0).getNickname());
            tvCount1.setText(FHUtils.intToF(mGetUserGuardsResponseDatas.get(0).getFriendliness()));
            TCUtils.showLevelWithUrl(HisGuardActivity.this, ivLevel1, mGetUserGuardsResponseDatas.get(0).getLevel());
        }
        // 列表数据从第四个开始
        if (mGetUserGuardsResponseDatas.size() > 3) {
            mAdapterDatas.clear();
            for (int i = 3; i < mGetUserGuardsResponseDatas.size(); i++) {
                mAdapterDatas.add(mGetUserGuardsResponseDatas.get(i));
            }
            mAdapter.setNewData(mAdapterDatas);
        }
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        mPresenter.getUserGuards(userId, 0);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.polygonImageView_1:
                goToOthersCommunite(mGetUserGuardsResponseDatas.get(0).getUserId());
                break;
            case R.id.polygonImageView_2:
                goToOthersCommunite(mGetUserGuardsResponseDatas.get(1).getUserId());
                break;
            case R.id.polygonImageView_3:
                goToOthersCommunite(mGetUserGuardsResponseDatas.get(2).getUserId());
                break;
            case R.id.btn_shouhu:
                goLiveRoom(mOtherUserInfo.getUserId(), mOtherUserInfo.getHeadUrl(), mOtherUserInfo.getBroadcastType());
                break;
            case R.id.tv_sure:
                goLiveRoom(mOtherUserInfo.getUserId(), mOtherUserInfo.getHeadUrl(), mOtherUserInfo.getBroadcastType());
                break;
            default:
                break;
        }
    }

    private void goLiveRoom(String userId, String headUrl, int broadType) {
        AppUtils.startLiveActivity(HisGuardActivity.this, userId, headUrl, broadType, true, 100);
    }

    private void goToOthersCommunite(String userId) {
        if (!userId.equals(FeihuZhiboApplication.getApplication().mDataManager.getCurrentUserId())) {
            Intent intent = new Intent(HisGuardActivity.this, OthersCommunityActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        } else {
            onToast("这个是你哦，谢谢你的守护~", Gravity.CENTER, 0, 0);
        }
    }
}
