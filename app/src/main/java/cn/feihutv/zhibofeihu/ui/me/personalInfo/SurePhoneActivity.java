package cn.feihutv.zhibofeihu.ui.me.personalInfo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
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
import cn.feihutv.zhibofeihu.data.network.http.model.me.ModifyPhoneRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.ModifyPhoneResponse;
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
 *      time   : 2017/11/1 17:38
 *      desc   : 更换手机号码
 *      version: 1.0
 * </pre>
 */

public class SurePhoneActivity extends BaseActivity {

    @BindView(R.id.phone_back)
    TCActivityTitle mTitle;

    @BindView(R.id.edit_num)
    EditText mEtNum;

    @BindView(R.id.verifity_code)
    EditText mEtVerifiCode;

    @BindView(R.id.btn_finish)
    Button mBtFinish;

    @BindView(R.id.tv_phonecode)
    TextView textView;
    private String mVerificode;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sure_phone;
    }

    @Override
    protected void initWidget() {
        setUnBinder(ButterKnife.bind(this));

        Intent intent = getIntent();
        mVerificode = intent.getStringExtra("verificode");
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

    @OnClick({R.id.tv_phonecode, R.id.btn_finish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_phonecode:
                if (TextUtils.isEmpty(getPhoneNum())) {
                    onToast("请输入手机号码！");
                    return;
                }
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
                        .doGetVerifyCodeApiCall(new GetVerifyCodeRequest(getPhoneNum(), 3, 1))
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
                if (!TCUtils.isPhoneNumValid(getPhoneNum())) {
                    onToast("请输入正确的手机号");
                    return;
                }
                if (TextUtils.isEmpty(getVerificode())) {
                    onToast("请输入验证码");
                    return;
                }
                showLoading();
                new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                        .doModifyPhoneApiCall(new ModifyPhoneRequest(mVerificode, getPhoneNum(), getVerificode()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<ModifyPhoneResponse>() {
                            @Override
                            public void accept(@NonNull ModifyPhoneResponse modifyPhoneResponse) throws Exception {
                                if (modifyPhoneResponse.getCode() == 0) {
                                    onToast("修改手机号成功", Gravity.CENTER, 0, 0);
                                    Intent intent = new Intent();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("phonenumber", getPhoneNum());
                                    intent.putExtras(bundle);
                                    setResult(PersonalInfoActivity.REQUSST_PHONENUM, intent);
                                    finish();
                                } else {
                                    if (modifyPhoneResponse.getCode() == 4043) {
                                        onToast("该手机号已被绑定");
                                    } else {
                                        onToast("验证码有误");
                                    }
                                }
                                hideLoading();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                hideLoading();
                                onToast("修改失败");
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

    private String getVerificode() {
        return mEtVerifiCode.getText().toString().trim();
    }
}
