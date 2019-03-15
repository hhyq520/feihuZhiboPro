package cn.feihutv.zhibofeihu.ui.me.shop;


import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseResponse;
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
public interface CarMvpPresenter<V extends CarMvpView> extends MvpPresenter<V> {

    //定义业务处理方法

    // 购买商品
    void buyGoods(int id, int cnt);

    void getStoreZuojia();

    // 更新用户数据
    void saveUserDatas(LoadUserDataBaseResponse.UserData userData);

}
