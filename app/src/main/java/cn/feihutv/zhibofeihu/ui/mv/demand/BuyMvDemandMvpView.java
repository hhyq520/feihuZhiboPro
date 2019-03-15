package cn.feihutv.zhibofeihu.ui.mv.demand;

import cn.feihutv.zhibofeihu.data.network.http.model.mv.BuyMVResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.FeedbackMVResponse;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : sichu.chen
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface BuyMvDemandMvpView extends MvpView {
    void onBuyMVResp(BuyMVResponse response);

    void onFeedbackMVResp(FeedbackMVResponse response);

    void onGetCncPlayUrlResp(String url);

    //定义view接口

}
