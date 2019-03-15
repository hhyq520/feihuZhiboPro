package cn.feihutv.zhibofeihu.ui.home.history;


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
public interface HistoryMvpPresenter<V extends HistoryMvpView> extends MvpPresenter<V> {

    //定义业务处理方法

    void queryHistory();

    void deleteAllHistory();

    void loadRoomById(String roomId);// 请求直播间信息

}
