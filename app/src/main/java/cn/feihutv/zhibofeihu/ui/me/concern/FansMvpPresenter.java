package cn.feihutv.zhibofeihu.ui.me.concern;


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
public interface FansMvpPresenter<V extends FansMvpView> extends MvpPresenter<V> {

    //定义业务处理方法

    void getFollowers(int offset);

}
