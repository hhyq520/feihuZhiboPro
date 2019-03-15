package cn.feihutv.zhibofeihu.ui.me.dowm;


import java.util.List;

import cn.feihutv.zhibofeihu.data.db.model.MvDownLog;
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
public interface MyDwomMvpPresenter<V extends MyDwomMvpView> extends MvpPresenter<V> {

    //定义业务处理方法

    List<MvDownLog> getMyMvDownLog();

}
