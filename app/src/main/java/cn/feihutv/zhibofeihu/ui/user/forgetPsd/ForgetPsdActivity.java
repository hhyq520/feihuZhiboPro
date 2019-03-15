package cn.feihutv.zhibofeihu.ui.user.forgetPsd;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.umeng.analytics.MobclickAgent;

import java.lang.ref.WeakReference;

import cn.feihutv.zhibofeihu.R;

import cn.feihutv.zhibofeihu.ui.main.MainActivity;
import cn.feihutv.zhibofeihu.ui.user.forgetPsd.ForgetPsdMvpView;
import cn.feihutv.zhibofeihu.ui.user.forgetPsd.ForgetPsdMvpPresenter;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.utils.TCUtils;

/**
 * <pre>
 *     author : huang hao
 *     time   :
 *     desc   : 界面绘制
 *     version: 1.0
 * </pre>
 */
public class ForgetPsdActivity extends BaseActivity implements ForgetPsdMvpView {

    @Inject
    ForgetPsdMvpPresenter<ForgetPsdMvpView> mPresenter;
    @BindView(R.id.tv_getcode)
    TextView tvGetCode;

    @BindView(R.id.forget_edit_num)
    EditText editPhone;

    @BindView(R.id.edit_newpsd)
    EditText editNewPsd;

    @BindView(R.id.edit_surepsd)
    EditText editSurePsd;

    @BindView(R.id.edit_verifity)
    EditText editVerifiCode;

    @BindView(R.id.btn_login)
    Button btnSure;

    @BindView(R.id.forget_back)
    TextView textView;

    private boolean isAvailableUsername, isAvailableVerification, isAvailablePsd, isAvailablePsdAgain;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget_psd;
    }


    @Override
    protected void initWidget() {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this));

        //persenter调用初始化
        mPresenter.onAttach(ForgetPsdActivity.this);

        StatusBarUtil.setTransparentForImageView(getActivity(), textView);
    }


    @Override
    protected void onDestroy() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroy();
    }

    @OnClick({R.id.forget_back, R.id.tv_getcode, R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forget_back:
                MobclickAgent.onEvent(ForgetPsdActivity.this, "10006");
                finish();
                break;
            case R.id.tv_getcode:
                mPresenter.sendRegistVerifyCode(getPhoneNum());
                break;
            case R.id.btn_login:
                MobclickAgent.onEvent(ForgetPsdActivity.this, "10147");
                mPresenter.sureToLogin(getPhoneNum(), getFirstPsd(), getSurePsd(), getVerification());
                break;
            default:
                break;
        }
    }

    @Override
    public void openMainActivity() {
        startActivity(new Intent(getActivity(), MainActivity.class));
    }

    @Override
    public Activity getActivity() {
        return ForgetPsdActivity.this;
    }

    @Override
    public void startTimer() {
        TCUtils.startTimer(getApplicationContext(), new WeakReference<>(tvGetCode), getResources().getString(R.string.getCode), 60, 1);
    }

    /**
     * 获取输入的手机号码
     *
     * @return
     */
    private String getPhoneNum() {
        return editPhone.getText().toString().trim();
    }

    /**
     * 获取输入的验证码
     *
     * @return
     */
    private String getVerification() {
        return editVerifiCode.getText().toString().trim();
    }

    /**
     * 获取输入的新密码
     *
     * @return
     */
    private String getFirstPsd() {
        return editNewPsd.getText().toString().trim();
    }

    /**
     * 获取再次输入的密码
     *
     * @return
     */
    private String getSurePsd() {
        return editSurePsd.getText().toString().trim();
    }

    /**
     * 是否将获取验证码按钮设置为可用状态
     */
    private void canGetVerification() {
        if (isAvailableUsername) {
            tvGetCode.setEnabled(true);
        } else {
            tvGetCode.setEnabled(false);
        }
    }

    /**
     * 是否将确定忘记密码按钮设置为可用状态
     */
    private void canSureToLogin() {
        if (isAvailableUsername && isAvailablePsd && isAvailableVerification && isAvailablePsdAgain) {
            btnSure.setEnabled(true);
        } else {
            btnSure.setEnabled(false);
        }
    }

    /**
     * 检测edittext输入状态 并根据输入的内容 设置相应的boolean值
     *
     * @param editText
     */
    private boolean checkAvailable(EditText editText) {
        if (!TextUtils.isEmpty(editText.getText().toString().trim())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void eventOnClick() {
        editPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isAvailableUsername = checkAvailable(editPhone);
                canGetVerification();
                canSureToLogin();
            }
        });

        editVerifiCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isAvailableVerification = checkAvailable(editVerifiCode);
                canSureToLogin();
            }
        });

        editNewPsd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isAvailablePsd = checkAvailable(editNewPsd);
                canSureToLogin();
            }
        });

        editSurePsd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                isAvailablePsdAgain = checkAvailable(editSurePsd);
                canSureToLogin();
            }
        });

    }
}
