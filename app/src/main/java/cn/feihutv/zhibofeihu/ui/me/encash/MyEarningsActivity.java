package cn.feihutv.zhibofeihu.ui.me.encash;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetEncashInfoResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.ValueChangePush;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.rxbus.RxBusSubscribe;
import cn.feihutv.zhibofeihu.rxbus.ThreadMode;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.widget.TCActivityTitle;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import cn.feihutv.zhibofeihu.utils.TCConstants;
import cn.feihutv.zhibofeihu.utils.TCUtils;

/**
 * <pre>
 *     author : liwen.chen
 *     time   :
 *     desc   : 我的收益
 *     version: 1.0
 * </pre>
 */
public class MyEarningsActivity extends BaseActivity implements MyEarningsMvpView {

    @Inject
    MyEarningsMvpPresenter<MyEarningsMvpView> mPresenter;

    @BindView(R.id.zb_eui_edit)
    TCActivityTitle mTitle;

    @BindView(R.id.tv_hubi)
    TextView tv_hubi;

    @BindView(R.id.tv_money)
    TextView tv_money;
    private String mAlipayAccount;
    private String mIncrease;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_earnings;
    }


    @Override
    protected void initWidget() {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this));

        //persenter调用初始化
        mPresenter.onAttach(MyEarningsActivity.this);

        mPresenter.getEncashInfo();

    }

    @Override
    protected void eventOnClick() {
        mTitle.setReturnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mTitle.setMoreListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyEarningsActivity.this, WithdrawalrecordActivity.class));
            }
        });
    }


    @Override
    public void getEncashInfo(GetEncashInfoResponse.GetEncashInfoResponseData encashInfoResponseData) {
        int getgHB = encashInfoResponseData.getgHB();
        mIncrease = encashInfoResponseData.getIncrease();
        mAlipayAccount = encashInfoResponseData.getAlipayAccount();
        SharePreferenceUtil.saveSeesionInt(MyEarningsActivity.this, TCConstants.GHB, getgHB);
        SharePreferenceUtil.saveSeesion(MyEarningsActivity.this, TCConstants.ALIPAYACCOUNT, mAlipayAccount);
        SharePreferenceUtil.saveSeesion(MyEarningsActivity.this, TCConstants.INCREASE, mIncrease);
        tv_hubi.setText(String.valueOf(getgHB));
        tv_money.setText(TCUtils.toDecimal((getgHB * 0.01 * Float.valueOf(mIncrease))) + "元");
    }

    @OnClick(R.id.earnings_btn_get)
    public void onViewClicked() {
        if (!TextUtils.isEmpty(mAlipayAccount)) {
            startActivity(new Intent(this, IncomeActivity.class));
        } else {
            startActivity(new Intent(this, FirstBindZhiFuBao.class));
        }
    }

    // 数值变化
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_VALUE_CHANGE, threadMode = ThreadMode.MAIN)
    public void onReceiveValueChangePush(ValueChangePush pushData) {
        if (pushData != null) {
            if ("GHB".equals(pushData.getType())) {
                tv_hubi.setText(String.valueOf(pushData.getNewVal()));
                float remoney = pushData.getNewVal() * Float.valueOf(mIncrease) / 100;
                tv_money.setText(String.valueOf(remoney));
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
