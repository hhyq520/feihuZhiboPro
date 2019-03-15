package cn.feihutv.zhibofeihu.rxbus.bangdan;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/10/26 16:55
 *      desc   : 向子页面发送当前信息
 *      version: 1.0
 * </pre>
 */

public class LuckPush {

    private int currentPosition;

    private int rankType;

    public LuckPush(int currentPosition, int rankType) {
        this.currentPosition = currentPosition;
        this.rankType = rankType;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public int getRankType() {
        return rankType;
    }

    public void setRankType(int rankType) {
        this.rankType = rankType;
    }
}
