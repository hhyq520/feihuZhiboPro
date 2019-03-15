package cn.feihutv.zhibofeihu.ui.mv.demand;

import cn.feihutv.zhibofeihu.data.network.http.model.mv.EnablePostMVResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetNeedCollectListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.UnCollectNeedResponse;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : sichu.chen
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface MyMvCollectMvpView extends MvpView {
    void onGetNeedCollectListResp(GetNeedCollectListResponse response);

    void onUnCollectNeedResp(UnCollectNeedResponse response);

    void onEnablePostMVResp(EnablePostMVResponse response);

    //定义view接口

}
