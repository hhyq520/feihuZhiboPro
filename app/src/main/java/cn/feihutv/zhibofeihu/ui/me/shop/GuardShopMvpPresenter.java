package cn.feihutv.zhibofeihu.ui.me.shop;


import cn.feihutv.zhibofeihu.di.PerActivity;
import cn.feihutv.zhibofeihu.ui.base.MvpPresenter;


/**
 * <pre>
 *     author : liwen.chen
 *     time   :
 *     desc   : 定义P操作
 *     version: 1.0
 * </pre>
 */
@PerActivity
public interface GuardShopMvpPresenter<V extends GuardShopMvpView> extends MvpPresenter<V> {

    //定义业务处理方法

    // 检索主播
    void checkGuardInfo(String accountId);


    // 立即开通
    void buyGuard(String uid, int goodsId, int huType);

    void getSysGuardGoodsBean();

}
