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
public interface PostMvVideoMvpPresenter<V extends PostMvVideoMvpView> extends MvpPresenter<V> {

    //定义业务处理方法
   void getCncUploadToken(String originFileName,String fileMd5,String originFileSize);



    void getCosSign(int type, String ext,String path);



    void postMV(String title, String cover, String videoId, String needId);


    //定义业务处理方法
    void getAllNeedList(String forPostMV, String forMe, String offset, String count);

    void enablePostMV(String id);



}
