package cn.feihutv.zhibofeihu.ui.user.phoneLogin;

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
public interface PhoneLoginMvpView extends MvpView {

    //定义view接口

    void openMainActivity();

    void showForbidLoginDlg(String str, String qq);

    Activity getActivity();
}
