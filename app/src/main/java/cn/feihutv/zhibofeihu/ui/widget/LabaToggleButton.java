package cn.feihutv.zhibofeihu.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import cn.feihutv.zhibofeihu.R;

/**
 * Created by huanghao on 2017/11/7.
 */

public class LabaToggleButton extends View {
    private Bitmap onBackgroundImage;
    private Bitmap offBackgroundImage;
    private Bitmap blockImage;
    private Bitmap offBlockImage;
    private ToggleState state = ToggleState.Open;
    private int currentX;
    private int mouseDownX = -1;
    private boolean isMove = false;
    private ToggleState preState;
    private boolean isInvalidate = true;
    public LabaToggleButton(Context context) {
        super(context);
        onBackgroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.icon_bg_gray);
        offBackgroundImage = BitmapFactory.decodeResource(getResources(),  R.drawable.icon_bg_yellow);
        blockImage = BitmapFactory.decodeResource(getResources(), R.drawable.icon_laba_yellow);
        offBlockImage=BitmapFactory.decodeResource(getResources(), R.drawable.icon_laba_gray);
    }

    public LabaToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        onBackgroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.icon_bg_gray);
        offBackgroundImage = BitmapFactory.decodeResource(getResources(),  R.drawable.icon_bg_yellow);
        blockImage = BitmapFactory.decodeResource(getResources(), R.drawable.icon_laba_yellow);
        offBlockImage=BitmapFactory.decodeResource(getResources(), R.drawable.icon_laba_gray);
    }

    private OnClickListener clickListener = null;

    /*
     * 其实应该是stateChange事件，懒得改了
     */
    public void SetOnClickListener(OnClickListener listener) {
        if (listener != null)
            clickListener = listener;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //layout方法是在ondraw方法之前执行，
        //在这个做判断是防止用户在设置Image之前setState，
        //这样Image为null，系统无法获取block宽度，也就无法设置currentX坐标,
        //系统将采用默认Open状态,会出现即使用户设置状态为close，而界面显示仍未open状态,
        //在这里判断，是因为当前image和state肯定已经设置完毕了
        if (state == ToggleState.Open) {
            currentX = 2;
        } else {
            if (blockImage == null || offBackgroundImage == null || onBackgroundImage == null)
                return;
            currentX=offBackgroundImage.getWidth()-blockImage.getWidth()-2;
        }
    }
    public enum ToggleState {
        Open, Close
    }


    public void setState(ToggleState state) {
        preState = this.state = state;
        if (state == ToggleState.Open) {
            currentX = 2;
        } else {
            if (blockImage != null && offBackgroundImage != null)
                currentX = offBackgroundImage.getWidth() - blockImage.getWidth() - 2;
        }
        invalidate();
    }

    public ToggleState getState() {
        return state;
    }




    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(onBackgroundImage.getWidth(), onBackgroundImage.getHeight());

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 有效性判断，如果其中有一个为空，拒绝绘制，继续绘制也没有意义
        if (blockImage == null || offBackgroundImage == null || onBackgroundImage == null)
            return;
        if ((currentX + blockImage.getWidth() / 2) > onBackgroundImage.getWidth() / 2) {
            canvas.drawBitmap(offBackgroundImage, 0, 0, null);
            canvas.drawBitmap(blockImage, currentX-2, 4, null);
        } else {
            canvas.drawBitmap(onBackgroundImage, 0, 0, null);
            canvas.drawBitmap(offBlockImage, currentX+2, 4, null);
        }
//        canvas.drawBitmap(blockImage, currentX, 1, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        currentX = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 记录鼠标按下时的x
                mouseDownX = (int) event.getX();
                // 按下的时候不进行重绘
                isInvalidate = false;
                break;
            case MotionEvent.ACTION_MOVE:
                // 记录鼠标发生了移动
                isMove = true;
                break;
            case MotionEvent.ACTION_UP:
                // 判断鼠标按下和抬起过程中是否发生了移动
                // 鼠标抬起时，判断当前x坐标位置
                if (isMove) {
                    // 发生了移动，判断当前位置
                    if ((currentX + blockImage.getWidth() / 2) > onBackgroundImage.getWidth() / 2) {
                        // 背景图的后半段
                        currentX = onBackgroundImage.getWidth() - blockImage.getWidth();
                        state = ToggleState.Close;
                    } else {
                        // 背景图的前半段
                        currentX = 2;
                        state = ToggleState.Open;
                    }
                } else {
                    // 没有发生移动,即为点击事件，更改状态，同时改变滑块位置
                    if (state == ToggleState.Open) {
                        state = ToggleState.Close;
                        currentX = onBackgroundImage.getWidth() - blockImage.getWidth() - 2;
                    } else {
                        state = ToggleState.Open;
                        currentX = 2;
                    }
                }
                // 复位，以免影响下一次的触摸事件
                isMove = false;
                if (preState != state && clickListener != null) {
                    clickListener.onClick(this);
                    preState = state;
                }
                break;
        }
        if (currentX < 2)
            currentX = 2;
        if (currentX + blockImage.getWidth() >= onBackgroundImage.getWidth())
            currentX = onBackgroundImage.getWidth() - blockImage.getWidth() - 2;
        // 通知控件绘制
        if (isInvalidate)
            invalidate();
        else
            isInvalidate = true;
        return true;
    }

}