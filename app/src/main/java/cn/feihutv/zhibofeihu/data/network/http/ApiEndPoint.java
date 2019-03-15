package cn.feihutv.zhibofeihu.data.network.http;


import cn.feihutv.zhibofeihu.BuildConfig;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/09/25
 *     desc   : 定义http api 接口访问路径
 *     version: 1.0
 * </pre>
 */
public final class ApiEndPoint {


    /**
     * 服务地址
     */
    public static final String ENDPOINT_SERVER_RUL = BuildConfig.BASE_URL + "/";


    /**
     * 用户模块访问路径
     */
    public final static class UserPoint {


        /**
         * 登录接口访问地址
         */
        public static final String ENDPOINT_SERVER_LOGIN = BuildConfig.BASE_URL
                + "/userLogin";

        /**
         * 第三方登录接口访问地址
         */
        public static final String ENDPOINT_PLATFORM_LOGIN = BuildConfig.BASE_URL
                + "/platformLogin";

        /**
         * 游客登录接口访问地址
         */
        public static final String ENDPOINT_GUEST_LOGIN = BuildConfig.BASE_URL
                + "/guestLogin";
        /**
         * 用户背包数据访问地址
         */
        public static final String ENDPOINT_USER_BAG = BuildConfig.BASE_URL
                + "/loadBagGifts";
        /**
         * 获取认证状态
         */
        public static final String ENDPOINT_CERTIFI_DATA = BuildConfig.BASE_URL
                + "/getCertifiData";
    }


    /**
     * 公共模块访问路径
     */
    public final static class CommonPoint {


        /**
         * 检查更新
         */
        public static final String ENDPOINT_CHECK_UPDATE = BuildConfig.BASE_URL
                + "/checkVersion";

        /**
         * 获取socket token
         */
        public static final String ENDPOINT_GET_SOCKET_TOKEN = BuildConfig.BASE_URL
                + "/getSocketToken";

        /**
         * 获取房间信息
         */
        public static final String ENDPOINT_GET_ROOM_INFO = BuildConfig.BASE_URL
                + "/loadRoomById";

        /**
         * 获取签到信息
         */
        public static final String ENDPOINT_GET_SIGN_DATA = BuildConfig.BASE_URL
                + "/v3/loadSignData";

        public static final String ENDPOINT_GET_SIGN_V = "v3/loadSignData";

        /**
         * 签到
         */
        public static final String ENDPOINT_TO_SIGN = BuildConfig.BASE_URL
                + "/v3/sign";

        /**
         * 签到
         */
        public static final String ENDPOINT_TO_SIGN_V = "v3/sign";
        /**
         * Android提交版本信息
         */
        public static final String ENDPOINT_LOG_DEVICE = BuildConfig.BASE_URL
                + "/logDevice";

        /**
         * 获取验证码
         */
        public static final String ENDPOINT_VERIFYCODE = BuildConfig.BASE_URL
                + "/getVerificationCode";

        /**
         * 找回密码
         */
        public static final String ENDPOINT_RESTPASSWORD = BuildConfig.BASE_URL
                + "/resetPassword";

        /**
         * 注册用户
         */
        public static final String ENDPOINT_CREATEUSER = BuildConfig.BASE_URL
                + "/createUser";

        /**
         * 定位
         */
        public static final String ENDPOINT_LOCATION = "http://maps.google.cn/maps/api/geocode/json";

        /**
         * 提交分享日志
         */
        public static final String ENDPOINT_LOGSHARE = BuildConfig.BASE_URL
                + "/logShare";

        /**
         * 关注
         */
        public static final String ENDPOINT_FOLLOW = BuildConfig.BASE_URL
                + "/follow";
        /**
         * 取消关注
         */
        public static final String ENDPOINT_UNFOLLOW = BuildConfig.BASE_URL
                + "/unfollow";

        /**
         * 加载房间贡献榜
         */
        public static final String ENDPOINT_LOADROOMCONTRILIST = BuildConfig.BASE_URL
                + "/loadRoomContriList";

        /**
         * 购买商品
         */
        public static final String ENDPOINT_BUYGOODS = BuildConfig.BASE_URL
                + "/v3/buyGoods";

        /**
         * 送礼物
         */
        public static final String ENDPOINT_SENDGIFT = BuildConfig.BASE_URL
                + "/v3/sendGift";

        /**
         * 购买商品
         */
        public static final String ENDPOINT_BUYGOODSV = "v3/buyGoods";

        /**
         * 送礼物
         */
        public static final String ENDPOINT_SENDGIFTV = "v3/sendGift";

        /**
         * 消息接口
         */
        public static final String ENDPOINT_LOADMSGLIST = BuildConfig.BASE_URL
                + "/loadMsgList";

        /**
         * 获取好友列表
         */
        public static final String ENDPOINT_GETFRIENDS = BuildConfig.BASE_URL
                + "/getFriends";

        /**
         * 私信
         */
        public static final String ENDPOINT_MESSAGE = BuildConfig.BASE_URL
                + "/message";

        /**
         * 检查动态是否存在
         */
        public static final String ENDPOINT_IS_FEEDEXIST = BuildConfig.BASE_URL
                + "/isFeedExist";
    }

    /**
     * 首页模块访问路径
     */
    public final static class TopPoint {

        /**
         * 加载直播列表
         */
        public static final String ENDPOINT_LOAD_ROOM_LIST = BuildConfig.BASE_URL
                + "/loadRoomListByTag";

        /**
         * 加载关注推荐列表
         */
        public static final String ENDPOINT_LOAD_CARE_RECOMMENDED_LIST = BuildConfig.BASE_URL
                + "/loadCareRecommendedList";

        /**
         * 加载首页banner
         */
        public static final String ENDPOINT_GET_BANNER_BY_TYPE = BuildConfig.BASE_URL
                + "/getBannerByType";


        /**
         * 搜索
         */
        public static final String ENDPOINT_SEARCH = BuildConfig.BASE_URL
                + "/v2/search";

        /**
         * 搜索
         */
        public static final String ENDPOINT_SEARCH_V = "v2/search";


        /**
         * 搜索页面的推荐
         */
        public static final String ENDPOINT_LOAD_RECOMMENDED_LIST = BuildConfig.BASE_URL
                + "/loadRecommendedList";

    }

    public final static class BandDanPoint {
        /**
         * 拉取贡献榜
         */
        public static final String ENDPOINT_LOAD_CONTRI_RANK_LIST = BuildConfig.BASE_URL
                + "/loadContriRankList";

        /**
         * 拉取排行榜
         */
        public static final String ENDPOINT_LOAD_INCOME_RANK_LIST = BuildConfig.BASE_URL
                + "/loadIncomeRankList";


        /**
         * 拉取幸运榜-幸运记录
         */
        public static final String ENDPOINT_LOAD_LUCK_RECORD_LIST = BuildConfig.BASE_URL
                + "/loadLuckRecordList";

        /**
         * 拉取幸运榜-总榜
         */
        public static final String ENDPOINT_LOAD_LUCKRANK_LIST = BuildConfig.BASE_URL
                + "/loadLuckRankList";

        /**
         * 拉取守护榜
         */
        public static final String ENDPOINT_GET_GUARD_RANK = BuildConfig.BASE_URL
                + "/getGuardRank";

    }

    public final static class LiveRoomPoint {
        /**
         * 切换到后台或前台
         */
        public static final String ENDPOINT_SWITCHAPPFOCUS = BuildConfig.BASE_URL
                + "/switchAppFocus";
        /**
         * 设置场控
         */
        public static final String ENDPOINT_SETROOMMGR = BuildConfig.BASE_URL
                + "/setRoomMgr";
        /**
         * 取消场控
         */
        public static final String ENDPOINT_CANCEL_ROOMMGR = BuildConfig.BASE_URL
                + "/cancelRoomMgr";

        /**
         * 禁言
         */
        public static final String ENDPOINT_BAN = BuildConfig.BASE_URL
                + "/ban";

        /**
         * 全站小喇叭
         */
        public static final String ENDPOINT_SENDLOUDSPEAKER = BuildConfig.BASE_URL
                + "/sendLoudspeaker";

        /**
         * 房间在线列表
         */
        public static final String ENDPOINT_LOADROOMMEMBERS = BuildConfig.BASE_URL
                + "/loadRoomMembers";

        /**
         * 游客数量
         */
        public static final String ENDPOINT_LOADROOMGUESTCNT = BuildConfig.BASE_URL
                + "/loadRoomGuestCnt";

        /**
         * 房管列表
         */
        public static final String ENDPOINT_GETROOMMGRS = BuildConfig.BASE_URL
                + "/getRoomMgrs";

        /**
         * 获取当前坐骑
         */
        public static final String ENDPOINT_GETCURRMOUNT = BuildConfig.BASE_URL
                + "/getRoomInfo";

        /**
         * 主播下播后推荐2个直播间
         */
        public static final String ENDPOINT_LOADOTHERROOMS = BuildConfig.BASE_URL
                + "/loadOtherRooms";

        /**
         * 举报
         */
        public static final String ENDPOINT_REPORT = BuildConfig.BASE_URL
                + "/report";
        /**
         * 房间贵宾席
         */
        public static final String ENDPOINT_ROOMVIP = BuildConfig.BASE_URL
                + "/loadRoomVipUsers";

        /**
         *检查是否禁播
         */
        public static final String ENDPOINT_TESTSTARTLIVE = BuildConfig.BASE_URL
                + "/testStartLive";
    }

    public final static class WanFaPoint {
        /**
         * 获取幸运彩池开奖倒计时（进入直播间时）
         */
        public static final String ENDPOINT_GETJACKPOTCOUNTDOWN = BuildConfig.BASE_URL
                + "/getJackpotCountDown";

        /**
         * 获取幸运彩池数据
         */
        public static final String ENDPOINT_GETJACKPOTDATA = BuildConfig.BASE_URL
                + "/getJackpotData";
        /**
         * 幸运彩池获取幸运记录
         */
        public static final String ENDPOINT_GETLUCKLOGSBYID = BuildConfig.BASE_URL
                + "/getLuckLogsById";

        /**
         * 幸运彩池获取我的幸运记录
         */
        public static final String ENDPOINT_GETMYLUCKLOGS = BuildConfig.BASE_URL
                + "/getMyLuckLogs";

        /**
         * 获取玩法当前状态
         */
        public static final String ENDPOINT_GETGAMESTATUS = BuildConfig.BASE_URL
                + "/getGameStatus";

        /**
         * 主播开奖
         */
        public static final String ENDPOINT_GAMESETTLE = BuildConfig.BASE_URL
                + "/gameSettle";

        /**
         * 获取押注情况（用于5s轮询更新界面）
         */
        public static final String ENDPOINT_GETGAMEBET = BuildConfig.BASE_URL
                + "/getGameBet";

        /**
         * 收到每局结果后，前端查询输赢详情
         */
        public static final String ENDPOINT_GETGAMEROUNDRESULTDETAIL = BuildConfig.BASE_URL
                + "/getGameRoundResultDetail";

        /**
         * 主播抢权限
         */
        public static final String ENDPOINT_GAMEPREEMPT = BuildConfig.BASE_URL
                + "/gamePreempt";

        /**
         * 参加玩法
         */
        public static final String ENDPOINT_GAMEBET = BuildConfig.BASE_URL
                + "/gameBet";

        /**
         * 今日押注情况
         */
        public static final String ENDPOINT_GETUSERGAMEDATAV3 = BuildConfig.BASE_URL
                + "/getUserGameDataV3";

        /**
         * 开奖记录
         */
        public static final String ENDPOINT_GETGAMESETTLEHISTORY = BuildConfig.BASE_URL
                + "/getGameSettleHistory";

        /**
         * 竞猜历史
         */
        public static final String ENDPOINT_GETGAMEBETHISTORY = BuildConfig.BASE_URL
                + "/getGameBetHistory";

        /**
         * 获取玩法页面玩法列表
         */
        public static final String ENDPOINT_GETGAMELIST = BuildConfig.BASE_URL
                + "/getGameList";

        public static final String ENDPOINT_GETGAMEICON = BuildConfig.BASE_URL
                + "/getExtGameIcon";
    }

    //我的模块
    public static final class MePoint {
        /**
         * 获取上传图片到cos的sign
         */
        public static final String ENDPOINT_GET_COS_SIGN = BuildConfig.BASE_URL
                + "/getCosSign";


        /**
         * 社区-获取动态列表
         */
        public static final String ENDPOINT_LOAD_FEED_LIST = BuildConfig.BASE_URL
                + "/loadFeedList";

        /**
         * 社区-获取全部动态
         */
        public static final String ENDPOINT_LOAD_ALL_FEED_LIST = BuildConfig.BASE_URL
                        + "/loadAllFeedList";

        /**
         * 获取动态详情
         */
        public static final String ENDPOINT_LOAD_FEED_DETAIL = BuildConfig.BASE_URL
                + "/loadFeedDetail";


        /**
         * 动态点赞
         */
        public static final String ENDPOINT_LIKE_FEED = BuildConfig.BASE_URL
                + "/likeFeed";


        /**
         * 动态点赞
         */
        public static final String ENDPOINT_LOAD_COMMENT_LIST = BuildConfig.BASE_URL
                + "/loadCommentList";

        /**
         * 删除动态
         */
        public static final String ENDPOINT_DELETE_FEED = BuildConfig.BASE_URL
                + "/deleteFeed";


        /**
         * 发表动态评论
         */
        public static final String ENDPOINT_POST_FEED_COMMENT = BuildConfig.BASE_URL
                + "/postFeedComment";


        /**
         * 发
         */
        public static final String ENDPOINT_SHARE_FEED = BuildConfig.BASE_URL
                + "/shareFeed";


        /**
         * 加载收礼和送礼明细
         */
        public static final String ENDPOINT_LOAD_USER_HB_DATA = BuildConfig.BASE_URL
                + "/loadUserHBData";


        /**
         * 发布动态
         */
        public static final String ENDPOINT_POST_FEED = BuildConfig.BASE_URL
                + "/postFeed";


        /**
         * 修改密码
         */
        public static final String ENDPOINT_MODIFY_USERPASS = BuildConfig.BASE_URL
                + "/modifyUserPass";

        /**
         * 拉取关注列表
         */
        public static final String ENDPOINT_GETFOLLOWS = BuildConfig.BASE_URL
                + "/getFollows";

        /**
         * 拉取粉丝列表
         */
        public static final String ENDPOINT_GETFOLLOWERS = BuildConfig.BASE_URL
                + "/getFollowers";

        /**
         * 绑定手机号码
         */
        public static final String ENDPOINT_BINDPHONE = BuildConfig.BASE_URL
                + "/bindPhone";

        /**
         * 绑定手机号码
         */
        public static final String ENDPOINT_CHECKVERIFICODE = BuildConfig.BASE_URL
                + "/checkVerifiCode";

        /**
         * 更换手机号码
         */
        public static final String ENDPOINT_MODIFYPHONE = BuildConfig.BASE_URL
                + "/modifyPhone";

        /**
         * 支付
         */
        public static final String ENDPOINT_PAY = BuildConfig.BASE_URL
                + "/pay";


        /**
         * 充值列表
         */
        public static final String ENDPOINT_GETPAYLIST = BuildConfig.BASE_URL
                + "/getPayList";

        /**
         * 提现
         */
        public static final String ENDPOINT_ENCASH = BuildConfig.BASE_URL
                + "/encash";


        /**
         * 获取账号提现相关信息
         */
        public static final String ENDPOINT_GET_ENCASHINFO = BuildConfig.BASE_URL
                + "/getEncashInfo";

        /**
         * 绑定支付宝
         */
        public static final String ENDPOINT_BINDALIPAY = BuildConfig.BASE_URL
                + "/bindAlipay";

        /**
         * 提现列表
         */
        public static final String ENDPOINT_GETENCASHLIST = BuildConfig.BASE_URL
                + "/getEncashList";

        /**
         * 拉取黑名单列表
         */
        public static final String ENDPOINT_GETBLACKLIST = BuildConfig.BASE_URL
                + "/getBlacklist";

        /**
         * 拉取黑名单列表
         */
        public static final String ENDPOINT_UNBLOCK = BuildConfig.BASE_URL
                + "/unblock";

        /**
         * 加入黑名单列表
         */
        public static final String ENDPOINT_BLOCK = BuildConfig.BASE_URL
                + "/block";

        /**
         * 我的守护
         * ta的守护
         */
        public static final String ENDPOINT_GET_USERGUARDS = BuildConfig.BASE_URL
                + "/getUserGuards";

        /**
         * 我守护的
         * ta守护的
         */
        public static final String ENDPOINT_GET_USERGUARDLIST = BuildConfig.BASE_URL
                + "/getUserGuardList";


        /**
         * 设置座驾
         */
        public static final String ENDPOINT_SETMOUNT = BuildConfig.BASE_URL
                + "/setMount";

        /**
         * 加载靓号列表
         */
        public static final String ENDPOINT_LOAD_SHOPACCOUNTIDLIST = BuildConfig.BASE_URL
                + "/loadShopAccountIdList";

        /**
         * 购买靓号
         */
        public static final String ENDPOINT_BUY_SHOPACCOUNTID = BuildConfig.BASE_URL
                + "/buyShopAccountId";

        /**
         * 检索飞虎id
         */
        public static final String ENDPOINT_CHECK_ACCOUNTID = BuildConfig.BASE_URL
                + "/checkAccountId";

        /**
         * 商城守护检索主播
         */
        public static final String ENDPOINT_CHECK_GUARDINFO = BuildConfig.BASE_URL
                + "/checkGuardInfo";


        /**
         * 商城守护主播
         */
        public static final String ENDPOINT_BUYGUARD = BuildConfig.BASE_URL
                + "/buyGuard";

        /**
         * 获得背包礼物列表
         */
        public static final String ENDPOINT_LOADBAGGIFTS = BuildConfig.BASE_URL
                + "/loadBagGifts";

        /**
         * 获取座驾列表
         */
        public static final String ENDPOINT_GET_MOUNTLIST = BuildConfig.BASE_URL
                + "/getMountList";

        /**
         * 发起交易
         */
        public static final String ENDPOINT_MAKE_TRADE = BuildConfig.BASE_URL
                + "/makeTrade";

        /**
         * 搜索靓号价格区间
         */
        public static final String ENDPOINT_LOAD_LIANGSEARCHKEY = BuildConfig.BASE_URL
                + "/loadLiangSearchKey";

        /**
         * 获取消息接收状态
         */
        public static final String ENDPOINT_GET_MSGSWITCH_STATUS = BuildConfig.BASE_URL
                + "/getMsgSwitchStatus";

        /**
         * 设置消息接收状态
         */
        public static final String ENDPOINT_SET_MSGSWITCH_STATUS = BuildConfig.BASE_URL
                + "/setMsgSwitchStatus";

        /**
         * 接受交易请求
         */
        public static final String ENDPOINT_ACCEPT_TRADE = BuildConfig.BASE_URL
                + "/acceptTrade";




    }


    //vip模块
    public static final class VipPoint {


        /**
         * 获得vip数据
         */
        public static final String ENDPOINT_GET_VIP_DATA = BuildConfig.BASE_URL
                + "/getVipData";

        /**
         * 购买vip
         */
        public static final String ENDPOINT_BUY_VIP = BuildConfig.BASE_URL
                + "/buyVip";

        /**
         * 赠送vip
         */
        public static final String ENDPOINT_SEND_VIP = BuildConfig.BASE_URL
                + "/sendVip";


        /**
         * vip-我的收获记录
         */
        public static final String ENDPOINT_GET_VIP_RECEIVE_LOG = BuildConfig.BASE_URL
                + "/getVipRecvLog";


        /**
         * vip-我的赠送记录
         */
        public static final String ENDPOINT_GET_VIP_SEND_LOG = BuildConfig.BASE_URL
                + "/getVipSendLog";


    }


    //mv模块
    public static final class MVPoint {

        /**
         * 获取上传mv视频的token和时间戳
         */
        public static final String ENDPOINT_GET_MV_TOKEN = BuildConfig.BASE_URL
                + "/getMVToken";


        /**
         * 发布mv
         */
        public static final String ENDPOINT_POST_MV = BuildConfig.BASE_URL
                + "/postMV";


        /**
         * mv广场
         */
        public static final String ENDPOINT_GET_ALL_MV_LIST = BuildConfig.BASE_URL
                + "/getAllMVList";


        /**
         * 发布需求
         */
        public static final String ENDPOINT_POST_NEED = BuildConfig.BASE_URL
                + "/postNeed";


        /**
         * 需求广场列表
         */
        public static final String ENDPOINT_GET_ALL_NEED_LIST = BuildConfig.BASE_URL
                + "/getAllNeedList";


        /**
         * 我的需求列表-所有需求
         */
        public static final String ENDPOINT_GET_MY_NEED_LIST = BuildConfig.BASE_URL
                + "/getMyNeedList";

        /**
         * 发布需求=- 获取发布赏金
         */
        public static final String ENDPOINT_GET_NEED_SYS_HB = BuildConfig.BASE_URL
                + "/getNeedSysHB";

        /**
         * 发布需求=- 修改需求
         */
        public static final String ENDPOINT_EDIT_NEED = BuildConfig.BASE_URL
                + "/editNeed";
        /**
         * 取消收藏需求
         */
        public static final String ENDPOINT_UN_COLLECT_NEED = BuildConfig.BASE_URL
                + "/uncollectNeed";

        /**
         * 收藏需求
         */
        public static final String ENDPOINT_COLLECT_NEED = BuildConfig.BASE_URL
                + "/collectNeed";



        /**
         * 获取需求下的作品列表
         */
        public static final String ENDPOINT_GET_MY_NEED_MV_LIST = BuildConfig.BASE_URL
                + "/getMyNeedMVList";



        /**
         * 获购买mv
         */
        public static final String ENDPOINT_BUY_MV = BuildConfig.BASE_URL
                + "/buyMV";



        /**
         * 反馈给主播mv作品修改
         */
        public static final String ENDPOINT_FEEDBACK_MV = BuildConfig.BASE_URL
                + "/feedbackMV";


        /**
         *  mv详情
         */
        public static final String ENDPOINT_GET_MV_DETAIL = BuildConfig.BASE_URL
                + "/getMVDetail";


        /**
         * 获取mv评论列表
         */
        public static final String ENDPOINT_GET_MV_COMMENT_LIST = BuildConfig.BASE_URL
                + "/getMVCommentList";


        /**
         *  mv点赞
         */
        public static final String ENDPOINT_LIKE_MV = BuildConfig.BASE_URL
                + "/likeMV";


        /**
         *  对评论点赞
         */
        public static final String ENDPOINT_LIKE_COMMENT = BuildConfig.BASE_URL
                + "/likeComment";

        /**
         *  mv送礼
         */
        public static final String ENDPOINT_GIFT_MV = BuildConfig.BASE_URL
                + "/giftMV";

        /**
         *  发表mv评论
         */
        public static final String ENDPOINT_POST_MV_COMMENT = BuildConfig.BASE_URL
                + "/postMVComment";


        /**
         *  我的mv作品列表
         */
        public static final String ENDPOINT_GET_MY_MV_LIST = BuildConfig.BASE_URL
                + "/getMyMVList";

        /**
         * 播放MV
         */
        public static final String ENDPOINT_PLAY_MV= BuildConfig.BASE_URL
                + "/playMV";

        /**
         * mv收礼记录
         */
        public static final String ENDPOINT_GET_MV_GIFT_LOG= BuildConfig.BASE_URL
                + "/getMVGiftLog";

        /**
         * 收藏列表
         */
        public static final String ENDPOINT_GET_NEED_COLLECT_LIST= BuildConfig.BASE_URL
                + "/getNeedCollectList";


        /**
         * 我的定制MV作品
         */
        public static final String ENDPOINT_GET_MY_CUSTOM_MADE_MV_LIST= BuildConfig.BASE_URL
                + "/getMyCustomMadeMVList";


        /**
         * 检查是否可以提交MV
         */
        public static final String ENDPOINT_ENABLE_POST_MV= BuildConfig.BASE_URL
                + "/enablePostMV";

        /**
         * 检查是否可以提交MV
         */
        public static final String ENDPOINT_GET_OTHER_MV_LIST= BuildConfig.BASE_URL
                + "/getOtherMVList";



        /**
         * 分享MV
         */
        public static final String ENDPOINT_SHARE_MV= BuildConfig.BASE_URL
                + "/shareMV";


        /**
         * 删除MV
         */
        public static final String ENDPOINT_DELETE_MV= BuildConfig.BASE_URL
                + "/deleteMV";


        /**
         * 删除need
         */
        public static final String ENDPOINT_DELETE_NEED= BuildConfig.BASE_URL
                + "/deleteNeed";



        /**
         * 查询是否有mv系统消息
         */
        public static final String ENDPOINT_QUERY_MV_NOTICE= BuildConfig.BASE_URL
                + "/queryMVNotice";



        /**
         * 设置mv系统消息为已读状态
         */
        public static final String ENDPOINT_CANCEL_MV_NOTICE= BuildConfig.BASE_URL
                + "/cancelMVNotice";

        /**
         * 下载mv
         */
        public static final String ENDPOINT_DOWN_LOAD_MV= BuildConfig.BASE_URL
                + "/downloadMV";



    }
}

