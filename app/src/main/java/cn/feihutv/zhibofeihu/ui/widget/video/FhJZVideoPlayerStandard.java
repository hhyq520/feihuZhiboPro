package cn.feihutv.zhibofeihu.ui.widget.video;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedHashMap;

import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.PlayMVRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.PlayMVResponse;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.jzvd.JZVideoPlayerStandard;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 这里可以监听到视频播放的生命周期和播放状态
 * 所有关于视频的逻辑都应该写在这里
 */
public class FhJZVideoPlayerStandard extends JZVideoPlayerStandard {
    public FhJZVideoPlayerStandard(Context context) {
        super(context);
    }

    public FhJZVideoPlayerStandard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextView title_play;

    public ImageView Detail_back;

    public String mvId;


    @Override
    public void init(final Context context) {
        super.init(context);

        title_play=findViewById(R.id.title_play);
        Detail_back=findViewById(R.id.Detail_back);
        Detail_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof Activity){
                     ((Activity) context).finish();
                }
            }
        });
    }


    public void setDetail_backShow(){
        Detail_back.setVisibility(VISIBLE);
    }


    public void setBaseVideoInfo(String mvid,String title,String playNum){
        this.mvId=mvid;
        titleTextView.setText(title);
        title_play.setText(playNum);
    }



    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == cn.jzvd.R.id.fullscreen) {
            if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
                //click quit fullscreen
            } else {
                //click goto fullscreen
            }
        }
    }


    @Override
    public void setUp(LinkedHashMap urlMap, int defaultUrlMapIndex, int screen, Object... objects) {
        super.setUp(urlMap, defaultUrlMapIndex, screen, objects);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return super.onTouch(v, event);
    }

    @Override
    public void startVideo() {
        super.startVideo();

        //调用播放请求
        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .doPlayMVCall(new PlayMVRequest(mvId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PlayMVResponse>() {
                    @Override
                    public void accept(@NonNull PlayMVResponse response) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                })

        );

    }



    @Override
    public void onStateNormal() {
        super.onStateNormal();

        AppLogger.e("FhJZVideoPlayerStandard------onStateNormal");
    }

    @Override
    public void onStatePreparing() {
        super.onStatePreparing();
        AppLogger.e("FhJZVideoPlayerStandard------onStatePreparing");
    }

    @Override
    public void onStatePlaying() {
        super.onStatePlaying();
        AppLogger.e("FhJZVideoPlayerStandard------onStatePlaying");
    }



    @Override
    public void onStatePause() {
        super.onStatePause();
        AppLogger.e("FhJZVideoPlayerStandard------onStatePause");
    }

    @Override
    public void onStateError() {
        super.onStateError();
        AppLogger.e("FhJZVideoPlayerStandard------onStateError");
    }

    @Override
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
        AppLogger.e("FhJZVideoPlayerStandard------onStateAutoComplete");
    }


    @Override
    public void onInfo(int what, int extra) {
        super.onInfo(what, extra);
        AppLogger.e("FhJZVideoPlayerStandard------onInfo");
    }

    @Override
    public void onError(int what, int extra) {
        super.onError(what, extra);
        AppLogger.e("FhJZVideoPlayerStandard------onError");
    }

    @Override
    public void startWindowFullscreen() {
        super.startWindowFullscreen();

        AppLogger.e("FhJZVideoPlayerStandard------startWindowFullscreen");
    }

    @Override
    public void startWindowTiny() {

    }

}
