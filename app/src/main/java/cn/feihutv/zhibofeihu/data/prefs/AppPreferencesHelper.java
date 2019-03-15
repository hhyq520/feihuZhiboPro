package cn.feihutv.zhibofeihu.data.prefs;


import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import cn.feihutv.zhibofeihu.data.DataManager;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseResponse;
import cn.feihutv.zhibofeihu.di.ApplicationContext;
import cn.feihutv.zhibofeihu.di.PreferenceInfo;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/09/25
 *     desc   : Preferences 业务操作类
 *     version: 1.0
 * </pre>
 */
@Singleton
public class AppPreferencesHelper implements PreferencesHelper {

    private static final String PREF_KEY_USER_LOGGED_IN_MODE = "PREF_KEY_USER_LOGGED_IN_MODE";
    private static final String PREF_KEY_CURRENT_USER_ID = "PREF_KEY_CURRENT_USER_ID";
    public static final String PREF_KEY_TIME_CHA_ZHI = "PREF_KEY_TIME_CHA_ZHI";
    private static final String PREF_KEY_API_KEY = "PREF_KEY_API_KEY";
    private static final String PREF_LOG_DEVICE_ID = "PREF_LOG_DEVICE_ID";
    private static final String PREF_KEY_CURRENT_USER_PROFILE_PIC_URL
            = "PREF_KEY_CURRENT_USER_PROFILE_PIC_URL";

    /**
     * UserId      	string	//用户Id
     * Phone       	string //用户手机号
     * NickName    	string //用户昵称
     * AccountId   	string //用户对外显示的ID
     * HeadUrl     	string //用户头像
     * Gender      	int	//用户性别
     * HB             	int	//用户虎币值
     * GHB          	int       //金虎币
     * YHB          	int       //银虎币
     * Exp       	int	//用户经验值
     * Level       	int	//用户等级
     * Follows   	int  //关注数
     * Followers     	int  //粉丝数量
     * RoomName    	string // 直播间名字
     * LiveCnt     int   //直播次数
     * Signature   	string //个性签名
     * Location    	string //所在地
     * CertifiStatus int//认证状态 0未认证 1已认证 2认证中  3认证失败
     * Income      	int	//总收到金虎币
     * Contri      	int	//总送出虎币
     * LiveCover     string   //直播间封面
     * CrtMount       int        //当前坐骑ID
     * Modified {
     * NickName int //昵称已改动次数，无字段就是0
     * Gender int //性别已修改次数，无字段就是0
     * }
     * GuildName string //公会名，为空时表示不属于任何公会
     */
    public static final String PREF_KEY_USERID = "PREF_KEY_USERID";
    public static final String PREF_KEY_PHONE = "PREF_KEY_PHONE";
    public static final String PREF_KEY_NICKNAME = "PREF_KEY_NICKNAME";
    public static final String PREF_KEY_ACCOUNTID = "PREF_KEY_ACCOUNTID";
    public static final String PREF_KEY_HEADURL = "PREF_KEY_HEADURL";
    public static final String PREF_KEY_GENDER = "PREF_KEY_GENDER";
    public static final String PREF_KEY_HB = "PREF_KEY_HB";
    public static final String PREF_KEY_GHB = "PREF_KEY_GHB";
    public static final String PREF_KEY_YHB = "PREF_KEY_YHB";
    public static final String PREF_KEY_EXP = "PREF_KEY_EXP";
    public static final String PREF_KEY_LEVEL = "PREF_KEY_LEVEL";
    public static final String PREF_KEY_FOLLOWS = "PREF_KEY_FOLLOWS";
    public static final String PREF_KEY_FOLLOWERS = "PREF_KEY_FOLLOWERS";
    public static final String PREF_KEY_ROOMNAME = "PREF_KEY_ROOMNAME";
    public static final String PREF_KEY_LIVECNT = "PREF_KEY_LIVECNT";
    public static final String PREF_KEY_SIGNATURE = "PREF_KEY_SIGNATURE";
    public static final String PREF_KEY_LOCATION = "PREF_KEY_LOCATION";
    public static final String PREF_KEY_CERTIFISTATUS = "PREF_KEY_CERTIFISTATUS";
    public static final String PREF_KEY_INCOME = "PREF_KEY_INCOME";
    public static final String PREF_KEY_CONTRI = "PREF_KEY_CONTRI";
    public static final String PREF_KEY_LIVECOVER = "PREF_KEY_LIVECOVER";
    public static final String PREF_KEY_CRTMOUNT = "PREF_KEY_CRTMOUNT";
    public static final String PREF_KEY_GUILDNAME = "PREF_KEY_GUILDNAME";
    public static final String PREF_KEY_MODNICKNAME = "PREF_KEY_MODNICKNAME";
    public static final String PREF_KEY_MODGENDER = "PREF_KEY_MODGENDER";
    public static final String PREF_KEY_ISLIANG = "PREF_KEY_ISLIANG";
    public static final String PREF_KEY_VIP = "PREF_KEY_VIP";
    public static final String PREF_KEY_VIPEXPIRED = "PREF_KEY_VIPEXPIRED";
    public static final String PREF_KEY_XIAOLABA_COUNT = "PREF_KEY_XIAOLABA_COUNT";
    public static final String PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN";
    public static final String PREF_KEY_Vip_CZZ = "PREF_KEY_Vip_CZZ";
    public static final String PREF_KEY_VIP_EXPIRETIME = "PREF_KEY_VIP_EXPIRETIME";

    private final SharedPreferences mPrefs;

    @Inject
    public AppPreferencesHelper(@ApplicationContext Context context,
                                @PreferenceInfo String prefFileName) {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }


    @Override
    public String getCurrentUserId() {
        return mPrefs.getString(PREF_KEY_CURRENT_USER_ID, "");
    }

    @Override
    public void setCurrentUserId(String userId) {
        mPrefs.edit().putString(PREF_KEY_CURRENT_USER_ID, userId).apply();
    }


    @Override
    public int getCurrentUserLoggedInMode() {
        return mPrefs.getInt(PREF_KEY_USER_LOGGED_IN_MODE,
                DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.getType());
    }

    @Override
    public void setCurrentUserLoggedInMode(DataManager.LoggedInMode mode) {
        mPrefs.edit().putInt(PREF_KEY_USER_LOGGED_IN_MODE, mode.getType()).apply();
    }

    @Override
    public String getAccessToken() {
        return mPrefs.getString(PREF_KEY_ACCESS_TOKEN, null);
    }

    @Override
    public void setAccessToken(String accessToken) {
        mPrefs.edit().putString(PREF_KEY_ACCESS_TOKEN, accessToken).apply();
    }

    @Override
    public Long getTimeChaZhi() {
        return mPrefs.getLong(PREF_KEY_TIME_CHA_ZHI, 0L);
    }

    @Override
    public void setTimeChaZhi(Long chaZhi) {
        mPrefs.edit().putLong(PREF_KEY_TIME_CHA_ZHI, chaZhi).apply();
    }

    @Override
    public String getApiKey() {
        return mPrefs.getString(PREF_KEY_API_KEY, "");
    }

    @Override
    public void setApiKey(String key) {
        mPrefs.edit().putString(PREF_KEY_API_KEY, key).apply();
    }

    @Override
    public int getDeviceId() {
        int derId=mPrefs.getInt(PREF_LOG_DEVICE_ID, -1);

        return derId;
    }

    @Override
    public void setDeviceId(int deviceId) {
        mPrefs.edit().putInt(PREF_LOG_DEVICE_ID, deviceId).apply();
        //设备id 写入用户手机

    }

    @Override
    public LoadUserDataBaseResponse.UserData getUserData() {
        LoadUserDataBaseResponse.UserData userData = new LoadUserDataBaseResponse.UserData();
        userData.setUserId(mPrefs.getString(PREF_KEY_USERID, ""));
        userData.setPhone(mPrefs.getString(PREF_KEY_PHONE, ""));
        userData.setNickName(mPrefs.getString(PREF_KEY_NICKNAME, ""));
        userData.setAccountId(mPrefs.getString(PREF_KEY_ACCOUNTID, ""));
        userData.setHeadUrl(mPrefs.getString(PREF_KEY_HEADURL, ""));
        userData.setGender(mPrefs.getInt(PREF_KEY_GENDER, 0));
        userData.sethB(mPrefs.getLong(PREF_KEY_HB, 0));
        userData.setgHB(mPrefs.getLong(PREF_KEY_GHB, 0));
        userData.setyHB(mPrefs.getLong(PREF_KEY_YHB, 0));
        userData.setExp(mPrefs.getInt(PREF_KEY_EXP, 0));
        userData.setLevel(mPrefs.getInt(PREF_KEY_LEVEL, 0));
        userData.setFollows(mPrefs.getInt(PREF_KEY_FOLLOWS, 0));
        userData.setFollowers(mPrefs.getInt(PREF_KEY_FOLLOWERS, 0));
        userData.setRoomName(mPrefs.getString(PREF_KEY_ROOMNAME, ""));
        userData.setLiveCnt(mPrefs.getInt(PREF_KEY_LIVECNT, 0));
        userData.setSignature(mPrefs.getString(PREF_KEY_SIGNATURE, ""));
        userData.setLocation(mPrefs.getString(PREF_KEY_LOCATION, ""));
        userData.setCertifiStatus(mPrefs.getInt(PREF_KEY_CERTIFISTATUS, 0));
        userData.setIncome(mPrefs.getLong(PREF_KEY_INCOME, 0));
        userData.setContri(mPrefs.getLong(PREF_KEY_CONTRI, 0));
        userData.setLiveCover(mPrefs.getString(PREF_KEY_LIVECOVER, ""));
        userData.setCrtMount(mPrefs.getInt(PREF_KEY_CRTMOUNT, 0));
        userData.setGuildName(mPrefs.getString(PREF_KEY_GUILDNAME, ""));
        LoadUserDataBaseResponse.Modified modified = new LoadUserDataBaseResponse.Modified();
        modified.setGender(mPrefs.getInt(PREF_KEY_MODGENDER, 0));
        modified.setNickName(mPrefs.getInt(PREF_KEY_MODNICKNAME, 0));
        userData.setmModified(modified);
        userData.setVip(mPrefs.getInt(PREF_KEY_VIP, 1));
        userData.setVipExpired(mPrefs.getBoolean(PREF_KEY_VIPEXPIRED, false));
        userData.setLiang(mPrefs.getBoolean(PREF_KEY_ISLIANG, false));
        userData.setVipCZZ(mPrefs.getInt(PREF_KEY_Vip_CZZ, 0));
        userData.setVipExpireTime(mPrefs.getLong(PREF_KEY_VIP_EXPIRETIME, 0));
        return userData;
    }

    @Override
    public void saveUserData(LoadUserDataBaseResponse.UserData userData) {
        mPrefs.edit().putString(PREF_KEY_USERID, userData.getUserId()).apply();
        mPrefs.edit().putString(PREF_KEY_PHONE, userData.getPhone()).apply();
        mPrefs.edit().putString(PREF_KEY_NICKNAME, userData.getNickName()).apply();
        mPrefs.edit().putString(PREF_KEY_ACCOUNTID, userData.getAccountId()).apply();
        mPrefs.edit().putString(PREF_KEY_HEADURL, userData.getHeadUrl()).apply();
        mPrefs.edit().putInt(PREF_KEY_GENDER, userData.getGender()).apply();
        mPrefs.edit().putLong(PREF_KEY_HB, userData.gethB()).apply();
        mPrefs.edit().putLong(PREF_KEY_GHB, userData.getgHB()).apply();
        mPrefs.edit().putLong(PREF_KEY_YHB, userData.getyHB()).apply();
        mPrefs.edit().putInt(PREF_KEY_EXP, userData.getExp()).apply();
        mPrefs.edit().putInt(PREF_KEY_LEVEL, userData.getLevel()).apply();
        mPrefs.edit().putInt(PREF_KEY_FOLLOWS, userData.getFollows()).apply();
        mPrefs.edit().putInt(PREF_KEY_FOLLOWERS, userData.getFollowers()).apply();
        mPrefs.edit().putString(PREF_KEY_ROOMNAME, userData.getRoomName()).apply();
        mPrefs.edit().putInt(PREF_KEY_LIVECNT, userData.getLiveCnt()).apply();
        mPrefs.edit().putString(PREF_KEY_SIGNATURE, userData.getSignature()).apply();
        mPrefs.edit().putString(PREF_KEY_LOCATION, userData.getLocation()).apply();
        mPrefs.edit().putInt(PREF_KEY_CERTIFISTATUS, userData.getCertifiStatus()).apply();
        mPrefs.edit().putLong(PREF_KEY_INCOME, userData.getIncome()).apply();
        mPrefs.edit().putLong(PREF_KEY_CONTRI, userData.getContri()).apply();
        mPrefs.edit().putString(PREF_KEY_LIVECOVER, userData.getLiveCover()).apply();
        mPrefs.edit().putInt(PREF_KEY_CRTMOUNT, userData.getCrtMount()).apply();
        mPrefs.edit().putString(PREF_KEY_GUILDNAME, userData.getGuildName()).apply();
        mPrefs.edit().putInt(PREF_KEY_MODNICKNAME, userData.getmModified().getNickName()).apply();
        mPrefs.edit().putInt(PREF_KEY_MODGENDER, userData.getmModified().getGender()).apply();
        mPrefs.edit().putBoolean(PREF_KEY_ISLIANG, userData.isLiang()).apply();
        mPrefs.edit().putInt(PREF_KEY_VIP, userData.getVip()).apply();
        mPrefs.edit().putBoolean(PREF_KEY_VIPEXPIRED, userData.isVipExpired()).apply();
        mPrefs.edit().putInt(PREF_KEY_Vip_CZZ, userData.getVipCZZ()).apply();
        mPrefs.edit().putLong(PREF_KEY_VIP_EXPIRETIME, userData.getVipExpireTime()).apply();

    }

    @Override
    public int getXiaolabaCount() {
        return mPrefs.getInt(PREF_KEY_XIAOLABA_COUNT, 0);
    }

    @Override
    public void saveXiaolabaCount(int count) {
        mPrefs.edit().putInt(PREF_KEY_XIAOLABA_COUNT, count).apply();
    }
}
