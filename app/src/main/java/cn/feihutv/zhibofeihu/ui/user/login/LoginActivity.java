package cn.feihutv.zhibofeihu.ui.user.login;


import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.main.MainActivity;
import cn.feihutv.zhibofeihu.ui.user.phoneLogin.PhoneLoginActivity;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.weiget.dialog.DialogForce;
import cn.feihutv.zhibofeihu.utils.weiget.dialog.RxDialogSureCancel;

/**
 * <pre>
 *     author : liwen.chen
 *     time   : 2017-09-29
 *     desc   : 登录首页
 *     version: 1.0
 * </pre>
 */
public class LoginActivity extends BaseActivity implements LoginMvpView {

    @BindView(R.id.login_tv_yk)
    TextView textView;

    @Inject
    LoginMvpPresenter<LoginMvpView> mPresenter;

    public static LoginActivity loginActivitySelf;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initWidget() {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this));

        //persenter调用初始化
        mPresenter.onAttach(LoginActivity.this);

        //获取设备id
        mPresenter.getUserDeviceId();
        loginActivitySelf = this;

        StatusBarUtil.setTransparentForImageView(getActivity(), textView);


        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getBooleanExtra("force", false)) {
                DialogForce dialogForce = new DialogForce(getActivity());
                dialogForce.show();
            } else if (intent.getBooleanExtra("bannedLogin", false)) {
                String msg = intent.getStringExtra("Msg");
                String QQ = intent.getStringExtra("QQ");
                String Duration = intent.getStringExtra("Duration");
                String str = "您因【" + msg + "】被系统封号了，封号时长为【" + Duration + "】分钟，若有疑问请联系飞虎客服（QQ：" + QQ + "）";
                showForbidLoginDlg(str, QQ);
            }
        }

    }

    @Override
    protected void onDestroy() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void goPhoneLogin() {
        // 跳转到手机登录
        startActivity(new Intent(getActivity(), PhoneLoginActivity.class));
    }

    @Override
    public void openMainActivity() {
        startActivity(new Intent(getActivity(), MainActivity.class));
        finish();
    }

    @Override
    public Activity getActivity() {
        return LoginActivity.this;
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

    @OnClick({R.id.login_tv_yk, R.id.rbqq, R.id.rbwx, R.id.rbsina, R.id.rbphone, R.id.login_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_tv_yk:
                mPresenter.guestLogin();
                break;
            case R.id.rbqq:
                mPresenter.platformLogin(SHARE_MEDIA.QQ);
                break;
            case R.id.rbwx:
                mPresenter.platformLogin(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.rbsina:
                mPresenter.platformLogin(SHARE_MEDIA.SINA);
                break;
            case R.id.rbphone:
                mPresenter.openPhoneLoginActivity();
                break;
            case R.id.login_btn:
                if (UMShareAPI.get(getApplicationContext()).isInstall(getActivity(), SHARE_MEDIA.QQ)) {
                    mPresenter.platformLogin(SHARE_MEDIA.QQ);
                } else {
                    if (UMShareAPI.get(getApplicationContext()).isInstall(getActivity(), SHARE_MEDIA.WEIXIN)) {
                        mPresenter.platformLogin(SHARE_MEDIA.WEIXIN);
                    } else {
                        mPresenter.platformLogin(SHARE_MEDIA.SINA);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
