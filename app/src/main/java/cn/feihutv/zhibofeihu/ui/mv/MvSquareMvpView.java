package cn.feihutv.zhibofeihu.ui.mv;

import cn.feihutv.zhibofeihu.data.network.http.model.mv.CancelMVNoticeResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.QueryMVNoticeResponse;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : sichu.chen
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface MvSquareMvpView extends MvpView {


    //定义view接口
    void onQueryMVNoticeResp(QueryMVNoticeResponse response);

    void onCancelMVNoticeResp(CancelMVNoticeResponse response);
}
