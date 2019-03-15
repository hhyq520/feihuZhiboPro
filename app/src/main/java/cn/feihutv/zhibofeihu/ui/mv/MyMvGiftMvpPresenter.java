package cn.feihutv.zhibofeihu.ui.mv;


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
public interface MyMvGiftMvpPresenter<V extends MyMvGiftMvpView> extends MvpPresenter<V> {

    //定义业务处理方法

   // mv收礼记录
   void   getMVGiftLog(String mvId, String offset, String count);


}
