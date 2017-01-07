package com.example.managerfile.task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import org.apache.http.auth.NTCredentials;

import com.example.managerfile.R;
import com.example.managerfile.adapter.CollectFileListAdapter;
import com.example.managerfile.adapter.GridAdapter;
import com.example.managerfile.adapter.ListAdapter;
import com.example.managerfile.listener.CollectedFileOnClickListener;
import com.example.managerfile.listener.CollectedFileOnLongClickListener;
import com.example.managerfile.model.Check;
import com.example.managerfile.model.CollectFile;
import com.example.managerfile.model.DetailCategoryFileinfo;
import com.example.managerfile.model.Fileinfo;
import com.example.managerfile.ui.FileManagerActivity;
import com.example.managerfile.ui.base.FileProgressDialog;
import com.example.managerfile.util.FileUtil;
import com.example.managerfile.util.SortByFileDate;
import com.example.managerfile.util.SortByFileSize;
import com.example.managerfile.util.SortChineseFileName;
import com.example.managerfile.util.ZipUtil;

import android.R.integer;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class RefleshCollectionFileTask extends AsyncTask<Void, Void, List<DetailCategoryFileinfo>>
{
	private File mFile;
	private String mPath;
	private Context mContext;
	FileManagerActivity mFileManagerActivity;
	private FileProgressDialog mDialog;
	private DialogInterface.OnCancelListener mOnCancelListener = null;
	private Fileinfo mFileinfo;
	private List<Fileinfo> listFileinfos = new ArrayList<Fileinfo>();
	private File[] files;
	private boolean isShowHideFile;
	private File currentParent;
	private SharedPreferences.Editor editor;
	private static final String SETTING_SORT_STYLE = "com.FileManger.sort";
	private int count = 0;
	private SharedPreferences settingPreferences;
	private FrameLayout mSearchBar;
	private ListView fileListView;
	private TextView mPathView;
	private LinearLayout mNoRecodeLayout;
	private CollectFileListAdapter mListAdapter;
	private Check position = null;
	private Stack<Check> mChecks;
	private int fileType;
	private int operate = 0;
	private String destionFilePath;
	private String sourceFilePath;

	public RefleshCollectionFileTask(Context context, FrameLayout frameLayout, ListView listView, String path, Check position, LinearLayout noFilesView, SharedPreferences mSharedPreferences,
			int fileType)
	{
		mContext = context;
		// settingPreferences = mSharedPreferences;
		mNoRecodeLayout = noFilesView;
		mSearchBar = frameLayout;
		if (mSearchBar.getVisibility() == View.VISIBLE) {
			mSearchBar.setVisibility(View.GONE);
		}

		this.fileType = fileType;
		fileListView = listView;
		mPath = path;
		this.position = position;
		files = new File(path).listFiles();
		mPathView = (TextView) mSearchBar.findViewById(R.id.file_path);
		settingPreferences = mSharedPreferences;
		count = settingPreferences.getInt(SETTING_SORT_STYLE, 1);
		isShowHideFile = settingPreferences.getBoolean(FileUtil.SETTING_HIDE_FILE, false);
		mOnCancelListener = new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface paramDialogInterface)
			{
				cancel(true);
			}
		};
	}

	public RefleshCollectionFileTask(Context context, FrameLayout frameLayout, ListView listView, LinearLayout noFilesView, SharedPreferences mSharedPreferences, Stack<Check> list)
	{
		mContext = context;
		// settingPreferences = mSharedPreferences;
		mNoRecodeLayout = noFilesView;
		mSearchBar = frameLayout;
		fileListView = listView;
		mChecks = list;
		mPathView = (TextView) mSearchBar.findViewById(R.id.file_path);
		settingPreferences = mSharedPreferences;
		count = settingPreferences.getInt(SETTING_SORT_STYLE, 1);
		isShowHideFile = settingPreferences.getBoolean(FileUtil.SETTING_HIDE_FILE, false);
		mOnCancelListener = new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface paramDialogInterface)
			{
				cancel(true);
			}
		};
	}

	public RefleshCollectionFileTask(Context context, FrameLayout frameLayout, ListView listView, String path, Fileinfo fileinfo, LinearLayout noFilesView, SharedPreferences mSharedPreferences,
			Stack<Check> list, int type)
	{
		mContext = context;
		// settingPreferences = mSharedPreferences;
		mNoRecodeLayout = noFilesView;
		mSearchBar = frameLayout;
		fileListView = listView;
		mPath = path;
		mFileinfo = fileinfo;
		mChecks = list;
		mPathView = (TextView) mSearchBar.findViewById(R.id.file_path);
		settingPreferences = mSharedPreferences;
		count = settingPreferences.getInt(SETTING_SORT_STYLE, 1);
		isShowHideFile = settingPreferences.getBoolean(FileUtil.SETTING_HIDE_FILE, false);
		operate = type;
		mOnCancelListener = new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface paramDialogInterface)
			{
				cancel(true);
			}
		};
	}

	public RefleshCollectionFileTask(Context context, FrameLayout frameLayout, ListView listView, String path, LinearLayout noFilesView, SharedPreferences mSharedPreferences, Stack<Check> list,
			int type)
	{
		mContext = context;
		// settingPreferences = mSharedPreferences;
		mNoRecodeLayout = noFilesView;
		mSearchBar = frameLayout;
		fileListView = listView;
		mPath = path;
		mFileinfo = new Fileinfo(mPath);
		mChecks = list;
		mPathView = (TextView) mSearchBar.findViewById(R.id.file_path);
		settingPreferences = mSharedPreferences;
		count = settingPreferences.getInt(SETTING_SORT_STYLE, 1);
		isShowHideFile = settingPreferences.getBoolean(FileUtil.SETTING_HIDE_FILE, false);
		operate = type;
		mOnCancelListener = new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface paramDialogInterface)
			{
				cancel(true);
			}
		};
	}

	public RefleshCollectionFileTask(Context context, FrameLayout frameLayout, ListView listView, String path, LinearLayout noFilesView, SharedPreferences mSharedPreferences, Stack<Check> list)
	{
		mContext = context;
		// settingPreferences = mSharedPreferences;
		mNoRecodeLayout = noFilesView;
		mSearchBar = frameLayout;
		fileListView = listView;
		mPath = path;
		mFileinfo = new Fileinfo(mPath);
		mChecks = list;
		mPathView = (TextView) mSearchBar.findViewById(R.id.file_path);
		settingPreferences = mSharedPreferences;
		count = settingPreferences.getInt(SETTING_SORT_STYLE, 1);
		isShowHideFile = settingPreferences.getBoolean(FileUtil.SETTING_HIDE_FILE, false);
		mOnCancelListener = new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface paramDialogInterface)
			{
				cancel(true);
			}
		};
	}

	public RefleshCollectionFileTask(Context context, FrameLayout frameLayout, ListView listView, String parentFilePath, String destionFilePath, String sourceFilePath, LinearLayout noFilesView,
			SharedPreferences mSharedPreferences, Stack<Check> list, int type)
	{
		mContext = context;
		// settingPreferences = mSharedPreferences;
		mNoRecodeLayout = noFilesView;
		mSearchBar = frameLayout;
		fileListView = listView;
		mPath = parentFilePath;
		mFileinfo = new Fileinfo(mPath);
		mChecks = list;
		this.destionFilePath = destionFilePath;
		this.sourceFilePath = sourceFilePath;
		mPathView = (TextView) mSearchBar.findViewById(R.id.file_path);
		settingPreferences = mSharedPreferences;
		count = settingPreferences.getInt(SETTING_SORT_STYLE, 1);
		isShowHideFile = settingPreferences.getBoolean(FileUtil.SETTING_HIDE_FILE, false);
		operate = type;
		mOnCancelListener = new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface paramDialogInterface)
			{
				cancel(true);
			}
		};
	}

	@Override
	protected List<DetailCategoryFileinfo> doInBackground(Void... arg0)
	{
		// TODO Auto-generated method stub
		doOpearte(operate);
		if (mPath != null) {
			files = new File(mPath).listFiles();
		}
		else {
			files = null;
		}
		if (files.length > 0 && files != null) {

			for (int i = 0; i < files.length; i++) {

				String filepath = null;
				Fileinfo fileItem;
				try {
					filepath = files[i].getCanonicalPath();
				}
				catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (!isShowHideFile && files[i].isHidden()) {

					continue;
				}
				fileItem = new Fileinfo(filepath);
				listFileinfos.add(fileItem);

			}
			if (count == 1) {
				Collections.sort(listFileinfos, new SortChineseFileName());
			}
			if (count == 2) {
				Collections.sort(listFileinfos, new SortByFileDate());
			}
			if (count == 3) {
				Collections.sort(listFileinfos, new SortByFileSize());
			}
			return reInitData(listFileinfos);
		}
		else {
			return null;
		}
	}

	@Override
	protected void onPostExecute(List<DetailCategoryFileinfo> res)
	{
		if (mDialog != null) {
			mDialog.cancel();
		}
		if (res == null) {
			mNoRecodeLayout.setVisibility(View.VISIBLE);
		}
		if (fileType == 8) {
			if (mNoRecodeLayout.getVisibility() == View.VISIBLE) {
				mNoRecodeLayout.setVisibility(View.GONE);
			}
		}
		mListAdapter = new CollectFileListAdapter(mContext, res);
		fileListView.setAdapter(mListAdapter);
		fileListView.setOnItemClickListener(new CollectedFileOnClickListener(mContext, mNoRecodeLayout, mSearchBar, fileListView, res, mChecks, settingPreferences));
		fileListView.setOnItemLongClickListener(new CollectedFileOnLongClickListener(mContext, mNoRecodeLayout, mSearchBar, fileListView, res, 9, mChecks, settingPreferences));
		if (mSearchBar.getVisibility() == View.GONE) {
			mSearchBar.setVisibility(View.VISIBLE);
		}
		if (mPath.lastIndexOf(File.separator) == -1) {
			mPath = mPath + File.separator;
		}
		mPathView.setText(mPath);
		if (position != null) {
			fileListView.setSelectionFromTop(position.getPoisition(), position.getListTop());
		}
	}

	@Override
	protected void onPreExecute()
	{
		String str = mContext.getResources().getString(R.string.loading);
		mDialog = new FileProgressDialog(mContext);
		mDialog.show();
		mDialog.setCancelable(true);
		mDialog.setOnCancelListener(mOnCancelListener);
	}

	private List<DetailCategoryFileinfo> reInitData(List<Fileinfo> list)
	{
		if (list.size() == 0 || list == null) {
			return null;
		}
		List<DetailCategoryFileinfo> deList = new ArrayList<DetailCategoryFileinfo>();
		for (int i = 0; i < list.size(); i++) {
			DetailCategoryFileinfo infCategoryFileinfo = new DetailCategoryFileinfo();
			String pathString = list.get(i).getmPath();
			infCategoryFileinfo.setPath(pathString);
			if (list.get(i).isDrectory()) {
				infCategoryFileinfo.setNumber(FileUtil.GetAfterDirectoryFileCounts(pathString));
				infCategoryFileinfo.setDrectory(true);
			}
			else {
				infCategoryFileinfo.setStroage(FileUtil.FormetFileSize(new File(list.get(i).getmPath()).length()));
			}
			infCategoryFileinfo.setTitle(list.get(i).getmName());
			infCategoryFileinfo.setDate(list.get(i).getmDate());
			deList.add(infCategoryFileinfo);

		}
		return deList;
	}

	private void doOpearte(int operate)
	{
		if (operate == 1) {
			if (!mFileinfo.isDrectory()) {
				FileUtil.deleteFile(mFileinfo.getmPath());
			}
			if (mFileinfo.isDrectory()) {
				FileUtil.deleteDirectory(mFileinfo.getmPath(), true);
			}
		}
		else if (operate == 2) {
			Collection<File> files = new ArrayList<File>();
			File resFile;
			if (sourceFilePath != null) {
				resFile = new File(sourceFilePath);
				if (resFile != null) {
					files.add(resFile);
				}
			}
			File zipFile;
			zipFile = new File(mPath + File.separator + destionFilePath);
			try {
				ZipUtil.zipFiles(files, zipFile);
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (operate == 3) {

		}
	}
}
