package com.example.managerfile.ui;

import java.io.File;

import com.example.managerfile.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.managerfile.ui.operate.DialogCompressFiles;
import com.example.managerfile.ui.operate.DialogCopyFile;
import com.example.managerfile.ui.operate.DialogCutFile;
import com.example.managerfile.ui.operate.DialogDeleteFile;
import com.example.managerfile.ui.operate.DialogRenameFile;
import com.example.managerfile.ui.operate.DialogShareFile;
import com.example.managerfile.ui.operate.DialogShowFileAttribute;
import com.example.managerfile.model.Fileinfo;

public class SearchChoiceOprateDialog
{
	private Context mContext;
	String[] mMenu;
	private File mFile;
	private Fileinfo mFileinfo;
	int res;
	FileManagerActivity mFileManagerActivity;
	ShowSearchResaultActivity mShowSearchResaultActivity;

	public SearchChoiceOprateDialog(Context context, File file)
	{
		mContext = context;
		mMenu = mContext.getResources().getStringArray(R.array.menu2);
		mFile = file;

	}

	public SearchChoiceOprateDialog(Context context, Fileinfo file, FileManagerActivity fileManagerActivity)
	{
		mContext = context;
		mFileinfo = file;
		if (mFileinfo.isDrectory())
		{
			mMenu = mContext.getResources().getStringArray(R.array.menu);
		}
		else
		{
			mMenu = mContext.getResources().getStringArray(R.array.menu1);
		}
		mFileManagerActivity = fileManagerActivity;

	}

	public SearchChoiceOprateDialog(Context context, Fileinfo file, ShowSearchResaultActivity fileManagerActivity)
	{
		mContext = context;
		mFileinfo = file;
		if (mFileinfo.isDrectory())
		{
			mMenu = mContext.getResources().getStringArray(R.array.menu);
		}
		else
		{
			mMenu = mContext.getResources().getStringArray(R.array.menu2);
		}
		mShowSearchResaultActivity = fileManagerActivity;

	}

	public int initDialog()
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle(mFileinfo.getmName());

		dialog.setItems(mMenu, new DialogInterface.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dia, int which)
			{
				// TODO Auto-generated method stub
				switch (which)
				{
				case 0:
					new DialogDeleteFile(mContext, mFileinfo, mShowSearchResaultActivity).init1();
					break;
				case 1:
					new DialogShareFile(mContext, mFileinfo).init();
					break;
				case 2:
					new DialogCompressFiles(mContext, mFileinfo, mFileManagerActivity).init();
					break;
				case 3:
					new DialogRenameFile(mContext, mFileinfo, mFileManagerActivity).init();
					break;
				case 4:
					new DialogShowFileAttribute(mContext, mFileinfo).init();
					break;
				default:
					break;
				}

			}
		});

		dialog.show();
		return res;
	}
}
