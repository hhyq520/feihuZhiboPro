package cn.feihutv.zhibofeihu.utils;
/**
 * Created by clw on 2017/02/28.
 */

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

public class UiUtil {
    private static int screenWidth = 0;
    private static int screenHeight = 0;
    private static float screenDensity = 0;
    private static int densityDpi = 0;
    private static int statusBarHeight = 0;


    public static void initialize(Context context) {
        if (context == null) {
            return;
        }
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;     // 屏幕宽度
        screenHeight = metrics.heightPixels;   // 屏幕高度
        screenDensity = metrics.density;      // 0.75 / 1.0 / 1.5 / 2.0 / 3.0
        densityDpi = metrics.densityDpi;  //120 160 240 320 480
    }

    public static int dip2px(float dipValue) {
        return (int) (dipValue * screenDensity + 0.5f);
    }

    public static int px2dip(float pxValue) {

        return (int) (pxValue / screenDensity + 0.5f);
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
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
