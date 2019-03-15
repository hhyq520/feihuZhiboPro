package cn.feihutv.zhibofeihu.ui.widget;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetCosSignRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetCosSignResponse;
import cn.feihutv.zhibofeihu.ui.live.ReportActivity;
import cn.feihutv.zhibofeihu.ui.live.adapter.GridImageAdapter;
import cn.feihutv.zhibofeihu.ui.live.renzheng.IdentityInfoActivity;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.AppUtils;
import cn.feihutv.zhibofeihu.utils.DeviceInfoUtil;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.FullyGridLayoutManager;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by chenliwen on 2017/6/22 13:48.
 * 佛祖保佑，永无BUG
 */

public class BaskDialogFragment extends DialogFragment implements View.OnClickListener {

    private static final int TAKE_PICTURE = 0x000001;
    ImageView backBtn;
    TextView send;
    EditText editText;
    TextView locationTxt;
    RecyclerView recycle;
    ImageView switchButton;
    ProgressBar waitView;
    private X5WebView mWebView;
    private UploadPicTask uploadPicTask;
    public void setmWebView(X5WebView mWebView) {
        this.mWebView = mWebView;
    }
    private List<LocalMedia> selectList = new ArrayList<>();
    public static List<Activity> activityList = new ArrayList<Activity>();
    private LocationManager locationManager;//位置服务
    private Location location;
    private String provider;//位置提供器
    private String locationNow = "";
    private List<String> imgurls = new ArrayList<>();
    private String cosHeadUrl = "https://img.feihutv.cn//hubao/feed";
    private int count = 0;
    private boolean isClick = false;
    private String hubaoIssueNo;
    private String device;
    GridImageAdapter adapter;
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
                    public void accept(@io.reactivex.annotations.NonNull Bundle bundle) throws Exception {
                        int code = bundle.getInt("code");
                        switch (code) {
                            case 1003:
                                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0); //强制隐藏键盘
                                if (selectList.size() > 0) {
                                    LocalMedia item = selectList.get(count);
                                    String imagePath = item.getCompressPath();
                                    String ext = imagePath.substring(imagePath.lastIndexOf(".") + 1).toLowerCase();
                                    if (ext.equals("jpeg")) {
                                        ext = "jpg";
                                    }
                                  UploadPicTask uploadPicTask = new UploadPicTask(getActivity());
                                    uploadPicTask.execute(ext, imagePath);
                                } else {
                                    sendFeed();
                                }
                                break;
                            case 1000:
                                String url = bundle.getString("imgName");
                                final String picUrl = cosHeadUrl + url;
                                Log.e("picUrl", picUrl);
                                imgurls.add(picUrl);
                                count++;
                                if (count == selectList.size()) {
                                    sendFeed();
                                    count = 0;
                                } else {
                                    LocalMedia item = selectList.get(count);
                                    String imagePath = item.getCompressPath();
                                    String ext = imagePath.substring(imagePath.lastIndexOf(".") + 1).toLowerCase();
                                    if (ext.equals("jpeg")) {
                                        ext = "jpg";
                                    }
                                    UploadPicTask uploadPicTask = new UploadPicTask(getActivity());
                                    uploadPicTask.execute(ext, imagePath);
                                }
                                break;
                            case 1001:
                                FHUtils.showToast("上传出错,请重新上传");
//                                hideLoading();
                                count = 0;
                                clearImages();
                                break;
                            default:
                                break;
                        }
                    }
                });
    }


    public interface OnCancelDialogListener {
        void cancel();
    }

    private OnCancelDialogListener mListener;

    public void setOnCancelDialogListener(OnCancelDialogListener listener) {
        mListener = listener;
    }
    public void setHubaoIssueNo(String hubaoIssueNo) {
        this.hubaoIssueNo = hubaoIssueNo;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog mDetailDialog = new Dialog(getActivity(), R.style.dialog);
        mDetailDialog.setContentView(R.layout.activity_send_hubao);
//        mDetailDialog.setCancelable(false);
        mDetailDialog.setCanceledOnTouchOutside(true);
        send = (TextView) mDetailDialog.findViewById(R.id.send);
        backBtn = (ImageView) mDetailDialog.findViewById(R.id.back_btn);
        backBtn.setOnClickListener(this);
        send.setOnClickListener(this);
        editText = (EditText) mDetailDialog.findViewById(R.id.editText);
        locationTxt = (TextView) mDetailDialog.findViewById(R.id.location);
         recycle = (RecyclerView) mDetailDialog.findViewById(R.id.recycle);
        switchButton = (ImageView) mDetailDialog.findViewById(R.id.switch_button);
        init();
        return mDetailDialog;
    }

    protected void init() {
        device = DeviceInfoUtil.getPhoneBrand();
        activityList.add(getActivity());

        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isClick) {
                    //判断是否有权限
                    isClick = true;
                    switchButton.setSelected(true);
                    if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                        //权限还没有授予，需要在这里写申请权限的代码
                        ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            1);
                    } else {
                        dingWei();
                    }
                } else {
                    switchButton.setSelected(false);
                    isClick = false;
                    locationTxt.setText("");
                }
            }
        });
        FullyGridLayoutManager manager = new FullyGridLayoutManager(getContext(), 4, GridLayoutManager.VERTICAL, false);
        recycle.setLayoutManager(manager);
        adapter = new GridImageAdapter(getContext(), onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(3);
        recycle.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
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
                            PictureSelector.create(getActivity()).externalPicturePreview(position, selectList);
                            break;
                    }
                }
            }
        });
        clearImages();
    }


    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            // 进入相册 以下是例子：不需要的api可以不写
            PictureSelector.create(getActivity())
                    .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                    .maxSelectNum(3)// 最大图片选择数量
                    .minSelectNum(0)// 最小选择数量
                    .imageSpanCount(4)// 每行显示个数
                    .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                    .previewImage(true)// 是否可预览图片
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
                    //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                    //.rotateEnabled() // 裁剪是否可旋转图片
                    //.scaleEnabled()// 裁剪是否可放大缩小图片
                    //.videoQuality()// 视频录制质量 0 or 1
                    //.videoSecond()//显示多少秒以内的视频or音频也可适用
                    //.recordVideoSecond()//录制视频秒数 默认60s
                    .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
        }
    };

    private void dingWei() {
        //权限已经被授予，在这里直接写要执行的相应方法即可
        //更改头像
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);//获得位置服务
        provider = judgeProvider(locationManager);
        if (provider != null) {//有位置提供器的情况
            //为了压制getLastKnownLocation方法的警告
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            Toast.makeText(getActivity(), "没有可用的位置提供器，请手动打开定位权限", Toast.LENGTH_SHORT).show();
        }
        return null;
    }


    /**
     * 得到当前经纬度并开启线程去反向地理编码
     */
    public void getLocation(final Location location) {
        String latitude = location.getLatitude() + "";
        String longitude = location.getLongitude() + "";
        String url = "http://maps.google.cn/maps/api/geocode/json?latlng=" + latitude + "," + longitude + "&sensor=true,language=zh-CN";
//
//        FHHttp.getRequest(url, new Callback() {
//
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                try {
//                    JSONObject jsonObject = new JSONObject(response.body().string());
//                    JSONArray array = jsonObject.getJSONArray("results");
//                    TypeToken<List<LocationEntity>> typeToken = new TypeToken<List<LocationEntity>>() {
//                    };
//                    List<LocationEntity> list = new Gson().fromJson(array.toString(), typeToken.getType());
//                    for (final LocationEntity locationEntity : list) {
//                        if (locationEntity.getTypes().size() == 2 && locationEntity.getTypes().get(0).equals("locality") && locationEntity.getTypes().get(1).equals("political")) {
//                            getActivity().runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    locationNow = locationEntity.getFormatted_address();
//                                    locationTxt.setText(locationNow.replace("中国", ""));
//                                }
//                            });
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }

    private void showPopDialog() {
        final Dialog pickDialog = new Dialog(getActivity(), R.style.floag_dialog);
        pickDialog.setContentView(R.layout.pop_send);

        WindowManager windowManager = getActivity().getWindowManager();
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
                openCamera();
                pickDialog.dismiss();
            }
        });
        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upLoad();
                pickDialog.dismiss();
            }
        });
        pickDialog.show();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PictureConfig.REQUEST_CAMERA:
            case PictureConfig.CHOOSE_REQUEST:
                // 图片选择结果回调
                selectList = PictureSelector.obtainMultipleResult(data);
                // 例如 LocalMedia 里面返回三种path
                // 1.media.getPath(); 为原图path
                // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                break;
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                if( Bimp.tempSelectBitmap.size()>0) {
                    Bimp.tempSelectBitmap.clear();
                }
                if (imgurls.size() > 0) {
                    imgurls.clear();
                }
                dismiss();
                break;
            case R.id.send:
                if (Bimp.tempSelectBitmap.size() == 0) {
                    FHUtils.showToast("至少选择一张图片");
                } else {
                    send.setEnabled(false);
                    Bundle bundle = new Bundle();
                    bundle.putInt("code", 1003);
                    dealHander(bundle);
                }
                break;
        }
    }


    private void sendFeed() {
        StringBuilder strbuilder = new StringBuilder();
        for (int i = 0; i < imgurls.size(); i++) {
            if (i > 0) {
                strbuilder.append(",");
            }
            strbuilder.append(imgurls.get(i).substring(imgurls.get(i).lastIndexOf("/") + 1));
        }
//        ServerAPI.hubaoFeed(hubaoIssueNo, device, locationNow, editText.getText().toString().trim(), strbuilder.toString(), new IFHCallBack() {
//            @Override
//            public void cbBlock(FHResponse resp) {
//                if (resp.success) {
//                    showSuccessDialog();
//                    editText.setText("");
//                    send.setEnabled(false);
//
//                } else {
//                    Log.e("hubaoFeed", resp.errMsg);
//                    imgurls.clear();
//                }
//                waitView.setVisibility(View.GONE);
//            }
//        });
        AppLogger.e(imgurls.toString());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if( Bimp.tempSelectBitmap.size()>0) {
            Bimp.tempSelectBitmap.clear();
        }
        if (imgurls.size() > 0) {
            imgurls.clear();
        }
        if (uploadPicTask != null && uploadPicTask.getStatus() != AsyncTask.Status.FINISHED)
            uploadPicTask.cancel(true);
        super.onDismiss(dialog);
    }

    private void showSuccessDialog() {
        final Dialog successDialog = new Dialog(getActivity(), R.style.floag_dialog);
        successDialog.setContentView(R.layout.pop_success);
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = successDialog.getWindow();
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.CENTER);
        lp.width = (int) (display.getWidth()); //设置宽度
        successDialog.setCanceledOnTouchOutside(true);
        successDialog.getWindow().setAttributes(lp);
        successDialog.show();
        if(mListener!=null){
            if( Bimp.tempSelectBitmap.size()>0) {
                Bimp.tempSelectBitmap.clear();
            }
            if (imgurls.size() > 0) {
                imgurls.clear();
            }
            successDialog.dismiss();
            dismiss();
            mListener.cancel();
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if( Bimp.tempSelectBitmap.size()>0) {
            Bimp.tempSelectBitmap.clear();
        }
        if (imgurls.size() > 0) {
            imgurls.clear();
        }
    }


    public class UploadPicTask extends AsyncTask<String, Void, String> {

        private Activity context = null;

        public UploadPicTask(Activity ctx) {
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
            String result = upLoadPic(params[0], params[1]);
            return result;
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }

        private String upLoadPic(String ext, final String path) {
            new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                    .doGetCosSignApiCall(new GetCosSignRequest(5,ext))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<GetCosSignResponse>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull GetCosSignResponse response) throws Exception {
                            if (response.getCode() == 0) {
                                String sign = response.getCosSignData().getSign();
                                final String imgName = response.getCosSignData().getImgName();
                                try {
                                    sign = URLDecoder.decode(sign, "UTF-8");
                                    AppUtils.doUploadCover(getActivity(), path, sign, imgName, new IUploadTaskListener() {
                                        @Override
                                        public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
                                            PutObjectResult result = (PutObjectResult) cosResult;
                                            if (result != null) {
                                                Bundle b = new Bundle();
                                                b.putString("imgName", imgName);
                                                b.putInt("code", 1000);
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
                                            Log.w("TEST", "cancel");
                                        }
                                    });
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                FHUtils.showToast("上传出错,请重新上传");
//                                hideLoading();
                                count = 0;
                                clearImages();
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {

                        }
                    }));
            return "";
        }
    }

    private void clearImages() {
// 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        RxPermissions permissions = new RxPermissions(getActivity());
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    PictureFileUtils.deleteCacheDirFile(getContext());
                } else {
                    Toast.makeText(getContext(),
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            dingWei();
        } else {
            // Permission Denied
        }
    }



    private void upLoad() {
        PictureSelector.create(getActivity())
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(3)// 最大图片选择数量
                .minSelectNum(0)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                .previewImage(true)// 是否可预览图片
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
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                //.rotateEnabled() // 裁剪是否可旋转图片
                //.scaleEnabled()// 裁剪是否可放大缩小图片
                //.videoQuality()// 视频录制质量 0 or 1
                //.videoSecond()//显示多少秒以内的视频or音频也可适用
                //.recordVideoSecond()//录制视频秒数 默认60s
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    private void openCamera() {
        //start开启功能，TYPE_PICK_MEDIA - 选择图片/视频/音频 TYPE_TAKE_PICTURE - 拍照
        PictureSelector.create(getActivity())
                .openCamera(PictureMimeType.ofImage())// 单独拍照，也可录像或也可音频 看你传入的类型是图片or视频
                .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles
                .maxSelectNum(3)// 最大图片选择数量
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
                .circleDimmedLayer(false)// 是否圆形裁剪
                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
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
    }
}
