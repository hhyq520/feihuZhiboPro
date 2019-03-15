package cn.feihutv.zhibofeihu.ui.me.setting;

import cn.feihutv.zhibofeihu.data.network.http.model.me.GetMsgSwitchStatusResponse;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : liwen.chen
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface MsgReceSettingsMvpView extends MvpView {

    //定义view接口

    // 获取消息接收状态
    void getMsgSwitchStatus(GetMsgSwitchStatusResponse.GetMsgSwitchStatusResponseData msgSwitchStatusResponseData);

    void setMsgStatusSucc(int type);
}
