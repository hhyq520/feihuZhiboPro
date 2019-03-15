package cn.feihutv.zhibofeihu.ui.mv;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.internal.ProgressDrawable;
import com.scwang.smartrefresh.layout.internal.pathview.PathsView;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.BuildConfig;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.local.model.SysbagBean;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetAllMvListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GiftMVResponse;
import cn.feihutv.zhibofeihu.ui.base.BaseFragment;
import cn.feihutv.zhibofeihu.ui.live.OnItemClickListener;
import cn.feihutv.zhibofeihu.ui.me.recharge.RechargeActivity;
import cn.feihutv.zhibofeihu.ui.me.vip.MyVipActivity;
import cn.feihutv.zhibofeihu.ui.mv.adapter.MvVideoListAdapter;
import cn.feihutv.zhibofeihu.ui.widget.dialog.GiftNewDialogView;
import cn.feihutv.zhibofeihu.ui.widget.dialog.GiftNumDialog;
import cn.feihutv.zhibofeihu.utils.CustomDialogUtils;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.ShareUtil;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.feihutv.zhibofeihu.utils.weiget.dialog.RxDialogSureCancel;

import static cn.feihutv.zhibofeihu.FeihuZhiboApplication.getApplication;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/
 *     desc   : MV 视频列表界面
 *     version: 1.0
 * </pre>
 */
public class MvVideoListFragment extends BaseFragment implements MvVideoListMvpView,
        View.OnClickListener {

    @Inject
    MvVideoListMvpPresenter<MvVideoListMvpView> mPresenter;

    @BindView(R.id.rv_videoList)
    RecyclerView mRecyclerView;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.ll_load_fail)
    LinearLayout ll_load_fail;

    MvVideoListAdapter mMvVideoListAdapter;

    private List<GetAllMvListResponse.GetAllMvList> mGetAllMvLists = new ArrayList<>();
    private GetAllMvListResponse.GetAllMvList mAllMvList;

    private boolean isFirstLoad = true;

    private boolean isPrepared;//当前页面是否加载

    private String nextOffset = "";
    boolean isPullRefresh = true;//默认下拉刷新
    private Dialog mPickDialog1;  // 分享弹框
    private int mvId;

    public static MvVideoListFragment newInstance() {
        Bundle args = new Bundle();
        MvVideoListFragment fragment = new MvVideoListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mv_video_list;
    }


    @Override
    protected void initWidget(View view) {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this, view));
        isPrepared = true;
        //Presenter调用初始化
        mPresenter.onAttach(MvVideoListFragment.this);

        loadRefreshUI();
        mMvVideoListAdapter = new MvVideoListAdapter(getContext(), mGetAllMvLists);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mMvVideoListAdapter);

        //添加分隔线
//        mRecyclerView.setEmptyView(ll_load_fail);
//        mRecyclerView.setAdapter(mMvVideoListAdapter);
//        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
//            @Override
//            public void onRefresh() {
//                isPullRefresh = true;
//                //获取mv 列表
//                mPresenter.getAllMVList(nextOffset);
//            }
//
//            @Override
//            public void onLoadMore() {
//                isPullRefresh = false;
//                //获取mv 列表
//                mPresenter.getAllMVList(nextOffset);
//            }
//        });



        mMvVideoListAdapter.setClickCallBack(new MvVideoListAdapter.ItemClickCallBack() {
            @Override
            public void onItemClick(int pos) {
                GetAllMvListResponse.GetAllMvList allMvList = (GetAllMvListResponse.GetAllMvList)
                        mGetAllMvLists.get(pos);

                String videoId = allMvList.getVideoId();
                Bundle bundle = new Bundle();
                bundle.putSerializable("allMvList", allMvList);
                bundle.putString("url", mMvVideoListAdapter.mMvVideoUrlMap.get(videoId));
                goActivity(MvVideoDetailsActivity.class, bundle);
            }

            @Override
            public void onMv_downClick(int pos) {
                if (!TCUtils.checkGuest(getActivity())) {
                    if (mPresenter.getUserData().getVip() > 0) {
                        if (!mPresenter.getUserData().isVipExpired()) {
                            // vip正常
                            onToast("弹出MV内容大小");
                        } else {
                            // 已过期
                            showNotVip();
                        }
                    } else {
                        // 不是vip
                        showNotVip();
                    }
                }
            }

            @Override
            public void onMv_giftClick(int pos) {
                if (!TCUtils.checkGuest(getActivity())) {
                    mPresenter.requestBagData(mGetAllMvLists.get(pos).getMVId() + "");
                }
            }

            @Override
            public void onMv_shareClick(int pos) {
                mvId = mGetAllMvLists.get(pos).getMVId();
                mAllMvList = mGetAllMvLists.get(pos);
                showShareDialog();
            }
        });


    }



    private void loadRefreshUI(){

        refreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        refreshLayout.setHeaderHeight(60);

        refreshLayout.setEnableAutoLoadmore(true);//开启自动加载功能（非必须）
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isPullRefresh = true;
                        mPresenter.getAllMVList(nextOffset);


                    }
                }, 2000);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        isPullRefresh = false;
                        mPresenter.getAllMVList(nextOffset);
                        refreshlayout.finishLoadmore();

//                        mAdapter.loadmore(initData());
//                        if (mAdapter.getItemCount() > 60) {
//                            Toast.makeText(getApplication(), "数据全部加载完毕", Toast.LENGTH_SHORT).show();
//                            refreshlayout.finishLoadmoreWithNoMoreData();//将不会再次触发加载更多事件
//                        } else {
//                            refreshlayout.finishLoadmore();
//                        }
                    }
                }, 2000);
            }
        });
    }



    /**
     * 分享
     */
    private void showShareDialog() {
        mPickDialog1 = new Dialog(getContext(), R.style.color_dialog);
        mPickDialog1.setContentView(R.layout.dialog_share_mv);

        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = mPickDialog1.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = (int) (display.getWidth()); //设置宽度
        mPickDialog1.getWindow().setAttributes(lp);

        ImageView ivCircle = (ImageView) mPickDialog1.findViewById(R.id.iv_share_circle);
        ImageView ivWx = (ImageView) mPickDialog1.findViewById(R.id.iv_share_wx);
        ImageView ivWb = (ImageView) mPickDialog1.findViewById(R.id.iv_share_wb);
        ImageView ivQq = (ImageView) mPickDialog1.findViewById(R.id.iv_share_qq);
        ImageView ivQzone = (ImageView) mPickDialog1.findViewById(R.id.iv_share_qzone);
        ImageView ivCopy = (ImageView) mPickDialog1.findViewById(R.id.iv_copy);
        ImageView ivDwonLoad = (ImageView) mPickDialog1.findViewById(R.id.iv_dowmload);


        ivCircle.setOnClickListener(this);
        ivWx.setOnClickListener(this);
        ivWb.setOnClickListener(this);
        ivQq.setOnClickListener(this);
        ivQzone.setOnClickListener(this);
        ivCopy.setOnClickListener(this);
        ivDwonLoad.setOnClickListener(this);

        mPickDialog1.show();
    }

    @Override
    public void onGetAllMVListResp(GetAllMvListResponse response) {
        if (isPullRefresh) {
//            mRecyclerView.refreshComplete();
            refreshLayout.finishRefresh();
        } else {
//            mRecyclerView.loadMoreComplete();
            refreshLayout.finishLoadmore();
        }
        if (response != null) {
            if (response.getCode() == 0) {
                GetAllMvListResponse.GetAllMvListData getAllMvListData = response.getGetAllMvListData();
                List<GetAllMvListResponse.GetAllMvList> mvLists = getAllMvListData.getGetAllMvLists();
                List<GetAllMvListResponse.GetAllMvList> newMvLists = new ArrayList<>();
                if (mvLists != null && mvLists.size() > 0) {
                    nextOffset = getAllMvListData.getNextOffset();
                    if (isPullRefresh) {
                        newMvLists.addAll(mvLists);
                        newMvLists.addAll(mGetAllMvLists);
                        mMvVideoListAdapter.setMvData(newMvLists);
                        mGetAllMvLists = mvLists;
                    } else {
                        mGetAllMvLists.addAll(mvLists);
                        mMvVideoListAdapter.notifyDataSetChanged();
                    }


                } else {
                    if (!isPullRefresh) {
//                        mRecyclerView.setNoMore(true);
                    }
                }

            }
        }
    }

    GiftNewDialogView giftDialogView;

    @Override
    public void showGiftDialog(final List<SysbagBean> sysbagBeanList) {
        if (FeihuZhiboApplication.getApplication().mDataManager.getLiveGiftList().size() == 0) {
            return;
        }
        giftDialogView = new GiftNewDialogView(sysbagBeanList, getContext(), true, true);
        Window dlgwin = giftDialogView.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        dlgwin.setGravity(Gravity.BOTTOM);
        dlgwin.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dlgwin.setAttributes(lp);
        giftDialogView.show();
        giftDialogView.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
        giftDialogView.setGiftDialogListener(new GiftNewDialogView.GiftDialogListener() {
            @Override
            public void sendGift(int id, int count, boolean isBag) {

                if (mPresenter.isGuestUser()) {
                    if (BuildConfig.isForceLoad.equals("1")) {
                        CustomDialogUtils.showQZLoginDialog(getActivity(), false);
                    } else {
                        CustomDialogUtils.showLoginDialog(getActivity(), false);
                    }
                } else {
                    showLoading("正在处理中...");
                    if (isBag) {
                        mPresenter.dealBagSendGift(sysbagBeanList, id, count);
                    } else {
                        mPresenter.dealSendGift(sysbagBeanList, id, count);
                    }
                }

                giftDialogView.dismiss();
            }

            @Override
            public void chargr() {
                if (mPresenter.isGuestUser()) {
                    if (BuildConfig.isForceLoad.equals("1")) {
                        CustomDialogUtils.showQZLoginDialog(getActivity(), false);
                    } else {
                        CustomDialogUtils.showLoginDialog(getActivity(), false);
                    }
                    return;
                }
                Intent intent = new Intent(getContext(), RechargeActivity.class);
                intent.putExtra("fromWhere", "直播间礼物页面");
                startActivity(intent);
                giftDialogView.dismiss();
            }

            @Override
            public void showNumDialog() {
                showGiftNumDialog();
            }
        });
    }

    @Override
    public void onGiftMVResp(GiftMVResponse response) {
        hideLoading();
        if (response.getCode() == 0) {
            onToast("赠送成功");
        } else {
            onToast("赠送失败");
        }
    }

    private GiftNumDialog giftNumDialog;

    private void showGiftNumDialog() {
        giftNumDialog = new GiftNumDialog(getContext(), R.style.InputDialog);
        Window dlgwin = giftNumDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        dlgwin.setGravity(Gravity.BOTTOM);
        dlgwin.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dlgwin.setAttributes(lp);
        giftNumDialog.show();
        giftNumDialog.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (giftDialogView != null) {
                    giftDialogView.setEditText(position);
                }
            }
        });
    }


    @Override
    protected void lazyLoad() {

        if (isPrepared && isVisible && mGetAllMvLists.size() == 0) {
            isPrepared = false;
            //获取mv 列表
            mPresenter.getAllMVList("");
        }

    }


    @Override
    public void onDestroyView() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroyView();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_share_circle:
                MobclickAgent.onEvent(getContext(), "10137");
                if (FHUtils.isAppInstalled(getContext(), "com.tencent.mm")) {
                    share(SHARE_MEDIA.WEIXIN_CIRCLE);
                } else {
                    onToast("未安装微信", Gravity.CENTER, 0, 0);
                }
                break;
            case R.id.iv_share_wx:
                MobclickAgent.onEvent(getContext(), "10136");
                if (FHUtils.isAppInstalled(getContext(), "com.tencent.mm")) {
                    share(SHARE_MEDIA.WEIXIN);
                } else {
                    onToast("未安装微信", Gravity.CENTER, 0, 0);
                }
                break;
            case R.id.iv_share_wb:
                MobclickAgent.onEvent(getContext(), "10140");
                share(SHARE_MEDIA.SINA);
                break;
            case R.id.iv_share_qq:
                MobclickAgent.onEvent(getContext(), "10138");
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPerssion(1010)) {
                        share(SHARE_MEDIA.QQ);
                    }
                } else {
                    share(SHARE_MEDIA.QQ);
                }
                break;
            case R.id.iv_share_qzone:
                MobclickAgent.onEvent(getContext(), "10139");
                share(SHARE_MEDIA.QZONE);
                break;
            case R.id.iv_copy:
                onToast("复制链接");
                break;
            case R.id.iv_dowmload:
                if (mPresenter.getUserData().getVip() > 0) {
                    if (!mPresenter.getUserData().isVipExpired()) {
                        // vip正常
                        onToast("弹出MV内容大小");
                    } else {
                        // 已过期
                        showNotVip();
                    }
                } else {
                    // 不是vip
                    showNotVip();
                }
                break;
            default:
                break;
        }
    }

    private void share(SHARE_MEDIA plat) {
        String imgUrl = mAllMvList.getCover();
        String nickName = mAllMvList.getOwnerNickname();
        String mvId = String.valueOf(mAllMvList.getMVId());
        String mTitle = mAllMvList.getTitle();
        String url = getApplication().mDataManager.getSysConfig().getMvShareUrl() + "?mvId=" + mvId;
        url.replace("https", "http");
        ShareUtil.ShareWeb(getActivity(), nickName, mTitle,
                imgUrl, url, plat, umShareListener);
    }

    UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            showLoading();
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
            onToast("分享成功", Gravity.CENTER, 0, 0);
            hideLoading();
            mPresenter.shareMv(mvId + "", to);
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            FHUtils.showToast("分享失败");
            hideLoading();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            hideLoading();
        }
    };

    private boolean checkPerssion(int code) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 需要弹出dialog让用户手动赋予权限
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, code);
            return false;
        } else {
            return true;
        }
    }


    private void showNotVip() {
        final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(getContext(), R.style.color_dialog);
        rxDialogSureCancel.setCancel("取消");
        rxDialogSureCancel.setSure("开通会员");
        rxDialogSureCancel.getTvContent().setGravity(Gravity.CENTER);
        rxDialogSureCancel.setContent("下载失败！\n赶紧成为VIP，下载更多精彩MV");

        rxDialogSureCancel.setSureListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MyVipActivity.class);
                intent.putExtra("isFromRoom", true);
                startActivity(intent);
                rxDialogSureCancel.dismiss();
            }
        });

        rxDialogSureCancel.setCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rxDialogSureCancel.dismiss();
            }
        });

        rxDialogSureCancel.show();
    }



    public static class ClassicsHeader extends LinearLayout implements RefreshHeader {

        private TextView mHeaderText;//标题文本
        private PathsView mArrowView;//下拉箭头
        private ImageView mProgressView;//刷新动画视图
        private ProgressDrawable mProgressDrawable;//刷新动画

        public ClassicsHeader(Context context) {
            super(context);
            initView(context);
        }
        public ClassicsHeader(Context context, AttributeSet attrs) {
            super(context, attrs);
            this.initView(context);
        }
        public ClassicsHeader(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            this.initView(context);
        }
        private void initView(Context context) {
            setGravity(Gravity.CENTER);
            mHeaderText = new TextView(context);
            mProgressDrawable = new ProgressDrawable();
            mArrowView = new PathsView(context);
            mProgressView = new ImageView(context);
            mProgressView.setImageDrawable(mProgressDrawable);
            mArrowView.parserPaths("M20,12l-1.41,-1.41L13,16.17V4h-2v12.17l-5.58,-5.59L4,12l8,8 8,-8z");
            addView(mProgressView, DensityUtil.dp2px(20), DensityUtil.dp2px(20));
            addView(mArrowView, DensityUtil.dp2px(20), DensityUtil.dp2px(20));
            addView(new View(context), DensityUtil.dp2px(20), DensityUtil.dp2px(20));
            addView(mHeaderText, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            setMinimumHeight(DensityUtil.dp2px(60));
        }
        @NonNull
        public View getView() {
            return this;//真实的视图就是自己，不能返回null
        }
        @Override
        public SpinnerStyle getSpinnerStyle() {
            return SpinnerStyle.Translate;//指定为平移，不能null
        }
        @Override
        public void onStartAnimator(RefreshLayout layout, int headHeight, int extendHeight) {
            mProgressDrawable.start();//开始动画
        }
        @Override
        public int onFinish(RefreshLayout layout, boolean success) {
            mProgressDrawable.stop();//停止动画
            if (success){
                mHeaderText.setText("刷新完成");
            } else {
                mHeaderText.setText("刷新失败");
            }
            return 500;//延迟500毫秒之后再弹回
        }
        @Override
        public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
            switch (newState) {
                case None:
                case PullDownToRefresh:
                    mHeaderText.setText("下拉开始刷新");
                    mArrowView.setVisibility(VISIBLE);//显示下拉箭头
                    mProgressView.setVisibility(GONE);//隐藏动画
                    mArrowView.animate().rotation(0);//还原箭头方向
                    break;
                case Refreshing:
                    mHeaderText.setText("正在刷新");
                    mProgressView.setVisibility(VISIBLE);//显示加载动画
                    mArrowView.setVisibility(GONE);//隐藏箭头
                    break;
                case ReleaseToRefresh:
                    mHeaderText.setText("释放立即刷新");
                    mArrowView.animate().rotation(180);//显示箭头改为朝上
                    break;
            }
        }
        @Override
        public boolean isSupportHorizontalDrag() {
            return false;
        }
        @Override
        public void onInitialized(RefreshKernel kernel, int height, int extendHeight) {
        }
        @Override
        public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {
        }
        @Override
        public void onPullingDown(float percent, int offset, int headHeight, int extendHeight) {
        }
        @Override
        public void onReleasing(float percent, int offset, int headHeight, int extendHeight) {
        }

        @Override
        public void onRefreshReleased(RefreshLayout layout, int headerHeight, int extendHeight) {
        }
        @Override
        public void setPrimaryColors(@ColorInt int ... colors){
        }
    }

}
