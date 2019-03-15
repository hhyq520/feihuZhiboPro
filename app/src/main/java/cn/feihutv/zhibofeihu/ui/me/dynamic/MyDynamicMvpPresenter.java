package cn.feihutv.zhibofeihu.ui.me.dynamic;


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
public interface MyDynamicMvpPresenter<V extends MyDynamicMvpView> extends MvpPresenter<V> {

    //定义业务处理方法

    // 获取动态列表
    void loadFeedList(String userId, String last);

    // 获取全部动态
    void loadAllFeedList(int offset);
}
