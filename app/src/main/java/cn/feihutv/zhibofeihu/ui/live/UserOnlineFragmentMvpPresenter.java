package cn.feihutv.zhibofeihu.ui.live;

import cn.feihutv.zhibofeihu.di.PerActivity;
import cn.feihutv.zhibofeihu.ui.base.MvpPresenter;


/**
 * <pre>
 *     author : huang hao
 *     time   :
 *     desc   : 定义P操作
 *     version: 1.0
 * </pre>
 */
@PerActivity
public interface UserOnlineFragmentMvpPresenter<V extends UserOnlineFragmentMvpView> extends MvpPresenter<V> {

    //定义业务处理方法
    void loadRoomMembers(int offset, int count, String type);
    void loadRoomGuestCnt();
    void loadRoomMembersMore(int offset, int count, String type);
}
