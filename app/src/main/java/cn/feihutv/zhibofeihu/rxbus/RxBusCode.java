package cn.feihutv.zhibofeihu.rxbus;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/10/16
 *     desc   : 定义rx bus  code
 *     version: 1.0
 * </pre>
 */
public class RxBusCode {


    //rx code :
    public final static int RX_BUS_CODE_ROOM_CHAT = 1001;

    //rx code :
    public final static int RX_BUS_CODE_ROOM_BARRAGE = 1002;

    //rx code :
    public final static int RX_BUS_CODE_NOTICE_USER_MSG = 1003;


    //rx code :
    public final static int RX_BUS_CODE_FORCED_OFFLINE = 1004;


    //rx code :
    public final static int RX_BUS_CODE_ALERT_ERROR = 1005;


    //rx code :
    public final static int RX_BUS_CODE_ALERT_MSG = 1006;


    //rx code :
    public final static int RX_BUS_CODE_NOTICE_JOIN_ROOM = 1007;


    //rx code :
    public final static int RX_BUS_CODE_NOTICE_LEAVE_ROOM = 1008;


    //rx code :
    public final static int RX_BUS_CODE_VALUE_CHANGE = 1009;


    //rx code :
    public final static int RX_BUS_CODE_UPDATE_PLAY_URL = 1010;


    //rx code :
    public final static int RX_BUS_CODE_NOTICE_ROOM_CARE = 1011;


    //rx code :
    public final static int RX_BUS_CODE_ROOM_GIFT = 1012;


    //rx code :
    public final static int RX_BUS_CODE_ROOM_FOLLOW = 1013;


    //rx code :
    public final static int RX_BUS_CODE_ROOM_BAN = 1014;


    //rx code :
    public final static int RX_BUS_CODE_BANNED_LIVE = 1015;


    //rx code :
    public final static int RX_BUS_CODE_BANNED_JOIN_ROOM = 1016;


    //rx code : 被封号
    public final static int RX_BUS_CODE_BANNED_LOGIN = 1017;


    //rx code :
    public final static int RX_BUS_CODE_NEW_MSG = 1018;


    //rx code :
    public final static int RX_BUS_CODE_FORCED_CLOSE_LIVE = 1019;


    //rx code :
    public final static int RX_BUS_CODE_ALL_ROOM_MSG = 1020;


    //rx code :
    public final static int RX_BUS_CODE_GAME_START_ROUND = 1021;


    //rx code :
    public final static int RX_BUS_CODE_GAME_RESULT = 1022;


    //rx code :
    public final static int RX_BUS_CODE_JACKPOT_GIFT_COUNTDOWN = 1023;


    //rx code :
    public final static int RX_BUS_CODE_ROOM_MGR = 1024;


    //rx code :
    public final static int RX_BUS_CODE_STREAM_ERROR = 1025;


    //rx code :
    public final static int RX_BUS_CODE_UPDATE_PLAY_STATUS = 1026;


    //rx code : 实名认证通知
    public final static int RX_BUS_CODE_CERTIFICATION_RESULT = 1027;


    //rx code :socket 连接异常,一次丢包
    public final static int RX_BUS_CODE_SOCKET_CONNECT_ERROR = 2001;


    //rx code :socket 连接掉线异常 3次心跳没回应则丢包 异常 需重连
    public final static int RX_BUS_CODE_SOCKET_LOST_CONNECTION_ERROR = 2002;

    //rx code :socket 连接中断
    public final static int RX_BUS_CODE_SOCKET_CONNECTION_INTERRUPT_ERROR = 2003;


    //rx code :socket 重新连接成功，需要验证token
    public final static int RX_BUS_CODE_SOCKET_RECONNECT_COMPLETE = 2010;


    //rx code :socket 重新连接成功 验证token 也成功
    public final static int RX_BUS_CODE_SOCKET_RECONNECT_SUCCEED = 2011;


    //rx code :socket 重新连接失败，重新发起连接
    public final static int RX_BUS_CODE_SOCKET_RECONNECT_FAIL = 2012;


    //rx code :socket 重新连接后回应鉴权失败，用户需要重新登录  errorMsg:CheckSigFailed
    public final static int RX_BUS_CODE_HTTP_RECONNECT_SIG_FAILED = 2013;

    //rx code : 发送给子Fragment
    public final static int RX_BUS_CODE_POSITION_CURRENTITEM = 2014;

    //rx code :接受到系统消息
    public final static int RX_BUS_CODE_NOTICE_SYS_MESSAGE = 2014;

    //rx code :接受到私信消息
    public final static int RX_BUS_CODE_NOTICE_SECRET_MESSAGE = 2015;

    //rx code :触发游客登录
    public final static int RX_BUS_CODE_NOTICE_ENTER_LOGIN = 2016;

    //rx code :网络切换到4G
    public final static int RX_BUS_CODE_NOTICE_NETWORK_CHANGE = 2017;

    //rx code :更换靓号
    public final static int RX_BUS_CODE_NOTICE_ACCOUNTID_CHANGE = 2018;

    //rx code :飞虎流星交易等待确认
    public final static int RX_BUS_NOTICE_CODE_TRADE_SURE = 2019;

    //用户信息
    public final static int RX_BUS_NOTICE_CODE_USERINFO = 2020;


    //操作事件处理------
    //播放mv 列表视频事件处理
    public final static int RX_BUS_CLICK_CODE_MV_LIST_PLAY = 3000;

    //mv 需求广场---> 提交需求
    public final static int RX_BUS_CLICK_CODE_MV_POST_DEMAND = 3001;

    //mv 需求广场---> 提交mv
    public final static int RX_BUS_CLICK_CODE_MV_POST_MV = 3002;

    //守护
    public final static int RX_BUS_NOTICE_CODE_GUARD = 2021;

    //开通vip
    public final static int RX_BUS_NOTICE_CODE_VIP = 2022;

    //
    public final static int RX_BUS_NOTICE_CODE_HAVACARE = 2023;
    public final static int RX_BUS_NOTICE_CODE_NOHAVACARE = 2024;
    public final static int RX_BUS_NOTICE_CODE_NOTIFYWANFAHISTORY = 2025;
    public final static int RX_BUS_NOTICE_CODE_SHARE = 2026;

    public final static int RX_BUS_NOTICE_CODE_MODIFY_HEAD = 2027;  // 更换头像成功
    public final static int RX_BUS_NOTICE_CODE_MODIFY_COVER = 2028; // 更换封面成功
    public final static int RX_BUS_NOTICE_CODE_DELETE_DYNAMIC = 2029; // 删除动态成功
    public final static int RX_BUS_NOTICE_CODE_ROOMINCOME = 2030; //
    public final static int RX_BUS_NOTICE_CODE_POST_SUCC = 2031; // 发布动态成功
}
