package cn.feihutv.zhibofeihu.di.component;


import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.data.DataManager;
import cn.feihutv.zhibofeihu.di.ApplicationContext;
import cn.feihutv.zhibofeihu.di.module.ApplicationModule;
import dagger.Component;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/09/25
 *     desc   : 依赖注入 app 基本依赖：定义需要注入的业务类，具体实现
 *     version: 1.0
 * </pre>
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(FeihuZhiboApplication app);

//    void inject(SyncService service);

    @ApplicationContext
    Context context();

    Application application();

    DataManager getDataManager();
}