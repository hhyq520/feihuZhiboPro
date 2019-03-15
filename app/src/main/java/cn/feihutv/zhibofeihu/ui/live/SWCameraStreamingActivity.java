package cn.feihutv.zhibofeihu.ui.live;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.chinanetcenter.StreamPusher.sdk.OnErrorListener;
import com.chinanetcenter.StreamPusher.sdk.SPConfig;
import com.chinanetcenter.StreamPusher.sdk.SPManager;
import com.chinanetcenter.StreamPusher.sdk.SPSurfaceView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cn.feihutv.zhibofeihu.R;

import cn.feihutv.zhibofeihu.data.db.model.SysGiftNewBean;
import cn.feihutv.zhibofeihu.data.db.model.SysLaunchTagBean;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.GetRoomMrgsResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.UserGiftBean;
import cn.feihutv.zhibofeihu.data.network.socket.model.common.NetworkChangePush;
import cn.feihutv.zhibofeihu.data.network.socket.model.common.SocketConnectError;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.JoinRoomResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.StartLiveResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.StreamErrorPush;
import cn.feihutv.zhibofeihu.data.network.socket.xsocket.FHSocket;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.rxbus.RxBusSubscribe;
import cn.feihutv.zhibofeihu.rxbus.ThreadMode;
import cn.feihutv.zhibofeihu.ui.live.SWCameraStreamingMvpView;
import cn.feihutv.zhibofeihu.ui.live.SWCameraStreamingMvpPresenter;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.ui.live.adapter.MainFragmentPageAdapter;
import cn.feihutv.zhibofeihu.ui.live.adapter.OffineGiftAdapter;
import cn.feihutv.zhibofeihu.ui.main.MainActivity;
import cn.feihutv.zhibofeihu.ui.user.UserAgreement;
import cn.feihutv.zhibofeihu.ui.widget.dialog.ContributionListDialogFragment;
import cn.feihutv.zhibofeihu.ui.widget.dialog.DetailDialogFragment;
import cn.feihutv.zhibofeihu.utils.AndroidBug54971Workaround;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.DialogUtil;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.PermissionCheck;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import cn.feihutv.zhibofeihu.utils.ShareUtil;
import cn.feihutv.zhibofeihu.utils.TCConstants;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.feihutv.zhibofeihu.utils.weiget.dialog.RoomMrgsDialogFragment;
import cn.feihutv.zhibofeihu.utils.weiget.dialog.RxDialogSureCancel;
import cn.feihutv.zhibofeihu.utils.weiget.view.PickerView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.chinanetcenter.StreamPusher.sdk.SPConfig.DECODER_MODE_HARD;

/**
 * <pre>
 *     author : huang hao
 *     time   :
 *     desc   : 界面绘制
 *     version: 1.0
 * </pre>
 */
public class SWCameraStreamingActivity extends BaseActivity implements SWCameraStreamingMvpView, ChatLiveFragment.LiveShareListener, ChatLiveFragment.ChatLiveFragmentListener {

    @Inject
    SWCameraStreamingMvpPresenter<SWCameraStreamingMvpView> mPresenter;
    @BindView(R.id.btn_yanzhi)
    Button btnYanzhi;
    @BindView(R.id.btn_xinxiu)
    Button btnXinxiu;
    @BindView(R.id.btn_caiyi)
    Button btnCaiyi;
    @BindView(R.id.btn_xinnan)
    Button btnXinnan;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.btn_exit)
    Button btnExit;
    @BindView(R.id.title)
    EditText title;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.btn_agree)
    CheckBox btnAgree;
    @BindView(R.id.btn_begin)
    Button btnBegin;
    @BindView(R.id.prepare_view)
    RelativeLayout prepareView;
    @BindView(R.id.mPreviewView)
    SPSurfaceView mPreviewView;
    @BindView(R.id.switch_button)
    ImageView switchButton;
    @BindView(R.id.icon_fresh)
    ImageView icon_fresh;

    private String tag = "";
    private int from = 1;
    private boolean isClick = false;
    private static int mCurActivityHashCode = 0;
    private boolean mIsUserPushing = false;
    private PermissionCheck mPermissionChecker;
    private String roomId;
    private ArrayList<Fragment> fragments;
    private ChatLiveFragment chatFragment;
    private NoFragment noFragment;
    private List<SysLaunchTagBean> sysLaunchTagBeenList = new ArrayList<>();
    private int refeshCount = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_swcamera_streaming;
    }

    private void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    protected void beforeSet() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void initWidget() {
//        AndroidBug54971Workaround.assistActivity(findViewById(android.R.id.content));
//        hideBottomUIMenu();
//        final Window dlgwin = getWindow();
//        dlgwin.getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
//            @Override
//            public void onSystemUiVisibilityChange(int visibility) {
//                hideBottomUIMenu();
//            }
//        });

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this));

        //persenter调用初始化
        mPresenter.onAttach(SWCameraStreamingActivity.this);
        mPermissionChecker = new PermissionCheck(this);

        boolean isPermissionOK = Build.VERSION.SDK_INT < Build.VERSION_CODES.M || mPermissionChecker.checkPermission();
        fragments = new ArrayList<>();

        if (isPermissionOK) {
            mPresenter.startPublish(mPreviewView);
            mPresenter.initSPManager();
        }
        mPresenter.dealTelephone();
        sysLaunchTagBeenList = mPresenter.getkaiboSysLaunchTagBean();
        if (sysLaunchTagBeenList.size() >= 8) {
            icon_fresh.setVisibility(View.VISIBLE);
        } else {
            icon_fresh.setVisibility(View.GONE);
        }

        if (sysLaunchTagBeenList.size() >= 4) {
            btnYanzhi.setText(sysLaunchTagBeenList.get(0).getName());
            btnXinxiu.setText(sysLaunchTagBeenList.get(1).getName());
            btnCaiyi.setText(sysLaunchTagBeenList.get(2).getName());
            btnXinnan.setText(sysLaunchTagBeenList.get(3).getName());
            refeshCount++;
        }
        if (!TCUtils.isWifiAvailable(this)) {
            if (isNetworkConnected()) {
                showNetChangeDialog();
            } else {
                showComfirmDialog("无网络，确定退出直播间", true);
            }
        }
    }


    @OnClick({R.id.icon_fresh, R.id.switch_button, R.id.btn_yanzhi, R.id.btn_xinxiu, R.id.btn_caiyi, R.id.btn_xinnan, R.id.btn_exit, R.id.btn_share_weibo, R.id.btn_share_qzone, R.id.btn_share_wx, R.id.btn_share_wx_friends, R.id.btn_share_qq, R.id.btn_begin, R.id.tv_xieyi})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.icon_fresh:
                if (sysLaunchTagBeenList.size() >= (refeshCount + 1) * 4) {
                    btnYanzhi.setSelected(false);
                    btnXinxiu.setSelected(false);
                    btnCaiyi.setSelected(false);
                    btnXinnan.setSelected(false);
                    btnYanzhi.setText(sysLaunchTagBeenList.get(refeshCount * 4).getName());
                    btnXinxiu.setText(sysLaunchTagBeenList.get(1 + refeshCount * 4).getName());
                    btnCaiyi.setText(sysLaunchTagBeenList.get(2 + refeshCount * 4).getName());
                    btnXinnan.setText(sysLaunchTagBeenList.get(3 + refeshCount * 4).getName());
                    refeshCount++;
                } else {
                    refeshCount--;
                    if (sysLaunchTagBeenList.size() >= (refeshCount * 4) && (refeshCount - 1 >= 0)) {
                        btnYanzhi.setSelected(false);
                        btnXinxiu.setSelected(false);
                        btnCaiyi.setSelected(false);
                        btnXinnan.setSelected(false);
                        btnYanzhi.setText(sysLaunchTagBeenList.get((refeshCount - 1) * 4).getName());
                        btnXinxiu.setText(sysLaunchTagBeenList.get(1 + (refeshCount - 1) * 4).getName());
                        btnCaiyi.setText(sysLaunchTagBeenList.get(2 + (refeshCount - 1) * 4).getName());
                        btnXinnan.setText(sysLaunchTagBeenList.get(3 + (refeshCount - 1) * 4).getName());
                    }
                }
                break;
            case R.id.btn_yanzhi:
                if ((refeshCount - 1) * 4 < sysLaunchTagBeenList.size() && sysLaunchTagBeenList.size() > 0) {
                    tag = sysLaunchTagBeenList.get((refeshCount - 1) * 4).getTag();
                    btnYanzhi.setSelected(true);
                    btnXinxiu.setSelected(false);
                    btnCaiyi.setSelected(false);
                    btnXinnan.setSelected(false);
                }
                break;
            case R.id.btn_xinxiu:
                if ((refeshCount - 1) * 4 < sysLaunchTagBeenList.size() && sysLaunchTagBeenList.size() > 0) {
                    tag = sysLaunchTagBeenList.get((refeshCount - 1) * 4 + 1).getTag();
                    btnYanzhi.setSelected(false);
                    btnXinxiu.setSelected(true);
                    btnCaiyi.setSelected(false);
                    btnXinnan.setSelected(false);
                }
                break;
            case R.id.btn_caiyi:
                if ((refeshCount - 1) * 4 < sysLaunchTagBeenList.size() && sysLaunchTagBeenList.size() > 0) {
                    tag = sysLaunchTagBeenList.get((refeshCount - 1) * 4 + 2).getTag();
                    btnYanzhi.setSelected(false);
                    btnXinxiu.setSelected(false);
                    btnCaiyi.setSelected(true);
                    btnXinnan.setSelected(false);
                }
                break;
            case R.id.btn_xinnan:
                if ((refeshCount - 1) * 4 < sysLaunchTagBeenList.size() && sysLaunchTagBeenList.size() > 0) {
                    tag = sysLaunchTagBeenList.get((refeshCount - 1) * 4 + 3).getTag();
                    btnYanzhi.setSelected(false);
                    btnXinxiu.setSelected(false);
                    btnCaiyi.setSelected(false);
                    btnXinnan.setSelected(true);
                }
                break;
            case R.id.btn_exit:
                MobclickAgent.onEvent(SWCameraStreamingActivity.this, "10132");
                finish();
                break;
            case R.id.btn_share_weibo:
                MobclickAgent.onEvent(SWCameraStreamingActivity.this, "10131");
                from = 2;
                mPresenter.share(from, SHARE_MEDIA.SINA);
                break;
            case R.id.btn_share_qzone:
                MobclickAgent.onEvent(SWCameraStreamingActivity.this, "10130");
                if (FHUtils.isAppInstalled(this, "com.tencent.mobileqq")) {
                    from = 2;
                    mPresenter.share(from, SHARE_MEDIA.QZONE);
                } else {
                    onToast("未安装QQ");
                }
                break;
            case R.id.btn_share_wx:
                MobclickAgent.onEvent(SWCameraStreamingActivity.this, "10127");
                if (FHUtils.isAppInstalled(this, "com.tencent.mm")) {
                    from = 2;
                    mPresenter.share(from, SHARE_MEDIA.WEIXIN);
                } else {
                    onToast("未安装微信");
                }
                break;
            case R.id.btn_share_wx_friends:
                MobclickAgent.onEvent(SWCameraStreamingActivity.this, "10128");
                if (FHUtils.isAppInstalled(this, "com.tencent.mm")) {
                    from = 2;
                    mPresenter.share(from, SHARE_MEDIA.WEIXIN_CIRCLE);
                } else {
                    onToast("未安装微信");
                }
                break;
            case R.id.btn_share_qq:
                MobclickAgent.onEvent(SWCameraStreamingActivity.this, "10129");
                if (FHUtils.isAppInstalled(this, "com.tencent.mobileqq")) {
                    from = 2;
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (checkPerssion()) {
                            mPresenter.share(from, SHARE_MEDIA.QQ);
                        }
                    } else {
                        mPresenter.share(from, SHARE_MEDIA.QQ);
                    }
                } else {
                    onToast("未安装QQ");
                }
                break;
            case R.id.tv_xieyi:
                Intent intent = new Intent(this, UserAgreement.class);
                intent.putExtra("url", mPresenter.getSysConfigBean().getCriterionUrl() + "?rand=" + System.currentTimeMillis() + "");
                intent.putExtra("title", "直播平台协议");
                startActivity(intent);
                break;
            case R.id.btn_begin:
                if (TextUtils.isEmpty(tag)) {
                    onToast("请选择标签");
                    return;
                }
                if (btnAgree.isChecked()) {
                    if (!TextUtils.isEmpty(title.getText())) {
                        if (isNetworkConnected()) {
                            hideKeyboard();
                            roomId = SharePreferenceUtil.getSession(SWCameraStreamingActivity.this, "PREF_KEY_USERID");
                            showLoading();
                            mPresenter.joinRoom(roomId, title.getText().toString(), Integer.valueOf(tag), "0", false);
                        } else {
                            onToast("网络连接失败，请检查您的网络");
                        }
                    } else {
                        onToast("请填写直播标题");
                    }
                } else {
                    onToast("您尚未勾选同意直播平台协议");
                }
                break;
            case R.id.switch_button:
                if (!isClick) {
                    //判断是否有权限
                    isClick = true;
                    switchButton.setSelected(true);
                    if (ContextCompat.checkSelfPermission(SWCameraStreamingActivity.this,
                            Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        //权限还没有授予，需要在这里写申请权限的代码
                        ActivityCompat.requestPermissions(SWCameraStreamingActivity.this,
                                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                1012);
                    } else {
                        mPresenter.dingwei();
                    }
                } else {
                    switchButton.setSelected(false);
                    isClick = false;
                    tvLocation.setText("");
                }
                break;
            default:
                break;

        }
    }

    private boolean checkPerssion() {
        if (ActivityCompat.checkSelfPermission(SWCameraStreamingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 需要弹出dialog让用户手动赋予权限
            ActivityCompat.requestPermissions(SWCameraStreamingActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1011);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1010) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) || !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {

            }
        } else if (requestCode == 1011) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPresenter.share(from, SHARE_MEDIA.QQ);
            } else {
                // Permission Denied
            }
            return;
        } else if (requestCode == 1012) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPresenter.dingwei();
            } else {
                // Permission Denied
            }
            return;
        } else if (requestCode == PermissionCheck.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {
            if (verifyPermissions(grantResults)) {
                mPresenter.startPublish(mPreviewView);
                mPresenter.initSPManager();
            } else {
                onToast("请允许权限");
            }
        }
    }

    private boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }
        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Activity getActivity() {
        return SWCameraStreamingActivity.this;
    }

    @Override
    public void setLocationText(String text) {
        tvLocation.setText(text);
    }

    boolean actBack = false;

    @Override
    public void onBackPressed() {
        actBack = true;
        showComfirmDialog(TCConstants.TIPS_MSG_STOP_PUSH, false);
    }

    @Override
    public void showNetChangeDialog() {
        SPManager.onPause();
        final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(getActivity());//提示弹窗
        rxDialogSureCancel.setContent(R.string.tip_4g);
        rxDialogSureCancel.setCancel("取消");
        rxDialogSureCancel.setSure("确定");
        rxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.quitRoom();
                rxDialogSureCancel.cancel();
            }
        });
        rxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPManager.startPushStream();
                rxDialogSureCancel.cancel();
            }
        });
        rxDialogSureCancel.setCanceledOnTouchOutside(false);
        rxDialogSureCancel.show();
    }

    @Override
    public void showBanDialog(String msg, String time) {
        final Dialog pickDialog2 = new Dialog(SWCameraStreamingActivity.this, R.style.user_dialog);
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
                finish();
                pickDialog2.dismiss();
            }
        });
        pickDialog2.setCanceledOnTouchOutside(false);
        pickDialog2.show();
    }

    @Override
    public void showOfflineGifts(List<StartLiveResponce.OfflineGiftsData> list) {
        final Dialog dialog = new Dialog(this, R.style.user_dialog);
        dialog.setContentView(R.layout.offline_gift);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = dialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();

        lp.width = (int) (display.getWidth() - TCUtils.dip2px(this, 50)); //设置宽度
        dlgwin.setGravity(Gravity.CENTER);
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);

        OffineGiftAdapter adapter = new OffineGiftAdapter(SWCameraStreamingActivity.this);
        recyclerView.setAdapter(adapter);

        adapter.setDatas(list, mPresenter.getSysConfigBean(), mPresenter);
        Button knowBtn = (Button) dialog.findViewById(R.id.know_btn);
        knowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    @Override
    public void initViewPager(JoinRoomResponce.JoinRoomData tcRoomInfo) {
        prepareView.setVisibility(View.GONE);
        chatFragment = ChatLiveFragment.getInstance(tcRoomInfo);
        chatFragment.setChatLiveFragmentListener(SWCameraStreamingActivity.this);
        chatFragment.setShareListener(SWCameraStreamingActivity.this);
        noFragment = new NoFragment();
        fragments.add(noFragment);
        fragments.add(chatFragment);
        MainFragmentPageAdapter myFragmentPagerAdapter = new MainFragmentPageAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(myFragmentPagerAdapter);
        viewPager.setCurrentItem(1);
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        viewPager.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean getmIsUserPushing() {
        return mIsUserPushing;
    }

    @Override
    public void setmIsUserPushing(boolean mIsUserPushing) {
        this.mIsUserPushing = mIsUserPushing;
    }

    @Override
    public void goMainActivity() {
        if (comfirmDialog != null) {
            comfirmDialog.dismiss();
        }
        startActivity(new Intent(SWCameraStreamingActivity.this, MainActivity.class));
        finish();
    }

    private long mSecond = 0;

    @Override
    public void showDetailDialog(Bundle args) {
        //确认则显示观看detail
        DetailDialogFragment dialogFragment = new DetailDialogFragment();
        mSecond = System.currentTimeMillis() - SharePreferenceUtil.getSessionLong(this, "live_time");
        args.putString("time", TCUtils.formattedTime(mSecond / 1000));
        dialogFragment.setArguments(args);
        dialogFragment.setCancelable(false);
        dialogFragment.show(getSupportFragmentManager(), "");
        dialogFragment.setOnCancelDialogListener(new DetailDialogFragment.OnCancelDialogListener() {
            @Override
            public void cancel() {
                startActivity(new Intent(SWCameraStreamingActivity.this, MainActivity.class));
                finish();
            }
        });
    }


    @Override
    public void closeLive() {
        actBack = true;
        showComfirmDialog(TCConstants.TIPS_MSG_STOP_PUSH, false);
    }

    @Override
    public void showBeautyDialog() {
        showBeautyPickDialog(SWCameraStreamingActivity.this);
    }


    public SPManager.FilterType mCurrentFilter = SPManager.FilterType.BEAUTYG;
    public int currentSelect = 0;

    public Dialog showBeautyPickDialog(final Activity activity) {
        Dialog pickDialog = new Dialog(activity, R.style.floag_dialog);
        pickDialog.setContentView(R.layout.beauty_pick_view);
//        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Translucent_Diglog);
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View rootView = inflater.inflate(R.layout.beauty_pick_view, null);
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog.getWindow();
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = (int) (display.getWidth()); //设置宽度
        pickDialog.getWindow().setAttributes(lp);
        final SeekBar beautySeekBar = (SeekBar) pickDialog.findViewById(R.id.beauty_level);
        final TextView beautyValueTv = (TextView) pickDialog.findViewById(R.id.beauty_level_value);
        PickerView pickerView = (PickerView) pickDialog.findViewById(R.id.pickerView);
        List<String> data = new ArrayList<String>();
        if (currentSelect == 0) {
            data.add("美颜平滑");
            data.add("美颜普通1");
            data.add("美白");
            data.add("美颜普通2");
        } else if (currentSelect == 1) {
            data.add("美颜普通2");
            data.add("美白");
            data.add("美颜平滑");
            data.add("美颜普通1");
        } else if (currentSelect == 2) {
            data.add("美白");
            data.add("美颜平滑");
            data.add("美颜普通1");
            data.add("美颜普通2");
        } else if (currentSelect == 3) {
            data.add("美颜平滑");
            data.add("美颜普通1");
            data.add("美颜普通2");
            data.add("美白");

        }

        pickerView.setData(data);

        pickerView.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                switch (text) {
                    case "美白":
                        mCurrentFilter = SPManager.FilterType.SKINWHITEN;
                        SPManager.switchFilter(mCurrentFilter);
                        currentSelect = 0;
                        break;
                    case "美颜平滑":
                        mCurrentFilter = SPManager.FilterType.BEAUTYB;
                        currentSelect = 1;
                        SPManager.switchFilter(mCurrentFilter);
                        break;
                    case "美颜普通1":
                        currentSelect = 2;
                        mCurrentFilter = SPManager.FilterType.BEAUTYG;
                        SPManager.switchFilter(mCurrentFilter);
                        break;
                    case "美颜普通2":
                        currentSelect = 3;
                        mCurrentFilter = SPManager.FilterType.BEAUTYG1;
                        SPManager.switchFilter(mCurrentFilter);
                        break;
                }
            }
        });
        beautySeekBar.setMax(10);//美颜参数范围0~10
        if (mCurrentFilter.getLevel() < 0) {
            beautySeekBar.setEnabled(false);
            beautySeekBar.setProgress(0);
            beautyValueTv.setText("" + 0);
        } else {
            beautySeekBar.setEnabled(true);
            beautySeekBar.setProgress(mCurrentFilter.getLevel());
            beautyValueTv.setText("" + mCurrentFilter.getLevel());
        }
        SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!fromUser) return;
                switch (seekBar.getId()) {
                    case R.id.beauty_level:
                        if (progress != mCurrentFilter.getLevel()) {
                            mCurrentFilter.setLevel(progress);
                            beautyValueTv.setText("" + progress);
                            SPManager.switchFilter(mCurrentFilter);
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        beautySeekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        // 设置宽度为屏宽, 靠近屏幕底部。
        pickDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (chatFragment != null) {
                    chatFragment.setGameIconVisual(true);
                }
            }
        });
        pickDialog.show();
        return pickDialog;
    }

    @Override
    public void showContribueDialog() {
        ContributionListDialogFragment dialogFragment = new ContributionListDialogFragment();

        Bundle bundle = new Bundle();
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getSupportFragmentManager(), "");
    }

    private RoomMrgsDialogFragment roomMrgsDialogFragment;

    @Override
    public void showRoomMrgsDialog() {
        roomMrgsDialogFragment = RoomMrgsDialogFragment.newInstance(mPresenter);
        roomMrgsDialogFragment.setOndissMissListener(new RoomMrgsDialogFragment.OndissMissListener() {
            @Override
            public void dissmiss() {
                if (chatFragment != null) {
                    chatFragment.setGameIconVisual(true);
                }
            }
        });
        roomMrgsDialogFragment.show(getFragmentManager(), "");
    }

    @Override
    public void cancelRoomMgrState(boolean isSuccess, int pos) {
        if (roomMrgsDialogFragment != null) {
            roomMrgsDialogFragment.cancelRoomMgr(isSuccess, pos);
        }
    }

    @Override
    public void notifyRoomMrgList(List<GetRoomMrgsResponce.RoomMrgData> datas) {
        if (roomMrgsDialogFragment != null) {
            roomMrgsDialogFragment.notifyRoomMrgList(datas);
        }
    }

    @Override
    public void openMusicFile() {
        if (chatFragment != null) {
            chatFragment.setMusicPause("停止");
        }
    }

    @Override
    public void socketDisconnect() {

    }

    @Override
    public void socketReconnect() {

    }

    @Override
    public void openFlashLight(boolean isOpen) {
        SPManager.flashCamera(isOpen ? SPManager.SWITCH_ON : SPManager.SWITCH_OFF);
    }

    @Override
    public void changeCamera(boolean isChange) {
        mPresenter.changCamera();

    }

    @Override
    public void openMusic(String filePath) {

    }

    @Override
    public void stopMusic() {
        mPresenter.stopMusic(chatFragment);
    }

    @Override
    public void micoreMusic() {
        mPresenter.micoreMusic(chatFragment);
    }

    @Override
    public void musicProcess(SeekBar seekBar) {

    }

    @Override
    public void volumeProcess(SeekBar seekBar) {
        SPManager.setBgmVolume(1.0f * seekBar.getProgress() / seekBar.getMax());
    }

    @Override
    public void successQuiteRoom() {
        stopPublish();
        mPresenter.quitRoom();
    }

    @Override
    public void openFile() {
        mPresenter.openFile();
    }

    @Override
    public void liveError() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.show();
        alertDialog.getWindow().setContentView(R.layout.quit_live_error);
        alertDialog.setCanceledOnTouchOutside(false);
        TextView tvOk = (TextView) alertDialog.getWindow().findViewById(R.id.tv_ok);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.quitRoom();
            }
        });
    }

    @Override
    public void micProgress(SeekBar seekBar) {
        SPManager.setMicVolume(1.0f * seekBar.getProgress() / seekBar.getMax());
    }

    @Override
    public void qqShare() {
        from = 1;
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPerssion()) {
                mPresenter.share(from, SHARE_MEDIA.QQ);
            }
        } else {
            mPresenter.share(from, SHARE_MEDIA.QQ);
        }
    }

    @Override
    public void qqzoneShare() {
        from = 1;
        mPresenter.share(from, SHARE_MEDIA.QZONE);

    }

    @Override
    public void wxShare() {
        from = 1;
        mPresenter.share(from, SHARE_MEDIA.WEIXIN);

    }

    @Override
    public void wbShare() {
        from = 1;
        mPresenter.share(from, SHARE_MEDIA.SINA);
    }

    @Override
    public void wxcircleShare() {
        from = 1;
        mPresenter.share(from, SHARE_MEDIA.WEIXIN_CIRCLE);
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
        }

    }

    private SysGiftNewBean getGiftBeanByID(String id) {
        return mPresenter.getGiftBeanByID(id);
    }

    private void stopPublish() {
        SPManager.stopPushStream();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SPManager.onResume();
        SPManager.PushState pushState = SPManager.getPushState();
        boolean isMute = pushState.isMute;
        SPManager.muteMic(SPManager.SWITCH_OFF);
        mPresenter.switchAppFocus(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SPManager.PushState pushState = SPManager.getPushState();
        boolean isMute = pushState.isMute;
        Log.e("print", "onResume: ---->" + isMute);
        SPManager.muteMic(SPManager.SWITCH_ON);
        SPManager.onPause();
        mPresenter.switchAppFocus(false);
    }

    @Override
    protected void onStop() {
        // 为保证时序，只有当前Activity才调用SPManager接口
        if (mCurActivityHashCode == this.hashCode()) {
            if (mIsUserPushing) {
                SPManager.stopPushStream();
            }
            SPManager.onPause();
        }

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        // 为保证时序，只有当前Activity才调用SPManager接口
        if (mCurActivityHashCode == this.hashCode()) {
            SPManager.release();
        }
        SPManager.stopBgm();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SPManager.onResume();
        if (mIsUserPushing) {
            SPManager.startPushStream();
        }
    }

    RxDialogSureCancel comfirmDialog;

    public void showComfirmDialog(String msg, final Boolean isError) {
        comfirmDialog = new RxDialogSureCancel(getActivity());//提示弹窗
        comfirmDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return true;
            }
        });
        comfirmDialog.setContent(msg);
        comfirmDialog.setCancel("取消");
        comfirmDialog.setSure("确定");
        comfirmDialog.getTvSure().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPublish();
                mPresenter.quitRoom();
                comfirmDialog.cancel();
            }
        });
        if (isError) {
            comfirmDialog.getTvCancel().setVisibility(View.GONE);
        }
        comfirmDialog.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                comfirmDialog.cancel();
            }
        });
        comfirmDialog.setCanceledOnTouchOutside(false);
        comfirmDialog.show();

    }

    private int socketConnectCount = 0;

    ///socket断掉
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_SOCKET_CONNECTION_INTERRUPT_ERROR, threadMode = ThreadMode.MAIN)
    public void onReceiveSocketDissConnectPush(SocketConnectError socketConnectError) {
        onToast("socket断掉");
        actBack = true;
        socketConnectCount++;
        if (socketConnectCount == 4) {
            showComfirmDialog(TCConstants.ERROR_MSG_NET_DISCONNECTED, true);
            socketConnectCount = 0;
        }
    }

    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_SOCKET_RECONNECT_SUCCEED, threadMode = ThreadMode.MAIN)
    public void onReceiveSocketConnectPush(SocketConnectError socketConnectError) {
        onToast("socket重新连接成功");
        socketConnectCount = 0;
        reStartLive();
    }

    //切换网络
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_NOTICE_NETWORK_CHANGE, threadMode = ThreadMode.MAIN)
    public void onReceiveNetworkChangePush(NetworkChangePush push) {
        if (push.getCode() == 0) {
            showNetChangeDialog();
        } else if (push.getCode() == -1) {
            showComfirmDialog("无网络，确定退出直播间", true);
        }
    }

    private String content;
    private String imgUrl;
    private String targetUrl;

    @RxBusSubscribe(code = RxBusCode.RX_BUS_NOTICE_CODE_SHARE, threadMode = ThreadMode.MAIN)
    public void onReceiveShare(JSONObject pushdata) {
        try {
            content = pushdata.getString("content");
            imgUrl = pushdata.getString("imgUrl");
            targetUrl = pushdata.getString("targetUrl");
            showLiveShareDialog();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void reStartLive() {
        mPresenter.joinRoom(roomId, title.getText().toString(), Integer.valueOf(tag), "1", true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    private void showLiveShareDialog() {
        final Dialog pickDialog3 = new Dialog(this, R.style.floag_dialog);
        pickDialog3.setContentView(R.layout.start_live_share);

        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog3.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = (int) (display.getWidth()); //设置宽度
        pickDialog3.getWindow().setAttributes(lp);
        pickDialog3.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

            }
        });
        ImageView tv_share_friends = (ImageView) pickDialog3.findViewById(R.id.tv_share_friends);
        ImageView tv_share_weibo = (ImageView) pickDialog3.findViewById(R.id.tv_share_weibo);
        ImageView tv_share_wechat = (ImageView) pickDialog3.findViewById(R.id.tv_share_wechat);
        ImageView tv_share_qzone = (ImageView) pickDialog3.findViewById(R.id.tv_share_qzone);
        ImageView tv_share_qq = (ImageView) pickDialog3.findViewById(R.id.tv_share_qq);
        tv_share_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share(SHARE_MEDIA.WEIXIN_CIRCLE);
            }
        });
        tv_share_weibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share(SHARE_MEDIA.SINA);
            }
        });
        tv_share_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share(SHARE_MEDIA.WEIXIN);
            }
        });
        tv_share_qzone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share(SHARE_MEDIA.QZONE);
            }
        });
        tv_share_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPerssion()) {
                        share(SHARE_MEDIA.QQ);
                    }
                } else {
                    share(SHARE_MEDIA.QQ);
                }
            }
        });
        pickDialog3.show();
    }

    private void share(SHARE_MEDIA plat) {
        ShareUtil.ShareWeb(this, content, "飞虎直播",
                imgUrl, targetUrl, plat, umShareListener);
    }

    UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
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
}
