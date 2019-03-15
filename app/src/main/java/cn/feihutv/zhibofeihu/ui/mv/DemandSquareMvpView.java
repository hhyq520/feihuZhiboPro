package cn.feihutv.zhibofeihu.ui.mv;

import cn.feihutv.zhibofeihu.data.network.http.model.mv.CollectNeedResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.EnablePostMVResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetAllNeedListResponse;
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
public interface DemandSquareMvpView extends MvpView {
    void onGetAllNeedList(GetAllNeedListResponse response);

    void onUnCollectNeedResp(UnCollectNeedResponse response);

    void onCollectNeedResp(CollectNeedResponse response);

    void onEnablePostMVResp(EnablePostMVResponse response);

    //定义view接口

}
