package cn.feihutv.zhibofeihu.ui.live;

import cn.feihutv.zhibofeihu.data.network.socket.model.live.JoinRoomResponce;
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
public interface PLVideoViewMvpPresenter<V extends PLVideoViewMvpView> extends MvpPresenter<V> {

    void joinRoom(String roomId, String reconnect,boolean isFirst);

    void leaveRoom(String roomId);

    void logShare(int from, int to);

    void saveHistory(JoinRoomResponce.JoinRoomData joinRoomData);

}
