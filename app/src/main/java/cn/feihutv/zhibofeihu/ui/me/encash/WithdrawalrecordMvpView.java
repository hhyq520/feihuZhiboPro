package cn.feihutv.zhibofeihu.ui.me.encash;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.me.GetEncashListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetPayListResponse;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : liwen.chen
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface WithdrawalrecordMvpView extends MvpView {

    //定义view接口

    void showNodataView();

    void showErrorView();

    void showContent();

    void getDatas(List<GetEncashListResponse.GetEncashListResponseData> getEncashListResponseDatas);
}
