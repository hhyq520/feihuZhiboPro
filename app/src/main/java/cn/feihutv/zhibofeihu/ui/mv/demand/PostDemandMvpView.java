package cn.feihutv.zhibofeihu.ui.mv.demand;

import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetNeedSysHBResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.PostNeedResponse;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : sichu.chen
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface PostDemandMvpView extends MvpView {
    void onGetNeedSysHBResp(GetNeedSysHBResponse response);

    void onPostNeedDataResp(PostNeedResponse response);

    //定义view接口

}
