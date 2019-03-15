package cn.feihutv.zhibofeihu.data.network.socket;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.data.network.socket.model.SocketBaseResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.SocketMessage;
import cn.feihutv.zhibofeihu.data.network.socket.model.common.SocketConnectError;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.AlertErrorPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.AlertMsgPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.AllRoomMsgPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.BannedJoinRoomPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.BannedLivePush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.BannedLoginPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.CertificationResultPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.ForcedCloseLivePush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.ForcedOfflinePush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.GameResultPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.GameStartRoundPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.JackpotGiftCountdownPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.NoticeJoinRoomPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.NoticeLeaveRoomPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.NoticeRoomCarePush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.NoticeUserMsgPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.RoomBanPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.RoomBarragePush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.RoomChatPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.RoomFollowPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.RoomGiftPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.RoomIncomePush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.RoomMgrPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.StreamErrorPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.UpdatePlayStatusPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.UpdatePlayUrlPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.ValueChangePush;
import cn.feihutv.zhibofeihu.data.network.socket.xsocket.FHSendMessageCallBack;
import cn.feihutv.zhibofeihu.data.network.socket.xsocket.FHSocket;
import cn.feihutv.zhibofeihu.data.network.socket.xsocket.IFHCallBack;
import cn.feihutv.zhibofeihu.data.network.socket.xsocket.tcp.client.XTcpClient;
import cn.feihutv.zhibofeihu.data.network.socket.xsocket.tcp.client.bean.TcpMsg;
import cn.feihutv.zhibofeihu.data.network.socket.xsocket.tcp.client.listener.TcpClientListener;
import cn.feihutv.zhibofeihu.data.prefs.AppPreferencesHelper;
import cn.feihutv.zhibofeihu.rxbus.RxBus;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.DbMessageUtil;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/10/13
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class RxSocketUtil implements TcpClientListener {

    private static RxSocketUtil instance;

    private Map<String, Observable<? extends SocketBaseResponse>> mObservableMap;


    private RxSocketUtil() {
        mObservableMap = new HashMap<>();
    }


    private final static int mHeartBeatDelayTime = 10;// 心跳间隔时间

    Disposable mHeartDisposable; //心跳执行器

    public static RxSocketUtil getInstance() {
        if (instance == null) {
            synchronized (RxSocketUtil.class) {
                if (instance == null) {
                    instance = new RxSocketUtil();
                }
            }
        }
        return instance;
    }


    public Observable<SocketBaseResponse> doConningSocket(String reqTag, final List<String> socketIP) {
        Observable<SocketBaseResponse> observable = (Observable<SocketBaseResponse>) mObservableMap.get(reqTag);
        if (observable == null) {
            observable = Observable.create(new ObservableOnSubscribe<SocketBaseResponse>() {
                @Override
                public void subscribe(final ObservableEmitter<SocketBaseResponse> ob) throws Exception {
                    FHSocket.SocketAddress = socketIP;
                    AppLogger.i("socket", "init socket ,the socket start....");
                    //添加tcp 监听
                    FHSocket.getInstance().removeTcpClientListener(RxSocketUtil.this);
                    FHSocket.getInstance().addTcpClientListener(RxSocketUtil.this);
                    //执行连接
                    FHSocket.getInstance().connect(new IFHCallBack() {
                        @Override
                        public void cbBlock(SocketBaseResponse resp) {

                            if (resp.success) {
                                //连接成功后启动心跳
                                startHeartBeat();

                                AppLogger.i("socket", "the socket connect 连接成功...." + resp);
                                if (resp.isFirstConnected) {
                                    //首次连接成功
                                    ob.onNext(resp);
                                    ob.onComplete();
                                } else {
                                    //非首次建立连接成功，需要验证token
                                    RxBus.get().send(RxBusCode.RX_BUS_CODE_SOCKET_RECONNECT_COMPLETE, new SocketConnectError("1"));
                                }


                            } else {
                                AppLogger.e("socket", "the socket connect 连接失败...." + resp);
                                //连接失败
                                RxBus.get().send(RxBusCode.RX_BUS_CODE_SOCKET_RECONNECT_FAIL, new SocketConnectError());
                            }


                        }
                    });


                }
            });
        }
        return observable;
    }


    public void startHeartBeat() {
        //先取消
        if (mHeartDisposable != null && !mHeartDisposable.isDisposed()) {
            mHeartDisposable.dispose();
        }

        AppLogger.i("socket", "the HeartBeat start....");
        Observable.interval(0, mHeartBeatDelayTime, TimeUnit.SECONDS)
                .observeOn(Schedulers.io())

                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mHeartDisposable = d;
                    }

                    @Override
                    public void onNext(Long aLong) {

                        AppLogger.i("socket", "the socket ------->开始发送心跳包......");
                        FHSocket.getInstance().runHearBeat();//开启发送心跳包

                    }

                    @Override
                    public void onError(Throwable e) {
                        AppLogger.e("心跳包发送失败" + e);
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        ;


    }


    /**
     * 取消订阅
     */
    public void cancelHeartDisposable() {
        if (mHeartDisposable != null && !mHeartDisposable.isDisposed()) {
            mHeartDisposable.dispose();
            AppLogger.e("====发送心跳包定时器取消======");
        }
    }


    public <T> Observable<T> sendMessage(final String reqTag, final String data, final Class<T> respClass) {
        Observable<T> observable = (Observable<T>) mObservableMap.get(reqTag);
        if (observable == null) {
            observable = Observable.create(new ObservableOnSubscribe<T>() {
                @Override
                public void subscribe(final ObservableEmitter<T> sub) throws Exception {

                    AppLogger.i("socket", "sendMessage------>" + reqTag + "");
                    FHSocket.getInstance().sendMsg(data, new FHSendMessageCallBack() {

                        @Override
                        public void onFailure(String errorMsg) {
                            SocketBaseResponse response = new SocketBaseResponse();
                            response.success = false;
                            AppLogger.w("发送失败");
                            T resp = null;
                            try {
                                resp = (T) Class.forName(respClass.getName());
                            } catch (ClassNotFoundException e1) {
                                e1.printStackTrace();
                            }
                            sub.onNext(resp);
                            sub.onComplete();
                        }

                        @Override
                        public void onMessage(String message) {

                            SocketMessage msSocketMessage = parseSocketMsg(message);

                            if (msSocketMessage.getReqTag().equals(reqTag)) {
                                //只接受对应请求的消息

                                AppLogger.w("发送成功,收到的数据--> req: " + msSocketMessage.getReqTag() + " " +
                                        "  data=" + msSocketMessage.getData() + "      |对应解析回应：" + respClass);
                                //解析回应数据
                                T resp = null;
                                try {
                                    resp = new Gson().fromJson(msSocketMessage.getData(), respClass);
                                } catch (JsonSyntaxException e) {
                                    try {
                                        resp = (T) Class.forName(respClass.getName());
                                        sub.onNext(resp);
                                        sub.onComplete();
                                    } catch (ClassNotFoundException e1) {
                                        e1.printStackTrace();
                                    }
                                    throw new JsonSyntaxException("解析socket 数据异常：" + e);
                                }
                                sub.onNext(resp);
                                sub.onComplete();
                            }
                        }
                    });

                }
            });
        }

        return observable;
    }


    /**
     * 解析服务端返回数据，仅仅在FHSocket调用了该方法
     */
    public static SocketMessage parseSocketMsg(String msg) {
        String mt = "";
        SocketMessage message = new SocketMessage();
        int indexOf = msg.indexOf("|");
        if (indexOf == -1) {
            return message;
        }
        String reqId = msg.substring(0, indexOf);
        message.setReqTag(reqId);
        String msgContent = msg.substring(indexOf + 1, msg.length());
        message.setData(msgContent);

        try {
            JSONObject jsonObj = new JSONObject(msgContent);
            int code = 0;
            code = jsonObj.getInt("Code");
            message.setCode(code);
        } catch (JSONException e) {
        }
        try {
            JSONObject jsonObj = new JSONObject(msgContent);
            mt = jsonObj.getString("MsgType");
            message.setMsgType(mt);
        } catch (JSONException e) {
        }

        try {
            String action = "";
            JSONObject jsonObj = new JSONObject(msgContent);
            action = jsonObj.getString("Action");
            message.setAction(action);
        } catch (JSONException e) {
        }


        if (mt.isEmpty()) {
            Log.e("parseSocketMsg", "【无法解析服务端数据】:" + msgContent);
        }

        return message;
    }


    @Override
    public void onConnected(boolean isSucceed, XTcpClient client) {
    }

    @Override
    public void onSended(XTcpClient client, TcpMsg tcpMsg) {
    }

    @Override
    public void onDisconnected(XTcpClient client, String msg, Exception e) {
    }

    @Override
    public void onReceive(XTcpClient client, TcpMsg tcpMsg) {

        String message = tcpMsg.getSourceDataString();
        if (!message.equals("pong")) {
            //接受到消息,只处理推送消息
            SocketMessage socketMessage = RxSocketUtil.parseSocketMsg(message);
            if ("push".equals(socketMessage.getMsgType())) {
                AppLogger.i("socket 收到推送消息：" + tcpMsg.getSourceDataString());

                String action = socketMessage.getAction();
                String jsonData = socketMessage.getData();
                switch (action) {
                    case "RoomChat":
                        RxBus.get().send(RxBusCode.RX_BUS_CODE_ROOM_CHAT,
                                parsePushData(action, jsonData, RoomChatPush.class));
                        break;
                    case "RoomBarrage":

                        RxBus.get().send(RxBusCode.RX_BUS_CODE_ROOM_BARRAGE,
                                parsePushData(action, jsonData, RoomBarragePush.class));
                        break;
                    case "NoticeUserMsg":
                        RxBus.get().send(RxBusCode.RX_BUS_CODE_NOTICE_USER_MSG,
                                parsePushData(action, jsonData, NoticeUserMsgPush.class));
                        break;
                    case "ForcedOffline":
                        RxBus.get().send(RxBusCode.RX_BUS_CODE_FORCED_OFFLINE,
                                parsePushData(action, jsonData, ForcedOfflinePush.class));
                        break;
                    case "AlertError":
                        RxBus.get().send(RxBusCode.RX_BUS_CODE_ALERT_ERROR,
                                parsePushData(action, jsonData, AlertErrorPush.class));
                        break;
                    case "AlertMsg":
                        RxBus.get().send(RxBusCode.RX_BUS_CODE_ALERT_MSG,
                                parsePushData(action, jsonData, AlertMsgPush.class));
                        break;
                    case "NoticeJoinRoom":
                        RxBus.get().send(RxBusCode.RX_BUS_CODE_NOTICE_JOIN_ROOM,
                                parsePushData(action, jsonData, NoticeJoinRoomPush.class));
                        break;
                    case "NoticeLeaveRoom":
                        RxBus.get().send(RxBusCode.RX_BUS_CODE_NOTICE_LEAVE_ROOM,
                                parsePushData(action, jsonData, NoticeLeaveRoomPush.class));
                        break;
                    case "ValueChange":
                        try {
                            JSONObject jsonObject = new JSONObject(jsonData);
                            String type = jsonObject.getString("Type");
                            long newValue = jsonObject.getLong("NewVal");
                            if (type.equals("GHB")) {
                                SharePreferenceUtil.saveSeesionLong(FeihuZhiboApplication.getApplication(), AppPreferencesHelper.PREF_KEY_GHB, newValue);
                            } else if (type.equals("HB")) {
                                SharePreferenceUtil.saveSeesionLong(FeihuZhiboApplication.getApplication(), AppPreferencesHelper.PREF_KEY_HB, newValue);
                            } else if (type.equals("Exp")) {
                                SharePreferenceUtil.saveSeesionInt(FeihuZhiboApplication.getApplication(), AppPreferencesHelper.PREF_KEY_EXP, (int) newValue);
                            } else if (type.equals("Level")) {
                                SharePreferenceUtil.saveSeesionInt(FeihuZhiboApplication.getApplication(), AppPreferencesHelper.PREF_KEY_LEVEL, (int) newValue);
                            } else if (type.equals("YHB")) {
                                SharePreferenceUtil.saveSeesionLong(FeihuZhiboApplication.getApplication(), AppPreferencesHelper.PREF_KEY_YHB, newValue);
                            } else if (type.equals("Xiaolaba")) {
                                SharePreferenceUtil.saveSeesionInt(FeihuZhiboApplication.getApplication(), AppPreferencesHelper.PREF_KEY_XIAOLABA_COUNT, (int) newValue);
                            } else if (type.equals("Income")) {
                                SharePreferenceUtil.saveSeesionLong(FeihuZhiboApplication.getApplication(), AppPreferencesHelper.PREF_KEY_INCOME, newValue);
                            } else if (type.equals("Contri")) {
                                SharePreferenceUtil.saveSeesionLong(FeihuZhiboApplication.getApplication(), AppPreferencesHelper.PREF_KEY_CONTRI, newValue);
                            } else if (type.equals("Vip")) {
                                SharePreferenceUtil.saveSeesionInt(FeihuZhiboApplication.getApplication(), AppPreferencesHelper.PREF_KEY_VIP, (int) newValue);
                            } else if (type.equals("VipCzz")) {
                                SharePreferenceUtil.saveSeesionInt(FeihuZhiboApplication.getApplication(), AppPreferencesHelper.PREF_KEY_Vip_CZZ, (int) newValue);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        RxBus.get().send(RxBusCode.RX_BUS_CODE_VALUE_CHANGE,
                                parsePushData(action, jsonData, ValueChangePush.class));
                        break;
                    case "UpdatePlayUrl":
                        RxBus.get().send(RxBusCode.RX_BUS_CODE_UPDATE_PLAY_URL,
                                parsePushData(action, jsonData, UpdatePlayUrlPush.class));
                        break;
                    case "NoticeRoomCare":
                        RxBus.get().send(RxBusCode.RX_BUS_CODE_NOTICE_ROOM_CARE,
                                parsePushData(action, jsonData, NoticeRoomCarePush.class));
                        break;
                    case "RoomGift":
                        RxBus.get().send(RxBusCode.RX_BUS_CODE_ROOM_GIFT,
                                parsePushData(action, jsonData, RoomGiftPush.class));
                        break;
                    case "RoomFollow":
                        RxBus.get().send(RxBusCode.RX_BUS_CODE_ROOM_FOLLOW,
                                parsePushData(action, jsonData, RoomFollowPush.class));
                        break;
                    case "RoomBan":
                        RxBus.get().send(RxBusCode.RX_BUS_CODE_ROOM_BAN,
                                parsePushData(action, jsonData, RoomBanPush.class));
                        break;
                    case "BannedLive":
                        RxBus.get().send(RxBusCode.RX_BUS_CODE_BANNED_LIVE,
                                parsePushData(action, jsonData, BannedLivePush.class));
                        break;
                    case "BannedJoinRoom":
                        RxBus.get().send(RxBusCode.RX_BUS_CODE_BANNED_JOIN_ROOM,
                                parsePushData(action, jsonData, BannedJoinRoomPush.class));
                        break;
                    case "BannedLogin":
                        RxBus.get().send(RxBusCode.RX_BUS_CODE_BANNED_LOGIN,
                                parsePushData(action, jsonData, BannedLoginPush.class));
                        break;
                    case "NewMsg":
                        DbMessageUtil.ListenPushMessage();
                        break;
                    case "ForcedCloseLive":
                        AppLogger.e("主播强制下播+ForcedCloseLive");
                        RxBus.get().send(RxBusCode.RX_BUS_CODE_FORCED_CLOSE_LIVE,
                                parsePushData(action, jsonData, ForcedCloseLivePush.class));
                        break;
                    case "AllRoomMsg":
                        RxBus.get().send(RxBusCode.RX_BUS_CODE_ALL_ROOM_MSG,
                                parsePushData(action, jsonData, AllRoomMsgPush.class));
                        break;
                    case "GameStartRound":
                        RxBus.get().send(RxBusCode.RX_BUS_CODE_GAME_START_ROUND,
                                parsePushData(action, jsonData, GameStartRoundPush.class));
                        break;
                    case "GameResult":
                        RxBus.get().send(RxBusCode.RX_BUS_CODE_GAME_RESULT,
                                parsePushData(action, jsonData, GameResultPush.class));
                        break;
                    case "JackpotGiftCountdown":
                        RxBus.get().send(RxBusCode.RX_BUS_CODE_JACKPOT_GIFT_COUNTDOWN,
                                parsePushData(action, jsonData, JackpotGiftCountdownPush.class));
                        break;
                    case "RoomMgr":
                        RxBus.get().send(RxBusCode.RX_BUS_CODE_ROOM_MGR,
                                parsePushData(action, jsonData, RoomMgrPush.class));
                        break;
                    case "StreamError":
                        //FHUtils.showToast("直播发生异常StreamError哦");
                        RxBus.get().send(RxBusCode.RX_BUS_CODE_STREAM_ERROR,
                                parsePushData(action, jsonData, StreamErrorPush.class));
                        break;
                    case "UpdatePlayStatus":
                        RxBus.get().send(RxBusCode.RX_BUS_CODE_UPDATE_PLAY_STATUS,
                                parsePushData(action, jsonData, UpdatePlayStatusPush.class));
                        break;
                    case "CertificationResult":
                        try {
                            JSONObject jsonObject = new JSONObject(jsonData);
                            boolean status = jsonObject.getBoolean("Status");
                            if (status) {
                                SharePreferenceUtil.saveSeesionInt(FeihuZhiboApplication.getApplication(), AppPreferencesHelper.PREF_KEY_CERTIFISTATUS, 1);
                            } else {
                                SharePreferenceUtil.saveSeesionInt(FeihuZhiboApplication.getApplication(), AppPreferencesHelper.PREF_KEY_CERTIFISTATUS, 3);
                            }
                            RxBus.get().send(RxBusCode.RX_BUS_CODE_CERTIFICATION_RESULT, status);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "RoomIncome":
                        RxBus.get().send(RxBusCode.RX_BUS_NOTICE_CODE_ROOMINCOME,
                                parsePushData(action, jsonData, RoomIncomePush.class));
                        break;
                    default:
                        AppLogger.e("收到无法识别socket推送消息：" + action);
                        break;
                }
            }
        }
    }


    private <T> T parsePushData(String action, String jsonStr, Class cls) {
        T pushData = null;
        try {
            pushData = (T) new Gson().fromJson(jsonStr, cls);
        } catch (JsonSyntaxException e) {
            AppLogger.e("解析socket推送消息：" + action + " 失败--->" + e);
            try {
                pushData = (T) Class.forName(cls.getName());
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
        }
        return pushData;
    }

    @Override
    public void onValidationFail(XTcpClient client, TcpMsg tcpMsg) {
    }


}
