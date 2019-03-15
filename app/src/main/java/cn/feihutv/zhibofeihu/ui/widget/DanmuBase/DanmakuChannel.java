package cn.feihutv.zhibofeihu.ui.widget.DanmuBase;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.tools.ScreenUtils;

import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.local.model.TCChatEntity;
import cn.feihutv.zhibofeihu.utils.TCUtils;


/**
 * Created by walkingMen on 2016/5/12.
 */
public class DanmakuChannel extends FrameLayout {

    public boolean isRunning = false;
    public DanmuModel mEntity;
    private DanmakuActionInter danAction;
    private boolean isLand=false;
    public DanmakuActionInter getDanAction() {
        return danAction;
    }

    public void setDanAction(DanmakuActionInter danAction) {
        this.danAction = danAction;
    }

    public DanmakuChannel(Context context) {
        super(context);
        init();
    }

    public void setIsLand(){
        this.isLand=true;
    }

    public DanmakuChannel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DanmakuChannel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DanmakuChannel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.danmaku_channel_layout, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.setClipToOutline(false);
        }
    }


    public void setDanmakuEntity(DanmuModel entity) {
        mEntity = entity;
    }

    public void mStartAnimation(DanmuModel entity) {
        isRunning = true;
        setDanmakuEntity(entity);
        if (mEntity != null) {
            final View view = View.inflate(getContext(), R.layout.item_live_danmu, null);
            ImageView imgHead=(ImageView) view.findViewById(R.id.img_head);
            ImageView img_level=(ImageView) view.findViewById(R.id.img_level);
            ImageView img_vip=(ImageView) view.findViewById(R.id.img_vip);
            ImageView img_guard=(ImageView) view.findViewById(R.id.img_guard);
            TextView textContent=(TextView) view.findViewById(R.id.content);
            TextView textLiang=(TextView) view.findViewById(R.id.text_liang);
            TCUtils.showPicWithUrl(getContext(), imgHead, entity.getHeadUrl(), R.drawable.face);
            TCUtils.showLevelWithUrl(getContext(),img_level,entity.getLevel());
            if(entity.getVip()>0){
                if(entity.isVipExpired()){
                    img_vip.setVisibility(GONE);
                }else{
                    img_vip.setVisibility(VISIBLE);
                    String cosVipIconRootPath = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosVipIconRootPath();
                    Glide.with(getContext()).load(cosVipIconRootPath + "icon_vip" + entity.getVip() + ".png").into(img_vip);
                }
            }else {
                img_vip.setVisibility(GONE);
            }
            if(entity.getGuardType()==0){
                img_guard.setVisibility(GONE);
            }else{
                if(entity.isGuardExpired()){
                    img_guard.setVisibility(GONE);
                }else{
                    img_guard.setVisibility(VISIBLE);
                }
            }
            textLiang.setText(entity.getAccountId());
            if(entity.isLiang()){
                TCUtils.setFontBeauti(getContext(),textLiang);
            }else{
                Typeface fontFace2 = Typeface.createFromAsset(getContext().getAssets(), "fonts/normal.ttf");
                textLiang.setTextColor(getContext().getResources().getColor(R.color.app_white));
                textLiang.setShadowLayer(1, 0, 2, Color.parseColor("#00000000"));
                textLiang.setTypeface(fontFace2);
            }

            textContent.setText(entity.getContent());
            view.measure(-1, -1);
            int measuredWidth = view.getMeasuredWidth();
            int measuredHeight = view.getMeasuredHeight();
            int leftMargin = getContext().getResources().getDisplayMetrics().widthPixels;

            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "translationX",leftMargin,0);
            if(!isLand) {
                objectAnimator.setDuration(1500);
            }else {
                objectAnimator.setDuration(5500);
            }
            objectAnimator.start();
            objectAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    next(view);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
                });



//            final Animation anim = AnimationHelper.createTranslateAnim(getContext(), leftMargin, -measuredWidth);
//            anim.setDuration(4500);
//            anim.setAnimationListener(new Animation.AnimationListener() {
//                @Override
//                public void onAnimationStart(Animation animation) {
//
//                }
//
//                @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
//                @Override
//                public void onAnimationEnd(Animation animation) {
//                    if (!((Activity) getContext()).isDestroyed()) {//防止内存溢出
//                        new Handler().post(new Runnable() {
//                            public void run() {
//                                view.clearAnimation();
//                                DanmakuChannel.this.removeView(view);
//                                if (danAction != null) {
//                                    danAction.pollDanmu();
//                                }
//                            }
//                        });
//                    }
//                    isRunning = false;
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation animation) {
//                }
//            });
//            view.startAnimation(anim);
            this.addView(view);
        }
    }

    private void next(final View view){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int leftMargin = getContext().getResources().getDisplayMetrics().widthPixels;
                int measuredWidth = view.getMeasuredWidth();
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "translationX",0,-measuredWidth);
                objectAnimator.setDuration(1500);
                objectAnimator.start();
                objectAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                    if (!((Activity) getContext()).isDestroyed()) {//防止内存溢出
                        new Handler().post(new Runnable() {
                            public void run() {
                                view.clearAnimation();
                                DanmakuChannel.this.removeView(view);
                                if (danAction != null) {
                                    danAction.pollDanmu();
                                }
                            }
                        });
                    }
                    isRunning = false;



                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
            }
        }, 2000);//2秒后执行Runnable中的run方法
    }
}
