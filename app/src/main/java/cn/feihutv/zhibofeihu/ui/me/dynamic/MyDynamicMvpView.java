package cn.feihutv.zhibofeihu.ui.me.dynamic;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.me.DynamicItem;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : liwen.chen
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface MyDynamicMvpView extends MvpView {

    //定义view接口

    void getDatas(List<DynamicItem> dynamicItems);

    void getDatasError();
}
