package com.example.managerfile.ui.operate;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Stack;

import com.example.managerfile.R;

import android.R.integer;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.managerfile.task.DeleteFileTask;
import com.example.managerfile.task.DeleteSearchFileTask;
import com.example.managerfile.task.GetFileListTask;
import com.example.managerfile.task.RefleshCollectionFileTask;
import com.example.managerfile.ui.FileManagerActivity;
import com.example.managerfile.ui.ShowSearchResaultActivity;
import com.example.managerfile.model.Check;
import com.example.managerfile.model.DetailCategoryFileinfo;
import com.example.managerfile.model.Fileinfo;

public class DialogDeleteFile
{
	Context mContext;
	File mFile;
	FileManagerActivity mFileManagerActivity;
	ShowSearchResaultActivity mShowSearchResaultActivity;
	private Fileinfo mFileinfo = null;
	private List<Fileinfo> mList = null;
	private int fileType;
	private ListView listView;
	private LinearLayout mFileNoRecodeLayout;
	private TextView tvTitle;
	private FrameLayout mSearchBar;
	private List<DetailCategoryFileinfo> list;
	private Stack<Check> checks;
	private SharedPreferences settingSharedPreferences;

	public DialogDeleteFile(Context context, Fileinfo file, FileManagerActivity fileManagerActivity)
	{
		mContext = context;
		// mFile = file;
		mFileManagerActivity = fileManagerActivity;
		mFileinfo = file;
	}

	public DialogDeleteFile(Context context, Fileinfo file, ShowSearchResaultActivity showSearchResaultActivity)
	{
		mContext = context;
		// mFile = file;
		mShowSearchResaultActivity = showSearchResaultActivity;
		mFileinfo = file;
	}

	public DialogDeleteFile(Context context, List<Fileinfo> file, FileManagerActivity fileManagerActivity)
	{
		mContext = context;
		// mFile = file;
		mFileManagerActivity = fileManagerActivity;
		mList = file;
	}

	public DialogDeleteFile(Context context, ListView listView, List<DetailCategoryFileinfo> list, Fileinfo file, LinearLayout layout, TextView tvTitle, FrameLayout frameLayout, int fileType,
			Stack<Check> t)
	{
		mContext = context;
		mFileinfo = file;
		this.mSearchBar = frameLayout;
		this.listView = listView;
		this.list = list;
		mFileNoRecodeLayout = layout;
		this.tvTitle = tvTitle;
		this.fileType = fileType;
		checks = t;
	}

	public DialogDeleteFile(Context context, ListView listView, List<DetailCategoryFileinfo> list, Fileinfo file, LinearLayout layout, FrameLayout frameLayout, Stack<Check> t,
			SharedPreferences mPreferences)
	{
		mContext = context;
		mFileinfo = file;
		this.mSearchBar = frameLayout;
		this.listView = listView;
		this.list = list;
		mFileNoRecodeLayout = layout;
		settingSharedPreferences = mPreferences;
		checks = t;
	}

	public void init()
	{

		String stringFormate;
		AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(mContext);
		mDialogBuilder.setTitle(R.string.delete);
		if (mFileinfo != null) {

			if (!mFileinfo.isDrectory()) {
				stringFormate = mContext.getResources().getString(R.string.delete_file);

				mDialogBuilder.setMessage(String.format(stringFormate, mFileinfo.getmName()));
			}
			else {
				stringFormate = mContext.getResources().getString(R.string.delete_dir);
				mDialogBuilder.setMessage(String.format(stringFormate, mFileinfo.getmName()));
			}
		}
		if (mList != null && mList.size() != 0) {

			int number1 = getDirectoryNumber();
			int number2 = getFileNumber();

			if (number1 != 0 && number2 == 0) {
				stringFormate = mContext.getResources().getString(R.string.delete_list_dir);
				mDialogBuilder.setMessage(String.format(stringFormate, number1));
			}
			else if (number2 != 0 && number1 == 0) {
				stringFormate = mContext.getResources().getString(R.string.delete_list_file);
				mDialogBuilder.setMessage(String.format(stringFormate, number2));
			}
			else {
				stringFormate = mContext.getResources().getString(R.string.delete_list);
				mDialogBuilder.setMessage(String.format(stringFormate, number1, number2));
			}

		}
		mDialogBuilder.setPositiveButton(R.string.ok, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1)
			{
				// TODO Auto-generated method stub

				if (mFileinfo != null) {
					DeleteFileTask mDeleteFileTask = new DeleteFileTask(mFileinfo, mFileManagerActivity, mContext);
					mDeleteFileTask.execute();
				}
				if (mList != null) {
					DeleteFileTask mDeleteFileTask = new DeleteFileTask(mList, mFileManagerActivity, mContext);
					mDeleteFileTask.execute();
				}

			}
		});
		mDialogBuilder.setNegativeButton(R.string.cancel, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1)
			{
				// TODO Auto-generated method stub

			}
		});
		mDialogBuilder.show();
	}

	public void init1()
	{

		String stringFormate;
		AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(mContext);
		mDialogBuilder.setTitle(R.string.delete);
		if (!mFileinfo.isDrectory()) {
			stringFormate = mContext.getResources().getString(R.string.delete_file);

			mDialogBuilder.setMessage(String.format(stringFormate, mFileinfo.getmName()));
		}
		else {
			stringFormate = mContext.getResources().getString(R.string.delete_dir);
			mDialogBuilder.setMessage(String.format(stringFormate, mFileinfo.getmName()));
		}

		mDialogBuilder.setPositiveButton(R.string.ok, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1)
			{
				// TODO Auto-generated method stub

				DeleteSearchFileTask mDeleteFileTask = new DeleteSearchFileTask(mFileinfo, mShowSearchResaultActivity, mContext);
				mDeleteFileTask.execute();

			}
		});
		mDialogBuilder.setNegativeButton(R.string.cancel, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1)
			{
				// TODO Auto-generated method stub

			}
		});
		mDialogBuilder.show();
	}

	public void init2()
	{
		String stringFormate;
		AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(mContext);
		mDialogBuilder.setTitle(R.string.delete);
		if (mFileinfo != null) {

			if (!mFileinfo.isDrectory()) {
				stringFormate = mContext.getResources().getString(R.string.delete_file);

				mDialogBuilder.setMessage(String.format(stringFormate, mFileinfo.getmName()));
			}
			else {
				stringFormate = mContext.getResources().getString(R.string.delete_dir);
				mDialogBuilder.setMessage(String.format(stringFormate, mFileinfo.getmName()));
			}
		}

		mDialogBuilder.setPositiveButton(R.string.ok, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1)
			{
				// TODO Auto-generated method stub

				GetFileListTask mDeleteFileTask = new GetFileListTask(listView, list, mFileinfo, mFileNoRecodeLayout, tvTitle, fileType, mSearchBar, checks, mContext, true, false);
				mDeleteFileTask.execute();

			}
		});
		mDialogBuilder.setNegativeButton(R.string.cancel, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1)
			{
				// TODO Auto-generated method stub

			}
		});
		mDialogBuilder.show();
	}

	public void init3()
	{
		String stringFormate;
		AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(mContext);
		mDialogBuilder.setTitle(R.string.delete);
		if (mFileinfo != null) {

			if (!mFileinfo.isDrectory()) {
				stringFormate = mContext.getResources().getString(R.string.delete_file);

				mDialogBuilder.setMessage(String.format(stringFormate, mFileinfo.getmName()));
			}
			else {
				stringFormate = mContext.getResources().getString(R.string.delete_dir);
				mDialogBuilder.setMessage(String.format(stringFormate, mFileinfo.getmName()));
			}
		}

		mDialogBuilder.setPositiveButton(R.string.ok, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1)
			{
				// TODO Auto-generated method stub
				String parentPath = null;
				try {
					parentPath = new File(mFileinfo.getmPath()).getParentFile().getCanonicalPath();
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				RefleshCollectionFileTask mRefleshCollectionFileTask = new RefleshCollectionFileTask(mContext, mSearchBar, listView, parentPath, mFileinfo, mFileNoRecodeLayout, settingSharedPreferences, checks, 1);
				mRefleshCollectionFileTask.execute();

			}
		});
		mDialogBuilder.setNegativeButton(R.string.cancel, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1)
			{
				// TODO Auto-generated method stub

			}
		});
		mDialogBuilder.show();
	}

	private int getDirectoryNumber()
	{
		int count = 0;
		for (int i = 0; i < mList.size(); i++) {
			if (mList.get(i).isDrectory()) {
				count++;
			}
		}
		return count;
	}

	private int getFileNumber()
	{
		int count = 0;
		for (int i = 0; i < mList.size(); i++) {
			if (!mList.get(i).isDrectory()) {
				count++;
			}
		}
		return count;
	}
}
