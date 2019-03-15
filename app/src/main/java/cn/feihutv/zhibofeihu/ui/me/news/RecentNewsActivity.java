package cn.feihutv.zhibofeihu.ui.me.news;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.DataManager;
import cn.feihutv.zhibofeihu.data.db.model.RecentItem;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadMsgListResponce;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.rxbus.RxBusSubscribe;
import cn.feihutv.zhibofeihu.rxbus.ThreadMode;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.me.setting.MsgReceSettingsActivity;
import cn.feihutv.zhibofeihu.ui.widget.dialog.adapter.RecentAdapter;
import cn.feihutv.zhibofeihu.ui.widget.swipelistview.BaseSwipeListViewListener;
import cn.feihutv.zhibofeihu.ui.widget.swipelistview.SwipeListView;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import cn.feihutv.zhibofeihu.utils.UiUtil;
import cn.feihutv.zhibofeihu.utils.glideutil.GlideApp;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/22 10:31
 *      desc   : 我的---私信
 *      version: 1.0
 * </pre>
 */

public class RecentNewsActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.front)
    RelativeLayout mRelativeLayout;

    @BindView(R.id.icon)
    ImageView icon;

    @BindView(R.id.fh_msg)
    TextView tvRecentMsg;

    @BindView(R.id.empty)
    TextView mEmpty;

    @BindView(R.id.recent_listview)
    SwipeListView mRecentListView;

    @BindView(R.id.iv_more)
    ImageView ivMore;

    @BindView(R.id.back_btn)
    ImageView backBtn;

    @BindView(R.id.framelayout)
    FrameLayout mFrameLayout;

    @BindView(R.id.view_dot)
    View viewDot;

    private PopupWindow mPopWindow;

    private List<RecentItem> mRecentDatas;
    private RecentAdapter mAdapter;
    private DataManager mDataManager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recent_news;
    }

    @Override
    protected void initWidget() {
        setUnBinder(ButterKnife.bind(this));

        mDataManager = FeihuZhiboApplication.getApplication().mDataManager;

        initRecentData();
        initView();

        if (SharePreferenceUtil.getSessionBoolean(FeihuZhiboApplication.getApplication(), "fhkf_news", false)) {
            viewDot.setVisibility(View.VISIBLE);
        } else {
            viewDot.setVisibility(View.GONE);
        }
        String fh_msg = SharePreferenceUtil.getSession(this, "FH_MSG" + SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_USERID"));
        if (TextUtils.isEmpty(fh_msg)) {
            tvRecentMsg.setText("欢迎来到飞虎直播，很高兴为您服务...");
        } else {
            tvRecentMsg.setText(fh_msg);
        }

        ivMore.setOnClickListener(this);
    }

    @Override
    protected void eventOnClick() {
        backBtn.setOnClickListener(this);

        mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewDot.setVisibility(View.GONE);
                SharePreferenceUtil.saveSeesionBoolean(FeihuZhiboApplication.getApplication(), "fhkf_news", false);
                Intent toChatIntent = new Intent(RecentNewsActivity.this, ChatActivity.class);
                toChatIntent.putExtra("userId", "");
                toChatIntent.putExtra("nickName", "飞虎客服");
                startActivity(toChatIntent);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        initRecentData();
    }

    private void initRecentData() {
        mRecentDatas = mDataManager.queryRecentMessageItem(SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_USERID"));
        mAdapter = new RecentAdapter(this, mRecentDatas, mRecentListView);
        mRecentListView.setAdapter(mAdapter);
    }

    private void initView() {
        mRecentListView.setEmptyView(mEmpty);
        if (mRecentListView != null) {
            UiUtil.initialize(this);
            int screenWidth = UiUtil.getScreenWidth();
            mRecentListView.setOffsetLeft(screenWidth - UiUtil.dip2px(85));
        }
        mRecentListView
                .setSwipeListViewListener(new BaseSwipeListViewListener() {
                    @Override
                    public void onOpened(int position, boolean toRight) {
                    }

                    @Override
                    public void onClosed(int position, boolean fromRight) {
                    }

                    @Override
                    public void onListChanged() {

                    }

                    @Override
                    public void onMove(int position, float x) {
                    }

                    @Override
                    public void onStartOpen(int position, int action, boolean right) {
                        // L.d("swipe", String.format(
                        // "onStartOpen %d - action %d", position, action));
                    }

                    @Override
                    public void onStartClose(int position, boolean right) {
                        // L.d("swipe",
                        // String.format("onStartClose %d", position));
                    }

                    @Override
                    public void onClickFrontView(int position) {
                        // L.d("swipe",
                        // String.format("onClickFrontView %d", position));
                        // T.showShort(mApplication, "item onClickFrontView ="
                        // + mAdapter.getItem(position));
                        RecentItem item = (RecentItem) mAdapter
                                .getItem(position);
//                    item.setNewNum(0);
                        item.setIsRead(true);
                        new CompositeDisposable().add(mDataManager
                                .updateRecentItem(item)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Object>() {
                                    @Override
                                    public void accept(@NonNull Object obj) throws Exception {

                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {

                                    }
                                })

                        );
                        Intent toChatIntent = new Intent(RecentNewsActivity.this, ChatActivity.class);
                        toChatIntent.putExtra("userId", item.getUserId());
                        toChatIntent.putExtra("nickName", item.getName());
                        toChatIntent.putExtra("headUrl", item.getHeadImg());
                        startActivity(toChatIntent);
                    }

                    @Override
                    public void onClickBackView(int position) {
                        mRecentListView.closeOpenedItems();// 关闭打开的项
                    }

                    @Override
                    public void onDismiss(int[] reverseSortedPositions) {
                        for (int position : reverseSortedPositions) {
                            mAdapter.remove(position);
                        }
                        // mAdapter.notifyDataSetChanged();
                    }
                });
    }

    //收到消息
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_NOTICE_SECRET_MESSAGE, threadMode = ThreadMode.MAIN)
    public void onReceiveLoadMsgListDataPush(LoadMsgListResponce.LoadMsgListData pushData) {
        update(pushData.getTime(), pushData.getContent(), pushData.getUserId(), pushData.getHeadUrl());
    }


    public void update(int time, String content, String senderId, String headUrl) {
        SharePreferenceUtil.saveSeesionInt(this, "news_count", 0);
        if (senderId.equals("")) {
            GlideApp.loadImgtransform(this, headUrl, R.drawable.face, icon);
            tvRecentMsg.setText(content);
            SharePreferenceUtil.saveSeesion(this, "FH_MSG" + SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_USERID"), content);
            SharePreferenceUtil.saveSeesionBoolean(FeihuZhiboApplication.getApplication(), "fhkf_news", true);
            viewDot.setVisibility(View.VISIBLE);
        } else {
            initRecentData();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_more:
                if (mPopWindow != null) {
                    if (mPopWindow.isShowing()) {
                        mPopWindow.dismiss();
                        mPopWindow = null;
                        showPopupWindow();
                    } else {
                        showPopupWindow();
                    }
                } else {
                    showPopupWindow();
                }
                break;
            case R.id.pop_day:
                MobclickAgent.onEvent(this, "10025");
                if (mAdapter != null) {
                    List<RecentItem> datas = mDataManager.queryRecentMessageItem(SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_USERID"));
                    for (RecentItem item : datas) {
                        if (!item.getIsRead()) {
                            item.setIsRead(true);
                            new CompositeDisposable().add(mDataManager
                                    .saveRecentItem(item)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<Object>() {
                                        @Override
                                        public void accept(@NonNull Object obj) throws Exception {

                                        }
                                    }, new Consumer<Throwable>() {
                                        @Override
                                        public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {

                                        }
                                    })

                            );
                        }
                    }
                    mAdapter.setData(datas);
                }
                mPopWindow.dismiss();
                break;
            case R.id.pop_month:
                startActivity(new Intent(RecentNewsActivity.this, MsgReceSettingsActivity.class));
                mPopWindow.dismiss();
                break;
            case R.id.back_btn:
                finish();
                break;
            default:
                break;
        }

    }

    private void showPopupWindow() {
        View contentView = LayoutInflater.from(RecentNewsActivity.this).inflate(R.layout.pop_msg_choose, null);
        mPopWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        TextView tv1 = (TextView) contentView.findViewById(R.id.pop_day);
        TextView tv2 = (TextView) contentView.findViewById(R.id.pop_month);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopWindow.setOutsideTouchable(true);
        mPopWindow.showAsDropDown(mFrameLayout, 0, 20, Gravity.RIGHT);
    }
}
