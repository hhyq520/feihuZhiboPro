package cn.feihutv.zhibofeihu.ui.me.dynamic;

import cn.feihutv.zhibofeihu.data.network.http.model.me.GetCosSignResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.PostFeedResponse;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : sichu.chen
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface SendDynamicMvpView extends MvpView {

    //定义view接口
    void  onGetLocationResp(String location);

    void notifyCosSignResponce(GetCosSignResponse response, String path,String oldPath);

    void onPostFeedResp(PostFeedResponse postFeedResponse);
}
