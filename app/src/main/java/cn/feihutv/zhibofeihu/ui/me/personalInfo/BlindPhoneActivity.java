package cn.feihutv.zhibofeihu.ui.me.personalInfo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.common.GetVerifyCodeRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.GetVerifyCodeResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.BindPhoneRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.BindPhoneResponse;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.widget.TCActivityTitle;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/1 15:26
 *      desc   : 绑定手机页面
 *      version: 1.0
 * </pre>
 */

public class BlindPhoneActivity extends BaseActivity {

    @BindView(R.id.phone_back)
    TCActivityTitle mTitle;

    @BindView(R.id.phone_edit)
    EditText mEtPhone;

    @BindView(R.id.tv_phonecode)
    TextView tvGetCode;

    @BindView(R.id.verifity_code)
    EditText mEtCode;

    @BindView(R.id.first_psd)
    EditText mEtFirstPsd;

    @BindView(R.id.next_pwd)
    EditText mEtSurePsd;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bindphone;
    }

    @Override
    protected void eventOnClick() {
        mTitle.setReturnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void initWidget() {
        setUnBinder(ButterKnife.bind(this));
    }


    private String getEtPhone() {
        return mEtPhone.getText().toString().trim();
    }

    private String getVerifityCode() {
        return mEtCode.getText().toString().trim();
    }

    private String getFirstPsd() {
        return mEtFirstPsd.getText().toString().trim();
    }

    private String getSurePsd() {
        return mEtSurePsd.getText().toString().trim();
    }

    @OnClick({R.id.tv_phonecode, R.id.btn_finish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_phonecode:
                if (!isNetworkConnected()) {
                    onToast("网络异常，请检查您的网络");
                    return;
                }
                if (TextUtils.isEmpty(getEtPhone())) {
                    onToast("请输入手机号码！");
                    return;
                }
                if (!TCUtils.isPhoneNumValid(getEtPhone())) {
                    onToast("请输入正确的手机号！");
                    return;
                }
                showLoading();
                new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                        .doGetVerifyCodeApiCall(new GetVerifyCodeRequest(getEtPhone(), 3, 1))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<GetVerifyCodeResponse>() {
                            @Override
                            public void accept(@NonNull GetVerifyCodeResponse getVerifyCodeResponse) throws Exception {
                                if (getVerifyCodeResponse.getCode() == 0) {
                                    TCUtils.startTimer2(getApplicationContext(), new WeakReference<>(tvGetCode), getResources().getString(R.string.getCode), 60, 1);
                                } else {
                                    if (getVerifyCodeResponse.getCode() == 4018) {
                                        onToast("短信次数已用完");
                                    } else if (getVerifyCodeResponse.getCode() == 4015) {
                                        onToast("该手机号已绑定别的账号，请输入其他手机号");
                                    } else {
                                        onToast("手机号已注册或验证码错误");
                                    }
                                }
                                hideLoading();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                hideLoading();
                                onToast("网络异常，绑定失败！");
                            }
                        }));
                break;
            case R.id.btn_finish:
                if (!isNetworkConnected()) {
                    onToast("网络异常，请检查您的网络");
                    return;
                }
                if (TextUtils.isEmpty(getEtPhone())) {
                    onToast("请输入手机号码！");
                    return;
                }
                if (!TCUtils.isPhoneNumValid(getEtPhone())) {
                    onToast("请输入正确的手机号！");
                    return;
                }
                if (TextUtils.isEmpty(getVerifityCode())) {
                    onToast("请输入验证码！");
                    return;
                }
                if (TextUtils.isEmpty(getFirstPsd())) {
                    onToast("请输入密码！");
                    return;
                }
                if (TCUtils.isNumPsd(getFirstPsd())) {
                    onToast("密码应由6-15位数字和字母组成");
                    return;
                }
                if (TCUtils.isZimuPsd(getFirstPsd())) {
                    onToast("密码应由6-15位数字和字母组成");
                    return;
                }
                if (!TCUtils.isPsd(getFirstPsd())) {
                    onToast("密码应由6-15位数字和字母组成");
                    return;
                }
                if (!getFirstPsd().equals(getSurePsd())) {
                    onToast("两次输入密码不一致！");
                    return;
                }
                showLoading();
                new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                        .doBindPhoneApiCall(new BindPhoneRequest(getEtPhone(), getVerifityCode(), getFirstPsd()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<BindPhoneResponse>() {
                            @Override
                            public void accept(@NonNull BindPhoneResponse bindPhoneResponse) throws Exception {
                                if (bindPhoneResponse.getCode() == 0) {
                                    onToast("绑定成功！虎币+10");
                                    Intent intent = new Intent();
                                    intent.putExtra("phonenumber", getEtPhone());
                                    setResult(PersonalInfoActivity.REQUSST_PHONENUM, intent);
                                    finish();
                                } else {
                                    if (bindPhoneResponse.getCode() == 4006) {
                                        onToast("验证码错误");
                                    } else {
                                        onToast("绑定失败");
                                    }
                                }
                                hideLoading();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                hideLoading();
                                onToast("网络异常，绑定失败！");
                            }
                        }));
                break;
            default:
                break;
        }
    }
}
