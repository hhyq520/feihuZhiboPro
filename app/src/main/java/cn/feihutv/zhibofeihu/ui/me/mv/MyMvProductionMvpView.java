package cn.feihutv.zhibofeihu.ui.me.mv;

import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMyMVListResponse;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : sichu.chen
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface MyMvProductionMvpView extends MvpView {


    void GetMyMVListResp(GetMyMVListResponse response);

    //定义view接口

}
