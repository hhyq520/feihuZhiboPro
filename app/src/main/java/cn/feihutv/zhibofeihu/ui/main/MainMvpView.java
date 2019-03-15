package cn.feihutv.zhibofeihu.ui.main;

import android.app.Activity;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadSignResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.SignResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.TestStartLiveResponce;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : sichu.chen
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface MainMvpView extends MvpView {

    //定义view接口
    void showCustomVersionDialog();//强制更新dialog

    void gotoRoom(LoadRoomResponce.LoadRoomData data);//跳转直播间

    void showSignDialog(int days);//签到dialog

    Activity getActivity();

    void notifyLive(TestStartLiveResponce responce);
}
