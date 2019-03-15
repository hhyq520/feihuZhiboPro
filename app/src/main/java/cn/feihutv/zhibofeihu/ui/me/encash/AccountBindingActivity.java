package cn.feihutv.zhibofeihu.ui.me.encash;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.me.BindAlipayRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.BindAlipayResponse;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.widget.TCActivityTitle;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import cn.feihutv.zhibofeihu.utils.TCConstants;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/3 17:10
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class AccountBindingActivity extends BaseActivity {

    @BindView(R.id.account_back)
    TCActivityTitle mTitle;

    @BindView(R.id.account_edit)
    EditText account_edit;

    @BindView(R.id.name_edit)
    EditText name_edit;

    @BindView(R.id.tip_text)
    TextView tip_text;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_accountbinding;
    }

    @Override
    protected void initWidget() {
        setUnBinder(ButterKnife.bind(this));

        String str = "<font color='#A1A0A0'>请确保支付宝账号与实名认证的账号</font>" +
                "<font color='#EF3425'>[一致]</font>" +
                "<font color='#A0A0A0'>并且输入</font>" +
                "<font color='#EF3425'>[正确]</font>" +
                "<font color='#A0A0A0'>,否则绑定后</font>" +
                "<font color='#EF3425'>[无法提现]</font>";
        tip_text.setText(Html.fromHtml(str));
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

    @OnClick(R.id.btn_bind)
    public void onViewClicked() {
        if (TextUtils.isEmpty(getAccount())) {
            onToast("请输入帐号", Gravity.CENTER, 0, 0);
            return;
        }
        if (TextUtils.isEmpty(getName())) {
            onToast("请输入姓名", Gravity.CENTER, 0, 0);
            return;
        }
        if (!isNetworkConnected()) {
            onToast("请检查您的网络", Gravity.CENTER, 0, 0);
            return;
        }
        showLoading();
        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .doBindAlipayCall(new BindAlipayRequest(getAccount(), getName()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BindAlipayResponse>() {
                    @Override
                    public void accept(@NonNull BindAlipayResponse bindAlipayResponse) throws Exception {
                        if (bindAlipayResponse.getCode() == 0) {
                            SharePreferenceUtil.saveSeesion(AccountBindingActivity.this, TCConstants.ALIPAYACCOUNT,getAccount());
                            showDialog();
                        } else {
                            if (bindAlipayResponse.getCode() == 4344) {
                                onToast("绑定失败,需和实名认证的姓名一致！", Gravity.CENTER, 0, 0);
                            } else if (bindAlipayResponse.getCode() == 4343) {
                                onToast("请先实名认证！", Gravity.CENTER, 0, 0);
                            } else {
                                onToast("绑定失败", Gravity.CENTER, 0, 0);
                            }
                            AppLogger.i(bindAlipayResponse.getCode() + " " + bindAlipayResponse.getErrMsg());
                        }
                        hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        hideLoading();
                        onToast("绑定失败", Gravity.CENTER, 0, 0);
                    }
                }));
    }

    private String getAccount() {
        return account_edit.getText().toString().trim();
    }

    private String getName() {
        return name_edit.getText().toString().trim();
    }

    private void showDialog() {
        final Dialog pickDialog2 = new Dialog(this, R.style.color_dialog);
        pickDialog2.setContentView(R.layout.dialog_bind_succ);

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog2.getWindow();
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.CENTER);
        lp.width = (int) (display.getWidth()); //设置宽度

        pickDialog2.getWindow().setAttributes(lp);

        Button button = (Button) pickDialog2.findViewById(R.id.btn_pop_renzheng);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountBindingActivity.this, IncomeActivity.class));
                finish();
                pickDialog2.dismiss();
            }
        });
        pickDialog2.setCanceledOnTouchOutside(false);
        pickDialog2.show();
    }
}
