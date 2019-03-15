package cn.feihutv.zhibofeihu.utils;

/**
 * Created by Administrator on 2017/2/14.
 */

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * Glide图像裁剪
 */
public class TCGlideCircleTransform extends BitmapTransformation {

    public TCGlideCircleTransform(Context context){
        super(context);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return TCUtils.createCircleImage(toTransform, 0);
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {

    }
}
