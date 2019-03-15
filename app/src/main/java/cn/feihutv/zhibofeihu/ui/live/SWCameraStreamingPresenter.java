package cn.feihutv.zhibofeihu.ui.live;
import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import cn.feihutv.zhibofeihu.data.db.model.SysConfigBean;
import cn.feihutv.zhibofeihu.data.db.model.SysGiftNewBean;
import cn.feihutv.zhibofeihu.data.db.model.SysLaunchTagBean;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LocationRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LocationResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LogShareRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.CancelRoomMgrRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.CancelRoomMgrResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.GetRoomMrgsRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.GetRoomMrgsResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.CloseLiveRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.CloseLiveResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.JoinRoomRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.JoinRoomResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.LeaveRoomRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.LeaveRoomResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.StartLiveRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.StartLiveResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.SwitchAppFocusRequest;
import cn.feihutv.zhibofeihu.rxbus.RxBus;
import cn.feihutv.zhibofeihu.ui.base.BasePresenter;
import cn.feihutv.zhibofeihu.data.DataManager;

import com.chinanetcenter.StreamPusher.sdk.OnErrorListener;
import com.chinanetcenter.StreamPusher.sdk.SPConfig;
import com.chinanetcenter.StreamPusher.sdk.SPManager;
import com.chinanetcenter.StreamPusher.sdk.SPSurfaceView;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import cn.feihutv.zhibofeihu.ui.widget.dialog.MusicPickDialog;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import cn.feihutv.zhibofeihu.utils.ShareUtil;
import cn.feihutv.zhibofeihu.utils.TCConstants;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.LOCATION_SERVICE;
import static com.chinanetcenter.StreamPusher.sdk.SPConfig.DECODER_MODE_HARD;


/**
 * <pre>
 *     author : huang hao
 *     time   :
 *     desc   : p业务处理实现类
 *     version: 1.0
 * </pre>
 */
public class SWCameraStreamingPresenter<V extends SWCameraStreamingMvpView> extends BasePresenter<V>
        implements SWCameraStreamingMvpPresenter<V> {

    private String mPushUrl = "";
    private int mCurrentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private boolean mIsBgmPlaying = false;
    private int mCurrentBgmIndex = -1;
    private int mOpenedCameraId = -1;
    private SPConfig mSPConfig = null;
    private List<String> mBgmFiles = new LinkedList<String>();
    private int from=-1;
    @Inject
    public SWCameraStreamingPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void share(int from,SHARE_MEDIA plat) {
        getMvpView().showLoading();
        this.from=from;
        String headUrl = SharePreferenceUtil.getSession(getMvpView().getActivity(),"PREF_KEY_HEADURL");
        String nickName = SharePreferenceUtil.getSession(getMvpView().getActivity(),"PREF_KEY_NICKNAME");
        String accountId = SharePreferenceUtil.getSession(getMvpView().getActivity(),"PREF_KEY_ACCOUNTID");
        if (TextUtils.isEmpty(nickName)) {
            nickName = "";
        } else {
            nickName = "我是[" + nickName + "]";
        }
        ShareUtil.ShareWeb(getMvpView().getActivity(), "春风十里不如你，我与你相约在飞虎直播。。。", nickName + "我在飞虎直播等你！",
                headUrl, TCConstants.REGISTER_URL, plat, umShareListener);
    }

    UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            int to = 0;
            if (share_media.equals(SHARE_MEDIA.WEIXIN_CIRCLE)) {
                to = 1;
            } else if (share_media.equals(SHARE_MEDIA.SINA)) {
                to = 2;
            } else if (share_media.equals(SHARE_MEDIA.WEIXIN)) {
                to = 3;
            } else if (share_media.equals(SHARE_MEDIA.QZONE)) {
                to = 4;
            } else if (share_media.equals(SHARE_MEDIA.QQ)) {
                to = 5;
            }
             getCompositeDisposable().add(getDataManager()
                             .doLogShareApiCall(new LogShareRequest(from,to))
                             .subscribeOn(Schedulers.io())
                             .observeOn(AndroidSchedulers.mainThread())
                             .subscribe(new Consumer<Object>() {
                                 @Override
                                 public void accept(@NonNull Object obj) throws Exception {

                                 }
                             },getConsumer())

                     );

            getMvpView().onToast("分享成功");
            getMvpView().hideLoading();
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            getMvpView().onToast("分享失败");
            getMvpView().hideLoading();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            getMvpView().hideLoading();
        }
    };




    private LocationManager locationManager;//位置服务
    private String provider;//位置提供器
    private Location location;
    private String locationNow = "";
    @Override
    public void dingwei() {
        //权限已经被授予，在这里直接写要执行的相应方法即可
        //更改头像
        locationManager = (LocationManager) getMvpView().getActivity().getSystemService(LOCATION_SERVICE);//获得位置服务
        provider = judgeProvider(locationManager);
        if (provider != null) {//有位置提供器的情况
            //为了压制getLastKnownLocation方法的警告
            if (ActivityCompat.checkSelfPermission(getMvpView().getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getMvpView().getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                getLocation(location);//得到当前经纬度并开启线程去反向地理编码
            } else {
                //暂时无法获得当前位置
                locationNow = "";
            }
        } else {
            //不存在位置提供器的情况
        }
    }

    @Override
    public void switchAppFocus(boolean to) {
        getCompositeDisposable().add(getDataManager()
                        .doSwitchAppFocusApiCall(new SwitchAppFocusRequest(to))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(@NonNull Object obj) throws Exception {

                            }
                        },getConsumer())

                );

    }

    @Override
    public void startPublish(SPSurfaceView mPreviewView) {
        mSPConfig = SPManager.getConfig();
        mSPConfig.setRtmpUrl(mPushUrl);
        mSPConfig.setSurfaceView(mPreviewView);
        mSPConfig.setCameraId(mCurrentCameraId);
        mSPConfig.setEncoderMode(SPConfig.ENCODER_MODE_HARD);
        mSPConfig.setDecoderMode(DECODER_MODE_HARD);
        mSPConfig.setFps(24);
        mSPConfig.setAudioPlayMode(SPConfig.AUDIO_PLAY_OPENSL);
        mSPConfig.setVideoBitrate(700 * 1024);
        mSPConfig.setHasVideo(true);
        mSPConfig.setHasAudio(true);
        mSPConfig.setAppIdAndAuthKey("feihutv", "4E6A79B5936840B688E3259D6018E614");
        mSPConfig.setEchoCancellation(true);
        mSPConfig.setVideoResolution(SPManager.VideoResolution.VIDEO_RESOLUTION_480P, SPManager.VideoRatio.RATIO_16_9);
        SPManager.setOnErrorListener(new OnErrorListener() {
            @Override
            public void onError(int what, String extra) {
                Log.e("print", "onError: ---->" + what + "  " + extra);
                dealSpErr(what,extra);
            }
        });

        SPManager.setOnStateListener(new SPManager.OnStateListener() {
            @Override
            public void onState(int what, String extra) {
                Log.e("print", "onState: ---->" + what + "  " + extra);
                switch (what) {
                    case SPManager.STATE_CAMERA_OPEN_SUCCESS:
                        mOpenedCameraId = Integer.parseInt(extra);
                        // 消息在摄像头线程回调，为了保证前后置摄像头切换时不会出现mirror切换延时，尽量在该回调中调用setMirror
                        if (mOpenedCameraId == 1) {
                            SPManager.setMirror(true, true);
                        } else {
                            SPManager.setMirror(false, false);
                        }
                        break;
                    case SPManager.STATE_BGM_PLAY_COMPLETION:
                        if (mBgmFiles.size() == 0) {
                            mCurrentBgmIndex = -1;
                            mIsBgmPlaying = false;
                            break;
                        }
                        mCurrentBgmIndex++;
                        if (mCurrentBgmIndex >= mBgmFiles.size()) {
                            mCurrentBgmIndex = 0;
                        }
                        if (mIsBgmPlaying && SPManager.startBgm(mBgmFiles.get(mCurrentBgmIndex))) {
                            mIsBgmPlaying = true;
                        } else {
                            mIsBgmPlaying = false;
                        }
                        break;
                }
            }
        });

        SPManager.setMicVolume(1.0f);
        SPManager.setMicVolume(1.0f);
    }

    @Override
    public void initSPManager() {
        SPManager.init(getMvpView().getActivity(), mSPConfig);
        SPManager.switchFilter(SPManager.FilterType.BEAUTYG);
    }



    @Override
    public void dealTelephone() {
        TelephonyManager tm = (TelephonyManager) getMvpView().getActivity().getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }



    @Override
    public void quitRoom() {
        if(getMvpView().isNetworkConnected()) {
            getDataManager()
                    .doLoadLeaveRoomApiCall(new LeaveRoomRequest())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<LeaveRoomResponce>() {
                        @Override
                        public void accept(@NonNull LeaveRoomResponce response) throws Exception {
                            if (response.getCode() == 0) {
                                getDataManager()
                                        .doCloseLiveLiveApiCall(new CloseLiveRequest())
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Consumer<CloseLiveResponse>() {
                                            @Override
                                            public void accept(@NonNull CloseLiveResponse response) throws Exception {
                                                if (response.getCode() == 0) {
                                                    int ghb = response.getmCloseLiveData().getGHB();
                                                    int watchers = response.getmCloseLiveData().getWatches();
                                                    int followers = response.getmCloseLiveData().getFollowers();
                                                    Bundle args = new Bundle();
                                                    args.putInt("GHB", ghb);
                                                    args.putInt("Watches", watchers);
                                                    args.putInt("Followers", followers);
                                                    getMvpView().showDetailDialog(args);
                                                    SPManager.stopBgm();
                                                } else {
                                                    getMvpView().goMainActivity();
                                                }
                                            }
                                        }, getConsumer());
                            } else {
                                getMvpView().goMainActivity();
                            }
                        }
                    }, getConsumer());
        }else{
            getMvpView().goMainActivity();
        }
    }

    @Override
    public SysGiftNewBean getGiftBeanByID(String id) {
        return  getDataManager().getGiftBeanByID(id);
    }

    @Override
    public SysConfigBean getSysConfigBean() {
        return getDataManager().getSysConfig();
    }

    @Override
    public void joinRoom(String roomId, final String roomName, final int tag, final String reconnect, final boolean reJoinRoom) {
        getDataManager()
                .doLoadJoinRoomSocketApiCall(new JoinRoomRequest(roomId,reconnect))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JoinRoomResponce>() {
                    @Override
                    public void accept(@NonNull JoinRoomResponce response) throws Exception {
                        if(response.getCode()==0){
                            getDataManager().saveXiaolabaCount(response.getmJoinRoomData().getXiaolaba());
                            startLive("3",roomName,locationNow.replace("中国", ""),tag,reconnect,response.getmJoinRoomData(),reJoinRoom);
                        }else{
                            getMvpView().onToast("加入房间失败");
                            getMvpView().hideLoading();
                            AppLogger.e("joinRoom"+response.getErrMsg());
                        }
                    }
                }, getConsumer());
    }

    @Override
    public void changCamera() {
        if (SPManager.switchCamera()) {
            mCurrentCameraId = mCurrentCameraId == 0 ? 1 : 0;
            mSPConfig.setCameraId(mCurrentCameraId);
        }
    }

    @Override
    public void stopMusic(ChatLiveFragment chatFragment) {
        if (mIsBgmPlaying) {
            boolean s = SPManager.stopBgm();
            if (s && chatFragment != null) {
                chatFragment.setMusicPause("播放");
                mIsBgmPlaying = false;
            }
        } else {
            if (mBgmFiles.size() == 0) {
                return;
            }
            boolean s = SPManager.startBgm(mBgmFiles.get(mCurrentBgmIndex));
            if (s && chatFragment != null) {
                mIsBgmPlaying = true;
                chatFragment.setMusicPause("停止");
            }
        }
    }

    private boolean mIsPlayingback = false;
    @Override
    public void micoreMusic(ChatLiveFragment chatFragment) {
        if (mIsPlayingback) {
            boolean s = SPManager.switchAudioLoop(SPManager.SWITCH_OFF);
            if (s && chatFragment != null) {
                chatFragment.setMicBegin("耳机");
                mIsPlayingback = !mIsPlayingback;
            }
        } else {
            boolean s = SPManager.switchAudioLoop(SPManager.SWITCH_ON);
            if (s && chatFragment != null) {
                chatFragment.setMicBegin("外放");
                mIsPlayingback = !mIsPlayingback;
            }
        }
    }

    private MusicPickDialog mBgmPickDialog = null;
    @Override
    public void openFile() {
        if (mBgmPickDialog == null) {
            mBgmPickDialog = new MusicPickDialog();
        }
        if (mBgmPickDialog.isAdded())
            return;
        mBgmPickDialog.setOnclickListener(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    mBgmFiles = ((MusicPickDialog) dialog).getCheckedMusicList();
                    if (mIsBgmPlaying && SPManager.stopBgm()) {
                        mIsBgmPlaying = false;
                    }
                    if (mBgmFiles.size() == 0) {
                        mCurrentBgmIndex = -1;
                    } else {
                        mCurrentBgmIndex = 0;
                        if (SPManager.startBgm(mBgmFiles.get(mCurrentBgmIndex))) {
                            mIsBgmPlaying = true;
                            SPManager.setBgmVolume(0.5f);
                        }
                    }
                    getMvpView().openMusicFile();
                }

            }
        });
        mBgmPickDialog.show(getMvpView().getActivity().getFragmentManager(), mBgmPickDialog.getClass().getSimpleName());
    }

    @Override
    public void getRoomMgrs() {
        getCompositeDisposable().
                add(getDataManager().
                        doGetRoomMrgsRequestApiCall(new GetRoomMrgsRequest())
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<GetRoomMrgsResponce>() {
                            @Override
                            public void accept(@NonNull GetRoomMrgsResponce getRoomMrgsResponce) throws Exception {
                                if (getRoomMrgsResponce.getCode() == 0) {
                                    getMvpView().notifyRoomMrgList(getRoomMrgsResponce.getRoomMrgDataList());
                                } else {
                                    getMvpView().notifyRoomMrgList(getRoomMrgsResponce.getRoomMrgDataList());
                                    AppLogger.e(getRoomMrgsResponce.getErrMsg());
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {

                            }
                        }));
    }

    @Override
    public void cancelRoomMgr(String userId, final int position) {
        getCompositeDisposable().
                add(getDataManager().
                        doCancelRoomMgrCall(new CancelRoomMgrRequest(userId))
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<CancelRoomMgrResponce>() {
                            @Override
                            public void accept(@NonNull CancelRoomMgrResponce cancelRoomMgrResponce) throws Exception {
                                if (cancelRoomMgrResponce.getCode() == 0) {
                                    getMvpView().cancelRoomMgrState(true,position);
                                } else {
                                    getMvpView().cancelRoomMgrState(false,position);
                                    AppLogger.e(cancelRoomMgrResponce.getErrMsg());
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {

                            }
                        }));
    }

    @Override
    public List<SysLaunchTagBean> getkaiboSysLaunchTagBean() {
        return getDataManager().getkaiboSysLaunchTagBean();
    }


    private void startLive(String device, String roomName, String adress, int tag, String reconnect, final JoinRoomResponce.JoinRoomData roomData, final boolean reJoinRoom) {
        getDataManager()
                .doLoadStartLiveApiCall(new StartLiveRequest(device,roomName,adress,reconnect,String.valueOf(tag)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<StartLiveResponce>() {
                    @Override
                    public void accept(@NonNull StartLiveResponce response) throws Exception {
                        if(response.getCode()==0){
                            SharePreferenceUtil.saveSeesionLong(getMvpView().getActivity(), "live_time", System.currentTimeMillis());
                            List<StartLiveResponce.OfflineGiftsData> list=response.getmStartLiveData().getOfflineGiftsDataList();
                            if (list!= null && list.size() > 0) {
                                if(!reJoinRoom) {
                                    getMvpView().showOfflineGifts(list);
                                }
                            }
                            SPManager.setConfig(SPManager.getConfig().setRtmpUrl(response.getmStartLiveData().getPushUrl()));
                            boolean mIsUserPushing = !getMvpView().getmIsUserPushing();
                            boolean mIsSuccess = false;
                            if (mIsUserPushing) {
                                mIsSuccess = SPManager.startPushStream();
                            } else {
                                mIsSuccess = SPManager.stopPushStream();
                            }
                            if (!mIsSuccess) {
                                mIsUserPushing = !mIsUserPushing;
                                getMvpView().setmIsUserPushing(mIsUserPushing);
                            }

                            getMvpView().initViewPager(roomData);
                        }else{
                            if (response.getCode() == 4603) {
                                getMvpView().onToast("您已被禁止直播");
                                String desc = response.getErrExtMsgData().getDesc();
                                String duration = response.getErrExtMsgData().getDuration();
                                getMvpView().showBanDialog(desc, duration);

                            } else if (response.getCode() == 4009) {
                                getMvpView().onToast("直播标题长度超限或内容违规");
                            } else if (response.getCode() == 4801) {
                                getMvpView().onToast("标题含敏感词，请更换标题。");
                            }else{
                                getMvpView().onToast("网络异常，请稍候重试");
                            }
                            AppLogger.e("startLive"+response.getErrMsg());
                        }
                        getMvpView().hideLoading();
                    }
                }, getConsumer());
    }

    PhoneStateListener listener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                //电话等待接听
                case TelephonyManager.CALL_STATE_RINGING:
                    SPManager.onPause();
                    break;
                //电话接听
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    SPManager.onPause();
                    break;
                //电话挂机
                case TelephonyManager.CALL_STATE_IDLE:
                    SPManager.startPushStream();
                    break;
            }
        }
    };

    private void dealSpErr(final int what,final String extra){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(what);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                switch (integer) {
                    case SPManager.ERROR_BGM_PLAY:
                        mIsBgmPlaying = false;
                        mCurrentBgmIndex = -1;
                        break;
                    case SPManager.ERROR_PARAM:
                        if (extra.equals("encoderMode =0 error mode!")) {
                            getMvpView().onToast("不支持硬编，自动切换到软编！");
                            mSPConfig.setEncoderMode(SPConfig.ENCODER_MODE_SOFT);
                            mSPConfig.setDecoderMode(SPConfig.DECODER_MODE_SOFT);
                            SPManager.init(getMvpView().getActivity(), mSPConfig);
                        }
                        break;
                    case 3303:
                        // 连接失败connect failed 4

                        break;
                    case 3304:
                        // 数据发送失败
                        getMvpView().onToast("推流数据发送失败,正在重连");
                        break;
                    case 4301:
                        getMvpView().onToast("网络环境差");
                        break;
                    case 5301:
                        // 推流成功
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 得到当前经纬度并开启线程去反向地理编码
     */
    public void getLocation(final Location location) {
        String latitude = location.getLatitude() + "";
        String longitude = location.getLongitude() + "";
//        String url = "http://maps.google.cn/maps/api/geocode/json?latlng=" + latitude + "," + longitude + "&sensor=true,language=zh-CN";
        String latlng=latitude+","+longitude;
        getCompositeDisposable().add(getDataManager()
                         .doGetLocationApiCall(new LocationRequest(latlng,true,"zh-CN"))
                         .subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribe(new Consumer<LocationResponce>() {
                             @Override
                             public void accept(@NonNull LocationResponce locationResponce) throws Exception {
                                 List<LocationResponce.LocationEntity> list=locationResponce.getLocationEntities();
                                 for (final LocationResponce.LocationEntity locationEntity : list) {
                                     if (locationEntity.getTypes().size() == 2 && locationEntity.getTypes().get(0).equals("locality") && locationEntity.getTypes().get(1).equals("political")) {
                                         locationNow = locationEntity.getFormatted_address();
                                         getMvpView().setLocationText(locationNow.replace("中国", ""));
                                     }
                                 }
                             }
                         },getConsumer()));

    }

    /**
     * 判断是否有可用的内容提供器
     *
     * @return 不存在返回null
     */
    private String judgeProvider(LocationManager locationManager) {
        List<String> prodiverlist = locationManager.getProviders(true);
        if (prodiverlist.contains(LocationManager.NETWORK_PROVIDER)) {
            return LocationManager.NETWORK_PROVIDER;
        } else if (prodiverlist.contains(LocationManager.GPS_PROVIDER)) {
            return LocationManager.GPS_PROVIDER;
        } else {
            locationNow = "";
            getMvpView().onToast("没有可用的位置提供器，请手动打开定位权限");
        }
        return null;
    }

}
