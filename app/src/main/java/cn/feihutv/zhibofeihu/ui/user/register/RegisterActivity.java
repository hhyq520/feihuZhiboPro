package cn.feihutv.zhibofeihu.ui.user.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.umeng.analytics.MobclickAgent;

import java.lang.ref.WeakReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import cn.feihutv.zhibofeihu.R;

import cn.feihutv.zhibofeihu.ui.main.MainActivity;
import cn.feihutv.zhibofeihu.ui.user.UserAgreement;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.TCConstants;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.feihutv.zhibofeihu.utils.weiget.dialog.RxDialogSureCancel;


/**
 * <pre>
 *     author : huang hao
 *     desc   : 注册页面
 *     version: 1.0
 * </pre>
 */
public class RegisterActivity extends BaseActivity implements RegisterMvpView {

    @Inject
    RegisterMvpPresenter<RegisterMvpView> mPresenter;
    @BindView(R.id.register_phone)
    EditText edit_phone;

    @BindView(R.id.register_verification)
    EditText edit_verification;

    @BindView(R.id.register_nickname)
    EditText edit_nickname;

    @BindView(R.id.register_pwd)
    EditText edit_pwd;

    @BindView(R.id.register_first_psd)
    EditText edit_first_psd;

    @BindView(R.id.register_tv_getcode)
    TextView register_tv_getcode;

    @BindView(R.id.register_btn_login)
    Button btnRegister;

    @BindView(R.id.login_back)
    TextView textView;

    @BindView(R.id.img_agree)
    ImageView img_agree;

    private int sourceLen;
    private int destLen;
    private boolean isDialog = false;
    private boolean isAvailableUsername, isAvailablePsd, isAvailableVerification, isAvailablePsdAgain, isAvailableNick;
    private boolean isAgree = true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }


    @Override
    protected void initWidget() {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this));

        //persenter调用初始化
        mPresenter.onAttach(RegisterActivity.this);

        StatusBarUtil.setTransparentForImageView(getActivity(), textView);

        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            isDialog = intent.getBooleanExtra("isdialog", false);
        }
        String str = "abcdefghijklmnopqrstuvwxyz";
        String strNew = "";
        char[] b = str.toCharArray();
        for (int i = 0; i < 4; i++) {
            strNew += str.charAt((int) (Math.random() * 26));
        }
        edit_nickname.setText(strNew);

        isAvailableNick = checkAvailable(edit_nickname);
    }

    public String stringFilter(String str) throws PatternSyntaxException {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“'。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("");
    }

    @Override
    protected void onDestroy() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroy();
    }

    @OnClick({R.id.login_back, R.id.register_btn_login, R.id.register_tv_getcode, R.id.tv_xy, R.id.img_agree})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_xy:
                Intent intent = new Intent(this, UserAgreement.class);
                intent.putExtra("url", mPresenter.getSysConfig().getUseragreementUrl() + "?rand=" + System.currentTimeMillis());
                intent.putExtra("title", "用户协议");
                startActivity(intent);
                break;
            case R.id.login_back:
                MobclickAgent.onEvent(RegisterActivity.this, "10003");
                finish();
                break;
            case R.id.img_agree:
                if (isAgree) {
                    img_agree.setImageResource(R.drawable.check_cancel);
                    isAgree = false;
                } else {
                    img_agree.setImageResource(R.drawable.check);
                    isAgree = true;
                }
                canRegister();
                break;
            case R.id.register_tv_getcode:
                mPresenter.sendRegistVerifyCode(getPhoneNum());
                break;
            case R.id.register_btn_login:
                MobclickAgent.onEvent(RegisterActivity.this, "10002");
                int length = getNick().length();
                if (length < 2) {
                    onToast("昵称在2-8位之间", Gravity.CENTER, 0, 0);
                    return;
                }
                if (isNetworkConnected()) {
                    mPresenter.createUser(getPhoneNum(), getFirstPsd(), getNick(), getPsdAgain(), getVerification(), isDialog);
                } else {
                    onToast("请求失败，请检查网络!", Gravity.CENTER, 0, 0);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public Activity getActivity() {
        return RegisterActivity.this;
    }

    @Override
    public void openMainActivity() {
        startActivity(new Intent(getActivity(), MainActivity.class));
    }

    @Override
    public void startTimer() {
        TCUtils.startTimer(getApplicationContext(), new WeakReference<>(register_tv_getcode), getResources().getString(R.string.getCode), 60, 1);
    }

    /**
     * 获取输入的手机号码
     * @return
     */
    private String getPhoneNum() {
        return edit_phone.getText().toString().trim();
    }

    /**
     * 获取输入的验证码
     * @return
     */
    private String getVerification() {
        return edit_verification.getText().toString().trim();
    }

    /**
     * 获取输入的密码
     * @return
     */
    private String getFirstPsd() {
        return edit_first_psd.getText().toString().trim();
    }

    /**
     * 获取再次输入的密码
     * @return
     */
    private String getPsdAgain() {
        return edit_pwd.getText().toString().trim();
    }

    /**
     * 获取注册时输入的昵称
     * @return
     */
    private String getNick() {
        return edit_nickname.getText().toString().trim();
    }

    /**
     * 是否将获取验证码按钮设置为可用状态
     */
    private void canGetVerification() {
        if(isAvailableUsername){
            register_tv_getcode.setEnabled(true);
        }else {
            register_tv_getcode.setEnabled(false);
        }
    }

    /**
     * 是否将立即注册按钮设置为可用状态
     */
    private void canRegister() {
        if(isAvailableUsername && isAvailableNick && isAvailablePsd && isAvailablePsdAgain && isAvailableVerification && isAgree){
            btnRegister.setEnabled(true);
        }else {
            btnRegister.setEnabled(false);
        }
    }

    /**
     * 检测edittext输入状态 并根据输入的内容 设置相应的boolean值
     * @param editText
     */
    private boolean checkAvailable(EditText editText){
        if(!TextUtils.isEmpty(editText.getText().toString().trim())){
            return true;
        }else {
            return false;
        }
    }

    @Override
    protected void eventOnClick() {
        edit_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isAvailableUsername = checkAvailable(edit_phone);
                canGetVerification();
                canRegister();
            }
        });

        edit_verification.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isAvailableVerification = checkAvailable(edit_verification);
                canRegister();
            }
        });

        edit_first_psd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isAvailablePsd = checkAvailable(edit_first_psd);
                canRegister();
            }
        });

        edit_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isAvailablePsdAgain = checkAvailable(edit_pwd);
                canRegister();
            }
        });

        edit_nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String editable = getNick();
                String str = stringFilter(editable); //过滤特殊字符
                if (!editable.equals(str)) {
                    edit_nickname.setText(str);
                }
                edit_nickname.setSelection(edit_nickname.length());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                isAvailableNick = checkAvailable(edit_nickname);
                canRegister();
            }
        });
    }
}
