package cn.feihutv.zhibofeihu.utils;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/09/25
 *     desc   : app 常量类
 *     version: 1.0
 * </pre>
 */
public final class AppConstants {

    public static final String STATUS_CODE_SUCCESS = "success";
    public static final String STATUS_CODE_FAILED = "failed";

    public static final int API_STATUS_CODE_LOCAL_ERROR = 0;

    public static final String DB_NAME = "feihutv.db";
    public static final String PREF_NAME = "feihutv_pref";

    public static final long NULL_INDEX = -1L;

    public static final String TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss";

    private AppConstants() {
        // This utility class is not publicly instantiable
    }


    public static String cosHeadUrl = "https://img.feihutv.cn//";

    public static String QQ_APP_ID="1106077402";
    public static String QQ_SECRET="0T9HrQW69t8q9ATI";
    public static String WX_APP_ID="wxca08974ea4a9efac";
    public static String WX_APP_SECRET="1a28ca4b5e15073a179406c1b828e219";
    public static final String Weibo_APP_KEY = "1973853121";
    public static final String Weibo_APP_SECRET = "76cf9d003a397b456a66bc374dcbae92";
    public static final String REDIRECT_URL = "http://api.weibo.com/oauth2/default.html";
    public static final String COS_APPID = "1253350878";

    public static final int ERROR_CODE_1 = -10;  // 连接socket失败

    public static final int ERROR_CODE_2 = -11; // 验证socket toke

}
