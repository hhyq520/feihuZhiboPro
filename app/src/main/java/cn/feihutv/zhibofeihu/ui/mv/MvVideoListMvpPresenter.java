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
public interface MvVideoListMvpPresenter<V extends MvVideoListMvpView> extends MvpPresenter<V> {

    //定义业务处理方法

    void getAllMVList(String offset);


    void shareMv(String mvId, int to);

    void requestBagData(String mvId);

    void dealBagSendGift(List<SysbagBean> sysbagBeanList, int id, int count);

    void dealSendGift(List<SysbagBean> sysbagBeanList, int id, int count);

    //mv送礼
    void   giftMV(String mvId, String giftId, String giftCnt);


}
