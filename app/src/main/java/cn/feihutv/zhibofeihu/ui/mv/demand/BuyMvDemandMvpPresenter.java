package cn.feihutv.zhibofeihu.ui.mv.demand;


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
public interface BuyMvDemandMvpPresenter<V extends BuyMvDemandMvpView> extends MvpPresenter<V> {

    //定义业务处理方法

    void  buyMv(String mvId);


    void feedbackMV(String mvId, String require);


    void   getCncPlayUrl(String videoId);


}
