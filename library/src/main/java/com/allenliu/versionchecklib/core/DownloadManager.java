package com.allenliu.versionchecklib.core;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.allenliu.versionchecklib.BuildConfig;
import com.allenliu.versionchecklib.R;
import com.allenliu.versionchecklib.callback.DownloadListener;
import com.allenliu.versionchecklib.utils.ALog;
import com.allenliu.versionchecklib.utils.AppUtils;
import com.allenliu.versionchecklib.utils.DownloadUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.request.BaseRequest;

import java.io.File;

import okhttp3.Call;
import okhttp3.Response;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by allenliu on 2017/8/16.
 */

public class DownloadManager {
    private static int lastProgress = 0;
    private static DownloadListener listener;
    private static Context mContext;
    private static NotificationManager manager;
    private static NotificationCompat.Builder builder;
    private static VersionParams mVersionParams;
    private static Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    int progress = msg.arg2;
                    int totalSize = msg.arg1;
                    Log.e("pppppppp", totalSize + "/totalSize");
                    Log.e("aaaaaaaa", progress + "/progress");
                    listener.onCheckerDownloading((int) (((double) (progress * 1.00) / totalSize) * 100));
                    progress = (int) (((double) (progress * 1.00) / totalSize) * 100);
                    if (progress - lastProgress >= 5) {
                        lastProgress = progress;
                        Log.e("bbbbbbbbb", lastProgress + "/progress");
                        builder.setContentText(String.format(mContext.getString(R.string.versionchecklib_download_progress), lastProgress));
                        builder.setProgress(100, lastProgress, false);
                        manager.notify(0, builder.build());

//                    builder.setProgress(100, lastProgress, false);
                    }
                    break;
                case 1:
                    int status = msg.arg1;
                    String targetPath = msg.obj.toString();
                    if (status == 0 || status == 3) {
                        File file = new File(targetPath);
                        listener.onCheckerDownloadSuccess(file);
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        Uri uri;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            uri = VersionFileProvider.getUriForFile(mContext, mContext.getPackageName() + ".versionProvider", file);
                            ALog.e(mContext.getPackageName() + "");
                            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } else {
                            uri = Uri.fromFile(file);
                        }
                        ALog.e("APK download Success");
                        //设置intent的类型
                        i.setDataAndType(uri, "application/vnd.android.package-archive");
                        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, i, 0);
                        builder.setContentIntent(pendingIntent);
                        builder.setContentText(mContext.getString(R.string.versionchecklib_download_finish));
                        builder.setProgress(100, 100, false);
                        manager.notify(0, builder.build());
                        AppUtils.installApk(mContext, file);
                        if (mContext instanceof Activity) {
                            ((Activity) mContext).finish();
                        }
                    } else if (status != 2) {
                        Intent intent = new Intent(mContext, mVersionParams.getCustomDownloadActivityClass());
                        intent.putExtra("isRetry", true);
                        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
                        builder.setContentIntent(pendingIntent);
                        builder.setContentText(mContext.getString(R.string.versionchecklib_download_fail));
                        builder.setProgress(100, 0, false);
                        manager.notify(0, builder.build());
                        ALog.e("file download failed");
//                showFailDialog();
                        listener.onCheckerDownloadFail();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public static void downloadAPK(Context context, String url, VersionParams versionParams, DownloadListener mlistener) {
        if (url == null || url.isEmpty()) {
            return;
        }
        if (versionParams.isSilentDownload()) {
            silentDownloadAPK(context, url, versionParams, listener);
            return;
        }
        mContext = context;
        listener = mlistener;
        mVersionParams = versionParams;
        lastProgress = 0;
//        ApkBroadCastReceiver.downloadApkPath = versionParams.getDownloadAPKPath();
        if (!versionParams.isForceRedownload()) {
            //判断本地文件是否存在
            String downloadPath = versionParams.getDownloadAPKPath() + context.getString(R.string.versionchecklib_download_apkname, context.getPackageName());
            if (checkAPKIsExists(context, downloadPath)) {
                AppUtils.installApk(context, new File(downloadPath));
                if (context instanceof Activity)
                    ((Activity) context).finish();
                return;
            }
        }
        manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(context);
        Intent intent = new Intent(context, versionParams.getCustomDownloadActivityClass());
        intent.putExtra("isRetry", false);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentTitle(context.getString(R.string.app_name));
        builder.setTicker(context.getString(R.string.versionchecklib_downloading));
        DownloadUtils.getInstance().downloadAppAsync(context,
                url,
                versionParams.getDownloadAPKPath(),
                context.getString(R.string.versionchecklib_download_apkname, context.getPackageName() + BuildConfig.VERSION_NAME),
                "1", null, true, null, new com.allenliu.versionchecklib.utils.DownloadListener() {
                    @Override
                    public void onProgress(long totalSize, long progress, String url, String downloadId, Object object, String targetPath, String tempPath, String ext) {
                        Message message = handler.obtainMessage(0);
                        message.arg1 = (int) totalSize;
                        message.arg2 = (int) progress;
                        message.sendToTarget();
                    }

                    @Override
                    public void onFinish(int status, String url, String downloadId, Object object, String targetPath, String tempPath, String error, Throwable throwable, String ext) {
                        Message message = handler.obtainMessage(1);
                        message.arg1 = status;
                        message.obj = targetPath;
                        message.sendToTarget();
                    }
                });
    }

    private static void silentDownloadAPK(final Context context, String url, final VersionParams versionParams, final DownloadListener listener) {

        OkGo.get(url).execute(new FileCallback(versionParams.getDownloadAPKPath(), context.getString(R.string.versionchecklib_download_apkname, context.getPackageName())) {
            @Override
            public void onBefore(BaseRequest request) {
                super.onBefore(request);

            }

            @Override
            public void onSuccess(File file, Call call, Response response) {
                listener.onCheckerDownloadSuccess(file);

            }

            @Override
            public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                super.downloadProgress(currentSize, totalSize, progress, networkSpeed);
                ALog.e("silent downloadProgress:" + progress + "");
                int currentProgress = (int) (progress * 100);
//                showLoadingDialog(currentProgress);
                if (currentProgress - lastProgress >= 5) {
                    lastProgress = currentProgress;
                }
                listener.onCheckerDownloading(currentProgress);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                ALog.e("file silent download failed");
//                showFailDialog();
                listener.onCheckerDownloadFail();
            }
        });
    }

    public static boolean checkAPKIsExists(Context context, String downloadPath) {
        File file = new File(downloadPath);
        boolean result = false;
        if (file.exists()) {
            try {
                PackageManager pm = context.getPackageManager();
                PackageInfo info = pm.getPackageArchiveInfo(downloadPath,
                        PackageManager.GET_ACTIVITIES);
                //判断安装包存在并且包名一样并且版本号不一样
                ALog.e("本地安装包版本号：" + info.versionCode + "\n 当前app版本号：" + context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode);
                if (info != null && context.getPackageName().equals(info.packageName) && context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode != info.versionCode) {
                    result = true;
                }
            } catch (Exception e) {
                result = false;
            }
        }
        return result;

    }
}
