package cn.feihutv.zhibofeihu.ui.me.dynamic;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.tencent.cos.model.COSRequest;
import com.tencent.cos.model.COSResult;
import com.tencent.cos.model.PutObjectResult;
import com.tencent.cos.task.listener.IUploadTaskListener;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetCosSignResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.PostFeedRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.PostFeedResponse;
import cn.feihutv.zhibofeihu.rxbus.RxBus;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.live.ReportActivity;
import cn.feihutv.zhibofeihu.ui.live.adapter.GridImageAdapter;
import cn.feihutv.zhibofeihu.ui.widget.Bimp;
import cn.feihutv.zhibofeihu.ui.widget.LoadingView;
import cn.feihutv.zhibofeihu.ui.widget.flashview.FlashDataParser;
import cn.feihutv.zhibofeihu.utils.AppConstants;
import cn.feihutv.zhibofeihu.utils.AppUtils;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.FastBlur;
import cn.feihutv.zhibofeihu.utils.FileUtils;
import cn.feihutv.zhibofeihu.utils.FullyGridLayoutManager;
import cn.feihutv.zhibofeihu.utils.PhotoUtils;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.graphics.BitmapFactory.decodeFile;


/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/
 *     desc   : 界面绘制
 *     version: 1.0
 * </pre>
 */
public class SendDynamicActivity extends BaseActivity implements SendDynamicMvpView {

    @Inject
    SendDynamicMvpPresenter<SendDynamicMvpView> mPresenter;

    private static final int TAKE_PICTURE = 0x000001;


    private List<LocalMedia> selectList = new ArrayList<>();//选择的图片集合

    @BindView(R.id.menu_send)
    TextView menu_send;

    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.location)
    TextView locationTxt;
    @BindView(R.id.dy_rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.switch_button)
    ImageView switchButton;

    @BindView(R.id.wait_proBar)
    ProgressBar wait_proBar;

    private GridImageAdapter mAdapter;

    public static List<Activity> activityList = new ArrayList<Activity>();
    private LocationManager locationManager;//位置服务
    private Location location;
    private String provider;//位置提供器
    private String locationNow = "";
    private List<String> imgUrls = new ArrayList<>();
    private int count = 0;
    private boolean isClick = false;
    private LoadingView loadingView;
    private Uri imageUri;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_send_dynamic;
    }


    @Override
    protected void initWidget() {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this));

        //Presenter调用初始化
        mPresenter.onAttach(SendDynamicActivity.this);

        init();

    }


    @Override
    protected void initWidget(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            String context = savedInstanceState.getString("context", "");
            savedInstanceState.getString(MediaStore.EXTRA_OUTPUT, imageUri.getPath());
            editText.setText(context);
        }
    }

    void init() {
        activityList.add(this);

        FullyGridLayoutManager manager = new FullyGridLayoutManager(SendDynamicActivity.this, 4, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new GridImageAdapter(SendDynamicActivity.this, new GridImageAdapter.onAddPicClickListener() {
            @Override
            public void onAddPicClick() {
                showPopDialog();
            }
        });
        mAdapter.setList(selectList);
        mAdapter.setSelectMax(9);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            //PictureSelector.create(MainActivity.this).externalPicturePreview(position, "/custom_file", selectList);
                            PictureSelector.create(SendDynamicActivity.this).externalPicturePreview(position, selectList);
                            break;
                        default:
                            break;
                    }
                }
            }
        });

        loadingView = new LoadingView();

        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isClick) {
                    //判断是否有权限
                    isClick = true;
                    switchButton.setSelected(true);
                    if (ContextCompat.checkSelfPermission(SendDynamicActivity.this,
                            Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        //权限还没有授予，需要在这里写申请权限的代码
                        ActivityCompat.requestPermissions(SendDynamicActivity.this,
                                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                1);
                    } else {
                        dingWei();
                    }
                } else {
                    switchButton.setSelected(false);
                    isClick = false;
                    locationTxt.setText("");
                    locationTxt.setHint("所在位置");
                }
            }
        });

    }


    private void dingWei() {

        //权限已经被授予，在这里直接写要执行的相应方法即可
        //更改头像
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);//获得位置服务
        provider = judgeProvider(locationManager);
        if (provider != null) {//有位置提供器的情况
            //为了压制getLastKnownLocation方法的警告
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                mPresenter.getLocation(location);//得到当前经纬度并开启线程去反向地理编码
                wait_proBar.setVisibility(View.VISIBLE);
            } else {
                //暂时无法获得当前位置
                locationNow = "";
            }
        } else {
            //不存在位置提供器的情况
        }
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
            Toast.makeText(this, "没有可用的位置提供器，请手动打开定位权限", Toast.LENGTH_SHORT).show();
        }
        return null;
    }


    // 获取定位
    @Override
    public void onGetLocationResp(String location) {
        wait_proBar.setVisibility(View.GONE);
        locationNow = location;
        locationTxt.setText(locationNow.replace("中国", ""));
    }

    private void postDynamic() {
        PostFeedRequest request = new PostFeedRequest();
        request.setLocation(locationTxt.getText().toString());
        request.setContent(editText.getText().toString());
        for (int i = 0; i < imgUrls.size(); i++) {
            switch (i) {
                case 0:
                    request.setImg1(imgUrls.get(i));
                    break;
                case 1:
                    request.setImg2(imgUrls.get(i));
                    break;
                case 2:
                    request.setImg3(imgUrls.get(i));
                    break;
                case 3:
                    request.setImg4(imgUrls.get(i));
                    break;
                case 4:
                    request.setImg5(imgUrls.get(i));
                    break;
                case 5:
                    request.setImg6(imgUrls.get(i));
                    break;
                case 6:
                    request.setImg7(imgUrls.get(i));
                    break;
                case 7:
                    request.setImg8(imgUrls.get(i));
                    break;
                case 8:
                    request.setImg9(imgUrls.get(i));
                    break;
                default:
                    break;
            }
        }
        mPresenter.postFeed(request);
    }

    @Override
    public void onPostFeedResp(PostFeedResponse response) {
        hideLoading();
        if (response.getCode() == 0) {
            showSuccessDialog();
            SharePreferenceUtil.saveSeesion(SendDynamicActivity.this, "content", "");
        } else {
            if(response.getCode()==4804){
                onToast("上传的图片尺寸过大！");
            }else if(response.getCode()==4803){
                onToast("上传的文件过大！");
            } else{
                onToast("未知错误！");
            }

        }

    }

    @Override
    public void notifyCosSignResponce(GetCosSignResponse response, final String path,final String oldPath) {
        if (response.getCode() == 0) {
            String sign = response.getCosSignData().getSign();
            final String imgName = response.getCosSignData().getImgName();
            String ext=imgName.substring(imgName.lastIndexOf(".")+1);
            final String smallImgName=imgName.substring(0,imgName.lastIndexOf("."))+"_thumb."+ext;
            try {
                sign = URLDecoder.decode(sign, "UTF-8");
                final  String signT=sign;
                AppUtils.doUploadCover(SendDynamicActivity.this, path, sign, imgName, new IUploadTaskListener() {
                    @Override
                    public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
                        PutObjectResult result = (PutObjectResult) cosResult;
                        if(result!=null) {
                            Bundle b = new Bundle();
                            b.putString("imgName", imgName);
                            b.putInt("code", 1000);
                            dealHander(b);
                        }
//                        if (result != null) {
//
//                            Bitmap bitmap=decodeSampleBitmapFromPath(oldPath,250,250);
//
//                            String ext = oldPath.substring(oldPath.lastIndexOf(".") + 1).toLowerCase();
//
//                            if(ext.toLowerCase().equals("webp")){
//                                FHUtils.showToast("不支持此类图片格式！");
//                                return;
//                            }
//                            String newPath=saveBitmap(SendDynamicActivity.this,bitmap, ext);
//                            AppUtils.doUploadCover(SendDynamicActivity.this, newPath, signT, smallImgName, new IUploadTaskListener() {
//                                @Override
//                                public void onProgress(COSRequest cosRequest, long l, long l1) {
//
//                                }
//
//                                @Override
//                                public void onCancel(COSRequest cosRequest, COSResult cosResult) {
//
//                                }
//
//                                @Override
//                                public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
//
//                                }
//
//                                @Override
//                                public void onFailed(COSRequest cosRequest, COSResult cosResult) {
//                                    Bundle b = new Bundle();
//                                    b.putInt("code", 1001);
//                                    dealHander(b);
//                                }
//                            });
//                        }
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
                        Log.w("TEST", "cancel");
                    }
                });


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            onToast("上传出错,请重新上传");
            hideLoading();
            count = 0;
            PhotoUtils.clearImages(SendDynamicActivity.this);
        }
    }

    private void showPopDialog() {
        final Dialog pickDialog = new Dialog(this, R.style.color_dialog);
        pickDialog.setContentView(R.layout.pop_send);

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog.getWindow();
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.CENTER);
        lp.width = (int) (display.getWidth()); //设置宽度
        pickDialog.setCanceledOnTouchOutside(true);
        pickDialog.getWindow().setAttributes(lp);
        TextView camera = (TextView) pickDialog.findViewById(R.id.take_photo);
        TextView album = (TextView) pickDialog.findViewById(R.id.album);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MobclickAgent.onEvent(SendDynamicActivity.this, "10110");
                if (!android.text.TextUtils.isEmpty(editText.getText())) {
                    SharePreferenceUtil.saveSeesion(SendDynamicActivity.this, "content", editText.getText().toString());
                }
                openCamera();
                pickDialog.dismiss();
            }
        });
        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MobclickAgent.onEvent(SendDynamicActivity.this, "10111");
                if (!android.text.TextUtils.isEmpty(editText.getText())) {
                    SharePreferenceUtil.saveSeesion(SendDynamicActivity.this, "content", editText.getText().toString());
                }

                int hasWriteContactsPermission = ContextCompat.checkSelfPermission(SendDynamicActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED) {
//                    finish();
//                    Intent intent = new Intent(SendDynamicActivity.this,
//                            AlbumActivity.class);
//                    intent.putExtra("maxnum", 9);
//                    startActivity(intent);
                } // 需要弹出dialog让用户手动赋予权限
                else {
                    ActivityCompat.requestPermissions(SendDynamicActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
                }
                pickDialog.dismiss();

                getPicFrom();
            }
        });
        pickDialog.show();
    }

    public Uri getMediaFileUri() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "dy_images");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        //创建Media File
        String timeStamp = String.valueOf(System.currentTimeMillis());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        return Uri.fromFile(mediaFile);
    }

    /*保存界面状态，如果activity意外被系统killed，返回时可以恢复状态值*/
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.putString(MediaStore.EXTRA_OUTPUT, getMediaFileUri().getPath());
            savedInstanceState.putString("context", editText.getText().toString());
            if (imageUri != null) {
                savedInstanceState.putString(MediaStore.EXTRA_OUTPUT, imageUri.getPath());
            }
        }
        super.onSaveInstanceState(savedInstanceState); //实现父类方法 放在最后 防止拍照后无法返回当前activity
    }


    @Override
    protected void onResume() {
        super.onResume();
        editText.setText(SharePreferenceUtil.getSession(SendDynamicActivity.this, "content"));
    }


    //发送点击事件
    @OnClick({R.id.menu_send, R.id.menu_return})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.menu_return:
                SharePreferenceUtil.saveSeesion(SendDynamicActivity.this, "content", "");
                Bimp.tempSelectBitmap.clear();
                for (int i = 0; i < activityList.size(); i++) {
                    if (null != activityList.get(i)) {
                        activityList.get(i).finish();
                    }
                }
                break;
            case R.id.menu_send:
                if (TextUtils.isEmpty(editText.getText()) && selectList.size() == 0) {
                    onToast("说点什么或者分享点图片吧~");
                } else {
                    showLoading();
                    menu_send.setEnabled(false);
                    Bundle bundle = new Bundle();
                    bundle.putInt("code", 1003);
                    dealHander(bundle);
                }
                break;
            default:
                break;
        }


    }

    private void openCamera() {
        PictureSelector.create(SendDynamicActivity.this)
                .openCamera(PictureMimeType.ofImage())// 单独拍照，也可录像或也可音频 看你传入的类型是图片or视频
                .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles
                .maxSelectNum(9)// 最大图片选择数量
                .minSelectNum(0)// 最小选择数量
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                .previewImage(false)// 是否可预览图片
                .previewVideo(false)// 是否可预览视频
                .enablePreviewAudio(false) // 是否可播放音频
                .isCamera(true)// 是否显示拍照按钮
                .enableCrop(false)// 是否裁剪
                .compress(true)// 是否压缩
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
//                .withAspectRatio(4, 3)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
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

    private void getPicFrom() {
        PictureSelector.create(SendDynamicActivity.this)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(9)// 最大图片选择数量
                .minSelectNum(0)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                .previewImage(true)// 是否可预览图片
                .previewVideo(false)// 是否可预览视频
                .enablePreviewAudio(false) // 是否可播放音频
                .isCamera(false)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                .enableCrop(false)// 是否裁剪
                .compress(true)// 是否压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                //.compressSavePath(getPath())//压缩图片保存地址
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
//                .withAspectRatio(4, 3)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                .isGif(false)// 是否显示gif图片
                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽
                .circleDimmedLayer(false)// 是否圆形裁剪
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PictureConfig.CHOOSE_REQUEST:
                if(selectList.size()>0){
                    selectList.clear();
                }
                selectList.addAll(PictureSelector.obtainMultipleResult(data));
                // 例如 LocalMedia 里面返回三种path
                // 1.media.getPath(); 为原图path
                // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
//                upLoadImages();
                mAdapter.setList(selectList);
                mAdapter.notifyDataSetChanged();
                break;
            case PictureConfig.REQUEST_CAMERA:
                // 图片选择结果回调
                selectList.addAll(PictureSelector.obtainMultipleResult(data));
                // 例如 LocalMedia 里面返回三种path
                // 1.media.getPath(); 为原图path
                // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
//                upLoadImages();
                mAdapter.setList(selectList);
                mAdapter.notifyDataSetChanged();
                break;
            case TAKE_PICTURE:
//                if (Bimp.tempSelectBitmap.size() < 9 && resultCode == RESULT_OK) {
//                    String fileName = String.valueOf(System.currentTimeMillis());
//                    Bitmap bm = (Bitmap) data.getExtras().get("data");
//                    FileUtils.saveBitmap(bm, fileName);
//                    ImageItem takePhoto = new ImageItem();
//                    takePhoto.setBitmap(bm);
//                    Bimp.tempSelectBitmap.add(takePhoto);
//                    Bimp.tempSelected.add(1);
//                }
                if (Bimp.tempSelectBitmap.size() < 9 && resultCode == RESULT_OK) {
                    if (data != null) {
                        if (data.hasExtra("data")) {
                            Bitmap bm = data.getParcelableExtra("data");
                            ImageItem takePhoto = new ImageItem();
                            takePhoto.setBitmap(bm);
                            takePhoto.setCapture(true);
                            takePhoto.setImagePath(imageUri.getPath());
                            Bimp.tempSelectBitmap.add(takePhoto);
                            Bimp.tempSelected.add(1);
                        }
                    } else {
                        ImageItem takePhoto = new ImageItem();
                        takePhoto.setCapture(true);
                        takePhoto.setImagePath(imageUri.getPath());
                        Bimp.tempSelectBitmap.add(takePhoto);
                        Bimp.tempSelected.add(1);
                    }
                }
                break;
            default:
                break;
        }
    }


    private void showSuccessDialog() {
        RxBus.get().send(RxBusCode.RX_BUS_NOTICE_CODE_POST_SUCC);
        final Dialog successDialog = new Dialog(this, R.style.floag_dialog);
        successDialog.setContentView(R.layout.pop_success);

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = successDialog.getWindow();
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.CENTER);
        lp.width = (int) (display.getWidth()); //设置宽度
        successDialog.setCanceledOnTouchOutside(true);
        successDialog.getWindow().setAttributes(lp);
        successDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                successDialog.dismiss();
                finish();
            }
        }, 100);
    }


    private Bitmap decodeSampleBitmapFromPath(String path, int reqWidth, int reqHeight) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        decodeFile(path, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap bm = decodeFile(path, options);
        return bm;
    }

    public int calculateInSampleSize(BitmapFactory.Options op, int reqWidth,
                                     int reqHeight) {
        int originalWidth = op.outWidth;
        int originalHeight = op.outHeight;
        int inSampleSize = 1;
        if (originalWidth > reqWidth || originalHeight > reqHeight) {
            int halfWidth = originalWidth / 2;
            int halfHeight = originalHeight / 2;
            inSampleSize *= 2;
            while ((halfWidth > reqWidth)
                    ||(halfHeight > reqHeight)) {
                halfHeight=halfHeight/2;
                halfWidth=halfWidth/2;
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    private  String generateFileName() {
        return UUID.randomUUID().toString();
    }

    public  String saveBitmap(Context context, Bitmap mBitmap,String ext) {
        String savePath;
        String SD_PATH = "/sdcard/dskqxt/pic/";
        String IN_PATH = "/dskqxt/pic/";
        File filePic;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            savePath = SD_PATH;
        } else {
            savePath = context.getApplicationContext().getFilesDir()
                    .getAbsolutePath()
                    + IN_PATH;
        }
        try {
            filePic = new File(savePath + generateFileName() + "."+ext);
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            if(ext.equals("png")){
                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            }else {
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            }
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return filePic.getAbsolutePath();
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
                            case 1003:
                                showLoading();
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0); //强制隐藏键盘
                                if (selectList.size() > 0) {
                                    LocalMedia item = selectList.get(count);

                                    String imagePath = item.getCompressPath();
                                    String oldPath=item.getCompressPath();
                                    Bitmap bitmap=decodeSampleBitmapFromPath(imagePath,1000,1000);

                                    String ext = imagePath.substring(imagePath.lastIndexOf(".") + 1).toLowerCase();

                                    if(ext.toLowerCase().equals("webp")){
                                        FHUtils.showToast("不支持此类图片格式！");
                                        return;
                                    }
                                     imagePath=saveBitmap(SendDynamicActivity.this,bitmap, ext);
                                    if (ext.equals("jpeg")) {
                                        ext = "jpg";
                                    }
                                    ReUploadPicTask uploadPicTask = new ReUploadPicTask(SendDynamicActivity.this);
                                    uploadPicTask.execute(ext, imagePath,oldPath);
                                } else {
                                    postDynamic();

                                }
                                break;
                            case 1000:
                                String url = bundle.getString("imgName");
                                final String picUrl = AppConstants.cosHeadUrl + url;
                                Log.e("picUrl", picUrl);
                                imgUrls.add(picUrl);
                                count++;
                                if (count == selectList.size()) {
                                    postDynamic();
                                    count = 0;
                                } else {
                                    LocalMedia item = selectList.get(count);
                                    String imagePath = item.getCompressPath();
                                    String oldPath=item.getCompressPath();
                                    Bitmap bitmap=decodeSampleBitmapFromPath(imagePath,1000,1000);
                                    String ext = imagePath.substring(imagePath.lastIndexOf(".") + 1).toLowerCase();
                                    if(ext.toLowerCase().equals("webp")){
                                        FHUtils.showToast("不支持此类图片格式！");
                                        return;
                                    }
                                    imagePath=saveBitmap(SendDynamicActivity.this,bitmap, ext);
                                    if (ext.equals("jpeg")) {
                                        ext = "jpg";
                                    }
                                    ReUploadPicTask uploadPicTask = new ReUploadPicTask(SendDynamicActivity.this);
                                    uploadPicTask.execute(ext, imagePath,oldPath);
                                }
                                break;
                            case 1001:
                                onToast("上传出错,请重新上传");
                                hideLoading();
                                count = 0;
                                PhotoUtils.clearImages(SendDynamicActivity.this);
                                break;
                            default:
                                break;
                        }
                    }
                });
    }


    @Override
    protected void onDestroy() {
        //销毁之前必须调用
        mPresenter.onDetach();
        String savePath;
        String SD_PATH = "/sdcard/dskqxt/pic/";
        String IN_PATH = "/dskqxt/pic/";
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            savePath = SD_PATH;
        } else {
            savePath = getFilesDir().getAbsolutePath() + IN_PATH;
        }
        FlashDataParser.deleteFile(new File(savePath));
        super.onDestroy();
    }


    public class ReUploadPicTask extends AsyncTask<String, Void, String> {

        private Activity context = null;

        public ReUploadPicTask(Activity ctx) {
            this.context = ctx;
        }

        @Override
        protected void onPostExecute(String result) {

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = upLoadPic(params[0], params[1],params[2]);
            return result;
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }

        private String upLoadPic(String ext, final String path,String oldPAth) {
            mPresenter.getCosSign(3, ext, path,oldPAth);
            return "";
        }
    }
}
