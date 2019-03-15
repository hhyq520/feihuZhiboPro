package cn.feihutv.zhibofeihu.ui.me.encash;


import cn.feihutv.zhibofeihu.data.db.model.SysConfigBean;
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
public interface IncomeMvpPresenter<V extends IncomeMvpView> extends MvpPresenter<V> {

    //定义业务处理方法

    void enCash(String cash);

    SysConfigBean getSysConfig();

}
