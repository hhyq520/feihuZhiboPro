package cn.feihutv.zhibofeihu.ui.me.dynamic;

import android.app.Activity;

import cn.feihutv.zhibofeihu.data.network.socket.model.live.LoadOtherUserInfoResponce;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : sichu.chen
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface OthersCommunityMvpView extends MvpView {

    //定义view接口

    void getDatas(LoadOtherUserInfoResponce.OtherUserInfo otherUserInfo);

    Activity getActivity();

    // 关注成功
    void followSucc();

    // 取消关注成功
    void unFollow();

    // 添加黑名单成功
    void blockSucc();

    // 移除黑名单成功
    void unBlockSucc();

}
