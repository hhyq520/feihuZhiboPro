package cn.feihutv.zhibofeihu.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.cnc.mediaplayer.sdk.lib.auth.CNCMediaPlayerAuthentication;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import cn.feihutv.zhibofeihu.BuildConfig;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LogDeviceRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LogDeviceResponse;
import cn.feihutv.zhibofeihu.utils.AppConstants;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.DeviceInfoUtil;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import cn.jpush.android.api.JPushInterface;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;

//import com.mrgo.drpg.load.MrgoManager;


/**
 * Created by huanghao on 2017/8/5.
 */

public class InitAppService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler.sendEmptyMessageDelayed(0, 100);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            initApp();
        }
    };

    private void initApp() {

        Config.DEBUG = true;
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        config.setSinaAuthType(UMShareConfig.AUTH_TYPE_SSO);
        UMShareAPI.get(getApplicationContext()).setShareConfig(config);

        PlatformConfig.setWeixin(AppConstants.WX_APP_ID, AppConstants.WX_APP_SECRET);
        PlatformConfig.setQQZone(AppConstants.QQ_APP_ID, AppConstants.QQ_SECRET);
        PlatformConfig.setSinaWeibo(AppConstants.Weibo_APP_KEY, AppConstants.Weibo_APP_SECRET, AppConstants.REDIRECT_URL);

        JPushInterface.setDebugMode(true);
        JPushInterface.init(getApplicationContext());

        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                // x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);

        // 网宿SDK鉴权
        CNCMediaPlayerAuthentication.requestAuth(getApplicationContext(),"feihutv", "6E52FA7AF24644D3B14B6B1A9373B840", null);

        MobclickAgent.setDebugMode(false);

        String android_id = DeviceInfoUtil.getAndroid_id(this);
        String imsi = DeviceInfoUtil.getImsi(this);
        String android_imei = DeviceInfoUtil.getAndroid_IMEI(this);
        int systemApplication = DeviceInfoUtil.isSystemApplication(this) == true ? 1 : 0;
        String deviceId = DeviceInfoUtil.getDeviceId(this);
        String phoneModel = DeviceInfoUtil.getPhoneModel();
        String phoneBrand = DeviceInfoUtil.getPhoneBrand();
        int appVersionCode = DeviceInfoUtil.getAppVersionCode(this);
        String appVersionName = DeviceInfoUtil.getAppVersionName(this);
        String buildVersion = DeviceInfoUtil.getBuildVersion();
        int deviceId1 = SharePreferenceUtil.getSessionInt(this, "deviceId", -1);
        if (deviceId1 == -1) {
             new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                             .doLogDeviceApiCall(new LogDeviceRequest(
                                     BuildConfig.market_value, systemApplication+"", appVersionCode + "", appVersionName, android_imei, imsi, deviceId, android_id, phoneBrand, phoneModel, buildVersion))
                             .subscribeOn(Schedulers.io())
                             .observeOn(AndroidSchedulers.mainThread())
                             .subscribe(new Consumer<LogDeviceResponse>() {
                                 @Override
                                 public void accept(@NonNull LogDeviceResponse response) throws Exception {
                                     if(response.getCode()==0){
                                         SharePreferenceUtil.saveSeesionInt(InitAppService.this, "deviceId", response.getDeviceId());
                                     }else{
                                         AppLogger.e(response.getErrMsg());
                                     }
                                 }
                             }, new Consumer<Throwable>() {
                                 @Override
                                 public void accept(@NonNull Throwable throwable) throws Exception {

                                 }
                             })

                     );
        }
    }


}
