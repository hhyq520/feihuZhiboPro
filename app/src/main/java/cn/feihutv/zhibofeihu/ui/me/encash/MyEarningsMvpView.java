package cn.feihutv.zhibofeihu.ui.me.encash;

import cn.feihutv.zhibofeihu.data.network.http.model.me.GetEncashInfoResponse;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : liwen.chen
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface MyEarningsMvpView extends MvpView {

    //定义view接口

    void getEncashInfo(GetEncashInfoResponse.GetEncashInfoResponseData encashInfoResponseData);

}
