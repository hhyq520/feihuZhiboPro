package cn.feihutv.zhibofeihu.ui.live.pclive;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.feihutv.zhibofeihu.BuildConfig;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.db.model.SysConfigBean;
import cn.feihutv.zhibofeihu.data.db.model.SysGiftNewBean;
import cn.feihutv.zhibofeihu.data.db.model.SysMountNewBean;
import cn.feihutv.zhibofeihu.data.local.SysdataHelper;
import cn.feihutv.zhibofeihu.data.local.model.SysbagBean;
import cn.feihutv.zhibofeihu.data.local.model.TCChatEntity;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.GetCurrMountResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.JoinRoomResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.AllRoomMsgPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.NoticeJoinRoomPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.RoomBanPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.RoomChatPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.RoomFollowPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.RoomGiftPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.RoomMgrPush;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.ValueChangePush;
import cn.feihutv.zhibofeihu.di.component.ActivityComponent;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.rxbus.RxBusSubscribe;
import cn.feihutv.zhibofeihu.rxbus.ThreadMode;
import cn.feihutv.zhibofeihu.ui.base.BaseFragment;
import cn.feihutv.zhibofeihu.ui.home.HotFragment;
import cn.feihutv.zhibofeihu.ui.live.OnItemClickListener;
import cn.feihutv.zhibofeihu.ui.live.OnStrClickListener;
import cn.feihutv.zhibofeihu.ui.live.adapter.ChatMsgRecycleAdapter;
import cn.feihutv.zhibofeihu.ui.live.models.GiftModel;
import cn.feihutv.zhibofeihu.ui.me.recharge.RechargeActivity;
import cn.feihutv.zhibofeihu.ui.widget.DanmuBase.DanmakuActionPcManager;
import cn.feihutv.zhibofeihu.ui.widget.DanmuBase.DanmakuPChannel;
import cn.feihutv.zhibofeihu.ui.widget.DanmuBase.DanmuModel;
import cn.feihutv.zhibofeihu.ui.widget.MagicTextView;
import cn.feihutv.zhibofeihu.ui.widget.TalkGiftView;
import cn.feihutv.zhibofeihu.ui.widget.dialog.GiftNewDialogView;
import cn.feihutv.zhibofeihu.ui.widget.dialog.GiftNumDialog;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.AppUtils;
import cn.feihutv.zhibofeihu.utils.CustomDialogUtils;
import cn.feihutv.zhibofeihu.utils.FloatViewWindowManger;
import cn.feihutv.zhibofeihu.utils.NumAnim;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import cn.feihutv.zhibofeihu.utils.TCConstants;
import cn.feihutv.zhibofeihu.utils.TCDanmuMgr;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.ui.widget.DanmakuView;

/**
 * <pre>
 *     author : huang hao
 *     time   :
 *     desc   : 界面绘制
 *     version: 1.0
 * </pre>
 */
public class LiveChatFragment extends BaseFragment implements LiveChatFragmentMvpView, TalkGiftView.OnTextSendListener {

    @Inject
    LiveChatFragmentMvpPresenter<LiveChatFragmentMvpView> mPresenter;

    @BindView(R.id.im_msg_listview)
    RecyclerView mListViewMsg;
    @BindView(R.id.talk_gift_view)
    TalkGiftView talkGiftView;
    @BindView(R.id.gift_content)
    LinearLayout giftcontent;
    @BindView(R.id.danmaku_view)
    DanmakuView danmakuView;
    @BindView(R.id.dan_vip)
    DanmakuPChannel danVip;
    Unbinder unbinder;
    DanmakuActionPcManager danmakuActionManager;
    @BindView(R.id.pic_ly)
    ImageView picLy;
    @BindView(R.id.pic_jc)
    ImageView picJc;
    private Timer messageTimer;
    private MessageTimerTask messageTimerTask;
    private Timer staticGiftTimer;
    private StaticGiftTimerTask giftTimerTask;
    private InputMethodManager imm;
    private ArrayList<TCChatEntity> mArrayListChatEntity = new ArrayList<>();
    private ChatMsgRecycleAdapter mChatMsgListAdapter;
    public TCDanmuMgr mDanmuMgr;
    private List<GiftModel> staticGiftModels = new ArrayList<>();
    private List<AllRoomMsgPush> allRoomMessage = new ArrayList<>();
    private List<View> giftViewCollection = new ArrayList<View>();
    private static boolean isFullScreen = false;
    private RequestBuilder<Bitmap> mRequestBuilder;
    private boolean isFirst = true;

    public static void setFullScreen(boolean isFullScreen) {
        LiveChatFragment.isFullScreen = isFullScreen;
    }

    /**
     * 动画相关
     */
    private NumAnim giftNumAnim;
    private TranslateAnimation inAnim;
    private TranslateAnimation outAnim;

    private final int SHOW_MESSAGE = 1;
    private final int STATICGIFTSHOW = 2;


    public static LiveChatFragment liveChatFragment;
    private JoinRoomResponce.JoinRoomData roomInfo;
    private Context context;

    public static LiveChatFragment getInstance(JoinRoomResponce.JoinRoomData roomInfo, Context context) {
        LiveChatFragment fragment = new LiveChatFragment();
        fragment.roomInfo = roomInfo;
        liveChatFragment = fragment;
        fragment.context = context;
        return fragment;
    }

    @Override
    public void onTextSend(String msg, boolean mDanmuOpen) {
        if (mDanmuOpen) {
            mPresenter.sendLoudspeaker(msg);
        } else {
            if (msg.length() == 0)
                return;
            try {
                byte[] byte_num = msg.getBytes("utf8");
                if (byte_num.length > 160) {
                    Toast.makeText(getContext(), "请输入内容", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return;
            }
            final TCChatEntity entity = new TCChatEntity();
            entity.setSenderName(SharePreferenceUtil.getSession(getContext(), "PREF_KEY_NICKNAME") + "  ");
            entity.setLevel(SharePreferenceUtil.getSessionInt(getContext(), "PREF_KEY_LEVEL"));
            entity.setContext(msg);
            entity.setType(TCConstants.PC_TYPE);
            entity.setUserId(SharePreferenceUtil.getSession(getContext(), "PREF_KEY_USERID"));
            mPresenter.sendRoomMsg(entity, msg);
        }
    }

    @Override
    public void OnEditClick() {

    }

    @Override
    public void giftClick() {
        requestSysbag();
    }

    @Override
    public void OnClick() {

        if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
            if (BuildConfig.isForceLoad.equals("1")) {
                if (mListener != null) {
                    mListener.showLogin(true);
                }
            }
        }else {
            picJc.setVisibility(View.GONE);
            picLy.setVisibility(View.GONE);
        }
    }

    @Override
    public void keyBoardHide() {
        picJc.setVisibility(View.VISIBLE);
        picLy.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLogin() {
        if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
            if (BuildConfig.isForceLoad.equals("1")) {
                if (mListener != null) {
                    mListener.showLogin(true);
                }
            }
        }else {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick({R.id.pic_ly, R.id.pic_jc})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pic_ly:
                if(mListener!=null){
                    mListener.lyClick();
                }
                break;
            case R.id.pic_jc:
                if(mListener!=null){
                    mListener.jcClick();
                }
                break;
        }
    }

    public interface OnChatClickListener {
        void listClick(String userID);

        void danmuClick(String roomId);

        void showLogin(boolean isFromEdit);

        void jcClick();

        void lyClick();
    }

    private OnChatClickListener mListener;

    public void setOnChatClickListener(OnChatClickListener listener) {
        mListener = listener;
    }

    private class MessageTimerTask extends TimerTask {
        public void run() {
            dealNews(SHOW_MESSAGE);
        }
    }

    private class StaticGiftTimerTask extends TimerTask {
        public void run() {
            dealNews(STATICGIFTSHOW);
        }
    }


    public void clearDanmuMessage() {
        try {
            if (allRoomMessage != null) {
                allRoomMessage.clear();
            }
            if (mDanmuMgr != null) {
                mDanmuMgr.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideSoftInputFromWindow() {
        if (talkGiftView != null)
            talkGiftView.hideSoftInputFromWindow();
    }

    private void requestSysbag() {
        mPresenter.requestBagData();
    }

    private void dealNews(final int what) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(what);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        switch (integer) {
                            case SHOW_MESSAGE:
                                showAllRoomMessage();
                                break;
                            case STATICGIFTSHOW:
                                showStaticGift();
                                break;
                            default:
                                break;
                        }
                    }
                });
    }

    private void showStaticGift() {
        if (staticGiftModels.size() >= 2) {
            GiftModel giftModel = staticGiftModels.get(0);
            showGift(giftModel.getTag(), giftModel.getCount(), giftModel.getGiftname(),
                    giftModel.getUserName(), giftModel.getHeadUrl(), giftModel.getGiftid(),giftModel.isVip());
            staticGiftModels.remove(giftModel);
        } else if (staticGiftModels.size() > 0) {
            GiftModel giftModel = staticGiftModels.remove(0);
            showGift(giftModel.getTag(), giftModel.getCount(), giftModel.getGiftname(),
                    giftModel.getUserName(), giftModel.getHeadUrl(), giftModel.getGiftid(),giftModel.isVip());
        }
    }

    /**
     * 显示礼物的方法
     */
    private void showGift(String tag, int count, String giftname, String userName, String headUrl, int giftid,boolean isVip) {
        Log.e("gggggg", giftid + "");
        if (giftcontent == null) {
            return;
        }
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
                if (giftNum != null) {
                    giftNum.setVisibility(View.GONE);
                }
            } else {
                giftNum.setVisibility(View.VISIBLE);
            }
            if (giftNum != null) {
                if (count == 520) {
                    giftNum.setText("520x1");/*设置礼物数量*/
                } else if (count == 1314) {
                    giftNum.setText("1314x1");/*设置礼物数量*/
                } else {
                    giftNum.setText("x" + count);/*设置礼物数量*/
                }
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
                    List<SysConfigBean> list = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean();
                    if (list != null && list.size() > 0) {
                        String url = list.get(0).getCosGiftRootPath();
                        if(giftBean.getEnableVip()==1&&isVip){
                            String iconName=giftBean.getIcon().substring(0,giftBean.getIcon().lastIndexOf("."))+"_vip.png";
                            mRequestBuilder.load(url + "/" + iconName).into(giftImage);
                        }else {
                            mRequestBuilder.load(url + "/" + giftBean.getIcon()).into(giftImage);
                        }
                    }
                }
            } else {
                SysMountNewBean mountNewBeanBean = mPresenter.getMountBeanByID(String.valueOf(giftid));
                if (mountNewBeanBean != null) {
                    List<SysConfigBean> list = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean();
                    if (list != null && list.size() > 0) {
                        String url = list.get(0).getCosMountRootPath();
                        mRequestBuilder.load(url + "/" + mountNewBeanBean.getIcon()).into(giftImage);
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
                giftNum.setVisibility(View.GONE);
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
                        giftNum.setText("x" + showNum);
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
                        giftNum.setText("x" + count);
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
                        giftNum.setText("x" + count);
                        giftNum.setTag(R.id.gift_num, count);/*给数量控件设置标记*/
                    }
                }
                giftNum.setTag(R.id.gift_id, giftid);
            } else {
                giftNum.setText("x" + count);
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
                    List<SysConfigBean> list = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean();
                    if (list != null && list.size() > 0) {
                        String url = list.get(0).getCosGiftRootPath();
                        if(giftBean.getEnableVip()==1&&isVip){
                            String iconName=giftBean.getIcon().substring(0,giftBean.getIcon().lastIndexOf("."))+"_vip.png";
                            mRequestBuilder.load(url + "/" + iconName).into(giftImage);
                        }else {
                            mRequestBuilder.load(url + "/" + giftBean.getIcon()).into(giftImage);
                        }
                    }
                }
            } else {
                SysMountNewBean mountNewBeanBean = mPresenter.getMountBeanByID(String.valueOf(giftid));
                if (mountNewBeanBean != null) {
                    List<SysConfigBean> list = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean();
                    if (list != null && list.size() > 0) {
                        String url = list.get(0).getCosMountRootPath();
                        mRequestBuilder.load(url + "/" + mountNewBeanBean.getIcon()).into(giftImage);
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
            view = LayoutInflater.from(getContext()).inflate(R.layout.pc_item_gift, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.topMargin = 10;
            view.setLayoutParams(lp);
            giftcontent.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
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

    private void showAllRoomMessage() {
        if (allRoomMessage.size() > 0) {
            AllRoomMsgPush pushData = allRoomMessage.remove(0);
            String msg10 = pushData.getMsg();
            String msgType = pushData.getMsgName();

            if (mDanmuMgr != null) {
                if (msgType.equals("Sys")) {
                    mDanmuMgr.addSysDanmu(msg10);
                    return;
                }
            }

            String masterName = pushData.getMasterName();
            String roomId = pushData.getRoomId();
            int giftId = pushData.getGiftId();
            int giftCnt = pushData.getGiftCnt();
            String roomName = pushData.getRoomName();
            if (mDanmuMgr != null) {
                if (msgType.equals("CountDown5min")) {
                    if (AppUtils.judegeShowWanfa()) {
                        mDanmuMgr.addHudongDanmu(msg10, 3);
                    }
                } else if (msgType.equals("CountDown60s")) {
                    if (AppUtils.judegeShowWanfa()) {
                        mDanmuMgr.addHudongDanmu(msg10, 1);
                    }
                } else if (msgType.equals("GameStartRound60s")) {
                    if (AppUtils.judegeShowWanfa()) {
                        mDanmuMgr.addGameStartRound60s();
                    }
                } else {
                    String nickname = pushData.getSender().getNickName();
                    String headUrl = pushData.getSender().getHeadUrl();
                    int level = pushData.getSender().getLevel();
                    if (mDanmuMgr != null) {
                        if (msgType.equals("GameMsg")) {
                            if (AppUtils.judegeShowWanfa()) {
                                mDanmuMgr.addGameDanmu(msg10, masterName, roomId);
                            }
                        } else if (msgType.equals("GaoShePao")) {
                            mDanmuMgr.addDaPaoDanmu(masterName, nickname, roomId);
                        } else if (msgType.equals("Loudspeaker")) {
                            if (roomId.equals(roomInfo.getRoomId())) {
                                mDanmuMgr.addLabaDanmu(msg10, nickname, masterName, true, roomId);
                            } else {
                                mDanmuMgr.addLabaDanmu(msg10, nickname, masterName, false, roomId);
                            }
                        } else if (msgType.equals("LargeQuantityGift")) {
                            if((pushData.getSender().getVip()>0&&!pushData.getSender().isVipExpired())||(pushData.getSender().getGuardType()>0&&!pushData.getSender().isGuardExpired())) {
                                mDanmuMgr.addGiftDanmu(nickname, masterName, giftId, giftCnt, false, roomId,true);
                            }else{
                                mDanmuMgr.addGiftDanmu(nickname, masterName, giftId, giftCnt, false, roomId,false);
                            }
                        } else if (msgType.equals("Money300")) {
                            mDanmuMgr.addMoney300Danmu(nickname, masterName, giftId, giftCnt, roomId);
                        } else if (msgType.equals("Money180")) {
                            if (roomId.equals(roomInfo.getRoomId())) {
                                if((pushData.getSender().getVip()>0&&!pushData.getSender().isVipExpired())||(pushData.getSender().getGuardType()>0&&!pushData.getSender().isGuardExpired())) {
                                    mDanmuMgr.addGiftDanmu(nickname, masterName, giftId, giftCnt, true, roomId,true);
                                }else{
                                    mDanmuMgr.addGiftDanmu(nickname, masterName, giftId, giftCnt, true, roomId,false);
                                }
                            }
                        } else if (msgType.equals("CCAward")) {
                            mDanmuMgr.addCCAwardDanmu(msg10, nickname, giftId);
                        } else if (msgType.equals("GameWin")) {
                            if (AppUtils.judegeShowWanfa()) {
                                mDanmuMgr.addGamewinDanmu(msg10, nickname);
                            }
                        } else if (msgType.equals("BuyVIP")) {
                            DanmuModel model = new DanmuModel(headUrl, level,
                                    "恭喜" + nickname + "开通VIP，成为全平台的贵宾！", pushData.getSender().getVip(),
                                    pushData.getSender().isVipExpired(), pushData.getSender().getGuardType(),
                                    pushData.getSender().isGuardExpired(), pushData.getSender().isLiang(), pushData.getSender().getShowId());
                            danmakuActionManager.addDanmu(model);

                            final TCChatEntity entity = new TCChatEntity();
                            entity.setSenderName("");
                            entity.setLevel(0);
                            entity.setContext("系统提示：" + "恭喜" + nickname + "开通VIP，成为全平台的贵宾！");
                            entity.setType(TCConstants.SYSTEM_TYPE);
                            entity.setUserId("0");
                            notifyMsg(entity);

                            mDanmuMgr.addVipSuccess(nickname);
                        } else if (msgType.equals("BuyGuard")) {
                            DanmuModel model = new DanmuModel(headUrl, level,
                                    nickname + "成功守护主播，果然是真爱啊~", pushData.getSender().getVip(),
                                    pushData.getSender().isVipExpired(), pushData.getSender().getGuardType(),
                                    pushData.getSender().isGuardExpired(), pushData.getSender().isLiang(), pushData.getSender().getShowId());
                            danmakuActionManager.addDanmu(model);

                            final TCChatEntity entity = new TCChatEntity();
                            entity.setSenderName("");
                            entity.setLevel(0);
                            entity.setContext("系统提示：" + nickname + "成功守护主播，果然是真爱啊~");
                            entity.setType(TCConstants.SYSTEM_TYPE);
                            entity.setUserId("0");
                            notifyMsg(entity);
                            mDanmuMgr.addGuardSuccess(nickname, masterName);
                        }

                    }
                }
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_live_chat_fragment;
    }


    @Override
    protected void initWidget(View view) {
        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            mPresenter.onAttach(this);
        }
        initView();
    }

    private void initView() {
        mRequestBuilder = Glide.with(this).asBitmap().apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL));
        if (messageTimer == null) {
            messageTimer = new Timer(true);
            messageTimerTask = new MessageTimerTask();
            messageTimer.schedule(messageTimerTask, 1000, 2500);
        }
        if (staticGiftTimer == null) {
            staticGiftTimer = new Timer(true);
            giftTimerTask = new StaticGiftTimerTask();
            staticGiftTimer.schedule(giftTimerTask, 1000, 500);
        }
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        mListViewMsg.setLayoutManager(new LinearLayoutManager(getContext()));// 布局管理器。
        ((SimpleItemAnimator)mListViewMsg.getItemAnimator()).setSupportsChangeAnimations(false);
        mListViewMsg.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。



//        if (isFirst) {
            final TCChatEntity entity = new TCChatEntity();
            entity.setSenderName("");
            entity.setLevel(0);
            entity.setContext("我们直播倡导绿色直播，封面和直播内容涉及色情、低俗、暴力、引诱、暴露等都会将被封停账号，同时禁止直播闹事，集会，文明直播从我做起【网警24小时在线巡查】");
            entity.setType(TCConstants.SYSTEM_TYPE);
            entity.setUserId("0");
        if(mArrayListChatEntity.size()>0){
            mArrayListChatEntity.clear();
        }
            mArrayListChatEntity.add(entity);
//            mChatMsgListAdapter.setDatasNew(mArrayListChatEntity);
//            notifyMsg(entity);
            isFirst = false;
//        }
        mChatMsgListAdapter = new ChatMsgRecycleAdapter(getContext(), true,mArrayListChatEntity);
        mListViewMsg.setAdapter(mChatMsgListAdapter);

        mDanmuMgr = new TCDanmuMgr(mPresenter.getSysConfigBean(), FeihuZhiboApplication.getApplication().mDataManager.getSysGiftNew(), getContext(), true, false, false);
        mDanmuMgr.setDanmakuView(danmakuView);
        danmakuView.setOnDanmakuClickListener(new IDanmakuView.OnDanmakuClickListener() {
            @Override
            public void onDanmakuClick(BaseDanmaku latest) {
                if (latest.userId == 0) {
                    return;
                }
                if (!String.valueOf(latest.userId).equals(roomInfo.getRoomId())) {
                    mPresenter.loadRoomById(String.valueOf(latest.userId));
                }
            }

            @Override
            public void onDanmakuClick(IDanmakus danmakus) {

            }
        });

        danmakuActionManager = new DanmakuActionPcManager();
        danVip.setDanAction(danmakuActionManager);
        danmakuActionManager.addChannel(danVip);
        mChatMsgListAdapter.setOnStrClickListener(new OnStrClickListener() {
            @Override
            public void onItemClick(String userId) {
                if (!TextUtils.isEmpty(userId) && !userId.equals("0")) {
                    if (mListener != null) {
                        mListener.listClick(userId);
                    }
                }
            }
        });
        talkGiftView.setmOnTextSendListener(this);
        giftNumAnim = new NumAnim();
        inAnim = (TranslateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.gift_in);
        outAnim = (TranslateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.gift_out);
        clearTiming();
        if (!SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
            mPresenter.getCurrMount(roomInfo.getRoomId());
        }
        isFullScreen = false;
    }

    @Override
    protected void lazyLoad() {

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
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                removeView.startAnimation(outAnim);
            }
        });
    }

    @Override
    public void onDestroyView() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void gotoRoom(LoadRoomResponce.LoadRoomData data) {
        FloatViewWindowManger.removeSmallWindow();
        // 1为PC   2为手机
        getActivity().finish();
        AppUtils.startLiveActivity(LiveChatFragment.this, data.getRoomId(), data.getHeadUrl(), data.getBroadcastType(), false, HotFragment.START_LIVE_PLAY);
    }

    @Override
    public void notifyChatMsg(TCChatEntity tcChatEntity) {
//        notifyMsg(tcChatEntity);
    }

    GiftNewDialogView giftDialogView;

    @Override
    public void showGiftDialog(final List<SysbagBean> sysbagBeanList) {
        if (FeihuZhiboApplication.getApplication().mDataManager.getLiveGiftList().size() == 0) {
            return;
        }
        picJc.setVisibility(View.GONE);
        picLy.setVisibility(View.GONE);
        giftDialogView = new GiftNewDialogView(sysbagBeanList, getContext(), true, true);
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
                picJc.setVisibility(View.VISIBLE);
                picLy.setVisibility(View.VISIBLE);
            }
        });
        giftDialogView.setGiftDialogListener(new GiftNewDialogView.GiftDialogListener() {
            @Override
            public void sendGift(int id, int count, boolean isBag) {
                if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                    if (BuildConfig.isForceLoad.equals("1")) {
                        CustomDialogUtils.showQZLoginDialog(getActivity(), false);
                    } else {
                        CustomDialogUtils.showLoginDialog(getActivity(), false);
                    }
                } else {
                    if (isBag) {
                        mPresenter.dealBagSendGift(sysbagBeanList, id, count);
                    } else {
                        mPresenter.dealSendGift(sysbagBeanList, id, count);
                    }
                }
            }

            @Override
            public void chargr() {
                if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                    if (BuildConfig.isForceLoad.equals("1")) {
                        CustomDialogUtils.showQZLoginDialog(getActivity(), false);
                    } else {
                        CustomDialogUtils.showLoginDialog(getActivity(), false);
                    }
                    return;
                }
                Intent intent = new Intent(getActivity(), RechargeActivity.class);
                intent.putExtra("fromWhere", "直播间礼物页面");
                startActivity(intent);
            }

            @Override
            public void showNumDialog() {
                showGiftNumDialog();
            }
        });
    }

    @Override
    public void sendGiftSuccess(List<SysbagBean> sysbagBeanList, int id, int count) {
        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName("" + SharePreferenceUtil.getSession(getContext(), "PREF_KEY_NICKNAME") + "  ");
        SysGiftNewBean giftBean = mPresenter.getGiftBeanByID(String.valueOf(id));
        entity.setContext("送出了" + giftBean.getName() + "×" + count);
        entity.setType(TCConstants.PC_TYPE);
        entity.setLevel(SharePreferenceUtil.getSessionInt(getContext(), "PREF_KEY_LEVEL"));
//        notifyMsg(entity);
        if (!giftBean.getIsAnimation().equals("1")) {
            GiftModel giftModel = new GiftModel(SharePreferenceUtil.getSession(getContext(), "PREF_KEY_USERID"), count, giftBean.getName(),
                    SharePreferenceUtil.getSession(getContext(), "PREF_KEY_NICKNAME"),
                    SharePreferenceUtil.getSession(getContext(), "PREF_KEY_HEADURL"), id, false);
//            staticGiftModels.add(giftModel);
        }

        for (SysbagBean sysbagBeen : sysbagBeanList) {
            if (sysbagBeen.getId() == id) {
                if (sysbagBeen.getCnt() > count) {
                    sysbagBeen.setCnt(sysbagBeen.getCnt() - count);
                } else {
                    sysbagBeanList.remove(sysbagBeen);
                }
                if (giftDialogView != null) {
                    giftDialogView.setData(SysdataHelper.getbagBeanList(mPresenter.getSysGiftNewBean(), sysbagBeanList));
                }
                break;
            }
        }
    }

    @Override
    public void notifyGetCurrMount(GetCurrMountResponce.CurrMountData data) {
        TCChatEntity entity = new TCChatEntity();
        final LoadUserDataBaseResponse.UserData userData = FeihuZhiboApplication.getApplication().mDataManager.getUserData();
        entity.setGuardType(data.getGuardType());
        if(data.getGuardType()!=0){
            entity.setGuardExpired(false);
            SharePreferenceUtil.saveSeesionBoolean(FeihuZhiboApplication.getApplication(),"meisGuard",true);
        }else{
            entity.setGuardExpired(true);
            SharePreferenceUtil.saveSeesionBoolean(FeihuZhiboApplication.getApplication(),"meisGuard",false);
        }
        entity.setVip(userData.getVip());
        entity.setVipExpired(userData.isVipExpired());
        entity.setAccountId(userData.getAccountId());
        entity.setLiang(userData.isLiang());
        entity.setSenderName("" + userData.getNickName() + "  ");
        entity.setLevel(userData.getLevel());
        entity.setUserId(userData.getUserId());
        entity.setType(TCConstants.JOIN_TYPE);
        if (data.getId() != 0) {
            SysMountNewBean mountBean = mPresenter.getMountBeanByID(String.valueOf(data.getId()));
            if (mountBean != null) {
                entity.setZuojia("驾驶" + mountBean.getName());
                entity.setContext("进入直播间");
                if (!mountBean.getIsAnimation().equals("1")) {
                    GiftModel giftModel = new GiftModel(SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_USERID"),
                            0, "驾驶[" + mountBean.getName() + "]进入直播间", SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_NICKNAME"),
                            SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_HEADURL"), data.getId(), false);
                    if(SharePreferenceUtil.getSessionInt(getContext(),"PREF_KEY_VIP")>0){
                        if(!SharePreferenceUtil.getSessionBoolean(getContext(),"PREF_KEY_VIPEXPIRED")){

                        }else{
                            staticGiftModels.add(giftModel);
                        }
                    }else {
                        staticGiftModels.add(giftModel);
                    }

                }
            }else{
                entity.setZuojia("");
                entity.setContext("进入直播间");
            }
        }else{
            entity.setZuojia("");
            entity.setContext("进入直播间");
        }
        notifyMsg(entity);
        if ((userData.getVip() > 0&&!userData.isVipExpired()) || data.getGuardType() != 0) {
            if(!TextUtils.isEmpty(entity.getZuojia())){
                DanmuModel model = new DanmuModel(userData.getHeadUrl(), userData.getLevel(),
                        userData.getNickName()+entity.getZuojia() + "进入直播间~", userData.getVip(),
                        userData.isVipExpired(), data.getGuardType(),
                        data.getGuardType()==0?true:false, userData.isLiang(), userData.getAccountId());
                danmakuActionManager.addDanmu(model);
            }else{
                DanmuModel model = new DanmuModel(userData.getHeadUrl(), userData.getLevel(),
                        userData.getNickName() + "进入直播间~", userData.getVip(),
                        userData.isVipExpired(), data.getGuardType(),
                        data.getGuardType()==0?true:false, userData.isLiang(), userData.getAccountId());
                danmakuActionManager.addDanmu(model);
            }

        }
    }

    private GiftNumDialog giftNumDialog;

    private void showGiftNumDialog() {
        giftNumDialog = new GiftNumDialog(getContext(), R.style.InputDialog);
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

    public void notifyMsg(final TCChatEntity entity) {

        if (mArrayListChatEntity.size() > 200) {
            mArrayListChatEntity.remove(0);
        }
        mArrayListChatEntity.add(entity);
        mChatMsgListAdapter.setDatas(mArrayListChatEntity);
        mListViewMsg.smoothScrollToPosition(mArrayListChatEntity.size()-1);
//        mChatMsgListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mDanmuMgr != null) {
            mDanmuMgr.resume();
        }
        if (giftDialogView != null) {
            giftDialogView.setHbCoin();
            giftDialogView.setYinCoin();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mDanmuMgr != null) {
            mDanmuMgr.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        isFullScreen = false;

        liveChatFragment = null;


        if (staticGiftModels.size() > 0) {
            staticGiftModels.clear();
        }
    }

    public void cancelTimer() {
        if (messageTimer != null) {
            messageTimer.cancel();
        }

        if (staticGiftTimer != null) {
            staticGiftTimer.cancel();
        }
        if (giftTimerTask != null) {
            giftTimerTask.cancel();
        }
    }

    ///房间聊天消息推送
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_ROOM_CHAT, threadMode = ThreadMode.MAIN)
    public void onReceiveRoomChatPush(RoomChatPush pushData) {
        String message = pushData.getMsg();
        String userId = pushData.getSender().getUserId();
        String nickname = pushData.getSender().getNickName();
        int level = pushData.getSender().getLevel();
//        if (userId.equals(SharePreferenceUtil.getSession(getContext(), "PREF_KEY_USERID"))) {
//
//        } else {
        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName(nickname + "  ");
        entity.setLevel(level);
        entity.setUserId(userId);
        entity.setContext(message);
        entity.setGuardType(pushData.getSender().getGuardType());
        entity.setGuardExpired(pushData.getSender().isGuardExpired());
        entity.setVip(pushData.getSender().getVip());
        entity.setVipExpired(pushData.getSender().isVipExpired());
        entity.setAccountId(pushData.getSender().getShowId());
        entity.setLiang(pushData.getSender().isLiang());
        entity.setType(TCConstants.TEXT_TYPE);
        notifyMsg(entity);
//        }
    }

    ///加入房间消息推送
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_NOTICE_JOIN_ROOM, threadMode = ThreadMode.MAIN)
    public void onReceiveJoinRoomPush(NoticeJoinRoomPush pushData) {
        int mountId = pushData.getMount();
        int level = pushData.getSender().getLevel();
        String userId = pushData.getSender().getUserId();
        String headUrl = pushData.getSender().getHeadUrl();
        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName("" + pushData.getSender().getNickName() + "  ");
        if (mountId != 0) {
            SysMountNewBean mountBean = mPresenter.getMountBeanByID(String.valueOf(mountId));
            if (mountBean != null) {
                entity.setContext("驾驶" + mountBean.getName() + "进入直播间");
            } else {
                entity.setContext("进入直播间");
            }
            if (mountBean != null && !isFullScreen) {
//                                showGift(userId, 0, "驾驶[" + mountBean.getName() + "]进入直播间", jsonObject.getString("NickName"), headUrl, mountId);
                if (!mountBean.getIsAnimation().equals("1")) {
                    GiftModel giftModel = new GiftModel(userId, 0,
                            "驾驶[" + mountBean.getName() + "]进入直播间", pushData.getSender().getNickName(), headUrl, mountId, false);
                    if(pushData.getSender().getVip()>0){
                        if(!pushData.getSender().isVipExpired()){

                        }else{
                            staticGiftModels.add(giftModel);
                        }
                    }else{
                        staticGiftModels.add(giftModel);
                    }
                } else {

                }
            }
            entity.setType(TCConstants.JOIN_TYPE);
        } else {
            entity.setContext("进入直播间");
            entity.setType(TCConstants.JOIN_TYPE);
        }
        entity.setGuardType(pushData.getSender().getGuardType());
        entity.setGuardExpired(pushData.getSender().isGuardExpired());
        entity.setVip(pushData.getSender().getVip());
        entity.setVipExpired(pushData.getSender().isVipExpired());
        entity.setAccountId(pushData.getSender().getShowId());
        entity.setLiang(pushData.getSender().isLiang());
        entity.setLevel(level);
        entity.setUserId(userId);
        notifyMsg(entity);

        if (pushData.getSender().getVip() > 0 || pushData.getSender().getGuardType() != 0) {
            if (!pushData.getSender().isVipExpired() || !pushData.getSender().isGuardExpired()) {
                DanmuModel model = new DanmuModel(headUrl, level,
                        pushData.getSender().getNickName()+entity.getContext(), pushData.getSender().getVip(),
                        pushData.getSender().isVipExpired(), pushData.getSender().getGuardType(),
                        pushData.getSender().isGuardExpired(), pushData.getSender().isLiang(), pushData.getSender().getShowId());
                danmakuActionManager.addDanmu(model);
            }
        }
    }

    ///收到礼物
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_ROOM_GIFT, threadMode = ThreadMode.MAIN)
    public void onReceiveRoomGiftPush(RoomGiftPush pushData) {
        int giftid = pushData.getGiftId();
        int giftcount = pushData.getGiftCnt();
        String userId = pushData.getSender().getUserId();
        String nickname = pushData.getSender().getNickName();
        int level = pushData.getSender().getLevel();
        String headUrl = pushData.getSender().getHeadUrl();
//        if (userId.equals(SharePreferenceUtil.getSession(getContext(), "PREF_KEY_USERID"))) {
//
//        } else {
        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName("" + nickname + "  ");
        SysGiftNewBean giftBean = mPresenter.getGiftBeanByID(String.valueOf(giftid));
        if (giftBean == null) {
            return;
        }
        entity.setContext("送出了" + giftBean.getName() + "×" + giftcount);
        entity.setType(TCConstants.GIFT_TYPE);
        entity.setGuardType(pushData.getSender().getGuardType());
        entity.setGuardExpired(pushData.getSender().isGuardExpired());
        entity.setVip(pushData.getSender().getVip());
        entity.setVipExpired(pushData.getSender().isVipExpired());
        entity.setAccountId(pushData.getSender().getShowId());
        entity.setLiang(pushData.getSender().isLiang());
        entity.setLevel(level);
        entity.setUserId(userId);
        notifyMsg(entity);
        if (!isFullScreen && !giftBean.getIsAnimation().equals("1")) {
            if((pushData.getSender().getVip()>0&&!pushData.getSender().isVipExpired())||(pushData.getSender().getGuardType()>0&&!pushData.getSender().isGuardExpired())){
                GiftModel giftModel = new GiftModel(userId, giftcount, giftBean.getName(),
                        nickname,
                        headUrl, giftid, true);
                staticGiftModels.add(giftModel);
            }else{
                GiftModel giftModel = new GiftModel(userId, giftcount, giftBean.getName(),
                        nickname,
                        headUrl, giftid, false);
                staticGiftModels.add(giftModel);
            }


        }
//        }
    }

    ///主播被关注
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_ROOM_FOLLOW, threadMode = ThreadMode.MAIN)
    public void onReceiveRoomFollowPush(RoomFollowPush pushData) {
        String userId = pushData.getSender().getUserId();
        String nickname = pushData.getSender().getNickName();
        int level = pushData.getSender().getLevel();
//        if (userId.equals(SharePreferenceUtil.getSession(getContext(), "PREF_KEY_USERID"))) {
//
//        } else {
        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName("  " + nickname + "  ");
        entity.setContext("关注了主播");
        entity.setUserId(userId);
        entity.setGuardType(pushData.getSender().getGuardType());
        entity.setGuardExpired(pushData.getSender().isGuardExpired());
        entity.setVip(pushData.getSender().getVip());
        entity.setVipExpired(pushData.getSender().isVipExpired());
        entity.setAccountId(pushData.getSender().getShowId());
        entity.setLiang(pushData.getSender().isLiang());
        entity.setType(TCConstants.CONCERN_TYPE);
        entity.setLevel(level);
        notifyMsg(entity);
//        }
    }

    ///用户被禁言
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_ROOM_BAN, threadMode = ThreadMode.MAIN)
    public void onReceiveRoomBanPush(RoomBanPush pushData) {
        String userId = pushData.getTarget().getUserId();
        String banName = pushData.getSender().getNickName();
        String nickname = pushData.getTarget().getNickName();
        int level = pushData.getTarget().getLevel();
        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName(nickname);
        entity.setGuardType(pushData.getSender().getGuardType());
        entity.setGuardExpired(pushData.getSender().isGuardExpired());
        entity.setVip(pushData.getSender().getVip());
        entity.setVipExpired(pushData.getSender().isVipExpired());
        entity.setAccountId(pushData.getSender().getShowId());
        entity.setLiang(pushData.getSender().isLiang());
        entity.setBanName("被" + banName);
        entity.setContext("禁言30分钟");
        entity.setLevel(level);
        entity.setType(TCConstants.BAN_TYPE);
        entity.setUserId(userId);
        notifyMsg(entity);
    }

    ///全站消息
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_ALL_ROOM_MSG, threadMode = ThreadMode.MAIN)
    public void onReceiveAllRoomMsgPush(AllRoomMsgPush pushData) {
        AppLogger.e("收到礼物");
        if (!isFullScreen) {
            allRoomMessage.add(pushData);
        }
    }

    ///房管更新
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_ROOM_MGR, threadMode = ThreadMode.MAIN)
    public void onReceiveRoomMgrPush(RoomMgrPush pushData) {
        boolean status = pushData.isStatus();
        if (status) {
            String userId = pushData.getTarget().getUserId();
            String nickname = pushData.getTarget().getNickName();
            int level = pushData.getTarget().getLevel();
            TCChatEntity entity = new TCChatEntity();
            entity.setSenderName("系统提示: ");
            entity.setUserId(userId);
            entity.setContext(nickname + "成为场控");
            entity.setLevel(level);
            entity.setType(TCConstants.SYSTEM_TYPE);
            notifyMsg(entity);
        }
    }

    ///数值变化
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_VALUE_CHANGE, threadMode = ThreadMode.MAIN)
    public void onReceiveValueChangePush(ValueChangePush pushData) {
        if (giftDialogView != null) {
            giftDialogView.setHbCoin();
            giftDialogView.setYinCoin();
        }

        if (pushData.getType().equals("Xiaolaba")) {
            if (talkGiftView != null) {
                talkGiftView.onFreshXiaolabaCount();
            }
        }
    }

}
