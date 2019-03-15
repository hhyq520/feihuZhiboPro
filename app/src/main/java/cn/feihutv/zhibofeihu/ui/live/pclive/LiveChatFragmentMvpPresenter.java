package cn.feihutv.zhibofeihu.ui.live.pclive;

import java.util.List;

import cn.feihutv.zhibofeihu.data.db.model.SysConfigBean;
import cn.feihutv.zhibofeihu.data.db.model.SysGiftNewBean;
import cn.feihutv.zhibofeihu.data.db.model.SysMountNewBean;
import cn.feihutv.zhibofeihu.data.local.model.SysbagBean;
import cn.feihutv.zhibofeihu.data.local.model.TCChatEntity;
import cn.feihutv.zhibofeihu.di.PerActivity;
import cn.feihutv.zhibofeihu.ui.base.MvpPresenter;


/**
 * <pre>
 *     author : huang hao
 *     time   :
 *     desc   : 定义P操作
 *     version: 1.0
 * </pre>
 */
@PerActivity
public interface LiveChatFragmentMvpPresenter<V extends LiveChatFragmentMvpView> extends MvpPresenter<V> {

    //定义业务处理方法
    void loadRoomById(String userId);// 请求直播间信息
    void sendLoudspeaker(String msg);
    void sendRoomMsg(TCChatEntity tcChatEntity, String msg);//发送消息
    void requestBagData();//请求背包数据
    void dealBagSendGift(List<SysbagBean> sysbagBeanList, int id, int count);
    void dealSendGift(List<SysbagBean> sysbagBeanList,int id,  int count);
    List<SysGiftNewBean> getSysGiftNewBean();
    SysGiftNewBean getGiftBeanByID(String id);
    void getCurrMount(String roomId);//获取当前座驾
    SysMountNewBean getMountBeanByID(String id);

    SysConfigBean getSysConfigBean();
}
