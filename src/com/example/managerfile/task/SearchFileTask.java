package com.example.managerfile.task;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.example.managerfile.R;
import com.example.managerfile.model.Fileinfo;
import com.example.managerfile.ui.ShowSearchResaultActivity;
import com.example.managerfile.ui.base.FileProgressDialog;
import com.example.managerfile.ui.base.FileSearchProgressDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

public class SearchFileTask extends AsyncTask<Void, String, List<Fileinfo>>
{

	private String currentPath;
	private String keyWords;
	private Context mContext;
	private ArrayList<String> mList = new ArrayList<String>();
	private ProgressDialog mDialog;
	private DialogInterface.OnCancelListener mOnCancelListener = null;
	private TextView mTextView;

	public SearchFileTask(String currentPath, String keywords, Context context)
	{

		mContext = context;
		this.currentPath = currentPath;
		this.keyWords = keywords;
		mOnCancelListener = new DialogInterface.OnCancelListener()
		{
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
		doSearchFile(currentPath, keyWords);
		return null;
	}

	private void doSearchFile(String dir, String expr)
	{

		File[] files = new File(dir).listFiles();
		if (files == null)
		{
			return;
		}
		int length = files.length;
		Log.i("length:---------------------------------", ":"+length);
		String name;
		String lowercaseName = "";
		expr = expr.toLowerCase();
		// searchDir = dir.getAbsolutePath();

		for (int i = 0; i < length; i++)
		{

			if (files[i].isHidden())
			{
				continue;
			}
			name = files[i].getName();

			lowercaseName = name.toLowerCase();

			if (lowercaseName.indexOf(expr) != -1 && !files[i].isDirectory())
			{
				mList.add(files[i].getPath());
				
			}
			if (files[i].isDirectory())
				doSearchFile(files[i].getPath(), expr);
		}

	}

	@Override
	protected void onPostExecute(List<Fileinfo> list)
	{
		if (mDialog != null)
		{
			mDialog.cancel();
		}
		Log.i("size:---------------------------------", ":"+mList.size());
		Intent intent = new Intent();
		intent.putExtra("keyword", keyWords);
		intent.putStringArrayListExtra("SearchList", mList);
		intent.setClass(mContext, ShowSearchResaultActivity.class);
		mContext.startActivity(intent);

	}

	@Override
	protected void onPreExecute()
	{

		mDialog = new FileSearchProgressDialog(mContext, mContext.getResources().getString(R.string.searching));
		mDialog.show();
		mDialog.setCancelable(true);
		// mTextView=mDialog.get
		mDialog.setOnCancelListener(mOnCancelListener);
	}

	@Override
	protected void onProgressUpdate(String... values)
	{

		// mDialog.getSerachprocessTextView.setText(values);
	}
}
