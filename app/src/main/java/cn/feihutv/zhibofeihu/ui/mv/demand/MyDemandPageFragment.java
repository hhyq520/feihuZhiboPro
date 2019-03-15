package cn.feihutv.zhibofeihu.ui.mv.demand;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.DeleteMVResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.DeleteNeedResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.EditNeedRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.EditNeedResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMyNeedListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMyNeedMVListResponse;
import cn.feihutv.zhibofeihu.ui.base.BaseFragment;
import cn.feihutv.zhibofeihu.ui.mv.MvVideoDetailsActivity;
import cn.feihutv.zhibofeihu.ui.mv.demand.adapter.MvAllDemandListAdapter;
import cn.feihutv.zhibofeihu.ui.mv.demand.adapter.MvDemandMvListAdapter;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.ListViewDecoration;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/
 *     desc   : 我的需求列表
 *     version: 1.0
 * </pre>
 */
public class MyDemandPageFragment extends BaseFragment implements MyDemandPageMvpView {

    @Inject
    MyDemandPageMvpPresenter<MyDemandPageMvpView> mPresenter;
    MvAllDemandListAdapter mMvAllDemandListAdapter;

    //待成交，已完成适配器
    MvDemandMvListAdapter mMvDemandMvListAdapter;

    @BindView(R.id.myList)
    public   XRecyclerView mRecyclerView;

    @BindView(R.id.ll_mv_demand_empty)
    LinearLayout  ll_mv_demand_empty;

    @BindView(R.id.tv_post_demand)
    TextView tv_post_demand;

    @BindView(R.id.tv_sure)
    TextView tv_sure;


    private  String  nextOffset="0";//我的需求列表 offset

    private int type;//mv类型
    private String offset="0"; //已成交 offset
    private String count="20";

    private int loadMyDemandListType=0;//我的需求列表状态；1：下拉刷新；2：加载更多
    private int loadMyDemandMVListType=0;//我的已成交/已完成列表状态；1：下拉刷新；2：加载更多


    List<GetMyNeedListResponse.GetMyNeedList> mGetMyNeedLists=new ArrayList<>();
    List<GetMyNeedMVListResponse.GetMyNeedMVList> mNeedMvListData=new ArrayList<>();

    GetMyNeedListResponse.GetMyNeedList mClickItemData;


    public static MyDemandPageFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt("type",type);
        MyDemandPageFragment fragment = new MyDemandPageFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_demand_page;
    }


    @Override
    protected void initWidget(View view) {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this, view));

        //Presenter调用初始化
        mPresenter.onAttach(MyDemandPageFragment.this);

        type=getArguments().getInt("type");

        switch (type){
            case 0:
                //全部需求
                loadAllDemandUIAdapter();
                mPresenter.getMyNeedList("0","20");
                tv_post_demand.setVisibility(View.VISIBLE);
                break;
            case 1:

                loadMyMVListDemandUIAdapter();
                mPresenter.getMyNeedMVList("5",offset,count);// mv状态   5待成交，3已成交
                break;
            case 2:

                loadMyMVListDemandUIAdapter();
                mPresenter.getMyNeedMVList("3",offset,count);// mv状态   5待成交，3已成交
                break;
        }


    }


    private void loadAllDemandUIAdapter(){
        //我的需求列表
        mMvAllDemandListAdapter=new MvAllDemandListAdapter(getContext(),mGetMyNeedLists);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        //添加分隔线
        mRecyclerView.addItemDecoration(new ListViewDecoration());
        mRecyclerView.setAdapter(mMvAllDemandListAdapter);

        mRecyclerView.setEmptyView(ll_mv_demand_empty);

        mMvAllDemandListAdapter.setClickCallBack(new MvAllDemandListAdapter.ItemClickCallBack() {
            @Override
            public void onDelClick(View v, int position) {
                mClickItemData= mGetMyNeedLists.get(position);
                int itemState=mClickItemData.getStatus();
                if(itemState==3){//需求状态 1显示倒计时可修改 2无倒计时可修改  3显示过期可删除
                    //过期--调用删除
                    showLoading("正在删除...");
                    mPresenter.deleteNeed(mClickItemData.getNeedId());
                }else{
                    //可以修改
                    ModifyDemand();
                }
            }
        });

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                loadMyDemandListType=1;
                mPresenter.getMyNeedList("0","20");
            }

            @Override
            public void onLoadMore() {
                loadMyDemandListType=2;
                mPresenter.getMyNeedList(nextOffset,"20");
            }
        });

    }



    //加载我的mv已完成、待完成mv
    private void loadMyMVListDemandUIAdapter(){
        int mvType=5;
        if(type==1){
            mvType=5;
        }else{
            mvType=3;
        }

        mMvDemandMvListAdapter=new MvDemandMvListAdapter(getContext(),mvType,mNeedMvListData);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        //添加分隔线
        mRecyclerView.setEmptyView(ll_mv_demand_empty);

        mRecyclerView.setAdapter(mMvDemandMvListAdapter);
        mMvDemandMvListAdapter.setClickCallBack(new MvDemandMvListAdapter.ItemClickCallBack() {
            @Override
            public void onItemClick(View v, int position) {
                GetMyNeedMVListResponse.GetMyNeedMVList myNeedMVList=   mNeedMvListData.get(position);

               if(type==1) {
                   //待成交列表 点击跳转购买mv页面
                   Bundle bundle = new Bundle();
                   bundle.putSerializable("myNeedMVList", myNeedMVList);
                   goActivityForResult(BuyMvDemandActivity.class, bundle, 1400);
               }else if(type==2){
                   //已完成列表 点击跳转mv 详情页面
                   Bundle bundle = new Bundle();
                   bundle.putSerializable("userDemandMv",myNeedMVList);
                   goActivityForResult(MvVideoDetailsActivity.class, bundle, 100);
               }
            }
        });

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                loadMyDemandMVListType=1;
                if(type==1){
                    mPresenter.getMyNeedMVList("5","0",count);// mv状态   4待成交，3已成交
                }else if(type==2){
                    mPresenter.getMyNeedMVList("3","0",count);// mv状态   4待成交，3已成交
                }

            }

            @Override
            public void onLoadMore() {
                loadMyDemandMVListType=2;
                if(type==1){
                    mPresenter.getMyNeedMVList("5",offset,count);// mv状态   4待成交，3已成交
                }else if(type==2){
                    mPresenter.getMyNeedMVList("3",offset,count);// mv状态   4待成交，3已成交
                }

            }
        });

    }




    @Override
    protected void lazyLoad() {

    }



    MyDemandModifyDialog modifyDialog;
    private String editDemandDesc="";
    private void ModifyDemand(){
        modifyDialog=new MyDemandModifyDialog(getContext());

        modifyDialog.setTv_songName(mClickItemData.getTitle());

        modifyDialog.setSureListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editDemandDesc= modifyDialog.getEt_input_desc().getText().toString();

                if(TextUtils.isEmpty(editDemandDesc)){
                    onToast("内容不能为空！");
                    return;
                }
                showHintDialog();

            }
        });
        modifyDialog.setCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyDialog.dismiss();
            }
        });
        modifyDialog.show();
    }

    HintDialog hintDialog;
    private void showHintDialog(){
        hintDialog=new HintDialog(getContext());

        hintDialog.setSureListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Long hb= mPresenter.getUserData().gethB();
                if((mClickItemData.gethB()*0.2f)>hb){
                    //余额不足
                    onToast("余额不足!请充值");
                    modifyDialog.dismiss();
                    hintDialog.dismiss();
                    return;

                }

                mPresenter.editNeed(
                        new EditNeedRequest(
                                mClickItemData.getNeedId(),
                                mClickItemData.getSongName(),
                                mClickItemData.getSingerName(),
                                editDemandDesc
                        ));
                modifyDialog.dismiss();
                hintDialog.dismiss();
            }
        });
        hintDialog.setCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hintDialog.dismiss();
            }
        });
        hintDialog.show();

    }



    @Override
    public void onEditNeedResp(EditNeedResponse response) {
        hideLoading();
        if(response.getCode()==0){
            onToast("修改提交成功");
            mRecyclerView.refresh();
        }else{
            onToast("修改失败:"+response.getErrMsg());
        }
    }

    @Override
    public void onDeleteMVResp(DeleteMVResponse response) {

    }

    @Override
    public void onDeleteNeedResp(DeleteNeedResponse response) {
        hideLoading();
        if(response.getCode()==0){
            onToast("删除成功");
            mRecyclerView.refresh();
        }else{
            onToast("删除失败:"+response.getErrMsg());
        }
    }

    @Override
    public void onGetMyNeedListResp(GetMyNeedListResponse response) {
        //我的全部需求-已发送消息
        switch (loadMyDemandListType){
            case 1:
                mRecyclerView.refreshComplete();
                break;
            case 2:
                mRecyclerView.loadMoreComplete();
                break;
        }
        if(response!=null){
            if(response.getCode()==0){
                GetMyNeedListResponse.GetMyNeedListData  myNeedListData=  response.getGetMyNeedListData();

                List<GetMyNeedListResponse.GetMyNeedList>  myNeedLists= myNeedListData.getGetMyNeedLists();

                if(myNeedLists!=null&&myNeedLists.size()>0){
                    nextOffset= myNeedListData.getNextOffset();
                    if(loadMyDemandListType==1){
                        mGetMyNeedLists.clear();
                    }
                    mGetMyNeedLists.addAll(myNeedLists);
                    mMvAllDemandListAdapter.notifyDataSetChanged();
                }else{
                    if(loadMyDemandListType==1){
                        mGetMyNeedLists.clear();
                        mMvAllDemandListAdapter.notifyDataSetChanged();
                    }
                    if(loadMyDemandListType==2){
                        //没有更多了
                        mRecyclerView.setNoMore(true);
                    }

                }
            }
        }
    }



    @Override
    public void onGetMyNeedMVListResp(GetMyNeedMVListResponse response) {

        //返回待成交，已完成列表
        if(loadMyDemandMVListType==1){
            mRecyclerView.refreshComplete();
        }else if(loadMyDemandMVListType==2){
            mRecyclerView.loadMoreComplete();
        }

        if(response.getCode()==0){
            GetMyNeedMVListResponse.GetMyNeedMVListData myNeedMVListData= response.getGetMyNeedMVListData();
            List<GetMyNeedMVListResponse.GetMyNeedMVList> myNeedMVLists=myNeedMVListData.getGetMyNeedMVLists();

            if(myNeedMVLists!=null&&myNeedMVLists.size()>0){

                offset=myNeedMVListData.getNextOffset();
                if(loadMyDemandMVListType==1){
                    mNeedMvListData.clear();
                }
                mNeedMvListData.addAll(myNeedMVLists);
                mMvDemandMvListAdapter.notifyDataSetChanged();
            }else{
                if(loadMyDemandMVListType==1){
                    mNeedMvListData.clear();
                    mMvDemandMvListAdapter.notifyDataSetChanged();
                }

                if(loadMyDemandMVListType==2){
                    //没有更多了
                    mRecyclerView.setNoMore(true);
                }
            }

            if(mNeedMvListData.size()>0) {
                tv_post_demand.setVisibility(View.GONE);
            }

        }
    }


    @OnClick({R.id.tv_post_demand,R.id.tv_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_post_demand:
            case R.id.tv_sure:
                //发送需求
                goActivityForResult(PostDemandActivity.class,1000);
                break;
        }
    }


    @Override
    public void onDestroyView() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroyView();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AppLogger.e("onActivityResult   "+resultCode);
    }
}
