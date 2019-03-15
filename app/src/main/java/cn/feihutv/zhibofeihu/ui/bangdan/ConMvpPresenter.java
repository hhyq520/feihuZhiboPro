package cn.feihutv.zhibofeihu.ui.bangdan;


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
public interface ConMvpPresenter<V extends ConMvpView> extends MvpPresenter<V> {

    //定义业务处理方法

    void loadContriRankList(String rankType);

}
