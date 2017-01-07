package com.example.managerfile.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.example.managerfile.R;
import com.example.managerfile.adapter.ImageListAdapter;
import com.example.managerfile.adapter.ListAdapter;
import com.example.managerfile.adapter.MusicListAdapter;
import com.example.managerfile.adapter.NormalFileListAdapter;
import com.example.managerfile.adapter.VideoListAdapter;
import com.example.managerfile.model.Check;
import com.example.managerfile.model.CollectFile;
import com.example.managerfile.model.DetailCategoryFileinfo;
import com.example.managerfile.model.Imageinfo;
import com.example.managerfile.model.Musicinfo;
import com.example.managerfile.model.Videoinfo;
import com.example.managerfile.provider.common.CollectFileDao;
import com.example.managerfile.task.GetFileListTask;
import com.example.managerfile.task.GetMediaImageTask;
import com.example.managerfile.task.GetMediaMusicTask;
import com.example.managerfile.task.GetMediaVideoTask;
import com.example.managerfile.task.RefleshCollectionFileTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ShowCategoryFilesActivity extends Activity
{
	private int FileType = 0;
	public ListAdapter mListAdapter;
	private TextView tvTitle;
	private TextView mTextView;
	private LinearLayout btnBack;
	private Context mContext;
	private LinearLayout mFileNoRecodeLayout;
	private ListView mListView;
	private List<Musicinfo> musicinfoslList;
	private List<Videoinfo> videoinfoslList;
	private List<Imageinfo> imageinfosList;
	private List<DetailCategoryFileinfo> detailCategoryFileinfosList;
	private MusicListAdapter mMusicListAdapter;
	private VideoListAdapter mVideoListAdapter;
	private ImageListAdapter mImageListAdapter;
	private FrameLayout mSearchBar;
	private CollectFileDao collectFileDao;
	private Stack<Check> mChecklist;
	private SharedPreferences settingPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_category_files);
		initView();
	}

	private void initView()
	{
		mContext = this;
		mListView = (ListView) findViewById(R.id.files_list);
		mFileNoRecodeLayout = (LinearLayout) findViewById(R.id.empty_view);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		mTextView = (TextView) findViewById(R.id.btnBack_text);
		mTextView.setText(R.string.back);
		btnBack = (LinearLayout) findViewById(R.id.btnBack);
		mSearchBar = (FrameLayout) findViewById(R.id.search_bar);
		if (mSearchBar.getVisibility() == View.VISIBLE) {
			mSearchBar.setVisibility(View.GONE);
		}
		collectFileDao = new CollectFileDao(mContext);
		mChecklist = new Stack<Check>();
		mChecklist.push(new Check("over", 10000));
		settingPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				finish();
			}
		});
		Intent intent = getIntent();
		FileType = intent.getIntExtra("type", 0);
		if (FileType > 0) {
			switch (FileType)
			{
			case 1:
				new GetMediaMusicTask(mMusicListAdapter, mListView, musicinfoslList, mFileNoRecodeLayout, FileType, mContext).execute();
				tvTitle.setText(R.string.music);
				break;
			case 2:
				new GetMediaVideoTask(mVideoListAdapter, mListView, videoinfoslList, mFileNoRecodeLayout, FileType, mContext).execute();
				tvTitle.setText(R.string.video);
				break;
			case 3:
				new GetMediaImageTask(mImageListAdapter, mListView, imageinfosList, mFileNoRecodeLayout, FileType, mContext).execute();
				tvTitle.setText(R.string.picture);
				break;
			default:
				new GetFileListTask(mListView, detailCategoryFileinfosList, mFileNoRecodeLayout, tvTitle, FileType, mSearchBar, mChecklist, mContext, settingPreferences).execute();
				break;
			}
		}
	}

	private void setEmptyVideoView()
	{
		if (videoinfoslList == null || videoinfoslList.size() == 0) {
			mFileNoRecodeLayout.setVisibility(View.VISIBLE);
		}
	}

	private void setEmptyImageView()
	{
		if (imageinfosList == null || imageinfosList.size() == 0) {
			mFileNoRecodeLayout.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onBackPressed()
	{
		// super.onBackPressed();
		if (FileType == 8) {
			Check check = null;
			if (mChecklist != null) {
				check = mChecklist.pop();
			}
			if (check == null) {

				finish();
			}
			else {
				String path = check.getFilePath();
				if (mChecklist.size() == 1) {
					new GetFileListTask(mListView, detailCategoryFileinfosList, mFileNoRecodeLayout, tvTitle, FileType, mSearchBar, mChecklist, mContext,settingPreferences).execute();
				}
				else if (!path.equals("over")) {
					String parentPathString = null;
					try {
						parentPathString = new File(path).getParentFile().getCanonicalPath();
					}
					catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					new RefleshCollectionFileTask(mContext, mSearchBar, mListView, parentPathString, check, mFileNoRecodeLayout, settingPreferences,FileType).execute();
				}
				else {
					finish();
				}
			}
		}
		else {
			finish();
		}
	}
}
