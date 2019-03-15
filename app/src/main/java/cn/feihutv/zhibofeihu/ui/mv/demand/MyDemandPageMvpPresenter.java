package cn.feihutv.zhibofeihu.ui.mv.demand;


import cn.feihutv.zhibofeihu.data.network.http.model.mv.EditNeedRequest;
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
public interface MyDemandPageMvpPresenter<V extends MyDemandPageMvpView> extends MvpPresenter<V> {

    //定义业务处理方法
    void getMyNeedList(String offset, String count);


    void editNeed(EditNeedRequest request);


    void  getMyNeedMVList(String status, String offset, String count);


    void deleteMV(String mvId);

    void deleteNeed(String nid);

}
