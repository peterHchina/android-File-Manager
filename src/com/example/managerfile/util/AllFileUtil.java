package com.example.managerfile.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.example.managerfile.model.ApkPackageinfo;
import com.example.managerfile.model.CategoryFileinfo;
import com.example.managerfile.model.CollectFile;
import com.example.managerfile.model.Compressinfo;
import com.example.managerfile.model.DetailCategoryFileinfo;
import com.example.managerfile.model.Imageinfo;
import com.example.managerfile.model.Musicinfo;
import com.example.managerfile.model.Videoinfo;
import com.example.managerfile.provider.common.CollectFileDao;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Files.FileColumns;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Files;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video;
import android.util.Log;
import android.widget.Toast;

public class AllFileUtil
{
	public static String sZipFileMimeType = "application/zip";

	public static enum FileCategory
	{
		All, Music, Video, Picture, Theme, Doc, Zip, Apk, Custom, Other, Favorite
	}

	public enum SortMethod
	{
		name, size, date, type
	}

	public static FileCategory[] sCategories = new FileCategory[]
	{ FileCategory.Music, FileCategory.Video, FileCategory.Picture, FileCategory.Theme, FileCategory.Doc, FileCategory.Zip, FileCategory.Apk, FileCategory.Other };
	private static HashMap<FileCategory, CategoryFileinfo> mCategoryInfo = new HashMap<FileCategory, CategoryFileinfo>();

	public static List<Videoinfo> GetVideoList(Context context)
	{
		List<Videoinfo> mVideoList = new ArrayList<Videoinfo>();
		// MediaStore.Video.Thumbnails.DATA:视频缩略图的文件路径
		Cursor cursor = null;
		String[] thumbColumns =
		{ MediaStore.Video.Thumbnails.DATA, MediaStore.Video.Thumbnails.VIDEO_ID };
		if (context != null) {
			cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
		}
		if (cursor == null) {
			Toast.makeText(context, "没有找到可播放视频文件", 1).show();
			return null;
		}
		if (cursor.moveToFirst()) {
			do {
				Videoinfo info = new Videoinfo();
				int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media._ID));
				info.setId(id);
				Cursor thumbCursor = context.getContentResolver().query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, thumbColumns, MediaStore.Video.Thumbnails.VIDEO_ID + "=" + id, null, null);
				if (thumbCursor.moveToFirst()) {
					info.setThumbPath(thumbCursor.getString(thumbCursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA)));
				}
				info.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
				info.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)));
				info.setAlbum(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM)));
				info.setDisplayName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)));
				info.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)));
				info.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)));
				info.setMimeType(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)));
				info.setArtist(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST)));
				mVideoList.add(info);
			}
			while (cursor.moveToNext());
			cursor.close();
		}
		return mVideoList;
	}

	public static List<Musicinfo> GetMusicList(Context context)
	{
		List<Musicinfo> mMusicList = new ArrayList<Musicinfo>();
		Cursor cursor = null;
		if (context != null) {
			cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
		}
		if (cursor == null) {
			Toast.makeText(context, "没有找到可播放音乐文件", 1).show();
			return null;
		}
		if (cursor.moveToFirst()) {
			do {
				Musicinfo info = new Musicinfo();
				info.setId(cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media._ID)));
				info.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
				info.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)));
				info.setAlbum(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM)));
				info.setDisplayName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)));
				info.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)));
				info.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)));
				info.setMimeType(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)));
				info.setArtist(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST)));
				mMusicList.add(info);
			}
			while (cursor.moveToNext());
			cursor.close();
		}
		return mMusicList;
	}

	public static List<Imageinfo> GetImageList(Context context)
	{
		List<Imageinfo> mImageList = new ArrayList<Imageinfo>();
		Cursor cursor = null;
		if (context != null) {
			cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
		}
		if (cursor == null) {
			Toast.makeText(context, "没有找到可播放图像文件", 1).show();
			return null;
		}
		if (cursor.moveToFirst()) {
			do {
				Imageinfo info = new Imageinfo();
				info.setId(cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media._ID)));
				info.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
				info.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)));
				info.setDisplayName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)));
				info.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)));
				info.setMimeType(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)));
				mImageList.add(info);
			}
			while (cursor.moveToNext());
			cursor.close();
		}
		return mImageList;
	}

	@SuppressLint("NewApi")
	public static List<DetailCategoryFileinfo> GetDocumentList(Context context)
	{
		List<DetailCategoryFileinfo> mDocumentList = new ArrayList<DetailCategoryFileinfo>();
		Cursor cursor = null;
		if (context != null) {
			cursor = context.getContentResolver().query(MediaStore.Files.getContentUri("external"), null, buildDocSelection(), null, null);
		}
		if (cursor == null) {
			Toast.makeText(context, "没有找到可看的文档文件", 1).show();
			return null;
		}
		if (cursor.moveToFirst()) {
			do {
				DetailCategoryFileinfo info = new DetailCategoryFileinfo();
				info.setId(cursor.getInt(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID)));
				info.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)));
				info.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE)));
				info.setDisplayName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)));
				info.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)));
				info.setMimeType(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE)));
				mDocumentList.add(info);
			}
			while (cursor.moveToNext());
			cursor.close();
		}
		return mDocumentList;
	}

	@SuppressLint("NewApi")
	public static List<ApkPackageinfo> GetApkPackageList(Context context)
	{
		List<ApkPackageinfo> mApkPackageList = new ArrayList<ApkPackageinfo>();
		String sectionString = FileColumns.DATA + " LIKE '%.apk'";
		Cursor cursor = null;
		if (context != null) {
			cursor = context.getContentResolver().query(MediaStore.Files.getContentUri("external"), null, sectionString, null, null);
		}
		if (cursor == null) {
			Toast.makeText(context, "没有找到可看的apk文件", 1).show();
			return null;
		}
		if (cursor.moveToFirst()) {
			do {
				ApkPackageinfo info = new ApkPackageinfo();
				info.setId(cursor.getInt(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID)));
				info.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)));
				info.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE)));
				info.setDisplayName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)));
				info.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)));
				info.setMimeType(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE)));
				mApkPackageList.add(info);
			}
			while (cursor.moveToNext());
			cursor.close();
		}
		return mApkPackageList;
	}

	@SuppressLint("NewApi")
	public static List<Compressinfo> GetCompressList(Context context)
	{
		List<Compressinfo> mCompressList = new ArrayList<Compressinfo>();
		Cursor cursor = null;
		if (context != null) {
			cursor = context.getContentResolver().query(MediaStore.Files.getContentUri("external"), null, buildComSelection(), null, null);
		}
		if (cursor == null) {
			Toast.makeText(context, "没有找到可看的压缩文件", 1).show();
			return null;
		}
		if (cursor.moveToFirst()) {
			do {
				Compressinfo info = new Compressinfo();
				info.setId(cursor.getInt(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID)));
				info.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)));
				info.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE)));
				info.setDisplayName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)));
				info.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)));
				info.setMimeType(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE)));
				mCompressList.add(info);
			}
			while (cursor.moveToNext());
			cursor.close();
		}
		return mCompressList;
	}

	@SuppressLint("NewApi")
	public static void refreshCategoryInfo(Context context)
	{
		// clear
		for (FileCategory fc : sCategories) {
			setCategoryInfo(fc, 0, 0);
		}
		// query database
		String volumeName = "external";
		Uri uri = Audio.Media.getContentUri(volumeName);
		refreshMediaCategory(context, FileCategory.Music, uri);

		uri = Video.Media.getContentUri(volumeName);
		refreshMediaCategory(context, FileCategory.Video, uri);

		uri = Images.Media.getContentUri(volumeName);
		refreshMediaCategory(context, FileCategory.Picture, uri);

		uri = Files.getContentUri(volumeName);
		refreshMediaCategory(context, FileCategory.Theme, uri);
		refreshMediaCategory(context, FileCategory.Doc, uri);
		refreshMediaCategory(context, FileCategory.Zip, uri);
		refreshMediaCategory(context, FileCategory.Apk, uri);
	}

	@SuppressLint("NewApi")
	public static List<DetailCategoryFileinfo> getDetailCategoryInfo(Context mContext, FileCategory fc)
	{
		List<DetailCategoryFileinfo> mDetailCategoryFileList = new ArrayList<DetailCategoryFileinfo>();
		Uri uri = Files.getContentUri("external");
		Cursor cursor = null;
		if (mContext != null) {
			cursor = mContext.getContentResolver().query(uri, null, buildSelectionByCategory(fc), null, null);
		}
		if (cursor == null) {
			Toast.makeText(mContext, "没有找到可看的文件", 1).show();
			return null;
		}
		if (cursor.moveToFirst()) {
			do {
				DetailCategoryFileinfo info = new DetailCategoryFileinfo();
				info.setId(cursor.getInt(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID)));
				info.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)));
				info.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE)));
				info.setDisplayName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)));
				info.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)));
				info.setMimeType(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE)));
				mDetailCategoryFileList.add(info);
			}
			while (cursor.moveToNext());
			cursor.close();
		}
		return mDetailCategoryFileList;
	}

	public static List<DetailCategoryFileinfo> getAllCollectFiles(Context context)
	{
		CollectFileDao mDao = new CollectFileDao(context);
		List<DetailCategoryFileinfo> list = new ArrayList<DetailCategoryFileinfo>();
		if (mDao != null) {

			List<CollectFile> temp = mDao.getAllCollectFiles();
			if (temp != null && temp.size() > 0) {
				for (int i = 0; i < temp.size(); i++) {
					File file = new File(temp.get(i).getPath());
					DetailCategoryFileinfo fileinfo = new DetailCategoryFileinfo();
					fileinfo.setPath(temp.get(i).getPath());
					fileinfo.setTitle(temp.get(i).getFileName());
					if (file.isFile()) {
						fileinfo.setSize(file.length());
					}
					list.add(fileinfo);
				}
			}
		}
		return list;
	}

	public static String buildSortOrder(SortMethod sort)
	{
		String sortOrder = null;
		switch (sort)
		{
		case name:
			sortOrder = FileColumns.TITLE + " asc";
			break;
		case size:
			sortOrder = FileColumns.SIZE + " asc";
			break;
		case date:
			sortOrder = FileColumns.DATE_MODIFIED + " desc";
			break;
		case type:
			sortOrder = FileColumns.MIME_TYPE + " asc, " + FileColumns.TITLE + " asc";
			break;
		}
		return sortOrder;
	}

	public static boolean refreshMediaCategory(Context mContext, FileCategory fc, Uri uri)
	{
		String[] columns = new String[]
		{ "COUNT(*)", "SUM(_size)" };
		Cursor c = mContext.getContentResolver().query(uri, columns, buildSelectionByCategory(fc), null, null);
		if (c == null) {
			return false;
		}

		if (c.moveToNext()) {
			setCategoryInfo(fc, c.getLong(0), c.getLong(1));
			c.close();
			return true;
		}
		return false;
	}

	public static String buildSelectionByCategory(FileCategory cat)
	{
		String selection = null;
		switch (cat)
		{
		case Theme:
			selection = FileColumns.DATA + " LIKE " + "'%.itz'" + " or " + FileColumns.DATA + " LIKE " + " '%.iuz'" + " or " + FileColumns.DATA + " LIKE " + " '%.mtz'" + " or " + FileColumns.DATA + " LIKE " + " '%.ttf'" + " or " + FileColumns.DATA + " LIKE " + " '%.txj'";
			// selection=buildThemeSelection();
			break;
		case Doc:
			selection = buildDocSelection();
			break;
		case Zip:
			selection = buildComSelection();
			break;
		case Apk:
			selection = FileColumns.DATA + " LIKE '%.apk'";
			break;
		default:
			selection = null;
		}
		return selection;
	}

	private static void setCategoryInfo(FileCategory fc, long count, long size)
	{
		CategoryFileinfo info = mCategoryInfo.get(fc);
		if (info == null) {
			info = new CategoryFileinfo();
			mCategoryInfo.put(fc, info);
		}
		info.setCount(count);
		info.setSize(size);
	}

	public static HashMap<FileCategory, CategoryFileinfo> getCategoryInfos()
	{
		return mCategoryInfo;
	}

	private static String buildDocSelection()
	{
		StringBuilder selection = new StringBuilder();
		Iterator<String> iter = FileUtil.sDocMimeTypesSet.iterator();
		while (iter.hasNext()) {
			selection.append("(" + FileColumns.MIME_TYPE + "=='" + iter.next() + "') OR ");
		}
		return selection.substring(0, selection.lastIndexOf(")") + 1);
	}

	private static String buildComSelection()
	{
		StringBuilder selection = new StringBuilder();
		Iterator<String> iter = FileUtil.sZipFileMimeType.iterator();
		while (iter.hasNext()) {
			selection.append("(" + FileColumns.MIME_TYPE + "=='" + iter.next() + "') OR ");
		}
		return selection.substring(0, selection.lastIndexOf(")") + 1);
	}

	private static String buildThemeSelection()
	{
		StringBuilder selection = new StringBuilder();
		Iterator<String> iter = FileUtil.sThemeFileMimeType.iterator();
		while (iter.hasNext()) {
			selection.append(FileColumns.DATA + " LIKE " + "'%." + iter.next() + "'  or ");
		}
		return selection.toString();
	}

	public static class SDCardInfo
	{
		public long total;
		public long free;
	}

	public static SDCardInfo getSDCardInfo()
	{
		String sDcString = android.os.Environment.getExternalStorageState();

		if (sDcString.equals(android.os.Environment.MEDIA_MOUNTED)) {
			File pathFile = android.os.Environment.getExternalStorageDirectory();

			try {
				android.os.StatFs statfs = new android.os.StatFs(pathFile.getPath());

				// 获取SDCard上BLOCK总数
				long nTotalBlocks = statfs.getBlockCount();

				// 获取SDCard上每个block的SIZE
				long nBlocSize = statfs.getBlockSize();

				// 获取可供程序使用的Block的数量
				long nAvailaBlock = statfs.getAvailableBlocks();

				// 获取剩下的所有Block的数量(包括预留的一般程序无法使用的块)
				long nFreeBlock = statfs.getFreeBlocks();

				SDCardInfo info = new SDCardInfo();
				// 计算SDCard 总容量大小MB
				info.total = nTotalBlocks * nBlocSize;

				// 计算 SDCard 剩余大小MB
				info.free = nAvailaBlock * nBlocSize;

				return info;
			}
			catch (IllegalArgumentException e) {
			}
		}
		return null;
	}

	public static void notifyFileSystemChanged(String path, Context mContext)
	{
		if (path == null)
			return;
		final File f = new File(path);
		final Intent intent;
		if (f.isDirectory()) {
			intent = new Intent(Intent.ACTION_MEDIA_MOUNTED);
			intent.setClassName("com.android.providers.media", "com.android.providers.media.MediaScannerReceiver");
			intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));

		}
		else {
			intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			intent.setData(Uri.fromFile(new File(path)));

		}
		mContext.sendBroadcast(intent);
	}
}
