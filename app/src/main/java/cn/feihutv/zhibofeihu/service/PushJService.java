package cn.feihutv.zhibofeihu.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import cn.feihutv.zhibofeihu.activitys.SplashActiity;
import cn.feihutv.zhibofeihu.ui.main.MainActivity;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by huanghao on 2017/7/7.
 */

public class PushJService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent!=null) {
            Bundle bundle = intent.getExtras();
            if(bundle!=null) {
                String url = bundle.getString(JPushInterface.EXTRA_EXTRA);
                if (!TextUtils.isEmpty(url)) {
                    if(MainActivity.MainActivityself!=null){
                        MainActivity.MainActivityself.finish();
                    }
                    Intent i = new Intent(context,SplashActiity.class);
                    i.putExtra("url",url);
                    AppLogger.d(url);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i );
                }
            }
        }
    }
}
