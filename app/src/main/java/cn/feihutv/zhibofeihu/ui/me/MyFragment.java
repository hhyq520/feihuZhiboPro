package cn.feihutv.zhibofeihu.ui.me;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.tencent.cos.model.COSRequest;
import com.tencent.cos.model.COSResult;
import com.tencent.cos.model.PutObjectResult;
import com.tencent.cos.task.listener.IUploadTaskListener;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.db.model.MessageEntity;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadMsgListResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.me.DynamicItem;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetCosSignResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseResponse;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.rxbus.RxBusSubscribe;
import cn.feihutv.zhibofeihu.rxbus.ThreadMode;
import cn.feihutv.zhibofeihu.ui.base.BaseFragment;
import cn.feihutv.zhibofeihu.ui.me.concern.ConcernActivity;
import cn.feihutv.zhibofeihu.ui.me.concern.FansActivity;
import cn.feihutv.zhibofeihu.ui.me.dynamic.*;
import cn.feihutv.zhibofeihu.ui.me.dynamic.MyDynamicFragment;
import cn.feihutv.zhibofeihu.ui.me.mv.MyMvPageFragment;
import cn.feihutv.zhibofeihu.ui.me.news.RecentNewsActivity;
import cn.feihutv.zhibofeihu.ui.me.personalInfo.PersonalInfoActivity;
import cn.feihutv.zhibofeihu.ui.widget.Bimp;
import cn.feihutv.zhibofeihu.ui.widget.BottomBarView;
import cn.feihutv.zhibofeihu.ui.widget.scrolllayoutview.ScrollableLayout;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.AppUtils;
import cn.feihutv.zhibofeihu.utils.FastBlur;
import cn.feihutv.zhibofeihu.utils.FileUtils;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import cn.feihutv.zhibofeihu.utils.TCConstants;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.feihutv.zhibofeihu.utils.UiUtil;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;


/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/
 *     desc   : 我的界面
 *     version: 1.0
 * </pre>
 */
public class MyFragment extends BaseFragment implements MyMvpView, PtrHandler, ScrollableLayout.OnScrollListener, BottomBarView.OnCliclMsg {

    @Inject
    public MyMvpPresenter<MyMvpView> mPresenter;

    @BindView(R.id.head_bg)
    ImageView headBg;

    @BindView(R.id.more_btn)
    ImageView moreBtn;

    @BindView(R.id.head_img)
    ImageView headImg;

    @BindView(R.id.rl)
    RelativeLayout rl;

    @BindView(R.id.host_name)
    TextView hostName;

    @BindView(R.id.person_sex)
    ImageView personSex;

    @BindView(R.id.ll1)
    LinearLayout ll1;

    @BindView(R.id.host_id)
    TextView hostId;

    @BindView(R.id.ll2)
    LinearLayout ll2;

    @BindView(R.id.host_sign)
    TextView hostSign;

    @BindView(R.id.concern_txt)
    TextView concernTxt;

    @BindView(R.id.bottomBar_layout)
    BottomBarView bottomBarView;

    @BindView(R.id.fans_txt)
    TextView fansTxt;

    @BindView(R.id.sliding_tabs)
    TabLayout slidingTabs;

    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @BindView(R.id.concern)
    TextView concern;

    @BindView(R.id.fans)
    TextView fans;

    @BindView(R.id.sl_root)
    ScrollableLayout slRoot;

    @BindView(R.id.pfl_root)
    PtrClassicFrameLayout pflRoot;

    @BindView(R.id.include_relativeLayout)
    RelativeLayout relativeLayout;

    //    @BindView(R.id.send_dynamic)
//    ImageView sendDynamic;
    @BindView(R.id.img_level)
    ImageView imgLevel;

    @BindView(R.id.img_vip)
    ImageView imgVip;

    @BindView(R.id.img_beauti)
    ImageView imgBeauti;

    Unbinder unbinder;
    @BindView(R.id.mine_btn_edit)
    ImageView mineBtnEdit;

    @BindView(R.id.my_tab_driver_left)
    TextView my_tab_driver_left;

    @BindView(R.id.my_tab_driver_right)
    TextView my_tab_driver_right;

    private ArrayList<DynamicItem> datas;
    private List<BaseDynamicFragment> fragmentList = new ArrayList<>();
    private List<LocalMedia> selectList = new ArrayList<>();
    private static final int TAKE_PICTURE = 0x000001;
    private String location;
    private String accountId;
    private int lastY = 0;
    String headUrl;


    public static MyFragment newInstance() {
        Bundle args = new Bundle();
        MyFragment fragment = new MyFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onScroll(int currentY, int maxY) {
//        Log.e("onScroll", currentY + "/" + maxY);
//        if (currentY > lastY || currentY == maxY) {
//            sendDynamic.setVisibility(View.GONE);
//        } else {
//            sendDynamic.setVisibility(View.VISIBLE);
//        }
        lastY = currentY;
    }


    private boolean bPermission = false;
    private static final int CAPTURE_IMAGE_CAMERA = 100;
    private static final int IMAGE_STORE = 200;
    private static final int CROP_CHOOSE = 10;
    private TabPageAdapter tabAdapter;

    private List<BaseDynamicFragment> mTabFragmentList = new ArrayList<>();
    private MyDynamicFragment myDynamicFragment;
    private DynamicPopWindow mDynamicPopWindow;
    private View mTab_my_arrow_down;

    private boolean isTabAllDynamic = true;//是否作为全部动态标识  默认：false 否；

    View mTab1View;//动态tab

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my;
    }


    @Override
    protected void initWidget(View view) {


        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this, view));

        //Presenter调用初始化
        mPresenter.onAttach(MyFragment.this);

        location = mPresenter.getUserData().getLocation();
        accountId = mPresenter.getUserData().getAccountId();

        UiUtil.initialize(getContext());
        int screenWidth = UiUtil.getScreenWidth();
        int screenHeight = (int) (screenWidth * 0.57);
        relativeLayout.setLayoutParams(new LinearLayout.LayoutParams(screenWidth, screenHeight));

        initUI();

        // 点击私信
        bottomBarView.setOnCliclMsg(this);

        int concernCount = mPresenter.getUserData().getFollows();
        String concern = "";
        if (concernCount >= 10000) {
            concern = new BigDecimal((double) (concernCount / 10000)).setScale(1, BigDecimal.ROUND_HALF_DOWN) + "万";
        } else {
            concern = (concernCount) + "";
        }
        concernTxt.setText(concern);
        int fansCount = mPresenter.getUserData().getFollowers();
        String fans = "";
        if (fansCount >= 10000) {
            fans = new BigDecimal((double) (fansCount / 10000)).setScale(1, BigDecimal.ROUND_HALF_DOWN) + "万";
        } else {
            fans = (fansCount) + "";
        }
        fansTxt.setText(fans);
        String vipIconRootPath = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosVipIconRootPath();
        if (mPresenter.getUserData().isLiang()) {
            imgBeauti.setVisibility(View.VISIBLE);
        } else {
            imgBeauti.setVisibility(View.GONE);
        }
        TCUtils.showVipLevelWithUrl(getContext(), imgVip, mPresenter.getUserData().getVip(), mPresenter.getUserData().isVipExpired());
    }


    //
    private void initUI() {
        mDynamicPopWindow = new DynamicPopWindow();
        mDynamicPopWindow.createDialog(getContext());
        // init page
        myDynamicFragment = MyDynamicFragment.newInstance(mPresenter.getLoinUserId());
//        myDynamicFragment.pullToRefresh();//初始化加载数据
        myDynamicFragment.setOnfreshListener(new MyDynamicFragment.OnfreshListener() {
            @Override
            public void onfreshComplete() {
                refreshComplete();
            }
        });
        mTabFragmentList.add(myDynamicFragment);
        MyPerPageFragment perPageFragment = new MyPerPageFragment();

        //我的mv 页面
        MyMvPageFragment myMvPageFragment = new MyMvPageFragment();
        myMvPageFragment.setOnRefreshListener(new MyMvPageFragment.OnRefreshListener() {
            @Override
            public void onRefreshComplete() {
                refreshComplete();
            }
        });
        mTabFragmentList.add(myMvPageFragment);
        mTabFragmentList.add(perPageFragment);
        tabAdapter = new TabPageAdapter(getChildFragmentManager(), getContext(), mTabFragmentList);
        viewpager.setAdapter(tabAdapter);
        slidingTabs.setupWithViewPager(viewpager);
        for (int i = 0; i < mTabFragmentList.size(); i++) {
            TabLayout.Tab tab = slidingTabs.getTabAt(i);
            //注意！！！这里就是添加我们自定义的布局
            tab.setCustomView(tabAdapter.getCustomView(i));
            //这里是初始化时，默认item0被选中
            if (i == 0) {
                mTab1View = tab.getCustomView();
                TextView tabText = ((TextView) mTab1View.findViewById(R.id.tab_text));
                mTab1View.findViewById(R.id.tab_my_arrow).setVisibility(View.VISIBLE);
                tabText.setSelected(true);
                mTab_my_arrow_down = mTab1View.findViewById(R.id.tab_my_arrow_down);
            }
        }

        //关闭pop
        mDynamicPopWindow.mPopTop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (mTab_my_arrow_down != null) {
                    mTab_my_arrow_down.setVisibility(View.GONE);
                    ((ImageView) mTab1View.findViewById(R.id.tab_my_arrow))
                            .setImageDrawable(ResourcesCompat.getDrawable(getResources()
                                    , R.drawable.icon_return, null));
                }

            }
        });
        mDynamicPopWindow.setViewOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) v;
                TextView tabText = ((TextView) mTab1View.findViewById(R.id.tab_text));
                mDynamicPopWindow.dismiss();
                if (isTabAllDynamic) {
                    //加载全部动态
                    isTabAllDynamic = false;
                    myDynamicFragment.loadDynamic(1);
                    textView.setText("全部动态");
                    tabText.setText("我的动态");
                } else {
                    //加载我的动态
                    isTabAllDynamic = true;
                    myDynamicFragment.loadDynamic(0);
                    textView.setText("我的动态");
                    tabText.setText("全部动态");
                }
            }
        });

        final TabLayout.Tab mTab1 = slidingTabs.getTabAt(0);
        mTab1.getCustomView().setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (mTab1.isSelected()) {
                    mTab_my_arrow_down.setVisibility(View.VISIBLE);
                    mDynamicPopWindow.showPopupWindow(v);
                    ((ImageView) mTab1View.findViewById(R.id.tab_my_arrow))
                            .setImageDrawable(ResourcesCompat.getDrawable(getResources()
                                    , R.drawable.icon_unfold, null));
                } else {
                    viewpager.setCurrentItem(0);
                }
                return false;
            }
        });


        slidingTabs.setTabMode(TabLayout.MODE_FIXED);
        slidingTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                int position = tab.getPosition();
                View view = tab.getCustomView();
                TextView tabText = ((TextView) view.findViewById(R.id.tab_text));
                viewpager.setCurrentItem(position);
                switch (position) {
                    case 0:
                        my_tab_driver_left.setVisibility(View.GONE);
                        my_tab_driver_right.setVisibility(View.VISIBLE);
                        ((ImageView) view.findViewById(R.id.tab_my_arrow))
                                .setImageDrawable(ResourcesCompat.getDrawable(getResources()
                                        , R.drawable.tab_my_icon_right, null));
                        break;
                    case 1:
                        my_tab_driver_left.setVisibility(View.GONE);
                        my_tab_driver_right.setVisibility(View.GONE);
                        break;
                    case 2:
                        my_tab_driver_left.setVisibility(View.VISIBLE);
                        my_tab_driver_right.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }

                ((TextView) tab.getCustomView().findViewById(R.id.tab_text)).setSelected(true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    View view = tab.getCustomView();
                    ((ImageView) view.findViewById(R.id.tab_my_arrow))
                            .setImageDrawable(ResourcesCompat.getDrawable(getResources()
                                    , R.drawable.icon_arrow_down, null));

                    view.findViewById(R.id.tab_my_arrow_down).setVisibility(View.GONE);
                }
                ((TextView) tab.getCustomView().findViewById(R.id.tab_text)).setSelected(false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        pflRoot.setEnabledNextPtrAtOnce(true);
        pflRoot.setLastUpdateTimeRelateObject(this);
        pflRoot.setPtrHandler(this);
        pflRoot.setKeepHeaderWhenRefresh(true);
        slRoot.setOnScrollListener(this);
        viewpager.setOffscreenPageLimit(3);
        viewpager.setCurrentItem(0);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }


            @Override
            public void onPageSelected(int position) {
                slRoot.getHelper().setCurrentScrollableContainer(tabAdapter.getItem(position));
            }


            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });

        slRoot.getHelper().setCurrentScrollableContainer(tabAdapter.getItem(0));
    }


    // 点击私信
    @Override
    public void clickMsg() {
        MobclickAgent.onEvent(getContext(), "10148");
        SharePreferenceUtil.saveSeesionInt(getContext(), "news_count", 0);
        startActivity(new Intent(getActivity(), RecentNewsActivity.class));
    }


    @Override
    protected void lazyLoad() {

    }


    @Override
    public void onDestroyView() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroyView();
    }


    public void refreshComplete() {
        if (pflRoot != null) {
            pflRoot.refreshComplete();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LoadUserDataBaseResponse.UserData userData = mPresenter.getUserData();
        bottomBarView.setMessageCount(SharePreferenceUtil.getSessionInt(getContext(), "news_count"));
        String singature = userData.getSignature();
        if (singature.length() == 0) {
            hostSign.setText("这家伙很懒，什么都没留下...");
        } else {
            hostSign.setText(singature);
        }
        TCUtils.showVipLevelWithUrl(getContext(), imgVip, userData.getVip(), userData.isVipExpired());
        hostId.setText(String.valueOf(userData.getAccountId()));
        hostName.setText(userData.getNickName());
        int level = userData.getLevel();
        TCUtils.showLevelWithUrl(getContext(), imgLevel, level);
        TCUtils.showPicWithUrl(getContext(), headImg, userData.getHeadUrl(), R.drawable.face);
        headUrl = SharePreferenceUtil.getSession(getContext(), "PREF_KEY_LIVECOVER");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Glide.with(this).load(headUrl)
                    .apply(new RequestOptions()
                            .dontAnimate()
                            .transform(new FastBlur(getContext(), 10))
                            .placeholder(R.drawable.bg)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .transition(DrawableTransitionOptions.withCrossFade(1000))
                    .into(headBg);
        } else {
            Glide.with(this).load(headUrl)
                    .apply(new RequestOptions()
                            .dontAnimate()
                            .placeholder(R.drawable.bg)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .transition(DrawableTransitionOptions.withCrossFade(1000))
                    .into(headBg);
        }
        int gender = userData.getGender();
        switch (gender) {
            case 2:
                personSex.setVisibility(View.VISIBLE);
                personSex.setImageResource(R.drawable.ss_female);
                break;
            case 1:
                personSex.setVisibility(View.VISIBLE);
                personSex.setImageResource(R.drawable.ss_male);
                break;
            case 0:
            default:
                personSex.setVisibility(View.GONE);
                break;
        }

        mPresenter.loadUserDataBase();

    }

    @Override
    public void onLoadUserDataBaseResp(LoadUserDataBaseResponse resp) {

        if (resp.getCode() == 0) {

            int follows = resp.getUserData().getFollows();
            int followers = resp.getUserData().getFollowers();
            String concernString = "";
            String fansString = "";
            if (follows >= 10000) {
                concernString = new BigDecimal((double) (follows / 10000)).setScale(1, BigDecimal.ROUND_HALF_DOWN) + "万";
            } else {
                concernString = (follows) + "";
            }
            if (followers >= 10000) {
                fansString = new BigDecimal((double) (followers / 10000)).setScale(1, BigDecimal.ROUND_HALF_DOWN) + "万";
            } else {
                fansString = (followers) + "";
            }
            fansTxt.setText(fansString);
            concernTxt.setText(concernString);
        }
    }

    @OnClick(R.id.more_btn)
    public void onClick() {

        startActivityForResult(new Intent(getActivity(), SendDynamicActivity.class), 1005);
    }


    @OnClick({R.id.concern, R.id.fans, R.id.concern_txt, R.id.fans_txt, R.id.mine_btn_edit, R.id.head_bg, R.id.head_img})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.concern:
                startActivity(new Intent(getActivity(), ConcernActivity.class));
                break;
            case R.id.fans:
                startActivity(new Intent(getActivity(), FansActivity.class));
                break;
            case R.id.concern_txt:
                startActivity(new Intent(getActivity(), ConcernActivity.class));
                break;
            case R.id.fans_txt:
                startActivity(new Intent(getActivity(), FansActivity.class));
                break;
            case R.id.mine_btn_edit:
                MobclickAgent.onEvent(getContext(), "10112");
                startActivity(new Intent(getContext(), PersonalInfoActivity.class));
                break;
            case R.id.head_bg:
                ShowBigImageActivity.start(getContext(), mPresenter.getUserData().getLiveCover(), false, 2, "封面");
                break;
            case R.id.head_img:
                ShowBigImageActivity.start(getContext(), mPresenter.getUserData().getHeadUrl(), false, 1, "头像");
                break;
            default:
                break;
        }
    }

    /**
     * 检查裁剪图像相关的权限
     *
     * @return 权限不足返回false，否则返回true
     */
    private boolean checkCropPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
//            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(PersonalInfoActivity.this, Manifest.permission.READ_PHONE_STATE)) {
//                permissions.add(Manifest.permission.READ_PHONE_STATE);
//            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (permissions.size() != 0) {
                ActivityCompat.requestPermissions(getActivity(),
                        permissions.toArray(new String[0]),
                        TCConstants.WRITE_PERMISSION_REQ_CODE);
                return false;
            }
        }

        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 102:
                if (myDynamicFragment != null) {
                    myDynamicFragment.pullToRefresh();
                }
                break;
            case 1005:
                if (viewpager != null) {
                    viewpager.setCurrentItem(0);
                }
                if (myDynamicFragment != null) {
                    myDynamicFragment.pullToRefresh();
                }
                break;
            default:
                break;
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
        switch (requestCode) {
            case TCConstants.WRITE_PERMISSION_REQ_CODE:
                for (int ret : grantResults) {
                    if (ret != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                bPermission = true;
                break;
            default:
                break;
        }
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//        AppLogger.e("checkCanDoRefresh--------------======isCanPullToRefresh=="+slRoot.isCanPullToRefresh());
//        if (viewpager.getCurrentItem() == 0 && slRoot.isCanPullToRefresh()) {
//            return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
//        }
        return false;
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        //主页面开始刷新
        AppLogger.e("onRefreshBegin");
        refreshComplete();
        tabAdapter.getItem(viewpager.getCurrentItem()).pullToRefresh();

//        if (tabAdapter.getCount() > viewpager.getCurrentItem()) {
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    // 接收更换靓号的通知
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_NOTICE_ACCOUNTID_CHANGE, threadMode = ThreadMode.MAIN)
    public void onReceivePositioncurrentitem(String accountId) {

        hostId.setText(accountId);
    }

    //收到消息
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_NOTICE_SECRET_MESSAGE, threadMode = ThreadMode.MAIN)
    public void onReceiveLoadMsgListDataPush(LoadMsgListResponce.LoadMsgListData pushData) {
        bottomBarView.addMsg();

    }

    // 接收更换头像成功的通知
    @RxBusSubscribe(code = RxBusCode.RX_BUS_NOTICE_CODE_MODIFY_HEAD, threadMode = ThreadMode.MAIN)
    public void onReceiveModifyHead(String headUrl) {
        TCUtils.showPicWithUrl(getContext(), headImg, headUrl, R.drawable.face);
    }

    // 接收更换封面成功的通知
    @RxBusSubscribe(code = RxBusCode.RX_BUS_NOTICE_CODE_MODIFY_COVER, threadMode = ThreadMode.MAIN)
    public void onReceiveModifyCover(String coverUrl) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Glide.with(this).load(coverUrl)
                    .apply(new RequestOptions()
                            .dontAnimate()
                            .transform(new FastBlur(getContext(), 10))
                            .placeholder(R.drawable.bg)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .transition(DrawableTransitionOptions.withCrossFade(1000))
                    .into(headBg);
        } else {
            Glide.with(this).load(coverUrl)
                    .apply(new RequestOptions()
                            .dontAnimate()
                            .placeholder(R.drawable.bg)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .transition(DrawableTransitionOptions.withCrossFade(1000))
                    .into(headBg);
        }
    }
}
