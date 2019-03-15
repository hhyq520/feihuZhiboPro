package cn.feihutv.zhibofeihu.data.network.socket;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/10/12
 *     desc   : socket 请求路径
 *     version: 1.0
 * </pre>
 */
public class SocketApiEndPoint {

    /**
     * 公共模块访问路径
     */
    public final static class CommonPoint {

        /**
         *
         */
        public static final String ENDPOINT_VERIFY_TOKEN = "verifyToken";


        /**
         *连接socket
         */
        public static final String ENDPOINT_CONNING_SOCKET = "ConningSocket";


        /**
         *获取用户基本数据
         */
        public static final String ENDPOINT_LOAD_USER_DATABASE = "loadUserDataBase";


        /**
         *获取他人基本数据
         */
        public static final String ENDPOINT_LOAD_OTHER_USER_DATABASE = "loadOtherUserInfo";

        /**
         *给直播间内用户发送聊天消息
         */
        public static final String ENDPOINT_SEND_ROOMMSG = "sendRoomChat";
    }

    public final static class LivePoint{
        /**
         * 加入房间
         */
        public static final String ENDPOINT_JOINROOM ="joinRoom";

        /**
         * 开播
         */
        public static final String ENDPOINT_STARTLIVE = "startLive";

        /**
         * 退出房间
         */
        public static final String ENDPOINT_LEAVE_ROOM = "leaveRoom";

        /**
         * 关闭直播
         */
        public static final String ENDPOINT_CLOSELIVE = "closeLive";

        /**
         * 获取直播状态
         */
        public static final String ENDPOINT_GETLIVESTATUS = "getLiveStatus";
    }


    //我的模块
    public static final class MePoint{
        /**
         * 修改直播封面
         */
        public static final String ENDPOINT_MODIFY_PROFILE ="modifyProfile";


    }
}
