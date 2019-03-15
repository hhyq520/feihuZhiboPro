package cn.feihutv.zhibofeihu.ui.me.recharge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.alipay.sdk.pay.PayResult;
import cn.feihutv.zhibofeihu.BuildConfig;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.db.model.SysHBBean;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.ValueChangePush;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.rxbus.RxBusSubscribe;
import cn.feihutv.zhibofeihu.rxbus.ThreadMode;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.ui.widget.TCActivityTitle;
import cn.feihutv.zhibofeihu.utils.AppConstants;
import cn.feihutv.zhibofeihu.utils.CustomDialogUtils;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import cn.feihutv.zhibofeihu.utils.TCActivityRecharge;
import cn.feihutv.zhibofeihu.utils.UiUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *     author : liwen.chen
 *     time   :
 *     desc   : 充值
 *     version: 1.0
 * </pre>
 */
public class RechargeActivity extends BaseActivity implements RechargeMvpView {

    @Inject
    RechargeMvpPresenter<RechargeMvpView> mPresenter;

    @BindView(R.id.btn_cz)
    Button btnCz;
    @BindView(R.id.define_edit)
    EditText defineEdit;
    private String TAG = "rechargeActivity";

    @BindView(R.id.zb_eui_edit)
    TCActivityTitle atTitle;

    @BindView(R.id.tca1)
    TCActivityRecharge tca1;

    @BindView(R.id.tca2)
    TCActivityRecharge tca2;

    @BindView(R.id.tca3)
    TCActivityRecharge tca3;

    @BindView(R.id.tca4)
    TCActivityRecharge tca4;

    @BindView(R.id.tca5)
    TCActivityRecharge tca5;

    @BindView(R.id.tca6)
    TCActivityRecharge tca6;

    @BindView(R.id.btn_pay_zfb)
    Button btn_pay_zfb;

    @BindView(R.id.btn_pay_wx)
    Button btn_pay_wx;


    @BindView(R.id.tv_hubi)
    TextView tvHubi;

    private boolean isChoose = false;

    private int flag = 0;

    private int payType;

    private int hubiKind = 0;
    private IWXAPI api;

    private static final int PAY_ALIPAY = 1;
    private static final int PAY_WXPAY = 2;
    private String from = "";


    @Override
    protected int getLayoutId() {
        return R.layout.activity_recharge;
    }


    @Override
    protected void initWidget() {
        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this));

        //persenter调用初始化
        mPresenter.onAttach(RechargeActivity.this);

        init();
    }

    private void init() {
        tvHubi.setText(String.valueOf(mPresenter.getUserData().gethB()));
        mPresenter.getSysHBBean();
        api = WXAPIFactory.createWXAPI(this, AppConstants.WX_APP_ID);
        api.registerApp(AppConstants.WX_APP_ID);
        Intent intent = getIntent();
        if (intent != null) {
            from = intent.getStringExtra("fromWhere");
            SharePreferenceUtil.saveSeesion(RechargeActivity.this, "charge_from", from);
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("type", "跳转到充值");
            map.put("from", from);
            MobclickAgent.onEvent(this, "10106", map);
        }
        defineEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLastState(flag);
                if (defineEdit != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(defineEdit, 0);
                }
                flag = 0;
            }
        });
    }

    @Override
    protected void eventOnClick() {
        atTitle.setReturnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MobclickAgent.onEvent(RechargeActivity.this, "10107");
                finish();
            }
        });

        atTitle.setMoreListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RechargeActivity.this, PrepaidrecordsActivity.class));
            }
        });

        tca1.setOnLinstenerTextColor(new TCActivityRecharge.onLinstenerTextColor() {
            @Override
            public void onSetColor(TextView tvNum, TextView tvMoney) {
                setLastState(flag);
                flag = 1;
                hubiKind = 2000;
                setBg();
                tca1.setBackgroundResource(R.drawable.chongzhi_shape);
                tvNum.setTextColor(getResources().getColor(R.color.app_white));
                tvMoney.setTextColor(getResources().getColor(R.color.app_white));
            }
        });

        tca2.setOnLinstenerTextColor(new TCActivityRecharge.onLinstenerTextColor() {
            @Override
            public void onSetColor(TextView tvNum, TextView tvMoney) {
                setLastState(flag);
                flag = 2;
                hubiKind = 5000;
                setBg();
                tca2.setBackgroundResource(R.drawable.chongzhi_shape);
                tvNum.setTextColor(getResources().getColor(R.color.app_white));
                tvMoney.setTextColor(getResources().getColor(R.color.app_white));
            }
        });

        tca3.setOnLinstenerTextColor(new TCActivityRecharge.onLinstenerTextColor() {
            @Override
            public void onSetColor(TextView tvNum, TextView tvMoney) {
                setLastState(flag);
                flag = 3;
                hubiKind = 10000;
                setBg();
                tca3.setBackgroundResource(R.drawable.chongzhi_shape);
                tvNum.setTextColor(getResources().getColor(R.color.app_white));
                tvMoney.setTextColor(getResources().getColor(R.color.app_white));
            }
        });

        tca4.setOnLinstenerTextColor(new TCActivityRecharge.onLinstenerTextColor() {
            @Override
            public void onSetColor(TextView tvNum, TextView tvMoney) {
                setLastState(flag);
                flag = 4;
                hubiKind = 50000;
                setBg();
                tca4.setBackgroundResource(R.drawable.chongzhi_shape);
                tvNum.setTextColor(getResources().getColor(R.color.app_white));
                tvMoney.setTextColor(getResources().getColor(R.color.app_white));
            }
        });

        tca5.setOnLinstenerTextColor(new TCActivityRecharge.onLinstenerTextColor() {
            @Override
            public void onSetColor(TextView tvNum, TextView tvMoney) {
                setLastState(flag);
                flag = 5;
                hubiKind = 100000;
                setBg();
                tca5.setBackgroundResource(R.drawable.chongzhi_shape);
                tvNum.setTextColor(getResources().getColor(R.color.app_white));
                tvMoney.setTextColor(getResources().getColor(R.color.app_white));
            }
        });

        tca6.setOnLinstenerTextColor(new TCActivityRecharge.onLinstenerTextColor() {
            @Override
            public void onSetColor(TextView tvNum, TextView tvMoney) {
                setLastState(flag);
                flag = 6;
                hubiKind = 300000;
                tca6.setBackgroundResource(R.drawable.chongzhi_shape);
                tvNum.setTextColor(getResources().getColor(R.color.app_white));
                tvMoney.setTextColor(getResources().getColor(R.color.app_white));
            }
        });


    }

    public void setLastState(int count) {
        switch (count) {
            case 1:
                tca1.setTextColor();
                defineEdit.setText("");
                hideKey();
                break;
            case 2:
                tca2.setTextColor();
                defineEdit.setText("");
                hideKey();
                break;
            case 3:
                tca3.setTextColor();
                defineEdit.setText("");
                hideKey();
                break;
            case 4:
                tca4.setTextColor();
                defineEdit.setText("");
                hideKey();
                break;
            case 5:
                tca5.setTextColor();
                defineEdit.setText("");
                hideKey();
                break;
            case 6:
                tca6.setTextColor();
                defineEdit.setText("");
                hideKey();
                break;
            case 0:
                defineEdit.setText("");
                hideKey();
                break;
            default:
                break;
        }
        setBg();
    }

    public void setBg() {
        tca1.setBackgroundResource(R.drawable.chongzhi_unselect_shape);
        tca2.setBackgroundResource(R.drawable.chongzhi_unselect_shape);
        tca3.setBackgroundResource(R.drawable.chongzhi_unselect_shape);
        tca4.setBackgroundResource(R.drawable.chongzhi_unselect_shape);
        tca5.setBackgroundResource(R.drawable.chongzhi_unselect_shape);
        tca6.setBackgroundResource(R.drawable.chongzhi_unselect_shape);
    }

    @Override
    protected void onDestroy() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroy();
    }

    private void hideKey() {
        if (defineEdit != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(defineEdit.getWindowToken(), 0);
        }
    }

    private void dealHander(final Message message) {
        Observable.create(new ObservableOnSubscribe<Message>() {
            @Override
            public void subscribe(ObservableEmitter<Message> e) throws Exception {
                e.onNext(message);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Message>() {
                    @Override
                    public void accept(@NonNull Message message) throws Exception {
                        int code = message.what;
                        switch (code) {
                            case PAY_ALIPAY:
                                PayResult payResult = new PayResult((Map<String, String>) message.obj);
                                /**
                                 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                                 */
                                String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                                String resultStatus = payResult.getResultStatus();
                                // 判断resultStatus 为9000则代表支付成功
                                if (TextUtils.equals(resultStatus, "9000")) {
                                    // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("type", "跳转到支付宝充值成功");
                                    map.put("from", from);
                                    MobclickAgent.onEvent(RechargeActivity.this, "10106", map);
                                    onToast("充值成功", Gravity.CENTER, 0, 0);
                                } else {
                                    MobclickAgent.onEvent(RechargeActivity.this, "10108");
                                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                                    onToast("支付失败", Gravity.CENTER, 0, 0);
                                }
                                break;
                            case PAY_WXPAY:
                                try {
                                    JSONObject json = new JSONObject(message.getData().getString("response"));
                                    if (null != json) {
                                        PayReq req = new PayReq();
                                        req.appId = json.getString("appid");
                                        req.partnerId = json.getString("partnerid");
                                        req.prepayId = json.getString("prepayid");
                                        req.packageValue = json.getString("package");
                                        req.nonceStr = json.getString("noncestr");
                                        req.timeStamp = json.getString("timestamp");
                                        req.sign = json.getString("sign");
                                        req.extData = "app data";
                                        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                                        api.registerApp(AppConstants.WX_APP_ID);
                                        api.sendReq(req);
                                    } else {
                                        MobclickAgent.onEvent(RechargeActivity.this, "10109");
                                        Log.d("PAY_GET", "返回错误" + json.getString("retmsg"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                            default:
                                break;
                        }
                    }
                });
    }

    @Override
    public void getSysHb(List<SysHBBean> sysHBBean) {
        if (sysHBBean != null && sysHBBean.size() > 0) {
            tca1.setNumText(2000 + "币");
            tca1.setPriceText(20 + "元");

            tca2.setNumText(5000 + "币");
            tca2.setPriceText(50 + "元");

            tca3.setNumText(10000 + "币");
            tca3.setPriceText(100 + "元");

            tca4.setNumText(50000 + "币");
            tca4.setPriceText(500 + "元");

            tca5.setNumText(100000 + "币");
            tca5.setPriceText(1000 + "元");

            tca6.setNumText(300000 + "币");
            tca6.setPriceText(3000 + "元");
        }
    }

    @Override
    public void paySucc(String pf, String strPay) {
        Log.e("pay", strPay);
        if (pf.equals("weixin")) {
            xwpay(strPay);
            btnCz.setEnabled(true);
        } else {
            alipay(strPay);
            btnCz.setEnabled(true);
        }
    }

    private void xwpay(String jsonObject) {
        if (UiUtil.isAppInstalled(this, "com.tencent.mm")) {
            boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
            if (isPaySupported) {
                Message message = new Message();
                message.what = PAY_WXPAY;
                Bundle b = new Bundle();
                b.putString("response", jsonObject);
                message.setData(b);
                dealHander(message);
                btnCz.setEnabled(true);
            } else {
                onToast("微信版本不支持支付");
            }

        } else {
            onToast("未安装微信");
        }
    }

    private void alipay(final String orderInfo) {
        onToast("获取订单中...", Gravity.CENTER, 0, 0);
        btnCz.setEnabled(true);
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(RechargeActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());
                Message msg = new Message();
                msg.what = PAY_ALIPAY;
                msg.obj = result;
                dealHander(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    @OnClick({R.id.rl_zfb, R.id.rl_wx, R.id.btn_cz, R.id.btn_pay_zfb, R.id.btn_pay_wx})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_zfb:
            case R.id.btn_pay_zfb:
                MobclickAgent.onEvent(RechargeActivity.this, "10104");
                isChoose = true;
                payType = PAY_ALIPAY;
                btn_pay_zfb.setBackgroundResource(R.drawable.icon_pay_select);
                btn_pay_wx.setBackgroundResource(R.drawable.icon_pay_normal);
                break;
            case R.id.rl_wx:
            case R.id.btn_pay_wx:
                MobclickAgent.onEvent(RechargeActivity.this, "10105");
                isChoose = true;
                payType = PAY_WXPAY;
                Log.d(TAG, "onClick: -----======>" + btn_pay_wx.isSelected());
                btn_pay_zfb.setBackgroundResource(R.drawable.icon_pay_normal);
                btn_pay_wx.setBackgroundResource(R.drawable.icon_pay_select);
                break;
            case R.id.btn_cz:
                if (mPresenter.isGuestUser()) {
                    if (BuildConfig.isForceLoad.equals("1")) {
                        CustomDialogUtils.showQZLoginDialog(RechargeActivity.this, false);
                    } else {
                        CustomDialogUtils.showLoginDialog(RechargeActivity.this, false);
                    }
                }
                if (!TextUtils.isEmpty(defineEdit.getText().toString())
                        && Integer.valueOf(defineEdit.getText().toString()) >= 100
                        && Integer.valueOf(defineEdit.getText().toString()) <= 3000) {
                    flag = 7;
                } else if (!TextUtils.isEmpty(defineEdit.getText().toString())) {
                    onToast("输入金额在100-3000元之间", Gravity.CENTER, 0, 0);
                    return;
                }
                if (!isChoose) {
                    onToast("请选择任意一种支付方式支付", Gravity.CENTER, 0, 0);
                } else if (flag == 0) {
                    onToast("请选择任意一种虎币类型", Gravity.CENTER, 0, 0);
                } else {
                    MobclickAgent.onEvent(RechargeActivity.this, "10106");
                    if (payType == PAY_ALIPAY) {
                        pay("alipay");
                    } else if (payType == PAY_WXPAY) {
                        pay("weixin");
                    }
                }
                break;
            default:
                break;
        }
    }

    private void pay(final String pf) {
        btnCz.setEnabled(false);
        if (flag == 7) {
            hubiKind = Integer.valueOf(defineEdit.getText().toString()) * 100;
        }
        if (hubiKind < 100) {
            onToast("请输入不小于100的值", Gravity.CENTER, 0, 0);
            return;
        }

        mPresenter.pay(pf, hubiKind);
    }

    // 数值变化
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_VALUE_CHANGE, threadMode = ThreadMode.MAIN)
    public void onReceiveValueChangePush(ValueChangePush pushData) {
        if (pushData != null) {
            if ("HB".equals(pushData.getType())) {
                tvHubi.setText(String.valueOf(pushData.getNewVal()));
            }
        }
    }

}
