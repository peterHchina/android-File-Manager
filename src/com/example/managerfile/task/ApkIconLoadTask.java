package com.example.managerfile.task;

import java.io.File;

import com.example.managerfile.R;
import com.example.managerfile.util.OpenFile;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;
import com.example.managerfile.util.FileUtil;

public class ApkIconLoadTask extends AsyncTask<Void, Void, Drawable>
{
	Context mContext;
	private String mApkFilePath;
	private ImageView mFileIcon;

	public ApkIconLoadTask(Context context, String filepath, ImageView fileIcon)
	{
		mContext = context;
		mApkFilePath = filepath;
		mFileIcon = fileIcon;
	}

	@Override
	protected Drawable doInBackground(Void... arg0)
	{
		// TODO Auto-generated method stub
		return FileUtil.getAPKDrawable(mApkFilePath, mContext);

	}

	@Override
	protected void onPreExecute()
	{
		mFileIcon.setImageResource(R.drawable.apk_file);
	}

	@Override
	protected void onPostExecute(Drawable res)
	{
		mFileIcon.setImageDrawable(res);

	}

}
