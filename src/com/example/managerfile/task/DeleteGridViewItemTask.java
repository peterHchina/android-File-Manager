package com.example.managerfile.task;

import java.io.File;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.example.managerfile.R;
import com.example.managerfile.R.color;
import com.example.managerfile.ui.FileManagerActivity;
import com.example.managerfile.util.FileUtil;
import com.example.managerfile.adapter.PasteGridAdapter;
import com.example.managerfile.model.Fileinfo;

public class DeleteGridViewItemTask extends AsyncTask<Void, Void, Boolean>
{
	private File mFile;
	private String mPath;
	private int position;
	private Context mContext;
	FileManagerActivity mFileManagerActivity = null;
	private ProgressDialog mDialog;
	private DialogInterface.OnCancelListener mOnCancelListener = null;
	private Fileinfo mFileinfo;

	public DeleteGridViewItemTask(int id, FileManagerActivity fileManagerActivity, Context context)
	{
		position = id;
		mFileManagerActivity = fileManagerActivity;
		mContext = context;
		mOnCancelListener = new DialogInterface.OnCancelListener()
		{
			public void onCancel(DialogInterface paramDialogInterface)
			{
				cancel(true);
			}
		};
	}

	public DeleteGridViewItemTask(Fileinfo file, Context context)
	{
		mFileinfo = file;
		mContext = context;
		mOnCancelListener = new DialogInterface.OnCancelListener()
		{
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

		mFileManagerActivity.mFileinfos.remove(position);

		return null;
	}

	@Override
	protected void onPostExecute(Boolean res)
	{
		if (mDialog != null)
		{
			mDialog.cancel();
		}
		if (mFileManagerActivity != null)
		{
			mFileManagerActivity.mPasteGridView.setAdapter(new PasteGridAdapter(mContext, mFileManagerActivity.mFileinfos));
		}

	}

	@Override
	protected void onPreExecute()
	{
		String str = mContext.getResources().getString(R.string.deleteng);
		mDialog = ProgressDialog.show(mContext, null, str);
		mDialog.setCancelable(true);
		mDialog.setOnCancelListener(mOnCancelListener);
	}

}
