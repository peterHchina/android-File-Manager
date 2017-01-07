package com.example.managerfile.task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

public class CopyListFileTask extends AsyncTask<Void, Void, Boolean>
{

	private Context mContext;
	private Fileinfo sourceFileinfo;
	private Fileinfo desFileinfo;

	private ProgressDialog mDialog;
	private DialogInterface.OnCancelListener mOnCancelListener = null;
	private FileManagerActivity mFileManagerActivity;
	private List<Fileinfo> mFileinfos=new  ArrayList<Fileinfo>();

	public CopyListFileTask(Context context, List<Fileinfo> fileinfos, Fileinfo desFileinfo, FileManagerActivity fileManagerActivity)
	{
		mContext = context;

		this.desFileinfo = desFileinfo;
		mFileManagerActivity = fileManagerActivity;
		
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
		int count = mFileManagerActivity.mFileinfos.size();
		for (int i = 0; i < count; i++)
		{
			if (!mFileManagerActivity.mFileinfos.get(i).isCut())
			{

				if (mFileManagerActivity.mFileinfos.get(i).isDrectory())
				{
					try
					{
						FileUtil.copyDirectiory(mFileManagerActivity.mFileinfos.get(i).getmPath(), desFileinfo.getmPath());
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
						FileUtil.copyFile(mFileManagerActivity.mFileinfos.get(i).getmPath(), desFileinfo.getmPath());

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
				if (mFileManagerActivity.mFileinfos.get(i).isDrectory())
				{
					try
					{
						FileUtil.copyDirectiory(mFileManagerActivity.mFileinfos.get(i).getmPath(), desFileinfo.getmPath());
						FileUtil.deleteDirectory(mFileManagerActivity.mFileinfos.get(i).getmPath(), true);
						mFileinfos.add(mFileManagerActivity.mFileinfos.get(i));
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
						FileUtil.copyFile(mFileManagerActivity.mFileinfos.get(i).getmPath(), desFileinfo.getmPath());
						FileUtil.deleteFile(mFileManagerActivity.mFileinfos.get(i).getmPath());
						mFileinfos.add(mFileManagerActivity.mFileinfos.get(i));
					}
					catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}

		}
		if (mFileinfos != null && mFileinfos.size() != 0)
		{

			for (int j = 0; j < mFileinfos.size(); j++)
			{
				mFileManagerActivity.mFileinfos.remove(mFileinfos.get(j));
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

			mFileManagerActivity.adapter.NofityAll();

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
