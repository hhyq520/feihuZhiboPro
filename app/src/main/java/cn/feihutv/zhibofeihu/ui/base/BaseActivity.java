package cn.feihutv.zhibofeihu.ui.base;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Unbinder;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.di.component.ActivityComponent;
import cn.feihutv.zhibofeihu.di.component.DaggerActivityComponent;
import cn.feihutv.zhibofeihu.di.module.ActivityModule;
import cn.feihutv.zhibofeihu.rxbus.RxBus;
import cn.feihutv.zhibofeihu.ui.user.login.LoginActivity;
import cn.feihutv.zhibofeihu.ui.widget.ScreenSwitch;
import cn.feihutv.zhibofeihu.ui.widget.dialog.ProgressLoadingDialog;
import cn.feihutv.zhibofeihu.utils.CommonUtils;
import cn.feihutv.zhibofeihu.utils.NetworkUtils;

/**
 * <pre>
 *     @author : sichu.chen
 *     time   : 2017/09/25
 *     desc   : activity 界面基类
 *     version: 1.0
 * </pre>
 */

public abstract class BaseActivity extends AppCompatActivity
        implements MvpView, BaseFragment.Callback {

    private ProgressLoadingDialog mProgressDialog;

    private ActivityComponent mActivityComponent;

    private Unbinder mUnBinder;

    private FragmentManager fragmentManager;

    private BaseFragment showFragment;

    /**
     * @return 布局id
     */
    protected abstract int getLayoutId();

    /**
     * 初始化控件
     */
    protected abstract void initWidget();

    /**
     * 事件绑定
     */
    protected void eventOnClick() {
    }

    protected void initWidget(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeSet();
        fragmentManager = getSupportFragmentManager();
        mActivityComponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(((FeihuZhiboApplication) getApplication()).getComponent())
                .build();
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }
        RxBus.get().register(this);
        initWidget();
        eventOnClick();
        initWidget(savedInstanceState);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    public ActivityComponent getActivityComponent() {
        return mActivityComponent;
    }


    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void showLoading() {
        hideLoading();
        mProgressDialog = CommonUtils.showLoadingDialog(this, "");
    }


    @Override
    public void showLoading(String msg) {
        hideLoading();
        mProgressDialog = CommonUtils.showLoadingDialog(this, msg);
    }


    public void setLoadingText(String msg){
        if(mProgressDialog!=null){
           mProgressDialog.setTextMessage(msg);
        }
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }


    @Override
    public void onToast(String message) {
        if (message != null) {
            showSnackBar(message);
        } else {
            showSnackBar(getString(R.string.some_error));
        }
    }

    private void showSnackBar(String message) {
//        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
//                message, Snackbar.LENGTH_SHORT);
//        View sbView = snackbar.getView();
//        TextView textView = (TextView) sbView
//                .findViewById(android.support.design.R.id.snackbar_text);
//        textView.setTextColor(ContextCompat.getColor(this, R.color.white));
//        snackbar.show();

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();


//        SnackbarUtils.Short(getWindow().getDecorView(),message)
//                .confirm().gravityFrameLayout(Gravity.CENTER).messageCenter().show();
    }

    private void showSnackBar(String msg, int gravity, int xOffset, int yOffset) {
        View view = LayoutInflater.from(BaseActivity.this).inflate(R.layout.toast_weiget, null);
        TextView tvMsg = (TextView) view.findViewById(R.id.tv_msg);
        tvMsg.setText(msg);
        Toast toast = new Toast(BaseActivity.this);
        toast.setView(view);
        toast.setGravity(gravity, xOffset, yOffset);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

    public void setLightStatusBar(Activity activity, @NonNull View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            activity.getWindow().setStatusBarColor(Color.WHITE);
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
        }
    }

    protected void beforeSet() {

    }

    @Override
    public void onToast(@StringRes int resId) {
        onToast(getString(resId));
    }

    @Override
    public void onToast(String msg, int gravity, int xOffset, int yOffset) {
        if (msg != null) {
            showSnackBar(msg, gravity, xOffset, yOffset);
        } else {
            showSnackBar(getString(R.string.some_error));
        }
    }

    @Override
    public boolean isNetworkConnected() {
        return NetworkUtils.isNetworkConnected(getApplicationContext());
    }

    @Override
    public void onFragmentAttached() {

    }

    @Override
    public void onFragmentDetached(String tag) {

    }

    @Override
    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        }
    }

    @Override
    public void openActivityOnTokenExpire() {
        goActivity(LoginActivity.class);
        finish();
    }

    public void setUnBinder(Unbinder unBinder) {
        mUnBinder = unBinder;
    }

    @Override
    protected void onDestroy() {

        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        RxBus.get().unRegister(this);
        super.onDestroy();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    public void goActivityForResult(Class<?> descClass, int requestCode) {
        ScreenSwitch.switchActivity(this, descClass, null, requestCode);
    }

    public void goActivityForResult(Class<?> descClass, Bundle bundle, int requestCode) {
        ScreenSwitch.switchActivity(this, descClass, bundle, requestCode);
    }

    public void goActivity(Class<?> descClass, Bundle bundle) {
        goActivityForResult(descClass, bundle, 0);
    }

    public void goActivity(Class<?> descClass) {
        goActivity(descClass, null);
    }


    public void showFragment(int resId, BaseFragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (showFragment != null) {
            fragmentTransaction.hide(showFragment);
        }
        Fragment mFragment = fragmentManager.findFragmentByTag(fragment.getClass().getName());
        if (mFragment != null) {
            fragmentTransaction.show(mFragment);
            showFragment = (BaseFragment) mFragment;
        } else {

            fragmentTransaction.add(resId, fragment, fragment.getClass().getName());
            showFragment = fragment;
        }
        fragmentTransaction.commit();
    }

}
