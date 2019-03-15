package cn.feihutv.zhibofeihu.ui.user.forgetPsd;

import android.view.Gravity;

import cn.feihutv.zhibofeihu.data.network.http.model.common.GetVerifyCodeRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.GetVerifyCodeResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.common.RestPsdRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.RestPsdResponse;
import cn.feihutv.zhibofeihu.ui.base.BasePresenter;
import cn.feihutv.zhibofeihu.data.DataManager;

import javax.inject.Inject;

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
 *     author : huang hao
 *     desc   : p业务处理实现类
 *     version: 1.0
 * </pre>
 */
public class ForgetPsdPresenter<V extends ForgetPsdMvpView> extends BasePresenter<V>
        implements ForgetPsdMvpPresenter<V> {


    @Inject
    public ForgetPsdPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void sendRegistVerifyCode(String phoneNum) {
        if (TCUtils.isPhoneNumValid(phoneNum)) {
            getCompositeDisposable().add(getDataManager()
                    .doGetVerifyCodeApiCall(new GetVerifyCodeRequest(phoneNum, 2, 0))
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<GetVerifyCodeResponse>() {
                        @Override
                        public void accept(@NonNull GetVerifyCodeResponse getVerifyCodeResponse) throws Exception {
                            if (getVerifyCodeResponse.getCode() == 0) {
                                getMvpView().startTimer();
                            } else {
                                if (getVerifyCodeResponse.getCode() == 4018) {
                                    getMvpView().onToast("短信次数已用完", Gravity.CENTER, 0, 0);
                                } else {
                                    getMvpView().onToast("短信次数已用完", Gravity.CENTER, 0, 0);
                                    AppLogger.e(getVerifyCodeResponse.getErrMsg());
                                }
                            }
                        }
                    }, getConsumer()));
        } else {
            getMvpView().onToast("请输入正确的手机号", Gravity.CENTER, 0, 0);
        }
    }

    @Override
    public void sureToLogin(String phone, String newPsd, String passwordVerify, String verifiCode) {
        if (TCUtils.isZimuPsd(newPsd)) {
            getMvpView().onToast("密码不能为纯字母", Gravity.CENTER, 0, 0);
            return;
        }
        if (TCUtils.isNumPsd(newPsd)) {
            getMvpView().onToast("密码不能为数字", Gravity.CENTER, 0, 0);
            return;
        }
        if (!TCUtils.isPsd(newPsd)) {
            getMvpView().onToast("密码不符合规则", Gravity.CENTER, 0, 0);
            return;
        }
        if (!TCUtils.isPhoneNumValid(phone)) {
            getMvpView().onToast("请输入正确的手机号", Gravity.CENTER, 0, 0);
            return;
        }
        if (!TCUtils.isPasswordValid(newPsd)) {
            getMvpView().onToast("密码长度应为6-15位", Gravity.CENTER, 0, 0);
            return;
        }
        if (!newPsd.equals(passwordVerify)) {
            getMvpView().onToast("两次输入密码不一致", Gravity.CENTER, 0, 0);
            return;
        }

        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .doRestPsdApiCall(new RestPsdRequest(phone, newPsd, verifiCode))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RestPsdResponse>() {
                    @Override
                    public void accept(@NonNull RestPsdResponse restPsdResponse) throws Exception {
                        if (restPsdResponse.getCode() == 0) {
                            String token = restPsdResponse.getRestPsdResponseData().getToken();
                            String uid = restPsdResponse.getRestPsdResponseData().getUid();
                            // 保存token uid
                            getDataManager().setApiKey(token);
                            getDataManager().setCurrentUserId(uid);
                            long t = FHUtils.getLongTime() + getDataManager().getTimeChaZhi();
                            getDataManager().updateApiHeader(uid, String.valueOf(t), token);
                            getMvpView().onToast("修改成功！", Gravity.CENTER, 0, 0);
                            getMvpView().getActivity().finish();
                        } else {
                            if (restPsdResponse.getCode() == 4006) {
                                getMvpView().onToast("验证码有误，请重新输入!", Gravity.CENTER, 0, 0);
                            } else if (restPsdResponse.getCode() == 4007) {
                                getMvpView().onToast("手机号未注册！", Gravity.CENTER, 0, 0);
                            }
                            AppLogger.e(restPsdResponse.getErrMsg());
                        }
                        getMvpView().hideLoading();
                    }
                }, getConsumer()));
    }
}
