package cn.feihutv.zhibofeihu.ui.live;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.LoadRoomGuestRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.LoadRoomGuestResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.LoadRoomMemberResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.LoadRoomMembersRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetUserGuardsRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetUserGuardsResponse;
import cn.feihutv.zhibofeihu.rxbus.RxBus;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.ui.base.BaseFragment;
import cn.feihutv.zhibofeihu.ui.live.adapter.OnlineUserAdapter;
import cn.feihutv.zhibofeihu.ui.widget.swiperefreshlayout.FHHeaderView;
import cn.feihutv.zhibofeihu.ui.widget.swiperefreshlayout.RecycleViewScrollHelper;
import cn.feihutv.zhibofeihu.ui.widget.swiperefreshlayout.VRefreshLayout;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.ListViewDecoration;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by huanghao on 2017/11/10.
 */

public class OnLineUserFragment extends BaseFragment implements VRefreshLayout.OnRefreshListener {

    @BindView(R.id.vip_rv)
    RecyclerView vipRv;
    @BindView(R.id.vip_swip)
    VRefreshLayout vipSwip;
    @BindView(R.id.empty_view)
    LinearLayout emptyView;
    OnlineUserAdapter adapter;
    @BindView(R.id.text_tip)
    TextView textTip;
    Unbinder unbinder1;
    @BindView(R.id.text_user)
    TextView textUser;
    private int count = 20;
    private boolean isPrepared;
    private List<LoadRoomMemberResponce.MemberData> datas = new ArrayList<>();
    private int offset = 0;
    private String type = "mgrs";
    private FHHeaderView mHeaderView;

    public static OnLineUserFragment newInstance() {
        Bundle args = new Bundle();
        OnLineUserFragment fragment = new OnLineUserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.vip_user_fragment;
    }

    @Override
    protected void initWidget(View view) {
        setUnBinder(ButterKnife.bind(this, view));
        isPrepared = true;
        adapter = new OnlineUserAdapter(datas);
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
                Bundle bundle =new Bundle();
                bundle.putString("userId",datas.get(position).getUserId());
                bundle.putBoolean("isBangdan",false);
                RxBus.get().send(RxBusCode.RX_BUS_NOTICE_CODE_USERINFO,
                        bundle);
            }
        });

        RecycleViewScrollHelper mScrollHelper = new RecycleViewScrollHelper(new RecycleViewScrollHelper.OnScrollPositionChangedListener() {
            @Override
            public void onScrollToTop() {

            }

            @Override
            public void onScrollToBottom() {
                new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                        .doLoadRoomMembersApiCall(new LoadRoomMembersRequest(offset, count, type))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<LoadRoomMemberResponce>() {
                            @Override
                            public void accept(@NonNull LoadRoomMemberResponce responce) throws Exception {
                                if (responce.getCode() == 0) {
                                    offset = responce.getRoomMemberData().getNextOffset();
                                    type = responce.getRoomMemberData().getNextType();
                                    if(responce.getRoomMemberData().getMemberDatas().size()>0) {
                                        datas.addAll(responce.getRoomMemberData().getMemberDatas());
                                        adapter.setNewData(datas);
                                    }else {
                                        textUser.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    AppLogger.e(responce.getErrMsg());
                                    textUser.setVisibility(View.VISIBLE);
                                }
                                loadRoomGuestCnt();
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
        textTip.setText("哎呀！怎么会没有人呢~");
        getData();
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onRefresh() {
        AppLogger.e("onfresh");
        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .doLoadRoomMembersApiCall(new LoadRoomMembersRequest(0, count, type))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoadRoomMemberResponce>() {
                    @Override
                    public void accept(@NonNull LoadRoomMemberResponce responce) throws Exception {
                        if (responce.getCode() == 0) {
                            AppLogger.e("getDataSuccess");
                            offset = responce.getRoomMemberData().getNextOffset();
                            type = responce.getRoomMemberData().getNextType();
                            if (datas.size() > 0) {
                                datas.clear();
                            }
                            datas.addAll(responce.getRoomMemberData().getMemberDatas());
                            adapter.setNewData(datas);
                            if (responce.getRoomMemberData().getMemberDatas().size() > 0) {
                                emptyView.setVisibility(View.GONE);
                                vipSwip.setVisibility(View.VISIBLE);
                            } else {
                                emptyView.setVisibility(View.VISIBLE);
                                vipSwip.setVisibility(View.GONE);
                            }
                        } else {
                            AppLogger.e("getDataFail");
                            AppLogger.e(responce.getErrMsg());
                        }
                        vipSwip.refreshComplete();
                        loadRoomGuestCnt();
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
                .doLoadRoomMembersApiCall(new LoadRoomMembersRequest(0, count, type))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoadRoomMemberResponce>() {
                    @Override
                    public void accept(@NonNull LoadRoomMemberResponce responce) throws Exception {
                        if (responce.getCode() == 0) {
                            AppLogger.e("getDataSuccess");
                            offset = responce.getRoomMemberData().getNextOffset();
                            type = responce.getRoomMemberData().getNextType();
                            if (datas.size() > 0) {
                                datas.clear();
                            }
                            datas.addAll(responce.getRoomMemberData().getMemberDatas());
                            adapter.setNewData(datas);
                            if (responce.getRoomMemberData().getMemberDatas().size() > 0) {
                                emptyView.setVisibility(View.GONE);
                                vipSwip.setVisibility(View.VISIBLE);
                            } else {
                                emptyView.setVisibility(View.VISIBLE);
                                vipSwip.setVisibility(View.GONE);
                            }
                            loadRoomGuestCnt();
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

    private void loadRoomGuestCnt(){
         new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                         .doLoadRoomGuestApiCall(new LoadRoomGuestRequest())
                         .subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribe(new Consumer<LoadRoomGuestResponce>() {
                             @Override
                             public void accept(@NonNull LoadRoomGuestResponce responce) throws Exception {
                                    if(responce.getCode()==0){
                                    }else{
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
    public void onDestroyView() {
        super.onDestroyView();
        if (datas.size() > 0) {
            datas.clear();
        }
        unbinder1.unbind();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder1 = ButterKnife.bind(this, rootView);
        return rootView;
    }
}
