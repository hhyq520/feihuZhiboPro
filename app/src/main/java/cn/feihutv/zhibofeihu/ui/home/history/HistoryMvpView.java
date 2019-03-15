package cn.feihutv.zhibofeihu.ui.home.history;

import android.app.Activity;

import java.util.List;

import cn.feihutv.zhibofeihu.data.db.model.HistoryRecordBean;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomResponce;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : liwen.chen
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface HistoryMvpView extends MvpView {

    //定义view接口

    Activity getActivity();

    void getAllHistory(List<HistoryRecordBean> recordBeanList);

    void gotoRoom(LoadRoomResponce.LoadRoomData loadRoomData);
}
