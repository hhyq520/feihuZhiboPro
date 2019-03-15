package cn.feihutv.zhibofeihu.utils;

import android.app.Activity;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

/**
 * 分享所在的Activity里复写onActivityResult方法,注意不可在fragment中实现，
 * 如果在fragment中调用分享，在fragment依赖的Activity中实现，如果不实现onActivityResult方法，会导致分享或回调无法正常进行
 *  UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
 */
public class ShareUtil {
    private static UMWeb web;
    private static UMImage imageurl;
    /**
     *
     * @param activity QQ分享 6.0需要权限
     * @param text  内容
     * @param title  标题
     * @param imgurl  缩略图链接
     * @param url   点击链接
     * @param shareMedia 平台
     * @param shareListener 分享回调
     */
    public static void  ShareWeb(Activity activity, String text, String title, String imgurl, String url, SHARE_MEDIA shareMedia, UMShareListener shareListener){
        web = new UMWeb(url);
        web.setTitle(title);
        web.setThumb(new UMImage(activity,imgurl));
        web.setDescription(text);
        new ShareAction(activity)
                .withMedia(web)
                .setPlatform(shareMedia)
                .setCallback(shareListener).share();
    }

    /**
     *注意 QQ不支持纯文本分享
     * @param activity
     * @param text 文字
     * @param shareMedia 平台
     * @param shareListener 分享回调
     */
    public static void ShareText(Activity activity, String text, SHARE_MEDIA shareMedia, UMShareListener shareListener){
        new ShareAction(activity).withText(text)
                .setPlatform(shareMedia)
                .setCallback(shareListener).share();
    }

    /**
     *
     * @param activity QQ分享 6.0需要权限
     * @param url 图片网址
     * @param shareMedia 平台
     * @param shareListener 分享回调
     */
    public static void shareImageUrl(Activity activity, String url, SHARE_MEDIA shareMedia, UMShareListener shareListener){
        imageurl = new UMImage(activity,url);
        new ShareAction(activity).withMedia(imageurl)
                .setPlatform(shareMedia)
                .setCallback(shareListener).share();
    }
}
