package cn.feihutv.zhibofeihu.ui.me.personalInfo;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.socket.model.me.ModifyProfileLiveCoverRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.me.ModifyProfileLiveCoverResponse;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.me.recharge.RechargeActivity;
import cn.feihutv.zhibofeihu.ui.widget.TCActivityTitle;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.feihutv.zhibofeihu.utils.weiget.dialog.RxDialogSureCancel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/10/30 20:18
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class NicknameModifyActivity extends BaseActivity {

    @BindView(R.id.nickname_back)
    TCActivityTitle mTitle;

    @BindView(R.id.nick_name)
    TextView tvNick;

    @BindView(R.id.edit_nickname)
    EditText mEditText;

    private int sourceLen;
    private int destLen;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_nickname_modify;
    }

    @Override
    protected void initWidget() {
        setUnBinder(ButterKnife.bind(NicknameModifyActivity.this));

        initView();
    }

    private void initView() {

        Intent intent = getIntent();
        if (intent != null) {
            String nickName = intent.getStringExtra("nickName");
            tvNick.setText(nickName);
        }

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String editable = mEditText.getText().toString();
                String str = stringFilter(editable); //过滤特殊字符
                if (!editable.equals(str)) {
                    mEditText.setText(str);
                }
                mEditText.setSelection(mEditText.length());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    public static String stringFilter(String str) throws PatternSyntaxException {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“'。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("");
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

    @OnClick(R.id.btn_finish)
    public void onViewClicked() {
        final String strNick = mEditText.getText().toString().trim();
        if (TextUtils.isEmpty(strNick)) {
            onToast("请输入昵称", Gravity.CENTER, 0, 0);
            return;
        }

        if (strNick.length() < 2 || strNick.length() > 16) {
            onToast("昵称在2-8位之间", Gravity.CENTER, 0, 0);
            return;
        }

        showLoading();
        FeihuZhiboApplication.getApplication().mDataManager
                .doModifyProfileLiveCoverApiCall(new ModifyProfileLiveCoverRequest("NickName", strNick))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ModifyProfileLiveCoverResponse>() {
                    @Override
                    public void accept(@NonNull ModifyProfileLiveCoverResponse modifyProfileLiveCoverResponse) throws Exception {
                        if (modifyProfileLiveCoverResponse.getCode() == 0) {
                            onToast("修改成功", Gravity.CENTER, 0, 0);
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putString("nickName", strNick);
                            intent.putExtras(bundle);
                            setResult(PersonalInfoActivity.REQUSST_NICKNAME, intent);
                            finish();
                        } else {
                            if (modifyProfileLiveCoverResponse.getCode() == 4202) {
                                final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(NicknameModifyActivity.this);//提示弹窗
                                rxDialogSureCancel.setContent("虎币不足，快去前往充值吧~");
                                rxDialogSureCancel.setSure("前往");
                                rxDialogSureCancel.setCancel("取消");
                                rxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(NicknameModifyActivity.this, RechargeActivity.class));
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
                            } else if (modifyProfileLiveCoverResponse.getCode() == 4016) {
                                onToast("该昵称已被占用", Gravity.CENTER, 0, 0);
                            } else if (modifyProfileLiveCoverResponse.getCode() == 4009) {
                                onToast("长度超限或内容违规，请重新设置!", Gravity.CENTER, 0, 0);
                            } else {
                                onToast("修改失败，请重新设置!", Gravity.CENTER, 0, 0);
                            }
                            AppLogger.i(modifyProfileLiveCoverResponse.getCode() + " " + modifyProfileLiveCoverResponse.getErrMsg());
                        }
                        hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        hideLoading();
                        onToast("修改失败，请重新设置!", Gravity.CENTER, 0, 0);
                    }
                });
    }
}
