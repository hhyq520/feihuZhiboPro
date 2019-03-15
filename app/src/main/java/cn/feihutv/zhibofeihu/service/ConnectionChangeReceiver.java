package cn.feihutv.zhibofeihu.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import cn.feihutv.zhibofeihu.data.network.socket.model.common.NetworkChangePush;
import cn.feihutv.zhibofeihu.rxbus.RxBus;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;

/**
 * Created by huanghao on 2017/10/30.
 * 发送 -1 网络不可用 1 切换到wifi  0 切换到 4G
 */

public class ConnectionChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        NetworkChangePush push=new NetworkChangePush();
        if (activeNetwork != null) {
            if (activeNetwork.isConnected()) {
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    push.setCode(1);
                    RxBus.get().send(RxBusCode.RX_BUS_CODE_NOTICE_NETWORK_CHANGE,push);
                } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    push.setCode(0);
                    RxBus.get().send(RxBusCode.RX_BUS_CODE_NOTICE_NETWORK_CHANGE,push);
                }
            }else{
                push.setCode(-1);
                RxBus.get().send(RxBusCode.RX_BUS_CODE_NOTICE_NETWORK_CHANGE,push);
            }
        } else {
            push.setCode(-1);
            RxBus.get().send(RxBusCode.RX_BUS_CODE_NOTICE_NETWORK_CHANGE,push);
        }
    }
}
