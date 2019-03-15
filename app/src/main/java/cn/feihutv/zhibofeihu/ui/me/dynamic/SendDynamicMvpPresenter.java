package cn.feihutv.zhibofeihu.ui.me.dynamic;


import android.location.Location;

import cn.feihutv.zhibofeihu.data.network.http.model.me.PostFeedRequest;
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
public interface SendDynamicMvpPresenter<V extends SendDynamicMvpView> extends MvpPresenter<V> {

    //定义业务处理方法
   void  getLocation(Location location);

    void  postFeed(PostFeedRequest request);

    void getCosSign(int type, String ext,String path,String oldPath);

}
