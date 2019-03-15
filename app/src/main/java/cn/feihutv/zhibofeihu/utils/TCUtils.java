package cn.feihutv.zhibofeihu.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.feihutv.zhibofeihu.BuildConfig;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseResponse;
import cn.feihutv.zhibofeihu.data.prefs.AppPreferencesHelper;


/**
 * Created by Administrator on 2017/2/14.
 */
public class TCUtils {

    /**
     * 验证手机格式
     * 判断手机号是否有效
     */
    public static boolean isMobileNO(String mobiles) {
        /*
         * 手机号码
         * 移动：134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188
         * 联通：130,131,132,145,152,155,156,1709,171,176,185,186
         * 电信：133,134,153,1700,177,180,181,189
         * 总结起来就是第一位必定为1，第二位必定为3或4或5或7或8，其他位置的可以为0-9
         */
        String telRegex = "(13\\d|14[57]|15[^4,\\D]|17[678]|18\\d)\\d{8}|170[059]\\d{7}";
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        } else {
            return mobiles.length() == 11 && mobiles.matches(telRegex);
        }
    }

    public static boolean isPsd(String str) {
        // 只允许字母和数字
        String regEx = "^[a-zA-Z0-9]\\w{5,14}$";
        if (TextUtils.isEmpty(str)) {
            return false;
        } else {
            return str.matches(regEx);
        }
    }

    public static boolean isNumPsd(String str) {
        // 判断是否为纯数字
        String regEx = "^\\d+$";
        if (TextUtils.isEmpty(str)) {
            return false;
        } else {
            return str.matches(regEx);
        }
    }

    /**
     * @param str
     * @return
     */
    public static boolean isZimuPsd(String str) {
        // 判断是否为纯字母
        String regEx = "^([a-zA-Z]+)$";
        if (TextUtils.isEmpty(str)) {
            return false;
        } else {
            return str.matches(regEx);
        }
    }


    /**
     * 判断手机号是否有效
     *
     * @param phoneNum 手机号
     * @return 有效则返回true, 无效则返回false
     */
    public static boolean isPhoneNumValid(String phoneNum) {
        return phoneNum.length() == 11 && phoneNum.matches("[0-9]{1,}");
    }


    // 判断是否符合身份证号码的规范
    public static boolean isIDCard(String IDCard) {
        String s = IDCard.toLowerCase();
        if (IDCard != null) {
            String IDCardRegex = "\\d{17}[\\d|x]|\\d{15}";
            return s.matches(IDCardRegex);
        }
        return false;
    }

    /**
     * 在按钮上启动一个定时器
     *
     * @param tvVerifyCode  验证码控件
     * @param defaultString 按钮上默认的字符串
     * @param max           失效时间（单位：s）
     * @param interval      更新间隔（单位：s）
     */
    private static CountDownTimer timer;

    public static void startTimer(final Context context, final WeakReference<TextView> tvVerifyCode,
                                  final String defaultString,
                                  int max,
                                  int interval) {
        tvVerifyCode.get().setEnabled(false);

        // 由于CountDownTimer并不是准确计时，在onTick方法调用的时候，time会有1-10ms左右的误差，这会导致最后一秒不会调用onTick()
        // 因此，设置间隔的时候，默认减去了10ms，从而减去误差。
        // 经过以上的微调，最后一秒的显示时间会由于10ms延迟的积累，导致显示时间比1s长max*10ms的时间，其他时间的显示正常,总时间正常
        timer = new CountDownTimer(max * 1000, interval * 1000 - 10) {

            @Override
            public void onTick(long time) {
                // 第一次调用会有1-10ms的误差，因此需要+15ms，防止第一个数不显示，第二个数显示2s
                if (null == tvVerifyCode.get())
                    this.cancel();
                else {
                    tvVerifyCode.get().setText("" + ((time + 15) / 1000) + "s");
                    tvVerifyCode.get().setTextColor(context.getResources().getColor(R.color.noticetextcolor));
                }
            }

            @Override
            public void onFinish() {
                if (null == tvVerifyCode.get()) {
                    this.cancel();
                    return;
                }
                tvVerifyCode.get().setEnabled(true);
                tvVerifyCode.get().setText("重新获取");
                tvVerifyCode.get().setTextColor(context.getResources().getColor(R.color.appColor));
            }
        }.start();
    }

    public static void startTimer2(final Context context, final WeakReference<TextView> tvVerifyCode,
                                   final String defaultString,
                                   int max,
                                   int interval) {
        tvVerifyCode.get().setEnabled(false);

        // 由于CountDownTimer并不是准确计时，在onTick方法调用的时候，time会有1-10ms左右的误差，这会导致最后一秒不会调用onTick()
        // 因此，设置间隔的时候，默认减去了10ms，从而减去误差。
        // 经过以上的微调，最后一秒的显示时间会由于10ms延迟的积累，导致显示时间比1s长max*10ms的时间，其他时间的显示正常,总时间正常
        timer = new CountDownTimer(max * 1000, interval * 1000 - 10) {

            @Override
            public void onTick(long time) {
                // 第一次调用会有1-10ms的误差，因此需要+15ms，防止第一个数不显示，第二个数显示2s
                if (null == tvVerifyCode.get())
                    this.cancel();
                else {
                    tvVerifyCode.get().setText("" + ((time + 15) / 1000) + "s");
                    tvVerifyCode.get().setTextColor(context.getResources().getColor(R.color.app_white));
                    tvVerifyCode.get().setBackgroundResource(R.drawable.btn_send_gray_shape);
                }
            }

            @Override
            public void onFinish() {
                if (null == tvVerifyCode.get()) {
                    this.cancel();
                    return;
                }
                tvVerifyCode.get().setEnabled(true);
                tvVerifyCode.get().setText("获取验证码");
                tvVerifyCode.get().setTextColor(context.getResources().getColor(R.color.app_white));
                tvVerifyCode.get().setBackgroundResource(R.drawable.btn_send_shape);
            }
        }.start();
    }

    public static void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    public static void saveLoginInfo(String token, String uid) {
        FeihuZhiboApplication.getApplication().mDataManager.setApiKey(token);
        FeihuZhiboApplication.getApplication().mDataManager.setCurrentUserId(uid);
    }


    /**
     * @param verifyCode 验证码
     * @return 同上
     */
    public static boolean isVerifyCodeValid(String verifyCode) {
        return verifyCode.length() > 3;
    }

    /**
     * 将毫秒数转换成年月日
     *
     * @param millis
     * @return
     */
    public static String millsTotime(Long millis) {
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(millis));
        return date;
    }


    /**
     * 将毫秒数转换成月日时  03-03  10：00
     *
     * @param millis
     * @return
     */
    public static String millsToHour(Long millis) {
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new java.util.Date(millis));
        return date;
    }


    /**
     * @param password 用户输入密码
     * @return 有效则返回true, 无效则返回false
     */
    public static boolean isPasswordValid(String password) {
        return password.length() >= 6 && password.length() <= 15;
    }

    // 根据原图绘制圆形图片
    static public Bitmap createCircleImage(Bitmap source, int min) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        if (0 == min) {
            min = source.getHeight() > source.getWidth() ? source.getWidth() : source.getHeight();
        }
        Bitmap target = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
        // 创建画布
        Canvas canvas = new Canvas(target);
        // 绘圆
        canvas.drawCircle(min / 2, min / 2, min / 2, paint);
        // 设置交叉模式
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        // 绘制图片
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    // 字符串截断
    public static String getLimitString(String source, int length) {
        if (null != source && source.length() > length) {
//            int reallen = 0;
            return source.substring(0, length) + "...";
        }
        return source;
    }

    /**
     * 时间格式化
     */
    public static String formattedTime(long second) {
        String hs, ms, ss, formatTime;

        long h, m, s;
        h = second / 3600;
        m = (second % 3600) / 60;
        s = (second % 3600) % 60;
        if (h < 10) {
            hs = "0" + h;
        } else {
            hs = "" + h;
        }

        if (m < 10) {
            ms = "0" + m;
        } else {
            ms = "" + m;
        }

        if (s < 10) {
            ss = "0" + s;
        } else {
            ss = "" + s;
        }
        if (hs.equals("00")) {
            formatTime = ms + ":" + ss;
        } else {
            formatTime = hs + ":" + ms + ":" + ss;
        }

        return formatTime;
    }

    /**
     * 获取一段字符串的字符个数（包含中英文，一个中文算2个字符）
     */
    public static int getCharacterNum(final String content) {
        if (null == content || "".equals(content)) {
            return 0;
        } else {
            return (content.length() + getChineseNum(content));
        }
    }

    /**
     * 返回字符串里中文字或者全角字符的个数
     */
    public static int getChineseNum(String s) {
        int num = 0;
        char[] myChar = s.toCharArray();
        for (int i = 0; i < myChar.length; i++) {
            if ((char) (byte) myChar[i] != myChar[i]) {
                num++;
            }
        }
        return num;
    }

    /**
     * 获取当月的 天数
     */
    public static int getCurrentMonthDay() {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 获取当月的日期
     */
    public static int getCurrentDay() {

        Calendar a = Calendar.getInstance();
        int day = a.get(Calendar.DATE);
        return day;
    }

    /*
    * 获取网络类型
    */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }


    public static int dipToPx(Context context, int dip) {
        return (int) (dip * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static int dp2pxConvertInt(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    public static float sp2px(Context context, float spValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.<br>
     * <br>
     * Callers should check whether the path is local before assuming it
     * represents a local file.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     */
    @TargetApi(19)
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {

                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public static String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    public static String getTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        return format.format(new Date(time));
    }

    public static String getMHTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
        return format.format(new Date(time));
    }

    public static String getMonth(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd");
        return format.format(new Date(time));
    }

    public static String getHourAndMin(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(time));
    }

    public static String getChatTime(long timesamp) {
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        Date today = new Date(System.currentTimeMillis());
        Date otherDay = new Date(timesamp);
        int temp = Integer.parseInt(sdf.format(today)) - Integer.parseInt(sdf.format(otherDay));

        switch (temp) {
            case 0:
                result = "今天 " + getHourAndMin(timesamp);
                break;
            case 1:
                result = "昨天 " + getHourAndMin(timesamp);
                break;
            case 2:
                result = "前天 " + getHourAndMin(timesamp);
                break;

            default:
                result = temp + "天前 ";
//                result = getTime(timesamp);
                break;
        }

        return result;
    }

    public static void setScalse(View view) {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator animator_x = ObjectAnimator.ofFloat(view, "scaleX", 1.3f, 1.1f, 1.2f, 1.1f, 1f);
        ObjectAnimator animator_y = ObjectAnimator.ofFloat(view, "scaleY", 1.3f, 1.1f, 1.2f, 1.1f, 1f);
        set.play(animator_x).with(animator_y);
        set.setDuration(500);
        set.start();
    }

    private static Bitmap decodeResource(Resources resources, int id) {
        TypedValue value = new TypedValue();
        resources.openRawResource(id, value);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inTargetDensity = value.density;
        return BitmapFactory.decodeResource(resources, id, opts);
    }


    public static String toDecimal(double f) {
        DecimalFormat df = new DecimalFormat(".##");
        String st = df.format(f);
        if (f < 1) {
            return "0" + st;
        } else {
            return st;
        }
    }

    public static int dp2px(Context context, float dp) {
        return (int) (context.getResources().getDisplayMetrics().density * dp);
    }

    /**
     * 圆角显示图片
     *
     * @param context  一般为activtiy
     * @param view     图片显示类
     * @param url      图片url
     * @param defResId 默认图 id
     */
    public static void showPicWithUrl(Context context, ImageView view, String url, int defResId) {
        if (context == null || view == null) {
            return;
        }
        try {
            if (TextUtils.isEmpty(url)) {
                view.setImageResource(defResId);
            } else {
                Glide.with(context).load(url).apply(new RequestOptions().circleCrop().placeholder(defResId).diskCacheStrategy(DiskCacheStrategy.ALL)).into(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showPicWithUrlfade(Context context, ImageView view, String url, int defResId) {
        if (context == null || view == null) {
            return;
        }
        try {
            if (TextUtils.isEmpty(url)) {
                view.setImageResource(defResId);
            } else {
                Glide.with(context).load(url).apply(new RequestOptions().circleCrop().placeholder(defResId).diskCacheStrategy(DiskCacheStrategy.ALL)).transition(DrawableTransitionOptions.withCrossFade(1000)).into(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否是VIP
     *
     * @param userData
     * @return
     */
    public static boolean isVip(LoadUserDataBaseResponse.UserData userData) {
        /**
         * 1、vip是否大于0
         * 2、vip是否过期
         */
        if (userData.getVip() > 0) {
            if (!userData.isVipExpired()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static void showLevelWithUrl(Context context, ImageView view, int level) {
        if (context == null || view == null) {
            return;
        }
        Glide.with(context).load("https://img.feihutv.cn/Level/rank_" + level + ".png").apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)).into(view);
    }

    public static void showVipLevelWithUrl(Context context, ImageView view, int vip, boolean isVipExpired) {
        if (context == null || view == null) {
            return;
        }
        if (vip == 0) {
            view.setVisibility(View.GONE);
            return;
        } else {
            view.setVisibility(View.VISIBLE);
            String cosRootPath = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosVipIconRootPath();
            String url = "";
            if (isVipExpired) {
                url = cosRootPath + "icon_vip" + vip + "_una.png";
            } else {
                url = cosRootPath + "icon_vip" + vip + ".png";
            }
            Glide.with(context).load(url).into(view);
        }
    }

    public static void setFontBeauti(Context context, TextView textView) {
        Typeface fontFace1 = Typeface.createFromAsset(context.getAssets(), "fonts/sthupo.ttf");
        textView.setTextColor(context.getResources().getColor(R.color.beautiColor));
        textView.setShadowLayer(1, 0, 2, Color.parseColor("#000000"));
        textView.setTypeface(fontFace1);
    }

    public static void setFontNormal(Context context, TextView textView) {
        Typeface fontFace2 = Typeface.createFromAsset(context.getAssets(), "fonts/normal.ttf");
        textView.setTextColor(context.getResources().getColor(R.color.diver));
        textView.setShadowLayer(1, 0, 2, Color.parseColor("#00000000"));
        textView.setTypeface(fontFace2);
    }

    public static String hidePhoneMid(String phoneNum) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < phoneNum.length(); i++) {
            char c = phoneNum.charAt(i);
            if (i >= 3 && i <= 6) {
                sb.append('*');
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static boolean isWifiAvailable(Context ct) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                ct.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected() && networkInfo
                .getType() == ConnectivityManager.TYPE_WIFI);
    }

    //  检查是否是游客  在pc
    public static boolean checkGuestInPc(Activity activity) {
        if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), AppPreferencesHelper.PREF_KEY_USERID).startsWith("g")) {
            if (BuildConfig.isForceLoad.equals("1")) {
                CustomDialogUtils.showQZLoginDialog(activity, true);
            } else {
                CustomDialogUtils.showLoginDialog(activity, true);
            }
            return true;
        } else {
            return false;
        }
    }

    //  检查是否是游客  非pc
    public static boolean checkGuest(Activity activity) {
        if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), AppPreferencesHelper.PREF_KEY_USERID).startsWith("g")) {
            if (BuildConfig.isForceLoad.equals("1")) {
                CustomDialogUtils.showQZLoginDialog(activity, false);
            } else {
                CustomDialogUtils.showLoginDialog(activity, false);
            }
            return true;
        } else {
            return false;
        }
    }
}
