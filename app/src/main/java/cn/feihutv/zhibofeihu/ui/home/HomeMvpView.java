package cn.feihutv.zhibofeihu.ui.home;

import java.util.List;

import cn.feihutv.zhibofeihu.data.db.model.SysLaunchTagBean;
import cn.feihutv.zhibofeihu.data.network.http.model.home.GetBannerResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.home.LoadRoomListResponse;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : liwen.chen
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface HomeMvpView extends MvpView {

    //定义view接口
    void loadLaunchTag(List<SysLaunchTagBean> sysLaunchTagBeen);
}
