package cn.feihutv.zhibofeihu.ui.user.login;

import android.app.Activity;

import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : liwen.chen
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface LoginMvpView extends MvpView {

    //定义view接口

    // 跳转到手机登录页面
    void goPhoneLogin();

    /**
     * 打开主页面
     */
    void openMainActivity();

    Activity getActivity();

    void showForbidLoginDlg(String str, String qq);
}
