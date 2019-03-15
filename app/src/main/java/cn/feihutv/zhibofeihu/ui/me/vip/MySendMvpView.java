package cn.feihutv.zhibofeihu.ui.me.vip;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.vip.GetVipReceiveLogResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.vip.GetVipSendLogResponse;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : liwen.chen
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface MySendMvpView extends MvpView {

    //定义view接口

    //定义view接口

    void showNoData();

    void showContent();

    void showErrorView();

    void getDatas(List<GetVipSendLogResponse.SendLogList> sendLogLists, int offset);

}
