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
public interface MyMvCollectMvpPresenter<V extends MyMvCollectMvpView> extends MvpPresenter<V> {

    //定义业务处理方法


    //收藏列表
     void  getNeedCollectList(String offset, String count);


   // 取消收藏需求

        void    unCollectNeed(String needId);

    void enablePostMV(String id);

}
