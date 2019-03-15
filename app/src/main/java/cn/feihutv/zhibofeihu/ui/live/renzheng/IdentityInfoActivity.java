package cn.feihutv.zhibofeihu.ui.live.renzheng;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.prefs.AppPreferencesHelper;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.HttpLoggingInterceptor;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import cn.feihutv.zhibofeihu.utils.TrustAllCerts;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * <pre>
 *     author : liwen.chen
 *     time   :
 *     desc   : 实名认证
 *     version: 1.0
 * </pre>
 */
public class IdentityInfoActivity extends BaseActivity implements IdentityInfoMvpView {

    @Inject
    IdentityInfoMvpPresenter<IdentityInfoMvpView> mPresenter;
    @BindView(R.id.id_card_img)
    ImageView idCardImg;
    @BindView(R.id.edit_name)
    EditText editName;
    @BindView(R.id.edit_id_num)
    EditText editIdNum;

    private List<LocalMedia> selectList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_identity_info;
    }


    @Override
    protected void initWidget() {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this));

        //persenter调用初始化
        mPresenter.onAttach(IdentityInfoActivity.this);
    }


    @Override
    protected void onDestroy() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroy();
    }

    @OnClick({R.id.identity_return, R.id.id_card_img, R.id.btn_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.identity_return:
                finish();
                break;
            case R.id.id_card_img:
                showPhotoDialog();
                break;
            case R.id.btn_commit:
                if (selectList.size() == 0) {
                    onToast("请上传身份证");
                    return;
                }
                if (TextUtils.isEmpty(editIdNum.getText())) {
                    onToast("请输入身份证号");
                    return;
                }
                if (TextUtils.isEmpty(editName.getText())) {
                    onToast("请输入姓名");
                    return;
                }
                String path = selectList.get(0).getCompressPath();
                String ext = path.substring(path.lastIndexOf(".") + 1).toLowerCase();
                if (ext.equals("jpeg")) {
                    ext = "jpg";
                }
                showLoading();
                upLoadImage(editName.getText().toString(), editIdNum.getText().toString(), path, ext, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        hideLoading();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        try {
                            AppLogger.e("result", result);
                            JSONObject jsonObject = new JSONObject(result);
                            int code = jsonObject.getInt("Code");
                            if (code == 0) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SharePreferenceUtil.saveSeesionInt(IdentityInfoActivity.this, AppPreferencesHelper.PREF_KEY_CERTIFISTATUS, 2);
                                        showDialog();
                                    }
                                });
                            } else {
                                AppLogger.e(response.toString());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        onToast("上传失败", Gravity.CENTER, 0, 0);
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            Log.e("print", "onResponse: --->" + e.toString());
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideLoading();
                            }
                        });
                    }
                });
                break;
            default:
                break;
        }
    }

    private void showDialog() {
        final Dialog pickDialog2 = new Dialog(this, R.style.color_dialog);
        pickDialog2.setContentView(R.layout.dialog_bind_succ);

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog2.getWindow();
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.CENTER);
        lp.width = (int) (display.getWidth()); //设置宽度

        pickDialog2.getWindow().setAttributes(lp);

        Button button = (Button) pickDialog2.findViewById(R.id.btn_pop_renzheng);
        TextView textView = (TextView) pickDialog2.findViewById(R.id.tv_pop);
        textView.setText(R.string.identityinfo);
        button.setText("确定");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                pickDialog2.dismiss();
            }
        });
        pickDialog2.setCanceledOnTouchOutside(false);
        pickDialog2.show();
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
                upLoad();
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

    private void upLoad() {
        PictureSelector.create(IdentityInfoActivity.this)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(1)// 最大图片选择数量
                .minSelectNum(0)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
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
        PictureSelector.create(IdentityInfoActivity.this)
                .openCamera(PictureMimeType.ofImage())// 单独拍照，也可录像或也可音频 看你传入的类型是图片or视频
                .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles
                .maxSelectNum(1)// 最大图片选择数量
                .minSelectNum(0)// 最小选择数量
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                .previewImage(false)// 是否可预览图片
                .previewVideo(false)// 是否可预览视频
                .enablePreviewAudio(false) // 是否可播放音频
                .isCamera(true)// 是否显示拍照按钮
                .enableCrop(false)// 是否裁剪
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
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
                    if (selectList.size() > 0) {
                        Bitmap bm = BitmapFactory.decodeFile(selectList.get(0).getCompressPath());
                        idCardImg.setImageBitmap(bm);
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    public static void upLoadImage(String userName, String idCard, String filepath, String ext, Callback callback) {
        MediaType MEDIA_TYPE;
        long t = System.currentTimeMillis();
        String uid = SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID");
        String token = SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_API_KEY");
        String photoName = uid + "_" + t + "." + ext;
        String sig = FHUtils.getMD5(photoName + t + uid + token);
        File file = new File(filepath);
        if (ext.equals("jpg")) {
            MEDIA_TYPE = MediaType.parse("image/jpg");
        } else {
            MEDIA_TYPE = MediaType.parse("image/png");
        }

        RequestBody fileBody = RequestBody.create(MEDIA_TYPE, file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("photoName", photoName)
                .addFormDataPart("t", String.valueOf(t))
                .addFormDataPart("uid", uid)
                .addFormDataPart("sig", sig)
                .addFormDataPart("userName", userName)
                .addFormDataPart("idCard", idCard)
                .addFormDataPart("file", file.getName(), fileBody)
                .build();
        String UPLOAD_URL_DEBUG = "https://mm-dev.feihutv.cn/uploadPhotoes";
        Request request = new Request.Builder()
                .url(UPLOAD_URL_DEBUG)
                .post(requestBody)
                .build();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new HttpLoggingInterceptor());
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.sslSocketFactory(createSSLSocketFactory());
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        Call call = builder.build().newCall(request);
        call.enqueue(callback);
    }

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ssfFactory;
    }
}
