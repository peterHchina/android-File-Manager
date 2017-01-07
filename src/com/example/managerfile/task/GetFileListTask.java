package com.example.managerfile.task;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import com.example.managerfile.R;
import com.example.managerfile.adapter.CollectFileListAdapter;
import com.example.managerfile.adapter.ListAdapter;
import com.example.managerfile.adapter.NormalFileListAdapter;
import com.example.managerfile.listener.CollectedFileOnClickListener;
import com.example.managerfile.listener.CollectedFileOnLongClickListener;
import com.example.managerfile.model.Check;
import com.example.managerfile.model.DetailCategoryFileinfo;
import com.example.managerfile.model.Fileinfo;
import com.example.managerfile.model.Videoinfo;
import com.example.managerfile.provider.common.CollectFileDao;
import com.example.managerfile.ui.ShowSearchResaultActivity;
import com.example.managerfile.ui.base.FileProgressDialog;
import com.example.managerfile.ui.base.FileSearchProgressDialog;
import com.example.managerfile.util.AllFileUtil;
import com.example.managerfile.util.OpenFile;
import com.example.managerfile.util.AllFileUtil.FileCategory;
import com.example.managerfile.util.FileUtil;

import android.R.integer;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class GetFileListTask extends AsyncTask<Void, String, List<DetailCategoryFileinfo>>
{

	private NormalFileListAdapter listNormalAdapter;
	private CollectFileListAdapter listCollectionAdapter;
	private ListView fileListView;
	private int type;
	private Context mContext;
	private ArrayList<String> mList = new ArrayList<String>();
	private ProgressDialog mDialog;
	private DialogInterface.OnCancelListener mOnCancelListener = null;
	private TextView mTextView;
	private List<DetailCategoryFileinfo> detailCategoryFileinfosList;
	private LinearLayout mFileNoRecodeLayout;
	private TextView tvTitle;
	private FrameLayout mSearchBar;
	private Stack<Check> mChecklList;
	private SharedPreferences mSharedPreferences;
	private boolean isDelete = false;
	private boolean isReName = false;
	private Fileinfo mFileinfo;
	private CollectFileDao mDao;

	public GetFileListTask(ListView listView, List<DetailCategoryFileinfo> list, LinearLayout layout, TextView textView, int type, FrameLayout frameLayout, Stack<Check> list2, Context context)
	{

		mContext = context;
		this.type = type;
		tvTitle = textView;
		fileListView = listView;
		mSearchBar = frameLayout;
		mFileNoRecodeLayout = layout;
		detailCategoryFileinfosList = list;
		mChecklList = list2;
		mOnCancelListener = new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface paramDialogInterface)
			{
				cancel(true);
			}
		};
	}

	public GetFileListTask(ListView listView, List<DetailCategoryFileinfo> list, Fileinfo fileinfo, LinearLayout layout, TextView textView, int type, FrameLayout frameLayout, Stack<Check> list2,
			Context context, boolean isDelete,boolean isReName)
	{

		mContext = context;
		this.type = type;
		tvTitle = textView;
		fileListView = listView;
		mSearchBar = frameLayout;
		mFileNoRecodeLayout = layout;
		detailCategoryFileinfosList = list;
		mChecklList = list2;
		this.isDelete = isDelete;
		this.isReName=isReName;
		mFileinfo = fileinfo;
		mDao = new CollectFileDao(context);
		mOnCancelListener = new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface paramDialogInterface)
			{
				cancel(true);
			}
		};
	}

	public GetFileListTask(ListView listView, List<DetailCategoryFileinfo> list, LinearLayout layout, TextView textView, int type, FrameLayout frameLayout, Stack<Check> list2, Context context,
			SharedPreferences mPreferences)
	{

		mContext = context;
		this.type = type;
		tvTitle = textView;
		fileListView = listView;
		mSearchBar = frameLayout;
		mFileNoRecodeLayout = layout;
		detailCategoryFileinfosList = list;
		mChecklList = list2;
		mSharedPreferences = mPreferences;
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

		switch (type)
		{
		case 4:
			detailCategoryFileinfosList = AllFileUtil.getDetailCategoryInfo(mContext, FileCategory.Theme);
			break;
		case 5:
			detailCategoryFileinfosList = AllFileUtil.getDetailCategoryInfo(mContext, FileCategory.Apk);
			break;
		case 6:
			detailCategoryFileinfosList = AllFileUtil.getDetailCategoryInfo(mContext, FileCategory.Zip);
			break;
		case 7:
			detailCategoryFileinfosList = AllFileUtil.getDetailCategoryInfo(mContext, FileCategory.Doc);
			break;
		case 8:
			detailCategoryFileinfosList = deleteCollectFile();
			break;
		default:
			break;
		}
		if (detailCategoryFileinfosList != null && detailCategoryFileinfosList.size() > 0) {
			return reInitList(detailCategoryFileinfosList);
		}
		return null;
	}

	@Override
	protected void onPostExecute(List<DetailCategoryFileinfo> list)
	{
		if (mDialog != null) {
			mDialog.cancel();
		}
		setTile(type);
		if (list != null && list.size() > 0) {
			if (type == 8) {
				if (mFileNoRecodeLayout.getVisibility() == View.VISIBLE) {
					mFileNoRecodeLayout.setVisibility(View.GONE);
				}
				if (mSearchBar.getVisibility() == View.VISIBLE) {
					mSearchBar.setVisibility(View.GONE);
				}
				listCollectionAdapter = new CollectFileListAdapter(mContext, list);
				fileListView.setAdapter(listCollectionAdapter);
				fileListView.setOnItemClickListener(new CollectedFileOnClickListener(mContext, mFileNoRecodeLayout, mSearchBar, fileListView, list, mChecklList, mSharedPreferences));
				fileListView.setOnItemLongClickListener(new CollectedFileOnLongClickListener(mContext, mFileNoRecodeLayout, mSearchBar, tvTitle, fileListView, list, type, mChecklList,mSharedPreferences));
			}
			else {
				if (mSearchBar.getVisibility() == View.VISIBLE) {
					mSearchBar.setVisibility(View.GONE);
				}
				listNormalAdapter = new NormalFileListAdapter(mContext, list);
				fileListView.setAdapter(listNormalAdapter);
				fileListView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long id)
					{
						// TODO Auto-generated method stub
						OpenFile.openFile(new File(detailCategoryFileinfosList.get(position).getPath()), mContext);
					}
				});
			}
		}
		else {
			setEmptyDetailFilecView(type);
		}
	}

	@Override
	protected void onPreExecute()
	{

		mDialog = new FileProgressDialog(mContext, mContext.getResources().getString(R.string.searching));
		mDialog.show();
		mDialog.setCancelable(true);
		mDialog.setOnCancelListener(mOnCancelListener);
	}

	@Override
	protected void onProgressUpdate(String... values)
	{

		// mDialog.getSerachprocessTextView.setText(values);
	}

	private List<DetailCategoryFileinfo> reInitList(List<DetailCategoryFileinfo> list)
	{
		if (list == null || list.size() == 0) {
			return null;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		for (DetailCategoryFileinfo info : list) {
			if (new File(info.getPath()).isFile()) {
				info.setStroage(FileUtil.FormetFileSize(info.getSize()));
				info.setDate(dateFormat.format(new Date(new File(info.getPath()).lastModified())));
			}
			else {
				info.setNumber(FileUtil.GetAfterDirectoryFileCounts(info.getPath()));
				info.setDrectory(true);
				info.setDate(dateFormat.format(new Date(new File(info.getPath()).lastModified())));
			}
		}
		return list;
	}

	private void setEmptyDetailFilecView(int type)
	{
		ImageView imageView = (ImageView) mFileNoRecodeLayout.findViewById(R.id.no_file_iocn);
		TextView textView = (TextView) mFileNoRecodeLayout.findViewById(R.id.no_file_text);
		if (detailCategoryFileinfosList == null || detailCategoryFileinfosList.size() == 0) {
			fileListView.setVisibility(View.GONE);
			mFileNoRecodeLayout.setVisibility(View.VISIBLE);
		}
		switch (type)
		{
		case 4:
			imageView.setImageResource(R.drawable.no_theme);
			textView.setText(R.string.no_theme);
			break;
		case 5:
			imageView.setImageResource(R.drawable.no_apk);
			textView.setText(R.string.no_apk);
			break;
		case 6:
			imageView.setImageResource(R.drawable.no_compressed);
			textView.setText(R.string.no_compress);
			break;
		case 7:
			imageView.setImageResource(R.drawable.no_files);
			textView.setText(R.string.no_documents);
			break;
		case 8:
			imageView.setImageResource(R.drawable.no_collection);
			textView.setText(R.string.no_collection);
			break;
		default:
			break;
		}
	}

	private void setTile(int type)
	{
		switch (type)
		{
		case 4:
			tvTitle.setText(R.string.theme);
			break;
		case 5:
			tvTitle.setText(R.string.apk);
			break;
		case 6:
			tvTitle.setText(R.string.compress_bag);
			break;
		case 7:
			tvTitle.setText(R.string.document);
			break;
		case 8:
			tvTitle.setText(R.string.collection);
			break;
		default:
			break;
		}
	}

	private List<DetailCategoryFileinfo> deleteCollectFile()
	{
		if (isDelete) {
			String mPath = mFileinfo.getmPath();
			if (!mFileinfo.isDrectory()) {
				FileUtil.deleteFile(mPath);

			}
			else {
				FileUtil.deleteDirectory(mPath, true);
			}
			mDao.delete(mFileinfo.getmPath());
		}
		return AllFileUtil.getAllCollectFiles(mContext);
	}

	private List<DetailCategoryFileinfo> reflashCollectFile()
	{
		if (isReName || mFileinfo != null) {
			mDao.update(mFileinfo.getmPath(), mFileinfo.getmName());
		}
		return AllFileUtil.getAllCollectFiles(mContext);
	}

}
