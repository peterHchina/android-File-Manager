package com.example.managerfile.ui.operate;

import java.io.File;

import com.example.managerfile.model.Fileinfo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class DialogShareFile
{

	private Context mContext;
	private Fileinfo mFileinfo;

	public DialogShareFile(Context context, Fileinfo f)
	{
		mContext = context;
		mFileinfo = f;
	}

	public void init()
	{
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(mFileinfo.getmPath())));
		intent.setType("*/*");
		mContext.startActivity(intent);

	}
}
