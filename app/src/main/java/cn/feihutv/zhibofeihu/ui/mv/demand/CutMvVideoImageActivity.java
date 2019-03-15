package cn.feihutv.zhibofeihu.ui.mv.demand;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.widget.TCActivityTitle;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.FileUtils;
import cn.feihutv.zhibofeihu.utils.video.DeviceUtils;
import cn.feihutv.zhibofeihu.utils.video.ExtractFrameWorkThread;
import cn.feihutv.zhibofeihu.utils.video.ExtractVideoInfoUtil;

/**
 * <pre>
 *     @author : sichu.chen
 *     time   : 2017/11/20
 *     desc   : 设置封面
 *     version: 1.0
 * </pre>
 */
public class CutMvVideoImageActivity extends BaseActivity {


    @BindView(R.id.tc_title)
    TCActivityTitle tc_title;

    @BindView(R.id.mv_editImage)
    ImageView mv_editImage;


    @BindView(R.id.mv_choose_layout)
    LinearLayout mv_choose_layout;

    @BindView(R.id.mv_cut_seekbBar)
    SeekBar mv_cut_seekbBar;

    @BindView(R.id.cut_mv_img1)
    ImageView cut_mv_img1;
    @BindView(R.id.cut_mv_img2)
    ImageView cut_mv_img2;
    @BindView(R.id.cut_mv_img3)
    ImageView cut_mv_img3;
    @BindView(R.id.cut_mv_img4)
    ImageView cut_mv_img4;
    @BindView(R.id.cut_mv_img5)
    ImageView cut_mv_img5;
    @BindView(R.id.cut_mv_img6)
    ImageView cut_mv_img6;

    long endPosition;

    int lastProgress;
    List<ImageView> mImageViews = new ArrayList<>();

    List<VideoEditInfo> mVideoEditInfoList = new ArrayList<>();

    private ExtractVideoInfoUtil mExtractVideoInfoUtil;
    private ExtractFrameWorkThread mExtractFrameWorkThread;

    private String OutPutFileDirPath;

    String chooseImagePath = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cut_mv_video;
    }

    @Override
    protected void initWidget() {
        //初始化注入控件
        setUnBinder(ButterKnife.bind(this));

        OutPutFileDirPath = FileUtils.getMvFileDir();

        mImageViews.add(cut_mv_img1);
        mImageViews.add(cut_mv_img2);
        mImageViews.add(cut_mv_img3);
        mImageViews.add(cut_mv_img4);
        mImageViews.add(cut_mv_img5);
        mImageViews.add(cut_mv_img6);


        tc_title.setReturnListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tc_title.setMoreListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("chooseImg", chooseImagePath);
                setResult(1001, intent);
                finish();
            }
        });


        String path = getIntent().getStringExtra("path");

        mExtractVideoInfoUtil = new ExtractVideoInfoUtil(path);
        String duration = mExtractVideoInfoUtil.getVideoLength();
        int w = mExtractVideoInfoUtil.getVideoWidth();
        int h = mExtractVideoInfoUtil.getVideoHeight();
        int degree = mExtractVideoInfoUtil.getVideoDegree();
        String mimeType = mExtractVideoInfoUtil.getMimetype();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("duration:").append(duration).append("ms ");
        stringBuilder.append("width:").append(w).append(" ");
        stringBuilder.append("height:").append(h).append(" ");
        stringBuilder.append("degree:").append(degree).append(" ");
        stringBuilder.append("mimeType:").append(mimeType);

        AppLogger.i("选择的视频：" + stringBuilder.toString());

        showLoading("正在初始化...");
        String firstCutVideoPath = mExtractVideoInfoUtil.extractFrames(OutPutFileDirPath);
        showTopImageView(firstCutVideoPath);
        chooseImagePath = firstCutVideoPath;//默认选择第一帧图片

        //开始截取视频
        endPosition = Long.valueOf(mExtractVideoInfoUtil.getVideoLength());
        long startPosition = 0;
        int thumbnailsCount = 36;
        int extractW = (DeviceUtils.getScreenWidth(this)) / 4;
        int extractH = DeviceUtils.dip2px(this, 55);
        if(extractW>1000){
            extractW=1000;
        }
        mExtractFrameWorkThread = new ExtractFrameWorkThread(
                extractW, extractH, mUIHandler, path,
                OutPutFileDirPath, startPosition, endPosition, thumbnailsCount);
        mExtractFrameWorkThread.start();

        mv_cut_seekbBar.setMax(35);

        mv_cut_seekbBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(final SeekBar seekBar, int progress, boolean fromUser) {


                VideoEditInfo vei = mVideoEditInfoList.get(progress);
                chooseImagePath = vei.path;
                if (vei != null) {
                    Glide.with(CutMvVideoImageActivity.this)
                            .load("file://" + vei.path)
                            .transition(new DrawableTransitionOptions().crossFade(800))
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e,
                                                            Object model, Target<Drawable> target,
                                                            boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model,
                                                               Target<Drawable> target,
                                                               DataSource dataSource,
                                                               boolean isFirstResource) {

                                    setSeekBarDrawableTop(seekBar, resource);
                                    return false;
                                }
                            })
                            .into(mv_editImage);

                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }


    private void showTopImageView(String firstCutVideoPath){
        Glide.with(this)
                .load("file://" + firstCutVideoPath)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        setSeekBarDrawableTop(mv_cut_seekbBar, resource);
                        return false;
                    }
                })
                .into(mv_editImage);


    }

    private void setSeekBarDrawableTop(SeekBar seekBar, Drawable resource) {
        resource = zoomDrawable(resource, 80, 80);
        Drawable[] array = new Drawable[2];
        array[0] = getResources().getDrawable(R.drawable.mv_cut_bg);
        ;
        array[1] = resource;
        LayerDrawable result = new LayerDrawable(array);
        result.setLayerInset(0, 0, 0, 0, 0);
        result.setLayerInset(1, 7, 7, 7, 7);
        seekBar.setThumb(result);
        seekBar.setThumbOffset(0);
    }


    private Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable);
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
                matrix, true);
        return new BitmapDrawable(null, newbmp);
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }



    private final MainHandler mUIHandler = new MainHandler(this);

    private static class MainHandler extends Handler {
        private final WeakReference<CutMvVideoImageActivity> mActivity;

        MainHandler(CutMvVideoImageActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            CutMvVideoImageActivity activity = mActivity.get();
            if (activity != null) {
                if (msg.what == ExtractFrameWorkThread.MSG_SAVE_SUCCESS) {
                    if (activity.mv_choose_layout != null) {
                        VideoEditInfo info = (VideoEditInfo) msg.obj;
                        AppLogger.e("cutImage", info.toString());
                        activity.mVideoEditInfoList.add(info);
                        if (activity.mVideoEditInfoList.size() == 36) {
                            for (int i = 0; i < 6; i++) {

                                VideoEditInfo infos = activity.mVideoEditInfoList.get(i * 6);
                                if(i==0){
                                    activity. chooseImagePath=infos.path;
                                    activity. showTopImageView(infos.path);
                                }
                                if (infos != null) {
                                    Glide.with(activity)
                                            .load("file://" + infos.path)
                                            .into(activity.mImageViews.get(i));
                                }
                            }

                            activity.hideLoading();

                        }


                    }
                }
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mExtractVideoInfoUtil.release();
        if (mExtractFrameWorkThread != null) {
            mExtractFrameWorkThread.stopExtract();
        }
        mUIHandler.removeCallbacksAndMessages(null);
        if (!TextUtils.isEmpty(OutPutFileDirPath)) {
//            PictureUtils.deleteFile(new File(OutPutFileDirPath));
        }
    }
}
