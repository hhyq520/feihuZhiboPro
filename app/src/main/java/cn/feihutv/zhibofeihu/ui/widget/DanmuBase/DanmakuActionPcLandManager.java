package cn.feihutv.zhibofeihu.ui.widget.DanmuBase;


import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import cn.feihutv.zhibofeihu.data.local.model.TCChatEntity;
import cn.feihutv.zhibofeihu.ui.widget.DanmuBase.DanmakuActionInter;
import cn.feihutv.zhibofeihu.ui.widget.DanmuBase.DanmakuLandActionInter;
import cn.feihutv.zhibofeihu.ui.widget.DanmuBase.DanmakuLandChannel;
import cn.feihutv.zhibofeihu.ui.widget.DanmuBase.DanmakuPChannel;
import cn.feihutv.zhibofeihu.ui.widget.DanmuBase.DanmuModel;

/**
 * Created by walkingMen on 2016/5/12.
 */
public class DanmakuActionPcLandManager implements DanmakuLandActionInter {
    public List<DanmakuLandChannel> channels = new LinkedList<>();

    public Queue<TCChatEntity> danEntities = new LinkedList<>();

    @Override
    public void addDanmu(TCChatEntity dan) {
        danEntities.add(dan);
        looperDan();
    }

    @Override
    public void pollDanmu() {
        looperDan();
    }

    public void addChannel(DanmakuLandChannel channel) {
        channels.add(channel);
    }

    public DanmakuActionPcLandManager() {

    }

    public void looperDan() {
        for (int i = 0; i < channels.size(); i++) {
            if (!channels.get(i).isRunning && danEntities.size() > 0) {
                TCChatEntity poll = danEntities.poll();
                channels.get(i).mStartAnimation(poll);
            }
        }
    }
}
