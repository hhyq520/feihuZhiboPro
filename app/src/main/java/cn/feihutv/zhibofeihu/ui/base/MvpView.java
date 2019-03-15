package cn.feihutv.zhibofeihu.ui.base;

import android.support.annotation.StringRes;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/09/25
 *     desc   : view 控件基本功能接口，服务于界面
 *     version: 1.0
 * </pre>
 */
public interface MvpView {

    /**
     * 显示加载框
     */
    void showLoading();

    /**
     * 显示加载框
     */
    void showLoading(String msg);

    void hideLoading();

    //用户鉴权失败，跳转登录页面
    void openActivityOnTokenExpire();

    void onToast(@StringRes int resId);

    void onToast(String msg, int gravity, int xOffset, int yOffset);

    void onToast(String message);

    boolean isNetworkConnected();

    void hideKeyboard();


}
