package cn.feihutv.zhibofeihu.ui.bangdan.luck;


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
public interface DayListMvpPresenter<V extends DayListMvpView> extends MvpPresenter<V> {

    //定义业务处理方法
    void loadLuckRecordList(String rankType, int offset, int count);

    LoadUserDataBaseResponse.UserData getUserDatas();
}
