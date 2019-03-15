package cn.feihutv.zhibofeihu.ui.user.login;

import android.util.Log;
import android.view.Gravity;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import javax.inject.Inject;

import cn.feihutv.zhibofeihu.BuildConfig;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.data.DataManager;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LogDeviceRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LogDeviceResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.user.LoginRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.user.LoginResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseResponse;
import cn.feihutv.zhibofeihu.ui.base.BasePresenter;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.DeviceInfoUtil;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * <pre>
 *     author : liwen.chen
 *     desc   : p业务处理实现类
 *     version: 1.0
 * </pre>
 */
public class LoginPresenter<V extends LoginMvpView> extends BasePresenter<V>
        implements LoginMvpPresenter<V> {


    @Inject
    public LoginPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void platformLogin(SHARE_MEDIA platform) {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().onToast("网络连接失败，请检查您的网络", Gravity.CENTER, 0, 0);
            getMvpView().hideLoading();
            return;
        }
        if (platform.equals(SHARE_MEDIA.QQ)) {
            QQLogin();
        } else if (platform.equals(SHARE_MEDIA.WEIXIN)) {
            WXLogin();
        } else if (platform.equals(SHARE_MEDIA.SINA)) {
            WeiboLogin();
        }
    }

    @Override
    public void guestLogin() {
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .doGuestLoginApiCall(new LoginRequest.GuestLoginRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoginResponse>() {
                    @Override
                    public void accept(@NonNull LoginResponse loginResponse) throws Exception {
                        if (loginResponse.getCode() == 0) {

//                            // 登录成功
                            LoginResponse.LoginResponseData loginResponseData = loginResponse.getLoginResponseData();
                            AppLogger.i(loginResponseData.toString());
                            String token = loginResponseData.getToken();
                            String uid = loginResponseData.getUid();
                            getDataManager().setApiKey(token);
                            getDataManager().setCurrentUserId(uid);
                            long t = FHUtils.getLongTime() + getDataManager().getTimeChaZhi();
                            getDataManager().updateApiHeader(uid, String.valueOf(t), token);
                            LoadUserDataBaseResponse.UserData userData = new LoadUserDataBaseResponse.UserData();
                            userData.setUserId(uid);
                            LoadUserDataBaseResponse.Modified modified = new LoadUserDataBaseResponse.Modified();
                            modified.setGender(1);
                            modified.setNickName(0);
                            userData.setmModified(modified);
                            getDataManager().saveUserData(userData);
                            enterLogin("0", new Consumer<LoginResponse>() {
                                @Override
                                public void accept(@NonNull LoginResponse baseResponse) throws Exception {
                                    if (baseResponse.getCode() == 0) {
                                        getMvpView().openMainActivity();
                                    } else {
                                        AppLogger.i("baseResponse:  " + baseResponse.getErrMsg());
                                        if (baseResponse.getCode() == 4605) {
                                            //{"Code":4605,"ErrExtMsg":"太帅了","ErrExtMsg1":"30","ErrExtMsg2":"2685849936","ErrMsg":"BannedLogin"}
                                            String str = "您因【" + baseResponse.getErrExtMsg() + "】被系统封号了，封号时长为【" + baseResponse.getErrExtMsgTime() + "】分钟，若有疑问请联系飞虎客服（QQ：" + baseResponse.getErrExtMsgQ() + "）";
                                            getMvpView().showForbidLoginDlg(str, baseResponse.getErrExtMsgQ());
                                        } else if (baseResponse.getCode() == 4606) {
                                            //{"Code":4605,"ErrExtMsg":"涉嫌违规","ErrExtMsg1":1440,"ErrExtMsg2":"2685849936","ErrMsg":"BannedLogin"}
                                            String str = "您因【" + baseResponse.getErrExtMsg() + "】被系统封号了，封号时长为【1】天，若有疑问请联系飞虎客服（QQ：" + baseResponse.getErrExtMsgQ() + "）";
                                            getMvpView().showForbidLoginDlg(str, baseResponse.getErrExtMsgQ());
                                        } else if (baseResponse.getCode() == 4027) {
                                            FHUtils.showToast("该账号已在电脑端登陆");
                                        }
                                    }
                                    getMvpView().hideLoading();
                                }
                            });
                        } else {
                            // 登录失败
                            getMvpView().hideLoading();
                        }
                        getMvpView().hideLoading();
                    }
                }, getConsumer()));
    }

    @Override
    public void openPhoneLoginActivity() {
        getMvpView().goPhoneLogin();
    }


    /**
     * 登录新浪微博
     */
    private void WeiboLogin() {
        // UMShareAPI.get(LoginActivity.this).doOauthVerify(LoginActivity.this, SHARE_MEDIA.SINA, authListener);
        UMShareAPI.get(getMvpView().getActivity()).getPlatformInfo(getMvpView().getActivity(), SHARE_MEDIA.SINA, authListener);
    }

    /**
     * 登录微信
     */
    private void WXLogin() {
        if (UMShareAPI.get(FeihuZhiboApplication.getApplication().getApplicationContext()).isInstall(getMvpView().getActivity(), SHARE_MEDIA.WEIXIN)) {
            UMShareAPI.get(getMvpView().getActivity()).doOauthVerify(getMvpView().getActivity(), SHARE_MEDIA.WEIXIN, authListener);
        } else {
            getMvpView().onToast("尚未安装微信客户端", Gravity.CENTER, 0, 0);
            getMvpView().hideLoading();
        }
    }

    /**
     * 登录QQ
     */
    private void QQLogin() {

        if (UMShareAPI.get(FeihuZhiboApplication.getApplication().getApplicationContext()).isInstall(getMvpView().getActivity(), SHARE_MEDIA.QQ)) {
            UMShareAPI.get(getMvpView().getActivity()).doOauthVerify(getMvpView().getActivity(), SHARE_MEDIA.QQ, authListener);
        } else {
            getMvpView().onToast("尚未安装QQ客户端", Gravity.CENTER, 0, 0);
            getMvpView().hideLoading();
        }
    }

    UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //授权开始的回调
            Log.e("platformLogin", "onStart");
            getMvpView().showLoading();
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
            if (getDataManager().getDeviceId() == -1) {
                String android_id = DeviceInfoUtil.getAndroid_id(getMvpView().getActivity());
                String imsi = DeviceInfoUtil.getImsi(getMvpView().getActivity());
                String android_imei = DeviceInfoUtil.getAndroid_IMEI(getMvpView().getActivity());
                int systemApplication = DeviceInfoUtil.isSystemApplication(getMvpView().getActivity()) == true ? 1 : 0;
                String deviceId = DeviceInfoUtil.getDeviceId(getMvpView().getActivity());
                String phoneModel = DeviceInfoUtil.getPhoneModel();
                String phoneBrand = DeviceInfoUtil.getPhoneBrand();
                int appVersionCode = DeviceInfoUtil.getAppVersionCode(getMvpView().getActivity());
                String appVersionName = DeviceInfoUtil.getAppVersionName(getMvpView().getActivity());
                final String finalOpid = opid;
                final String finalAccessToken = accessToken;
                String buildVersion = DeviceInfoUtil.getBuildVersion();
                getCompositeDisposable().add(getDataManager()
                        .doLogDeviceApiCall(new LogDeviceRequest(BuildConfig.market_value, String.valueOf(systemApplication), String.valueOf(appVersionCode), appVersionName, android_imei, imsi, deviceId, android_id, phoneBrand, phoneModel, buildVersion))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<LogDeviceResponse>() {
                            @Override
                            public void accept(@NonNull LogDeviceResponse logDeviceResponse) throws Exception {
                                if (logDeviceResponse.getCode() == 0) {
                                    getDataManager().setDeviceId(logDeviceResponse.getDeviceId());
                                    getLogin(platform, finalOpid, finalAccessToken);
                                } else {

                                }
                                getMvpView().hideLoading();
                            }
                        }, getConsumer()));
            } else {
                getLogin(platform, opid, accessToken);
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            getMvpView().hideLoading();
            Log.e("platformLogin", "onError");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            getMvpView().hideLoading();
            Log.e("platformLogin", "onCancel");
        }
    };

    private void getLogin(SHARE_MEDIA platform, String opid, String accessToken) {
        String pf = "0";
        if (platform.equals(SHARE_MEDIA.WEIXIN)) {
            pf = "2";
        } else if (platform.equals(SHARE_MEDIA.QQ)) {
            pf = "3";
        } else if (platform.equals(SHARE_MEDIA.SINA)) {
            pf = "4";
        }
        getCompositeDisposable().add(getDataManager()
                .doPlatformLoginApiCall(new LoginRequest.PlatformLoginRequest(pf, opid, accessToken, String.valueOf(getDataManager().getDeviceId())))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoginResponse>() {
                    @Override
                    public void accept(@NonNull LoginResponse loginResponse) throws Exception {
                        if (loginResponse.getCode() == 0) {
                            LoginResponse.LoginResponseData loginResponseData = loginResponse.getLoginResponseData();
                            String token = loginResponseData.getToken();
                            String uid = loginResponseData.getUid();
                            // 保存token uid
                            getDataManager().setApiKey(token);
                            getDataManager().setCurrentUserId(uid);

                            long t = FHUtils.getLongTime() + getDataManager().getTimeChaZhi();
                            getDataManager().updateApiHeader(uid, String.valueOf(t), token);
                            enterLogin("0", new Consumer<LoginResponse>() {
                                @Override
                                public void accept(@NonNull LoginResponse baseResponse) throws Exception {
                                    if (baseResponse.getCode() == 0) {
                                        getMvpView().openMainActivity();
                                    } else {
                                        AppLogger.i("baseResponse:  " + baseResponse.getErrMsg());
                                        if (baseResponse.getCode() == 4605) {
                                            //{"Code":4605,"ErrExtMsg":"太帅了","ErrExtMsg1":"30","ErrExtMsg2":"2685849936","ErrMsg":"BannedLogin"}
                                            String str = "您因【" + baseResponse.getErrExtMsg() + "】被系统封号了，封号时长为【" + baseResponse.getErrExtMsgTime() + "】分钟，若有疑问请联系飞虎客服（QQ：" + baseResponse.getErrExtMsgQ() + "）";
                                            getMvpView().showForbidLoginDlg(str, baseResponse.getErrExtMsgQ());
                                        } else if (baseResponse.getCode() == 4606) {
                                            //{"Code":4605,"ErrExtMsg":"涉嫌违规","ErrExtMsg1":1440,"ErrExtMsg2":"2685849936","ErrMsg":"BannedLogin"}
                                            String str = "您因【" + baseResponse.getErrExtMsg() + "】被系统封号了，封号时长为【1】天，若有疑问请联系飞虎客服（QQ：" + baseResponse.getErrExtMsgQ() + "）";
                                            getMvpView().showForbidLoginDlg(str, baseResponse.getErrExtMsgQ());
                                        } else if (baseResponse.getCode() == 4027) {
                                            FHUtils.showToast("该账号已在电脑端登陆");
                                        }
                                    }
                                    getMvpView().hideLoading();
                                }
                            });
                        } else {
                            AppLogger.i("loginResponse:  " + loginResponse.getErrMsg());
                            getMvpView().hideLoading();
                        }

                    }
                }, getConsumer()));
    }


    @Override
    public void getUserDeviceId() {
        if (getDataManager().getDeviceId() == -1) {
            //首次安装或者重新安装需获取用户设备id
            String android_id = DeviceInfoUtil.getAndroid_id(getMvpView().getActivity());
            String imsi = DeviceInfoUtil.getImsi(getMvpView().getActivity());
            String android_imei = DeviceInfoUtil.getAndroid_IMEI(getMvpView().getActivity());
            int systemApplication = DeviceInfoUtil.isSystemApplication(getMvpView().getActivity()) == true ? 1 : 0;
            String deviceId = DeviceInfoUtil.getDeviceId(getMvpView().getActivity());
            String phoneModel = DeviceInfoUtil.getPhoneModel();
            String phoneBrand = DeviceInfoUtil.getPhoneBrand();
            int appVersionCode = DeviceInfoUtil.getAppVersionCode(getMvpView().getActivity());
            String appVersionName = DeviceInfoUtil.getAppVersionName(getMvpView().getActivity());
            String buildVersion = DeviceInfoUtil.getBuildVersion();

            getCompositeDisposable().add(getDataManager()
                    .doLogDeviceApiCall(new LogDeviceRequest(BuildConfig.market_value, String.valueOf(systemApplication), String.valueOf(appVersionCode), appVersionName, android_imei, imsi, deviceId, android_id, phoneBrand, phoneModel, buildVersion))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<LogDeviceResponse>() {
                        @Override
                        public void accept(@NonNull LogDeviceResponse logDeviceResponse) throws Exception {
                            if (logDeviceResponse.getCode() == 0) {
                                //保存用户设备id
                                getDataManager().setDeviceId(logDeviceResponse.getDeviceId());

                            }
                        }
                    }, getConsumer()));
        }
    }
}
