package cn.feihutv.zhibofeihu.activitys;

import android.app.Activity;

import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/05/14
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface SplashMvpView extends MvpView {

    //定义view接口

    /**
     * 打开登录界面
     */
    void openLoginActivity();

    /**
     * 打开主页面
     */
    void openMainActivity();


    Activity getActivity();

}
