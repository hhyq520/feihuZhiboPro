package cn.feihutv.zhibofeihu.ui.me.recharge;

import java.util.List;

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
public interface PrepaidrecordsMvpView extends MvpView {

    //定义view接口

    void showNodataView();

    void showErrorView();

    void showContent();

    void getDatas(List<GetPayListResponse.GetPayListResponseData> payListResponseDatas);

}
