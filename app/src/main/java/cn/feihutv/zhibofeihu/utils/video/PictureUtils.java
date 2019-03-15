package cn.feihutv.zhibofeihu.utils.video;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class PictureUtils {
    public static final String POSTFIX = ".jpeg";

    public static String saveImageToSD(Bitmap bmp, String dirPath) {
        if (bmp == null) {
            return "";
        }
        File appDir = new File(dirPath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file.getAbsolutePath();
    }




    public static String saveImageToSD(int w,Bitmap bmp, String dirPath) {
        if (bmp == null) {
            return "";
        }
        bmp= scaleImage(w,bmp);
        File appDir = new File(dirPath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file.getAbsolutePath();
    }


    private static Bitmap scaleImage(int w,Bitmap bm) {
        if (bm == null) {
            return null;
        }
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = w * 1.0f / width;
//        float scaleHeight =extractH*1.0f / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleWidth);
        Bitmap newBm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                true);
        if (!bm.isRecycled()) {
            bm.recycle();
            bm = null;
        }
        return newBm;
    }




    public static String saveImageToSDForEdit(Bitmap bmp, String dirPath, String fileName) {
        if (bmp == null) {
            return "";
        }
        File appDir = new File(dirPath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    public static void deleteFile(File f) {
        if (f.isDirectory()) {
            File[] files = f.listFiles();
            if (files != null && files.length > 0) {
                for (int i = 0; i < files.length; ++i) {
                    deleteFile(files[i]);
                }
            }
        }
        f.delete();
    }

    /**
     * 默认目标图的宽度为 800 ，当 reqWidth reqHeight 都为 0 时，使用默认宽度；
     * 有一个为 0 时，使用不为 0 的计算缩放比例，和采样系数；
     * 当都不为 0 时，先计算，在选择合适的值。
     * 当原图尺寸，小于目标尺寸时，不进行缩放，只进行压缩；否则，先缩小尺寸，在压缩。
     *
     * @param srcPath   原图路径
     * @param dstPath   目标图路径
     * @param reqWidth  目标宽度
     * @param reqHeight 目标高度
     * @param quality   生成图片的质量 0-100
     * @return false 压缩失败，true 压缩成功
     */
    public static boolean compressImage(String srcPath, String dstPath, int reqWidth, int reqHeight, int quality) {
        float defaultWidth = 800.f;

        // 获取 bitmap 信息，但不加载 bitmap
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcPath, options);
        int width = options.outWidth;
        int height = options.outHeight;

        if (width == 0 || height == 0) {
            return false;
        }

        // 计算采样比例
        int sample = 1;
        if (reqWidth != 0 && reqHeight != 0) {
            sample = Math.min(width / reqWidth, height / reqHeight);
        } else if (reqWidth != 0) {
            sample = width / reqWidth;
        } else if (reqHeight != 0) {
            sample = height / reqHeight;
        } else {
            sample = width / 800;
        }

        // 加载 bitmap
        options.inJustDecodeBounds = false;
        options.inSampleSize = sample;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, options);

        width = options.outWidth;
        height = options.outHeight;

        // 计算缩放比例
        float scale;
        if (reqWidth != 0 && reqHeight != 0) {
            scale = Math.min((float) reqWidth / width, (float) reqHeight / height);
        } else if (reqWidth != 0) {
            scale = (float) reqWidth / width;
        } else if (reqHeight != 0) {
            scale = (float) reqHeight / height;
        } else {
            scale = defaultWidth / width;
        }

        // 图片缩放
        if (scale < 1.0f) {
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        }

        // 保存到目的地址
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        quality = quality < 0 ? 0 : quality;
        quality = quality > 100 ? 100 : quality;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
        try {
            FileOutputStream fos = new FileOutputStream(new File(dstPath));
            fos.write(outputStream.toByteArray());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
