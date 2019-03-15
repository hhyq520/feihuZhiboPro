package cn.feihutv.zhibofeihu.ui.live;

import java.util.List;

import cn.feihutv.zhibofeihu.data.db.model.SysConfigBean;
import cn.feihutv.zhibofeihu.data.db.model.SysGiftNewBean;
import cn.feihutv.zhibofeihu.data.db.model.SysMountNewBean;
import cn.feihutv.zhibofeihu.data.local.model.TCChatEntity;
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
public interface ChatLiveMvpPresenter<V extends ChatLiveMvpView> extends MvpPresenter<V> {

    //定义业务处理方法
    List<SysGiftNewBean> getSysGiftNewBean();
    SysConfigBean getSysConfigBean();
    void showUserInfo(String id);//用户信息dialog
    void sendRoomMsg(TCChatEntity tcChatEntity, String msg);//发送消息
    void loadRoomContriList(String userId,int rankType);//请求直播间贡献列表
    void getJackpotCountDown();//获取幸运彩池开奖倒计时
    void sendLoudspeaker(String msg);//发送全站小喇叭
    SysMountNewBean getMountBeanByID(String id);
    SysGiftNewBean getGiftBeanByID(String id);
    void getLiveStatus();
    void getGameStatus(String name,boolean showStyleDialog);
    void showJoinResult(String name, int issueRound);
    void getGameStatus2(String name);
    void gamePreempt(String name, int openStyle);
    void notifyCare(boolean havaCare,String userID);
}
