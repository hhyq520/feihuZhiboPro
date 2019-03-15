package cn.feihutv.zhibofeihu.ui.bangdan.luck;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.bangdan.LoadLuckRecordListResponse;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : liwen.chen
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface DayListMvpView extends MvpView {

    //定义view接口

    void getDatas(LoadLuckRecordListResponse.LoadLuckRecordListResponseData recordListResponseData);

    void showErrorView();
}
