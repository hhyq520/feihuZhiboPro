package cn.feihutv.zhibofeihu.ui.user.phoneLogin;

import android.view.Gravity;

import javax.inject.Inject;

import cn.feihutv.zhibofeihu.data.DataManager;
import cn.feihutv.zhibofeihu.data.network.http.model.user.LoginRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.user.LoginResponse;
import cn.feihutv.zhibofeihu.ui.base.BasePresenter;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.TCUtils;
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
public class PhoneLoginPresenter<V extends PhoneLoginMvpView> extends BasePresenter<V>
        implements PhoneLoginMvpPresenter<V> {


    @Inject
    public PhoneLoginPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void phoneLogin(String phoneNum, String password, boolean isDialog) {
        attemptPhoneLogin(phoneNum, password, isDialog);
    }

    /**
     * 手机号密码登录
     *
     * @param phoneNum 手机号码
     * @param password 密码
     */
    public void attemptPhoneLogin(String phoneNum, String password, boolean isDialog) {
        if (TCUtils.isPhoneNumValid(phoneNum)) {
            if (TCUtils.isPasswordValid(password)) {
                if (getMvpView().isNetworkConnected()) {
                    //调用LoginHelper进行普通登陆
                    if (isDialog) {
                        getDataManager().setUserAsLoggedOut();
                    }
                    userLogin(phoneNum, password);
                } else {
                    getMvpView().onToast("网络不可用！", Gravity.CENTER, 0, 0);
                    getMvpView().hideLoading();

                }
            } else {
                getMvpView().onToast("请输入不低于6位的密码", Gravity.CENTER, 0, 0);
                getMvpView().hideLoading();
            }
        } else {
            getMvpView().onToast("请输入正确的手机号", Gravity.CENTER, 0, 0);
            getMvpView().hideLoading();
        }
    }

    private void userLogin(final String phone, final String userPass) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .doServerLoginApiCall(new LoginRequest.ServerLoginRequest(phone, userPass))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoginResponse>() {
                    @Override
                    public void accept(@NonNull LoginResponse loginResponse) throws Exception {
                        if (loginResponse.getCode() == 0) {
                            String token = loginResponse.getLoginResponseData().getToken();
                            String uid = loginResponse.getLoginResponseData().getUid();
                            getDataManager().setApiKey(token);
                            getDataManager().setCurrentUserId(uid);
                            long t = FHUtils.getLongTime() + getDataManager().getTimeChaZhi();
                            getDataManager().updateApiHeader(uid, String.valueOf(t), token);
                            enterLogin("0", new Consumer<LoginResponse>() {
                                @Override
                                public void accept(@NonNull LoginResponse baseResponse) throws Exception {
                                    if (baseResponse.getCode() == 0) {
                                        getMvpView().openMainActivity();
                                        getMvpView().hideLoading();
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
                                        getMvpView().hideLoading();
                                    }
                                }
                            });
                        } else {
                            if (loginResponse.getCode() == 4605) {
                                getMvpView().onToast("该账号已被禁止登录", Gravity.CENTER, 0, 0);
                            } else if (loginResponse.getCode() == 4027) {
                                getMvpView().onToast("该账号已在电脑端登陆", Gravity.CENTER, 0, 0);
                            } else if (loginResponse.getCode() == 4007) {
                                getMvpView().onToast("该账号未注册！", Gravity.CENTER, 0, 0);
                            } else if (loginResponse.getCode() == 4012) {
                                getMvpView().onToast("账号或密码错误", Gravity.CENTER, 0, 0);
                            }

                            AppLogger.e(loginResponse.getErrMsg());
                            getMvpView().hideLoading();
                        }

                    }
                }, getConsumer()));
    }
}
