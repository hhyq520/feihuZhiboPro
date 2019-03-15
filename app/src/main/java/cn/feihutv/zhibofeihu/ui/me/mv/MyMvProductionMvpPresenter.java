package cn.feihutv.zhibofeihu.ui.me.mv;


import cn.feihutv.zhibofeihu.di.PerActivity;
import cn.feihutv.zhibofeihu.ui.base.MvpPresenter;


/**
 * <pre>
 *     author : sichu.chen
 *     time   :
 *     desc   : 定义P操作
 *     version: 1.0
 * </pre>
 */
@PerActivity
public interface MyMvProductionMvpPresenter<V extends MyMvProductionMvpView> extends MvpPresenter<V> {

    //定义业务处理方法

    void getMyMVList(String status, String offset, String count);



}
