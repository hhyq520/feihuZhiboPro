package cn.feihutv.zhibofeihu.ui.me.shop;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.me.LoadLiangSearchKeyResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LoadShopAccountIdListResponse;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : liwen.chen
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface BeautiMvpView extends MvpView {

    //定义view接口

    void getDatas(List<LoadShopAccountIdListResponse.LoadShopAccountIdListResponseData> loadShopAccountIdListResponseDatas);

    void showNotEnough();

    // 购买成功
    void buySucc(String accountId);

    /**
     * 获取虎币区间成功
      */

    void succLiangSearch(List<LoadLiangSearchKeyResponse.LoadLiangSearchKeyResponseData> loadLiangSearchKeyResponseDatas);
}
