package cn.feihutv.zhibofeihu.ui.live.pclive;

import java.util.List;

import cn.feihutv.zhibofeihu.data.db.model.SysConfigBean;
import cn.feihutv.zhibofeihu.data.db.model.SysGiftNewBean;
import cn.feihutv.zhibofeihu.data.db.model.SysMountNewBean;
import cn.feihutv.zhibofeihu.data.local.model.SysbagBean;
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
public interface LivePlayInPCMvpPresenter<V extends LivePlayInPCMvpView> extends MvpPresenter<V> {

    //定义业务处理方法
    void joinRoom(String roomId, String reconnect,boolean isFirst);
    void saveHistory(JoinRoomResponce.JoinRoomData joinRoomData);
    void loadOtherRooms();
    void sendLoudspeaker(String msg);//发送全站小喇叭
    void sendToRoom(String msg);
    void getJackpotCountDown();//获取幸运彩池开奖倒计时
    void getCurrMount(String roomId);//获取当前座驾
    SysMountNewBean getMountBeanByID(String id);
    void showUserInfo(final JoinRoomResponce.JoinRoomData tcRoomInfo,final String userId, final boolean isPCland, final boolean isBangdan,boolean isHost);
    void care(String userId);
    void uncare(String userId);
    void logShare(int from, int to);
    void requestBagData();//请求背包数据
    void dealBagSendGift(List<SysbagBean> sysbagBeanList, int id, int count);
    void dealSendGift(List<SysbagBean> sysbagBeanList,int id,  int count);
    List<SysGiftNewBean> getSysGiftNewBean();
    SysGiftNewBean getGiftBeanByID(String id);
    void leaveRoom(int code);
    void loadRoomById(String userId);// 请求直播间信息
    void showJoinResult(final String name, int issueRound);
    void showORJoinResult(final String name, int issueRound);

    SysConfigBean getSysConfigBean();
}
