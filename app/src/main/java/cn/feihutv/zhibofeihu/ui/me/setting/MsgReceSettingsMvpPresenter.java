package cn.feihutv.zhibofeihu.ui.me.setting;


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
public interface MsgReceSettingsMvpPresenter<V extends MsgReceSettingsMvpView> extends MvpPresenter<V> {

    //定义业务处理方法

    void getMsgSwitchStatus();

    void setMsgSwitchStatus(String key, String value, int type);

}
