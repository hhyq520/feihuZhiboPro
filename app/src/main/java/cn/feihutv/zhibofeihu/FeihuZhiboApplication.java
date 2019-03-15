package cn.feihutv.zhibofeihu;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatDelegate;
import android.view.WindowManager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import cn.feihutv.zhibofeihu.data.DataManager;
import cn.feihutv.zhibofeihu.di.component.ApplicationComponent;
import cn.feihutv.zhibofeihu.di.component.DaggerApplicationComponent;
import cn.feihutv.zhibofeihu.di.module.ApplicationModule;
import cn.feihutv.zhibofeihu.service.InitAppService;
import cn.feihutv.zhibofeihu.ui.widget.font.RxTool;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.DynamicTimeFormat;
import cn.feihutv.zhibofeihu.utils.FileUtils;
import cn.feihutv.zhibofeihu.utils.rxdownload.CustomSqliteActor;
import okhttp3.OkHttpClient;
import zlc.season.rxdownload3.core.DownloadConfig;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/09/25
 *     desc   : app in
 *     version: 1.0
 * </pre>
 */
public class FeihuZhiboApplication extends Application {

    @Inject
    public DataManager mDataManager;//数据管理器

    private ApplicationComponent mApplicationComponent;

    private static FeihuZhiboApplication instance;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context).setTimeFormat(new DynamicTimeFormat("更新于 %s"));
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //实例化dagger
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this)).build();

        mApplicationComponent.inject(this);

        CrashReport.initCrashReport(getApplicationContext(), "d08fcd35e1", false);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        RxTool.init(this);
        //init log
        AppLogger.init();
        //net init
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                . writeTimeout(10, TimeUnit.SECONDS)
                .build();
        AndroidNetworking.initialize(getApplicationContext(),okHttpClient);
        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
        Intent intent = new Intent(this, InitAppService.class);
        startService(intent);
        //下载管理初始化参数
        executeDownLoadConfig();
    }

    private List<Activity> activityList = new LinkedList();

    //添加Activity到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    // 遍历所有Activity并finish
    public void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        System.exit(0);
    }

    public static FeihuZhiboApplication getApplication() {
        return instance;
    }


    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }


    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();

    public WindowManager.LayoutParams getMywmParams() {
        return wmParams;
    }




    private void executeDownLoadConfig(){

        DownloadConfig.Builder builder = DownloadConfig.Builder.Companion.create(this)
                .enableDb(true) //启用数据库
                .enableAutoStart(false)              //自动开始下载
                .setDbActor(new CustomSqliteActor(this))
                .enableService(true)
                .enableNotification(false)
                .setDefaultPath(FileUtils.getMvDownFileDir())
                ;

        DownloadConfig.INSTANCE.init(builder);
    }



}
