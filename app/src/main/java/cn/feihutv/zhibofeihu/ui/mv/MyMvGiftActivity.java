package cn.feihutv.zhibofeihu.ui.mv;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMVGiftLogResponse;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.mv.adapter.MyMvGiftAdapter;
import cn.feihutv.zhibofeihu.ui.widget.TCActivityTitle;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/
 *     desc   : 界面绘制
 *     version: 1.0
 * </pre>
 */
public class MyMvGiftActivity extends BaseActivity implements MyMvGiftMvpView {

    @Inject
    MyMvGiftMvpPresenter<MyMvGiftMvpView> mPresenter;


    @BindView(R.id.myRecyclerView)
    RecyclerView mRecycleView;


    @BindView(R.id.zb_title)
    TCActivityTitle zb_title;

    MyMvGiftAdapter mMyMvGiftAdapter;
    List<GetMVGiftLogResponse.GetMVGiftLog> mGetMVGiftLogs=new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_mv_gift;
    }


    @Override
    protected void initWidget() {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this));

        //Presenter调用初始化
        mPresenter.onAttach(MyMvGiftActivity.this);

        String mvId=getIntent().getStringExtra("mvId");

        zb_title.setMoreListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        zb_title.setReturnListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        mMyMvGiftAdapter=new MyMvGiftAdapter(MyMvGiftActivity.this,mGetMVGiftLogs);

        mRecycleView.setLayoutManager(new LinearLayoutManager(MyMvGiftActivity.this));
        //添加分隔线
//        mRecycleView.addItemDecoration(new ListViewDecoration());
        mRecycleView.setAdapter(mMyMvGiftAdapter);

        mPresenter.getMVGiftLog(mvId,"0","20");

    }

    @Override
    public void onGetMVGiftLogResp(GetMVGiftLogResponse response) {
        if(response.getCode()==0){
            List<GetMVGiftLogResponse.GetMVGiftLog> mvGiftLogList=response.getGetMVGiftLogData().getGetMVGiftLogs();
            mGetMVGiftLogs.addAll(mvGiftLogList);
            mMyMvGiftAdapter.notifyDataSetChanged();

        }
    }

    @Override
    protected void onDestroy() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroy();
    }


}
