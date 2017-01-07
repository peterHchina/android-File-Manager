package com.example.managerfile.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.util.ByteArrayBuffer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;



public class ImageUtil {

	public static final String TAG = ImageUtil.class.getSimpleName();

	public static final long maxBitmapSize = 5 * 1024 * 1024;// 5M



	public static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.RGB_565;

	public static final Bitmap.CompressFormat BITMAP_COMPRESS_FORMAT = CompressFormat.PNG;

	

	public static Bitmap decodeByteArray(byte[] byteArray) {
		if (byteArray == null || byteArray.length == 0) {
			return null;
		}

//		MemoryUtil.trace();

		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inSampleSize = 1;
		opt.inJustDecodeBounds = true;
		opt.inPreferredConfig = BITMAP_CONFIG;
		BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, opt);
		opt.inSampleSize = computeSampleSizeByMemoryBudget(opt);
//		GlobalVars.Logd(TAG,
//				"Decode byte array BitmapFactory.Options.inSampleSize:"
//						+ opt.inSampleSize);
		opt.inJustDecodeBounds = false;
		opt.inInputShareable = true;
		opt.inPurgeable = true;
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
					byteArray.length, opt);
		} catch (OutOfMemoryError e) {

		}
		return bitmap;
	}

	public static Bitmap decodeFile(String pathName) {
		if (pathName == null || pathName.isEmpty()) {
			return null;
		}

//		MemoryUtil.trace();

		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inSampleSize = 1;
		opt.inJustDecodeBounds = true;
		opt.inPreferredConfig = BITMAP_CONFIG;
		BitmapFactory.decodeFile(pathName);
		opt.inSampleSize = computeSampleSizeByMemoryBudget(opt);
//		GlobalVars.Logd(TAG, "Decode file BitmapFactory.Options.inSampleSize:"
//				+ opt.inSampleSize);
		opt.inJustDecodeBounds = false;
		opt.inInputShareable = true;
		opt.inPurgeable = true;
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeFile(pathName, opt);
		} catch (OutOfMemoryError e) {
		
		}
		return bitmap;
	}

	public static void writeFile(Bitmap bitmap, String pathName) {
		if (bitmap == null || pathName == null || pathName.isEmpty()) {
			return;
		}
		try {
			File file = new File(pathName);
			if (!file.exists()) {
				CompressFormat format = BITMAP_COMPRESS_FORMAT;
				FileOutputStream fos = new FileOutputStream(file);
				bitmap.compress(format, 100, fos);
				fos.flush();
				fos.close();
			}
		} catch (Exception e) {
			
		}

	}

	/**
	 * @param opt
	 *            BitmapFactory.Options which is decoded by just decode bounds
	 * @return The best simple size
	 */
	public static int computeSampleSizeByMemoryBudget(BitmapFactory.Options opt) {
		int config = 4;
		if (opt.inPreferredConfig == Config.ARGB_8888) {
			config = 4;
		} else {
			config = 2;
		}
		long availableMemSize = MemoryUtil.getAvailableNativeMemorySize();
		int inSimpleSize = opt.inSampleSize;
		long bitmapSize = (opt.outWidth / inSimpleSize)
				* (opt.outHeight / inSimpleSize) * config;
		// Bitmap size is bigger than mem size we call gc first
		if (availableMemSize <= bitmapSize || bitmapSize >= maxBitmapSize) {
			MemoryUtil.trace();
		
			MemoryUtil.gc();
		
			MemoryUtil.trace();
			availableMemSize = MemoryUtil.getAvailableNativeMemorySize();
		}
		// Bitmap size is larger than MAX_BITMAP_SZIE we make simple size
		// smaller anywhere
		if (bitmapSize >= maxBitmapSize) {
			inSimpleSize <<= 1;
			bitmapSize = (opt.outWidth / inSimpleSize)
					* (opt.outHeight / inSimpleSize) * config;
		}
		// Calculate a best simple size
		while (availableMemSize < bitmapSize) {
			if (inSimpleSize <= 8) {
				inSimpleSize <<= 1;
			} else {
				inSimpleSize <<= 2;
				break;
			}
			bitmapSize = (opt.outWidth / inSimpleSize)
					* (opt.outHeight / inSimpleSize) * config;
		}
		return inSimpleSize;
	}


}
