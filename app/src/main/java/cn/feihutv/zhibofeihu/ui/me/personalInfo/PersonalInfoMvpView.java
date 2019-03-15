package cn.feihutv.zhibofeihu.ui.me.personalInfo;

import android.app.Activity;

import cn.feihutv.zhibofeihu.data.network.http.model.me.GetCosSignResponse;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : liwen.chen
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface PersonalInfoMvpView extends MvpView {

    //定义view接口

    Activity getActivity();

    // 修改性别成功
    void modifyProfileGenderSucc(int gender);
    void notifyCosSignResponce(GetCosSignResponse response, String path);
}
