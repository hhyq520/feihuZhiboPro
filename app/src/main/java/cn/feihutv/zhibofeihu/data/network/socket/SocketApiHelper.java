package cn.feihutv.zhibofeihu.data.network.socket;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.SocketBaseResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.common.SendRoomMsgRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.common.SendRoomResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.common.VerifySocketTokenRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.common.VerifySocketTokenResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.CloseLiveRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.CloseLiveResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.GetLiveStatusRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.GetLiveStatusResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.JoinRoomRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.JoinRoomResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.LeaveRoomRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.LeaveRoomResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.LoadOtherUserInfoRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.LoadOtherUserInfoResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.StartLiveRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.StartLiveResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.me.ModifyProfileLiveCoverRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.me.ModifyProfileLiveCoverResponse;
import io.reactivex.Observable;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/09/27
 *     desc   : socket api 业务接口定义
 *     version: 1.0
 * </pre>
 */

public interface SocketApiHelper {


    SocketApiHeader getSocketApiHeader();

    //连接socket
    Observable<SocketBaseResponse> doConnectSocketApiCall(List<String> socketIp);


    //登录Socket服务器
    Observable<VerifySocketTokenResponse> doVerifySocketTokenSocketApiCall(VerifySocketTokenRequest request);



    //加载用户基础信息
    Observable<LoadUserDataBaseResponse> doLoadUserDataBaseSocketApiCall(LoadUserDataBaseRequest request);

    //加入房间
    Observable<JoinRoomResponce> doLoadJoinRoomSocketApiCall(JoinRoomRequest request);

    //开播
    Observable<StartLiveResponce> doLoadStartLiveApiCall(StartLiveRequest request);

    //离开房间
    Observable<LeaveRoomResponce> doLoadLeaveRoomApiCall(LeaveRoomRequest request);

    //关闭直播
    Observable<CloseLiveResponse> doCloseLiveLiveApiCall(CloseLiveRequest request);

    //加载其他用户数据
    Observable<LoadOtherUserInfoResponce> doLoadOtherUserInfoApiCall(LoadOtherUserInfoRequest request);

    //发送聊天消息
    Observable<SendRoomResponce> doSendRoomMsgApiCall(SendRoomMsgRequest request);

    //获取直播状态
    Observable<GetLiveStatusResponce> doGetLiveStatusApiCall(GetLiveStatusRequest request);


    //修改直播封面 --修改个人资料
    Observable<ModifyProfileLiveCoverResponse> doModifyProfileLiveCoverApiCall(ModifyProfileLiveCoverRequest request);



}
