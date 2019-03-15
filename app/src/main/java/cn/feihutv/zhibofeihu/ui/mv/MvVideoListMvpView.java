package cn.feihutv.zhibofeihu.ui.mv;

import java.util.List;

import cn.feihutv.zhibofeihu.data.local.model.SysbagBean;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetAllMvListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GiftMVResponse;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : sichu.chen
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface MvVideoListMvpView extends MvpView {

    //定义view接口
    void onGetAllMVListResp(GetAllMvListResponse response);

    void showGiftDialog(List<SysbagBean> sysbagBeanList);

    void onGiftMVResp(GiftMVResponse response);


}
