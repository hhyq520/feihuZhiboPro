package cn.feihutv.zhibofeihu.ui.live;

import android.app.Activity;
import android.content.Context;

import java.util.List;

import cn.feihutv.zhibofeihu.data.local.model.TCChatEntity;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomContriResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GamePreemptResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameRoundResultDetailResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameStatusResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.JackpotCountDownResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.LoadOtherUserInfoResponce;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : huang hao
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface ChatLiveMvpView extends MvpView {

    //定义view接口
    Context getChatContext();

    Activity getChatActivity();

    void goReportActivity(String userId);//跳转举报页面

    void goOthersCommunityActivity(String userId);//跳转他人社区

    void notifyChatMsg(TCChatEntity tcChatEntity);//刷新聊天列表

    void notifyContriList(List<LoadRoomContriResponce.RoomContriData> datas);//刷新在线贡献榜

    void notifyCaichiCountDown(JackpotCountDownResponce.JackpotCountDownData data);//刷新彩池倒计时

    void notifyLiveState(boolean state);

    void notifyGameState(GetGameStatusResponce.GameStatusData data, String name, boolean showStyleDialog);

    void getJoinResult(GetGameRoundResultDetailResponce getGameRoundResultDetailResponce, String name);

    void notifyGameState2(GetGameStatusResponce.GameStatusData data, String name);

    void notifyGamePreempt(GamePreemptResponce gamePreemptResponce, String name, boolean isSuccess);

    void initDerectMsg(String sendId, String nickName, String headUrl);
}
