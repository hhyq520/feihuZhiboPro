package cn.feihutv.zhibofeihu.ui.me.dynamic;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomContriResponce;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : liwen.chen
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface OtherPageMvpView extends MvpView {

    //定义view接口

    void getContriDatas(List<LoadRoomContriResponce.RoomContriData> roomContriDataList);

}
