package cn.feihutv.zhibofeihu.ui.user.login;


import com.umeng.socialize.bean.SHARE_MEDIA;

import cn.feihutv.zhibofeihu.di.PerActivity;
import cn.feihutv.zhibofeihu.ui.base.MvpPresenter;


/**
 * <pre>
 *     author : liwen.chen
 *     desc   : 定义P操作
 *     version: 1.0
 * </pre>
 */
@PerActivity
public interface LoginMvpPresenter<V extends LoginMvpView> extends MvpPresenter<V> {

    //定义业务处理方法

    // 第三方登录
    void platformLogin(SHARE_MEDIA platform);

    // 游客登录
    void guestLogin();

    // 调转到手机页面
    void openPhoneLoginActivity();

    //获取用户设备id
    void getUserDeviceId();

}
