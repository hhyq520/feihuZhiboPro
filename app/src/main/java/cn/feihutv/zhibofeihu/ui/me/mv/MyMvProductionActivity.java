package cn.feihutv.zhibofeihu.ui.me.mv;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMyMVListResponse;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.me.mv.adapter.MyMvProductionAdapter;
import cn.feihutv.zhibofeihu.ui.mv.MvVideoDetailsActivity;
import cn.feihutv.zhibofeihu.ui.mv.demand.PostMvVideoActivity;
import cn.feihutv.zhibofeihu.ui.widget.TCActivityTitle;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/
 *     desc   : 我的mv作品列表
 *     version: 1.0
 * </pre>
 */
public class MyMvProductionActivity extends BaseActivity implements MyMvProductionMvpView {

    @Inject
    MyMvProductionMvpPresenter<MyMvProductionMvpView> mPresenter;


    @BindView(R.id.myRecyclerView)
    XRecyclerView mRecyclerView;

    @BindView(R.id.zb_title)
    TCActivityTitle zb_title;
    boolean isPullRefresh = true;//刷新标记;true 为下拉刷新；

    @BindView(R.id.ll_empty_view)
    LinearLayout ll_empty_view;

    @BindView(R.id.iv_more)
    ImageView mView;

    String pageSize = "8";
    String offNext = "0";
    int  chooseItem=0;


    List<ProductionMenuInfo> menuStrArray=new ArrayList<>();

    MyMvProductionAdapter mMyMvProductionAdapter;
    public List<GetMyMVListResponse.GetMyMVList> mGetMyMVLists = new ArrayList<>();


    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_mv_production;
    }


    @Override
    protected void initWidget() {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this));

        //Presenter调用初始化
        mPresenter.onAttach(MyMvProductionActivity.this);


        zb_title.setReturnListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(MyMvProductionActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                isPullRefresh = true;

                mPresenter.getMyMVList(chooseItem+"", "0", pageSize);

            }

            @Override
            public void onLoadMore() {
                isPullRefresh = false;
                mPresenter.getMyMVList(chooseItem+"", offNext, pageSize);
            }
        });


        mMyMvProductionAdapter = new MyMvProductionAdapter(MyMvProductionActivity.this, mGetMyMVLists);
        mRecyclerView.setAdapter(mMyMvProductionAdapter);

        mRecyclerView.setEmptyView(ll_empty_view);
        mRecyclerView.refresh();

        mMyMvProductionAdapter.setClickCallBack(new MyMvProductionAdapter.ItemClickCallBack() {
            @Override
            public void onItemClick(View v, int position) {
                GetMyMVListResponse.GetMyMVList myMVList=    mGetMyMVLists.get(position);

                int itemState=myMVList.getStatus();
                if(itemState==1){
                    //已过期 提示
                    onToast("MV需求已过期，去发布新的作品吧~ ");
                    return;
                }

                if(myMVList.getStatus()==6){//待修改跳转修改mv 界面
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("modifMVList",myMVList);
                    goActivityForResult(PostMvVideoActivity.class,bundle,100);
                }else{
                    //从我的作品 点击进入 mv 播放详情页面
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("zbMyProductionMv",myMVList);
                    goActivity(MvVideoDetailsActivity.class,bundle);
                }
            }
        });

//MV状态 0全部  1已过期 2审核失败 3已完成 4待审核 5待成交 6待修改
        menuStrArray.add(new ProductionMenuInfo(0,"全部作品"));
        menuStrArray.add(new ProductionMenuInfo(6,"需修改"));
        menuStrArray.add(new ProductionMenuInfo(5,"待成交"));
        menuStrArray.add(new ProductionMenuInfo(4,"待审核"));
        menuStrArray.add(new ProductionMenuInfo(3,"已成交"));
        menuStrArray.add(new ProductionMenuInfo(2,"审核失败"));
        menuStrArray.add(new ProductionMenuInfo(1,"已过期"));
    }


    @Override
    public void GetMyMVListResp(GetMyMVListResponse response) {
        if (isPullRefresh) {
            mRecyclerView.refreshComplete();
        } else {
            mRecyclerView.loadMoreComplete();
        }
        if (response.getCode() == 0) {
            List<GetMyMVListResponse.GetMyMVList> myMVLists = response
                    .getGetMyMVListData().getGetMyMVLists();

            if (myMVLists != null && myMVLists.size() > 0) {
                offNext = response.getGetMyMVListData().getNextOffset();

                if (isPullRefresh) {
                    mGetMyMVLists.clear();
                }
                mGetMyMVLists.addAll(myMVLists);
                mMyMvProductionAdapter.notifyDataSetChanged();
            } else {
                if (isPullRefresh) {
                    mGetMyMVLists.clear();
                    mMyMvProductionAdapter.notifyDataSetChanged();
                }else{
                    mRecyclerView.setNoMore(true);
                }
            }
        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode){
            case 8000:
                mRecyclerView.refresh();
                break;
        }

    }

    @Override
    protected void onDestroy() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroy();
    }


    @OnClick(R.id.iv_more)
    public void onViewClicked() {
        showPopupWindow();
    }


    Map<Integer,RadioButton> mCheckBoxMap=new HashMap<>();

    PopupWindow mPopWindow;
    private void showPopupWindow() {

        LinearLayout contentView = (LinearLayout) LayoutInflater.from(MyMvProductionActivity.this)
                .inflate(R.layout.dialog_mv_my_layout, null);

        mPopWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setOutsideTouchable(true);
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());

        LinearLayout my_proLayout=contentView.findViewById(R.id.my_proLayout);



        for(ProductionMenuInfo proInfo:menuStrArray){
            View itemView = LayoutInflater.from(MyMvProductionActivity.this)
                    .inflate(R.layout.dialog_mv_my_item, null)  ;
            RadioButton checkBox=itemView.findViewById(R.id.itemText);
            checkBox.setText(proInfo.name);
            if(proInfo.id==chooseItem){
                checkBox.setChecked(true);
            }else{
                checkBox.setChecked(false);
            }
            mCheckBoxMap.put(proInfo.id,checkBox);
            checkBox.setTag(proInfo.id);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tag= (int) v.getTag();
                    chooseItem=tag;
                    for(Map.Entry<Integer,RadioButton> sc:mCheckBoxMap.entrySet()){
                        sc.getValue().setChecked(false);
                    }
                    if(mCheckBoxMap.get(tag)!=null){
                        mCheckBoxMap.get(tag).setChecked(true);
                    }
                    mRecyclerView.refresh();
                    mPopWindow.dismiss();
                }
            });
            my_proLayout.addView(itemView);
        }

        mPopWindow.showAsDropDown(mView, 0, 10);
    }


    public static class ProductionMenuInfo{
        public int id;
        public String name;

        public ProductionMenuInfo(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }


}
