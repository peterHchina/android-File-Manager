package com.example.managerfile.ui.operate;

import java.io.File;

import com.example.managerfile.R;
import com.example.managerfile.ui.FileManagerActivity;

import android.R.integer;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.preference.PreferenceManager;

public class DialogSortFile
{
	Context mContext;
	private SharedPreferences.Editor editor;
	private SharedPreferences settingPreferences;
	private static final String SETTING_SORT_STYLE = "com.FileManger.sort";
	private FileManagerActivity mFileManagerActivity;
	private File mCurrentParent;

	public DialogSortFile(Context context, FileManagerActivity fileManagerActivity,
			SharedPreferences sharedPreferences, File currentParent)
	{
		mContext = context;
		settingPreferences = sharedPreferences;
		editor = settingPreferences.edit();
		mFileManagerActivity = fileManagerActivity;
		mCurrentParent=currentParent;
	}

	public void init()
	{
		AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
		mBuilder.setTitle(R.string.sort);
		int count = settingPreferences.getInt(SETTING_SORT_STYLE, 0);
		mBuilder.setSingleChoiceItems(R.array.sort, count - 1, new OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				switch (which)
				{
				case 0:
					editor.putInt(SETTING_SORT_STYLE, 1);
					editor.commit();
					mFileManagerActivity.reflashPath(mCurrentParent.getPath());
					dialog.dismiss();
					break;
				case 1:
					editor.putInt(SETTING_SORT_STYLE, 2);
					editor.commit();
					mFileManagerActivity.reflashPath(mCurrentParent.getPath());
					dialog.dismiss();
					break;
				case 2:
					editor.putInt(SETTING_SORT_STYLE, 3);
					editor.commit();
					mFileManagerActivity.reflashPath(mCurrentParent.getPath());
					dialog.dismiss();
					break;
				default:
					editor.putInt(SETTING_SORT_STYLE, 1);
					editor.commit();
					mFileManagerActivity.reflashPath(mCurrentParent.getPath());
					dialog.dismiss();
					break;
				}

			}
		});
//		mBuilder.setPositiveButton(R.string.ok, new OnClickListener()
//		{
//
//			@Override
//			public void onClick(DialogInterface arg0, int arg1)
//			{
//				// TODO Auto-generated method stub
//				editor.commit();
//				mFileManagerActivity.reflashPath(mCurrentParent.getPath());
//			}
//		});
//		mBuilder.setNegativeButton(R.string.cancel, null);
		
		
		mBuilder.show();
	}
}
