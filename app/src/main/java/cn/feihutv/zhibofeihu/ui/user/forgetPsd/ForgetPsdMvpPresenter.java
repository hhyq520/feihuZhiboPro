package cn.feihutv.zhibofeihu.ui.user.forgetPsd;

import cn.feihutv.zhibofeihu.di.PerActivity;
import cn.feihutv.zhibofeihu.ui.base.MvpPresenter;


/**
 * <pre>
 *     author : huang hao
 *     time   :
 *     desc   : 定义P操作
 *     version: 1.0
 * </pre>
 */
@PerActivity
public interface ForgetPsdMvpPresenter<V extends ForgetPsdMvpView> extends MvpPresenter<V> {

    //定义业务处理方法
    void sendRegistVerifyCode(String phoneNum);//获取验证码
    void sureToLogin(String phone, String newPsd, String passwordVerify, String verifiCode);//修改密码登录
}
