package cn.feihutv.zhibofeihu.ui.live;

import android.app.Activity;
import android.os.Bundle;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.GetRoomMrgsResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.UserGiftBean;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.JoinRoomResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.StartLiveResponce;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : huang hao
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface SWCameraStreamingMvpView extends MvpView {

    //定义view接口
    Activity getActivity();

    void setLocationText(String text);//修改定位文字

    void showNetChangeDialog();//切换到4G时的提示

    void showBanDialog(String msg, String time);//禁播提示

    void showOfflineGifts(List<StartLiveResponce.OfflineGiftsData> list);//下播礼物

    void initViewPager(JoinRoomResponce.JoinRoomData roomData);

    boolean getmIsUserPushing();

    void  setmIsUserPushing(boolean mIsUserPushing);

    void goMainActivity();

    void showDetailDialog(Bundle args);

    void cancelRoomMgrState(boolean isSuccess,int position);

    void notifyRoomMrgList(List<GetRoomMrgsResponce.RoomMrgData> datas);

    void openMusicFile();
}
