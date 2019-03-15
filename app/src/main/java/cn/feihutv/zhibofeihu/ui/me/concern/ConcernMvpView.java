package cn.feihutv.zhibofeihu.ui.me.concern;

import android.app.Activity;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.me.FollowsResponse;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : liwen.chen
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface ConcernMvpView extends MvpView {

    //定义view接口

    void showNoData();

    void showContent();

    void showErrorView();

    void getDatas(List<FollowsResponse.FollowsResponseData> responseData);

    Activity getActivity();

}
