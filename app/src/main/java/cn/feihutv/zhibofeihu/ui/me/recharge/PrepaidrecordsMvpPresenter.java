package cn.feihutv.zhibofeihu.ui.me.recharge;


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
public interface PrepaidrecordsMvpPresenter<V extends PrepaidrecordsMvpView> extends MvpPresenter<V> {

    //定义业务处理方法

    void getPayList();

}
