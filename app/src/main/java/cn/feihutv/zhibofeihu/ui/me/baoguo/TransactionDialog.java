package cn.feihutv.zhibofeihu.ui.me.baoguo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.me.CheckAccountIdRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.CheckAccountIdResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.MakeTradeRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.MakeTradeResponse;
import cn.feihutv.zhibofeihu.rxbus.RxBus;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.rxbus.bangdan.LuckPush;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/13 14:38
 *      desc   : 飞虎流星交易
 *      version: 1.0
 * </pre>
 */

public class TransactionDialog extends Dialog {

    @BindView(R.id.find_edit)
    EditText findEdit;
    @BindView(R.id.count_edit)
    EditText countEdit;
    @BindView(R.id.money_edit)
    EditText moneyEdit;
    private Context mContext;
    private String userId;
    private TradeMakeSure mTradeMakeSure;
    private int count;

    public void setTradeMakeSure(TradeMakeSure tradeMakeSure) {
        mTradeMakeSure = tradeMakeSure;
    }

    public TransactionDialog(Context context, int count) {
        super(context, R.style.color_dialog);
        mContext = context;
        this.count = count;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_dialog);
        ButterKnife.bind(this);
        countEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    @OnClick({R.id.cancel, R.id.ok, R.id.txt_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                MobclickAgent.onEvent(mContext, "10122");
                dismiss();
                break;
            case R.id.ok:

                if (TextUtils.isEmpty(findEdit.getText())) {
                    FHUtils.showToast("请输入买家ID");
                    return;
                }

                if (FeihuZhiboApplication.getApplication().mDataManager.getCurrentUserId().equals(userId)) {
                    FHUtils.showToast("不能和自己交易！");
                    return;
                }

                MobclickAgent.onEvent(mContext, "10123");
                if (TextUtils.isEmpty(countEdit.getText())) {
                    FHUtils.showToast("请输入交易个数");
                    return;
                }
                if (TextUtils.isEmpty(moneyEdit.getText())) {
                    FHUtils.showToast("请输入交易金额");
                    return;
                }

                if (count < Integer.parseInt(countEdit.getText().toString().trim())) {
                    FHUtils.showToast("您的飞虎流星数量不足！");
                    return;
                }

                int money = Integer.valueOf(moneyEdit.getText().toString());
                if ((money / Double.parseDouble(countEdit.getText().toString())) < 100.00) {
                    FHUtils.showToast("飞虎流星单价不低于100虎币");
                    return;
                }
                if (!TextUtils.isEmpty(userId)) {
                    new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                            .doMakeTradeCall(new MakeTradeRequest(userId, moneyEdit.getText().toString().trim(), countEdit.getText().toString()))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<MakeTradeResponse>() {
                                @Override
                                public void accept(@NonNull MakeTradeResponse makeTradeResponse) throws Exception {
                                    if (makeTradeResponse.getCode() == 0) {
                                        FHUtils.showToast("等待对方确认");
                                        if (mTradeMakeSure != null) {
                                            mTradeMakeSure.isTrading();
                                        }
                                        dismiss();
                                    } else {
                                        if (makeTradeResponse.getCode() == 4204) {
                                            FHUtils.showToast("您的飞虎流星数量不足！");
                                        }
                                        AppLogger.i(makeTradeResponse.getCode() + " " + makeTradeResponse.getErrMsg());
                                    }
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(@NonNull Throwable throwable) throws Exception {

                                }
                            }));
                } else {
                    FHUtils.showToast("请检索主播的ID");
                }
                break;
            case R.id.txt_search:
                if (TextUtils.isEmpty(findEdit.getText())) {
                    FHUtils.showToast("请输入ID号码");
                    return;
                }
                if (!TCUtils.isNumPsd(findEdit.getText().toString())) {
                    FHUtils.showToast("请输入纯数字");
                    return;
                }
                new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                        .doCheckAccountIdCall(new CheckAccountIdRequest(findEdit.getText().toString().trim()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<CheckAccountIdResponse>() {
                            @Override
                            public void accept(@NonNull CheckAccountIdResponse checkAccountIdResponse) throws Exception {
                                if (checkAccountIdResponse.getCode() == 0) {
                                    if (!TextUtils.isEmpty(userId)) {
                                        findEdit.setText("");
                                    }
                                    userId = checkAccountIdResponse.getCheckAccountIdResponseData().getUid();
                                    String nickname = checkAccountIdResponse.getCheckAccountIdResponseData().getNickname();
                                    findEdit.setText(findEdit.getText().toString() + nickname);
                                } else {
                                    AppLogger.i(checkAccountIdResponse.getCode() + " " + checkAccountIdResponse.getErrMsg());
                                    if (checkAccountIdResponse.getCode() == 4041) {
                                        FHUtils.showToast("用户不存在");
                                    } else {
                                        FHUtils.showToast("检索出错");
                                    }
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                throwable.printStackTrace();
                            }
                        }));

                break;
            default:
                break;
        }
    }

    public interface TradeMakeSure {
        void isTrading();
    }

}
