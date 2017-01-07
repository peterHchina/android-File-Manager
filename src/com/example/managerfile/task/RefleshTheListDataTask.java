package com.example.managerfile.task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.managerfile.R;
import com.example.managerfile.adapter.GridAdapter;
import com.example.managerfile.adapter.ListAdapter;
import com.example.managerfile.model.Fileinfo;
import com.example.managerfile.ui.FileManagerActivity;
import com.example.managerfile.ui.base.FileProgressDialog;
import com.example.managerfile.util.FileUtil;
import com.example.managerfile.util.SortByFileDate;
import com.example.managerfile.util.SortByFileSize;
import com.example.managerfile.util.SortChineseFileName;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

public class RefleshTheListDataTask extends AsyncTask<Void, Void, List<Fileinfo>>
{
	private File mFile;
	private String mPath;
	private Context mContext;
	FileManagerActivity mFileManagerActivity;
	private FileProgressDialog mDialog;
	private DialogInterface.OnCancelListener mOnCancelListener = null;
	private Fileinfo mFileinfo;
	private List<Fileinfo> listFileinfos = new ArrayList<Fileinfo>();
	private File[] files;
	private boolean isShowHideFile;
	private File currentParent;
	private SharedPreferences.Editor editor;
	private static final String SETTING_SORT_STYLE = "com.FileManger.sort";
	private int count = 0;
	private SharedPreferences settingPreferences;

	public RefleshTheListDataTask(List<Fileinfo> listFileinfos, File[] files, File currentParent, Boolean isShowHideFile, FileManagerActivity fileManagerActivity, Context context,
			SharedPreferences mSharedPreferences)
	{
		this.listFileinfos = listFileinfos;
		mFileManagerActivity = fileManagerActivity;
		mContext = context;
		this.files = files;
		this.isShowHideFile = isShowHideFile;
		this.currentParent = currentParent;
		settingPreferences = mSharedPreferences;
		count = settingPreferences.getInt(SETTING_SORT_STYLE, 1);
		mOnCancelListener = new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface paramDialogInterface)
			{
				cancel(true);
			}
		};
	}

	@Override
	protected List<Fileinfo> doInBackground(Void... arg0)
	{
		// TODO Auto-generated method stub

		for (int i = 0; i < files.length; i++) {

			String filepath = null;
			Fileinfo fileItem;
			try {
				filepath = currentParent.getCanonicalPath().toString() +File.separator + files[i].getName();
			}

			catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (!isShowHideFile && files[i].isHidden()) {

				continue;

			}
			fileItem = new Fileinfo(filepath);
			listFileinfos.add(fileItem);

		}
		if (count == 1) {
			Collections.sort(listFileinfos, new SortChineseFileName());
		}
		if (count == 2) {
			Collections.sort(listFileinfos, new SortByFileDate());
		}
		if (count == 3) {
			Collections.sort(listFileinfos, new SortByFileSize());
		}
		return listFileinfos;
	}

	@Override
	protected void onPostExecute(List<Fileinfo> res)
	{
		if (mDialog != null) {
			mDialog.cancel();
		}
		mFileManagerActivity.listFileinfos = res;
		for (int i = 0; i < mFileManagerActivity.listFileinfos.size(); i++) {
			Log.e(mFileManagerActivity.listFileinfos.get(i).getmName()+"======================================",mFileManagerActivity.listFileinfos.get(i).getmPath());
		}
		mFileManagerActivity.mListAdapter = new ListAdapter(mContext, mFileManagerActivity.listFileinfos);
		mFileManagerActivity.mListAdapter.NofityAll();
		mFileManagerActivity.mListView.setAdapter(mFileManagerActivity.mListAdapter);
		mFileManagerActivity.mListView.setSelectionFromTop(mFileManagerActivity.memoryCount.getListPosition(), mFileManagerActivity.memoryCount.getListTop());
		mFileManagerActivity.mGridAdapter = new GridAdapter(mContext, mFileManagerActivity.listFileinfos);
		mFileManagerActivity.mGridView.setAdapter(mFileManagerActivity.mGridAdapter);
		mFileManagerActivity.mGridView.setSelection(mFileManagerActivity.memoryCount.getListPosition());

	}

	@Override
	protected void onPreExecute()
	{
		String str = mContext.getResources().getString(R.string.deleteng);
		mDialog = new FileProgressDialog(mContext,str);
		mDialog.show();
		mDialog.setCancelable(true);
		mDialog.setOnCancelListener(mOnCancelListener);
	}

}
