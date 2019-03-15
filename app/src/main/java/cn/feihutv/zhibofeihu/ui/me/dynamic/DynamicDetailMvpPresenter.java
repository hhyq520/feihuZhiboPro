package cn.feihutv.zhibofeihu.ui.me.dynamic;


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
public interface DynamicDetailMvpPresenter<V extends DynamicDetailMvpView> extends MvpPresenter<V> {

    //定义业务处理方法
    //获取动态详情
    void loadFeedDetail(String id);

    //加载评论列表
    void loadCommentList(String id, String last, int cnt);


    //动态点赞
    void likeFeed(String id);


    //提交分享日志
    void logShare(int from, int to);


    //分享
    void  shareFeed(String id);


    //删除动态
    void   deleteFeed(String id);



    //发表动态评论
    void postFeedComment(String id, String content, String reply);

}
