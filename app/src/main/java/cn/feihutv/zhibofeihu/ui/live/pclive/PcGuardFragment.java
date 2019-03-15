package cn.feihutv.zhibofeihu.ui.live.pclive;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetUserGuardsRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetUserGuardsResponse;
import cn.feihutv.zhibofeihu.rxbus.RxBus;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.ui.base.BaseFragment;
import cn.feihutv.zhibofeihu.ui.live.adapter.GuardRankAdapter;
import cn.feihutv.zhibofeihu.ui.live.adapter.PcGuardRankAdapter;
import cn.feihutv.zhibofeihu.ui.widget.swiperefreshlayout.FHHeaderView;
import cn.feihutv.zhibofeihu.ui.widget.swiperefreshlayout.RecycleViewScrollHelper;
import cn.feihutv.zhibofeihu.ui.widget.swiperefreshlayout.VRefreshLayout;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.ListViewDecoration;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by huanghao on 2017/11/13.
 */

public class PcGuardFragment extends BaseFragment implements VRefreshLayout.OnRefreshListener {
    @BindView(R.id.fans_rv)
    RecyclerView fansRv;
    @BindView(R.id.fans_swip)
    VRefreshLayout fansSwip;
    @BindView(R.id.text_tip)
    TextView textTip;
    @BindView(R.id.empty_view)
    LinearLayout emptyView;
    Unbinder unbinder;
    @BindView(R.id.btn_guard)
    Button btnGuard;
    @BindView(R.id.guard_frm)
    FrameLayout guardFrm;
    private String userId;
    private boolean isPrepared;
    private boolean isFreshing = false;
    private PcGuardRankAdapter adapter;
    private FHHeaderView mHeaderView;
    private int offset = 0;
    private List<GetUserGuardsResponse.GetUserGuardsResponseData> datas = new ArrayList<>();

    public static PcGuardFragment newInstance(String userId) {
        Bundle args = new Bundle();
        PcGuardFragment fragment = new PcGuardFragment();
        fragment.userId = userId;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fans_pc_guard_fragment;
    }

    @Override
    protected void initWidget(View view) {
        setUnBinder(ButterKnife.bind(this, view));
        btnGuard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxBus.get().send(RxBusCode.RX_BUS_NOTICE_CODE_GUARD);
            }
        });
        isPrepared = true;
        mHeaderView = new FHHeaderView(getContext());
        fansSwip.setHeaderView(mHeaderView);// add headerView
        fansSwip.addOnRefreshListener(this);
        adapter = new PcGuardRankAdapter(datas);
        fansRv.setLayoutManager(new LinearLayoutManager(getContext()));
        //添加分隔线
        fansRv.addItemDecoration(new ListViewDecoration(1));
        fansRv.setAdapter(adapter);
//        adapter.loadMoreEnd();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    RxBus.get().send(RxBusCode.RX_BUS_NOTICE_CODE_USERINFO,
                            datas.get(position).getUserId());
            }
        });

        RecycleViewScrollHelper mScrollHelper = new RecycleViewScrollHelper(new RecycleViewScrollHelper.OnScrollPositionChangedListener() {
            @Override
            public void onScrollToTop() {

            }

            @Override
            public void onScrollToBottom() {
                offset=datas.size();
                new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                        .doGetUserGuardsCall(new GetUserGuardsRequest(userId, String.valueOf(offset), "20"))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<GetUserGuardsResponse>() {
                            @Override
                            public void accept(@NonNull GetUserGuardsResponse responce) throws Exception {
                                if (responce.getCode() == 0) {
                                    if(responce.getGuardsResponseDatas().size()>0) {
                                        datas.addAll(responce.getGuardsResponseDatas());
                                        adapter.setNewData(datas);
                                    }
                                } else {
                                    AppLogger.e(responce.getErrMsg());
                                }
//                                adapter.loadMoreEnd();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {

                            }
                        })
                );
            }

            @Override
            public void onScrollToUnknown(boolean isTopViewVisible, boolean isBottomViewVisible) {

            }
        });
        mScrollHelper.attachToRecycleView(fansRv);

    }



    @Override
    protected void lazyLoad() {
        if (isVisible && isPrepared && datas.size() == 0) {
            getData();
        }
    }

    private void getData() {
        AppLogger.e("getData");
        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .doGetUserGuardsCall(new GetUserGuardsRequest(userId, "0", "20"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetUserGuardsResponse>() {
                    @Override
                    public void accept(@NonNull GetUserGuardsResponse responce) throws Exception {
                        if (responce.getCode() == 0) {
                            AppLogger.e("getDataSuccess");
                            if (datas.size() > 0) {
                                datas.clear();
                            }
                            datas.addAll(responce.getGuardsResponseDatas());
                            if (responce.getGuardsResponseDatas().size() > 0) {
                                emptyView.setVisibility(View.GONE);
                                fansSwip.setVisibility(View.VISIBLE);
                                adapter.setNewData(datas);
                            } else {
                                emptyView.setVisibility(View.VISIBLE);
                                fansSwip.setVisibility(View.GONE);
                            }
                        } else {
                            AppLogger.e("getDataFail");
                            AppLogger.e(responce.getErrMsg());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        AppLogger.e(throwable.getMessage());
                    }
                })
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        if (datas.size() > 0) {
            datas.clear();
            adapter.setNewData(datas);
        }

        if (!isFreshing) {
            isFreshing = true;
            new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                    .doGetUserGuardsCall(new GetUserGuardsRequest(userId, "0", "20"))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<GetUserGuardsResponse>() {
                        @Override
                        public void accept(@NonNull GetUserGuardsResponse responce) throws Exception {
                            if (responce.getCode() == 0) {
                                AppLogger.e("getDataSuccess");
                                datas.addAll(responce.getGuardsResponseDatas());
                                if (responce.getGuardsResponseDatas().size() > 0) {
                                    emptyView.setVisibility(View.GONE);
                                    fansSwip.setVisibility(View.VISIBLE);
                                    adapter.setNewData(datas);
                                } else {
                                    emptyView.setVisibility(View.VISIBLE);
                                    fansSwip.setVisibility(View.GONE);
                                }
                            } else {
                                AppLogger.e("getDataFail");
                                AppLogger.e(responce.getErrMsg());
                            }
                            isFreshing = false;
                            fansSwip.refreshComplete();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {
                            AppLogger.e(throwable.getMessage());
                        }
                    })
            );
        }
    }
}
