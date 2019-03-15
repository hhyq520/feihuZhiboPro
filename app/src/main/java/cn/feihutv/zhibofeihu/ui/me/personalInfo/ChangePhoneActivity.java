package cn.feihutv.zhibofeihu.ui.me.personalInfo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
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
import cn.feihutv.zhibofeihu.data.network.http.model.me.CheckVerifiCodeRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.CheckVerifiCodeResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseResponse;
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
 *      time   : 2017/11/1 17:30
 *      desc   : 更换手机号码
 *      version: 1.0
 * </pre>
 */

public class ChangePhoneActivity extends BaseActivity {

    @BindView(R.id.phone_back)
    TCActivityTitle mTitle;

    @BindView(R.id.edit_num)
    EditText mEtNum;

    @BindView(R.id.verifity_code)
    EditText mEtCode;

    @BindView(R.id.tv_phonecode)
    TextView textView;
    private LoadUserDataBaseResponse.UserData mUserData;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_phone;
    }

    @Override
    protected void initWidget() {
        setUnBinder(ButterKnife.bind(this));

        mUserData = FeihuZhiboApplication.getApplication().mDataManager.getUserData();
        mEtNum.setText(mUserData.getPhone());
    }

    @Override
    protected void eventOnClick() {
        mTitle.setReturnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mEtNum.setEnabled(false);
    }

    @OnClick({R.id.tv_phonecode, R.id.btn_finish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_phonecode:
                if (!TCUtils.isPhoneNumValid(getPhoneNum())) {
                    onToast("请输入正确的手机号");
                    return;
                }
                if (!isNetworkConnected()) {
                    onToast("请检查网络");
                    return;
                }
                showLoading();
                new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                        .doGetVerifyCodeApiCall(new GetVerifyCodeRequest(mUserData.getPhone(), 3, 0))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<GetVerifyCodeResponse>() {
                            @Override
                            public void accept(@NonNull GetVerifyCodeResponse getVerifyCodeResponse) throws Exception {
                                if (getVerifyCodeResponse.getCode() == 0) {
                                    TCUtils.startTimer2(getApplicationContext(), new WeakReference<>(textView), getResources().getString(R.string.getCode), 60, 1);
                                } else {
                                    if (getVerifyCodeResponse.getCode() == 4018) {
                                        onToast("短信次数已用完");
                                    } else if (getVerifyCodeResponse.getCode() == 4015) {
                                        onToast("该手机号已注册");
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
                                onToast("网络异常");
                            }
                        }));
                break;
            case R.id.btn_finish:
                if (TextUtils.isEmpty(getPhoneNum())) {
                    onToast("请输入手机号码！");
                    return;
                }
                if (TextUtils.isEmpty(getVerifiCode())) {
                    onToast("请输入验证码");
                    return;
                }
                if (!isNetworkConnected()) {
                    onToast("请检查网络");
                    return;
                }

                showLoading();
                new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                        .doCheckVerifiCodeApiCall(new CheckVerifiCodeRequest(getVerifiCode()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<CheckVerifiCodeResponse>() {
                            @Override
                            public void accept(@NonNull CheckVerifiCodeResponse checkVerifiCodeResponse) throws Exception {
                                if (checkVerifiCodeResponse.getCode() == 0) {
                                    if (checkVerifiCodeResponse.isBoolean()) {
                                        Intent intent = new Intent(ChangePhoneActivity.this, SurePhoneActivity.class);
                                        intent.putExtra("verificode", getVerifiCode());
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        onToast("验证码错误", Gravity.CENTER, 0, 0);
                                    }
                                }
                                hideLoading();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                hideLoading();
                                onToast("网络异常", Gravity.CENTER, 0, 0);
                            }
                        }));
                break;
            default:
                break;
        }
    }


    private String getPhoneNum() {
        return mEtNum.getText().toString().trim();
    }

    private String getVerifiCode() {
        return mEtCode.getText().toString().trim();
    }
}
