package cn.feihutv.zhibofeihu.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static cn.feihutv.zhibofeihu.data.network.socket.xsocket.utils.ByteUtils.bytesToHexString;

public class FileUtils {


	private static String APP_DIR_NAME = "fh";
	private static String FILE_DIR_NAME = "mvfiles";
	private static String MV_DOWN_FILE_DIR_NAME = "downfile";
	private static String mRootDir;
	private static String mAppRootDir;
	private static String mFileDir;
	private static String mMvDownFileDir;

	public static final String TYPE_JPG = "jpg";
	public static final String TYPE_GIF = "gif";
	public static final String TYPE_PNG = "png";
	public static final String TYPE_BMP = "bmp";
	public static final String TYPE_UNKNOWN = "unknown";


	public static void init() {
		mRootDir = getRootPath();
		if (mRootDir != null && !"".equals(mRootDir)) {
			mAppRootDir = mRootDir + "/" + APP_DIR_NAME;
			mFileDir = mAppRootDir + "/" + FILE_DIR_NAME;
			mMvDownFileDir = mAppRootDir + "/" + MV_DOWN_FILE_DIR_NAME;
			File appDir = new File(mAppRootDir);
			if (!appDir.exists()) {
				appDir.mkdirs();
			}
			File fileDir = new File(mAppRootDir + "/" + FILE_DIR_NAME);
			if (!fileDir.exists()) {
				fileDir.mkdirs();
			}

			File mvfileDir = new File(mAppRootDir + "/" + MV_DOWN_FILE_DIR_NAME);
			if (!mvfileDir.exists()) {
				mvfileDir.mkdirs();
			}


		} else {
			mRootDir = "";
			mAppRootDir = "";
			mFileDir = "";
		}
	}


	public static String getMvDownFileDir() {
		init();
		return mMvDownFileDir;
	}



	public static String getMvFileDir() {
		init();
		return mFileDir;
	}


	public static String getRootPath() {
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			return Environment.getExternalStorageDirectory().getAbsolutePath(); // filePath:  /sdcard/
		} else {
			return Environment.getDataDirectory().getAbsolutePath() + "/data"; // filePath:  /data/data/
		}
	}



	public static String SDPATH = Environment.getExternalStorageDirectory()
			+ "/Photo_LJ/";

	public static void saveBitmap(Bitmap bm, String picName) {
		try {
			if (!isFileExist("")) {
				File tempf = createSDDir("");
			}
			File f = new File(SDPATH, picName + ".JPEG");
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static File createSDDir(String dirName) throws IOException {
		File dir = new File(SDPATH + dirName);
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			System.out.println("createSDDir:" + dir.getAbsolutePath());
			System.out.println("createSDDir:" + dir.mkdir());
		}
		return dir;
	}

	public static boolean isFileExist(String fileName) {
		File file = new File(SDPATH + fileName);
		file.isFile();
		return file.exists();
	}
	
	public static void delFile(String fileName){
		File file = new File(SDPATH + fileName);
		if(file.isFile()){
			file.delete();
        }
		file.exists();

	}


	public static void delete(String filePath) {
		File file = new File(filePath);
		if(file.exists() && file.isFile()) {
			file.delete();
		}

	}

	public static void deleteDir() {
		File dir = new File(SDPATH);
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return;
		
		for (File file : dir.listFiles()) {
			if (file.isFile())
				file.delete(); 
			else if (file.isDirectory())
				deleteDir(); 
		}
		dir.delete();
	}

	public static boolean fileIsExists(String path) {
		try {
			File f = new File(path);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {

			return false;
		}
		return true;
	}

//	/*
//	* 从Assets中读取图片
//	*/
	public static Bitmap getImageFromAssetsFile(Context context, String fileName)
	{
		Bitmap image = null;
		AssetManager am = context.getResources().getAssets();
		try
		{
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return image;

	}

	public static Bitmap readBitmap565FromFile(String filename) {
		Bitmap bitmap = null;
		File file = new File(filename);
		if (file.exists()) {
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			options.inPurgeable = true;
			options.inInputShareable = true;
			try {
				bitmap = BitmapFactory.decodeFile(filename, options);
				if (bitmap == null) {
					file.delete();
				}
			} catch (OutOfMemoryError e) {
				e.printStackTrace();

				if (bitmap != null && !bitmap.isRecycled()) {
					bitmap.recycle();
					bitmap = null;
				}
				System.gc();
			}
		}

		return bitmap;
	}


	public static String getFileMD5(File updateFile) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			AppLogger.e("file", "Exception while getting digest"+e);
			return null;
		}

		InputStream is;
		try {
			is = new FileInputStream(updateFile);
		} catch (FileNotFoundException e) {
			AppLogger.e("file","Exception while getting FileInputStream"+e);
			return null;
		}

		byte[] buffer = new byte[8192];
		int read;
		try {
			while ((read = is.read(buffer)) > 0) {
				digest.update(buffer, 0, read);
			}
			byte[] md5sum = digest.digest();
			BigInteger bigInt = new BigInteger(1, md5sum);
			String output = bigInt.toString(16);
			// Fill to 32 chars
			output = String.format("%32s", output).replace(' ', '0');
			return output;
		} catch (IOException e) {
			throw new RuntimeException("Unable to process file for MD5", e);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				AppLogger.e("file", "Exception on closing MD5 input stream"+e);
			}
		}
	}

	/**
	 * 获取指定文件大小
	 * @return
	 * @throws Exception 　　
	 */
	public static long getFileSize(File file) throws Exception {
		long size = 0;
		if (file.exists()) {
			FileInputStream fis = null;
			fis = new FileInputStream(file);
			size = fis.available();
		} else {
			file.createNewFile();
		}
		return size;
	}


	//读文件
	public String readSDFile(String filePath,String fileName) throws IOException {

		StringBuffer sb = new StringBuffer();
		try {
			File file = new File(filePath,fileName);
			if(!file.exists()){
				return sb.toString();
			}
			BufferedReader br = new BufferedReader(new FileReader(file));
			String readLine = "";
			while ((readLine = br.readLine()) != null) {
				sb.append(readLine);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	//写文件
	public void writeSDFile(String fileName, String write_str) throws IOException{
		File file = new File(fileName);
		FileOutputStream fos = new FileOutputStream(file);
		byte [] bytes = write_str.getBytes();
		fos.write(bytes);
		fos.close();
	}



	/**
	 * 根据文件流判断图片类型
	 * @param fis
	 * @return jpg/png/gif/bmp
	 */
	public static String getPicType(FileInputStream fis) {
		//读取文件的前几个字节来判断图片格式
		byte[] b = new byte[4];
		try {
			fis.read(b, 0, b.length);
			String type = bytesToHexString(b).toUpperCase();
			if (type.contains("FFD8FF")) {
				return TYPE_JPG;
			} else if (type.contains("89504E47")) {
				return TYPE_PNG;
			} else if (type.contains("47494638")) {
				return TYPE_GIF;
			} else if (type.contains("424D")) {
				return TYPE_BMP;
			}else{
				return TYPE_UNKNOWN;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(fis != null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}
