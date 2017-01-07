package com.example.managerfile.ui.operate;

import com.example.managerfile.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;
import com.example.managerfile.ui.FileManagerActivity;

public class DialogCloseApp
{

	Context mContext;

	FileManagerActivity fileManagerActivity;
	public DialogCloseApp(Context context)
	{
		mContext = context;
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle(mContext.getResources().getString(R.string.close_app));
		dialog.setOnCancelListener(new OnCancelListener()
		{

			@Override
			public void onCancel(DialogInterface arg0)
			{
				// TODO Auto-generated method stub

			}
		});
		dialog.setOnKeyListener(new OnKeyListener()
		{

			@Override
			public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2)
			{
				// TODO Auto-generated method stub
                fileManagerActivity.finish();
				return false;
			}
		});

		dialog.show();
	}

}
