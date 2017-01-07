package com.example.managerfile.ui;

import java.io.File;

import com.example.managerfile.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.example.managerfile.ui.operate.DialogCompressFiles;
import com.example.managerfile.ui.operate.DialogCopyFile;
import com.example.managerfile.ui.operate.DialogCutFile;
import com.example.managerfile.ui.operate.DialogDeleteFile;
import com.example.managerfile.ui.operate.DialogRenameFile;
import com.example.managerfile.ui.operate.DialogShareFile;
import com.example.managerfile.ui.operate.DialogShowFileAttribute;
import com.example.managerfile.model.Fileinfo;
import com.example.managerfile.provider.common.CollectFileDao;

public class ChoiceOprateDialog
{
	private Context mContext;
	String[] mMenu;
	private File mFile;
	private Fileinfo mFileinfo;
	int res;
	CollectFileDao collectFileDao;
	FileManagerActivity mFileManagerActivity;
	ShowSearchResaultActivity mShowSearchResaultActivity;

	public ChoiceOprateDialog(Context context, File file)
	{
		mContext = context;
		mMenu = mContext.getResources().getStringArray(R.array.menu);
		mFile = file;
		collectFileDao=new CollectFileDao(mContext);

	}

	public ChoiceOprateDialog(Context context, Fileinfo file, FileManagerActivity fileManagerActivity)
	{
		mContext = context;
		mFileinfo = file;
		collectFileDao=new CollectFileDao(mContext);
		if (mFileinfo.isDrectory()) {
			mMenu = mContext.getResources().getStringArray(R.array.menu);
		}
		else {
			mMenu = mContext.getResources().getStringArray(R.array.menu1);
		}
		mFileManagerActivity = fileManagerActivity;

	}

	public ChoiceOprateDialog(Context context, Fileinfo file, ShowSearchResaultActivity fileManagerActivity)
	{
		mContext = context;
		mFileinfo = file;
		collectFileDao=new CollectFileDao(mContext);
		if (mFileinfo.isDrectory()) {
			mMenu = mContext.getResources().getStringArray(R.array.menu);
		}
		else {
			mMenu = mContext.getResources().getStringArray(R.array.menu1);
		}
		mShowSearchResaultActivity = fileManagerActivity;

	}

	public int initDialog()
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle(mFileinfo.getmName());
		if (mFileinfo.isDrectory()) {
			dialog.setItems(mMenu, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dia, int which)
				{
					// TODO Auto-generated method stub
					switch (which)
					{
					case 0:
						new DialogCopyFile(mContext, mFileinfo, mFileManagerActivity).init();
						break;
					case 1:
						new DialogDeleteFile(mContext, mFileinfo, mFileManagerActivity).init();
						break;
					case 2:
						mFileinfo.setCut(true);
						new DialogCutFile(mContext, mFileinfo, mFileManagerActivity).init();
						break;
					case 3:
						new DialogCompressFiles(mContext, mFileinfo, mFileManagerActivity).init();
						break;
					case 4:
						new DialogRenameFile(mContext, mFileinfo, mFileManagerActivity).init();
						break;
					case 5:
						collectFile(mFileinfo);
						break;
					case 6:
						new DialogShowFileAttribute(mContext, mFileinfo).init();
						break;
					default:
						break;
					}

				}
			});
		}
		else {
			dialog.setItems(mMenu, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dia, int which)
				{
					// TODO Auto-generated method stub
					switch (which)
					{
					case 0:
						new DialogCopyFile(mContext, mFileinfo, mFileManagerActivity).init();
						break;
					case 1:
						new DialogDeleteFile(mContext, mFileinfo, mFileManagerActivity).init();
						break;
					case 2:
						mFileinfo.setCut(true);
						new DialogCutFile(mContext, mFileinfo, mFileManagerActivity).init();
						break;
					case 3:
						new DialogShareFile(mContext, mFileinfo).init();
						break;
					case 4:
						new DialogCompressFiles(mContext, mFileinfo, mFileManagerActivity).init();
						break;
					case 5:
						new DialogRenameFile(mContext, mFileinfo, mFileManagerActivity).init();
						break;
					case 6:
						collectFile(mFileinfo);
						break;
					case 7:
						new DialogShowFileAttribute(mContext, mFileinfo).init();
						break;
					default:
						break;
					}

				}
			});
		}

		dialog.show();
		return res;
	}

	public void collectFile(Fileinfo fileinfo)
	{
     if (collectFileDao.add(fileinfo.getmPath(),fileinfo.getmName())!=-1) {
		Toast.makeText(mContext, R.string.collect_success, Toast.LENGTH_SHORT).show();
	}  else {
		Toast.makeText(mContext, R.string.collect_fail, Toast.LENGTH_SHORT).show();
	}
	}
}
