package cn.feihutv.zhibofeihu.ui.widget.DanmuBase;


import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by walkingMen on 2016/5/12.
 */
public class DanmakuActionManager implements DanmakuActionInter {
    public List<DanmakuChannel> channels = new LinkedList<>();

    public Queue<DanmuModel> danEntities = new LinkedList<>();

    @Override
    public void addDanmu(DanmuModel dan) {
        danEntities.add(dan);
        looperDan();
    }

    @Override
    public void pollDanmu() {
        looperDan();
    }

    public void addChannel(DanmakuChannel channel) {
        channels.add(channel);
    }

    public DanmakuActionManager() {

    }

    public void looperDan() {
        for (int i = 0; i < channels.size(); i++) {
            if (!channels.get(i).isRunning && danEntities.size() > 0) {
                DanmuModel poll = danEntities.poll();
                channels.get(i).mStartAnimation(poll);
            }
        }
    }
}
