package cn.feihutv.zhibofeihu.ui.live.pclive;

import android.app.Activity;
import android.content.Context;

import java.util.List;

import cn.feihutv.zhibofeihu.data.local.model.SysbagBean;
import cn.feihutv.zhibofeihu.data.network.http.model.common.FollowResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.UnFollowResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.LoadOtherRoomsResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.JackpotCountDownResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.JoinRoomResponce;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : huang hao
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface LivePlayInPCMvpView extends MvpView {

    //定义view接口
    void joinRoomRespoce(JoinRoomResponce responce);
    void loadOtherRoomsResponce(LoadOtherRoomsResponce responce);
    void notifyCaichiCountDown(JackpotCountDownResponce.JackpotCountDownData data);//刷新彩池倒计时
    void notifyGetCurrMount(int id);//刷新当前座驾
    Activity getPcActivity();
    void setLoginDialogCount(int count);
    void goReportActivity(String id);
    void goOthersCommunityActivity(String id);
    void followOrUnfollow(boolean isfollow);
    void careResponce(FollowResponce responce);
    void uncareRespoce(UnFollowResponce responce);
    void showLandGiftDialog(List<SysbagBean> sysbagBeanList);
    void sendGiftSuccess(List<SysbagBean> sysbagBeanList,int id ,int count);
    void leaveRoomResponce(int code);
    void gotoRoom(LoadRoomResponce.LoadRoomData data);
    void DerectMsgDialogDissMiss();
    void  initDerectMsg(String sendId, String nickName, String headUrl,boolean isPcLand);
    void notifyWanfaHistory();
}
