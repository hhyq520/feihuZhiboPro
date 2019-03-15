package cn.feihutv.zhibofeihu.ui.me.dynamic;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LogShareResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.me.CommenItem;
import cn.feihutv.zhibofeihu.data.network.http.model.me.DeleteFeedResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LikeFeedResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LoadFeedDetailResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.PhotoInfo;
import cn.feihutv.zhibofeihu.data.network.http.model.me.PostFeedCommentResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.ShareFeedResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseResponse;
import cn.feihutv.zhibofeihu.rxbus.RxBus;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.me.adapter.CommentAdapter;
import cn.feihutv.zhibofeihu.ui.widget.ImagePagerActivity;
import cn.feihutv.zhibofeihu.ui.widget.MultiImageView;
import cn.feihutv.zhibofeihu.ui.widget.TalkGiftView;
import cn.feihutv.zhibofeihu.ui.widget.recyclerloadmore.EndlessRecyclerOnScrollListener;
import cn.feihutv.zhibofeihu.ui.widget.recyclerloadmore.LoadingFooter;
import cn.feihutv.zhibofeihu.ui.widget.wraprecyclerview.WrapRecyclerView;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.ListViewDecoration;
import cn.feihutv.zhibofeihu.utils.ShareUtil;
import cn.feihutv.zhibofeihu.utils.TCConstants;
import cn.feihutv.zhibofeihu.utils.TCGlideCircleTransform;
import cn.feihutv.zhibofeihu.utils.TimeUtil;
import cn.feihutv.zhibofeihu.utils.weiget.dialog.RxDialog;
import cn.feihutv.zhibofeihu.utils.weiget.dialog.RxDialogSureCancel;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/
 *     desc   : 动态详情
 *     version: 1.0
 * </pre>
 */
public class DynamicDetailActivity extends BaseActivity implements DynamicDetailMvpView,
        TalkGiftView.OnTextSendListener, View.OnClickListener {

    @Inject
    DynamicDetailMvpPresenter<DynamicDetailMvpView> mPresenter;

    @BindView(R.id.back_btn)
    ImageView backBtn;
    @BindView(R.id.recycle_view)
    WrapRecyclerView mRecyclerView; // 评论
    @BindView(R.id.inputBar)
    TalkGiftView messageEdit;

    private ImageView headImg;
    private TextView nickName;
    private TextView sendTime;
    private LinearLayout locationView;
    private TextView position;
    private ImageView deleteImg;
    private TextView content;
    private ImageView shareImage;
    private TextView shareCount;
    private TextView commentCount;
    private ImageView zanImg;
    private TextView zanCount;
    private MultiImageView multiImagView;

    private LoadFeedDetailResponse.LoadFeedDetailResponseData mLoadFeedDetailResponseData;
    private CommentAdapter adapter;
    private List<CommenItem> datas = new ArrayList<>();
    private int commentPos = -1;//默认评论，其他回复
    private LoadingFooter mLoadingFooter;
    private LinearLayout mLayout;
    private boolean isLoadMore = false;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_dynamic_detail;
    }


    @Override
    protected void initWidget() {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this));

        //Presenter调用初始化
        mPresenter.onAttach(DynamicDetailActivity.this);


        addHeaderView();
        initrecycleview();
        String feedId = getIntent().getStringExtra("feedId");

        //加载动态详情
        mPresenter.loadFeedDetail(feedId);
        showLoading();
        messageEdit.setGiftImgGone();
        messageEdit.setLabaImgGone();
        messageEdit.setmOnTextSendListener(this);
    }


    @Override
    public void onLoadFeedDetailResp(LoadFeedDetailResponse.LoadFeedDetailResponseData loadFeedDetailResponseData) {
        if (!mPresenter.getLoinUserId().equals(loadFeedDetailResponseData.getUserId())) {
            deleteImg.setVisibility(View.GONE);
        }
        mLoadFeedDetailResponseData = loadFeedDetailResponseData;
        mPresenter.loadCommentList(loadFeedDetailResponseData.getId(), "", 10);
        initView();
    }

    private void initView() {
        if (mLoadFeedDetailResponseData == null) {
            return;
        }

        try {
            Glide.with(this).load(mLoadFeedDetailResponseData.getHeadUrl())
                    .apply(new RequestOptions().placeholder(R.drawable.face)
                            .transform(new TCGlideCircleTransform(this))
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(headImg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        nickName.setText(mLoadFeedDetailResponseData.getNickName());
        sendTime.setText(TimeUtil.converFeedTime(mLoadFeedDetailResponseData.getPublishTime()));
        if (TextUtils.isEmpty(mLoadFeedDetailResponseData.getLocation())) {
            locationView.setVisibility(View.GONE);
        } else {
            locationView.setVisibility(View.VISIBLE);
            position.setText(mLoadFeedDetailResponseData.getLocation());
        }
        content.setText(mLoadFeedDetailResponseData.getContent());
        shareCount.setText(String.valueOf(mLoadFeedDetailResponseData.getForwarding()));
        commentCount.setText(String.valueOf(mLoadFeedDetailResponseData.getComments()));
        zanCount.setText(String.valueOf(mLoadFeedDetailResponseData.getLikes()));
        final List<PhotoInfo> photos = mLoadFeedDetailResponseData.getPhotos();
        if (photos.size() > 0) {
            multiImagView.setVisibility(View.VISIBLE);
            multiImagView.setList(photos);
            multiImagView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    //imagesize是作为loading时的图片size
                    ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());

                    List<String> photoUrls = new ArrayList<String>();
                    for (PhotoInfo photoInfo : photos) {
                        photoUrls.add(photoInfo.getUrl());
                    }
                    ImagePagerActivity.startImagePagerActivity(DynamicDetailActivity.this, photoUrls, position, imageSize);
                }
            });
        } else {
            multiImagView.setVisibility(View.GONE);
        }
        if (mLoadFeedDetailResponseData.isLiked()) {
            zanImg.setImageResource(R.drawable.praise);
        } else {
            zanImg.setImageResource(R.drawable.zan);
        }

    }

    private void addHeaderView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        mLayout = (LinearLayout) inflater.inflate(R.layout.dynamic_detail_recycleview_header, null);
        multiImagView = (MultiImageView) mLayout.findViewById(R.id.multiImagView);
        headImg = (ImageView) mLayout.findViewById(R.id.head_pic);
        nickName = (TextView) mLayout.findViewById(R.id.user_name);
        sendTime = (TextView) mLayout.findViewById(R.id.send_time);
        locationView = (LinearLayout) mLayout.findViewById(R.id.location_view);
        position = (TextView) mLayout.findViewById(R.id.position);
        deleteImg = (ImageView) mLayout.findViewById(R.id.delete_btn);
        content = (TextView) mLayout.findViewById(R.id.dynamic_content);
        shareImage = (ImageView) mLayout.findViewById(R.id.share_img);
        shareCount = (TextView) mLayout.findViewById(R.id.share_count);
        commentCount = (TextView) mLayout.findViewById(R.id.comment_count);
        zanImg = (ImageView) mLayout.findViewById(R.id.zan_img);
        zanCount = (TextView) mLayout.findViewById(R.id.zan_count);
        mRecyclerView.addHeaderView(mLayout);
        deleteImg.setOnClickListener(this);
        shareImage.setOnClickListener(this);
        zanImg.setOnClickListener(this);
    }

    private void initrecycleview() {
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        //这句就是添加我们自定义的分隔线
        mRecyclerView.addItemDecoration(new ListViewDecoration(1));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new CommentAdapter(this);
        adapter.setReplyListener(new CommentAdapter.ReplyListener() {
            @Override
            public void reply(int position) {
                InputMethodManager m = (InputMethodManager) messageEdit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                commentPos = position;
                messageEdit.showSoftInputFromWindow();
                messageEdit.setEditTextTip("@" + datas.get(position).getNickName());
            }

            @Override
            public void headClick(int position) {

                if (!datas.get(position).getUserId().equals(mPresenter.getLoinUserId())) {
                    Intent intent = new Intent(DynamicDetailActivity.this, OthersCommunityActivity.class);
                    intent.putExtra("userId", datas.get(position).getUserId());
                    startActivity(intent);
                }

            }

        });
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        if (mLoadingFooter == null) {
            mLoadingFooter = new LoadingFooter(DynamicDetailActivity.this);
            //adapter.setFooterView(mLoadingFooter);
        }
    }

    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener(2) {
        @Override
        public void onLoadMore(View view) {
            super.onLoadMore(view);
            isLoadMore = true;
            loadMoreDatas();
        }
    };

    private void loadMoreDatas() {
        if (datas == null || datas.size() <= 0) {
            return;
        }
        if (mLoadingFooter.getState() == LoadingFooter.State.Loading || (mLoadingFooter.getState() == LoadingFooter.State.TheEnd)) {
            return;
        }
        mLoadingFooter.setState(LoadingFooter.State.Loading);
        adapter.setFooterView(mLoadingFooter);

        //加载评论列表
        mPresenter.loadCommentList(mLoadFeedDetailResponseData.getId(), datas.get(datas.size() - 1).getId(), 10);

    }


    @Override
    protected void onDestroy() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroy();
    }


    @Override
    public void OnEditClick() {

    }

    @Override
    public void giftClick() {

    }

    @Override
    public void OnClick() {

    }

    @Override
    public void keyBoardHide() {

    }

    @Override
    public void showLogin() {

    }

    @OnClick(R.id.back_btn)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.delete_btn:
                showPopWindow();
                break;
            case R.id.share_img:
                showShareDialog();
                break;
            case R.id.zan_img:
                dianZan();
                break;
            case R.id.btn_share_circle:
                share(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.btn_share_wb:
                share(SHARE_MEDIA.SINA);
                break;
            case R.id.btn_share_wx:
                share(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.btn_share_qzone:
                share(SHARE_MEDIA.QZONE);
                break;
            case R.id.btn_share_qq:
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPerssion()) {
                        share(SHARE_MEDIA.QQ);
                    }
                } else {
                    share(SHARE_MEDIA.QQ);
                }
                break;
            default:
        }
    }

    private void dianZan() {
        if (!mLoadFeedDetailResponseData.isLiked()) {
            mPresenter.likeFeed(mLoadFeedDetailResponseData.getId());
        }
    }

    @Override
    public void onLikeFeedResp(LikeFeedResponse response) {
        if (response.getCode() == 0) {
            mLoadFeedDetailResponseData.setLiked(true);
            int likes = mLoadFeedDetailResponseData.getLikes();
            mLoadFeedDetailResponseData.setLikes(likes + 1);
            zanImg.setImageResource(R.drawable.praise);
            zanCount.setText((likes + 1) + "");
        }
    }

    private boolean checkPerssion() {
        if (ActivityCompat.checkSelfPermission(DynamicDetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 需要弹出dialog让用户手动赋予权限
            ActivityCompat.requestPermissions(DynamicDetailActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1010);
            return false;
        } else {
            return true;
        }
    }

    private void share(SHARE_MEDIA plat) {
        String headUrl = mLoadFeedDetailResponseData.getHeadUrl();
        String nickName = mLoadFeedDetailResponseData.getNickName();
        String content = mLoadFeedDetailResponseData.getContent();
        List<PhotoInfo> mPhotoInfos = mLoadFeedDetailResponseData.getPhotos();
        if (mPhotoInfos != null && mPhotoInfos.size() > 0) {
            ShareUtil.ShareWeb(this, content, nickName + "发布新动态了！",
                    mPhotoInfos.get(0).getUrl(), TCConstants.REGISTER_URL, plat, umShareListener);
        } else {
            ShareUtil.ShareWeb(this, content, nickName + "发布新动态了！",
                    headUrl, TCConstants.REGISTER_URL, plat, umShareListener);
        }
    }

    UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

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
            mPresenter.logShare(4, to);

            FHUtils.showToast("分享成功");
            shareCount.setText((mLoadFeedDetailResponseData.getForwarding() + 1) + "");
            mPresenter.shareFeed(mLoadFeedDetailResponseData.getId());

        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            FHUtils.showToast("分享失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {

        }
    };

    @Override
    public void onShareFeedResp(ShareFeedResponse response) {
        //分享回调
        if (response.getCode() == 0) {
            mLoadFeedDetailResponseData.setForwarding(mLoadFeedDetailResponseData.getForwarding() + 1);
        }
    }

    @Override
    public void feedNotExist() {
        mRecyclerView.removeHeaderView();
        mRecyclerView.setVisibility(View.GONE);
        messageEdit.setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == 1010) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                share(SHARE_MEDIA.QQ);
            } else {
                // Permission Denied
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private void showShareDialog() {
        final Dialog pickDialog3 = new Dialog(this, R.style.floag_dialog);
        pickDialog3.setContentView(R.layout.share_dialog);

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog3.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = (int) (display.getWidth()); //设置宽度
        pickDialog3.getWindow().setAttributes(lp);

        ImageView tv_share_friends = (ImageView) pickDialog3.findViewById(R.id.btn_share_circle);
        ImageView tv_share_weibo = (ImageView) pickDialog3.findViewById(R.id.btn_share_wb);
        ImageView tv_share_wechat = (ImageView) pickDialog3.findViewById(R.id.btn_share_wx);
        ImageView tv_share_qzone = (ImageView) pickDialog3.findViewById(R.id.btn_share_qzone);
        ImageView tv_share_qq = (ImageView) pickDialog3.findViewById(R.id.btn_share_qq);
        tv_share_friends.setOnClickListener(this);
        tv_share_weibo.setOnClickListener(this);
        tv_share_wechat.setOnClickListener(this);
        tv_share_qzone.setOnClickListener(this);
        tv_share_qq.setOnClickListener(this);
        pickDialog3.show();
    }

    private void showPopWindow() {
        final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(DynamicDetailActivity.this, R.style.color_dialog);
        rxDialogSureCancel.setSure("确定");
        rxDialogSureCancel.setContent("确定删除此动态吗？");
        rxDialogSureCancel.setCancel("取消");
        rxDialogSureCancel.setSureListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.deleteFeed(mLoadFeedDetailResponseData.getId());
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

    String message = "";

    @Override
    public void onTextSend(final String msg, boolean mDanmuOpen) {
        message = msg;
        if (mLoadFeedDetailResponseData != null) {
            if (commentPos >= 0) {
                mPresenter.postFeedComment(mLoadFeedDetailResponseData.getId(), msg, datas.get(commentPos).getUserId());
            } else {
                mPresenter.postFeedComment(mLoadFeedDetailResponseData.getId(), msg, "");
            }
        }
    }

    @Override
    public void onPostFeedCommentResp(PostFeedCommentResponse response) {
        if (response.getCode() == 0) {
            isLoadMore = false;
            mPresenter.loadCommentList(mLoadFeedDetailResponseData.getId(), "", 10);
            commentCount.setText((mLoadFeedDetailResponseData.getComments() + 1) + "");
            mLoadFeedDetailResponseData.setComments((mLoadFeedDetailResponseData.getComments() + 1));
        } else {
            onToast("发送失败！", Gravity.CENTER, 0, 0);
        }
    }

    @Override
    public void onLoadCommentListResp(List<CommenItem> list) {

        if (list != null && list.size() > 0) {
            mLoadingFooter.setState(LoadingFooter.State.Normal);
            adapter.removeFooterView();
            if (!isLoadMore) {
                datas.clear();
            }
            datas.addAll(list);

            adapter.setDatas(datas);
        } else {
            //如果返回的数据为空，则显示空白view
            mLoadingFooter.setState(LoadingFooter.State.TheEnd, true);
            adapter.removeFooterView();
            if (list.size() == 0) {
                mLoadingFooter.setState(LoadingFooter.State.TheEnd, true);
                adapter.removeFooterView();
            }
        }

    }


    @Override
    public void onLogShareResp(LogShareResponce response) {

    }

    @Override
    public void onDeleteFeedResp(DeleteFeedResponse response) {
        if (response.getCode() == 0) {
            onToast("删除成功！", Gravity.CENTER, 0, 0);
            RxBus.get().send(RxBusCode.RX_BUS_NOTICE_CODE_DELETE_DYNAMIC);
            finish();
        } else {
            onToast("删除失败，请稍后再试！", Gravity.CENTER, 0, 0);
        }
    }


}
