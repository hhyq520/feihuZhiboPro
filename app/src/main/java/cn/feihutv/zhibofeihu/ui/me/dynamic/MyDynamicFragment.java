package cn.feihutv.zhibofeihu.ui.me.dynamic;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LogShareRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LogShareResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.me.DynamicItem;
import cn.feihutv.zhibofeihu.data.network.http.model.me.ShareFeedRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.ShareFeedResponse;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.rxbus.RxBusSubscribe;
import cn.feihutv.zhibofeihu.rxbus.ThreadMode;
import cn.feihutv.zhibofeihu.ui.me.BaseDynamicFragment;
import cn.feihutv.zhibofeihu.ui.me.adapter.DynamicPageAdapter;
import cn.feihutv.zhibofeihu.ui.me.adapter.MyDynamicPageAdapter;
import cn.feihutv.zhibofeihu.ui.mv.MvVideoDetailsActivity;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.ShareUtil;
import cn.feihutv.zhibofeihu.utils.TCConstants;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *     @author : liwen.chen
 *     time   : 2017/
 *     desc   : 我的动态
 *     version: 1.0
 * </pre>
 */
public class MyDynamicFragment extends BaseDynamicFragment implements MyDynamicMvpView, View.OnClickListener {

    @Inject
    MyDynamicMvpPresenter<MyDynamicMvpView> mPresenter;

    @BindView(R.id.rv_dynamic)
    XRecyclerView mRecyclerView;

    @BindView(R.id.ll_empty_view)
    LinearLayout llEmptyView;

    @BindView(R.id.tv_no_desc)
    TextView mTvNoDesc;

    private DynamicPageAdapter adapter;//全部动态
    private MyDynamicPageAdapter mMyDynamicPageAdapter;//我的动态
    private List<DynamicItem> datas = new ArrayList<>();
    private String userId;
    private boolean isLoadMyDynamic = true;//加载我的动态
    private int curSharePos = -1;
    private int mOffset;
    private String lastFeedId = "";
    boolean isPullRefresh = true;//刷新标记;true 为下拉刷新；

    private int feedType = 0; // 全部动态

    public interface OnfreshListener {
        void onfreshComplete();
    }

    private OnfreshListener mListener;

    public void setOnfreshListener(OnfreshListener onfreshListener) {
        mListener = onfreshListener;
    }


    public static MyDynamicFragment newInstance(String userId) {
        MyDynamicFragment fragment = new MyDynamicFragment();
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_dynamic;
    }


    @Override
    protected void initWidget(View view) {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this, view));

        //Presenter调用初始化
        mPresenter.onAttach(MyDynamicFragment.this);


        Bundle bundle = getArguments();
        userId = bundle.getString("userId");

        // 初始化
        initAllDynamicView();
    }

    @Override
    protected void lazyLoad() {

    }

    //加载动态方法 type 1：我的动态；其他全部动态
    public void loadDynamic(int type) {
        if (type == 1) {
            feedType = 1;
            //我的动态
            initMyDynamicView();
        } else {
            feedType = 0;
            //全部动态
            initAllDynamicView();
        }
    }

    /**
     * 初始化全部动态
     */
    private void initAllDynamicView() {
        mOffset = 0;
        isLoadMyDynamic = false;
        isPullRefresh = true;
        if (datas.size() > 0) {
            datas.clear();
        }
        mRecyclerView.setNoMore(false);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mTvNoDesc.setText("多关注几个主播就有动态啦~");
        mRecyclerView.setEmptyView(llEmptyView);
        adapter = new DynamicPageAdapter(getContext(), datas);


        adapter.setOnItemClickListener(new DynamicPageAdapter.OnItemClickListener() {
            @Override
            public void share(int position) {
                MobclickAgent.onEvent(getContext(), "10143");
                curSharePos = position;
                showShareDialog();
            }

            @Override
            public void goDetail(int position, int feedType) {
                if (feedType == 1) {
                    // 普通动态
                    Intent intent = new Intent(getActivity(), DynamicDetailActivity.class);
                    intent.putExtra("isother_com", false);
                    intent.putExtra("feedId", datas.get(position).getId());
                    startActivityForResult(intent, 101);
                } else if (feedType == 2) {
                    // MV
                    Intent intent = new Intent(getActivity(), MvVideoDetailsActivity.class);
                    intent.putExtra("mvId", datas.get(position).getJumpId());
                    intent.putExtra("mvImg", datas.get(position).getImgList().get(0));
                    startActivityForResult(intent, 101);
//                    onToast("跳转到MV详情", Gravity.CENTER, 0, 0);
                }
            }

        });

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                isPullRefresh = true;
                mPresenter.loadAllFeedList(0);
            }

            @Override
            public void onLoadMore() {
                isPullRefresh = false;

                mPresenter.loadAllFeedList(mOffset);
            }
        });

        mRecyclerView.setAdapter(adapter);

        mPresenter.loadAllFeedList(mOffset);

    }

    /**
     * 初始化我的动态
     */
    private void initMyDynamicView() {
        lastFeedId = "";
        isLoadMyDynamic = true;
        isPullRefresh = true;
        if (datas.size() > 0) {
            datas.clear();
        }
        mRecyclerView.setNoMore(false);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mMyDynamicPageAdapter = new MyDynamicPageAdapter(getContext(), datas);
        mRecyclerView.setAdapter(mMyDynamicPageAdapter);
        mTvNoDesc.setText("您的动态怎么空空如也~");
        mRecyclerView.setEmptyView(llEmptyView);
        mMyDynamicPageAdapter.setOnItemClickListener(new MyDynamicPageAdapter.OnItemClickListener() {
            @Override
            public void goDetail(int position, int feedType) {
                if (feedType == 1) {
                    // 普通动态
                    Intent intent = new Intent(getActivity(), DynamicDetailActivity.class);
                    intent.putExtra("isother_com", false);
                    intent.putExtra("feedId", datas.get(position).getId());
                    startActivityForResult(intent, 101);
                } else if (feedType == 2) {
                    // MV
                    Intent intent = new Intent(getActivity(), MvVideoDetailsActivity.class);
                    intent.putExtra("mvId", datas.get(position).getJumpId());
                    intent.putExtra("mvImg", datas.get(position).getImgList().get(0));
//                    onToast("跳转到MV详情", Gravity.CENTER, 0, 0);
                }
            }
        });

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                isPullRefresh = true;
                mPresenter.loadFeedList(userId, "");

            }

            @Override
            public void onLoadMore() {
                isPullRefresh = false;
                mPresenter.loadFeedList(userId, lastFeedId);
            }
        });



        mPresenter.loadFeedList(userId, lastFeedId);
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
        try {
            if (101 == requestCode) {
                if (data == null) {
                    return;
                }
                String roomId = data.getStringExtra(TCConstants.ROOM_ID);
                for (int i = 0; i < datas.size(); i++) {
                    DynamicItem info = datas.get(i);
                    if (info != null && info.getId().equals(roomId)) {
                        datas.remove(info);
                        adapter.setDatas(datas);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getDatas(List<DynamicItem> dynamicItems) {
        updateRefreshUI();

        if (dynamicItems != null && dynamicItems.size() > 0) {
            llEmptyView.setVisibility(View.GONE);
            if (isPullRefresh) {
                datas.clear();
            }
            datas.addAll(dynamicItems);

            if (isLoadMyDynamic) {
                lastFeedId = datas.get(datas.size() - 1).getId();
                mMyDynamicPageAdapter.setDatas(datas);
            } else {
                mOffset = datas.size();
                adapter.setDatas(datas);
            }
        } else {
            //如果返回的数据为空，则显示空白view
            if (datas.size() == 0) {
                llEmptyView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void updateRefreshUI() {
        if (isPullRefresh) {
            mRecyclerView.refreshComplete();
        } else {
            mRecyclerView.loadMoreComplete();
        }
        //回调刷新状态
        if (mListener != null) {
            mListener.onfreshComplete();
        }
    }

    private void showShareDialog() {
        final Dialog pickDialog3 = new Dialog(getContext(), R.style.color_dialog);
        pickDialog3.setContentView(R.layout.share_dialog);

        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog3.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = (int) (display.getWidth()); //设置宽度
        pickDialog3.getWindow().setAttributes(lp);

        ImageView tv_share_friends = (ImageView) pickDialog3.findViewById(R.id.btn_share_circle);
        ImageView tv_share_weibo = (ImageView) pickDialog3.findViewById(R.id.btn_share_wb);
        ImageView tv_share_wechat = (ImageView) pickDialog3.findViewById(R.id.btn_share_wx);
        ImageView tv_share_qzone = (ImageView) pickDialog3.findViewById(R.id.btn_share_qzone);
        ImageView tv_share_qq = (ImageView) pickDialog3.findViewById(R.id.btn_share_qq);
        tv_share_friends.setOnClickListener(this);
        tv_share_weibo.setOnClickListener(this);
        tv_share_wechat.setOnClickListener(this);
        tv_share_qzone.setOnClickListener(this);
        tv_share_qq.setOnClickListener(this);
        pickDialog3.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_share_circle:
                share(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.btn_share_wb:
                share(SHARE_MEDIA.SINA);
                break;
            case R.id.btn_share_wx:
                share(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.btn_share_qzone:
                share(SHARE_MEDIA.QZONE);
                break;
            case R.id.btn_share_qq:
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPerssion()) {
                        share(SHARE_MEDIA.QQ);
                    }
                } else {
                    share(SHARE_MEDIA.QQ);
                }
                break;
            default:
                break;
        }
    }

    private void share(SHARE_MEDIA plat) {
        String headUrl = datas.get(curSharePos).getHeadurl();
        String nickName = datas.get(curSharePos).getNickname();
        String content = datas.get(curSharePos).getContent();
        if (TextUtils.isEmpty(content)) {
            content = "快来主页看看吧";
        }
        if (datas.get(curSharePos).getPhotos() != null && datas.get(curSharePos).getPhotos().size() > 0) {
            ShareUtil.ShareWeb(getActivity(), content, nickName + "发布新动态了！",
                    datas.get(curSharePos).getPhotos().get(0).getUrl(), TCConstants.REGISTER_URL, plat, umShareListener);
        } else {
            ShareUtil.ShareWeb(getActivity(), content, nickName + "发布新动态了！",
                    headUrl, TCConstants.REGISTER_URL, plat, umShareListener);
        }

    }

    private boolean checkPerssion() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 需要弹出dialog让用户手动赋予权限
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1010);
            return false;
        } else {
            return true;
        }
    }

    UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            int to = 0;
            if (share_media.equals(SHARE_MEDIA.WEIXIN_CIRCLE)) {
                to = 1;
            } else if (share_media.equals(SHARE_MEDIA.SINA)) {
                to = 2;
            } else if (share_media.equals(SHARE_MEDIA.WEIXIN)) {
                to = 3;
            } else if (share_media.equals(SHARE_MEDIA.QZONE)) {
                to = 4;
            } else if (share_media.equals(SHARE_MEDIA.QQ)) {
                to = 5;
            }
            new CompositeDisposable().add(
                    FeihuZhiboApplication.getApplication().mDataManager
                            .doLogShareApiCall(new LogShareRequest(4, to))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())

                            .subscribe(new Consumer<LogShareResponce>() {
                                @Override
                                public void accept(@NonNull LogShareResponce logShareResponce) throws Exception {
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(@NonNull Throwable throwable) throws Exception {
                                }
                            })
            );

            FHUtils.showToast("分享成功");

            if (datas != null && datas.size() > 0) {
                datas.get(curSharePos).setForwarding(datas.get(curSharePos).getForwarding() + 1);
                adapter.setDatas(datas);

                new CompositeDisposable().add(
                        FeihuZhiboApplication.getApplication().mDataManager
                                .doShareFeedApiCall(new ShareFeedRequest(datas.get(curSharePos).getId()))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())

                                .subscribe(new Consumer<ShareFeedResponse>() {
                                    @Override
                                    public void accept(@NonNull ShareFeedResponse shareFeedResponse) throws Exception {

                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(@NonNull Throwable throwable) throws Exception {
                                    }
                                })
                );

            }

        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            FHUtils.showToast("分享失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {

        }
    };


    @Override
    public void getDatasError() {
        if (!isPullRefresh) {
            mRecyclerView.setNoMore(true);
        }

        updateRefreshUI();
    }

    @Override
    public void pullToRefresh() {

    }

    @Override
    public void refreshComplete() {

    }

    @Override
    public View getScrollableView() {
        return mRecyclerView;
    }

    // 删除动态成功
    @RxBusSubscribe(code = RxBusCode.RX_BUS_NOTICE_CODE_DELETE_DYNAMIC, threadMode = ThreadMode.MAIN)
    public void onReceiveDeleteDynamic() {
        if (feedType == 1) {
            //我的动态
            initMyDynamicView();
        } else {
            //全部动态
            initAllDynamicView();
        }
    }
    // 发布动态成功
    @RxBusSubscribe(code = RxBusCode.RX_BUS_NOTICE_CODE_POST_SUCC, threadMode = ThreadMode.MAIN)
    public void onReceivePostSuccDynamic() {
        if (feedType == 1) {
            //我的动态
            initMyDynamicView();
        } else {
            //全部动态
            initAllDynamicView();
        }
    }
}
