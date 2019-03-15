package cn.feihutv.zhibofeihu.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.UUID;

/**
 * Created by huanghao on 2017/5/19.
 */

public class DeviceInfoUtil {
    /**
     * 获取手机Android 版本（4.4、5.0、5.1 ...）
     *
     * @return
     */
    public static String getBuildVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机品牌
     *
     * @return
     */
    public static String getPhoneBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getPhoneModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取设备的唯一标识，deviceId
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        String serial = null;

        String m_szDevIDShort = "35" +
            Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

            Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

            Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

            Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

            Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

            Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

            Build.USER.length() % 10; //13 位

        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }


    /**
     * 判断是否为系统应用
     *
     * @param context
     * @return
     */
    public static boolean isSystemApplication(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo info = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_UNINSTALLED_PACKAGES);
            if (info != null && (info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                return true;
            } else if (info != null && (info.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                return true;
            }
        } catch (Exception e) {
			e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取手机的imei码
     *
     * @param context
     * @return
     */
    public static String getAndroid_IMEI(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = telephonyManager.getDeviceId();
            if (imei != null && !imei.equals("")){
                return imei;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取手机的imsi码
     *
     * @param context
     * @return
     */
    public static String getImsi(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imsi = tm.getSimSerialNumber();
            if (imsi != null && !imsi.equals("")) {
                return imsi;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 获取Android_id
     *
     * @param context
     * @return
     */
    public static String getAndroid_id(Context context) {
        try {
            String android_id = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            if (android_id != null) {
                return android_id;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    /**
     * 返回当前程序版本名
     */
    public static int getAppVersionCode(Context context) {
        int versionCode = 1;
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionCode = pi.versionCode;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionCode;
    }

}
