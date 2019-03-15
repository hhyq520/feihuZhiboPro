package cn.feihutv.zhibofeihu.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.text.TextUtils;


import com.tencent.cos.COSClient;
import com.tencent.cos.COSClientConfig;
import com.tencent.cos.common.COSEndPoint;
import com.tencent.cos.model.COSRequest;
import com.tencent.cos.model.COSResult;
import com.tencent.cos.model.GetObjectMetadataRequest;
import com.tencent.cos.model.GetObjectMetadataResult;
import com.tencent.cos.model.PutObjectRequest;
import com.tencent.cos.model.PutObjectResult;
import com.tencent.cos.task.listener.ICmdTaskListener;
import com.tencent.cos.task.listener.IUploadTaskListener;

import java.lang.reflect.Method;

import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.ui.live.PLVideoViewActivity;
import cn.feihutv.zhibofeihu.ui.live.pclive.LivePlayInPCActivity;

/**

 * @author jingle1267@163.com
 */
public final class AppUtils {

    private static final boolean DEBUG = true;
    private static final String TAG = "AppUtils";

    public static int activityCount = 0;


    /**
     * Don't let anyone instantiate this class.
     */
    private AppUtils() {
        throw new Error("Do not need instantiate!");
    }


    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasNavigationBar;
    }

    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime = 0;

    public static void startLiveActivity(Context context, String roomId, String headUrl, int broadcastType, boolean isShowWindow) {
        if(TextUtils.isEmpty(roomId)){
            return;
        }
        if (System.currentTimeMillis() - lastClickTime < MIN_CLICK_DELAY_TIME) {
            return;
        }
        lastClickTime = System.currentTimeMillis();
        Intent intent = null;
        if (broadcastType == 1) {// 1为PC   2为手机
            intent = new Intent(context, LivePlayInPCActivity.class);
        } else {
            intent = new Intent(context, PLVideoViewActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("room_id", roomId);
        intent.putExtra("showWindow", isShowWindow);
        intent.putExtra("headUrl", headUrl);
        context.startActivity(intent);
    }

    public static void startLiveActivity(Context context, String roomId, String headUrl, int broadcastType, boolean isShowWindow, boolean isNeedGuard) {
        if(TextUtils.isEmpty(roomId)){
            return;
        }
        if (System.currentTimeMillis() - lastClickTime < MIN_CLICK_DELAY_TIME) {
            return;
        }
        lastClickTime = System.currentTimeMillis();
        Intent intent = null;
        if (broadcastType == 1) {// 1为PC   2为手机
            intent = new Intent(context, LivePlayInPCActivity.class);
        } else {
            intent = new Intent(context, PLVideoViewActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("room_id", roomId);
        intent.putExtra("showWindow", isShowWindow);
        intent.putExtra("headUrl", headUrl);
        intent.putExtra("isNeedGuard", isNeedGuard);
        context.startActivity(intent);
    }

    public static void startLiveActivity(Activity activity, String roomId, String headUrl, int broadcastType, boolean isShowWindow, int requestCode) {
        if(TextUtils.isEmpty(roomId)){
            return;
        }
        if (System.currentTimeMillis() - lastClickTime < MIN_CLICK_DELAY_TIME) {
            return;
        }
        lastClickTime = System.currentTimeMillis();
        Intent intent = null;
        if (broadcastType == 1) {// 1为PC   2为手机
            intent = new Intent(activity, LivePlayInPCActivity.class);
        } else {
            intent = new Intent(activity, PLVideoViewActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("room_id", roomId);
        intent.putExtra("showWindow", isShowWindow);
        intent.putExtra("headUrl", headUrl);
        if (requestCode >= 0) {
            activity.startActivityForResult(intent, requestCode);
        } else {
            activity.startActivity(intent);
        }
    }

    public static void startLiveActivity(Fragment fragment, String roomId, String headUrl, int broadcastType, boolean isShowWindow, int requestCode) {
        if(TextUtils.isEmpty(roomId)){
            return;
        }
        if (System.currentTimeMillis() - lastClickTime < MIN_CLICK_DELAY_TIME) {
            return;
        }
        lastClickTime = System.currentTimeMillis();
        Intent intent = null;
        if (broadcastType == 1) {// 1为PC   2为手机
            intent = new Intent(fragment.getContext(), LivePlayInPCActivity.class);
        } else {
            intent = new Intent(fragment.getContext(), PLVideoViewActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("room_id", roomId);
        intent.putExtra("showWindow", isShowWindow);
        intent.putExtra("headUrl", headUrl);
        if (requestCode >= 0) {
            fragment.startActivityForResult(intent, requestCode);
        } else {
            fragment.startActivity(intent);
        }
    }
    public static void getAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(localIntent);
    }
    public static boolean judegeShowWanfa(){
        boolean isShow=false;
        int openPlayGame= SharePreferenceUtil.getSessionInt(FeihuZhiboApplication.getApplication(), "openPlayGame", 0);
        int openPlayLimitLevel=SharePreferenceUtil.getSessionInt(FeihuZhiboApplication.getApplication(), "openPlayLimitLevel", 1);
        if(openPlayGame==1){
            if(openPlayLimitLevel==1){
                if(SharePreferenceUtil.getSessionInt(FeihuZhiboApplication.getApplication(),"PREF_KEY_LEVEL",0)<6){
                   isShow=false;
                }else{
                    isShow=true;
                }
            }else{
                isShow=true;
            }
        }else{
           isShow=false;
        }
        return isShow;
    }

    /**
     * 上传图片
     *
     * @param path 图片的路径
     * @param sign 服务器返回的
     */
    public static void doUploadCover(Context context, final String path, String sign, String cosPath, IUploadTaskListener listener) {

        String bucket = "feihuzhibo";
        String srcPath = path;

        PutObjectRequest putObjectRequest = new PutObjectRequest();
        putObjectRequest.setBucket(bucket);
        putObjectRequest.setCosPath(cosPath);
        putObjectRequest.setSrcPath(srcPath);
        putObjectRequest.setSign(sign);
        putObjectRequest.setListener(listener);
        //创建COSClientConfig对象，根据需要修改默认的配置参数
        COSClientConfig config = new COSClientConfig();
        //设置园区
        config.setEndPoint(COSEndPoint.COS_GZ);
        config.setHttpProtocol("https://");
        COSClient cos = new COSClient(context, AppConstants.COS_APPID, config, "headloudphoto");
        PutObjectResult putObjectResult = cos.putObject(putObjectRequest);
    }

    public static void getFileIsExit(Context context,String sign,String cosPath,ICmdTaskListener listener){
        String bucket = "feihuzhibo";
        GetObjectMetadataRequest getObjectMetadataRequest = new GetObjectMetadataRequest();
        getObjectMetadataRequest.setBucket(bucket);
        getObjectMetadataRequest.setCosPath(cosPath);
        getObjectMetadataRequest.setSign(sign);
        getObjectMetadataRequest.setListener(listener);
        COSClientConfig config = new COSClientConfig();
        //设置园区
        config.setEndPoint(COSEndPoint.COS_GZ);
        config.setHttpProtocol("https://");
        COSClient cos = new COSClient(context, AppConstants.COS_APPID, config, "headloudphoto");
        GetObjectMetadataResult result = cos.getObjectMetadata(getObjectMetadataRequest);
    }
}
