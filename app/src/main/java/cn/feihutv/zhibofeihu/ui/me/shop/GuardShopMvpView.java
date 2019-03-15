package cn.feihutv.zhibofeihu.ui.me.shop;

import java.util.List;

import cn.feihutv.zhibofeihu.data.db.model.SysGuardGoodsBean;
import cn.feihutv.zhibofeihu.data.network.http.model.me.CheckGuardInfoResponse;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : liwen.chen
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface GuardShopMvpView extends MvpView {

    //定义view接口

    // 查询主播成功
    void checkGuardInfoSucc(CheckGuardInfoResponse.CheckGuardInfoResponseData checkGuardInfoResponseData);

    // 查询系统守护表成功
    void getSysGuardGoodsSucc(List<SysGuardGoodsBean> sysGuardGoodsBeen);

    // 显示虎币不足
    void showHbNotEnoughDialog();

    void showBuySuccDialog(int huType);
}
