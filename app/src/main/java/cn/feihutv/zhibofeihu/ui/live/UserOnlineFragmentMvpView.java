package cn.feihutv.zhibofeihu.ui.live;

import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.LoadRoomGuestResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.LoadRoomMemberResponce;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : huang hao
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface UserOnlineFragmentMvpView extends MvpView {

    //定义view接口
    void notifyList(LoadRoomMemberResponce.RoomMemberData roomMemberData);
    void onFreshStop();
    void loadMoreStop();
    void notifyMoreList(LoadRoomMemberResponce.RoomMemberData roomMemberData);
    void notifyGuestCnt(LoadRoomGuestResponce.RoomGuestData data);
}
