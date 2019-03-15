package cn.feihutv.zhibofeihu.ui.mv;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.luck.picture.lib.permissions.RxPermissions;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.BuildConfig;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.db.model.MvDownLog;
import cn.feihutv.zhibofeihu.data.local.model.SysbagBean;
import cn.feihutv.zhibofeihu.data.network.http.model.common.FollowResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetAllMvListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMVCommentListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMVDetailResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMyMVListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMyNeedMVListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GiftMVResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.LikeCommentResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.LikeMVResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.PlayMVResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.PostMVCommentResponse;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.live.OnItemClickListener;
import cn.feihutv.zhibofeihu.ui.me.dowm.MyDownActivity;
import cn.feihutv.zhibofeihu.ui.me.dynamic.OthersCommunityActivity;
import cn.feihutv.zhibofeihu.ui.me.recharge.RechargeActivity;
import cn.feihutv.zhibofeihu.ui.me.vip.MyVipActivity;
import cn.feihutv.zhibofeihu.ui.mv.demand.adapter.MvVideoCommentAdapter;
import cn.feihutv.zhibofeihu.ui.widget.TalkGiftView;
import cn.feihutv.zhibofeihu.ui.widget.dialog.GiftNewDialogView;
import cn.feihutv.zhibofeihu.ui.widget.dialog.GiftNumDialog;
import cn.feihutv.zhibofeihu.ui.widget.dialog.MvMsgDialog;
import cn.feihutv.zhibofeihu.ui.widget.video.FhJZVideoPlayerStandard;
import cn.feihutv.zhibofeihu.utils.AppUtils;
import cn.feihutv.zhibofeihu.utils.CustomDialogUtils;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.FileUtils;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import cn.feihutv.zhibofeihu.utils.ShareUtil;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.feihutv.zhibofeihu.utils.TimeUtil;
import cn.feihutv.zhibofeihu.utils.glideutil.GlideApp;
import cn.feihutv.zhibofeihu.utils.rxdownload.CustomMission;
import cn.feihutv.zhibofeihu.utils.weiget.dialog.RxDialogSureCancel;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import zlc.season.rxdownload3.RxDownload;
import zlc.season.rxdownload3.core.Deleted;
import zlc.season.rxdownload3.core.Downloading;
import zlc.season.rxdownload3.core.Failed;
import zlc.season.rxdownload3.core.Mission;
import zlc.season.rxdownload3.core.Normal;
import zlc.season.rxdownload3.core.Status;
import zlc.season.rxdownload3.core.Succeed;
import zlc.season.rxdownload3.core.Suspend;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static zlc.season.rxdownload3.helper.UtilsKt.dispose;


/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/
 *     desc   : mv 详情页面
 *     version: 1.0
 * </pre>
 */
public class MvVideoDetailsActivity extends BaseActivity implements MvVideoDetailsMvpView,
        View.OnClickListener {

    @Inject
    MvVideoDetailsMvpPresenter<MvVideoDetailsMvpView> mPresenter;

    @BindView(R.id.videoplayer)
    FhJZVideoPlayerStandard videoplayer;

    TextView mv_title;
    TextView mMvDetailTime;
    TextView mMvDetailSee;
    TextView mMvComment;
    TextView mv_detail_follow;
    TextView mv_down;
    TextView mv_share;

    ImageView mMvUserHead;
    ImageView mMvUserIng;
    TextView mMvUserName;

    View mv_detail_like;
    ImageView iv_add_follow;

    TextView mv_detail_comment_num;
    RelativeLayout rl_user;


    @BindView(R.id.mv_detail_not)
    TextView mv_detail_not;

    @BindView(R.id.recycle_view)
    XRecyclerView mRecycleView;
    @BindView(R.id.inputBar)
    TalkGiftView mInputBar;

    GetMVDetailResponse.GetMVDetail mvDetail;

    private Dialog mPickDialog1;  // 分享弹框

    private boolean isPullDown = true;//是否下拉刷新，默认下拉刷新
    private boolean isKeyWordIsShow = false;
    private boolean liveStatus = false;

    private String mvId;
    private String videoId;
    private String mvUrl = "";
    private Disposable disposable;
    CustomMission customMission;

    MvVideoCommentAdapter mMvVideoCommentAdapter;
    String nextOffset = "0";
    String pageSize = "10";
    List<GetMVCommentListResponse.GetMVCommentList> mGetMVCommentLists = new ArrayList<>();
    GetMVCommentListResponse.GetMVCommentList clickItemMvCommentList;

    GetAllMvListResponse.GetAllMvList mGetAllMvList;

    GetMyMVListResponse.GetMyMVList zbMyProductionMv;//我的作品页面
    private Status currentStatus;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_mv_video_details;
    }


    @Override
    protected void initWidget() {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this));

        //Presenter调用初始化
        mPresenter.onAttach(MvVideoDetailsActivity.this);

        requestPermission(WRITE_EXTERNAL_STORAGE);
        //初始化ui
        initUi();
        videoplayer.setDetail_backShow();

        mGetAllMvList = (GetAllMvListResponse.GetAllMvList) getIntent()
                .getSerializableExtra("allMvList");

        if (mGetAllMvList != null) {
            //从视频列表跳转进来
            String url = getIntent().getStringExtra("url");
            videoplayer.setUp(url
                    , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");
            mvId = mGetAllMvList.getMVId() + "";
            videoId = mGetAllMvList.getVideoId();

            //获取mv 详情
            mPresenter.getMVDetail(mvId);

            mRecycleView.refresh();
            liveStatus = mGetAllMvList.getLiveStatus();
        }

        zbMyProductionMv = (GetMyMVListResponse.GetMyMVList) getIntent()
                .getSerializableExtra("zbMyProductionMv");
        if (zbMyProductionMv != null) {
            //主播从我的作品页面跳转进来
            GlideApp.loadImg(MvVideoDetailsActivity.this, zbMyProductionMv.getCover(), R.drawable.bg
                    , videoplayer.thumbImageView);

            TCUtils.showPicWithUrl(MvVideoDetailsActivity.this, mMvUserHead,
                    zbMyProductionMv.getAvatar(), R.drawable.face);

            mMvUserName.setText(zbMyProductionMv.getNickname());
            mMvUserIng.setVisibility(View.GONE);
            mvId = zbMyProductionMv.getMvId();
            videoId = zbMyProductionMv.getVideoId();
            //获取mv 详情
            mPresenter.getMVDetail(mvId);
            mRecycleView.refresh();

        }

        GetMyNeedMVListResponse.GetMyNeedMVList myNeedMVList = (GetMyNeedMVListResponse.GetMyNeedMVList)
                getIntent().getSerializableExtra("userDemandMv");
        //用户 从已完成作品跳转过来
        if (null != myNeedMVList) {

            mvId = myNeedMVList.getMvId();
            videoId = myNeedMVList.getVideoId();
            //获取mv 详情
            mPresenter.getMVDetail(mvId);
            //获取mv 评论列表

            GlideApp.loadImg(MvVideoDetailsActivity.this, myNeedMVList.getCover(), R.drawable.bg
                    , videoplayer.thumbImageView);

            mRecycleView.refresh();

        }

        //从动态跳转过来
        String demand_mvId = getIntent().getStringExtra("mvId");
        String demand_mvImg = getIntent().getStringExtra("mvImg");
        if (!TextUtils.isEmpty(demand_mvId)) {
            mvId = demand_mvId;
            //获取mv 详情
            mPresenter.getMVDetail(mvId);
            GlideApp.loadImg(MvVideoDetailsActivity.this, demand_mvImg, R.drawable.bg
                    , videoplayer.thumbImageView);
            mRecycleView.refresh();
        }

    }


    @Override
    public void onGetDownMvUrlResp(String mvUrl) {

    }


    @Override
    public void onGetMvUrlResp(String url) {
        mvUrl = url;
        createMvDownDisposable();

        videoplayer.setUp(url
                , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");

    }

    //初始化头部 定制view
    private View initMvHeadUI() {
        RelativeLayout view = (RelativeLayout) LayoutInflater.from(MvVideoDetailsActivity.this).inflate
                (R.layout.activity_mv_video_details_head, null, false);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);

        mv_title = view.findViewById(R.id.mv_title);
        mMvDetailTime = view.findViewById(R.id.mv_detail_time);
        mMvDetailSee = view.findViewById(R.id.mv_detail_see);
        mMvComment = view.findViewById(R.id.mv_comment);
        mMvComment.setOnClickListener(this);
        mv_detail_follow = view.findViewById(R.id.mv_detail_follow);
        mv_down = view.findViewById(R.id.mv_down);
        mv_down.setOnClickListener(this);
        mv_share = view.findViewById(R.id.mv_share);
        mv_share.setOnClickListener(this);
        mMvUserHead = view.findViewById(R.id.mv_user_head);
        mMvUserIng = view.findViewById(R.id.mv_user_ing);
        mMvUserName = view.findViewById(R.id.mv_user_name);
        mv_detail_like = view.findViewById(R.id.mv_detail_like);
        mv_detail_like.setOnClickListener(this);
        iv_add_follow = view.findViewById(R.id.iv_add_follow);
        mv_detail_comment_num = view.findViewById(R.id.mv_detail_comment_num);
        rl_user = view.findViewById(R.id.rl_user);
        rl_user.setOnClickListener(this);
        return view;
    }


    private void initUi() {

        mInputBar.setLabaImgGone();
        mv_detail_not.setOnClickListener(this);

        mMvVideoCommentAdapter = new MvVideoCommentAdapter(MvVideoDetailsActivity.this, mGetMVCommentLists);
        mRecycleView.setLayoutManager(new LinearLayoutManager(MvVideoDetailsActivity.this));
        mRecycleView.addHeaderView(initMvHeadUI());

        mRecycleView.setAdapter(mMvVideoCommentAdapter);

        mMvVideoCommentAdapter.setClickCallBack(new MvVideoCommentAdapter.ItemClickCallBack() {
            @Override
            public void onItemClick(int pos) {

            }

            @Override
            public void onUser_nameClick(int pos) {
                if (isKeyWordIsShow) {
                    mInputBar.hideSoftInputFromWindow();
                    return;
                }

                if (mGetMVCommentLists != null && mGetMVCommentLists.size() > 0) {
                    clickItemMvCommentList = mGetMVCommentLists.get(pos);
                    //回复评论
                    mInputBar.messageTextView.setHint("回复@" + clickItemMvCommentList.getNickname() + ":");
                    mInputBar.showSoftInputFromWindow();
                    mInputBar.messageTextView.setFocusable(true);
                    mInputBar.messageTextView.setFocusableInTouchMode(true);
                    mInputBar.messageTextView.requestFocus();
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    isKeyWordIsShow = true;

                }
            }

            @Override
            public void onCommentClick(int pos) {
                if (mGetMVCommentLists != null && mGetMVCommentLists.size() > 0) {
                    clickItemMvCommentList = mGetMVCommentLists.get(pos);
                    //点赞评论
                    if (clickItemMvCommentList.getLiked()) {
                        return;
                    }
                    mPresenter.likeComment(clickItemMvCommentList.getCommentId());
                    clickItemMvCommentList.setLiked(true);
                    clickItemMvCommentList.setLikes(clickItemMvCommentList.getLikes() + 1);
                    mMvVideoCommentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onUserHeadClick(int pos) {

                //点击用户头像 跳转他人社区
                if (mGetMVCommentLists != null && mGetMVCommentLists.size() > 0) {

                    if (!TCUtils.checkGuest(MvVideoDetailsActivity.this)) {
                        clickItemMvCommentList = mGetMVCommentLists.get(pos);
                        Bundle bundle = new Bundle();
                        bundle.putString("userId", clickItemMvCommentList.getUid());
                        goActivity(OthersCommunityActivity.class, bundle);
                    }
                }

            }
        });


        mRecycleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //关闭软键盘
                InputMethodManager imm =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mInputBar.messageTextView.getWindowToken(), 0);
                return false;
            }
        });


        mRecycleView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                //下拉刷新
                isPullDown = true;
                //获取mv 评论列表
                mPresenter.getMVCommentList(mvId + "", "0", pageSize);
                mPresenter.getMVDetail(mvId);
            }

            @Override
            public void onLoadMore() {
                //加载更多
                isPullDown = false;
                //获取mv 评论列表
                mPresenter.getMVCommentList(mvId + "", nextOffset, pageSize);
            }
        });


        mInputBar.setmOnTextSendListener(new TalkGiftView.OnTextSendListener() {
            @Override
            public void onTextSend(String msg, boolean mDanmuOpen) {

                if (!TCUtils.checkGuest(getActivity())) {
                    String commentid = "0";
                    //点击发送
                    if (clickItemMvCommentList != null) {
                        commentid = clickItemMvCommentList.getCommentId();
                    }
                    mPresenter.postMVComment(mvId + "", commentid, msg);
                    mInputBar.hideSoftInputFromWindow();
                }
            }


            @Override
            public void OnEditClick() {

            }

            @Override
            public void giftClick() {
                //显示礼物
                mPresenter.requestBagData(mvId + "");

            }

            @Override
            public void OnClick() {

            }

            @Override
            public void keyBoardHide() {
                isKeyWordIsShow = false;
            }

            @Override
            public void showLogin() {

            }
        });

    }


    @Override
    public void onGetMVCommentListResp(GetMVCommentListResponse response) {
        if (isPullDown) {
            mRecycleView.refreshComplete();
        } else {
            mRecycleView.loadMoreComplete();
        }
        if (response.getCode() == 0) {
            GetMVCommentListResponse.GetMVCommentListData mvCommentListData = response.getGetMVCommentListData();

            List<GetMVCommentListResponse.GetMVCommentList> mvCommentList = mvCommentListData.getGetMVCommentLists();
            if (mvCommentList != null && mvCommentList.size() > 0) {
                nextOffset = mvCommentListData.getNextOffset();
                if (isPullDown) {
                    mGetMVCommentLists.clear();
                }

                mGetMVCommentLists.addAll(mvCommentList);
            } else {
                if (!isPullDown) {
                    mRecycleView.setNoMore(true);
                }
            }

            mMvVideoCommentAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onGetMVDetailResp(GetMVDetailResponse response) {
        if (response.getCode() == 0) {
            mvDetail = response.getGetMVDetail();
            videoId=mvDetail.getVideoId();
            mPresenter.getMVUrl(videoId);
            GlideApp.loadImg(MvVideoDetailsActivity.this, mvDetail.getCover(), R.drawable.bg
                    , videoplayer.thumbImageView);

            TCUtils.showPicWithUrl(MvVideoDetailsActivity.this, mMvUserHead,
                    mvDetail.getUserAvatar(), R.drawable.face);

            mMvUserName.setText(mvDetail.getMasterNickname());

            if (liveStatus) {
                mMvUserIng.setVisibility(View.VISIBLE);
            } else {
                mMvUserIng.setVisibility(View.GONE);
            }

            mv_detail_comment_num.setText("评论：" + mvDetail.getComments());
            mv_detail_not.setText("" + mvDetail.getMasterNickname() + "收到了"
                    + mvDetail.getGiftIncome() + "虎币");

            mMvDetailTime.setText(TimeUtil.getMVTimeYYmmdd(mvDetail.getT()));
            mMvUserName.setText(mvDetail.getMasterNickname());
            TCUtils.showPicWithUrl(MvVideoDetailsActivity.this, mMvUserHead,
                    mvDetail.getMasterAvatar(), R.drawable.face);

            mMvDetailSee.setText(mvDetail.getPlays());
            mMvComment.setText(mvDetail.getLikes() + "");
            if (mvDetail.getLiked()) {
                mMvComment.setClickable(false);
                Drawable drawable = ContextCompat.getDrawable(MvVideoDetailsActivity.this, R.drawable.mv_like_s);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                mMvComment.setCompoundDrawables(drawable, null, null, null);
            }
            mv_title.setText(mvDetail.getTitle());

            if (mvDetail.getMasterUid().equals(mPresenter.getLoinUserId())) {
                //如果主播是当前登录用户 则隐藏关注按钮
                mv_detail_like.setVisibility(View.GONE);
            }

            if (mvDetail.getFollowed()) {
                //已关注
                iv_add_follow.setVisibility(View.GONE);
                mv_detail_follow.setText("已关注");
                mv_detail_like.setClickable(false);

            } else {
                mv_detail_like.setClickable(true);
            }

        }
    }

    @Override
    public void onLikeMVResp(LikeMVResponse response) {
        if (response.getCode() == 0) {

            mMvComment.setText((mvDetail.getLikes() + 1) + "");
            Drawable drawable = ContextCompat.getDrawable(MvVideoDetailsActivity.this, R.drawable.mv_like_s);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mMvComment.setCompoundDrawables(drawable, null, null, null);

        }
    }

    @Override
    public void onLikeCommentResp(LikeCommentResponse response) {
        if (response.getCode() == 0) {

        }
    }

    @Override
    public void onGiftMVResp(GiftMVResponse response) {
        hideLoading();
        if (response.getCode() == 0) {
            onToast("赠送成功");
            mPresenter.getMVDetail(mvId);
        } else {
            onToast("赠送失败");
        }

    }

    @Override
    public void onPostMVCommentResp(PostMVCommentResponse response) {

        if (response.getCode() == 0) {
            onToast("评论成功");
            //获取mv 评论列表
            mPresenter.getMVCommentList(mvId, "0", "20");
            if (mInputBar.messageTextView != null) {
                mInputBar.messageTextView.setHint("说点什么吧...");
            }
            mvDetail.setComments(String.valueOf(Integer.parseInt(mvDetail.getComments()) + 1));
            mv_detail_comment_num.setText("评论：" + mvDetail.getComments());
        } else {
            onToast("提交失败！");
        }
    }


    @Override
    public void onFollowResp(FollowResponce response) {
        if (response.getCode() == 0) {
            iv_add_follow.setVisibility(View.GONE);
            mv_detail_follow.setText("已关注");
            mv_detail_like.setClickable(false);
        } else {
            mv_detail_like.setClickable(true);
        }
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        //销毁之前必须调用
        mPresenter.onDetach();
        dispose(disposable);
        super.onDestroy();
    }


    private void requestPermission(String permission) {
        new RxPermissions(this)
                .request(permission)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (!aBoolean) {
                            finish();
                        }
                    }
                });
    }


    //创建mv下载任务
    private boolean createMvDownDisposable() {

        boolean isInit = true;
        String downUrl = mvUrl;
        String fileFFName = ".mp4";
        if (downUrl.contains(".mp4")) {
            fileFFName = ".mp4";
        } else if (downUrl.contains(".mp3")) {
            fileFFName = ".mp3";
        }

        String saveName = mvId + fileFFName;
        String savePath = FileUtils.getMvDownFileDir();
        Boolean rangeFlag = false;
        String tag = mvId + mPresenter.getLoinUserId();//标记为mvId 加用户id 拼接成主键标记
        MvDownLog userMvDownLog = FeihuZhiboApplication.getApplication()
                .mDataManager.getMvDownLogByMvId(mvId, mPresenter.getLoinUserId());
        if (userMvDownLog != null) {
            downUrl = userMvDownLog.getMvUrl();//如果mv 已经下载过则使用以前的有效连接
        }
        Mission mission = new Mission(downUrl, saveName, savePath);
        mission.setTag(tag);
        customMission = new CustomMission(mission,
                mvDetail.getCover(), mvDetail.getTitle()
                , mvDetail.getMasterNickname(), mvDetail.getMasterAvatar(), -1);
        if (disposable == null) {


            disposable = RxDownload.INSTANCE.create(customMission)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Status>() {
                        @Override
                        public void accept(Status status) throws Exception {
                            //下载监听
                            currentStatus = status;
                            int downState = -1;
                            //下载状态,-1 未下载，0：已下载；1：正在下载;2:已暂停；3：下载失败；4：连接已过期
                            if (currentStatus instanceof Normal) {
                                downState = -1;
                            } else if (currentStatus instanceof Suspend) {
                                //"已暂停");
                                customMission.setDownState(2);
                                RxDownload.INSTANCE.update(customMission);
                            } else if (currentStatus instanceof Failed) {
                                //下载失败");
                                customMission.setDownState(3);
                                RxDownload.INSTANCE.update(customMission);
                            } else if (currentStatus instanceof Downloading) {
                                //正在下载...");

                            } else if (currentStatus instanceof Succeed) {
                                //下载完成跳转下载中心
                                customMission.setDownState(0);
                                RxDownload.INSTANCE.update(customMission);
                            } else if (currentStatus instanceof Deleted) {
                                //"已经删除,重新下载");

                            }

                        }
                    });

            isInit = false;
        }
        return isInit;
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onPlayMVResp(PlayMVResponse response) {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mv_comment:
                //点赞
                if (!TCUtils.checkGuest(getActivity())) {
                    mPresenter.likeMV(mvId + "");
                }
                break;
            case R.id.mv_down:
                //下载视频
                if (!TCUtils.checkGuest(getActivity())) {

                    if(mPresenter.getUserData().getVip() > 0) {
                        //是回应下载
                        mPresenter.getDownMVUrl(mvId);
                        //执行下载任务
                        if (currentStatus instanceof Normal) {
                            startDownMV();
                        } else if (currentStatus instanceof Suspend) {
                            onToast("已暂停");
                        } else if (currentStatus instanceof Failed) {
                            onToast("下载失败");
                        } else if (currentStatus instanceof Downloading) {
                            onToast("正在下载...");
                        } else if (currentStatus instanceof Succeed) {
                            //下载完成跳转下载中心
                            goActivity(MyDownActivity.class);
                        } else if (currentStatus instanceof Deleted) {
                            onToast("已经删除,重新下载");
                            startDownMV();
                        }
                    }else{
                        final MvMsgDialog   mvMsgDialog=   new MvMsgDialog();
                        mvMsgDialog.openShowMVMsgCancelDialog(getActivity(),
                                "         下载失败！\n赶紧成为vip,下载更多精彩MV",
                                "开通会员", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mvMsgDialog.mShowMVMsgDialog.dismiss();
                                        goActivity(MyVipActivity.class);

                                    }
                                });
                    }

                }
                break;
            case R.id.mv_share:
                //分享
                showShareDialog();
                break;
            case R.id.mv_user_head:
                //

                break;
            case R.id.mv_detail_like:
                //关注
                if (!TCUtils.checkGuest(getActivity())) {
                    mPresenter.followUser(mvDetail.getMasterUid());
                    mv_detail_like.setClickable(false);
                }
                break;

            case R.id.mv_detail_not:
                //跳转礼物
                Bundle bundle = new Bundle();
                bundle.putString("mvId", mvId + "");
                goActivity(MyMvGiftActivity.class, bundle);

                break;
            case R.id.iv_share_circle:
                MobclickAgent.onEvent(getActivity(), "10137");
                if (FHUtils.isAppInstalled(getActivity(), "com.tencent.mm")) {
                    share(SHARE_MEDIA.WEIXIN_CIRCLE);
                } else {
                    onToast("未安装微信", Gravity.CENTER, 0, 0);
                }
                break;
            case R.id.iv_share_wx:
                MobclickAgent.onEvent(getActivity(), "10136");
                if (FHUtils.isAppInstalled(getActivity(), "com.tencent.mm")) {
                    share(SHARE_MEDIA.WEIXIN);
                } else {
                    onToast("未安装微信", Gravity.CENTER, 0, 0);
                }
                break;
            case R.id.iv_share_wb:
                MobclickAgent.onEvent(getActivity(), "10140");
                share(SHARE_MEDIA.SINA);
                break;
            case R.id.iv_share_qq:
                MobclickAgent.onEvent(getActivity(), "10138");
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPerssion(1010)) {
                        share(SHARE_MEDIA.QQ);
                    }
                } else {
                    share(SHARE_MEDIA.QQ);
                }
                break;
            case R.id.iv_share_qzone:
                MobclickAgent.onEvent(getActivity(), "10139");
                share(SHARE_MEDIA.QZONE);
                break;
            case R.id.iv_copy:
                onToast("复制链接");
                break;
            case R.id.iv_dowmload:
                if (mPresenter.getUserData().getVip() > 0) {
                    if (!mPresenter.getUserData().isVipExpired()) {
                        // vip正常
                        onToast("弹出MV内容大小");
                    } else {
                        // 已过期
                        showNotVip();
                    }
                } else {
                    // 不是vip
                    showNotVip();
                }
                break;
            case R.id.rl_user:
                if (liveStatus) {
                    AppUtils.startLiveActivity(getActivity(), mvDetail.getMasterUid(), mvDetail.getMasterAvatar(), mvDetail.getBroadCastType(), true, 100);
                } else {
                    if (!TCUtils.checkGuest(MvVideoDetailsActivity.this)) {
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("userId", mvDetail.getMasterUid());
                        goActivity(OthersCommunityActivity.class, bundle1);
                    }
                }
                break;
            default:
                break;
        }
    }


    private void startDownMV() {
        //开始执行下载mv
        String id = mPresenter.getLoinUserId() + mvId;//id 由用户id+mvid 拼接而成昨晚记录的唯一id存储
        MvDownLog userMvDownLog = FeihuZhiboApplication.getApplication()
                .mDataManager.getMvDownLogByMvId(mvId, mPresenter.getLoinUserId());
        if (userMvDownLog == null) {

            RxDownload.INSTANCE.start(customMission).subscribe();
            String validityTime = FeihuZhiboApplication.getApplication()
                    .mDataManager.getSysConfig().getMvDownUrlValidityTime();
            if (TextUtils.isEmpty(validityTime)) {
                validityTime = "3600";
            }
            MvDownLog downLog = new MvDownLog();

            downLog.setId(id);
            downLog.setIcon(mvDetail.getCover());
            downLog.setMvId(mvId);
            downLog.setUserId(mPresenter.getLoinUserId());
            downLog.setMvUrl(mvUrl);
            downLog.setTitle(mvDetail.getTitle());
            downLog.setZbIcon(mvDetail.getMasterAvatar());
            downLog.setZbName(mvDetail.getMasterNickname());
            downLog.setState(1);//设置正在下载
            downLog.setMvDownUrlValidityTime(Long.parseLong(validityTime));
            FeihuZhiboApplication.getApplication()
                    .mDataManager.saveMvDownLog(downLog);

        } else {
            customMission.setUrl(userMvDownLog.getMvUrl());
            RxDownload.INSTANCE.start(customMission).subscribe();
        }
    }


    GiftNewDialogView giftDialogView;

    @Override
    public void showGiftDialog(final List<SysbagBean> sysbagBeanList) {
        if (FeihuZhiboApplication.getApplication().mDataManager.getLiveGiftList().size() == 0) {
            return;
        }
        giftDialogView = new GiftNewDialogView(sysbagBeanList, MvVideoDetailsActivity.this, true, true);
        Window dlgwin = giftDialogView.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        dlgwin.setGravity(Gravity.BOTTOM);
        dlgwin.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dlgwin.setAttributes(lp);
        giftDialogView.show();
        giftDialogView.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
        giftDialogView.setGiftDialogListener(new GiftNewDialogView.GiftDialogListener() {
            @Override
            public void sendGift(int id, int count, boolean isBag) {

                if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                    if (BuildConfig.isForceLoad.equals("1")) {
                        CustomDialogUtils.showQZLoginDialog(MvVideoDetailsActivity.this, false);
                    } else {
                        CustomDialogUtils.showLoginDialog(MvVideoDetailsActivity.this, false);
                    }

                } else {
                    showLoading("正在处理中...");
                    if (isBag) {
                        mPresenter.dealBagSendGift(sysbagBeanList, id, count);
                    } else {
                        mPresenter.dealSendGift(sysbagBeanList, id, count);
                    }
                }

                giftDialogView.dismiss();
            }

            @Override
            public void chargr() {
                if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                    if (BuildConfig.isForceLoad.equals("1")) {
                        CustomDialogUtils.showQZLoginDialog(MvVideoDetailsActivity.this, false);
                    } else {
                        CustomDialogUtils.showLoginDialog(MvVideoDetailsActivity.this, false);
                    }
                    return;
                }
                Intent intent = new Intent(MvVideoDetailsActivity.this, RechargeActivity.class);
                intent.putExtra("fromWhere", "直播间礼物页面");
                startActivity(intent);
                giftDialogView.dismiss();
            }

            @Override
            public void showNumDialog() {
                showGiftNumDialog();
            }
        });
    }

    @Override
    public Activity getActivity() {
        return MvVideoDetailsActivity.this;
    }


    private GiftNumDialog giftNumDialog;

    private void showGiftNumDialog() {
        giftNumDialog = new GiftNumDialog(MvVideoDetailsActivity.this, R.style.InputDialog);
        Window dlgwin = giftNumDialog.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        dlgwin.setGravity(Gravity.BOTTOM);
        dlgwin.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dlgwin.setAttributes(lp);
        giftNumDialog.show();
        giftNumDialog.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (giftDialogView != null) {
                    giftDialogView.setEditText(position);
                }
            }
        });
    }


    /**
     * 分享
     */
    private void showShareDialog() {
        mPickDialog1 = new Dialog(getActivity(), R.style.color_dialog);
        mPickDialog1.setContentView(R.layout.dialog_share_mv);

        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = mPickDialog1.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = (int) (display.getWidth()); //设置宽度
        mPickDialog1.getWindow().setAttributes(lp);

        ImageView ivCircle = (ImageView) mPickDialog1.findViewById(R.id.iv_share_circle);
        ImageView ivWx = (ImageView) mPickDialog1.findViewById(R.id.iv_share_wx);
        ImageView ivWb = (ImageView) mPickDialog1.findViewById(R.id.iv_share_wb);
        ImageView ivQq = (ImageView) mPickDialog1.findViewById(R.id.iv_share_qq);
        ImageView ivQzone = (ImageView) mPickDialog1.findViewById(R.id.iv_share_qzone);
        ImageView ivCopy = (ImageView) mPickDialog1.findViewById(R.id.iv_copy);
        ImageView ivDwonLoad = (ImageView) mPickDialog1.findViewById(R.id.iv_dowmload);

        ivCircle.setOnClickListener(this);
        ivWx.setOnClickListener(this);
        ivWb.setOnClickListener(this);
        ivQq.setOnClickListener(this);
        ivQzone.setOnClickListener(this);
        ivCopy.setOnClickListener(this);
        ivDwonLoad.setOnClickListener(this);

        mPickDialog1.show();
    }

    private void share(SHARE_MEDIA plat) {
        String imgUrl = mGetAllMvList.getCover();
        String nickName = mGetAllMvList.getOwnerNickname();
        String mTitle = mGetAllMvList.getTitle();
        String url = FeihuZhiboApplication.getApplication().mDataManager.getSysConfig().getMvShareUrl() + "?mvId=" + mvId;
        url.replace("https", "http");
        ShareUtil.ShareWeb(getActivity(), nickName, mTitle,
                imgUrl, url, plat, umShareListener);
    }

    UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            showLoading();
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            int to = 0;
            if (share_media.equals(SHARE_MEDIA.WEIXIN_CIRCLE)) {
                to = 1;
            } else if (share_media.equals(SHARE_MEDIA.SINA)) {
                to = 2;
            } else if (share_media.equals(SHARE_MEDIA.WEIXIN)) {
                to = 3;
            } else if (share_media.equals(SHARE_MEDIA.QZONE)) {
                to = 4;
            } else if (share_media.equals(SHARE_MEDIA.QQ)) {
                to = 5;
            }
            onToast("分享成功", Gravity.CENTER, 0, 0);
            hideLoading();
            mPresenter.shareMv(mvId, to);
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            FHUtils.showToast("分享失败");
            hideLoading();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            hideLoading();
        }
    };

    private boolean checkPerssion(int code) {
        if (ActivityCompat.checkSelfPermission(getActivity(), WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 需要弹出dialog让用户手动赋予权限
            ActivityCompat.requestPermissions(getActivity(), new String[]{WRITE_EXTERNAL_STORAGE}, code);
            return false;
        } else {
            return true;
        }
    }

    private void showNotVip() {
        final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(getActivity(), R.style.color_dialog);
        rxDialogSureCancel.setCancel("取消");
        rxDialogSureCancel.setSure("开通会员");
        rxDialogSureCancel.getTvContent().setGravity(Gravity.CENTER);
        rxDialogSureCancel.setContent("下载失败！\n赶紧成为VIP，下载更多精彩MV");

        rxDialogSureCancel.setSureListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 跳到VIP隐藏赠送和收赠记录
                Intent intent = new Intent(getActivity(), MyVipActivity.class);
                intent.putExtra("isFromRoom", true);
                startActivity(intent);
                rxDialogSureCancel.dismiss();
            }
        });

        rxDialogSureCancel.setCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rxDialogSureCancel.dismiss();
            }
        });

        rxDialogSureCancel.show();
    }

}
