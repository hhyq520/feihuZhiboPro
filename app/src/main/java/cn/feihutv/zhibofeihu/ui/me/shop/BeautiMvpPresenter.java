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
public interface BeautiMvpPresenter<V extends BeautiMvpView> extends MvpPresenter<V> {

    //定义业务处理方法

    // 加载靓号列表
    void loadShopAccountIdList(int liangId);

    // 购买靓号
    void buyShopAccountId(String id);

    void saveaccountId(String accountId);

    // 搜索靓号价格区间
    void loadLiangSearchKey();


}
