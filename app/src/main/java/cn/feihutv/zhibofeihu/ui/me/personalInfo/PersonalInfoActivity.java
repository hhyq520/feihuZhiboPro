package cn.feihutv.zhibofeihu.ui.me.personalInfo;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetCosSignResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseResponse;
import cn.feihutv.zhibofeihu.data.prefs.AppPreferencesHelper;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.live.renzheng.IdentityInfoActivity;
import cn.feihutv.zhibofeihu.ui.live.renzheng.UploadFailureActivity;
import cn.feihutv.zhibofeihu.ui.live.renzheng.UploadSuccessActivity;
import cn.feihutv.zhibofeihu.ui.main.MainActivity;
import cn.feihutv.zhibofeihu.ui.me.dynamic.SendDynamicActivity;
import cn.feihutv.zhibofeihu.ui.me.recharge.RechargeActivity;
import cn.feihutv.zhibofeihu.ui.me.shop.ShoppingActivity;
import cn.feihutv.zhibofeihu.ui.widget.TCActivityTitle;
import cn.feihutv.zhibofeihu.ui.widget.TCLineControllerView;
import cn.feihutv.zhibofeihu.utils.AppConstants;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.AppUtils;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.feihutv.zhibofeihu.utils.glideutil.GlideApp;
import cn.feihutv.zhibofeihu.utils.weiget.dialog.RxDialog;
import cn.feihutv.zhibofeihu.utils.weiget.dialog.RxDialogSureCancel;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.graphics.BitmapFactory.decodeFile;

/**
 * <pre>
 *     @author : liwen.chen
 *     time   :
 *     desc   : 个人资料
 *     version: 1.0
 * </pre>
 */
public class PersonalInfoActivity extends BaseActivity implements PersonalInfoMvpView {

    @Inject
    PersonalInfoMvpPresenter<PersonalInfoMvpView> mPresenter;

    @BindView(R.id.person_btn_back)
    TCActivityTitle mTitle;

    @BindView(R.id.person_rl)
    RelativeLayout rlHead;

    @BindView(R.id.person_rl2)
    RelativeLayout rlCover;

    @BindView(R.id.person_nick)
    TCLineControllerView nickName;

    @BindView(R.id.person_signature)
    TCLineControllerView signature;

    @BindView(R.id.feihu_id)
    TCLineControllerView fhId;

    @BindView(R.id.person_sex)
    TCLineControllerView sex;

    @BindView(R.id.authentication)
    TCLineControllerView authentication;

    @BindView(R.id.modify_password)
    TCLineControllerView modifyPassword;

    @BindView(R.id.person_binding)
    TCLineControllerView bindPhone;

    @BindView(R.id.person_headImg)
    ImageView imgHead;

    @BindView(R.id.person_imgBg)
    ImageView imgCover;

    public static final int ADDRESS_REQUSST_CODE = 1001;
    public static final int REQUSST_NICKNAME = 1002;
    public static final int REQUSST_SIGNATURE = 1003;
    public static final int REQUSST_PHONENUM = 1004;

    private LoadUserDataBaseResponse.UserData mUserData;
    private List<LocalMedia> selectList = new ArrayList<>();
    private int flag = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_info;
    }


    @Override
    protected void initWidget() {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this));

        //persenter调用初始化
        mPresenter.onAttach(PersonalInfoActivity.this);

        mUserData = mPresenter.getUserData();
        initView();
    }

    private void initView() {
        Glide.with(getActivity()).load(mUserData.getHeadUrl()).into(imgHead);
        Glide.with(getActivity()).load(mUserData.getLiveCover()).into(imgCover);
        nickName.setContent(mUserData.getNickName());
        switch (mUserData.getGender()) {
            case 2:
                sex.setContent("女");
                break;
            case 1:
                sex.setContent("男");
                break;
            case 0:
            default:
                sex.setContent("保密");
                break;
        }
        String sign = mUserData.getSignature();
        if (!TextUtils.isEmpty(sign)) {
            signature.setContent(TCUtils.getLimitString(sign, 5));
        } else {
            signature.setContent("请输入");
        }
        fhId.setContent(mUserData.getAccountId());

        if (mUserData.getCertifiStatus() == 1) {
            authentication.setContent("已认证");
            authentication.hideRight(true);
        } else {
            authentication.setContent("未认证");
            authentication.hideRight(false);
        }
        bindPhone.setContentColor(R.color.red_pressed);
        if (!TextUtils.isEmpty(mUserData.getPhone())) {
            bindPhone.setContent(TCUtils.hidePhoneMid(mUserData.getPhone()));
        } else {
            bindPhone.setContent("未绑定");
        }

        TCUtils.showPicWithUrl(getActivity(), imgHead, mUserData.getHeadUrl(), R.drawable.face);


    }

    @Override
    protected void eventOnClick() {
        mTitle.setReturnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        //销毁之前必须调用
        mPresenter.onDetach();
        clearImages();
        super.onDestroy();
    }

    @Override
    public Activity getActivity() {
        return PersonalInfoActivity.this;
    }

    @Override
    public void modifyProfileGenderSucc(int gender) {
        switch (gender) {
            case 2:
                sex.setContent("女");
                break;
            case 1:
                sex.setContent("男");
                break;
            case 0:
            default:
                sex.setContent("保密");
                break;
        }
    }

    @OnClick({R.id.person_rl, R.id.person_rl2, R.id.person_nick, R.id.person_sex, R.id.feihu_id, R.id.person_signature, R.id.authentication, R.id.modify_password, R.id.person_binding})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.person_rl:
                flag = 1;
                showPhotoDialog();
                break;
            case R.id.person_rl2:
                flag = 2;
                showPhotoDialog();
                break;
            case R.id.person_nick:
                Intent intentNick = new Intent(getActivity(), NicknameModifyActivity.class);
                intentNick.putExtra("nickName", mUserData.getNickName());
                startActivityForResult(intentNick, REQUSST_NICKNAME);
                break;
            case R.id.person_sex:
                if (mUserData.getmModified().getGender() > 0) {
                    onToast("性别只可更改一次", Gravity.CENTER, 0, 0);
                } else {
                    ShowSelectSexDialog();
                }

                break;
            case R.id.feihu_id:
                final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(getActivity());//提示弹窗
                rxDialogSureCancel.setContent("是否前往商城购买靓号？");
                rxDialogSureCancel.setSure("前往");
                rxDialogSureCancel.setCancel("取消");
                rxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), ShoppingActivity.class));
                        rxDialogSureCancel.cancel();
                    }
                });
                rxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rxDialogSureCancel.cancel();
                    }
                });
                rxDialogSureCancel.show();
                break;
            case R.id.person_signature:
                Intent intent = new Intent(getActivity(), SignatureActivity.class);
                intent.putExtra("sign", mUserData.getSignature());
                startActivityForResult(intent, REQUSST_SIGNATURE);
                break;
            case R.id.authentication:
                if (TextUtils.isEmpty(mUserData.getPhone())) {
                    // 跳转到绑定手机号码
                    final RxDialogSureCancel rxDialogSureCancel1 = new RxDialogSureCancel(this);
                    rxDialogSureCancel1.setSure("前往");
                    rxDialogSureCancel1.setCancel("取消");
                    rxDialogSureCancel1.setContent("绑定手机号开启认证啦");
                    rxDialogSureCancel1.setSureListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivityForResult(new Intent(getActivity(), BlindPhoneActivity.class), REQUSST_PHONENUM);
                            rxDialogSureCancel1.dismiss();
                        }
                    });

                    rxDialogSureCancel1.setCancelListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            rxDialogSureCancel1.dismiss();
                        }
                    });

                    rxDialogSureCancel1.show();
                    return;
                }
                int state = SharePreferenceUtil.getSessionInt(getActivity(), AppPreferencesHelper.PREF_KEY_CERTIFISTATUS);
                if (state == 0) {
                    // 未认证
                    startActivity(new Intent(this, IdentityInfoActivity.class));
                } else if (state == 1) {
                    // 1已认证
                } else if (state == 2) {
                    // 认证中
                    startActivity(new Intent(getActivity(), UploadSuccessActivity.class));
                } else if (state == 3) {
                    // 认证失败
                    startActivity(new Intent(getActivity(), UploadFailureActivity.class));
                }
                break;
            case R.id.modify_password:
                if (TextUtils.isEmpty(mUserData.getPhone())) {
                    onToast("您未绑定手机号，不可修改密码", Gravity.CENTER, 0, 0);
                } else {
                    startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
                }
                break;
            case R.id.person_binding:
                MobclickAgent.onEvent(getActivity(), "10115");
                if (TextUtils.isEmpty(mUserData.getPhone())) {
                    // 如果手机号码为空的情况下，显示未绑定，点击可以绑定
                    startActivityForResult(new Intent(getActivity(), BlindPhoneActivity.class), REQUSST_PHONENUM);
                } else {
                    startActivityForResult(new Intent(this, ChangePhoneActivity.class), REQUSST_PHONENUM);
                }
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

    @Override
    public void notifyCosSignResponce(GetCosSignResponse response, String path) {
        if (response.getCode() == 0) {
            String sign = response.getCosSignData().getSign();
            final String imgName = response.getCosSignData().getImgName();
            try {
                sign = URLDecoder.decode(sign, "UTF-8");
                AppUtils.doUploadCover(PersonalInfoActivity.this, path, sign, imgName, new IUploadTaskListener() {
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
                                    onToast("上传成功");
                                    String url = bundle.getString("imgName");
                                    final String headUrl = AppConstants.cosHeadUrl + url;
                                    AppLogger.e(headUrl);
                                    GlideApp.loadImgtransform(PersonalInfoActivity.this,
                                            headUrl, R.drawable.face, imgHead);
                                    mPresenter.modifyProfile_HeadUrl(headUrl);
                                    clearImages();
                                } else if (flag == 2) {
                                    onToast("上传成功");
                                    String url = bundle.getString("imgName");
                                    final String headUrl = AppConstants.cosHeadUrl + url;
                                    GlideApp.loadImg(PersonalInfoActivity.this,
                                            headUrl, R.drawable.bg, DiskCacheStrategy.RESOURCE, 1000, imgCover);
                                    AppLogger.e(headUrl);
                                    mPresenter.modifyProfile_LiveCover(headUrl);
                                    clearImages();
                                }

                                break;
                            case 1001:
                                hideLoading();
                                onToast("上传出错,请重新上传");
                                break;

                        }
                    }
                });
    }

    private void getPicFrom() {
        //start开启功能，TYPE_PICK_MEDIA - 选择图片/视频/音频 TYPE_TAKE_PICTURE - 拍照
        if (flag == 1) {
            PictureSelector.create(PersonalInfoActivity.this)
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
            PictureSelector.create(PersonalInfoActivity.this)
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
                    PictureFileUtils.deleteCacheDirFile(PersonalInfoActivity.this);
                } else {
                    Toast.makeText(PersonalInfoActivity.this,
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

    private void openCamera() {
        //start开启功能，TYPE_PICK_MEDIA - 选择图片/视频/音频 TYPE_TAKE_PICTURE - 拍照
        if (flag == 1) {
            PictureSelector.create(PersonalInfoActivity.this)
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
            PictureSelector.create(PersonalInfoActivity.this)
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

    /**
     * 选择性别对话框
     */
    private void ShowSelectSexDialog() {
        final Dialog dialog = new Dialog(this, R.style.color_dialog);
        dialog.setContentView(R.layout.dialog_select_sex);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = dialog.getWindow();
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = (int) (display.getWidth()); //设置宽度

        dialog.getWindow().setAttributes(lp);

        Button btn_male = (Button) dialog.findViewById(R.id.btn_ss_male);
        btn_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.modifyProfileGender(1);
                dialog.dismiss();
            }
        });

        Button btn_female = (Button) dialog.findViewById(R.id.btn_ss_female);
        btn_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.modifyProfileGender(2);
                dialog.dismiss();
            }
        });

        Button btn_unkonw = (Button) dialog.findViewById(R.id.btn_ss_unkonw);
        btn_unkonw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.modifyProfileGender(0);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode != RESULT_OK || requestCode != SIGNATURE_REQUEST_CODE) {
//            return;
//        }

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
            case ADDRESS_REQUSST_CODE:
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    String address = bundle.getString("address");
                    //person_address.setContent(address);
                }
                break;
            case REQUSST_NICKNAME:
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    String nickname = bundle.getString("nickName");
                    nickName.setContent(nickname);
                    mUserData.setNickName(nickname);
                    mPresenter.saveUserData(mUserData);
                }
                break;
            case REQUSST_SIGNATURE:
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    String signature1 = bundle.getString("signature");
                    mUserData.setSignature(signature1);
                    signature.setContent(TCUtils.getLimitString(signature1, 5));
                    mPresenter.saveUserData(mUserData);
                }
                break;
            case REQUSST_PHONENUM:
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    String phone = bundle.getString("phonenumber");
                    mUserData.setPhone(phone);
                    bindPhone.setContent(TCUtils.hidePhoneMid(phone));
                    mPresenter.saveUserData(mUserData);
                }
                break;
            default:
                break;
        }
    }

    private void upLoadImages() {
        if (selectList.size() == 0) {
            return;
        }
        showLoading();
        String imagePath = selectList.get(0).getCompressPath();
        Bitmap bitmap=decodeSampleBitmapFromPath(imagePath,1000,1000);

        String ext = imagePath.substring(imagePath.lastIndexOf(".") + 1).toLowerCase();
        if(ext.toLowerCase().equals("webp")){
            FHUtils.showToast("不支持此类图片格式！");
            return;
        }
        imagePath=saveBitmap(PersonalInfoActivity.this,bitmap, ext);
        if (ext.equals("jpeg")) {
            ext = "jpg";
        }
        if (flag == 1) {
            mPresenter.getCosSign(1, ext, imagePath);
        } else {
            mPresenter.getCosSign(2, ext, imagePath);
        }
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
                                     int reqheight) {
        int originalWidth = op.outWidth;
        int originalHeight = op.outHeight;
        int inSampleSize = 1;
        if (originalWidth > reqWidth || originalHeight > reqheight) {
            int halfWidth = originalWidth / 2;
            int halfHeight = originalHeight / 2;
            inSampleSize *= 2;
            while ((halfWidth > reqWidth)
                    ||(halfHeight > reqheight)) {
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

    public  String saveBitmap(Context context, Bitmap mBitmap, String ext) {
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

}





