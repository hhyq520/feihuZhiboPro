package cn.feihutv.zhibofeihu.ui.live;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import java.util.List;

import cn.feihutv.zhibofeihu.data.local.model.SysbagBean;
import cn.feihutv.zhibofeihu.data.local.model.TCChatEntity;
import cn.feihutv.zhibofeihu.data.network.http.model.common.FollowResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomContriResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.UnFollowResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.GetCurrMountResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.LoadOtherRoomsResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameListResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameStatusResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.JackpotCountDownResponce;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : huang hao
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface ChatPlayFragmentMvpView extends MvpView {

    //定义view接口
    void gotoRoom(LoadRoomResponce.LoadRoomData data);
    Context getChatPlayContext();
    Activity getChatPlayActivity();
    void goReportActivity(String userId);//跳转举报页面
    int getLoginDialogCount();
    void setLoginDialogCount(int count);
    ImageView getConcernImage();
    void goOthersCommunityActivity(String userId);//跳转他人社区
    void notifyChatMsg(TCChatEntity tcChatEntity);//刷新聊天列表
    void notifyContriList(List<LoadRoomContriResponce.RoomContriData> datas);//刷新在线贡献榜
    void notifyCaichiCountDown(JackpotCountDownResponce.JackpotCountDownData data);//刷新彩池倒计时
    void notifyGetCurrMount(GetCurrMountResponce.CurrMountData data);//刷新当前座驾
    void notifyUncareResponce(UnFollowResponce responce);
    void notifyFollowResponce(FollowResponce responce);
    void showGiftDialog(List<SysbagBean> sysbagBeanList);
    void sendGiftSuccess(List<SysbagBean> sysbagBeanList,int id ,int count);
    void getGameListSuccess(GetGameListResponce getGameListResponce);
    void  kaiqiangResponce(String name ,GetGameStatusResponce responce);
    void loadOtherRoomsResponce(LoadOtherRoomsResponce responce);
    void leaveRoomResponce(int code);
    void initDerectMsg(String sendId, String nickName, String headUrl);
}
