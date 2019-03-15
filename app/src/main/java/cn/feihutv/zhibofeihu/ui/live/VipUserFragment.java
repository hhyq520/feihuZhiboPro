package cn.feihutv.zhibofeihu.ui.live;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.RoomVipRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.RoomVipResponce;
import cn.feihutv.zhibofeihu.rxbus.RxBus;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.ui.base.BaseFragment;
import cn.feihutv.zhibofeihu.ui.live.adapter.RoomVipAdapter;
import cn.feihutv.zhibofeihu.ui.widget.swiperefreshlayout.FHHeaderView;
import cn.feihutv.zhibofeihu.ui.widget.swiperefreshlayout.RecycleViewScrollHelper;
import cn.feihutv.zhibofeihu.ui.widget.swiperefreshlayout.VRefreshLayout;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.ListViewDecoration;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by huanghao on 2017/11/10.
 */

public class VipUserFragment extends BaseFragment implements VRefreshLayout.OnRefreshListener {

    @BindView(R.id.vip_rv)
    RecyclerView vipRv;
    @BindView(R.id.vip_swip)
    VRefreshLayout vipSwip;
    @BindView(R.id.empty_view)
    LinearLayout emptyView;
    Unbinder unbinder;
    RoomVipAdapter adapter;
    @BindView(R.id.line_bottom)
    LinearLayout lineBottom;
    @BindView(R.id.btn_vip)
    Button btnVip;
    @BindView(R.id.btn_vip2)
    Button btn_vip;
    @BindView(R.id.text_tip)
    TextView textTip;
    @BindView(R.id.text_vip)
    TextView textVip;
    private int count = 20;
    private boolean isPrepared;
    private List<RoomVipResponce.RoomVipModel> datas = new ArrayList<>();
    private int offset = 0;
    private FHHeaderView mHeaderView;
    private boolean isHost = false;

    public static VipUserFragment newInstance(boolean isHost) {
        Bundle args = new Bundle();
        VipUserFragment fragment = new VipUserFragment();
        fragment.setArguments(args);
        fragment.isHost = isHost;
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.vvip_user_fragment;
    }

    @Override
    protected void initWidget(View view) {
        setUnBinder(ButterKnife.bind(this, view));
        isPrepared = true;
        adapter = new RoomVipAdapter(datas);
        vipRv.setLayoutManager(new LinearLayoutManager(getContext()));
        //添加分隔线
        vipRv.addItemDecoration(new ListViewDecoration(5));
        vipRv.setAdapter(adapter);
        mHeaderView = new FHHeaderView(getContext());
        vipSwip.setHeaderView(mHeaderView);// add headerView
        vipSwip.addOnRefreshListener(this);
//        adapter.loadMoreEnd();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                RxBus.get().send(RxBusCode.RX_BUS_NOTICE_CODE_USERINFO,
                        datas.get(position).getUserId());
            }
        });
        if (SharePreferenceUtil.getSessionInt(FeihuZhiboApplication.getApplication(), "PREF_KEY_VIP") > 0) {
            if (!SharePreferenceUtil.getSessionBoolean(FeihuZhiboApplication.getApplication(), "PREF_KEY_VIPEXPIRED")) {
                btn_vip.setText("加速升级");
                textVip.setText("VIP等级越高,特权越多,抓紧升级！");
            }
        }


        if (isHost) {
            lineBottom.setVisibility(View.GONE);
            btn_vip.setVisibility(View.GONE);
            btnVip.setVisibility(View.GONE);
            textTip.setText("暂无贵宾哦,您不去展示下您的才艺吗~");
        }

        RecycleViewScrollHelper mScrollHelper = new RecycleViewScrollHelper(new RecycleViewScrollHelper.OnScrollPositionChangedListener() {
            @Override
            public void onScrollToTop() {

            }

            @Override
            public void onScrollToBottom() {
                new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                        .doRoomVipApiCall(new RoomVipRequest(offset, count))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<RoomVipResponce>() {
                            @Override
                            public void accept(@NonNull RoomVipResponce responce) throws Exception {
                                if (responce.getCode() == 0) {
                                    offset = responce.getRoomVipData().getNextOffset();
                                    if (responce.getRoomVipData().getRoomVipModelList().size() > 0) {
                                        datas.addAll(responce.getRoomVipData().getRoomVipModelList());
                                        adapter.setNewData(datas);
                                    }
                                } else {
                                    AppLogger.e(responce.getErrMsg());
                                }
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
        mScrollHelper.attachToRecycleView(vipRv);
        getData();
    }

    @Override
    protected void lazyLoad() {
//        if(isPrepared&&isVisible&&datas.size()==0) {
//            AppLogger.e("lazyLoad");
//
//            isPrepared=false;
//        }
    }

    @Override
    public void onRefresh() {
        AppLogger.e("onfresh");
        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .doRoomVipApiCall(new RoomVipRequest(0, count))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RoomVipResponce>() {
                    @Override
                    public void accept(@NonNull RoomVipResponce responce) throws Exception {
                        if (responce.getCode() == 0) {
                            AppLogger.e("getDataSuccess");
                            offset = responce.getRoomVipData().getNextOffset();
                            if (datas.size() > 0) {
                                datas.clear();
                            }
                            datas.addAll(responce.getRoomVipData().getRoomVipModelList());
                            adapter.setNewData(datas);
                            if (responce.getRoomVipData().getRoomVipModelList().size() > 0) {
                                emptyView.setVisibility(View.GONE);
                                vipSwip.setVisibility(View.VISIBLE);
                                if (!isHost) {
                                    lineBottom.setVisibility(View.VISIBLE);
                                }
                            } else {
                                emptyView.setVisibility(View.VISIBLE);
                                vipSwip.setVisibility(View.GONE);
                                lineBottom.setVisibility(View.GONE);
                            }
                        } else {
                            AppLogger.e("getDataFail");
                            AppLogger.e(responce.getErrMsg());
                        }
                        vipSwip.refreshComplete();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        AppLogger.e(throwable.getMessage());
                    }
                })
        );
    }

    private void getData() {
        AppLogger.e("getData");
        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .doRoomVipApiCall(new RoomVipRequest(0, count))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RoomVipResponce>() {
                    @Override
                    public void accept(@NonNull RoomVipResponce responce) throws Exception {
                        if (responce.getCode() == 0) {
                            AppLogger.e("getDataSuccess");
                            offset = responce.getRoomVipData().getNextOffset();
                            if (datas.size() > 0) {
                                datas.clear();
                            }
                            datas.addAll(responce.getRoomVipData().getRoomVipModelList());
                            adapter.setNewData(datas);
                            if (responce.getRoomVipData().getRoomVipModelList().size() > 0) {
                                emptyView.setVisibility(View.GONE);
                                vipSwip.setVisibility(View.VISIBLE);
                                if (!isHost) {
                                    lineBottom.setVisibility(View.VISIBLE);
                                }
                            } else {
                                emptyView.setVisibility(View.VISIBLE);
                                vipSwip.setVisibility(View.GONE);
                                lineBottom.setVisibility(View.GONE);
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
        if (datas.size() > 0) {
            datas.clear();
        }
        unbinder.unbind();
    }


    @OnClick({R.id.btn_vip, R.id.btn_vip2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_vip:
            case R.id.btn_vip2:
                RxBus.get().send(RxBusCode.RX_BUS_NOTICE_CODE_VIP);
                break;
        }
    }
}
