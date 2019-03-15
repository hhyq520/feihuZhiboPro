package cn.feihutv.zhibofeihu.ui.live.pclive;

import java.util.List;

import cn.feihutv.zhibofeihu.data.local.model.SysbagBean;
import cn.feihutv.zhibofeihu.data.local.model.TCChatEntity;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.GetCurrMountResponce;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : huang hao
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface LiveChatFragmentMvpView extends MvpView {

    //定义view接口
    void gotoRoom(LoadRoomResponce.LoadRoomData data);
    void notifyChatMsg(TCChatEntity tcChatEntity);//刷新聊天列表
    void showGiftDialog(List<SysbagBean> sysbagBeanList);
    void sendGiftSuccess(List<SysbagBean> sysbagBeanList,int id ,int count);
    void notifyGetCurrMount(GetCurrMountResponce.CurrMountData data);//刷新当前座驾
}
