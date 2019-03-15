package cn.feihutv.zhibofeihu.ui.live;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.feihutv.zhibofeihu.R;

import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomContriResponce;
import cn.feihutv.zhibofeihu.di.component.ActivityComponent;
import cn.feihutv.zhibofeihu.ui.base.BaseFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.ui.live.adapter.RankFansAdapter;
import cn.feihutv.zhibofeihu.ui.widget.flashview.FlashDataParser;
import cn.feihutv.zhibofeihu.ui.widget.flashview.FlashView;
import cn.feihutv.zhibofeihu.ui.widget.swiperefreshlayout.SuperSwipeRefreshLayout;
import cn.feihutv.zhibofeihu.utils.ListViewDecoration;

/**
 * <pre>
 *     author : huang hao
 *     time   :
 *     desc   : 界面绘制
 *     version: 1.0
 * </pre>
 */
public class RankContriFragment extends BaseFragment implements RankContriMvpView,SuperSwipeRefreshLayout.OnPullRefreshListener,SuperSwipeRefreshLayout.OnPushLoadMoreListener {

    @Inject
    RankContriMvpPresenter<RankContriMvpView> mPresenter;
    @BindView(R.id.swipe_target)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh)
    SuperSwipeRefreshLayout swipeRefresh;
    // Header View
    private FlashView flashView;

    private FlashView flashViewFoot;
    private TextView footerTextView;


    private Context mContext;
    private RankFansAdapter adapter;
    private List<LoadRoomContriResponce.RoomContriData> datas=new ArrayList<>();
    private String rankType;


    public static RankContriFragment getInstance(String rankType) {
        RankContriFragment fragment = new RankContriFragment();
        fragment.rankType=rankType;
        return fragment;
    }
    private OnStrClickListener onItemClickListener;
    public void setOnItemClickListener(OnStrClickListener listener){
        onItemClickListener=listener;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.rank_host_activity;
    }

    @Override
    protected void initWidget(View view) {
        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            mPresenter.onAttach(this);
        }

        mContext=getContext();
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new ListViewDecoration());
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new RankFansAdapter(datas);
        swipeRefresh.setHeaderView(createHeaderView());// add headerView
        swipeRefresh.setFooterView(createFooterView());
        swipeRefresh.setTargetScrollWithLayout(true);
        swipeRefresh.setOnPullRefreshListener(this);
        swipeRefresh.setOnPushLoadMoreListener(this);
        mRecyclerView.setAdapter(adapter);
        if(rankType.equals(mContext.getString(R.string.day_con))){
            initdaydatas();
        }else if(rankType.equals(mContext.getString(R.string.month_con))){
            initdaydatas1();
        }else if(rankType.equals(mContext.getString(R.string.sum_con))){
            initdaydatas2();
        }
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClick(datas.get(position).getUserId());
                }
            }
        });
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onDestroyView() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroyView();
    }


    @Override
    public void onRefresh() {
        int type=1;
        if(rankType.equals(mContext.getString(R.string.day_con))){
            type=1;
        }else if(rankType.equals(mContext.getString(R.string.month_con))){
            type=2;
        }else if(rankType.equals(mContext.getString(R.string.sum_con))){
            type=3;
        }
        mPresenter.loadRoomContriList(type);
    }

    @Override
    public void onPullDistance(int distance) {

    }

    @Override
    public void onPullEnable(boolean enable) {
        if(enable==true){
            flashView.setVisibility(View.VISIBLE);
            flashView.reload("loading", "flashAnims");
            flashView.play("loading", FlashDataParser.FlashLoopTimeForever);
            flashView.setScale(0.16f,0.16f);
        }
    }

    @Override
    public void onLoadMore() {
        footerTextView.setText("加载中...");
        flashViewFoot.setVisibility(View.VISIBLE);
        flashViewFoot.reload("loading", "flashAnims");
        flashViewFoot.play("loading", FlashDataParser.FlashLoopTimeForever);
        flashViewFoot.setScale(0.16f,0.16f);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                flashViewFoot.setVisibility(View.GONE
                );
                flashViewFoot.stop();
                swipeRefresh.setLoadMore(false);
            }
        }, 2000);
    }

    @Override
    public void onPushDistance(int distance) {

    }

    @Override
    public void onPushEnable(boolean enable) {

    }

    private View createHeaderView() {
        View headerView = LayoutInflater.from(swipeRefresh.getContext())
                .inflate(R.layout.layout_head, null);
        flashView=(FlashView) headerView.findViewById(R.id.flashview);
        return headerView;
    }

    private View createFooterView() {
        View footerView = LayoutInflater.from(swipeRefresh.getContext())
                .inflate(R.layout.layout_footer, null);
        footerTextView=(TextView) footerView.findViewById(R.id.footer_text_view);
        flashViewFoot=(FlashView) footerView.findViewById(R.id.flashview);
        return footerView;
    }

    private void initdaydatas() {
        mPresenter.loadRoomContriList(1);
    }

    private void initdaydatas1() {
        mPresenter.loadRoomContriList(2);
    }

    private void initdaydatas2() {
        mPresenter.loadRoomContriList(3);
    }

    @Override
    public void notifyContriList(List<LoadRoomContriResponce.RoomContriData> roomContriDatas, int type) {

        if(datas.size()>0){
            datas.clear();
        }
        datas.addAll(roomContriDatas);
        adapter.setNewData(datas);
    }

    @Override
    public void freshStop() {
        swipeRefresh.setRefreshing(false);
        flashView.setVisibility(View.GONE);
        flashView.stop();
    }
}
