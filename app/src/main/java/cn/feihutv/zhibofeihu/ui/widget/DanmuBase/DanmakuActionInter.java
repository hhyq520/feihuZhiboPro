package cn.feihutv.zhibofeihu.ui.widget.DanmuBase;

import cn.feihutv.zhibofeihu.data.network.socket.model.push.AllRoomMsgPush;

/**
 * Created by walkingMen on 2016/5/12.
 * 弹幕动作类
 */
public interface DanmakuActionInter {
    /**
     * 添加弹幕
     */
    void addDanmu(DanmuModel push);

    /**
     * 移出弹幕
     */
    void pollDanmu();
}
