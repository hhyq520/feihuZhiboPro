package cn.feihutv.zhibofeihu.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.db.model.SysConfigBean;
import cn.feihutv.zhibofeihu.data.db.model.SysGiftNewBean;
import cn.feihutv.zhibofeihu.data.local.SysdataHelper;
import cn.feihutv.zhibofeihu.data.local.model.TCChatEntity;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.ChatSender;
import cn.feihutv.zhibofeihu.ui.base.BasePresenter;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.BaseCacheStuffer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.ui.widget.DanmakuView;

/**
 * Created by clw on 2017/3/3.
 * 弹幕管理类
 */

public class TCDanmuMgr {

    private static final String TAG = "TCDanmuMgr";

    //弹幕显示的时间(如果是list的话，会 * i)，记得加上mDanmakuView.getCurrentTime()
    private static final long ADD_DANMU_TIME = 2000;

    private static final int PINK_COLOR = 0xffff5a93;//粉红 楼主
    private static final int ORANGE_COLOR = 0xffff815a;//橙色 我
    private static final int BLACK_COLOR = 0xb2000000;//黑色 普通

    private int BITMAP_WIDTH = 40;//
    private int BITMAP_HEIGHT = 16;
    private float DANMU_TEXT_SIZE = 10f;//弹幕字体的大小
//    private int   EMOJI_SIZE      = 14;//emoji的大小

    //这两个用来控制两行弹幕之间的间距
    private int DANMU_PADDING = 4;
    private int DANMU_PADDING_INNER = 4;
    private int DANMU_RADIUS = 5;//圆角半径

    private int mMsgColor = 0;

    private Context mContext;
    private DanmakuView mDanmakuView;
    private DanmakuContext mDanmakuContext;

    private HandlerThread mDanmuThread;
    private Handler mDanmuHandler;
    private boolean isPC = false;
    private boolean isPCLandTalk = false;
    private boolean isVip = false;
    private List<SysGiftNewBean> sysGiftNewBeanList = new ArrayList<>();
    private SysConfigBean sysConfigBeanList;

    public TCDanmuMgr(SysConfigBean sysConfigBeanList, List<SysGiftNewBean> sysGiftNewBeanList, Context context, boolean isPC, boolean isPCLandTalk, boolean isVip) {
        this.mContext = context;
        setSize(context);
        this.isVip = isVip;
        initDanmuConfig();
        mDanmuThread = new HandlerThread("DamuThread");
        mDanmuThread.start();
        mDanmuHandler = new Handler(mDanmuThread.getLooper());
        this.isPC = isPC;
        this.isPCLandTalk = isPCLandTalk;
        mMsgColor = mContext.getResources().getColor(R.color.app_white);
        this.sysGiftNewBeanList = sysGiftNewBeanList;
        this.sysConfigBeanList = sysConfigBeanList;
    }

    /**
     * 设置弹幕view
     *
     * @param danmakuView 弹幕view
     */
    public void setDanmakuView(DanmakuView danmakuView) {
        this.mDanmakuView = danmakuView;
        initDanmuView();
    }


    /**
     * 弹幕渲染暂停
     */
    public void pause() {
        if (mDanmakuView != null && mDanmakuView.isPrepared()) {
            mDanmakuView.pause();
        }
    }

    /**
     * 弹幕隐藏
     */
    public void hide() {
        if (mDanmakuView != null) {
            mDanmakuView.hide();
        }
    }

    /**
     * 弹幕显示
     */
    public void show() {
        if (mDanmakuView != null) {
            mDanmakuView.show();
        }
    }

    /**
     * 弹幕渲染恢复
     */
    public void resume() {
        if (mDanmakuView != null && mDanmakuView.isPrepared() && mDanmakuView.isPaused()) {
            mDanmakuView.resume();
        }
    }

    public void clear() {
        if (mDanmakuView != null) {
            mDanmakuView.clear();
            mDanmakuView.clearDanmakusOnScreen();
        }
    }

    /**
     * 弹幕资源释放
     */
    public void destroy() {
        if (mDanmuThread != null) {
            mDanmuThread.quit();
            mDanmuThread = null;
        }
        if (mDanmakuView != null) {
            mDanmakuView.release();
            mDanmakuView = null;
        }
        mContext = null;

    }

    /**
     * 添加一条弹幕消息
     */
    public void addDanmu(final TCChatEntity chatEntity) {
        if (mDanmuHandler != null) {
            mDanmuHandler.post(new Runnable() {
                @Override
                public void run() {
                    addDanmuInternal(chatEntity);
                }
            });
        }
    }

    /**
     * 添加一条互动游戏弹幕消息
     */
    public void addHudongDanmu(final String msg, final int min) {
        if (mDanmuHandler != null) {
            mDanmuHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mDanmakuView == null) {
                        return;
                    }
                    BaseDanmaku danmaku = mDanmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
                    if (danmaku == null) {
                        return;
                    }
                    danmaku.userId = 0;
                    danmaku.isGuest = false;//isGuest此处用来判断是赞还是评论
                    String text = "<font color='#FFBB00'>" + msg + "</font>" +
                            "<font color='#ffffff'>还有</font>" +
                            "<font color='#FFBB00'>" + min + "</font>" +
                            "<font color='#FFBB00'>分钟</font>" +
                            "<font color='#ffffff'>开启,各位主播加把劲为粉丝们谋福利哦!!!</font>";
                    danmaku.text = Html.fromHtml(text);
                    danmaku.padding = DANMU_PADDING;
                    danmaku.priority = 1;  // 1:一定会显示, 一般用于本机发送的弹幕,但会导致行数的限制失效
                    danmaku.isLive = true;
                    danmaku.time = mDanmakuView.getCurrentTime() + ADD_DANMU_TIME;
                    danmaku.textSize = DANMU_TEXT_SIZE;
                    danmaku.textColor = Color.WHITE;
                    danmaku.textShadowColor = 0; // 重要：如果有图文混排，最好不要设置描边(设textShadowColor=0)，否则会进行两次复杂的绘制导致运行效率降低
                    mDanmakuView.addDanmaku(danmaku);
                }
            });
        }
    }


    public void addVipDanmu(final ChatSender chatSender, final String headUrl, final String nickName, final String msg) {
        if (mDanmuHandler != null) {
            Observable.create(new ObservableOnSubscribe<Bitmap>() {
                @Override
                public void subscribe(final ObservableEmitter<Bitmap> emitter) throws Exception {
                    RequestBuilder<Bitmap> req = Glide.with(mContext).asBitmap().apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL));
                    req.load(headUrl).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            emitter.onNext(resource);
                            emitter.onComplete();
                        }
                    });
                }
            }).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread()).
                    subscribe(new Consumer<Bitmap>() {
                        @Override
                        public void accept(Bitmap bitmap) throws Exception {
                            if (mDanmakuView == null) {
                                return;
                            }
                            BaseDanmaku danmaku = mDanmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
                            if (danmaku == null) {
                                return;
                            }
                            danmaku.userId = 0;
                            danmaku.isGuest = false;//isGuest此处用来判断是赞还是评论
                            bitmap = makeRoundCorner(bitmap);
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("name", nickName);
                            map.put("content", msg);
                            map.put("bitmap", bitmap);
                            map.put("chatSender", chatSender);
                            mDanmakuView.setTag(map);


//                            TCCircleDrawable circleDrawable = new TCCircleDrawable(bitmap);
//                            circleDrawable.setBounds(0, 0, 23, 23);
//                            SpannableStringBuilder  spannable = createSpannable(circleDrawable, nickName, msg);
                            danmaku.text = "";
                            danmaku.padding = DANMU_PADDING;
                            danmaku.priority = 1;  // 1:一定会显示, 一般用于本机发送的弹幕,但会导致行数的限制失效
                            danmaku.isLive = true;
                            danmaku.time = mDanmakuView.getCurrentTime() + ADD_DANMU_TIME;
                            danmaku.textSize = 0;
                            danmaku.textColor = Color.WHITE;
                            danmaku.textShadowColor = 0; // 重要：如果有图文混排，最好不要设置描边(设textShadowColor=0)，否则会进行两次复杂的绘制导致运行效率降低
                            mDanmakuView.addDanmaku(danmaku);
                        }
                    });
        }
    }

    /**
     * 将图片变成圆形
     *
     * @param bitmap
     * @return
     */
    private static Bitmap makeRoundCorner(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int left = 0, top = 0, right = width, bottom = height;
        float roundPx = height / 2;
        if (width > height) {
            left = (width - height) / 2;
            top = 0;
            right = left + height;
            bottom = height;
        } else if (height > width) {
            left = 0;
            top = (height - width) / 2;
            right = width;
            bottom = top + width;
            roundPx = width / 2;
        }
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        int color = 0xff424242;
        Paint paint = new Paint();
        Rect rect = new Rect(left, top, right, bottom);
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }


    private SpannableStringBuilder createSpannable(Drawable drawable, String sender, String content) {
        String text = "bitmap";
        int spanLen = 0;
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);

        //head pic
        TCCenteredImageSpan span = new TCCenteredImageSpan(drawable);
        spannableStringBuilder.setSpan(span, 0, text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spanLen += text.length();

        //msg sender
        if (!TextUtils.isEmpty(sender)) {
            spannableStringBuilder.append(" ");
            spannableStringBuilder.append(sender.trim());
            spanLen += sender.trim().length() + 1;
        }

        //msg content
        if (!TextUtils.isEmpty(content)) {
            spannableStringBuilder.append(" ");
            spannableStringBuilder.append(content.trim());
            spanLen += 1;
            spannableStringBuilder.setSpan(new ForegroundColorSpan(mMsgColor), spanLen, spanLen + content.trim().length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        return spannableStringBuilder;
    }

    public void addSysDanmu(final String msg) {
        if (mDanmuHandler != null) {
            mDanmuHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mDanmakuView == null) {
                        return;
                    }
                    BaseDanmaku danmaku = mDanmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
                    if (danmaku == null) {
                        return;
                    }
                    danmaku.userId = 0;
                    danmaku.isGuest = false;//isGuest此处用来判断是赞还是评论
                    String text = "<font color='#ffffff'>" + msg + "</font>";
                    danmaku.text = Html.fromHtml(text);
                    danmaku.padding = DANMU_PADDING;
                    danmaku.priority = 1;  // 1:一定会显示, 一般用于本机发送的弹幕,但会导致行数的限制失效
                    danmaku.isLive = true;
                    danmaku.time = mDanmakuView.getCurrentTime() + ADD_DANMU_TIME;
                    danmaku.textSize = DANMU_TEXT_SIZE;
                    danmaku.textColor = Color.WHITE;
                    danmaku.textShadowColor = 0; // 重要：如果有图文混排，最好不要设置描边(设textShadowColor=0)，否则会进行两次复杂的绘制导致运行效率降低
                    mDanmakuView.addDanmaku(danmaku);
                }
            });
        }
    }

    public void addGameStartRound60s() {
        if (mDanmuHandler != null) {
            mDanmuHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mDanmakuView == null) {
                        return;
                    }
                    BaseDanmaku danmaku = mDanmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
                    if (danmaku == null) {
                        return;
                    }
                    danmaku.userId = 0;
                    danmaku.isGuest = false;//isGuest此处用来判断是赞还是评论
                    String text = "<font color='#FFBB00'>游戏竞猜</font>" +
                            "<font color='#ffffff'>还有</font>" +
                            "<font color='#FFBB00'>1分钟</font>" +
                            "<font color='#ffffff'>开启新回合!!!</font>";
                    danmaku.text = Html.fromHtml(text);
                    danmaku.padding = DANMU_PADDING;
                    danmaku.priority = 1;  // 1:一定会显示, 一般用于本机发送的弹幕,但会导致行数的限制失效
                    danmaku.isLive = true;
                    danmaku.time = mDanmakuView.getCurrentTime() + ADD_DANMU_TIME;
                    danmaku.textSize = DANMU_TEXT_SIZE;
                    danmaku.textColor = Color.WHITE;
                    danmaku.textShadowColor = 0; // 重要：如果有图文混排，最好不要设置描边(设textShadowColor=0)，否则会进行两次复杂的绘制导致运行效率降低
                    mDanmakuView.addDanmaku(danmaku);
                }
            });
        }
    }

    public void addGameDanmu(final String type, final String msg, final String roomId) {
        if (mDanmuHandler != null) {
            mDanmuHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mDanmakuView == null) {
                        return;
                    }
                    BaseDanmaku danmaku = mDanmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
                    if (danmaku == null) {
                        return;
                    }
                    danmaku.userId = 0;
                    danmaku.isGuest = false;//isGuest此处用来判断是赞还是评论
                    String text = "<font color='#ffffff'>恭喜</font>" +
                            "<font color='#FFBB00'>" + msg + "</font>" +
                            "<font color='#ffffff'>获得下一轮</font>" +
                            "<font color='#FFBB00'>" + type + "的开奖权限</font>";
                    danmaku.text = Html.fromHtml(text, imageGetter, null);
                    danmaku.padding = DANMU_PADDING;
                    danmaku.priority = 1;  // 1:一定会显示, 一般用于本机发送的弹幕,但会导致行数的限制失效
                    danmaku.isLive = true;
                    danmaku.time = mDanmakuView.getCurrentTime() + ADD_DANMU_TIME;
                    danmaku.textSize = DANMU_TEXT_SIZE;
                    danmaku.textColor = Color.WHITE;
                    danmaku.textShadowColor = 0; // 重要：如果有图文混排，最好不要设置描边(设textShadowColor=0)，否则会进行两次复杂的绘制导致运行效率降低
                    mDanmakuView.addDanmaku(danmaku);
                }
            });
        }
    }

    public void addDaPaoDanmu(final String roomname, final String senderName, final String roomId) {
        if (mDanmuHandler != null) {
            mDanmuHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mDanmakuView == null) {
                        return;
                    }
                    BaseDanmaku danmaku = mDanmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
                    if (danmaku == null) {
                        return;
                    }
                    danmaku.userId = Integer.parseInt(roomId);
                    danmaku.isGuest = false;//isGuest此处用来判断是赞还是评论
                    String text = "<font color='#FFBB00'>" + senderName + "</font>" +
                            "<font color='#ffffff'>在</font>" +
                            "<font color='#FFBB00'>" + roomname + "</font>" +
                            "<font color='#ffffff'>的直播间,送出高射炮快来围观吧！</font>";
                    danmaku.text = Html.fromHtml(text);
                    danmaku.padding = DANMU_PADDING;
                    danmaku.priority = 1;  // 1:一定会显示, 一般用于本机发送的弹幕,但会导致行数的限制失效
                    danmaku.isLive = true;
                    danmaku.time = mDanmakuView.getCurrentTime() + ADD_DANMU_TIME;
                    danmaku.textSize = DANMU_TEXT_SIZE;
                    danmaku.textColor = Color.WHITE;
                    danmaku.textShadowColor = 0; // 重要：如果有图文混排，最好不要设置描边(设textShadowColor=0)，否则会进行两次复杂的绘制导致运行效率降低
                    mDanmakuView.addDanmaku(danmaku);
                }
            });
        }
    }

    public void addMoney300Danmu(final String senderName, final String roomname, final int giftId, final int giftCnt, final String roomId) {
        if (mDanmuHandler != null) {
            mDanmuHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mDanmakuView == null) {
                        return;
                    }
                    BaseDanmaku danmaku = mDanmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
                    if (danmaku == null) {
                        return;
                    }
                    danmaku.userId = Integer.valueOf(roomId);
                    ;
                    danmaku.isGuest = false;//isGuest此处用来判断是赞还是评论
                    SysGiftNewBean bean = SysdataHelper.getGiftBeanByID(sysGiftNewBeanList, String.valueOf(giftId));
                    String giftName = "";
                    if (bean != null) {
                        giftName = bean.getName();
                    }

                    String text = "<font color='#FFBB00'>" + senderName + "</font>" +
                            "<font color='#ffffff'>在</font>" +
                            "<font color='#FFBB00'>" + roomname + "</font>" +
                            "<font color='#ffffff'>的直播间,送出</font>" +
                            "<font color='#ffffff'>&nbsp&nbsp</font>" +
                            "<font color='#FFBB00'>" + giftName + "</font>" +
                            "<font color='#FFBB00'>×" + giftCnt + "</font>";
                    danmaku.text = Html.fromHtml(text);
                    danmaku.padding = DANMU_PADDING;
                    danmaku.priority = 1;  // 1:一定会显示, 一般用于本机发送的弹幕,但会导致行数的限制失效
                    danmaku.isLive = true;
                    danmaku.time = mDanmakuView.getCurrentTime() + ADD_DANMU_TIME;
                    danmaku.textSize = DANMU_TEXT_SIZE;
                    danmaku.textColor = Color.WHITE;
                    danmaku.textShadowColor = 0; // 重要：如果有图文混排，最好不要设置描边(设textShadowColor=0)，否则会进行两次复杂的绘制导致运行效率降低
                    mDanmakuView.addDanmaku(danmaku);
                }
            });
        }
    }

    public void addGuardSuccess(final String senderName, final String roomName) {
        if (mDanmuHandler != null) {
            mDanmuHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mDanmakuView == null) {
                        return;
                    }
                    BaseDanmaku danmaku = mDanmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
                    if (danmaku == null) {
                        return;
                    }
                    danmaku.userId = 0;
                    danmaku.isGuest = false;//isGuest此处用来判断是赞还是评论
                    String text = "<font color='#FFBB00'>" + senderName + "</font>" +
                            "<font color='#ffffff'>成功守护主播，果然是真爱啊~</font>" +
                            "<font color='#FFBB00'>" + "[" + roomName + "</font>" +
                            "<font color='#FFBB00'>的直播间]</font>";
                    danmaku.text = Html.fromHtml(text);
                    danmaku.padding = DANMU_PADDING;
                    danmaku.priority = 1;  // 1:一定会显示, 一般用于本机发送的弹幕,但会导致行数的限制失效
                    danmaku.isLive = true;
                    danmaku.time = mDanmakuView.getCurrentTime() + ADD_DANMU_TIME;
                    danmaku.textSize = DANMU_TEXT_SIZE;
                    danmaku.textColor = Color.WHITE;
                    danmaku.textShadowColor = 0; // 重要：如果有图文混排，最好不要设置描边(设textShadowColor=0)，否则会进行两次复杂的绘制导致运行效率降低
                    mDanmakuView.addDanmaku(danmaku);
                }
            });
        }
    }

    public void addVipSuccess(final String senderName) {
        if (mDanmuHandler != null) {
            mDanmuHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mDanmakuView == null) {
                        return;
                    }
                    BaseDanmaku danmaku = mDanmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
                    if (danmaku == null) {
                        return;
                    }
                    danmaku.userId = 0;
                    danmaku.isGuest = false;//isGuest此处用来判断是赞还是评论
                    String text =
                            "<font color='#ffffff'>恭喜</font>" +
                                    "<font color='#FFBB00'>" + senderName + "</font>" +
                                    "<font color='#ffffff'>开通VIP，成为全平台的贵宾！</font>";
                    danmaku.text = Html.fromHtml(text);
                    danmaku.padding = DANMU_PADDING;
                    danmaku.priority = 1;  // 1:一定会显示, 一般用于本机发送的弹幕,但会导致行数的限制失效
                    danmaku.isLive = true;
                    danmaku.time = mDanmakuView.getCurrentTime() + ADD_DANMU_TIME;
                    danmaku.textSize = DANMU_TEXT_SIZE;
                    danmaku.textColor = Color.WHITE;
                    danmaku.textShadowColor = 0; // 重要：如果有图文混排，最好不要设置描边(设textShadowColor=0)，否则会进行两次复杂的绘制导致运行效率降低
                    mDanmakuView.addDanmaku(danmaku);
                }
            });
        }
    }

    public void addGamewinDanmu(final String hubi, final String senderName) {
        if (mDanmuHandler != null) {
            mDanmuHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mDanmakuView == null) {
                        return;
                    }
                    BaseDanmaku danmaku = mDanmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
                    if (danmaku == null) {
                        return;
                    }
                    danmaku.userId = 0;
                    danmaku.isGuest = false;//isGuest此处用来判断是赞还是评论
                    String text = "<font color='#FFBB00'>" + senderName + "</font>" +
                            "<font color='#ffffff'>在高赔率竞猜中竞猜成功获得</font>" +
                            "<font color='#FFBB00'>" + hubi + "</font>" +
                            "<font color='#FFBB00'>虎币！</font>";
                    danmaku.text = Html.fromHtml(text);
                    danmaku.padding = DANMU_PADDING;
                    danmaku.priority = 1;  // 1:一定会显示, 一般用于本机发送的弹幕,但会导致行数的限制失效
                    danmaku.isLive = true;
                    danmaku.time = mDanmakuView.getCurrentTime() + ADD_DANMU_TIME;
                    danmaku.textSize = DANMU_TEXT_SIZE;
                    danmaku.textColor = Color.WHITE;
                    danmaku.textShadowColor = 0; // 重要：如果有图文混排，最好不要设置描边(设textShadowColor=0)，否则会进行两次复杂的绘制导致运行效率降低
                    mDanmakuView.addDanmaku(danmaku);
                }
            });
        }
    }

    public void addCCAwardDanmu(final String hubi, final String senderName, final int giftId) {
        if (mDanmuHandler != null) {
            mDanmuHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mDanmakuView == null) {
                        return;
                    }
                    BaseDanmaku danmaku = mDanmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
                    if (danmaku == null) {
                        return;
                    }
                    danmaku.userId = 0;
                    danmaku.isGuest = false;//isGuest此处用来判断是赞还是评论

                    String gift = "";
                    if (giftId == 1) {
                        gift = "小黄瓜";
                    } else if (giftId == 2) {
                        gift = "小香蕉";
                    } else if (giftId == 8) {
                        gift = "小爱心";
                    } else if (giftId == 20) {
                        gift = "飞虎流星";
                    }
                    String text = "<font color='#ffffff'>恭喜</font>" +
                            "<font color='#FFBB00'>" + senderName + "</font>" +
                            "<font color='#ffffff'>爆得" + gift + "大奖，获得</font>" +
                            "<font color='#FFBB00'>" + hubi + "虎币</font>" +
                            "<font color='#ffffff'>奖励！</font>";
                    danmaku.text = Html.fromHtml(text);
                    danmaku.padding = DANMU_PADDING;
                    danmaku.priority = 1;  // 1:一定会显示, 一般用于本机发送的弹幕,但会导致行数的限制失效
                    danmaku.isLive = true;
                    danmaku.time = mDanmakuView.getCurrentTime() + ADD_DANMU_TIME;
                    danmaku.textSize = DANMU_TEXT_SIZE;
                    danmaku.textColor = Color.WHITE;
                    danmaku.textShadowColor = 0; // 重要：如果有图文混排，最好不要设置描边(设textShadowColor=0)，否则会进行两次复杂的绘制导致运行效率降低
                    mDanmakuView.addDanmaku(danmaku);
                }
            });
        }
    }

    public void addLabaDanmu(final String msg, final String senderName, final String masterName, final boolean isAtRoom, final String roomId) {
        if (mDanmuHandler != null) {
            mDanmuHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mDanmakuView == null) {
                        return;
                    }
                    BaseDanmaku danmaku = mDanmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
                    if (danmaku == null) {
                        return;
                    }
                    danmaku.userId = Integer.valueOf(roomId);
                    danmaku.isGuest = false;//isGuest此处用来判断是赞还是评论
                    String text = "";
                    if (isAtRoom) {
                        text = "<img src=\"" + R.drawable.laba + "\" />" +
                                "<font color='#ffffff'>&nbsp&nbsp</font>" +
                                "<font color='#FFBB00'>" + senderName + ":</font>" +
                                "<font color='#ffffff'>" + msg + "</font>";
                    } else {
                        text = "<img src=\"" + R.drawable.laba + "\" />" +
                                "<font color='#ffffff'>&nbsp&nbsp</font>" +
                                "<font color='#FFBB00'>" + senderName + ":</font>" +
                                "<font color='#ffffff'>" + msg + "</font>" +
                                "<font color='#eF3425'>[</font>" +
                                "<font color='#eF3425'>" + masterName + "</font>" +
                                "<font color='#eF3425'>的直播间]</font>";
                    }
                    danmaku.text = Html.fromHtml(text, imageGetter, null);
                    danmaku.padding = DANMU_PADDING;
                    danmaku.priority = 1;  // 1:一定会显示, 一般用于本机发送的弹幕,但会导致行数的限制失效
                    danmaku.isLive = true;
                    danmaku.time = mDanmakuView.getCurrentTime() + ADD_DANMU_TIME;
                    danmaku.textSize = DANMU_TEXT_SIZE;
                    danmaku.textColor = Color.WHITE;
                    danmaku.textShadowColor = 0; // 重要：如果有图文混排，最好不要设置描边(设textShadowColor=0)，否则会进行两次复杂的绘制导致运行效率降低
                    mDanmakuView.addDanmaku(danmaku);
                }
            });
        }
    }

    private Bitmap tempBitmap;
    private String textTemp;

    public void addGiftDanmu(final String senderName, final String roomName, final int giftId, final int giftCnt, final boolean isMoney180, final String roomId, final boolean isShowVIp) {
        if (mDanmuHandler != null) {
            mDanmuHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mDanmakuView == null) {
                        return;
                    }
                    SysGiftNewBean sysGiftNewBean = SysdataHelper.getGiftBeanByID(sysGiftNewBeanList, String.valueOf(giftId));
                    String url = "";
                    if (sysConfigBeanList != null) {
                        if (isShowVIp && sysGiftNewBean.getEnableVip() == 1) {
                            String name = sysGiftNewBean.getIcon().substring(0, sysGiftNewBean.getIcon().lastIndexOf(".")) + "_vip.png";
                            url = sysConfigBeanList.getCosGiftRootPath() + "/" + name;
                        } else {
                            url = sysConfigBeanList.getCosGiftRootPath() + "/" + sysGiftNewBean.getIcon();
                        }

                    }
                    final String downUrl = url;
                    Observable.create(new ObservableOnSubscribe<Bitmap>() {
                        @Override
                        public void subscribe(final ObservableEmitter<Bitmap> emitter) throws Exception {
                            RequestBuilder<Bitmap> req = Glide.with(mContext).asBitmap().apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL));
                            req.load(downUrl).into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                    emitter.onNext(resource);
                                    emitter.onComplete();
                                }
                            });
                        }
                    }).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread()).
                            subscribe(new Consumer<Bitmap>() {
                                @Override
                                public void accept(Bitmap bitmap) throws Exception {
                                    tempBitmap = bitmap;
                                    final BaseDanmaku danmaku = mDanmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
                                    if (danmaku == null) {
                                        return;
                                    }
                                    danmaku.userId = Integer.valueOf(roomId);
                                    danmaku.isGuest = false;//isGuest此处用来判断是赞还是评论


                                    if (isMoney180) {
                                        textTemp = "<font color='#FFBB00'>" + senderName + "</font>" +
                                                "<font color='#ffffff'>送给</font>" +
                                                "<font color='#FFBB00'>" + roomName + "</font>" +
                                                "<font color='#ffffff'>&nbsp&nbsp</font>" +
                                                "<img src=\"" + downUrl + "\"/>" +
                                                "<font color='#FFBB00' >×" + giftCnt + "</font>";

                                    } else {
                                        textTemp = "<font color='#FFBB00'>" + senderName + "</font>" +
                                                "<font color='#ffffff'>在</font>" +
                                                "<font color='#FFBB00'>" + roomName + "</font>" +
                                                "<font color='#ffffff'>的直播间送出</font>" +
                                                "<font color='#ffffff'>&nbsp&nbsp</font>" +
                                                "<img src=\"" + downUrl + "\" />" +
                                                "<font color='#FFBB00' >×" + giftCnt + "</font>";

                                    }
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                        danmaku.text = Html.fromHtml(textTemp, Html.FROM_HTML_MODE_LEGACY, imageGetterBitmap, null);
                                    } else {
                                        danmaku.text = Html.fromHtml(textTemp, imageGetterBitmap, null);
                                    }
                                    danmaku.padding = 6;
                                    danmaku.priority = 1;  // 1:一定会显示, 一般用于本机发送的弹幕,但会导致行数的限制失效
                                    danmaku.isLive = true;
                                    if (mDanmakuView != null) {
                                        danmaku.time = mDanmakuView.getCurrentTime() + ADD_DANMU_TIME;
                                    }
                                    danmaku.textSize = DANMU_TEXT_SIZE;
                                    danmaku.textColor = Color.WHITE;
                                    danmaku.textShadowColor = 0; // 重要：如果有图文混排，最好不要设置描边(设textShadowColor=0)，否则会进行两次复杂的绘制导致运行效率降低
                                    mDanmakuView.addDanmaku(danmaku);
                                }
                            });

                }
            });
        }
    }


    Html.ImageGetter imageGetter = new Html.ImageGetter() {
        public Drawable getDrawable(String source) {
            Drawable drawable = null;
            int rId = Integer.parseInt(source);
            drawable = ContextCompat.getDrawable(mContext, rId);
            int width = TCUtils.dip2px(mContext, 20) * drawable.getIntrinsicWidth() / drawable.getIntrinsicHeight();
            drawable.setBounds(0, 0, width, TCUtils.dip2px(mContext, 20));
            return drawable;
        }
    };

    Html.ImageGetter imageGetterBitmap = new Html.ImageGetter() {
        public Drawable getDrawable(String source) {
            Drawable drawable = null;
            drawable = new BitmapDrawable(mContext.getResources(), tempBitmap);
            int width = TCUtils.dip2px(mContext, 20) * drawable.getIntrinsicWidth() / drawable.getIntrinsicHeight();
            drawable.setBounds(0, 0, width, TCUtils.dip2px(mContext, 20));
            return drawable;

//            InputStream is = null;
//            try {
//                is = (InputStream) new URL(source).getContent();
//                Drawable d = Drawable.createFromStream(is, "src");
//                d.setBounds(0, 0, d.getIntrinsicWidth(),
//                        d.getIntrinsicHeight());
//                is.close();
//                return d;
//            } catch (Exception e) {
//                return null;
//            }
        }
    };

    /**
     * 对数值进行转换，适配手机，必须在初始化之前，否则有些数据不会起作用
     */
    private void setSize(Context context) {
        BITMAP_WIDTH = TCUtils.dp2pxConvertInt(context, 28);
        BITMAP_HEIGHT = TCUtils.dp2pxConvertInt(context, BITMAP_HEIGHT);
//        EMOJI_SIZE = TCUtils.dp2pxConvertInt(context, EMOJI_SIZE);
        DANMU_PADDING = TCUtils.dp2pxConvertInt(context, DANMU_PADDING);
        DANMU_PADDING_INNER = TCUtils.dp2pxConvertInt(context, DANMU_PADDING_INNER);
        DANMU_RADIUS = TCUtils.dp2pxConvertInt(context, DANMU_RADIUS);
        DANMU_TEXT_SIZE = TCUtils.sp2px(context, DANMU_TEXT_SIZE);
    }

    /**
     * 初始化配置
     */
    private void initDanmuConfig() {
        // 设置最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap<>();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 2); // 滚动弹幕最大显示2行

        // 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);

        mDanmakuContext = DanmakuContext.create();

        if (isVip) {
            mDanmakuContext
                    .setDanmakuStyle(IDisplayer.DANMAKU_STYLE_NONE)
                    .setDuplicateMergingEnabled(false)
                    .setScrollSpeedFactor(2f)//越大速度越慢
                    .setScaleTextSize(1.2f)
                    .setCacheStuffer(new VipCacheStuffer(mContext), mCacheStufferAdapter)
                    .setMaximumLines(maxLinesPair)
                    .preventOverlapping(overlappingEnablePair);
        } else {
            mDanmakuContext
                    .setDanmakuStyle(IDisplayer.DANMAKU_STYLE_NONE)
                    .setDuplicateMergingEnabled(false)
                    .setScrollSpeedFactor(2f)//越大速度越慢
                    .setScaleTextSize(1.2f)
                    .setCacheStuffer(new BackgroundCacheStuffer(), mCacheStufferAdapter)
                    .setMaximumLines(maxLinesPair)
                    .preventOverlapping(overlappingEnablePair);
        }
    }


    public class VipCacheStuffer extends BaseCacheStuffer {
        private int AVATAR_DIAMETER; //头像直径
        private int AVATAR_PADDING; // 头像边框宽度
        private int TEXT_LEFT_PADDING; // 文字和头像间距
        private int TEXT_RIGHT_PADDING; // 文字和右边线距离
        private int TEXT_SIZE; // 文字大小

        private int NICK_COLOR = 0xffff1874;//昵称 红色
        private int TEXT_COLOR = 0xffeeeeee;  //文字内容  白色
        private int TEXT_BG_COLOR = 0x66000000; // 文字灰色背景色值
        private int TEXT_BG_RADIUS; // 文字灰色背景圆角

        public VipCacheStuffer(Context context) {
            // 初始化固定参数，这些参数可以根据自己需求自行设定
            AVATAR_DIAMETER = TCUtils.dip2px(context, 33);
            AVATAR_PADDING = TCUtils.dip2px(context, 1);
            TEXT_LEFT_PADDING = TCUtils.dip2px(context, 2);
            TEXT_RIGHT_PADDING = TCUtils.dip2px(context, 8);
            TEXT_SIZE = TCUtils.dip2px(context, 13);
            TEXT_BG_RADIUS = TCUtils.dip2px(context, 8);
        }

        @Override
        public void measure(BaseDanmaku danmaku, TextPaint paint, boolean fromWorkerThread) {
            // 初始化数据
            Map<String, Object> map = (Map<String, Object>) mDanmakuView.getTag();
            String name = (String) map.get("name");
            String content = (String) map.get("content");

            // 设置画笔
            paint.setTextSize(TEXT_SIZE);

            // 计算名字和内容的长度，取最大值
            float nameWidth = paint.measureText(name);
            float contentWidth = paint.measureText(content);
            float maxWidth = Math.max(nameWidth, contentWidth);

            // 设置弹幕区域的宽高
            danmaku.paintWidth = maxWidth + AVATAR_DIAMETER + AVATAR_PADDING * 2 + TEXT_LEFT_PADDING + TEXT_RIGHT_PADDING; // 设置弹幕区域的宽度
            danmaku.paintHeight = AVATAR_DIAMETER + AVATAR_PADDING * 2; // 设置弹幕区域的高度
        }

        @Override
        public void drawStroke(BaseDanmaku danmaku, String lineText, Canvas canvas, float left, float top, Paint paint) {
        }

        @Override
        public void drawText(BaseDanmaku danmaku, String lineText, Canvas canvas, float left, float top, TextPaint paint, boolean fromWorkerThread) {
        }

        @Override
        public void clearCaches() {
        }

        @Override
        public void drawBackground(BaseDanmaku danmaku, Canvas canvas, float left, float top) {
            // 初始化数据
            Map<String, Object> map = (Map<String, Object>) mDanmakuView.getTag();
            String name = (String) map.get("name");
            String content = (String) map.get("content");
            Bitmap bitmap = (Bitmap) map.get("bitmap");

            // 设置画笔
            Paint paint = new Paint();
            paint.setTextSize(TEXT_SIZE);

            // 绘制文字灰色背景
            Rect rect = new Rect();
            paint.getTextBounds(content, 0, content.length(), rect);
            paint.setColor(TEXT_BG_COLOR);
            paint.setAntiAlias(true);
            float bgLeft = left + AVATAR_DIAMETER / 2 + AVATAR_PADDING;
            float bgTop = top + AVATAR_DIAMETER / 2 + AVATAR_PADDING;
            float bgRight = left + AVATAR_DIAMETER + AVATAR_PADDING * 2 + TEXT_LEFT_PADDING + rect.width() + TEXT_RIGHT_PADDING;
            float bgBottom = top + AVATAR_DIAMETER + AVATAR_PADDING;
            canvas.drawRoundRect(new RectF(bgLeft, bgTop, bgRight, bgBottom), TEXT_BG_RADIUS, TEXT_BG_RADIUS, paint);

            // 绘制头像背景
            paint.setColor(Color.WHITE);
            float centerX = left + AVATAR_DIAMETER / 2 + AVATAR_PADDING;
            float centerY = left + AVATAR_DIAMETER / 2 + AVATAR_PADDING;
            float radius = AVATAR_DIAMETER / 2 + AVATAR_PADDING; // 半径
            canvas.drawCircle(centerX, centerY, radius, paint);

            // 绘制头像
            float avatorLeft = left + AVATAR_PADDING;
            float avatorTop = top + AVATAR_PADDING;
            float avatorRight = left + AVATAR_PADDING + AVATAR_DIAMETER;
            float avatorBottom = top + AVATAR_PADDING + AVATAR_DIAMETER;
            canvas.drawBitmap(bitmap, null, new RectF(avatorLeft, avatorTop, avatorRight, avatorBottom), paint);

            // 绘制名字
            paint.setColor(NICK_COLOR);
            float nameLeft = left + AVATAR_DIAMETER + AVATAR_PADDING * 2 + TEXT_LEFT_PADDING;
            float nameBottom = top + rect.height() + AVATAR_PADDING + (AVATAR_DIAMETER / 2 - rect.height()) / 2;
//            canvas.drawText(name, nameLeft, nameBottom, paint);
            canvas.drawBitmap(bitmap, null, new RectF(nameLeft, avatorTop, avatorRight, nameBottom), paint);

            // 绘制弹幕内容
            paint.setColor(TEXT_COLOR);
            float contentLeft = nameLeft;
            float contentBottom = top + AVATAR_PADDING + AVATAR_DIAMETER / 2 + rect.height() + (AVATAR_DIAMETER / 2 - rect.height()) / 2;
            canvas.drawText(content, contentLeft, contentBottom, paint);
        }
    }


    /**
     * 绘制背景(自定义弹幕样式)
     */
    private class BackgroundCacheStuffer extends SpannedCacheStuffer {
        // 通过扩展SimpleTextCacheStuffer或SpannedCacheStuffer个性化你的弹幕样式
        final Paint paint = new Paint();

        @Override
        public void measure(BaseDanmaku danmaku, TextPaint paint, boolean fromWorkerThread) {
//            danmaku.padding = 10;  // 在背景绘制模式下增加padding
            super.measure(danmaku, paint, fromWorkerThread);
        }

        @Override
        public void drawBackground(BaseDanmaku danmaku, Canvas canvas, float left, float top) {
            paint.setAntiAlias(true);
//            if (!danmaku.isGuest && danmaku.userId == mGoodUserId && mGoodUserId != 0) {
//                paint.setColor(PINK_COLOR);//粉红 楼主
//            } else if (!danmaku.isGuest && danmaku.userId == mMyUserId
//                    && danmaku.userId != 0) {
//                paint.setColor(ORANGE_COLOR);//橙色 我
//            } else {
//                paint.setColor(BLACK_COLOR);//黑色 普通
//            }
//            if (danmaku.isGuest) {//如果是赞 就不要设置背景
//                paint.setColor(Color.TRANSPARENT);
//            }
            if (isVip) {
                NinePatchDrawable bg = (NinePatchDrawable) mContext.getResources().getDrawable(R.drawable.barrage);
                if (bg != null) {
                    bg.setBounds((int) left + 7, (int) top + 5, (int) danmaku.paintWidth, (int) danmaku.paintHeight);
                    bg.draw(canvas);
                }
            } else {
                if (isPCLandTalk) {
                    paint.setColor(Color.argb(127, 33, 33, 45));
                    canvas.drawRoundRect(new RectF(left + 8, top + DANMU_PADDING_INNER
                                    , left + danmaku.paintWidth - DANMU_PADDING_INNER + 8,
                                    top + danmaku.paintHeight - DANMU_PADDING_INNER + 6),//+6 主要是底部被截得太厉害了，+6是增加padding的效果
                            DANMU_RADIUS, DANMU_RADIUS, paint);
                } else {
                    if (isPC) {
                        NinePatchDrawable bg = (NinePatchDrawable) mContext.getResources().getDrawable(R.drawable.notice);
                        if (bg != null) {
                            bg.setBounds((int) left + 9, (int) top + 5, (int) danmaku.paintWidth, (int) danmaku.paintHeight+5);
                            bg.draw(canvas);
                        }
                    } else {
//                    paint.setColor(Color.argb(255,62,54,92));
//                    canvas.drawRoundRect(new RectF(left + 8, top + DANMU_PADDING_INNER
//                                    , left + danmaku.paintWidth - DANMU_PADDING_INNER + 8,
//                                    top + danmaku.paintHeight - DANMU_PADDING_INNER + 6),//+6 主要是底部被截得太厉害了，+6是增加padding的效果
//                            DANMU_RADIUS, DANMU_RADIUS, paint);
                        NinePatchDrawable bg = (NinePatchDrawable) mContext.getResources().getDrawable(R.drawable.icon_chat);
                        if (bg != null) {
                            bg.setBounds((int) left + 7, (int) top + 3, (int) danmaku.paintWidth, (int) danmaku.paintHeight);
                            bg.draw(canvas);
                        }
                    }

                }

            }
//            Drawable bg;
//            if(!isPCLandTalk) {
//                if (isPC) {
//                    bg = (Drawable) mContext.getResources().getDrawable(R.drawable.danmu_shape);
//                } else {
//                    bg = (Drawable) mContext.getResources().getDrawable(R.drawable.danmu_shape);
//                }
//                if (bg != null) {
//                    bg.setBounds((int) left + 6, (int) top+6, (int) danmaku.paintWidth + 6, (int) danmaku.paintHeight + 6);
//                    bg.draw(canvas);
//                }
//            }else{
//                bg = (Drawable) mContext.getResources().getDrawable(R.drawable.danmu_shape);
//                if (bg != null) {
//                    bg.setBounds((int) left + 6, (int) top+6, (int) danmaku.paintWidth + 6, (int) danmaku.paintHeight + 6);
//                    bg.draw(canvas);
//                }
//            }
        }

        @Override
        public void drawStroke(BaseDanmaku danmaku, String lineText, Canvas canvas, float left, float top, Paint paint) {
            // 禁用描边绘制
        }
    }

    private BaseCacheStuffer.Proxy mCacheStufferAdapter = new BaseCacheStuffer.Proxy() {

        @Override
        public void prepareDrawing(final BaseDanmaku danmaku, boolean fromWorkerThread) {
//            if (danmaku.text instanceof Spanned) { // 根据你的条件检查是否需要需要更新弹幕
//            }
        }

        @Override
        public void releaseResource(BaseDanmaku danmaku) {
            // TODO 重要:清理含有ImageSpan的text中的一些占用内存的资源 例如drawable
            if (danmaku.text instanceof Spanned) {
                danmaku.text = "";
            }
            mDanmakuView.setTag(null);
        }
    };

    private void initDanmuView() {
        if (mDanmakuView != null) {
            mDanmakuView.setCallback(new DrawHandler.Callback() {
                @Override
                public void prepared() {
                    mDanmakuView.start();
                }

                @Override
                public void updateTimer(DanmakuTimer timer) {

                }

                @Override
                public void danmakuShown(BaseDanmaku danmaku) {

                }

                @Override
                public void drawingFinished() {

                }
            });
            mDanmakuView.prepare(new BaseDanmakuParser() {

                @Override
                protected Danmakus parse() {
                    return new Danmakus();
                }
            }, mDanmakuContext);
            mDanmakuView.enableDanmakuDrawingCache(true);
        }
    }

    private void addDanmuInternal(final TCChatEntity chatEntity) {
        if (mDanmakuView == null) {
            return;
        }

        Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(final ObservableEmitter<Bitmap> emitter) throws Exception {
                RequestBuilder<Bitmap> req = Glide.with(mContext).asBitmap().apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL));
                // TODO: 2017/12/15
                req.load("https://img.feihutv.cn/Level/rank_" + chatEntity.getLevel() + ".png").into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        emitter.onNext(resource);
                        emitter.onComplete();
                    }
                });
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        BaseDanmaku danmaku = mDanmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
                        if (danmaku == null) {
                            return;
                        }
                        danmaku.userId = 0;
                        danmaku.isGuest = false;//isGuest此处用来判断是赞还是评论

                        SpannableStringBuilder spannable;
                        danmaku.text = createSpastring(bitmap, chatEntity);
                        danmaku.padding = DANMU_PADDING;
                        danmaku.priority = 1;  // 1:一定会显示, 一般用于本机发送的弹幕,但会导致行数的限制失效
                        danmaku.isLive = true;
                        danmaku.time = mDanmakuView.getCurrentTime() + ADD_DANMU_TIME;
                        danmaku.textSize = DANMU_TEXT_SIZE;
                        if (chatEntity.getType() == TCConstants.GIFT_TYPE) {
                            danmaku.textColor = ContextCompat.getColor(mContext, R.color.appColor);
                        } else if (chatEntity.getType() == TCConstants.TEXT_TYPE) {
                            danmaku.textColor = ContextCompat.getColor(mContext, R.color.app_white);
                        } else if (chatEntity.getType() == TCConstants.JOIN_TYPE) {
                            danmaku.textColor = ContextCompat.getColor(mContext, R.color.app_white);
                        } else if (chatEntity.getType() == TCConstants.CONCERN_TYPE) {
                            danmaku.textColor = ContextCompat.getColor(mContext, R.color.appColor);
                        } else if (chatEntity.getType() == TCConstants.BAN_TYPE) {
                            danmaku.textColor = ContextCompat.getColor(mContext, R.color.appColor);
                        }

                        danmaku.textShadowColor = 0; // 重要：如果有图文混排，最好不要设置描边(设textShadowColor=0)，否则会进行两次复杂的绘制导致运行效率降低
                        mDanmakuView.addDanmaku(danmaku);
                    }
                });
    }

    private Bitmap getDefaultBitmap(Bitmap bitmap) {
        Bitmap mDefauleBitmap = null;
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix matrix = new Matrix();
            matrix.postScale(((float) 62) / width, ((float) 36) / height);
            mDefauleBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        }
        return mDefauleBitmap;
    }


    private SpannableString createSpastring(Bitmap level, TCChatEntity item) {
        SpannableString spanString;
        if (item.getLevel() == 0) {
            spanString = new SpannableString(item.getSenderName() + item.getContext());
        } else {
            if (TextUtils.isEmpty(item.getZuojia())) {
                spanString = new SpannableString("bitmap" + item.getSenderName() + item.getContext());
            } else {
                spanString = new SpannableString("bitmap" + item.getSenderName() + item.getZuojia() + item.getContext());
            }
        }

        if (item.getLevel() == 0) {
            spanString = new SpannableString(item.getSenderName() + item.getContext());
        } else {
            if (TextUtils.isEmpty(item.getZuojia())) {
                spanString = new SpannableString("bitmap" + item.getSenderName() + item.getContext());
            } else {
                spanString = new SpannableString("bitmap" + item.getSenderName() + item.getZuojia() + item.getContext());
            }
        }

        String text = "bitmap";

        if (item.getType() == TCConstants.GIFT_TYPE) {
            spanString.setSpan(new ImageSpan(mContext, getDefaultBitmap(level), ImageSpan.ALIGN_BASELINE), 0, text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            spanString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.app_white)),
                    text.length(), text.length() + item.getSenderName().length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        } else if (item.getType() == TCConstants.TEXT_TYPE) {
            // 根据名称计算颜色
            SpannableString sb;
            if (item.getLevel() != 0) {
                spanString.setSpan(new ImageSpan(mContext, getDefaultBitmap(level), ImageSpan.ALIGN_BASELINE), 0, text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                spanString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.app_white)),
                        text.length(), text.length() + item.getSenderName().length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

                sb = handler(spanString,
                        "bitmap" + item.getSenderName() + item.getContext());
            } else {
                spanString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.appColor)),
                        0, item.getSenderName().length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

                sb = handler(spanString,
                        item.getSenderName() + item.getContext());
            }
            return sb;
        } else if (item.getType() == TCConstants.JOIN_TYPE) {
            SpannableString sb;
            spanString.setSpan(new ImageSpan(mContext, getDefaultBitmap(level), ImageSpan.ALIGN_BASELINE), 0, text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            spanString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.app_white)),
                    text.length(), text.length() + item.getSenderName().length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            spanString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.appColor)),
                    text.length() + item.getSenderName().length(), text.length() + item.getSenderName().length() + item.getZuojia().length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            sb = handler(spanString,
                    "bitmap" + item.getSenderName() + item.getContext());
            return sb;
        } else if (item.getType() == TCConstants.CONCERN_TYPE) {
            SpannableString sb;
            spanString.setSpan(new ImageSpan(mContext, getDefaultBitmap(level), ImageSpan.ALIGN_BASELINE), 0, text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            spanString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.app_white)),
                    text.length(), text.length() + item.getSenderName().length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            sb = handler(spanString,
                    "bitmap" + item.getSenderName() + item.getContext());
            return sb;
        } else if (item.getType() == TCConstants.BAN_TYPE) {
            SpannableString spanString1 = new SpannableString(item.getSenderName() + item.getBanName() + item.getContext());
            spanString1.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.app_white)),
                    0, item.getSenderName().length() + item.getBanName().length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            return spanString1;
        }
        return spanString;
    }


    private Bitmap getDefaultBitmap(int drawableId) {
        Bitmap mDefauleBitmap = null;
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), drawableId);
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix matrix = new Matrix();
            matrix.postScale(((float) BITMAP_WIDTH) / width, ((float) BITMAP_HEIGHT) / height);
            mDefauleBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        }
        return mDefauleBitmap;
    }

    private SpannableStringBuilder createSpannable(Bitmap bitmap, String sender, String content) {
        String text = "bitmap";
        int spanLen = 0;
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);

        //head pic
        //TCCenteredImageSpan span = new TCCenteredImageSpan(drawable);
        spannableStringBuilder.setSpan(new ImageSpan(mContext, bitmap), 0, text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spanLen += text.length();

        //msg sender
        if (!TextUtils.isEmpty(sender)) {
            spannableStringBuilder.append(" ");
            spannableStringBuilder.append(sender.trim());
            spanLen += sender.trim().length() + 1;
        }

        //msg content
        if (!TextUtils.isEmpty(content)) {
            spannableStringBuilder.append(" ");
            spannableStringBuilder.append(content.trim());
            spanLen += 1;
            spannableStringBuilder.setSpan(new ForegroundColorSpan(mMsgColor), 0, spanLen + content.trim().length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            spanLen = spanLen + content.trim().length();
            handler(spannableStringBuilder, content, spanLen);


        }
        return spannableStringBuilder;
    }

    private SpannableString createSpannableString(Bitmap bitmap, String sender, String content) {
        String text = "bitmap";
        int spanLen = 0;
        SpannableString spannableString = new SpannableString(text + " " + sender + " " + content);
        //head pic

        handler(spannableString, text + " " + sender + " " + content);
        //TCCenteredImageSpan span = new TCCenteredImageSpan(drawable);
        spannableString.setSpan(new ImageSpan(mContext, bitmap), 0, text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }


    private SpannableStringBuilder handler(SpannableStringBuilder sb, String content, int spalen) {
        String regex = "(\\[f_static_)\\d{1,3}(\\])";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        while (m.find()) {
            String tempText = m.group();
            try {
                String png = "face/png/" + tempText.substring("[".length(), tempText.length() - "]".length()) + ".png";
                try {
                    sb.setSpan(new ImageSpan(mContext, BitmapFactory.decodeStream(mContext.getAssets().open(png))), 0, m.end() - m.start(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    spalen=spalen+(m.end()-m.start());
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            } catch (Exception e) {
                Log.i("amp", "haha");
                e.printStackTrace();
            }
        }
        return sb;
    }

    private SpannableString handler(SpannableString sb, String content) {
        String regex = "(\\[f_static_)\\d{1,3}(\\])";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        while (m.find()) {
            String tempText = m.group();
            try {
                String png = "face/png/" + tempText.substring("[".length(), tempText.length() - "]".length()) + ".png";
                try {
                    sb.setSpan(new ImageSpan(mContext, BitmapFactory.decodeStream(mContext.getAssets().open(png))), m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            } catch (Exception e) {
                Log.d("amp", e.toString());
                e.printStackTrace();
            }
        }
        return sb;
    }
}
