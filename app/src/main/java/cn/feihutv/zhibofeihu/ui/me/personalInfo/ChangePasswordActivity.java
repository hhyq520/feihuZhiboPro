package cn.feihutv.zhibofeihu.ui.me.personalInfo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.home.GetBannerRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.home.GetBannerResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.ModifyUserPassRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.ModifyUserPassResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.me.ModifyProfileLiveCoverRequest;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.widget.TCActivityTitle;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/10/31 18:56
 *      desc   : 修改密码
 *      version: 1.0
 * </pre>
 */

public class ChangePasswordActivity extends BaseActivity {

    @BindView(R.id.pw_now)
    EditText mEtOldPsw;

    @BindView(R.id.pw_new)
    EditText mEtNewPsw;

    @BindView(R.id.pw_again)
    EditText mEtAgain;

    @BindView(R.id.psw_back)
    TCActivityTitle mTitle;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_psw;
    }

    @Override
    protected void initWidget() {
        setUnBinder(ButterKnife.bind(ChangePasswordActivity.this));
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
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_finish:
                // 完成
                if (TextUtils.isEmpty(getNewPsw())) {
                    return;
                }
                if (TextUtils.isEmpty(getOldPsw())) {
                    return;
                }
                if (TextUtils.isEmpty(getAgainPsw())) {
                    return;
                }

                if (TCUtils.isNumPsd(getNewPsw())) {
                    onToast("密码不能为纯数字", Gravity.CENTER, 0, 0);
                    return;
                }
                if (TCUtils.isZimuPsd(getNewPsw())) {
                    onToast("密码不能为纯字母", Gravity.CENTER, 0, 0);
                    return;
                }
                if (!TCUtils.isPsd(getNewPsw())) {
                    onToast("密码规则不对", Gravity.CENTER, 0, 0);
                    return;
                }
                if (!getNewPsw().equals(getAgainPsw())) {
                    onToast("两次输入密码不一致", Gravity.CENTER, 0, 0);
                    return;
                }
                if (!isNetworkConnected()) {
                    onToast("网络异常，修改失败！", Gravity.CENTER, 0, 0);
                    return;
                }
                showLoading();
                new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                        .doModifyUserPassApiCall(new ModifyUserPassRequest(getOldPsw(), getNewPsw()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<ModifyUserPassResponse>() {
                            @Override
                            public void accept(@NonNull ModifyUserPassResponse modifyUserPassResponse) throws Exception {
                                if (modifyUserPassResponse.getCode() == 0) {
                                    onToast("修改成功", Gravity.CENTER, 0, 0);
                                    finish();
                                } else {
                                    if (modifyUserPassResponse.getCode() == 4014) {
                                        onToast("原密码错误", Gravity.CENTER, 0, 0);
                                    } else {
                                        onToast("修改失败", Gravity.CENTER, 0, 0);
                                        AppLogger.i(modifyUserPassResponse.getCode() + modifyUserPassResponse.getErrMsg());
                                    }
                                }
                                hideLoading();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                hideLoading();
                                onToast("修改失败！", Gravity.CENTER, 0, 0);
                            }
                        }));

                break;
            default:
                break;
        }

    }

    private String getOldPsw() {
        return mEtOldPsw.getText().toString().trim();
    }

    private String getNewPsw() {
        return mEtNewPsw.getText().toString().trim();
    }

    private String getAgainPsw() {
        return mEtAgain.getText().toString().trim();
    }
}
