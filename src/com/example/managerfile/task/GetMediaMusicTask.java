package com.example.managerfile.task;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.example.managerfile.R;
import com.example.managerfile.adapter.MusicListAdapter;
import com.example.managerfile.model.Fileinfo;
import com.example.managerfile.model.Musicinfo;
import com.example.managerfile.ui.ShowSearchResaultActivity;
import com.example.managerfile.ui.base.FileProgressDialog;
import com.example.managerfile.ui.base.FileSearchProgressDialog;
import com.example.managerfile.util.AllFileUtil;
import com.example.managerfile.util.FileUtil;
import com.example.managerfile.util.OpenFile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class GetMediaMusicTask extends AsyncTask<Void, String, List<Musicinfo>>
{

	private MusicListAdapter listAdapter;
	private ListView fileListView;
	private int type;
	private Context mContext;
	private ArrayList<String> mList = new ArrayList<String>();
	private ProgressDialog mDialog;
	private DialogInterface.OnCancelListener mOnCancelListener = null;
	private TextView mTextView;
	private List<Musicinfo> musicinfoslList;
	private LinearLayout mFileNoRecodeLayout;

	public GetMediaMusicTask(MusicListAdapter adapter, ListView listView, List<Musicinfo> list, LinearLayout layout, int type, Context context)
	{

		mContext = context;
		this.type = type;
		fileListView = listView;
		musicinfoslList = list;
		listAdapter = adapter;
		mFileNoRecodeLayout = layout;
		mOnCancelListener = new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface paramDialogInterface)
			{
				cancel(true);
			}
		};
	}

	@Override
	protected List<Musicinfo> doInBackground(Void... arg0)
	{
		// TODO Auto-generated method stub
		if (type == 1) {
			return reInitList(AllFileUtil.GetMusicList(mContext));
		}
		return null;
	}

	@Override
	protected void onPostExecute(List<Musicinfo> list)
	{
		if (mDialog != null) {
			mDialog.cancel();
		}
		if (list.size() > 0 && list != null) {
			listAdapter = new MusicListAdapter(mContext, list, mFileNoRecodeLayout);
			fileListView.setAdapter(listAdapter);

			musicinfoslList = list;
			fileListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
				{
					// TODO Auto-generated method stub
					OpenFile.openFile(new File(musicinfoslList.get(position).getPath()), mContext);
				}
			});
		}
		else {
			setEmptyMusicView();
			return;
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

	private List<Musicinfo> reInitList(List<Musicinfo> list)
	{
		if (list == null || list.size() == 0) {
			return null;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		for (Musicinfo info : list) {
			info.setStroage(FileUtil.FormetFileSize(info.getSize()));
			info.setDate(dateFormat.format(new Date(new File(info.getPath()).lastModified())));
		}
		return list;
	}

	private void setEmptyMusicView()
	{
		ImageView imageView = (ImageView) mFileNoRecodeLayout.findViewById(R.id.no_file_iocn);
		TextView textView = (TextView) mFileNoRecodeLayout.findViewById(R.id.no_file_text);
		if (musicinfoslList == null || musicinfoslList.size() == 0) {
			mFileNoRecodeLayout.setVisibility(View.VISIBLE);
			imageView.setImageResource(R.drawable.no_audio);
			textView.setText(R.string.no_music);
		}
	}
}
