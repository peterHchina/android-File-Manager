package com.example.managerfile.ui.operate;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Stack;

import com.example.managerfile.R;
import com.example.managerfile.task.GetFileListTask;
import com.example.managerfile.task.RefleshCollectionFileTask;
import com.example.managerfile.ui.FileManagerActivity;

import android.R.fraction;
import android.R.integer;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.managerfile.model.Check;
import com.example.managerfile.model.DetailCategoryFileinfo;
import com.example.managerfile.model.Fileinfo;

public class DialogRenameFile
{
	private Context mContext;
	File file;
	private String tempPath;
	private EditText filenameEditText;
	int res;
	private Fileinfo mFileinfo;
	private int fileType;
	private ListView listView;
	private LinearLayout mFileNoRecodeLayout;
	private TextView tvTitle;
	private FrameLayout mSearchBar;
	private List<DetailCategoryFileinfo> list;
	private Stack<Check> checks;
	FileManagerActivity mFileManagerActivity;
	private SharedPreferences mSharedPreferences;

	public DialogRenameFile(Context context, File file)
	{
		mContext = context;
		this.file = file;
	}

	public DialogRenameFile(Context context, Fileinfo file, FileManagerActivity fileManagerActivity)
	{
		mContext = context;
		this.mFileinfo = file;
		mFileManagerActivity = fileManagerActivity;
		this.file = new File(mFileinfo.getmPath());
	}

	public DialogRenameFile(Context context, ListView listView, List<DetailCategoryFileinfo> list, Fileinfo file, LinearLayout layout, TextView tvTitle, FrameLayout frameLayout, int fileType,
			Stack<Check> t)
	{
		mContext = context;
		mFileinfo = file;
		this.mSearchBar = frameLayout;
		this.listView = listView;
		this.list = list;
		this.file = new File(mFileinfo.getmPath());
		mFileNoRecodeLayout = layout;
		this.tvTitle = tvTitle;
		this.fileType = fileType;
		checks = t;
	}

	public DialogRenameFile(Context context, ListView listView, List<DetailCategoryFileinfo> list, Fileinfo file, LinearLayout layout, FrameLayout frameLayout,
			Stack<Check> t, SharedPreferences sharedPreferences)
	{
		mContext = context;
		mFileinfo = file;
		this.mSearchBar = frameLayout;
		this.listView = listView;
		this.list = list;
		this.file = new File(mFileinfo.getmPath());
		mFileNoRecodeLayout = layout;
		this.tvTitle = tvTitle;
		this.fileType = fileType;
		checks = t;
		mSharedPreferences = sharedPreferences;
	}

	public int init()
	{

		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle(R.string.rename);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		LinearLayout vLayout = (LinearLayout) inflater.inflate(R.layout.dialog_create_file, null);
		filenameEditText = (EditText) vLayout.findViewById(R.id.file_name);
		tempPath = new File(mFileinfo.getmPath()).getParent();
		// Log.i("tempPath:::::::::::::::::::::::::::::::::::::::::::::::::::::::",
		// tempPath);
		filenameEditText.setText(mFileinfo.getmName());
		dialog.setView(vLayout);
		dialog.setPositiveButton(R.string.ok, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1)
			{
				// TODO Auto-generated method stub
				// Log.i("file name", tempPath + "/" +
				// filenameEditText.getText().toString().trim());
				File mFile = new File(tempPath + "/" + filenameEditText.getText().toString().trim());
				if (mFile.exists()) {
					return;
				}
				else {
					file.renameTo(mFile);
					mFileManagerActivity.reflashPath(tempPath);
				}

			}
		});

		dialog.setNegativeButton(R.string.cancel, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1)
			{
				// TODO Auto-generated method stub
				res = 0;
			}
		});
		dialog.show();
		return res;
	}

	public int init1()
	{

		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle(R.string.rename);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		LinearLayout vLayout = (LinearLayout) inflater.inflate(R.layout.dialog_create_file, null);
		filenameEditText = (EditText) vLayout.findViewById(R.id.file_name);
		tempPath = new File(mFileinfo.getmPath()).getParent();
		// Log.i("tempPath:::::::::::::::::::::::::::::::::::::::::::::::::::::::",
		// tempPath);
		filenameEditText.setText(mFileinfo.getmName());
		dialog.setView(vLayout);
		dialog.setPositiveButton(R.string.ok, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1)
			{
				// TODO Auto-generated method stub
				File mFile = new File(tempPath + "/" + filenameEditText.getText().toString().trim());
				if (mFile.exists()) {
					return;
				}
				else {
					mFileinfo = new Fileinfo(mFile.getPath());
					file.renameTo(mFile);
					GetFileListTask mGetFileListTask = new GetFileListTask(listView, list, mFileinfo, mFileNoRecodeLayout, tvTitle, fileType, mSearchBar, checks, mContext, false, true);
					mGetFileListTask.execute();
				}

			}
		});

		dialog.setNegativeButton(R.string.cancel, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1)
			{
				// TODO Auto-generated method stub
				res = 0;
			}
		});
		dialog.show();
		return res;
	}

	public int init2()
	{

		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle(R.string.rename);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		LinearLayout vLayout = (LinearLayout) inflater.inflate(R.layout.dialog_create_file, null);
		filenameEditText = (EditText) vLayout.findViewById(R.id.file_name);
		tempPath = new File(mFileinfo.getmPath()).getParent();
		// Log.i("tempPath:::::::::::::::::::::::::::::::::::::::::::::::::::::::",
		filenameEditText.setText(mFileinfo.getmName());
		dialog.setView(vLayout);
		dialog.setPositiveButton(R.string.ok, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1)
			{
				// TODO Auto-generated method stub
				File mFile = new File(tempPath + "/" + filenameEditText.getText().toString().trim());
				String parentFilePath=null;
				try {
					parentFilePath = new File(mFileinfo.getmPath()).getParentFile().getCanonicalPath();
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (mFile.exists()) {
					return;
				}
				else {
					file.renameTo(mFile);
					RefleshCollectionFileTask mRefleshCollectionFileTask = new RefleshCollectionFileTask(mContext, mSearchBar, listView, parentFilePath,mFileNoRecodeLayout, mSharedPreferences, checks, 3);
					mRefleshCollectionFileTask.execute();
				}

			}
		});

		dialog.setNegativeButton(R.string.cancel, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1)
			{
				// TODO Auto-generated method stub
				res = 0;
			}
		});
		dialog.show();
		return res;
	}
}
