package cn.feihutv.zhibofeihu.ui.mv;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.BuildConfig;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.db.model.SysConfigBean;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.CollectNeedResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.EnablePostMVResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetAllNeedListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.UnCollectNeedResponse;
import cn.feihutv.zhibofeihu.data.prefs.AppPreferencesHelper;
import cn.feihutv.zhibofeihu.rxbus.RxBusSubscribe;
import cn.feihutv.zhibofeihu.rxbus.ThreadMode;
import cn.feihutv.zhibofeihu.ui.base.BaseFragment;
import cn.feihutv.zhibofeihu.ui.live.renzheng.IdentityInfoActivity;
import cn.feihutv.zhibofeihu.ui.live.renzheng.UploadFailureActivity;
import cn.feihutv.zhibofeihu.ui.live.renzheng.UploadSuccessActivity;
import cn.feihutv.zhibofeihu.ui.me.personalInfo.BlindPhoneActivity;
import cn.feihutv.zhibofeihu.ui.mv.adapter.DemandSquareAdapter;
import cn.feihutv.zhibofeihu.ui.mv.demand.PostDemandActivity;
import cn.feihutv.zhibofeihu.ui.mv.demand.PostMvVideoActivity;
import cn.feihutv.zhibofeihu.ui.mv.demand.ShowDemandDialog;
import cn.feihutv.zhibofeihu.ui.user.UserAgreement;
import cn.feihutv.zhibofeihu.ui.widget.dialog.MvMsgDialog;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.CommonUtils;
import cn.feihutv.zhibofeihu.utils.CustomDialogUtils;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.feihutv.zhibofeihu.utils.weiget.dialog.RxDialogSureCancel;

import static cn.feihutv.zhibofeihu.rxbus.RxBusCode.RX_BUS_CLICK_CODE_MV_POST_DEMAND;
import static cn.feihutv.zhibofeihu.rxbus.RxBusCode.RX_BUS_CLICK_CODE_MV_POST_MV;


/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/
 *     desc   : 需求广场
 *     version: 1.0
 * </pre>
 */
public class DemandSquareFragment extends BaseFragment implements DemandSquareMvpView,
        View.OnClickListener {

    @Inject
    DemandSquareMvpPresenter<DemandSquareMvpView> mPresenter;
    @BindView(R.id.rv_videoList)
    XRecyclerView mRecyclerView;

    @BindView(R.id.iv_mv_sq_add)
    ImageView iv_mv_sq_add;
    @BindView(R.id.iv_mv_sq_desc)
    ImageView iv_mv_sq_desc;
    @BindView(R.id.iv_mv_sq_post)
    ImageView iv_mv_sq_post;
    @BindView(R.id.ll_load_fail)
    LinearLayout ll_load_fail;
    DemandSquareAdapter mDemandSquareAdapter;

    List<GetAllNeedListResponse.GetAllNeedList> mGetAllMvLists = new ArrayList<>();

    private boolean isFirstLoad = true;

    private boolean isPrepared;//当前页面是否加载

    private int nextOffset = 0;
    private int nextForMe = 1;
    boolean isPullRefresh = true;//下拉刷新
    private int pageSize = 10;

    GetAllNeedListResponse.GetAllNeedList mItemGetAllNeedList;


    public static DemandSquareFragment newInstance() {
        Bundle args = new Bundle();
        DemandSquareFragment fragment = new DemandSquareFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_demand_square;
    }


    @Override
    protected void initWidget(View view) {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this, view));

        //Presenter调用初始化
        mPresenter.onAttach(DemandSquareFragment.this);

        isPrepared = true;
        mDemandSquareAdapter = new DemandSquareAdapter(getContext(), mGetAllMvLists);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mRecyclerView.setEmptyView(ll_load_fail);
        mRecyclerView.setAdapter(mDemandSquareAdapter);

        iv_mv_sq_add.setOnClickListener(this);
        iv_mv_sq_desc.setOnClickListener(this);
        iv_mv_sq_post.setOnClickListener(this);
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isShow) {
                    showBtnMenu();
                }
                return false;
            }
        });
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                isPullRefresh = true;
                mPresenter.getAllNeedList("0", "1", "0", pageSize + "");
            }

            @Override
            public void onLoadMore() {
                isPullRefresh = false;
                mPresenter.getAllNeedList("0", nextForMe + "", nextOffset + "", pageSize + "");

            }
        });
        mDemandSquareAdapter.setClickCallBack(new DemandSquareAdapter.ItemClickCallBack() {
            @Override
            public void onItemClick(View v, int position) {
                if (isShow) {
                    showBtnMenu();
                    return;
                }
                if (!TCUtils.checkGuest(getActivity())) {

                    int status = mPresenter.getUserData().getCertifiStatus();
                    if (1 != status) {
                        //不是主播
                        guestToast();
                        return;
                    }

                    //弹窗
                    mItemGetAllNeedList = (GetAllNeedListResponse.GetAllNeedList)
                            mGetAllMvLists.get(position);
                    String forUid = mItemGetAllNeedList.getForUid();
                    String uid = mItemGetAllNeedList.getUid();
                    if (uid.equals(mPresenter.getUserData().getUserId())) {
                        new MvMsgDialog().openShowMVMsgDialog(getContext(), "自己发的需求不能提交！");
                        return;
                    }
                    if (TextUtils.isEmpty(forUid)) {
                        forUid = "0";
                    }
                    if ("0".equals(forUid)) {
                        //公共需求
                        openShowDemandDialog();
                    } else {
                        //指定需求
                        if (forUid.equals(mPresenter.getUserData().getUserId())) {
                            //指定给当前用户的需求
                            openShowDemandDialog();
                        } else {
                            new MvMsgDialog().openShowMVMsgDialog(getContext(), "此需求为指定需求");
                        }
                    }
                }
            }
        });


        mRecyclerView.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {
                AppLogger.e("------------" + holder.toString());
            }
        });

    }


    public static final int REQUSST_PHONENUM = 1004;

    private void guestToast() {
        Activity mActivity = getBaseActivity();
        //普通用户判断
        if (mPresenter.isGuestUser()) {
            if (BuildConfig.isForceLoad.equals("1")) {
                CustomDialogUtils.showQZLoginDialog(mActivity, false);
            } else {
                CustomDialogUtils.showLoginDialog(mActivity, false);
            }
        } else {

            if (TextUtils.isEmpty(mPresenter.getUserData().getPhone())) {
                // 跳转到绑定手机号码
                final RxDialogSureCancel rxDialogSureCancel1 = new RxDialogSureCancel(getActivity());
                rxDialogSureCancel1.setSure("前往");
                rxDialogSureCancel1.setCancel("取消");
                rxDialogSureCancel1.setContent("绑定手机号开启认证啦");
                rxDialogSureCancel1.setSureListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivityForResult(new Intent(getActivity(), BlindPhoneActivity.class),
                                REQUSST_PHONENUM);
                        rxDialogSureCancel1.dismiss();
                    }
                });

                rxDialogSureCancel1.setCancelListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rxDialogSureCancel1.dismiss();
                    }
                });

                rxDialogSureCancel1.show();
                return;
            } else {
                int state = SharePreferenceUtil.getSessionInt(mActivity, AppPreferencesHelper.PREF_KEY_CERTIFISTATUS);
//                if (state == 1) {//已认证
//                    FloatViewWindowManger.removeSmallWindow();
//                    if (SharePreferenceUtil.getSessionInt(mActivity,
//                            AppPreferencesHelper.PREF_KEY_LIVECNT) > 0) {
//                        mPresenter.testStartLive();
//                    } else {
//                        startActivity(new Intent(mActivity, RenzhengSuccessActivity.class));
//                    }
//                } else

                if (state == 2) {
                    startActivity(new Intent(mActivity, UploadSuccessActivity.class));
                } else if (state == 3) {
                    //认证失败
                    startActivity(new Intent(mActivity, UploadFailureActivity.class));
                } else if (state == 0) {
                    //未认证
                    showKaiboDialog();

                }
            }
        }
    }


    /**
     * 开播对话框
     */
    private void showKaiboDialog() {
        final Dialog pickDialog2 = new Dialog(getContext(), R.style.color_dialog);
        pickDialog2.setContentView(R.layout.pop_kaibo);

        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog2.getWindow();
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.CENTER);
        lp.width = (int) (display.getWidth()); //设置宽度

        pickDialog2.getWindow().setAttributes(lp);
        Button button = (Button) pickDialog2.findViewById(R.id.btn_pop_renzheng);
        TextView tv_cancle = (TextView) pickDialog2.findViewById(R.id.tv_cancle);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), IdentityInfoActivity.class));
                pickDialog2.dismiss();
            }
        });
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDialog2.dismiss();
            }
        });
        pickDialog2.setCanceledOnTouchOutside(false);
        pickDialog2.show();
    }


    ShowDemandDialog showDemandDialog;

    private void openShowDemandDialog() {

        showDemandDialog = new ShowDemandDialog(getContext());
        showDemandDialog.getTvContent().setText(mItemGetAllNeedList.getRequire());
        showDemandDialog.getTv_userName().setText(mItemGetAllNeedList.getNickname());
        TCUtils.showPicWithUrl(getContext(), (ImageView) showDemandDialog.getIv_head(),
                mItemGetAllNeedList.getAvatar(), R.drawable.face);
        showDemandDialog.setCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDemandDialog.dismiss();
            }
        });

        showDemandDialog.setSureListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确定
                showDemandDialog.dismiss();

                long inCome = mPresenter.getUserData().getIncome();//收到的礼物币值
                SysConfigBean mSysConfigBean = FeihuZhiboApplication.getApplication().mDataManager
                        .getSysConfig();
                Long mvMinHb = mSysConfigBean.getMvIssueMinHb();
                if (mvMinHb == 0) {
                    mvMinHb = 300000L;
                }
                if (inCome >= mvMinHb) {
                    //判断是否已经提交过
                    mPresenter.enablePostMV(mItemGetAllNeedList.getNeedId());
                } else {
                    String coinText = CommonUtils.getWCoinText(String.valueOf(mvMinHb));
                    new MvMsgDialog().openShowMVMsgDialog(getContext(),
                            "收到礼物" + coinText + "才能接受需求赚取视频悬赏快去升级吧！", "确定");
                }
            }
        });

        if (mItemGetAllNeedList.isCollected()) {
            showDemandDialog.getIv_save().setImageResource(R.drawable.icon_mv_collect_s);
        } else {
            showDemandDialog.getIv_save().setImageResource(R.drawable.icon_mv_collect_n);
        }
        showDemandDialog.setSaveOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDemandDialog.getIv_save().setClickable(false);
                if (mItemGetAllNeedList.isCollected()) {
                    mPresenter.unCollectNeed(mItemGetAllNeedList.getNeedId());

                } else {
                    mPresenter.collectNeed(mItemGetAllNeedList.getNeedId());
                }
            }
        });

        showDemandDialog.show();

    }

    @Override
    public void onEnablePostMVResp(EnablePostMVResponse response) {
        if (response.getCode() == 0) {
            if (!response.isEnable()) {
                onToast("您已经提交过了哦！");
                return;
            }
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable("AllNeedListItemNeed", mItemGetAllNeedList);
        goActivity(PostMvVideoActivity.class, bundle);

    }

    @Override
    public void onUnCollectNeedResp(UnCollectNeedResponse response) {
        if (showDemandDialog != null) {
            showDemandDialog.getIv_save().setClickable(true);
        }
        if (response.getCode() == 0) {
            onToast("取消收藏");
            showDemandDialog.getIv_save().setImageResource(R.drawable.icon_mv_collect_n);
            if (mItemGetAllNeedList != null) {
                mItemGetAllNeedList.setCollected(false);
                mDemandSquareAdapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    public void onCollectNeedResp(CollectNeedResponse response) {
        if (showDemandDialog != null) {
            showDemandDialog.getIv_save().setClickable(true);
        }
        if (response.getCode() == 0) {
            onToast("收藏成功");
            showDemandDialog.getIv_save().setImageResource(R.drawable.icon_mv_collect_s);
            if (mItemGetAllNeedList != null) {
                mItemGetAllNeedList.setCollected(true);
                mDemandSquareAdapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    protected void lazyLoad() {
        if (isPrepared && isVisible && mGetAllMvLists.size() == 0) {
            isPrepared = false;
            //获取
            mPresenter.getAllNeedList("0", "1", "0", pageSize + "");
        }
    }


    @Override
    public void onGetAllNeedList(GetAllNeedListResponse response) {
        if (isPullRefresh) {
            mRecyclerView.refreshComplete();
        } else {
            mRecyclerView.loadMoreComplete();
        }
        if (response != null) {
            if (response.getCode() == 0) {
                GetAllNeedListResponse.GetAllNeedListData mData = response.getGetAllNeedListData();
                List<GetAllNeedListResponse.GetAllNeedList> mGetAllNeedLists =
                        mData.getGetAllNeedList();
                if (mGetAllNeedLists != null && mGetAllNeedLists.size() > 0) {
                    if (isPullRefresh) {
                        mGetAllMvLists.clear();
                    }
                    nextOffset = mData.getNextOffset();
                    nextForMe = mData.getNextForMe();
                    mGetAllMvLists.addAll(mGetAllNeedLists);
                    mDemandSquareAdapter.notifyDataSetChanged();
                } else {
                    if (!isPullRefresh) {
//                        mRecyclerView.setNoMore(true);
//                         onToast("没有更多需求了");
                    }
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroyView();
    }


    private boolean isShow = false;


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_mv_sq_add:
                showBtnMenu();
                break;
            case R.id.iv_mv_sq_desc:
                showBtnMenu();
                Bundle bundle = new Bundle();
                bundle.putString("url", mPresenter.getSysConfig().getMvInstructionUrl());
                bundle.putString("title", "MV玩法说明");
                goActivity(UserAgreement.class, bundle);
                break;
            case R.id.iv_mv_sq_post:
                if (!TCUtils.checkGuest(getActivity())) {
                    showBtnMenu();
                    goActivityForResult(PostDemandActivity.class, 1000);
                }
                break;
            default:
                break;
        }
    }

    @RxBusSubscribe(code = RX_BUS_CLICK_CODE_MV_POST_DEMAND,threadMode = ThreadMode.MAIN)
    public void onPostDemandActivity(Integer state){
        //发布成功后刷新
        mRecyclerView.refresh();
    }


    private void showBtnMenu() {
        if (!isShow) {
            iv_mv_sq_desc.setVisibility(View.VISIBLE);
            iv_mv_sq_post.setVisibility(View.VISIBLE);
            isShow = true;
        } else {
            iv_mv_sq_desc.setVisibility(View.GONE);
            iv_mv_sq_post.setVisibility(View.GONE);
            isShow = false;
        }
    }


}
