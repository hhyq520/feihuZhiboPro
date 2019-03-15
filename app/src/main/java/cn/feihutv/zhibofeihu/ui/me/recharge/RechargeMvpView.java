package cn.feihutv.zhibofeihu.ui.me.recharge;

import java.util.List;

import cn.feihutv.zhibofeihu.data.db.model.SysHBBean;
import cn.feihutv.zhibofeihu.data.network.http.model.me.PayResponse;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : huang hao
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface RechargeMvpView extends MvpView {

    //定义view接口

    void getSysHb(List<SysHBBean> sysHBBean);

    void paySucc(String pf, String strPay);

}
