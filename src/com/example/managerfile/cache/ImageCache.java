package com.example.managerfile.cache;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.example.managerfile.cache.wrap.BitmapWrap;
import com.example.managerfile.cache.wrap.ImageInfo;
import com.example.managerfile.cache.wrap.ImageInfo.ImageType;
import com.example.managerfile.util.FileUtil;
import com.example.managerfile.util.ImageUtil;



import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;



public class ImageCache implements MapCache<ImageInfo, Bitmap> {
	private String TAG = ImageCache.class.getSimpleName();

	private static StringBuffer buffer = new StringBuffer();

	private static String filePath;

	private static String secondaryFilePath;

	private String choosedPath;

	private ConcurrentHashMap<ImageInfo, BitmapWrap> memoryCache;

	private final Map<ImageType, String> folderMap;

	private static final int MAX_CAPACITY = 10;// 一级缓存的最大空间
	private static final long DELAY_BEFORE_PURGE = 10 * 1000;// 定时清理缓存
	private static ImageCache Instance;
	
	private HashMap<ImageInfo, Bitmap> mFirstLevelCache;

	public ImageCache() {
		filePath = "/sdcard/.FileManager/cache/image";
		secondaryFilePath = "/data/data/com.example.filemanger/cache/image";
		memoryCache = new ConcurrentHashMap<ImageInfo, BitmapWrap>(MAX_CAPACITY / 2);
		// 0.75是加载因子为经验值，true则表示按照最近访问量的高低排序，false则表示按照插入顺序排序
		mFirstLevelCache = new LinkedHashMap<ImageInfo, Bitmap>(MAX_CAPACITY / 2, 0.75f, true) {
			private static final long serialVersionUID = 1L;

			protected boolean removeEldestEntry(Entry<ImageInfo, Bitmap> eldest) {
				if (size() > MAX_CAPACITY) {// 当超过一级缓存阈值的时候，将老的值从一级缓存搬到二级缓存
					memoryCache.put(eldest.getKey(), new BitmapWrap(eldest.getValue()));
					return true;
				}
				return false;
			};
		};
		folderMap = new HashMap<ImageType, String>();
		checkPath();
	}

	private void checkPath() {
		if (Environment.getExternalStorageState().equals("mounted")) {
			choosedPath = filePath;
		} else {
			choosedPath = secondaryFilePath;
		}
		for (ImageType t : ImageType.values()) {
			buffer.delete(0, buffer.length());
			buffer.append(choosedPath).append("/").append(t.toString());
			folderMap.put(t, buffer.toString());
		}
		File file;
		for (String s : folderMap.values()) {
			file = new File(s);
			if (!file.exists()) {
				file.mkdirs();
			}
		}
	}

	public synchronized void clear() {
		flush();
		mFirstLevelCache.clear();
		memoryCache.clear();
	}

	public synchronized String getFolder(ImageType type) {
		return folderMap.get(type);
	}

	public synchronized String getPath(ImageInfo imageInfo) {
		buffer.delete(0, buffer.length());
		buffer.append(getFolder(imageInfo.getImageType())).append("/").append(imageInfo.getImageName());
		return buffer.toString();
	}

	/**
	 * @descriptions:Flush memory cache to file
	 */
	public synchronized void flush() {
		Set<Entry<ImageInfo, BitmapWrap>> set = memoryCache.entrySet();
		for (Entry<ImageInfo, BitmapWrap> entry : set) {
			ImageInfo key = entry.getKey();
			BitmapWrap wrap = entry.getValue();
			Bitmap bitmap = wrap.getWrap();
			if (bitmap != null) {
				write(key, bitmap);
			}
		}
	}

	public synchronized boolean reclaim(ReclaimLevel reclaimLevel) {
		// int size = 0;
		// if (reclaimLevel == ReclaimLevel.WEIGHT)
		// {
		// clear();
		// }
		// if (reclaimLevel == ReclaimLevel.MODERATE)
		// {
		// size = memoryCache.size() / 2;
		// }
		// else if (reclaimLevel == ReclaimLevel.LIGHT)
		// {
		// size = memoryCache.size() / 4;
		// }
		// List<Entry<ImageInfo, BitmapWrap>> entries = new
		// ArrayList<Map.Entry<ImageInfo, BitmapWrap>>();
		// for (Entry<ImageInfo, BitmapWrap> entry : memoryCache.entrySet())
		// {
		// entries.add(entry);
		// }
		// Collections.sort(entries, new WrapHitComparator());
		// for (int i = 0; i < size; i++)
		// {
		// remove(entries.get(i).getKey());
		// }
		// entries.clear();
		mFirstLevelCache.clear();
		memoryCache.clear();
		return true;
	}

	public synchronized boolean containsKey(ImageInfo paramK) {
		return memoryCache.containsKey(paramK);
	}

	public synchronized Bitmap getFromCache(ImageInfo paramK) {
//		BitmapWrap wrap = memoryCache.get(paramK);
//		Bitmap bitmap = null;
//		if (wrap != null) {
//			bitmap = wrap.getWrap();
//			if (bitmap != null && !bitmap.isRecycled()) {
//				wrap.hit();
//			} else {
//				bitmap = null;
//			}
//
//		}
		Bitmap bitmap =getBitmapFromCache(paramK);
		return bitmap;
	}

	public synchronized Bitmap get(ImageInfo paramK) {
//		BitmapWrap wrap = memoryCache.get(paramK);
		Bitmap bitmap;
//		if (wrap != null) {
//			bitmap = wrap.getWrap();
//			if (bitmap != null && !bitmap.isRecycled()) {
//				wrap.hit();
//				Log.v("GetBitmap", "GetBitmap1");
//			} else {
//				bitmap = read(paramK);
//				wrap.setWrap(bitmap);
//				memoryCache.put(paramK, wrap);
//				Log.v("GetBitmap", "GetBitmap2");
//			}
//			Log.v("GetBitmap", "GetBitmap3");
//		} else {
//			bitmap = read(paramK);
//			memoryCache.put(paramK, new BitmapWrap(bitmap));
//			Log.v("GetBitmap", "GetBitmap4");
//		}
		bitmap =getBitmapFromCache(paramK);
		if(bitmap==null)
		{
			bitmap = read(paramK);
			if(bitmap!=null)
			{
				addImage2Cache(paramK,bitmap);
			}
		}
		return bitmap;
	}

	public synchronized void put(ImageInfo paramK, Bitmap paramV) {
		addImage2Cache(paramK,paramV);
		write(paramK, paramV);
//		if (memoryCache.containsKey(paramK)) {
//			BitmapWrap wrap = memoryCache.get(paramK);
//			wrap.hit();
//			wrap.setWrap(paramV);
//			memoryCache.put(paramK, wrap);
//			write(paramK, paramV);
//		} else {
//			memoryCache.put(paramK, new BitmapWrap(paramV));
//			write(paramK, paramV);
//		}
	}

	public synchronized void remove(ImageInfo paramK) {
		memoryCache.remove(paramK);
	}

	/**
	 * @descriptions: Read cache from file cache, If exists, It will return the
	 *                cached bitmap and put the bitmap to memory cache
	 * @param paramK
	 * @return If exists it will reutrn the bitmap, Otherwise it return null
	 */
	public synchronized Bitmap read(ImageInfo paramK) {
		buffer.delete(0, buffer.length());
		buffer.append(folderMap.get(paramK.getImageType()));
		buffer.append("/");
		buffer.append(paramK.getImageName());
		return ImageUtil.decodeFile(buffer.toString());
	}
	
	public synchronized String getImageUrl(ImageInfo paramK) {
		buffer.delete(0, buffer.length());
		buffer.append(folderMap.get(paramK.getImageType()));
		buffer.append("/");
		buffer.append(paramK.getImageName());
		return buffer.toString();
	}

	public synchronized void write(ImageInfo paramK, Bitmap paramV) {
		buffer.delete(0, buffer.length());
		buffer.append(folderMap.get(paramK.getImageType()));
		buffer.append("/");
		buffer.append(paramK.getImageName());
		ImageUtil.writeFile(paramV, buffer.toString());
	}

	public synchronized String getimagename(ImageInfo paramK) {
		buffer.delete(0, buffer.length());
		buffer.append(folderMap.get(paramK.getImageType()));
		buffer.append("/");
		buffer.append(paramK.getImageName());
		return (buffer.toString());
	}

	public synchronized void write(String paramK, Bitmap paramV) {
		// buffer.delete(0, buffer.length());
		// buffer.append(folderMap.get(paramK.getImageType()));
		// buffer.append("/");
		// buffer.append(paramK.getImageName());
		ImageUtil.writeFile(paramV, paramK);
	}

	@Override
	public synchronized void destroy() {
		Collection<String> collection = folderMap.values();
		for (String path : collection) {
			FileUtil.deleteDirectory(path, false);
		}
		mFirstLevelCache.clear();
		memoryCache.clear();
	}

//	// 定时清理缓存
//	private Runnable mClearCache = new Runnable() {
//		@Override
//		public void run() {
//			
//			clear2();
//		}
//	};

//	private Handler mPurgeHandler = new Handler();
//
//	// 重置缓存清理的timer
//	public void resetPurgeTimer() {
//		mPurgeHandler.removeCallbacks(mClearCache);
//		mPurgeHandler.postDelayed(mClearCache, DELAY_BEFORE_PURGE);
//	}
//
//	/**
//	 * 清理缓存
//	 */
//	private void clear2() {
//		mFirstLevelCache.clear();
//		memoryCache.clear();
//	}
	
	/**
	 * 返回缓存，如果没有则返回null
	 * 
	 * @param url
	 * @return
	 */
	public Bitmap getBitmapFromCache(ImageInfo imageInfo) {
		Bitmap bitmap = null;
		bitmap = getFromFirstLevelCache(imageInfo);// 从一级缓存中拿
		if (bitmap != null) {
			return bitmap;
		}
		bitmap = getFromSecondLevelCache(imageInfo);// 从二级缓存中拿
		return bitmap;
	}

	/**
	 * 从二级缓存中拿
	 * 
	 * @param url
	 * @return
	 */
	private Bitmap getFromSecondLevelCache(ImageInfo imageInfo) {
		Bitmap bitmap = null;
		BitmapWrap wrap= memoryCache.get(imageInfo);
		if (wrap != null) {
			bitmap = wrap.getWrap();
			if (bitmap == null) {// 由于内存吃紧，软引用已经被gc回收了
				memoryCache.remove(imageInfo);
			}
		}
		return bitmap;
	}

	/**
	 * 从一级缓存中拿
	 * 
	 * @param url
	 * @return
	 */
	private Bitmap getFromFirstLevelCache(ImageInfo imageInfo) {
		Bitmap bitmap = null;
		synchronized (mFirstLevelCache) {
			bitmap = mFirstLevelCache.get(imageInfo);
			if (bitmap != null) {// 将最近访问的元素放到链的头部，提高下一次访问该元素的检索速度（LRU算法）
				mFirstLevelCache.remove(imageInfo);
				mFirstLevelCache.put(imageInfo, bitmap);
			}
		}
		return bitmap;
	}
	
	/**
	 * 放入缓存
	 * 
	 * @param url
	 * @param value
	 */
	public void addImage2Cache(ImageInfo imageInfo, Bitmap value) {
		if (value == null || imageInfo == null) {
			return;
		}
		synchronized (mFirstLevelCache) {
			mFirstLevelCache.put(imageInfo, value);
		}
	}

	public static ImageCache getInstance()
	{
		if(Instance==null)
		{
			Instance=new ImageCache();
		}
		return Instance;
	}
}
