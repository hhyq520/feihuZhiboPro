package cn.feihutv.zhibofeihu.ui.live;

import cn.feihutv.zhibofeihu.data.network.http.model.me.GetCosSignResponse;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : huang hao
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface ReportMvpView extends MvpView {

    //定义view接口
    void notifyCosSignResponce(GetCosSignResponse response,String path);
    void reportResponce();
}
