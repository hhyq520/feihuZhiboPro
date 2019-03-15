package cn.feihutv.zhibofeihu.ui.user.phoneLogin;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.ui.main.MainActivity;
import cn.feihutv.zhibofeihu.ui.user.forgetPsd.ForgetPsdActivity;
import cn.feihutv.zhibofeihu.ui.user.login.LoginActivity;
import cn.feihutv.zhibofeihu.ui.user.register.RegisterActivity;
import cn.feihutv.zhibofeihu.utils.weiget.dialog.RxDialogSureCancel;
import cn.feihutv.zhibofeihu.utils.weiget.view.LineEditText;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

/**
 * <pre>
 *     author : liwen.chen
 *     desc   : 手机登录界面
 *     version: 1.0
 * </pre>
 */
public class PhoneLoginActivity extends BaseActivity implements PhoneLoginMvpView {

    @BindView(R.id.login_back)
    TextView textView;
    @BindView(R.id.login_edit_account)
    EditText loginEditAccount;
    @BindView(R.id.login_ll1)
    LinearLayout loginLl1;
    @BindView(R.id.login_edit_psd)
    EditText loginEditPsd;
    @BindView(R.id.login_ll2)
    LinearLayout loginLl2;
    @BindView(R.id.login_btn)
    Button loginBtn;
    @BindView(R.id.tv_forget)
    TextView tvForget;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    private boolean isDialog = false;

    @Inject
    PhoneLoginMvpPresenter<PhoneLoginMvpView> mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_phone_login;
    }

    @Override
    protected void initWidget() {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this));

        //persenter调用初始化
        mPresenter.onAttach(PhoneLoginActivity.this);

        StatusBarUtil.setTransparentForImageView(getActivity(), textView);

        Intent intent = getIntent();
        if (intent != null) {
            isDialog = intent.getBooleanExtra("isdialog", false);
        }
    }

    @Override
    protected void onDestroy() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    protected void eventOnClick() {

        Observable<CharSequence> accountObservable = RxTextView.textChanges(loginEditAccount).skip(1);
        Observable<CharSequence> psdObservable = RxTextView.textChanges(loginEditPsd).skip(1);

        Observable.combineLatest(accountObservable, psdObservable, new BiFunction<CharSequence, CharSequence, Boolean>() {
            @Override
            public Boolean apply(@NonNull CharSequence charSequence, @NonNull CharSequence charSequence2) throws Exception {

                boolean isAvailableUsername = !TextUtils.isEmpty(getAccount());

                boolean isAvailablePassword = !TextUtils.isEmpty(getPsd());

                return isAvailableUsername && isAvailablePassword;
            }
        }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception {
                loginBtn.setEnabled(aBoolean);
            }
        });
    }

    @OnClick({R.id.login_back, R.id.login_btn, R.id.tv_forget, R.id.tv_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_back:
                if (isDialog) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    FeihuZhiboApplication.getApplication().mDataManager.setUserAsLoggedOut();
                    finish();
                } else {
                    finish();
                }
                break;
            case R.id.login_btn:
                MobclickAgent.onEvent(PhoneLoginActivity.this, "10004");
                hideKeyboard();
                mPresenter.phoneLogin(getAccount(), getPsd(), isDialog);
                break;
            case R.id.tv_forget:
                MobclickAgent.onEvent(PhoneLoginActivity.this, "10005");
                startActivity(new Intent(this, ForgetPsdActivity.class));
                break;
            case R.id.tv_register:
                MobclickAgent.onEvent(PhoneLoginActivity.this, "10001");
                Intent intent = new Intent(this, RegisterActivity.class);
                intent.putExtra("isdialog", isDialog);
                startActivity(intent);
                break;
            default:
                break;

        }
    }

    @Override
    public void openMainActivity() {
        if (MainActivity.MainActivityself != null) {
            MainActivity.MainActivityself.finish();
        }
        if (LoginActivity.loginActivitySelf != null) {
            LoginActivity.loginActivitySelf.finish();
        }
        startActivity(new Intent(getActivity(), MainActivity.class));
        finish();
    }

    @Override
    public void showForbidLoginDlg(String str, final String qq) {
        final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(getActivity());//提示弹窗
        rxDialogSureCancel.setContent(str);
        rxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager)
                        getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("id", qq);
                clipboard.setPrimaryClip(clip);
                onToast("复制成功！");
                rxDialogSureCancel.cancel();
            }
        });
        rxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rxDialogSureCancel.cancel();
            }
        });
        rxDialogSureCancel.show();
    }

    @Override
    public Activity getActivity() {
        return PhoneLoginActivity.this;
    }

    private String getAccount() {
        return loginEditAccount.getText().toString().trim();
    }

    private String getPsd() {
        return loginEditPsd.getText().toString().trim();
    }
}
