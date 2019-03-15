package cn.feihutv.zhibofeihu.ui.mv.demand;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chinanetcenter.wcs.android.api.FileUploader;
import com.chinanetcenter.wcs.android.entity.OperationMessage;
import com.chinanetcenter.wcs.android.internal.UploadFileRequest;
import com.chinanetcenter.wcs.android.listener.FileUploaderListener;
import com.chinanetcenter.wcs.android.utils.WCSLogUtil;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.tencent.cos.model.COSRequest;
import com.tencent.cos.model.COSResult;
import com.tencent.cos.model.PutObjectResult;
import com.tencent.cos.task.listener.IUploadTaskListener;

import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.db.model.SysConfigBean;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetCosSignResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.CncGetUploadTokenResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.EnablePostMVResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetAllNeedListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMyMVListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.PostMvResponse;
import cn.feihutv.zhibofeihu.rxbus.RxBus;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.mv.adapter.DemandSquareAdapter2;
import cn.feihutv.zhibofeihu.ui.widget.TCActivityTitle;
import cn.feihutv.zhibofeihu.ui.widget.dialog.MvMsgDialog;
import cn.feihutv.zhibofeihu.utils.AppConstants;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.AppUtils;
import cn.feihutv.zhibofeihu.utils.CommonUtils;
import cn.feihutv.zhibofeihu.utils.FileUtils;
import cn.feihutv.zhibofeihu.utils.video.DeviceUtils;
import cn.feihutv.zhibofeihu.utils.video.ExtractFrameWorkThread;
import cn.feihutv.zhibofeihu.utils.video.ExtractVideoInfoUtil;
import cn.feihutv.zhibofeihu.utils.video.PictureUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static cn.feihutv.zhibofeihu.rxbus.RxBusCode.RX_BUS_CLICK_CODE_MV_POST_MV;


/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/
 *     desc   : 发布mv
 *     version: 1.0
 * </pre>
 */
public class PostMvVideoActivity extends BaseActivity implements PostMvVideoMvpView {

    @Inject
    PostMvVideoMvpPresenter<PostMvVideoMvpView> mPresenter;

    @BindView(R.id.tc_title)
    TCActivityTitle tc_title;
    @BindView(R.id.et_demand_title)
    EditText et_demand_title;

    @BindView(R.id.mv_upload_video)
    TextView mv_upload_video;
    @BindView(R.id.mv_upload_videoImg)
    TextView mv_upload_videoImg;


    @BindView(R.id.mv_first_cutImg)
    ImageView mv_first_cutImg;

    @BindView(R.id.mv_cutImg)
    ImageView mv_cutImg;


    @BindView(R.id.rv_videoList)
    XRecyclerView mRecyclerView;

    @BindView(R.id.mv_first_cutImg_play)
    TextView mv_first_cutImg_play;

    @BindView(R.id.fl_mv_cutImg)
    FrameLayout fl_mv_cutImg;
    @BindView(R.id.fl_mv_first_cutImg)
    FrameLayout fl_mv_first_cutImg;

    @BindView(R.id.swt_dz)
    CheckBox swt_dz;

    @BindView(R.id.mv_first_cutImg_del)
    TextView mv_first_cutImg_del;

    @BindView(R.id.mv_first_cutImg_del2)
    TextView mv_first_cutImg_del2;

    private ExtractVideoInfoUtil mExtractVideoInfoUtil;
    private ExtractFrameWorkThread mExtractFrameWorkThread;


    private String theMvVideoPath = "";
    private String theMvVideoImgPath = "";//封面路径
    private String OutPutFileDirPath;

    private String theMvVideoImgServerUrl = "";//上传后生成的图片链接
    private String theMvVideoId = "";//上传后生成的图片链接

    int nextOffset = 0;
    int nextForMe = 1;
    int pageSize = 10;
    private String needId = "0";
    private String submitNeedId = "0";
    int reType = 0;

    private boolean isUpLoaded = false;//是否

    String mvTitle = "";

    DemandSquareAdapter2 mDemandSquareAdapter;

    List<GetAllNeedListResponse.GetAllNeedList> mGetAllMvLists = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_post_mv_video;
    }


    @Override
    protected void initWidget() {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this));

        //Presenter调用初始化
        mPresenter.onAttach(PostMvVideoActivity.this);

        tc_title.setReturnListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        OutPutFileDirPath = FileUtils.getMvFileDir();
        com.chinanetcenter.wcs.android.Config.baseUrl = "https://ovpadmin.up28.v1.wcsapi.com";


        //主播修改作品
        GetMyMVListResponse.GetMyMVList myModifItemMV = (GetMyMVListResponse.GetMyMVList)
                getIntent().getSerializableExtra("modifMVList");


        //
        GetAllNeedListResponse.GetAllNeedList getAllNeedList = (GetAllNeedListResponse.GetAllNeedList)
                getIntent().getSerializableExtra("AllNeedListItemNeed");

        mDemandSquareAdapter = new DemandSquareAdapter2(PostMvVideoActivity.this, mGetAllMvLists);
        LinearLayoutManager layoutManager = new LinearLayoutManager(PostMvVideoActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        //添加分隔线
//        mRecyclerView.addItemDecoration(new ListViewDecoration());
        mRecyclerView.setAdapter(mDemandSquareAdapter);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);

        if (getAllNeedList != null) {//不为空则是定制mv
            mRecyclerView.setVisibility(View.VISIBLE);
            swt_dz.setVisibility(View.GONE);
            mGetAllMvLists.add(getAllNeedList);
            mDemandSquareAdapter.notifyDataSetChanged();
            needId = getAllNeedList.getNeedId();
            mRecyclerView.setPullRefreshEnabled(false);
            mRecyclerView.setLoadingMoreEnabled(false);

        } else {
            //定制mv 需
            if (myModifItemMV != null) {
                tc_title.setTitle("修改MV");
                mRecyclerView.setVisibility(View.GONE);
                swt_dz.setVisibility(View.GONE);
                //主播修改mv
                needId = myModifItemMV.getNeedId();
                et_demand_title.setText(myModifItemMV.getTitle());
                et_demand_title.setSelection(myModifItemMV.getTitle().length());
                fl_mv_first_cutImg.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(myModifItemMV.getCover())
                        .into(mv_first_cutImg);
                fl_mv_cutImg.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(myModifItemMV.getCover())
                        .into(mv_cutImg);
                GetAllNeedListResponse.GetAllNeedList modifItem = new GetAllNeedListResponse.GetAllNeedList();
                modifItem.setTitle(myModifItemMV.getTitle());
                modifItem.setAvatar(myModifItemMV.getAvatar());
                modifItem.setNickname(myModifItemMV.getNickname());
                modifItem.sethB(myModifItemMV.gethB());
                modifItem.setT(myModifItemMV.getT());
                modifItem.setUid(myModifItemMV.getUid());
                modifItem.setForUid(mPresenter.getLoinUserId());
                modifItem.setSongName(myModifItemMV.getSongName());
                modifItem.setSingerName(myModifItemMV.getSingerName());
                modifItem.setRequire(myModifItemMV.getRequire());

                mRecyclerView.setVisibility(View.VISIBLE);
                swt_dz.setVisibility(View.GONE);
                mGetAllMvLists.add(modifItem);
                mDemandSquareAdapter.notifyDataSetChanged();
                mRecyclerView.setPullRefreshEnabled(false);
                mRecyclerView.setLoadingMoreEnabled(false);

            } else {

                //非定制mv
                mRecyclerView.setVisibility(View.VISIBLE);
                swt_dz.setVisibility(View.VISIBLE);
                swt_dz.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                            if (isChecked) {
                                //加载需求列表
                                mRecyclerView.setVisibility(View.VISIBLE);
                            } else {
                                needId = "0";
                                mRecyclerView.setVisibility(View.GONE);
                            }

                    }
                });

                //获取
                mPresenter.getAllNeedList("1", nextForMe + "", nextOffset + "", pageSize + "");
                mRecyclerView.setPullRefreshEnabled(true);
                mRecyclerView.setLoadingMoreEnabled(true);
                mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
                    @Override
                    public void onRefresh() {
                        reType = 1;
                        mPresenter.getAllNeedList("1", "1", "0", pageSize + "");
                    }

                    @Override
                    public void onLoadMore() {
                        reType = 2;
                        if (nextForMe == -2) {
                            mRecyclerView.setLoadingMoreEnabled(false);
                            mRecyclerView.setNoMore(true);
                        } else {
                            mPresenter.getAllNeedList("1", nextForMe + "", nextOffset + "", pageSize + "");
                        }

                    }
                });


                mDemandSquareAdapter.setClickCallBack(new DemandSquareAdapter2.ItemClickCallBack() {
                    @Override
                    public void onItemClick(int pos) {
                        //点击选择某行
                        GetAllNeedListResponse.GetAllNeedList mGetAllNeedList1=mGetAllMvLists.get(pos);
                        String userId=mPresenter.getLoinUserId();
                        if(userId.equals(mGetAllNeedList1.getUid())){
                            onToast("不能选择自己的需求！");
                            return;
                        }

                        if(!isPostDzMv()){
                            return;
                        }

                        needId = mGetAllNeedList1.getNeedId();
                        mPresenter.enablePostMV(mGetAllNeedList1.getNeedId());


                    }
                });



                mRecyclerView.setRecyclerListener(new RecyclerView.RecyclerListener(){

                    @Override
                    public void onViewRecycled(RecyclerView.ViewHolder holder) {

                        AppLogger.e("--------onViewRecycled"+holder);
                        if(holder instanceof  DemandSquareAdapter2.ViewHolder){
                            AppLogger.e("--------onViewRecycled");
                        }
                    }
                });

            }
        }


        tc_title.setMoreListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mvTitle = et_demand_title.getText().toString();
                if (TextUtils.isEmpty(mvTitle)) {
                    onToast("请填写标题");
                    return;
                }

                if (TextUtils.isEmpty(theMvVideoImgServerUrl)) {
                    onToast("请编辑封面");
                    return;
                }


                if (mUploadTokenData == null || !isUpLoaded) {
                    onToast("请上传视频");
                    return;
                }

                showLoading("正在发布...");
                //发布mv-
                mPresenter.postMV(mvTitle, theMvVideoImgServerUrl,
                        mUploadTokenData.getVideoId(), submitNeedId);

            }
        });
    }



    @Override
    public void onEnablePostMVResp(EnablePostMVResponse response) {
        if(response.getCode()==0){
            if(!response.isEnable()){
                onToast("您已经提交过了哦！");
                return;
            }
        }
        submitNeedId=needId;
        mDemandSquareAdapter.setItemChooseState(needId);

    }

    @Override
    public void onGetAllNeedList(GetAllNeedListResponse response) {
        if (reType == 1) {
            mRecyclerView.refreshComplete();
        } else if (reType == 2) {
            mRecyclerView.loadMoreComplete();
        }
        if (response != null) {
            if (response.getCode() == 0) {
                GetAllNeedListResponse.GetAllNeedListData mData = response.getGetAllNeedListData();
                nextOffset = mData.getNextOffset();
                nextForMe = mData.getNextForMe();
                if (mData.getGetAllNeedList() != null && mData.getGetAllNeedList().size() > 0) {
                    if (reType == 1) {
                        mGetAllMvLists.clear();
                    }

//                    String userId=mPresenter.getLoinUserId();
//                    List<GetAllNeedListResponse.GetAllNeedList> mList=new ArrayList<>();
//                    for(GetAllNeedListResponse.GetAllNeedList needList:mData.getGetAllNeedList()){
//                        if(!needList.getUid().equals(userId)){
//                            //排除掉自己
//                            mList.add(needList);
//                        }
//                    }

//                    mGetAllMvLists.addAll(mList);
                    mGetAllMvLists.addAll(mData.getGetAllNeedList());
                } else {
                    mRecyclerView.setNoMore(true);
                }

                mDemandSquareAdapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    public void onPostMvResp(PostMvResponse response) {
        hideLoading();
        if (response.getCode() == 0) {
            onToast("发布成功");
            setResult(8000);
            RxBus.get().send(RX_BUS_CLICK_CODE_MV_POST_MV,8000);
            finish();
        } else if (response.getCode() == 4901) {
            onToast("您已经提交过MV作品了！不能重复提交");
        } else if (response.getCode() == 4009) {
            onToast(getString(R.string.mv_title_error_hint));
        } else {
            onToast("发布失败：" + response.getErrMsg());
        }


    }

    @Override
    protected void onDestroy() {
        //销毁之前必须调用
        mPresenter.onDetach();
        if (mDemandSquareAdapter != null) {
            mDemandSquareAdapter.clearChooseItem();
        }
        super.onDestroy();
    }


    @OnClick({R.id.mv_upload_video, R.id.mv_upload_videoImg,R.id.mv_first_cutImg_del,R.id.mv_first_cutImg_del2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mv_upload_video:
                //上传视频


                if(submitNeedId.equals("0")){
                    //如果是公开mv 则判断是否达到要求
                    if(!isPostGKMv()){
                        return;
                    }
                }


                getPicFrom();

                break;
            case R.id.mv_upload_videoImg:
                if (TextUtils.isEmpty(theMvVideoPath)) {
                    onToast("请先选择MV视频");
                } else {
                    //跳转截取封面界面
                    Bundle bundle = new Bundle();
                    bundle.putString("path", theMvVideoPath);
                    goActivityForResult(CutMvVideoImageActivity.class, bundle, 10);
                }

                break;
            case  R.id.mv_first_cutImg_del:
                //删除视频
                fl_mv_first_cutImg.setVisibility(View.GONE);
                mUploadTokenData=null;

                break;
            case  R.id.mv_first_cutImg_del2:
                //删除封面
                fl_mv_cutImg.setVisibility(View.GONE);
                theMvVideoImgServerUrl="";
                theMvVideoImgPath = "";

                break;
        }
    }


    //检查是否符合发布定制mv 要求
    private boolean isPostDzMv(){

        boolean isPost=false;
        long inCome=  FeihuZhiboApplication.getApplication().mDataManager.getUserData().getIncome();//收到的礼物币值
        SysConfigBean mSysConfigBean= FeihuZhiboApplication.getApplication().mDataManager
                .getSysConfig();
        Long  mvMinHb=  mSysConfigBean.getMvIssueMinHb();
        AppLogger.e("发布定制mv 需要虎币数"+mvMinHb);
        if(mvMinHb==0){
            mvMinHb=300000L;
        }
        if(inCome>=mvMinHb){
            isPost=true;
        } else{
            String coinText=  CommonUtils.getWCoinText(String.valueOf(mvMinHb));
            new MvMsgDialog().openShowMVMsgDialog(PostMvVideoActivity.this,
                    "收到礼物"+coinText+"才能接受需求赚取视频悬赏快去升级吧！","确定");
        }
        return  isPost;
    }


    //检查是否符合发布公开mv 要求
    private boolean isPostGKMv(){

        boolean isPost=false;
        long inCome=  FeihuZhiboApplication.getApplication().mDataManager.getUserData().getIncome();//收到的礼物币值
        SysConfigBean mSysConfigBean= FeihuZhiboApplication.getApplication().mDataManager
                .getSysConfig();
        Long  mvMinHb=  mSysConfigBean.getMvIssueOpenMinHb();
        AppLogger.e("发布公开mv 需要虎币数"+mvMinHb);
        if(mvMinHb==0){
            mvMinHb=1000000L;
        }
        if(inCome>=mvMinHb){
            isPost=true;
        } else{
            String coinText=  CommonUtils.getWCoinText(String.valueOf(mvMinHb));
            new MvMsgDialog().openShowMVMsgDialog(PostMvVideoActivity.this,
                    "收到礼物"+coinText+"才能发布公开视频快去升级吧！","确定");
        }
        return  isPost;
    }

    private void getPicFrom() {
        PictureSelector.create(PostMvVideoActivity.this)
                .openGallery(PictureMimeType.ofVideo())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_default_style)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .maxSelectNum(1)// 最大图片选择数量 int
                .minSelectNum(0)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .previewVideo(true)// 是否可预览视频 true or false
                .enablePreviewAudio(true) // 是否可播放音频 true or false
                .isCamera(false)// 是否显示拍照按钮 true or false
//                .imageFormat("jpeg")// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                .enableCrop(false)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                .glideOverride(160, 160)// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(4, 3)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(false)// 是否显示gif图片 true or false
//                .compressSavePath(getPath())//压缩图片保存地址
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .openClickSound(true)// 是否开启点击声音 true or false
//                .selectionMedia( )// 是否传入已选图片 List<LocalMedia> list
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .cropCompressQuality(90)// 裁剪压缩质量 默认90 int
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
//                .cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
                .rotateEnabled(false) // 裁剪是否可旋转图片 true or false
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                .videoQuality(0)// 视频录制质量 0 or 1 int
                .videoMaxSecond(3000)// 显示多少秒以内的视频or音频也可适用 int
                .videoMinSecond(2)// 显示多少秒以内的视频or音频也可适用 int
                .recordVideoSecond(1800)//视频秒数录制 默认60s int
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }


    private List<LocalMedia> selectList = new ArrayList<>();//选择的图片集合


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PictureConfig.CHOOSE_REQUEST:
                // 图片选择结果回调
                selectList = PictureSelector.obtainMultipleResult(data);
                if (selectList != null && selectList.size() > 0) {

                    theMvVideoPath = selectList.get(0).getPath();

                    mExtractVideoInfoUtil = new ExtractVideoInfoUtil(theMvVideoPath);
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
                    int extractW = (DeviceUtils.getScreenWidth(this)) / 4;
                    String firstCutVideoPath = mExtractVideoInfoUtil.extractFrames(extractW,OutPutFileDirPath);
                    fl_mv_first_cutImg.setVisibility(View.VISIBLE);
                    Glide.with(this)
                            .load("file://" + firstCutVideoPath)
                            .into(mv_first_cutImg);


                    updateMvVideoFile();
                }
                break;
            case 10:
                //封面选择完毕
                if (data == null) {
                    return;
                }
                theMvVideoImgPath = data.getStringExtra("chooseImg");
                if (TextUtils.isEmpty(theMvVideoImgPath)) {
                    return;
                }
                fl_mv_cutImg.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load("file://" + theMvVideoImgPath)
                        .into(mv_cutImg);

                //开始上传封面
                Bundle bundle = new Bundle();
                bundle.putInt("code", 1003);
                dealHander(bundle);
                break;
        }
    }


    private void updateMvVideoFile() {

        showLoading("准备上传...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(theMvVideoPath);
                String fileName = file.getName();
                String fileSize = "";
                String fileMd5 = "";
                try {
                    fileSize = FileUtils.getFileSize(file) + "";
                    fileMd5 = FileUtils.getFileMD5(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mPresenter.getCncUploadToken(fileName, fileMd5, fileSize);
            }
        }).start();
    }


    CncGetUploadTokenResponse.CncGetUploadTokenData mUploadTokenData;

    private static final long DEFAULT_BLOCK_SIZE = 1 * 1024 * 1024;

    @Override
    public void onGetCncUploadTokenResp(CncGetUploadTokenResponse response1) {


        hideLoading();
        showLoading("正在上传...");
        WCSLogUtil.isEnableLog();
        if (response1.getCode() == 200) {
            HashMap<String, String> callbackBody = new HashMap<>();
            mUploadTokenData = response1.getUploadTokenData();
            com.chinanetcenter.wcs.android.Config.baseUrl = mUploadTokenData.getUploadUrl();
            File file = new File(theMvVideoPath);
            setLoadingText("正在上传，请稍等...");

            FileUploader.upload(PostMvVideoActivity.this,
                    mUploadTokenData.getUploadToken(),
                    file, callbackBody, new FileUploaderListener() {
                        @Override
                        public void onSuccess(int i, JSONObject jsonObject) {

                            AppLogger.e("文件上传成功：" + jsonObject.toString());

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    hideLoading();
                                    onToast("上传成功");
                                    if (isUpLoaded) {
                                        //如果是再次上传成功 则清除封面
                                        theMvVideoImgPath = "";
                                        fl_mv_cutImg.setVisibility(View.GONE);
                                    }

                                    isUpLoaded = true;
                                }
                            });

                        }

                        @Override
                        public void onFailure(OperationMessage operationMessage) {
                            AppLogger.e("文件上传失败" + operationMessage);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    onToast("上传失败");
                                    hideLoading();
                                }
                            });
                        }

                        @Override
                        public void onProgress(UploadFileRequest request, long currentSize, long totalSize) {
                            double a = currentSize;
                            double b = totalSize;
                            final String val = ((int) ((a / b) * 100)) + "%";
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setLoadingText("已上传" + val.trim());
                                }
                            });

                            AppLogger.i("file", "mv已上传：" + (int) ((a / b) * 100) + "");


                        }

                    });


//            FileUploader.sliceUpload("CncGetUpload",
//                    PostMvVideoActivity.this,
//                    mUploadTokenData.getUploadToken(),
//                    file, callbackBody,
//                    new SliceUploaderListener() {
//                        @Override
//                        public void onSliceUploadSucceed(JSONObject jsonObject) {
//                            AppLogger.e("文件上传成功："+jsonObject.toString());
//
//                            hideLoading();
//                            onToast("上传成功");
//                        }
//
//                        @Override
//                        public void onSliceUploadFailured(HashSet<String> hashSet) {
//                            AppLogger.e("文件上传失败");
//                            onToast("上传失败");
//                            hideLoading();
//                        }
//
//                        @Override
//                        public void onProgress(long uploaded, long total) {
//                            super.onProgress(uploaded, total);
//                            double a=uploaded;
//                            double b=total;
//                            String val=((int)((a/b)*100))+"%";
//                            setLoadingText("已上传"+val);
//                            AppLogger.i("file","mv已上传："+(int)((a/b)*100)+"");
//                        }
//                    });

        }

    }


    @Override
    public void notifyCosSignResponce(GetCosSignResponse response, String path) {
        if (response.getCode() == 0) {
            String sign = response.getCosSignData().getSign();
            final String imgName = response.getCosSignData().getImgName();
            try {
                sign = URLDecoder.decode(sign, "UTF-8");
                AppUtils.doUploadCover(PostMvVideoActivity.this, path, sign, imgName, new IUploadTaskListener() {
                    @Override
                    public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
                        PutObjectResult result = (PutObjectResult) cosResult;
                        hideLoading();
                        if (result != null) {
                            Bundle b = new Bundle();
                            b.putString("imgName", imgName);

                            theMvVideoImgServerUrl = AppConstants.cosHeadUrl + imgName;

                            AppLogger.i("上传封面成功：" + theMvVideoImgServerUrl);

                            PictureUtils.deleteFile(new File(OutPutFileDirPath));

                        }
                    }

                    @Override
                    public void onFailed(COSRequest COSRequest, final COSResult cosResult) {
                        hideLoading();
                        Bundle b = new Bundle();
                        b.putInt("code", 1001);

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

        }
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

                                String imagePath = theMvVideoImgPath;
                                String ext = imagePath.substring(imagePath.lastIndexOf(".") + 1).toLowerCase();
                                if (ext.equals("jpeg")) {
                                    ext = "jpg";
                                }
                                PostMvVideoActivity.ReUploadPicTask uploadPicTask =
                                        new PostMvVideoActivity.ReUploadPicTask(PostMvVideoActivity.this);
                                uploadPicTask.execute(ext, imagePath);

                                break;

                            case 1001:
                                onToast("上传出错,请重新上传");
                                hideLoading();

                                break;
                        }
                    }
                });
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
            mPresenter.getCosSign(6, ext, path);
            return "";
        }
    }

}
