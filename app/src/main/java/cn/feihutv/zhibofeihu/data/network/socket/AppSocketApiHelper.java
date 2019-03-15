package cn.feihutv.zhibofeihu.data.network.socket;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import cn.feihutv.zhibofeihu.data.network.socket.model.common.SendRoomMsgRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.common.SendRoomResponce;
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
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.SocketBaseResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.common.VerifySocketTokenRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.common.VerifySocketTokenResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.me.ModifyProfileLiveCoverRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.me.ModifyProfileLiveCoverResponse;
import io.reactivex.Observable;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/09/27
 *     desc   : app socket api 业务实现
 *     version: 1.0
 * </pre>
 */
@Singleton
public class AppSocketApiHelper implements SocketApiHelper {

    private SocketApiHeader mSocketApiHeader;

    @Inject
    public AppSocketApiHelper(SocketApiHeader mSocketApiHeader) {
        this.mSocketApiHeader = mSocketApiHeader;
    }

    @Override
    public SocketApiHeader getSocketApiHeader() {
        return mSocketApiHeader;
    }

    @Override
    public Observable<SocketBaseResponse> doConnectSocketApiCall(final List<String> socketIp) {

        return RxSocketUtil.getInstance()
                .doConningSocket(SocketApiEndPoint.CommonPoint.ENDPOINT_CONNING_SOCKET, socketIp);

    }


    @Override
    public Observable<VerifySocketTokenResponse> doVerifySocketTokenSocketApiCall(final VerifySocketTokenRequest request) {

        //封装消息体
        String reqData = mSocketApiHeader.getProtectedApiHeader()
                .getSocketRequestData(SocketApiEndPoint.CommonPoint.ENDPOINT_VERIFY_TOKEN,
                        request);


        return RxSocketUtil.getInstance()
                .sendMessage(SocketApiEndPoint.CommonPoint.ENDPOINT_VERIFY_TOKEN,
                        reqData
                        , VerifySocketTokenResponse.class
                );
    }


    @Override
    public Observable<LoadUserDataBaseResponse> doLoadUserDataBaseSocketApiCall(LoadUserDataBaseRequest request) {
        //封装消息体
        String reqData = mSocketApiHeader.getProtectedApiHeader()
                .getSocketRequestData(SocketApiEndPoint.CommonPoint.ENDPOINT_LOAD_USER_DATABASE,
                        request);
        return RxSocketUtil.getInstance()
                .sendMessage(SocketApiEndPoint.CommonPoint.ENDPOINT_LOAD_USER_DATABASE,
                        reqData, LoadUserDataBaseResponse.class);


    }

    @Override
    public Observable<JoinRoomResponce> doLoadJoinRoomSocketApiCall(JoinRoomRequest request) {
        String reqData = mSocketApiHeader.getProtectedApiHeader()
                .getSocketRequestData(SocketApiEndPoint.LivePoint.ENDPOINT_JOINROOM,
                        request);
        return RxSocketUtil.getInstance()
                .sendMessage(SocketApiEndPoint.LivePoint.ENDPOINT_JOINROOM,
                        reqData, JoinRoomResponce.class);
    }

    @Override
    public Observable<StartLiveResponce> doLoadStartLiveApiCall(StartLiveRequest request) {
        String reqData = mSocketApiHeader.getProtectedApiHeader()
                .getSocketRequestData(SocketApiEndPoint.LivePoint.ENDPOINT_STARTLIVE,
                        request);
        return RxSocketUtil.getInstance()
                .sendMessage(SocketApiEndPoint.LivePoint.ENDPOINT_STARTLIVE,
                        reqData, StartLiveResponce.class);
    }

    @Override
    public Observable<LeaveRoomResponce> doLoadLeaveRoomApiCall(LeaveRoomRequest request) {
        String reqData = mSocketApiHeader.getProtectedApiHeader()
                .getSocketRequestData(SocketApiEndPoint.LivePoint.ENDPOINT_LEAVE_ROOM,
                        request);
        return RxSocketUtil.getInstance()
                .sendMessage(SocketApiEndPoint.LivePoint.ENDPOINT_LEAVE_ROOM,
                        reqData, LeaveRoomResponce.class);
    }

    @Override
    public Observable<CloseLiveResponse> doCloseLiveLiveApiCall(CloseLiveRequest request) {
        String reqData = mSocketApiHeader.getProtectedApiHeader()
                .getSocketRequestData(SocketApiEndPoint.LivePoint.ENDPOINT_CLOSELIVE,
                        request);
        return RxSocketUtil.getInstance()
                .sendMessage(SocketApiEndPoint.LivePoint.ENDPOINT_CLOSELIVE,
                        reqData, CloseLiveResponse.class);
    }

    @Override
    public Observable<LoadOtherUserInfoResponce> doLoadOtherUserInfoApiCall(LoadOtherUserInfoRequest request) {
        String reqData = mSocketApiHeader.getProtectedApiHeader()
                .getSocketRequestData(SocketApiEndPoint.CommonPoint.ENDPOINT_LOAD_OTHER_USER_DATABASE,
                        request);
        return RxSocketUtil.getInstance()
                .sendMessage(SocketApiEndPoint.CommonPoint.ENDPOINT_LOAD_OTHER_USER_DATABASE,
                        reqData, LoadOtherUserInfoResponce.class);
    }

    @Override
    public Observable<SendRoomResponce> doSendRoomMsgApiCall(SendRoomMsgRequest request) {
        String reqData = mSocketApiHeader.getProtectedApiHeader()
                .getSocketRequestData(SocketApiEndPoint.CommonPoint.ENDPOINT_SEND_ROOMMSG,
                        request);
        return RxSocketUtil.getInstance()
                .sendMessage(SocketApiEndPoint.CommonPoint.ENDPOINT_SEND_ROOMMSG,
                        reqData, SendRoomResponce.class);
    }

    @Override
    public Observable<GetLiveStatusResponce> doGetLiveStatusApiCall(GetLiveStatusRequest request) {
        String reqData = mSocketApiHeader.getProtectedApiHeader()
                .getSocketRequestData(SocketApiEndPoint.LivePoint.ENDPOINT_GETLIVESTATUS,
                        request);
        return RxSocketUtil.getInstance()
                .sendMessage(SocketApiEndPoint.LivePoint.ENDPOINT_GETLIVESTATUS,
                        reqData, GetLiveStatusResponce.class);
    }

    @Override
    public Observable<ModifyProfileLiveCoverResponse> doModifyProfileLiveCoverApiCall(ModifyProfileLiveCoverRequest request) {
        String reqData = mSocketApiHeader.getProtectedApiHeader()
                .getSocketRequestData(SocketApiEndPoint.MePoint.ENDPOINT_MODIFY_PROFILE,
                        request);
        return RxSocketUtil.getInstance()
                .sendMessage(SocketApiEndPoint.MePoint.ENDPOINT_MODIFY_PROFILE,
                        reqData, ModifyProfileLiveCoverResponse.class);
    }

}
