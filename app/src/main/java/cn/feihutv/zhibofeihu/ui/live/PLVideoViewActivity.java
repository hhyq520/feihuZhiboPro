package cn.feihutv.zhibofeihu.ui.live;

import android.Manifest;
import android.app.Dialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chinanetcenter.StreamPusher.sdk.SPManager;
import com.cnc.mediaplayer.sdk.lib.event.AuthEvent;
import com.cnc.mediaplayer.sdk.lib.event.GeneralEvent;
import com.cnc.mediaplayer.sdk.lib.event.OnStatusCodeEventListener;
import com.cnc.mediaplayer.sdk.lib.event.PlayEvent;
import com.cnc.mediaplayer.sdk.lib.renderview.IRenderView;
import com.cnc.mediaplayer.sdk.lib.videoview.IMediaEventsListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.SoftReference;
import java.util.ArrayList;

import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;


import cn.feihutv.zhibofeihu.data.network.socket.model.common.NetworkChangePush;
import cn.feihutv.zhibofeihu.data.network.socket.model.common.SocketConnectError;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.JoinRoomResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.UpdatePlayStatusPush;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.rxbus.RxBusSubscribe;
import cn.feihutv.zhibofeihu.rxbus.ThreadMode;
import cn.feihutv.zhibofeihu.ui.live.PLVideoViewMvpView;
import cn.feihutv.zhibofeihu.ui.live.PLVideoViewMvpPresenter;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.ui.live.adapter.MainFragmentPageAdapter;
import cn.feihutv.zhibofeihu.ui.widget.CNCVideoViewEx;
import cn.feihutv.zhibofeihu.ui.widget.GuardDialogFragment;
import cn.feihutv.zhibofeihu.ui.widget.dialog.ContributionListDialogFragment;
import cn.feihutv.zhibofeihu.ui.widget.flashview.FlashDataParser;
import cn.feihutv.zhibofeihu.ui.widget.flashview.FlashView;
import cn.feihutv.zhibofeihu.utils.AlterWindowUtil;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.FastBlur;
import cn.feihutv.zhibofeihu.utils.FloatViewWindowManger;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import cn.feihutv.zhibofeihu.utils.ShareUtil;
import cn.feihutv.zhibofeihu.utils.TCConstants;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.feihutv.zhibofeihu.utils.weiget.dialog.RxDialogSureCancel;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * <pre>
 *     author : huang hao
 *     time   :
 *     desc   : 界面绘制
 *     version: 1.0
 * </pre>
 */
public class PLVideoViewActivity extends BaseActivity implements PLVideoViewMvpView, OnStatusCodeEventListener, IMediaEventsListener {

    @Inject
    PLVideoViewMvpPresenter<PLVideoViewMvpView> mPresenter;

    private CNCVideoViewEx videoView;
    private RelativeLayout mLoadingView;
    private FlashView loadingView;
    private View plView;
    private ImageView coverView;
    private ImageView pauseBg;
    private ImageView iconClose;
    private ViewPager viewPager;
    private FrameLayout pauseFrmbg;
    private String roomId;
    private String headUrl;
    private JoinRoomResponce.JoinRoomData roomInfo;
    private String mPlayUrl = "";
    private int mCurrentMemberCount;
    protected final int CONNECTION_INIT = -2;
    protected int mLastConnectionType = CONNECTION_INIT;
    /**
     * 标识网络已恢复
     */
    protected boolean mConnectionRecover;
    protected boolean mConnectionError=false;
    private boolean isGood = false;
    private Handler mOnStatusCodeEventHandler;
    private ArrayList<Fragment> fragments;
    private ChatPlayFragment chatFragment;
    private NoFragment noFragment;
    public static int OVERLAY_PERMISSION_REQ_CODE = 1234;
    private boolean showWindow = true;
    private boolean mIsActivityPaused = true;
    private boolean isNeedGuard=false;
    @Override
    protected void beforeSet() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_plvideo_view;
    }


    @Override
    protected void initWidget() {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this));

        //persenter调用初始化
        mPresenter.onAttach(PLVideoViewActivity.this);

        initView();

        Intent intent = getIntent();
        roomId = intent.getStringExtra("room_id");
        headUrl = intent.getStringExtra("headUrl");
        isNeedGuard=intent.getBooleanExtra("isNeedGuard",false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                RequestBuilder<Bitmap> requestBuilder = Glide.with(this).asBitmap().apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE).transform(new FastBlur(PLVideoViewActivity.this, 15)));
                requestBuilder.load(headUrl).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        try {
                            int width = resource.getWidth();
                            int height = resource.getHeight();
                            float rate = (float) getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().heightPixels;
                            int wTargrt = (int) (height * rate);
                            int edge = (width - wTargrt) / 2;
                            Bitmap bitmap = Bitmap.createBitmap(resource, edge, 0, wTargrt, height);
                            coverView.setImageBitmap(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        }else{
            Glide.with(this).load(headUrl)
                    .apply(new RequestOptions().dontAnimate().transform(new BlurTransformation(this, 1, 5)).placeholder(R.drawable.cover).diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .into(coverView);
        }

        loadingView.setVisibility(View.VISIBLE);
        loadingView.reload("loading", "flashAnims");
        loadingView.play("loading", FlashDataParser.FlashLoopTimeForever);
        loadingView.setScale(0.25f, 0.25f);

        if (!isNetworkConnected()) {
            onToast("当前无网络连接，请检查网络！");
            return;
        }

        if (roomId != null) {
            mPresenter.joinRoom(roomId, "0",true);
        }

    }

    @Override
    public void joinRoomRespoce(JoinRoomResponce responce) {
        if (responce.getCode() == 0) {
            FeihuZhiboApplication.getApplication().mDataManager.saveXiaolabaCount(responce.getmJoinRoomData().getXiaolaba());
            JoinRoomResponce.JoinRoomData joinRoomData = responce.getmJoinRoomData();
            if (joinRoomData != null && joinRoomData.getRoomId().equals(roomId)) {
                initSdk(joinRoomData);
                // 加入观看历史数据库
                mPresenter.saveHistory(joinRoomData);
                AppLogger.e("joinroom","加入房间成功");
                if(isNeedGuard){
                    GuardDialogFragment dialogFragment = GuardDialogFragment.newInstance(joinRoomData.getMasterDataList().getHeadUrl(),
                            joinRoomData.getMasterDataList().getNickName(), joinRoomData.getRoomId(), joinRoomData.getMasterDataList().getAccountId());
                    dialogFragment.show(getSupportFragmentManager(), "");
                }
            } else {
                onToast("获取直播间信息失败！");
                finish();
            }
        } else {
            if (responce.getCode() == 4604) {
                String desc = responce.getErrExtMsgData().getDesc();
                String duration = responce.getErrExtMsgData().getDuration();
                showBanDialog(desc, duration);
            }
            AppLogger.e("joinroom",responce.getErrMsg());
        }
    }


    public void showBanDialog(String msg, String time) {
        final Dialog pickDialog2 = new Dialog(this, R.style.floag_dialog);
        pickDialog2.setContentView(R.layout.pop_ban_zhibo);

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog2.getWindow();
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.CENTER);
        lp.width = (int) (display.getWidth()); //设置宽度
        pickDialog2.getWindow().setAttributes(lp);

        TextView tvContent = (TextView) pickDialog2.findViewById(R.id.tv_pop);
        String str = "<font color='#5d5d5d'>对不起,您因</font>" +
                "<font color='#5d5d5d'>[</font>" + msg +
                "<font color='#5d5d5d'>]</font>" +
                "<font color='#5d5d5d'>被踢出直播间,时长为</font>" +
                time + "分钟" +
                "<font color='#5d5d5d'>.</font>";

        tvContent.setText(Html.fromHtml(str));

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

    // 初始化view
    private void initView() {

        videoView = (CNCVideoViewEx) findViewById(R.id.video_view);

        mLoadingView = (RelativeLayout) findViewById(R.id.LoadingView);

        loadingView = (FlashView) findViewById(R.id.loading_view);

        iconClose=(ImageView) findViewById(R.id.img_close);
        plView = findViewById(R.id.pl_view);

        viewPager = (ViewPager) findViewById(R.id.view_pager);

        coverView = (ImageView) findViewById(R.id.CoverView);

        pauseFrmbg=(FrameLayout) findViewById(R.id.pause_bg_frm);
        pauseBg = (ImageView) findViewById(R.id.pause_bg);

    }

    private void initSdk(final JoinRoomResponce.JoinRoomData roomInfo) {
        this.roomInfo=roomInfo;
        mPlayUrl = roomInfo.getPlayUrl();
        mCurrentMemberCount = roomInfo.getOnlineUserCnt();

        initHandler();
        videoView.setAspectRatio(IRenderView.AR_ASPECT_FILL_PARENT);
        videoView.play(mPlayUrl);
        iconClose.setVisibility(View.GONE);
        videoView.setOnStatusCodeEventListener(this);
        videoView.setOnMediaEventsListener(this);
        fragments = new ArrayList<>();
        chatFragment = ChatPlayFragment.getInstance(roomInfo);
        chatFragment.setChatPlayFragmentListener(new ChatPlayFragment.ChatPlayFragmentListener() {
            @Override
            public void back() {
                Intent rstData = new Intent();
                int memberCount = mCurrentMemberCount - 1;
                rstData.putExtra(TCConstants.MEMBER_COUNT, memberCount >= 0 ? memberCount : 0);
                rstData.putExtra(TCConstants.ROOM_ID, roomInfo.getRoomId());
                setResult(0, rstData);
                if (Build.VERSION.SDK_INT >= 23) {
                    askForPermission();
                } else {
                    if (AlterWindowUtil.checkAlertWindowsPermission(PLVideoViewActivity.this) && isGood && showWindow) {
                        showVideoWindow();
                    } else {
                        quitroom();
                    }
                    finish();
                }
            }

            @Override
            public void onfresh() {
                stopPlay(true);
                if (!TextUtils.isEmpty(mPlayUrl)) {
                    videoView.play(mPlayUrl);
                }
            }

            @Override
            public void showContribueDialog() {
                showDetailContribution();
            }

            @Override
            public void uadatePlayUrl(String playurl, boolean roomstate) {
                if (roomstate && !TextUtils.isEmpty(playurl)) {
                    mPlayUrl = playurl;
                    videoView.play(mPlayUrl);
                } else {
                }
            }

            @Override
            public void showPausePicture(boolean state) {

                if (state) {
                    pauseFrmbg.setVisibility(View.GONE);
                } else {
                    pauseFrmbg.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void hideProessBar() {
                if (loadingView != null) {
                    loadingView.setVisibility(View.GONE);
                    iconClose.setVisibility(View.GONE);
                }
            }

            @Override
            public void socketDisconnect() {

            }

            @Override
            public void socketReconnect() {

            }
        });

        chatFragment.setShareListener(new ChatPlayFragment.PlayShareListener() {
            @Override
            public void qqShare() {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPerssion()) {
                        share(SHARE_MEDIA.QQ);
                    }
                } else {
                    share(SHARE_MEDIA.QQ);
                }
            }

            @Override
            public void qqzoneShare() {
                share(SHARE_MEDIA.QZONE);
            }

            @Override
            public void wxShare() {
                share(SHARE_MEDIA.WEIXIN);
            }

            @Override
            public void wbShare() {
                share(SHARE_MEDIA.SINA);
            }

            @Override
            public void wxcircleShare() {
                share(SHARE_MEDIA.WEIXIN_CIRCLE);
            }
        });

        noFragment = new NoFragment();
        fragments.add(noFragment);
        fragments.add(chatFragment);
        MainFragmentPageAdapter myFragmentPagerAdapter = new MainFragmentPageAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(myFragmentPagerAdapter);
        viewPager.setCurrentItem(1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        SoftReference<PhoneStateListener> softListener = new SoftReference<PhoneStateListener>(listener);

        TelephonyManager tm = (TelephonyManager) this.getApplicationContext().getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(softListener.get(), PhoneStateListener.LISTEN_CALL_STATE);

        if (TextUtils.isEmpty(mPlayUrl) && chatFragment != null) {
            chatFragment.initRecommendView(true);
        }

        iconClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private boolean checkPerssion() {
        if (ActivityCompat.checkSelfPermission(PLVideoViewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 需要弹出dialog让用户手动赋予权限
            ActivityCompat.requestPermissions(PLVideoViewActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1010);
            return false;
        } else {
            return true;
        }
    }

    private void showDetailContribution() {
        ContributionListDialogFragment dialogFragment = new ContributionListDialogFragment();
        Bundle bundle = new Bundle();
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getSupportFragmentManager(), "");
    }

    private void share(SHARE_MEDIA plat) {
        // TODO: 2017/7/3
        showLoading();
        if (roomInfo != null && roomInfo.getMasterDataList() != null) {
            String headUrl = roomInfo.getMasterDataList().getHeadUrl();
            String nickName = roomInfo.getMasterDataList().getNickName();
            String accountId = roomInfo.getRoomId();
            String title = roomInfo.getMasterDataList().getRoomName();
            if (TextUtils.isEmpty(nickName)) {
                nickName = "";
            }
            ShareUtil.ShareWeb(this, "精彩就不间断，主播带你嗨，还有幸运礼包等你拿，赶快来看看！", nickName + "【" + title + "】,火热直播中！",
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
            mPresenter.logShare(1, to);
            onToast("分享成功");
            hideLoading();
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            hideLoading();
            onToast("分享失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            hideLoading();
        }
    };

    PhoneStateListener listener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                //电话等待接听
                case TelephonyManager.CALL_STATE_RINGING:
                    if (videoView != null) {
                        videoView.onPause();
                    }
                    break;
                //电话接听
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (videoView != null) {
                        videoView.onPause();
                    }
                    break;
                //电话挂机
                case TelephonyManager.CALL_STATE_IDLE:
                    if (videoView != null) {
                        videoView.start();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void initHandler() {
        mOnStatusCodeEventHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case GeneralEvent.PARAMETER_ERROR:
                        AppLogger.e("handleMessage: ---->输入参数错误");
                        break;
                    case GeneralEvent.SDK_INIT_SUCCESS:
                        AppLogger.e("handleMessage: ---->SDK初始化成功");
                        break;
                    case GeneralEvent.SDK_INIT_FAILED:
                        AppLogger.e("handleMessage: ---->SDK初始化失败");
                        break;
                    case AuthEvent.AUTHORIZING:
                        AppLogger.e("handleMessage: ---->正在鉴权中");
                        break;
                    case AuthEvent.SDK_EXPIRED:
                        AppLogger.e("handleMessage: ---->过期");
                        break;
                    case AuthEvent.VERSION_OLD:
                        AppLogger.e("handleMessage: ---->SDK版本过低");
                        break;
                    case AuthEvent.SDK_UNMATCHED:
                        AppLogger.e("handleMessage: ---->SDK类型不匹配");
                        break;
                    case AuthEvent.APPID_UNMATCHED:
                        AppLogger.e("handleMessage: ---->AppID类型不匹配");
                        break;
                    case AuthEvent.APPKEY_UNMATCHED:
                        AppLogger.e("handleMessage: ---->authKey 不匹配");
                        break;
                    case AuthEvent.RESP_ERROR:
                        AppLogger.e("handleMessage: ---->鉴权服务器响应错误");
                        break;
                    case AuthEvent.UNKNOWN_ERROR:
                        AppLogger.e("handleMessage: ---->未知鉴权错误");
                        break;
                    case GeneralEvent.POOR_NETWORK_CONDITION:
                        AppLogger.e("handleMessage: ---->网络环境差");
                        onToast("网络环境差");
                        break;
                    case 5401:
                        isGood = true;
                        loadingView.setVisibility(View.GONE);
                        coverView.setImageBitmap(null);
                        coverView.setVisibility(View.GONE);
                        iconClose.setVisibility(View.GONE);
                        plView.setVisibility(View.GONE);
                        AppLogger.e("handleMessage: ---->请求成功");
                        break;
                    default:
                        AppLogger.e("handleMessage: ---->" + msg.what + "  " + msg.obj.toString());
                        break;
                }
            }
        };
    }

    @Override
    protected void onDestroy() {
        //销毁之前必须调用

        if (loadingView != null) {
            loadingView.stop();
        }
        if (videoView != null) {
            videoView.onDestroy();
        }
        if (mOnStatusCodeEventHandler != null) {
            mOnStatusCodeEventHandler.removeCallbacksAndMessages(null);
            mOnStatusCodeEventHandler = null;
        }
        mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsActivityPaused = false;
        if (videoView != null) {
            videoView.onResume();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (videoView != null) {
            videoView.onStart();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIsActivityPaused = true;
        stopPlay(false);
    }


    @Override
    public void onStatusCodeCallback(int what, String extra) {
        if (mOnStatusCodeEventHandler != null) {
            mOnStatusCodeEventHandler.obtainMessage(what, extra).sendToTarget();
        }
    }

    @Override
    public void onMediaPause() {
        AppLogger.e("onMediaPause");
    }

    @Override
    public void onMediaStart() {
        loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBufferingStart() {
        loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBufferingEnd() {

    }

    @Override
    public void onMediaCompletion() {
        loadingView.setVisibility(View.GONE);
        iconClose.setVisibility(View.GONE);
    }

    @Override
    public void onMediaPrepared() {

    }

    @Override
    public void onMediaError(int i, int i1) {

    }

    @Override
    public void onMediaInfo(int what, int extra) {
        String errorText = "[" + extra + "] " + PlayEvent.getErrorText(extra);
        AppLogger.e("onMediaError: " + what + "," + extra + "。错误说明：" + errorText);
    }


    /**
     * 观众退出直播间
     */
    private void quitroom() {
        mPresenter.leaveRoom(roomId);
    }

    /**
     * 退出时，离开直播间
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent rstData = new Intent();
        int memberCount = mCurrentMemberCount - 1;
        rstData.putExtra(TCConstants.MEMBER_COUNT, memberCount >= 0 ? memberCount : 0);
        rstData.putExtra(TCConstants.ROOM_ID, roomId);
        setResult(0, rstData);
        if (Build.VERSION.SDK_INT >= 23) {
            askForPermission();
        } else {
            if (AlterWindowUtil.checkAlertWindowsPermission(this) && isGood && showWindow) {
                showVideoWindow();
            } else {
                quitroom();
            }
            finish();
        }
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                finish();
            } else {
                if (isGood) {
                    showVideoWindow();
                }
                finish();
            }

        }
    }

    private void showVideoWindow() {
        if (!SharePreferenceUtil.getSessionBoolean(this, "xuanfuchuan", true)) {
            quitroom();
            return;
        }
        stopPlay(true);
        FloatViewWindowManger.createSmallWindow(getApplicationContext(), mPlayUrl, roomId, headUrl, 2);
    }

    protected void stopPlay(boolean clearLastFrame) {
        if (videoView != null) {
            videoView.onPause();
        }
    }

    /**
     * 请求用户给予悬浮窗的权限
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void askForPermission() {
        if (!Settings.canDrawOverlays(this)) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
                quitroom();
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
            quitroom();
            finish();
        } else {
            if (isGood && showWindow) {
                showVideoWindow();
            }else {
                quitroom();
            }
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == 1010) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                share(SHARE_MEDIA.QQ);
            } else {
                // Permission Denied
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }






    /**
     * 重连
     */
    protected void reconnect(int delayMilis) {
        mOnStatusCodeEventHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (videoView == null) {
                    return;
                }
                mConnectionRecover = false;
                videoView.reconnect();
            }
        }, delayMilis);
    }

    /**
     * 显示确认消息
     *
     * @param msg     消息内容
     * @param isError true错误消息（必须退出） false提示消息（可选择是否退出）
     */

    RxDialogSureCancel comfirmDialog;
    public void showComfirmDialog(String msg, final Boolean isError) {
        comfirmDialog = new RxDialogSureCancel(this);//提示弹窗
        comfirmDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return true;
            }
        });
        comfirmDialog.setContent(msg);
        comfirmDialog.setCancel("取消");
        comfirmDialog.setSure("确定");

        if (!isError) {
            comfirmDialog.getTvSure().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    comfirmDialog.dismiss();
                    Intent rstData = new Intent();
                    int memberCount = mCurrentMemberCount - 1;
                    rstData.putExtra(TCConstants.MEMBER_COUNT, memberCount >= 0 ? memberCount : 0);
                    rstData.putExtra(TCConstants.ROOM_ID, roomId);
                    setResult(0, rstData);
                    quitroom();
                    finish();
                }
            });
            comfirmDialog.getTvCancel().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    comfirmDialog.dismiss();
                }
            });
        } else {
            comfirmDialog.getTvSure().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //当情况为错误的时候，直接停止拉流
                    Intent rstData = new Intent();
                    int memberCount = mCurrentMemberCount - 1;
                    rstData.putExtra(TCConstants.MEMBER_COUNT, memberCount >= 0 ? memberCount : 0);
                    rstData.putExtra(TCConstants.ROOM_ID, roomId);
                    setResult(0, rstData);
                    quitroom();
                    finish();
                    comfirmDialog.dismiss();
                }
            });
            comfirmDialog.getTvCancel().setVisibility(View.GONE);
        }
        comfirmDialog.setCanceledOnTouchOutside(false);
        comfirmDialog.show();

    }


    private int socketConnectCount = 0;
    ///socket断掉
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_SOCKET_CONNECTION_INTERRUPT_ERROR,threadMode = ThreadMode.MAIN)
    public void onReceiveSocketDissConnectPush(SocketConnectError socketConnectError){
        socketConnectCount++;
        if (socketConnectCount == 4) {
            showComfirmDialog(TCConstants.ERROR_MSG_NET_DISCONNECTED, true);
        }
    }

    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_SOCKET_RECONNECT_SUCCEED,threadMode = ThreadMode.MAIN)
    public void onReceiveSocketConnectPush(SocketConnectError socketConnectError){
        socketConnectCount = 0;
        if (roomId != null) {
            mPresenter.joinRoom(roomId,"0",false);
        }
    }

    //切换网络
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_NOTICE_NETWORK_CHANGE,threadMode = ThreadMode.MAIN)
    public void onReceiveNetworkChangePush(NetworkChangePush push){

       if(push.getCode()==-1){
           mConnectionError = true;
           showComfirmDialog("无网络，确定退出直播间",true);
       }else{
           if(push.getCode()==0){
               showNetChangeDialog();
           }
           //网络从错误中恢复，或者网络连接有变化，即做重连准备
           if (mConnectionError || mLastConnectionType != CONNECTION_INIT) {
               mConnectionError = false;
               mConnectionRecover = true;
               if (videoView != null) {
                   onToast("网络恢复，1s后尝试重连...");
                   reconnect(1000);
               }
           }
           mLastConnectionType = push.getCode();
       }
    }

    //直播状态发生变化
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_UPDATE_PLAY_STATUS, threadMode = ThreadMode.MAIN)
    public void onReceiveUpdatePlayStatusPush(UpdatePlayStatusPush pushData) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            RequestBuilder<Bitmap> requestBuilder = Glide.with(this).asBitmap().apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE).transform(new FastBlur(PLVideoViewActivity.this, 15)));
            requestBuilder.load(headUrl).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    try {
                        int width = resource.getWidth();
                        int height = resource.getHeight();
                        float rate = (float) getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().heightPixels;
                        int wTargrt = (int) (height * rate);
                        int edge = (width - wTargrt) / 2;
                        Bitmap bitmap = Bitmap.createBitmap(resource, edge, 0, wTargrt, height);
                        pauseBg.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }else{
            Glide.with(this).load(headUrl)
                    .apply(new RequestOptions().dontAnimate().transform(new BlurTransformation(this, 1, 15)).placeholder(R.drawable.cover).diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .into(pauseBg);
        }

        if(pushData.isPlayStatus()){
            pauseFrmbg.setVisibility(View.GONE);
        }else{
            pauseFrmbg.setVisibility(View.VISIBLE);
        }
    }

    public void showNetChangeDialog() {
        SPManager.onPause();
        final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(PLVideoViewActivity.this);//提示弹窗
        rxDialogSureCancel.setContent(R.string.tip_4g);
        rxDialogSureCancel.setCancel("取消");
        rxDialogSureCancel.setSure("确定");
        rxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitroom();
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

    private String content;
    private String imgUrl;
    private String targetUrl;
    @RxBusSubscribe(code = RxBusCode.RX_BUS_NOTICE_CODE_SHARE,threadMode = ThreadMode.MAIN)
    public void onReceiveShare(JSONObject pushdata){
        try {
            content = pushdata.getString("content");
            imgUrl = pushdata.getString("imgUrl");
            targetUrl = pushdata.getString("targetUrl");
            showLiveShareDialog();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showLiveShareDialog() {
        final Dialog pickDialog3 = new Dialog(this, R.style.floag_dialog);
        pickDialog3.setContentView(R.layout.start_live_share);

        WindowManager windowManager =getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog3.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = (int) (display.getWidth()); //设置宽度
        pickDialog3.getWindow().setAttributes(lp);

        ImageView tv_share_friends = (ImageView) pickDialog3.findViewById(R.id.tv_share_friends);
        ImageView tv_share_weibo = (ImageView) pickDialog3.findViewById(R.id.tv_share_weibo);
        ImageView tv_share_wechat = (ImageView) pickDialog3.findViewById(R.id.tv_share_wechat);
        ImageView tv_share_qzone = (ImageView) pickDialog3.findViewById(R.id.tv_share_qzone);
        ImageView tv_share_qq = (ImageView) pickDialog3.findViewById(R.id.tv_share_qq);
        tv_share_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share1(SHARE_MEDIA.WEIXIN_CIRCLE);
            }
        });
        tv_share_weibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share1(SHARE_MEDIA.SINA);
            }
        });
        tv_share_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share1(SHARE_MEDIA.WEIXIN);
            }
        });
        tv_share_qzone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share1(SHARE_MEDIA.QZONE);
            }
        });
        tv_share_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPerssion()) {
                        share1(SHARE_MEDIA.QQ);
                    }
                } else {
                    share1(SHARE_MEDIA.QQ);
                }
            }
        });
        pickDialog3.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
            }
        });
        pickDialog3.show();
    }


    private void share1(SHARE_MEDIA plat) {
        ShareUtil.ShareWeb(this, content, "飞虎直播",
                imgUrl, targetUrl, plat, umShareListener1);
    }

    UMShareListener umShareListener1 = new UMShareListener() {
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
