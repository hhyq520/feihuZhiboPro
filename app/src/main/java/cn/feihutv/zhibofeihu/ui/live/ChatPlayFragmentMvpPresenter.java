package cn.feihutv.zhibofeihu.ui.live;

import android.content.Context;

import java.util.List;

import cn.feihutv.zhibofeihu.data.db.model.SysConfigBean;
import cn.feihutv.zhibofeihu.data.db.model.SysGiftNewBean;
import cn.feihutv.zhibofeihu.data.db.model.SysMountNewBean;
import cn.feihutv.zhibofeihu.data.local.model.SysbagBean;
import cn.feihutv.zhibofeihu.data.local.model.TCChatEntity;
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
public interface ChatPlayFragmentMvpPresenter<V extends ChatPlayFragmentMvpView> extends MvpPresenter<V> {

    //定义业务处理方法
    List<SysGiftNewBean> getSysGiftNewBean();
    SysConfigBean getSysConfigBean();
    void joinRoom(String roomId);
    void leaveRoom(int code);
    void loadRoomById(String userId);// 请求直播间信息
    void showUserInfo(JoinRoomResponce.JoinRoomData roomData,String id, boolean isBangdan,boolean isHost);//用户信息dialog
    void sendRoomMsg(TCChatEntity tcChatEntity, String msg);//发送消息
    void loadRoomContriList(String userId,int rankType);//请求直播间贡献列表
    void sendLoudspeaker(String msg);//发送全站小喇叭
    void getJackpotCountDown();//获取幸运彩池开奖倒计时
    void getCurrMount(String roomId);//获取当前座驾
    SysMountNewBean getMountBeanByID(String id);
    void unFollowed(String id);//取消关注
    void Followed(String id);//关注
    void requestBagData();//请求背包数据
    SysGiftNewBean getGiftBeanByID(String id);
    void dealBagSendGift(List<SysbagBean> sysbagBeanList,  int id,  int count);
    String getGoodIDByGiftID(String id);
    void dealSendGift(List<SysbagBean> sysbagBeanList,int id,  int count);
    void getGameList(String usetId);
    void kaiqiang(String style);
    void loadOtherRooms();
    void showJoinResult(final String name, int issueRound);
}
