package cn.feihutv.zhibofeihu.utils.glideutil;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import java.security.MessageDigest;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/12/02
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class GlideRoundTransformCenterCrop extends CenterCrop {
    private static float radius = 0f;


    public GlideRoundTransformCenterCrop(int dp) {
        super();
        this.radius = Resources.getSystem().getDisplayMetrics().density * dp;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform,
                               int outWidth, int outHeight) {
        Bitmap transform = super.transform(pool, toTransform, outWidth, outHeight);
        return roundCrop(pool, transform);
    }

    private static Bitmap roundCrop(BitmapPool pool, Bitmap source) {
        if (source == null)
            return null;
        Bitmap result = pool.get(source.getWidth(), source.getHeight(),
                Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(source.getWidth(), source.getHeight(),
                    Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP,
                BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
        canvas.drawRoundRect(rectF, radius, radius, paint);
        return result;
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {

    }

}
