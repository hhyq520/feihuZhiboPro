package cn.feihutv.zhibofeihu.ui.mv.demand;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.EnablePostMVResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetAllNeedListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetNeedCollectListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.UnCollectNeedResponse;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.mv.demand.adapter.MyMvCollectAdapter;
import cn.feihutv.zhibofeihu.ui.widget.TCActivityTitle;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/
 *     desc   : 界面绘制
 *     version: 1.0
 * </pre>
 */
public class MyMvCollectActivity extends BaseActivity implements MyMvCollectMvpView {

    @Inject
    MyMvCollectMvpPresenter<MyMvCollectMvpView> mPresenter;


    @BindView(R.id.myRecyclerView)
    XRecyclerView mRecycleView;

    @BindView(R.id.ll_mv_empty)
    LinearLayout ll_mv_empty;


    @BindView(R.id.zb_title)
    TCActivityTitle zb_title;
    MyMvCollectAdapter mMyMvCollectAdapter;

    String pageSize="6";
    String nextOffset="0";
    int reType=1;//刷新状态
    GetNeedCollectListResponse.GetNeedCollectList mGetNeedCollectListItem;
    List<GetNeedCollectListResponse.GetNeedCollectList> mGetNeedCollectLists=new ArrayList<>();
    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_mv_collect;
    }


    @Override
    protected void initWidget() {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this));

        //Presenter调用初始化
        mPresenter.onAttach(MyMvCollectActivity.this);

        zb_title.setReturnListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mMyMvCollectAdapter=new MyMvCollectAdapter(MyMvCollectActivity.this,mGetNeedCollectLists);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MyMvCollectActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.setEmptyView(ll_mv_empty);
        //添加分隔线
        mRecycleView.setAdapter(mMyMvCollectAdapter);

        mRecycleView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                reType=1;
                mPresenter.getNeedCollectList("0",pageSize);
            }

            @Override
            public void onLoadMore() {
                reType=2;
                mPresenter.getNeedCollectList(nextOffset,pageSize);
            }
        });
        mRecycleView.refresh();
        mMyMvCollectAdapter.setClickCallBack(new MyMvCollectAdapter.ItemClickCallBack() {
            @Override
            public void onItemClick(View v, int position) {
                //取消收藏
                mGetNeedCollectListItem =mGetNeedCollectLists.get(position);

                if(mGetNeedCollectListItem.getT()>0){
                    //点击未过期的收藏需求跳转需求提交页面
                    mPresenter.enablePostMV(mGetNeedCollectListItem.getNeedId());
                }

            }

            @Override
            public void onCollectClick(View v, int position) {
                //取消收藏
                GetNeedCollectListResponse.GetNeedCollectList mGetNeedCollectList
                        =mGetNeedCollectLists.get(position);
                mPresenter.unCollectNeed(mGetNeedCollectList.getNeedId());
            }
        });

    }



    @Override
    public void onEnablePostMVResp(EnablePostMVResponse response) {
        if(response.getCode()==0){
            if(!response.isEnable()){
                onToast("您已经提交过了哦！");
                return;
            }
        }

        Bundle bundle=new Bundle();
        GetAllNeedListResponse.GetAllNeedList mGetAllNeedList=new GetAllNeedListResponse.GetAllNeedList();
        mGetAllNeedList.setCollected(mGetNeedCollectListItem.isCollected());
        mGetAllNeedList.setAvatar(mGetNeedCollectListItem.getAvatar());
        mGetAllNeedList.setForUid(mGetNeedCollectListItem.getForUid());
        mGetAllNeedList.sethB(mGetNeedCollectListItem.gethB());
        mGetAllNeedList.setNeedId(mGetNeedCollectListItem.getNeedId());
        mGetAllNeedList.setNickname(mGetNeedCollectListItem.getNickname());
        mGetAllNeedList.setSingerName(mGetNeedCollectListItem.getSingerName());
        mGetAllNeedList.setSongName(mGetNeedCollectListItem.getSongName());
        mGetAllNeedList.setRequire(mGetNeedCollectListItem.getRequire());
        mGetAllNeedList.setT(mGetNeedCollectListItem.getT());
        mGetAllNeedList.setTitle(mGetNeedCollectListItem.getTitle());
        mGetAllNeedList.setUid(mGetNeedCollectListItem.getUid());
        bundle.putSerializable("AllNeedListItemNeed",mGetAllNeedList);
        goActivity(PostMvVideoActivity.class,bundle);

    }

    @Override
    public void onUnCollectNeedResp(UnCollectNeedResponse response) {
        if(response.getCode()==0){
            onToast("取消收藏成功");
            mRecycleView.refresh();
        }else{
            onToast("取消收藏失败");
        }
    }


    @Override
    public void onGetNeedCollectListResp(GetNeedCollectListResponse response) {
        if(reType==1){
            mRecycleView.refreshComplete();
        }else{
            mRecycleView.loadMoreComplete();
        }
        if(response.getCode()==0){
            List<GetNeedCollectListResponse.GetNeedCollectList> mGetNeedCollect=
                    response.getGetNeedCollectListData().getGetNeedCollectLists();
            if(mGetNeedCollect!=null&&mGetNeedCollect.size()>0){
                nextOffset=response.getGetNeedCollectListData().getNextOffset();
                if(reType==1){
                    mGetNeedCollectLists.clear();
                }

                mGetNeedCollectLists.addAll(mGetNeedCollect);
                mMyMvCollectAdapter.notifyDataSetChanged();
            }else{
                if(reType==1){
                    mGetNeedCollectLists.clear();
                    mMyMvCollectAdapter.notifyDataSetChanged();
                }
                if(reType==2){
                    mRecycleView.setNoMore(true);
                }
            }
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 8000:
                mRecycleView.refresh();
                break;
        }


    }

    @Override
    protected void onDestroy() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroy();
    }


}
