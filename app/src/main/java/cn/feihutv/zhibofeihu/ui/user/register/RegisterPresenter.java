package cn.feihutv.zhibofeihu.ui.user.register;

import android.view.Gravity;

import javax.inject.Inject;

import cn.feihutv.zhibofeihu.BuildConfig;
import cn.feihutv.zhibofeihu.data.DataManager;
import cn.feihutv.zhibofeihu.data.db.model.SysConfigBean;
import cn.feihutv.zhibofeihu.data.network.http.model.common.GetVerifyCodeRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.GetVerifyCodeResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LogDeviceRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LogDeviceResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.user.CreateUserRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.user.CreateUserResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.user.LoginResponse;
import cn.feihutv.zhibofeihu.ui.base.BasePresenter;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.DeviceInfoUtil;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * <pre>
 *     author : huang hao
 *     time   :
 *     desc   : p业务处理实现类
 *     version: 1.0
 * </pre>
 */
public class RegisterPresenter<V extends RegisterMvpView> extends BasePresenter<V>
        implements RegisterMvpPresenter<V> {


    @Inject
    public RegisterPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void sendRegistVerifyCode(String phoneNum) {
        if (TCUtils.isPhoneNumValid(phoneNum)) {
            getCompositeDisposable().add(getDataManager()
                    .doGetVerifyCodeApiCall(new GetVerifyCodeRequest(phoneNum, 1, 1))
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<GetVerifyCodeResponse>() {
                        @Override
                        public void accept(@NonNull GetVerifyCodeResponse getVerifyCodeResponse) throws Exception {
                            if (getVerifyCodeResponse.getCode() == 0) {
                                getMvpView().startTimer();
                            } else {
                                if (getVerifyCodeResponse.getCode() == 4015) {
                                    getMvpView().onToast("用户已存在", Gravity.CENTER, 0, 0);
                                } else if (getVerifyCodeResponse.getCode() == 4018) {
                                    getMvpView().onToast("短信次数已用完", Gravity.CENTER, 0, 0);
                                } else {
                                    getMvpView().onToast("手机号已注册或验证码错误", Gravity.CENTER, 0, 0);
                                }
                            }
                        }
                    }, getConsumer()));
        } else {
            getMvpView().onToast("请输入正确的手机号", Gravity.CENTER, 0, 0);
        }
    }

    @Override
    public void createUser(final String phone, final String userPass, final String nickName, final String passwordVerify, final String verifiCode, final boolean isDialog) {
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
                                //注册
                                register(phone, userPass, nickName, passwordVerify, verifiCode, isDialog);
                            } else {
                                getMvpView().hideLoading();
                            }
                        }
                    }, getConsumer()));
        } else {
            //注册
            register(phone, userPass, nickName, passwordVerify, verifiCode, isDialog);
        }
    }

    @Override
    public SysConfigBean getSysConfig() {
        return getDataManager().getSysConfig();
    }

    private void register(String phone, String userPass, String nickName, String passwordVerify, String verifiCode, boolean isDialog) {
        if (TCUtils.isZimuPsd(userPass)) {
            getMvpView().onToast("密码不能为纯字母！", Gravity.CENTER, 0, 0);
            return;
        }
        if (TCUtils.isNumPsd(userPass)) {
            getMvpView().onToast("密码不能为纯数字！", Gravity.CENTER, 0, 0);
            return;
        }
        if (!TCUtils.isPsd(userPass)) {
            getMvpView().onToast("密码不符合规则！", Gravity.CENTER, 0, 0);
            return;
        }
        if (!TCUtils.isPhoneNumValid(phone)) {
            getMvpView().onToast("请输入正确的手机号！", Gravity.CENTER, 0, 0);
            return;
        }
        if (!TCUtils.isPasswordValid(userPass)) {
            getMvpView().onToast("密码长度应为6-15位！", Gravity.CENTER, 0, 0);
            return;
        }
        if (!userPass.equals(passwordVerify)) {
            getMvpView().onToast("两次输入密码不一致！", Gravity.CENTER, 0, 0);
            return;
        }
        getMvpView().showLoading();
        if (isDialog) {
            getDataManager().setUserAsLoggedOut();
        }
        getCompositeDisposable().add(getDataManager()
                .doCreateUserApiCall(new CreateUserRequest(phone, userPass, nickName, verifiCode, String.valueOf(getDataManager().getDeviceId())))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CreateUserResponse>() {
                    @Override
                    public void accept(@NonNull CreateUserResponse createUserResponse) throws Exception {
                        if (createUserResponse.getCode() == 0) {
                            String token = createUserResponse.getCreateUserResponseData().getToken();
                            String uid = createUserResponse.getCreateUserResponseData().getUid();
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
                                    }
                                    getMvpView().hideLoading();
                                }
                            });
                        } else {
                            if (createUserResponse.getCode() == 4016) {
                                getMvpView().onToast("昵称已存在", Gravity.CENTER, 0, 0);
                            } else if (createUserResponse.getCode() == 4006) {
                                getMvpView().onToast("验证码输入有误，请重新输入", Gravity.CENTER, 0, 0);
                            } else if (createUserResponse.getCode() == 4009) {
                                getMvpView().onToast("长度超限或内容违规", Gravity.CENTER, 0, 0);
                            } else {
                                getMvpView().onToast("手机号已注册或验证码错误", Gravity.CENTER, 0, 0);
                            }
                            getMvpView().hideLoading();
                        }

                    }
                }, getConsumer()));
    }
}
