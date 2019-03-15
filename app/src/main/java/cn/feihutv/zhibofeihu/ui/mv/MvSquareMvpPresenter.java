package cn.feihutv.zhibofeihu.ui.mv;


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
public interface MvSquareMvpPresenter<V extends MvSquareMvpView> extends MvpPresenter<V> {

    //定义业务处理方法

    //查询是否有mv系统消息
    void queryMVNotice();

    // 设置mv系统消息为已读状态
    void cancelMVNotice();



}

