package cn.feihutv.zhibofeihu.ui.live;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomContriResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetUserGuardsRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetUserGuardsResponse;
import cn.feihutv.zhibofeihu.rxbus.RxBus;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.ui.base.BaseFragment;
import cn.feihutv.zhibofeihu.ui.live.adapter.GuardRankAdapter;
import cn.feihutv.zhibofeihu.ui.widget.swiperefreshlayout.FHHeaderView;
import cn.feihutv.zhibofeihu.ui.widget.swiperefreshlayout.RecycleViewScrollHelper;
import cn.feihutv.zhibofeihu.ui.widget.swiperefreshlayout.VRefreshLayout;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.FHUtils;
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

public class GuardFragment extends BaseFragment implements VRefreshLayout.OnRefreshListener {
    @BindView(R.id.fans_rv)
    RecyclerView fansRv;
    @BindView(R.id.fans_swip)
    VRefreshLayout fansSwip;
    @BindView(R.id.text_tip)
    TextView textTip;
    @BindView(R.id.empty_view)
    LinearLayout emptyView;
    @BindView(R.id.bg_re)
    RelativeLayout bgRel;
    Unbinder unbinder;
    @BindView(R.id.btn_guard)
    Button btnGuard;
    @BindView(R.id.guard_frm)
    FrameLayout guardFrm;
    private String userId;
    private boolean isPrepared;
    private boolean isFreshing = false;
    private GuardRankAdapter adapter;
    private FHHeaderView mHeaderView;
    private int offset = 0;
    private boolean isWatchLive;
    private List<GetUserGuardsResponse.GetUserGuardsResponseData> datas = new ArrayList<>();

    public static GuardFragment newInstance(String userId, boolean isWatchLive) {
        Bundle args = new Bundle();
        GuardFragment fragment = new GuardFragment();
        fragment.userId = userId;
        fragment.isWatchLive = isWatchLive;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fans_guard_fragment;
    }

    @Override
    protected void initWidget(View view) {
        setUnBinder(ButterKnife.bind(this, view));
        if(isWatchLive){
            guardFrm.setVisibility(View.VISIBLE);
        }else{
            guardFrm.setVisibility(View.GONE);
        }
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
        adapter = new GuardRankAdapter(datas);
        setHeaderView();
        fansRv.setLayoutManager(new LinearLayoutManager(getContext()));
        //添加分隔线
        fansRv.addItemDecoration(new ListViewDecoration(1));
        fansRv.setAdapter(adapter);
//        adapter.loadMoreEnd();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (datas.size() > 3) {
                    RxBus.get().send(RxBusCode.RX_BUS_NOTICE_CODE_USERINFO,
                            datas.get(position + 3).getUserId());
                }

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
                                    datas.addAll(responce.getGuardsResponseDatas());
                                    List<GetUserGuardsResponse.GetUserGuardsResponseData> newData = new ArrayList<GetUserGuardsResponse.GetUserGuardsResponseData>();
                                    for (int i = 0; i < datas.size(); i++) {
                                        if (i > 2) {
                                            newData.add(datas.get(i));
                                        }
                                    }
                                    adapter.setNewData(newData);
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

    private ImageView imgHead1;
    private ImageView imgHead2;
    private ImageView imgHead3;
    private TextView textNickname1;
    private TextView textNickname2;
    private TextView textNickname3;
    private TextView textHubi1;
    private TextView textHubi2;
    private TextView textHubi3;
    private ImageView imgLevel1;
    private ImageView imgLevel2;
    private ImageView imgLevel3;
    private LinearLayout linearLayoutNo1;
    private LinearLayout linearLayoutNo2;
    private LinearLayout linearLayoutNo3;

    private void setHeaderView() {
        View mHeadView = LayoutInflater.from(getContext()).inflate(R.layout.view_head_guardrank, null);
        imgHead1 = (ImageView) mHeadView.findViewById(R.id.img_head1);
        imgHead2 = (ImageView) mHeadView.findViewById(R.id.img_head2);
        imgHead3 = (ImageView) mHeadView.findViewById(R.id.img_head3);
        textNickname1 = (TextView) mHeadView.findViewById(R.id.text_nickname1);
        textNickname2 = (TextView) mHeadView.findViewById(R.id.nick_name2);
        textNickname3 = (TextView) mHeadView.findViewById(R.id.text_nickname3);
        textHubi1 = (TextView) mHeadView.findViewById(R.id.text_hubi1);
        textHubi2 = (TextView) mHeadView.findViewById(R.id.text_hubi2);
        textHubi3 = (TextView) mHeadView.findViewById(R.id.text_hubi3);
        imgLevel1 = (ImageView) mHeadView.findViewById(R.id.img_level1);
        imgLevel2 = (ImageView) mHeadView.findViewById(R.id.img_level2);
        imgLevel3 = (ImageView) mHeadView.findViewById(R.id.img_level3);
        linearLayoutNo1 = (LinearLayout) mHeadView.findViewById(R.id.lin_no1);
        linearLayoutNo2 = (LinearLayout) mHeadView.findViewById(R.id.lin_no2);
        linearLayoutNo3 = (LinearLayout) mHeadView.findViewById(R.id.lin_no3);
        linearLayoutNo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (datas.size() > 0) {
                    RxBus.get().send(RxBusCode.RX_BUS_NOTICE_CODE_USERINFO,
                            datas.get(0).getUserId());
                }
            }
        });
        linearLayoutNo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (datas.size() > 1) {
                    RxBus.get().send(RxBusCode.RX_BUS_NOTICE_CODE_USERINFO,
                            datas.get(1).getUserId());
                }
            }
        });
        linearLayoutNo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (datas.size() > 2) {
                    RxBus.get().send(RxBusCode.RX_BUS_NOTICE_CODE_USERINFO,
                            datas.get(2).getUserId());
                }
            }
        });
        FrameLayout frm_top = (FrameLayout) mHeadView.findViewById(R.id.frm_top);
        ViewGroup.LayoutParams layoutParams = frm_top.getLayoutParams();
        int width = getContext().getResources().getDisplayMetrics().widthPixels - TCUtils.dip2px(getContext(), 22);
        layoutParams.width = width;
        layoutParams.height = width * 180 / 750;
        frm_top.setLayoutParams(layoutParams);
        adapter.setHeaderView(mHeadView);
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
                                guardFrm.setBackgroundResource(R.color.app_white);
                                bgRel.setBackgroundResource(R.drawable.bg_fensi);
                                fansSwip.setVisibility(View.VISIBLE);
                                if (datas.size() == 1) {
                                    linearLayoutNo1.setVisibility(View.VISIBLE);
                                    linearLayoutNo2.setVisibility(View.INVISIBLE);
                                    linearLayoutNo3.setVisibility(View.INVISIBLE);
                                    TCUtils.showPicWithUrl(getContext(), imgHead1, datas.get(0).getAvatar(), R.drawable.face);
                                    textNickname1.setText(TCUtils.getLimitString(datas.get(0).getNickname(),5));
                                    textHubi1.setText(FHUtils.intToF(datas.get(0).getFriendliness()) + "");
                                    TCUtils.showLevelWithUrl(getContext(), imgLevel1, datas.get(0).getLevel());
                                    List<GetUserGuardsResponse.GetUserGuardsResponseData> newData = new ArrayList<GetUserGuardsResponse.GetUserGuardsResponseData>();
                                    adapter.setNewData(newData);
                                } else if (datas.size() == 2) {
                                    linearLayoutNo1.setVisibility(View.VISIBLE);
                                    linearLayoutNo2.setVisibility(View.VISIBLE);
                                    linearLayoutNo3.setVisibility(View.INVISIBLE);
                                    TCUtils.showPicWithUrl(getContext(), imgHead1, datas.get(0).getAvatar(), R.drawable.face);
                                    textNickname1.setText(TCUtils.getLimitString(datas.get(0).getNickname(),5));
                                    textHubi1.setText(FHUtils.intToF(datas.get(0).getFriendliness())+ "");
                                    TCUtils.showLevelWithUrl(getContext(), imgLevel1, datas.get(0).getLevel());


                                    TCUtils.showPicWithUrl(getContext(), imgHead2, datas.get(1).getAvatar(), R.drawable.face);
                                    textNickname2.setText(TCUtils.getLimitString(datas.get(1).getNickname(),5));
                                    textHubi2.setText(FHUtils.intToF(datas.get(1).getFriendliness())+ "");
                                    TCUtils.showLevelWithUrl(getContext(), imgLevel2, datas.get(1).getLevel());
                                    List<GetUserGuardsResponse.GetUserGuardsResponseData> newData = new ArrayList<GetUserGuardsResponse.GetUserGuardsResponseData>();
                                    adapter.setNewData(newData);
                                } else if (datas.size() == 3) {
                                    inithead();
                                    List<GetUserGuardsResponse.GetUserGuardsResponseData> newData = new ArrayList<GetUserGuardsResponse.GetUserGuardsResponseData>();
                                    adapter.setNewData(newData);
                                } else {
                                    inithead();
                                    List<GetUserGuardsResponse.GetUserGuardsResponseData> newData = new ArrayList<GetUserGuardsResponse.GetUserGuardsResponseData>();
                                    for (int i = 0; i < datas.size(); i++) {
                                        if (i > 2) {
                                            newData.add(datas.get(i));
                                        }
                                    }
                                    adapter.setNewData(newData);
                                }
                            } else {
                                bgRel.setBackgroundResource(R.color.bangdan_bg);
                                guardFrm.setBackgroundResource(R.color.bangdan_bg);
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
        linearLayoutNo1.setVisibility(View.INVISIBLE);
        linearLayoutNo2.setVisibility(View.INVISIBLE);
        linearLayoutNo3.setVisibility(View.INVISIBLE);

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
                                    guardFrm.setBackgroundResource(R.color.app_white);
                                    bgRel.setBackgroundResource(R.drawable.bg_fensi);
                                    fansSwip.setVisibility(View.VISIBLE);
                                    if (datas.size() == 1) {
                                        linearLayoutNo1.setVisibility(View.VISIBLE);
                                        linearLayoutNo2.setVisibility(View.INVISIBLE);
                                        linearLayoutNo3.setVisibility(View.INVISIBLE);
                                        TCUtils.showPicWithUrl(getContext(), imgHead1, datas.get(0).getAvatar(), R.drawable.face);
                                        textNickname1.setText(TCUtils.getLimitString(datas.get(0).getNickname(),5));
                                        textHubi1.setText(FHUtils.intToF(datas.get(0).getFriendliness())+ "");
                                        TCUtils.showLevelWithUrl(getContext(), imgLevel1, datas.get(0).getLevel());
                                        List<GetUserGuardsResponse.GetUserGuardsResponseData> newData = new ArrayList<GetUserGuardsResponse.GetUserGuardsResponseData>();
                                        adapter.setNewData(newData);
                                    } else if (datas.size() == 2) {
                                        linearLayoutNo1.setVisibility(View.VISIBLE);
                                        linearLayoutNo2.setVisibility(View.VISIBLE);
                                        linearLayoutNo3.setVisibility(View.INVISIBLE);
                                        TCUtils.showPicWithUrl(getContext(), imgHead1, datas.get(0).getAvatar(), R.drawable.face);
                                        textNickname1.setText(TCUtils.getLimitString(datas.get(0).getNickname(),5));
                                        textHubi1.setText(FHUtils.intToF(datas.get(0).getFriendliness())+ "");
                                        TCUtils.showLevelWithUrl(getContext(), imgLevel1, datas.get(0).getLevel());

                                        TCUtils.showPicWithUrl(getContext(), imgHead2, datas.get(1).getAvatar(), R.drawable.face);
                                        textNickname2.setText(TCUtils.getLimitString(datas.get(1).getNickname(),5));
                                        textHubi2.setText(FHUtils.intToF(datas.get(1).getFriendliness()) + "");
                                        TCUtils.showLevelWithUrl(getContext(), imgLevel2, datas.get(1).getLevel());
                                        List<GetUserGuardsResponse.GetUserGuardsResponseData> newData = new ArrayList<GetUserGuardsResponse.GetUserGuardsResponseData>();
                                        adapter.setNewData(newData);
                                    } else if (datas.size() == 3) {
                                        inithead();
                                        List<GetUserGuardsResponse.GetUserGuardsResponseData> newData = new ArrayList<GetUserGuardsResponse.GetUserGuardsResponseData>();
                                        adapter.setNewData(newData);
                                    } else {
                                        inithead();
                                        List<GetUserGuardsResponse.GetUserGuardsResponseData> newData = new ArrayList<GetUserGuardsResponse.GetUserGuardsResponseData>();
                                        for (int i = 0; i < datas.size(); i++) {
                                            if (i > 2) {
                                                newData.add(datas.get(i));
                                            }
                                        }
                                        adapter.setNewData(newData);
                                    }
                                } else {
                                    bgRel.setBackgroundResource(R.color.bangdan_bg);
                                    guardFrm.setBackgroundResource(R.color.bangdan_bg);
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

    private void inithead() {
        linearLayoutNo1.setVisibility(View.VISIBLE);
        linearLayoutNo2.setVisibility(View.VISIBLE);
        linearLayoutNo3.setVisibility(View.VISIBLE);
        TCUtils.showPicWithUrl(getContext(), imgHead1, datas.get(0).getAvatar(), R.drawable.face);
        textNickname1.setText(TCUtils.getLimitString(datas.get(0).getNickname(),5));
        textHubi1.setText(FHUtils.intToF(datas.get(0).getFriendliness())+ "");
        TCUtils.showLevelWithUrl(getContext(), imgLevel1, datas.get(0).getLevel());

        TCUtils.showPicWithUrl(getContext(), imgHead2, datas.get(1).getAvatar(), R.drawable.face);
        textNickname2.setText(TCUtils.getLimitString(datas.get(1).getNickname(),5));
        textHubi2.setText(FHUtils.intToF(datas.get(1).getFriendliness())+ "");
        TCUtils.showLevelWithUrl(getContext(), imgLevel2, datas.get(1).getLevel());


        TCUtils.showPicWithUrl(getContext(), imgHead3, datas.get(2).getAvatar(), R.drawable.face);
        textNickname3.setText(TCUtils.getLimitString(datas.get(2).getNickname(),5));
        textHubi3.setText(FHUtils.intToF(datas.get(2).getFriendliness()) + "");
        TCUtils.showLevelWithUrl(getContext(), imgLevel3, datas.get(2).getLevel());

    }
}
