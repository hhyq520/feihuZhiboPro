package cn.feihutv.zhibofeihu.ui.live.renzheng;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.ui.live.renzheng.UploadFailureMvpView;
import cn.feihutv.zhibofeihu.ui.live.renzheng.UploadFailureMvpPresenter;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.ui.widget.TCActivityTitle;

/**
 * <pre>
 *     author : huang hao
 *     time   :
 *     desc   : 界面绘制
 *     version: 1.0
 * </pre>
 */
public class UploadFailureActivity extends BaseActivity implements UploadFailureMvpView {

    @BindView(R.id.back)
    TCActivityTitle back;
    @BindView(R.id.btn_ok)
    Button btnOk;
    @BindView(R.id.tv_fail)
    TextView textView;
    @Inject
    UploadFailureMvpPresenter<UploadFailureMvpView> mPresenter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_upload_failure;
    }


    @Override
    protected void initWidget() {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this));

        //persenter调用初始化
        mPresenter.onAttach(UploadFailureActivity.this);

        back.setReturnListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UploadFailureActivity.this, IdentityInfoActivity.class));
                finish();
            }
        });
        mPresenter.getCertifiData();
    }


    @Override
    protected void onDestroy() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroy();
    }


    @Override
    public void changeText(String text) {
        textView.setText(text);
    }
}
