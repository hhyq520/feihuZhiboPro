package cn.feihutv.zhibofeihu.ui.mv.demand;


import cn.feihutv.zhibofeihu.data.network.http.model.mv.PostNeedRequest;
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
public interface PostDemandMvpPresenter<V extends PostDemandMvpView> extends MvpPresenter<V> {

    //定义业务处理方法
    void getNeedSysHB();

    void postNeedData(PostNeedRequest request);
}
