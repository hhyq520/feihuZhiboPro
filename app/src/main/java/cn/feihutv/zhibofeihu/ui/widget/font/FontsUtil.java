package cn.feihutv.zhibofeihu.ui.widget.font;


import android.content.Context;
import android.graphics.Typeface;

/**
 * 类名:FontsUtil <br/>
 * 作者 ：王洪贺 <br/>
 * 描述：获取自定义字体typefacespan的单例 <br/>
 * 2015年7月15日
 */
public class FontsUtil {

    public static FontsUtil fontsUtil;

    private Context mContext;
    private static Typeface numTypeface;
    private static Typeface charTypeface;

    public FontsUtil(Context context) {
        this.mContext = context;
        // 字体资源放在内存中，避免反复读取浪费资源
        numTypeface = Typeface.createFromAsset(mContext.getAssets(),
                "fonts/normal.ttf");
        charTypeface = Typeface.createFromAsset(mContext.getAssets(),
                "fonts/sthupo.ttf");

    }

    /**
     * <br/>
     * 概述：字体单例，避免反复读取 <br/>
     * 
     * @param context
     * <br/>
     * @return
     */
    public static FontsUtil getInstance(Context context) {
        if (fontsUtil == null) {
            fontsUtil = new FontsUtil(context);
        }
        return fontsUtil;
    }

    /**
     * <br/>
     * 概述：获取英文字母的字体typefacespan <br/>
     * 
     * @param context
     * <br/>
     * @return
     */
    public MyTypefaceSpan getMyCharTypefaceSpan() {
        return new MyTypefaceSpan(charTypeface);
    }

    /**
     * <br/>
     * 概述：获取数字的字体typefacespan <br/>
     * 
     * @param context
     * <br/>
     * @return
     */
    public MyTypefaceSpan getMyNumTypefaceSpan() {
        return new MyTypefaceSpan(numTypeface);
    }

}