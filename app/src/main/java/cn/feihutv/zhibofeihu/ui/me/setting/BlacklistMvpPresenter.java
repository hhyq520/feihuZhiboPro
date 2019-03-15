package cn.feihutv.zhibofeihu.ui.me.setting;


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
public interface BlacklistMvpPresenter<V extends BlacklistMvpView> extends MvpPresenter<V> {

    //定义业务处理方法
    // 拉取黑名单列表
    void getBlacklist(int offset);

    // 移除黑名单
    void unblock(String userId, int position);

}
