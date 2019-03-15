package cn.feihutv.zhibofeihu.ui.me;

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
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LogShareRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LogShareResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.me.DynamicItem;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LoadFeedListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LoadFeedListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.ShareFeedRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.ShareFeedResponse;
import cn.feihutv.zhibofeihu.ui.me.adapter.DynamicPageAdapter;
import cn.feihutv.zhibofeihu.ui.me.dynamic.DynamicDetailActivity;
import cn.feihutv.zhibofeihu.ui.mv.MvVideoDetailsActivity;
import cn.feihutv.zhibofeihu.utils.AppLogger;
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
 *      @author : liwen.chen
 *      time   : 2017/11/29 16:49
 *      desc   : TA的动态
 *      version: 1.0
 * </pre>
 */

public class OtherDynamicFragment extends BaseDynamicFragment implements View.OnClickListener {

    @BindView(R.id.rv_dynamic)
    XRecyclerView mRecyclerView;

    @BindView(R.id.ll_empty_view)
    LinearLayout llEmptyView;

    @BindView(R.id.tv_no_desc)
    TextView mTvDesc;

    boolean isPullRefresh = true;//刷新标记;true 为下拉刷新；

    private String userId;
    private String lastFeedId = "";
    private int curSharePos = -1;

    private DynamicPageAdapter mMyDynamicPageAdapter;//Ta的动态
    private List<DynamicItem> datas = new ArrayList<>();

    public interface OnfreshListener {
        void onfreshComplete();
    }

    private OnfreshListener mListener;

    public void setOnfreshListener(OnfreshListener onfreshListener) {
        mListener = onfreshListener;
    }

    public static OtherDynamicFragment newInstance(String userId) {
        OtherDynamicFragment fragment = new OtherDynamicFragment();
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_other_dynamic;
    }

    @Override
    protected void initWidget(View view) {
        setUnBinder(ButterKnife.bind(this, view));

        Bundle arguments = getArguments();
        userId = arguments.getString("userId");

        initMyDynamicView();

    }

    /**
     * 初始化我的动态
     */
    private void initMyDynamicView() {
        lastFeedId = "";
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mMyDynamicPageAdapter = new DynamicPageAdapter(getContext(), datas);
        mTvDesc.setText("这个人有点懒，还没发过动态呢~");
        mRecyclerView.setEmptyView(llEmptyView);
        mRecyclerView.setAdapter(mMyDynamicPageAdapter);
        mMyDynamicPageAdapter.setOnItemClickListener(new DynamicPageAdapter.OnItemClickListener() {
            @Override
            public void share(int position) {
                curSharePos = position;
                showShareDialog();
            }

            @Override
            public void goDetail(int position, int feedType) {
                if (feedType == 1) {
                    // 普通动态
                    Intent intent = new Intent(getActivity(), DynamicDetailActivity.class);
                    intent.putExtra("isother_com", true);
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

                loadDatas(userId, "");

                mRecyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                isPullRefresh = false;

                loadDatas(userId, lastFeedId);
            }
        });

        loadDatas(userId, lastFeedId);
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

    private void loadDatas(String userId, String last) {
        showLoading();
        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .doLoadFeedListApiCall(new LoadFeedListRequest(userId, last, String.valueOf(6)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoadFeedListResponse>() {
                    @Override
                    public void accept(@NonNull LoadFeedListResponse loadFeedListResponse) throws Exception {

                        if (isPullRefresh) {
                            mRecyclerView.refreshComplete();
                        } else {
                            mRecyclerView.loadMoreComplete();
                        }

                        if (loadFeedListResponse.getCode() == 0) {
                            List<DynamicItem> dynamicItems = loadFeedListResponse.getDynamicItemList();

                            if (dynamicItems.size() > 0) {
                                lastFeedId = dynamicItems.get(dynamicItems.size() - 1).getId();
                                if (isPullRefresh) {
                                    datas.clear();
                                }
                                datas.addAll(dynamicItems);
                                mMyDynamicPageAdapter.notifyDataSetChanged();
                            } else {
                                if (isPullRefresh) {
                                    datas.clear();
                                    mMyDynamicPageAdapter.notifyDataSetChanged();
                                } else {
                                    mRecyclerView.setNoMore(true);
                                }
                            }
                        } else {
                            AppLogger.i(loadFeedListResponse.getCode() + " " + loadFeedListResponse.getErrMsg());
                        }
                        if (mListener != null) {
                            mListener.onfreshComplete();
                        }
                        hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        hideLoading();
                        if (mListener != null) {
                            mListener.onfreshComplete();
                        }
                    }
                }));

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

    private boolean checkPerssion() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 需要弹出dialog让用户手动赋予权限
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1010);
            return false;
        } else {
            return true;
        }
    }

    private void share(SHARE_MEDIA plat) {
        String headUrl = datas.get(curSharePos).getHeadurl();
        String nickName = datas.get(curSharePos).getNickname();
        String content = datas.get(curSharePos).getContent();
        if (datas.get(curSharePos).getPhotos() != null && datas.get(curSharePos).getPhotos().size() > 0) {
            ShareUtil.ShareWeb(getActivity(), content, nickName + "发布新动态了！",
                    datas.get(curSharePos).getPhotos().get(0).getUrl(), TCConstants.REGISTER_URL, plat, umShareListener);
        } else {
            ShareUtil.ShareWeb(getActivity(), content, nickName + "发布新动态了！",
                    headUrl, TCConstants.REGISTER_URL, plat, umShareListener);
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
                mMyDynamicPageAdapter.setDatas(datas);

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
    protected void lazyLoad() {

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
}
