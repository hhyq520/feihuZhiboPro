package cn.feihutv.zhibofeihu.ui.widget.DanmuBase;

import cn.feihutv.zhibofeihu.data.local.model.TCChatEntity;

/**
 * Created by walkingMen on 2016/5/12.
 * 弹幕动作类
 */
public interface DanmakuLandActionInter {
    /**
     * 添加弹幕
     */
    void addDanmu(TCChatEntity push);

    /**
     * 移出弹幕
     */
    void pollDanmu();
}
