package com.example.managerfile.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.channels.Channel;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.regex.Pattern;

import org.apache.http.util.ByteArrayBuffer;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

public class FileUtil
{
	public enum FileType
	{
		voice, temp, file
	}

	static final int ERROR = -1;

	public static final String TAG = FileUtil.class.getSimpleName();
	public static final String SHARED_PREFS_NAME = "com.FileManger_preferences";
	public static final String SETTING_HIDE_FILE = "com.FileManger.HideFile";
	public static final String SETTING_SWITCH_STAUTE = "com.FileManger.Switch";
	public static FileType mType;
	public static int length;
	public static int number;
	private long fileSzie = 0;
	private int fileNumber = 0;
	private int dirNumber = 0;
	public static HashSet<String> sDocMimeTypesSet = new HashSet<String>() {
		{
			add("text/plain");
			add("application/pdf");
			add("application/msword");
			add("application/vnd.ms-excel");
			add("application/vnd.ms-powerpoint");
		}
	};
	public static HashSet<String> sZipFileMimeType = new HashSet<String>() {
		{
			add("application/zip");
			add("application/rar");
			add("application/tar");
			add("application/bz2");
			add("application/7z");
		}

	};
	public static HashSet<String> sThemeFileMimeType = new HashSet<String>() {
		{
			add("mtz");
			add("txj");
			add("iuz");
			add("itz");
			add("ttf");
		}

	};
	public static boolean isSDCardReady()
	{
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	/*
	 * @return extension including "." and extension is lowercase
	 */
	public static String getExtensionWithDot(String name)
	{
		if (name == null || name.length() <= 0) {
			return null;
		}

		name = name.toLowerCase();
		int dot = name.lastIndexOf(".");
		String tmp = null;
		String ret = null;
		if (dot >= 0) {
			ret = name.substring(dot);
			tmp = name.substring(0, dot);
			if (tmp != null && tmp.length() > 0) {
				dot = tmp.lastIndexOf(".");
				if (dot >= 0) {
					tmp = tmp.substring(dot);
					if (tmp != null && tmp.length() > 0 && tmp.equalsIgnoreCase(".tar")) {
						ret = name.substring(dot);
					}
				}
			}
		}
		return ret;
	}

	public static String getExtensionWithDot1(String name)
	{
		if (name == null || name.length() <= 0) {
			return null;
		}

		int dot = name.lastIndexOf(".");
		String tmp = null;
		String ret = null;
		if (dot >= 0) {
			ret = name.substring(dot);
			tmp = name.substring(0, dot);
			if (tmp != null && tmp.length() > 0) {
				dot = tmp.lastIndexOf(".");
				if (dot >= 0) {
					tmp = tmp.substring(dot);
					if (tmp != null && tmp.length() > 0 && tmp.equalsIgnoreCase(".tar")) {
						ret = name.substring(dot);
					}
				}
			}
		}
		return ret;
	}

	public static String getFileSizes(File f) throws Exception
	{// 取得文件大小
		long s = 0;
		if (f.exists()) {
			FileInputStream fis = null;
			fis = new FileInputStream(f);
			s = fis.available();
		}
		else {
			f.createNewFile();

		}
		return FormetFileSize(s);
	}

	// public static String FormetFileSize(long fileS)
	// {// 转换文件大小
	// DecimalFormat df = new DecimalFormat("0.##");
	// String fileSizeString = "";
	// if (fileS < 1024) {
	// fileSizeString = df.format((double) fileS) + "B";
	// }
	// else if (fileS < 1048576) {
	// fileSizeString = df.format((double) fileS / 1024) + "KB";
	// }
	// else if (fileS < 1073741824) {
	// fileSizeString = df.format((double) fileS / 1048576) + "MB";
	// }
	// else {
	// fileSizeString = df.format((double) fileS / 1073741824) + "GB";
	// }
	// return fileSizeString;
	// }
	public static String FormetFileSize(long size)
	{
		long kb = 1024;
		long mb = kb * 1024;
		long gb = mb * 1024;

		if (size >= gb) {
			return String.format("%.2f GB", (float) size / gb);
		}
		else if (size >= mb) {
			float f = (float) size / mb;
			return String.format(f > 100 ? "%.2f MB" : "%.2f MB", f);
		}
		else if (size >= kb) {
			float f = (float) size / kb;
			return String.format(f > 100 ? "%.2f KB" : "%.2f KB", f);
		}
		else
			return String.format("%d B", size);
	}

	/*
	 * @return extension, not including "." and extension is lowercase
	 */
	public static String getExtensionWithoutDot(String name)
	{
		if (name == null || name.length() <= 0) {
			return null;
		}
		if (name.indexOf(".") == -1) {
			return "";
		}
		name = name.toLowerCase();
		int dot = name.lastIndexOf(".");
		String tmp = null;
		String ret = null;
		if (dot >= 0) {
			ret = name.substring(dot);
			tmp = name.substring(0, dot);
			if (tmp != null && tmp.length() > 0) {
				dot = tmp.lastIndexOf(".");
				if (dot >= 0) {
					tmp = tmp.substring(dot);
					if (tmp != null && tmp.length() > 0 && tmp.equalsIgnoreCase(".tar")) {
						ret = name.substring(dot);
					}
				}
			}
		}
		if (ret != null && ret.length() > 1) {
			ret = ret.substring(1);
		}
		return ret;
	}

	// TODO:???
	public static String getFileExtensionFromName(String paramString)
	{
		if (paramString == null || paramString.length() <= 0)
			return null;
		int i = indexOfExtension(paramString);
		if (i == -1) {
			return "";
		}
		try {
			return paramString.substring(i + 1);
		}
		catch (Throwable e) {
			e.printStackTrace();
		}
		return "";
	}

	// TODO:???
	public static String getFileNameWithoutExtention(String paramString)
	{
		if (paramString == null || paramString.length() <= 0)
			return null;
		int i = indexOfExtension(paramString);
		if (i == -1) {
			return paramString;
		}
		try {
			return paramString.substring(0, i);
		}
		catch (Throwable e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String getFileExtensionFromUrl(String paramString)
	{
		if ((paramString != null) && (paramString.length() > 0)) {
			int i = paramString.lastIndexOf('?');
			if (i > 0)
				paramString = paramString.substring(0, i);
			int j = paramString.lastIndexOf('/');
			if (j >= 0 && j < paramString.length() - 1) {
				int k = j + 1;
				String str1 = paramString.substring(k);
				if ((str1.length() <= 0) || (!Pattern.matches("[a-zA-Z_0-9\\.\\-\\(\\)]+", str1)))
					return "";
				int m = str1.lastIndexOf('.');
				if (m >= 0 && m < paramString.length() - 1) {
					return str1.substring(m + 1);
				}
			}
		}
		return "";
	}

	private static int indexOfExtension(String paramString)
	{
		if (paramString == null || paramString.length() <= 0) {
			return -1;
		}
		int i = paramString.lastIndexOf(".");
		if (paramString.lastIndexOf("/") > i) {
			return -1;
		}
		return i;
	}

	// TODO:???
	public static boolean isGif(String path)
	{
		String fileExt = path.substring(path.lastIndexOf(".") + 1);
		if (fileExt.equalsIgnoreCase("gif")) {
			return true;
		}
		else {
			return false;
		}
	}

	// TODO:???
	public static byte[] readFile(String path)
	{
		if (path == null || path.length() <= 0) {
			return null;
		}
		File file = new File(path);
		if (file == null || !file.exists()) {
			return null;
		}
		FileInputStream fis = null;
		ByteArrayBuffer byteArray = null;
		byte[] buffer = new byte[1024];
		int i = 0;
		try {
			fis = new FileInputStream(file);
			while ((i = fis.read(buffer)) != -1) {
				if (byteArray == null) {
					byteArray = new ByteArrayBuffer(i);
				}
				else {
					byteArray.append(buffer, 0, i);
				}
			}
		}
		catch (Throwable e) {
			e.printStackTrace();
		}
		finally {
			FileUtil.closeQuietly(fis);
		}
		if (byteArray != null) {
			return byteArray.buffer();
		}

		return null;
	}

	/*
	 * from org.apache.commons.io Unconditionally close an <code>Reader</code>.
	 * <p> Equivalent to {@link Reader#close()}, except any exceptions will be
	 * ignored. This is typically used in finally blocks.
	 * 
	 * @param input the Reader to close, may be null or already closed
	 */
	public static void closeQuietly(Reader input)
	{
		try {
			if (input != null) {
				input.close();
			}
		}
		catch (Throwable ioe) {
			// ignore
		}
	}

	/*
	 * from org.apache.commons.io Unconditionally close a <code>Channel</code>.
	 * <p> Equivalent to {@link Channel#close()}, except any exceptions will be
	 * ignored. This is typically used in finally blocks.
	 * 
	 * @param channel the Channel to close, may be null or already closed
	 */
	public static void closeQuietly(Channel channel)
	{
		try {
			if (channel != null) {
				channel.close();
			}
		}
		catch (Throwable ioe) {
			// ignore
		}
	}

	/*
	 * from org.apache.commons.io Unconditionally close a <code>Writer</code>.
	 * <p> Equivalent to {@link Writer#close()}, except any exceptions will be
	 * ignored. This is typically used in finally blocks.
	 * 
	 * @param output the Writer to close, may be null or already closed
	 */
	public static void closeQuietly(Writer output)
	{
		try {
			if (output != null) {
				output.close();
			}
		}
		catch (Throwable ioe) {
			// ignore
		}
	}

	/*
	 * from org.apache.commons.io Unconditionally close an
	 * <code>InputStream</code>. <p> Equivalent to {@link InputStream#close()},
	 * except any exceptions will be ignored. This is typically used in finally
	 * blocks.
	 * 
	 * @param input the InputStream to close, may be null or already closed
	 */
	public static void closeQuietly(InputStream input)
	{
		try {
			if (input != null) {
				input.close();
			}
		}
		catch (Throwable ioe) {
			// ignore
		}
	}

	/*
	 * from org.apache.commons.io Unconditionally close an
	 * <code>OutputStream</code>. <p> Equivalent to {@link
	 * OutputStream#close()}, except any exceptions will be ignored. This is
	 * typically used in finally blocks.
	 * 
	 * @param output the OutputStream to close, may be null or already closed
	 */
	public static void closeQuietly(OutputStream output)
	{
		try {
			if (output != null) {
				output.close();
			}
		}
		catch (Throwable ioe) {
			// ignore
		}
	}

	/**
	 * 根据路径删除指定的目录或文件，无论存在与�?
	 * 
	 * @param sPath
	 *            要删除的目录或文�?
	 * @return 删除成功返回 true，否则返�?false�?
	 */
	public static boolean DeleteFolder(String sPath)
	{
		boolean flag = false;
		File file = new File(sPath);
		// 判断目录或文件是否存�?
		if (!file.exists()) { // 不存在返�?false
			return flag;
		}
		else {
			// 判断是否为文�?
			if (file.isFile()) { // 为文件时调用删除文件方法
				return deleteFile(sPath);
			}
			else { // 为目录时调用删除目录方法
				return deleteDirectory(sPath, true);
			}
		}
	}

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件�?
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String sPath)
	{
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	/**
	 * 删除目录（文件夹）以及目录下的文�?
	 * 
	 * @param sPath
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public static boolean deleteDirectory(String sPath, boolean deleteCurrentDir)
	{
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔�?
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则�?�?
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		boolean flag = true;
		// 删除文件夹下的所有文�?包括子目�?
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文�?
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} // 删除子目�?
			else {
				flag = deleteDirectory(files[i].getAbsolutePath(), true);
				if (!flag)
					break;
			}
		}
		// 删除当前目录
		if (flag && deleteCurrentDir) {
			flag = dirFile.delete();
		}
		return flag;
	}

	public static long LOW_DISK_SIZE_QUOTA = 2 * 1024 * 1024; // 2M

	/**
	 * @return Available disk size in bytes
	 */
	public static long getAvailableDiskSize()
	{
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return blockSize * availableBlocks;
	}

	/**
	 * @param space
	 *            in bytes
	 * @return true if free space is larger than space otherwise false
	 */
	public static boolean hasEnoughtSpace(long space)
	{
		if (getAvailableDiskSize() > space) {
			return true;
		}
		return false;
	}

	public static Drawable getAPKDrawable(String filePath, Context context)
	{
		Drawable dr = null;
		if (filePath != null) {
			String PATH_PackageParser = "android.content.pm.PackageParser";
			String PATH_AssetManager = "android.content.res.AssetManager";
			try {
				// apk包的文件路径
				// 这是�?��Package 解释�? 是隐藏的
				// 构�?函数的参数只有一�? apk文件的路�?
				// PackageParser packageParser = new PackageParser(apkPath);
				Class<?> pkgParserCls = Class.forName(PATH_PackageParser);
				Class<?>[] typeArgs = new Class[1];
				typeArgs[0] = String.class;
				Constructor<?> pkgParserCt = pkgParserCls.getConstructor(typeArgs);
				Object[] valueArgs = new Object[1];
				valueArgs[0] = filePath;
				Object pkgParser = pkgParserCt.newInstance(valueArgs);
				// 这个是与显示有关�? 里面涉及到一些像素显示等�? 我们使用默认的情�?
				DisplayMetrics metrics = new DisplayMetrics();
				metrics.setToDefaults();
				// PackageParser.Package mPkgInfo =
				// packageParser.parsePackage(newFile(apkPath), apkPath,metrics,
				// 0);
				typeArgs = new Class[4];
				typeArgs[1] = String.class;
				typeArgs[0] = File.class;
				typeArgs[2] = DisplayMetrics.class;
				typeArgs[3] = Integer.TYPE;
				Method pkgParser_parsePackageMtd = pkgParserCls.getDeclaredMethod("parsePackage", typeArgs);
				valueArgs = new Object[4];
				valueArgs[0] = new File(filePath);
				valueArgs[1] = filePath;
				valueArgs[2] = metrics;
				valueArgs[3] = 0;
				Object pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser, valueArgs);
				// 应用程序信息�? 这个公开�? 不过有些函数, 变量没公�?
				// ApplicationInfo info = mPkgInfo.applicationInfo;
				Field appInfoFld = pkgParserPkg.getClass().getDeclaredField("applicationInfo");
				ApplicationInfo info = (ApplicationInfo) appInfoFld.get(pkgParserPkg);
				// uid 输出�?-1"，原因是未安装，系统未分配其Uid�?
				Class<?> assetMagCls = Class.forName(PATH_AssetManager);
				Constructor<?> assetMagCt = assetMagCls.getConstructor((Class[]) null);
				Object assetMag = assetMagCt.newInstance((Object[]) null);
				typeArgs = new Class[1];
				typeArgs[0] = String.class;
				Method assetMag_addAssetPathMtd = assetMagCls.getDeclaredMethod("addAssetPath", typeArgs);
				valueArgs = new Object[1];
				valueArgs[0] = filePath;
				assetMag_addAssetPathMtd.invoke(assetMag, valueArgs);
				Resources res = context.getResources();
				typeArgs = new Class[3];
				typeArgs[0] = assetMag.getClass();
				typeArgs[1] = res.getDisplayMetrics().getClass();
				typeArgs[2] = res.getConfiguration().getClass();
				Constructor<?> resCt = Resources.class.getConstructor(typeArgs);
				valueArgs = new Object[3];
				valueArgs[0] = assetMag;
				valueArgs[1] = res.getDisplayMetrics();
				valueArgs[2] = res.getConfiguration();
				res = (Resources) resCt.newInstance(valueArgs);
				CharSequence label = null;

				// app名字
				if (info.labelRes != 0) {
					label = res.getText(info.labelRes);
				}
				// 这里就是读取apk程序的图�?
				if (info.icon != 0) {
					dr = res.getDrawable(info.icon);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return dr;
	}

	public void GetDirectoryDetailAndSize(String filepath)
	{
		String mPathString = filepath;
		File mFile = new File(mPathString);

		if (!mFile.isDirectory()) {
			return;

		}
		if (!mPathString.endsWith(File.separator)) {
			mPathString = mPathString + File.separator;
		}
		File dirFile = new File(mPathString);

		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {

			if (files[i].isFile()) {
				fileSzie += files[i].length();
				fileNumber++;

			}
			else {
				dirNumber++;
				GetDirectoryDetailAndSize(files[i].getAbsolutePath());

			}
		}
		setFileNumber(fileNumber);
		setFileSzie(fileSzie);
		setDirNumber(dirNumber);

	}

	public static int GetAfterDirectoryFileCounts(String filepath)
	{
		String mPathString = filepath;
		File mFile = new File(mPathString);

		if (!mFile.isDirectory()) {
			return 0;

		}
		if (!mPathString.endsWith(File.separator)) {
			mPathString = mPathString + File.separator;
		}
		File dirFile = new File(mPathString);

		File[] files = dirFile.listFiles();
		if (files == null) {
			return 0;
		}
		else {
			return AfterDirectoryFileCount(files);
		}

	}

	public static int AfterDirectoryFileCount(File[] f)
	{
		int count = 0;
		if (f != null) {
			for (int i = 0; i < f.length; i++) {
				if (!f[i].isHidden()) {
					count++;
				}
			}
		}

		return count;

	}

	/**
	 * 外部存储是否可用
	 * 
	 * @return
	 */
	static public boolean externalMemoryAvailable()
	{
		return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}

	/**
	 * 获取手机内部可用空间大小
	 * 
	 * @return
	 */
	static public long getAvailableInternalMemorySize()
	{
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	/**
	 * 获取手机内部空间大小
	 * 
	 * @return
	 */
	static public long getTotalInternalMemorySize()
	{
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return totalBlocks * blockSize;
	}

	/**
	 * 获取手机外部可用空间大小
	 * 
	 * @return
	 */
	static public long getAvailableExternalMemorySize()
	{
		if (externalMemoryAvailable()) {
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks();
			return availableBlocks * blockSize;
		}
		else {
			return ERROR;
		}
	}

	/**
	 * 获取手机外部空间大小
	 * 
	 * @return
	 */
	static public long getTotalExternalMemorySize()
	{
		if (externalMemoryAvailable()) {
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long totalBlocks = stat.getBlockCount();
			return totalBlocks * blockSize;
		}
		else {
			return ERROR;
		}
	}

	public long getFileSzie()
	{
		return fileSzie;
	}

	public void setFileSzie(long fileSzie)
	{
		this.fileSzie = fileSzie;
	}

	public int getFileNumber()
	{
		return fileNumber;
	}

	public void setFileNumber(int fileNumber)
	{
		this.fileNumber = fileNumber;
	}

	public int getDirNumber()
	{
		return dirNumber;
	}

	public void setDirNumber(int dirNumber)
	{
		this.dirNumber = dirNumber;
	}

	// 复制文件
	public static void copyFile(String sourcefile, String targetfile) throws IOException
	{
		File sourceFile = new File(sourcefile);
		File targetFile;
		if (sourcefile.equals(targetfile)) {
			targetFile = touchNewFile(targetfile);

		}
		else {
			targetFile = touchNewFile(targetfile + File.separator + new File(sourcefile).getName());
		}
		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		try {
			// 新建文件输入流并对它进行缓冲
			inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

			// 新建文件输出流并对它进行缓冲
			outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

			// 缓冲数组
			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			// 刷新此缓冲的输出�?
			outBuff.flush();
		}
		finally {
			// 关闭�?
			if (inBuff != null)
				inBuff.close();
			if (outBuff != null)
				outBuff.close();
		}
	}

	// 复制文件
	public static void copyFile1(File sourceFile, File targetFile) throws IOException
	{
		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		try {
			// 新建文件输入流并对它进行缓冲
			inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

			// 新建文件输出流并对它进行缓冲
			outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

			// 缓冲数组
			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			// 刷新此缓冲的输出�?
			outBuff.flush();
		}
		finally {
			// 关闭�?
			if (inBuff != null)
				inBuff.close();
			if (outBuff != null)
				outBuff.close();
		}
	}

	// 复制文件�?
	public static void copyDirectiory1(String sourceDir, String targetDir) throws IOException
	{
		// 新建目标目录
		File f = touchNewFile(targetDir);
		f.mkdirs();
		targetDir = f.getPath();
		// 获取源文件夹当前下的文件或目�?
		File[] file = (new File(sourceDir)).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				// 源文�?
				File sourceFile = file[i];
				// 目标文件
				File targetFile = new File(new File(targetDir).getAbsolutePath() + File.separator + file[i].getName());
				copyFile1(sourceFile, targetFile);
			}
			if (file[i].isDirectory()) {
				// 准备复制的源文件�?
				String dir1 = sourceDir + "/" + file[i].getName();
				// 准备复制的目标文件夹
				String dir2 = targetDir + "/" + file[i].getName();
				copyDirectiory1(dir1, dir2);
			}
		}
	}

	// 复制文件�?
	public static void copyDirectiory(String sourceDir, String targetDir) throws IOException
	{
		File f;
		// 新建目标目录
		if (sourceDir.equals(targetDir)) {
			f = touchNewFile(targetDir);
			f.mkdirs();
			targetDir = f.getPath();
		}
		else {
			if (targetDir.lastIndexOf(File.separator) == (targetDir.length() - 1)) {
				targetDir += new File(sourceDir).getName();
				f = touchNewFile(targetDir);
				f.mkdirs();
				targetDir = f.getPath();
			}
			else {
				targetDir += File.separator + new File(sourceDir).getName();
				f = touchNewFile(targetDir);
				f.mkdirs();
				targetDir = f.getPath();
			}
		}

		// 获取源文件夹当前下的文件或目�?
		File[] file = (new File(sourceDir)).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				// 源文�?
				File sourceFile = file[i];
				// 目标文件
				File targetFile = new File(new File(targetDir).getAbsolutePath() + File.separator + file[i].getName());
				copyFile1(sourceFile, targetFile);
			}
			if (file[i].isDirectory()) {
				// 准备复制的源文件�?
				String dir1 = sourceDir + "/" + file[i].getName();
				// 准备复制的目标文件夹
				String dir2 = targetDir + "/" + file[i].getName();
				copyDirectiory1(dir1, dir2);
			}
		}
	}

	public static File touchNewFile(String path)
	{
		int i = 1;
		String temp = path;
		while (true) {
			File file = new File(path);
			if (file.exists()) {
				path = temp;

				if (file.isDirectory()) {
					path += "(" + i + ")";

				}
				else {
					Log.i("1234567890+++++++++++++++++++++1", getFileNameWithoutExtention(path));
					Log.i("1234567890+++++++++++++++++++++2", getExtensionWithoutDot(path));
					if (getExtensionWithDot(path) == null) {
						path = getFileNameWithoutExtention(path) + "(" + i + ")";
					}
					else {
						path = getFileNameWithoutExtention(path) + "(" + i + ")" + getExtensionWithDot(path);
					}

					Log.i("1234567890+++++++++++++++++++++3", path);
				}
				i++;
			}
			else {
				return file;
			}

		}
	}

	// 建立一个MIME类型与文件后缀名的匹配表
	public static final String[][] MIME_MapTable =
	{
			// {后缀名， MIME类型}
	{ ".3gp", "video/3gpp" },
	{ ".apk", "application/vnd.android.package-archive" },
	{ ".asf", "video/x-ms-asf" },
	{ ".avi", "video/x-msvideo" },
	{ ".bin", "application/octet-stream" },
	{ ".bmp", "image/bmp" },
	{ ".c", "text/plain" },
	{ ".class", "application/octet-stream" },
	{ ".conf", "text/plain" },
	{ ".cpp", "text/plain" },
	{ ".doc", "application/msword" },
	{ ".docx", "application/msword" },
	{ ".exe", "application/octet-stream" },
	{ ".gif", "image/gif" },
	{ ".gtar", "application/x-gtar" },
	{ ".gz", "application/x-gzip" },
	{ ".h", "text/plain" },
	{ ".htm", "text/html" },
	{ ".html", "text/html" },
	{ ".jar", "application/java-archive" },
	{ ".java", "text/plain" },
	{ ".jpeg", "image/jpeg" },
	{ ".jpg", "image/jpeg" },
	{ ".js", "application/x-javascript" },
	{ ".log", "text/plain" },
	{ ".m3u", "audio/x-mpegurl" },
	{ ".m4a", "audio/mp4a-latm" },
	{ ".m4b", "audio/mp4a-latm" },
	{ ".m4p", "audio/mp4a-latm" },
	{ ".m4u", "video/vnd.mpegurl" },
	{ ".m4v", "video/x-m4v" },
	{ ".mov", "video/quicktime" },
	{ ".mp2", "audio/x-mpeg" },
	{ ".mp3", "audio/x-mpeg" },
	{ ".mp4", "video/mp4" },
	{ ".mpc", "application/vnd.mpohun.certificate" },
	{ ".mpe", "video/mpeg" },
	{ ".mpeg", "video/mpeg" },
	{ ".mpg", "video/mpeg" },
	{ ".mpg4", "video/mp4" },
	{ ".mpga", "audio/mpeg" },
	{ ".msg", "application/vnd.ms-outlook" },
	{ ".ogg", "audio/ogg" },
	{ ".pdf", "application/pdf" },
	{ ".png", "image/png" },
	{ ".pps", "application/vnd.ms-powerpoint" },
	{ ".ppt", "application/vnd.ms-powerpoint" },
	{ ".pptx", "application/vnd.ms-powerpoint" },
	{ ".prop", "text/plain" },
	{ ".rar", "application/x-rar-compressed" },
	{ ".rc", "text/plain" },
	{ ".rmvb", "audio/x-pn-realaudio" },
	{ ".rtf", "application/rtf" },
	{ ".sh", "text/plain" },
	{ ".tar", "application/x-tar" },
	{ ".tgz", "application/x-compressed" },
	{ ".txt", "text/plain" },
	{ ".wav", "audio/x-wav" },
	{ ".wma", "audio/x-ms-wma" },
	{ ".wmv", "audio/x-ms-wmv" },
	{ ".dat", "text/plain" },
	{ ".sh", "text/plain" },
	{ ".py", "text/plain" },
	{ ".m", "text/plain" },
	{ ".wps", "application/vnd.ms-works" },
			// {".xml", "text/xml"},
	{ ".xml", "text/plain" },
	{ ".z", "application/x-compress" },
	{ ".zip", "application/zip" },
	{ "", "*/*" } };

	/**
	 * 根据文件后缀名获得对应的MIME类型。
	 * 
	 * @param file
	 */
	public static String getMIMEType(File file)
	{
		String type = "*/*";
		String fName = file.getName();
		// 获取后缀名前的分隔符"."在fName中的位置。
		int dotIndex = fName.lastIndexOf(".");
		if (dotIndex < 0) {
			return type;
		}
		/* 获取文件的后缀名 */
		String end = fName.substring(dotIndex, fName.length()).toLowerCase();
		if (end == "")
			return type;
		// 在MIME和文件类型的匹配表中找到对应的MIME类型。
		for (int i = 0; i < MIME_MapTable.length; i++) {
			if (end.equals(MIME_MapTable[i][0]))
				type = MIME_MapTable[i][1];
		}
		return type;
	}

	public static String getMimeType(String url)
	{
		String type = null;
		String extension = MimeTypeMap.getFileExtensionFromUrl(url);
		if (extension != null) {
			MimeTypeMap mime = MimeTypeMap.getSingleton();
			type = mime.getMimeTypeFromExtension(extension);
		}
		return type;
	}

	/**
	 * 打开文件
	 * 
	 * @param file
	 */
	public static void openFile(File file, Context mcontext)
	{
		// Uri uri = Uri.parse("file://"+file.getAbsolutePath());
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// 设置intent的Action属性
		intent.setAction(Intent.ACTION_VIEW);
		// 获取文件file的MIME类型
		String type = getMIMEType(file);
		// 设置intent的data和Type属性。
		intent.setDataAndType(/* uri */Uri.fromFile(file), type);
		// 跳转
		try {
			mcontext.startActivity(intent);
		}
		catch (Exception exception) {

		}

	}

	public static InetAddress intToInet(int value)
	{
		byte[] bytes = new byte[4];
		for (int i = 0; i < 4; i++) {
			bytes[i] = byteOfInt(value, i);
		}
		try {
			return InetAddress.getByAddress(bytes);
		}
		catch (UnknownHostException e) {
			// This only happens if the byte array has a bad length
			return null;
		}
	}

	public static byte byteOfInt(int value, int which)
	{
		int shift = which * 8;
		return (byte) (value >> shift);
	}

	public static String ipToString(int addr, String sep)
	{
		if (addr > 0) {
			StringBuffer buf = new StringBuffer();
			buf.append(byteOfInt(addr, 0)).append(sep).append(byteOfInt(addr, 1)).append(sep).append(byteOfInt(addr, 2)).append(sep).append(byteOfInt(addr, 3));
			return buf.toString();
		}
		else {
			return null;
		}
	}

	public static void sleepIgnoreInterupt(long millis)
	{
		try {
			Thread.sleep(millis);
		}
		catch (InterruptedException e) {
		}
	}

	public static String getSdDirectory()
	{
		return Environment.getExternalStorageDirectory().getPath();
	}
}
