package cn.feihutv.zhibofeihu.ui.me;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.me.GetUserGuardsResponse;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : liwen.chen
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface HisGuardMvpView extends MvpView {

    //定义view接口

    void showNoData();

    void showContent();

    void showErrorView();

    void getDatas(List<GetUserGuardsResponse.GetUserGuardsResponseData> getUserGuardsResponseDatas);

}
