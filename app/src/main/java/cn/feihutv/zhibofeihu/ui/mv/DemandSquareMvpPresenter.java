package cn.feihutv.zhibofeihu.ui.mv;


import cn.feihutv.zhibofeihu.data.db.model.SysConfigBean;
import cn.feihutv.zhibofeihu.di.PerActivity;
import cn.feihutv.zhibofeihu.ui.base.MvpPresenter;


/**
 * <pre>
 *     author : sichu.chen
 *     time   :
 *     desc   : 定义P操作
 *     version: 1.0
 * </pre>
 */
@PerActivity
public interface DemandSquareMvpPresenter<V extends DemandSquareMvpView> extends MvpPresenter<V> {

    //定义业务处理方法
    void getAllNeedList(String forPostMV, String forMe, String offset, String count);



    void unCollectNeed(String id);

    void collectNeed(String id);


    void enablePostMV(String id);

    SysConfigBean getSysConfig();

}
