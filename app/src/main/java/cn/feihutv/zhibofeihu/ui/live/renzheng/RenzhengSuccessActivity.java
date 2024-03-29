package cn.feihutv.zhibofeihu.ui.live.renzheng;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.ui.live.SWCameraStreamingActivity;
import cn.feihutv.zhibofeihu.ui.live.renzheng.RenzhengSuccessMvpView;
import cn.feihutv.zhibofeihu.ui.live.renzheng.RenzhengSuccessMvpPresenter;
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
public class RenzhengSuccessActivity extends BaseActivity implements RenzhengSuccessMvpView {
    @BindView(R.id.back)
    TCActivityTitle back;
    @BindView(R.id.btn_ok)
    Button btnOk;
    @Inject
    RenzhengSuccessMvpPresenter<RenzhengSuccessMvpView> mPresenter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_renzheng_success;
    }


    @Override
    protected void initWidget() {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this));

        //persenter调用初始化
        mPresenter.onAttach(RenzhengSuccessActivity.this);

        back.setReturnListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RenzhengSuccessActivity.this, SWCameraStreamingActivity.class));
                finish();
            }
        });
    }


    @Override
    protected void onDestroy() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroy();
    }


}
