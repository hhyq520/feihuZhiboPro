package cn.feihutv.zhibofeihu.utils;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


public class SharePreferenceUtil {

	public static void saveSeesion(Context context, String key, String value) {
		SharedPreferences mySharePreferences = context.getSharedPreferences(
				"feihutv_pref", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = mySharePreferences.edit();
		editor.putString(key, value);
		editor.commit();

	}
	public static void saveSeesionBoolean(Context context, String key, boolean value) {
		SharedPreferences mySharePreferences = context.getSharedPreferences(
				"feihutv_pref", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = mySharePreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	public static void saveSeesionFloat(Context context, String key, float value) {
		SharedPreferences mySharePreferences = context.getSharedPreferences(
				"feihutv_pref", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = mySharePreferences.edit();
		editor.putFloat(key, value);
		editor.commit();
	}
	public static void saveSeesionInt(Context context, String key, int value) {
		SharedPreferences mySharePreferences = context.getSharedPreferences(
				"feihutv_pref", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = mySharePreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static void saveSeesionLong(Context context, String key, long value) {
		SharedPreferences mySharePreferences = context.getSharedPreferences(
				"feihutv_pref", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = mySharePreferences.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public static String getSession(Context context, String key) {
		return getSession(context, key, null);
	}
    public static String getSession(Context context, String key, String defaultValue) {
        SharedPreferences mySharePerferences = context.getSharedPreferences(
            "feihutv_pref", Activity.MODE_PRIVATE);
        String userId = mySharePerferences.getString(key, defaultValue);
        return userId;
    }
	public static boolean getSessionBoolean(Context context, String key) {
		SharedPreferences mySharePerferences = context.getSharedPreferences(
				"feihutv_pref", Activity.MODE_PRIVATE);
		boolean userId = mySharePerferences.getBoolean(key, false);
		return userId;
	}
	public static boolean getSessionBoolean(Context context, String key, boolean defvalue) {
		SharedPreferences mySharePerferences = context.getSharedPreferences(
				"feihutv_pref", Activity.MODE_PRIVATE);
		boolean userId = mySharePerferences.getBoolean(key, defvalue);
		return userId;
	}
	public static Float getSessionFloat(Context context, String key) {
		SharedPreferences mySharePerferences = context.getSharedPreferences(
				"feihutv_pref", Activity.MODE_PRIVATE);
		Float userId = mySharePerferences.getFloat(key, 0);
		return userId;
	}
	public static int getSessionInt(Context context, String key) {
		SharedPreferences mySharePerferences = context.getSharedPreferences(
				"feihutv_pref", Activity.MODE_PRIVATE);
		int position = mySharePerferences.getInt(key, 0);
		return position;
	}
	public static int getSessionInt(Context context, String key, int defValue) {
		SharedPreferences mySharePerferences = context.getSharedPreferences(
				"feihutv_pref", Activity.MODE_PRIVATE);
		int position = mySharePerferences.getInt(key, defValue);
		return position;
	}
	public static long getSessionLong(Context context, String key) {
		SharedPreferences mySharePerferences = context.getSharedPreferences(
				"feihutv_pref", Activity.MODE_PRIVATE);
		long position = mySharePerferences.getLong(key, 0);
		return position;
	}
	public static long getSessionLong(Context context, String key, long devaue) {
		SharedPreferences mySharePerferences = context.getSharedPreferences(
				"feihutv_pref", Activity.MODE_PRIVATE);
		long position = mySharePerferences.getLong(key,devaue);
		return position;
	}

	/**
	 * 保存登录用户信息
	 * @param context
	 * @param user
	 */
//	public static void saveLoginUser(Context context, TCUserInfo user){
//		SharedPreferences mySharePerferences = context.getSharedPreferences(
//				"feihutv_pref", Activity.MODE_PRIVATE);
//		SharedPreferences.Editor editor = mySharePerferences.edit();
//		editor.putString(TCConstants.USER_ID,user.getUserId());
//		editor.putString(TCConstants.PHONE_NUMBER,user.getPhoneNumber());
//		editor.putString(TCConstants.NICK_NAME,user.getNickName());
//		editor.putString(TCConstants.ACCOUNT_ID,user.getAccountId());
//		editor.putString(TCConstants.HEAD_URL,user.getHeadUrl());
//		editor.putInt(TCConstants.GENDER,user.getGender());
//		editor.putInt(TCConstants.HB,user.getHb());
//		editor.putInt(TCConstants.YHB,user.getYhb());
//		editor.putInt(TCConstants.GHB,user.getGhb());
//		editor.putInt(TCConstants.EXP,user.getExp());
//		editor.putInt(TCConstants.LEVEL,user.getLevel());
//		editor.putString(TCConstants.LOCATION,user.getLocation());
//		editor.putString(TCConstants.SIGNATURE,user.getSingature());
//		editor.putString(TCConstants.LOCATION,user.getLocation());
//		editor.putString(TCConstants.ROOMNAME,user.getRoomName());
//		editor.putInt(TCConstants.CERIFICATION,user.getCertifiStatus());
//		editor.putInt(TCConstants.CONTRI,user.getContri());
//		editor.putInt(TCConstants.INCOME,user.getIncome());
//		editor.putString(TCConstants.LIVECOVER,user.getLiveCover());
//		editor.putInt(TCConstants.MNICKNAME,user.getmNickname());
//		editor.putInt(TCConstants.MGENDER,user.getmGender());
//		editor.putInt(TCConstants.CTRMOUNT,user.getCrtMount());
//		editor.putString(TCConstants.COSHEADICONNAME,user.getCosHeadIconName());
//		editor.putInt(TCConstants.FOLLOWERS,user.getFollowers());
//		editor.putInt(TCConstants.FOLLOWS,user.getFollows());
//        editor.putInt(TCConstants.INCOME, user.getIncome());
//        editor.putInt(TCConstants.LIVECNT, user.getLiveCnt());
//		editor.putString(TCConstants.GuildName, user.getGuildName());
//		editor.commit();
//	}
	/**
	 * 获取登录用户信息
	 * @param context
	 */
//	public static TCUserInfo getLoginUser(Context context){
//		SharedPreferences mySharePerferences = context.getSharedPreferences("feihutv_pref", Activity.MODE_PRIVATE);
//		TCUserInfo userInfo=new TCUserInfo();
//		userInfo.setUserId(mySharePerferences.getString(TCConstants.USER_ID,""));
//		userInfo.setPhoneNumber(mySharePerferences.getString(TCConstants.PHONE_NUMBER,"1"));
//		userInfo.setNickName(mySharePerferences.getString(TCConstants.NICK_NAME,"name"));
//		userInfo.setAccountId(mySharePerferences.getString(TCConstants.ACCOUNT_ID,"1"));
//		userInfo.setHeadUrl(mySharePerferences.getString(TCConstants.HEAD_URL,""));
//		userInfo.setGender(mySharePerferences.getInt(TCConstants.GENDER,0));
//		userInfo.setHb(mySharePerferences.getInt(TCConstants.HB,0));
//		userInfo.setYhb(mySharePerferences.getInt(TCConstants.YHB,0));
//		userInfo.setGhb(mySharePerferences.getInt(TCConstants.GHB,0));
//		userInfo.setExp(mySharePerferences.getInt(TCConstants.EXP,0));
//		userInfo.setLevel(mySharePerferences.getInt(TCConstants.LEVEL,0));
//		userInfo.setLocation(mySharePerferences.getString(TCConstants.LOCATION,""));
//		userInfo.setRoomName(mySharePerferences.getString(TCConstants.ROOMNAME,""));
//		userInfo.setSingature(mySharePerferences.getString(TCConstants.SIGNATURE,""));
//		userInfo.setLocation(mySharePerferences.getString(TCConstants.LOCATION,""));
//		userInfo.setIncome(mySharePerferences.getInt(TCConstants.INCOME,0));
//		userInfo.setContri(mySharePerferences.getInt(TCConstants.CONTRI,0));
//		userInfo.setLiveCover(mySharePerferences.getString(TCConstants.LIVECOVER,""));
//		userInfo.setmGender(mySharePerferences.getInt(TCConstants.MGENDER,0));
//		userInfo.setmNickname(mySharePerferences.getInt(TCConstants.MNICKNAME,0));
//		userInfo.setCertifiStatus(mySharePerferences.getInt(TCConstants.CERIFICATION,0));
//		userInfo.setCosHeadIconName(mySharePerferences.getString(TCConstants.COSHEADICONNAME,""));
//		userInfo.setFollowers(mySharePerferences.getInt(TCConstants.FOLLOWERS,0));
//		userInfo.setFollows(mySharePerferences.getInt(TCConstants.FOLLOWS,0));
//		userInfo.setCrtMount(mySharePerferences.getInt(TCConstants.CTRMOUNT,0));
//        userInfo.setLiveCnt(mySharePerferences.getInt(TCConstants.LIVECNT, 0));
//		userInfo.setGuildName(mySharePerferences.getString(TCConstants.GuildName,""));
//		Log.e("userInfo",userInfo.toString());
//		return userInfo;
//	}

	public static void clearAll(Context context){
		SharedPreferences mySharePreferences = context.getSharedPreferences(
				"feihutv_pref", Activity.MODE_PRIVATE);
		mySharePreferences.edit().clear().commit();
	}
}
