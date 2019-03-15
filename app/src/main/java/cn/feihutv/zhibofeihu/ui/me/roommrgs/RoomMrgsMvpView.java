package cn.feihutv.zhibofeihu.ui.me.roommrgs;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.GetRoomMrgsResponce;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : liwen.chen
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface RoomMrgsMvpView extends MvpView {

    //定义view接口

    void showContent();

    void showNodataView();

    void showErrorView();

    void getDatas(List<GetRoomMrgsResponce.RoomMrgData> roomMrgDataList);

    void showCancelSucc(int position);

}
