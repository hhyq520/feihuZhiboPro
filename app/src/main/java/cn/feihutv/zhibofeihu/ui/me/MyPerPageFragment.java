package cn.feihutv.zhibofeihu.ui.me;


import android.Manifest;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomContriRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomContriResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LogShareRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LogShareResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.ValueChangePush;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.rxbus.RxBusSubscribe;
import cn.feihutv.zhibofeihu.rxbus.ThreadMode;
import cn.feihutv.zhibofeihu.ui.me.baoguo.BegPackageActivity;
import cn.feihutv.zhibofeihu.ui.me.dowm.MyDownActivity;
import cn.feihutv.zhibofeihu.ui.me.encash.MyEarningsActivity;
import cn.feihutv.zhibofeihu.ui.me.guard.GuardActivity;
import cn.feihutv.zhibofeihu.ui.me.news.NoticeActivity;
import cn.feihutv.zhibofeihu.ui.me.recharge.RechargeActivity;
import cn.feihutv.zhibofeihu.ui.me.roommrgs.RoomMrgsActivity;
import cn.feihutv.zhibofeihu.ui.me.setting.SettingsActivity;
import cn.feihutv.zhibofeihu.ui.me.shop.ShoppingActivity;
import cn.feihutv.zhibofeihu.ui.me.vip.MyVipActivity;
import cn.feihutv.zhibofeihu.ui.widget.TCLineControllerView;
import cn.feihutv.zhibofeihu.ui.widget.scrolllayoutview.ScrollableHelper;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import cn.feihutv.zhibofeihu.utils.ShareUtil;
import cn.feihutv.zhibofeihu.utils.TCGlideCircleTransform;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by huanghao on 2017/5/9.
 */

public class MyPerPageFragment extends BaseDynamicFragment implements
        ScrollableHelper.ScrollableContainer, View.OnClickListener {

    Unbinder unbinder;
    @BindView(R.id.tv_hubi)
    TextView tvHubi;
    @BindView(R.id.tcl_idnum)
    TCLineControllerView tclIdnum;
    @BindView(R.id.tcl_news)
    RelativeLayout tclNews;
    @BindView(R.id.tcl_mall)
    TCLineControllerView tclMall;
    @BindView(R.id.tcl_baoguo)
    TCLineControllerView tclBaoguo;
    @BindView(R.id.con_one)
    ImageView conOne;
    @BindView(R.id.con_two)
    ImageView conTwo;
    @BindView(R.id.con_three)
    ImageView conThree;
    @BindView(R.id.contribution_text)
    RelativeLayout contributionNext;
    @BindView(R.id.tcl_send_gift)
    TCLineControllerView tclSendGift;
    @BindView(R.id.tcl_get_gift)
    TCLineControllerView tclGetGift;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.gonghui)
    TCLineControllerView gonghui;
    @BindView(R.id.sys_img)
    ImageView sys_img;
    @BindView(R.id.tcl_hubao)
    TCLineControllerView hubao;
    @BindView(R.id.tcl_earnings)
    TCLineControllerView tcl_earnings;
    @BindView(R.id.tcl_changkong)
    TCLineControllerView tcl_changkong;
    @BindView(R.id.view_ck)
    View mView;
    @BindView(R.id.view_earn)
    View view_earn;

    @BindView(R.id.view_hubao)
    View view_hubao;

    private String[] titles = {"我的守护", "我守护的"};

    private LoadUserDataBaseResponse.UserData mUserData;

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initWidget(View view) {

    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.person_page_view, null);
        ButterKnife.bind(this, rootView);
        mUserData = FeihuZhiboApplication.getApplication().mDataManager.getUserData();
        if (TextUtils.isEmpty(mUserData.getGuildName())) {
            gonghui.setVisibility(View.GONE);
        } else {
            gonghui.setContent(mUserData.getGuildName());
        }
        initView(rootView);
        return rootView;
    }

    private void initView(View view) {
        tvHubi.setText(String.valueOf(mUserData.gethB()));
        tclIdnum.setContent(mUserData.getAccountId());

        tclGetGift.setContent(FHUtils.intToF(mUserData.getIncome()));
        tclSendGift.setContent(FHUtils.intToF(mUserData.getContri()));
        tclMall.setContent("守护 靓号 座驾");
        initData(mUserData.getUserId());

        int openHubao = SharePreferenceUtil.getSessionInt(getContext(), "openHubao", 0);
        int openPlayLimitLevel = SharePreferenceUtil.getSessionInt(getContext(), "hbNeedLevel", 0);
        if (openHubao == 1 && mUserData.getLevel() >= openPlayLimitLevel) {
            hubao.setVisibility(View.VISIBLE);
            view_hubao.setVisibility(View.VISIBLE);
        } else {
            hubao.setVisibility(View.GONE);
            view_hubao.setVisibility(View.GONE);
        }

        if (mUserData.getCertifiStatus() != 1) {
            tcl_changkong.setVisibility(View.GONE);
            mView.setVisibility(View.GONE);
            view_earn.setVisibility(View.GONE);
            tcl_earnings.setVisibility(View.GONE);
        }

        boolean mine_notice = SharePreferenceUtil.getSessionBoolean(getContext(), "Mine_notice");
        if (mine_notice) {
            sys_img.setVisibility(View.VISIBLE);
        } else {
            sys_img.setVisibility(View.INVISIBLE);
        }

    }

    private void initData(String uid) {

        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .doLoadRoomContriApiCall(new LoadRoomContriRequest(uid, 3))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoadRoomContriResponce>() {
                    @Override
                    public void accept(@NonNull LoadRoomContriResponce loadRoomContriResponce) throws Exception {

                        if (loadRoomContriResponce.getCode() == 0) {
                            List<LoadRoomContriResponce.RoomContriData> contributeModels = loadRoomContriResponce.getRoomContriDataList();
                            int size = contributeModels.size();
                            switch (size) {
                                case 0:
                                    conOne.setVisibility(View.INVISIBLE);
                                    conTwo.setVisibility(View.INVISIBLE);
                                    conThree.setVisibility(View.INVISIBLE);
                                    break;
                                case 1:
                                    Glide.with(getContext()).load(contributeModels.get(0).getHeadUrl()).apply(new RequestOptions()
                                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                            .transform(new TCGlideCircleTransform(getContext())))
                                            .into(conThree);
                                    conOne.setVisibility(View.INVISIBLE);
                                    conTwo.setVisibility(View.INVISIBLE);
                                    conThree.setVisibility(View.VISIBLE);
                                    break;
                                case 2:
                                    Glide.with(getContext()).load(contributeModels.get(0).getHeadUrl()).apply(new RequestOptions()
                                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                            .transform(new TCGlideCircleTransform(getContext())))
                                            .into(conTwo);

                                    Glide.with(getContext()).load(contributeModels.get(1).getHeadUrl()).apply(new RequestOptions()
                                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                            .transform(new TCGlideCircleTransform(getContext())))
                                            .into(conThree);
                                    conOne.setVisibility(View.INVISIBLE);
                                    conTwo.setVisibility(View.VISIBLE);
                                    conThree.setVisibility(View.VISIBLE);
                                    break;
                                case 3:
                                default:
                                    Glide.with(getContext()).load(contributeModels.get(0).getHeadUrl()).apply(new RequestOptions()
                                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                            .transform(new TCGlideCircleTransform(getContext())))
                                            .into(conOne);

                                    Glide.with(getContext()).load(contributeModels.get(1).getHeadUrl()).apply(new RequestOptions()
                                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                            .transform(new TCGlideCircleTransform(getContext())))
                                            .into(conTwo);

                                    Glide.with(getContext()).load(contributeModels.get(2).getHeadUrl()).apply(new RequestOptions()
                                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                            .transform(new TCGlideCircleTransform(getContext())))
                                            .into(conThree);
                                    conOne.setVisibility(View.VISIBLE);
                                    conTwo.setVisibility(View.VISIBLE);
                                    conThree.setVisibility(View.VISIBLE);
                                    break;
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                })
        );
    }

    @OnClick({R.id.btn_recharge, R.id.tcl_news, R.id.tcl_level, R.id.tcl_idnum, R.id.tcl_mall, R.id.tcl_baoguo, R.id.contribution_text, R.id.tcl_changkong
            , R.id.tcl_earnings, R.id.tcl_invite_friends, R.id.tcl_setting, R.id.me_vip, R.id.tcl_shouhu, R.id.tcl_mydown, R.id.tcl_hubao})
    public void onClick(View view) {
        LoadUserDataBaseResponse.UserData userData = FeihuZhiboApplication.getApplication().mDataManager.getUserData();
        switch (view.getId()) {
            case R.id.btn_recharge:
                Intent intent1 = new Intent(getContext(), RechargeActivity.class);
                intent1.putExtra("fromWhere", "个人主页页面");
                startActivity(intent1);
                break;
            case R.id.tcl_idnum:
                ClipboardManager clipboard = (ClipboardManager) getContext()
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("id", userData.getAccountId());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(), "已复制", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tcl_news:
                sys_img.setVisibility(View.INVISIBLE);
                SharePreferenceUtil.saveSeesionBoolean(getContext(), "Mine_notice", false);
                startActivity(new Intent(getContext(), NoticeActivity.class));
                break;
            case R.id.tcl_level:
                startActivity(new Intent(getContext(), LevelActivity.class));
                break;
            case R.id.tcl_mall:
                startActivity(new Intent(getContext(), ShoppingActivity.class));
                break;
            case R.id.tcl_baoguo:
                // 包裹
                startActivity(new Intent(getContext(), BegPackageActivity.class));
                break;
            case R.id.contribution_text:
                userData = FeihuZhiboApplication.
                        getApplication().mDataManager.getUserData();
                if (userData.getCertifiStatus() == 1) {
                    Intent intent = new Intent(getContext(), ContributionListActivity.class);
                    intent.putExtra("userId", userData.getUserId());
                    intent.putExtra("noDataTitle", "暂无贡献哦，马上开播吸引粉丝送礼吧~");
                    startActivity(intent);
                } else {
                    FHUtils.showToast("暂无粉丝贡献榜");
                }
                break;
            case R.id.tcl_changkong:
                startActivity(new Intent(getContext(), RoomMrgsActivity.class));
                break;
            case R.id.tcl_earnings:
                startActivity(new Intent(getContext(), MyEarningsActivity.class));
                break;
            case R.id.tcl_invite_friends:
                showShareDialog();
                break;
            case R.id.btn_share_circle:
                MobclickAgent.onEvent(getContext(), "10137");
                if (FHUtils.isAppInstalled(getContext(), "com.tencent.mm")) {
                    share(SHARE_MEDIA.WEIXIN_CIRCLE);
                } else {
                    onToast("未安装微信", Gravity.CENTER, 0, 0);
                }
                break;
            case R.id.btn_share_wx:
                MobclickAgent.onEvent(getContext(), "10136");
                if (FHUtils.isAppInstalled(getContext(), "com.tencent.mm")) {
                    share(SHARE_MEDIA.WEIXIN);
                } else {
                    onToast("未安装微信", Gravity.CENTER, 0, 0);
                }

                break;
            case R.id.btn_share_wb:
                MobclickAgent.onEvent(getContext(), "10140");
                share(SHARE_MEDIA.SINA);
                break;
            case R.id.btn_share_qq:
                MobclickAgent.onEvent(getContext(), "10138");
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPerssion(1010)) {
                        share(SHARE_MEDIA.QQ);
                    }
                } else {
                    share(SHARE_MEDIA.QQ);
                }
                break;
            case R.id.btn_share_qzone:
                MobclickAgent.onEvent(getContext(), "10139");
                share(SHARE_MEDIA.QZONE);
                break;
            case R.id.tcl_setting:
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.me_vip:
                startActivity(new Intent(getContext(), MyVipActivity.class));
                break;
            case R.id.tcl_shouhu:
                Intent intent2 = new Intent(getContext(), GuardActivity.class);
                intent2.putExtra("userId", userData.getUserId());
                intent2.putExtra("titles", titles);
                startActivity(intent2);
                break;
            case R.id.tcl_mydown:
                goActivity(MyDownActivity.class);
                break;
            case R.id.tcl_hubao:
                startActivity(new Intent(getContext(), HubaoActivity.class));
                break;
            default:
                break;
        }
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    private void showShareDialog() {
        final Dialog pickDialog1 = new Dialog(getContext(), R.style.color_dialog);
        pickDialog1.setContentView(R.layout.share_dialog);

        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog1.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = (int) (display.getWidth()); //设置宽度
        pickDialog1.getWindow().setAttributes(lp);

        ImageView btn_share_circle = (ImageView) pickDialog1.findViewById(R.id.btn_share_circle);
        ImageView btn_share_wx = (ImageView) pickDialog1.findViewById(R.id.btn_share_wx);
        ImageView btn_share_wb = (ImageView) pickDialog1.findViewById(R.id.btn_share_wb);
        ImageView btn_share_qq = (ImageView) pickDialog1.findViewById(R.id.btn_share_qq);
        ImageView btn_share_qzone = (ImageView) pickDialog1.findViewById(R.id.btn_share_qzone);

        btn_share_circle.setOnClickListener(this);
        btn_share_wx.setOnClickListener(this);
        btn_share_wb.setOnClickListener(this);
        btn_share_qq.setOnClickListener(this);
        btn_share_qzone.setOnClickListener(this);

        pickDialog1.show();
    }


    @Override
    public View getScrollableView() {
        return scrollView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    // 数值变化
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_VALUE_CHANGE, threadMode = ThreadMode.MAIN)
    public void onReceiveValueChangePush(ValueChangePush pushData) {
        if (pushData != null) {
            if ("HB".equals(pushData.getType())) {
                tvHubi.setText(String.valueOf(pushData.getNewVal()));
            } else if ("Income".equals(pushData.getType())) {
                tclGetGift.setContent(FHUtils.intToF(pushData.getNewVal()));
            } else if ("Contri".equals(pushData.getType())) {
                tclSendGift.setContent(FHUtils.intToF(pushData.getNewVal()));
            }
        }
    }

    @Override
    public void pullToRefresh() {

    }

    @Override
    public void refreshComplete() {

    }

    private void share(SHARE_MEDIA plat) {
        String headUrl = mUserData.getHeadUrl();
        String nickName = mUserData.getNickName();
        String accountId = mUserData.getAccountId();
        String url = SharePreferenceUtil.getSession(getContext(), "registerUrl") + "?AccountId=" + accountId;
        url.replace("https", "http");
        if (TextUtils.isEmpty(nickName)) {
            nickName = "我";
        } else {
            nickName = "[" + nickName + "]";
        }
        ShareUtil.ShareWeb(getActivity(), nickName + "入驻飞虎,想看TA是如何开撩的，赶紧进来看看吧～", nickName + "入驻飞虎直播啦！",
                headUrl, url, plat, umShareListener);
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
            new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                    .doLogShareApiCall(new LogShareRequest(3, to))
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
                    }));
            FHUtils.showToast("分享成功");
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            FHUtils.showToast("分享失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {

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

    // 接收更换靓号的通知
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_NOTICE_ACCOUNTID_CHANGE, threadMode = ThreadMode.MAIN)
    public void onReceivePositioncurrentitem(String accountId) {

        tclIdnum.setContent(accountId);
    }

    // 接收系统消息
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_NOTICE_SYS_MESSAGE, threadMode = ThreadMode.MAIN)
    public void onReceiveSystemNews() {
        boolean mine_notice = SharePreferenceUtil.getSessionBoolean(getContext(), "Mine_notice");
        if (mine_notice) {
            sys_img.setVisibility(View.VISIBLE);
        } else {
            sys_img.setVisibility(View.INVISIBLE);
        }
    }

    //收到实名认证成功
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_CERTIFICATION_RESULT, threadMode = ThreadMode.MAIN)
    public void onReceiveCertificationResult(boolean statue) {
        if (statue) {
            tcl_changkong.setVisibility(View.VISIBLE);
            mView.setVisibility(View.VISIBLE);
            view_earn.setVisibility(View.VISIBLE);
            tcl_earnings.setVisibility(View.VISIBLE);
        }
    }
}
