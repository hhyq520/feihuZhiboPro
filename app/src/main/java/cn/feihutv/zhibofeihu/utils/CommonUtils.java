/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package cn.feihutv.zhibofeihu.utils;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.feihutv.zhibofeihu.ui.widget.dialog.ProgressLoadingDialog;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public final class CommonUtils {


    private CommonUtils() {
    }

    public static ProgressLoadingDialog showLoadingDialog(Context context, String message) {
        ProgressLoadingDialog progressDialog = new ProgressLoadingDialog(context);
        progressDialog.show();
        if (TextUtils.isEmpty(message)) {
//            progressDialog.setMessage(context.getString(R.string.loding));
            progressDialog.setMessage("正在加载中...");
        } else {
            progressDialog.setMessage(message);
        }
        return progressDialog;
    }


    @SuppressLint("all")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static boolean isEmailValid(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * 验证手机号码
     *
     * @param phoneNumber 手机号码
     * @return boolean
     */
    public static boolean checkPhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile("^1[0-9]{10}$");
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    /**
     * 验证用户名
     *
     * @param username 用户名
     * @return boolean
     */
    public static boolean checkUsername(String username) {
        String regex = "([a-zA-Z0-9]{6,12})";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(username);
        return m.matches();
    }


    public static String loadJSONFromAsset(Context context, String jsonFileName)
            throws IOException {

        AssetManager manager = context.getAssets();
        InputStream is = manager.open(jsonFileName);

        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();

        return new String(buffer, "UTF-8");
    }



    /**
     * check if sdcard exist
     *
     * @return
     */
    public static boolean isSdcardExist() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }


    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s) {
        if (TextUtils.isEmpty(s) || s.equals("")) {
            return "";
        }
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

//    static String getString(Context context, int resId) {
//        return context.getResources().getString(resId);
//    }



    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    public static final String MIME_TEXT = "text/plain";
    public static final String MIME_IMAGE = "image/*";
    public static final String MIME_AUDIO = "audio/*";
    public static final String MIME_VIDEO = "video/*";

    /**
     * 调用浏览器
     *
     * @param context 上下文
     * @param uri     访问地址
     */
    public static void callBrowser(Context context, String uri) {
        Intent intent = new Intent();
        intent.setData(Uri.parse(uri));
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 调用发送短信
     *
     * @param context 上下文
     * @param number  电话号码
     * @param content 短信内容
     */
    public static void callMessage(Context context, String number, String content) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SENDTO);
        intent.putExtra("sms_body", content);
        intent.setData(Uri.parse(String.format("smsto:%s", number)));
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 调用拨打电话
     *
     * @param context 上下文
     * @param number  电话号码
     */
    public static void callPhone(Context context, String number) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(String.format("tel:%s", number)));
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 打开文件
     *
     * @param context 上下文
     * @param path    文件路径
     * @param mime    文件类型
     */
    public static void openFile(Context context, String path, String mime) {
        Intent intent = new Intent();
        intent.setDataAndType(Uri.fromFile(new File(path)), mime);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }




    //保留两位小数
    public static String get2MaximumFractionDigits(Double countValue) {
        NumberFormat ddf1 = NumberFormat.getNumberInstance();
        ddf1.setMaximumFractionDigits(2);
        return ddf1.format(countValue);
    }

    public static String getCoinText(String val){
        if(TextUtils.isEmpty(val)){
            return "";
        }
        Double deVal=Double.parseDouble(val);
        if(deVal>9999){
            deVal=deVal/10000;
            val= getDoubleMax2(deVal)+"万";
        }
        return val;
    }



    public static String getWCoinText(String val){
        if(TextUtils.isEmpty(val)){
            return "";
        }
        Double deVal=Double.parseDouble(val);
        if(deVal>9999){
            deVal=deVal/10000;
            val= getDoubleMax2(deVal)+"W";
        }
        return val;
    }




    public static String getDoubleMax2(Double countValue) {
        if (countValue == 0.0) {
            return "0.0";
        } else {
            java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");

            String cv=df.format(countValue);
            if(cv.substring(cv.lastIndexOf(".")+1,cv.length()).equals("00")){
                String sss=cv.substring(0,cv.lastIndexOf("."));
                cv=sss;
            }
            return cv;
        }

    }

}
