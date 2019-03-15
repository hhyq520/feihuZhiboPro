package cn.feihutv.zhibofeihu.ui.widget.DanmuBase;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.db.model.SysFontColorBean;
import cn.feihutv.zhibofeihu.data.local.model.TCChatEntity;
import cn.feihutv.zhibofeihu.utils.TCConstants;
import cn.feihutv.zhibofeihu.utils.TCUtils;


/**
 * Created by walkingMen on 2016/5/12.
 */
public class DanmakuLandChannel extends FrameLayout {

    public boolean isRunning = false;
    public TCChatEntity mEntity;
    private DanmakuLandActionInter danAction;

    public DanmakuLandActionInter getDanAction() {
        return danAction;
    }

    public void setDanAction(DanmakuLandActionInter danAction) {
        this.danAction = danAction;
    }

    public DanmakuLandChannel(Context context) {
        super(context);
        init();
    }


    public DanmakuLandChannel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DanmakuLandChannel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DanmakuLandChannel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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


    public void setDanmakuEntity(TCChatEntity entity) {
        mEntity = entity;
    }

    public void mStartAnimation(TCChatEntity entity) {
        isRunning = true;
        setDanmakuEntity(entity);
        if (mEntity != null) {
            final View view = View.inflate(getContext(), R.layout.item_pcland_danmu, null);
            ImageView imgLevel=(ImageView)view.findViewById(R.id.img_level);
            ImageView imgVip=(ImageView)view.findViewById(R.id.img_vip);
            ImageView imgGuard=(ImageView)view.findViewById(R.id.img_guard);
            TextView textLiang=(TextView) view.findViewById(R.id.text_liang);
            TextView textNickname=(TextView) view.findViewById(R.id.text_nickname);
            TextView textContent=(TextView) view.findViewById(R.id.text_content);
                SysFontColorBean sysFontColorBean1=FeihuZhiboApplication.getApplication().mDataManager.getSysFontColorBeanByKey("normalChat");
                SysFontColorBean sysFontColorBean2=FeihuZhiboApplication.getApplication().mDataManager.getSysFontColorBeanByKey("vipChat");
                String color1= " ";
                color1="#"+ sysFontColorBean1.getPhone();
                TCUtils.showLevelWithUrl(getContext(),imgLevel,entity.getLevel());
                if(entity.getVip()>0){
                    if(entity.isVipExpired()){
                        imgVip.setVisibility(GONE);
                    }else{
                        color1="#"+ sysFontColorBean2.getPhone();
                        String cosVipIconRootPath = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosVipIconRootPath();
                        Glide.with(getContext()).load(cosVipIconRootPath + "icon_vip" + entity.getVip() + ".png").into(imgVip);
                    }
                }else {
                    imgVip.setVisibility(GONE);
                }
                if(entity.getGuardType()==0){
                    imgGuard.setVisibility(GONE);
                }else{
                    if(entity.isGuardExpired()){
                        color1="#"+ sysFontColorBean2.getPhone();
                        imgGuard.setVisibility(GONE);
                    }else{
                        imgGuard.setVisibility(VISIBLE);
                    }
                }
                if(entity.isLiang()){
                    TCUtils.setFontBeauti(getContext(),textLiang);
                }else {
                    TCUtils.setFontNormal(getContext(),textLiang);
                }
                textLiang.setText(entity.getAccountId());
                SysFontColorBean sysFontColorBean=FeihuZhiboApplication.getApplication().mDataManager.getSysFontColorBeanByKey("nickname");
                String color="#"+ sysFontColorBean.getPhone();


            if(entity.getType()== TCConstants.TEXT_TYPE){
                textNickname.setText(entity.getSenderName());
                textNickname.setTextColor(Color.parseColor(color));
                textContent.setText(entity.getContext());
                textContent.setTextColor(Color.parseColor(color1));
            }else if(entity.getType()== TCConstants.JOIN_TYPE){
                textNickname.setText(entity.getSenderName());
                textNickname.setTextColor(Color.parseColor(color));
                textContent.setText(entity.getZuojia()+entity.getContext());
                textContent.setTextColor(Color.parseColor("#e8a9ff"));
            }else if(entity.getType()== TCConstants.CONCERN_TYPE){
                textNickname.setText(entity.getSenderName());
                textNickname.setTextColor(Color.parseColor(color));
                textContent.setText(entity.getContext());
                textContent.setTextColor(Color.parseColor("#e8a9ff"));
            }else if(entity.getType()== TCConstants.BAN_TYPE){
                imgGuard.setVisibility(GONE);
                imgVip.setVisibility(GONE);
                textLiang.setVisibility(GONE);
                textNickname.setText(entity.getSenderName());
                textNickname.setTextColor(Color.parseColor(color));
                textContent.setText(entity.getBanName()+entity.getContext());
                textContent.setTextColor(Color.parseColor("#fd593f"));
            }



            view.measure(-1, -1);
            int measuredWidth = view.getMeasuredWidth();
            int measuredHeight = view.getMeasuredHeight();
            int leftMargin = getContext().getResources().getDisplayMetrics().widthPixels;
            final Animation anim = AnimationHelper.createTranslateAnim(getContext(), leftMargin, -measuredWidth);
            anim.setDuration(8000);

            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
                @Override
                public void onAnimationEnd(Animation animation) {
                    if (!((Activity) getContext()).isDestroyed()) {//防止内存溢出
                        new Handler().post(new Runnable() {
                            public void run() {
                                view.clearAnimation();
                                DanmakuLandChannel.this.removeView(view);
                                if (danAction != null) {
                                    danAction.pollDanmu();
                                }
                            }
                        });
                    }
                    isRunning = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            view.startAnimation(anim);
            this.addView(view);
        }
    }
}
