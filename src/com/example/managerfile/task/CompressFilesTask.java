package com.example.managerfile.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.zip.ZipOutputStream;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.example.managerfile.R;
import com.example.managerfile.util.CompressFilesUtil;
import com.example.managerfile.util.ZipUtil;
import com.example.managerfile.ui.FileManagerActivity;
import com.example.managerfile.util.FileUtil;
import com.example.managerfile.model.Fileinfo;

public class CompressFilesTask extends AsyncTask<Void, Void, Boolean>
{
	private File mFile;
	private String mPath;
	private Context mContext;
	FileManagerActivity mFileManagerActivity = null;
	private ProgressDialog mDialog;
	private DialogInterface.OnCancelListener mOnCancelListener = null;
	private Fileinfo mFileinfo;
	private String compressName;
	private List<Fileinfo> mList = null;

	public CompressFilesTask(Fileinfo file, FileManagerActivity fileManagerActivity, Context context, String compress)
	{
		mFileinfo = file;
		mFileManagerActivity = fileManagerActivity;
		mContext = context;
		compressName = compress;
		mOnCancelListener = new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface paramDialogInterface)
			{
				cancel(true);
			}
		};
	}

	public CompressFilesTask(List<Fileinfo> file, FileManagerActivity fileManagerActivity, Context context, String compress)
	{
		mList = file;
		mFileManagerActivity = fileManagerActivity;
		mContext = context;
		compressName = compress;
		mOnCancelListener = new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface paramDialogInterface)
			{
				cancel(true);
			}
		};
	}

	public CompressFilesTask(Fileinfo file, Context context, String compress)
	{
		mFileinfo = file;
		mContext = context;
		compressName = compress;
		mOnCancelListener = new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface paramDialogInterface)
			{
				cancel(true);
			}
		};
	}

	@Override
	protected Boolean doInBackground(Void... arg0)
	{
		// TODO Auto-generated method stub
		Collection<File> files = new ArrayList<File>();
		File resFile;
		if (mList != null && mList.size() != 0) {
			mPath = mList.get(0).getmPath();
			files = getFileList();
			resFile = new File(mPath);
		}
		else {
			mPath = mFileinfo.getmPath();
			resFile = new File(mPath);
			if (resFile != null) {
				files.add(resFile);
			}
		}

		File zipFile;
		zipFile = new File(resFile.getParentFile().getPath() + File.separator + compressName);
		try {
			ZipUtil.zipFiles(files, zipFile);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	@Override
	protected void onPostExecute(Boolean res)
	{
		if (mDialog != null) {
			mDialog.cancel();
		}
		if (mFileManagerActivity != null) {
			mFileManagerActivity.mOpearte.setVisibility(View.GONE);
			mFileManagerActivity.reflashPath(new File(mPath).getParent());
			if (mFileManagerActivity.isEdit) {
				mFileManagerActivity.isEdit = !mFileManagerActivity.isEdit;
			}
		}
		Toast.makeText(mContext, R.string.compress_success, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onPreExecute()
	{
		String str = mContext.getResources().getString(R.string.compresseng);
		mDialog = ProgressDialog.show(mContext, null, str);
		mDialog.setCancelable(true);
		mDialog.setOnCancelListener(mOnCancelListener);
	}

	private List<File> getFileList()
	{
		List<File> list = new ArrayList<File>();
		if (mList == null || mList.size() == 0) {
			return null;
		}
		for (int i = 0; i < mList.size(); i++) {
			list.add(new File(mList.get(i).getmPath()));
		}
		return list;
	}

}
