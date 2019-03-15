package cn.feihutv.zhibofeihu.ui.me.mv;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMyCustomMadeMVListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMyCustomMadeMVListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMyMVListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMyMVListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetOtherMVListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetOtherMVListResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseResponse;
import cn.feihutv.zhibofeihu.rxbus.RxBusSubscribe;
import cn.feihutv.zhibofeihu.rxbus.ThreadMode;
import cn.feihutv.zhibofeihu.ui.me.BaseDynamicFragment;
import cn.feihutv.zhibofeihu.ui.me.mv.adapter.MyMvPageAdapter;
import cn.feihutv.zhibofeihu.ui.mv.demand.MyDemandActivity;
import cn.feihutv.zhibofeihu.ui.mv.demand.PostMvVideoActivity;
import cn.feihutv.zhibofeihu.ui.widget.scrolllayoutview.ScrollableHelper;
import cn.feihutv.zhibofeihu.utils.glideutil.GlideApp;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static cn.feihutv.zhibofeihu.FeihuZhiboApplication.getApplication;
import static cn.feihutv.zhibofeihu.rxbus.RxBusCode.RX_BUS_CLICK_CODE_MV_POST_MV;


/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/11/24
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class MyMvPageFragment extends BaseDynamicFragment implements
        ScrollableHelper.ScrollableContainer {

    @BindView(R.id.rv_videoList)
    XRecyclerView mRecyclerView;

    @BindView(R.id.ll_null_dz)
    LinearLayout ll_emptyView;

    ImageView iv_mymv_photo1;
    ImageView iv_mymv_photo2;
    ImageView iv_mymv_photo3;
    CardView iv_mymv_CardView1;
    CardView iv_mymv_CardView2;
    CardView iv_mymv_CardView3;
    View ll_null_zp;//没作品view
    View ll_head_empty_view;
    View eaZpView;//认证主播 作品栏
    View rl_my_zp_list;

    MyMvPageAdapter mMyMvPageAdapter;

    List<GetMyCustomMadeMVListResponse.GetMyCustomMadeMVList> mGetAllMvLists = new ArrayList<>();

    private boolean isFirstLoad = true;

    private boolean isPrepared;//当前页面是否加载

    private String nextOffset = "0";
    private String pageSize = "10";
    boolean isPullRefresh = true;//刷新标记;true 为下拉刷新；

    private boolean isFromOther = false;
    private String userId;

    public interface OnRefreshListener {
        void onRefreshComplete();
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        mOnRefreshListener = onRefreshListener;
    }

    private OnRefreshListener mOnRefreshListener;


    public static MyMvPageFragment newInstance(String userId, boolean isFromOther) {
        MyMvPageFragment fragment = new MyMvPageFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isFromOther", isFromOther);
        bundle.putString("userId", userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_mv_video;
    }


    @Override
    protected void initWidget(View view) {
        ButterKnife.bind(this, view);
        isPrepared = true;

        Bundle bundle = getArguments();
        if (bundle != null) {
            isFromOther = bundle.getBoolean("isFromOther", false);
            userId = bundle.getString("userId");
        }


        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (isFromOther) {
            //他人主页
            mRecyclerView.setEmptyView(ll_emptyView);
        } else {
            //我的主页
            mRecyclerView.addHeaderView(initMvHeadUI());
        }

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                //下拉刷新
                isPullRefresh = true;
                if (isFromOther) {
                    getOtherMadeMVList(userId, "0", pageSize);

                } else {
                    LoadUserDataBaseResponse.UserData userData = FeihuZhiboApplication
                            .getApplication().mDataManager.getUserData();
                    if (userData.getCertifiStatus() == 1) {
                        //是主播加载mv作品
                        eaZpView.setVisibility(View.VISIBLE);
                        loadMyMvList();
                    }
                    getMyCustomMadeMVList("0", pageSize);
                }

            }

            @Override
            public void onLoadMore() {
                //加载更多
                isPullRefresh = false;
                if (isFromOther) {
                    getOtherMadeMVList(userId, nextOffset, pageSize);
                } else {
                    getMyCustomMadeMVList(nextOffset, pageSize);
                }

            }
        });


        mMyMvPageAdapter = new MyMvPageAdapter(getContext(), mGetAllMvLists);
        mRecyclerView.setAdapter(mMyMvPageAdapter);
        mRecyclerView.refresh();

    }


    //初始化头部 定制view
    private View initMvHeadUI() {
        LinearLayout view = (LinearLayout) LayoutInflater.from(getContext()).inflate
                (R.layout.recyclerview_mymv_header, null, false);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);

        eaZpView = view.findViewById(R.id.rv_mymv_myzp);
        LoadUserDataBaseResponse.UserData userData = FeihuZhiboApplication.getApplication().mDataManager
                .getUserData();
        if (userData.getCertifiStatus() == 1) {
            //是主播显示作品
            eaZpView.setVisibility(View.VISIBLE);
        } else {
            eaZpView.setVisibility(View.GONE);
        }

        view.findViewById(R.id.mv_posted).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发布

                    Bundle bundle = new Bundle();
                    goActivity(PostMvVideoActivity.class, bundle);


            }
        });

        view.findViewById(R.id.mv_dz).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //定制
                goActivity(MyDemandActivity.class);

            }
        });

        iv_mymv_photo1 = view.findViewById(R.id.iv_mymv_photo1);
        iv_mymv_photo2 = view.findViewById(R.id.iv_mymv_photo2);
        iv_mymv_photo3 = view.findViewById(R.id.iv_mymv_photo3);
        iv_mymv_CardView1 = view.findViewById(R.id.iv_mymv_CardView1);
        iv_mymv_CardView2 = view.findViewById(R.id.iv_mymv_CardView2);
        iv_mymv_CardView3 = view.findViewById(R.id.iv_mymv_CardView3);
        iv_mymv_CardView1.setVisibility(View.INVISIBLE);
        iv_mymv_CardView2.setVisibility(View.INVISIBLE);
        iv_mymv_CardView3.setVisibility(View.INVISIBLE);
        ll_head_empty_view = view.findViewById(R.id.ll_head_empty_view);
        ll_null_zp = view.findViewById(R.id.ll_null_zp);
        rl_my_zp_list = view.findViewById(R.id.rl_my_zp_list);
        rl_my_zp_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击跳转我的作品页面
                goActivity(MyMvProductionActivity.class);

            }
        });
        return view;
    }


    //加载我的mv作品列表
    private void loadMyMvList() {
        String status = "0";
        String offset = "0";
        String count = "3";
        getApplication().mDataManager
                .doGetMyMVListCall(new GetMyMVListRequest(status, offset, count))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetMyMVListResponse>() {
                    @Override
                    public void accept(@NonNull GetMyMVListResponse response) throws Exception {

                        if (response.getCode() == 0) {
                            List<GetMyMVListResponse.GetMyMVList> myMVLists =
                                    response.getGetMyMVListData().getGetMyMVLists();

                            if (myMVLists != null && myMVLists.size() > 0) {
                                iv_mymv_CardView1.setVisibility(View.VISIBLE);
                                GlideApp.loadImg(getContext(), myMVLists.get(0).getCover(),
                                        R.drawable.bg, iv_mymv_photo1);
                                ll_null_zp.setVisibility(View.GONE);
                            } else {
                                //没有作品
                                ll_null_zp.setVisibility(View.VISIBLE);
                                return;
                            }

                            if (myMVLists.size() > 1) {
                                iv_mymv_CardView2.setVisibility(View.VISIBLE);
                                GlideApp.loadImg(getContext(), myMVLists.get(1).getCover(),
                                        R.drawable.bg, iv_mymv_photo2);
                            }
                            if (myMVLists.size() > 2) {
                                iv_mymv_CardView3.setVisibility(View.VISIBLE);
                                GlideApp.loadCropImg(getContext(), myMVLists.get(2).getCover(),
                                        R.drawable.bg, iv_mymv_photo3);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                    }
                });


    }


    @Override
    protected void lazyLoad() {

        if (isPrepared && isVisible && mGetAllMvLists.size() == 0) {
            isPrepared = false;
            //获取mv 列表
//             getAllMVList(nextOffset);
        }
    }


    //加载我的定制mv列表
    private void getMyCustomMadeMVList(String offset, String count) {
        getApplication().mDataManager
                .doGetMyCustomMadeMVListCall(new GetMyCustomMadeMVListRequest(offset, count))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetMyCustomMadeMVListResponse>() {
                    @Override
                    public void accept(@NonNull GetMyCustomMadeMVListResponse response) throws Exception {
                        updateRefreshUI();
                        if (response.getCode() == 0) {
                            GetMyCustomMadeMVListResponse.GetMyCustomMadeMVListData getAllMvListData = response.getGetMyCustomMadeMVListData();

                            List<GetMyCustomMadeMVListResponse.GetMyCustomMadeMVList> myCustomMadeMVLists =
                                    getAllMvListData.getGetMyCustomMadeMVLists();
                            if (myCustomMadeMVLists != null && myCustomMadeMVLists.size() > 0) {
                                ll_head_empty_view.setVisibility(View.GONE);
                                nextOffset = getAllMvListData.getNextOffset();
                                if (isPullRefresh) {
                                    mGetAllMvLists.clear();
                                }
                                mGetAllMvLists.addAll(myCustomMadeMVLists);
                                mMyMvPageAdapter.notifyDataSetChanged();
                            } else {
                                //如果返回的数据为空，则显示空白view
                                if (mGetAllMvLists.size() == 0) {
                                    ll_head_empty_view.setVisibility(View.VISIBLE);
                                }
                            }

                        } else {
                            if (!isPullRefresh) {
                                mRecyclerView.setNoMore(true);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        updateRefreshUI();
                    }
                });

    }


    //加载他人的定制mv列表
    private void getOtherMadeMVList(String userId, String offset, String count) {
        getApplication().mDataManager
                .doGetOtherMVListCall(new GetOtherMVListRequest(userId, offset, count))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetOtherMVListResponse>() {
                    @Override
                    public void accept(@NonNull GetOtherMVListResponse response) throws Exception {

                        updateRefreshUI();
                        if (response.getCode() == 0) {
                            GetOtherMVListResponse.GetOtherMVListData getAllMvListData
                                    = response.getGetOtherMVListData();

                            List<GetMyCustomMadeMVListResponse.GetMyCustomMadeMVList> myCustomMadeMVLists =
                                    getAllMvListData.getGetOtherMVLists();
                            if (myCustomMadeMVLists != null && myCustomMadeMVLists.size() > 0) {
                                nextOffset = getAllMvListData.getNextOffset();
                                if (isPullRefresh) {
                                    mGetAllMvLists.clear();
                                }
                                mGetAllMvLists.addAll(myCustomMadeMVLists);
                                mMyMvPageAdapter.notifyDataSetChanged();
                            }

                        } else {
                            if (!isPullRefresh) {
                                mRecyclerView.setNoMore(true);
                            }
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        updateRefreshUI();
                    }
                });

    }


    private void updateRefreshUI() {
        if (isPullRefresh) {
            mRecyclerView.refreshComplete();
        } else {
            mRecyclerView.loadMoreComplete();
        }
        //回调刷新状态
        if (mOnRefreshListener != null) {
            mOnRefreshListener.onRefreshComplete();
        }
    }



    @RxBusSubscribe(code = RX_BUS_CLICK_CODE_MV_POST_MV,threadMode = ThreadMode.MAIN)
    public void onPostMvActivity(Integer state){
        //发布成功后刷新
        mRecyclerView.refresh();
    }



    @Override
    public void pullToRefresh() {

    }

    @Override
    public void refreshComplete() {

    }

    @Override
    public View getScrollableView() {
        return mRecyclerView;
    }

}
