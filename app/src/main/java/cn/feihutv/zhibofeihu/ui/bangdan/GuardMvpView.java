package cn.feihutv.zhibofeihu.ui.bangdan;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.bangdan.GetGuardRankResponse;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : liwen.chen
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface GuardMvpView extends MvpView {

    //定义view接口

    void showErrorView();

    void getDatas(List<GetGuardRankResponse.GetGuardRankResponseData> rankResponseDatas);

}
