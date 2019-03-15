package cn.feihutv.zhibofeihu.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGABanner;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.home.GetBannerRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.home.GetBannerResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.home.LoadRoomListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.home.LoadRoomListResponse;
import cn.feihutv.zhibofeihu.ui.base.BaseFragment;
import cn.feihutv.zhibofeihu.ui.home.adapter.HotAdapter;
import cn.feihutv.zhibofeihu.ui.main.MainActivity;
import cn.feihutv.zhibofeihu.ui.me.HubaoActivity;
import cn.feihutv.zhibofeihu.ui.widget.StatusView;
import cn.feihutv.zhibofeihu.ui.widget.swiperefreshlayout.Config;
import cn.feihutv.zhibofeihu.ui.widget.swiperefreshlayout.FHHeaderView;
import cn.feihutv.zhibofeihu.ui.widget.swiperefreshlayout.RecycleViewScrollHelper;
import cn.feihutv.zhibofeihu.ui.widget.swiperefreshlayout.VRefreshLayout;
import cn.feihutv.zhibofeihu.utils.AppUtils;
import cn.feihutv.zhibofeihu.utils.FloatViewWindowManger;
import cn.feihutv.zhibofeihu.utils.NetworkUtils;
import cn.feihutv.zhibofeihu.utils.glideutil.GlideApp;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *     @author : liwen.chen
 *     date   : 2017/
 *     desc   : 界面绘制
 *     @version : 1.0
 * </pre>
 */
public class HotFragment extends BaseFragment implements VRefreshLayout.OnRefreshListener, StatusView.ClickRefreshListener {

    private static final String EXTRA_ID = "id";
    private HotAdapter mAdapter;
    private List<LoadRoomListResponse.LoadRoomListResponseData.ListData> mListDataList = new ArrayList<>();
    private int nextOffset = 0;
    private List<String> images = new ArrayList<>();
    private boolean isRefresh;
    private boolean isFirstLoad = true;
    @BindView(R.id.hot_recyclerview)
    RecyclerView mRecyclerView;

    @BindView(R.id.hot_swip)
    VRefreshLayout mVRefreshLayout;

    @BindView(R.id.status_view)
    StatusView mStatusView;
    public String mTag; //当前标签
    private int count = 20;
    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;
    private int[] bannerColors = {R.color.banner1, R.color.banner2};
    public static final int START_LIVE_PLAY = 100;
    private boolean isPrepared;//当前页面是否加载
    List<GetBannerResponse.BannerData> bannerDatas;

    public static HotFragment newInstance(String id, int position) {
        HotFragment fragment = new HotFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_ID, id);
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_hot;
    }

    @Override
    protected void initWidget(View view) {
        //初始化注入控件
        setUnBinder(ButterKnife.bind(this, view));
        isPrepared = true;
        init();
    }

    @Override
    protected void lazyLoad() {
        if (isPrepared && isVisible && mListDataList.size() == 0) {
            isPrepared = false;
            Bundle bundle = getArguments();
            if (bundle != null) {
                mTag = bundle.getString(EXTRA_ID, "1");
            }
            loadDatas(mTag, count, nextOffset);
        }
    }

    private void init() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mAdapter = new HotAdapter(mListDataList);
        addHeaderView();
        refreshConfig();
        mStatusView.setClickRefreshListener(this);
        FHHeaderView headerView = new FHHeaderView(getContext());
        mVRefreshLayout.setHeaderView(headerView);// add headerView
        mVRefreshLayout.addOnRefreshListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                final LoadRoomListResponse.LoadRoomListResponseData.ListData tcRoomInfo = mListDataList.get(position);
                gotoRoom(tcRoomInfo);
            }
        });

        RecycleViewScrollHelper mScrollHelper = new RecycleViewScrollHelper(new RecycleViewScrollHelper.OnScrollPositionChangedListener() {
            @Override
            public void onScrollToTop() {

            }

            @Override
            public void onScrollToBottom() {
                loadDatas(mTag, count, nextOffset);
            }

            @Override
            public void onScrollToUnknown(boolean isTopViewVisible, boolean isBottomViewVisible) {

            }
        });
        mScrollHelper.attachToRecycleView(mRecyclerView);

    }

    public void gotoRoom(LoadRoomResponce.LoadRoomData loadRoomData) {
        FloatViewWindowManger.removeSmallWindow();
        // 1为PC   2为手机
        AppUtils.startLiveActivity(getActivity(), loadRoomData.getRoomId(), loadRoomData.getHeadUrl(), loadRoomData.getBroadcastType(), true);
    }

    private void gotoRoom(final LoadRoomListResponse.LoadRoomListResponseData.ListData tcRoomInfo) {
        FloatViewWindowManger.removeSmallWindow();
        // 1为PC   2为手机
        AppUtils.startLiveActivity(HotFragment.this, tcRoomInfo.getRoomId(), tcRoomInfo.getHeadUrl(), tcRoomInfo.getBroadcastType(), false, START_LIVE_PLAY);
    }

    @Override
    public void onDestroyView() {
        //销毁之前必须调用
        super.onDestroyView();
    }

    private void addHeaderView() {
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.recycleview_head, null);
        final BGABanner svPhoto = (BGABanner) layout.findViewById(R.id.banner);
        svPhoto.setBackgroundColor(bannerColors[new Random().nextInt(2)]);
        int width = getResources().getDisplayMetrics().widthPixels;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) svPhoto.getLayoutParams();
        params.width = width;
        params.height = width * 170 / 750;
        svPhoto.setLayoutParams(params);
        svPhoto.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                GlideApp.loadImg(getContext(), model, DiskCacheStrategy.RESOURCE, itemView);
            }
        });
        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .doGetBannerByTypeApiCall(new GetBannerRequest("3"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetBannerResponse>() {
                    @Override
                    public void accept(@NonNull GetBannerResponse getBannerResponse) throws Exception {
                        if (getBannerResponse.getCode() == 0) {
                            String listJson = getBannerResponse.getData().getListJson();
                            bannerDatas = GetBannerResponse.BannerData.arrayBannerDataFromData(listJson);
                            images.clear();
                            long serverTime = getBannerResponse.getData().getServerTime();
                            for (int i = bannerDatas.size() - 1; i >= 0; i--) {
                                GetBannerResponse.BannerData bannerData = bannerDatas.get(i);
                                if (serverTime >= bannerData.getBeginTime() && serverTime <= bannerData.getEndTime()) {
                                    images.add(0, bannerData.getBannerImg());
                                } else {
                                    bannerDatas.remove(bannerData);
                                }
                            }
                            svPhoto.setData(images, null);
                        } else {
                            //处理回应签名失效问题
                            FeihuZhiboApplication.getApplication().mDataManager
                                    .handleResponseCode(getBannerResponse.getCode());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                    }
                }));
        svPhoto.setDelegate(new BGABanner.Delegate() {
            @Override
            public void onBannerItemClick(BGABanner banner, View itemView, Object m, int position) {
                MobclickAgent.onEvent(getContext(), "10015");
                GetBannerResponse.BannerData bannerData = bannerDatas.get(position);
                String bannerDirContent = bannerData.getBannerDirContent();
                int bannerDirType = bannerData.getBannerDirType();
                if (bannerDirType == 1) {
                    new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                            .doGetRoomDataApiCall(new LoadRoomRequest(bannerDirContent))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<LoadRoomResponce>() {
                                @Override
                                public void accept(@NonNull LoadRoomResponce loadRoomResponce) throws Exception {
                                    gotoRoom(loadRoomResponce.getLoadRoomData());
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(@NonNull Throwable throwable) throws Exception {

                                }
                            }));
                } else if (bannerDirType == 2) {
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra("url", bannerDirContent);
                    startActivity(intent);
                } else if (bannerDirType == 3) {
                    if (bannerDirContent.equals("1")) {
                        //榜单
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.putExtra("viewpagerContent", 1);
                        startActivity(intent);
                    } else if (bannerDirContent.equals("2")) {
                        //虎宝首页
                        Intent intent = new Intent(getActivity(), HubaoActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
        mAdapter.addHeaderView(layout);
    }

    private void loadDatas(String tag, int count, int offset) {
        if (isFirstLoad) {
            showProgressView();
        }
        if (!isNetworkConnected()) {
            onToast("网络连接失败，请检查您的网络", Gravity.CENTER, 0, 0);
            mVRefreshLayout.refreshComplete();
        }
        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .doLoadRoomListByTagApiCall(new LoadRoomListRequest(tag, String.valueOf(offset), String.valueOf(count)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoadRoomListResponse>() {
                    @Override
                    public void accept(@NonNull LoadRoomListResponse loadRoomListResponse) throws Exception {
                        if (loadRoomListResponse.getCode() == 0) {
                            List<LoadRoomListResponse.LoadRoomListResponseData.ListData> listDataList = loadRoomListResponse.getmLoadRoomListResponseData().getList();
                            if (listDataList.size() > 0) {
                                showContentView();
                                if (isRefresh) {
                                    if (NetworkUtils.isNetworkConnected(getContext())) {
                                        mListDataList.clear();
                                        isRefresh = false;
                                    }
                                }
                                mListDataList.addAll(listDataList);
                                mAdapter.setNewData(mListDataList);
                                isFirstLoad = false;
                            } else {
                                if (isFirstLoad) {
                                    showNodataView();
                                }
                            }
                            nextOffset = loadRoomListResponse.getmLoadRoomListResponseData().getNextOffset();
                        } else {
                            //处理回应签名失效问题
                            FeihuZhiboApplication.getApplication().mDataManager
                                    .handleResponseCode(loadRoomListResponse.getCode());
                        }
                        mVRefreshLayout.refreshComplete();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        isRefresh = false;
                        if (isFirstLoad) {
                            showErrorView();
                        }
                    }
                }));
    }

    // 加载错误
    private void showErrorView() {
        mVRefreshLayout.setVisibility(View.GONE);
        mStatusView.showErrorView();
        hideLoading();
    }

    // 没有数据
    private void showNodataView() {
        mVRefreshLayout.setVisibility(View.GONE);
        mStatusView.showNoDataView();
        hideLoading();
    }

    // 显示加载框
    private void showProgressView() {
        mVRefreshLayout.setVisibility(View.GONE);
        showLoading();
    }

    // 有内容
    private void showContentView() {
        mStatusView.setVisibility(View.GONE);
        mVRefreshLayout.setVisibility(View.VISIBLE);
        hideLoading();
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        loadDatas(mTag, count, 0);
    }

    private void refreshConfig() {
        if (mVRefreshLayout != null) {
            mVRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Config config = Config.getInstance();
                    mVRefreshLayout.setDragRate(config.dragRate);
                    mVRefreshLayout.setRatioOfHeaderHeightToReach(config.ratioOfHeaderHeightToReach);
                    mVRefreshLayout.setAutoRefreshDuration(config.autoRefreshDuration);
                    mVRefreshLayout.setToRetainDuration(config.toRetainDuration);
                    mVRefreshLayout.setToStartDuration(config.toStartDuration);
                    mVRefreshLayout.setCompleteStickDuration(config.completeStickDuration);
                }
            }, 200);
        }
    }

    @Override
    public void clickTorefresh() {
        loadDatas(mTag, count, nextOffset);
    }
}
