package cn.feihutv.zhibofeihu.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import cn.feihutv.zhibofeihu.FeihuZhiboApplication;

/**
 * Created by souhu_vine on 2017/3/1.
 */

public class FHUtils {

    /**
     * 方法用途: 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序），并且生成url参数串<br>
     * 实现步骤: <br>
     * @param paraMap    要排序的Map对象
     * @param urlEncode  是否需要URLENCODE
     * @param needSort  是否对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
     * @param connector 参数与参数之间的连接符
     * @return
     */
    public static String formatUrlMap(Map<String, String> paraMap, boolean urlEncode, boolean needSort, String connector) {
        String buff = "";
        Map<String, String> tmpMap = paraMap;
        try {
            List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(tmpMap.entrySet());

            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            if(needSort) {
                Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {

                    @Override
                    public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                        return (o1.getKey()).toString().compareTo(o2.getKey());
                    }
                });
            }
            // 构造URL 键值对的格式
            StringBuilder buf = new StringBuilder();
            for (Map.Entry<String, String> item : infoIds) {
                if (!StringUtil.isEmpty(item.getKey())) {
                    String key = item.getKey();
                    String val = item.getValue();
                    if (urlEncode) {
                        val = URLEncoder.encode(val, "utf-8");
                    }
                    buf.append(key + "=" + val);
                    buf.append(connector);
                }

            }
            buff = buf.toString();
            if (buff.isEmpty() == false) {
                buff = buff.substring(0, buff.length() - connector.length());
            }
        } catch (Exception e) {
            return null;
        }
        return buff;
    }

    public static String getMD5(String sSecret) {
        try {
            MessageDigest bmd5 = MessageDigest.getInstance("MD5");
            bmd5.update(sSecret.getBytes());
            int i;
            StringBuffer buf = new StringBuffer();
            byte[] b = bmd5.digest();
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String getString(byte[] b) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            sb.append(b[i]);
        }
        return sb.toString();
    }

    //getTime方法返回的就是10位的时间戳
    public static String getTime() {
        long time = System.currentTimeMillis() / 1000;//获取系统时间的10位的时间戳
        String str = String.valueOf(time);
        return str;
    }
    public static long getLongTime() {
        long time = System.currentTimeMillis() / 1000;//获取系统时间的10位的时间戳
        return time;
    }

    public static String intToF(long value) {
        String sValue="";
        if (value >= 100000000) {
            sValue = new BigDecimal((double) value / 100000000).setScale(2, BigDecimal.ROUND_DOWN) + "亿";
        } else if (value >= 10000) {
            sValue = new BigDecimal((double) value / 10000).setScale(2, BigDecimal.ROUND_DOWN) + "万";
        } else {
            sValue = value + "";
        }
        return sValue;
    }

    public static String intToPeople(long value) {
        String sValue="";
        if (value >= 10000) {
            sValue = new BigDecimal((double) value / 10000).setScale(2, BigDecimal.ROUND_DOWN) + "人";
        } else {
            sValue = value + "";
        }
        return sValue;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * Is the live streaming still available
     * @return is the live streaming is available
     */
    public static boolean isLiveStreamingAvailable() {
        return true;
    }

    public static void showToast(String msg) {
        if (msg != null) {
            Toast.makeText(FeihuZhiboApplication.getApplication(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isAppInstalled(Context context, String packagename) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            //System.out.println("没有安装");
            return false;
        } else {
            //System.out.println("已经安装");
            return true;
        }
    }
}
