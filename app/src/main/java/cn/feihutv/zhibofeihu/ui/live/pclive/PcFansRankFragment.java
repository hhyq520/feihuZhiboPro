package cn.feihutv.zhibofeihu.ui.live.pclive;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomContriRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomContriResponce;
import cn.feihutv.zhibofeihu.rxbus.RxBus;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.ui.base.BaseFragment;
import cn.feihutv.zhibofeihu.ui.live.adapter.FansRankAdapter;
import cn.feihutv.zhibofeihu.ui.live.adapter.PcFansRankAdapter;
import cn.feihutv.zhibofeihu.ui.widget.swiperefreshlayout.FHHeaderView;
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
 * Created by huanghao on 2017/11/11.
 */

public class PcFansRankFragment extends BaseFragment implements VRefreshLayout.OnRefreshListener{

    @BindView(R.id.fans_rv)
    RecyclerView fansRv;
    @BindView(R.id.fans_swip)
    VRefreshLayout fansSwip;
    @BindView(R.id.text_tip)
    TextView textTip;
    @BindView(R.id.empty_view)
    LinearLayout emptyView;
    Unbinder unbinder;
    private int type;
    private FHHeaderView mHeaderView;
    private PcFansRankAdapter adapter;
    private boolean isPrepared;
    private boolean isFreshing=false;
    private List<LoadRoomContriResponce.RoomContriData> datas=new ArrayList<>();
    public static PcFansRankFragment newInstance(int type) {
        Bundle args = new Bundle();
        PcFansRankFragment fragment = new PcFansRankFragment();
        fragment.type = type;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.pc_fans_rank_fragment;
    }

    @Override
    protected void initWidget(View view) {
        setUnBinder(ButterKnife.bind(this, view));
        isPrepared=true;
        mHeaderView = new FHHeaderView(getContext());
        fansSwip.setHeaderView(mHeaderView);// add headerView
        fansSwip.addOnRefreshListener(this);
        adapter=new PcFansRankAdapter(datas);
        fansRv.setLayoutManager(new LinearLayoutManager(getContext()));
        //添加分隔线
        fansRv.addItemDecoration(new ListViewDecoration(1));
        fansRv.setAdapter(adapter);
        adapter.loadMoreEnd();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    RxBus.get().send(RxBusCode.RX_BUS_NOTICE_CODE_USERINFO,
                            datas.get(position).getUserId());
            }
        });
    }



    @Override
    protected void lazyLoad() {
            if(isVisible&&isPrepared&&datas.size()==0){
                getData();
            }
    }

    private void getData(){
        AppLogger.e("getData");
        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .doLoadRoomContriApiCall(new LoadRoomContriRequest("",type))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoadRoomContriResponce>() {
                    @Override
                    public void accept(@NonNull LoadRoomContriResponce responce) throws Exception {
                        if(responce.getCode()==0) {
                            AppLogger.e("getDataSuccess");
                            if(datas.size()>0){
                                datas.clear();
                            }
                            datas.addAll(responce.getRoomContriDataList());
                            if(responce.getRoomContriDataList().size()>0){
                                emptyView.setVisibility(View.GONE);
                                fansSwip.setVisibility(View.VISIBLE);
                                adapter.setNewData(datas);
                            }else{
                                emptyView.setVisibility(View.VISIBLE);
                                fansSwip.setVisibility(View.GONE);
                            }
                        }else{
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
        // TODO: inflate a fragment view
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
        if(datas.size()>0){
            datas.clear();
            adapter.setNewData(datas);
        }
        if(!isFreshing) {
            isFreshing=true;
            new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                    .doLoadRoomContriApiCall(new LoadRoomContriRequest("",type))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<LoadRoomContriResponce>() {
                        @Override
                        public void accept(@NonNull LoadRoomContriResponce responce) throws Exception {
                            if(responce.getCode()==0) {
                                AppLogger.e("getDataSuccess");
                                datas.addAll(responce.getRoomContriDataList());
                                if(responce.getRoomContriDataList().size()>0){
                                    emptyView.setVisibility(View.GONE);
                                    fansSwip.setVisibility(View.VISIBLE);
                                    adapter.setNewData(datas);
                                }else{
                                    emptyView.setVisibility(View.VISIBLE);
                                    fansSwip.setVisibility(View.GONE);
                                }
                            }else{
                                AppLogger.e("getDataFail");
                                AppLogger.e(responce.getErrMsg());
                            }
                            isFreshing=false;
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
