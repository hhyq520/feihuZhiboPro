package cn.feihutv.zhibofeihu.ui.mv;

import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMVGiftLogResponse;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : sichu.chen
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface MyMvGiftMvpView extends MvpView {
    void onGetMVGiftLogResp(GetMVGiftLogResponse response);

    //定义view接口

}
