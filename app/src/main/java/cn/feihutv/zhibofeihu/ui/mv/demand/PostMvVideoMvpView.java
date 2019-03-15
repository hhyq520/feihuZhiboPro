package cn.feihutv.zhibofeihu.ui.mv.demand;

import cn.feihutv.zhibofeihu.data.network.http.model.me.GetCosSignResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.CncGetUploadTokenResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.EnablePostMVResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetAllNeedListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.PostMvResponse;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : sichu.chen
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface PostMvVideoMvpView extends MvpView {
    void onGetCncUploadTokenResp(CncGetUploadTokenResponse response1);

    void notifyCosSignResponce(GetCosSignResponse response, String path);

    void onPostMvResp(PostMvResponse response);

    void onGetAllNeedList(GetAllNeedListResponse response);

    void onEnablePostMVResp(EnablePostMVResponse response);


    //定义view接口

}
