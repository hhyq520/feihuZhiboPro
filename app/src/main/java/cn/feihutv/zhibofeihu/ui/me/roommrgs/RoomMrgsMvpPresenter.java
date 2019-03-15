package cn.feihutv.zhibofeihu.ui.me.roommrgs;


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
public interface RoomMrgsMvpPresenter<V extends RoomMrgsMvpView> extends MvpPresenter<V> {

    //定义业务处理方法

    // 拉取场控列表
    void getRoomMgrs();

    // 取消场控
    void cancelRoomMgr(String userId, int position);



}
