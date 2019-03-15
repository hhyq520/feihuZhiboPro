package cn.feihutv.zhibofeihu.ui.bangdan;

import android.view.View;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.bangdan.LoadContriRankListResponse;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : liwen.chen
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface RankingMvpView extends MvpView {

    //定义view接口

    void getDatas(List<LoadContriRankListResponse.LoadContriRankListResponseData> rankListResponseData);

    // 加载错误
    void showErrorView();

    // 没有数据
    void showNodataView();

    // 显示加载框
    void showProgressView();

    // 有内容
    void showContentView();
}
