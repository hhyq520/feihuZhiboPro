package cn.feihutv.zhibofeihu.ui.widget.video;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.utils.glideutil.GlideApp;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/11/13
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class VideoPlayerStandard  extends RelativeLayout implements View.OnClickListener,
        SeekBar.OnSeekBarChangeListener, View.OnTouchListener {


    public VideoPlayerStandard(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public VideoPlayerStandard(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public VideoPlayerStandard(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


//    CNCVideoViewEx mCNCVideoViewEx;

    public ImageView backButton;
    public ProgressBar bottomProgressBar, loadingProgressBar;
    public TextView titleTextView;
    public ImageView thumbImageView;
    public ImageView tinyBackImageView;
    public LinearLayout batteryTimeLayout;
    public ImageView batteryLevel;
    public TextView videoCurrentTime;
    public TextView retryTextView;
    public TextView clarity;

    public ImageView startButton;
    public SeekBar progressBar;
    public ImageView fullscreenButton;
    public TextView currentTimeTextView, totalTimeTextView;
    public ViewGroup textureViewContainer;
    public ViewGroup topContainer, bottomContainer;



    public void initView(Context context){

        LayoutInflater.from(context).inflate(R.layout.layout_mv_video_view, this);

        loadingProgressBar = findViewById(R.id.loading);
        bottomProgressBar = findViewById(R.id.bottom_progress);

//        mCNCVideoViewEx=findViewById(R.id.video_view);

        thumbImageView=findViewById(R.id.thumb);
        startButton = findViewById(R.id.start);
        fullscreenButton = findViewById(R.id.fullscreen);
        progressBar = findViewById(R.id.bottom_seek_progress);
        currentTimeTextView = findViewById(R.id.current);
        totalTimeTextView = findViewById(R.id.total);
        bottomContainer = findViewById(R.id.layout_bottom);

        startButton.setOnClickListener(this);
        fullscreenButton.setOnClickListener(this);
        progressBar.setOnSeekBarChangeListener(this);

    }


    public void setLiveThumbImage(Context mContext,String url){
        //直播封面
        GlideApp.loadImg(mContext, url, R.drawable.bg,
                thumbImageView);
    }





    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start:
                if(mMvVideoPlayerListener!=null){
                    mMvVideoPlayerListener.onStartPlay();
                }
                loadingProgressBar.setVisibility(VISIBLE);
                thumbImageView.setVisibility(GONE);

                break;
            case R.id.fullscreen:

                break;
        }

    }

    public void setMvVideoPlayerListener(MvVideoPlayerListener mvVideoPlayerListener) {
        mMvVideoPlayerListener = mvVideoPlayerListener;
    }

    private  MvVideoPlayerListener  mMvVideoPlayerListener;



    public void playVideo(String playUrl){
        loadingProgressBar.setVisibility(GONE);
//        mCNCVideoViewEx.play(playUrl);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
