package cn.feihutv.zhibofeihu.ui.widget;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/3/9.
 */

public class CustomsChildViewPager extends ViewPager {

    /** 触摸时按下的点 **/
    PointF downP = new PointF();
        /** 触摸时当前的点 **/
    PointF curP = new PointF();
    OnSingleTouchListener onSingleTouchListener;

    public CustomsChildViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomsChildViewPager(Context context) {
        super(context);
    }

    private float mDownPosX;
    private float mDownPosY;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        //当拦截触摸事件到达此位置的时候，返回true，
        //说明将onTouch拦截在此控件，进而执行此控件的onTouchEvent

        final float x = arg0.getX();
        final float y = arg0.getY();
        final int action = arg0.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownPosX = x;
                mDownPosY = y;

                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaX = Math.abs(x - mDownPosX);
                final float deltaY = Math.abs(y - mDownPosY);
                // 这里是够拦截的判断依据是左右滑动，读者可根据自己的逻辑进行是否拦截
                if (deltaX < deltaY) {
                    return false;
                }
        }

        return super.onInterceptTouchEvent(arg0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        //每次进行onTouch事件都记录当前的按下的坐标
        int position = 0;
        curP.x = arg0.getX();
        curP.y = arg0.getY();
        if(arg0.getAction() == MotionEvent.ACTION_DOWN){
            position = this.getCurrentItem();
            //记录按下时候的坐标
            //切记不可用 downP = curP ，这样在改变curP的时候，downP也会改变
            downP.x = arg0.getX();
            downP.y = arg0.getY();
            //此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
            if (position == 2) {
                getParent().requestDisallowInterceptTouchEvent(false);
            } else {
                getParent().requestDisallowInterceptTouchEvent(true);
            }
        }
        if(arg0.getAction() == MotionEvent.ACTION_MOVE){
            //此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
            position = this.getCurrentItem();
            if (position == 2) {
                getParent().requestDisallowInterceptTouchEvent(false);
            } else {
                getParent().requestDisallowInterceptTouchEvent(true);
            }
        }

        if(arg0.getAction() == MotionEvent.ACTION_UP){
            //在up时判断是否按下和松手的坐标为一个点
            //如果是一个点，将执行点击事件，这是我自己写的点击事件，而不是onclick
            if(downP.x==curP.x && downP.y==curP.y){
                onSingleTouch();
                return true;
            }
        }
        return super.onTouchEvent(arg0);
    }

        /**
         * 单击
         */
    public void onSingleTouch() {
        if (onSingleTouchListener!= null) {
            onSingleTouchListener.onSingleTouch();
        }
    }

        /**
         * 创建点击事件接口
         * @author wanpg
         *
         */
    public interface OnSingleTouchListener {
        void onSingleTouch();
    }

    public void setOnSingleTouchListener(OnSingleTouchListener onSingleTouchListener) {
        this.onSingleTouchListener = onSingleTouchListener;
    }


}
