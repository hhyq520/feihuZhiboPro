package cn.feihutv.zhibofeihu.activitys;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import com.jaeger.library.StatusBarUtil;
import javax.inject.Inject;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.socket.xsocket.FHSocket;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.main.MainActivity;
import cn.feihutv.zhibofeihu.ui.user.login.LoginActivity;

/**
 * <pre>
 *     @author : sichu.chen
 *     time   : 2017/05/14
 *     desc   : 界面绘制
 *     version: 1.0
 * </pre>
 */
public class SplashActiity extends BaseActivity implements SplashMvpView {

    @Inject
    SplashMvpPresenter<SplashMvpView> mPresenter;
    private String url = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }


    @Override
    protected void initWidget() {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this));

        //presenter调用初始化
        mPresenter.onAttach(SplashActiity.this);

        mPresenter.checkDataUpdate();

        if (mPresenter.checkPermission(SplashActiity.this)) {

            Intent intent = getIntent();
            if (intent != null) {
                url = intent.getStringExtra("url");
                if (url != null) {
                    Log.e("PushJService", url);
                }
            }
            if (!TextUtils.isEmpty(url)) {
                if (FHSocket.getInstance1() != null) {
                    FHSocket.getInstance().close();
                }
            }

        }
        StatusBarUtil.setTranslucent(getActivity());
    }


    @Override
    protected void onDestroy() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroy();
    }


    @Override
    public void openLoginActivity() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void openMainActivity() {
        Intent intent = new Intent(SplashActiity.this, MainActivity.class);
        intent.putExtra("url", url);
        if (url != null) {
            Log.e("PushJService1", url);
        }
        startActivity(intent);
        finish();
    }

    @Override
    public Activity getActivity() {
        return SplashActiity.this;
    }

}
