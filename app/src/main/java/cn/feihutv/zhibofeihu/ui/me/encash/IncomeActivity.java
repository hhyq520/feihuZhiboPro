package cn.feihutv.zhibofeihu.ui.me.encash;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.ValueChangePush;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.rxbus.RxBusSubscribe;
import cn.feihutv.zhibofeihu.rxbus.ThreadMode;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.user.UserAgreement;
import cn.feihutv.zhibofeihu.ui.widget.TCActivityTitle;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import cn.feihutv.zhibofeihu.utils.TCConstants;
import cn.feihutv.zhibofeihu.utils.TCUtils;

/**
 * <pre>
 *     author : liwen.chen
 *     time   :
 *     desc   : 收益提现
 *     version: 1.0
 * </pre>
 */
public class IncomeActivity extends BaseActivity implements IncomeMvpView {

    @Inject
    IncomeMvpPresenter<IncomeMvpView> mPresenter;

    @BindView(R.id.gold_count)
    TextView goldCount;

    @BindView(R.id.tv_money)
    TextView tvMoney;

    @BindView(R.id.account_id)
    TextView accountId;

    @BindView(R.id.gold_edit)
    EditText goldEdit;

    @BindView(R.id.real_money)
    TextView realMoney;

    @BindView(R.id.account_back)
    TCActivityTitle mTitle;

    private int mGold;
    private String mIncrease;
    private String mAlipayAccount;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_income;
    }


    @Override
    protected void initWidget() {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this));

        //persenter调用初始化
        mPresenter.onAttach(IncomeActivity.this);

        initView();
    }

    private void initView() {
        mIncrease = SharePreferenceUtil.getSession(IncomeActivity.this, TCConstants.INCREASE);
        mAlipayAccount = SharePreferenceUtil.getSession(IncomeActivity.this, TCConstants.ALIPAYACCOUNT);
        mGold = SharePreferenceUtil.getSessionInt(IncomeActivity.this, TCConstants.GHB);

        goldCount.setText(String.valueOf(mGold));
        tvMoney.setText(TCUtils.toDecimal((mGold * 0.01 * Float.valueOf(mIncrease))));
        accountId.setText(TCUtils.hidePhoneMid(mAlipayAccount));
    }

    @Override
    protected void eventOnClick() {

        mTitle.setReturnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        goldEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == KeyEvent.KEYCODE_ENDCALL) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    if (!TextUtils.isEmpty(getGoldEdit())) {
                        if (Integer.valueOf(getGoldEdit()) < 10000) {
                            onToast("请输入大于10000金虎币", Gravity.CENTER, 0, 0);
                            realMoney.setText("");
                            return true;
                        }
                        if (Integer.valueOf(getGoldEdit()) % 100 != 0) {
                            onToast("请输入100整数倍金虎币", Gravity.CENTER, 0, 0);
                            realMoney.setText("");
                            return true;
                        }
                        float money = Integer.valueOf(getGoldEdit()) * Float.valueOf(mIncrease) * 0.01f;
                        realMoney.setText(String.valueOf(money));
                    }
                    return true;
                }
                return false;
            }
        });


        goldEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    if (Integer.valueOf(s.toString()) < 10000) {
                        realMoney.setText(String.valueOf(""));
                        realMoney.setHint(R.string.text23);
                        return;
                    }
                    if (Integer.valueOf(s.toString()) % 100 != 0) {
                        realMoney.setText(String.valueOf(""));
                        realMoney.setHint(R.string.text23);
                        return;
                    }
                    float money = Integer.valueOf(s.toString()) * Float.valueOf(mIncrease) * 0.01f;
                    realMoney.setText(String.valueOf(money));
                } else {
                    realMoney.setText("");
                    realMoney.setHint(R.string.text23);
                }
            }
        });
    }

    @OnClick({R.id.change_account, R.id.xieyi, R.id.btn_tixian})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.change_account:
                Intent intent1 = new Intent(IncomeActivity.this, ChangeZFActivty.class);
                startActivityForResult(intent1, 0x001);
                break;
            case R.id.xieyi:
                Intent intent = new Intent(this, UserAgreement.class);
                intent.putExtra("url", mPresenter.getSysConfig().getUseragreementUrl() + "?rand=" + System.currentTimeMillis());
                intent.putExtra("title", "用户协议");
                startActivity(intent);
                break;
            case R.id.btn_tixian:
                if (!TextUtils.isEmpty(getGoldEdit())) {
                    if (Integer.valueOf(getGoldEdit()) < 10000) {
                        onToast("请输入大于10000金虎币", Gravity.CENTER, 0, 0);
                        return;
                    }
                    if (Integer.valueOf(getGoldEdit()) % 100 != 0) {
                        onToast("请输入100整数倍金虎币", Gravity.CENTER, 0, 0);
                        return;
                    }
                    mPresenter.enCash(getGoldEdit());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void showSucc() {
        realMoney.setText("");
        goldEdit.setText("");
        realMoney.setHint(getResources().getString(R.string.text23));
        goldEdit.setHint(getResources().getString(R.string.text21));
        showDialog();

    }

    private void showDialog() {
        final Dialog pickDialog2 = new Dialog(this, R.style.color_dialog);
        pickDialog2.setContentView(R.layout.dialog_tixian_succ);

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
                pickDialog2.dismiss();
            }
        });
        pickDialog2.setCanceledOnTouchOutside(false);
        pickDialog2.show();
    }

    private String getGoldEdit() {
        return goldEdit.getText().toString().trim();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x001 && resultCode == 0x002) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                String str = bundle.getString(TCConstants.ALIPAYACCOUNT);
                accountId.setText(TCUtils.hidePhoneMid(str));
            }
        }
    }

    // 数值变化
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_VALUE_CHANGE, threadMode = ThreadMode.MAIN)
    public void onReceiveValueChangePush(ValueChangePush pushData) {
        if (pushData != null) {
            if ("GHB".equals(pushData.getType())) {
                goldCount.setText(String.valueOf(pushData.getNewVal()));
                tvMoney.setText(TCUtils.toDecimal((pushData.getNewVal() * Float.valueOf(mIncrease) * 0.01)));
            }
        }
    }

    @Override
    protected void onDestroy() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroy();
    }

}
