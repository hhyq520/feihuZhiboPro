package cn.feihutv.zhibofeihu.ui.me.personalInfo;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.socket.model.me.ModifyProfileLiveCoverRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.me.ModifyProfileLiveCoverResponse;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.widget.TCActivityTitle;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/10/30 16:03
 *      desc   : 修改个性签名
 *      version: 1.0
 * </pre>
 */

public class SignatureActivity extends BaseActivity {

    @BindView(R.id.edit_sign)
    EditText mEditText;

    @BindView(R.id.tv_length)
    TextView mTextView;

    @BindView(R.id.sign_eui_edit)
    TCActivityTitle mTitle;

    private String mSign;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_signature;
    }

    @Override
    protected void initWidget() {

        setUnBinder(ButterKnife.bind(SignatureActivity.this));

        Intent intent = getIntent();
        if (intent != null) {
            mSign = intent.getStringExtra("sign");
            mTextView.setText("还可以输入" + (26 - mSign.length()) + "个字");
            mEditText.setText(mSign);
        }

    }

    @Override
    protected void eventOnClick() {

        mTitle.setReturnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mTextView.setText("还可以输入" + (26 - i - i2) + "个字");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @OnClick({R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                final String content = mEditText.getText().toString().trim();
                //将输入的个性签名保存到服务器
                if (!isNetworkConnected()) {
                    onToast("网络异常，修改失败！");
                    return;
                }
                showLoading();
                FeihuZhiboApplication.getApplication().mDataManager
                        .doModifyProfileLiveCoverApiCall(new ModifyProfileLiveCoverRequest("Signature", content))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<ModifyProfileLiveCoverResponse>() {
                            @Override
                            public void accept(@NonNull ModifyProfileLiveCoverResponse modifyProfileLiveCoverResponse) throws Exception {
                                if (modifyProfileLiveCoverResponse.getCode() == 0) {
                                    onToast("修改成功", Gravity.CENTER, 0, 0);
                                    Intent intent = new Intent();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("signature", content);
                                    intent.putExtras(bundle);
                                    setResult(PersonalInfoActivity.REQUSST_SIGNATURE, intent);
                                    finish();
                                } else {
                                    if (modifyProfileLiveCoverResponse.getCode() == 4009) {
                                        onToast("个性签名包含敏感词，请重新设置!", Gravity.CENTER, 0, 0);
                                    }
                                    AppLogger.i(modifyProfileLiveCoverResponse.getCode() + " " + modifyProfileLiveCoverResponse.getErrMsg());
                                }
                                hideLoading();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                hideLoading();
                                onToast("失败", Gravity.CENTER, 0, 0);
                            }
                        });
                break;
            default:
                break;
        }
    }

}
