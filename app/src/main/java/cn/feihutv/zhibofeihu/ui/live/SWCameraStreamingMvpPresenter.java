package cn.feihutv.zhibofeihu.ui.live;

import com.chinanetcenter.StreamPusher.sdk.SPSurfaceView;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;

import cn.feihutv.zhibofeihu.data.db.model.SysConfigBean;
import cn.feihutv.zhibofeihu.data.db.model.SysGiftNewBean;
import cn.feihutv.zhibofeihu.data.db.model.SysGoodsNewBean;
import cn.feihutv.zhibofeihu.data.db.model.SysLaunchTagBean;
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
public interface SWCameraStreamingMvpPresenter<V extends SWCameraStreamingMvpView> extends MvpPresenter<V> {

    //定义业务处理方法
        void share(int from,SHARE_MEDIA plat);//分享

        void dingwei();//定位

        void switchAppFocus(boolean to);//切换前后台

        void startPublish(SPSurfaceView m);//初始化直播相关参数

        void initSPManager();


        void dealTelephone();

        void quitRoom();

        SysGiftNewBean getGiftBeanByID(String id);

        SysConfigBean getSysConfigBean();

        void joinRoom(String roomId,String roomName,int tag,String reconnect,boolean reJoin);

        void changCamera();

        void stopMusic(ChatLiveFragment fragment);

        void micoreMusic(ChatLiveFragment fragment);

        void openFile();

        void getRoomMgrs();

        void cancelRoomMgr(String id,int position);

        List<SysLaunchTagBean> getkaiboSysLaunchTagBean();
}
