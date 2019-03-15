package cn.feihutv.zhibofeihu.ui.home;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.home.LoadRoomListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.home.LoadRoomListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMyCustomMadeMVListResponse;
import cn.feihutv.zhibofeihu.data.prefs.AppPreferencesHelper;
import cn.feihutv.zhibofeihu.ui.base.BaseFragment;
import cn.feihutv.zhibofeihu.ui.home.adapter.HotAdapter;
import cn.feihutv.zhibofeihu.ui.widget.StatusView;
import cn.feihutv.zhibofeihu.ui.widget.swiperefreshlayout.Config;
import cn.feihutv.zhibofeihu.ui.widget.swiperefreshlayout.FHHeaderView;
import cn.feihutv.zhibofeihu.ui.widget.swiperefreshlayout.RecycleViewScrollHelper;
import cn.feihutv.zhibofeihu.ui.widget.swiperefreshlayout.VRefreshLayout;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.AppUtils;
import cn.feihutv.zhibofeihu.utils.FloatViewWindowManger;
import cn.feihutv.zhibofeihu.utils.NetworkUtils;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/10/19 16:20
 *      desc   : 首页--标签--关注
 *      version: 1.0
 * </pre>
 */

public class ConcernFragment extends BaseFragment implements VRefreshLayout.OnRefreshListener {

    /**
     * 当前页面是否加载
     */
    private boolean isPrepared;

    public static final int START_LIVE_PLAY = 100;
    /**
     * 当前标签
     */
    public String mTag;
    private static final String EXTRA_ID = "id";
    private int count = 20;
    private int nextOffset = 0;
    private boolean isRefresh = true;
    private List<LoadRoomListResponse.LoadRoomListResponseData.ListData> mListDataList = new ArrayList<>();
    @BindView(R.id.hot_recyclerview)
    RecyclerView mRecyclerView;

    @BindView(R.id.hot_swip)
    VRefreshLayout mVRefreshLayout;

    @BindView(R.id.status_view)
    StatusView mStatusView;

    private HotAdapter mAdapter;
    private FHHeaderView mHeaderView;
    private LinearLayout mLayout;

    public static ConcernFragment newInstance(String id, int position) {
        ConcernFragment fragment = new ConcernFragment();
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

    private void init() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mAdapter = new HotAdapter(mListDataList);
        refreshConfig();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        mLayout = (LinearLayout) inflater.inflate(R.layout.concern_recycleview_head, null);
        mHeaderView = new FHHeaderView(getContext());
        mVRefreshLayout.setHeaderView(mHeaderView);// add headerView
        mVRefreshLayout.addOnRefreshListener(this);
        mAdapter.addHeaderView(mLayout);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                gotoRoom(mListDataList.get(position));
            }
        });

        RecycleViewScrollHelper mScrollHelper = new RecycleViewScrollHelper(new RecycleViewScrollHelper.OnScrollPositionChangedListener() {
            @Override
            public void onScrollToTop() {

            }

            @Override
            public void onScrollToBottom() {
                isRefresh = false;
                loadDatas(mTag, count, nextOffset);
            }

            @Override
            public void onScrollToUnknown(boolean isTopViewVisible, boolean isBottomViewVisible) {

            }
        });
        mScrollHelper.attachToRecycleView(mRecyclerView);
    }

    @Override
    protected void lazyLoad() {
        if (isPrepared && isVisible && mListDataList.size() == 0) {
            isPrepared = false;
            Bundle bundle = getArguments();
            if (bundle != null) {
                mTag = bundle.getString(EXTRA_ID, "1");
            }
            if (SharePreferenceUtil.getSession(getContext(), AppPreferencesHelper.PREF_KEY_USERID).startsWith("g")) {
                // 游客  加载推荐
                loadRecommendDatas();
            } else {
                // 用户 加载关注
                loadDatas(mTag, count, 0);
            }

        }
    }


    /**
     * 加载数据
     *
     * @param tag
     * @param count
     * @param offset
     */
    private void loadDatas(String tag, int count, int offset) {
        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .doLoadRoomListByTagApiCall(new LoadRoomListRequest(tag, String.valueOf(offset), String.valueOf(count)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoadRoomListResponse>() {
                    @Override
                    public void accept(@NonNull LoadRoomListResponse loadRoomListResponse) throws Exception {
                        if (isRefresh) {
                            mVRefreshLayout.refreshComplete();
                        }
                        if (loadRoomListResponse.getCode() == 0) {
                            nextOffset = loadRoomListResponse.getmLoadRoomListResponseData().getNextOffset();
                            List<LoadRoomListResponse.LoadRoomListResponseData.ListData> listDataList = loadRoomListResponse.getmLoadRoomListResponseData().getList();

                            if (isRefresh) {
                                mListDataList.clear();
                            }
                            if (listDataList != null && listDataList.size() > 0) {
                                mVRefreshLayout.setVisibility(View.VISIBLE);
                                mStatusView.setVisibility(View.GONE);


                                mAdapter.removeHeaderView(mLayout);
                                mListDataList.addAll(listDataList);
                                mAdapter.setNewData(mListDataList);
                            } else {
                                if (mListDataList.size() == 0) {
                                    loadRecommendDatas();
                                }
                            }
                        } else {
                            if (!isRefresh) {
                                // 没有更多数据了
                            }
                        }
                        hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        mVRefreshLayout.refreshComplete();
                        hideLoading();
                    }
                }));
    }

    /**
     * 加载推荐列表
     */
    private void loadRecommendDatas() {
        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .doLoadCareRecommendedListApiCall(new LoadRoomListRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoadRoomListResponse>() {
                    @Override
                    public void accept(@NonNull LoadRoomListResponse loadRoomListResponse) throws Exception {
                        if (isRefresh) {
                            mVRefreshLayout.refreshComplete();
                        }
                        if (loadRoomListResponse.getCode() == 0) {
                            List<LoadRoomListResponse.LoadRoomListResponseData.ListData> listDataList = loadRoomListResponse.getmLoadRoomListResponseData().getList();

                            if (listDataList != null && listDataList.size() > 0) {
                                mVRefreshLayout.setVisibility(View.VISIBLE);
                                mStatusView.setVisibility(View.GONE);
                                if (isRefresh) {
                                    mListDataList.clear();
                                }
                                if (mAdapter.getHeaderLayoutCount() == 0) {
                                    mAdapter.addHeaderView(mLayout);
                                }
                                mListDataList.addAll(listDataList);
                                mAdapter.setNewData(mListDataList);
                            } else {
                                mVRefreshLayout.setVisibility(View.GONE);
                                mStatusView.setVisibility(View.VISIBLE);
                                mStatusView.showNoDataView();
                            }
                        } else {
                            AppLogger.i("ConcernFragment" + loadRoomListResponse.getCode() + " " + loadRoomListResponse.getErrMsg());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        AppLogger.i("ConcernFragment" + throwable);
                    }
                }));
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

    private void gotoRoom(final LoadRoomListResponse.LoadRoomListResponseData.ListData tcRoomInfo) {
        FloatViewWindowManger.removeSmallWindow();
        // 1为PC   2为手机
        AppUtils.startLiveActivity(ConcernFragment.this, tcRoomInfo.getRoomId(), tcRoomInfo.getHeadUrl(), tcRoomInfo.getBroadcastType(), false, START_LIVE_PLAY);
    }

    @Override
    public void onRefresh() {
        isRefresh = true;

        if (SharePreferenceUtil.getSession(getContext(), AppPreferencesHelper.PREF_KEY_USERID).startsWith("g")) {
            // 游客  加载推荐
            loadRecommendDatas();
        } else {
            // 用户 加载关注
            loadDatas(mTag, count, 0);
        }
    }
}
