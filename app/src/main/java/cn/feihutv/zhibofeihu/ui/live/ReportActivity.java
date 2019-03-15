package cn.feihutv.zhibofeihu.ui.live;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.DebugUtil;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.tencent.cos.COSClient;
import com.tencent.cos.COSClientConfig;
import com.tencent.cos.common.COSEndPoint;
import com.tencent.cos.model.COSRequest;
import com.tencent.cos.model.COSResult;
import com.tencent.cos.model.PutObjectRequest;
import com.tencent.cos.model.PutObjectResult;
import com.tencent.cos.task.listener.IUploadTaskListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import cn.feihutv.zhibofeihu.R;

import cn.feihutv.zhibofeihu.data.network.http.model.me.GetCosSignResponse;
import cn.feihutv.zhibofeihu.ui.live.ReportMvpView;
import cn.feihutv.zhibofeihu.ui.live.ReportMvpPresenter;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.ui.live.adapter.GridImageAdapter;
import cn.feihutv.zhibofeihu.ui.widget.TCActivityTitle;
import cn.feihutv.zhibofeihu.utils.AppConstants;
import cn.feihutv.zhibofeihu.utils.AppUtils;
import cn.feihutv.zhibofeihu.utils.FullyGridLayoutManager;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * <pre>
 *     author : huang hao
 *     time   :
 *     desc   : 界面绘制
 *     version: 1.0
 * </pre>
 */
public class ReportActivity extends BaseActivity implements ReportMvpView, RadioGroup.OnCheckedChangeListener {

    @Inject
    ReportMvpPresenter<ReportMvpView> mPresenter;
    @BindView(R.id.radiogroup)
    RadioGroup radioGroup;
    @BindView(R.id.edit_rb3)
    EditText editText3;
    @BindView(R.id.tca_rp_reture)
    TCActivityTitle tcActivityTitle;
    @BindView(R.id.recycle)
    RecyclerView recycle;
    private String userId = "";
    private boolean isChecked = false;
    private int currentSelect = 0;
    private int count = 0;
    GridImageAdapter adapter;
    private List<LocalMedia> selectList = new ArrayList<>();
    private List<String> imgurls = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_report;
    }


    @Override
    protected void initWidget() {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this));

        //persenter调用初始化
        mPresenter.onAttach(ReportActivity.this);

        intiView();
    }

    private void intiView() {
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        if (!TextUtils.isEmpty(userId)) {
            SharePreferenceUtil.saveSeesion(this, "report_userid", userId);
        }

        radioGroup.setOnCheckedChangeListener(this);
        radioGroup.getChildAt(0).performClick();
        tcActivityTitle.setReturnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tcActivityTitle.setMoreListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isChecked) {
                    if (!TextUtils.isEmpty(editText3.getText().toString().trim())&&selectList.size()>0) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("code", 1003);
                        dealHander(bundle);
                    } else {
                        if(selectList.size()==0){
                            onToast("请至少上传一张照片");
                        }else{
                            onToast("请填写举报理由");
                        }

                    }
                } else {
                    onToast("请选择您举报的理由");
                }

            }
        });

        int currentPosition = SharePreferenceUtil.getSessionInt(this, "currentSelect", currentSelect);
        String content = SharePreferenceUtil.getSession(this, "Reportcontent");
        switch (currentPosition) {
            case 0:
                radioGroup.check(R.id.radiobutton1);
                editText3.setText(content);
                break;
            case 1:
                radioGroup.check(R.id.radiobutton2);
                editText3.setText(content);
                break;
            case 2:
                radioGroup.check(R.id.radiobutton3);
                editText3.setText(content);
                break;
            default:
                break;
        }
        FullyGridLayoutManager manager = new FullyGridLayoutManager(ReportActivity.this, 4, GridLayoutManager.VERTICAL, false);
        recycle.setLayoutManager(manager);
        adapter = new GridImageAdapter(ReportActivity.this, onAddPicClickListener);
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
                            PictureSelector.create(ReportActivity.this).externalPicturePreview(position, selectList);
                            break;
                        default:
                            break;
                    }
                }
            }
        });
        clearImages();

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
                    PictureFileUtils.deleteCacheDirFile(ReportActivity.this);
                } else {
                    Toast.makeText(ReportActivity.this,
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
                                imm.hideSoftInputFromWindow(editText3.getWindowToken(), 0); //强制隐藏键盘
                                if (selectList.size() > 0) {
                                    LocalMedia item = selectList.get(count);

                                    String imagePath = item.getCompressPath();
                                    String ext = imagePath.substring(imagePath.lastIndexOf(".") + 1).toLowerCase();
                                    if (ext.equals("jpeg")) {
                                        ext = "jpg";
                                    }
                                    ReUploadPicTask uploadPicTask = new ReUploadPicTask(ReportActivity.this);
                                    uploadPicTask.execute(ext, imagePath);
                                } else {
                                    report();

                                }
                                break;
                            case 1000:
                                String url = bundle.getString("imgName");
                                final String picUrl = AppConstants.cosHeadUrl + url;
                                Log.e("picUrl", picUrl);
                                imgurls.add(picUrl);
                                count++;
                                if (count == selectList.size()) {
                                    report();
                                    count = 0;
                                } else {
                                    LocalMedia item = selectList.get(count);
                                    String imagePath = item.getCompressPath();
                                    String ext = imagePath.substring(imagePath.lastIndexOf(".") + 1).toLowerCase();
                                    if (ext.equals("jpeg")) {
                                        ext = "jpg";
                                    }
                                    ReUploadPicTask uploadPicTask = new ReUploadPicTask(ReportActivity.this);
                                    uploadPicTask.execute(ext, imagePath);
                                }
                                break;
                            case 1001:
                                onToast("上传出错,请重新上传");
                                hideLoading();
                                count = 0;
                                clearImages();
                                break;
                            default:
                                break;
                        }
                    }
                });
    }


    private void report() {
        String content = "";
        content = editText3.getText().toString();
        // String id = SharePreferenceUtil.getSession(this, "report_userid");
        StringBuilder strbuilder = new StringBuilder();
        for (int i = 0; i < imgurls.size(); i++) {
            if (i > 0) {
                strbuilder.append(",");
            }
            strbuilder.append(imgurls.get(i).substring(imgurls.get(i).lastIndexOf("/") + 1));
        }
        mPresenter.report(SharePreferenceUtil.getSession(ReportActivity.this, "report_userid"), currentSelect + 1, content, strbuilder.toString());
    }

    @Override
    protected void onDestroy() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroy();
    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.radiobutton1:
                isChecked = true;
                currentSelect = 0;
                break;
            case R.id.radiobutton2:
                isChecked = true;
                currentSelect = 1;
                break;
            case R.id.radiobutton3:
                currentSelect = 2;
                isChecked = true;
                break;
        }
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            // 进入相册 以下是例子：不需要的api可以不写
            PictureSelector.create(ReportActivity.this)
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
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    @Override
    public void notifyCosSignResponce(GetCosSignResponse response, String path) {
        if (response.getCode() == 0) {
            String sign = response.getCosSignData().getSign();
            final String imgName = response.getCosSignData().getImgName();
            try {
                sign = URLDecoder.decode(sign, "UTF-8");
                AppUtils.doUploadCover(ReportActivity.this, path, sign, imgName, new IUploadTaskListener() {
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
            onToast("上传出错,请重新上传");
            hideLoading();
            count = 0;
            clearImages();
        }
    }

    @Override
    public void reportResponce() {
        hideLoading();
        clearImages();
        onToast("举报成功，感谢您的监督，我们会尽快核实并处理！");
        finish();
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
            String result = upLoadPic(params[0], params[1]);
            return result;
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }

        private String upLoadPic(String ext, final String path) {
            mPresenter.getCosSign(4, ext, path);
            return "";
        }
    }
}
