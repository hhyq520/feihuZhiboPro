package cn.feihutv.zhibofeihu.ui.live;

import cn.feihutv.zhibofeihu.data.network.socket.model.live.JoinRoomResponce;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : huang hao
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface PLVideoViewMvpView extends MvpView {

    //定义view接口
    void joinRoomRespoce(JoinRoomResponce responce);
}
