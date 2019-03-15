package cn.feihutv.zhibofeihu.ui.me.dynamic;


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
public interface OthersCommunityMvpPresenter<V extends OthersCommunityMvpView> extends MvpPresenter<V> {

    //定义业务处理方法

    void loadOtherUserInfo(String userId);

    // 取消关注
    void unfollow(String userId);

    // 关注
    void follow(String userId);

    // 统计分享
    void logShare(int to);

    // 移除黑名单
    void unblock(String userId);

    // 加入黑名单
    void block(String userId);
}
