package com.example.managerfile.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper
{

	private static String db_name = "file_db";
	private static int db_version = 1;
	public final static String FILE_TAB = "collect_tab";
	public final static String FILE_PATH = "path";
	public final static String FILE_NAME="name";
	private static DBHelper instance = null;

	public DBHelper(Context context)
	{
		super(context, db_name, null, db_version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE IF NOT EXISTS " + FILE_TAB + " (_id integer primary key autoincrement ," + FILE_PATH + " varchar(100), "+FILE_NAME+"  varchar(100))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int old_version, int new_version)
	{
		// TODO Auto-generated method stub
//		if (old_version == 2) {
//			db.execSQL("ALTER TABLE appinfo ADD name varchar(20) NULL");
//		}

	}

	public static DBHelper getInstance(Context context)
	{
		if (instance == null) {
			instance = new DBHelper(context);
		}
		return instance;
	}

}
