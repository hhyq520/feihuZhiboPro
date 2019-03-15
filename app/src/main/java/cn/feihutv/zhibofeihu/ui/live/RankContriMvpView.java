package cn.feihutv.zhibofeihu.ui.live;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomContriResponce;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : huang hao
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface RankContriMvpView extends MvpView {

    //定义view接口
    void notifyContriList(List<LoadRoomContriResponce.RoomContriData> datas,int type);//刷新在线贡献榜

    void freshStop();
}
