package cn.feihutv.zhibofeihu.utils;

import android.Manifest;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.PictureFileUtils;

import cn.feihutv.zhibofeihu.R;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @fileName PhotoUtils.java
 * @package com.immomo.momo.android.util
 * @description 图片工具类
 * @author 任东卫
 * @email 86930007@qq.com
 * @version 1.0
 */
public class PhotoUtils {
	/**
	 * 从文件中获取图片
	 *
	 * @param path
	 *            图片的路径
	 * @return
	 */
	public static Bitmap getBitmapFromFile(String path) {
		return BitmapFactory.decodeFile(path);
	}
	/**
	 * 获取图片的长度和宽度
	 *
	 * @param bitmap
	 *            图片bitmap对象
	 * @return
	 */
	public static Bundle getBitmapWidthAndHeight(Bitmap bitmap) {
		Bundle bundle = null;
		if (bitmap != null) {
			bundle = new Bundle();
			bundle.putInt("width", bitmap.getWidth());
			bundle.putInt("height", bitmap.getHeight());
			return bundle;
		}
		return null;
	}

	/**
	 * 判断图片高度和宽度是否过大
	 *
	 * @param bitmap
	 *            图片bitmap对象
	 * @return
	 */
	public static boolean bitmapIsLarge(Bitmap bitmap) {
		final int MAX_WIDTH = 60;
		final int MAX_HEIGHT = 60;
		Bundle bundle = getBitmapWidthAndHeight(bitmap);
		if (bundle != null) {
			int width = bundle.getInt("width");
			int height = bundle.getInt("height");
			if (width > MAX_WIDTH && height > MAX_HEIGHT) {
				return true;
			}
		}
		return false;
	}

	public static ImageSize getThumbnailDisplaySize(float srcWidth, float srcHeight, float dstMaxWH, float dstMinWH) {
		if (srcWidth <= 0 || srcHeight <= 0) { // bounds check
			return new ImageSize((int) dstMinWH, (int) dstMinWH);
		}

		float shorter;
		float longer;
		boolean widthIsShorter;

		//store
		if (srcHeight < srcWidth) {
			shorter = srcHeight;
			longer = srcWidth;
			widthIsShorter = false;
		} else {
			shorter = srcWidth;
			longer = srcHeight;
			widthIsShorter = true;
		}

		if (shorter < dstMinWH) {
			float scale = dstMinWH / shorter;
			shorter = dstMinWH;
			if (longer * scale > dstMaxWH) {
				longer = dstMaxWH;
			} else {
				longer *= scale;
			}
		} else if (longer > dstMaxWH) {
			float scale = dstMaxWH / longer;
			longer = dstMaxWH;
			if (shorter * scale < dstMinWH) {
				shorter = dstMinWH;
			} else {
				shorter *= scale;
			}
		}

		//restore
		if (widthIsShorter) {
			srcWidth = shorter;
			srcHeight = longer;
		} else {
			srcWidth = longer;
			srcHeight = shorter;
		}

		return new ImageSize((int) srcWidth, (int) srcHeight);
	}
	public static class ImageSize {
		public int width = 0;
		public int height = 0;

		public ImageSize(int width, int height) {
			this.width = width;
			this.height = height;
		}
	}




	public static void clearImages(@NonNull final Activity activity) {
// 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
		RxPermissions permissions = new RxPermissions(activity);
		permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {
			@Override
			public void onSubscribe(Disposable d) {
			}

			@Override
			public void onNext(Boolean aBoolean) {
				if (aBoolean) {
					PictureFileUtils.deleteCacheDirFile(activity.getApplicationContext());
				} else {
					Toast.makeText(activity,
							activity.getApplicationContext().
									getString(R.string.picture_jurisdiction),
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onError(Throwable e) {
			}

			@Override
			public void onComplete() {
			}
		});
	}


}
