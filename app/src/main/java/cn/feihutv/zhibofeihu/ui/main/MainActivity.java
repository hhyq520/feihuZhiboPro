package cn.feihutv.zhibofeihu.ui.main;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.allenliu.versionchecklib.core.AllenChecker;
import com.allenliu.versionchecklib.core.VersionParams;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.BuildConfig;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.TestStartLiveResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.user.LoginResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.common.NetworkChangePush;
import cn.feihutv.zhibofeihu.data.network.socket.model.common.SocketConnectError;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.BannedLoginPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.ForcedOfflinePush;
import cn.feihutv.zhibofeihu.data.network.socket.xsocket.FHSocket;
import cn.feihutv.zhibofeihu.data.prefs.AppPreferencesHelper;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.rxbus.RxBusSubscribe;
import cn.feihutv.zhibofeihu.rxbus.ThreadMode;
import cn.feihutv.zhibofeihu.service.DemoService;
import cn.feihutv.zhibofeihu.ui.bangdan.BangDanFragment;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.home.HomeFragment;
import cn.feihutv.zhibofeihu.ui.home.signin.SignInDialog;
import cn.feihutv.zhibofeihu.ui.live.SWCameraStreamingActivity;
import cn.feihutv.zhibofeihu.ui.live.renzheng.IdentityInfoActivity;
import cn.feihutv.zhibofeihu.ui.live.renzheng.RenzhengSuccessActivity;
import cn.feihutv.zhibofeihu.ui.live.renzheng.UploadFailureActivity;
import cn.feihutv.zhibofeihu.ui.live.renzheng.UploadSuccessActivity;
import cn.feihutv.zhibofeihu.ui.me.MyFragment;
import cn.feihutv.zhibofeihu.ui.me.personalInfo.BlindPhoneActivity;
import cn.feihutv.zhibofeihu.ui.mv.MvSquareFragment;
import cn.feihutv.zhibofeihu.ui.mv.demand.PostMvVideoActivity;
import cn.feihutv.zhibofeihu.ui.user.login.LoginActivity;
import cn.feihutv.zhibofeihu.ui.widget.TabBottomView;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.AppUtils;
import cn.feihutv.zhibofeihu.utils.CustomDialogUtils;
import cn.feihutv.zhibofeihu.utils.FloatViewWindowManger;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.feihutv.zhibofeihu.utils.weiget.dialog.RxDialogSureCancel;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.jzvd.JZVideoPlayer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/
 *     desc   : 界面绘制
 *     version: 1.0
 * </pre>
 */
public class MainActivity extends BaseActivity implements MainMvpView, View.OnClickListener {

    @Inject
    MainMvpPresenter<MainMvpView> mPresenter;
    @BindView(R.id.tab_bottom)
    TabBottomView tabBottom;
    @BindView(R.id.fragment)
    FrameLayout frameLayout;
    public static Activity MainActivityself;
    private Intent onHomeIntent;
    private String url;
    private boolean isGoLive = false;
    private long exitTime = 0;
    private int currentFragment = -1;
    private int lastFrament = 1;
    private PopupWindow popWindow;
    private int viewpagerContent = 0;
    private String roomId;
    private MyFragment tclCommunityFragment;
    private   MvSquareFragment  mMvSquareFragment;
    public static final int REQUSST_PHONENUM = 1004;
    private LoadUserDataBaseResponse.UserData mUserData;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void initWidget() {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this));

        //persenter调用初始化
        mPresenter.onAttach(MainActivity.this);

        MainActivityself = this;

        mUserData = mPresenter.getUserData();

        try {
            JPushInterface.setAlias(this, SharePreferenceUtil.getSession(MainActivity.this, "PREF_KEY_USERID"), new TagAliasCallback() {
                @Override
                public void gotResult(int i, String s, Set<String> set) {

                }
            });
        } catch (Exception e) {

        }

        showCustomVersionDialog();
        initData();

        //设置全屏部分横竖屏
        JZVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;  //横向
        JZVideoPlayer.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;  //纵向
        SharePreferenceUtil.saveSeesionBoolean(FeihuZhiboApplication.getApplication(),"meisGuard",false);//设置不是守护
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            url = intent.getStringExtra("url");
        }
        if (!android.text.TextUtils.isEmpty(url)) {
            try {
                JSONObject jsonObject = new JSONObject(url);
                String pushType = jsonObject.getString("pushType");
                if (pushType.equals("live")) {
                    roomId = jsonObject.getString("json");
                    isGoLive = true;
                } else {
                    isGoLive = false;
                    viewpagerContent = 2;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (viewpagerContent == 0) {
            showFragment(R.id.fragment, new HomeFragment());
        } else if (viewpagerContent == 1) {
            showFragment(R.id.fragment, new BangDanFragment());
            tabBottom.changeState(2);
        } else if (viewpagerContent == 2) {
            mMvSquareFragment=new MvSquareFragment();
            showFragment(R.id.fragment, mMvSquareFragment);
            tabBottom.changeState(4);
        } else if (viewpagerContent == 3) {
            showFragment(R.id.fragment, new MyFragment());
            tabBottom.changeState(5);
        }

        tabBottom.setTabBottomClickListener(new TabBottomView.TabBottomClickListener() {
            @Override
            public void click(int type) {
                onHomeIntent = null;
                switch (type) {
                    case 1:
                        MobclickAgent.onEvent(MainActivity.this, "10008");
                        lastFrament = currentFragment;
                        currentFragment = 1;
                        showFragment(R.id.fragment, new HomeFragment());
                        break;
                    case 2:
                        MobclickAgent.onEvent(MainActivity.this, "10016");
                        lastFrament = currentFragment;
                        currentFragment = 2;
                        showFragment(R.id.fragment, new BangDanFragment());
                        break;
                    case 3:
                        MobclickAgent.onEvent(MainActivity.this, "10017");
                        lastFrament = currentFragment;
                        currentFragment = 3;
                        downPopwindow();
                        break;
                    case 4:
                        MobclickAgent.onEvent(MainActivity.this, "10018");
                        lastFrament = currentFragment;
                        currentFragment = 4;

                        if(mMvSquareFragment==null) {
                            mMvSquareFragment = new MvSquareFragment();
                        }
                        showFragment(R.id.fragment,mMvSquareFragment);
                        mMvSquareFragment.queryMVNotice();
                        JZVideoPlayer.releaseAllVideos();
                        AppLogger.i("MvSquareFragment tab is  ...");

                        break;
                    case 5:
                        MobclickAgent.onEvent(MainActivity.this, "10019");
                        lastFrament = currentFragment;
                        tclCommunityFragment = new MyFragment();
                        if (mPresenter.isGuestUser()) {
                            if (BuildConfig.isForceLoad.equals("1")) {
                                CustomDialogUtils.showQZLoginDialog(MainActivity.this, false);
                            } else {
                                CustomDialogUtils.showLoginDialog(MainActivity.this, false);
                            }
                        } else {
                            currentFragment = 5;
                            showFragment(R.id.fragment, tclCommunityFragment);
                        }
                        break;
                    default:
                        break;
                }
            }
        });


        if (isGoLive) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mPresenter.loadRoomById(roomId);
                }
            }, 3500);
        } else {
            if (!mPresenter.isGuestUser()) {
                mPresenter.loadSignData();
            } else {
                SignInDialog signInDialog = new SignInDialog();
                signInDialog.show(getSupportFragmentManager());
            }
        }
        mPresenter.downLoadGiftResource();
    }

    /**
     * 开播对话框
     */
    private void showKaiboDialog() {
        final Dialog pickDialog2 = new Dialog(MainActivity.this, R.style.color_dialog);
        pickDialog2.setContentView(R.layout.pop_kaibo);

        WindowManager windowManager = getWindowManager();
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
                startActivity(new Intent(MainActivity.this, IdentityInfoActivity.class));
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

    @Override
    protected void onResume() {
        if (onHomeIntent != null) {
            viewpagerContent = onHomeIntent.getIntExtra("viewpagerContent", 0);
            if (viewpagerContent == 0) {
                showFragment(R.id.fragment, new HomeFragment());
                if (tabBottom != null) {
                    tabBottom.changeState(1);
                }
                currentFragment = 1;
            } else if (viewpagerContent == 1) {
                showFragment(R.id.fragment, new BangDanFragment());
                if (tabBottom != null) {
                    tabBottom.changeState(2);
                }
                currentFragment = 2;
            } else if (viewpagerContent == 2) {
                showFragment(R.id.fragment, new MvSquareFragment());
                if (tabBottom != null) {
                    tabBottom.changeState(4);
                }
                currentFragment = 4;
            } else if (viewpagerContent == 3) {

            }
        }
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroy();
    }


    @Override
    public void showCustomVersionDialog() {
        if (BuildConfig.VERSION_CODE < SharePreferenceUtil.getSessionInt(MainActivity.this, "versionId", 10000)) {
            VersionParams.Builder builder = new VersionParams.Builder()
                    .setRequestUrl("http://www.baidu.com")
                    .setPauseRequestTime(100)
                    .setService(DemoService.class);
            stopService(new Intent(this, DemoService.class));
            builder.setDownloadAPKPath(getExternalStorageDirectory(this) + "/AllenVersionPath");
            builder.setCustomDownloadActivityClass(CustomVersionDialogActivity.class);
            CustomVersionDialogActivity.isCustomDownloading = true;
            builder.setCustomDownloadActivityClass(CustomVersionDialogActivity.class);
            builder.setForceRedownload(false);
            if (BuildConfig.VERSION_CODE < SharePreferenceUtil.getSessionInt(MainActivity.this, "needVersionId", 10000)) {
                CustomVersionDialogActivity.isForceUpdate = true;
                builder.setCustomDownloadActivityClass(CustomVersionDialogActivity.class);
            } else {
                CustomVersionDialogActivity.isForceUpdate = false;
                builder.setCustomDownloadActivityClass(CustomVersionDialogActivity.class);
                builder.setForceRedownload(false);
            }
            AllenChecker.startVersionCheck(this, builder.build());
        } else {

        }
    }

    @Override
    public void gotoRoom(LoadRoomResponce.LoadRoomData data) {
        FloatViewWindowManger.removeSmallWindow();
        // 1为PC   2为手机
        AppUtils.startLiveActivity(MainActivity.this, data.getRoomId(), data.getHeadUrl(), data.getBroadcastType(), false);
    }

    @Override
    public void showSignDialog(int days) {
        SignInDialog.newInstance(days).show(getSupportFragmentManager());
    }


    @Override
    public Activity getActivity() {
        return MainActivity.this;
    }

    @Override
    public void notifyLive(TestStartLiveResponce responce) {
        if (responce.getCode() == 0) {
            startActivity(new Intent(MainActivity.this, SWCameraStreamingActivity.class));
        } else {
            String desc = responce.getErrExtMsgData().getDesc();
            String duration = responce.getErrExtMsgData().getDuration();
            showBanDialog(desc, duration);
        }
    }


    private void downPopwindow() {
        // showAsDropDown(View anchor);相对某个控件的位置（正左下方），无偏移
        // showAsDropDown(View anchor, int x, int
        // y);相对某个控件的位置，有偏移;x表示相对x轴的偏移，正表示向左，负表示向右；y表示相对y轴的偏移，正是向下，负是向上；
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_kaibo_choose, null);
        // 这里就给具体大小的数字，要不然位置不好计算
        popWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popWindow.setAnimationStyle(R.style.anim);// 淡入淡出动画
//        popWindow.setTouchable(false);// 是否响应touch事件
        popWindow.setFocusable(true);// 是否具有获取焦点的能力
        // 点击PopupWindow以外的区域，PopupWindow是否会消失。
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        popWindow.setOutsideTouchable(true);

        TextView tvLive = (TextView) contentView.findViewById(R.id.tv_live);
        TextView tvMv = (TextView) contentView.findViewById(R.id.tv_mv);

        tvLive.setOnClickListener(this);
        tvMv.setOnClickListener(this);

        popWindow.showAtLocation(tabBottom, Gravity.BOTTOM | Gravity.CENTER, 0, TCUtils.dip2px(getActivity(), 62));
    }


    public void showBanDialog(String msg, String time) {
        final Dialog pickDialog2 = new Dialog(MainActivity.this, R.style.color_dialog);
        pickDialog2.setContentView(R.layout.pop_ban);

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog2.getWindow();
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.CENTER);
        lp.width = (int) (display.getWidth()); //设置宽度
        pickDialog2.getWindow().setAttributes(lp);

        TextView tvContent = (TextView) pickDialog2.findViewById(R.id.tv_pop);
        String str = "<font color='#000000'>您因</font>" +
                "<font color='#eF3425'> </font>" + msg +
                "<font color='#eF3425'> </font>" +
                "<font color='#000000'>被系统禁播";
        tvContent.setText(Html.fromHtml(str));
        TextView tvTime = (TextView) pickDialog2.findViewById(R.id.tv_time);
        tvTime.setText("禁播时长为" + time + "分钟");
        TextView tvOk = (TextView) pickDialog2.findViewById(R.id.tv_ok);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDialog2.dismiss();
            }
        });
        pickDialog2.setCanceledOnTouchOutside(false);
        pickDialog2.show();
    }

    private String getExternalStorageDirectory(Context ctx) {
        String path;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            return null;
        }
        return path + "/" + ctx.getPackageName();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        onHomeIntent = intent;
        super.onNewIntent(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (JZVideoPlayer.backPress()) {
                return false;
            }
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            onToast("再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }


    //异地登录，强制掉线处理
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_FORCED_OFFLINE, threadMode = ThreadMode.MAIN)
    public void onReceiveForcedOfflinePush(ForcedOfflinePush pushData) {
        //收到下线通知，进行下线处理
        //跳转登录页面
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.putExtra("force", true);
        startActivity(intent);
        TCUtils.saveLoginInfo("", "");
        FHSocket.getInstance().close();
        finish();
    }

    //
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_BANNED_LOGIN, threadMode = ThreadMode.MAIN)
    public void onReceiveBannedLoginPush(BannedLoginPush pushData) {
        //收到下线通知，进行下线处理
        //跳转登录页面
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.putExtra("bannedLogin", true);
        intent.putExtra("Msg", pushData.getMsg());
        intent.putExtra("QQ", pushData.getQq());
        intent.putExtra("Duration", pushData.getDuration());
        startActivity(intent);
        mPresenter.setUserAsLoggedOut();
        finish();
    }


    //socket 连接建立成功 需要向服务端重新建立授权
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_SOCKET_RECONNECT_COMPLETE, threadMode = ThreadMode.MAIN)
    public void onReceiveFSocketConnectError(final SocketConnectError socketConnectError) {
        //收到下线通知，进行下线处理
        AppLogger.e("网络状态不佳：正在重连.....请求状态=" + socketConnectError.isReconnecting);

        mPresenter.enterLogin(socketConnectError.isReconnecting, new Consumer<LoginResponse>() {
            @Override
            public void accept(@NonNull LoginResponse loginResponse) throws Exception {

                if (loginResponse.getCode() != 0) {
                    if ("0".equals(socketConnectError.isReconnecting)) {
                        onToast("你已掉线，请重新登录！");
                        openActivityOnTokenExpire();//跳转登录页面
                        mPresenter.setUserAsLoggedOut();
                    }
                }
            }
        });

    }

    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_NOTICE_ENTER_LOGIN, threadMode = ThreadMode.MAIN)
    public void onReceiveEnterLogin() {
        mPresenter.enterLogin("0", new Consumer<LoginResponse>() {
            @Override
            public void accept(@NonNull LoginResponse loginResponse) throws Exception {
                if (loginResponse.getCode() == 0) {
                    CustomDialogUtils.enterLoginSuccess();
                } else {
                    CustomDialogUtils.enterLoginFail(loginResponse);
                }
            }
        });
    }

    //切换网络
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_NOTICE_NETWORK_CHANGE, threadMode = ThreadMode.MAIN)
    public void onReceiveNetworkChangePush(NetworkChangePush push) {
        if (push.getCode() == 0) {
            onToast("切换到4G,请注意流量");
        } else if (push.getCode() == 1) {
            onToast("切换到wifi");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (currentFragment == 5) {
            tclCommunityFragment.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == REQUSST_PHONENUM) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                String phone = bundle.getString("phonenumber");
                mUserData.setPhone(phone);
                mPresenter.saveUserData(mUserData);
            }
        }

    }

    //收到消息
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_CERTIFICATION_RESULT, threadMode = ThreadMode.MAIN)
    public void onReceiveCertificationResult(boolean statue) {
        if (statue) {
            mUserData.setCertifiStatus(1);
        } else {
            mUserData.setCertifiStatus(3);
        }
        mPresenter.saveUserData(mUserData);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_live:
                // 开播
                if (mPresenter.isGuestUser()) {
                    if (BuildConfig.isForceLoad.equals("1")) {
                        CustomDialogUtils.showQZLoginDialog(MainActivity.this, false);
                    } else {
                        CustomDialogUtils.showLoginDialog(MainActivity.this, false);
                    }
                } else {
                    if (popWindow != null) {
                        popWindow.dismiss();
                    }
                    if (TextUtils.isEmpty(mUserData.getPhone())) {
                        // 跳转到绑定手机号码
                        final RxDialogSureCancel rxDialogSureCancel1 = new RxDialogSureCancel(getActivity());
                        rxDialogSureCancel1.setSure("前往");
                        rxDialogSureCancel1.setCancel("取消");
                        rxDialogSureCancel1.setContent("绑定手机号开启认证啦");
                        rxDialogSureCancel1.setSureListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivityForResult(new Intent(getActivity(), BlindPhoneActivity.class), REQUSST_PHONENUM);
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
                        int state = SharePreferenceUtil.getSessionInt(MainActivity.this, AppPreferencesHelper.PREF_KEY_CERTIFISTATUS);
                        if (state == 1) {
                            FloatViewWindowManger.removeSmallWindow();
                            if (SharePreferenceUtil.getSessionInt(MainActivity.this, AppPreferencesHelper.PREF_KEY_LIVECNT) > 0) {
                                mPresenter.testStartLive();
                            } else {
                                startActivity(new Intent(MainActivity.this, RenzhengSuccessActivity.class));
                            }
                        } else if (state == 2) {
                            startActivity(new Intent(MainActivity.this, UploadSuccessActivity.class));
                        } else if (state == 3) {
                            //认证失败
                            startActivity(new Intent(MainActivity.this, UploadFailureActivity.class));
                        } else if (state == 0) {
                            showKaiboDialog();
                        }
                    }
                }
                break;
            case R.id.tv_mv:
                // 发布MV
                if (mPresenter.isGuestUser()) {
                    if (BuildConfig.isForceLoad.equals("1")) {
                        CustomDialogUtils.showQZLoginDialog(MainActivity.this, false);
                    } else {
                        CustomDialogUtils.showLoginDialog(MainActivity.this, false);
                    }
                } else {
                    if (popWindow != null) {
                        popWindow.dismiss();
                    }

                    if (TextUtils.isEmpty(mUserData.getPhone())) {
                        // 跳转到绑定手机号码
                        final RxDialogSureCancel rxDialogSureCancel1 = new RxDialogSureCancel(getActivity());
                        rxDialogSureCancel1.setSure("前往");
                        rxDialogSureCancel1.setCancel("取消");
                        rxDialogSureCancel1.setContent("绑定手机号开启认证啦");
                        rxDialogSureCancel1.setSureListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivityForResult(new Intent(getActivity(), BlindPhoneActivity.class), REQUSST_PHONENUM);
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
                        int state = SharePreferenceUtil.getSessionInt(MainActivity.this, AppPreferencesHelper.PREF_KEY_CERTIFISTATUS);
                        if (state == 1) {
                            startActivity(new Intent(getActivity(), PostMvVideoActivity.class));
                        } else if (state == 2) {
                            startActivity(new Intent(MainActivity.this, UploadSuccessActivity.class));
                        } else if (state == 3) {
                            //认证失败
                            startActivity(new Intent(MainActivity.this, UploadFailureActivity.class));
                        } else if (state == 0) {
                            showKaiboDialog();
                        }
                    }
                }
                break;
            default:
                break;
        }
    }
}
