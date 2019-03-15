package cn.feihutv.zhibofeihu.ui.me.dowm.adapter;

import cn.feihutv.zhibofeihu.data.db.model.MvDownLog;
import zlc.season.rxdownload3.core.Mission;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/12/12
 *     desc   : 下载信息
 *     version: 1.0
 * </pre>
 */
public class MyDownInfo {


    private int type=0;

    private  Mission  mMission;

    private MvDownLog mMvDownLog;


    public MyDownInfo(int type, Mission mission, MvDownLog mvDownLog) {
        this.type = type;
        mMission = mission;
        mMvDownLog = mvDownLog;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Mission getMission() {
        return mMission;
    }

    public void setMission(Mission mission) {
        mMission = mission;
    }

    public MvDownLog getMvDownLog() {
        return mMvDownLog;
    }

    public void setMvDownLog(MvDownLog mvDownLog) {
        mMvDownLog = mvDownLog;
    }
}
