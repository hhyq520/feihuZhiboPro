package cn.feihutv.zhibofeihu.ui.me.shop;

import java.util.List;

import cn.feihutv.zhibofeihu.data.db.model.SysGoodsNewBean;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : liwen.chen
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface CarMvpView extends MvpView {

    //定义view接口

    // 成功查询座驾
    void getZjSucc(List<SysGoodsNewBean> sysGoodsNewBeen);

    void setMountSucc();

    void buyGoodsSucc();

    void hbNotEnough();

    void showNotVip();

}
