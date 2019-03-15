package cn.feihutv.zhibofeihu.ui.me.setting;

import android.app.Activity;
import java.util.List;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetBlacklistResponse;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : liwen.chen
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface BlacklistMvpView extends MvpView {

    //定义view接口

    void showNoData();

    void showContent();

    void showErrorView();

    void removeSucc(int position);

    void getDatas(List<GetBlacklistResponse.GetBlacklistResponseData> responseData);

    Activity getActivity();

}
