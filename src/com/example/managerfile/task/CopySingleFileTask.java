package com.example.managerfile.task;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.example.managerfile.R;
import com.example.managerfile.model.Fileinfo;
import com.example.managerfile.ui.FileManagerActivity;
import com.example.managerfile.util.FileUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;

public class CopySingleFileTask extends AsyncTask<Void, Void, Boolean>
{

	private Context mContext;
	private Fileinfo sourceFileinfo;
	private Fileinfo desFileinfo;

	private ProgressDialog mDialog;
	private DialogInterface.OnCancelListener mOnCancelListener = null;
	private FileManagerActivity mFileManagerActivity;
	private List<Fileinfo> mFileinfos;

	public CopySingleFileTask(Context context, Fileinfo fileinfos, Fileinfo desFileinfo, FileManagerActivity fileManagerActivity)
	{
		mContext = context;

		this.desFileinfo = desFileinfo;
		mFileManagerActivity = fileManagerActivity;
		sourceFileinfo = fileinfos;
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
		if (!sourceFileinfo.isCut())
		{

			if (sourceFileinfo.isDrectory())
			{
				try
				{
					FileUtil.copyDirectiory(sourceFileinfo.getmPath(), desFileinfo.getmPath());

				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				try
				{
					FileUtil.copyFile(sourceFileinfo.getmPath(), desFileinfo.getmPath());

				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
		else
		{

			if (sourceFileinfo.isDrectory())
			{
				try
				{
					FileUtil.copyDirectiory(sourceFileinfo.getmPath(), desFileinfo.getmPath());
					FileUtil.deleteDirectory(sourceFileinfo.getmPath(), true);
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				try
				{
					FileUtil.copyFile(sourceFileinfo.getmPath(), desFileinfo.getmPath());
					FileUtil.deleteFile(sourceFileinfo.getmPath());
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			if (mFileManagerActivity.mFileinfos.size() != 0)
			{
				mFileManagerActivity.mFileinfos.remove(sourceFileinfo);
				mFileManagerActivity.adapter.NofityAll();

			}
		}
		return true;
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
			mFileManagerActivity.reflashPath(desFileinfo.getmPath());

		}

	}

	@Override
	protected void onPreExecute()
	{
		String str = mContext.getResources().getString(R.string.copying);
		mDialog = ProgressDialog.show(mContext, null, str);
		mDialog.setCancelable(true);
		mDialog.setOnCancelListener(mOnCancelListener);
	}
}
