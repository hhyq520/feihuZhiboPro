package cn.feihutv.zhibofeihu.utils.glideutil;

import android.content.Context;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import cn.feihutv.zhibofeihu.utils.TCGlideCircleTransform;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/10/31 13:37
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class CustomOptions {

    public static RequestOptions getOptions(Context context, int resId) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(resId)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .transform(new TCGlideCircleTransform(context));

        return options;
    }

    public static RequestOptions getOptions(int resId, DiskCacheStrategy diskCacheStrategy) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(resId)
                .diskCacheStrategy(diskCacheStrategy);

        return options;
    }

    public static RequestOptions getOptions(int resId) {
        RequestOptions options = new RequestOptions()
                .placeholder(resId);

        return options;
    }

    public static RequestOptions getOptions(DiskCacheStrategy diskCacheStrategy) {
        RequestOptions options = new RequestOptions()
                .dontAnimate()
                .diskCacheStrategy(diskCacheStrategy);

        return options;
    }



    public static RequestOptions getCropOptions(int resId,int dp) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
               .transform(new GlideRoundTransformCenterCrop(dp))
                .placeholder(resId)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

        return options;
    }

}
