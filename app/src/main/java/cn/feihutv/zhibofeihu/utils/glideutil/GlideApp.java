package cn.feihutv.zhibofeihu.utils.glideutil;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/10/31 13:59
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class GlideApp {

    public static void loadImg(Context context, String url, int resId, DiskCacheStrategy diskCacheStrategy, int fadeTime, ImageView imageView) {
        Glide.with(context).load(url)
                .apply(CustomOptions.getOptions(resId, diskCacheStrategy)).transition(DrawableTransitionOptions.withCrossFade(fadeTime)).into(imageView);
    }


    public static void loadImg(Context context, String url, int resId, ImageView imageView) {
        Glide.with(context).load(url).apply(CustomOptions.getOptions(resId)).into(imageView);
    }

    public static void loadImg(Context context, String url, int resId, int fadeTime, ImageView imageView) {
        Glide.with(context).load(url)
                .apply(CustomOptions.getOptions(resId)).transition(DrawableTransitionOptions.withCrossFade(fadeTime)).into(imageView);
    }

    public static void loadImgtransform(Context context, String url, int resId, ImageView imageView) {
        Glide.with(context).load(url)
                .apply(CustomOptions.getOptions(context, resId)).into(imageView);
    }

    public static void loadImg(Context context, String url, DiskCacheStrategy diskCacheStrategy, ImageView imageView) {
        Glide.with(context).load(url)
                .apply(CustomOptions.getOptions(diskCacheStrategy)).into(imageView);
    }


    public static void loadImg(Context context, String url,
                               DiskCacheStrategy diskCacheStrategy, SimpleTarget imageView) {
        Glide.with(context).asBitmap().load(url)
                .apply(CustomOptions.getOptions(diskCacheStrategy)).into(imageView);
    }


    public static void loadThumbnailImg(Context context, String url,String thumbnailUrl,
                               DiskCacheStrategy diskCacheStrategy, ImageView imageView,
                                @Nullable RequestListener requestListener) {
        Glide.with(context).load(url)
                .thumbnail(Glide.with(context).load(thumbnailUrl))
                .listener(requestListener)
                .apply(CustomOptions.getOptions(diskCacheStrategy)).into(imageView);
    }



    public static void loadCropImg(Context context, String url, int resId, ImageView imageView){
        Glide.with(context)
                .load(url)
                .apply(CustomOptions.getCropOptions(resId,5))
                .into(imageView);
    }


}
