package cn.feihutv.zhibofeihu.ui.me;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.tencent.cos.model.COSRequest;
import com.tencent.cos.model.COSResult;
import com.tencent.cos.model.PutObjectResult;
import com.tencent.cos.task.listener.IUploadTaskListener;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.DataManager;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetCosSignRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetCosSignResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.me.ModifyProfileLiveCoverRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.me.ModifyProfileLiveCoverResponse;
import cn.feihutv.zhibofeihu.rxbus.RxBus;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.me.personalInfo.PersonalInfoActivity;
import cn.feihutv.zhibofeihu.utils.AppConstants;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.AppUtils;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.feihutv.zhibofeihu.utils.glideutil.GlideApp;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/23 14:06
 *      desc   : 显示大图
 *      version: 1.0
 * </pre>
 */

public class ShowBigImageActivity extends BaseActivity {

    @BindView(R.id.image)
    PhotoView image;

    @BindView(R.id.tv_back)
    TextView tvBack;

    @BindView(R.id.iv_more)
    ImageView ivMore;

    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    private String title = "";

    private String url;
    private static final String INTENT_EXTRA_IMAGE = "INTENT_EXTRA_IMAGE";
    private static String IS_FROM_OTHER = "IS_FROM_OTHER";
    private static final String TITLE = "TITLE";
    private boolean isFromOther = false;
    private static final String FLAG = "FLAG";
    private int flag = -1;
    private List<LocalMedia> selectList = new ArrayList<>();
    private DataManager mDataManager;

    public static void start(Context context, String url, boolean isFromOther, int flag, String title) {
        Intent intent = new Intent();
        intent.putExtra(INTENT_EXTRA_IMAGE, url);
        intent.putExtra(IS_FROM_OTHER, isFromOther);
        intent.putExtra(FLAG, flag);
        intent.putExtra(TITLE, title);
        intent.setClass(context, ShowBigImageActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_big_image;
    }

    @Override
    protected void initWidget() {
        setUnBinder(ButterKnife.bind(this));

        Intent intent = getIntent();
        url = intent.getStringExtra(INTENT_EXTRA_IMAGE);
        isFromOther = intent.getBooleanExtra(IS_FROM_OTHER, false);
        flag = intent.getIntExtra(FLAG, -1);
        title = intent.getStringExtra(TITLE);

        mTvTitle.setText(title);
        mDataManager = FeihuZhiboApplication.getApplication().mDataManager;

        if (isFromOther) {
            rlTitle.setVisibility(View.GONE);
        } else {
            rlTitle.setVisibility(View.VISIBLE);
        }

        showImageView(url);
        image.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                finish();
            }

            @Override
            public void onOutsidePhotoTap() {
                finish();
            }
        });
    }

    private void showImageView(String filepath) {
        //需要上传图片的原始的宽高
        if (filepath != null) {
            Glide.with(this).load(filepath).into(image);
        }
    }

    @OnClick({R.id.tv_back, R.id.iv_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.iv_more:
                showPhotoDialog();
                break;
            default:
                break;
        }
    }


    /**
     * 图片选择对话框
     */
    private void showPhotoDialog() {
        final Dialog pickDialog = new Dialog(this, R.style.Custom_Progress);
        pickDialog.setContentView(R.layout.dialog_pic_choose);

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog.getWindow();
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = (int) (display.getWidth()); //设置宽度

        pickDialog.getWindow().setAttributes(lp);

        TextView camera = (TextView) pickDialog.findViewById(R.id.chos_camera);
        TextView picLib = (TextView) pickDialog.findViewById(R.id.pic_lib);
        TextView cancel = (TextView) pickDialog.findViewById(R.id.btn_cancel);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
                pickDialog.dismiss();
            }
        });

        picLib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPicFrom();
                pickDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDialog.dismiss();
            }
        });

        pickDialog.show();
    }

    private void openCamera() {
        //start开启功能，TYPE_PICK_MEDIA - 选择图片/视频/音频 TYPE_TAKE_PICTURE - 拍照
        if (flag == 1) {
            PictureSelector.create(ShowBigImageActivity.this)
                    .openCamera(PictureMimeType.ofImage())// 单独拍照，也可录像或也可音频 看你传入的类型是图片or视频
                    .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles
                    .maxSelectNum(1)// 最大图片选择数量
                    .minSelectNum(0)// 最小选择数量
                    .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                    .previewImage(false)// 是否可预览图片
                    .previewVideo(false)// 是否可预览视频
                    .enablePreviewAudio(false) // 是否可播放音频
                    .isCamera(true)// 是否显示拍照按钮
                    .enableCrop(true)// 是否裁剪
                    .compress(true)// 是否压缩
                    .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                    .isGif(false)// 是否显示gif图片
                    .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                    .circleDimmedLayer(true)// 是否圆形裁剪
                    .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                    .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                    .openClickSound(false)// 是否开启点击声音
                    .selectionMedia(selectList)// 是否传入已选图片
                    .previewEggs(false)//预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                    //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                    .cropCompressQuality(90)// 裁剪压缩质量 默认为100
                    .minimumCompressSize(100)// 小于100kb的图片不压缩
//                    .cropWH(1,1)// 裁剪宽高比，设置如果大于图片本身宽高则无效
                    //.rotateEnabled() // 裁剪是否可旋转图片
                    .scaleEnabled(true)// 裁剪是否可放大缩小图片
                    //.videoQuality()// 视频录制质量 0 or 1
                    //.videoSecond()////显示多少秒以内的视频or音频也可适用
                    .forResult(PictureConfig.REQUEST_CAMERA);//结果回调onActivityResult code
        } else {
            PictureSelector.create(ShowBigImageActivity.this)
                    .openCamera(PictureMimeType.ofImage())// 单独拍照，也可录像或也可音频 看你传入的类型是图片or视频
                    .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles
                    .maxSelectNum(1)// 最大图片选择数量
                    .minSelectNum(0)// 最小选择数量
                    .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                    .previewImage(false)// 是否可预览图片
                    .previewVideo(false)// 是否可预览视频
                    .enablePreviewAudio(false) // 是否可播放音频
                    .isCamera(true)// 是否显示拍照按钮
                    .enableCrop(true)// 是否裁剪
                    .compress(true)// 是否压缩
                    .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    .withAspectRatio(4, 3)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                    .isGif(false)// 是否显示gif图片
                    .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                    .circleDimmedLayer(false)// 是否圆形裁剪
                    .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                    .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                    .openClickSound(false)// 是否开启点击声音
                    .selectionMedia(selectList)// 是否传入已选图片
                    .previewEggs(false)//预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                    //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                    .cropCompressQuality(90)// 裁剪压缩质量 默认为100
                    .minimumCompressSize(100)// 小于100kb的图片不压缩
//                    .cropWH(4,3)// 裁剪宽高比，设置如果大于图片本身宽高则无效
                    //.rotateEnabled() // 裁剪是否可旋转图片
                    .scaleEnabled(true)// 裁剪是否可放大缩小图片
                    //.videoQuality()// 视频录制质量 0 or 1
                    //.videoSecond()////显示多少秒以内的视频or音频也可适用
                    .forResult(PictureConfig.REQUEST_CAMERA);//结果回调onActivityResult code
        }
    }

    private void getPicFrom() {
        //start开启功能，TYPE_PICK_MEDIA - 选择图片/视频/音频 TYPE_TAKE_PICTURE - 拍照
        if (flag == 1) {
            PictureSelector.create(ShowBigImageActivity.this)
                    .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                    .maxSelectNum(1)// 最大图片选择数量
                    .minSelectNum(0)// 最小选择数量
                    .imageSpanCount(4)// 每行显示个数
                    .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                    .previewImage(false)// 是否可预览图片
                    .previewVideo(false)// 是否可预览视频
                    .enablePreviewAudio(false) // 是否可播放音频
                    .isCamera(false)// 是否显示拍照按钮
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                    //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                    .enableCrop(true)// 是否裁剪
                    .compress(true)// 是否压缩
                    .synOrAsy(true)//同步true或异步false 压缩 默认同步
                    //.compressSavePath(getPath())//压缩图片保存地址
                    //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                    .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                    .isGif(false)// 是否显示gif图片
                    .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                    .circleDimmedLayer(true)// 是否圆形裁剪
                    .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                    .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                    .openClickSound(false)// 是否开启点击声音
                    .selectionMedia(selectList)// 是否传入已选图片
//                        .videoMaxSecond(15)
//                        .videoMinSecond(10)
                    .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                    .cropCompressQuality(90)// 裁剪压缩质量 默认100
                    .minimumCompressSize(100)// 小于100kb的图片不压缩
//                    .cropWH(1,1)// 裁剪宽高比，设置如果大于图片本身宽高则无效
                    //.rotateEnabled() // 裁剪是否可旋转图片
                    .scaleEnabled(true)// 裁剪是否可放大缩小图片
                    //.videoQuality()// 视频录制质量 0 or 1
                    //.videoSecond()//显示多少秒以内的视频or音频也可适用
                    //.recordVideoSecond()//录制视频秒数 默认60s
                    .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
        } else {
            PictureSelector.create(ShowBigImageActivity.this)
                    .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                    .maxSelectNum(1)// 最大图片选择数量
                    .minSelectNum(0)// 最小选择数量
                    .imageSpanCount(4)// 每行显示个数
                    .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                    .previewImage(false)// 是否可预览图片
                    .previewVideo(false)// 是否可预览视频
                    .enablePreviewAudio(false) // 是否可播放音频
                    .isCamera(false)// 是否显示拍照按钮
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                    //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                    .enableCrop(true)// 是否裁剪
                    .compress(true)// 是否压缩
                    .synOrAsy(true)//同步true或异步false 压缩 默认同步
                    //.compressSavePath(getPath())//压缩图片保存地址
                    //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                    .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    .withAspectRatio(4, 3)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                    .isGif(false)// 是否显示gif图片
                    .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                    .circleDimmedLayer(false)// 是否圆形裁剪
                    .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                    .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                    .openClickSound(false)// 是否开启点击声音
                    .selectionMedia(selectList)// 是否传入已选图片
//                        .videoMaxSecond(15)
//                        .videoMinSecond(10)
                    .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                    .cropCompressQuality(90)// 裁剪压缩质量 默认100
                    .minimumCompressSize(100)// 小于100kb的图片不压缩
//                    .cropWH(4,3)// 裁剪宽高比，设置如果大于图片本身宽高则无效
                    //.rotateEnabled() // 裁剪是否可旋转图片
                    .scaleEnabled(true)// 裁剪是否可放大缩小图片
                    //.videoQuality()// 视频录制质量 0 or 1
                    //.videoSecond()//显示多少秒以内的视频or音频也可适用
                    //.recordVideoSecond()//录制视频秒数 默认60s
                    .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PictureConfig.CHOOSE_REQUEST:
                // 图片选择结果回调
                selectList = PictureSelector.obtainMultipleResult(data);
                // 例如 LocalMedia 里面返回三种path
                // 1.media.getPath(); 为原图path
                // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                upLoadImages();
                break;
            case PictureConfig.REQUEST_CAMERA:
                // 图片选择结果回调
                selectList = PictureSelector.obtainMultipleResult(data);
                upLoadImages();
                // 例如 LocalMedia 里面返回三种path
                // 1.media.getPath(); 为原图path
                // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                break;
            default:
                break;
        }
    }

    private void upLoadImages() {
        if (selectList.size() == 0) {
            return;
        }
        String imagePath = selectList.get(0).getCompressPath();
        String ext = imagePath.substring(imagePath.lastIndexOf(".") + 1).toLowerCase();
        if (ext.equals("jpeg")) {
            ext = "jpg";
        }
        if (flag == 1) {
            getCosSign(1, ext, imagePath);
        } else {
            getCosSign(2, ext, imagePath);
        }
    }

    private void getCosSign(int type, String ext, final String path) {
        showLoading();
        new CompositeDisposable().add(mDataManager
                .doGetCosSignApiCall(new GetCosSignRequest(type, ext))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetCosSignResponse>() {
                    @Override
                    public void accept(@NonNull GetCosSignResponse response) throws Exception {
                        getCosSucc(response, path);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        hideLoading();
                        onToast("上传失败！", Gravity.CENTER, 0, 0);
                    }
                }));
    }

    private void getCosSucc(GetCosSignResponse response, String path) {
        if (response.getCode() == 0) {
            String sign = response.getCosSignData().getSign();
            final String imgName = response.getCosSignData().getImgName();
            try {
                sign = URLDecoder.decode(sign, "UTF-8");
                AppUtils.doUploadCover(ShowBigImageActivity.this, path, sign, imgName, new IUploadTaskListener() {
                    @Override
                    public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
                        PutObjectResult result = (PutObjectResult) cosResult;
                        if (result != null) {
                            //TCUtils.showPicWithUrl(PersonalInfoActivity.this, personHeadImg,result.access_url,R.drawable.face);
                            Bundle b = new Bundle();
                            b.putInt("code", 1000);
                            b.putString("imgName", imgName);
                            dealHander(b);
                        }
                    }

                    @Override
                    public void onFailed(COSRequest COSRequest, final COSResult cosResult) {
                        Bundle b = new Bundle();
                        b.putInt("code", 1001);
                        dealHander(b);
                        Log.w("TEST", "上传出错： ret =" + cosResult.code + "; msg =" + cosResult.msg);
                    }

                    @Override
                    public void onProgress(COSRequest cosRequest, final long currentSize, final long totalSize) {
                        float progress = (float) currentSize / totalSize;
                        progress = progress * 100;
                        Log.w("TEST", "进度：  " + (int) progress + "%");
                    }

                    @Override
                    public void onCancel(COSRequest cosRequest, COSResult cosResult) {

                    }
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            AppLogger.e(response.getErrMsg());
        }
        hideLoading();
    }

    private void dealHander(final Bundle bundle) {
        Observable.create(new ObservableOnSubscribe<Bundle>() {
            @Override
            public void subscribe(ObservableEmitter<Bundle> emitter) throws Exception {
                emitter.onNext(bundle);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<Bundle>() {
                    @Override
                    public void accept(@NonNull Bundle bundle) throws Exception {
                        int code = bundle.getInt("code");
                        switch (code) {
                            case 1000:
                                if (flag == 1) {
                                    String url = bundle.getString("imgName");
                                    final String headUrl = AppConstants.cosHeadUrl + url;
                                    AppLogger.e(headUrl);
                                    modifyProfileHeadUrl(headUrl);
                                    clearImages();
                                } else if (flag == 2) {
                                    String url = bundle.getString("imgName");
                                    final String headUrl = AppConstants.cosHeadUrl + url;
                                    AppLogger.e(headUrl);
                                    modifyProfileLiveCover(headUrl);
                                    clearImages();
                                }
                                break;
                            case 1001:
                                hideLoading();
                                onToast("上传出错,请重新上传");
                                break;
                            default:
                                break;

                        }
                    }
                });
    }

    private void modifyProfileHeadUrl(final String headUrl) {
        mDataManager.doModifyProfileLiveCoverApiCall(new ModifyProfileLiveCoverRequest("HeadUrl", headUrl))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ModifyProfileLiveCoverResponse>() {
                    @Override
                    public void accept(@NonNull ModifyProfileLiveCoverResponse modifyProfileLiveCoverResponse) throws Exception {
                        if (modifyProfileLiveCoverResponse.getCode() == 0) {
                            LoadUserDataBaseResponse.UserData userData = mDataManager.getUserData();
                            userData.setHeadUrl(headUrl);
                            mDataManager.saveUserData(userData);
                            RxBus.get().send(RxBusCode.RX_BUS_NOTICE_CODE_MODIFY_HEAD, headUrl);
                            onToast("上传成功", Gravity.CENTER, 0, 0);
                            finish();
                        } else {
                            onToast("上传失败", Gravity.CENTER, 0, 0);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        onToast("上传失败", Gravity.CENTER, 0, 0);
                    }
                });
    }

    private void modifyProfileLiveCover(final String coverUrl) {
        mDataManager.doModifyProfileLiveCoverApiCall(new ModifyProfileLiveCoverRequest("LiveCover", coverUrl))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ModifyProfileLiveCoverResponse>() {
                    @Override
                    public void accept(@NonNull ModifyProfileLiveCoverResponse modifyProfileLiveCoverResponse) throws Exception {
                        if (modifyProfileLiveCoverResponse.getCode() == 0) {
                            LoadUserDataBaseResponse.UserData userData = mDataManager.getUserData();
                            userData.setLiveCover(coverUrl);
                            mDataManager.saveUserData(userData);
                            RxBus.get().send(RxBusCode.RX_BUS_NOTICE_CODE_MODIFY_COVER, coverUrl);
                            onToast("上传成功", Gravity.CENTER, 0, 0);
                            finish();
                        } else {
                            onToast("上传失败", Gravity.CENTER, 0, 0);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        onToast("上传失败", Gravity.CENTER, 0, 0);
                    }
                });
    }

    private void clearImages() {
// 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    PictureFileUtils.deleteCacheDirFile(ShowBigImageActivity.this);
                } else {
                    Toast.makeText(ShowBigImageActivity.this,
                            getString(R.string.picture_jurisdiction), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }

}
