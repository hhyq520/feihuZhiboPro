package cn.feihutv.zhibofeihu.di.module;


import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import cn.feihutv.zhibofeihu.BuildConfig;
import cn.feihutv.zhibofeihu.data.AppDataManager;
import cn.feihutv.zhibofeihu.data.DataManager;
import cn.feihutv.zhibofeihu.data.db.AppDbHelper;
import cn.feihutv.zhibofeihu.data.db.DbHelper;
import cn.feihutv.zhibofeihu.data.network.http.ApiHeader;
import cn.feihutv.zhibofeihu.data.network.http.ApiHelper;
import cn.feihutv.zhibofeihu.data.network.http.AppApiHelper;
import cn.feihutv.zhibofeihu.data.network.socket.AppSocketApiHelper;
import cn.feihutv.zhibofeihu.data.network.socket.SocketApiHeader;
import cn.feihutv.zhibofeihu.data.network.socket.SocketApiHelper;
import cn.feihutv.zhibofeihu.data.prefs.AppPreferencesHelper;
import cn.feihutv.zhibofeihu.data.prefs.PreferencesHelper;
import cn.feihutv.zhibofeihu.di.ApiInfo;
import cn.feihutv.zhibofeihu.di.ApplicationContext;
import cn.feihutv.zhibofeihu.di.DatabaseInfo;
import cn.feihutv.zhibofeihu.di.PreferenceInfo;
import cn.feihutv.zhibofeihu.utils.AppConstants;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/09/25
 *     desc   : dagger2 注入 应用公共方法注入实现 相当于 new 某个对象实现
 *     version: 1.0
 * </pre>
 */

@Module
public class ApplicationModule {

    private final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @DatabaseInfo
    String provideDatabaseName() {
        return AppConstants.DB_NAME;
    }

    @Provides
    @ApiInfo
    String provideMarket() {
        return BuildConfig.market_value;
    }

    @Provides
    @PreferenceInfo
    String providePreferenceName() {
        return AppConstants.PREF_NAME;
    }

    @Provides
    @Singleton
    DataManager provideDataManager(AppDataManager appDataManager) {
        return appDataManager;
    }

    @Provides
    @Singleton
    DbHelper provideDbHelper(AppDbHelper appDbHelper) {
        return appDbHelper;
    }

    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper(AppPreferencesHelper appPreferencesHelper) {
        return appPreferencesHelper;
    }

    @Provides
    @Singleton
    ApiHelper provideApiHelper(AppApiHelper appApiHelper) {
        return appApiHelper;
    }


    @Provides
    @Singleton
    SocketApiHelper provideSocketApiHelper(AppSocketApiHelper socketApiHelper) {
        return socketApiHelper;
    }


    @Provides
    @Singleton
    ApiHeader.ProtectedApiHeader provideProtectedApiHeader(@ApiInfo String market,
                                                           PreferencesHelper preferencesHelper) {

        String device = String.valueOf(3);
        long t = FHUtils.getLongTime() + preferencesHelper.getTimeChaZhi();
        String uid = preferencesHelper.getCurrentUserId();
        String apiKey = preferencesHelper.getApiKey();
        String info=preferencesHelper.getDeviceId()+"";
        return new ApiHeader.ProtectedApiHeader(device,
                market, String.valueOf(t), uid, apiKey,preferencesHelper,info);
    }




    @Provides
    @Singleton
    SocketApiHeader.SocketProtectedApiHeader provideSocketProtectedApiHeader(PreferencesHelper preferencesHelper) {
        String market = BuildConfig.market_value;
        String device = String.valueOf(3);
        String info=preferencesHelper.getDeviceId()+"";
        return new SocketApiHeader.SocketProtectedApiHeader(device,market,info);
    }



}
