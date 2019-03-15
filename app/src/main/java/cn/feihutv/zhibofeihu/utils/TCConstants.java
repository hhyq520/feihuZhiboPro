package cn.feihutv.zhibofeihu.utils;


import cn.feihutv.zhibofeihu.FeihuZhiboApplication;

/**
 * Created by Administrator on 2017/2/17.
 */

public class TCConstants {
    public static String PHONE_NUMBER = "phone_number";
    public static String NICK_NAME = "nick_name";
    public static String ACCOUNT_ID = "account_id";
    public static String HEAD_URL = "head_url";
    public static String GENDER = "user_gender";
    public static String HB = "user_hb";
    public static String GHB = "user_ghb";
    public static String YHB = "user_yhb";
    public static String EXP = "user_exp";
    public static String ROOMNAME = "user_roomname";
    public static String LEVEL = "user_level";
    public static String SIGNATURE = "user_signature";
    public static String LOCATION = "user_location";
    public static String CERIFICATION = "user_cerification";
    public static String INCOME = "user_income";
    public static String CONTRI = "user_contri";
    public static String MNICKNAME = "modify_name";
    public static String MGENDER = "modify_gender";
    public static String COSHEADICONNAME = "user_cosheadiconname";
    public static String FOLLOWERS = "user_followers";
    public static String FOLLOWS = "user_followes";
    public static String HOT_LIST_REFRESH_TIME = "hot_list_refresh_time";
    public static String NEW_LIST_REFRESH_TIME = "new_list_refresh_time";
    public static String CONCERN_LIST_REFRESH_TIME = "concern_list_refresh_time";
    public static String LIVECNT = "live_count"; // 开播次数
    public static final int LOCATION_PERMISSION_REQ_CODE = 1;
    public static final int WRITE_PERMISSION_REQ_CODE = 2;
    public static String LIVECOVER = "live_cover";
    public static final int ERRORCODE_GuestForbidden = 4004;
    public static String ALIPAYACCOUNT = "alipay_account";
    public static String INCREASE = "increase";
    public static String GuildName = "guild_name";
    //直播端右下角listview显示type
    public static final int TEXT_TYPE = 0;
    public static final int GIFT_TYPE = 1;
    public static final int JOIN_TYPE = 2;
    public static final int CONCERN_TYPE = 3;
    public static final int BAN_TYPE = 4;
    public static final int PC_TYPE = 5;
    public static final int SYSTEM_TYPE = 6;
    public static final int JOIN_NORMAL = 7;

    public static final String TIPS_MSG_STOP_PUSH = "当前正在直播，是否退出直播？";

    /**
     * 用户可见的错误提示语
     */
    public static final String ERROR_MSG_NET_DISCONNECTED = "网络异常，请检查网络";
    //直播端错误信息
    public static final String ERROR_MSG_CREATE_GROUP_FAILED = "创建直播房间失败,Error:";
    public static final String ERROR_MSG_GET_PUSH_URL_FAILED = "拉取直播推流地址失败,Error:";
    public static final String ERROR_MSG_OPEN_CAMERA_FAIL = "无法打开摄像头，需要摄像头权限";
    public static final String ERROR_MSG_OPEN_MIC_FAIL = "无法打开麦克风，需要麦克风权限";
    public static final String ERROR_MSG_RECORD_PERMISSION_FAIL = "无法进行录屏,需要录屏权限";
    public static final String ERROR_MSG_NO_LOGIN_CACHE = "您的帐号已在其它地方登陆";
    public static final String ACTIVITY_RESULT = "activity_result";
    public static final String ERROR_MSG_NOT_QCLOUD_LINK = "非腾讯云链接，若要放开限制请联系腾讯云商务团队";


    /**
     * 常量字符串
     */
    public static final String USER_ID = "user_id";
    public static final String USER_SIG = "user_sig";
    public static final String USER_NICK = "user_nick";
    public static final String USER_HEADPIC = "user_headpic";
    public static final String USER_COVER = "user_cover";
    public static final String USER_LOC = "user_location";


    public static final String PUBLISH_URL = "publish_url";
    public static final String ROOM_ID = "room_id";
    public static final String ROOM_TITLE = "room_title";
    public static final String COVER_PIC = "cover_pic";
    public static final String GROUP_ID = "group_id";
    public static final String PLAY_URL = "play_url";
    public static final String PLAY_TYPE = "play_type";
    public static final String PUSHER_AVATAR = "pusher_avatar";
    public static final String PUSHER_ID = "pusher_id";
    public static final String PUSHER_NAME = "pusher_name";
    public static final String MEMBER_COUNT = "member_count";
    public static final String FILE_ID = "file_id";
    public static final String SHARE_PLATFORM = "share_platform";

    public static final String CMD_KEY = "userAction";
    public static final String DANMU_TEXT = "actionParam";

    public static final String NOTIFY_QUERY_USERINFO_RESULT = "notify_query_userinfo_result";

    //主播退出广播字段
    public static final String EXIT_APP = "EXIT_APP";

    //    public static final String TIPS_REGISTER_NO_PHONRNUMBER="1";
    public static final String TIPS_REGISTER_PHONRNUMB_LENGTH_ER = "2";
    public static final String TIPS_REGISTER_PHONRNUMB_OTHER_CHAR = "3";
    //    public static final String TIPS_REGISTER_PHONRNUMB_ERR="4";
    public static final String TIPS_REGISTER_PASSWORD_SHORT = "5";
    public static final String TIPS_REGISTER_PASSWORD_TLONG = "6";
    public static final String TIPS_REGISTER_PASSWORD_OTHER_CHAR = "7";
    //    public static final String TIPS_LOGIN_NO_PHONRNUMBER="8";
    public static final String TIPS_LOGIN_PHONRNUMB_LENGTH_ER = "9";
    public static final String TIPS_LOGIN_PHONRNUMB_OTHER_CHAR = "10";
    //    public static final String TIPS_LOGIN_PHONRNUMB_ERR="11";
//    public static final String TIPS_LOGIN_PASSWORD_SHORT="12";
    public static final String TIPS_LOGIN_PASSWORD_TLONG = "13";
    public static final String TIPS_LOGIN_PASSWORD_OTHER_CHAR = "14";
    //    public static final String TIPS_REGISTER_PASSWORD_ALLNUM="15";
    public static final String TIPS_LOGIN_PASSWORD_ALLNUM = "16";
    //    public static final String TIPS_NO_WEIXIN="17";
//    public static final String TIPS_PHONRNUMBER_OR_PASSWORD_ERR="18";
//    public static final String TIPS_NO_NETWORK="19";
//    public static final String TIPS_NO_PASSWORD="20";
//    public static final String TIPS_PHONENUM_NO_REGISTER="21";
//    public static final String TIPS_LOGIN_ERROR="22";
//    public static final String TIPS_REGISTER_NO_YANZHENGMA="23";
//    public static final String TIPS_REGISTER_NO_NICKNAME="24";
//    public static final String TIPS_REGISTER_PASSWORD_LENERR="25";
//    public static final String TIPS_REGISTER_PASSWORD_TWO_DIF="26";
//    public static final String TIPS_REGISTER_SUCCESS="27";
//    public static final String TIPS_REGISTER_FAILED="28";
    public static final String TIPS_SEND_FAILED = "29";
//    public static final String TIPS_REGISTER_ERROR="30";


    // http://img.feihutv.cn/uploadHtml/register.html
    public static final String REGISTER_URL = SharePreferenceUtil.getSession(
            FeihuZhiboApplication.getApplication(), "registerUrl") + "?fromAccountId="
            + SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_ACCOUNTID");



}
