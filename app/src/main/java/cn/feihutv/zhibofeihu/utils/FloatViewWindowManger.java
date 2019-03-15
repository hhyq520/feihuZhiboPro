package cn.feihutv.zhibofeihu.utils;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.cnc.mediaplayer.sdk.lib.event.AuthEvent;
import com.cnc.mediaplayer.sdk.lib.event.GeneralEvent;
import com.cnc.mediaplayer.sdk.lib.event.OnStatusCodeEventListener;
import com.cnc.mediaplayer.sdk.lib.renderview.IRenderView;

import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.LeaveRoomRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.LeaveRoomResponce;
import cn.feihutv.zhibofeihu.ui.widget.CNCVideoViewEx;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by huanghao on 2017/3/24.
 */

public class FloatViewWindowManger {
    private static final String TAG = FloatViewWindowManger.class.getSimpleName();
    /**
     * 用于控制在屏幕上添加或移除悬浮窗
     */
    private static WindowManager mWindowManager;
    private static WindowManager.LayoutParams mLayout;
    private static View mWindowsView;
    // 窗口宽高值
    private static float x, y;
    private static ProgressBar progressBar;
    private static ImageView videoBg;
    private static CNCVideoViewEx tcvideoview;
    private static ImageView close;
    private static Handler mOnStatusCodeEventHandler;

    public static void createSmallWindow(Context context, final String playUrl, final String roomId, final String headUrl, final int broadcastType) {
        try {
            if(mWindowManager!=null){
                removeSmallWindow();
            }
            initHandler();
            // 取得系统窗体
            mWindowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            // 窗体的布局样式
            mLayout = FeihuZhiboApplication.getApplication().getMywmParams();
            // 设置窗体显示类型——TYPE_SYSTEM_ALERT(系统提示)
            mLayout.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            // 设置窗体焦点及触摸：
            // FLAG_NOT_FOCUSABLE(不能获得按键输入焦点)
            mLayout.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            // 设置显示的模式
            mLayout.format = PixelFormat.RGBA_8888;
            // 设置对齐的方法
            mLayout.gravity = Gravity.TOP | Gravity.LEFT;
            // 设置窗体宽度和高度

            if(broadcastType == 1) {// PC
                mLayout.width = TCUtils.dipToPx(context, 200);
                mLayout.height = TCUtils.dipToPx(context, 150);
                mLayout.x = (context.getResources().getDisplayMetrics().widthPixels - TCUtils.dipToPx(context, 200));
                mLayout.y = (context.getResources().getDisplayMetrics().heightPixels - TCUtils.dipToPx(context, 225));
                //将制定View解析后添加到窗口管理器里面
                mWindowsView = View.inflate(context, R.layout.windows_layout, null);
            }else {// 手机
                float rate = (float) context.getResources().getDisplayMetrics().heightPixels / context.getResources().getDisplayMetrics().widthPixels;
                int height = (int) (TCUtils.dipToPx(context, 150) * rate);
                mLayout.width = TCUtils.dipToPx(context, 150);
                mLayout.height = height;
                mLayout.x = (context.getResources().getDisplayMetrics().widthPixels - TCUtils.dipToPx(context, 150));
                mLayout.y = context.getResources().getDisplayMetrics().heightPixels - height - TCUtils.dipToPx(context, 75);
                //将制定View解析后添加到窗口管理器里面
                mWindowsView = View.inflate(context, R.layout.ve_windows_layout, null);
            }

            tcvideoview = (CNCVideoViewEx) mWindowsView.findViewById(R.id.video_view);
            close = (ImageView) mWindowsView.findViewById(R.id.close);
            videoBg = (ImageView) mWindowsView.findViewById(R.id.video_bg);
            progressBar = (ProgressBar) mWindowsView.findViewById(R.id.progressBar);
            mWindowManager.addView(mWindowsView, mLayout);

            tcvideoview.setOnStatusCodeEventListener(new OnStatusCodeEventListener() {
                @Override
                public void onStatusCodeCallback(int what, String extra) {
                    if(mOnStatusCodeEventHandler!=null)
                        mOnStatusCodeEventHandler.obtainMessage(what, extra).sendToTarget();
                }
            });
            if(broadcastType == 1) {// PC
                tcvideoview.setAspectRatio(IRenderView.AR_4_3_FIT_PARENT);
            }else {// 手机
                tcvideoview.setAspectRatio(IRenderView.AR_MATCH_PARENT);
            }
            tcvideoview.play(playUrl);

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        tcvideoview.closePlayer();
                        tcvideoview.onDestroy();
                        quitRoom(roomId);
                        removeSmallWindow();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

            mWindowsView.setOnTouchListener(new View.OnTouchListener() {
                float mTouchStartX;
                float mTouchStartY;
                boolean ismove = true;
                int moveX = 0;
                int moveY = 0;
                long currentMS;

                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    try {
                        x = event.getRawX();
                        y = event.getRawY() - 25;
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                mTouchStartX = event.getX();
                                mTouchStartY = event.getY();
                                ismove = false;
                                moveX = 0;
                                moveY = 0;
                                currentMS = System.currentTimeMillis();
                                break;
                            case MotionEvent.ACTION_UP:
                                long moveTime = System.currentTimeMillis() - currentMS;
                                if (moveTime < 200 || (moveX < 20 && moveY < 20)) {
                                    removeSmallWindow();
                                    AppUtils.startLiveActivity(FeihuZhiboApplication.getApplication(), roomId, headUrl, broadcastType, true);
                                }
                                ismove = true;
                                break;
                            case MotionEvent.ACTION_MOVE:
                                //原始坐标减去移动坐标
                                Log.i("main", "mTx值=" + event.getX() + "\nmTy值=" + event.getY());
                                ismove = true;
                                moveX += Math.abs(event.getX() - mTouchStartX);//X轴距离
                                moveY += Math.abs(event.getY() - mTouchStartY);//y轴距离
                                if (mLayout != null) {
                                    mLayout.x = (int) (x - mTouchStartX);
                                    mLayout.y = (int) (y - mTouchStartY);
                                    mWindowManager.updateViewLayout(mWindowsView, mLayout);
                                }
                                Log.i("main", "x值=" + x + "\ny值=" + y + "\nmTouchX" + mTouchStartX + "\nmTouchY=" + mTouchStartY);
                                break;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return true;
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void quitRoom(String roomId){
        FeihuZhiboApplication.getApplication().mDataManager
                .doLoadLeaveRoomApiCall(new LeaveRoomRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LeaveRoomResponce>() {
                    @Override
                    public void accept(@NonNull LeaveRoomResponce response) throws Exception {
                        if (response.getCode() == 0) {

                        } else {
                            AppLogger.e(response.getErrMsg());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                });
    }

    private static void initHandler() {
        mOnStatusCodeEventHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case GeneralEvent.PARAMETER_ERROR:
                        Log.e(TAG, "handleMessage: ---->输入参数错误");
                        break;
                    case GeneralEvent.SDK_INIT_SUCCESS:
                        Log.e(TAG, "handleMessage: ---->SDK初始化成功");
                        break;
                    case GeneralEvent.SDK_INIT_FAILED:
                        Log.e(TAG, "handleMessage: ---->SDK初始化失败");
                        break;
                    case AuthEvent.AUTHORIZING:
                        Log.e(TAG, "handleMessage: ---->正在鉴权中");
                        break;
                    case AuthEvent.SDK_EXPIRED:
                        Log.e(TAG, "handleMessage: ---->过期");
                        break;
                    case AuthEvent.VERSION_OLD:
                        Log.e(TAG, "handleMessage: ---->SDK版本过低");
                        break;
                    case AuthEvent.SDK_UNMATCHED:
                        Log.e(TAG, "handleMessage: ---->SDK类型不匹配");
                        break;
                    case AuthEvent.APPID_UNMATCHED:
                        Log.e(TAG, "handleMessage: ---->AppID类型不匹配");
                        break;
                    case AuthEvent.APPKEY_UNMATCHED:
                        Log.e(TAG, "handleMessage: ---->authKey 不匹配");
                        break;
                    case AuthEvent.RESP_ERROR:
                        Log.e(TAG, "handleMessage: ---->鉴权服务器响应错误");
                        break;
                    case AuthEvent.UNKNOWN_ERROR:
                        Log.e(TAG, "handleMessage: ---->未知鉴权错误");
                        break;
                    case 5401:
                        try {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                            if (videoBg != null) {
                                videoBg.setImageBitmap(null);
                                videoBg.setVisibility(View.GONE);
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        Log.e(TAG, "handleMessage: ---->请求成功");
                        break;
                    default:
                        Log.e(TAG, "handleMessage: ---->" + msg.what + "  " + msg.obj.toString());
                        break;
                }
            }
        };
    }

//    private static void quitroom(String roomId){
//        ServerAPI.leaveRoom(roomId, new IFHCallBack() {
//            @Override
//            public void cbBlock(FHResponse resp) {
//
//            }
//        });
//    }

    /**
     * 将小悬浮窗从屏幕上移除。
     */
    public static void removeSmallWindow() {
        try {
            if(tcvideoview!=null) {
                tcvideoview.closePlayer();
                tcvideoview.onDestroy();
            }
            if (mOnStatusCodeEventHandler != null) {
                mOnStatusCodeEventHandler.removeCallbacksAndMessages(null);
                mOnStatusCodeEventHandler = null;
            }
            if (mWindowManager != null && mWindowsView != null) {
                mWindowManager.removeView(mWindowsView);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        mWindowManager = null;
        mWindowsView = null;
        mLayout = null;
        progressBar = null;
        videoBg = null;
        tcvideoview = null;
        close = null;
    }

}
