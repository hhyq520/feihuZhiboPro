package cn.feihutv.zhibofeihu.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

public class TCCircleDrawable extends Drawable {

    private Paint mPaint;
    private Bitmap mBitmap;

    public TCCircleDrawable(Bitmap bitmap) {
        mBitmap = bitmap;
        BitmapShader bitmapShader = new BitmapShader(bitmap,
                Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setShader(bitmapShader);
    }



    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(getIntrinsicWidth() / 2, getIntrinsicHeight() / 2, getIntrinsicWidth() / 2, mPaint);
    }

    @Override
    public int getIntrinsicWidth() {
        return mBitmap.getWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        return mBitmap.getHeight();
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}

