package cn.feihutv.zhibofeihu.di.component;


import cn.feihutv.zhibofeihu.di.PerService;
import cn.feihutv.zhibofeihu.di.module.ServiceModule;
import dagger.Component;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/09/25
 *     desc   : 依赖注入 Service 基本依赖：定义需要注入的业务类，具体实现
 *     version: 1.0
 * </pre>
 */

@PerService
@Component(dependencies = ApplicationComponent.class, modules = ServiceModule.class)
public interface ServiceComponent {

//    void inject(SyncService service);

}
