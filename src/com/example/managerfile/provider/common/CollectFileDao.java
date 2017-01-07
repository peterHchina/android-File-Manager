package com.example.managerfile.provider.common;

import java.util.ArrayList;
import java.util.List;

import com.example.managerfile.model.CollectFile;
import com.example.managerfile.provider.DBHelper;
import com.example.managerfile.util.AllFileUtil.SDCardInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.http.SslCertificate.DName;

public class CollectFileDao
{
	private DBHelper dbHelper;

	public CollectFileDao(Context context)
	{
		dbHelper = DBHelper.getInstance(context);
	}

	public List<CollectFile> find(String path)
	{
		boolean reslute = false;
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		List<CollectFile> list = new ArrayList<CollectFile>();
		db.beginTransaction();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from " + DBHelper.FILE_TAB + " where " + DBHelper.FILE_PATH + " =?", new String[]
			{ path });

			if (cursor.moveToNext()) {
				do {
					CollectFile file = new CollectFile();
					file.setFileName(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.FILE_NAME)));
					file.setPath(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.FILE_PATH)));
					list.add(file);
				}
				while (cursor.moveToNext());
			}
			cursor.close();
			db.setTransactionSuccessful();
			db.endTransaction();
		}
		return list;
	}

	public int delete(String path)
	{
		int isSuccessed = -100;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.beginTransaction();
		if (db.isOpen()) {
			// isSuccessed=db.execSQL("delete from " + DBHelper.FILE_TAB +
			// " where " + DBHelper.FILE_PATH + " =?", new String[]
			// { path });

			isSuccessed = db.delete(DBHelper.FILE_TAB, DBHelper.FILE_PATH + "  = ? ", new String[]
			{ path });
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		return isSuccessed;
	}

	public long add(String path, String fileName)
	{
		long is = -1;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.beginTransaction();
		ContentValues cv = new ContentValues();
		cv.put(DBHelper.FILE_PATH, path);
		cv.put(DBHelper.FILE_NAME, fileName);
		if (db.isOpen()) {
			// db.execSQL("insert into appinfo ( packagename) values(?)", new
			// String[]{ packagename });
			is = db.insert(DBHelper.FILE_TAB, null, cv);
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		return is;
	}

	public void update(String path, String fileName)
	{
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.beginTransaction();
		String sql = "alter " + DBHelper.FILE_TAB + " where " + DBHelper.FILE_PATH + " path" + " =?";
		ContentValues cV = new ContentValues();
		cV.put(DBHelper.FILE_NAME, fileName);
		cV.put(DBHelper.FILE_PATH, path);
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select " + DBHelper.FILE_PATH + " from " + DBHelper.FILE_TAB + " where " + DBHelper.FILE_PATH + " =?", new String[]
			{ path });
			if (cursor.moveToFirst()) {
				db.update(DBHelper.FILE_TAB, cV, "path = ? ", new String[]
				{ path });
			}
			cursor.close();
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public List<CollectFile> getAllCollectFiles()
	{
		ArrayList<CollectFile> list = new ArrayList<CollectFile>();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		db.beginTransaction();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from " + DBHelper.FILE_TAB, null);
			if (cursor.moveToFirst()) {
				do {
					CollectFile file = new CollectFile();
					file.setPath(cursor.getString(cursor.getColumnIndex(DBHelper.FILE_PATH)));
					file.setFileName(cursor.getString(cursor.getColumnIndex(DBHelper.FILE_NAME)));
					list.add(file);
				}
				while (cursor.moveToNext());
			}
			cursor.close();
			db.setTransactionSuccessful();
			db.endTransaction();
		}
		return list;
	}
}
