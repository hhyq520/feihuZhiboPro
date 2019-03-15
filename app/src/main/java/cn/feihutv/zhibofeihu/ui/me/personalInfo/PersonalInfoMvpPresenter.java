package cn.feihutv.zhibofeihu.ui.me.personalInfo;


import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseResponse;
import cn.feihutv.zhibofeihu.di.PerActivity;
import cn.feihutv.zhibofeihu.ui.base.MvpPresenter;


/**
 * <pre>
 *     author : liwen.chen
 *     time   :
 *     desc   : 定义P操作
 *     version: 1.0
 * </pre>
 */
@PerActivity
public interface PersonalInfoMvpPresenter<V extends PersonalInfoMvpView> extends MvpPresenter<V> {

    //定义业务处理方法
    void saveUserData(LoadUserDataBaseResponse.UserData userData);

    // 修改性别 1 男 2 女 0 保密
    void modifyProfileGender(int gender);
    void getCosSign(int type, String ext,String path);
    void modifyProfile_HeadUrl(String v);
    void modifyProfile_LiveCover(String v);
}
