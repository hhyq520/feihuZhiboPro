package cn.feihutv.zhibofeihu.ui.user.register;
import cn.feihutv.zhibofeihu.data.db.model.SysConfigBean;
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
public interface RegisterMvpPresenter<V extends RegisterMvpView> extends MvpPresenter<V> {

    //定义业务处理方法
    void sendRegistVerifyCode(String phoneNum);//发送验证码

    void createUser(String phone, String userPass, String nickName, String passwordVerify, String verifiCode,boolean isDialog);

    SysConfigBean getSysConfig();

}
