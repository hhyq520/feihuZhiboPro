package cn.feihutv.zhibofeihu.ui.me.encash;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
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
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import cn.feihutv.zhibofeihu.utils.TCConstants;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/4 11:14
 *      desc   : 更换支付宝帐号
 *      version: 1.0
 * </pre>
 */

public class ChangeZFActivty extends BaseActivity {

    @BindView(R.id.account_back)
    TCActivityTitle mTitle;

    @BindView(R.id.old_no_edit)
    TextView old_no_edit;

    @BindView(R.id.new_no_edit)
    EditText new_no_edit;

    @BindView(R.id.name_edit)
    EditText name_edit;

    @BindView(R.id.tip_text)
    TextView tip_text;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_zhifu_account;
    }

    @Override
    protected void initWidget() {
        setUnBinder(ButterKnife.bind(this));
        old_no_edit.setText(TCUtils.hidePhoneMid(SharePreferenceUtil.getSession(ChangeZFActivty.this, TCConstants.ALIPAYACCOUNT)));
        String str = "<font color='#A1A0A0'>请确保支付宝账号与实名认证的账号</font>" +
                "<font color='#FF6666'>[一致]</font>" +
                "<font color='#A1A0A0'>并且输入</font>" +
                "<font color='#FF6666'>[正确]</font>" +
                "<font color='#A1A0A0'>,否则绑定后</font>" +
                "<font color='#FF6666'>[无法提现]</font>";
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

    private String getEditNewAccount() {
        return new_no_edit.getText().toString().trim();
    }

    private String getEditName() {
        return name_edit.getText().toString().trim();
    }


    @OnClick(R.id.btn_tixian)
    public void onViewClicked() {

        if (TextUtils.isEmpty(getEditNewAccount())) {
            return;
        }

        if (TextUtils.isEmpty(getEditName())) {

        }

        if (!isNetworkConnected()) {
            return;
        }
        showLoading();

        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .doBindAlipayCall(new BindAlipayRequest(getEditNewAccount(), getEditName()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<BindAlipayResponse>() {
                    @Override
                    public void accept(@NonNull BindAlipayResponse bindAlipayResponse) throws Exception {
                        if (bindAlipayResponse.getCode() == 0) {
                            SharePreferenceUtil.saveSeesion(ChangeZFActivty.this, TCConstants.ALIPAYACCOUNT, getEditNewAccount());
                            showDialog();
                        } else {
                            if (bindAlipayResponse.getCode() == 4344) {
                                onToast("绑定失败，请填写与实名认证一致的姓名", Gravity.CENTER, 0, 0);
                            } else {
                                onToast("绑定失败", Gravity.CENTER, 0, 0);
                            }
                        }
                        hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        hideLoading();
                        onToast("绑定失败，请检查您的网络");
                    }
                }));

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
        button.setText("去提现");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString(TCConstants.ALIPAYACCOUNT, getEditNewAccount());
                intent.putExtras(bundle);
                setResult(0x002, intent);
                finish();
                pickDialog2.dismiss();
            }
        });
        pickDialog2.setCanceledOnTouchOutside(false);
        pickDialog2.show();
    }
}
