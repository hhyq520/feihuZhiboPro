package cn.feihutv.zhibofeihu.ui.widget;

import android.animation.Animator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.umeng.analytics.MobclickAgent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.db.model.SysGiftNewBean;
import cn.feihutv.zhibofeihu.data.db.model.SysMountNewBean;
import cn.feihutv.zhibofeihu.data.local.model.TCChatEntity;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomContriRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomContriResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.GetExtGameIconRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.GetExtGameIconResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.LoadOtherRoomsResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameListResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameStatusRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameStatusResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.JoinRoomResponce;
import cn.feihutv.zhibofeihu.ui.live.OnStrClickListener;
import cn.feihutv.zhibofeihu.ui.live.adapter.GameImageAdapter;
import cn.feihutv.zhibofeihu.ui.live.adapter.TCUserContriListAdapter;
import cn.feihutv.zhibofeihu.ui.widget.DanmuBase.DanmakuActionManager;
import cn.feihutv.zhibofeihu.ui.widget.DanmuBase.DanmakuActionPcLandManager;
import cn.feihutv.zhibofeihu.ui.widget.DanmuBase.DanmakuChannel;
import cn.feihutv.zhibofeihu.ui.widget.DanmuBase.DanmakuLandChannel;
import cn.feihutv.zhibofeihu.ui.widget.DanmuBase.DanmuModel;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.AppUtils;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.NumAnim;
import cn.feihutv.zhibofeihu.utils.TCDanmuMgr;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.ui.widget.DanmakuView;

/**
 * Created by huanghao on 2017/4/13.
 */

public class LivePlayInPCLandView extends RelativeLayout {

    @BindView(R.id.root_view)
    RelativeLayout rlControllLayerLand;
    @BindView(R.id.btn_linearlayout)
    LinearLayout btnLinearlayout;
    @BindView(R.id.btn_right_land)
    LinearLayout btnRightLand;
    //    @BindView(R.id.danmakuView)
//    DanmakuView danmakuView;
    @BindView(R.id.mHeadIcon_land)
    ImageView mHeadIconLand;
    @BindView(R.id.tv_broadcasting_time_land)
    TextView tvBroadcastingTimeLand;
    @BindView(R.id.text_member)
    TextView tvMemberCountsLand;
    @BindView(R.id.ctl_btnSwitch_land)
    ImageView ctlBtnSwitchLand;
    @BindView(R.id.rv_user_avatar_land)
    RecyclerView mUserAvatarList;
    @BindView(R.id.img_live_exit_land)
    ImageView imgLiveExitLand;
    @BindView(R.id.tv_hubi_land)
    TextView tvHubiLand;
    @BindView(R.id.ll_hubi_land)
    LinearLayout llHubiLand;
    @BindView(R.id.tv_pc_account_land)
    TextView tvPcAccountLand;
    @BindView(R.id.count_menber_land)
    TextView countMember;
    @BindView(R.id.btn_msg_land)
    ImageView btnMsgLand;
    @BindView(R.id.btn_share_land)
    Button btnShareLand;
    @BindView(R.id.btn_refresh_land)
    Button btnRefreshLand;
    @BindView(R.id.giftcontent)
    LinearLayout giftcontent;
    @BindView(R.id.hb)
    RelativeLayout hb;
    @BindView(R.id.btn_cloase_danmu)
    ImageView btnCloaseDanmu;
    @BindView(R.id.btn_game)
    Button btnGame;
    @BindView(R.id.labaView)
    DanmakuView labaView;
    @BindView(R.id.text_xiuxi_land)
    TextView textXiuxiLand;
    @BindView(R.id.line_land)
    LinearLayout lineLand;
    @BindView(R.id.recommend_head_image_left_land)
    ImageView recommendHeadImageLeftLand;
    @BindView(R.id.recommend_nickname_left_land)
    TextView recommendNicknameLeftLand;
    @BindView(R.id.recommend_count_left_land)
    TextView recommendCountLeftLand;
    @BindView(R.id.recommend_head_image_right_land)
    ImageView recommendHeadImageRightLand;
    @BindView(R.id.recommend_nickname_right_land)
    TextView recommendNicknameRightLand;
    @BindView(R.id.recommend_count_right_land)
    TextView recommendCountRightLand;
    @BindView(R.id.recommend_view_land)
    RelativeLayout recommendViewLand;
    @BindView(R.id.img_t1_land)
    ImageView imgT1Land;
    @BindView(R.id.img_t2_land)
    ImageView imgT2Land;
    @BindView(R.id.img_t3_land)
    ImageView imgT3Land;
    @BindView(R.id.img_t4_land)
    ImageView imgT4Land;
    @BindView(R.id.caichi_time_left_land)
    LinearLayout caichiTimeLeftLand;
    @BindView(R.id.text_caichi_open_land)
    TextView text_caichi_open_land;
    @BindView(R.id.msg_new_land)
    ImageView msgNewLand;
    @BindView(R.id.pic_ly_land)
    ImageView picLyLand;
    @BindView(R.id.pic_jc_land)
    ImageView picJcLand;
    @BindView(R.id.member_frm)
    FrameLayout memberFrm;
    @BindView(R.id.rec_left_land)
    LinearLayout recLeftLand;
    @BindView(R.id.rec_right_land)
    LinearLayout recRightLand;
    @BindView(R.id.dan_vip)
    DanmakuChannel danVip;
    @BindView(R.id.dan_mu1)
    DanmakuLandChannel danMu1;
    @BindView(R.id.dan_mu2)
    DanmakuLandChannel danMu2;

    //弹幕
//    private TCDanmuMgr mDanmuMgr;
    private TCDanmuMgr mDanmrLabaMgr;
    private TCUserContriListAdapter mAvatarListAdapter;
    private Context mContext;
    private View rootView;
    private boolean isClicked = false;
    private boolean isShow = true;
    private boolean isBarrOpen = true;
    /**
     * 动画相关
     */
    private NumAnim giftNumAnim;
    private TranslateAnimation inAnim;
    private TranslateAnimation outAnim;
    private JoinRoomResponce.JoinRoomData roomInfo;
    private List<View> giftViewCollection = new ArrayList<View>();
    private DanmakuActionManager danmakuActionManager;
    private DanmakuActionPcLandManager danmakuActionManage1;
    private DanmakuActionPcLandManager danmakuActionManager2;

    public interface OnHorControllListener {
        void fullScreen();

        void headImageClick(String userId);

        void switchClick();

        void share();

        void sendgift();

        void msgClick();

        void onClickMsgBnt();

        void shakeClick(GetGameStatusResponce.GameStatusData json);

        void jincaiClick(int style, GetGameStatusResponce.GameStatusData json);

        void caichiClick();

        void recClick(int pos);

        void danmuClick(int id);

        void showWeekStarDialog(String url);

        void showOnlineDialog();

        void showGameDialog();
    }

    private OnHorControllListener mListener;

    public void setOnHorControllListener(OnHorControllListener listener) {
        mListener = listener;
    }


    public LivePlayInPCLandView(Context context) {
        this(context, null, 0);
    }

    public LivePlayInPCLandView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LivePlayInPCLandView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    public void setTcRoomInfo(JoinRoomResponce.JoinRoomData info) {
        roomInfo = info;
    }

    public boolean onTouchEvent(boolean isVertical, MotionEvent event) {
        if (!isVertical) {
            if (isClicked) {
                btnLinearlayout.setVisibility(VISIBLE);
                btnRightLand.setVisibility(VISIBLE);
                isClicked = false;
                isShow = true;
            } else {
                if (isShow) {
                    rlControllLayerLand.animate().alpha(0).setDuration(1000).setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            rlControllLayerLand.setAlpha(1f);
                            rlControllLayerLand.setVisibility(INVISIBLE);
                            isShow = false;
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    }).start();
                } else {
                    rlControllLayerLand.setVisibility(VISIBLE);
                    isShow = true;
                }

            }
        }
        return onTouchEvent(event);
    }

    public TCDanmuMgr getmDanmuMgr() {
        return mDanmrLabaMgr;
    }

    private boolean isone=true;
    public void reciveMessage(TCChatEntity chatEntity) {
//        if (mDanmuMgr != null && isBarrOpen) {
//            mDanmuMgr.addDanmu(chatEntity);
//        }
        if(isBarrOpen) {
            if(isone) {
                danmakuActionManage1.addDanmu(chatEntity);
            }else {
                danmakuActionManager2.addDanmu(chatEntity);
            }
            isone=!isone;
        }
    }


    public void setReccommendGone() {
        recommendViewLand.setVisibility(GONE);
    }

    public void setRecommendView(List<LoadOtherRoomsResponce.OtherRoomData> datas) {
        recommendViewLand.setVisibility(View.VISIBLE);
        String userCount = "";
        if (datas.get(0).getOnlineUserCnt() >= 10000) {
            userCount = new BigDecimal((double) datas.get(0).getOnlineUserCnt() / 10000).setScale(1, BigDecimal.ROUND_HALF_DOWN) + "万人";
        } else {
            userCount = datas.get(0).getOnlineUserCnt() + "人";
        }
        recommendCountLeftLand.setText("在线:" + userCount);
        TCUtils.showPicWithUrl(getContext(), recommendHeadImageLeftLand, datas.get(0).getHeadUrl(), R.drawable.face);
        recommendNicknameLeftLand.setText(datas.get(0).getNickName());

        String userCount1 = "";
        if (datas.get(0).getOnlineUserCnt() >= 10000) {
            userCount1 = new BigDecimal((double) datas.get(1).getOnlineUserCnt() / 10000).setScale(1, BigDecimal.ROUND_HALF_DOWN) + "万人";
        } else {
            userCount1 = datas.get(1).getOnlineUserCnt() + "人";
        }
        recommendCountRightLand.setText("在线:" + userCount1);
        TCUtils.showPicWithUrl(getContext(), recommendHeadImageRightLand, datas.get(1).getHeadUrl(), R.drawable.face);
        recommendNicknameRightLand.setText(datas.get(1).getNickName());
    }


    public void setRlControllLayerLand() {
        btnLinearlayout.setVisibility(VISIBLE);
        btnRightLand.setVisibility(VISIBLE);
        imgLiveExitLand.setVisibility(VISIBLE);
        memberFrm.setVisibility(VISIBLE);
    }


    public ImageView getHeadImg() {
        return mHeadIconLand;
    }

    public void getNickName(String text) {
        tvBroadcastingTimeLand.setText(text);
    }

    public void getTvMemberCountsLand(String text) {
        tvMemberCountsLand.setText(text);
    }

    public void setImgMsgNewVis(boolean isVis) {
        if (isVis) {
            msgNewLand.setVisibility(VISIBLE);
        } else {
            msgNewLand.setVisibility(GONE);
        }
    }

    public TextView getTvHubiLand() {
        return tvHubiLand;
    }

    public TextView getTvPcAccountLand() {
        return tvPcAccountLand;
    }


    public void setSwitchView(boolean haveCare) {

        if (haveCare) {
            // 已关注
            ctlBtnSwitchLand.setVisibility(GONE);
            //ctlBtnSwitchLand.setImageResource(R.drawable.icon_havecare);
        } else {
            // 未关注
            ctlBtnSwitchLand.setVisibility(VISIBLE);
            ctlBtnSwitchLand.setImageResource(R.drawable.icon_focus);
        }
    }

    List<LoadRoomContriResponce.RoomContriData> contributeModels = new ArrayList<LoadRoomContriResponce.RoomContriData>();

    private void initView() {
        rootView = View.inflate(mContext, R.layout.activity_pc_play_land, this);
        ButterKnife.bind(this, rootView);
//        mDanmuMgr = new TCDanmuMgr(FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean(),
//                FeihuZhiboApplication.getApplication().mDataManager.getSysGiftNew(), mContext, false, true, false);
        mDanmrLabaMgr = new TCDanmuMgr(FeihuZhiboApplication.getApplication().mDataManager.getSysConfig(),
                FeihuZhiboApplication.getApplication().mDataManager.getSysGiftNew(), mContext, false, true, false);
//        mDanmuMgr.setDanmakuView(danmakuView);
        mDanmrLabaMgr.setDanmakuView(labaView);
        danmakuActionManager = new DanmakuActionManager();
        danmakuActionManage1 = new DanmakuActionPcLandManager();
        danmakuActionManager2 = new DanmakuActionPcLandManager();
        danVip.setIsLand();
        danVip.setDanAction(danmakuActionManager);
        danMu1.setDanAction(danmakuActionManage1);
        danMu2.setDanAction(danmakuActionManager2);
        danmakuActionManager.addChannel(danVip);
        danmakuActionManage1.addChannel(danMu1);
        danmakuActionManager2.addChannel(danMu2);
        labaView.setOnDanmakuClickListener(new IDanmakuView.OnDanmakuClickListener() {
            @Override
            public void onDanmakuClick(BaseDanmaku latest) {
                if (mListener != null) {
                    mListener.danmuClick(latest.userId);
                }
            }

            @Override
            public void onDanmakuClick(IDanmakus danmakus) {

            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mUserAvatarList.setLayoutManager(linearLayoutManager);
        mUserAvatarList.getItemAnimator().setChangeDuration(0);
        ((SimpleItemAnimator)mUserAvatarList.getItemAnimator()).setSupportsChangeAnimations(false);
        mAvatarListAdapter = new TCUserContriListAdapter(mContext, "1");
        mAvatarListAdapter.setHasStableIds(true);
        mAvatarListAdapter.setOnItemClickListener(new OnStrClickListener() {
            @Override
            public void onItemClick(String useId) {
                if (mListener != null) {
                    mListener.headImageClick(useId);
                }
            }
        });
        mUserAvatarList.setAdapter(mAvatarListAdapter);

        giftNumAnim = new NumAnim();
        inAnim = (TranslateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.gift_in);
        outAnim = (TranslateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.gift_out);
        clearTiming();
    }

    public void setVipDanmu(DanmuModel model) {
        danmakuActionManager.addDanmu(model);
    }


    public void sortContriList(String userid, int contri, String headurl) {
        if (contributeModels.size() == 0) {
            LoadRoomContriResponce.RoomContriData contributeModel = new LoadRoomContriResponce.RoomContriData();
            contributeModel.setUserId(userid);
            contributeModel.setHB(contri);
            contributeModel.setHeadUrl(headurl);
            contributeModels.add(contributeModel);
            if (mAvatarListAdapter != null) {
                mAvatarListAdapter.setDatas(contributeModels);
            }
        } else {
            if (isExistContriList(userid, contri)) {
                sortContri();
            } else {
                if (contributeModels.size() < 20) {
                    LoadRoomContriResponce.RoomContriData contributeModel = new LoadRoomContriResponce.RoomContriData();
                    contributeModel.setUserId(userid);
                    contributeModel.setHB(contri);
                    contributeModel.setHeadUrl(headurl);
                    contributeModels.add(contributeModel);
                    sortContri();
                } else {
                    if (contributeModels.get(19).getHB() < contri) {
                        contributeModels.remove(19);
                        LoadRoomContriResponce.RoomContriData contributeModel = new LoadRoomContriResponce.RoomContriData();
                        contributeModel.setUserId(userid);
                        contributeModel.setHB(contri);
                        contributeModel.setHeadUrl(headurl);
                        contributeModels.add(contributeModel);
                        sortContri();
                    }
                }
            }
        }
    }

    private boolean isExistContriList(String userId, int contri) {
        boolean isExist = false;
        for (LoadRoomContriResponce.RoomContriData item : contributeModels) {
            try {
                if (item.getUserId().equals(userId)) {
                    isExist = true;
                    item.setHB(contri);
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return isExist;
            }
        }
        return isExist;
    }

    public void loadRoomCri() {
        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .doLoadRoomContriApiCall(new LoadRoomContriRequest("", 0))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoadRoomContriResponce>() {
                    @Override
                    public void accept(@NonNull LoadRoomContriResponce responce) throws Exception {
                        if (responce.getCode() == 0) {
                            contributeModels = responce.getRoomContriDataList();
                            mAvatarListAdapter.setDatas(contributeModels);
                        } else {
                            AppLogger.e(responce.getErrMsg());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));
    }

    private void sortContri() {
        try {
            if (contributeModels.size() > 0)
                Collections.sort(contributeModels, new Comparator<LoadRoomContriResponce.RoomContriData>() {
                    @Override
                    public int compare(LoadRoomContriResponce.RoomContriData lhs, LoadRoomContriResponce.RoomContriData rhs) {
                        long sort1 = lhs.getHB();
                        long sort2 = rhs.getHB();
                        return (sort1 == sort2 ? 0 : (sort1 < sort2 ? 1 : -1));
                    }
                });
            if (mAvatarListAdapter != null) {
                mAvatarListAdapter.notifyItemRangeChanged(0,contributeModels.size());
//                mAvatarListAdapter.setDatas(contributeModels);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteContriList(String uid) {
        try {
            if (TextUtils.isEmpty(uid)) {
                return;
            }
            if (contributeModels.size() > 0) {
                for (LoadRoomContriResponce.RoomContriData item : contributeModels) {
                    if (item.getUserId().equals(uid)) {
                        contributeModels.remove(item);
                        if (mAvatarListAdapter != null) {
                            mAvatarListAdapter.setDatas(contributeModels);
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCaichiOpenLand(String text) {
        text_caichi_open_land.setText(text);
    }

    public void setT1(int count) {
        setT(imgT1Land, count);
    }

    public void setT2(int count) {
        setT(imgT2Land, count);
    }

    public void setT3(int count) {
        setT(imgT3Land, count);
    }

    public void setT4(int count) {
        setT(imgT4Land, count);
    }

    public void setTimeLeftVis(boolean vis) {
        if (vis) {
            caichiTimeLeftLand.setVisibility(VISIBLE);
        } else {
            caichiTimeLeftLand.setVisibility(GONE);
        }
    }

    private void setT(ImageView img, int count) {
        switch (count) {
            case 0:
                img.setImageResource(R.drawable.t0);
                break;
            case 1:
                img.setImageResource(R.drawable.t1);
                break;
            case 2:
                img.setImageResource(R.drawable.t2);
                break;
            case 3:
                img.setImageResource(R.drawable.t3);
                break;
            case 4:
                img.setImageResource(R.drawable.t4);
                break;
            case 5:
                img.setImageResource(R.drawable.t5);
                break;
            case 6:
                img.setImageResource(R.drawable.t6);
                break;
            case 7:
                img.setImageResource(R.drawable.t7);
                break;
            case 8:
                img.setImageResource(R.drawable.t8);
                break;
            case 9:
                img.setImageResource(R.drawable.t9);
                break;
        }
    }

    @OnClick({R.id.pic_ly_land, R.id.pic_jc_land, R.id.member_frm, R.id.btn_share_land, R.id.mHeadIcon_land, R.id.btn_msg_land, R.id.ctl_btnSwitch_land, R.id.btn_gift, R.id.img_live_exit_land, R.id.btn_danmu, R.id.btn_cloase_danmu, R.id.btn_game, R.id.rec_left_land, R.id.rec_right_land})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pic_ly_land:
                kaiqiang("lyzb");
                break;
            case R.id.pic_jc_land:
                kaiqiang("yxjc");
                break;
            case R.id.member_frm:
                if (mListener != null) {
                    mListener.showOnlineDialog();
                }
                break;
            case R.id.btn_share_land:
                if (mListener != null) {
                    mListener.share();
                }
                break;
            case R.id.ctl_btnSwitch_land:
                if (mListener != null) {
                    mListener.switchClick();
                }
                break;
            case R.id.mHeadIcon_land:
                if (mListener != null) {
                    mListener.headImageClick("");
                }
                break;
            case R.id.btn_gift:
                if (mListener != null) {
                    btnLinearlayout.setVisibility(GONE);
                    btnRightLand.setVisibility(GONE);
//                    imgLiveExitLand.setVisibility(GONE);
//                    memberFrm.setVisibility(GONE);
                    mListener.sendgift();
                }
                break;
            case R.id.img_live_exit_land:
//                if (mDanmuMgr != null) {
//                    mDanmuMgr.clear();
//                }
                if (mDanmrLabaMgr != null) {
                    mDanmrLabaMgr.clear();
                }
                if (mListener != null) {
                    mListener.fullScreen();
                }
                break;
            case R.id.btn_danmu:
                MobclickAgent.onEvent(getContext(), "10021");
                if (mListener != null) {
                    mListener.msgClick();
                }
                break;
            case R.id.btn_cloase_danmu:
                if (isBarrOpen) {
                    isBarrOpen = false;
                    btnCloaseDanmu.setImageResource(R.drawable.barr_close);
                    danMu1.setVisibility(INVISIBLE);
                    danMu2.setVisibility(INVISIBLE);
                    giftcontent.setVisibility(INVISIBLE);
                    danVip.setVisibility(INVISIBLE);
                } else {
                    isBarrOpen = true;
                    btnCloaseDanmu.setImageResource(R.drawable.barr_open);
                    danMu1.setVisibility(VISIBLE);
                    danMu2.setVisibility(VISIBLE);
                    giftcontent.setVisibility(VISIBLE);
                    danVip.setVisibility(VISIBLE);
                }
                break;
            case R.id.btn_msg_land:
                if (mListener != null) {
                    mListener.onClickMsgBnt();
                }
                msgNewLand.setVisibility(GONE);
                break;
            case R.id.btn_game:
//                downPopwindow();
                if(mListener!=null){
                    mListener.showGameDialog();
                }
//               /showGameDialog();
                break;
            case R.id.rec_left_land:
                if (mListener != null) {
                    mListener.recClick(1);
                }
                break;
            case R.id.rec_right_land:
                if (mListener != null) {
                    mListener.recClick(2);
                }
                break;
        }
    }


    List<GetExtGameIconResponce.ExtGameIconData> datas = new ArrayList<>();
    GameImageAdapter adapter;
    public void showGameDialog(GetExtGameIconResponce responce){
        final Dialog pickDialog3 = new Dialog(getContext(), R.style.floag_dialog);
        pickDialog3.setContentView(R.layout.game_view);

        WindowManager windowManager =((Activity)mContext).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog3.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = (int) (display.getWidth()); //设置宽度
        pickDialog3.getWindow().setAttributes(lp);
        pickDialog3.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
            }
        });
        FrameLayout cancel = (FrameLayout) pickDialog3.findViewById(R.id.frm_cancel);
        final RecyclerView recyclerView=(RecyclerView) pickDialog3.findViewById(R.id.recycle_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter=new GameImageAdapter(datas);
        recyclerView.setAdapter(adapter);

        if(responce.getCode()==0){
            pickDialog3.show();
            if(datas.size()>0){
                datas.clear();
            }
            datas =responce.getExtGameIconDatas();
            adapter.setNewData(datas);
        }else{
            pickDialog3.dismiss();
            AppLogger.e(responce.getErrMsg());
        }

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GetExtGameIconResponce.ExtGameIconData item= (GetExtGameIconResponce.ExtGameIconData) adapter.getItem(position);
                if(item.getHtmlUrl().equals("yxjc")){
                    kaiqiang("yxjc");
                }else if(item.getHtmlUrl().equals("lyzb")){
                    kaiqiang("lyzb");
                }else if(item.getHtmlUrl().equals("xyjc")){
                    if (mListener != null) {
                        mListener.caichiClick();
                    }
                }else {
                    if (mListener != null) {
                        mListener.showWeekStarDialog(item.getHtmlUrl());
                    }
                }
                pickDialog3.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDialog3.dismiss();
            }
        });
    }



    private PopupWindow popWindow;

    private void downPopwindow() {
        if (roomInfo == null) return;
        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .doGetGameListApiCall(new GetGameListRequest(roomInfo.getRoomId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetGameListResponce>() {
                    @Override
                    public void accept(@NonNull GetGameListResponce responce) throws Exception {
                        if (responce.getCode() == 0) {
                            int len = responce.getGameList().size();
                            if (len > 0) {
                                View contentView = LayoutInflater.from(getContext()).inflate(R.layout.pop_game_land_dialog, null);
                                popWindow = new PopupWindow(contentView, TCUtils.dipToPx(getContext(), 135), TCUtils.dipToPx(getContext(), 110));
                                popWindow.setAnimationStyle(R.style.anim);// 淡入淡出动画
                                popWindow.setFocusable(true);// 是否具有获取焦点的能力
                                popWindow.setBackgroundDrawable(new BitmapDrawable());
                                popWindow.setOutsideTouchable(true);
                                popWindow.setTouchInterceptor(new OnTouchListener() {
                                    public boolean onTouch(View v, MotionEvent event) {
                                        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                                            popWindow.dismiss();
                                            return true;
                                        }
                                        return false;
                                    }
                                });
                                popWindow.showAsDropDown(btnGame, TCUtils.dip2px(getContext(), 38), -TCUtils.dip2px(getContext(), 20));
                                final ImageView imageViewLeft = (ImageView) contentView.findViewById(R.id.img_caichi);
                                ImageView imageViewRight = (ImageView) contentView.findViewById(R.id.img_shake);
                                ImageView imageViewMiddle = (ImageView) contentView.findViewById(R.id.img_jincai);
                                ImageView imageWeek = (ImageView) contentView.findViewById(R.id.img_week);
                                LinearLayout linWanfa = (LinearLayout) contentView.findViewById(R.id.lin_wanfa);
                                imageViewLeft.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (mListener != null) {
                                            mListener.caichiClick();
                                        }
                                        popWindow.dismiss();
                                    }
                                });
                                imageViewRight.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        kaiqiang("lyzb");
                                        popWindow.dismiss();
                                    }
                                });
                                imageViewMiddle.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        kaiqiang("yxjc");
                                        popWindow.dismiss();
                                    }
                                });
                                imageWeek.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (mListener != null) {
//                                            mListener.showWeekStarDialog();
                                        }
                                        popWindow.dismiss();
                                    }
                                });

                                if (AppUtils.judegeShowWanfa()) {
                                    linWanfa.setVisibility(VISIBLE);
                                } else {
                                    linWanfa.setVisibility(GONE);
                                }
                            } else {
                                FHUtils.showToast("活动还未开启！");
                            }
                        } else {

                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                })

        );
    }


    private void kaiqiang(final String style) {
        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .doGetGameStatusApiCall(new GetGameStatusRequest(style))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetGameStatusResponce>() {
                    @Override
                    public void accept(@NonNull GetGameStatusResponce responce) throws Exception {
                        if (responce.getCode() == 0) {
                            int openStyle = responce.getGameStatusData().getOpenStyle();
                            if (style.equals("yxjc")) {
                                if (mListener != null) {
                                    mListener.jincaiClick(openStyle, responce.getGameStatusData());
                                }

                            } else {
                                if (mListener != null) {
                                    mListener.shakeClick(responce.getGameStatusData());
                                }
                            }
                        } else {
                            AppLogger.e(responce.getErrMsg());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));
    }


    public void onDestory() {
//        if (mDanmuMgr != null) {
//            mDanmuMgr.destroy();
//            mDanmuMgr = null;
//        }
        if (mDanmrLabaMgr != null) {
            mDanmrLabaMgr.destroy();
            mDanmrLabaMgr = null;
        }
        if (popWindow != null) {
            popWindow.dismiss();
        }
    }

    public void onResume() {
//        if (mDanmuMgr != null) {
//            mDanmuMgr.resume();
//        }
        if (mDanmrLabaMgr != null) {
            mDanmrLabaMgr.resume();
        }
    }

    public void onPause() {
//        if (mDanmuMgr != null) {
//            mDanmuMgr.pause();
//        }
        if (mDanmrLabaMgr != null) {
            mDanmrLabaMgr.pause();
        }
    }

    /**
     * 定时清除礼物
     */
    private void clearTiming() {
        try {
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (giftcontent != null) {
                        int count = giftcontent.getChildCount();
                        for (int i = 0; i < count; i++) {
                            View view = giftcontent.getChildAt(i);
                            ImageView crvheadimage = (ImageView) view.findViewById(R.id.crvheadimage);
                            long nowtime = System.currentTimeMillis();
                            long upTime = (Long) crvheadimage.getTag(R.id.time_value);
                            if ((nowtime - upTime) >= 3000) {
                                removeGiftView(i);
                                return;
                            }
                        }
                    }
                }
            };
            Timer timer = new Timer();
            timer.schedule(task, 0, 3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setImgVisual(boolean isShow) {
        if (isShow) {
            picJcLand.setVisibility(VISIBLE);
            picLyLand.setVisibility(VISIBLE);
        } else {
            picJcLand.setVisibility(GONE);
            picLyLand.setVisibility(GONE);
        }
    }

    /**
     * 显示礼物的方法
     */
    public void showGift(String tag, int count, String giftname, String userName, String headUrl, int giftid,boolean isVip) {
        View giftView = giftcontent.findViewWithTag(tag);
        if (giftView == null) {/*该用户不在礼物显示列表*/
            if (giftcontent.getChildCount() > 2) {/*如果正在显示的礼物的个数超过两个，那么就移除最后一次更新时间比较长的*/
                View giftView1 = giftcontent.getChildAt(0);
                ImageView picTv1 = (ImageView) giftView1.findViewById(R.id.crvheadimage);
                long lastTime1 = (Long) picTv1.getTag(R.id.time_value);
                View giftView2 = giftcontent.getChildAt(1);
                ImageView picTv2 = (ImageView) giftView2.findViewById(R.id.crvheadimage);
                long lastTime2 = (Long) picTv2.getTag(R.id.time_value);
                if (lastTime1 > lastTime2) {/*如果第二个View显示的时间比较长*/
                    removeGiftView(1);
                } else {/*如果第一个View显示的时间长*/
                    removeGiftView(0);
                }
            }

            giftView = addGiftView();/*获取礼物的View的布局*/
            giftView.setTag(tag);/*设置view标识*/

            ImageView crvheadimage = (ImageView) giftView.findViewById(R.id.crvheadimage);
            TextView giftUserName = (TextView) giftView.findViewById(R.id.gift_user_nickname);
            TextView giftNAme = (TextView) giftView.findViewById(R.id.gift_name);
            ImageView giftImage = (ImageView) giftView.findViewById(R.id.ivgift);
            final MagicTextView giftNum = (MagicTextView) giftView.findViewById(R.id.giftNum);/*找到数量控件*/
            if (count == 0) {
                giftNum.setVisibility(View.INVISIBLE);
            } else {
                giftNum.setVisibility(View.VISIBLE);
            }

            if (count == 520) {
                giftNum.setText("520x1");/*设置礼物数量*/
            } else if (count == 1314) {
                giftNum.setText("1314x1");/*设置礼物数量*/
            } else {
                StringBuilder stringBuilder=new StringBuilder();
                stringBuilder.append("x");
                stringBuilder.append(count+"");
                giftNum.setText(stringBuilder);/*设置礼物数量*/
            }
            if (count == 0) {
                giftNAme.setText(giftname);
            } else {
                giftNAme.setText("送出了" + giftname);
            }
            giftUserName.setText(userName);
            TCUtils.showPicWithUrl(getContext(), crvheadimage, headUrl, R.drawable.face);
            crvheadimage.setTag(R.id.time_value, System.currentTimeMillis());/*设置时间标记*/
            giftNum.setTag(R.id.gift_num, count);/*给数量控件设置标记*/
            giftNum.setTag(R.id.gift_id, giftid);
            if (count == 520) {
                giftNum.setTag(R.id.gift_type, 1);//520标注1，1314标注2，其他0
            } else if (count == 1314) {
                giftNum.setTag(R.id.gift_type, 2);//520标注1，1314标注2，其他0
            } else {
                giftNum.setTag(R.id.gift_type, 0);//520标注1，1314标注2，其他0
            }
            if (count != 0) {
                SysGiftNewBean giftBean = getGiftByID(giftid);
                if (giftBean != null) {
                    if (FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean() != null && FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().size() > 0) {
                        String url = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosGiftRootPath();
                        RequestBuilder<Bitmap> requestBuilder = Glide.with(getContext()).asBitmap().apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL));
                        if(giftBean.getEnableVip()==1&&isVip){
                            String iconName=giftBean.getIcon().substring(0,giftBean.getIcon().lastIndexOf("."))+"_vip.png";
                            requestBuilder.load(url + "/" + iconName).into(giftImage);
                        }else {
                            requestBuilder.load(url + "/" + giftBean.getIcon()).into(giftImage);
                        }
                    }
                }
            } else {
                SysMountNewBean mountNewBeanBean = FeihuZhiboApplication.getApplication().mDataManager.getMountBeanByID(String.valueOf(giftid));
                if (mountNewBeanBean != null) {
                    if (FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean() != null && FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().size() > 0) {
                        String url = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosMountRootPath();
                        RequestBuilder<Bitmap> requestBuilder = Glide.with(getContext()).asBitmap().apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL));
                        requestBuilder.load(url + "/" + mountNewBeanBean.getIcon()).into(giftImage);
                    }
                }
            }

            giftcontent.addView(giftView);/*将礼物的View添加到礼物的ViewGroup中*/
            giftcontent.invalidate();/*刷新该view*/
            giftView.startAnimation(inAnim);/*开始执行显示礼物的动画*/
            inAnim.setAnimationListener(new Animation.AnimationListener() {/*显示动画的监听*/
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    giftNumAnim.start(giftNum);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        } else {/*该用户在礼物显示列表*/
            ImageView crvheadimage = (ImageView) giftView.findViewById(R.id.crvheadimage);/*找到头像控件*/
            TextView giftUserName = (TextView) giftView.findViewById(R.id.gift_user_nickname);
            TextView giftNAme = (TextView) giftView.findViewById(R.id.gift_name);
            ImageView giftImage = (ImageView) giftView.findViewById(R.id.ivgift);
            MagicTextView giftNum = (MagicTextView) giftView.findViewById(R.id.giftNum);/*找到数量控件*/
            if (count == 0) {
                giftNum.setVisibility(View.INVISIBLE);
            } else {
                giftNum.setVisibility(View.VISIBLE);
            }
            if ((int) (giftNum.getTag(R.id.gift_id)) == giftid) {
                int showNum = (Integer) giftNum.getTag(R.id.gift_num) + count;
                int giftType = (Integer) giftNum.getTag(R.id.gift_type);
                if (giftType == 0) {
                    if (count == 520) {
                        giftNum.setText("520x1");
                        giftNum.setTag(R.id.gift_num, 520);/*给数量控件设置标记*/
                    } else if (count == 1314) {
                        giftNum.setText("1314x1");
                        giftNum.setTag(R.id.gift_num, 1314);/*给数量控件设置标记*/
                    } else {
                        StringBuilder stringBuilder=new StringBuilder();
                        stringBuilder.append("x");
                        stringBuilder.append(showNum+"");
                        giftNum.setText(stringBuilder);
                        giftNum.setTag(R.id.gift_num, showNum);/*给数量控件设置标记*/
                    }
                } else if (giftType == 1) {
                    //520
                    if (count == 520) {
                        giftNum.setText("520x" + showNum / 520);
                        giftNum.setTag(R.id.gift_num, showNum);/*给数量控件设置标记*/
                    } else if (count == 1314) {
                        giftNum.setText("1314x1");
                        giftNum.setTag(R.id.gift_num, 1314);/*给数量控件设置标记*/
                    } else {
                        StringBuilder stringBuilder=new StringBuilder();
                        stringBuilder.append("x");
                        stringBuilder.append(count+"");
                        giftNum.setText(stringBuilder);
                        giftNum.setTag(R.id.gift_num, count);/*给数量控件设置标记*/
                    }
                } else if (giftType == 2) {
                    //1314
                    if (count == 1314) {
                        giftNum.setText("1314x" + showNum / 1314);
                        giftNum.setTag(R.id.gift_num, showNum);/*给数量控件设置标记*/
                    } else if (count == 520) {
                        giftNum.setText("520x1");
                        giftNum.setTag(R.id.gift_num, 520);/*给数量控件设置标记*/
                    } else {
                        StringBuilder stringBuilder=new StringBuilder();
                        stringBuilder.append("x");
                        stringBuilder.append(count+"");
                        giftNum.setText(stringBuilder);
                        giftNum.setTag(R.id.gift_num, count);/*给数量控件设置标记*/
                    }
                }
                giftNum.setTag(R.id.gift_id, giftid);
            } else {
                StringBuilder stringBuilder=new StringBuilder();
                stringBuilder.append("x");
                stringBuilder.append(count+"");
                giftNum.setText(stringBuilder);
                giftNum.setTag(R.id.gift_num, count);/*给数量控件设置标记*/
                giftNum.setTag(R.id.gift_id, giftid);
            }

            if (count == 520) {
                giftNum.setTag(R.id.gift_type, 1);//520标注1，1314标注2，其他0
            } else if (count == 1314) {
                giftNum.setTag(R.id.gift_type, 2);//520标注1，1314标注2，其他0
            } else {
                giftNum.setTag(R.id.gift_type, 0);//520标注1，1314标注2，其他0
            }
            if (count == 0) {
                giftNAme.setText(giftname);
            } else {
                giftNAme.setText("送出了" + giftname);
            }
            giftUserName.setText(userName);
            TCUtils.showPicWithUrl(getContext(), crvheadimage, headUrl, R.drawable.face);
            crvheadimage.setTag(R.id.time_value, System.currentTimeMillis());
            if (count != 0) {
                giftNumAnim.start(giftNum);
            }
            if (count != 0) {
                SysGiftNewBean giftBean = getGiftByID(giftid);
                if (giftBean != null) {
                    if (FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean() != null && FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().size() > 0) {
                        String url = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosGiftRootPath();
                        RequestBuilder<Bitmap> requestBuilder = Glide.with(getContext()).asBitmap().apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL));
                        if(giftBean.getEnableVip()==1&&isVip){
                            String iconName=giftBean.getIcon().substring(0,giftBean.getIcon().lastIndexOf("."))+"_vip.png";
                            requestBuilder.load(url + "/" + iconName).into(giftImage);
                        }else {
                            requestBuilder.load(url + "/" + giftBean.getIcon()).into(giftImage);
                        }
                    }
                }
            } else {
                SysMountNewBean mountNewBeanBean = FeihuZhiboApplication.getApplication().mDataManager.getMountBeanByID(String.valueOf(giftid));
                if (mountNewBeanBean != null) {
                    if (FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean() != null && FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().size() > 0) {
                        String url = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosMountRootPath();
                        RequestBuilder<Bitmap> requestBuilder = Glide.with(getContext()).asBitmap().apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL));
                        requestBuilder.load(url + "/" + mountNewBeanBean.getIcon()).into(giftImage);
                    }
                }
            }
        }
    }

    public SysGiftNewBean getGiftByID(int giftId) {
        SysGiftNewBean gift = new SysGiftNewBean();
        List<SysGiftNewBean> data = FeihuZhiboApplication.getApplication().mDataManager.getLiveGiftList();
        if (data != null) {
            for (SysGiftNewBean info : data) {
                if (Integer.valueOf(info.getId()) == giftId) {
                    gift = info;
                    break;
                }
            }
        }
        return gift;
    }

    /**
     * 添加礼物view,(考虑垃圾回收)
     */
    private View addGiftView() {
        View view = null;
        if (giftViewCollection.size() <= 0) {
            /*如果垃圾回收中没有view,则生成一个*/
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_gift, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.topMargin = 10;
            view.setLayoutParams(lp);
            giftcontent.addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View view) {
                }

                @Override
                public void onViewDetachedFromWindow(View view) {
                    giftViewCollection.add(view);
                }
            });
        } else {
            view = giftViewCollection.get(0);
            giftViewCollection.remove(view);
        }
        return view;
    }


    /**
     * 删除礼物view
     */
    private void removeGiftView(final int index) {
        final View removeView = giftcontent.getChildAt(index);
        outAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                giftcontent.removeViewAt(index);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                removeView.startAnimation(outAnim);
            }
        });
    }
}
