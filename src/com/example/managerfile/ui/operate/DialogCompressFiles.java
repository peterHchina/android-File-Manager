package com.example.managerfile.ui.operate;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Stack;

import com.example.managerfile.model.Check;
import com.example.managerfile.model.DetailCategoryFileinfo;
import com.example.managerfile.model.Fileinfo;
import com.example.managerfile.task.CompressFilesTask;
import com.example.managerfile.task.RefleshCollectionFileTask;
import com.example.managerfile.ui.FileManagerActivity;
import com.example.managerfile.util.FileUtil;

import com.example.managerfile.R;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class DialogCompressFiles
{
	Context mContext;
	Fileinfo mFileinfo = null;
	FileManagerActivity mFileManagerActivity;
	String extenString;
	private CompressFilesTask mCompressFilesTask;
	private EditText filenameEditText;
	List<Fileinfo> mList = null;
	private ListView listView;
	private LinearLayout mFileNoRecodeLayout;
	private TextView tvTitle;
	private FrameLayout mSearchBar;
	private List<DetailCategoryFileinfo> list;
	private Stack<Check> checks;
	private SharedPreferences settingSharedPreferences;
	private String mPath;

	public DialogCompressFiles(Context context, Fileinfo fileinfo, FileManagerActivity fileManagerActivity)
	{
		mContext = context;
		mFileinfo = fileinfo;
		mFileManagerActivity = fileManagerActivity;
	}

	public DialogCompressFiles(Context context, List<Fileinfo> fileinfo, FileManagerActivity fileManagerActivity)
	{
		mContext = context;
		mList = fileinfo;
		mFileManagerActivity = fileManagerActivity;
	}

	public DialogCompressFiles(Context context, Fileinfo fileinfo)
	{
		mContext = context;
		mFileinfo = fileinfo;
	}

	public DialogCompressFiles(Context context, FrameLayout frameLayout, List<DetailCategoryFileinfo> list, ListView listView, String path, LinearLayout noFilesView,
			SharedPreferences mSharedPreferences, Stack<Check> t)
	{
		mContext = context;
		// settingPreferences = mSharedPreferences;
		mFileNoRecodeLayout = noFilesView;
		mSearchBar = frameLayout;
		this.listView = listView;
		mPath = path;
		mFileinfo = new Fileinfo(mPath);
		this.list = list;
		settingSharedPreferences = mSharedPreferences;
		checks = t;
	}

	public void init()
	{
		extenString = mContext.getResources().getString(R.string.will_be_compress);
		AlertDialog.Builder mDialog = new AlertDialog.Builder(mContext);
		mDialog.setTitle(R.string.compress);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		LinearLayout vLayout = (LinearLayout) inflater.inflate(R.layout.dialog_create_file, null);
		filenameEditText = (EditText) vLayout.findViewById(R.id.file_name);
		mDialog.setView(vLayout);
		if (mFileinfo != null) {

			if (!mFileinfo.isDrectory()) {
				filenameEditText.setText(mFileinfo.getmName().substring(0, mFileinfo.getmName().lastIndexOf(".")) + ".zip");
				// mDialog.setMessage(mFileinfo.getmName() + "\n" + extenString
				// +
				// "\n" + mFileinfo.getmName().substring(0,
				// mFileinfo.getmName().lastIndexOf(".")) + ".zip");
			}
			else {
				filenameEditText.setText(mFileinfo.getmName() + ".zip");
				// mDialog.setMessage(mFileinfo.getmName() + "\n" + extenString
				// +
				// "\n" + mFileinfo.getmName() + ".zip");

			}
		}
		else {
			if (mList.size() == 1) {
				mFileinfo = mList.get(0);
				if (!mFileinfo.isDrectory()) {
					filenameEditText.setText(mFileinfo.getmName().substring(0, mFileinfo.getmName().lastIndexOf(".")) + ".zip");
					// mDialog.setMessage(mFileinfo.getmName() + "\n" +
					// extenString
					// +
					// "\n" + mFileinfo.getmName().substring(0,
					// mFileinfo.getmName().lastIndexOf(".")) + ".zip");
				}
				else {
					filenameEditText.setText(mFileinfo.getmName() + ".zip");
					// mDialog.setMessage(mFileinfo.getmName() + "\n" +
					// extenString
					// +
					// "\n" + mFileinfo.getmName() + ".zip");

				}
			}
			else {
				filenameEditText.setText(mContext.getResources().getString(R.string.new_compress) + ".zip");
			}

		}
		mDialog.setPositiveButton(R.string.ok, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1)
			{
				// TODO Auto-generated method stub
				if (mFileinfo != null) {
					mCompressFilesTask = new CompressFilesTask(mFileinfo, mFileManagerActivity, mContext, filenameEditText.getText().toString().trim());
					mCompressFilesTask.execute();
				}
				if (mList != null) {
					mCompressFilesTask = new CompressFilesTask(mList, mFileManagerActivity, mContext, filenameEditText.getText().toString().trim());
					mCompressFilesTask.execute();
				}
			}
		});

		mDialog.setNegativeButton(R.string.cancel, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1)
			{
				// TODO Auto-generated method stub

			}
		});
		mDialog.show();
	}

	public void init1()
	{
		extenString = mContext.getResources().getString(R.string.will_be_compress);
		AlertDialog.Builder mDialog = new AlertDialog.Builder(mContext);
		mDialog.setTitle(R.string.compress);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		LinearLayout vLayout = (LinearLayout) inflater.inflate(R.layout.dialog_create_file, null);
		filenameEditText = (EditText) vLayout.findViewById(R.id.file_name);
		mDialog.setView(vLayout);
		if (!mFileinfo.isDrectory()) {
			filenameEditText.setText(mFileinfo.getmName().substring(0, mFileinfo.getmName().lastIndexOf(".")) + ".zip");
			// mDialog.setMessage(mFileinfo.getmName() + "\n" + extenString +
			// "\n" + mFileinfo.getmName().substring(0,
			// mFileinfo.getmName().lastIndexOf(".")) + ".zip");
		}
		else {
			filenameEditText.setText(mFileinfo.getmName() + ".zip");
			// mDialog.setMessage(mFileinfo.getmName() + "\n" + extenString +
			// "\n" + mFileinfo.getmName() + ".zip");

		}
		mDialog.setPositiveButton(R.string.ok, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1)
			{
				// TODO Auto-generated method stub
				mCompressFilesTask = new CompressFilesTask(mFileinfo, mFileManagerActivity, mContext, filenameEditText.getText().toString().trim());
				mCompressFilesTask.execute();
			}
		});

		mDialog.setNegativeButton(R.string.cancel, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1)
			{
				// TODO Auto-generated method stub

			}
		});
		mDialog.show();
	}

	public void init2()
	{
		extenString = mContext.getResources().getString(R.string.will_be_compress);
		AlertDialog.Builder mDialog = new AlertDialog.Builder(mContext);
		mDialog.setTitle(R.string.compress);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		LinearLayout vLayout = (LinearLayout) inflater.inflate(R.layout.dialog_create_file, null);
		filenameEditText = (EditText) vLayout.findViewById(R.id.file_name);
		mDialog.setView(vLayout);
		if (!mFileinfo.isDrectory()) {
			filenameEditText.setText(mFileinfo.getmName().substring(0, mFileinfo.getmName().lastIndexOf(".")) + ".zip");
			// mDialog.setMessage(mFileinfo.getmName() + "\n" + extenString +
			// "\n" + mFileinfo.getmName().substring(0,
			// mFileinfo.getmName().lastIndexOf(".")) + ".zip");
		}
		else {
			filenameEditText.setText(mFileinfo.getmName() + ".zip");
			// mDialog.setMessage(mFileinfo.getmName() + "\n" + extenString +
			// "\n" + mFileinfo.getmName() + ".zip");

		}
		mDialog.setPositiveButton(R.string.ok, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1)
			{
				// TODO Auto-generated method stub
				mCompressFilesTask = new CompressFilesTask(mFileinfo, mContext, filenameEditText.getText().toString().trim());
				mCompressFilesTask.execute();
			}
		});

		mDialog.setNegativeButton(R.string.cancel, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1)
			{
				// TODO Auto-generated method stub

			}
		});
		mDialog.show();
	}

	public void init3()
	{
		extenString = mContext.getResources().getString(R.string.will_be_compress);
		AlertDialog.Builder mDialog = new AlertDialog.Builder(mContext);
		mDialog.setTitle(R.string.compress);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		LinearLayout vLayout = (LinearLayout) inflater.inflate(R.layout.dialog_create_file, null);
		filenameEditText = (EditText) vLayout.findViewById(R.id.file_name);
		mDialog.setView(vLayout);
		if (!mFileinfo.isDrectory()) {
			filenameEditText.setText(mFileinfo.getmName().substring(0, mFileinfo.getmName().lastIndexOf(".")) + ".zip");
			// mDialog.setMessage(mFileinfo.getmName() + "\n" + extenString +
			// "\n" + mFileinfo.getmName().substring(0,
			// mFileinfo.getmName().lastIndexOf(".")) + ".zip");
		}
		else {
			filenameEditText.setText(mFileinfo.getmName() + ".zip");
			// mDialog.setMessage(mFileinfo.getmName() + "\n" + extenString +
			// "\n" + mFileinfo.getmName() + ".zip");

		}
		mDialog.setPositiveButton(R.string.ok, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1)
			{
				// TODO Auto-generated method stub
				String parentPath = null;
				try {
					parentPath = new File(mPath).getParentFile().getCanonicalPath();
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				RefleshCollectionFileTask mRefleshCollectionFileTask = new RefleshCollectionFileTask(mContext, mSearchBar, listView, parentPath,  filenameEditText.getText().toString().trim(),mPath, mFileNoRecodeLayout, settingSharedPreferences, checks, 2);
				mRefleshCollectionFileTask.execute();
			}
		});

		mDialog.setNegativeButton(R.string.cancel, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1)
			{
				// TODO Auto-generated method stub

			}
		});
		mDialog.show();
	}
}
