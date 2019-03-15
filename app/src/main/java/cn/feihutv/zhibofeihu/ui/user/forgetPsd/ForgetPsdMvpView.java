package cn.feihutv.zhibofeihu.ui.user.forgetPsd;

import android.app.Activity;

import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : huang hao
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface ForgetPsdMvpView extends MvpView {

    //定义view接口
    /**
     * 打开主页面
     */
    void openMainActivity();

    Activity getActivity();

    void startTimer();
}
