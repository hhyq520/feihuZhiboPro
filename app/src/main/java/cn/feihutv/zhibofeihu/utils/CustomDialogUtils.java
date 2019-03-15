package cn.feihutv.zhibofeihu.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import cn.feihutv.zhibofeihu.BuildConfig;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LogDeviceRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LogDeviceResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.user.LoginRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.user.LoginResponse;
import cn.feihutv.zhibofeihu.data.network.socket.xsocket.FHSocket;
import cn.feihutv.zhibofeihu.data.network.socket.xsocket.IFHCallBack;
import cn.feihutv.zhibofeihu.rxbus.RxBus;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.user.phoneLogin.PhoneLoginActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by chenliwen on 2017/8/17 14:27.
 */

public class CustomDialogUtils {
    public static void showLoginDialog(final Activity activity, boolean isPc) {
        if (activity == null) {
            return;
        }
        final Dialog pickDialog = new Dialog(activity, R.style.color_dialog);
        pickDialog.setContentView(R.layout.pop_login);
        pickDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return true;
            }
        });
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog.getWindow();
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.CENTER);
        if (isPc) {
            lp.width = TCUtils.dipToPx(activity, 300); //设置宽度
            pickDialog.getWindow().setAttributes(lp);
        } else {
            lp.width = (int) (display.getWidth()); //设置宽度
            lp.y = 110;
            pickDialog.getWindow().setAttributes(lp);
        }
        Button button = (Button) pickDialog.findViewById(R.id.btn_pop_login);
        TextView tv_cancle = (TextView) pickDialog.findViewById(R.id.tv_cancle);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
                FHSocket.getInstance().close();
                Intent intent = new Intent(activity, PhoneLoginActivity.class);
                intent.putExtra("isdialog", true);
                activity.startActivity(intent);
                pickDialog.dismiss();
            }
        });
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDialog.dismiss();
            }
        });
        pickDialog.setCanceledOnTouchOutside(false);
        pickDialog.show();
    }

    public static Dialog pickDialog;


    public static CancleListener cancleListener;

    public static void setCancleListener(CancleListener cancleListener) {
        CustomDialogUtils.cancleListener = cancleListener;
    }

    public static void showQZLoginDialog(final Activity activity, boolean isPc) {
        if (activity == null) {
            return;
        }
        pickDialog = new Dialog(activity, R.style.color_dialog);
        pickDialog.setContentView(R.layout.custom_login_dialog);
        pickDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return true;
            }
        });
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog.getWindow();
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.CENTER);
        if (isPc) {
            lp.width = TCUtils.dipToPx(activity, 300); //设置宽度
            pickDialog.getWindow().setAttributes(lp);
        } else {
            lp.width = (int) (display.getWidth()); //设置宽度
            lp.y = 110;
            pickDialog.getWindow().setAttributes(lp);
        }
        RadioGroup radioGroup = (RadioGroup) pickDialog.findViewById(R.id.dialog_rg);
        TextView btnCancle = (TextView) pickDialog.findViewById(R.id.dialog_btn_cancle);
        Button btnLogin = (Button) pickDialog.findViewById(R.id.btnLogin);
        final RelativeLayout dialog_rl = (RelativeLayout) pickDialog.findViewById(R.id.dialog_rl);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.dialog_login_qq:
                        MobclickAgent.onEvent(activity, "10152");
                        QQLogin(dialog_rl, pickDialog, activity);
                        break;
                    case R.id.dialog_login_wx:
                        MobclickAgent.onEvent(activity, "10153");
                        WXLogin(dialog_rl, pickDialog, activity);
//                        dialog_rl.setVisibility(View.GONE);
                        break;
                    case R.id.dialog_login_wb:
                        MobclickAgent.onEvent(activity, "10154");
                        WeiboLogin(pickDialog, activity);
                        dialog_rl.setVisibility(View.GONE);
                        break;
                    case R.id.dialog_login_phone:
                        MobclickAgent.onEvent(activity, "10156");
//                        activity.finish();
//                        TCUtils.saveLoginInfo("", "");
                        Intent intent = new Intent(activity, PhoneLoginActivity.class);
                        intent.putExtra("isdialog", true);
                        activity.startActivity(intent);
                        pickDialog.dismiss();
                        break;
                    default:
                        break;
                }
            }
        });
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MobclickAgent.onEvent(activity, "10155");
                if (cancleListener != null) {
                    cancleListener.cancleClick();
                }
                if (pickDialog.isShowing()) {
                    pickDialog.dismiss();
                }
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QQLogin(dialog_rl, pickDialog, activity);
                MobclickAgent.onEvent(activity, "10157");
//                dialog_rl.setVisibility(View.GONE);
            }
        });
        pickDialog.setCanceledOnTouchOutside(false);
        pickDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (cancleListener != null) {
                    cancleListener.cancleClick();
                }
            }
        });
        pickDialog.show();
    }

    public interface CancleListener {
        void cancleClick();

        void loginSuccess();
    }


    /**
     * 登录新浪微博
     */
    public static void WeiboLogin(Dialog dialog, Activity activity) {
        UMShareAPI.get(activity.getApplicationContext()).getPlatformInfo(activity, SHARE_MEDIA.SINA, authListener);
    }

    /**
     * 登录微信
     */
    public static void WXLogin(RelativeLayout dialog_rl, Dialog dialog, Activity activity) {
        if (UMShareAPI.get(activity.getApplicationContext()).isInstall(activity, SHARE_MEDIA.WEIXIN)) {
            dialog_rl.setVisibility(View.GONE);
            UMShareAPI.get(activity.getApplicationContext()).doOauthVerify(activity, SHARE_MEDIA.WEIXIN, authListener);
        } else {
            MobclickAgent.onEvent(activity, "10159");
            WeiboLogin(dialog, activity);
        }
    }

    /**
     * 登录QQ
     */
    public static void QQLogin(RelativeLayout dialog_rl, Dialog dialog, Activity activity) {
        dialog_rl.setVisibility(View.GONE);
        if (UMShareAPI.get(activity.getApplicationContext()).isInstall(activity, SHARE_MEDIA.QQ)) {
            UMShareAPI.get(activity.getApplicationContext()).doOauthVerify(activity, SHARE_MEDIA.QQ, authListener);
        } else {
            MobclickAgent.onEvent(activity, "10159");
            WXLogin(dialog_rl, dialog, activity);
        }


    }


    static UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //授权开始的回调
            Log.e("platformLogin", "onStart");
            showOnLoading(pickDialog, true);
        }

        @Override
        public void onComplete(final SHARE_MEDIA platform, int action, Map<String, String> data) {
            Log.e("platformLogin", "onComplete");
            String opid = null;
            String accessToken = null;
            for (String key : data.keySet()) {
                if (platform == SHARE_MEDIA.WEIXIN) {
                    if (key.equals("openid")) {
                        opid = data.get(key);
                    }
                } else {
                    if (key.equals("uid")) {
                        opid = data.get(key);
                    }
                }
                if (key.equals("access_token")) {
                    accessToken = data.get(key);
                }
            }
            if (SharePreferenceUtil.getSessionInt(FeihuZhiboApplication.getApplication(), "deviceId", -1) == -1) {
                String android_id = DeviceInfoUtil.getAndroid_id(FeihuZhiboApplication.getApplication());
                String imsi = DeviceInfoUtil.getImsi(FeihuZhiboApplication.getApplication());
                String android_imei = DeviceInfoUtil.getAndroid_IMEI(FeihuZhiboApplication.getApplication());
                int systemApplication = DeviceInfoUtil.isSystemApplication(FeihuZhiboApplication.getApplication()) == true ? 1 : 0;
                String deviceId = DeviceInfoUtil.getDeviceId(FeihuZhiboApplication.getApplication());
                String phoneModel = DeviceInfoUtil.getPhoneModel();
                String phoneBrand = DeviceInfoUtil.getPhoneBrand();
                int appVersionCode = DeviceInfoUtil.getAppVersionCode(FeihuZhiboApplication.getApplication());
                String appVersionName = DeviceInfoUtil.getAppVersionName(FeihuZhiboApplication.getApplication());
                final String finalOpid = opid;
                final String finalAccessToken = accessToken;
                String buildVersion = DeviceInfoUtil.getBuildVersion();
                new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                        .doLogDeviceApiCall(new LogDeviceRequest(BuildConfig.market_value, systemApplication + "", appVersionCode + "", appVersionName, android_imei, imsi, deviceId, android_id, phoneBrand, phoneModel, buildVersion))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<LogDeviceResponse>() {
                            @Override
                            public void accept(@NonNull LogDeviceResponse response) throws Exception {
                                if (response.getCode() == 0) {
                                    SharePreferenceUtil.saveSeesionInt(FeihuZhiboApplication.getApplication(), "deviceId", response.getDeviceId());
                                    getLogin(platform, finalOpid, finalAccessToken);
                                } else {

                                }
                                pickDialog.dismiss();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {

                            }
                        }));
            } else {
                getLogin(platform, opid, accessToken);
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            pickDialog.dismiss();
            showOnLoading(pickDialog, false);
            Log.e("platformLogin", "onError");

        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            pickDialog.dismiss();
            showOnLoading(pickDialog, false);
            Log.e("platformLogin", "onCancel");

        }
    };

    private static void getLogin(SHARE_MEDIA platform, String opid, final String accessToken) {
        String pf = "0";
        if (platform.equals(SHARE_MEDIA.WEIXIN)) {
            pf = "2";
        } else if (platform.equals(SHARE_MEDIA.QQ)) {
            pf = "3";
        } else if (platform.equals(SHARE_MEDIA.SINA)) {
            pf = "4";
        }
        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .doPlatformLoginApiCall(new LoginRequest.PlatformLoginRequest(pf, opid, accessToken, String.valueOf(FeihuZhiboApplication.getApplication().mDataManager.getDeviceId())))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoginResponse>() {
                    @Override
                    public void accept(@NonNull LoginResponse response) throws Exception {
                        if (response.getCode() == 0) {
                            LoginResponse.LoginResponseData loginResponseData = response.getLoginResponseData();
                            String token = loginResponseData.getToken();
                            String uid = loginResponseData.getUid();
                            // 保存token uid
                            FeihuZhiboApplication.getApplication().mDataManager.setApiKey(token);
                            FeihuZhiboApplication.getApplication().mDataManager.setCurrentUserId(uid);
                            long t = FHUtils.getLongTime() + FeihuZhiboApplication.getApplication().mDataManager.getTimeChaZhi();
                            FeihuZhiboApplication.getApplication().mDataManager.updateApiHeader(uid, String.valueOf(t), token);

                            RxBus.get().send(RxBusCode.RX_BUS_CODE_NOTICE_ENTER_LOGIN);
                            pickDialog.dismiss();
                        } else {
                            if (response.getCode() == 4605) {
                                FHUtils.showToast("该账号已被禁止登录");
                            } else if (response.getCode() == 4027) {
                                FHUtils.showToast("该账号已在电脑端登陆");
                            }
                            pickDialog.dismiss();
                            showOnLoading(pickDialog, false);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));

    }

    public static void enterLoginSuccess() {
        FHUtils.showToast("登录成功");
        if (pickDialog != null) {
            pickDialog.dismiss();
        }
        pickDialog.dismiss();
        showOnLoading(pickDialog, false);
        if (cancleListener != null) {
            cancleListener.loginSuccess();
        }
    }

    public static void enterLoginFail(LoginResponse response) {
        if (response.getCode() == 4605) {
            FHUtils.showToast("该账号已被禁止登录");
        } else if (response.getCode() == 4606) {
            FHUtils.showToast("该手机已被禁止登录");
        }
        pickDialog.dismiss();
        showOnLoading(pickDialog, false);
    }

    /**
     * trigger loading模式
     *
     * @param active
     */

    public static void showOnLoading(Dialog dialog, boolean active) {
        if (dialog == null) {
            return;
        }
        try {
            RelativeLayout relativeLayout = (RelativeLayout) dialog.findViewById(R.id.rl);
            if (active) {
                relativeLayout.setVisibility(View.VISIBLE);
            } else {
                relativeLayout.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
