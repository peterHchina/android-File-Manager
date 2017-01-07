package com.example.managerfile.task;

import java.io.File;
import java.io.IOException;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;

import com.example.managerfile.R;
import com.example.managerfile.R.color;
import com.example.managerfile.ui.FileManagerActivity;
import com.example.managerfile.util.FileUtil;
import com.example.managerfile.model.Fileinfo;

public class DeleteFileTask extends AsyncTask<Void, Void, Boolean>
{
	private File mFile;
	private String mPath;
	private Context mContext;
	FileManagerActivity mFileManagerActivity = null;
	private ProgressDialog mDialog;
	private DialogInterface.OnCancelListener mOnCancelListener = null;
	private Fileinfo mFileinfo;
	private List<Fileinfo> mList = null;

	public DeleteFileTask(Fileinfo file, FileManagerActivity fileManagerActivity, Context context)
	{
		mFileinfo = file;
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

	public DeleteFileTask(List<Fileinfo> file, FileManagerActivity fileManagerActivity, Context context)
	{
		mList = file;
		mFileinfo = mList.get(0);
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

	public DeleteFileTask(Fileinfo file, Context context)
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

		mPath = mFileinfo.getmPath();
		if (!mFileinfo.isDrectory())
		{
			FileUtil.deleteFile(mPath);
		}
		if (mFileinfo.isDrectory())
		{
			FileUtil.deleteDirectory(mPath, true);
		}

		if (mList != null && mList.size() != 0)
		{
			for (int i = 0; i < mList.size(); i++)
			{
				if (mList.get(i).isDrectory())
				{
					FileUtil.deleteDirectory(mList.get(i).getmPath(), true);
				}
				else
				{
					FileUtil.deleteFile(mList.get(i).getmPath());
				}
			}
		}

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
			mFileManagerActivity.mOpearte.setVisibility(View.GONE);
			String pathString=null;
			try {
				pathString = new File(mPath).getParentFile().getCanonicalPath();
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mFileManagerActivity.reflashPath(pathString);
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
