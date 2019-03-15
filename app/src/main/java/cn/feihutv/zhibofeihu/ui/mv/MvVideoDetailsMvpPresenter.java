package cn.feihutv.zhibofeihu.ui.mv;


import java.util.List;

import cn.feihutv.zhibofeihu.data.local.model.SysbagBean;
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
public interface MvVideoDetailsMvpPresenter<V extends MvVideoDetailsMvpView> extends MvpPresenter<V> {

    //定义业务处理方法




    // mv详情

    void getMVDetail(String mvId);


    // 获取mv评论列表
    void     getMVCommentList(String mvId, String offset, String count);



    //mv点赞
    void    likeMV(String mvId);


    // 对评论点赞
    void  likeComment(String commentId);


    //mv送礼
    void   giftMV(String mvId, String giftId, String giftCnt);




    //发表mv评论
    void     postMVComment(String mvId, String commentId, String content);



    void followUser(String uid);


   // 播放MV
   void  playMV(String mvId);


    void requestBagData(String mvId);//请求背包数据


    void dealBagSendGift(List<SysbagBean> sysbagBeanList, int id, int count);
    void dealSendGift(List<SysbagBean> sysbagBeanList,int id,  int count);


    // 分享
    void shareMv(String mvId, int to);


    void getDownMVUrl( String mvId);

    void getMVUrl( String videoId);

}
