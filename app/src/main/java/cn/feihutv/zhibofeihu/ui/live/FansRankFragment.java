package cn.feihutv.zhibofeihu.ui.live;

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
import cn.feihutv.zhibofeihu.data.network.http.ApiEndPoint;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomContriRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomContriResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.RoomVipRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.RoomVipResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.ForcedCloseLivePush;
import cn.feihutv.zhibofeihu.rxbus.RxBus;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.ui.base.BaseFragment;
import cn.feihutv.zhibofeihu.ui.live.adapter.FansRankAdapter;
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
 * Created by huanghao on 2017/11/11.
 */

public class FansRankFragment extends BaseFragment implements VRefreshLayout.OnRefreshListener{

    @BindView(R.id.fans_rv)
    RecyclerView fansRv;
    @BindView(R.id.fans_swip)
    VRefreshLayout fansSwip;
    @BindView(R.id.text_tip)
    TextView textTip;
    @BindView(R.id.frm_bg)
    FrameLayout frmBg;
    @BindView(R.id.empty_view)
    LinearLayout emptyView;
    Unbinder unbinder;
    private int type;
    private FHHeaderView mHeaderView;
    private FansRankAdapter adapter;
    private boolean isPrepared;
    private boolean isFreshing=false;
    private boolean isFirst=true;
    private List<LoadRoomContriResponce.RoomContriData> datas=new ArrayList<>();
    public static FansRankFragment newInstance(int type) {
        Bundle args = new Bundle();
        FansRankFragment fragment = new FansRankFragment();
        fragment.type = type;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fans_rank_fragment;
    }

    @Override
    protected void initWidget(View view) {
        setUnBinder(ButterKnife.bind(this, view));
        isPrepared=true;
        mHeaderView = new FHHeaderView(getContext());
        fansSwip.setHeaderView(mHeaderView);// add headerView
        fansSwip.addOnRefreshListener(this);
        adapter=new FansRankAdapter(datas);
        setHeaderView();
        fansRv.setLayoutManager(new LinearLayoutManager(getContext()));
        //添加分隔线
        fansRv.addItemDecoration(new ListViewDecoration(1));
        fansRv.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(datas.size()>3) {
                    RxBus.get().send(RxBusCode.RX_BUS_NOTICE_CODE_USERINFO,
                            datas.get(position + 3).getUserId());
                }
            }
        });
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
    private ImageView imgGuard1;
    private ImageView imgGuard2;;
    private ImageView imgGuard3;;
    private ImageView imgVip1;
    private ImageView imgVip2;
    private ImageView imgVip3;
    private LinearLayout linearLayoutNo1;
    private LinearLayout linearLayoutNo2;
    private LinearLayout linearLayoutNo3;

    private void setHeaderView(){
        View mHeadView = LayoutInflater.from(getContext()).inflate(R.layout.view_head_fansrank, null);
        imgHead1=(ImageView) mHeadView.findViewById(R.id.img_head1);
        imgHead2=(ImageView) mHeadView.findViewById(R.id.img_head2);
        imgHead3=(ImageView) mHeadView.findViewById(R.id.img_head3);
        textNickname1=(TextView) mHeadView.findViewById(R.id.text_nickname1);
        textNickname2=(TextView) mHeadView.findViewById(R.id.nick_name2);
        textNickname3=(TextView) mHeadView.findViewById(R.id.text_nickname3);
        textHubi1=(TextView) mHeadView.findViewById(R.id.text_hubi1);
        textHubi2=(TextView) mHeadView.findViewById(R.id.text_hubi2);
        textHubi3=(TextView) mHeadView.findViewById(R.id.text_hubi3);
        imgLevel1=(ImageView) mHeadView.findViewById(R.id.img_level1);
        imgLevel2=(ImageView) mHeadView.findViewById(R.id.img_level2);
        imgLevel3=(ImageView) mHeadView.findViewById(R.id.img_level3);
        imgGuard1=(ImageView) mHeadView.findViewById(R.id.img_guard1);
        imgGuard2=(ImageView) mHeadView.findViewById(R.id.img_guard2);
        imgGuard3=(ImageView) mHeadView.findViewById(R.id.img_guard3);
        imgVip1=(ImageView) mHeadView.findViewById(R.id.img_vip1);
        imgVip2=(ImageView) mHeadView.findViewById(R.id.img_vip2);
        imgVip3=(ImageView) mHeadView.findViewById(R.id.img_vip3);
        linearLayoutNo1=(LinearLayout) mHeadView.findViewById(R.id.lin_no1);
        linearLayoutNo2=(LinearLayout) mHeadView.findViewById(R.id.lin_no2);
        linearLayoutNo3=(LinearLayout) mHeadView.findViewById(R.id.lin_no3);
        linearLayoutNo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(datas.size()>0) {
                    RxBus.get().send(RxBusCode.RX_BUS_NOTICE_CODE_USERINFO,
                            datas.get(0).getUserId());
                }
            }
        });
        linearLayoutNo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(datas.size()>1) {
                    RxBus.get().send(RxBusCode.RX_BUS_NOTICE_CODE_USERINFO,
                            datas.get(1).getUserId());
                }
            }
        });
        linearLayoutNo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(datas.size()>2) {
                    RxBus.get().send(RxBusCode.RX_BUS_NOTICE_CODE_USERINFO,
                            datas.get(2).getUserId());
                }
            }
        });
        FrameLayout frm_top=(FrameLayout) mHeadView.findViewById(R.id.frm_top);
        ViewGroup.LayoutParams layoutParams=frm_top.getLayoutParams();
        int width=getContext().getResources().getDisplayMetrics().widthPixels- TCUtils.dip2px(getContext(),22);
        layoutParams.width=width;
        layoutParams.height=width*180/750;
        frm_top.setLayoutParams(layoutParams);
        adapter.setHeaderView(mHeadView);
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
                            if(datas.size()>0){
                                frmBg.setBackgroundResource(R.drawable.bg_fensi);
                                emptyView.setVisibility(View.GONE);
                                fansSwip.setVisibility(View.VISIBLE);
                                if(datas.size()==1){
                                    linearLayoutNo1.setVisibility(View.VISIBLE);
                                    linearLayoutNo2.setVisibility(View.INVISIBLE);
                                    linearLayoutNo3.setVisibility(View.INVISIBLE);
                                    TCUtils.showPicWithUrl(getContext(), imgHead1, datas.get(0).getHeadUrl(), R.drawable.face);
                                    textNickname1.setText(datas.get(0).getNickName());
                                    textHubi1.setText(FHUtils.intToF(datas.get(0).getHB()));
                                    TCUtils.showLevelWithUrl(getContext(),imgLevel1,datas.get(0).getLevel());
                                    if(datas.get(0).getVip()>0){
                                        if(datas.get(0).isVipExpired()){
                                            imgVip1.setVisibility(View.GONE);
                                        }else{
                                            String cosVipIconRootPath = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosVipIconRootPath();
                                            Glide.with(getContext()).load(cosVipIconRootPath + "icon_vip" +datas.get(0).getVip() + ".png").into(imgVip1);
                                        }
                                    }else {
                                        imgVip1.setVisibility(View.GONE);
                                    }
                                    if(datas.get(0).getGuardType()==0){
                                        imgGuard1.setVisibility(View.GONE);
                                    }else{
                                        if(datas.get(0).isGuardExpired()){
                                            imgGuard1.setVisibility(View.GONE);
                                        }else{
                                            imgGuard1.setVisibility(View.VISIBLE);
                                        }
                                    }
                                    List<LoadRoomContriResponce.RoomContriData> newData=new ArrayList<LoadRoomContriResponce.RoomContriData>();
                                    adapter.setNewData(newData);
                                }else if(datas.size()==2){
                                    linearLayoutNo1.setVisibility(View.VISIBLE);
                                    linearLayoutNo2.setVisibility(View.VISIBLE);
                                    linearLayoutNo3.setVisibility(View.INVISIBLE);
                                    TCUtils.showPicWithUrl(getContext(), imgHead1, datas.get(0).getHeadUrl(), R.drawable.face);
                                    textNickname1.setText(datas.get(0).getNickName());
                                    textHubi1.setText(FHUtils.intToF(datas.get(0).getHB())+"");
                                    TCUtils.showLevelWithUrl(getContext(),imgLevel1,datas.get(0).getLevel());
                                    if(datas.get(0).getVip()>0){
                                        if(datas.get(0).isVipExpired()){
                                            imgVip1.setVisibility(View.GONE);
                                        }else{
                                            String cosVipIconRootPath = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosVipIconRootPath();
                                            Glide.with(getContext()).load(cosVipIconRootPath + "icon_vip" +datas.get(0).getVip() + ".png").into(imgVip1);
                                        }
                                    }else {
                                        imgVip1.setVisibility(View.GONE);
                                    }
                                    if(datas.get(0).getGuardType()==0){
                                        imgGuard1.setVisibility(View.GONE);
                                    }else{
                                        if(datas.get(0).isGuardExpired()){
                                            imgGuard1.setVisibility(View.GONE);
                                        }else{
                                            imgGuard1.setVisibility(View.VISIBLE);
                                        }
                                    }

                                    TCUtils.showPicWithUrl(getContext(), imgHead2, datas.get(1).getHeadUrl(), R.drawable.face);
                                    textNickname2.setText(datas.get(1).getNickName());
                                    textHubi2.setText(FHUtils.intToF(datas.get(1).getHB())+"");
                                    TCUtils.showLevelWithUrl(getContext(),imgLevel2,datas.get(1).getLevel());
                                    if(datas.get(1).getVip()>0){
                                        if(datas.get(1).isVipExpired()){
                                            imgVip2.setVisibility(View.GONE);
                                        }else{
                                            String cosVipIconRootPath = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosVipIconRootPath();
                                            Glide.with(getContext()).load(cosVipIconRootPath + "icon_vip" +datas.get(1).getVip() + ".png").into(imgVip2);
                                        }
                                    }else {
                                        imgVip2.setVisibility(View.GONE);
                                    }
                                    if(datas.get(1).getGuardType()==0){
                                        imgGuard2.setVisibility(View.GONE);
                                    }else{
                                        if(datas.get(1).isGuardExpired()){
                                            imgGuard2.setVisibility(View.GONE);
                                        }else{
                                            imgGuard2.setVisibility(View.VISIBLE);
                                        }
                                    }
                                    List<LoadRoomContriResponce.RoomContriData> newData=new ArrayList<LoadRoomContriResponce.RoomContriData>();
                                    adapter.setNewData(newData);
                                }else if(datas.size()==3){
                                    inithead();
                                    List<LoadRoomContriResponce.RoomContriData> newData=new ArrayList<LoadRoomContriResponce.RoomContriData>();
                                    adapter.setNewData(newData);
                                }else if(datas.size()>3){
                                    inithead();
                                    List<LoadRoomContriResponce.RoomContriData> newData=new ArrayList<LoadRoomContriResponce.RoomContriData>();
                                   for(int i=0;i<datas.size();i++){
                                       if(i>2) {
                                           newData.add(datas.get(i));
                                       }
                                   }
                                    adapter.setNewData(newData);
                                }
                            }else{
                                frmBg.setBackgroundResource(R.color.bangdan_bg);
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
        linearLayoutNo1.setVisibility(View.INVISIBLE);
        linearLayoutNo2.setVisibility(View.INVISIBLE);
        linearLayoutNo3.setVisibility(View.INVISIBLE);

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
                                    frmBg.setBackgroundResource(R.drawable.bg_fensi);
                                    fansSwip.setVisibility(View.VISIBLE);
                                    if(datas.size()==1){
                                        linearLayoutNo1.setVisibility(View.VISIBLE);
                                        linearLayoutNo2.setVisibility(View.INVISIBLE);
                                        linearLayoutNo3.setVisibility(View.INVISIBLE);
                                        TCUtils.showPicWithUrl(getContext(), imgHead1, datas.get(0).getHeadUrl(), R.drawable.face);
                                        textNickname1.setText(datas.get(0).getNickName());
                                        textHubi1.setText(FHUtils.intToF(datas.get(0).getHB())+"");
                                        TCUtils.showLevelWithUrl(getContext(),imgLevel1,datas.get(0).getLevel());
                                        if(datas.get(0).getVip()>0){
                                            if(datas.get(0).isVipExpired()){
                                                imgVip1.setVisibility(View.GONE);
                                            }else{
                                                String cosVipIconRootPath = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosVipIconRootPath();
                                                Glide.with(getContext()).load(cosVipIconRootPath + "icon_vip" +datas.get(0).getVip() + ".png").into(imgVip1);
                                            }
                                        }else {
                                            imgVip1.setVisibility(View.GONE);
                                        }
                                        if(datas.get(0).getGuardType()==0){
                                            imgGuard1.setVisibility(View.GONE);
                                        }else{
                                            if(datas.get(0).isGuardExpired()){
                                                imgGuard1.setVisibility(View.GONE);
                                            }else{
                                                imgGuard1.setVisibility(View.VISIBLE);
                                            }
                                        }
                                        List<LoadRoomContriResponce.RoomContriData> newData=new ArrayList<LoadRoomContriResponce.RoomContriData>();
                                        adapter.setNewData(newData);
                                    }else if(datas.size()==2){
                                        linearLayoutNo1.setVisibility(View.VISIBLE);
                                        linearLayoutNo2.setVisibility(View.VISIBLE);
                                        linearLayoutNo3.setVisibility(View.INVISIBLE);
                                        TCUtils.showPicWithUrl(getContext(), imgHead1, datas.get(0).getHeadUrl(), R.drawable.face);
                                        textNickname1.setText(datas.get(0).getNickName());
                                        textHubi1.setText(FHUtils.intToF(datas.get(0).getHB())+"");
                                        TCUtils.showLevelWithUrl(getContext(),imgLevel1,datas.get(0).getLevel());
                                        if(datas.get(0).getVip()>0){
                                            if(datas.get(0).isVipExpired()){
                                                imgVip1.setVisibility(View.GONE);
                                            }else{
                                                String cosVipIconRootPath = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosVipIconRootPath();
                                                Glide.with(getContext()).load(cosVipIconRootPath + "icon_vip" +datas.get(0).getVip() + ".png").into(imgVip1);
                                            }
                                        }else {
                                            imgVip1.setVisibility(View.GONE);
                                        }
                                        if(datas.get(0).getGuardType()==0){
                                            imgGuard1.setVisibility(View.GONE);
                                        }else{
                                            if(datas.get(0).isGuardExpired()){
                                                imgGuard1.setVisibility(View.GONE);
                                            }else{
                                                imgGuard1.setVisibility(View.VISIBLE);
                                            }
                                        }

                                        TCUtils.showPicWithUrl(getContext(), imgHead2, datas.get(1).getHeadUrl(), R.drawable.face);
                                        textNickname2.setText(datas.get(1).getNickName());
                                        textHubi2.setText(FHUtils.intToF(datas.get(1).getHB())+"");
                                        TCUtils.showLevelWithUrl(getContext(),imgLevel2,datas.get(1).getLevel());
                                        if(datas.get(1).getVip()>0){
                                            if(datas.get(1).isVipExpired()){
                                                imgVip2.setVisibility(View.GONE);
                                            }else{
                                                String cosVipIconRootPath = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosVipIconRootPath();
                                                Glide.with(getContext()).load(cosVipIconRootPath + "icon_vip" +datas.get(1).getVip() + ".png").into(imgVip2);
                                            }
                                        }else {
                                            imgVip2.setVisibility(View.GONE);
                                        }
                                        if(datas.get(1).getGuardType()==0){
                                            imgGuard2.setVisibility(View.GONE);
                                        }else{
                                            if(datas.get(1).isGuardExpired()){
                                                imgGuard2.setVisibility(View.GONE);
                                            }else{
                                                imgGuard2.setVisibility(View.VISIBLE);
                                            }
                                        }
                                        List<LoadRoomContriResponce.RoomContriData> newData=new ArrayList<LoadRoomContriResponce.RoomContriData>();
                                        adapter.setNewData(newData);
                                    }else if(datas.size()==3){
                                        inithead();
                                        List<LoadRoomContriResponce.RoomContriData> newData=new ArrayList<LoadRoomContriResponce.RoomContriData>();
                                        adapter.setNewData(newData);
                                    }else if(datas.size()>3){
                                        inithead();
                                        List<LoadRoomContriResponce.RoomContriData> newData=new ArrayList<LoadRoomContriResponce.RoomContriData>();
                                        for(int i=0;i<datas.size();i++){
                                            if(i>2) {
                                                newData.add(datas.get(i));
                                            }
                                        }
                                        adapter.setNewData(newData);
                                    }
                                }else{
                                    frmBg.setBackgroundResource(R.color.bangdan_bg);
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

    private void inithead(){
        linearLayoutNo1.setVisibility(View.VISIBLE);
        linearLayoutNo2.setVisibility(View.VISIBLE);
        linearLayoutNo3.setVisibility(View.VISIBLE);
        TCUtils.showPicWithUrl(getContext(), imgHead1, datas.get(0).getHeadUrl(), R.drawable.face);
        textNickname1.setText(datas.get(0).getNickName());
        textHubi1.setText(FHUtils.intToF(datas.get(0).getHB())+"");
        TCUtils.showLevelWithUrl(getContext(),imgLevel1,datas.get(0).getLevel());
        if(datas.get(0).getVip()>0){
            if(datas.get(0).isVipExpired()){
                imgVip1.setVisibility(View.GONE);
            }else{
                String cosVipIconRootPath = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosVipIconRootPath();
                Glide.with(getContext()).load(cosVipIconRootPath + "icon_vip" +datas.get(0).getVip() + ".png").into(imgVip1);
            }
        }else {
            imgVip1.setVisibility(View.GONE);
        }
        if(datas.get(0).getGuardType()==0){
            imgGuard1.setVisibility(View.GONE);
        }else{
            if(datas.get(0).isGuardExpired()){
                imgGuard1.setVisibility(View.GONE);
            }else{
                imgGuard1.setVisibility(View.VISIBLE);
            }
        }

        TCUtils.showPicWithUrl(getContext(), imgHead2, datas.get(1).getHeadUrl(), R.drawable.face);
        textNickname2.setText(datas.get(1).getNickName());
        textHubi2.setText(FHUtils.intToF(datas.get(1).getHB())+"");
        TCUtils.showLevelWithUrl(getContext(),imgLevel2,datas.get(1).getLevel());
        if(datas.get(1).getVip()>0){
            if(datas.get(1).isVipExpired()){
                imgVip2.setVisibility(View.GONE);
            }else{
                String cosVipIconRootPath = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosVipIconRootPath();
                Glide.with(getContext()).load(cosVipIconRootPath + "icon_vip" +datas.get(1).getVip() + ".png").into(imgVip2);
            }
        }else {
            imgVip2.setVisibility(View.GONE);
        }
        if(datas.get(1).getGuardType()==0){
            imgGuard2.setVisibility(View.GONE);
        }else{
            if(datas.get(1).isGuardExpired()){
                imgGuard2.setVisibility(View.GONE);
            }else{
                imgGuard2.setVisibility(View.VISIBLE);
            }
        }

        TCUtils.showPicWithUrl(getContext(), imgHead3, datas.get(2).getHeadUrl(), R.drawable.face);
        textNickname3.setText(datas.get(2).getNickName());
        textHubi3.setText(FHUtils.intToF(datas.get(2).getHB())+"");
        TCUtils.showLevelWithUrl(getContext(),imgLevel3,datas.get(2).getLevel());
        if(datas.get(2).getVip()>0){
            if(datas.get(2).isVipExpired()){
                imgVip3.setVisibility(View.GONE);
            }else{
                String cosVipIconRootPath = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosVipIconRootPath();
                Glide.with(getContext()).load(cosVipIconRootPath + "icon_vip" +datas.get(2).getVip() + ".png").into(imgVip3);
            }
        }else {
            imgVip3.setVisibility(View.GONE);
        }
        if(datas.get(2).getGuardType()==0){
            imgGuard3.setVisibility(View.GONE);
        }else{
            if(datas.get(2).isGuardExpired()){
                imgGuard3.setVisibility(View.GONE);
            }else{
                imgGuard3.setVisibility(View.VISIBLE);
            }
        }
    }
}
