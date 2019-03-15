package cn.feihutv.zhibofeihu.ui.mv.demand;

import cn.feihutv.zhibofeihu.data.network.http.model.mv.DeleteMVResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.DeleteNeedResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.EditNeedResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMyNeedListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMyNeedMVListResponse;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : sichu.chen
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface MyDemandPageMvpView extends MvpView {

    //定义view接口
    void onGetMyNeedListResp(GetMyNeedListResponse response);

    void onEditNeedResp(EditNeedResponse response);

    void onGetMyNeedMVListResp(GetMyNeedMVListResponse response);

    void onDeleteMVResp(DeleteMVResponse response);

    void onDeleteNeedResp(DeleteNeedResponse response);
}
