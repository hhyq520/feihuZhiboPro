package cn.feihutv.zhibofeihu.ui.home.search;

import android.app.Activity;

import java.util.List;

import cn.feihutv.zhibofeihu.data.db.model.SearchHistoryInfo;
import cn.feihutv.zhibofeihu.data.network.http.model.home.RecommendResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.home.SearchResponse;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : liwen.chen
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface SearchUserMvpView extends MvpView {

    //定义view接口

    // 显示加载错误
    void showErrorView();

    // 显示有内容返回
    void showContent(boolean isFromSearch);

    /**
     * 搜索无数据
     */
    void showNoDatas();

    /**
     * 搜索有数据
     *
     * @param searchResponseDatas
     */
    void getDatas(List<SearchResponse.SearchResponseData> searchResponseDatas);

    /**
     * 关注成功
     */
    void followSuccess(int position);

    /**
     * 获得推荐列表
     *
     * @param responseDatas
     */
    void getRecommedDatas(List<RecommendResponse.RecommendResponseData> responseDatas);

    /**
     * 获取搜索历史记录
     *
     * @param historyInfo
     */
    void getSearchInfo(List<SearchHistoryInfo> historyInfo);

    /**
     * 没有推荐的主播
     */
    void showNoRemmendHost();

    Activity getActivity();

}
