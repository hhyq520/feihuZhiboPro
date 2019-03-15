package cn.feihutv.zhibofeihu.ui.live;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;
import cn.feihutv.zhibofeihu.R;

import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.LoadRoomGuestResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.LoadRoomMemberResponce;
import cn.feihutv.zhibofeihu.di.component.ActivityComponent;
import cn.feihutv.zhibofeihu.ui.base.BaseFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.ui.live.adapter.UserOnlineAdapter;
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
public class UserOnlineFragment extends BaseFragment implements UserOnlineFragmentMvpView ,OnStrClickListener, SuperSwipeRefreshLayout.OnPullRefreshListener, SuperSwipeRefreshLayout.OnPushLoadMoreListener {

    @Inject
    UserOnlineFragmentMvpPresenter<UserOnlineFragmentMvpView> mPresenter;
    @BindView(R.id.demo_recycler)
    RecyclerView demo_recycler;
    @BindView(R.id.swipe_refresh)
    SuperSwipeRefreshLayout swipeRefresh;
    @BindView(R.id.text_user)
    TextView textUser;
    Unbinder unbinder;
    private LinearLayoutManager linearLayoutManager;
    private int lastVisibleItem;
    private UserOnlineAdapter adapter;
    private List<LoadRoomMemberResponce.MemberData> luckListEntities = new ArrayList<>();
    private int rankType;
    // Header View
    private FlashView flashView;

    private FlashView flashViewFoot;
    private TextView footerTextView;
    private int offset = 0;
    private String type = "mgrs";
    @Override
    public void notifyList(LoadRoomMemberResponce.RoomMemberData roomMemberData) {
        offset = roomMemberData.getNextOffset();
        type =  roomMemberData.getNextType();
        if (luckListEntities.size() > 0) {
            luckListEntities.clear();
        }
        if (roomMemberData.getMemberDatas().size() > 0) {
            luckListEntities.addAll(roomMemberData.getMemberDatas());
            adapter.setDatas(luckListEntities);
        } else {
            adapter.notifyDataSetChanged();
        }
        if (roomMemberData.getMemberDatas().size() < 20) {
            mPresenter.loadRoomGuestCnt();
        }
    }

    @Override
    public void onFreshStop() {
        if (swipeRefresh != null) {
            swipeRefresh.setRefreshing(false);
            flashView.setVisibility(View.GONE);
            flashView.stop();
        }
    }

    @Override
    public void loadMoreStop() {
        if (swipeRefresh != null) {
            flashViewFoot.setVisibility(View.GONE
            );
            flashViewFoot.stop();
            swipeRefresh.setLoadMore(false);
        }
    }

    @Override
    public void notifyMoreList(LoadRoomMemberResponce.RoomMemberData roomMemberData) {
        offset = roomMemberData.getNextOffset();
        type = roomMemberData.getNextType();
        if (roomMemberData.getMemberDatas().size() > 0) {
            luckListEntities.addAll(roomMemberData.getMemberDatas());
            adapter.setDatas(luckListEntities);
        }

        if (roomMemberData.getMemberDatas().size() < 20) {
           mPresenter.loadRoomGuestCnt();
        }
    }

    @Override
    public void notifyGuestCnt(LoadRoomGuestResponce.RoomGuestData data) {
        if (textUser != null) {
            textUser.setVisibility(View.VISIBLE);
            textUser.setText("游客数量:" + data.getGuestCnt());
        }
    }

    public interface UserOnlineFragmentListener {
        void onitemClick(String userId);
    }

    private UserOnlineFragmentListener mListener;

    public void setUserOnlineFragmentListener(UserOnlineFragmentListener listener) {
        mListener = listener;
    }

    public static UserOnlineFragment getInstance() {
        UserOnlineFragment fragment = new UserOnlineFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_online_fragment;
    }


    @Override
    protected void initWidget(View view) {
        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            mPresenter.onAttach(this);
        }

        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        demo_recycler.setLayoutManager(linearLayoutManager);

        //添加分隔线
        demo_recycler.addItemDecoration(new ListViewDecoration());

        demo_recycler.setAdapter(adapter = new UserOnlineAdapter(getContext()));

        swipeRefresh.setHeaderView(createHeaderView());// add headerView
        swipeRefresh.setFooterView(createFooterView());
        swipeRefresh.setTargetScrollWithLayout(true);
        swipeRefresh.setOnPullRefreshListener(this);
        swipeRefresh.setOnPushLoadMoreListener(this);
        adapter.setOnItemClickListener(this);

        mPresenter.loadRoomMembers(0, 20, type);
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
    public void onItemClick(String userId) {
        if (mListener != null) {
            mListener.onitemClick(userId);
        }
    }

    @Override
    public void onRefresh() {
        mPresenter.loadRoomMembers(0, 20, "mgrs");
    }

    @Override
    public void onPullDistance(int distance) {

    }

    @Override
    public void onPullEnable(boolean enable) {
        if (enable == true) {
            flashView.setVisibility(View.VISIBLE);
            flashView.reload("loading", "flashAnims");
            flashView.play("loading", FlashDataParser.FlashLoopTimeForever);
            flashView.setScale(0.16f, 0.16f);
        }
    }

    @Override
    public void onLoadMore() {
        footerTextView.setText("加载中...");
        flashViewFoot.setVisibility(View.VISIBLE);
        flashViewFoot.reload("loading", "flashAnims");
        flashViewFoot.play("loading", FlashDataParser.FlashLoopTimeForever);
        flashViewFoot.setScale(0.16f, 0.16f);
        offset = luckListEntities.size();
        mPresenter.loadRoomMembersMore(offset, 20, type);
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
        flashView = (FlashView) headerView.findViewById(R.id.flashview);
        return headerView;
    }

    private View createFooterView() {
        View footerView = LayoutInflater.from(swipeRefresh.getContext())
                .inflate(R.layout.layout_footer, null);
        footerTextView = (TextView) footerView.findViewById(R.id.footer_text_view);
        flashViewFoot = (FlashView) footerView.findViewById(R.id.flashview);
        return footerView;
    }
}
