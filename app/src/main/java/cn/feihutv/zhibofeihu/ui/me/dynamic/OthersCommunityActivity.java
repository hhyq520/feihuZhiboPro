package cn.feihutv.zhibofeihu.ui.me.dynamic;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.BuildConfig;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.LoadOtherUserInfoResponce;
import cn.feihutv.zhibofeihu.rxbus.RxBus;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.live.ReportActivity;
import cn.feihutv.zhibofeihu.ui.me.BaseDynamicFragment;
import cn.feihutv.zhibofeihu.ui.me.OtherDynamicFragment;
import cn.feihutv.zhibofeihu.ui.me.PerPageFragment;
import cn.feihutv.zhibofeihu.ui.me.ShowBigImageActivity;
import cn.feihutv.zhibofeihu.ui.me.mv.MyMvPageFragment;
import cn.feihutv.zhibofeihu.ui.me.news.ChatActivity;
import cn.feihutv.zhibofeihu.ui.widget.scrolllayoutview.ScrollableLayout;
import cn.feihutv.zhibofeihu.utils.CustomDialogUtils;
import cn.feihutv.zhibofeihu.utils.FastBlur;
import cn.feihutv.zhibofeihu.utils.ShareUtil;
import cn.feihutv.zhibofeihu.utils.TCConstants;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.feihutv.zhibofeihu.utils.UiUtil;
import cn.feihutv.zhibofeihu.utils.weiget.dialog.RxDialog;
import cn.feihutv.zhibofeihu.utils.weiget.dialog.RxDialogSureCancel;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/
 *     desc   : 他人社区
 *     version: 1.0
 * </pre>
 */
public class OthersCommunityActivity extends BaseActivity implements OthersCommunityMvpView, PtrHandler, View.OnClickListener {

    @Inject
    OthersCommunityMvpPresenter<OthersCommunityMvpView> mPresenter;

    // 背景
    @BindView(R.id.head_bg)
    ImageView headBg;

    // 头像
    @BindView(R.id.head_img)
    ImageView headImg;

    // 主播id
    @BindView(R.id.host_id)
    TextView hostId;

    // 主播昵称
    @BindView(R.id.host_name)
    TextView hostName;

    // 等级
    @BindView(R.id.img_level)
    ImageView imgLevel;

    // 性别
    @BindView(R.id.person_sex)
    ImageView personSex;

    // 关注人数
    @BindView(R.id.concern_txt)
    TextView concernTxt;

    // 粉丝人数
    @BindView(R.id.fans_txt)
    TextView fansTxt;

    // 个性签名
    @BindView(R.id.host_sign)
    TextView hostSign;

    @BindView(R.id.sliding_tabs)
    TabLayout slidingTabs;

    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @BindView(R.id.sl_root)
    ScrollableLayout slRoot;

    @BindView(R.id.pfl_root)
    PtrClassicFrameLayout pflRoot;

    @BindView(R.id.relativeLayout)
    RelativeLayout mRelativeLayout;

    @BindView(R.id.add_concern_txt)
    TextView conTxt;

    @BindView(R.id.img_vip)
    ImageView img_vip;

    @BindView(R.id.img_beauti)
    ImageView img_beauti;

    private String userId;

    private boolean isHaveCare;

    private CommonFragementPagerAdapter mPagerAdapter;

    private LoadOtherUserInfoResponce.OtherUserInfo mOtherUserInfo;

    private List<BaseDynamicFragment> fragmentList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_others_community;
    }


    @Override
    protected void initWidget() {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this));

        //Presenter调用初始化
        mPresenter.onAttach(OthersCommunityActivity.this);

        Intent intent = getIntent();
        if (intent != null) {
            userId = intent.getStringExtra("userId");
        }
        mPresenter.loadOtherUserInfo(userId);

    }

    private void initView(LoadOtherUserInfoResponce.OtherUserInfo otherUserInfo) {
        UiUtil.initialize(OthersCommunityActivity.this);
        int screenWidth = UiUtil.getScreenWidth();
        int screenHeight = (int) (screenWidth * 0.57);
        mRelativeLayout.setLayoutParams(new LinearLayout.LayoutParams(screenWidth, screenHeight));
        slidingTabs.setTabMode(TabLayout.MODE_FIXED);

        mPagerAdapter = new CommonFragementPagerAdapter(getSupportFragmentManager(), userId, otherUserInfo);
        pflRoot.setEnabledNextPtrAtOnce(true);
        pflRoot.setLastUpdateTimeRelateObject(this);
        pflRoot.setPtrHandler(this);
        pflRoot.setKeepHeaderWhenRefresh(true);
        viewpager.setAdapter(mPagerAdapter);
        viewpager.setOffscreenPageLimit(3);
        viewpager.setCurrentItem(0);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }


            @Override
            public void onPageSelected(int position) {
                slRoot.getHelper().setCurrentScrollableContainer(mPagerAdapter.getFragmentList().get(position));
            }


            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });

        slRoot.getHelper().setCurrentScrollableContainer(mPagerAdapter.getFragmentList().get(0));
        slidingTabs.setupWithViewPager(viewpager);
    }


    @Override
    protected void onDestroy() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroy();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_share_circle:
                share(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.btn_share_wx:
                share(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.btn_share_wb:
                share(SHARE_MEDIA.SINA);
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
            case R.id.btn_share_qzone:
                share(SHARE_MEDIA.QZONE);
                break;
            default:
                break;
        }
    }

    @OnClick({R.id.more_btn, R.id.add_concern, R.id.message_btn, R.id.back_btn, R.id.head_img})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.more_btn:
                showMoreDialog();
                break;
            case R.id.back_btn:
                finish();
                break;
            case R.id.add_concern:
                if (isHaveCare) {
                    // 取消关注
                    mPresenter.unfollow(userId);
                } else {
                    // 添加关注
                    if (mPresenter.isGuestUser()) {
                        if (BuildConfig.isForceLoad.equals("1")) {
                            CustomDialogUtils.showQZLoginDialog(OthersCommunityActivity.this, false);
                        } else {
                            CustomDialogUtils.showLoginDialog(OthersCommunityActivity.this, false);
                        }
                    } else {
                        if (mOtherUserInfo == null) {
                            return;
                        }
                        mPresenter.follow(userId);
                    }
                }
                break;
            case R.id.message_btn:
                MobclickAgent.onEvent(OthersCommunityActivity.this, "10149");
                Intent toChatIntent = new Intent(OthersCommunityActivity.this, ChatActivity.class);
                toChatIntent.putExtra("userId", mOtherUserInfo.getUserId());
                toChatIntent.putExtra("nickName", mOtherUserInfo.getNickName());
                toChatIntent.putExtra("headUrl", mOtherUserInfo.getHeadUrl());
                startActivity(toChatIntent);
                break;
            case R.id.head_img:
                if(mOtherUserInfo!=null) {
                    ShowBigImageActivity.start(OthersCommunityActivity.this, mOtherUserInfo.getHeadUrl(), true, -1, "头像");
                }
                break;
            default:
                break;
        }
    }

    private void share(SHARE_MEDIA plat) {
        String headUrl = mOtherUserInfo.getHeadUrl();
        String nickName = mOtherUserInfo.getNickName();
        String accountId = mOtherUserInfo.getAccountId();
        http:
//img.feihutv.cn//icon/share_default.png
        if (TextUtils.isEmpty(nickName)) {
            nickName = "我";
        } else {
            nickName = "[" + nickName + "]";
        }
        ShareUtil.ShareWeb(this, nickName + "入驻飞虎,想看TA是如何开撩的，赶紧进来看看吧～", nickName + "入驻飞虎直播啦！",
                headUrl, TCConstants.REGISTER_URL, plat, umShareListener);
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
            mPresenter.logShare(to);
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            onToast("分享失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {

        }
    };


    // 点击更多弹框
    private void showMoreDialog() {
        if (mOtherUserInfo == null) {
            return;
        }
        final Dialog moreDialog = new Dialog(OthersCommunityActivity.this, R.style.color_dialog);
        moreDialog.setContentView(R.layout.dialog_other_more);
        TextView share = (TextView) moreDialog.findViewById(R.id.share);
        TextView report = (TextView) moreDialog.findViewById(R.id.report);
        TextView lahei = (TextView) moreDialog.findViewById(R.id.lahei);
        if (mOtherUserInfo.isBlocked()) {
            lahei.setText("取消拉黑");
        } else {
            lahei.setText(getString(R.string.lahei));
        }
        final TextView close = (TextView) moreDialog.findViewById(R.id.btn_cancel);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OthersCommunityActivity.this, ReportActivity.class);
                intent.putExtra("userId", mOtherUserInfo.getUserId());
                startActivity(intent);
                moreDialog.dismiss();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreDialog.dismiss();
                showShareDialog();
            }
        });
        lahei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreDialog.dismiss();
                if (mOtherUserInfo.isBlocked()) {
                    mPresenter.unblock(mOtherUserInfo.getUserId());
                } else {
                    showLaheiDialog();
                }
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreDialog.dismiss();
            }
        });
        int width = getResources().getDisplayMetrics().widthPixels;
        Window dlgwin = moreDialog.getWindow();
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = width; //设置宽度
        moreDialog.getWindow().setAttributes(lp);
        moreDialog.show();
    }


    private void showLaheiDialog() {
        final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(getActivity());
        rxDialogSureCancel.setContent(getString(R.string.lahei_result));
        if (mOtherUserInfo.isBlocked()) {
            rxDialogSureCancel.setSure("移除黑名单");
        } else {
            rxDialogSureCancel.setSure(getString(R.string.lahei));
        }

        rxDialogSureCancel.setCancel("取消");
        rxDialogSureCancel.setSureListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOtherUserInfo.isBlocked()) {
                    mPresenter.unblock(mOtherUserInfo.getUserId());
                } else {
                    mPresenter.block(mOtherUserInfo.getUserId());
                }
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


    private void showShareDialog() {
        final Dialog pickDialog1 = new Dialog(getActivity(), R.style.color_dialog);
        pickDialog1.setContentView(R.layout.share_dialog);

        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog1.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = (int) (display.getWidth()); //设置宽度
        pickDialog1.getWindow().setAttributes(lp);

        ImageView btn_share_circle = (ImageView) pickDialog1.findViewById(R.id.btn_share_circle);
        ImageView btn_share_wx = (ImageView) pickDialog1.findViewById(R.id.btn_share_wx);
        ImageView btn_share_wb = (ImageView) pickDialog1.findViewById(R.id.btn_share_wb);
        ImageView btn_share_qq = (ImageView) pickDialog1.findViewById(R.id.btn_share_qq);
        ImageView btn_share_qzone = (ImageView) pickDialog1.findViewById(R.id.btn_share_qzone);

        btn_share_circle.setOnClickListener(this);
        btn_share_wx.setOnClickListener(this);
        btn_share_wb.setOnClickListener(this);
        btn_share_qq.setOnClickListener(this);
        btn_share_qzone.setOnClickListener(this);

        pickDialog1.show();
    }

    @Override
    public void getDatas(LoadOtherUserInfoResponce.OtherUserInfo otherUserInfo) {
        mOtherUserInfo = otherUserInfo;
        initView(otherUserInfo);

        // 设置基本信息
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Glide.with(this).load(otherUserInfo.getLiveCover())
                    .apply(new RequestOptions()
                            .dontAnimate()
                            .transform(new FastBlur(getActivity(), 10))
                            .placeholder(R.drawable.bg)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .transition(DrawableTransitionOptions.withCrossFade(1000))
                    .into(headBg);
        } else {
            Glide.with(this).load(otherUserInfo.getLiveCover())
                    .apply(new RequestOptions()
                            .dontAnimate()
                            .placeholder(R.drawable.bg)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .transition(DrawableTransitionOptions.withCrossFade(1000))
                    .into(headBg);
        }

        TCUtils.showPicWithUrl(getActivity(), headImg, otherUserInfo.getHeadUrl(), R.drawable.face);
        hostId.setText(otherUserInfo.getAccountId());
        if (TextUtils.isEmpty(otherUserInfo.getNickName())) {
            hostName.setText("主人有点懒，什么都没有留下…");
        } else {
            hostName.setText(otherUserInfo.getNickName());
        }

        TCUtils.showLevelWithUrl(getActivity(), imgLevel, otherUserInfo.getLevel());

        if (otherUserInfo.getGender() == 2) {
            personSex.setVisibility(View.VISIBLE);
            personSex.setImageResource(R.drawable.ss_female);
        } else if (otherUserInfo.getGender() == 1) {
            personSex.setVisibility(View.VISIBLE);
            personSex.setImageResource(R.drawable.ss_male);
        } else {
            personSex.setVisibility(View.GONE);
        }

        concernTxt.setText(String.valueOf(otherUserInfo.getFollows()));
        fansTxt.setText(String.valueOf(otherUserInfo.getFollowers()));

        hostSign.setText(otherUserInfo.getSignature());

        isHaveCare = otherUserInfo.isFollowed();

        if (isHaveCare) {
            conTxt.setText(getString(R.string.concerned));
        } else {
            conTxt.setText(R.string.concern);
        }

        if (otherUserInfo.isLiang()) {
            img_beauti.setVisibility(View.VISIBLE);
        } else {
            img_beauti.setVisibility(View.GONE);
        }

        TCUtils.showVipLevelWithUrl(getActivity(), img_vip, otherUserInfo.getVip(), otherUserInfo.isVipExpired());

    }

    @Override
    public Activity getActivity() {
        return OthersCommunityActivity.this;
    }

    // 关注成功
    @Override
    public void followSucc() {
        conTxt.setText(R.string.concerned);
        isHaveCare = true;
        mOtherUserInfo.setFollowed(true);
        fansTxt.setText(mOtherUserInfo.getFollowers() + 1 + "");
        mOtherUserInfo.setFollowers(mOtherUserInfo.getFollowers() + 1);
        RxBus.get().send(RxBusCode.RX_BUS_NOTICE_CODE_HAVACARE,mOtherUserInfo.getUserId());
    }

    // 取消关注成功
    @Override
    public void unFollow() {
        RxBus.get().send(RxBusCode.RX_BUS_NOTICE_CODE_NOHAVACARE,mOtherUserInfo.getUserId());
        conTxt.setText(getString(R.string.concern));
        isHaveCare = false;
        mOtherUserInfo.setFollowed(false);
        fansTxt.setText(mOtherUserInfo.getFollowers() - 1 + "");
        mOtherUserInfo.setFollowers(mOtherUserInfo.getFollowers() - 1);
    }

    // 添加黑名单成功
    @Override
    public void blockSucc() {
        mOtherUserInfo.setBlocked(true);
    }

    // 移除黑名单成功
    @Override
    public void unBlockSucc() {
        mOtherUserInfo.setBlocked(false);
        onToast("取消成功", Gravity.CENTER, 0, 0);
    }

    public void refreshComplete() {
        if (pflRoot != null) {
            pflRoot.refreshComplete();
        }
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//        if (viewpager.getCurrentItem() == 0 && slRoot.isCanPullToRefresh()) {
//            return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
//        }
        return false;
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        if (mPagerAdapter.getFragmentList().size() > viewpager.getCurrentItem()) {
            mPagerAdapter.getFragmentList().get(viewpager.getCurrentItem()).pullToRefresh();
        }
    }

    private boolean checkPerssion() {
        if (ActivityCompat.checkSelfPermission(OthersCommunityActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 需要弹出dialog让用户手动赋予权限
            ActivityCompat.requestPermissions(OthersCommunityActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1010);
            return false;
        } else {
            return true;
        }
    }

    public class CommonFragementPagerAdapter extends FragmentPagerAdapter {
        String[] titles = {"TA的动态", "MV音乐", "个人主页"};


        public CommonFragementPagerAdapter(FragmentManager fm, String userId, LoadOtherUserInfoResponce.OtherUserInfo mOtherUserInfo) {
            super(fm);

            OtherPageFragment otherPageFragment = OtherPageFragment.newInstance(mOtherUserInfo, userId);

            if (fragmentList.size() < 3) {
                OtherDynamicFragment otherDynamicFragment = OtherDynamicFragment.newInstance(userId);
                MyMvPageFragment myMvPageFragment = MyMvPageFragment.newInstance(userId, true);
                otherDynamicFragment.setOnfreshListener(new OtherDynamicFragment.OnfreshListener() {
                    @Override
                    public void onfreshComplete() {
                        refreshComplete();
                    }
                });
                fragmentList.add(otherDynamicFragment);
                fragmentList.add(myMvPageFragment);
                fragmentList.add(otherPageFragment);

            }
        }

        public List<BaseDynamicFragment> getFragmentList() {
            return fragmentList;
        }


        @Override
        public Fragment getItem(int position) {
            return getCount() > position ? fragmentList.get(position) : null;

        }

        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
