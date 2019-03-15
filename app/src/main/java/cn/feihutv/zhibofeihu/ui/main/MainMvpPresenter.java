package cn.feihutv.zhibofeihu.ui.main;


import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseResponse;
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
public interface MainMvpPresenter<V extends MainMvpView> extends MvpPresenter<V> {

    //定义业务处理方法
    void requestBagData();//请求背包数据

    void loadSignData();// 请求签到数据

    void downLoadGiftResource(); // 礼物资源更新

    void loadRoomById(String id);// 请求直播间信息

    void saveUserData(LoadUserDataBaseResponse.UserData userData);

    // 查看是否禁播
    void testStartLive();
}
