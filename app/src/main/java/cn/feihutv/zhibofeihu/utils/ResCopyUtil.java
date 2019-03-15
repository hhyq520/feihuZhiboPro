package cn.feihutv.zhibofeihu.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;


public class ResCopyUtil {
    public static boolean checkFileExist(String filepath) {
        try {
            File f = new File(filepath);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
        return true;
    }


    public static boolean copyAssetToSD(final AssetManager assetManager, final String fromAssetPath, final String toPath) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String sdpath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + toPath;
            return copyAssetFolder(assetManager, fromAssetPath, sdpath);
        }

        return false;
    }

    public static boolean copyAssetFolder(AssetManager assetManager, String fromAssetPath, String toPath) {
        try {

            String[] files = assetManager.list(fromAssetPath);
            new File(toPath).mkdirs();
            boolean res = true;
            for (String file : files)
                if (file.contains("."))
                    res &= copyAsset(assetManager, fromAssetPath + "/" + file, toPath + "/" + file);
                else
                    res &= copyAssetFolder(assetManager, fromAssetPath + "/" + file, toPath + "/" + file);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean copyAsset(AssetManager assetManager, String fromAssetPath, String toPath) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(fromAssetPath);
            new File(toPath).createNewFile();
            out = new FileOutputStream(toPath);
            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    public static String readJsonFromFile(String filepath) {
        File f = new File(filepath);
        String string = null;
        if (f.isFile()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(f);
                string = readStringFromInStream(fis);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return string;
    }

    /***
     * 从流中读取字符串
     * @param in 输入流
     * @return 读取到的字符串
     */
    private static String readStringFromInStream(InputStream in) {
        StringBuilder ret = null;
        try {
            byte b[] = new byte[8096];
            int readRet = -1;
            ret = new StringBuilder();
            while ((readRet = in.read(b)) > 0) {
                ret.append(new String(b, 0, readRet));
            }
        } catch (IOException e) {
            ret = null;
        }
        return ret != null ? ret.toString() : null;
    }

    /**
     * 从asset路径下读取对应文件转String输出
     *
     * @param mContext
     * @return
     */
    public static String getJson(Context mContext, String fileName) {
        // TODO Auto-generated method stub
        StringBuilder sb = new StringBuilder();
        AssetManager am = mContext.getAssets();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    am.open(fileName)));
            String next = "";
            while (null != (next = br.readLine())) {
                sb.append(next);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            sb.delete(0, sb.length());
        }
        return sb.toString().trim();
    }
}
