package cn.feihutv.zhibofeihu.ui.me.vip;

import java.util.List;

import cn.feihutv.zhibofeihu.data.db.model.SysVipBean;
import cn.feihutv.zhibofeihu.data.db.model.SysVipGoodsBean;
import cn.feihutv.zhibofeihu.data.network.http.model.vip.BuyVipResponse;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : liwen.chen
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface MyVipMvpView extends MvpView {

    //定义view接口

    void getSysVipGoodsSucc(List<SysVipGoodsBean> sysVipGoodsBeanList);

    void getSysVipSucc(List<SysVipBean> sysVipBeanList);

    // 购买vip成功
    void buyVipSucc(BuyVipResponse.BuyVipData vipData, int goodsId);

    // 购买失败， 虎币不足
    void failToBuy();

}
