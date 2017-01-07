package com.example.managerfile.task;

import java.io.File;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.example.managerfile.R;
import com.example.managerfile.R.color;
import com.example.managerfile.ui.FileManagerActivity;
import com.example.managerfile.ui.ShowSearchResaultActivity;
import com.example.managerfile.util.FileUtil;
import com.example.managerfile.model.Fileinfo;

public class DeleteSearchFileTask extends AsyncTask<Void, Void, Boolean>
{
	private File mFile;
	private String mPath;
	private Context mContext;
	ShowSearchResaultActivity mShowSearchResaultActivity = null;
	private ProgressDialog mDialog;
	private DialogInterface.OnCancelListener mOnCancelListener = null;
	private Fileinfo mFileinfo;

	public DeleteSearchFileTask(Fileinfo file, ShowSearchResaultActivity searchResaultActivity, Context context)
	{
		mFileinfo = file;
		mShowSearchResaultActivity = searchResaultActivity;
		mContext = context;
		mOnCancelListener = new DialogInterface.OnCancelListener()
		{
			public void onCancel(DialogInterface paramDialogInterface)
			{
				cancel(true);
			}
		};
	}

	public DeleteSearchFileTask(Fileinfo file, Context context)
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
		else
		{
			FileUtil.deleteDirectory(mPath, true);
		}
		mShowSearchResaultActivity.listFileinfos.remove(mFileinfo);
		return null;
	}

	@Override
	protected void onPostExecute(Boolean res)
	{
		if (mDialog != null)
		{
			mDialog.cancel();
		}
		if (mShowSearchResaultActivity != null)
		{
			mShowSearchResaultActivity.searchListAdapter.NofityAll();
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
