package cn.feihutv.zhibofeihu.activitys;


import android.app.Activity;

import cn.feihutv.zhibofeihu.di.PerActivity;
import cn.feihutv.zhibofeihu.ui.base.MvpPresenter;


/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/05/14
 *     desc   : 定义P操作
 *     version: 1.0
 * </pre>
 */
@PerActivity
public interface SplashMvpPresenter<V extends SplashMvpView> extends MvpPresenter<V> {

    //定义业务处理方法
    void checkDataUpdate();

    boolean checkPermission(Activity activity);

}
