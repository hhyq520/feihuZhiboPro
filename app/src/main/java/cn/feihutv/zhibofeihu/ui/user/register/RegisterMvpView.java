package cn.feihutv.zhibofeihu.ui.user.register;
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
public interface RegisterMvpView extends MvpView {

    //定义view接口
    Activity getActivity();

    void openMainActivity();

    void startTimer();
}
