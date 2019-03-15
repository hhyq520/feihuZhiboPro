package cn.feihutv.zhibofeihu.ui.base;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/09/25
 *     desc   :基础接口 所有界面必须实现该接口，该接口封装页面基本显示功能
 *     version: 1.0
 * </pre>
 */
public interface SubMvpView  extends MvpView{

    void onCreate();

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

    void attachParentMvpView(MvpView mvpView);
}
