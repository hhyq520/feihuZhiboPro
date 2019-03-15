package cn.feihutv.zhibofeihu.ui.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.cnc.mediaplayer.sdk.lib.event.PlayEvent;
import com.cnc.mediaplayer.sdk.lib.settings.CNCSDKSettings;
import com.cnc.mediaplayer.sdk.lib.utils.log.ALog;
import com.cnc.mediaplayer.sdk.lib.videoview.CNCVideoView;
import com.cnc.mediaplayer.sdk.lib.videoview.IMediaEventsListener;

/**
 * Created by wuhp on 2016/8/26.
 */
public class CNCVideoViewEx extends CNCVideoView {

    private final String TAG = CNCVideoViewEx.class.getSimpleName();
    protected int mSeekPosition;
    protected CNCSDKSettings mSDKSettings;
    private IMediaEventsListener mOnMediaEventsListener;

    public CNCVideoViewEx(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public CNCVideoViewEx(Context context) {
        super(context);
        init(context);
    }

    public CNCVideoViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CNCVideoViewEx(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setOnMediaEventsListener(IMediaEventsListener listener) {
        mOnMediaEventsListener = listener;
    }

    private void init(Context context) {
        mSDKSettings = CNCSDKSettings.getInstance();

        /**
         * 设置回调，把CNCVideoView的一些事件通过回调传递给CNCVideoViewEx
         */
        setMediaEventsListener(new IMediaEventsListener() {
            @Override
            public void onMediaPause() {
                if (mOnMediaEventsListener != null) {
                    mOnMediaEventsListener.onMediaPause();
                }
            }

            @Override
            public void onMediaStart() {

            }

            @Override
            public void onBufferingStart() {
            }

            @Override
            public void onBufferingEnd() {
//                if (mMediaController != null) {
//                    mMediaController.hideLoading();
//                }
                if (mOnMediaEventsListener != null) {
                    mOnMediaEventsListener.onBufferingEnd();
                }
            }

            @Override
            public void onMediaCompletion() {
//                if (mMediaController != null) {
//                    mMediaController.showComplete();
//                }
                if (mOnMediaEventsListener != null) {
                    mOnMediaEventsListener.onMediaCompletion();
                }
            }

            @Override
            public void onMediaPrepared() {
//                if (mMediaController != null) {
//                    mMediaController.hideLoading();
//                    mMediaController.setEnabled(true);
//                }
                if (mOnMediaEventsListener != null) {
                    mOnMediaEventsListener.onMediaPrepared();
                }
            }

            @Override
            public void onMediaError(int what, int extra) {
                String errorText = "[" + extra + "] " + PlayEvent.getErrorText(extra);
                ALog.e(TAG, "onMediaError: " + what + "," + extra + "。错误说明：" + errorText);
//                if (mMediaController != null) {
//                    mMediaController.showError(errorText);
//                }
                if (mOnMediaEventsListener != null) {
                    mOnMediaEventsListener.onMediaError(what, extra);
                }
            }

            @Override
            public void onMediaInfo(int what, int extra) {
                if (mOnMediaEventsListener != null) {
                    mOnMediaEventsListener.onMediaInfo(what, extra);
                }
            }
        });
    }


    /**
     * 启动播放器播放视频
     *
     * @param url
     */
    public void play(String url) {
        setVideoPath(url);
        start();
    }


    /*----------------生命周期 start---------------------------------------------------*/

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /**
     * 销毁播放器
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        //释放资源
    }

}
