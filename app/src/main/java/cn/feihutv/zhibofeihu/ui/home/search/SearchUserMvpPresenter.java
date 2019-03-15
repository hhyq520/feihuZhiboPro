package cn.feihutv.zhibofeihu.ui.home.search;


import cn.feihutv.zhibofeihu.data.db.model.SearchHistoryInfo;
import cn.feihutv.zhibofeihu.di.PerActivity;
import cn.feihutv.zhibofeihu.ui.base.MvpPresenter;


/**
 * <pre>
 *     author : liwen.chen
 *     time   :
 *     desc   : 定义P操作
 *     version: 1.0
 * </pre>
 */
@PerActivity
public interface SearchUserMvpPresenter<V extends SearchUserMvpView> extends MvpPresenter<V> {

    //定义业务处理方法

    void search(String accountId);

    void follow(String userId, int position);

    void loadRecommed(boolean isFromSearch);

    void querySearch();

    void deleteSearchInfoByKey(String accountId);

    void deleteAllSearchInfo();

    void saveSearchInfo(SearchHistoryInfo searchHistoryInfo);

}
